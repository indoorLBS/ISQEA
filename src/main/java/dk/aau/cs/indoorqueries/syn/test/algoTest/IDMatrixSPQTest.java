package dk.aau.cs.indoorqueries.syn.test.algoTest;

import dk.aau.cs.indoorqueries.common.algorithm.IDMatrix_SPQ;
import dk.aau.cs.indoorqueries.common.iDMatrix.DistMatrixGen;
import dk.aau.cs.indoorqueries.common.iDModel.GenTopology;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Point;
import dk.aau.cs.indoorqueries.common.utilities.DataGenConstant;
import dk.aau.cs.indoorqueries.syn.datagenerate.DataGen;

import java.io.IOException;
import java.util.HashMap;

/**
 * test SPQ using IDMatrix
 * @author Tiantian Liu
 */
public class IDMatrixSPQTest {
    public static void main(String[] arg) throws IOException {
        DataGen dataGen = new DataGen();
        dataGen.genAllData(DataGenConstant.dataType, DataGenConstant.divisionType);

        GenTopology genTopology = new GenTopology();
        genTopology.genTopology();

        DistMatrixGen distMatrixGen = new DistMatrixGen();
        distMatrixGen.readDist();

        HashMap<String, Double> d2dDistMap = distMatrixGen.getD2dDistMap();        // the distance between two doors
        HashMap<String, String> d2dRouteMap = distMatrixGen.getD2dRouteMap();

        Point ps = new Point(20, 20, 0);
        Point pt = new Point(546, 411, 0);

        IDMatrix_SPQ idMatrix_spq = new IDMatrix_SPQ();
        idMatrix_spq.p2pDistance(ps, pt, d2dDistMap, d2dRouteMap);
    }
}
