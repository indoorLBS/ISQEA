/**
 *
 */
package dk.aau.cs.indoorqueries.common.indoorEntitity;


/**
 * @author feng zijin
 *
 */
public class Connectivity implements Comparable<Object> {
    private int mDoorID;                // the through door to touch one partition

    private int mParID;                    // the direct-touchable partition

    /**
     * Constructor
     *
     * @param mDoorID
     * @param mParID
     */
    public Connectivity(int mDoorID, int mParID) {
        super();
        this.mDoorID = mDoorID;
        this.mParID = mParID;
    }

    /**
     * @return the mDoorID
     */
    public int getmDoorID() {
        return mDoorID;
    }

    /**
     * @param mDoorID the mDoorID to set
     */
    public void setmDoorID(int mDoorID) {
        this.mDoorID = mDoorID;
    }

    /**
     * @return the mParID
     */
    public int getmParID() {
        return mParID;
    }

    /**
     * @param mParID the mParID to set
     */
    public void setmParID(int mParID) {
        this.mParID = mParID;
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Object o) {
        // TODO Auto-generated method stub
        Connectivity other = (Connectivity) o;
        if (this.mDoorID > other.mDoorID)
            return 1;
        else if (this.mDoorID == other.getmDoorID()) {
            return this.mParID > other.mParID ? 1 : (this.mParID == other.mParID ? 0 : -1);
        } else {
            return -1;
        }
    }

}
