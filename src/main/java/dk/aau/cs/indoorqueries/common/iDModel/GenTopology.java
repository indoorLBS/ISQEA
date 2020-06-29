package dk.aau.cs.indoorqueries.common.iDModel;

import dk.aau.cs.indoorqueries.common.indoorEntitity.D2Ddistance;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Graph;
import dk.aau.cs.indoorqueries.common.indoorEntitity.IndoorSpace;

import java.io.IOException;
import java.util.HashMap;

public class GenTopology {
    /**
     * generate connectivityTier
     */
    public void genTopology() throws IOException {
        int partitionSize = IndoorSpace.iPartitions.size();
        for (int i = 0; i < partitionSize; i++) {

            // D2D distance Matrix
            HashMap<String, D2Ddistance> hashMap = IndoorSpace.iPartitions.get(i).getD2dHashMap();
            IndoorSpace.iD2D.putAll(hashMap);
//			System.out.println("partition " + IndoorSpace.iPartitions.get(i).getmID() + " has size " + IndoorSpace.iPartitions.get(i).getD2dHashMap().size());

            // set weight center of all of the partitions
            IndoorSpace.iPartitions.get(i).setCenter();

            // partition's distance matrix
            DistMatrix distMatrix = new DistMatrix(IndoorSpace.iPartitions.get(i).getmID(), true);
            IndoorSpace.iPartitions.get(i).setDistMatrix(distMatrix);

            // partition's connectivity tier
            Topology topology = new Topology(IndoorSpace.iPartitions.get(i).getmID());
            IndoorSpace.iPartitions.get(i).setTopology(topology);

            // add all the partitions into the Graph
            Graph.Partitions.put(i, IndoorSpace.iPartitions.get(i));
        }

//        int floorSize = IndoorSpace.iFloors.size();
//        for (int i = 0; i < floorSize; i ++) {
//            // D2D distance Matrix
//            HashMap<String, D2Ddistance> hashMap = IndoorSpace.iFloors.get(
//                    i).getD2dHashMap();
//            IndoorSpace.iD2D.putAll(hashMap);
//
////			// floor's distance matrix
////			DistMatrix distMatrix = new DistMatrix(IndoorSpace.iFloors.get(i).getmID(), false);
////			IndoorSpace.iFloors.get(i).setDistMatrix(distMatrix);
//
//            // partition's connectivity tier
//            Topology topology = new Topology(IndoorSpace.iFloors.get(i).getmID());
//            IndoorSpace.iFloors.get(i).setTopology(topology);
//
//            // add all the floors into the Graph
//            Graph.Floors.put(i, IndoorSpace.iFloors.get(i));
//        }
//
//        System.out.println("Partitions's distance matrix and connectivity tier generate finished!");

        // add all the doors as nodes into the Graph
//        int doorSize = IndoorSpace.iDoors.size();
//        for (int i = 0; i < doorSize; i++) {
//            Graph.Doors.put(i, IndoorSpace.iDoors.get(i));
//        }
    }
}
