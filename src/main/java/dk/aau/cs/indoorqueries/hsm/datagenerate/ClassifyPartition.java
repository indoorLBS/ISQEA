package dk.aau.cs.indoorqueries.hsm.datagenerate;

import dk.aau.cs.indoorqueries.common.indoorEntitity.IndoorSpace;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Partition;
import dk.aau.cs.indoorqueries.common.utilities.RoomType;

public class ClassifyPartition {

    // constructor
    public ClassifyPartition() {
    }

    /**
     * classify partitions according to topology properties
     * <p>
     * crucialpass: partition with 4 or more than 4 doors
     */
    public void classifyPar() {
        int crucial = 0;
        int simple = 0;


        for (int i = 0; i < IndoorSpace.iPartitions.size(); i++) {
            Partition par = IndoorSpace.iPartitions.get(i);
            if (par.getmDoors().size() >= 7) {
                par.settType(RoomType.CRUCIALPASS);
                IndoorSpace.iCrucialPartitions.add(par);
            } else {
                par.settType(RoomType.SIMPLEPASS);
            }

        }

//
    }
}
