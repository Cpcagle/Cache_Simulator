//import java.io.FileInputStream;
//import java.io.IOException;
//import java.util.Scanner;
//
///**
// * Created by psgardner on 4/21/2017.
// */
//public class CacheSimulator {
//
//
//
//    public CacheSimulator() {
//        System.exit(1);
//    }
//
//
//    public static void main(String[] args) {
//        int numSets = 0;
//        int setSize = 0;
//        int lineSize = 0;
//        try {
//            System.setIn(new FileInputStream(args[0]));
//            Scanner scanIn = new Scanner(System.in);
//            numSets = getAfterColon();
//            setSize = getAfterColon();
//            lineSize = getAfterColon();
//            System.out.println(numSets);
//            System.out.println(setSize);
//            System.out.println(lineSize);
//
//        } catch (IOException e) {
//            System.out.println(e.getMessage() + "Filename does not exist, or" +
//                    " you do not have read permissions." + USAGE);
//        }
//        CacheSimulator sim = null;
//        if (errorCheck(numSets, setSize, lineSize)) {
//            sim = new CacheSimulator(numSets, setSize, lineSize);
//        }
//    }
//
//
//}
