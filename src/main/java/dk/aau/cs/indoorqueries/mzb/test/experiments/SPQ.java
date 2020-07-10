package dk.aau.cs.indoorqueries.mzb.test.experiments;

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
import dk.aau.cs.indoorqueries.mzb.datagenerate.ClassifyPartition;
import dk.aau.cs.indoorqueries.mzb.datagenerate.MZBDataGenRead;
import rx.Observable;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class SPQ {
    public static double distDefault = 90;

    public void IDModel_SPQ(String parameter, String fileName, double distValue) throws IOException {
        MZBDataGenRead dateGenReadMen = new MZBDataGenRead();
        dateGenReadMen.dataGen();

        GenTopology genTopology = new GenTopology();
        genTopology.genTopology();

        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        long startMem = runtime.totalMemory() - runtime.freeMemory();

        String outFile = System.getProperty("user.dir") + fileName;

        String result = "";
        if (parameter == "distance" || parameter == "diType") {
            result += "IDModel_SPQ" + "\n";
            result += parameter + "\t" + "time(ns)" + "\t" + "memory(kb)" + "\t" + "visitDoors" + "\n";
            for (int i = 0; i >= 0; i--) {
                double distance = distValue;
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
                if (parameter == "objectNum") {
                    result += distance + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\t" + resultVisitDoorsAve2 + "\n";
                }
                if (parameter == "diType") {
                    result += DataGenConstant.divisionType + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\t" + resultVisitDoorsAve2 + "\n";
                }
            }
        }

        FileOutputStream output = new FileOutputStream(outFile, true);
        output.write(result.getBytes());
        output.flush();
        output.close();


    }

    public void IDMatrix_SPQ(String parameter, String fileName, double distValue) throws IOException {
        MZBDataGenRead dateGenReadMen = new MZBDataGenRead();
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
        if (parameter == "distance" || parameter == "diType") {
            result += "IDMatrix_SPQ" + "\n";
            result += parameter + "\t" + "time(ns)" + "\t" + "memory" + "\t" + "visitDoors" + "\n";
            for (int i = 0; i >= 0; i--) {
                double distance = distValue;
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
                if (parameter == "objectNum") {
                    result += distance + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\t" + resultVisitDoorsAve2 + "\n";
                }
                if (parameter == "diType") {
                    result += DataGenConstant.divisionType + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\t" + resultVisitDoorsAve2 + "\n";
                }
            }
        }


        FileOutputStream output = new FileOutputStream(outFile, true);
        output.write(result.getBytes());
        output.flush();
        output.close();


    }

    public void ICIndex_spq(String parameter, String fileName, double distValue) throws IOException {
        MZBDataGenRead dateGenReadMen = new MZBDataGenRead();
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
        if (parameter == "distance" || parameter == "diType") {
            result += "CIndex_SPQ" + "\n";
            result += parameter + "\t" + "time(ns)" + "\t" + "memory" + "\t" + "visitDoors" + "\n";
            for (int i = 0; i >= 0; i--) {
                double distance = distValue;
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
                if (parameter == "objectNum") {
                    result += distance + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\t" + resultVisitDoorsAve2 + "\n";
                }
                if (parameter == "diType") {
                    result += DataGenConstant.divisionType + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\t" + resultVisitDoorsAve2 + "\n";
                }
            }
        }
        FileOutputStream output = new FileOutputStream(outFile, true);
        output.write(result.getBytes());
        output.flush();
        output.close();
    }


    public void IPtree_SPQ(String parameter, String fileName, double distValue) throws IOException {
        MZBDataGenRead dateGenReadMen = new MZBDataGenRead();
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
        if (parameter == "distance" || parameter == "diType") {
            result += "IPtree_SPQ" + "\n";
            result += parameter + "\t" + "time(ns)" + "\t" + "memory" + "\t" + "visitDoors" + "\n";
            for (int i = 0; i >= 0; i--) {
                double distance = distValue;
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
                if (parameter == "objectNum") {
                    result += distance + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\t" + resultVisitDoorsAve2 + "\n";
                }
                if (parameter == "diType") {
                    result += DataGenConstant.divisionType + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\t" + resultVisitDoorsAve2 + "\n";
                }
            }
        }


        FileOutputStream output = new FileOutputStream(outFile, true);
        output.write(result.getBytes());
        output.flush();
        output.close();


    }


    public void VIPtree_SPQ(String parameter, String fileName, double distValue) throws IOException {
        MZBDataGenRead dateGenReadMen = new MZBDataGenRead();
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
        if (parameter == "distance" || parameter == "diType") {
            result += "VIPtree_SPQ" + "\n";
            result += parameter + "\t" + "time(ns)" + "\t" + "memory" + "\t" + "visitDoors" + "\n";
            for (int i = 0; i >= 0; i--) {
                double distance = distValue;
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
                if (parameter == "objectNum") {
                    result += distance + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\t" + resultVisitDoorsAve2 + "\n";
                }
                if (parameter == "diType") {
                    result += DataGenConstant.divisionType + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\t" + resultVisitDoorsAve2 + "\n";
                }
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
        if ((int) distance == (int) 30) {
            point1s.addAll(new ArrayList<>(Arrays.asList("34.0,5.0,0", "56.0,9.0,13", "61.0,33.0,16", "79.0,9.0,3", "32.0,6.0,0", "59.0,18.0,5", "121.0,24.0,15", "61.0,23.0,16", "90.0,1.0,0", "61.0,33.0,11")));
            point2s.addAll(new ArrayList<>(Arrays.asList("55.0,10.0,0", "70.0,34.0,13", "93.0,33.0,16", "52.0,28.0,3", "67.0,2.0,0", "80.0,30.0,5", "96.0,26.0,15", "55.0,5.0,16", "67.0,32.0,1", "71.0,9.0,11")));

        }
        if ((int) distance == (int) 60) {
            point1s.addAll(new ArrayList<>(Arrays.asList("34.0,5.0,0", "56.0,9.0,13", "61.0,33.0,16", "79.0,9.0,3", "32.0,6.0,0", "59.0,18.0,5", "121.0,24.0,15", "61.0,23.0,16", "90.0,1.0,0", "61.0,33.0,11")));
            point2s.addAll(new ArrayList<>(Arrays.asList("101.0,16.0,0", "19.0,33.0,13", "121.0,30.0,16", "115.0,28.0,3", "86.0,18.0,1", "14.0,34.0,5", "65.0,32.0,14", "13.0,28.0,16", "48.0,33.0,1", "3.0,33.0,11")));

        }
        if ((int) distance == (int) 90) {
            point1s.addAll(new ArrayList<>(Arrays.asList("34.0,5.0,0", "56.0,9.0,13", "61.0,33.0,16", "79.0,9.0,3", "32.0,6.0,0", "59.0,18.0,5", "121.0,24.0,15", "61.0,23.0,16", "90.0,1.0,0", "61.0,33.0,11")));
            point2s.addAll(new ArrayList<>(Arrays.asList("7.0,20.0,1", "112.0,27.0,11", "16.0,33.0,10", "104.0,20.0,0", "116.0,32.0,1", "18.0,26.0,9", "80.0,13.0,0", "30.0,32.0,15", "9.0,13.0,0", "24.0,30.0,14")));

        }
        if ((int) distance == (int) 120) {
            point1s.addAll(new ArrayList<>(Arrays.asList("34.0,5.0,0", "56.0,9.0,13", "61.0,33.0,16", "79.0,9.0,3", "32.0,6.0,0", "59.0,18.0,5", "121.0,24.0,15", "61.0,23.0,16", "90.0,1.0,0", "61.0,33.0,11")));
            point2s.addAll(new ArrayList<>(Arrays.asList("111.0,24.0,6", "82.0,34.0,8", "47.0,28.0,12", "74.0,12.0,11", "114.0,27.0,3", "71.0,13.0,9", "57.0,5.0,1", "111.0,28.0,1", "97.0,26.0,3", "29.0,18.0,0")));

        }
        if ((int) distance == (int) 150) {
            point1s.addAll(new ArrayList<>(Arrays.asList("34.0,5.0,0", "56.0,9.0,13", "61.0,33.0,16", "79.0,9.0,3", "32.0,6.0,0", "59.0,18.0,5", "121.0,24.0,15", "61.0,23.0,16", "90.0,1.0,0", "61.0,33.0,11")));
            point2s.addAll(new ArrayList<>(Arrays.asList("24.0,31.0,11", "56.0,3.0,6", "55.0,17.0,8", "56.0,15.0,13", "102.0,28.0,15", "60.0,7.0,15", "5.0,25.0,8", "76.0,14.0,2", "81.0,23.0,11", "69.0,24.0,1")));

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
//        spq.IDModel_SPQ("distance", fileName, 90);
//        spq.IDMatrix_SPQ("distance", fileName, 90);
//        spq.ICIndex_spq("distance", fileName, 90);
//        spq.IPtree_SPQ("distance", fileName, 90);
//        spq.VIPtree_SPQ("distance", fileName, 90);

        // diType
        String fileName = "/results/" + DataGenConstant.dataset + "/SPQ_diType.csv";
//        spq.IDModel_SPQ("diType", fileName, 90);
        spq.IDMatrix_SPQ("diType", fileName, 90);
//        spq.ICIndex_spq("diType", fileName, 90);
//        spq.IPtree_SPQ("diType", fileName, 90);
//        spq.VIPtree_SPQ("diType", fileName, 90);


    }

}
