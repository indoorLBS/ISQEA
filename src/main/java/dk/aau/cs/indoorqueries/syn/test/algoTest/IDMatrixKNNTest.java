package dk.aau.cs.indoorqueries.syn.test.algoTest;

import dk.aau.cs.indoorqueries.common.iDMatrix.IndexMatrixGen;
import dk.aau.cs.indoorqueries.common.iDModel.GenTopology;
import dk.aau.cs.indoorqueries.common.utilities.DataGenConstant;
import dk.aau.cs.indoorqueries.syn.datagenerate.DataGen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * test KNNQ using IDMatrix
 * @author Tiantian Liu
 */
public class IDMatrixKNNTest {
    public static void main(String[] arg) throws IOException {
        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        long startMem = runtime.totalMemory() - runtime.freeMemory();

        long start = System.nanoTime();
        DataGen dataGen = new DataGen();
        dataGen.genAllData(DataGenConstant.dataType, DataGenConstant.divisionType);

        GenTopology genTopology = new GenTopology();
        genTopology.genTopology();

//        ObjectGen objectGen = new ObjectGen();
//        objectGen.readObjects(500);

        IndexMatrixGen indexMatrixGen = new IndexMatrixGen();
        indexMatrixGen.indexMatrixGen();

        ArrayList<ArrayList<ArrayList<Double>>> indexMatrix = indexMatrixGen.getIndexList();
        HashMap<String, Double> d2dDistMap = indexMatrixGen.getD2dDistMap();

        long end = System.nanoTime();
        long endMem = runtime.totalMemory() - runtime.freeMemory();

        double time = (double) (end - start) / 1000 / 1000;
        double memory = (double) (endMem - startMem) / 1024 / 1024;
        System.out.println("time: " + time + " ms");
        System.out.println("memory: " + memory + " mb");


//        Point ps = new Point(20, 20, 0);
//        Point pt = new Point(300, 700, 1);
//
//        IDMatrix_KNN idMatrix_knn = new IDMatrix_KNN();
//        idMatrix_knn.knnQuery(ps, 3, indexMatrix, d2dDistMap);

    }
}
