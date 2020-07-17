package dk.aau.cs.indoorqueries.syn.test.experiments;

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
import dk.aau.cs.indoorqueries.syn.datagenerate.ClassifyPartition;
import dk.aau.cs.indoorqueries.syn.datagenerate.DataGen;
import rx.Observable;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * expriments for SPQ
 * @author Tiantian Liu
 */
public class SPQ {
    public void IDModel_SPQ(String parameter, String fileName) throws IOException {
        DataGen dataGen = new DataGen();
        dataGen.genAllData(DataGenConstant.dataType, DataGenConstant.divisionType);

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

        if (parameter == "floor" || parameter == "dataType" || parameter == "diType") {
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
            if (parameter == "floor") {
                result += DataGenConstant.nFloor + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\t" + resultVisitDoorsAve2 + "\n";
            }
            if (parameter == "dataType") {
                result += DataGenConstant.dataType + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\t" + resultVisitDoorsAve2 + "\n";
            }
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
        DataGen dataGen = new DataGen();
        dataGen.genAllData(DataGenConstant.dataType, DataGenConstant.divisionType);

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

        if (parameter == "floor" || parameter == "dataType" || parameter == "diType") {
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
            if (parameter == "floor") {
                result += DataGenConstant.nFloor + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\t" + resultVisitDoorsAve2 + "\n";
            }
            if (parameter == "dataType") {
                result += DataGenConstant.dataType + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\t" + resultVisitDoorsAve2 + "\n";
            }
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
        DataGen dataGen = new DataGen();
        dataGen.genAllData(DataGenConstant.dataType, DataGenConstant.divisionType);

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

        if (parameter == "floor" || parameter == "dataType" || parameter == "diType") {
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
            if (parameter == "floor") {
                result += DataGenConstant.nFloor + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\t" + resultVisitDoorsAve2 + "\n";
            }
            if (parameter == "dataType") {
                result += DataGenConstant.dataType + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\t" + resultVisitDoorsAve2 + "\n";
            }
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
        DataGen dataGen = new DataGen();
        dataGen.genAllData(DataGenConstant.dataType, DataGenConstant.divisionType);

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

        if (parameter == "floor" || parameter == "dataType" || parameter == "diType") {
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
            if (parameter == "floor") {
                result += DataGenConstant.nFloor + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\t" + resultVisitDoorsAve2 + "\n";
            }
            if (parameter == "dataType") {
                result += DataGenConstant.dataType + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\t" + resultVisitDoorsAve2 + "\n";
            }
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
        DataGen dataGen = new DataGen();
        dataGen.genAllData(DataGenConstant.dataType, DataGenConstant.divisionType);

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

        if (parameter == "floor" || parameter == "dataType" || parameter == "diType") {
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
            if (parameter == "floor") {
                result += DataGenConstant.nFloor + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\t" + resultVisitDoorsAve2 + "\n";
            }
            if (parameter == "dataType") {
                result += DataGenConstant.dataType + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\t" + resultVisitDoorsAve2 + "\n";
            }
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
        if ((int) distance == (int) 1500) {
            point1s.addAll(new ArrayList<>(Arrays.asList("1299.0,1127.0,1", "214.0,242.0,2", "1159.0,1206.0,0", "1327.0,1161.0,1", "79.0,164.0,1", "1235.0,36.0,1", "58.0,226.0,1", "1180.0,1210.0,2", "77.0,1355.0,1", "138.0,1199.0,0")));
            point2s.addAll(new ArrayList<>(Arrays.asList("210.0,545.0,2", "1125.0,1346.0,2", "204.0,370.0,1", "440.0,380.0,0", "1044.0,868.0,2", "592.0,973.0,0", "852.0,887.0,2", "1242.0,1318.0,1", "1116.0,331.0,1", "1121.0,1367.0,2")));

        }
        if ((int) distance == (int) 1300) {
            point1s.addAll(new ArrayList<>(Arrays.asList("1299.0,1127.0,1", "214.0,242.0,2", "1159.0,1206.0,0", "1327.0,1161.0,1", "79.0,164.0,1", "1235.0,36.0,1", "58.0,226.0,1", "1180.0,1210.0,2", "77.0,1355.0,1", "138.0,1199.0,0")));
            point2s.addAll(new ArrayList<>(Arrays.asList("1032.0,240.0,0", "193.0,12.0,0", "214.0,574.0,2", "1071.0,197.0,2", "297.0,329.0,0", "442.0,191.0,0", "613.0,1059.0,2", "213.0,530.0,2", "1252.0,1168.0,1", "1165.0,629.0,1")));

        }
        if ((int) distance == (int) 1100) {
//            point1s.addAll(new ArrayList<>(Arrays.asList("1321.0,247.0,3", "565.0,910.0,4", "210.0,718.0,2", "939.0,963.0,3", "898.0,1182.0,4", "1331.0,1096.0,4", "175.0,629.0,2", "244.0,972.0,1", "157.0,1327.0,2", "979.0,1185.0,4")));
//            point2s.addAll(new ArrayList<>(Arrays.asList("973.0,427.0,0", "207.0,1141.0,2", "1143.0,1200.0,4", "356.0,529.0,0", "271.0,1114.0,1", "420.0,982.0,4", "1207.0,831.0,2", "129.0,124.0,4", "260.0,987.0,3", "524.0,579.0,3")));

            point1s.addAll(new ArrayList<>(Arrays.asList("1299.0,1127.0,1", "214.0,242.0,2", "1159.0,1206.0,0", "1327.0,1161.0,1", "79.0,164.0,1", "1235.0,36.0,1", "58.0,226.0,1", "1180.0,1210.0,2", "77.0,1355.0,1", "138.0,1199.0,0")));
            point2s.addAll(new ArrayList<>(Arrays.asList("491.0,800.0,0", "1027.0,147.0,2", "180.0,759.0,0", "1197.0,831.0,2", "590.0,881.0,1", "1026.0,428.0,0", "562.0,785.0,2", "802.0,595.0,0", "853.0,566.0,1", "399.0,931.0,1")));

        }
        if ((int) distance == (int) 1700) {
//            point1s.addAll(new ArrayList<>(Arrays.asList("1365.0,1312.0,0", "293.0,257.0,3", "126.0,88.0,2", "1259.0,1098.0,3", "402.0,1198.0,2", "1081.0,195.0,0", "416.0,1037.0,0", "271.0,1336.0,4", "1249.0,213.0,2", "1015.0,283.0,3")));
//            point2s.addAll(new ArrayList<>(Arrays.asList("1154.0,25.0,1", "1330.0,1287.0,4", "996.0,1098.0,4", "228.0,310.0,4", "1287.0,228.0,3", "91.0,1157.0,3", "1322.0,68.0,4", "1069.0,386.0,0", "67.0,1136.0,2", "170.0,1340.0,1")));


            point1s.addAll(new ArrayList<>(Arrays.asList("1299.0,1127.0,1", "214.0,242.0,2", "1159.0,1206.0,0", "1327.0,1161.0,1", "79.0,164.0,1", "1235.0,36.0,1", "58.0,226.0,1", "1180.0,1210.0,2", "77.0,1355.0,1", "138.0,1199.0,0")));
            point2s.addAll(new ArrayList<>(Arrays.asList("212.0,333.0,2", "1204.0,1059.0,1", "66.0,24.0,0", "348.0,293.0,0", "12.0,11.0,0", "421.0,1181.0,2", "1130.0,1316.0,1", "279.0,299.0,0", "5.0,106.0,2", "1208.0,419.0,2")));

        }
        if ((int) distance == (int) 1900) {
//            point1s.addAll(new ArrayList<>(Arrays.asList("1289.0,249.0,0", "1219.0,1167.0,0", "1090.0,1334.0,2", "1308.0,1098.0,3", "53.0,1196.0,4", "1271.0,254.0,2", "77.0,109.0,1", "266.0,1356.0,4", "1097.0,1344.0,3", "1169.0,1112.0,1")));
//            point2s.addAll(new ArrayList<>(Arrays.asList("301.0,1172.0,4", "165.0,93.0,3", "8.0,264.0,4", "340.0,187.0,0", "1310.0,253.0,2", "253.0,1178.0,4", "1141.0,1281.0,4", "1175.0,143.0,0", "145.0,113.0,4", "42.0,36.0,3")));

            point1s.addAll(new ArrayList<>(Arrays.asList("1299.0,1127.0,1", "214.0,242.0,2", "1159.0,1206.0,0", "1327.0,1161.0,1", "79.0,164.0,1", "1235.0,36.0,1", "58.0,226.0,1", "1180.0,1210.0,2", "77.0,1355.0,1", "138.0,1199.0,0")));
            point2s.addAll(new ArrayList<>(Arrays.asList("113.0,153.0,2", "1359.0,1221.0,0", "84.0,145.0,1", "13.0,244.0,0", "1190.0,1227.0,0", "209.0,1183.0,0", "1059.0,1180.0,2", "239.0,58.0,1", "1165.0,134.0,0", "1360.0,118.0,2")));

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

//        // distance
//        String fileName = "/results/" + DataGenConstant.dataset + "/Dist_SPQ.csv";
//        spq.IDModel_SPQ("distance", fileName);
//        spq.IDMatrix_SPQ("distance", fileName);
//        spq.ICIndex_spq("distance", fileName);
//        spq.IPtree_SPQ("distance", fileName);
//        spq.VIPtree_SPQ("distance", fileName);
//

        // floor
//        String fileName = "/results/" + DataGenConstant.dataset + "/Floor_SPQ.csv";
//        spq.IDModel_SPQ("floor", fileName);
//        spq.IDMatrix_SPQ("floor", fileName);
//        spq.ICIndex_spq("floor", fileName);
//        spq.IPtree_SPQ("floor", fileName);
//        spq.VIPtree_SPQ("floor", fileName);
//

        // dataType
        String fileName = "/results/" + DataGenConstant.dataset + "/dataType_SPQ.csv";
//        spq.IDModel_SPQ("dataType", fileName);
//        spq.IDMatrix_SPQ("dataType", fileName);
//        spq.ICIndex_spq("dataType", fileName);
//        spq.IPtree_SPQ("dataType", fileName);
        spq.VIPtree_SPQ("dataType", fileName);


        // diType
//        String fileName = "/results/" + DataGenConstant.dataset + "/diType_SPQ.csv";
//        spq.IDModel_SPQ("diType", fileName);
//        spq.IDMatrix_SPQ("diType", fileName);
//        spq.ICIndex_spq("diType", fileName);
//        spq.IPtree_SPQ("diType", fileName);
//        spq.VIPtree_SPQ("diType", fileName);
    }

}
