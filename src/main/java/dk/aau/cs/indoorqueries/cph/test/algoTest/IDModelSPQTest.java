package dk.aau.cs.indoorqueries.cph.test.algoTest;

import dk.aau.cs.indoorqueries.common.algorithm.IDModel_SPQ;
import dk.aau.cs.indoorqueries.common.iDModel.GenTopology;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Point;
import dk.aau.cs.indoorqueries.cph.datagenerate.CPHDataGenRead;

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
        CPHDataGenRead dateGenReadMen = new CPHDataGenRead();
        dateGenReadMen.dataGen();

        GenTopology genTopology = new GenTopology();
        genTopology.genTopology();

//        ObjectGen objectGen = new ObjectGen();
//        objectGen.readObjects(1500);

        Point ps = new Point(20, 200, 0);
        Point pt = new Point(250, 400, 0);

        IDModel_SPQ idModel_spq = new IDModel_SPQ();
//        idModel_spq.getHostPartition(ps);
//        System.out.println(idModel_spq.getHostPartition(ps));
//        System.out.println(idModel_spq.getHostPartition(pt));
        idModel_spq.pt2ptDistance3(ps, pt);
    }
}
