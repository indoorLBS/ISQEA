package dk.aau.cs.indoorqueries.syn.test.algoTest;

import dk.aau.cs.indoorqueries.common.algorithm.VIPtree_KNN;
import dk.aau.cs.indoorqueries.common.iDModel.GenTopology;
import dk.aau.cs.indoorqueries.common.iPTree.VIPTree;
import dk.aau.cs.indoorqueries.common.indoorEntitity.IndoorObject;
import dk.aau.cs.indoorqueries.common.indoorEntitity.IndoorSpace;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Partition;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Point;
import dk.aau.cs.indoorqueries.common.utilities.DataGenConstant;
import dk.aau.cs.indoorqueries.syn.datagenerate.ClassifyPartition;
import dk.aau.cs.indoorqueries.syn.datagenerate.DataGen;
import dk.aau.cs.indoorqueries.syn.datagenerate.ObjectGen;

import java.io.IOException;
import java.util.ArrayList;

/**
 * test KNNQ using VIP-Tree
 * @author Tiantian Liu
 */
public class VIPtreeKNNTest {
    public static void main(String[] arg) throws IOException {
        DataGen dataGen = new DataGen();
        dataGen.genAllData(DataGenConstant.dataType, DataGenConstant.divisionType);

        GenTopology genTopology = new GenTopology();
        genTopology.genTopology();

        ClassifyPartition classifyPartition = new ClassifyPartition();
        classifyPartition.classifyPar();

//        System.out.println(IndoorSpace.iCrucialPartitions.size());
        ObjectGen objectGen = new ObjectGen();
        objectGen.readObjects(1500);

        VIPTree vipTree = VIPTree.createTree();
        vipTree.initVIPtree();

        vipTree.objectPro();


        Point ps = new Point(355.0, 961.0, 0);
        Point pt = new Point(300, 700, 1);

        VIPtree_KNN iPtree_knn = new VIPtree_KNN();
        iPtree_knn.vipTreeKNN(ps, 10, vipTree);

    }
}
