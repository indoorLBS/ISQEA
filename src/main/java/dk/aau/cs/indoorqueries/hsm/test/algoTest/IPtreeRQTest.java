package dk.aau.cs.indoorqueries.hsm.test.algoTest;

import dk.aau.cs.indoorqueries.common.algorithm.IPtree_RQ;
import dk.aau.cs.indoorqueries.common.iDModel.GenTopology;
import dk.aau.cs.indoorqueries.common.iPTree.VIPTree;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Point;
import dk.aau.cs.indoorqueries.hsm.datagenerate.ClassifyPartition;
import dk.aau.cs.indoorqueries.hsm.datagenerate.HSMDataGenRead;
import dk.aau.cs.indoorqueries.hsm.datagenerate.ObjectGen;

import java.io.IOException;

//import datagenerate.DataGen;

public class IPtreeRQTest {
    public static void main(String[] arg) throws IOException {
        HSMDataGenRead dateGenReadMen = new HSMDataGenRead();
        dateGenReadMen.dataGen();

        GenTopology genTopology = new GenTopology();
        genTopology.genTopology();

        ClassifyPartition classifyPartition = new ClassifyPartition();
        classifyPartition.classifyPar();

//        System.out.println(IndoorSpace.iCrucialPartitions.size());
        ObjectGen objectGen = new ObjectGen();
        objectGen.readObjects(1500);

        VIPTree ipTree = VIPTree.createTree();
        ipTree.initTree();

        ipTree.objectPro();


//
        Point ps = new Point(1450, 1650, 0);
        Point pt = new Point(15, 25, 13);

        IPtree_RQ iPtree_rq = new IPtree_RQ();
        iPtree_rq.ipTreeRQ(ps, 1000, ipTree);
    }
}
