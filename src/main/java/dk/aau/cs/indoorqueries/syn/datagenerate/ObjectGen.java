package dk.aau.cs.indoorqueries.syn.datagenerate;

import dk.aau.cs.indoorqueries.common.iDModel.GenTopology;
import dk.aau.cs.indoorqueries.common.indoorEntitity.IndoorObject;
import dk.aau.cs.indoorqueries.common.indoorEntitity.IndoorSpace;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Partition;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Point;
import dk.aau.cs.indoorqueries.common.utilities.DataGenConstant;
import dk.aau.cs.indoorqueries.common.utilities.RoomType;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * generate objects
 *
 * @author Tiantian Liu
 */

public class ObjectGen {
    private static String objectsFile = System.getProperty("user.dir") + "/objects_";

    /**
     * generate all objects
     *
     * @param number: number of objects
     */
    public void genAllObject(int number) throws IOException {
        int num = 0;
        while (num < number) {
            int x = (int) (Math.random() * (1368 + 1));
            int y = (int) (Math.random() * (1368 + 1));
            int floor = (int) (Math.random() * (DataGenConstant.nFloor));
            if (isLegal(x, y, floor) && !isExist(x, y, floor)) {
                int parId = getPointHostPartition(new Point(x, y, floor));
                if (IndoorSpace.iPartitions.get(parId).getmType() != RoomType.HALLWAY) {
                    IndoorObject ob = new IndoorObject(x, y, floor);
                    ob.findParIdSYN(ob);
                    IndoorSpace.iObject.add(ob);
                    num++;
                }
            }
        }
        System.out.println("Objects are generated, number of objects is " + IndoorSpace.iObject.size());
        saveObjects(number);
        System.out.println("objects have been saved");
    }

    public void readObjects(int number) throws IOException {
        Path path = Paths.get(objectsFile + 2500 + "_dataType_" + 1 + "_diType_" + DataGenConstant.divisionType + ".txt");
        Scanner scanner = new Scanner(path);
        int n = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] tempArr = line.split("\t");
            int objectId = Integer.parseInt(tempArr[0]);
            int x = Integer.parseInt(tempArr[1]);
            int y = Integer.parseInt(tempArr[2]);
            int floor = Integer.parseInt(tempArr[3]);
            int parId = Integer.parseInt(tempArr[4]);
            IndoorObject ob = new IndoorObject(objectId, x, y, floor, parId);
            IndoorSpace.iObject.add(ob);
            n++;
            Partition par = IndoorSpace.iPartitions.get(parId);
            par.addObject(objectId);

            if (n >= number) {
                break;
            }
        }
    }

    public void readObjectsfordivide(int number) throws IOException {
        Path path = Paths.get(objectsFile + 2500 + "_dataType_" + 1 + "_diType_" + 1 + ".txt");
        Scanner scanner = new Scanner(path);
        int n = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] tempArr = line.split("\t");
            int objectId = Integer.parseInt(tempArr[0]);
            int x = Integer.parseInt(tempArr[1]);
            int y = Integer.parseInt(tempArr[2]);
            int floor = Integer.parseInt(tempArr[3]);
            int parId = getPointHostPartition(new Point(x, y, floor));
            IndoorObject ob = new IndoorObject(objectId, x, y, floor, parId);
            IndoorSpace.iObject.add(ob);
            n++;
            Partition par = IndoorSpace.iPartitions.get(parId);
            par.addObject(objectId);

            if (n >= number) {
                break;
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
    public boolean isLegal(int x, int y, int floor) {
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

    /**
     * check whether
     *
     * @param x
     * @param y
     * @param floor
     */
    public boolean isExist(int x, int y, int floor) {
        for (int i = 0; i < IndoorSpace.iObject.size(); i++) {
            IndoorObject ob = IndoorSpace.iObject.get(i);
            if (x == ob.getObjectX() && y == ob.getObjectY() && floor == ob.getoFloor()) {
                return true;
            }
        }

        return false;
    }

    /**
     * save objects in .txt file
     */
    public void saveObjects(int number) throws IOException {
        String result = "";
        for (int i = 0; i < IndoorSpace.iObject.size(); i++) {
            IndoorObject ob = IndoorSpace.iObject.get(i);
            result += ob.getObjectId() + "\t" + ob.getObjectX() + "\t" + ob.getObjectY() + "\t" + ob.getoFloor() + "\t" + ob.getParId() + "\n";
        }
        try {
            FileWriter fw = new FileWriter(objectsFile + number + "_dataType_" + DataGenConstant.dataType + "_diType_" + DataGenConstant.divisionType + ".txt");
            fw.write(result);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
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

    public static void main(String args[]) throws IOException {
        dk.aau.cs.indoorqueries.syn.datagenerate.DataGen dataGen = new dk.aau.cs.indoorqueries.syn.datagenerate.DataGen();
        dataGen.genAllData(DataGenConstant.dataType, DataGenConstant.divisionType);

        GenTopology genTopology = new GenTopology();
        genTopology.genTopology();

        ClassifyPartition classifyPartition = new ClassifyPartition();
        classifyPartition.classifyPar();

//        System.out.println(IndoorSpace.iCrucialPartitions.size());
        ObjectGen objectGen = new ObjectGen();
//        objectGen.genAllObject(2500);
        objectGen.readObjectsfordivide(2500);
        objectGen.saveObjects(2500);

    }
}
