/**
 * A class that simulates a block in a memory cache.
 *
 * @author Cameron Cagle and Peter Gardner
 * @version April 28, 2017
 */
public class Block {
    /** the tag of the address */
    private int tag;
    /** the valid bit */
    private int valid;
    /** the dirty bit */
    private int dirty;
    /** the used cache */
    private long used;

    /**
     * initiates a block
     */
    public Block() {
        tag = 0;
        valid = 0;
        dirty = 0;
        used = 0;
    }
    /** gets used */
    public long getUsed() {
        return used;
    }
    /** sets used */
    public void setUsed(long used) {
        this.used = used;
    }
    /** get tag */
    public int getTag() {
        return tag;
    }
    /** set tag */
    public void setTag(int tag) {
        this.tag = tag;
    }
    /** get valid bit */
    public int getValid() {
        return valid;
    }
    /** set valid bit */
    public void setValid(int valid) {
        this.valid = valid;
    }
    /** get dirty bit */
    public int getDirty() {
        return dirty;
    }
    /** set dirty bit */
    public void setDirty(int dirty) {
        this.dirty = dirty;
    }
}
