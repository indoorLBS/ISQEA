package dk.aau.cs.indoorqueries.syn.test.experiments;

import com.github.davidmoten.rtree3d.Entry;
import com.github.davidmoten.rtree3d.RTree;
import com.github.davidmoten.rtree3d.geometry.Box;
import dk.aau.cs.indoorqueries.common.algorithm.*;
import dk.aau.cs.indoorqueries.common.iDMatrix.IndexMatrixGen;
import dk.aau.cs.indoorqueries.common.iDModel.GenTopology;
import dk.aau.cs.indoorqueries.common.iPTree.VIPTree;
import dk.aau.cs.indoorqueries.common.indoorEntitity.IndoorSpace;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Partition;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Point;
import dk.aau.cs.indoorqueries.common.rStarTree3D.TreeNode;
import dk.aau.cs.indoorqueries.common.utilities.DataGenConstant;
import dk.aau.cs.indoorqueries.syn.datagenerate.ClassifyPartition;
import dk.aau.cs.indoorqueries.syn.datagenerate.DataGen;
import dk.aau.cs.indoorqueries.syn.datagenerate.ObjectGen;
import rx.Observable;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * experiments for RQ
 * @author Tiantian Liu
 */
public class RQ {
    public int rDefault = 600;

    public void IDModel_rq(String parameter, String fileName, int num) throws IOException {
        DataGen dataGen = new DataGen();
        dataGen.genAllData(DataGenConstant.dataType, DataGenConstant.divisionType);

        GenTopology genTopology = new GenTopology();
        genTopology.genTopology();

        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        long startMem = runtime.totalMemory() - runtime.freeMemory();

        String outFile = System.getProperty("user.dir") + fileName;

        String result = "";
        if (parameter == "range") {
            ObjectGen objectGen = new ObjectGen();
            objectGen.readObjects(num);

            result += "IDModel_RQ" + "\n";
            result += "r" + "\t" + "time(ns)" + "\t" + "memory" + "\n";

            int[] rList = {200, 400, 600, 800, 1000};
            for (int i = 0; i >= 0; i--) {
                int r = rList[4];
                ArrayList<ArrayList<Point>> pointPairs = getPointPairs();

                long resultTimeSum2 = 0;
                long resultMenSum2 = 0;
                long resultTimeAve2 = 0;
                long resultMenAve2 = 0;


                for (int j = 0; j < 10; j++) {
                    ArrayList<Point> pointPair = pointPairs.get(j);
                    IDModel_RQ idModel_rq = new IDModel_RQ();
                    long resultTimeSum1 = 0;
                    long resultMenSum1 = 0;
                    long resultTimeAve1 = 0;
                    long resultMenAve1 = 0;

                    idModel_rq.range(pointPair.get(1), r);
                    for (int h = 0; h < 10; h++) {
//                        Runtime runtime = Runtime.getRuntime();
//                        runtime.gc();
//                        long startMem = runtime.totalMemory() - runtime.freeMemory();
                        long start = System.nanoTime();

                        idModel_rq.range(pointPair.get(1), r);
                        long end = System.nanoTime();
                        long endMem = runtime.totalMemory() - runtime.freeMemory();

                        long time = (end - start) / 1000;
                        long memory = (endMem - startMem) / 1024 / 1000;

                        resultTimeSum1 += time;
                        resultMenSum1 += memory;
                    }
                    resultTimeAve1 = resultTimeSum1 / 10;
                    resultMenAve1 = resultMenSum1 / 10;

                    resultTimeSum2 += resultTimeAve1;
                    resultMenSum2 += resultMenAve1;
                }

                resultTimeAve2 = resultTimeSum2 / 10;
                resultMenAve2 = resultMenSum2 / 10;
                result += r + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\n";
            }

        }

        if (parameter == "objectNum") {
            ObjectGen objectGen = new ObjectGen();
            objectGen.readObjects(num);

            result += "IDModel_RQ" + "\n";
            result += "objectNum" + "\t" + "time(ns)" + "\t" + "memory" + "\n";


            int r = rDefault;
            ArrayList<ArrayList<Point>> pointPairs = getPointPairs();

            long resultTimeSum2 = 0;
            long resultMenSum2 = 0;
            long resultTimeAve2 = 0;
            long resultMenAve2 = 0;

            for (int j = 0; j < 10; j++) {
                ArrayList<Point> pointPair = pointPairs.get(j);
                IDModel_RQ idModel_rq = new IDModel_RQ();
                long resultTimeSum1 = 0;
                long resultMenSum1 = 0;
                long resultTimeAve1 = 0;
                long resultMenAve1 = 0;
                idModel_rq.range(pointPair.get(1), r);
                for (int h = 0; h < 10; h++) {
//                    Runtime runtime = Runtime.getRuntime();
//                    runtime.gc();
//                    long startMem = runtime.totalMemory() - runtime.freeMemory();
                    long start = System.nanoTime();

                    idModel_rq.range(pointPair.get(1), r);
                    long end = System.nanoTime();
                    long endMem = runtime.totalMemory() - runtime.freeMemory();

                    long time = (end - start) / 1000;
                    long memory = (endMem - startMem) / 1024 / 1000;

                    resultTimeSum1 += time;
                    resultMenSum1 += memory;
                }
                resultTimeAve1 = resultTimeSum1 / 10;
                resultMenAve1 = resultMenSum1 / 10;

                resultTimeSum2 += resultTimeAve1;
                resultMenSum2 += resultMenAve1;
            }

            resultTimeAve2 = resultTimeSum2 / 10;
            resultMenAve2 = resultMenSum2 / 10;
            result += num + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\n";

        }

        if (parameter == "floor" || parameter == "dataType" || parameter == "diType") {
            ObjectGen objectGen = new ObjectGen();
            objectGen.readObjects(num);

            result += "IDModel_RQ" + "\n";
            result += parameter + "\t" + "time(ns)" + "\t" + "memory" + "\n";


            int r = rDefault;
            ArrayList<ArrayList<Point>> pointPairs = getPointPairs();

            long resultTimeSum2 = 0;
            long resultMenSum2 = 0;
            long resultTimeAve2 = 0;
            long resultMenAve2 = 0;

            for (int j = 0; j < 10; j++) {
                ArrayList<Point> pointPair = pointPairs.get(j);
                IDModel_RQ idModel_rq = new IDModel_RQ();
                long resultTimeSum1 = 0;
                long resultMenSum1 = 0;
                long resultTimeAve1 = 0;
                long resultMenAve1 = 0;
                idModel_rq.range(pointPair.get(1), r);
                for (int h = 0; h < 10; h++) {
//                    Runtime runtime = Runtime.getRuntime();
//                    runtime.gc();
//                    long startMem = runtime.totalMemory() - runtime.freeMemory();
                    long start = System.nanoTime();

                    idModel_rq.range(pointPair.get(1), r);
                    long end = System.nanoTime();
                    long endMem = runtime.totalMemory() - runtime.freeMemory();

                    long time = (end - start) / 1000;
                    long memory = (endMem - startMem) / 1024 / 1000;

                    resultTimeSum1 += time;
                    resultMenSum1 += memory;
                }
                resultTimeAve1 = resultTimeSum1 / 10;
                resultMenAve1 = resultMenSum1 / 10;

                resultTimeSum2 += resultTimeAve1;
                resultMenSum2 += resultMenAve1;
            }

            resultTimeAve2 = resultTimeSum2 / 10;
            resultMenAve2 = resultMenSum2 / 10;
            if (parameter == "floor") {
                result += DataGenConstant.nFloor + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\n";
            }
            if (parameter == "dataType") {
                result += DataGenConstant.dataType + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\n";
            }
            if (parameter == "diType") {
                result += DataGenConstant.divisionType + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\n";
            }

        }

        FileOutputStream output = new FileOutputStream(outFile, true);
        output.write(result.getBytes());
        output.flush();
        output.close();


    }

    public void IDMatrix_rq(String parameter, String fileName, int num) throws IOException {
        DataGen dataGen = new DataGen();
        dataGen.genAllData(DataGenConstant.dataType, DataGenConstant.divisionType);

        GenTopology genTopology = new GenTopology();
        genTopology.genTopology();

        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        long startMem = runtime.totalMemory() - runtime.freeMemory();

//        DistMatrixGen distMatrixGen = new DistMatrixGen();
//        distMatrixGen.readDist();

        IndexMatrixGen indexMatrixGen = new IndexMatrixGen();
        indexMatrixGen.indexMatrixGen();

        ArrayList<ArrayList<ArrayList<Double>>> indexMatrix = indexMatrixGen.getIndexList();
        HashMap<String, Double> d2dDistMap = indexMatrixGen.getD2dDistMap();

        String outFile = System.getProperty("user.dir") + fileName;

        String result = "";
        if (parameter == "range") {
            ObjectGen objectGen = new ObjectGen();
            objectGen.readObjects(num);

            result += "IDMatrix_RQ" + "\n";
            result += "r" + "\t" + "time(ns)" + "\t" + "memory" + "\n";

            int[] rList = {200, 400, 600, 800, 1000};
            for (int i = 0; i >= 0; i--) {
                int r = rList[4];
                ArrayList<ArrayList<Point>> pointPairs = getPointPairs();

                long resultTimeSum2 = 0;
                long resultMenSum2 = 0;
                long resultTimeAve2 = 0;
                long resultMenAve2 = 0;

                for (int j = 0; j < 10; j++) {
                    ArrayList<Point> pointPair = pointPairs.get(j);
                    IDMatrix_RQ idMatrix_rq = new IDMatrix_RQ();
                    long resultTimeSum1 = 0;
                    long resultMenSum1 = 0;
                    long resultTimeAve1 = 0;
                    long resultMenAve1 = 0;
                    idMatrix_rq.rangeQuery(pointPair.get(1), r, indexMatrix, d2dDistMap);
                    for (int h = 0; h < 10; h++) {
//                        Runtime runtime = Runtime.getRuntime();
//                        runtime.gc();
//                        long startMem = runtime.totalMemory() - runtime.freeMemory();
                        long start = System.nanoTime();

                        idMatrix_rq.rangeQuery(pointPair.get(1), r, indexMatrix, d2dDistMap);
                        long end = System.nanoTime();
                        long endMem = runtime.totalMemory() - runtime.freeMemory();

                        long time = (end - start) / 1000;
                        long memory = (endMem - startMem) / 1024 / 1000;

                        resultTimeSum1 += time;
                        resultMenSum1 += memory;
                    }
                    resultTimeAve1 = resultTimeSum1 / 10;
                    resultMenAve1 = resultMenSum1 / 10;

                    resultTimeSum2 += resultTimeAve1;
                    resultMenSum2 += resultMenAve1;
                }

                resultTimeAve2 = resultTimeSum2 / 10;
                resultMenAve2 = resultMenSum2 / 10;
                result += r + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\n";
            }

        }

        if (parameter == "objectNum") {
            ObjectGen objectGen = new ObjectGen();
            objectGen.readObjects(num);

            result += "IDMatrix_RQ" + "\n";
            result += "objectNum" + "\t" + "time(ns)" + "\t" + "memory" + "\n";


            int r = rDefault;
            ArrayList<ArrayList<Point>> pointPairs = getPointPairs();

            long resultTimeSum2 = 0;
            long resultMenSum2 = 0;
            long resultTimeAve2 = 0;
            long resultMenAve2 = 0;

            for (int j = 0; j < 10; j++) {
                ArrayList<Point> pointPair = pointPairs.get(j);
                IDMatrix_RQ idMatrix_rq = new IDMatrix_RQ();
                long resultTimeSum1 = 0;
                long resultMenSum1 = 0;
                long resultTimeAve1 = 0;
                long resultMenAve1 = 0;
                idMatrix_rq.rangeQuery(pointPair.get(1), r, indexMatrix, d2dDistMap);
                for (int h = 0; h < 10; h++) {
//                    Runtime runtime = Runtime.getRuntime();
//                    runtime.gc();
//                    long startMem = runtime.totalMemory() - runtime.freeMemory();
                    long start = System.nanoTime();

                    idMatrix_rq.rangeQuery(pointPair.get(1), r, indexMatrix, d2dDistMap);
                    long end = System.nanoTime();
                    long endMem = runtime.totalMemory() - runtime.freeMemory();

                    long time = (end - start) / 1000;
                    long memory = (endMem - startMem) / 1024 / 1000;

                    resultTimeSum1 += time;
                    resultMenSum1 += memory;
                }
                resultTimeAve1 = resultTimeSum1 / 10;
                resultMenAve1 = resultMenSum1 / 10;

                resultTimeSum2 += resultTimeAve1;
                resultMenSum2 += resultMenAve1;
            }

            resultTimeAve2 = resultTimeSum2 / 10;
            resultMenAve2 = resultMenSum2 / 10;
            result += num + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\n";

        }

        if (parameter == "floor" || parameter == "dataType" || parameter == "diType") {
            ObjectGen objectGen = new ObjectGen();
            objectGen.readObjects(num);

            result += "IDMatrix_RQ" + "\n";
            result += parameter + "\t" + "time(ns)" + "\t" + "memory" + "\n";


            int r = rDefault;
            ArrayList<ArrayList<Point>> pointPairs = getPointPairs();

            long resultTimeSum2 = 0;
            long resultMenSum2 = 0;
            long resultTimeAve2 = 0;
            long resultMenAve2 = 0;

            for (int j = 0; j < 10; j++) {
                ArrayList<Point> pointPair = pointPairs.get(j);
                IDMatrix_RQ idMatrix_rq = new IDMatrix_RQ();
                long resultTimeSum1 = 0;
                long resultMenSum1 = 0;
                long resultTimeAve1 = 0;
                long resultMenAve1 = 0;
                idMatrix_rq.rangeQuery(pointPair.get(1), r, indexMatrix, d2dDistMap);
                for (int h = 0; h < 10; h++) {
//                    Runtime runtime = Runtime.getRuntime();
//                    runtime.gc();
//                    long startMem = runtime.totalMemory() - runtime.freeMemory();
                    long start = System.nanoTime();

                    idMatrix_rq.rangeQuery(pointPair.get(1), r, indexMatrix, d2dDistMap);
                    long end = System.nanoTime();
                    long endMem = runtime.totalMemory() - runtime.freeMemory();

                    long time = (end - start) / 1000;
                    long memory = (endMem - startMem) / 1024 / 1000;

                    resultTimeSum1 += time;
                    resultMenSum1 += memory;
                }
                resultTimeAve1 = resultTimeSum1 / 10;
                resultMenAve1 = resultMenSum1 / 10;

                resultTimeSum2 += resultTimeAve1;
                resultMenSum2 += resultMenAve1;
            }

            resultTimeAve2 = resultTimeSum2 / 10;
            resultMenAve2 = resultMenSum2 / 10;
            if (parameter == "floor") {
                result += DataGenConstant.nFloor + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\n";
            }
            if (parameter == "dataType") {
                result += DataGenConstant.dataType + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\n";
            }
            if (parameter == "diType") {
                result += DataGenConstant.divisionType + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\n";
            }

        }

        FileOutputStream output = new FileOutputStream(outFile, true);
        output.write(result.getBytes());
        output.flush();
        output.close();


    }

    public void ICIndex_rq(String parameter, String fileName, int num) throws IOException {
        DataGen dataGen = new DataGen();
        dataGen.genAllData(DataGenConstant.dataType, DataGenConstant.divisionType);

        GenTopology genTopology = new GenTopology();
        genTopology.genTopology();

        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        long startMem = runtime.totalMemory() - runtime.freeMemory();

        final Box bounds = Box.create(0.0, 0.0, 0.0, 10000.0, 10000.0, 0.5);

        // create RTree
        int maxChildren = 20;
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

//        System.out.println("size = " + tree.size());
//        System.out.println("entries = " + tree.countEntries());
//        System.out.println("total boxes size = " + boxes.size());
//        for(int i = 0; i < boxes.size();i ++) {
//            System.out.println();
//
//            Box box = boxes.get(i);
//            System.out.println("Box " + box.cornerToString());
//
//            TreeNode node = box.getmNode();
//            System.out.println("Node " + node.cornerToString() + " id = " + node.getmID());
//
//            Partition partition = node.getmPartition();
//            System.out.println("Partition " + partition.cornerToString3D() + " id = " + partition.getmID());
//        }


        String fileInput = System.getProperty("user.dir") + "/source.r";
        Runtime.getRuntime().exec("/bin/sh R CMD BATCH " + fileInput);
        System.out.println("png");

        String outFile = System.getProperty("user.dir") + fileName;

        String result = "";
        if (parameter == "range") {
            ObjectGen objectGen = new ObjectGen();
            objectGen.readObjects(num);

            result += "ICIndex_RQ" + "\n";
            result += "r" + "\t" + "time(ns)" + "\t" + "memory" + "\n";

            int[] rList = {200, 400, 600, 800, 1000};
            for (int i = 0; i >= 0; i--) {
                int r = rList[4];

                ArrayList<ArrayList<Point>> pointPairs = getPointPairs();

                long resultTimeSum2 = 0;
                long resultMenSum2 = 0;
                long resultTimeAve2 = 0;
                long resultMenAve2 = 0;

                for (int j = 0; j < 10; j++) {
                    ArrayList<Point> pointPair = pointPairs.get(j);
                    ICIndex_RQ icIndex_rq = new ICIndex_RQ();
                    long resultTimeSum1 = 0;
                    long resultMenSum1 = 0;
                    long resultTimeAve1 = 0;
                    long resultMenAve1 = 0;
                    //                icIndex_rq.iRQ(pointPair.get(1), r, tree);
                    for (int h = 0; h < 10; h++) {
                        //                    Runtime runtime = Runtime.getRuntime();
                        //                    runtime.gc();
                        //                    long startMem = runtime.totalMemory() - runtime.freeMemory();
                        long start = System.nanoTime();

                        icIndex_rq.iRQ(pointPair.get(1), r, tree);
                        long end = System.nanoTime();
                        long endMem = runtime.totalMemory() - runtime.freeMemory();

                        long time = (end - start) / 1000;
                        long memory = (endMem - startMem) / 1024 / 1000;

                        resultTimeSum1 += time;
                        resultMenSum1 += memory;
                    }
                    resultTimeAve1 = resultTimeSum1 / 10;
                    resultMenAve1 = resultMenSum1 / 10;

                    resultTimeSum2 += resultTimeAve1;
                    resultMenSum2 += resultMenAve1;
                }

                resultTimeAve2 = resultTimeSum2 / 10;
                resultMenAve2 = resultMenSum2 / 10;
                result += r + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\n";
            }


        }

        if (parameter == "objectNum") {
            ObjectGen objectGen = new ObjectGen();
            objectGen.readObjects(num);

            result += "ICIndex_RQ" + "\n";
            result += "objectNum" + "\t" + "time(ns)" + "\t" + "memory" + "\n";


            int r = rDefault;
            ArrayList<ArrayList<Point>> pointPairs = getPointPairs();

            long resultTimeSum2 = 0;
            long resultMenSum2 = 0;
            long resultTimeAve2 = 0;
            long resultMenAve2 = 0;

            for (int j = 0; j < 10; j++) {
                ArrayList<Point> pointPair = pointPairs.get(j);
                ICIndex_RQ icIndex_rq = new ICIndex_RQ();
                long resultTimeSum1 = 0;
                long resultMenSum1 = 0;
                long resultTimeAve1 = 0;
                long resultMenAve1 = 0;
//                icIndex_rq.iRQ(pointPair.get(1), r, tree);
                for (int h = 0; h < 10; h++) {
//                    Runtime runtime = Runtime.getRuntime();
//                    runtime.gc();
//                    long startMem = runtime.totalMemory() - runtime.freeMemory();
                    long start = System.nanoTime();

                    icIndex_rq.iRQ(pointPair.get(1), r, tree);
                    long end = System.nanoTime();
                    long endMem = runtime.totalMemory() - runtime.freeMemory();

                    long time = (end - start) / 1000;
                    long memory = (endMem - startMem) / 1024 / 1000;

                    resultTimeSum1 += time;
                    resultMenSum1 += memory;
                }
                resultTimeAve1 = resultTimeSum1 / 10;
                resultMenAve1 = resultMenSum1 / 10;

                resultTimeSum2 += resultTimeAve1;
                resultMenSum2 += resultMenAve1;
            }

            resultTimeAve2 = resultTimeSum2 / 10;
            resultMenAve2 = resultMenSum2 / 10;
            result += num + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\n";

        }

        if (parameter == "floor" || parameter == "dataType" || parameter == "diType") {
            ObjectGen objectGen = new ObjectGen();
            objectGen.readObjects(num);

            result += "ICIndex_RQ" + "\n";
            result += parameter + "\t" + "time(ns)" + "\t" + "memory" + "\n";


            int r = rDefault;
            ArrayList<ArrayList<Point>> pointPairs = getPointPairs();

            long resultTimeSum2 = 0;
            long resultMenSum2 = 0;
            long resultTimeAve2 = 0;
            long resultMenAve2 = 0;

            for (int j = 0; j < 10; j++) {
                ArrayList<Point> pointPair = pointPairs.get(j);
                ICIndex_RQ icIndex_rq = new ICIndex_RQ();
                long resultTimeSum1 = 0;
                long resultMenSum1 = 0;
                long resultTimeAve1 = 0;
                long resultMenAve1 = 0;
//                icIndex_rq.iRQ(pointPair.get(1), r, tree);
                for (int h = 0; h < 10; h++) {
//                    Runtime runtime = Runtime.getRuntime();
//                    runtime.gc();
//                    long startMem = runtime.totalMemory() - runtime.freeMemory();
                    long start = System.nanoTime();

                    icIndex_rq.iRQ(pointPair.get(1), r, tree);
                    long end = System.nanoTime();
                    long endMem = runtime.totalMemory() - runtime.freeMemory();

                    long time = (end - start) / 1000;
                    long memory = (endMem - startMem) / 1024 / 1000;

                    resultTimeSum1 += time;
                    resultMenSum1 += memory;
                }
                resultTimeAve1 = resultTimeSum1 / 10;
                resultMenAve1 = resultMenSum1 / 10;

                resultTimeSum2 += resultTimeAve1;
                resultMenSum2 += resultMenAve1;
            }

            resultTimeAve2 = resultTimeSum2 / 10;
            resultMenAve2 = resultMenSum2 / 10;
            if (parameter == "floor") {
                result += DataGenConstant.nFloor + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\n";
            }
            if (parameter == "dataType") {
                result += DataGenConstant.dataType + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\n";
            }
            if (parameter == "diType") {
                result += DataGenConstant.divisionType + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\n";
            }

        }

        FileOutputStream output = new FileOutputStream(outFile, true);
        output.write(result.getBytes());
        output.flush();
        output.close();


    }

    public void IPtree_rq(String parameter, String fileName, int num) throws IOException {
        DataGen dataGen = new DataGen();
        dataGen.genAllData(DataGenConstant.dataType, DataGenConstant.divisionType);

        GenTopology genTopology = new GenTopology();
        genTopology.genTopology();

        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        long startMem = runtime.totalMemory() - runtime.freeMemory();

        ClassifyPartition classifyPartition = new ClassifyPartition();
        classifyPartition.classifyPar();

        VIPTree ipTree = VIPTree.createTree();
        ipTree.initTree();

        String outFile = System.getProperty("user.dir") + fileName;

        String result = "";
        if (parameter == "range") {
            ObjectGen objectGen = new ObjectGen();
            objectGen.readObjects(num);

            ipTree.objectPro();

            result += "IPtree_RQ" + "\n";
            result += "r" + "\t" + "time(ns)" + "\t" + "memory" + "\n";

            int[] rList = {200, 400, 600, 800, 1000};
            for (int i = 0; i >= 0; i--) {
                int r = rList[4];
                ArrayList<ArrayList<Point>> pointPairs = getPointPairs();

                long resultTimeSum2 = 0;
                long resultMenSum2 = 0;
                long resultTimeAve2 = 0;
                long resultMenAve2 = 0;

                for (int j = 0; j < 10; j++) {
                    ArrayList<Point> pointPair = pointPairs.get(j);
                    IPtree_RQ iPtree_rq = new IPtree_RQ();
                    long resultTimeSum1 = 0;
                    long resultMenSum1 = 0;
                    long resultTimeAve1 = 0;
                    long resultMenAve1 = 0;
                    iPtree_rq.ipTreeRQ(pointPair.get(1), r, ipTree);
                    for (int h = 0; h < 10; h++) {
//                        Runtime runtime = Runtime.getRuntime();
//                        runtime.gc();
//                        long startMem = runtime.totalMemory() - runtime.freeMemory();
                        long start = System.nanoTime();

                        iPtree_rq.ipTreeRQ(pointPair.get(1), r, ipTree);
                        long end = System.nanoTime();
                        long endMem = runtime.totalMemory() - runtime.freeMemory();

                        long time = (end - start) / 1000;
                        long memory = (endMem - startMem) / 1024 / 1000;

                        resultTimeSum1 += time;
                        resultMenSum1 += memory;
                    }
                    resultTimeAve1 = resultTimeSum1 / 10;
                    resultMenAve1 = resultMenSum1 / 10;

                    resultTimeSum2 += resultTimeAve1;
                    resultMenSum2 += resultMenAve1;
                }

                resultTimeAve2 = resultTimeSum2 / 10;
                resultMenAve2 = resultMenSum2 / 10;
                result += r + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\n";
            }

        }

        if (parameter == "objectNum") {
            ObjectGen objectGen = new ObjectGen();
            objectGen.readObjects(num);

            ipTree.objectPro();

            result += "IPtree_RQ" + "\n";
            result += "objectNum" + "\t" + "time(ns)" + "\t" + "memory" + "\n";


            int r = rDefault;
            ArrayList<ArrayList<Point>> pointPairs = getPointPairs();

            long resultTimeSum2 = 0;
            long resultMenSum2 = 0;
            long resultTimeAve2 = 0;
            long resultMenAve2 = 0;

            for (int j = 0; j < 10; j++) {
                ArrayList<Point> pointPair = pointPairs.get(j);
                IPtree_RQ iPtree_rq = new IPtree_RQ();
                long resultTimeSum1 = 0;
                long resultMenSum1 = 0;
                long resultTimeAve1 = 0;
                long resultMenAve1 = 0;
                iPtree_rq.ipTreeRQ(pointPair.get(1), r, ipTree);
                for (int h = 0; h < 10; h++) {
//                    Runtime runtime = Runtime.getRuntime();
//                    runtime.gc();
//                    long startMem = runtime.totalMemory() - runtime.freeMemory();
                    long start = System.nanoTime();

                    iPtree_rq.ipTreeRQ(pointPair.get(1), r, ipTree);
                    long end = System.nanoTime();
                    long endMem = runtime.totalMemory() - runtime.freeMemory();

                    long time = (end - start) / 1000;
                    long memory = (endMem - startMem) / 1024 / 1000;

                    resultTimeSum1 += time;
                    resultMenSum1 += memory;
                }
                resultTimeAve1 = resultTimeSum1 / 10;
                resultMenAve1 = resultMenSum1 / 10;

                resultTimeSum2 += resultTimeAve1;
                resultMenSum2 += resultMenAve1;
            }

            resultTimeAve2 = resultTimeSum2 / 10;
            resultMenAve2 = resultMenSum2 / 10;
            result += num + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\n";

        }

        if (parameter == "floor" || parameter == "dataType" || parameter == "diType") {
            ObjectGen objectGen = new ObjectGen();
            objectGen.readObjects(num);

            ipTree.objectPro();

            result += "IPtree_RQ" + "\n";
            result += parameter + "\t" + "time(ns)" + "\t" + "memory" + "\n";


            int r = rDefault;
            ArrayList<ArrayList<Point>> pointPairs = getPointPairs();

            long resultTimeSum2 = 0;
            long resultMenSum2 = 0;
            long resultTimeAve2 = 0;
            long resultMenAve2 = 0;

            for (int j = 0; j < 10; j++) {
                ArrayList<Point> pointPair = pointPairs.get(j);
                IPtree_RQ iPtree_rq = new IPtree_RQ();
                long resultTimeSum1 = 0;
                long resultMenSum1 = 0;
                long resultTimeAve1 = 0;
                long resultMenAve1 = 0;
                iPtree_rq.ipTreeRQ(pointPair.get(1), r, ipTree);
                for (int h = 0; h < 10; h++) {
//                    Runtime runtime = Runtime.getRuntime();
//                    runtime.gc();
//                    long startMem = runtime.totalMemory() - runtime.freeMemory();
                    long start = System.nanoTime();

                    iPtree_rq.ipTreeRQ(pointPair.get(1), r, ipTree);
                    long end = System.nanoTime();
                    long endMem = runtime.totalMemory() - runtime.freeMemory();

                    long time = (end - start) / 1000;
                    long memory = (endMem - startMem) / 1024 / 1000;

                    resultTimeSum1 += time;
                    resultMenSum1 += memory;
                }
                resultTimeAve1 = resultTimeSum1 / 10;
                resultMenAve1 = resultMenSum1 / 10;

                resultTimeSum2 += resultTimeAve1;
                resultMenSum2 += resultMenAve1;
            }

            resultTimeAve2 = resultTimeSum2 / 10;
            resultMenAve2 = resultMenSum2 / 10;
            if (parameter == "floor") {
                result += DataGenConstant.nFloor + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\n";
            }
            if (parameter == "dataType") {
                result += DataGenConstant.dataType + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\n";
            }
            if (parameter == "diType") {
                result += DataGenConstant.divisionType + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\n";
            }

        }

        FileOutputStream output = new FileOutputStream(outFile, true);
        output.write(result.getBytes());
        output.flush();
        output.close();

    }

    public void VIPtree_rq(String parameter, String fileName, int num) throws IOException {
        DataGen dataGen = new DataGen();
        dataGen.genAllData(DataGenConstant.dataType, DataGenConstant.divisionType);

        GenTopology genTopology = new GenTopology();
        genTopology.genTopology();

        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        long startMem = runtime.totalMemory() - runtime.freeMemory();

        ClassifyPartition classifyPartition = new ClassifyPartition();
        classifyPartition.classifyPar();

        VIPTree vipTree = VIPTree.createTree();
        vipTree.initVIPtree();

        String outFile = System.getProperty("user.dir") + fileName;

        String result = "";
        if (parameter == "range") {
            ObjectGen objectGen = new ObjectGen();
            objectGen.readObjects(num);

            vipTree.objectPro();

            result += "VIPtree_RQ" + "\n";
            result += "r" + "\t" + "time(ns)" + "\t" + "memory" + "\n";

            int[] rList = {200, 400, 600, 800, 1000};
            for (int i = 0; i >= 0; i--) {
                int r = rList[4];
                ArrayList<ArrayList<Point>> pointPairs = getPointPairs();

                long resultTimeSum2 = 0;
                long resultMenSum2 = 0;
                long resultTimeAve2 = 0;
                long resultMenAve2 = 0;

                for (int j = 0; j < 10; j++) {
                    ArrayList<Point> pointPair = pointPairs.get(j);
                    VIPtree_RQ viPtree_rq = new VIPtree_RQ();
                    long resultTimeSum1 = 0;
                    long resultMenSum1 = 0;
                    long resultTimeAve1 = 0;
                    long resultMenAve1 = 0;
                    viPtree_rq.vipTreeRQ(pointPair.get(1), r, vipTree);
                    for (int h = 0; h < 10; h++) {
//                        Runtime runtime = Runtime.getRuntime();
//                        runtime.gc();
//                        long startMem = runtime.totalMemory() - runtime.freeMemory();
                        long start = System.nanoTime();

                        viPtree_rq.vipTreeRQ(pointPair.get(1), r, vipTree);
                        long end = System.nanoTime();
                        long endMem = runtime.totalMemory() - runtime.freeMemory();

                        long time = (end - start) / 1000;
                        long memory = (endMem - startMem) / 1024 / 1000;

                        resultTimeSum1 += time;
                        resultMenSum1 += memory;
                    }
                    resultTimeAve1 = resultTimeSum1 / 10;
                    resultMenAve1 = resultMenSum1 / 10;

                    resultTimeSum2 += resultTimeAve1;
                    resultMenSum2 += resultMenAve1;
                }

                resultTimeAve2 = resultTimeSum2 / 10;
                resultMenAve2 = resultMenSum2 / 10;
                result += r + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\n";
            }

        }

        if (parameter == "objectNum") {
            ObjectGen objectGen = new ObjectGen();
            objectGen.readObjects(num);

            vipTree.objectPro();

            result += "VIPtree_RQ" + "\n";
            result += "objectNum" + "\t" + "time(ns)" + "\t" + "memory" + "\n";


            int r = rDefault;
            ArrayList<ArrayList<Point>> pointPairs = getPointPairs();

            long resultTimeSum2 = 0;
            long resultMenSum2 = 0;
            long resultTimeAve2 = 0;
            long resultMenAve2 = 0;

            for (int j = 0; j < 10; j++) {
                ArrayList<Point> pointPair = pointPairs.get(j);
                VIPtree_RQ viPtree_rq = new VIPtree_RQ();
                long resultTimeSum1 = 0;
                long resultMenSum1 = 0;
                long resultTimeAve1 = 0;
                long resultMenAve1 = 0;
                viPtree_rq.vipTreeRQ(pointPair.get(1), r, vipTree);
                for (int h = 0; h < 10; h++) {
//                    Runtime runtime = Runtime.getRuntime();
//                    runtime.gc();
//                    long startMem = runtime.totalMemory() - runtime.freeMemory();
                    long start = System.nanoTime();

                    viPtree_rq.vipTreeRQ(pointPair.get(1), r, vipTree);
                    long end = System.nanoTime();
                    long endMem = runtime.totalMemory() - runtime.freeMemory();

                    long time = (end - start) / 1000;
                    long memory = (endMem - startMem) / 1024 / 1000;

                    resultTimeSum1 += time;
                    resultMenSum1 += memory;
                }
                resultTimeAve1 = resultTimeSum1 / 10;
                resultMenAve1 = resultMenSum1 / 10;

                resultTimeSum2 += resultTimeAve1;
                resultMenSum2 += resultMenAve1;
            }

            resultTimeAve2 = resultTimeSum2 / 10;
            resultMenAve2 = resultMenSum2 / 10;
            result += num + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\n";

        }

        if (parameter == "floor" || parameter == "dataType" || parameter == "diType") {
            ObjectGen objectGen = new ObjectGen();
            objectGen.readObjects(num);

            vipTree.objectPro();

            result += "VIPtree_RQ" + "\n";
            result += parameter + "\t" + "time(ns)" + "\t" + "memory" + "\n";


            int r = rDefault;
            ArrayList<ArrayList<Point>> pointPairs = getPointPairs();

            long resultTimeSum2 = 0;
            long resultMenSum2 = 0;
            long resultTimeAve2 = 0;
            long resultMenAve2 = 0;

            for (int j = 0; j < 10; j++) {
                ArrayList<Point> pointPair = pointPairs.get(j);
                VIPtree_RQ viPtree_rq = new VIPtree_RQ();
                long resultTimeSum1 = 0;
                long resultMenSum1 = 0;
                long resultTimeAve1 = 0;
                long resultMenAve1 = 0;
                viPtree_rq.vipTreeRQ(pointPair.get(1), r, vipTree);
                for (int h = 0; h < 10; h++) {
//                    Runtime runtime = Runtime.getRuntime();
//                    runtime.gc();
//                    long startMem = runtime.totalMemory() - runtime.freeMemory();
                    long start = System.nanoTime();

                    viPtree_rq.vipTreeRQ(pointPair.get(1), r, vipTree);
                    long end = System.nanoTime();
                    long endMem = runtime.totalMemory() - runtime.freeMemory();

                    long time = (end - start) / 1000;
                    long memory = (endMem - startMem) / 1024 / 1000;

                    resultTimeSum1 += time;
                    resultMenSum1 += memory;
                }
                resultTimeAve1 = resultTimeSum1 / 10;
                resultMenAve1 = resultMenSum1 / 10;

                resultTimeSum2 += resultTimeAve1;
                resultMenSum2 += resultMenAve1;
            }

            resultTimeAve2 = resultTimeSum2 / 10;
            resultMenAve2 = resultMenSum2 / 10;
            if (parameter == "floor") {
                result += DataGenConstant.nFloor + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\n";
            }
            if (parameter == "dataType") {
                result += DataGenConstant.dataType + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\n";
            }
            if (parameter == "diType") {
                result += DataGenConstant.divisionType + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\n";
            }

        }

        FileOutputStream output = new FileOutputStream(outFile, true);
        output.write(result.getBytes());
        output.flush();
        output.close();

    }

    public ArrayList<ArrayList<Point>> getPointPairs() {
        ArrayList<ArrayList<Point>> pointPairs = new ArrayList<>();
        ArrayList<String> point1s = new ArrayList<>();
        ArrayList<String> point2s = new ArrayList<>();
        point1s.addAll(new ArrayList<>(Arrays.asList("355.0,961.0,0", "1271.0,260.0,2", "17.0,1159.0,1", "1186.0,467.0,1", "1289.0,1259.0,0", "180.0,287.0,2", "13.0,145.0,1", "739.0,168.0,1", "11.0,1081.0,2", "106.0,1189.0,0")));
        point2s.addAll(new ArrayList<>(Arrays.asList("1198.0,128.0,1", "365.0,877.0,0", "248.0,63.0,2", "453.0,1210.0,0", "485.0,209.0,0", "1232.0,4.0,1", "967.0,746.0,2", "1203.0,1297.0,0", "1119.0,1277.0,0", "1265.0,1362.0,1")));

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
        RQ rq = new RQ();
//        // range
//        String fileName = "/results/" + DataGenConstant.dataset + "/range_RQ";
//        rq.IDModel_rq("range", fileName, 1500);
//        rq.IDMatrix_rq("range", fileName, 1500);
//        rq.ICIndex_rq("range", fileName, 1500);
//        rq.IPtree_rq("range", fileName, 1500);
//        rq.VIPtree_rq("range", fileName, 1500);

//        // objectNum
//        String fileName = "/results/" + DataGenConstant.dataset + "/objectNum_RQ";
//        rq.IDModel_rq("objectNum", fileName, 2500);
//        rq.IDMatrix_rq("objectNum", fileName, 2500);
//        rq.ICIndex_rq("objectNum", fileName, 2500);
//        rq.IPtree_rq("objectNum", fileName, 2500);
//        rq.VIPtree_rq("objectNum", fileName, 2500);

        // floor
//        String fileName = "/results/" + DataGenConstant.dataset + "/floor_RQ";
//        rq.IDModel_rq("floor", fileName, 1500);
//        rq.IDMatrix_rq("floor", fileName, 1500);
//        rq.ICIndex_rq("floor", fileName, 1500);
//        rq.IPtree_rq("floor", fileName, 1500);
//        rq.VIPtree_rq("floor", fileName, 1500);

//         dataType
//        String fileName = "/results/" + DataGenConstant.dataset + "/dataType_RQ";
//        rq.IDModel_rq("dataType", fileName, 1500);
//        rq.IDMatrix_rq("dataType", fileName, 1500);
//        rq.ICIndex_rq("dataType", fileName, 1500);
//        rq.IPtree_rq("dataType", fileName, 1500);
//        rq.VIPtree_rq("dataType", fileName, 1500);

        // diType
        String fileName = "/results/" + DataGenConstant.dataset + "/diType_RQ";
//        rq.IDModel_rq("diType", fileName, 1500);
//        rq.IDMatrix_rq("diType", fileName, 1500);
//        rq.ICIndex_rq("diType", fileName, 1500);
        rq.IPtree_rq("diType", fileName, 1500);
//        rq.VIPtree_rq("diType", fileName, 1500);
    }
}
