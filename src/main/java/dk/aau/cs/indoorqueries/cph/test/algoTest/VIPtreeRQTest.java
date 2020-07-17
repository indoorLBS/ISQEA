package dk.aau.cs.indoorqueries.cph.test.algoTest;

import dk.aau.cs.indoorqueries.common.algorithm.VIPtree_RQ;
import dk.aau.cs.indoorqueries.common.iDModel.GenTopology;
import dk.aau.cs.indoorqueries.common.iPTree.VIPTree;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Point;
import dk.aau.cs.indoorqueries.cph.datagenerate.CPHDataGenRead;
import dk.aau.cs.indoorqueries.cph.datagenerate.ClassifyPartition;
import dk.aau.cs.indoorqueries.cph.datagenerate.ObjectGen;

import java.io.IOException;

/**
 * test RQ using VIP-Tree
 * @author Tiantian Liu
 */
public class VIPtreeRQTest {
    public static void main(String[] arg) throws IOException {
        CPHDataGenRead dateGenReadMen = new CPHDataGenRead();
        dateGenReadMen.dataGen();

        GenTopology genTopology = new GenTopology();
        genTopology.genTopology();

        ClassifyPartition classifyPartition = new ClassifyPartition();
        classifyPartition.classifyPar();

//        System.out.println(IndoorSpace.iCrucialPartitions.size());
        ObjectGen ObjectGen = new ObjectGen();
        ObjectGen.readObjects(1500);

        VIPTree vipTree = VIPTree.createTree();
        vipTree.initVIPtree();

        vipTree.objectPro();


        Point ps = new Point(20, 200, 0);
        Point pt = new Point(250, 400, 0);

        VIPtree_RQ viPtree_rq = new VIPtree_RQ();
        viPtree_rq.vipTreeRQ(ps, 300, vipTree);
    }
}
