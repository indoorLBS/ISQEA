/**
 *
 */
package dk.aau.cs.indoorqueries.common.iPTree;

/**
 * @author feng zijin
 *
 */
public class TimeInterval implements Comparable<TimeInterval> {
    public String path;
    public double distance;
    public double time1;
    public double time2;

    public TimeInterval(String path, double distance, double time1, double time2) {
        this.path = path;
        this.distance = distance;
        this.time1 = time1;
        this.time2 = time2;
    }

    public TimeInterval(String path, double distance, double time1) {
        this.path = path;
        this.distance = distance;
        this.time1 = time1;
    }

    public boolean isAvailable(double time) {
        if (this.time1 <= time && time < this.time2) return true;
        else return false;
    }

    public boolean equals(TimeInterval another) {
        if (path.equals(another.path)) {
            if (distance == another.distance) {
                return true;
            } else {
                System.out.println("something wrong_TimeInterval_equals");
                return false;
            }
        } else {
            return false;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(TimeInterval another) {
        if (this.time1 > another.time1)
            return 1;
        else if (this.time1 == another.time1) {
            if (this.time2 > another.time2)
                return 1;
            else if (this.time2 == another.time2)
                return 0;
            else
                return -1;
        } else {
            return -1;
        }
    }

    public String toString() {
        return "" + this.path + ";" + this.distance + ";" + this.time1 + ";" + this.time2 + "";
    }

}
