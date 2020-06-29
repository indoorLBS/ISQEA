package dk.aau.cs.indoorqueries.syn.test.modelTest;

import dk.aau.cs.indoorqueries.common.iDModel.GenTopology;
import dk.aau.cs.indoorqueries.common.utilities.DataGenConstant;
import dk.aau.cs.indoorqueries.syn.datagenerate.DataGen;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * dk.aau.cs.indoorqueries.test IDMatrix
 *
 * @author Tiantian Liu
 */

public class IDMatrixTest {
    public static void main(String arg[]) throws IOException {
        DataGen dataGen = new DataGen();
        dataGen.genAllData(DataGenConstant.dataType, DataGenConstant.divisionType);

        GenTopology genTopology = new GenTopology();
        genTopology.genTopology();

//        DistMatrixGen distMatrixGen = new DistMatrixGen();
//        distMatrixGen.d2dDistMatrixGen();

        String d2dDistFile = System.getProperty("user.dir") + "/d2dDistMatrix_floor_" + DataGenConstant.nFloor + ".txt";
        String output = System.getProperty("user.dir") + "/d2dDistMatrix_floor_" + 3 + ".txt";


        Path path = Paths.get(d2dDistFile);
        Scanner scanner = new Scanner(path);
        String result = "";

        //read line by line

        while (scanner.hasNextLine()) {
            //process each line
            String line = scanner.nextLine();
            String[] tempArr = line.split("\t");
//            System.out.println(h);
            String[] keyArr = tempArr[0].split("-");
            int doorId1 = Integer.parseInt(keyArr[0]);
            int doorId2 = Integer.parseInt(keyArr[1]);

            if (doorId1 < 648 && doorId2 < 648) {
                result += line + "\n";
                System.out.println(line);
            }
        }

        try {
            FileWriter fw = new FileWriter(output);
            fw.write(result);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        System.out.println("d2dDistMatrix for 3-floor is finished");


//        ArrayList<Integer> arr = new ArrayList<>(Arrays.asList(210, 212, 213, 214));
//
//        String result = "";
//        for (int i = 0; i < IndoorSpace.iDoors.size(); i++) {
////            if (i <= 214 && i >= 212 || i == 210) continue;
//            for (int j = 0; j < arr.size(); j++) {
//                System.out.println("door1: " + i + ", door2: " + arr.get(j));
//                Door door1 = IndoorSpace.iDoors.get(i);
//                Door door2 = IndoorSpace.iDoors.get(arr.get(j));
//
////                if (j <= 214 && j >= 212 || j == 210) continue;
//
//
//                result += i + "-" + arr.get(j) + "\t" + distMatrixGen.d2dDistance(door1, door2);
//
//                result += arr.get(j) + "-" + i + "\t" + distMatrixGen.d2dDistance(door2, door1);
//            }
//
//        }
//        String outputFile = System.getProperty("user.dir") + "/d2dDistMatrix" + "_left" + ".txt";
//        try {
//            FileWriter fw = new FileWriter(outputFile);
//            fw.write(result);
//            fw.flush();
//            fw.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//            return;
//        }
//        System.out.println("all is finished");


//        String dist = distMatrixGen.d2dDistance(IndoorSpace.iDoors.get(0), IndoorSpace.iDoors.get(212));
//        System.out.println(dist);

//        IndexMatrixGen indexMatrixGen = new IndexMatrixGen();
//        indexMatrixGen.indexMatrixGen();
//        ArrayList<ArrayList<ArrayList<Double>>> list = IndexMatrixGen.indexList;
//        System.out.println("list size: " + list.size());
//        for (int i = 0; i < list.size(); i++) {
//            System.out.println();
//            System.out.println("door: " + i);
//            System.out.println("size: " + list.get(i).size());
//            for (int j = 0; j < list.get(i).size(); j++) {
//                System.out.println(j + " " + list.get(i).get(j));
//            }
//        }
//        System.out.println(IndexMatrixGen.indexList);


    }
}
