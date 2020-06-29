package dk.aau.cs.indoorqueries.hsm.test.algoTest;

import dk.aau.cs.indoorqueries.common.algorithm.IPtree_SPQ;
import dk.aau.cs.indoorqueries.common.iDModel.GenTopology;
import dk.aau.cs.indoorqueries.common.iPTree.VIPTree;
import dk.aau.cs.indoorqueries.common.indoorEntitity.IndoorSpace;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Point;
import dk.aau.cs.indoorqueries.hsm.datagenerate.ClassifyPartition;
import dk.aau.cs.indoorqueries.hsm.datagenerate.HSMDataGenRead;

import java.io.IOException;

//import datagenerate.DataGen;

public class IPtreeSPQTest {
    public static void main(String[] arg) throws IOException {
        HSMDataGenRead dateGenReadMen = new HSMDataGenRead();
        dateGenReadMen.dataGen();

        GenTopology genTopology = new GenTopology();
        genTopology.genTopology();

        ClassifyPartition classifyPartition = new ClassifyPartition();
        classifyPartition.classifyPar();

        System.out.println(IndoorSpace.iCrucialPartitions.size());

        VIPTree ipTree = VIPTree.createTree();
        ipTree.initTree();

        Point ps = new Point(1450, 1650, 0);
        Point pt = new Point(1450, 1650, 6);

        IPtree_SPQ iPtree_spq = new IPtree_SPQ();
        iPtree_spq.ipTreeSPQ(ps, pt, ipTree);
    }


}
