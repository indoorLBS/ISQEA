package dk.aau.cs.indoorqueries.hsm.test.experiments;

import dk.aau.cs.indoorqueries.common.algorithm.IDModel_SPQ;
import dk.aau.cs.indoorqueries.common.iDModel.GenTopology;
import dk.aau.cs.indoorqueries.common.indoorEntitity.IndoorSpace;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Partition;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Point;
import dk.aau.cs.indoorqueries.common.utilities.DataGenConstant;
import dk.aau.cs.indoorqueries.common.utilities.RoomType;
import dk.aau.cs.indoorqueries.hsm.datagenerate.HSMDataGenRead;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

//import datagenerate.DataGen;

public class Prepare {
    ArrayList<ArrayList<Point>> pointPair = new ArrayList<>();

    public static void main(String[] arg) throws IOException {
        HSMDataGenRead dateGenReadMen = new HSMDataGenRead();
        dateGenReadMen.dataGen();

        GenTopology genTopology = new GenTopology();
        genTopology.genTopology();

        System.out.println("data is ready");
        Prepare prepare = new Prepare();

        ArrayList<String> result = prepare.pickOnePoint(10, 1100);
        System.out.println();
        System.out.println("result");
        for (int i = 0; i < result.size(); i++) {
            System.out.println(result.get(i));
        }


    }

    public ArrayList<String> pickTwoPoints(int num, double distance) throws IOException {
        ArrayList<String> results = new ArrayList<>();
        int n = 0;

//        DistMatrixGen distMatrixGen = new DistMatrixGen();
//        distMatrixGen.readDist();
////        System.out.println(1);
//        HashMap<String, Double> d2dDistMap = distMatrixGen.getD2dDistMap();		// the distance between two doors
//        HashMap<String, String> d2dRouteMap = distMatrixGen.getD2dRouteMap();
////        System.out.println(2);
//        IDMatrix_SPQ idMatrix_spq = new IDMatrix_SPQ();
        IDModel_SPQ idModel_spq = new IDModel_SPQ();
        while (n < num) {
            System.out.println("n: " + n);
            Point point1 = pickPoint();
//            System.out.println("point1 is finished");
            Point point2 = pickPoint();
//            System.out.println("point2 is finished");
            double dist = Double.parseDouble(idModel_spq.pt2ptDistance3(point1, point2).split("\t")[0]);
//            System.out.println(3);
//            double dist = Double.parseDouble(distStr.split("\t")[0]);
            if (dist < distance + 100 && dist > distance - 100) {
                String result = dist + "\t" + point1.getX() + "," + point1.getY() + "," + point1.getmFloor() + "\t" + point2.getX() + "," + point2.getY() + "," + point2.getmFloor();
//                System.out.println(result);
                results.add(result);
                n++;
            }
        }
        return results;
    }

    public ArrayList<String> pickOnePoint(int num, double distance) throws IOException {
        ArrayList<String> results = new ArrayList<>();
        int n = 0;
        IDModel_SPQ idModel_spq = new IDModel_SPQ();
        while (n < num) {
            System.out.println("n: " + n);
            ArrayList<String> point1s = new ArrayList<>();
            point1s.addAll(new ArrayList<>(Arrays.asList("840.0,1369.0,1", "1146.0,1474.0,6", "1367.0,787.0,3", "951.0,764.0,2", "595.0,1847.0,5", "888.0,778.0,2", "1252.0,680.0,6", "514.0,1842.0,1", "1683.0,1289.0,4", "712.0,1526.0,5")));

            System.out.println("point1 is finished");
            Point point2 = pickPoint();
            System.out.println("point2 is finished");
            String point1Str = point1s.get(n);
            String[] point1Arr = point1Str.split(",");
            Point point1 = new Point(Double.parseDouble(point1Arr[0]), Double.parseDouble(point1Arr[1]), Integer.parseInt(point1Arr[2]));

            double dist = Double.parseDouble(idModel_spq.pt2ptDistance3(point1, point2).split("\t")[0]);
//            System.out.println(3);
//            double dist = Double.parseDouble(distStr.split("\t")[0]);
            if (dist < distance + 100 && dist > distance - 100) {
                String result = dist + "\t" + point1.getX() + "," + point1.getY() + "," + point1.getmFloor() + "\t" + point2.getX() + "," + point2.getY() + "," + point2.getmFloor();
//                System.out.println(result);
                results.add(result);
                n++;
            }
        }
        return results;
    }

    public Point pickPoint() {
        Point point = null;
        while (true) {
            int x = (int) (Math.random() * (2000));
            int y = (int) (Math.random() * (2000));
            int floor = (int) (Math.random() * (DataGenConstant.nFloor));
            if (isLegal(x, y, floor)) {
                point = new Point(x, y, floor);
                System.out.println("point: " + point.getX() + "," + point.getY() + "," + point.getmFloor());
                return point;
            }
        }
    }

    /**
     * check whether the object is legal
     *
     * @param x
     * @param y
     * @param floor
     * @return
     */
    public boolean isLegal(double x, double y, int floor) {
        if (floor > DataGenConstant.nFloor) {
            System.out.println("something wrong with the random floor");
            return false;
        }


        int parId = -1;
        ArrayList<Integer> partitions = IndoorSpace.iFloors.get(floor).getmPartitions();
//        System.out.println("mPartitions: " + partitions);
        for (int i = 0; i < partitions.size(); i++) {
            int tempParId = partitions.get(i);
            Partition par = IndoorSpace.iPartitions.get(tempParId);
            if (par.getmType() == RoomType.STAIRCASE || par.getmType() == RoomType.HALLWAY) continue;
            if ((x >= par.getX1() && x <= par.getX2()) || (x <= par.getX1() && x >= par.getX2())) {
                if ((y >= par.getY1() && y <= par.getY2()) || (y <= par.getY1() && y >= par.getY2())) {
                    return true;
                }
            }
        }


        return false;
    }


}
