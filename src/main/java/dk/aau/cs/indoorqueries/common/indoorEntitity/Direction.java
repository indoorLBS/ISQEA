/**
 *
 */
package dk.aau.cs.indoorqueries.common.indoorEntitity;

/**
 * <h>Direction</h>
 * represent the direction of the door
 *
 * @author feng zijin
 *
 */
public class Direction {
    private int sID;        // the source partition/floor id
    private int eID;        // the ending partition/floor id
    private int doorID;        // the door id
    private int doorID2 = -1;        // the door id

    /**
     * Constructor
     *
     * @param sID
     * @param eID
     * @param doorID
     */
    public Direction(int sID, int eID, int doorID) {
        this.sID = sID;
        this.eID = eID;
        this.doorID = doorID;
    }

    /**
     * Constructor
     *
     * @param sID
     * @param eID
     * @param doorID
     * @param doorID2
     */
    public Direction(int sID, int eID, int doorID, int doorID2) {
        this.sID = sID;        // start partition/floor id
        this.eID = eID;        // ending partition/floor id
        this.doorID = doorID;        // first door need to go through
        this.doorID2 = doorID2;        // second door need to go through
    }

    /**
     * @return the sID
     */
    public int getsID() {
        return sID;
    }

    /**
     * @param sID
     *            the sID to set
     */
    public void setsID(int sID) {
        this.sID = sID;
    }

    /**
     * @return the eID
     */
    public int geteID() {
        return eID;
    }

    /**
     * @param eID
     *            the eID to set
     */
    public void seteID(int eID) {
        this.eID = eID;
    }

    /**
     * @return the doorID
     */
    public int getdoorID() {
        return doorID;
    }

    /**
     * @param doorID
     *            the doorID to set
     */
    public void setdoorID(int doorID) {
        this.doorID = doorID;
    }

    /**
     * @return the doorID2
     */
    public int getdoorID2() {
        return doorID2;
    }

    /**
     * @param doorID2
     *            the doorID2 to set
     */
    public void setdoorID2(int doorID2) {
        this.doorID2 = doorID2;
    }

    /**
     * @return string
     */
    public String toString() {
        return "p" + sID + " - d" + +doorID + " - d" + doorID2 + " - p" + eID;
    }
}
