/**
 *
 */
package dk.aau.cs.indoorqueries.mzb.datagenerate;

/**
 * <h>MZBDataGenConstant</h>
 * Constant Values in Data Generating
 * @author feng zijin
 *
 */
public class MZBDataGenConstant {

    // PARAMETERS FOR INDOOR SPACES
    /** dimensions of the floor */
    public static double floorRangeX = 138;

    public static double floorRangeY = 138;

    /** type of division */
    public static int divisionType = 1; // 1 means regular division; 0 means no division for hallway;

    public static int zoomLevel = 10;

    /** numbers of the floor */
    public static int nFloor = 17;

    /** length of stairway between two floors */
    public static double lenStairway = 2.0;

    // ID COUNTERS FOR INDOOR ENTITIES
    /** the ID counter of Partitions */
    public static int mID_Par = 0;

    /** the ID counter of Doors */
    public static int mID_Door = 0;

    /** the ID counter of Floors */
    public static int mID_Floor = 0;

    /** the ID counter of Objects */
    public static int mID_Object = 0;

    /** the ID counter of Shops */
    public static int mID_Shop = 0;

    // KEYWORDS
    public static int mKeyworSize = 0;

    // TREES
    /** maximum children of upper tree */
    public static int maxChildUpper = 6;

    /** maximum children of lower tree */
    public static int maxChildLower = 150;

    public static int curSizePar = -1;

    public static int curSizeDoor = -1;

    public static int K = 4;    // number of public door to classify crucial and simple partition

    public static int totalNodeSize = 0;    // current total node size in the tree, used to generate nodeID

    //number of leafnodes
    public static int leafNodeSize = 0;

    // multiplier, enlarge the size of the space by multiplier times
    public static int multiplier = 10;

    // traveling speed 83.34m/min
    public static double traveling_speed = 83.34;

    // distance cube generation. true - generate distance between all doors
    public static boolean generateAll = true;

    //0.2 0.4 0.6 0.8 1
    public static double variedDoorRate = 0.8;

    //4 8 12 16
    public static int sizeOfCheckPoint = 4;

    // 7 8 9 10 10.30 11 14 15 17 19 21 21.3 22 23
    public static double[][] checkPoints_4 = {{0, 24}, {7, 22}};
    public static double[][] checkPoints_8 = {{0, 24}, {7, 23}, {8, 22}, {9, 21.3}};
    public static double[][] checkPoints_12 = {{0, 24}, {7, 23}, {8, 22}, {9, 21.3}, {10, 21}, {10.3, 19}};
    public static double[][] checkPoints_16 = {{0, 24}, {7, 23}, {8, 22}, {9, 21.3}, {10, 21}, {10.3, 19}, {11, 17}, {14, 15}};
}
