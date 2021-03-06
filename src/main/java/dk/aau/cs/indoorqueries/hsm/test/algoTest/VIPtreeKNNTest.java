package dk.aau.cs.indoorqueries.hsm.test.algoTest;

import dk.aau.cs.indoorqueries.common.algorithm.VIPtree_KNN;
import dk.aau.cs.indoorqueries.common.iDModel.GenTopology;
import dk.aau.cs.indoorqueries.common.iPTree.VIPTree;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Point;
import dk.aau.cs.indoorqueries.hsm.datagenerate.ClassifyPartition;
import dk.aau.cs.indoorqueries.hsm.datagenerate.HSMDataGenRead;
import dk.aau.cs.indoorqueries.hsm.datagenerate.ObjectGen;

import java.io.IOException;

/**
 * test KNNQ using VIP-Tree
 * @author Tiantian Liu
 */
public class VIPtreeKNNTest {
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

        VIPTree vipTree = VIPTree.createTree();
        vipTree.initVIPtree();

        vipTree.objectPro();


        Point ps = new Point(1450, 1650, 0);


        VIPtree_KNN iPtree_knn = new VIPtree_KNN();
        iPtree_knn.vipTreeKNN(ps, 3, vipTree);

    }
}
