package dk.aau.cs.indoorqueries.common.iDMatrix;

import dk.aau.cs.indoorqueries.common.algorithm.BinaryHeap;
import dk.aau.cs.indoorqueries.common.iDModel.GenTopology;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Door;
import dk.aau.cs.indoorqueries.common.indoorEntitity.IndoorSpace;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Partition;
import dk.aau.cs.indoorqueries.common.utilities.Constant;
import dk.aau.cs.indoorqueries.common.utilities.DataGenConstant;
import dk.aau.cs.indoorqueries.common.utilities.Functions;
import dk.aau.cs.indoorqueries.syn.datagenerate.DataGen;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

/**
 * generate door to door distance matrix
 *
 * @author Tiantian Liu
 */
public class DistMatrixGen {
    private static String d2dDistFile = System.getProperty("user.dir") + "/distanceMatrix/" + DataGenConstant.dataset + "/d2dDistMatrix_floor_" + DataGenConstant.nFloor + "_dataType_" + DataGenConstant.dataType + "_diType_" + DataGenConstant.divisionType + ".txt";
    public static HashMap<String, Double> d2dDistMap = new HashMap<String, Double>();        // the distance between two doors
    public static HashMap<String, String> d2dRouteMap = new HashMap<String, String>();

    /**
     * read distance matrix
     * @throws IOException
     */
    public void readDist() throws IOException {
        Path path = Paths.get(d2dDistFile);
        Scanner scanner = new Scanner(path);

        //read line by line

        while (scanner.hasNextLine()) {
            //process each line
            String line = scanner.nextLine();
            String[] tempArr = line.split("\t");
//            System.out.println(h);
            d2dDistMap.put(tempArr[0], Double.parseDouble(tempArr[1]));
            d2dRouteMap.put(tempArr[0], Functions.arrayToString1D(tempArr, 2, tempArr.length));
        }
        System.out.println("d2dDistMap and d2dRouteMap is loaded");

    }

    /**
     * generate door to door distance matrix
     */
    public void d2dDistMatrixGen() {
        String result = "";
        for (int i = 0; i < IndoorSpace.iDoors.size(); i++) {
//            if (i <= 214 && i >= 212 || i == 210) continue;
            for (int j = 0; j < IndoorSpace.iDoors.size(); j++) {
                System.out.println("door1: " + i + ", door2: " + j);
                Door door1 = IndoorSpace.iDoors.get(i);
                Door door2 = IndoorSpace.iDoors.get(j);

//                if (j <= 214 && j >= 212 || j == 210) continue;


                result += i + "-" + j + "\t" + d2dDistance(door1, door2);
//                result += j+ "-" + i + "\t" + d2dDistance(door2, door1);
            }

            if ((i + 1) % 200 == 0) {
                String outputFile = System.getProperty("user.dir") + "/d2dDistMatrix_diType_" + DataGenConstant.divisionType + "_" + (i - 199) + "to" + i + ".txt";
                try {
                    FileWriter fw = new FileWriter(outputFile);
                    fw.write(result);
                    fw.flush();
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
                result = "";
                System.out.println("200 Door is finished");

            }
        }
        String outputFile = System.getProperty("user.dir") + "/d2dDistMatrix_diType_" + DataGenConstant.divisionType + "_last" + ".txt";
        try {
            FileWriter fw = new FileWriter(outputFile);
            fw.write(result);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        System.out.println("all is finished");
    }

    /**
     * calculate the distance between two doors
     * @param ds
     * @param dt
     * @return
     */
    public String d2dDistance(Door ds, Door dt) {
        double minDist = Constant.large;
        int dsId = ds.getmID();
        int dtId = dt.getmID();

        if (dsId == dtId) {
            return 0 + "\t" + dsId + "\n";
        }
        double[] dist = new double[IndoorSpace.iDoors.size()];
        ArrayList<ArrayList<Integer>> prev = new ArrayList<>();
        int[] isDoorVisited = new int[IndoorSpace.iDoors.size()];

        BinaryHeap H = new BinaryHeap(IndoorSpace.iDoors.size());

        for (int i = 0; i < IndoorSpace.iDoors.size(); i++) {
            if (i != dsId) {
                dist[i] = Constant.large;
            } else {
                dist[i] = 0;
            }
            H.insert(dist[i], i);
            prev.add(null);
        }

        while (H.heapSize > 0) {
            String minElement = H.delete_min();
            String[] minElementArr = minElement.split(",");
            int curDoorId = Integer.parseInt(minElementArr[1]);
            if (Double.parseDouble(minElementArr[0]) != dist[curDoorId]) {
                System.out.println("Something wrong with min heap: dk.aau.cs.indoorqueries.algorithm.IDModel_SPQ.pt2ptDistance3");
            }

            if (curDoorId == dtId) {
                System.out.println(getPath(prev, dsId, dtId));
                return dist[curDoorId] + "\t" + getPath(prev, dsId, dtId) + "\n";
            }

            if (dist[curDoorId] >= Constant.large) return minDist + "" + "\n";

            isDoorVisited[curDoorId] = 1;
            Door curDoor = IndoorSpace.iDoors.get(curDoorId);

            ArrayList<Integer> parts = curDoor.getmPartitions();
//            System.out.println("parts: " + parts);
            for (int j = 0; j < parts.size(); j++) {
                int nextParId = parts.get(j);
                Partition nextPar = IndoorSpace.iPartitions.get(nextParId);
                ArrayList<Integer> leaveDoors = nextPar.getmDoors();
//                System.out.println("leaveDoor: " + leaveDoors);
                for (int k = 0; k < leaveDoors.size(); k++) {
                    int leaveDoorId = leaveDoors.get(k);
//                    System.out.println("leaveDoorId: " + leaveDoorId);
                    if (isDoorVisited[leaveDoorId] != 1) {
//                        System.out.println("first if");
                        if (dist[curDoorId] + nextPar.getdistMatrix().getDistance(curDoorId, leaveDoorId) < dist[leaveDoorId]) {
//                            System.out.println("second if");
                            double oldDist = dist[leaveDoorId];
                            dist[leaveDoorId] = dist[curDoorId] + nextPar.getdistMatrix().getDistance(curDoorId, leaveDoorId);
//                            System.out.println("dist of leaveDoorId: " + dist[leaveDoorId]);
                            prev.set(leaveDoorId, new ArrayList<>(Arrays.asList(nextParId, curDoorId)));
                            H.updateNode(oldDist, leaveDoorId, dist[leaveDoorId], leaveDoorId);
//                            System.out.println("prev: " + prev.get(leaveDoorId));
                        }

                    }
                }
            }


        }
        return minDist + "" + "\n";
    }

    /**
     * @param prev
     * @param ds
     * @param de
     * @return a string path
     */
    public static String getPath(ArrayList<ArrayList<Integer>> prev, int ds, int de) {
        String result = de + "";

//		System.out.println("ds = " + ds + " de = " + de + " " + prev[de].par + " " + prev[de].door);
        int currp = prev.get(de).get(0);
        int currd = prev.get(de).get(1);

        while (currd != ds) {
            result = currd + "\t" + result;
//			System.out.println("current: " + currp + ", " + currd + " next: " + prev[currd].toString());
            currp = prev.get(currd).get(0);
            currd = prev.get(currd).get(1);

        }

        result = currd + "\t" + result;

        return result;
    }

    /**
     * get door to door distance map
     * @return
     */
    public HashMap<String, Double> getD2dDistMap() {
        return d2dDistMap;
    }

    /**
     * get door to door route map
     * @return
     */
    public HashMap<String, String> getD2dRouteMap() {
        return d2dRouteMap;
    }


    public static void main(String arg[]) throws IOException {
        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        long startMem = runtime.totalMemory() - runtime.freeMemory();
        long start = System.nanoTime();

        DataGen dataGen = new DataGen();
        dataGen.genAllData(DataGenConstant.dataType, DataGenConstant.divisionType);

        GenTopology genTopology = new GenTopology();
        genTopology.genTopology();

        DistMatrixGen distMatrixGen = new DistMatrixGen();
        distMatrixGen.d2dDistMatrixGen();

        long end = System.nanoTime();
        long endMem = runtime.totalMemory() - runtime.freeMemory();

        double time = (double) (end - start) / 1000 / 1000 / 1000 / 60;
        double memory = (double) (endMem - startMem) / 1024 / 1024;
        System.out.println("time: " + time + " min");
        System.out.println("memory: " + memory + " mb");
    }
}
