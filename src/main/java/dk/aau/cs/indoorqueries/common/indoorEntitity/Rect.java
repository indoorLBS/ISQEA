/**
 *
 */
package dk.aau.cs.indoorqueries.common.indoorEntitity;

import dk.aau.cs.indoorqueries.common.utilities.Constant;

/**
 * <h>Rect</h>
 * the shape rectangle as MBRs or Partitions
 *
 * @author feng zijin
 *
 */
public class Rect implements Range, Comparable<Object> {
    private double x1;            // minimum in the x-axis
    private double x2;            // maximum in the x-axis

    private double y1;            // minimum in the y-axis
    private double y2;            // minimum in the y-axis

    private double centerX;        // weight center in the x-axis
    private double centerY;        // weight center in the y-axis

    private int mRectID;    // the ID
    private int mFloor;        // the belonging floor

    /**
     * Constructor
     *
     * @param x1
     * @param x2
     * @param y1
     * @param y2
     * @param mRectID
     * @param mFloor
     */
    public Rect(double x1, double x2, double y1, double y2, int mRectID,
                int mFloor) {
        super();
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.mRectID = mRectID;
        this.mFloor = mFloor;
    }

    /**
     * Constructor
     *
     * @param x1
     * @param x2
     * @param y1
     * @param y2
     * @param mFloor
     *
     */
    public Rect(double x1, double x2, double y1, double y2, int mFloor) {
        super();
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.mRectID = 0;
        this.mFloor = mFloor;
    }

    /**
     * Constructor
     *
     * @param x1
     * @param x2
     * @param y1
     * @param y2
     *
     */
    public Rect(double x1, double x2, double y1, double y2) {
        super();
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.mRectID = 0;
        this.mFloor = 0;
    }

    /**
     * dk.aau.cs.indoorqueries.test if point is contained in
     *
     * @param point
     * @return True as contained
     */
    public boolean contain(Point point) {

        if (x1 <= point.getX() && x2 >= point.getX() && y1 <= point.getY()
                && y2 >= point.getY() && mFloor == point.getmFloor())
            return true;
        return false;
    }

    /**
     * @return the x1
     */
    public double getX1() {
        return x1;
    }

    /**
     * @param x1
     *            the x1 to set
     */
    public void setX1(double x1) {
        this.x1 = x1;
    }

    /**
     * @return the x2
     */
    public double getX2() {
        return x2;
    }

    /**
     * @param x2
     *            the x2 to set
     */
    public void setX2(double x2) {
        this.x2 = x2;
    }

    /**
     * @return the y1
     */
    public double getY1() {
        return y1;
    }

    /**
     * @param y1
     *            the y1 to set
     */
    public void setY1(double y1) {
        this.y1 = y1;
    }

    /**
     * @return the y2
     */
    public double getY2() {
        return y2;
    }

    /**
     * @param y2
     *            the y2 to set
     */
    public void setY2(double y2) {
        this.y2 = y2;
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
     * @return the mRectID
     */
    public int getmRectID() {
        return mRectID;
    }

    /**
     * @param mRectID
     *            the mRectID to set
     */
    public void setmRectID(int mRectID) {
        this.mRectID = mRectID;
    }

    /**
     * @return the centerX
     */
    public double getcenterX() {
        return centerX;
    }

    /**
     * @return the centerY
     */
    public double getcenterY() {
        return centerY;
    }

    /**
     * set the weight center of the rectangle
     */
    public void setCenter() {
        this.centerX = (this.x1 + this.x2) / 2;
        this.centerY = (this.y1 + this.y2) / 2;
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.IndoorObject)
     */
    @Override
    public int compareTo(Object arg0) {
        // TODO Auto-generated method stub
        Rect otherRect = (Rect) arg0;
        return this.mRectID > otherRect.mRectID ? 1
                : (this.mRectID == otherRect.mRectID ? 0 : -1);

    }

    /* (non-Javadoc)
     * @see dk.aau.cs.indoorqueries.indoor_entitity.Range#getMinDist(dk.aau.cs.indoorqueries.indoor_entitity.Point)
     */
    @Override
    public double getMinDist(Point point) {
        // TODO Auto-generated method stub

        double x0 = point.getX();
        double y0 = point.getY();

        if (this.contain(new Point(x0, y0, this.mFloor))) {
            return 0;
        } else if (x0 < this.x1) {
            if (y0 < this.y1) {
                return Math.sqrt((x0 - this.x1) * (x0 - this.x1) + (y0 - this.y1) * (y0 - this.y1));
            } else if (y0 > this.y2) {
                return Math.sqrt((x0 - this.x1) * (x0 - this.x1) + (y0 - this.y2) * (y0 - this.y2));
            } else {
                return this.x1 - x0;
            }
        } else if (x0 > this.x2) {
            if (y0 < this.y1) {
                return Math.sqrt((x0 - this.x2) * (x0 - this.x2) + (y0 - this.y1) * (y0 - this.y1));
            } else if (y0 > this.y2) {
                return Math.sqrt((x0 - this.x2) * (x0 - this.x2) + (y0 - this.y2) * (y0 - this.y2));
            } else {
                return x0 - this.x2;
            }
        } else {
            if (y0 < this.y1) {
                return this.y1 - y0;
            } else if (y0 > this.y2) {
                return y0 - this.y2;
            } else
                return 0;
        }

    }

    /* (non-Javadoc)
     * @see dk.aau.cs.indoorqueries.indoor_entitity.Range#getMaxDist(dk.aau.cs.indoorqueries.indoor_entitity.Point)
     */
    @Override
    public double getMaxDist(Point point) {
        Point pt1 = new Point(x1, y1);
        Point pt2 = new Point(x1, y2);
        Point pt3 = new Point(x2, y1);
        Point pt4 = new Point(x2, y2);

        Point p = new Point(point.getX(), point.getY());

        double[] dist = new double[4];
        dist[0] = p.eDist(pt1);
        dist[1] = p.eDist(pt2);
        dist[2] = p.eDist(pt3);
        dist[3] = p.eDist(pt4);

        double maxdist = 0.0;
        for (int i = 0; i < 4; i++) {
            if (maxdist < dist[i] + Constant.small)
                maxdist = dist[i];
        }
        return maxdist;
    }

    /**
     * dk.aau.cs.indoorqueries.test if the door is belonging to this partition
     *
     * @param another
     * @return True shows that it is
     */
    public boolean testDoor(Door another) {
        if (Math.abs(another.getX() - this.getX1()) < Constant.small
                || Math.abs(another.getX() - this.getX2()) < Constant.small) {
            if ((another.getY() >= this.getY1())
                    && (another.getY() <= this.getY2())) {
                // addDoor(pt);
                return true;
            }
        } else if (Math.abs(another.getY() - this.getY1()) < Constant.small
                || Math.abs(another.getY() - this.getY2()) < Constant.small) {
            if ((another.getX() >= this.getX1())
                    && (another.getX() <= this.getX2())) {
                // addDoor(pt);
                return true;
            }
        }
        return false;
    }

}
