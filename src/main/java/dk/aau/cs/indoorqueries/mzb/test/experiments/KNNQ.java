package dk.aau.cs.indoorqueries.mzb.test.experiments;

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
import dk.aau.cs.indoorqueries.mzb.datagenerate.ClassifyPartition;
import dk.aau.cs.indoorqueries.mzb.datagenerate.MZBDataGenRead;
import dk.aau.cs.indoorqueries.mzb.datagenerate.ObjectGen;
import rx.Observable;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * expriments for KNNQ
 * @author Tiantian Liu
 */
public class KNNQ {
    public static int kDefault = 10;

    public void IDModel_knnq(String parameter, String fileName, int num, int kValue) throws IOException {
        MZBDataGenRead dateGenReadMen = new MZBDataGenRead();
        dateGenReadMen.dataGen();

        GenTopology genTopology = new GenTopology();
        genTopology.genTopology();

        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        long startMem = runtime.totalMemory() - runtime.freeMemory();

        String outFile = System.getProperty("user.dir") + fileName;

        String result = "";
        if (parameter == "k") {
            ObjectGen objectGen = new ObjectGen();
            objectGen.readObjects(num);

            result += "IDModel_KNNQ" + "\n";
            result += "k" + "\t" + "time(ns)" + "\t" + "memory" + "\n";

//            int[] kList = {1, 5, 10, 50, 100};

            int k = kValue;
            ArrayList<ArrayList<Point>> pointPairs = getPointPairs();

            long resultTimeSum2 = 0;
            long resultMenSum2 = 0;
            long resultTimeAve2 = 0;
            long resultMenAve2 = 0;

            for (int j = 0; j < 10; j++) {
                ArrayList<Point> pointPair = pointPairs.get(j);
                IDModel_KNN idModel_knn = new IDModel_KNN();
                long resultTimeSum1 = 0;
                long resultMenSum1 = 0;
                long resultTimeAve1 = 0;
                long resultMenAve1 = 0;
                idModel_knn.knn(pointPair.get(1), k);
                for (int h = 0; h < 10; h++) {
//                        Runtime runtime = Runtime.getRuntime();
//                        runtime.gc();
//                        long startMem = runtime.totalMemory() - runtime.freeMemory();
                    long start = System.nanoTime();

                    idModel_knn.knn(pointPair.get(1), k);
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
            result += k + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\n";


        }

        if (parameter == "objectNum" || parameter == "diType") {
            ObjectGen objectGen = new ObjectGen();
            objectGen.readObjects(num);

            result += "IDModel_KNNQ" + "\n";
            result += parameter + "\t" + "time(ns)" + "\t" + "memory" + "\n";


            int k = kDefault;
            ArrayList<ArrayList<Point>> pointPairs = getPointPairs();

            long resultTimeSum2 = 0;
            long resultMenSum2 = 0;
            long resultTimeAve2 = 0;
            long resultMenAve2 = 0;

            for (int j = 0; j < 10; j++) {
                ArrayList<Point> pointPair = pointPairs.get(j);
                IDModel_KNN idModel_knn = new IDModel_KNN();
                long resultTimeSum1 = 0;
                long resultMenSum1 = 0;
                long resultTimeAve1 = 0;
                long resultMenAve1 = 0;
                idModel_knn.knn(pointPair.get(1), k);
                for (int h = 0; h < 10; h++) {
//                    Runtime runtime = Runtime.getRuntime();
//                    runtime.gc();
//                    long startMem = runtime.totalMemory() - runtime.freeMemory();
                    long start = System.nanoTime();

                    idModel_knn.knn(pointPair.get(1), k);
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
            if (parameter == "objectNum") {
                result += num + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\n";
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

    public void IDMatrix_knnq(String parameter, String fileName, int num, int kValue) throws IOException {
        MZBDataGenRead dateGenReadMen = new MZBDataGenRead();
        dateGenReadMen.dataGen();

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
        if (parameter == "k") {
            ObjectGen objectGen = new ObjectGen();
            objectGen.readObjects(num);

            result += "IDMatrix_KNNQ" + "\n";
            result += "k" + "\t" + "time(ns)" + "\t" + "memory" + "\n";

//            int[] kList = {1, 5, 10, 50, 100};


            int k = kValue;
            ArrayList<ArrayList<Point>> pointPairs = getPointPairs();
//
            long resultTimeSum2 = 0;
            long resultMenSum2 = 0;
            long resultTimeAve2 = 0;
            long resultMenAve2 = 0;

            for (int j = 0; j < 10; j++) {
                ArrayList<Point> pointPair = pointPairs.get(j);
                IDMatrix_KNN idMatrix_knn = new IDMatrix_KNN();
                long resultTimeSum1 = 0;
                long resultMenSum1 = 0;
                long resultTimeAve1 = 0;
                long resultMenAve1 = 0;
                idMatrix_knn.knnQuery(pointPair.get(1), k, indexMatrix, d2dDistMap);
                for (int h = 0; h < 10; h++) {
//                        Runtime runtime = Runtime.getRuntime();
//                        runtime.gc();
//                        long startMem = runtime.totalMemory() - runtime.freeMemory();
                    long start = System.nanoTime();

                    idMatrix_knn.knnQuery(pointPair.get(1), k, indexMatrix, d2dDistMap);
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
            result += k + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\n";


        }

        if (parameter == "objectNum" || parameter == "diType") {
            ObjectGen objectGen = new ObjectGen();
            objectGen.readObjects(num);

            result += "IDMatrix_KNNQ" + "\n";
            result += parameter + "\t" + "time(ns)" + "\t" + "memory" + "\n";


            int k = kDefault;
            ArrayList<ArrayList<Point>> pointPairs = getPointPairs();

            long resultTimeSum2 = 0;
            long resultMenSum2 = 0;
            long resultTimeAve2 = 0;
            long resultMenAve2 = 0;

            for (int j = 0; j < 10; j++) {
                ArrayList<Point> pointPair = pointPairs.get(j);
                IDMatrix_KNN idMatrix_knn = new IDMatrix_KNN();
                long resultTimeSum1 = 0;
                long resultMenSum1 = 0;
                long resultTimeAve1 = 0;
                long resultMenAve1 = 0;
                idMatrix_knn.knnQuery(pointPair.get(1), k, indexMatrix, d2dDistMap);
                for (int h = 0; h < 10; h++) {
//                    Runtime runtime = Runtime.getRuntime();
//                    runtime.gc();
//                    long startMem = runtime.totalMemory() - runtime.freeMemory();
                    long start = System.nanoTime();

                    idMatrix_knn.knnQuery(pointPair.get(1), k, indexMatrix, d2dDistMap);
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
            if (parameter == "objectNum") {
                result += num + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\n";
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

    public void ICIndex_knnq(String parameter, String fileName, int num, int kValue) throws IOException {
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
        if (parameter == "k") {
            ObjectGen objectGen = new ObjectGen();
            objectGen.readObjects(num);

            result += "ICIndex_KNNQ" + "\n";
            result += "k" + "\t" + "time(ns)" + "\t" + "memory" + "\n";

//            int[] kList = {1, 5, 10, 50, 100};

            int k = kValue;

            ArrayList<ArrayList<Point>> pointPairs = getPointPairs();

            long resultTimeSum2 = 0;
            long resultMenSum2 = 0;
            long resultTimeAve2 = 0;
            long resultMenAve2 = 0;

            for (int j = 0; j < 10; j++) {
                ArrayList<Point> pointPair = pointPairs.get(j);
                ICIndex_KNN icIndex_knn = new ICIndex_KNN();
                long resultTimeSum1 = 0;
                long resultMenSum1 = 0;
                long resultTimeAve1 = 0;
                long resultMenAve1 = 0;
                for (int h = 0; h < 10; h++) {
//                    Runtime runtime = Runtime.getRuntime();
//                    runtime.gc();
//                    long startMem = runtime.totalMemory() - runtime.freeMemory();
                    long start = System.nanoTime();

                    icIndex_knn.iKNN(pointPair.get(1), k, tree);
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
            result += k + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\n";


        }

        if (parameter == "objectNum" || parameter == "diType") {
            ObjectGen objectGen = new ObjectGen();
            objectGen.readObjects(num);

            result += "ICIndex_KNNQ" + "\n";
            result += parameter + "\t" + "time(ns)" + "\t" + "memory" + "\n";


            int k = kDefault;
            ArrayList<ArrayList<Point>> pointPairs = getPointPairs();

            long resultTimeSum2 = 0;
            long resultMenSum2 = 0;
            long resultTimeAve2 = 0;
            long resultMenAve2 = 0;

            for (int j = 0; j < 10; j++) {
                ArrayList<Point> pointPair = pointPairs.get(j);
                ICIndex_KNN icIndex_knn = new ICIndex_KNN();
                long resultTimeSum1 = 0;
                long resultMenSum1 = 0;
                long resultTimeAve1 = 0;
                long resultMenAve1 = 0;
                for (int h = 0; h < 10; h++) {
//                    Runtime runtime = Runtime.getRuntime();
//                    runtime.gc();
//                    long startMem = runtime.totalMemory() - runtime.freeMemory();
                    long start = System.nanoTime();

                    icIndex_knn.iKNN(pointPair.get(1), k, tree);
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
            if (parameter == "objectNum") {
                result += num + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\n";
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

    public void IPtree_knnq(String parameter, String fileName, int num, int kValue) throws IOException {
        MZBDataGenRead dateGenReadMen = new MZBDataGenRead();
        dateGenReadMen.dataGen();

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
        if (parameter == "k") {
            ObjectGen objectGen = new ObjectGen();
            objectGen.readObjects(num);

            ipTree.objectPro();

            result += "IPtree_KNNQ" + "\n";
            result += "k" + "\t" + "time(ns)" + "\t" + "memory" + "\n";


            int k = kValue;
            ArrayList<ArrayList<Point>> pointPairs = getPointPairs();

            long resultTimeSum2 = 0;
            long resultMenSum2 = 0;
            long resultTimeAve2 = 0;
            long resultMenAve2 = 0;

            for (int j = 0; j < 10; j++) {
                ArrayList<Point> pointPair = pointPairs.get(j);
                IPtree_KNN iPtree_knn = new IPtree_KNN();
                long resultTimeSum1 = 0;
                long resultMenSum1 = 0;
                long resultTimeAve1 = 0;
                long resultMenAve1 = 0;
                iPtree_knn.iptreeKNN(pointPair.get(1), k, ipTree);
                for (int h = 0; h < 10; h++) {
//                        Runtime runtime = Runtime.getRuntime();
//                        runtime.gc();
//                        long startMem = runtime.totalMemory() - runtime.freeMemory();
                    long start = System.nanoTime();

                    iPtree_knn.iptreeKNN(pointPair.get(1), k, ipTree);
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
            result += k + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\n";


        }

        if (parameter == "objectNum" || parameter == "diType") {
            ObjectGen objectGen = new ObjectGen();
            objectGen.readObjects(num);

            ipTree.objectPro();

            result += "IPtree_KNNQ" + "\n";
            result += parameter + "\t" + "time(ns)" + "\t" + "memory" + "\n";


            int k = kDefault;
            ArrayList<ArrayList<Point>> pointPairs = getPointPairs();

            long resultTimeSum2 = 0;
            long resultMenSum2 = 0;
            long resultTimeAve2 = 0;
            long resultMenAve2 = 0;

            for (int j = 0; j < 10; j++) {
                ArrayList<Point> pointPair = pointPairs.get(j);
                IPtree_KNN iPtree_knn = new IPtree_KNN();
                long resultTimeSum1 = 0;
                long resultMenSum1 = 0;
                long resultTimeAve1 = 0;
                long resultMenAve1 = 0;
                iPtree_knn.iptreeKNN(pointPair.get(1), k, ipTree);
                for (int h = 0; h < 10; h++) {
//                    Runtime runtime = Runtime.getRuntime();
//                    runtime.gc();
//                    long startMem = runtime.totalMemory() - runtime.freeMemory();
                    long start = System.nanoTime();

                    iPtree_knn.iptreeKNN(pointPair.get(1), k, ipTree);
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
            if (parameter == "objectNum") {
                result += num + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\n";
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

    public void VIPtree_knnq(String parameter, String fileName, int num, int kValue) throws IOException {
        MZBDataGenRead dateGenReadMen = new MZBDataGenRead();
        dateGenReadMen.dataGen();

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
        if (parameter == "k") {
            ObjectGen objectGen = new ObjectGen();
            objectGen.readObjects(num);

            vipTree.objectPro();

            result += "VIPtree_KNNQ" + "\n";
            result += "k" + "\t" + "time(ns)" + "\t" + "memory" + "\n";


            int k = kValue;
            ArrayList<ArrayList<Point>> pointPairs = getPointPairs();

            long resultTimeSum2 = 0;
            long resultMenSum2 = 0;
            long resultTimeAve2 = 0;
            long resultMenAve2 = 0;

            for (int j = 0; j < 10; j++) {
                ArrayList<Point> pointPair = pointPairs.get(j);
                VIPtree_KNN viPtree_knn = new VIPtree_KNN();
                long resultTimeSum1 = 0;
                long resultMenSum1 = 0;
                long resultTimeAve1 = 0;
                long resultMenAve1 = 0;
                viPtree_knn.vipTreeKNN(pointPair.get(1), k, vipTree);
                for (int h = 0; h < 10; h++) {
//                        Runtime runtime = Runtime.getRuntime();
//                        runtime.gc();
//                        long startMem = runtime.totalMemory() - runtime.freeMemory();
                    long start = System.nanoTime();

                    viPtree_knn.vipTreeKNN(pointPair.get(1), k, vipTree);
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
            result += k + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\n";


        }

        if (parameter == "objectNum" || parameter == "diType") {
            ObjectGen objectGen = new ObjectGen();
            objectGen.readObjects(num);

            vipTree.objectPro();

            result += "VIPtree_KNNQ" + "\n";
            result += parameter + "\t" + "time(ns)" + "\t" + "memory" + "\n";


            int k = kDefault;
            ArrayList<ArrayList<Point>> pointPairs = getPointPairs();

            long resultTimeSum2 = 0;
            long resultMenSum2 = 0;
            long resultTimeAve2 = 0;
            long resultMenAve2 = 0;

            for (int j = 0; j < 10; j++) {
                ArrayList<Point> pointPair = pointPairs.get(j);
                VIPtree_KNN viPtree_knn = new VIPtree_KNN();
                long resultTimeSum1 = 0;
                long resultMenSum1 = 0;
                long resultTimeAve1 = 0;
                long resultMenAve1 = 0;
                viPtree_knn.vipTreeKNN(pointPair.get(1), k, vipTree);
                for (int h = 0; h < 10; h++) {
//                    Runtime runtime = Runtime.getRuntime();
//                    runtime.gc();
//                    long startMem = runtime.totalMemory() - runtime.freeMemory();
                    long start = System.nanoTime();

                    viPtree_knn.vipTreeKNN(pointPair.get(1), k, vipTree);
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
            if (parameter == "objectNum") {
                result += num + "\t" + resultTimeAve2 + "\t" + resultMenAve2 + "\n";
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

        point1s.addAll(new ArrayList<>(Arrays.asList("118.0,27.0,8", "97.0,11.0,0", "99.0,27.0,15", "50.0,23.0,4", "41.0,34.0,8", "77.0,27.0,10", "122.0,31.0,13", "103.0,34.0,2", "123.0,27.0,7", "98.0,23.0,1")));
        point2s.addAll(new ArrayList<>(Arrays.asList("84.0,11.0,0", "64.0,22.0,8", "25.0,25.0,5", "76.0,20.0,9", "119.0,25.0,7", "8.0,29.0,1", "43.0,24.0,15", "48.0,31.0,12", "74.0,4.0,8", "45.0,24.0,8")));

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
        KNNQ knnq = new KNNQ();
        // k
//        String fileName = "/results/" + DataGenConstant.dataset + "/KNNQ_k";
//        knnq.IDModel_knnq("k", fileName, 1500, 100);
//        knnq.IDMatrix_knnq("k", fileName, 1500, 100);
//        knnq.ICIndex_knnq("k", fileName, 1500, 100);
//        knnq.IPtree_knnq("k", fileName, 1500, 100);
//        knnq.VIPtree_knnq("k", fileName, 1500, 100);

//        // objectNum
//        String fileName = "/results/" + DataGenConstant.dataset + "/KNNQ_objectNum";
//        knnq.IDModel_knnq("objectNum", fileName, 1500, kDefault);
//        knnq.IDMatrix_knnq("objectNum", fileName, 1500, kDefault);
//        knnq.ICIndex_knnq("objectNum", fileName, 1500, kDefault);
//        knnq.IPtree_knnq("objectNum", fileName, 1500, kDefault);
//        knnq.VIPtree_knnq("objectNum", fileName, 1500, kDefault);

//         diTYpe
        String fileName = "/results/" + DataGenConstant.dataset + "/KNNQ_diType";
//        knnq.IDModel_knnq("diType", fileName, 1500, kDefault);
//        knnq.IDMatrix_knnq("diType", fileName, 1500, kDefault);
        knnq.ICIndex_knnq("diType", fileName, 1500, kDefault);
//        knnq.IPtree_knnq("diType", fileName, 1500, kDefault);
//        knnq.VIPtree_knnq("diType", fileName, 1500, kDefault);


    }
}
