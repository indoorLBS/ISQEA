package dk.aau.cs.indoorqueries.common.algorithm;

import dk.aau.cs.indoorqueries.common.iPTree.LeafNode;
import dk.aau.cs.indoorqueries.common.iPTree.Node;
import dk.aau.cs.indoorqueries.common.iPTree.VIPTree;
import dk.aau.cs.indoorqueries.common.indoorEntitity.*;
import dk.aau.cs.indoorqueries.common.utilities.Constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Algorithm of procesing knnq using VIPTree
 * @author Tiantian Liu
 */
public class VIPtree_KNN {
    public static ArrayList<ArrayList<Double>> kObjects = new ArrayList<>();

    /**
     * process knnq using VIPTree
     * @param q
     * @param k
     * @param tree
     * @return
     */
    public ArrayList<ArrayList<Double>> vipTreeKNN(Point q, int k, VIPTree tree) {
        kObjects = new ArrayList<>();
        HashMap<Integer, HashMap<Integer, Double>> pointToNodeAccDoorsDist = new HashMap<>();

        Partition qPar = tree.partition(q);
        int qPartitionId = qPar.getmID();

        Node qNode = tree.leaf(qPartitionId);

        ArrayList<Integer> qParObjects = qPar.getmObjects();

        calDist(((LeafNode) qNode), q, qPar, k);

//        System.out.println(R);
        HashMap<Integer, HashMap<Integer, Double>> pointToAllAncesDoorDist = pointToAllAncestorDoorsDist(q, qPar, qNode, tree);

        BinaryHeap<Double> H = new BinaryHeap<>(tree.root().getNodeID() + 1);


        H.insert((double) getDistPointToNodeVIP(q, qPartitionId, tree.root(), tree, 1, pointToAllAncesDoorDist).get(-1), tree.root().getNodeID());

        while (H.heapSize != 0) {
            String current = H.delete_min();
            String[] currentArr = current.split(",");
            int curNodeId = Integer.parseInt(currentArr[1]);
            Node curNode = tree.getNode(curNodeId);
            double curDist = Double.parseDouble(currentArr[0]);

            if (curDist >= getKBound(k) && kObjects.size() == k) {
                System.out.println(kObjects);
                return kObjects;
            }

            if (curNode.getType() == "NonLeafNode") {
                ArrayList<Integer> children = curNode.getmChildren();

                for (int i = 0; i < children.size(); i++) {
                    int childId = children.get(i);
                    Node child = tree.getNode(childId);
                    if (child.getAllObjects().size() > 0) {
                        HashMap<Integer, Double> tempMap = getDistPointToNodeVIP(q, qPartitionId, child, tree, 1, pointToAllAncesDoorDist);
                        double tempDist = (double) tempMap.get(-1);
                        if (tempDist != 0) {
                            pointToNodeAccDoorsDist.put(childId, tempMap);
                        }
                        H.insert(tempDist, childId);
                    }
                }
            }

            if (curNode.getType() == "LeafNode") {
                ArrayList<Integer> objects = curNode.getAllObjects();
                ArrayList<Integer> partitions = curNode.getmPartitions();

                if (objects.size() == 0) continue;

                ArrayList<Integer> accDoors = curNode.getAccessDoors();
                HashMap<Integer, ArrayList<ArrayList<Double>>> allObDist = ((LeafNode) curNode).getObjectDist();

                HashMap<Integer, Double> pointToAccDists = new HashMap<>();

                if (pointToNodeAccDoorsDist.get(curNodeId) != null) {
                    pointToAccDists = pointToNodeAccDoorsDist.get(curNodeId);
                } else {
                    pointToAccDists = getDistPointToNodeVIP(q, qPartitionId, curNode, tree, 2, pointToAllAncesDoorDist);
                }

                for (int i = 0; i < accDoors.size(); i++) {
                    int accDoorId = accDoors.get(i);
                    double dist1 = pointToAccDists.get(accDoorId);

                    ArrayList<ArrayList<Double>> obDists = allObDist.get(accDoorId);

                    for (int j = 0; j < obDists.size(); j++) {
                        ArrayList<Double> obDist = obDists.get(j);
                        double dist2 = (double) obDist.get(1);
                        if (dist1 + dist2 > getKBound(k) && kObjects.size() == k) break;
                        else {
                            addObToKlist((int) (double) obDist.get(0), dist1 + dist2, k);
                        }
                    }
                }

            }


        }
        return null;
    }

    /**
     * calculate distances from a point to access doors in a node
     * @param point
     * @param qPartitionId
     * @param node
     * @param tree
     * @param caseNum
     * @param pointToAllAncesDoorsDist
     * @return
     */
    public HashMap<Integer, Double> getDistPointToNodeVIP(Point point, int qPartitionId, Node node, VIPTree tree, int caseNum, HashMap<Integer, HashMap<Integer, Double>> pointToAllAncesDoorsDist) {
        HashMap<Integer, Double> result = new HashMap<>();
        ArrayList<Integer> partitions = node.getmPartitions();

        Partition par = IndoorSpace.iPartitions.get(qPartitionId);

        for (int i = 0; i < partitions.size(); i++) {
            if (partitions.get(i) == qPartitionId) {
                result.put(-1, 0.0);
                if (caseNum == 1) {
                    return result;
                }
            }
        }

        Node leafNode = tree.leaf(point);
        ArrayList<Node> comAncestors = commonAncestor(leafNode, node, tree);
        Node sAncestor = comAncestors.get(0);
        Node tAncestor = comAncestors.get(1);
        Node comAncestor = tree.getNode(sAncestor.getParentNodeID());

        ArrayList<Integer> sAncestorAccDoors = sAncestor.getAccessDoors();
        ArrayList<Integer> tAncestorAccDoors = tAncestor.getAccessDoors();

        HashMap<Integer, Double> sPointToAncestorDist = new HashMap<>();


        sPointToAncestorDist = pointToAllAncesDoorsDist.get(sAncestor.getNodeID());


        ArrayList<Integer> accDoors = node.getAccessDoors();
        ArrayList<Integer> doors = par.getmDoors();

        double overalMinDist = Constant.large;
        for (int k = 0; k < accDoors.size(); k++) {
            int accDoorId = accDoors.get(k);

            double minDist = Constant.large;

            HashMap<Integer, String> tPointToAncestorDist = doorToAncestorDoorDistVIP(accDoorId, node, tAncestor, tree);

            for (int i = 0; i < sAncestorAccDoors.size(); i++) {
                int sAncAccDoorId = sAncestorAccDoors.get(i);

                double sDist = sPointToAncestorDist.get(sAncAccDoorId);
                if (sPointToAncestorDist.get(sAncAccDoorId) == null) {
                    System.out.println("something wrong with sDistStr");
                }

                for (int j = 0; j < tAncestorAccDoors.size(); j++) {
                    double dist = 0;
                    int tAncAccDoorId = tAncestorAccDoors.get(j);

                    String tDistStr = tPointToAncestorDist.get(tAncAccDoorId);
                    if (tDistStr == null) {
                        System.out.println("something wrong with tDistStr");
                    }
                    String[] tDistArr = tDistStr.split("\t");
                    double tDist = Double.parseDouble(tDistArr[0]);
                    if (sAncAccDoorId == tAncAccDoorId) {
                        dist = sDist + tDist;
                    } else {
                        String mDistStr = comAncestor.getDist(sAncAccDoorId, tAncAccDoorId);
                        if (mDistStr == null) {
                            System.out.println("something wrong with mDistStr");
                        }
                        String[] mDistArr = mDistStr.split("\t");
                        double mDist = Double.parseDouble(mDistArr[0]);
                        String mPath = arrToString(mDistArr, 2, mDistArr.length - 1);

                        dist = sDist + mDist + tDist;

                    }

                    if (dist < minDist) {
                        minDist = dist;
                    }
                }
            }

            result.put(accDoorId, minDist);

            if (minDist < overalMinDist) {
                overalMinDist = minDist;
            }

        }

        if (result.get(-1) == null) {
            result.put(-1, overalMinDist);
        }

        return result;
    }

    public HashMap<Integer, String> pointToAccLeafDist(Point point, LeafNode n, VIPTree tree) {
        HashMap<Integer, String> result = new HashMap<>(); // key: access door Id; Object: minDist + minPath
        Partition par = tree.partition(point);
        int parId = par.getmID();
        ArrayList<Integer> accessDoors = n.getAccessDoors();
//        ArrayList<Integer> superiorDoors = findSuperiorDoors(par, accessDoors);
        ArrayList<Integer> doors = par.getmDoors();
        for (int i = 0; i < accessDoors.size(); i++) {
            int accDoorId = accessDoors.get(i);
            double minDist = Constant.large;
            String minPath = "";
            for (int j = 0; j < doors.size(); j++) {
                double dist = 0;
                String path = "";

                int doorId = doors.get(j);
                Door door = IndoorSpace.iDoors.get(doorId);

                double dist1 = distPointDoor(point, door);

                if (doorId == accDoorId) {
                    dist = dist1;
                    path = String.valueOf(accDoorId);
                } else {
                    String dist2Str = n.getDist(doorId, accDoorId);
                    String[] dist2Arr = dist2Str.split("\t");
                    double dist2 = Double.parseDouble(dist2Arr[0]);

                    dist = dist1 + dist2;
                    path = arrToString(dist2Arr, 1, dist2Arr.length - 1);
                }
                if (dist <= minDist) {
                    minDist = dist;
                    minPath = path;
                }
            }
            result.put(accDoorId, minDist + "\t" + minPath);
        }
        return result;
    }


    public HashMap<Integer, String> pointToAncestorAccDoorDistVIP(Point point, Partition par, Node nLeaf, Node nAncestor, VIPTree tree) {
        HashMap<Integer, String> result = new HashMap<>(); // key: access door Id; Object: minDist + minPath
//        System.out.println("leafnode: " + nLeaf.getNodeID());

        ArrayList<Integer> doors = par.getTopology().getP2DLeave();

        ArrayList<Integer> accDoors = nAncestor.getAccessDoors();

        double overallMinDist = Constant.large;

        for (int i = 0; i < accDoors.size(); i++) {
            int accDoorId = accDoors.get(i);
//            System.out.println("accDoorID: " + accDoorId);
            double minDist = Constant.large;
//            String minPath = "";

            for (int j = 0; j < doors.size(); j++) {
                int doorId = doors.get(j);
//                System.out.println("doorId: " + doorId);
                double dist = Constant.large;
//                String path = "";

                double dist1 = distPointDoor(point, IndoorSpace.iDoors.get(doorId));

                if (doorId == accDoorId) {
                    dist = dist1;
//                    path = String.valueOf(accDoorId);
                } else {
                    String dist2Str = ((LeafNode) nLeaf).getDistVIP(doorId, accDoorId);
                    String[] dist2Arr = dist2Str.split("\t");
                    double dist2 = Double.parseDouble(dist2Arr[0]);

                    dist = dist1 + dist2;
//                    path = arrToString(dist2Arr, 1, dist2Arr.length - 1);
                }
                if (dist <= minDist) {
                    minDist = dist;
//                    minPath = path;
                }
            }
            result.put(accDoorId, minDist + "\t");
            if (minDist < overallMinDist) {
                overallMinDist = minDist;
            }
        }

        if (result.get(-1) == null) {
            result.put(-1, overallMinDist + "\t");
        }
        return result;
    }

    /**
     * calculate distances from a point to access doors of all ancestors
     * @param point
     * @param par
     * @param qNode
     * @param tree
     * @return
     */
    public HashMap<Integer, HashMap<Integer, Double>> pointToAllAncestorDoorsDist(Point point, Partition par, Node qNode, VIPTree tree) {
        HashMap<Integer, HashMap<Integer, Double>> result = new HashMap<>();

        Node node = qNode;
        ArrayList<Integer> doors = par.getTopology().getP2DLeave();

        while (true) {
            ArrayList<Integer> accDoors = node.getAccessDoors();
            HashMap<Integer, Double> doorToAccDoorDist = new HashMap<>();

            for (int i = 0; i < accDoors.size(); i++) {
                int accDoorId = accDoors.get(i);

                double minDist = Constant.large;

                for (int j = 0; j < doors.size(); j++) {
                    int doorId = doors.get(j);

                    double dist = Constant.large;

                    double dist1 = distPointDoor(point, IndoorSpace.iDoors.get(doorId));

                    if (doorId == accDoorId) {
                        dist = dist1;
                    } else {
                        double dist2 = Constant.large;
                        if (node.getType() == "LeafNode") {
                            dist2 = Double.parseDouble(qNode.getDist(doorId, accDoorId).split("\t")[0]);
                        } else {
                            dist2 = Double.parseDouble(((LeafNode) qNode).getDistVIP(doorId, accDoorId).split("\t")[0]);
                        }
                        dist = dist1 + dist2;
                    }

                    if (dist < minDist) {
                        minDist = dist;
                    }
                }
                doorToAccDoorDist.put(accDoorId, minDist);

            }
            result.put(node.getNodeID(), doorToAccDoorDist);
            if (node.getNodeID() == tree.root().getNodeID()) {
                break;
            }
            node = tree.getNode(node.getParentNodeID());
        }
        return result;
    }

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

    /**
     * change an array to string
     * @param arr
     * @param start
     * @param end
     * @return
     */
    public String arrToString(String[] arr, int start, int end) {
        String result = "";
        for (int i = start; i <= end; i++) {
            result += arr[i] + "\t";
        }
        return result;
    }

    /**
     * find the common ancestor of two nodes
     * @param n1
     * @param n2
     * @param tree
     * @return
     */
    public ArrayList<Node> commonAncestor(Node n1, Node n2, VIPTree tree) {
        ArrayList<Node> result = new ArrayList<Node>();
        Node temp_s = n1;
        Node temp_e = n2;
        while (temp_s.getHeight() != temp_e.getHeight()) {
            if (temp_s.getHeight() > temp_e.getHeight()) {
                temp_e = tree.getNode(temp_e.getParentNodeID());
            } else {
                temp_s = tree.getNode(temp_s.getParentNodeID());
            }
        }

        while (true) {
//			System.out.println("temp_s: " + temp_s.getNodeID());
//			System.out.println("temp_e: " + temp_e.getNodeID());

            // if reach the root
            if (temp_s.getParentNodeID() == tree.root().getNodeID()) {
                result.add(temp_s);
                result.add(temp_e);
                break;
            }
            // if reach the common ancestor
            if (temp_s.getParentNodeID() == temp_e.getParentNodeID()) {
                result.add(temp_s);
                result.add(temp_e);
                break;
            }

            temp_s = tree.getNode(temp_s.getParentNodeID());
            temp_e = tree.getNode(temp_e.getParentNodeID());
        }

        return result;
    }

    /**
     * calculate distance from a door to access doors of an ancestor
     * @param doorId
     * @param sNode
     * @param n
     * @param tree
     * @return
     */
    public HashMap<Integer, String> doorToAncestorDoorDistVIP(int doorId, Node sNode, Node n, VIPTree tree) {
        Node childNode = sNode;
        Node parentNode = tree.getNode(childNode.getParentNodeID());

        HashMap<Integer, String> result = new HashMap<>();


        if (childNode.getNodeID() == n.getNodeID()) {
            for (int i = 0; i < n.getAccessDoors().size(); i++) {
                int accDoorId = n.getAccessDoors().get(i);
                if (doorId == accDoorId) {
                    result.put(accDoorId, 0 + "\t");
                } else {
                    result.put(accDoorId, n.getDist(doorId, accDoorId));
                }
            }
            return result;
        }

        if (childNode.getType() == "LeafNode") {
            ArrayList<Integer> accDoors = n.getAccessDoors();

            for (int i = 0; i < accDoors.size(); i++) {
                int accDoorId = accDoors.get(i);
//                System.out.println("doorToAncestorDoorDistVIP: doorId: " + doorId + " accDoorId: " + accDoorId);
                result.put(accDoorId, ((LeafNode) childNode).getDistVIP(doorId, accDoorId));
            }
            return result;
        }

        HashMap<Integer, String> doorToChildNode = new HashMap<>();

        ArrayList<Integer> accDoors = childNode.getAccessDoors();
        for (int i = 0; i < accDoors.size(); i++) {
            int accDoorId = accDoors.get(i);
            if (accDoorId == doorId) {
                doorToChildNode.put(accDoorId, 0 + "\t");
            } else {
                doorToChildNode.put(accDoorId, childNode.getDist(doorId, accDoorId));
            }
        }


        while (childNode.getNodeID() != n.getNodeID()) {
//            System.out.println("childNode: " + sNode.toString());
//            System.out.println("parentNode: " + parentNode.toString());

            HashMap<Integer, String> doorToParNode = new HashMap<>();
            ArrayList<Integer> parentAccDoors = parentNode.getAccessDoors();
            ArrayList<Integer> childAccDoors = childNode.getAccessDoors();
            Boolean[] isVisited = new Boolean[parentAccDoors.size()];

            for (int i = 0; i < parentAccDoors.size(); i++) {
                int parAccDoorId = parentAccDoors.get(i);
//                System.out.println("parAccDoorId: " + parAccDoorId);
                double minDist = Constant.large;
                String minPath = "";
                for (int j = 0; j < childAccDoors.size(); j++) {
                    double dist = Constant.large;
                    String path = "";

                    int childAccDoorId = childAccDoors.get(j);
//                    System.out.println("childAccDoorId: " + childAccDoorId);
                    String doorToChildDoorStr = doorToChildNode.get(childAccDoorId);
//                    System.out.println("pointToChildDoorStr: " + doorToChildDoorStr);

//                    if (parAccDoorId == childAccDoorId) {
//                        pointToParNode.put(parAccDoorId, pointToChildDoorStr);
//                        continue;
//                    }

                    String[] doorToChildDoorArr = doorToChildDoorStr.split("\t");
                    double dist1 = Double.parseDouble(doorToChildDoorArr[0]);
                    String path1 = arrToString(doorToChildDoorArr, 1, doorToChildDoorArr.length - 1);

                    if (parAccDoorId == childAccDoorId) {
                        dist = dist1;
                        path = path1;
                    } else {

                        String childDoorToParentDoorStr = parentNode.getDist(childAccDoorId, parAccDoorId);
                        String[] childDoorToParentDoorArr = childDoorToParentDoorStr.split("\t");
                        double dist2 = Double.parseDouble(childDoorToParentDoorArr[0]);
                        String path2 = arrToString(childDoorToParentDoorArr, 2, childDoorToParentDoorArr.length - 1);

                        dist = dist1 + dist2;
                        path = path1 + path2;
                    }

                    if (dist < minDist) {
                        minDist = dist;
                        minPath = path;

                    }
                }
                doorToParNode.put(parAccDoorId, minDist + "\t" + minPath);

            }
            if (parentNode.getNodeID() == n.getNodeID()) {
                result = doorToParNode;
            }
            childNode = parentNode;
            parentNode = tree.getNode(parentNode.getParentNodeID());
            doorToChildNode = doorToParNode;

        }

        return result;
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

        for (int i = 0; i < kObjects.size(); i++) {
            if ((int) (double) kObjects.get(i).get(0) == objectId) {
                if (dist < (double) kObjects.get(i).get(1)) {
                    kObjects.remove(i);
                    i--;
                } else {
                    return;
                }
            }
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
     * calculate distances from a point to objects in a leafnode, and maintain the k nearest objects
     * @param node
     * @param q
     * @param par
     * @param k
     */

    public void calDist(LeafNode node, Point q, Partition par, int k) {
//        System.out.println("nodeId: " + node.getNodeID());
        ArrayList<Integer> objects = node.getAllObjects();
        ArrayList<Integer> pointLeaveDoors = par.getmDoors();
        for (int i = 0; i < objects.size(); i++) {
            int objectId = objects.get(i);
            IndoorObject ob = IndoorSpace.iObject.get(objectId);
            int obParId = ob.getParId();
            Partition obPar = IndoorSpace.iPartitions.get(obParId);
            ArrayList<Integer> obDoors = obPar.getmDoors();
            double minDist = Constant.large;
            if (obParId == par.getmID()) {
                minDist = distPointObject(q, ob);
            } else {
                for (int j = 0; j < pointLeaveDoors.size(); j++) {
                    int pointLeaveDoorId = pointLeaveDoors.get(j);
                    double dist = distPointDoor(q, IndoorSpace.iDoors.get(pointLeaveDoorId));
                    for (int h = 0; h < obDoors.size(); h++) {
                        int obDoorId = obDoors.get(h);
                        dist += distPointObject(IndoorSpace.iDoors.get(obDoorId), ob);
                        if (pointLeaveDoorId != obDoorId) {
                            dist += Double.parseDouble(node.getDist(pointLeaveDoorId, obDoorId).split("\t")[0]);
                        }
                        if (dist < minDist) {
                            minDist = dist;
                        }
                    }
                }
            }

            if (minDist < getKBound(k)) {
                addObToKlist(objectId, minDist, k);
            }
        }
    }


}
