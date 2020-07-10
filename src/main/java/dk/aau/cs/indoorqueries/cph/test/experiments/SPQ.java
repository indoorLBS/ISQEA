package dk.aau.cs.indoorqueries.cph.test.experiments;

import com.github.davidmoten.rtree3d.Entry;
import com.github.davidmoten.rtree3d.RTree;
import com.github.davidmoten.rtree3d.geometry.Box;
import dk.aau.cs.indoorqueries.common.algorithm.*;
import dk.aau.cs.indoorqueries.common.iDMatrix.DistMatrixGen;
import dk.aau.cs.indoorqueries.common.iDModel.GenTopology;
import dk.aau.cs.indoorqueries.common.iPTree.VIPTree;
import dk.aau.cs.indoorqueries.common.indoorEntitity.IndoorSpace;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Partition;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Point;
import dk.aau.cs.indoorqueries.common.rStarTree3D.TreeNode;
import dk.aau.cs.indoorqueries.common.utilities.DataGenConstant;
import dk.aau.cs.indoorqueries.cph.datagenerate.CPHDataGenRead;
import dk.aau.cs.indoorqueries.cph.datagenerate.ClassifyPartition;
import rx.Observable;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

//import datagenerate.DataGen;

public class SPQ {
    public static double distDefault = 1500;

    public void IDModel_SPQ(String parameter, String fileName) throws IOException {
        CPHDataGenRead dateGenReadMen = new CPHDataGenRead();
        dateGenReadMen.dataGen();

        GenTopology genTopology = new GenTopology();
        genTopology.genTopology();

        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        long startMem = runtime.totalMemory() - runtime.freeMemory();

        String outFile = System.getProperty("user.dir") + fileName;

        String result = "";
        if (parameter == "distance") {
            result += "IDModel_SPQ" + "\n";
            result += "distance" + "\t" + "time(ns)" + "\t" + "memory(kb)" + "\t" + "visitDoors" + "\n";
            for (int i = 0; i < 1; i++) {
                double distance = 1700;
                ArrayList<ArrayList<Point>> pointPairs = getPointPairs(distance);

                long resultTimeSum2 = 0;
                long resultMenSum2 = 0;
                int resultVisitDoorsSum2 = 0;
                long resultTimeAve2 = 0;
                long resultMenAve2 = 0;
                int resultVisitDoorsAve2 = 0;

                for (int j = 0; j < 10; j++) {
                    ArrayList<Point> pointPair = pointPairs.get(j);
                    IDModel_SPQ idModel_spq = new IDModel_SPQ();
                    long resultTimeSum1 = 0;
                    long resultMenSum1 = 0;
                    int resultVisitDoorsSum1 = 0;
                    long resultTimeAve1 = 0;
                    long resultMenAve1 = 0;
                    int resultVisitDoorsAve1 = 0;

                    idModel_spq.pt2ptDistance3(pointPair.get(0), pointPair.get(1));

                    for (int k = 0; k < 10; k++) {

                        long start = System.nanoTime();

                        int visitDoors = Integer.parseInt(idModel_spq.pt2ptDistance3(pointPair.get(0), pointPair.get(1)).split("\t")[1]);
                        long end = System.nanoTime();
                        long endMem = runtime.totalMemory() - runtime.freeMemory();

                        long time = (end - start) / 1000;
                        long memory = (endMem - startMem) / 1024 / 1000;

                        resultTimeSum1 += time;
                        resultMenSum1 += memory;
                        resultVisitDoorsSum1 += visitDoors;
                    }
                    resultTimeAve1 = resultTimeSum1 / 10;
                    resultMenAve1 = resultMenSum1 / 10;
                    resultVisitDoorsAve1 = resultVisitDoorsSum1 / 10;

                    resultTimeSum2 += resultTimeAve1;
                    resultMenSum2 += resultMenAve1;
                    resultVisitDoorsSum2 += resultVisitDoorsAve1;
                }

                resultTimeAve2 = resultTimeSum2 / 10;
                resultMenAve2 = resultMenSum2 / 10;
                resultVisitDoorsAve2 = resultVisitDoorsSum2 / 10;
                result += distance + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\t" + resultVisitDoorsAve2 + "\n";
            }
        }

        if (parameter == "diType") {
            result += "IDModel_SPQ" + "\n";
            result += parameter + "\t" + "time(ns)" + "\t" + "memory" + "\t" + "visitDoors" + "\n";
            ArrayList<ArrayList<Point>> pointPairs = getPointPairs(1500);

            long resultTimeSum2 = 0;
            long resultMenSum2 = 0;
            int resultVisitDoorsSum2 = 0;
            long resultTimeAve2 = 0;
            long resultMenAve2 = 0;
            int resultVisitDoorsAve2 = 0;

            for (int j = 0; j < 10; j++) {
                ArrayList<Point> pointPair = pointPairs.get(j);
                IDModel_SPQ idModel_spq = new IDModel_SPQ();
                long resultTimeSum1 = 0;
                long resultMenSum1 = 0;
                int resultVisitDoorsSum1 = 0;
                long resultTimeAve1 = 0;
                long resultMenAve1 = 0;
                int resultVisitDoorsAve1 = 0;
                idModel_spq.pt2ptDistance3(pointPair.get(0), pointPair.get(1));
                for (int k = 0; k < 10; k++) {
//                    Runtime runtime = Runtime.getRuntime();
//                    runtime.gc();
//                    long startMem = runtime.totalMemory() - runtime.freeMemory();
                    long start = System.nanoTime();

                    int visitDoors = Integer.parseInt(idModel_spq.pt2ptDistance3(pointPair.get(0), pointPair.get(1)).split("\t")[1]);
                    long end = System.nanoTime();
                    long endMem = runtime.totalMemory() - runtime.freeMemory();

                    long time = (end - start) / 1000;
                    long memory = (endMem - startMem) / 1024 / 1000;

                    resultTimeSum1 += time;
                    resultMenSum1 += memory;
                    resultVisitDoorsSum1 += visitDoors;
                }
                resultTimeAve1 = resultTimeSum1 / 10;
                resultMenAve1 = resultMenSum1 / 10;
                resultVisitDoorsAve1 = resultVisitDoorsSum1 / 10;

                resultTimeSum2 += resultTimeAve1;
                resultMenSum2 += resultMenAve1;
                resultVisitDoorsSum2 += resultVisitDoorsAve1;
            }

            resultTimeAve2 = resultTimeSum2 / 10;
            resultMenAve2 = resultMenSum2 / 10;
            resultVisitDoorsAve2 = resultVisitDoorsSum2 / 10;

            if (parameter == "diType") {
                result += DataGenConstant.divisionType + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\t" + resultVisitDoorsAve2 + "\n";
            }
        }

        FileOutputStream output = new FileOutputStream(outFile, true);
        output.write(result.getBytes());
        output.flush();
        output.close();


    }

    public void IDMatrix_SPQ(String parameter, String fileName) throws IOException {
        CPHDataGenRead dateGenReadMen = new CPHDataGenRead();
        dateGenReadMen.dataGen();

        GenTopology genTopology = new GenTopology();
        genTopology.genTopology();

        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        long startMem = runtime.totalMemory() - runtime.freeMemory();

        DistMatrixGen distMatrixGen = new DistMatrixGen();
        distMatrixGen.readDist();

        HashMap<String, Double> d2dDistMap = distMatrixGen.getD2dDistMap();        // the distance between two doors
        HashMap<String, String> d2dRouteMap = distMatrixGen.getD2dRouteMap();


        String outFile = System.getProperty("user.dir") + fileName;

        String result = "";
        if (parameter == "distance") {
            result += "IDMatrix_SPQ" + "\n";
            result += "distance" + "\t" + "time(ns)" + "\t" + "memory" + "\t" + "visitDoors" + "\n";
            for (int i = 4; i >= 4; i--) {
                double distance = 1100;
                ArrayList<ArrayList<Point>> pointPairs = getPointPairs(distance);

                long resultTimeSum2 = 0;
                long resultMenSum2 = 0;
                int resultVisitDoorsSum2 = 0;
                long resultTimeAve2 = 0;
                long resultMenAve2 = 0;
                int resultVisitDoorsAve2 = 0;

                for (int j = 0; j < 10; j++) {
                    ArrayList<Point> pointPair = pointPairs.get(j);
                    IDMatrix_SPQ idMatrix_spq = new IDMatrix_SPQ();
                    long resultTimeSum1 = 0;
                    long resultMenSum1 = 0;
                    int resultVisitDoorsSum1 = 0;
                    long resultTimeAve1 = 0;
                    long resultMenAve1 = 0;
                    int resultVisitDoorsAve1 = 0;
                    idMatrix_spq.p2pDistance(pointPair.get(0), pointPair.get(1), d2dDistMap, d2dRouteMap);
                    for (int k = 0; k < 10; k++) {
//                        Runtime runtime = Runtime.getRuntime();
//                        runtime.gc();
//                        long startMem = runtime.totalMemory() - runtime.freeMemory();
                        long start = System.nanoTime();

                        int visitDoors = idMatrix_spq.p2pDistance(pointPair.get(0), pointPair.get(1), d2dDistMap, d2dRouteMap);
                        long end = System.nanoTime();
                        long endMem = runtime.totalMemory() - runtime.freeMemory();

                        long time = (end - start) / 1000;
                        long memory = (endMem - startMem) / 1024 / 1000;

                        resultTimeSum1 += time;
                        resultMenSum1 += memory;
                        resultVisitDoorsSum1 += visitDoors;
                    }
                    resultTimeAve1 = resultTimeSum1 / 10;
                    resultMenAve1 = resultMenSum1 / 10;
                    resultVisitDoorsAve1 = resultVisitDoorsSum1 / 10;

                    resultTimeSum2 += resultTimeAve1;
                    resultMenSum2 += resultMenAve1;
                    resultVisitDoorsSum2 += resultVisitDoorsAve1;
                }

                resultTimeAve2 = resultTimeSum2 / 10;
                resultMenAve2 = resultMenSum2 / 10;
                resultVisitDoorsAve2 = resultVisitDoorsSum2 / 10;
                result += distance + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\t" + resultVisitDoorsAve2 + "\n";
            }
        }

        if (parameter == "diType") {
            result += "IDMatrix_SPQ" + "\n";
            result += parameter + "\t" + "time(ns)" + "\t" + "memory" + "\t" + "visitDoors" + "\n";
            ArrayList<ArrayList<Point>> pointPairs = getPointPairs(1500);

            long resultTimeSum2 = 0;
            long resultMenSum2 = 0;
            int resultVisitDoorsSum2 = 0;
            long resultTimeAve2 = 0;
            long resultMenAve2 = 0;
            int resultVisitDoorsAve2 = 0;

            for (int j = 0; j < 10; j++) {
                ArrayList<Point> pointPair = pointPairs.get(j);
                IDMatrix_SPQ idMatrix_spq = new IDMatrix_SPQ();
                long resultTimeSum1 = 0;
                long resultMenSum1 = 0;
                int resultVisitDoorsSum1 = 0;
                long resultTimeAve1 = 0;
                long resultMenAve1 = 0;
                int resultVisitDoorsAve1 = 0;
                idMatrix_spq.p2pDistance(pointPair.get(0), pointPair.get(1), d2dDistMap, d2dRouteMap);
                for (int k = 0; k < 10; k++) {
//                    Runtime runtime = Runtime.getRuntime();
//                    runtime.gc();
//                    long startMem = runtime.totalMemory() - runtime.freeMemory();
                    long start = System.nanoTime();

                    int visitDoors = idMatrix_spq.p2pDistance(pointPair.get(0), pointPair.get(1), d2dDistMap, d2dRouteMap);
                    long end = System.nanoTime();
                    long endMem = runtime.totalMemory() - runtime.freeMemory();

                    long time = (end - start) / 1000;
                    long memory = (endMem - startMem) / 1024 / 1000;

                    resultTimeSum1 += time;
                    resultMenSum1 += memory;
                    resultVisitDoorsSum1 += visitDoors;
                }
                resultTimeAve1 = resultTimeSum1 / 10;
                resultMenAve1 = resultMenSum1 / 10;
                resultVisitDoorsAve1 = resultVisitDoorsSum1 / 10;

                resultTimeSum2 += resultTimeAve1;
                resultMenSum2 += resultMenAve1;
                resultVisitDoorsSum2 += resultVisitDoorsAve1;
            }

            resultTimeAve2 = resultTimeSum2 / 10;
            resultMenAve2 = resultMenSum2 / 10;
            resultVisitDoorsAve2 = resultVisitDoorsSum2 / 10;

            if (parameter == "diType") {
                result += DataGenConstant.divisionType + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\t" + resultVisitDoorsAve2 + "\n";
            }
        }
        FileOutputStream output = new FileOutputStream(outFile, true);
        output.write(result.getBytes());
        output.flush();
        output.close();


    }

    public void ICIndex_spq(String parameter, String fileName) throws IOException {
        CPHDataGenRead dateGenReadMen = new CPHDataGenRead();
        dateGenReadMen.dataGen();

        GenTopology genTopology = new GenTopology();
        genTopology.genTopology();

        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        long startMem = runtime.totalMemory() - runtime.freeMemory();

        final Box bounds = Box.create(0.0, 0.0, 0.0, 10000.0, 10000.0, 0.5);

        // create RTree
        int maxChildren = 10;
        RTree<Integer, Box> tree = RTree.star().minChildren((maxChildren) / 2)
                .maxChildren(maxChildren).bounds(bounds).create();

//	    loadData();

        for (int i = 0; i < IndoorSpace.iPartitions.size(); i++) {
            Partition partition = IndoorSpace.iPartitions.get(i);
            TreeNode node = new TreeNode(partition.getX1(), partition.getY1(), (partition.getmFloor()) * 0.1
                    , partition.getX2(), partition.getY2(), (partition.getmFloor() + 1) * 0.1, partition,
                    partition.getdistMatrix(), partition.getTopology());

            Box box = Box.create(partition.getX1(), partition.getY1(), (partition.getmFloor()) * 0.1
                    , partition.getX2(), partition.getY2(), (partition.getmFloor() + 1) * 0.1);
            box.setmNode(node);

            tree = tree.add(partition.getmID(), box);

//	    		if ((partition.getmFloor()) * 0.1 != ((partition.getmFloor() + 1) * 0.1) - 0.1) System.out.println("something wrong " + partition.getmID() + " " + partition.getmFloor());
        }

        System.out.println("tree size = " + tree.size());

        ArrayList<Box> boxes = new ArrayList<Box>();

        for (int i = 0; i < IndoorSpace.iPartitions.size(); i++) {
            Partition partition = IndoorSpace.iPartitions.get(i);

            Observable<Entry<Integer, Box>> entries = tree.search(com.github.davidmoten.rtree3d.geometry.Point.create(partition.getcenterX(), partition.getcenterY()
                    , partition.getmFloor() * 0.1 + 0.05));
            int count = entries.count().toBlocking().single();

            entries.subscribe(
                    e -> boxes.add(e.geometry())
            );

            if (count != 1) System.out.println("something wrong with count " + partition.getmID());
        }

        System.out.println("size = " + tree.size());
        System.out.println("entries = " + tree.countEntries());
        System.out.println("total boxes size = " + boxes.size());
        for (int i = 0; i < boxes.size(); i++) {
            System.out.println();

            Box box = boxes.get(i);
            System.out.println("Box " + box.cornerToString());

            TreeNode node = box.getmNode();
            System.out.println("Node " + node.cornerToString() + " id = " + node.getmID());

            Partition partition = node.getmPartition();
            System.out.println("Partition " + partition.cornerToString3D() + " id = " + partition.getmID());
        }


        String fileInput = System.getProperty("user.dir") + "/source.r";
        Runtime.getRuntime().exec("/bin/sh R CMD BATCH " + fileInput);
        System.out.println("png");

        String outFile = System.getProperty("user.dir") + fileName;

        String result = "";
        if (parameter == "distance") {
            result += "CIndex_SPQ" + "\n";
            result += "distance" + "\t" + "time(ns)" + "\t" + "memory" + "\t" + "visitDoors" + "\n";
            for (int i = 0; i >= 0; i--) {
                double distance = 1900;
                ArrayList<ArrayList<Point>> pointPairs = getPointPairs(distance);

                long resultTimeSum2 = 0;
                long resultMenSum2 = 0;
                int resultVisitDoorsSum2 = 0;
                long resultTimeAve2 = 0;
                long resultMenAve2 = 0;
                int resultVisitDoorsAve2 = 0;

                for (int j = 0; j < 10; j++) {
                    ArrayList<Point> pointPair = pointPairs.get(j);
                    ICIndex_SPQ icIndex_spq = new ICIndex_SPQ();
                    long resultTimeSum1 = 0;
                    long resultMenSum1 = 0;
                    int resultVisitDoorsSum1 = 0;
                    long resultTimeAve1 = 0;
                    long resultMenAve1 = 0;
                    int resultVisitDoorsAve1 = 0;
                    icIndex_spq.pt2ptDistance3(pointPair.get(0), pointPair.get(1));
                    for (int k = 0; k < 10; k++) {
//                        Runtime runtime = Runtime.getRuntime();
//                        runtime.gc();
//                        long startMem = runtime.totalMemory() - runtime.freeMemory();
                        long start = System.nanoTime();

                        int visitDoors = Integer.parseInt(icIndex_spq.pt2ptDistance3(pointPair.get(0), pointPair.get(1)).split("\t")[1]);

                        long end = System.nanoTime();
                        long endMem = runtime.totalMemory() - runtime.freeMemory();

                        long time = (end - start) / 1000;
                        long memory = (endMem - startMem) / 1024 / 1000;

                        resultTimeSum1 += time;
                        resultMenSum1 += memory;
                        resultVisitDoorsSum1 += visitDoors;
                    }
                    resultTimeAve1 = resultTimeSum1 / 10;
                    resultMenAve1 = resultMenSum1 / 10;
                    resultVisitDoorsAve1 = resultVisitDoorsSum1 / 10;

                    resultTimeSum2 += resultTimeAve1;
                    resultMenSum2 += resultMenAve1;
                    resultVisitDoorsSum2 += resultVisitDoorsAve1;
                }

                resultTimeAve2 = resultTimeSum2 / 10;
                resultMenAve2 = resultMenSum2 / 10;
                resultVisitDoorsAve2 = resultVisitDoorsSum2 / 10;
                result += distance + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\t" + resultVisitDoorsAve2 + "\n";
            }
        }
        if (parameter == "diType") {
            result += "CIndex_SPQ" + "\n";
            result += parameter + "\t" + "time(ns)" + "\t" + "memory" + "\t" + "visitDoors" + "\n";
            ArrayList<ArrayList<Point>> pointPairs = getPointPairs(1500);

            long resultTimeSum2 = 0;
            long resultMenSum2 = 0;
            int resultVisitDoorsSum2 = 0;
            long resultTimeAve2 = 0;
            long resultMenAve2 = 0;
            int resultVisitDoorsAve2 = 0;

            for (int j = 0; j < 10; j++) {
                ArrayList<Point> pointPair = pointPairs.get(j);
                ICIndex_SPQ icIndex_spq = new ICIndex_SPQ();
                long resultTimeSum1 = 0;
                long resultMenSum1 = 0;
                int resultVisitDoorsSum1 = 0;
                long resultTimeAve1 = 0;
                long resultMenAve1 = 0;
                int resultVisitDoorsAve1 = 0;
                icIndex_spq.pt2ptDistance3(pointPair.get(0), pointPair.get(1));
                for (int k = 0; k < 10; k++) {
//                    Runtime runtime = Runtime.getRuntime();
//                    runtime.gc();
//                    long startMem = runtime.totalMemory() - runtime.freeMemory();
                    long start = System.nanoTime();

                    int visitDoors = Integer.parseInt(icIndex_spq.pt2ptDistance3(pointPair.get(0), pointPair.get(1)).split("\t")[1]);
                    long end = System.nanoTime();
                    long endMem = runtime.totalMemory() - runtime.freeMemory();

                    long time = (end - start) / 1000;
                    long memory = (endMem - startMem) / 1024 / 1000;

                    resultTimeSum1 += time;
                    resultMenSum1 += memory;
                    resultVisitDoorsSum1 += visitDoors;
                }
                resultTimeAve1 = resultTimeSum1 / 10;
                resultMenAve1 = resultMenSum1 / 10;
                resultVisitDoorsAve1 = resultVisitDoorsSum1 / 10;

                resultTimeSum2 += resultTimeAve1;
                resultMenSum2 += resultMenAve1;
                resultVisitDoorsSum2 += resultVisitDoorsAve1;
            }

            resultTimeAve2 = resultTimeSum2 / 10;
            resultMenAve2 = resultMenSum2 / 10;
            resultVisitDoorsAve2 = resultVisitDoorsSum2 / 10;

            if (parameter == "diType") {
                result += DataGenConstant.divisionType + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\t" + resultVisitDoorsAve2 + "\n";
            }
        }
        FileOutputStream output = new FileOutputStream(outFile, true);
        output.write(result.getBytes());
        output.flush();
        output.close();
    }


    public void IPtree_SPQ(String parameter, String fileName) throws IOException {
        CPHDataGenRead dateGenReadMen = new CPHDataGenRead();
        dateGenReadMen.dataGen();

        GenTopology genTopology = new GenTopology();
        genTopology.genTopology();

        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        long startMem = runtime.totalMemory() - runtime.freeMemory();

        ClassifyPartition classifyPartition = new ClassifyPartition();
        classifyPartition.classifyPar();

//        System.out.println(IndoorSpace.iCrucialPartitions.size());

        VIPTree ipTree = VIPTree.createTree();
        ipTree.initTree();

        String outFile = System.getProperty("user.dir") + fileName;

        String result = "";
        if (parameter == "distance") {
            result += "IPtree_SPQ" + "\n";
            result += "distance" + "\t" + "time(ns)" + "\t" + "memory" + "\t" + "visitDoors" + "\n";
            for (int i = 4; i >= 4; i--) {
                double distance = 1900;
                ArrayList<ArrayList<Point>> pointPairs = getPointPairs(distance);

                long resultTimeSum2 = 0;
                long resultMenSum2 = 0;
                int resultVisitDoorsSum2 = 0;
                long resultTimeAve2 = 0;
                long resultMenAve2 = 0;
                int resultVisitDoorsAve2 = 0;

                for (int j = 0; j < 10; j++) {
                    ArrayList<Point> pointPair = pointPairs.get(j);
                    IPtree_SPQ iPtree_spq = new IPtree_SPQ();
                    long resultTimeSum1 = 0;
                    long resultMenSum1 = 0;
                    int resultVisitDoorsSum1 = 0;
                    long resultTimeAve1 = 0;
                    long resultMenAve1 = 0;
                    int resultVisitDoorsAve1 = 0;
                    iPtree_spq.ipTreeSPQ(pointPair.get(0), pointPair.get(1), ipTree);
                    iPtree_spq.ipTreeSPQ(pointPair.get(0), pointPair.get(1), ipTree);
                    for (int k = 0; k < 10; k++) {
//                        Runtime runtime = Runtime.getRuntime();
//                        runtime.gc();
//                        long startMem = runtime.totalMemory() - runtime.freeMemory();
                        long start = System.nanoTime();

                        int visitDoors = iPtree_spq.ipTreeSPQ(pointPair.get(0), pointPair.get(1), ipTree);
//                        System.out.println(visitDoors);
                        long end = System.nanoTime();
                        long endMem = runtime.totalMemory() - runtime.freeMemory();

                        long time = (end - start) / 1000;
                        long memory = (endMem - startMem) / 1024 / 1000;

                        resultTimeSum1 += time;
                        resultMenSum1 += memory;
                        resultVisitDoorsSum1 += visitDoors;
                    }
                    resultTimeAve1 = resultTimeSum1 / 10;
                    resultMenAve1 = resultMenSum1 / 10;
                    resultVisitDoorsAve1 = resultVisitDoorsSum1 / 10;

                    resultTimeSum2 += resultTimeAve1;
                    resultMenSum2 += resultMenAve1;
                    resultVisitDoorsSum2 += resultVisitDoorsAve1;
                }

                resultTimeAve2 = resultTimeSum2 / 10;
                resultMenAve2 = resultMenSum2 / 10;
                resultVisitDoorsAve2 = resultVisitDoorsSum2 / 10;
                result += distance + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\t" + resultVisitDoorsAve2 + "\n";
            }
        }
        if (parameter == "diType") {
            result += "IPtree_SPQ" + "\n";
            result += parameter + "\t" + "time(ns)" + "\t" + "memory" + "\t" + "visitDoors" + "\n";
            ArrayList<ArrayList<Point>> pointPairs = getPointPairs(1500);

            long resultTimeSum2 = 0;
            long resultMenSum2 = 0;
            int resultVisitDoorsSum2 = 0;
            long resultTimeAve2 = 0;
            long resultMenAve2 = 0;
            int resultVisitDoorsAve2 = 0;

            for (int j = 0; j < 10; j++) {
                ArrayList<Point> pointPair = pointPairs.get(j);
                IPtree_SPQ iPtree_spq = new IPtree_SPQ();
                long resultTimeSum1 = 0;
                long resultMenSum1 = 0;
                int resultVisitDoorsSum1 = 0;
                long resultTimeAve1 = 0;
                long resultMenAve1 = 0;
                int resultVisitDoorsAve1 = 0;
                iPtree_spq.ipTreeSPQ(pointPair.get(0), pointPair.get(1), ipTree);
                for (int k = 0; k < 10; k++) {
//                    Runtime runtime = Runtime.getRuntime();
//                    runtime.gc();
//                    long startMem = runtime.totalMemory() - runtime.freeMemory();
                    long start = System.nanoTime();

                    int visitDoors = iPtree_spq.ipTreeSPQ(pointPair.get(0), pointPair.get(1), ipTree);
                    long end = System.nanoTime();
                    long endMem = runtime.totalMemory() - runtime.freeMemory();

                    long time = (end - start) / 1000;
                    long memory = (endMem - startMem) / 1024 / 1000;

                    resultTimeSum1 += time;
                    resultMenSum1 += memory;
                    resultVisitDoorsSum1 += visitDoors;
                }
                resultTimeAve1 = resultTimeSum1 / 10;
                resultMenAve1 = resultMenSum1 / 10;
                resultVisitDoorsAve1 = resultVisitDoorsSum1 / 10;

                resultTimeSum2 += resultTimeAve1;
                resultMenSum2 += resultMenAve1;
                resultVisitDoorsSum2 += resultVisitDoorsAve1;
            }

            resultTimeAve2 = resultTimeSum2 / 10;
            resultMenAve2 = resultMenSum2 / 10;
            resultVisitDoorsAve2 = resultVisitDoorsSum2 / 10;

            if (parameter == "diType") {
                result += DataGenConstant.divisionType + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\t" + resultVisitDoorsAve2 + "\n";
            }
        }

        FileOutputStream output = new FileOutputStream(outFile, true);
        output.write(result.getBytes());
        output.flush();
        output.close();


    }


    public void VIPtree_SPQ(String parameter, String fileName) throws IOException {
        CPHDataGenRead dateGenReadMen = new CPHDataGenRead();
        dateGenReadMen.dataGen();

        GenTopology genTopology = new GenTopology();
        genTopology.genTopology();

        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        long startMem = runtime.totalMemory() - runtime.freeMemory();

        ClassifyPartition classifyPartition = new ClassifyPartition();
        classifyPartition.classifyPar();

//        System.out.println(IndoorSpace.iCrucialPartitions.size());

        VIPTree vipTree = VIPTree.createTree();
        vipTree.initVIPtree();

        String outFile = System.getProperty("user.dir") + fileName;

        String result = "";
        if (parameter == "distance") {
            result += "VIPtree_SPQ" + "\n";
            result += "distance" + "\t" + "time(ns)" + "\t" + "memory" + "\t" + "visitDoors" + "\n";
            for (int i = 4; i >= 4; i--) {
                double distance = 1900;
                ArrayList<ArrayList<Point>> pointPairs = getPointPairs(distance);

                long resultTimeSum2 = 0;
                long resultMenSum2 = 0;
                int resultVisitDoorsSum2 = 0;
                long resultTimeAve2 = 0;
                long resultMenAve2 = 0;
                int resultVisitDoorsAve2 = 0;

                for (int j = 0; j < 10; j++) {
                    ArrayList<Point> pointPair = pointPairs.get(j);
                    VIPtree_SPQ viPtree_spq = new VIPtree_SPQ();
                    long resultTimeSum1 = 0;
                    long resultMenSum1 = 0;
                    int resultVisitDoorsSum1 = 0;
                    long resultTimeAve1 = 0;
                    long resultMenAve1 = 0;
                    int resultVisitDoorsAve1 = 0;
                    viPtree_spq.vipTreeSPQ(pointPair.get(0), pointPair.get(1), vipTree);
                    for (int k = 0; k < 10; k++) {
//                        Runtime runtime = Runtime.getRuntime();
//                        runtime.gc();
//                        long startMem = runtime.totalMemory() - runtime.freeMemory();
                        long start = System.nanoTime();

                        int visitDoors = viPtree_spq.vipTreeSPQ(pointPair.get(0), pointPair.get(1), vipTree);
                        long end = System.nanoTime();
                        long endMem = runtime.totalMemory() - runtime.freeMemory();

                        long time = (end - start) / 1000;
                        long memory = (endMem - startMem) / 1024 / 1000;

                        resultTimeSum1 += time;
                        resultMenSum1 += memory;
                        resultVisitDoorsSum1 += visitDoors;
                    }
                    resultTimeAve1 = resultTimeSum1 / 10;
                    resultMenAve1 = resultMenSum1 / 10;
                    resultVisitDoorsAve1 = resultVisitDoorsSum1 / 10;

                    resultTimeSum2 += resultTimeAve1;
                    resultMenSum2 += resultMenAve1;
                    resultVisitDoorsSum2 += resultVisitDoorsAve1;
                }

                resultTimeAve2 = resultTimeSum2 / 10;
                resultMenAve2 = resultMenSum2 / 10;
                resultVisitDoorsAve2 = resultVisitDoorsSum2 / 10;
                result += distance + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\t" + resultVisitDoorsAve2 + "\n";
            }
        }
        if (parameter == "diType") {
            result += "VIPtree_SPQ" + "\n";
            result += parameter + "\t" + "time(ns)" + "\t" + "memory" + "\t" + "visitDoors" + "\n";
            ArrayList<ArrayList<Point>> pointPairs = getPointPairs(1500);

            long resultTimeSum2 = 0;
            long resultMenSum2 = 0;
            int resultVisitDoorsSum2 = 0;
            long resultTimeAve2 = 0;
            long resultMenAve2 = 0;
            int resultVisitDoorsAve2 = 0;

            for (int j = 0; j < 10; j++) {
                ArrayList<Point> pointPair = pointPairs.get(j);
                VIPtree_SPQ viPtree_spq = new VIPtree_SPQ();
                long resultTimeSum1 = 0;
                long resultMenSum1 = 0;
                int resultVisitDoorsSum1 = 0;
                long resultTimeAve1 = 0;
                long resultMenAve1 = 0;
                int resultVisitDoorsAve1 = 0;
                viPtree_spq.vipTreeSPQ(pointPair.get(0), pointPair.get(1), vipTree);
                for (int k = 0; k < 10; k++) {
//                    Runtime runtime = Runtime.getRuntime();
//                    runtime.gc();
//                    long startMem = runtime.totalMemory() - runtime.freeMemory();
                    long start = System.nanoTime();

                    int visitDoors = viPtree_spq.vipTreeSPQ(pointPair.get(0), pointPair.get(1), vipTree);
                    long end = System.nanoTime();
                    long endMem = runtime.totalMemory() - runtime.freeMemory();

                    long time = (end - start) / 1000;
                    long memory = (endMem - startMem) / 1024 / 1000;

                    resultTimeSum1 += time;
                    resultMenSum1 += memory;
                    resultVisitDoorsSum1 += visitDoors;
                }
                resultTimeAve1 = resultTimeSum1 / 10;
                resultMenAve1 = resultMenSum1 / 10;
                resultVisitDoorsAve1 = resultVisitDoorsSum1 / 10;

                resultTimeSum2 += resultTimeAve1;
                resultMenSum2 += resultMenAve1;
                resultVisitDoorsSum2 += resultVisitDoorsAve1;
            }

            resultTimeAve2 = resultTimeSum2 / 10;
            resultMenAve2 = resultMenSum2 / 10;
            resultVisitDoorsAve2 = resultVisitDoorsSum2 / 10;

            if (parameter == "diType") {
                result += DataGenConstant.divisionType + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\t" + resultVisitDoorsAve2 + "\n";
            }
        }


        FileOutputStream output = new FileOutputStream(outFile, true);
        output.write(result.getBytes());
        output.flush();
        output.close();

    }

    public ArrayList<ArrayList<Point>> getPointPairs(double distance) {
        ArrayList<ArrayList<Point>> pointPairs = new ArrayList<>();
        ArrayList<String> point1s = new ArrayList<>();
        ArrayList<String> point2s = new ArrayList<>();
        if ((int) distance == (int) 1100) {
            point1s.addAll(new ArrayList<>(Arrays.asList("289.0,478.0,0", "1788.0,594.0,0", "287.0,499.0,0", "1801.0,272.0,0", "1800.0,283.0,0", "216.0,34.0,0", "1871.0,265.0,0", "1663.0,195.0,0", "136.0,257.0,0", "1538.0,73.0,0")));
            point2s.addAll(new ArrayList<>(Arrays.asList("979.0,265.0,0", "1140.0,254.0,0", "924.0,150.0,0", "908.0,301.0,0", "867.0,120.0,0", "1306.0,91.0,0", "1036.0,36.0,0", "632.0,32.0,0", "1157.0,243.0,0", "663.0,170.0,0")));

        }
        if ((int) distance == (int) 1300) {
            point1s.addAll(new ArrayList<>(Arrays.asList("289.0,478.0,0", "1788.0,594.0,0", "287.0,499.0,0", "1801.0,272.0,0", "1800.0,283.0,0", "216.0,34.0,0", "1871.0,265.0,0", "1663.0,195.0,0", "136.0,257.0,0", "1538.0,73.0,0")));
            point2s.addAll(new ArrayList<>(Arrays.asList("1152.0,248.0,0", "1201.0,32.0,0", "1219.0,245.0,0", "671.0,50.0,0", "573.0,177.0,0", "1420.0,187.0,0", "755.0,170.0,0", "364.0,51.0,0", "1365.0,242.0,0", "473.0,34.0,0")));

        }
        if ((int) distance == (int) 1500) {
            point1s.addAll(new ArrayList<>(Arrays.asList("289.0,478.0,0", "1788.0,594.0,0", "287.0,499.0,0", "1801.0,272.0,0", "1800.0,283.0,0", "216.0,34.0,0", "1871.0,265.0,0", "1663.0,195.0,0", "136.0,257.0,0", "1538.0,73.0,0")));
            point2s.addAll(new ArrayList<>(Arrays.asList("1319.0,248.0,0", "716.0,169.0,0", "1410.0,108.0,0", "423.0,143.0,0", "458.0,157.0,0", "1693.0,184.0,0", "581.0,30.0,0", "287.0,41.0,0", "1396.0,248.0,0", "356.0,248.0,0")));

        }
        if ((int) distance == (int) 1700) {
            point1s.addAll(new ArrayList<>(Arrays.asList("289.0,478.0,0", "1788.0,594.0,0", "287.0,499.0,0", "1801.0,272.0,0", "1800.0,283.0,0", "216.0,34.0,0", "1871.0,265.0,0", "1663.0,195.0,0", "136.0,257.0,0", "1538.0,73.0,0")));
            point2s.addAll(new ArrayList<>(Arrays.asList("1482.0,220.0,0", "583.0,56.0,0", "1487.0,172.0,0", "230.0,71.0,0", "240.0,32.0,0", "1898.0,279.0,0", "345.0,240.0,0", "234.0,323.0,0", "1794.0,128.0,0", "227.0,310.0,0")));

        }
        if ((int) distance == (int) 1900) {
            point1s.addAll(new ArrayList<>(Arrays.asList("289.0,478.0,0", "1788.0,594.0,0", "287.0,499.0,0", "1801.0,272.0,0", "1800.0,283.0,0", "216.0,34.0,0", "1871.0,265.0,0", "1663.0,195.0,0", "136.0,257.0,0", "1538.0,73.0,0")));
            point2s.addAll(new ArrayList<>(Arrays.asList("1804.0,108.0,0", "384.0,253.0,0", "1568.0,98.0,0", "94.0,250.0,0", "180.0,351.0,0", "1882.0,261.0,0", "108.0,257.0,0", "294.0,502.0,0", "1818.0,273.0,0", "292.0,483.0,0")));

        }

        for (int i = 0; i < point1s.size(); i++) {
            String point1Str = point1s.get(i);
            String point2Str = point2s.get(i);
            String[] point1Arr = point1Str.split(",");
            String[] point2Arr = point2Str.split(",");
            Point point1 = new Point(Double.parseDouble(point1Arr[0]), Double.parseDouble(point1Arr[1]), Integer.parseInt(point1Arr[2]));
            Point point2 = new Point(Double.parseDouble(point2Arr[0]), Double.parseDouble(point2Arr[1]), Integer.parseInt(point2Arr[2]));
            pointPairs.add(new ArrayList<>(Arrays.asList(point1, point2)));
        }
        return pointPairs;
    }

    public static void main(String[] arg) throws IOException {
        SPQ spq = new SPQ();

        // distance
//        String fileName = "/results/" + DataGenConstant.dataset + "/SPQ_Dist.csv";
//        spq.IDModel_SPQ("distance", fileName);
//        spq.IDMatrix_SPQ("distance", fileName);
//        spq.ICIndex_spq("distance", fileName);
//        spq.IPtree_SPQ("distance", fileName);
//        spq.VIPtree_SPQ("distance", fileName);

        // diType
//        String fileName = "/results/" + DataGenConstant.dataset + "/diType_SPQ.csv";
//        spq.IDModel_SPQ("diType", fileName);
//        spq.IDMatrix_SPQ("diType", fileName);
//        spq.ICIndex_spq("diType", fileName);
//        spq.IPtree_SPQ("diType", fileName);
//        spq.VIPtree_SPQ("diType", fileName);


    }

}
