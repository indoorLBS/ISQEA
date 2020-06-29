package dk.aau.cs.indoorqueries.common.iDMatrix;


/**
 * generate index matrix
 *
 * @author Tiantian Liu
 */


import dk.aau.cs.indoorqueries.common.indoorEntitity.IndoorSpace;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class IndexMatrixGen {
    public static ArrayList<ArrayList<ArrayList<Double>>> indexList = new ArrayList<>();
    public static HashMap<String, Double> d2dDistMap = new HashMap<String, Double>();

    /**
     * generate index matrix
     * @throws IOException
     */

    public void indexMatrixGen() throws IOException {
        calD2dDistMap();
        for (int i = 0; i < IndoorSpace.iDoors.size(); i++) {
            ArrayList<ArrayList<Double>> doorList = new ArrayList<>();
            for (int j = 0; j < IndoorSpace.iDoors.size(); j++) {
                int d1Id = i;
                int d2Id = j;
                String key = d1Id + "-" + d2Id;
                double dist = d2dDistMap.get(key);
                addToIndexList(doorList, d2Id, dist);
            }
            indexList.add(doorList);
        }
        System.out.println("indexList is finished");

    }

    /**
     * add a door with distance to index list
     * @param doorList
     * @param doorId
     * @param dist
     */

    public void addToIndexList(ArrayList<ArrayList<Double>> doorList, int doorId, double dist) {
        if (doorList.size() == 0) {
            doorList.add(new ArrayList<Double>(Arrays.asList((double) doorId, dist)));
            return;
        }
        for (int i = doorList.size() - 1; i >= 0; i--) {
            double tempDist = doorList.get(i).get(1);
            if (i == doorList.size() - 1 && dist >= tempDist) {
                doorList.add(new ArrayList<Double>(Arrays.asList((double) doorId, dist)));
                return;
            }
            if (i < doorList.size() - 1 && dist >= tempDist) {
                ArrayList<Double> lastItem = new ArrayList<>();
                lastItem = doorList.get(doorList.size() - 1);
                doorList.add(lastItem);
                for (int j = doorList.size() - 2; j > i + 1; j--) {
                    doorList.set(j, doorList.get(j - 1));
                }
                doorList.set(i + 1, new ArrayList<Double>(Arrays.asList((double) doorId, dist)));
                return;

            }
        }
        ArrayList<Double> lastItem = new ArrayList<>();
        lastItem = doorList.get(doorList.size() - 1);
        doorList.add(lastItem);
        for (int j = doorList.size() - 2; j > 0; j--) {
            doorList.set(j, doorList.get(j - 1));
        }
        doorList.set(0, new ArrayList<Double>(Arrays.asList((double) doorId, dist)));
    }

    public ArrayList<ArrayList<ArrayList<Double>>> getIndexList() {
        return indexList;
    }

    public void calD2dDistMap() throws IOException {
        DistMatrixGen distMatrixGen = new DistMatrixGen();
        distMatrixGen.readDist();
        d2dDistMap = distMatrixGen.getD2dDistMap();

    }

    public HashMap<String, Double> getD2dDistMap() {
        return this.d2dDistMap;
    }

    public static void main(String args[]) {
        IndexMatrixGen indexMatrixGen = new IndexMatrixGen();
        ArrayList<ArrayList<Double>> doorList = new ArrayList<>();
        indexMatrixGen.addToIndexList(doorList, 1, 45.0);
        System.out.println(doorList);
        indexMatrixGen.addToIndexList(doorList, 2, 35.0);
        System.out.println(doorList);
        indexMatrixGen.addToIndexList(doorList, 3, 78.0);
        System.out.println(doorList);
        indexMatrixGen.addToIndexList(doorList, 4, 57.0);
        System.out.println(doorList);
        indexMatrixGen.addToIndexList(doorList, 5, 12.0);
        System.out.println(doorList);
    }
}
