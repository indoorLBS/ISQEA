package dk.aau.cs.indoorqueries.syn.test.algoTest;

import dk.aau.cs.indoorqueries.common.algorithm.IDModel_RQ;
import dk.aau.cs.indoorqueries.common.iDModel.GenTopology;
import dk.aau.cs.indoorqueries.common.indoorEntitity.IndoorObject;
import dk.aau.cs.indoorqueries.common.indoorEntitity.IndoorSpace;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Partition;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Point;
import dk.aau.cs.indoorqueries.common.utilities.DataGenConstant;
import dk.aau.cs.indoorqueries.syn.datagenerate.DataGen;
import dk.aau.cs.indoorqueries.syn.datagenerate.ObjectGen;

import java.io.IOException;
import java.util.ArrayList;

/**
 * test RQ using IDModel
 *
 * @author Tiantian Liu
 */
public class IDModelRQTest {
    public static void main(String args[]) throws IOException {
        DataGen dataGen = new DataGen();
        dataGen.genAllData(DataGenConstant.dataType, DataGenConstant.divisionType);

        GenTopology genTopology = new GenTopology();
        genTopology.genTopology();

        ObjectGen objectGen = new ObjectGen();
        objectGen.readObjects(500);

        Point ps = new Point(20, 20, 0);
        Point pt = new Point(300, 700, 1);

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
        System.out.println(idModel_rq.range(ps, 1000));
    }
}
