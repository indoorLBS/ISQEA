package dk.aau.cs.indoorqueries.syn.test.algoTest;

import dk.aau.cs.indoorqueries.common.algorithm.VIPtree_SPQ;
import dk.aau.cs.indoorqueries.common.iDModel.GenTopology;
import dk.aau.cs.indoorqueries.common.iPTree.VIPTree;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Point;
import dk.aau.cs.indoorqueries.common.utilities.DataGenConstant;
import dk.aau.cs.indoorqueries.syn.datagenerate.ClassifyPartition;
import dk.aau.cs.indoorqueries.syn.datagenerate.DataGen;

import java.io.IOException;

/**
 * test SPQ using VIP-Tree
 * @author Tiantian Liu
 */
public class VIPtreeSPQTest {
    public static void main(String[] arg) throws IOException {
        DataGen dataGen = new DataGen();
        dataGen.genAllData(DataGenConstant.dataType, DataGenConstant.divisionType);

        GenTopology genTopology = new GenTopology();
        genTopology.genTopology();

        ClassifyPartition classifyPartition = new ClassifyPartition();
        classifyPartition.classifyPar();

//        ObjectGen objectGen = new ObjectGen();
//        objectGen.readObjects(500);

        VIPTree vipTree = VIPTree.createTree();
        vipTree.initVIPtree();

        Point ps = new Point(214.0, 242.0, 2);
        Point pt = new Point(1125.0, 1346.0, 2);


        VIPtree_SPQ vipTree_spq = new VIPtree_SPQ();
        vipTree_spq.vipTreeSPQ(ps, pt, vipTree);
    }
}
