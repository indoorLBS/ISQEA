package dk.aau.cs.indoorqueries.syn.test.algoTest;

import dk.aau.cs.indoorqueries.common.algorithm.IDMatrix_RQ;
import dk.aau.cs.indoorqueries.common.iDMatrix.IndexMatrixGen;
import dk.aau.cs.indoorqueries.common.iDModel.GenTopology;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Point;
import dk.aau.cs.indoorqueries.common.utilities.DataGenConstant;
import dk.aau.cs.indoorqueries.syn.datagenerate.DataGen;
import dk.aau.cs.indoorqueries.syn.datagenerate.ObjectGen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * test RQ using IDMatrix
 * @author Tiantian Liu
 */
public class IDMatrixRQTest {
    public static void main(String[] arg) throws IOException {
        DataGen dataGen = new DataGen();
        dataGen.genAllData(DataGenConstant.dataType, DataGenConstant.divisionType);

        GenTopology genTopology = new GenTopology();
        genTopology.genTopology();

        ObjectGen objectGen = new ObjectGen();
        objectGen.readObjects(500);

        IndexMatrixGen indexMatrixGen = new IndexMatrixGen();
        indexMatrixGen.indexMatrixGen();

        ArrayList<ArrayList<ArrayList<Double>>> indexMatrix = indexMatrixGen.getIndexList();
        HashMap<String, Double> d2dDistMap = indexMatrixGen.getD2dDistMap();


        Point ps = new Point(20, 20, 0);
        Point pt = new Point(300, 700, 1);

        IDMatrix_RQ idMatrix_rq = new IDMatrix_RQ();
        idMatrix_rq.rangeQuery(ps, 1000, indexMatrix, d2dDistMap);


    }
}
