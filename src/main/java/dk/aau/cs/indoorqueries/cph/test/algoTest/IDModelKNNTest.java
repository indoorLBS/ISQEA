package dk.aau.cs.indoorqueries.cph.test.algoTest;

import dk.aau.cs.indoorqueries.common.algorithm.IDModel_KNN;
import dk.aau.cs.indoorqueries.common.iDModel.GenTopology;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Point;
import dk.aau.cs.indoorqueries.cph.datagenerate.CPHDataGenRead;
import dk.aau.cs.indoorqueries.cph.datagenerate.ObjectGen;

import java.io.IOException;

//import datagenerate.DataGen;

/**
 * test kNNQ
 *
 * @author Tiantian Liu
 */

public class IDModelKNNTest {
    public static void main(String args[]) throws IOException {
        CPHDataGenRead dateGenReadMen = new CPHDataGenRead();
        dateGenReadMen.dataGen();

        GenTopology genTopology = new GenTopology();
        genTopology.genTopology();

        ObjectGen ObjectGen = new ObjectGen();
        ObjectGen.readObjects(1500);

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

        IDModel_KNN idModel_knn = new IDModel_KNN();
        System.out.println(idModel_knn.knn(ps, 3));
    }
}
