package dk.aau.cs.indoorqueries.mzb.test.algoTest;

import dk.aau.cs.indoorqueries.common.algorithm.IDMatrix_KNN;
import dk.aau.cs.indoorqueries.common.iDMatrix.IndexMatrixGen;
import dk.aau.cs.indoorqueries.common.iDModel.GenTopology;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Point;
import dk.aau.cs.indoorqueries.mzb.datagenerate.MZBDataGenRead;
import dk.aau.cs.indoorqueries.mzb.datagenerate.ObjectGen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * test KNNQ using IDMatrix
 * @author Tiantian Liu
 */
public class IDMatrixKNNTest {
    public static void main(String[] arg) throws IOException {
        MZBDataGenRead dateGenReadMen = new MZBDataGenRead();
        dateGenReadMen.dataGen();

        GenTopology genTopology = new GenTopology();
        genTopology.genTopology();

        dk.aau.cs.indoorqueries.mzb.datagenerate.ObjectGen objectGen = new ObjectGen();
        objectGen.readObjects(1500);

        IndexMatrixGen indexMatrixGen = new IndexMatrixGen();
        indexMatrixGen.indexMatrixGen();

        ArrayList<ArrayList<ArrayList<Double>>> indexMatrix = indexMatrixGen.getIndexList();
        HashMap<String, Double> d2dDistMap = indexMatrixGen.getD2dDistMap();


        Point ps = new Point(2, 12, 0);
        Point pt = new Point(15, 25, 13);

        IDMatrix_KNN idMatrix_knn = new IDMatrix_KNN();
        idMatrix_knn.knnQuery(ps, 3, indexMatrix, d2dDistMap);

    }
}
