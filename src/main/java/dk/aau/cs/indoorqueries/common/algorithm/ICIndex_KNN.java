package dk.aau.cs.indoorqueries.common.algorithm;

import com.github.davidmoten.rtree3d.*;
import com.github.davidmoten.rtree3d.geometry.Box;
import dk.aau.cs.indoorqueries.common.indoorEntitity.*;
import dk.aau.cs.indoorqueries.common.utilities.Constant;
import dk.aau.cs.indoorqueries.common.utilities.DataGenConstant;
import dk.aau.cs.indoorqueries.common.utilities.RoomType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.apache.commons.lang3.math.NumberUtils.min;

/**
 * algorithm of processing knnq using ICIndex
 * @author Tiantian Liu
 */
public class ICIndex_KNN {
    public static ArrayList<Integer> R_objects = new ArrayList<>();
    public static ArrayList<Integer> R_partitions = new ArrayList<>();
    public static ArrayList<ArrayList<Double>> kObjects = new ArrayList<>();

    /**
     * process knnq using ICIndex
     * @param q
     * @param k
     * @param T
     * @return
     */
    public ArrayList<ArrayList<Double>> iKNN(Point q, int k, RTree<Integer, Box> T) {
        kObjects = new ArrayList<>();
//        R_partitions = new ArrayList<>();
//        R_objects = new ArrayList<>();
//        kSeedsSelection(q, k);
//
////        System.out.println("R_objects: " + R_objects);
////        System.out.println("R_partitions:" + R_partitions);
//
//        for (int i = 0; i < R_objects.size(); i++) {
//            int objectId = R_objects.get(i);
////            System.out.println(i + " objects: " + objectId);
////            System.out.println("q.x: " + q.getX() + " q.y: " + q.getY() + " q.mFloor: " + q.getmFloor());
//            IndoorObject ob = IndoorSpace.iObject.get(objectId);
//            double dist = pt2objectDist(q, ob);
//            if (dist <= getKBound(k)) {
//                addObToKlist(objectId, dist, k);
//            }
//        }
        knn(q, k, T);
        System.out.println(kObjects);
        return kObjects;
    }

    /**
     * select k seeds
     * @param q
     * @param k
     * @param T
     */
    public void kSeedsSelection(Point q, int k, RTree<Integer, Box> T) {
        BinaryHeap<Double> H = new BinaryHeap<>(IndoorSpace.iDoors.size());
        int sPartitionId = getHostPartition(q);
        int num = 0;
        H.insert((double) num, sPartitionId);
        Partition sPar = IndoorSpace.iPartitions.get(sPartitionId);
        ArrayList<Integer> sObjects = sPar.getmObjects();
        R_objects.addAll(sObjects);
        R_partitions.add(sPartitionId);
        while (H.heapSize > 0 && R_objects.size() < k) {
            num++;
            String minElement = H.delete_min();
            String[] minElementArr = minElement.split(",");
            int curParId = Integer.parseInt(minElementArr[1]);
            Partition par = IndoorSpace.iPartitions.get(curParId);
            ArrayList<Integer> nextPars = par.getTopology().getP2PLeave();
            R_partitions.addAll(nextPars);
            for (int i = 0; i < nextPars.size(); i++) {
                int nextParId = nextPars.get(i);
                H.insert((double) num, nextParId);
                Partition nextPar = IndoorSpace.iPartitions.get(nextParId);
                ArrayList<Integer> objects = nextPar.getmObjects();
                for (int j = 0; j < objects.size(); j++) {
                    int tempObjectId = objects.get(j);
                    if (!R_objects.contains(tempObjectId)) {
                        R_objects.addAll(objects);
                    }
                }
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
     * get objects within r from a point
     * @param q
     * @param r
     * @param T
     */
    public void rangeSearch(Point q, double r, RTree<Integer, Box> T) {
        Queue<Node<Integer, Box>> Q = new Queue<>();
        Q.enqueue(T.root().get());
        System.out.println("root is in queue");
        while (Q.size() != 0) {
            Node<Integer, Box> node = Q.dequeue();
            if (node instanceof Leaf) {
                double minKN = minKDistPoint2Node(q, node);

                if (minKN <= r) {
                    List<Entry<Integer, Box>> entries = ((Leaf<Integer, Box>) node).entries();
                    ArrayList<Integer> pars = new ArrayList<>();
                    for (int i = 0; i < entries.size(); i++) {
                        pars.add(entries.get(i).value());
                    }
                    R_partitions.addAll(pars);

                    for (int i = 0; i < pars.size(); i++) {
                        Partition par = IndoorSpace.iPartitions.get(pars.get(i));
                        ArrayList<Integer> objects = par.getmObjects();
                        for (int j = 0; j < objects.size(); j++) {
                            int obId = objects.get(j);
                            IndoorObject ob = IndoorSpace.iObject.get(obId);
                            double minKO = minKDistPoint2Object(q, ob);

                            if (minKO <= r) {
                                R_objects.add(obId);
                            }
                        }
                    }
                }
            } else {
                for (Node<Integer, Box> child : ((NonLeaf<Integer, Box>) node).children()) {
                    double minKN = minKDistPoint2Node(q, node);

                    if (minKN <= r) Q.enqueue(child);
                }
            }
        }

    }


    /**
     * get min distance between a point and a node
     * @param q
     * @param node
     * @return
     */
    public double minKDistPoint2Node(Point q, Node<Integer, Box> node) {
        double minKN = 0;
        int qFloor = q.getmFloor();
        int nodeZ1 = (int) (node.geometry().mbb().z1() * 10);
        int nodeZ2 = (int) (node.geometry().mbb().z2() * 10);
        double nodeX1 = node.geometry().mbb().x1();
        double nodeX2 = node.geometry().mbb().x2();
        double nodeY1 = node.geometry().mbb().y1();
        double nodeY2 = node.geometry().mbb().y2();
        if (nodeZ2 > qFloor && qFloor >= nodeZ1) {
            minKN = minDistPoint2Rec(q, nodeX1, nodeX2, nodeY1, nodeY2);
        } else {
            List<Integer> stairs = IndoorSpace.iFloors.get(qFloor).getmDoors();
            int gap;
            for (int i = 0; i < stairs.size(); i++) {
                int door1Id = stairs.get(i);
                int door2Id;
                Door door1 = IndoorSpace.iDoors.get(door1Id);

                if (qFloor > nodeZ2) {
                    gap = qFloor - nodeZ2;
                    door2Id = door1Id - gap * IndoorSpace.iNumberDoorsPerFloor;
                } else {
                    gap = nodeZ1 - qFloor;
                    door2Id = door1Id + gap * IndoorSpace.iNumberDoorsPerFloor;
                }

                Door door2 = IndoorSpace.iDoors.get(door2Id);
                double dist1 = distPointDoor(q, door1);
                double dist2 = DataGenConstant.lenStairway * gap;
                double dist3 = minDistPoint2Rec(door2, nodeX1, nodeX2, nodeY1, nodeY2);
                if (dist1 + dist2 + dist3 < minKN) minKN = dist1 + dist2 + dist3;

            }
        }

        return minKN;
    }

    /**
     * get min distance between a point and an object
     * @param q
     * @param ob
     * @return
     */
    public double minKDistPoint2Object(Point q, IndoorObject ob) {
        double minKO = 0;
        int qFloor = q.getmFloor();
        int oFloor = ob.getoFloor();
        if (qFloor == oFloor) {
            minKO = Math.sqrt(Math.pow(ob.getObjectX() - q.getX(), 2) + Math.pow(ob.getObjectY() - q.getY(), 2));
        } else {
            int gap;
            List<Integer> stairs = IndoorSpace.iFloors.get(qFloor).getmDoors();
            for (int i = 0; i < stairs.size(); i++) {
                int door1Id = stairs.get(i);
                int door2Id;
                Door door1 = IndoorSpace.iDoors.get(door1Id);

                if (qFloor > oFloor) {
                    gap = qFloor - oFloor;
                    door2Id = door1Id - gap * IndoorSpace.iNumberDoorsPerFloor;
                } else {
                    gap = oFloor - qFloor;
                    door2Id = door1Id + gap * IndoorSpace.iNumberDoorsPerFloor;
                }

                Door door2 = IndoorSpace.iDoors.get(door2Id);
                double dist1 = distPointDoor(q, door1);
                double dist2 = DataGenConstant.lenStairway * gap;
                double dist3 = distPointObject(door2, ob);
                if (dist1 + dist2 + dist3 < minKO) minKO = dist1 + dist2 + dist3;
            }
        }
        return minKO;
    }

    /**
     * get min distance between a point and a rectangle
     * @param q
     * @param x1
     * @param x2
     * @param y1
     * @param y2
     * @return
     */
    public double minDistPoint2Rec(Point q, double x1, double x2, double y1, double y2) {
        double minDist = 0;
        double qX = q.getX();
        double qY = q.getY();

        if ((qX >= x1 && qX <= x2) || (qX >= x2 && qX <= x1)) {
            if ((qY >= y1 && qY <= y2) || (qY >= y2 && qY <= y1)) {
                minDist = 0;
            } else {
                double temp1 = Math.abs(qY - y1);
                double temp2 = Math.abs(qY - y2);
                minDist = min(temp1, temp2);
            }
        } else if ((qY >= y1 && qY <= y2) || (qY >= y2 && qY <= y1)) {
            double temp1 = Math.abs(qX - x1);
            double temp2 = Math.abs(qX - x2);
            minDist = min(temp1, temp2);
        } else {
            double temp1 = Math.sqrt(Math.pow(qX - x1, 2) + Math.pow(qY - y1, 2));
            double temp2 = Math.sqrt(Math.pow(qX - x1, 2) + Math.pow(qY - y2, 2));
            double temp3 = Math.sqrt(Math.pow(qX - x2, 2) + Math.pow(qY - y1, 2));
            double temp4 = Math.sqrt(Math.pow(qX - x2, 2) + Math.pow(qY - y2, 2));
            minDist = min(temp1, temp2, temp3, temp4);

        }

        return minDist;
    }

    /**
     * calculate distance between a point and a door
     */
    public double distPointDoor(Point point, Door door) {
        double dist = 0;
        dist = Math.sqrt(Math.pow(point.getX() - door.getX(), 2) + Math.pow(point.getY() - door.getY(), 2));
        return dist;
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
     * get host partition of a point
     */
    public int getHostPartition(Point point) {
        int partitionId = -1;
        int floor = point.getmFloor();
        ArrayList<Integer> pars = IndoorSpace.iFloors.get(floor).getmPartitions();
        for (int i = 0; i < pars.size(); i++) {
            Partition par = IndoorSpace.iPartitions.get(pars.get(i));
            if (par == null) continue;
            if (point.getX() >= par.getX1() && point.getX() <= par.getX2() && point.getY() >= par.getY1() && point.getY() <= par.getY2()) {
                partitionId = par.getmID();
                if (DataGenConstant.dataset.equals("MZB") && IndoorSpace.iPartitions.get(partitionId).getmType() == RoomType.HALLWAY) continue;
                return partitionId;
            }
        }
        return partitionId;
    }

//    public int getHostPartition(Point p, RTree<Integer, Box> T) {
//        int parId;
//
//        ArrayList<Integer> results = new ArrayList<>();
//
//        Observable<dk.aau.cs.indoorqueries.common.iCIndex.rtree3d.Entry<Integer, Box>> entries = T.search(Geometries.point(p.getX(), p.getY(),p.getmFloor()));
//
//
//        entries.subscribe(
//                e -> results.add(e.value())
//        );
//        parId = results.get(0);
//        return parId;
//    }


    /**
     * calculate the distance between two point
     */
    public double pt2objectDist(Point ps, IndoorObject ob, RTree<Integer, Box> T) {
        int doorSize = IndoorSpace.iDoors.size();
        double[][] dists = new double[doorSize][doorSize];
        int sPartitionId = getHostPartition(ps);
        int tPartitionId = ob.getParId();

        if (sPartitionId == tPartitionId) {
            return distPointObject(ps, ob);
        }
        Partition sPar = IndoorSpace.iPartitions.get(sPartitionId);
        Partition tPar = IndoorSpace.iPartitions.get(tPartitionId);
        ArrayList<Integer> sDoors = sPar.getTopology().getP2DEnter();
        ArrayList<Integer> tDoors = tPar.getTopology().getP2DLeave();

        for (int i = 0; i < sDoors.size(); i++) {
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
                if (dists[sDoorId][tDoorId] >= Constant.large && distPointDoor(ps, sDoor) + distPointObject(tDoor, ob) < minDist) {
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
                    if (minDist > distPointDoor(ps, sDoor) + dist[curDoorId] + distPointObject(curDoor, ob)) {
                        minDist = distPointDoor(ps, sDoor) + dist[curDoorId] + distPointObject(curDoor, ob);
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
                            if (minDist > distPointDoor(ps, prevDoor) + dists[prevDoorId][curDoorId] + distPointObject(curDoor, ob)) {
                                minDist = distPointDoor(ps, prevDoor) + dists[prevDoorId][curDoorId] + distPointObject(curDoor, ob);
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
                        if (minDist > distPointDoor(ps, sDoor) + dists[sDoorId][tempDoorId] + distPointObject(tempDoor, ob)) {
                            minDist = distPointDoor(ps, sDoor) + dists[sDoorId][tempDoorId] + distPointObject(tempDoor, ob);
//                            minPath = getPath(prev, sDoorId, tempDoorId);
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
                    if (!R_partitions.contains(nextParId)) continue;
                    ArrayList<Integer> leaveDoors = nextPar.getTopology().getP2DLeave();
//                    System.out.println("leaveDoor: " + leaveDoors);
                    for (int k = 0; k < leaveDoors.size(); k++) {
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
//        System.out.println("minDist: " + minDist + "  minPath: " + minPath);
        return minDist;

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
//        int currp = prev.get(de).get(0);
        int currd = prev.get(de).get(1);

        while (currd != ds) {
            result = currd + "\t" + result;
//			System.out.println("current: " + currp + ", " + currd + " next: " + prev[currd].toString());
//            currp = prev.get(currd).get(0);
            currd = prev.get(currd).get(1);

        }

        result = currd + "\t" + result;

        return result;
    }

    /**
     * get k nearest neighbors
     * @param q
     * @param k
     * @param T
     * @return
     */
    public ArrayList<ArrayList<Double>> knn(Point q, int k, RTree<Integer, Box> T) {
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

            if (dist[curDoorId] >= getKBound(k)) break;
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
                calDist(nextParObjects, curDoor, dist[curDoorId], k);


                ArrayList<Integer> leaveDoors = nextPar.getTopology().getP2DLeave();
                for (int j = 0; j < leaveDoors.size(); j++) {
                    int leaveDoorId = leaveDoors.get(j);
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
//        System.out.println(kObjects);
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
}
