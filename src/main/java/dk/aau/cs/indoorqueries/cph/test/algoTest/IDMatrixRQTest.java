package dk.aau.cs.indoorqueries.cph.test.algoTest;

import dk.aau.cs.indoorqueries.common.algorithm.IDMatrix_RQ;
import dk.aau.cs.indoorqueries.common.iDMatrix.IndexMatrixGen;
import dk.aau.cs.indoorqueries.common.iDModel.GenTopology;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Point;
import dk.aau.cs.indoorqueries.cph.datagenerate.CPHDataGenRead;
import dk.aau.cs.indoorqueries.cph.datagenerate.ObjectGen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

//import datagenerate.DataGen;

public class IDMatrixRQTest {
    public static void main(String[] arg) throws IOException {
        CPHDataGenRead dateGenReadMen = new CPHDataGenRead();
        dateGenReadMen.dataGen();

        GenTopology genTopology = new GenTopology();
        genTopology.genTopology();

        ObjectGen ObjectGen = new ObjectGen();
        ObjectGen.readObjects(1500);

        IndexMatrixGen indexMatrixGen = new IndexMatrixGen();
        indexMatrixGen.indexMatrixGen();

        ArrayList<ArrayList<ArrayList<Double>>> indexMatrix = indexMatrixGen.getIndexList();
        HashMap<String, Double> d2dDistMap = indexMatrixGen.getD2dDistMap();


        Point ps = new Point(2, 12, 0);
        Point pt = new Point(15, 25, 13);

        IDMatrix_RQ idMatrix_rq = new IDMatrix_RQ();
        idMatrix_rq.rangeQuery(ps, 1000, indexMatrix, d2dDistMap);


    }
}
