package dk.aau.cs.indoorqueries.cph.test.algoTest;

import dk.aau.cs.indoorqueries.common.algorithm.IDModel_RQ;
import dk.aau.cs.indoorqueries.common.iDModel.GenTopology;
import dk.aau.cs.indoorqueries.common.indoorEntitity.IndoorObject;
import dk.aau.cs.indoorqueries.common.indoorEntitity.IndoorSpace;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Partition;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Point;
import dk.aau.cs.indoorqueries.cph.datagenerate.CPHDataGenRead;
import dk.aau.cs.indoorqueries.cph.datagenerate.ObjectGen;

import java.io.IOException;
import java.util.ArrayList;

//import datagenerate.DataGen;

/**
 * test IDMatrixRQ
 *
 * @author Tiantian Liu
 */

public class IDModelRQTest {
    public static void main(String args[]) throws IOException {
        CPHDataGenRead dateGenReadMen = new CPHDataGenRead();
        dateGenReadMen.dataGen();

        GenTopology genTopology = new GenTopology();
        genTopology.genTopology();

        ObjectGen ObjectGen = new ObjectGen();
        ObjectGen.readObjects(500);

        Point ps = new Point(20, 200, 0);
        Point pt = new Point(250, 400, 0);

        for (int i = 0; i < IndoorSpace.iObject.size(); i++) {
            IndoorObject ob = IndoorSpace.iObject.get(i);
            System.out.println("objectID: " + ob.getObjectId() + ", x: " + ob.getObjectX() + ", y: " + ob.getObjectY() + ", floor: " + ob.getoFloor() + ", parId: " + ob.getParId());
        }

        for (int i = 0; i < 32; i++) {
            Partition par = IndoorSpace.iPartitions.get(i);
            ArrayList<Integer> parObjects = par.getmObjects();
            System.out.println("parId: " + i + " parObjects: " + parObjects);
        }

        IDModel_RQ idModel_rq = new IDModel_RQ();
        System.out.println(idModel_rq.range(ps, 500));
    }
}
