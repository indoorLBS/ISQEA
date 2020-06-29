package dk.aau.cs.indoorqueries.syn.test.experiments;

import dk.aau.cs.indoorqueries.common.algorithm.IDModel_SPQ;
import dk.aau.cs.indoorqueries.common.iDModel.GenTopology;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Point;
import dk.aau.cs.indoorqueries.common.utilities.DataGenConstant;
import dk.aau.cs.indoorqueries.syn.datagenerate.DataGen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Prepare {
    ArrayList<ArrayList<Point>> pointPair = new ArrayList<>();

    public static void main(String[] arg) throws IOException {
        DataGen dataGen = new DataGen();
        dataGen.genAllData(DataGenConstant.dataType, DataGenConstant.divisionType);

        GenTopology genTopology = new GenTopology();
        genTopology.genTopology();

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
            ArrayList<String> point1s = new ArrayList<>();
            point1s.addAll(new ArrayList<>(Arrays.asList("1299.0,1127.0,1", "214.0,242.0,2", "1159.0,1206.0,0", "1327.0,1161.0,1", "79.0,164.0,1", "1235.0,36.0,1", "58.0,226.0,1", "1180.0,1210.0,2", "77.0,1355.0,1", "138.0,1199.0,0")));

//            System.out.println("point1 is finished");
            Point point2 = pickPoint();
//            System.out.println("point2 is finished");
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
            int x = (int) (Math.random() * (1368 + 1));
            int y = (int) (Math.random() * (1368 + 1));
            int floor = (int) (Math.random() * (3));
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

        if (x >= 1368 || y >= 1368) {
            return false;
        }

        if (x <= 1080 && x >= 288) {
            if ((y <= 144 && y >= 0) || (y <= 1368 && y >= 1224)) {
                return false;
            }
        }

        if (y <= 1080 && y >= 288) {
            if ((x <= 144 && x >= 0) || (x <= 1368 && x >= 1224)) {
                return false;
            }
        }

        return true;
    }


}
