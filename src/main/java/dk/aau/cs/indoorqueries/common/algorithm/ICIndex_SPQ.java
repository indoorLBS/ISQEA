package dk.aau.cs.indoorqueries.common.algorithm;

import dk.aau.cs.indoorqueries.common.indoorEntitity.Door;
import dk.aau.cs.indoorqueries.common.indoorEntitity.IndoorSpace;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Partition;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Point;
import dk.aau.cs.indoorqueries.common.utilities.Constant;

import java.util.ArrayList;
import java.util.Arrays;

public class ICIndex_SPQ {
    public String pt2ptDistance3(Point ps, Point pt) {
        int visitDoors = 0;
        int doorSize = IndoorSpace.iDoors.size();
        double[][] dists = new double[doorSize][doorSize];
        int sPartitionId = getHostPartition(ps);
        int tPartitionId = getHostPartition(pt);
        if (sPartitionId == tPartitionId) {
            System.out.println(distPoint2Point(ps, pt));
            double minDist = distPoint2Point(ps, pt);
            return minDist + "\t" + visitDoors;
        }
        Partition sPar = IndoorSpace.iPartitions.get(sPartitionId);
        Partition tPar = IndoorSpace.iPartitions.get(tPartitionId);
        ArrayList<Integer> sDoors = sPar.getTopology().getP2DEnter();
        ArrayList<Integer> tDoors = tPar.getTopology().getP2DLeave();

        for (int i = 0; i < sDoors.size(); i++) {
            visitDoors++;
            int tempDoorId = sDoors.get(i);
            Door tempDoor = IndoorSpace.iDoors.get(tempDoorId);
            ArrayList<Integer> tempPars = tempDoor.getD2PLeave();
            int tempParId = -1;
            if (tempPars.size() > 2)
                System.out.println("something wrong with D2PLeave size: dk.aau.cs.indoorqueries.algorithm.IDModel_SPQ.pt2ptDistance3");
            for (int j = 0; j < tempPars.size(); j++) {
                if (tempPars.get(j) != sPartitionId) {
                    tempParId = tempPars.get(j);
                    break;
                }
            }
            if (tempParId != -1) {
                ArrayList<Integer> tempLeaveDoors = IndoorSpace.iPartitions.get(tempParId).getTopology().getP2DLeave();
                if (tempLeaveDoors.equals(new ArrayList<>(Arrays.asList(tempDoorId))) && tempParId != tPartitionId) {
                    sDoors.remove(i);
                    i--;
                }
            }

            for (int j = 0; j < tDoors.size(); j++) {
                int tempDoorId2 = tDoors.get(j);
                dists[tempDoorId][tempDoorId2] = Constant.large;
            }

        }

        double minDist = Constant.large;
        String minPath = "";

        for (int i = 0; i < sDoors.size(); i++) {
            ArrayList<Integer> Doors = new ArrayList<>();
            double[] dist = new double[IndoorSpace.iDoors.size()];
            ArrayList<ArrayList<Integer>> prev = new ArrayList<>();
            int sDoorId = sDoors.get(i);
            Door sDoor = IndoorSpace.iDoors.get(sDoorId);
            for (int j = 0; j < tDoors.size(); j++) {
                int tDoorId = tDoors.get(j);
                Door tDoor = IndoorSpace.iDoors.get(tDoorId);
                if (dists[sDoorId][tDoorId] >= Constant.large && distPointDoor(ps, sDoor) + distPointDoor(pt, tDoor) < minDist) {
                    Doors.add(tDoorId);
                }
            }

            // minHeap
            BinaryHeap<Double> H = new BinaryHeap<>(IndoorSpace.iDoors.size());
            for (int j = 0; j < IndoorSpace.iDoors.size(); j++) {
                if (j != sDoorId) {
                    dist[j] = Constant.large;
                    prev.add(null);
                } else {
                    dist[j] = 0;
                    prev.add(new ArrayList<>(Arrays.asList(sPartitionId, -1)));
                }
                H.insert(dist[j], j);

            }

            int[] isVisited = new int[IndoorSpace.iDoors.size()];
            int n = 0;
            while (H.heapSize > 0) {
//                System.out.println("while num: " + n++);
                visitDoors++;

                String minElement = H.delete_min();
                String[] minElementArr = minElement.split(",");
                int curDoorId = Integer.parseInt(minElementArr[1]);
                if (Double.parseDouble(minElementArr[0]) != dist[curDoorId]) {
                    System.out.println("Something wrong with min heap: dk.aau.cs.indoorqueries.algorithm.IDModel_SPQ.pt2ptDistance3");
                }
//                System.out.println("curDoorId: " + curDoorId);
                if (Double.parseDouble(minElementArr[0]) >= Constant.large) {
                    break;
                }
                Door curDoor = IndoorSpace.iDoors.get(curDoorId);


                if (Doors.contains(curDoorId)) {
                    Doors.remove(Doors.indexOf(curDoorId));
                    if (minDist > distPointDoor(ps, sDoor) + dist[curDoorId] + distPointDoor(pt, curDoor)) {
                        minDist = distPointDoor(ps, sDoor) + dist[curDoorId] + distPointDoor(pt, curDoor);
                        if (prev.get(curDoorId).get(0) == sPartitionId) {
                            minPath = curDoorId + "\t";
                            continue;
                        } else {
                            minPath = getPath(prev, sDoorId, curDoorId);
                        }

                    }
                    int prevParId = prev.get(curDoorId).get(0);
                    int prevDoorId = prev.get(curDoorId).get(1);

                    Door prevDoor = IndoorSpace.iDoors.get(prevDoorId);

                    Partition prevPar = IndoorSpace.iPartitions.get(prevParId);

                    while (prevDoorId != sDoorId) {
                        if (sDoors.contains(prevDoorId) && prevDoorId > sDoorId) {
                            dists[prevDoorId][curDoorId] = dist[curDoorId] - dist[prevDoorId];
                            if (minDist > distPointDoor(ps, prevDoor) + dists[prevDoorId][curDoorId] + distPointDoor(pt, curDoor)) {
                                minDist = distPointDoor(ps, prevDoor) + dists[prevDoorId][curDoorId] + distPointDoor(pt, curDoor);
                                minPath = getPath(prev, sDoorId, curDoorId);
                            }
                        }
                        prevParId = prev.get(prevDoorId).get(0);
                        prevDoorId = prev.get(prevDoorId).get(1);

                        prevDoor = IndoorSpace.iDoors.get(prevDoorId);

                        prevPar = IndoorSpace.iPartitions.get(prevParId);
                    }

                    if (Doors.size() == 0) break;
                } else if (sDoors.contains(curDoorId) && curDoorId < sDoorId) {
                    for (int j = 0; j < Doors.size(); j++) {
                        int tempDoorId = Doors.get(j);
                        Door tempDoor = IndoorSpace.iDoors.get(tempDoorId);
                        dists[sDoorId][tempDoorId] = dist[curDoorId] + dists[curDoorId][tempDoorId];
                        if (minDist > distPointDoor(ps, sDoor) + dists[sDoorId][tempDoorId] + distPointDoor(pt, tempDoor)) {
                            minDist = distPointDoor(ps, sDoor) + dists[sDoorId][tempDoorId] + distPointDoor(pt, tempDoor);
                            minPath = getPath(prev, sDoorId, tempDoorId);
                        }
                    }
                    break;
                }

                isVisited[curDoorId] = 1;

                ArrayList<Integer> parts = curDoor.getD2PLeave();
//                System.out.println("parts: " + parts);
                for (int j = 0; j < parts.size(); j++) {
                    int nextParId = parts.get(j);
                    Partition nextPar = IndoorSpace.iPartitions.get(nextParId);
                    ArrayList<Integer> leaveDoors = nextPar.getTopology().getP2DLeave();
//                    System.out.println("leaveDoor: " + leaveDoors);
                    for (int k = 0; k < leaveDoors.size(); k++) {
                        visitDoors++;
                        int leaveDoorId = leaveDoors.get(k);
//                        System.out.println("leaveDoorId: " + leaveDoorId);
                        if (isVisited[leaveDoorId] != 1) {
//                            System.out.println("first if");
                            if (dist[curDoorId] + nextPar.getdistMatrix().getDistance(curDoorId, leaveDoorId) < dist[leaveDoorId]) {
//                                System.out.println("second if");
                                double oldDist = dist[leaveDoorId];
                                dist[leaveDoorId] = dist[curDoorId] + nextPar.getdistMatrix().getDistance(curDoorId, leaveDoorId);
//                                System.out.println("dist of leaveDoorId: " + dist[leaveDoorId]);
                                prev.set(leaveDoorId, new ArrayList<>(Arrays.asList(nextParId, curDoorId)));
                                H.updateNode(oldDist, leaveDoorId, dist[leaveDoorId], leaveDoorId);
//                                System.out.println("prev: " + prev.get(leaveDoorId));
                            }

                        }
                    }
                }


            }
        }
        System.out.println("minDist: " + minDist + "  minPath: " + minPath);
        return minDist + "\t" + visitDoors;

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

    public double distPoint2Point(Point p1, Point p2) {
        double dist = 0;
        dist = Math.sqrt(Math.pow(p1.getX() - p2.getX(), 2) + Math.pow(p1.getY() - p2.getY(), 2));
        return dist;
    }

    /**
     * @param prev
     * @param ds
     * @param de
     * @return a string path
     */
    public static String getPath(ArrayList<ArrayList<Integer>> prev, int ds, int de) {
        String result = de + "";

//		System.out.println("ds = " + ds + " de = " + de + " " + prev[de].par + " " + prev[de].door);
        int currp = prev.get(de).get(0);
        int currd = prev.get(de).get(1);

        while (currd != ds) {
            result = currd + "\t" + result;
//			System.out.println("current: " + currp + ", " + currd + " next: " + prev[currd].toString());
            currp = prev.get(currd).get(0);
            currd = prev.get(currd).get(1);

        }

        result = currd + "\t" + result;

        return result;
    }
}
