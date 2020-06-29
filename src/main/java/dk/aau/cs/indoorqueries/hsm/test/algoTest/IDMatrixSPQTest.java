package dk.aau.cs.indoorqueries.hsm.test.algoTest;

import dk.aau.cs.indoorqueries.common.algorithm.IDMatrix_SPQ;
import dk.aau.cs.indoorqueries.common.iDMatrix.DistMatrixGen;
import dk.aau.cs.indoorqueries.common.iDModel.GenTopology;
import dk.aau.cs.indoorqueries.hsm.datagenerate.HSMDataGenRead;

import java.io.IOException;
import java.util.HashMap;

public class IDMatrixSPQTest {
    public static void main(String[] arg) throws IOException {
        HSMDataGenRead dateGenReadMen = new HSMDataGenRead();
        dateGenReadMen.dataGen();

        GenTopology genTopology = new GenTopology();
        genTopology.genTopology();

        DistMatrixGen distMatrixGen = new DistMatrixGen();
        distMatrixGen.readDist();

        HashMap<String, Double> d2dDistMap = distMatrixGen.getD2dDistMap();        // the distance between two doors
        HashMap<String, String> d2dRouteMap = distMatrixGen.getD2dRouteMap();

        dk.aau.cs.indoorqueries.common.indoorEntitity.Point ps = new dk.aau.cs.indoorqueries.common.indoorEntitity.Point(2, 12, 0);
        dk.aau.cs.indoorqueries.common.indoorEntitity.Point pt = new dk.aau.cs.indoorqueries.common.indoorEntitity.Point(15, 25, 13);

        IDMatrix_SPQ idMatrix_spq = new IDMatrix_SPQ();
        idMatrix_spq.p2pDistance(ps, pt, d2dDistMap, d2dRouteMap);


    }
}
