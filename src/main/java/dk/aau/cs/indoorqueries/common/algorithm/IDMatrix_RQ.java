package dk.aau.cs.indoorqueries.common.algorithm;

import dk.aau.cs.indoorqueries.common.indoorEntitity.*;
import dk.aau.cs.indoorqueries.common.utilities.DataGenConstant;
import dk.aau.cs.indoorqueries.common.utilities.RoomType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Algorithm of processing rq using IDMatrix
 * @author Tiantian Liu
 */
public class IDMatrix_RQ {
    /**
     * process rq using IDMatrix
     * @param q
     * @param r
     * @param indexMatrix
     * @param d2dDistMap
     * @return
     * @throws IOException
     */
    public ArrayList<Integer> rangeQuery(Point q, double r, ArrayList<ArrayList<ArrayList<Double>>> indexMatrix, HashMap<String, Double> d2dDistMap) throws IOException {
        ArrayList<Integer> R = new ArrayList<>();
        int[] isParVisited = new int[IndoorSpace.iPartitions.size()];
        int[] isDoorVisited = new int[IndoorSpace.iDoors.size()];
        double[] dist = new double[IndoorSpace.iDoors.size()];
        ArrayList<ArrayList<Integer>> prev = new ArrayList<>();

        int qPartitionId = getHostPartition(q);
        Partition qPar = IndoorSpace.iPartitions.get(qPartitionId);
        ArrayList<Integer> qParObjects = qPar.getmObjects();

        R = rangeSearch(qParObjects, q, r);
        isParVisited[qPartitionId] = 1;

        ArrayList<Integer> sDoors = new ArrayList<>();

        sDoors = qPar.getTopology().getP2DLeave();

        for (int i = 0; i < sDoors.size(); i++) {
            int sDoorId = sDoors.get(i);
            Door sDoor = IndoorSpace.iDoors.get(sDoorId);
            double r1 = r - distPointDoor(q, sDoor);
            ArrayList<ArrayList<Double>> doorArr = indexMatrix.get(sDoorId);
            for (int j = 0; j < doorArr.size(); j++) {
                int nextDoorId = (int) (double) doorArr.get(j).get(0);
                Door nextDoor = IndoorSpace.iDoors.get(nextDoorId);
                double distTemp = d2dDistMap.get(sDoorId + "-" + nextDoorId);
                if (distTemp > r1) break;
                else {
                    double r2 = r1 - distTemp;
                    ArrayList<Integer> pars = nextDoor.getD2PEnter();
                    for (int m = 0; m < pars.size(); m++) {
                        int parId = pars.get(m);
                        Partition par = IndoorSpace.iPartitions.get(parId);
                        ArrayList<Integer> objectsTemp = par.getmObjects();
                        R.addAll(rangeSearch(objectsTemp, nextDoor, r2, R));
                    }
                }
            }
        }
        System.out.println(R);
        return R;
    }

    /**
     * get objects within r from a point
     * @param q
     * @param r
     */
    public ArrayList<Integer> rangeSearch(ArrayList<Integer> objects, Point q, double r) {
        ArrayList<Integer> canObjects = new ArrayList<>(); // candidate objects
        for (int i = 0; i < objects.size(); i++) {
            int objectId = objects.get(i);
            IndoorObject ob = IndoorSpace.iObject.get(objectId);
            double dist = distPointObject(q, ob);
            if (dist <= r) {
                canObjects.add(objectId);
            }
        }
        return canObjects;
    }

    /**
     * get objects within r from a door
     * @param objects
     * @param r
     * @param R
     */
    public ArrayList<Integer> rangeSearch(ArrayList<Integer> objects, Door d, double r, ArrayList<Integer> R) {
        ArrayList<Integer> canObjects = new ArrayList<>(); // candidate objects
        for (int i = 0; i < objects.size(); i++) {
            int objectId = objects.get(i);
            IndoorObject ob = IndoorSpace.iObject.get(objectId);
            double dist = distPointObject(d, ob);
            if (dist <= r && !R.contains(objectId)) {
                canObjects.add(objectId);
            }
        }
        return canObjects;
    }

    /**
     * get host partition of a point
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
