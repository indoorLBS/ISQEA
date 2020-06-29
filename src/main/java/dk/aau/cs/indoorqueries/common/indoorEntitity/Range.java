/**
 *
 */
package dk.aau.cs.indoorqueries.common.indoorEntitity;


/**
 * <h>Range</h>
 * an interface Range
 *
 * @author feng zijin
 *
 */
public interface Range {
    /** get minimum distance between the Range and one Point */
    public abstract double getMinDist(Point point);

    /** get maximum distance between the Range and one Point */
    public abstract double getMaxDist(Point point);
}
