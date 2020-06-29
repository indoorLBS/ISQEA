package dk.aau.cs.indoorqueries.common.iPTree;

public class Dist implements Comparable<Dist> {
    public int mID;
    public double distance;
    public double time1;
    public double time2;
    public int prevID = -1;

    // for non leaf node
    public String path;

    public Dist(int mID) {
        this.mID = mID;
    }

    public Dist(int mID, double distance, double t1, double t2) {
        this.mID = mID;
        this.distance = distance;
        this.time1 = t1;
        this.time2 = t2;
    }

    public Dist(int mID, double distance, double t1, double t2, String path) {
        this.mID = mID;
        this.distance = distance;
        this.time1 = t1;
        this.time2 = t2;
        this.path = path;
    }

    public Dist(int mID, double distance, double t2, String path) {
        this.mID = mID;
        this.distance = distance;
        this.time2 = t2;
        this.path = path;
    }

    public Dist clone_1() {
        return new Dist(this.mID, this.distance, this.time1, this.time2);
    }

    public Dist clone_2() {
        return new Dist(this.mID, this.distance, this.time1, this.time2, this.path);
    }

    @Override
    public int compareTo(Dist another) {
        if (this.distance > another.distance) return 1;
        else if (this.distance == another.distance) return 0;
        else return -1;
    }

    public String toString() {
        return "d" + this.mID + " distance: " + this.distance + " t1: " + this.time1 + " t2: " + this.time2 + " prev: " + this.prevID + " path: " + this.path;
    }


}
