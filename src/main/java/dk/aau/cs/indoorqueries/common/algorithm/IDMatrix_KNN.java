package dk.aau.cs.indoorqueries.common.algorithm;

import dk.aau.cs.indoorqueries.common.indoorEntitity.*;
import dk.aau.cs.indoorqueries.common.utilities.Constant;
import dk.aau.cs.indoorqueries.common.utilities.DataGenConstant;
import dk.aau.cs.indoorqueries.common.utilities.RoomType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Algorithm of processing knnq using IDMatrix
 * @author Tiantian Liu
 */
public class IDMatrix_KNN {
    public static ArrayList<ArrayList<Double>> kObjects = new ArrayList<>();

    /**
     * process knnq using IDMatrix
     * @param q
     * @param k
     * @param indexMatrix
     * @param d2dDistMap
     * @return
     * @throws IOException
     */
    public ArrayList<ArrayList<Double>> knnQuery(Point q, int k, ArrayList<ArrayList<ArrayList<Double>>> indexMatrix, HashMap<String, Double> d2dDistMap) throws IOException {

        kObjects = new ArrayList<>();
        int[] isParVisited = new int[IndoorSpace.iPartitions.size()];
        int[] isDoorVisited = new int[IndoorSpace.iDoors.size()];
        double[] dist = new double[IndoorSpace.iDoors.size()];
        ArrayList<ArrayList<Integer>> prev = new ArrayList<>();

        int qPartitionId = getHostPartition(q);
        Partition qPar = IndoorSpace.iPartitions.get(qPartitionId);
        ArrayList<Integer> qParObjects = qPar.getmObjects();

        calDist(qParObjects, q, k);
        isParVisited[qPartitionId] = 1;

        ArrayList<Integer> sDoors = new ArrayList<>();

        sDoors = qPar.getTopology().getP2DLeave();

        for (int i = 0; i < sDoors.size(); i++) {
            int sDoorId = sDoors.get(i);
            Door sDoor = IndoorSpace.iDoors.get(sDoorId);
            double dist1 = distPointDoor(q, sDoor);
            ArrayList<ArrayList<Double>> doorArr = indexMatrix.get(sDoorId);
            for (int j = 0; j < doorArr.size(); j++) {
                int nextDoorId = (int) (double) doorArr.get(j).get(0);
                Door nextDoor = IndoorSpace.iDoors.get(nextDoorId);
                double dist2 = d2dDistMap.get(sDoorId + "-" + nextDoorId);
                if (dist2 + dist1 > getKBound(k)) break;
                else {
                    ArrayList<Integer> pars = nextDoor.getD2PEnter();
                    for (int m = 0; m < pars.size(); m++) {
                        int parId = pars.get(m);
                        Partition par = IndoorSpace.iPartitions.get(parId);
                        ArrayList<Integer> objectsTemp = par.getmObjects();
                        calDist(objectsTemp, nextDoor, dist1 + dist2, k);
                    }
                }
            }
        }
        System.out.println(kObjects);
        return kObjects;
    }

    /**
     * calculate distances from a point to objects and maintain the k nearest objects
     * @param objects
     * @param q
     * @param k
     */
    public void calDist(ArrayList<Integer> objects, Point q, int k) {
        ArrayList<Integer> canObjects = new ArrayList<>(); // candidate objects
        for (int i = 0; i < objects.size(); i++) {
            int objectId = objects.get(i);
            IndoorObject ob = IndoorSpace.iObject.get(objectId);
            double dist = distPointObject(q, ob);
            if (dist < getKBound(k)) {
                addObToKlist(objectId, dist, k);
            }
        }
    }

    /**
     * calculate distances from a door to objects and maintain the k nearest objects
     * @param objects
     * @param d
     * @param k
     */
    public void calDist(ArrayList<Integer> objects, Door d, double curDist, int k) {
        ArrayList<Integer> canObjects = new ArrayList<>(); // candidate objects
        for (int i = 0; i < objects.size(); i++) {
            int objectId = objects.get(i);
            IndoorObject ob = IndoorSpace.iObject.get(objectId);
            double dist = distPointObject(d, ob) + curDist;
            if (dist < getKBound(k)) {
                addObToKlist(objectId, dist, k);
            }
        }
    }


    /**
     * add an object to K object list
     *
     * @param objectId
     * @param dist
     * @param k
     */
    public void addObToKlist(int objectId, double dist, int k) {
        if (kObjects.size() == 0) {
            kObjects.add(new ArrayList<>(Arrays.asList((double) objectId, dist)));
            return;
        }

        if (kObjects.size() < k) {
            for (int i = kObjects.size() - 1; i >= 0; i--) {
                double tempDist = kObjects.get(i).get(1);
                double tempObjectId = (int) ((double) kObjects.get(i).get(0));
                if (objectId == tempObjectId) return;
                if (dist < tempDist) {
                    if (i + 1 == kObjects.size()) {
                        kObjects.add(new ArrayList<>(Arrays.asList((double) objectId, dist)));
                        break;
                    } else {
                        ArrayList<Double> lastItem = kObjects.get(kObjects.size() - 1);
                        kObjects.add(lastItem);
                        for (int j = kObjects.size() - 2; j > i + 1; j--) {
                            ArrayList<Double> tempItem = kObjects.get(j - 1);
                            kObjects.set(j, tempItem);
                        }
                        kObjects.set(i + 1, new ArrayList<>(Arrays.asList((double) objectId, dist)));
                        break;
                    }
                }
                if (i == 0) {
                    ArrayList<Double> lastItem = kObjects.get(kObjects.size() - 1);
                    kObjects.add(lastItem);
                    for (int j = kObjects.size() - 2; j > 0; j--) {
                        ArrayList<Double> tempItem = kObjects.get(j - 1);
                        kObjects.set(j, tempItem);
                    }
                    kObjects.set(0, new ArrayList<>(Arrays.asList((double) objectId, dist)));
                    break;
                }
            }
        }

        if (kObjects.size() == k) {
            for (int i = kObjects.size() - 1; i >= 0; i--) {
                double tempDist = kObjects.get(i).get(1);
                int tempObjectId = (int) ((double) kObjects.get(i).get(0));
                if (objectId == tempObjectId) return;
                if (dist <= tempDist) {
                    kObjects.set(i, new ArrayList<>(Arrays.asList((double) objectId, dist)));
                    objectId = tempObjectId;
                    dist = tempDist;
                }
            }
        }

        if (kObjects.size() > k) {
            System.out.println("something wrong with the kBound");
        }
    }

    /**
     * get kBound
     *
     * @param k
     * @return
     */
    public double getKBound(int k) {
        double kBound = Constant.large;
        if (kObjects.size() == k) {
            kBound = kObjects.get(0).get(1);
        }
        if (kObjects.size() > k) {
            System.out.println("something wrong with the kBound");
        }
        return kBound;
    }

    /**
     * get host partition of a point
     * @param point
     */
    public int getHostPartition(Point point) {
        int partitionId = -1;
        int floor = point.getmFloor();
        ArrayList<Integer> pars = IndoorSpace.iFloors.get(floor).getmPartitions();
        for (int i = 0; i < pars.size(); i++) {
            Partition par = IndoorSpace.iPartitions.get(pars.get(i));
            if (point.getX() >= par.getX1() && point.getX() <= par.getX2() && point.getY() >= par.getY1() && point.getY() <= par.getY2()) {
                partitionId = par.getmID();
                if (DataGenConstant.dataset.equals("MZB") && IndoorSpace.iPartitions.get(partitionId).getmType() == RoomType.HALLWAY) continue;
                return partitionId;
            }
        }
        return partitionId;
    }

    /**
     * calculate distance between a point and an Object
     * @param point
     * @param object
     */
    public double distPointObject(Point point, IndoorObject object) {
        double dist = 0;
        dist = Math.sqrt(Math.pow(point.getX() - object.getObjectX(), 2) + Math.pow(point.getY() - object.getObjectY(), 2));
        return dist;
    }

    /**
     * calculate distance between a point and a door
     */
    public double distPointDoor(Point point, Door door) {
        double dist = 0;
        dist = Math.sqrt(Math.pow(point.getX() - door.getX(), 2) + Math.pow(point.getY() - door.getY(), 2));
        return dist;
    }
}
