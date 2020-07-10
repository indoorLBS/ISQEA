package dk.aau.cs.indoorqueries.mzb.test.algoTest;

import dk.aau.cs.indoorqueries.common.algorithm.IDModel_SPQ;
import dk.aau.cs.indoorqueries.common.iDModel.GenTopology;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Point;
import dk.aau.cs.indoorqueries.mzb.datagenerate.MZBDataGenRead;

import java.io.IOException;

/**
 * test IDModelSPQ
 *
 * @author Tiantianliu
 */

public class IDModelSPQTest {
    public static void main(String args[]) throws IOException {
//        DataGen dataGen = new DataGen();
//        dataGen.genAllData();
        MZBDataGenRead dateGenReadMen = new MZBDataGenRead();
        dateGenReadMen.dataGen();

        GenTopology genTopology = new GenTopology();
        genTopology.genTopology();

//        ObjectGen objectGen = new ObjectGen();
//        objectGen.readObjects(1500);

        Point ps = new Point(2, 12, 0);
        Point pt = new Point(57, 4, 2);


        IDModel_SPQ idModel_spq = new IDModel_SPQ();
        idModel_spq.pt2ptDistance3(ps, pt);
    }
}
