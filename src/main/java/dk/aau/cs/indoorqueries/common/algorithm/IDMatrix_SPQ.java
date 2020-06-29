package dk.aau.cs.indoorqueries.common.algorithm;

import dk.aau.cs.indoorqueries.common.indoorEntitity.Door;
import dk.aau.cs.indoorqueries.common.indoorEntitity.IndoorSpace;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Partition;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Point;
import dk.aau.cs.indoorqueries.common.utilities.Constant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * shortest path (distance) query using IDMatrix
 *
 * @author Tiantian Liu
 */

public class IDMatrix_SPQ {
    /**
     * calculate shortest distance between two points, and return the distance with the path.
     *
     * @param ps
     * @param pt
     * @return
     * @throws IOException
     */
    public int p2pDistance(Point ps, Point pt, HashMap<String, Double> d2dDistMap, HashMap<String, String> d2dRouteMap) throws IOException {
        int visitDoors = 0;
        int sPartitionId = getHostPartition(ps);
        int tPartitionId = getHostPartition(pt);
        Partition sPar = IndoorSpace.iPartitions.get(sPartitionId);
        Partition tPar = IndoorSpace.iPartitions.get(tPartitionId);

        ArrayList<Integer> leaveDoors = sPar.getTopology().getP2DLeave();
        ArrayList<Integer> enterDoors = tPar.getTopology().getP2DEnter();

        double minDist = Constant.large;
        String minKey = "";

        for (int i = 0; i < leaveDoors.size(); i++) {
            int leaveDoorId = leaveDoors.get(i);
            visitDoors++;
            Door leaveDoor = IndoorSpace.iDoors.get(leaveDoorId);
            double dist1 = distPointDoor(ps, leaveDoor);
            for (int j = 0; j < enterDoors.size(); j++) {
                visitDoors++;
                int enterDoorId = enterDoors.get(j);
                Door enterDoor = IndoorSpace.iDoors.get(enterDoorId);
                double dist2 = distPointDoor(pt, enterDoor);
                String key = leaveDoorId + "-" + enterDoorId;
                double dist3 = d2dDistMap.get(key);
                double dist = dist1 + dist2 + dist3;
                if (dist < minDist) {
                    minDist = dist;
                    minKey = key;
                }

            }
        }

        String result = minDist + "\t" + d2dRouteMap.get(minKey);
        System.out.println(result);
        return visitDoors;
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
                return partitionId;
            }
        }
        return partitionId;
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
