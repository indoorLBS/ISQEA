package dk.aau.cs.indoorqueries.common.algorithm;

import dk.aau.cs.indoorqueries.common.indoorEntitity.Door;
import dk.aau.cs.indoorqueries.common.indoorEntitity.IndoorSpace;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Partition;
import dk.aau.cs.indoorqueries.common.utilities.Constant;
import dk.aau.cs.indoorqueries.common.utilities.Functions;

import java.util.ArrayList;
import java.util.Arrays;

public class Test {
    public static ArrayList<ArrayList<Double>> kObjects = new ArrayList<>();

    public static void main(String args[]) {
//        System.out.println(2 * 0.1);
//        System.out.println((2 + 1) * 0.1 - 0.1);
//        if ((2 * 0.1) != ((2 + 1) * 0.1 - 0.1)) {
//            System.out.println("something wrong");
//        }

//        System.out.println(min(0.2, 0.4, 1.3));
//        ArrayList<Integer> l1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
//
//
//        for (int i = 0; i < l1.size(); i++) {
//            for (int j = i + 1; j < l1.size(); j++) {
//                System.out.println(l1.get(i) + "-" + l1.get(j));
//            }
//        }
//        HashMap<String, String> hm = new HashMap<>();
//        hm.put("1", "abc");
//        System.out.println(hm.get("2"));


//        ArrayList<Integer> doors = new ArrayList<>(Arrays.asList(1014, 1021, 1014, 1021, 1056, 1057));
//        for (int j = 0; j < doors.size(); j++) {
//            System.out.println("j = " + doors.get(j));
//            for (int k = j + 1; k < doors.size(); k++) {
//                System.out.println("k = " + doors.get(k));
//                if (doors.get(j) == doors.get(k)) {
//                    System.out.println("equal");
//                    doors.remove(k);
//                    k--;
//                }
//            }
//            System.out.println();
//        }
//        System.out.println(doors);
//
//        if ((int)doors.get(1) == (int)doors.get(3)) {
//            System.out.println("yes");
//        }

//        String distPath = "123.1 1 2 3 4 5";
//        Test dk.aau.cs.indoorqueries.test = new Test();
//        System.out.println(dk.aau.cs.indoorqueries.test.convertPath(distPath));


//        DataGen dataGen = new DataGen();
//        dataGen.genAllData();
//
//        GenTopology genTopology = new GenTopology();
//        genTopology.genTopology();
//
//        System.out.println(IndoorSpace.iDoors.get(210).getmPartitions());
//        System.out.println(IndoorSpace.iPartitions.get(137).getTopology().getP2DLeave());
//
//        Test dk.aau.cs.indoorqueries.test = new Test();
//        System.out.println("result: " + dk.aau.cs.indoorqueries.test.d2dDist(44, 210));

        Test test = new Test();
        String[] arr = {"a", "1", "c"};
        System.out.println(Functions.arrayToString1D(arr, 1, 2));


    }

    public String convertPath(String distPath) {
        String result = "";
        String[] distPathArr = distPath.split(" ");
        String[] resultArr = new String[distPathArr.length];
        for (int i = 1; i < distPathArr.length; i++) {
            resultArr[distPathArr.length - i] = distPathArr[i];
        }
        resultArr[0] = distPathArr[0];
        result = arrToString(resultArr, 0, resultArr.length - 1);
        return result;
    }

    public String arrToString(String[] arr, int start, int end) {
        String result = "";
        for (int i = start; i <= end; i++) {
            result += arr[i] + " ";
        }
        return result;
    }

    /**
     * calculate the indoor distance between two doors
     *
     * @param ds door id ds
     * @param de door id de
     * @return distance
     */
    public String d2dDist(int ds, int de) {
//		System.out.println("ds = " + IndoorSpace.iDoors.get(ds).toString() + " de = " + IndoorSpace.iDoors.get(de).toString());
        String result = "";

        if (ds == de) return 0 + "\t";

        int size = IndoorSpace.iDoors.size();
        BinaryHeap<Double> H = new BinaryHeap<Double>(size);
        double[] dist = new double[size];        //stores the current shortest path distance
        // from source ds to a door de
        Test.PrevPair[] prev = new Test.PrevPair[size];        //stores the corresponding previous partition
        // and door pair (v,di) through which the dk.aau.cs.indoorqueries.algorithm
        // visits the current door de.
        boolean[] visited = new boolean[size];        // mark door as visited

        for (int i = 0; i < size; i++) {
            int doorID = IndoorSpace.iDoors.get(i).getmID();
            if (doorID != i) System.out.println("something wrong_Helper_d2dDist");
            if (doorID != ds) dist[i] = Constant.large;
            else dist[i] = 0;

            // enheap
            H.insert(dist[i], doorID);

            Test.PrevPair pair = null;
            prev[doorID] = pair;
        }

        while (H.heapSize > 0) {
            String[] str = H.delete_min().split(",");
            int di = Integer.parseInt(str[1]);
            double dist_di = Double.parseDouble(str[0]);

//			System.out.println("dequeue <" + di + ", " + dist_di + ">");

            if (di == de) {
//				System.out.println("d2dDist_ di = " + di + " de = " + de);
                result += getPath(prev, ds, de);
                return result = dist_di + "\t" + result;
            }

            visited[di] = true;
//			System.out.println("d" + di + " is newly visited");

            Door door = IndoorSpace.iDoors.get(di);
            ArrayList<Integer> parts = new ArrayList<Integer>();        // list of leavable partitions
            parts = door.getD2PEnter();

            int partSize = parts.size();

            for (int i = 0; i < partSize; i++) {
                ArrayList<Integer> doorTemp = new ArrayList<Integer>();
                int v = parts.get(i);        // partition id
                Partition partition = IndoorSpace.iPartitions.get(v);
                doorTemp = partition.getmDoors();

                // remove the visited doors
                ArrayList<Integer> doors = new ArrayList<Integer>();        // list of unvisited leavable doors
                int doorTempSize = doorTemp.size();
                for (int j = 0; j < doorTempSize; j++) {
                    int index = doorTemp.get(j);
//					System.out.println("index = " + index + " " + !visited[index]);
                    if (!visited[index]) {
                        doors.add(index);
                    }
                }

                int doorSize = doors.size();
//				System.out.println("doorSize = " + doorSize + ": " + Functions.printIntegerList(doors));

                for (int j = 0; j < doorSize; j++) {
                    int dj = doors.get(j);
                    if (visited[dj]) System.out.println("something wrong_Helper_d2dDist2");
//					System.out.println("for d" + di + " and d" + dj);

                    double fd2d = partition.getdistMatrix().getDistance(di, dj);
                    ;

                    if ((dist[di] + fd2d) < dist[dj]) {
                        double oldDj = dist[dj];
                        dist[dj] = dist[di] + fd2d;
                        H.updateNode(oldDj, dj, dist[dj], dj);
                        prev[dj] = new Test.PrevPair(v, di);
                        prev[dj].toString();
                    }
                }
            }
        }
        return result;
    }

    public class PrevPair {
        public int par;
        public int door;

        public PrevPair(int par, int door) {
            this.par = par;
            this.door = door;
        }

        public String toString() {
            return par + ", " + door;
        }

    }


    /**
     * @param prev
     * @return a string path
     */
    public static String getPath(Test.PrevPair[] prev, int ds, int de) {
        String result = de + "";

        System.out.println("ds = " + ds + " de = " + de + " " + prev[de].par + " " + prev[de].door);
        int currp = prev[de].par;
        int currd = prev[de].door;

        while (currd != ds) {
            result = currd + "\t" + result;
            System.out.println("current: " + currp + ", " + currd + " next: " + prev[currd].toString());
            currp = prev[currd].par;
            currd = prev[currd].door;

        }

        result = currd + "\t" + result;

        return result;
    }

    public void addToArrayListSorted(ArrayList<Double> obDist, ArrayList<ArrayList<Double>> obDists) {
        if (obDists.size() == 0) {
            obDists.add(obDist);
        } else {
            double dist = obDist.get(1);
            for (int i = obDists.size() - 1; i >= 0; i--) {
                double tempDist = obDists.get(i).get(1);
                double tempObjectId = (int) ((double) obDists.get(i).get(0));

                if (dist > tempDist) {
                    if (i + 1 == obDists.size()) {
                        obDists.add(obDist);
                        break;
                    } else {
                        ArrayList<Double> lastItem = obDists.get(obDists.size() - 1);
                        obDists.add(lastItem);
                        for (int j = obDists.size() - 2; j > i + 1; j--) {
                            ArrayList<Double> tempItem = obDists.get(j - 1);
                            obDists.set(j, tempItem);
                        }
                        obDists.set(i + 1, obDist);
                        break;
                    }
                }
                if (i == 0) {
                    ArrayList<Double> lastItem = obDists.get(obDists.size() - 1);
                    obDists.add(lastItem);
                    for (int j = obDists.size() - 2; j > 0; j--) {
                        ArrayList<Double> tempItem = obDists.get(j - 1);
                        obDists.set(j, tempItem);
                    }
                    obDists.set(0, obDist);
                    break;
                }
            }
        }
    }

    public void addObToKlist(int objectId, double dist, int k) {
        if (kObjects.size() == 0) {
            kObjects.add(new ArrayList<>(Arrays.asList((double) objectId, dist)));
            return;
        }

        for (int i = 0; i < kObjects.size(); i++) {
            if ((int) (double) kObjects.get(i).get(0) == objectId) {
                if (dist < (double) kObjects.get(i).get(1)) {
                    kObjects.remove(i);
                    i--;
                } else {
                    return;
                }
            }
        }


        if (kObjects.size() < k) {
            for (int i = kObjects.size() - 1; i >= 0; i--) {
                double tempDist = kObjects.get(i).get(1);
                double tempObjectId = (int) ((double) kObjects.get(i).get(0));
                if (objectId == tempObjectId) return;
                if (dist < tempDist) {
                    if (i + 1 == kObjects.size()) {
                        kObjects.add(new ArrayList<>(Arrays.asList((double) objectId, dist)));
                        break;
                    } else {
                        ArrayList<Double> lastItem = kObjects.get(kObjects.size() - 1);
                        kObjects.add(lastItem);
                        for (int j = kObjects.size() - 2; j > i + 1; j--) {
                            ArrayList<Double> tempItem = kObjects.get(j - 1);
                            kObjects.set(j, tempItem);
                        }
                        kObjects.set(i + 1, new ArrayList<>(Arrays.asList((double) objectId, dist)));
                        break;
                    }
                }
                if (i == 0) {
                    ArrayList<Double> lastItem = kObjects.get(kObjects.size() - 1);
                    kObjects.add(lastItem);
                    for (int j = kObjects.size() - 2; j > 0; j--) {
                        ArrayList<Double> tempItem = kObjects.get(j - 1);
                        kObjects.set(j, tempItem);
                    }
                    kObjects.set(0, new ArrayList<>(Arrays.asList((double) objectId, dist)));
                    break;
                }
            }
        }

        if (kObjects.size() == k) {
            for (int i = kObjects.size() - 1; i >= 0; i--) {
                double tempDist = kObjects.get(i).get(1);
                int tempObjectId = (int) ((double) kObjects.get(i).get(0));
                if (objectId == tempObjectId) return;
                if (dist <= tempDist) {
                    kObjects.set(i, new ArrayList<>(Arrays.asList((double) objectId, dist)));
                    objectId = tempObjectId;
                    dist = tempDist;
                }
            }
        }

        if (kObjects.size() > k) {
            System.out.println("something wrong with the kBound");
        }
    }


}
