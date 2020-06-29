/**
 *
 */
package dk.aau.cs.indoorqueries.common.indoorEntitity;

import dk.aau.cs.indoorqueries.common.utilities.Constant;

/**
 * <h1>Point</h1>
 * to describe a object's position on one of the floors
 *
 * @author feng zijin
 *
 */
public class Point {
    private double x;    // x coordinate
    private double y;    // y coordinate
    private int mFloor;    // belonging floor
    private int mType;

    /**
     * Constructor
     *
     * @param x
     * @param y
     * @param mFloor
     */
    public Point(double x, double y, int mFloor) {
        super();
        this.x = x;
        this.y = y;
        this.mFloor = mFloor;
    }

    /**
     * Constructor
     *
     * @param x
     * @param y
     * @param mFloor
     */
    public Point(double x, double y, int mFloor, int mType) {
        super();
        this.x = x;
        this.y = y;
        this.mFloor = mFloor;
        this.mType = mType;
    }

    /**
     * Constructor
     *
     * @param x
     * @param y
     */
    public Point(double x, double y) {
        super();
        this.x = x;
        this.y = y;
    }

    /**
     * Constructor
     *
     * @param point
     */
    public Point(Point point) {
        super();
        this.x = point.x;
        this.y = point.y;
        this.mFloor = point.mFloor;
    }

    /**
     * dk.aau.cs.indoorqueries.test if it is equal to another Point
     *
     * @param another
     * @return True shows that it is equal to another Point
     */
    public boolean isEqual(Point another) {
        // check whether they are the same class object
        if (this.getClass() != another.getClass()) return false;

        if ((Math.abs(x - another.x) < Constant.small)
                && (Math.abs(y - another.y) < Constant.small))
            return true;
        else
            return false;
    }

    /**
     * Euclidean Distance between two Point
     *
     * @param another
     * @return eDist
     */
    public double eDist(Point another) {
        return Math.sqrt(Math.pow(x - another.x, 2)
                + Math.pow(y - another.y, 2));
    }

    /**
     * @return the x
     */
    public double getX() {
        return x;
    }

    /**
     * @param x
     *            the x to set
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public double getY() {
        return y;
    }

    /**
     * @param y
     *            the y to set
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * @return the mType
     */
    public int getmType() {
        return mType;
    }

    /**
     * @param mType
     *            the mType to set
     */
    public void setmType(int mType) {
        this.mType = mType;
    }

    /**
     * @return the mFloor
     */
    public int getmFloor() {
        return mFloor;
    }

    /**
     * @param mFloor
     *            the mFloor to set
     */
    public void setmFloor(int mFloor) {
        this.mFloor = mFloor;
    }

    /**
     * reflection of a point
     *
     * @param axis decide the x-axis or y-axis
     * @param pivot
     */
    public void reflection(int axis, int pivot) {
        int x0, y0;
        if (0 == axis) {
            x0 = pivot;
            x = x0 + (x0 - x);
        }
        if (1 == axis) {
            y0 = pivot;
            y = y0 + (y0 - y);
        }
    }

    public String toString() {
        return "x = " + x + " y = " + y + " z = " + mFloor;
    }
}
