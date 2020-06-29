package dk.aau.cs.indoorqueries.common.algorithm;

import dk.aau.cs.indoorqueries.common.iPTree.LeafNode;
import dk.aau.cs.indoorqueries.common.iPTree.Node;
import dk.aau.cs.indoorqueries.common.iPTree.VIPTree;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Door;
import dk.aau.cs.indoorqueries.common.indoorEntitity.IndoorSpace;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Partition;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Point;
import dk.aau.cs.indoorqueries.common.utilities.Constant;

import java.util.ArrayList;
import java.util.HashMap;

public class IPtree_SPQ {
    public static int visitDoors = 0;

    public int ipTreeSPQ(Point s, Point t, VIPTree tree) {
        visitDoors = 0;
        Node sNode = tree.leaf(s);
        Node tNode = tree.leaf(t);
//        System.out.println("sNode: " + sNode.toString());
//        System.out.println("tNode: " + tNode.toString());

        if (sNode == tNode) {
            Partition sPar = tree.partition(s);
            Partition tPar = tree.partition(t);
            int sParId = sPar.getmID();
            int tParId = tPar.getmID();
            if (sParId == tParId) {
//                System.out.println(distPoint2Point(s, t));
                double minDist = distPoint2Point(s, t);
                return visitDoors;
            }

            double minDist = Constant.large;
            String minPath = "";
            ArrayList<Integer> sDoors = sPar.getmDoors();
            ArrayList<Integer> tDoors = tPar.getmDoors();
            for (int i = 0; i < sDoors.size(); i++) {
                double dist = Constant.large;
                String path = "";
                int sDoorId = sDoors.get(i);
                visitDoors++;
                double dist1 = distPointDoor(s, IndoorSpace.iDoors.get(sDoorId));
                for (int j = 0; j < tDoors.size(); j++) {
                    int tDoorId = tDoors.get(j);
                    visitDoors++;
                    double dist2 = distPointDoor(t, IndoorSpace.iDoors.get(tDoorId));
                    if (sDoorId == tDoorId) {
                        dist = dist1 + dist2;
                        path = sDoorId + "";
                    } else {
                        String dist3Str = sNode.getDist(sDoorId, tDoorId);
                        String[] dist3Arr = dist3Str.split("\t");
                        double dist3 = Double.parseDouble(dist3Arr[0]);
                        path = arrToString(dist3Arr, 1, dist3Arr.length - 1);
                        dist = dist1 + dist2 + dist3;
                    }
                    if (dist < minDist) {
                        minDist = dist;
                        minPath = path;
                    }
                }
            }
            System.out.println(minDist + "\t" + minPath);
            return visitDoors;

        }

        ArrayList<Node> comAncestors = commonAncestor(sNode, tNode, tree);
        Node sAncestor = comAncestors.get(0);
        Node tAncestor = comAncestors.get(1);
        Node comAncestor = tree.getNode(sAncestor.getParentNodeID());
//        System.out.println("sAncestor: " + sAncestor.toString());
//        System.out.println("tAncestor: " + tAncestor.toString());


        ArrayList<Integer> sAncestorAccDoors = sAncestor.getAccessDoors();
        ArrayList<Integer> tAncestorAccDoors = tAncestor.getAccessDoors();


        HashMap<Integer, String> sPointToAncestorDist = pointToAncestorDoorDist(s, sNode, sAncestor, tree);
        HashMap<Integer, String> tPointtoAncestorDist = pointToAncestorDoorDist(t, tNode, tAncestor, tree);

        double minDist = Constant.large;
        String minPath = "";

        for (int i = 0; i < sAncestorAccDoors.size(); i++) {
            int sAncAccDoorId = sAncestorAccDoors.get(i);
//            visitDoors++;

            String sDistStr = sPointToAncestorDist.get(sAncAccDoorId);
            if (sDistStr == null) {

                System.out.println("something wrong with sDistStr");
                continue;
            }

            String[] sDistArr = sDistStr.split("\t");
            double sDist = Double.parseDouble(sDistArr[0]);
            String sPath = arrToString(sDistArr, 1, sDistArr.length - 1);

            for (int j = 0; j < tAncestorAccDoors.size(); j++) {
//                visitDoors++;
                double dist = 0;
                String path = "";
                int tAncAccDoorId = tAncestorAccDoors.get(j);

                String tDistStr = tPointtoAncestorDist.get(tAncAccDoorId);
                if (tDistStr == null) {
                    System.out.println("something wrong with tDistStr");
                }
                String[] tDistArr = tDistStr.split("\t");
                double tDist = Double.parseDouble(tDistArr[0]);
                String[] tDistArrConvert = convertArr(tDistArr);
                String tPath = arrToString(tDistArrConvert, 1, tDistArrConvert.length - 2);

                if (sAncAccDoorId == tAncAccDoorId) {
                    dist = sDist + tDist;
                    path = sPath + tPath;
                } else {
                    String mDistStr = comAncestor.getDist(sAncAccDoorId, tAncAccDoorId);
                    if (mDistStr == null) {
                        System.out.println("something wrong with mDistStr");
                    }
                    String[] mDistArr = mDistStr.split("\t");
                    double mDist = Double.parseDouble(mDistArr[0]);
                    String mPath = arrToString(mDistArr, 2, mDistArr.length - 1);

                    dist = sDist + mDist + tDist;
                    path = sPath + mPath + tPath;
                }
//                System.out.println(dist +  "\t" + path);

                if (dist < minDist) {
                    minDist = dist;
                    minPath = path;
                }
            }
        }
        System.out.println(minDist + "\t" + minPath);
        return visitDoors;
    }


    public HashMap<Integer, String> pointToAncestorDoorDist(Point point, Node nLeaf, Node n, VIPTree tree) {
        Node childNode = nLeaf;
        Node parentNode = tree.getNode(childNode.getParentNodeID());

        HashMap<Integer, String> result = new HashMap<>();
        HashMap<Integer, String> pointToChildNode = pointToAccLeafDist(point, (LeafNode) childNode, tree);

        if (nLeaf.getNodeID() == n.getNodeID()) {
            result = pointToChildNode;
            return result;
        }

        while (childNode.getNodeID() != n.getNodeID()) {
//            System.out.println("childNode: " + nLeaf.toString());
//            System.out.println("parentNode: " + parentNode.toString());

            HashMap<Integer, String> pointToParNode = new HashMap<>();
            ArrayList<Integer> parentAccDoors = parentNode.getAccessDoors();
            ArrayList<Integer> childAccDoors = childNode.getAccessDoors();
            Boolean[] isVisited = new Boolean[parentAccDoors.size()];

            for (int i = 0; i < parentAccDoors.size(); i++) {
                int parAccDoorId = parentAccDoors.get(i);
                visitDoors++;
//                System.out.println("parAccDoorId: " + parAccDoorId);
                double minDist = Constant.large;
                String minPath = "";
                for (int j = 0; j < childAccDoors.size(); j++) {
                    visitDoors++;
                    double dist = Constant.large;
                    String path = "";

                    int childAccDoorId = childAccDoors.get(j);
//                    System.out.println("childAccDoorId: " + childAccDoorId);
                    String pointToChildDoorStr = pointToChildNode.get(childAccDoorId);
//                    System.out.println("pointToChildDoorStr: " + pointToChildDoorStr);

//                    if (parAccDoorId == childAccDoorId) {
//                        pointToParNode.put(parAccDoorId, pointToChildDoorStr);
//                        continue;
//                    }

                    String[] pointToChildDoorArr = pointToChildDoorStr.split("\t");
                    double dist1 = Double.parseDouble(pointToChildDoorArr[0]);
                    String path1 = arrToString(pointToChildDoorArr, 1, pointToChildDoorArr.length - 1);

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
                pointToParNode.put(parAccDoorId, minDist + "\t" + minPath);

            }
            if (parentNode.getNodeID() == n.getNodeID()) {
                result = pointToParNode;
            }
            childNode = parentNode;
            parentNode = tree.getNode(parentNode.getParentNodeID());
            pointToChildNode = pointToParNode;

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
            visitDoors++;
            double minDist = Constant.large;
            String minPath = "";
            for (int j = 0; j < doors.size(); j++) {
                double dist = 0;
                String path = "";

                int doorId = doors.get(j);
                visitDoors++;
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

    public ArrayList<Integer> findSuperiorDoors(Partition par, ArrayList<Integer> accessDoors) {
        ArrayList<Integer> result = new ArrayList<>();
        ArrayList<Integer> doors = par.getmDoors();
        for (int i = 0; i < doors.size(); i++) {
            int doorId = doors.get(i);
            if (accessDoors.contains(doorId)) {
                result.add(doorId);
                continue;
            }


            for (int j = 0; j < accessDoors.size(); j++) {
                int accDoorId = accessDoors.get(j);

                if (inSamePartition(doorId, accDoorId)) {
                    result.add(doorId);
                }
            }
        }
        return result;
    }

    public Boolean inSamePartition(int d1, int d2) {
        Door door1 = IndoorSpace.iDoors.get(d1);
        ArrayList<Integer> partitions1 = door1.getmPartitions();
        Door door2 = IndoorSpace.iDoors.get(d2);
        ArrayList<Integer> partitions2 = door2.getmPartitions();

        for (int i = 0; i < partitions1.size(); i++) {
            for (int j = 0; j < partitions2.size(); j++) {
                if ((int) partitions1.get(i) == (int) partitions2.get(j)) {
                    return true;
                }
            }
        }
        return false;
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

    public String arrToString(String[] arr, int start, int end) {
        String result = "";
        for (int i = start; i <= end; i++) {
            result += arr[i] + "\t";
        }
        return result;
    }

    public ArrayList<Node> commonAncestor(Node leaf_s, Node leaf_e, VIPTree tree) {
        ArrayList<Node> result = new ArrayList<Node>();
        Node temp_s = leaf_s;
        Node temp_e = leaf_e;

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

    public String[] convertArr(String[] arr) {
        String[] result = new String[arr.length];
        for (int i = 1; i < arr.length; i++) {
            result[arr.length - 1 - i] = arr[i];
        }
        return result;
    }
}
