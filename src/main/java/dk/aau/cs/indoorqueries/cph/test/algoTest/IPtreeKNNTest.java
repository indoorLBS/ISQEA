package dk.aau.cs.indoorqueries.cph.test.algoTest;

import dk.aau.cs.indoorqueries.common.algorithm.IPtree_KNN;
import dk.aau.cs.indoorqueries.common.iDModel.GenTopology;
import dk.aau.cs.indoorqueries.common.iPTree.VIPTree;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Point;
import dk.aau.cs.indoorqueries.cph.datagenerate.CPHDataGenRead;
import dk.aau.cs.indoorqueries.cph.datagenerate.ClassifyPartition;
import dk.aau.cs.indoorqueries.cph.datagenerate.ObjectGen;

import java.io.IOException;

//import datagenerate.DataGen;

public class IPtreeKNNTest {
    public static void main(String[] arg) throws IOException {
        CPHDataGenRead cphDataGenRead = new CPHDataGenRead();
        cphDataGenRead.dataGen();

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


        Point ps = new Point(20, 200, 0);
        Point pt = new Point(250, 400, 0);

//        for (int i = 0; i < IndoorSpace.iObject.size(); i++) {
//            IndoorObject ob = IndoorSpace.iObject.get(i);
//            System.out.println("objectID: " + ob.getObjectId() + ", x: " + ob.getObjectX() + ", y: " + ob.getObjectY() + ", floor: " + ob.getoFloor() + ", parId: " + ob.getParId());
//        }
//
//        for (int i = 0; i < 32; i++) {
//            Partition par = IndoorSpace.iPartitions.get(i);
//            ArrayList<Integer> parObjects = par.getmObjects();
//            System.out.println("parId: " + i + " parObjects: " + parObjects);
//        }

        IPtree_KNN iPtree_knn = new IPtree_KNN();
        iPtree_knn.iptreeKNN(ps, 3, ipTree);


    }

}
