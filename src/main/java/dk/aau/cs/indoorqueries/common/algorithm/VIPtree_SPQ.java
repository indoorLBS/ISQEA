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

public class VIPtree_SPQ {
    public static int visitDoors = 0;

    public int vipTreeSPQ(Point s, Point t, VIPTree tree) {
        visitDoors = 0;
        LeafNode sNode = (LeafNode) tree.leaf(s);
        LeafNode tNode = (LeafNode) tree.leaf(t);
//        System.out.println("sNode: " + sNode.toString());
//        System.out.println("tNode: " + tNode.toString());

        if (sNode == tNode) {
            Partition sPar = tree.partition(s);
            Partition tPar = tree.partition(t);
            int sParId = sPar.getmID();
            int tParId = tPar.getmID();
            if (sParId == tParId) {
                System.out.println(distPoint2Point(s, t));
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


        int sAncestorId = comAncestors.get(0).getNodeID();
        int tAncestorId = comAncestors.get(1).getNodeID();
        Node comAncestor = tree.getNode(tree.getNode(sAncestorId).getParentNodeID());
//        System.out.println("sAncestor: " + sAncestor.toString());
//        System.out.println("tAncestor: " + tAncestor.toString());


        ArrayList<Integer> sAncestorAccDoors = tree.getNode(sAncestorId).getAccessDoors();
        ArrayList<Integer> tAncestorAccDoors = tree.getNode(tAncestorId).getAccessDoors();
//        System.out.println("point to ancestor accDoor sNode");
        HashMap<Integer, String> sPointToAncestorDist = pointToAncestorAccDoorDistVIP(s, sNode, tree.getNode(sAncestorId), tree);
//        System.out.println("point to ancestor accDoor tNode");
        HashMap<Integer, String> tPointtoAncestorDist = pointToAncestorAccDoorDistVIP(t, tNode, tree.getNode(tAncestorId), tree);

        double minDist = Constant.large;
        String minPath = "";

        for (int i = 0; i < sAncestorAccDoors.size(); i++) {
            int sAncAccDoorId = sAncestorAccDoors.get(i);
//            visitDoors++;

            String sDistStr = sPointToAncestorDist.get(sAncAccDoorId);
            if (sDistStr == null) {
                System.out.println("something wrong with sDistStr");
            }

            String[] sDistArr = sDistStr.split("\t");
            double sDist = Double.parseDouble(sDistArr[0]);
            String sPath = arrToString(sDistArr, 1, sDistArr.length - 1);

            for (int j = 0; j < tAncestorAccDoors.size(); j++) {
                double dist = 0;
                String path = "";
                int tAncAccDoorId = tAncestorAccDoors.get(j);
//                visitDoors++;

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

    public ArrayList<Node> commonAncestor(Node leaf_s, Node leaf_e, VIPTree tree) {
        ArrayList<Node> result = new ArrayList<Node>();
        int temp_sId = leaf_s.getNodeID();
        int temp_eId = leaf_e.getNodeID();

        while (true) {
//			System.out.println("temp_s: " + temp_s.getNodeID());
//			System.out.println("temp_e: " + temp_e.getNodeID());

            // if reach the root
            if (tree.getNode(temp_sId).getParentNodeID() == tree.root().getNodeID()) {
                result.add(tree.getNode(temp_sId));
                result.add(tree.getNode(temp_eId));
                break;
            }
            // if reach the common ancestor
            if (tree.getNode(temp_sId).getParentNodeID() == tree.getNode(temp_eId).getParentNodeID()) {
                result.add(tree.getNode(temp_sId));
                result.add(tree.getNode(temp_eId));
                break;
            }

            temp_sId = tree.getNode(tree.getNode(temp_sId).getParentNodeID()).getNodeID();
            temp_eId = tree.getNode(tree.getNode(temp_eId).getParentNodeID()).getNodeID();
        }

        return result;
    }

    public HashMap<Integer, String> pointToAncestorAccDoorDistVIP(Point point, LeafNode nLeaf, Node nAncestor, VIPTree tree) {
        HashMap<Integer, String> result = new HashMap<>(); // key: access door Id; Object: minDist + minPath
        Partition par = tree.partition(point);
        ArrayList<Integer> doors = par.getTopology().getP2DLeave();

        ArrayList<Integer> accDoors = nAncestor.getAccessDoors();

        for (int i = 0; i < accDoors.size(); i++) {
            int accDoorId = accDoors.get(i);
            visitDoors++;
            double minDist = Constant.large;
            String minPath = "";

            for (int j = 0; j < doors.size(); j++) {
                int doorId = doors.get(j);
//                System.out.println("d1: " + doorId + " d2: " + accDoorId);
                visitDoors++;
                double dist = Constant.large;
                String path = "";

                double dist1 = distPointDoor(point, IndoorSpace.iDoors.get(doorId));

                if (doorId == accDoorId) {
                    dist = dist1;
                    path = String.valueOf(accDoorId);
                } else {
                    String dist2Str;
                    if (nLeaf.getNodeID() == nAncestor.getNodeID()) {
                        dist2Str = nLeaf.getDist(doorId, accDoorId);
                    } else {
                        dist2Str = nLeaf.getDistVIP(doorId, accDoorId);
                    }
                    if (dist2Str == null) continue;
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

    public String[] convertArr(String[] arr) {
        String[] result = new String[arr.length];
        for (int i = 1; i < arr.length; i++) {
            result[arr.length - 1 - i] = arr[i];
        }
        return result;
    }


}
