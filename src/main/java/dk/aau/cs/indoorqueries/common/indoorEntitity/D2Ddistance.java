package dk.aau.cs.indoorqueries.common.indoorEntitity;

/**
 * <h1>D2Ddistance</h1>
 * D2Ddistance
 * Indoor distance between two given doors
 *
 * @author feng zijin
 * @version 1.0
 * @since 2019-01-02
 */

public class D2Ddistance implements Comparable<Object> {
    private int doorID_1;  // the id of door 1
    private int doorID_2;  // the id of door 2
    private double distance;      // indoor distance between door

    /**
     * Constructor
     *
     * @param doorID_1
     * @param doorID_2
     * @param distance
     */
    public D2Ddistance(int doorID_1, int doorID_2, double distance) {
        super();
        this.doorID_1 = doorID_1;
        this.doorID_2 = doorID_2;
        this.distance = distance;
    }

    /**
     * this method is used to get the id of door 1
     *
     * @return the doorID_1
     */
    public int getDoorID_1() {
        return doorID_1;
    }

    /**
     * this method is used to set the id of door 1
     *
     * @param doorID_1 the doorID_1 to set
     */
    public void setDoorID_1(int doorID_1) {
        this.doorID_1 = doorID_1;
    }

    /**
     * this method is used to get the id of door 2
     *
     * @return the doorID_2
     */
    public int getDoorID_2() {
        return doorID_2;
    }

    /**
     * this method is used to set the id of door 2
     *
     * @param doorID_2 the doorID_2 to set
     */
    public void setDoorID_2(int doorID_2) {
        this.doorID_2 = doorID_2;
    }

    /**
     * this method is to get the distance between two door
     *
     * @return the distance
     */
    public double getDistance() {
        return distance;
    }

    /**
     * to set the distance between two door
     *
     * @param distance the distance to set
     */
    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String toString() {
        String tempString = this.doorID_1 + "#" + this.doorID_2 + " = " + this.distance;
        return tempString;
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Object arg0) {
        // TODO Auto-generated method stub
        D2Ddistance another = (D2Ddistance) arg0;

        if (this.distance < another.distance) {
            return -1;
        } else if (this.distance == another.distance) {
            if (this.doorID_2 < another.doorID_2) {
                return -1;
            } else if (this.doorID_2 == another.doorID_2) {
                return 0;
            } else {
                return 1;
            }
        } else {
            return 1;
        }
    }

}
