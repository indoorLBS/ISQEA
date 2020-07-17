package dk.aau.cs.indoorqueries.hsm.datagenerate;

import dk.aau.cs.indoorqueries.common.iDModel.GenTopology;
import dk.aau.cs.indoorqueries.common.indoorEntitity.IndoorObject;
import dk.aau.cs.indoorqueries.common.indoorEntitity.IndoorSpace;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Partition;
import dk.aau.cs.indoorqueries.common.utilities.DataGenConstant;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * generate objects
 *
 * @author Tiantian Liu
 */

public class ObjectGen {
    private static String objectsFile = System.getProperty("user.dir") + "/objects/HSM/objects_";

    /**
     * generate all objects
     *
     * @param number: number of objects
     */
    public void genAllObject(int number) throws IOException {
        int num = 0;
        while (num < number) {
//            System.out.println("num: " + num);
            int x = (int) (Math.random() * (2000));
            int y = (int) (Math.random() * (2000));
            int floor = (int) (Math.random() * (DataGenConstant.nFloor));
//            System.out.println("x: " + x + ", y: " + y + ", floor: " +floor);
            if (!isExist(x, y, floor)) {
                IndoorObject ob = new IndoorObject(x, y, floor);
                int parId = ob.findParId(ob);
                if (parId == -1) {
//                    System.out.println("parId = -1");
                    DataGenConstant.mID_Object--;
                    continue;
                }

                IndoorSpace.iObject.add(ob);
                num++;
            }
        }
        System.out.println("Objects are generated, number of objects is " + IndoorSpace.iObject.size());
        saveObjects(number);
        System.out.println("objects have been saved");
    }

    /**
     * read objects
     * @param number
     * @throws IOException
     */
    public void readObjects(int number) throws IOException {
        Path path = Paths.get(objectsFile + 2500 + ".txt");
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
            FileWriter fw = new FileWriter(objectsFile + number + ".txt");
            fw.write(result);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    public static void main(String args[]) throws IOException{
        HSMDataGenRead dateGenReadMen = new HSMDataGenRead();
        dateGenReadMen.dataGen();

        GenTopology genTopology = new GenTopology();
        genTopology.genTopology();

        ObjectGen ObjectGen = new ObjectGen();
//        ObjectGen.genAllObject(2500);
        ObjectGen.readObjects(250);
        for (int i = 0; i < IndoorSpace.iObject.size(); i++) {
            System.out.println(IndoorSpace.iObject.get(i).getObjectX() + "-" +IndoorSpace.iObject.get(i).getObjectY());
        }

    }
}
