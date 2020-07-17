package dk.aau.cs.indoorqueries.cph.test.algoTest;

import dk.aau.cs.indoorqueries.common.algorithm.IPtree_SPQ;
import dk.aau.cs.indoorqueries.common.iDModel.GenTopology;
import dk.aau.cs.indoorqueries.common.iPTree.VIPTree;
import dk.aau.cs.indoorqueries.common.indoorEntitity.IndoorSpace;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Point;
import dk.aau.cs.indoorqueries.cph.datagenerate.CPHDataGenRead;
import dk.aau.cs.indoorqueries.cph.datagenerate.ClassifyPartition;

import java.io.IOException;

/**
 * test SPQ using IP-Tree
 * @author Tiantian Liu
 */
public class IPtreeSPQTest {
    public static void main(String[] arg) throws IOException {
        CPHDataGenRead dateGenReadMen = new CPHDataGenRead();
        dateGenReadMen.dataGen();

        GenTopology genTopology = new GenTopology();
        genTopology.genTopology();

        ClassifyPartition classifyPartition = new ClassifyPartition();
        classifyPartition.classifyPar();

        System.out.println(IndoorSpace.iCrucialPartitions.size());

        VIPTree ipTree = VIPTree.createTree();
        ipTree.initTree();

        Point ps = new Point(20, 200, 0);
        Point pt = new Point(250, 400, 0);

        IPtree_SPQ iPtree_spq = new IPtree_SPQ();
        iPtree_spq.ipTreeSPQ(ps, pt, ipTree);
    }


}
