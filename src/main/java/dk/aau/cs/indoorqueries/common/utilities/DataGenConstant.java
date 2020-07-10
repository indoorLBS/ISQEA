package dk.aau.cs.indoorqueries.common.utilities;

import dk.aau.cs.indoorqueries.cph.datagenerate.CPHDataGenConstant;
import dk.aau.cs.indoorqueries.hsm.datagenerate.HSMDataGenConstant;
import dk.aau.cs.indoorqueries.mzb.datagenerate.MZBDataGenConstant;

/**
 * <h>HSMDataGenConstant</h>
 * Constant Values in Data Generating
 *
 * @author feng zijin, Tiantian Liu
 */

public class DataGenConstant {
    // PARAMETERS FOR INDOOR SPACES
    public static String dataset = "SYN";
    /**
     * dimensions of the floor
     */
    public static double floorRangeX = 1368;
    public static double floorRangeY = 1368;

    public static double zoomLevel = 0.6;

    /**
     * numbers of the floor
     */
    public static int nFloor = 5;

    /**
     * type of dataset
     */
    public static int dataType = 1; // 1 means regular dataset; 0 means less doors; 2 means more doors;

    /**
     * type of division
     */
    public static int divisionType = 1; // 1 means regular division; 0 means no division for hallway; 2 means more hallway;

    /**
     * length of stairway between two floors
     */
    public static double lenStairway = 20.0;

    // ID COUNTERS FOR INDOOR ENTITIES
    /**
     * the ID counter of Partitions
     */
    public static int mID_Par = 0;

    /**
     * the ID counter of Doors
     */
    public static int mID_Door = 0;

    /**
     * the ID counter of Floors
     */
    public static int mID_Floor = 0;

    /**
     * the ID counter of Objects
     */
    public static int mID_Object = 0;

    /**
     * the ID counter of Shops
     */
    public static int mID_Shop = 0;

    // KEYWORDS
    public static int mKeyworSize = 0;

    // TREES
    /**
     * maximum children of upper tree
     */
    public static int maxChildUpper = 6;

    /**
     * maximum children of lower tree
     */
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

    public static void init(String dataName) {
        if (dataName.equals("hsm")) {
            dataset = HSMDataGenConstant.dataset;
            floorRangeX = HSMDataGenConstant.floorRangeX;
            floorRangeY = HSMDataGenConstant.floorRangeY;
            zoomLevel = HSMDataGenConstant.zoomLevel;
            nFloor = HSMDataGenConstant.nFloor;
            lenStairway = HSMDataGenConstant.lenStairway;
            dataType = HSMDataGenConstant.dataType;
            divisionType = HSMDataGenConstant.divisionType; // 1 means regular division; 0 means no division for hallway; 2 means more hallway;

        } else if (dataName.equals("cph")) {
            dataset = CPHDataGenConstant.dataset;
            floorRangeX = CPHDataGenConstant.floorRangeX;
            floorRangeY = CPHDataGenConstant.floorRangeY;
            zoomLevel = CPHDataGenConstant.zoomLevel;
            nFloor = CPHDataGenConstant.nFloor;
            lenStairway = CPHDataGenConstant.lenStairway;
            dataType = CPHDataGenConstant.dataType;
            divisionType = CPHDataGenConstant.divisionType; // 1 means regular division; 0 means no division for hallway; 2 means more hallway;

        } else if (dataName.equals("mzb")) {
            dataset = MZBDataGenConstant.dataset;
            floorRangeX = MZBDataGenConstant.floorRangeX;
            floorRangeY = MZBDataGenConstant.floorRangeY;
            zoomLevel = MZBDataGenConstant.zoomLevel;
            nFloor = MZBDataGenConstant.nFloor;
            lenStairway = MZBDataGenConstant.lenStairway;
            dataType = MZBDataGenConstant.dataType;
            divisionType = MZBDataGenConstant.divisionType; // 1 means regular division; 0 means no division for hallway; 2 means more hallway;

        }

    }
}