/**
 *
 */
package dk.aau.cs.indoorqueries.cph.datagenerate;

/**
 * <h>CPHDataGenConstant</h>
 * Constant Values in Data Generating
 * @author feng zijin, Tiantian Liu
 *
 */
public class CPHDataGenConstant {

    // PARAMETERS FOR INDOOR SPACES
    public static String dataset = "CPH";
    /** dimensions of the floor */
    public static double floorRangeX = 2000;
    public static double floorRangeY = 2200;

    public static double zoomLevel = 0.6;

    /**
     * type of dataset
     */
    public static int dataType = 1; // 1 means regular dataset; 0 means less doors; 2 means more doors;

    /**
     * type of division
     * */
    public static int divisionType = 1; // 1 means regular division; 0 means no division for hallway;

    /** numbers of the floor */
    public static int nFloor = 1;

    /** length of stairway between two floors */
    public static double lenStairway = 20.0;

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

    public static int K = 4;    // number of doors to classify crucial and simple partition

    public static int totalNodeSize = 0;    // current total node size in the tree, used to generate nodeID

    //number of leafnodes
    public static int leafNodeSize = 0;

    // multiplier, enlarge the size of the space by multiplier times
    public static int multiplier = 10;

    // traveling speed 83.34m/min
    public static double traveling_speed = 83.34;

    // distance cube generation. true - generate distance between all doors
    public static boolean generateAll = true;


}
