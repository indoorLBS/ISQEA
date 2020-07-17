package dk.aau.cs.indoorqueries.syn.test.modelTest;

import dk.aau.cs.indoorqueries.common.iDModel.GenTopology;
import dk.aau.cs.indoorqueries.common.indoorEntitity.IndoorSpace;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Partition;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Point;
import dk.aau.cs.indoorqueries.common.utilities.DataGenConstant;
import dk.aau.cs.indoorqueries.common.utilities.RoomType;
import dk.aau.cs.indoorqueries.syn.datagenerate.DataGen;

import java.io.IOException;
import java.util.ArrayList;

/**
 * test IDModel
 *
 * @Author Tiantian Liu
 */
public class IDModelTest {
    public static void main(String arg[]) throws IOException {
        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        long startMem = runtime.totalMemory() - runtime.freeMemory();

        long start = System.nanoTime();
        DataGen dataGen = new DataGen();
        dataGen.genAllData(DataGenConstant.dataType, DataGenConstant.divisionType);
//        CPHDataGenRead dateGenReadMen = new CPHDataGenRead();
//        dateGenReadMen.dataGen();

        GenTopology genTopology = new GenTopology();
        genTopology.genTopology();
        long end = System.nanoTime();
        long endMem = runtime.totalMemory() - runtime.freeMemory();

        double time = (double) (end - start) / 1000 / 1000;
        double memory = (double) (endMem - startMem) / 1024 / 1024;
        System.out.println("time: " + time + " ms");
        System.out.println("memory: " + memory + " mb");

//        for (int i = 0; i < IndoorSpace.iFloors.size(); i++) {
//            System.out.println("floor: "+ i + " mPartition:" + IndoorSpace.iFloors.get(i).getmPartitions());
//        }
//
//        for (int i = 0; i < IndoorSpace.iPartitions.size(); i++) {
//            System.out.println(IndoorSpace.iPartitions.get(i).toString());
//
//        }
//
//        Partition par = IndoorSpace.iPartitions.get(96);
//        System.out.println(par.getdistMatrix().getDistance(0, 24));
//        ArrayList<Integer> doors = par.getmDoors();
//        String result = "";
//        for (int i = 0; i < doors.size(); i++) {
//            for (int j = i + 1; j < doors.size(); j++) {
//                Door door1 = IndoorSpace.iDoors.get(doors.get(i));
//                Door door2 = IndoorSpace.iDoors.get(doors.get(j));
//                double dist = 0;
//                Path path = Paths.get(System.getProperty("user.dir") + "/hallway_distMatrix_division_0" + ".txt");
//                Scanner scanner = new Scanner(path);
//
//                //read line by line
//                Boolean flag = false;
//                while(scanner.hasNextLine()){
//                    //process each line
//                    String line = scanner.nextLine();
//                    String[] tempArr = line.split("\t");
//                    if ((door1.getX() + "." + door1.getY() + "-" + door2.getX() + "." + door2.getY()).equals(tempArr[1])) {
//                        dist = Double.parseDouble(tempArr[2]);
//                        flag = true;
//                        break;
//                    }
//                }
//                if (!flag) {
//                    System.out.println("something wrong with hallway distance matrix");
//                    System.out.println(door1.getX() + "." + door1.getY() + "-" + door2.getX() + "." + door2.getY());
//                }
//                result += door1.getmID() + "-" + door2.getmID() + "\t" +
//                        door1.getX() + "." + door1.getY() + "-" + door2.getX() + "." + door2.getY() + "\t"
//                        + dist + "\n";
//            }
//        }
//
//        String outputFile = System.getProperty("user.dir") + "/hallway_distMatrix_division_0_new" + ".txt";
//        try {
//            FileWriter fw = new FileWriter(outputFile);
//            fw.write(result);
//            fw.flush();
//            fw.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//            return;
//        }


        Point ps = new Point(2, 12, 0);
        Point pt = new Point(54, 18, 1);

        IDModelTest idModelTest = new IDModelTest();

        System.out.println(idModelTest.getPointHostPartition(ps));


//        ObjectGen objectGen = new ObjectGen();
//        objectGen.readObjects();
//
//        System.out.println(IndoorSpace.iPartitions.get(137).getTopology().getP2DEnter());
//        System.out.println(IndoorSpace.iPartitions.get(137).getTopology().getP2DLeave());
//        System.out.println(IndoorSpace.iPartitions.get(137).getTopology().getP2PLeave());
//        System.out.println(IndoorSpace.iDoors.get(426).getD2PEnter());
//        System.out.println(IndoorSpace.iDoors.get(426).getD2PLeave());
//        System.out.println(IndoorSpace.iFloors.get(1).getmDoors());
//
//        for (int i = 0; i < IndoorSpace.iObject.size(); i++) {
//            IndoorObject ob = IndoorSpace.iObject.get(i);
//            System.out.println("objectID: " + ob.getObjectId() + ", x: " + ob.getObjectX() + ", y: " + ob.getObjectY() + ", floor: " + ob.getoFloor() + ", parId: " + ob.getParId());
//        }

    }

    public int getPointHostPartition(Point point) {
        int partitionId = -1;
        int floor = point.getmFloor();
        ArrayList<Integer> pars = IndoorSpace.iFloors.get(floor).getmPartitions();
//        System.out.println("mPartitions: " + pars);
        for (int i = 0; i < pars.size(); i++) {
            Partition par = IndoorSpace.iPartitions.get(pars.get(i));
            if (point.getX() >= par.getX1() && point.getX() <= par.getX2() && point.getY() >= par.getY1() && point.getY() <= par.getY2()) {
                partitionId = par.getmID();
                if (IndoorSpace.iPartitions.get(partitionId).getmType() == RoomType.HALLWAY) continue;
                return partitionId;
            }
        }
        return partitionId;
    }
}
