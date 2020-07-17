package dk.aau.cs.indoorqueries.common.iDModel;

import dk.aau.cs.indoorqueries.common.indoorEntitity.D2Ddistance;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Graph;
import dk.aau.cs.indoorqueries.common.indoorEntitity.IndoorSpace;
import dk.aau.cs.indoorqueries.common.utilities.DataGenConstant;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;

/**
 * generate topoloty of indoor space
 * @author Tiantian Liu
 */
public class GenTopology {
    public static HashMap<Integer, HashMap<String, Double>> hallwayDistMatrix_floor = new HashMap<>();
    /**
     * generate connectivityTier
     */
    public void genTopology() throws IOException {
        if (DataGenConstant.divisionType == 0) {
            for (int i = 0; i < 3; i++) {
                HashMap<String, Double> hallwayDistMatrix = new HashMap<>();
                double dist = 0;
                Path path = Paths.get(System.getProperty("user.dir") + "/inputfiles/" + DataGenConstant.dataset + "/hallway_distMatrix_division_0_floor_"+ i + ".txt");
                Scanner scanner = new Scanner(path);

                //read line by line
                Boolean flag = false;
                while(scanner.hasNextLine()){
                    //process each line
                    String line = scanner.nextLine();
                    String[] tempArr = line.split("\t");
                    hallwayDistMatrix.put(tempArr[1], Double.parseDouble(tempArr[2]));
                }
                hallwayDistMatrix_floor.put(i, hallwayDistMatrix);
                if (!DataGenConstant.dataset.equals("MZB")) break;
            }
        }
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
    }
}
