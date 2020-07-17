package dk.aau.cs.indoorqueries.mzb.test.algoTest;

import dk.aau.cs.indoorqueries.common.algorithm.VIPtree_SPQ;
import dk.aau.cs.indoorqueries.common.iDModel.GenTopology;
import dk.aau.cs.indoorqueries.common.iPTree.VIPTree;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Point;
import dk.aau.cs.indoorqueries.mzb.datagenerate.ClassifyPartition;
import dk.aau.cs.indoorqueries.mzb.datagenerate.MZBDataGenRead;

import java.io.IOException;

/**
 * test SPQ using VIP-Tree
 * @author Tiantian Liu
 */
public class VIPtreeSPQTest {
    public static void main(String[] arg) throws IOException {
        MZBDataGenRead dateGenReadMen = new MZBDataGenRead();
        dateGenReadMen.dataGen();

        GenTopology genTopology = new GenTopology();
        genTopology.genTopology();

        ClassifyPartition classifyPartition = new ClassifyPartition();
        classifyPartition.classifyPar();

//        ObjectGen objectGen = new ObjectGen();
//        objectGen.readObjects(1500);

        VIPTree vipTree = VIPTree.createTree();
        vipTree.initVIPtree();

        Point ps = new Point(2, 12, 0);
        Point pt = new Point(57, 4, 2);


        VIPtree_SPQ vipTree_spq = new VIPtree_SPQ();
        vipTree_spq.vipTreeSPQ(ps, pt, vipTree);
    }
}
