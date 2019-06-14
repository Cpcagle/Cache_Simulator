/**
 * A class that simulates a set-associative memory cache based on passed
 * in parameters.
 *
 * @author Cameron Cagle and Peter Gardner
 * @version April 28, 2017
 */
import java.util.Scanner;

public class CacheSim {
    /** number of sets in the cache */
    private int numSets;
    /** size of the set in the cache */
    private int setSize;
    /** size of the line in the cache */
    private int lineSize;
    /** Log base 2 of the number of sets */
    private int numSetsPower;
    /** Log base 2 of the size of the line */
    private int lineSizePower;
    /** miss in the cache      */
    private int miss;
    /** hit in the cache */
    private int hits;

    /** prints an error message */
    public static final String USAGE =
            "\nUsage: java CacheSimulator <input_file>\n";
    /** first three lines of input */
    public static final int STARTLINE = 3;
    /** initiates a blank 2D array */
    private Block[][] cache;

    /**
     * This method scans in the file to be read and data seperated into each
     * specific field.
     * @param scan what is scanned into the file.
     */
    public CacheSim(Scanner scan) {
        int num[] = new int[3];
        for (int i = 0; i < STARTLINE; i++){
            String line = scan.nextLine().trim();
            num[i] = Integer.parseInt(line.split(": ")[1]);
        }
        boolean err = errorCheck(num);
        if(!err) {
            System.exit(1);
        } else {
            this.numSets = num[0];
            this.setSize = num[1];
            this.lineSize = num[2];
            this.lineSizePower = (int)Math.ceil(Math.log(lineSize)/Math.log(2));
            this.numSetsPower = (int)Math.ceil(Math.log(numSets)/Math.log(2));
            this.cache = new Block[numSets][setSize];
            for(int i = 0; i < cache.length; i++) {
                for(int j = 0; j < cache[i].length; j++) {
                    cache[i][j] = new Block();
                }
            }
        }
    }


    /**
     * if the access is a read then it will read information
     * into the cache if it is valid and will determine if there
     * is a hit or a miss. Replaces LRU if tag doesn't match.
     * @param data information getting read into the cache.
     * @return true if the data is found in the cache.
     */
    public boolean read(int[] data){
        int[] toi = tagOffsetIndex(data);
        int tag = toi[0];
        int offset = toi[1];
        int index = toi[2];

        boolean found = false;

        for(int i = 0; i < setSize && !found; i++) {
            if(cache[index][i].getValid() == 1) {
                if (cache[index][i].getTag() == tag) {
                    found = true;
                }
            }
        }
        if(!found) {
            Block replace = leastRecentlyUsed(index);

            replace.setUsed(System.currentTimeMillis());
            replace.setTag(tag);
            replace.setValid(1);
        }

        return found;
    }

    /**
     * writes data to the cache and determines if there is data within the
     * cache. Replaces LRU if tag doesn't match but valid bit is 1.
     * @param data information getting wrote into the cache.
     * @return true if data is found within the cache.
     */
    public boolean write(int[] data) {
        int[] toi = tagOffsetIndex(data);
        int tag = toi[0];
        int offset = toi[1];
        int index = toi[2];
        boolean found = false;

        for(int i = 0; i < setSize && !found; i++) {
            if(cache[index][i].getValid() == 1) {
                if (cache[index][i].getTag() == tag) {
                    found = true;
                } else {

                }
            }
        }
        //TODO
        if(!found) {
            Block replace = leastRecentlyUsed(index);

            replace.setUsed(System.currentTimeMillis());
            replace.setTag(tag);
            replace.setValid(1);
        }


        return found;
    }

    /**
     * This is the main function and prints out all the information needed,
     * and separates all the data to determine whether or not it was a
     * hit or a miss in the cache and how many memory references there
     * were.
     */
    public static void main (String[] args) {
        printStats();
        Scanner scan = new Scanner(System.in);
        CacheSim simulator = new CacheSim(scan);
        while(scan.hasNextLine()) {
            String[] data = simulator.parseLine(scan.nextLine());
            String instruct = data[1];
            int[] address = {Integer.parseInt(data[0], 16),
                    Integer.parseInt(data[2])};
            String access = null;
            String hitMiss = null;
            int memRefs = 0;
            if (instruct.equals("R")) {
                access = "read";
                if(simulator.read(address)) {
                    simulator.hits++;
                    hitMiss = "hit";
                } else {
                    simulator.miss++;
                    hitMiss = "miss";
                    memRefs = 1;
                }
            } else {
                access = "write";
                if(simulator.write(address)) {
                    simulator.hits++;
                    hitMiss = "hit";
                } else {
                    simulator.miss++;
                    hitMiss = "miss";
                    memRefs = 2;
                }

            }
            System.out.printf("%6s %8s %7s %5s %6s %6s %7s\n", access,
                    data[0], simulator.tagOffsetIndex(address)[0],
                    simulator.tagOffsetIndex(address)[2],
                    simulator.tagOffsetIndex(address)[1], hitMiss, memRefs);

        }
        System.out.println("Simulation Summary Statistics");
        System.out.println("-----------------------------");
        System.out.printf("%-16s %1s %-10d\n", "Total hits", ":",
                simulator.hits);
        System.out.printf("%-16s %1s %-10d\n", "Total misses", ":",
                simulator.miss);
        System.out.printf("%-16s %1s %-10d\n", "Total accesses", ":",
                simulator.hits+simulator.miss);
        System.out.printf("%-16s %1s %-10f\n", "Hit ratio", ":",
                simulator.hits/((double)simulator.hits+simulator.miss));
        System.out.printf("%-16s %1s %-10f\n", "Miss ratio", ":",
                simulator.miss/((double)simulator.hits+simulator.miss));
        System.exit(0);
    }

    /**
     * this function is an error check for the entire program, checks for
     * power of 2 in a set, if the number of sets are too large, if the
     * line size is to small, and if the line size is a power of 2.
     * @param params
     * @return result of whether or not there was an error.
     */
    private static boolean errorCheck(int[] params) {
        boolean result = true;
        if(!isPowerOfTwo(params[0])) {
            System.out.println("Number of Sets must be a power of 2");
            result = false;
        }
        if(8192 < params[0]) {
            System.out.println("Number of Sets too large. Must not exceed " +
                    "8192");
            result = false;
        }
        if(8 < params[1]) {
            System.out.println("Associativity Level too high. Must not " +
                    "exceed 8");
            result = false;
        }
        if(3 > params[2]) {
            System.out.println("Line size too small. Must be at least 4");
            result = false;
        }
        if(!isPowerOfTwo(params[2])) {
            System.out.println("Number of Lines must be a power of 2");
            result = false;
        }
        if(!result) {
            System.out.println(USAGE);
        }
        return result;
    }

    /**
     * informs you if the power of 2 of the number passed in.
     * @param num number to be put to Log base 2.
     * @return power of 2 of the number.
     */
    private static boolean isPowerOfTwo(int num) {
        double i = Math.ceil(Math.log(num)/Math.log(2));
        return (Math.pow(2, i) == num);
    }

    /**
     * prints out all the data in a nice and neat format.
     */
    private static void printStats() {
        System.out.printf("%6s %8s %7s %5s %6s %6s %7s\n", "Access",
                "Address", "Tag  ", "Index", "Offset", "Result", "Memrefs");
        System.out.printf("%6s %8s %7s %5s %6s %6s %7s\n", "------",
                "--------", "-------", "-----", "------", "------", "-------");
    }

    /**
     * checks which set was the least recently used sets it to least.
     * @param index which set is being checked.
     * @return the result of what is least recently used.
     */
    private Block leastRecentlyUsed(int index) {
        double least = Double.MAX_VALUE;
        Block result = null;
        for(int i = 0; i < cache[index].length; i++) {
            if(cache[index][i].getUsed() < least) {
                result = cache[index][i];
                least = cache[index][i].getUsed();
            }
        }
        return result;
    }

    /**
     * parse's the line into 3 separate sections of data.
     * @param line the line of data to be parsed.
     * @return split: the data that has been split.
     */
    private String[] parseLine(String line) {
        String[] split = line.split(":");
        for(int i = 0; i < STARTLINE; i++) {
            int colon = split[i].indexOf(':');
            if(colon != -1) {
                split[i] = split[i].substring(0, colon);
            }
        }
        return split;
    }

    /**
     * gathers all the information regarding the tag, offset, and index.
     * @param data the information passed in to be split up and separated.
     * @return returns an array of the tag, offset, and index.
     */
    private int[] tagOffsetIndex(int[] data) {
        int tag;
        int offset;
        int index;

        String binary = Integer.toBinaryString(data[0]);
        while(binary.length() != 32) {
            binary = "0" + binary;
        }
        int findIndex = binary.length() - lineSizePower;
        offset = Integer.parseInt(binary.substring(findIndex,
                binary.length()), 2);
        findIndex -= numSetsPower;
        index = Integer.parseInt(binary.substring(findIndex,
                findIndex + numSetsPower), 2);
        tag = 0;
        if(binary.substring(0, findIndex).length() != 0) {
            tag = Integer.parseInt(binary.substring(0, findIndex), 2);
        }
        return new int[]{tag, offset, index};
        //TODO does this even work
    }
}
