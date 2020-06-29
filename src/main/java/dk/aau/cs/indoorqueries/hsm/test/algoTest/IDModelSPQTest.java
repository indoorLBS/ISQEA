package dk.aau.cs.indoorqueries.hsm.test.algoTest;

import dk.aau.cs.indoorqueries.common.algorithm.IDModel_SPQ;
import dk.aau.cs.indoorqueries.common.iDModel.GenTopology;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Point;
import dk.aau.cs.indoorqueries.hsm.datagenerate.HSMDataGenRead;
import dk.aau.cs.indoorqueries.hsm.datagenerate.ObjectGen;

import java.io.IOException;

//import datagenerate.DataGen;

/**
 * test IDModelSPQ
 *
 * @author Tiantianliu
 */

public class IDModelSPQTest {
    public static void main(String args[]) throws IOException {
//        DataGen dataGen = new DataGen();
//        dataGen.genAllData();
        HSMDataGenRead dateGenReadMen = new HSMDataGenRead();
        dateGenReadMen.dataGen();

        GenTopology genTopology = new GenTopology();
        genTopology.genTopology();

        ObjectGen objectGen = new ObjectGen();
        objectGen.genAllObject(2500);

        Point ps = new Point(1450, 1650, 0);
        Point pt = new Point(1450, 1650, 6);

        IDModel_SPQ idModel_spq = new IDModel_SPQ();
//        idModel_spq.getHostPartition(ps);
//        System.out.println(idModel_spq.getHostPartition(ps));
//        System.out.println(idModel_spq.getHostPartition(pt));
        idModel_spq.pt2ptDistance3(ps, pt);
    }
}
