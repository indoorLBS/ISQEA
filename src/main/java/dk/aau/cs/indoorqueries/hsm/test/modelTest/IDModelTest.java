package dk.aau.cs.indoorqueries.hsm.test.modelTest;

import dk.aau.cs.indoorqueries.common.iDModel.GenTopology;
import dk.aau.cs.indoorqueries.hsm.datagenerate.HSMDataGenRead;

import java.io.IOException;

/**
 * test IDModel
 *
 * @Author Tiantian Liu
 */
public class IDModelTest {
    public static void main(String arg[]) throws IOException {
//        DataGen dataGen = new DataGen();
//        dataGen.genAllData();
        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        long startMem = runtime.totalMemory() - runtime.freeMemory();

        long start = System.nanoTime();

        HSMDataGenRead dateGenReadMen = new HSMDataGenRead();
        dateGenReadMen.dataGen();

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
//        }
//
//        Point ps = new Point(2, 12, 0);
//        Point pt = new Point(54, 18, 0);
//
//        IDModelTest idModelTest = new IDModelTest();
//
//        System.out.println(idModelTest.getPointHostPartition(ps));


//        ObjectGen objectGen = new ObjectGen();
//        objectGen.readObjects(100);
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

//    public int getPointHostPartition(Point point) {
//        int partitionId = -1;
//        int floor = point.getmFloor();
//        ArrayList<Integer> pars = IndoorSpace.iFloors.get(floor).getmPartitions();
//        System.out.println("mPartitions: " + pars);
//        for (int i = 0; i < pars.size(); i++) {
//            Partition par = IndoorSpace.iPartitions.get(pars.get(i));
//            if (point.getX() >= par.getX1() && point.getX() <= par.getX2() && point.getY() >= par.getY1() && point.getY() <= par.getY2()) {
//                partitionId = par.getmID();
//                if (IndoorSpace.iPartitions.get(partitionId).getmType() == RoomType.HALLWAY) continue;
//                return partitionId;
//            }
//        }
//        return partitionId;
//    }
}
