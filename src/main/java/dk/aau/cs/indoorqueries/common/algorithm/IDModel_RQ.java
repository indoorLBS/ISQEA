package dk.aau.cs.indoorqueries.common.algorithm;

import dk.aau.cs.indoorqueries.common.indoorEntitity.*;
import dk.aau.cs.indoorqueries.common.utilities.Constant;
import dk.aau.cs.indoorqueries.common.utilities.DataGenConstant;
import dk.aau.cs.indoorqueries.common.utilities.RoomType;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Algorithm of processing range query using IDModel
 *
 * @author Tiantian Liu
 */
public class IDModel_RQ {

    /**
     * process rq using IDModel
     * @param q
     * @param r
     * @return
     */
    public ArrayList<Integer> range(Point q, double r) {
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
            double dist1 = distPointDoor(q, sDoor);
            dist[sDoorId] = dist1;
        }

        // minHeap
        BinaryHeap<Double> H = new BinaryHeap<>(IndoorSpace.iDoors.size());
        for (int j = 0; j < IndoorSpace.iDoors.size(); j++) {
            if (!sDoors.contains(j)) {
                dist[j] = Constant.large;
            }

            H.insert(dist[j], j);
            prev.add(null);
        }

        while (H.heapSize > 0) {
            String minElement = H.delete_min();
            String[] minElementArr = minElement.split(",");
            int curDoorId = Integer.parseInt(minElementArr[1]);
            if (Double.parseDouble(minElementArr[0]) != dist[curDoorId]) {
                System.out.println("Something wrong with min heap: dk.aau.cs.indoorqueries.algorithm.IDModel_SPQ.pt2ptDistance3");
            }
            Door curDoor = IndoorSpace.iDoors.get(curDoorId);

            if (dist[curDoorId] >= r) break;
            if (sDoors.contains(curDoorId)) {
                prev.set(curDoorId, new ArrayList<>(Arrays.asList(qPartitionId, -1)));
            }

            int prevParId = prev.get(curDoorId).get(0);
            int prevDoorId = prev.get(curDoorId).get(1);

            isDoorVisited[curDoorId] = 1;
            ArrayList<Integer> nextPars = curDoor.getD2PEnter();
            for (int i = 0; i < nextPars.size(); i++) {
                int nextParId = nextPars.get(i);
                if (nextParId == prevParId) continue;
                Partition nextPar = IndoorSpace.iPartitions.get(nextParId);
                ArrayList<Integer> nextParObjects = nextPar.getmObjects();
                ArrayList<Integer> newR = rangeSearch(nextParObjects, curDoor, r - dist[curDoorId], R);
                R.addAll(newR);

                ArrayList<Integer> leaveDoors = nextPar.getTopology().getP2DLeave();
                for (int k = 0; k < leaveDoors.size(); k++) {
                    int leaveDoorId = leaveDoors.get(k);
                    if (isDoorVisited[leaveDoorId] != 1) {

                        if (dist[curDoorId] + nextPar.getdistMatrix().getDistance(curDoorId, leaveDoorId) < dist[leaveDoorId]) {
                            double oldDist = dist[leaveDoorId];
                            dist[leaveDoorId] = dist[curDoorId] + nextPar.getdistMatrix().getDistance(curDoorId, leaveDoorId);
                            prev.set(leaveDoorId, new ArrayList<>(Arrays.asList(nextParId, leaveDoorId)));
                            H.updateNode(oldDist, leaveDoorId, dist[leaveDoorId], leaveDoorId);
                        }

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
