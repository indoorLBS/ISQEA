package dk.aau.cs.indoorqueries.syn.test.algoTest;

import dk.aau.cs.indoorqueries.common.algorithm.IPtree_SPQ;
import dk.aau.cs.indoorqueries.common.iDModel.GenTopology;
import dk.aau.cs.indoorqueries.common.iPTree.VIPTree;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Point;
import dk.aau.cs.indoorqueries.common.utilities.DataGenConstant;
import dk.aau.cs.indoorqueries.syn.datagenerate.ClassifyPartition;
import dk.aau.cs.indoorqueries.syn.datagenerate.DataGen;

import java.io.IOException;

public class IPtreeSPQTest {
    public static void main(String[] arg) throws IOException {
        long start = System.nanoTime();
        DataGen dataGen = new DataGen();
        dataGen.genAllData(DataGenConstant.dataType, DataGenConstant.divisionType);

        GenTopology genTopology = new GenTopology();
        genTopology.genTopology();

        ClassifyPartition classifyPartition = new ClassifyPartition();
        classifyPartition.classifyPar();

//        System.out.println(IndoorSpace.iCrucialPartitions.size());

        VIPTree vipTree = VIPTree.createTree();
        vipTree.initTree();
        long end = System.nanoTime();
        long time = end - start;
        System.out.println("time: " + time);

        Point ps = new Point(214.0, 242.0, 2);
        Point pt = new Point(1125.0, 1346.0, 2);

        IPtree_SPQ iPtree_spq = new IPtree_SPQ();
        iPtree_spq.ipTreeSPQ(ps, pt, vipTree);
    }


}
