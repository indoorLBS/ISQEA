package dk.aau.cs.indoorqueries.cph.test.algoTest;

import dk.aau.cs.indoorqueries.common.algorithm.VIPtree_SPQ;
import dk.aau.cs.indoorqueries.common.iDModel.GenTopology;
import dk.aau.cs.indoorqueries.common.iPTree.VIPTree;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Point;
import dk.aau.cs.indoorqueries.cph.datagenerate.CPHDataGenRead;
import dk.aau.cs.indoorqueries.cph.datagenerate.ClassifyPartition;

import java.io.IOException;

//import datagenerate.DataGen;

public class VIPtreeSPQTest {
    public static void main(String[] arg) throws IOException {
        CPHDataGenRead dateGenReadMen = new CPHDataGenRead();
        dateGenReadMen.dataGen();

        GenTopology genTopology = new GenTopology();
        genTopology.genTopology();

        ClassifyPartition classifyPartition = new ClassifyPartition();
        classifyPartition.classifyPar();

//        ObjectGen objectGen = new ObjectGen();
//        objectGen.readObjects(1500);

        VIPTree vipTree = VIPTree.createTree();
        vipTree.initVIPtree();

        Point ps = new Point(20, 200, 0);
        Point pt = new Point(250, 400, 0);


        VIPtree_SPQ vipTree_spq = new VIPtree_SPQ();
        vipTree_spq.vipTreeSPQ(ps, pt, vipTree);
    }
}
