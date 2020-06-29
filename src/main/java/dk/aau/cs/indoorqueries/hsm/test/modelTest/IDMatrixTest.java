package dk.aau.cs.indoorqueries.hsm.test.modelTest;

//import datagenerate.DataGen;

import dk.aau.cs.indoorqueries.common.iDMatrix.DistMatrixGen;
import dk.aau.cs.indoorqueries.common.iDModel.GenTopology;
import dk.aau.cs.indoorqueries.hsm.datagenerate.HSMDataGenRead;

import java.io.IOException;

/**
 * test IDMatrix
 *
 * @author Tiantian Liu
 */

public class IDMatrixTest {
    public static void main(String arg[]) throws IOException {
        HSMDataGenRead dateGenReadMen = new HSMDataGenRead();
        dateGenReadMen.dataGen();

        GenTopology genTopology = new GenTopology();
        genTopology.genTopology();

        DistMatrixGen distMatrixGen = new DistMatrixGen();

        distMatrixGen.d2dDistMatrixGen();


    }
}
