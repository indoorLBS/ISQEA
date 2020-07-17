package dk.aau.cs.indoorqueries.syn.test.algoTest;

import dk.aau.cs.indoorqueries.common.algorithm.IDModel_SPQ;
import dk.aau.cs.indoorqueries.common.iDModel.GenTopology;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Point;
import dk.aau.cs.indoorqueries.common.utilities.DataGenConstant;
import dk.aau.cs.indoorqueries.syn.datagenerate.DataGen;
import dk.aau.cs.indoorqueries.syn.datagenerate.ObjectGen;

import java.io.IOException;

/**
 * test SPQ using IDModel
 *
 * @author Tiantianliu
 */
public class IDModelSPQTest {
    public static void main(String args[]) throws IOException {
        DataGen dataGen = new DataGen();
        dataGen.genAllData(DataGenConstant.dataType, DataGenConstant.divisionType);


        GenTopology genTopology = new GenTopology();
        genTopology.genTopology();

        ObjectGen objectGen = new ObjectGen();
        objectGen.readObjects(1500);

        Point ps = new Point(298.0, 779.0, 2);
        Point pt = new Point(779.0, 898.0, 2);

        IDModel_SPQ idModel_spq = new IDModel_SPQ();
        idModel_spq.pt2ptDistance3(ps, pt);
    }
}
