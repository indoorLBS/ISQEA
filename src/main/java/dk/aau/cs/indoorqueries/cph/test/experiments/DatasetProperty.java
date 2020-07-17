package dk.aau.cs.indoorqueries.cph.test.experiments;

import dk.aau.cs.indoorqueries.common.iDModel.GenTopology;
import dk.aau.cs.indoorqueries.common.indoorEntitity.IndoorSpace;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Partition;
import dk.aau.cs.indoorqueries.common.utilities.DataGenConstant;
import dk.aau.cs.indoorqueries.common.utilities.RoomType;
import dk.aau.cs.indoorqueries.cph.datagenerate.CPHDataGenRead;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * test the property of dataset
 * @author Tiantian Liu
 */
public class DatasetProperty {
    public static void main(String[] arg) throws IOException {
        CPHDataGenRead dateGenReadMen = new CPHDataGenRead();
        dateGenReadMen.dataGen();

        GenTopology genTopology = new GenTopology();
        genTopology.genTopology();

        int[] doorDistribute = new int[IndoorSpace.iDoors.size()];
        int hallwaySize = 0;
        int roomSize = 0;
        int stairSize = 0;
        int crucialParSize = 0;
        String outputFile = System.getProperty("user.dir") + "/results/" + DataGenConstant.dataset +"/doorDistribute_diType_" + DataGenConstant.divisionType + ".txt";
        String result = "";
        int sum = 0;

        for (int i = 0; i < IndoorSpace.iPartitions.size(); i++) {
            Partition par = IndoorSpace.iPartitions.get(i);
            ArrayList<Integer> mDoors = par.getmDoors();
            doorDistribute[mDoors.size()] += 1;
            if (par.getmType() == RoomType.HALLWAY) {
                hallwaySize++;
            } else if (par.getmType() == RoomType.STAIRCASE) {
                stairSize++;
            } else {
                roomSize++;
            }

            if (mDoors.size() >= 6) {
                crucialParSize++;
            }
        }


        for (int i = 0; i < doorDistribute.length; i++) {
            sum += doorDistribute[i];
            if (doorDistribute[i] > 0) {
                result += i + "\t" + doorDistribute[i] + "\n";
            }
        }
        if (sum != IndoorSpace.iPartitions.size()) {
            System.out.println("something wrong with doorDistribute");
        }

        result += "partitionSize" + "\t" + IndoorSpace.iPartitions.size() + "\n";
        result += "doorSize" + "\t" + IndoorSpace.iDoors.size() + "\n";
        result += "hallwaySize" + "\t" + hallwaySize + "\n";
        result += "stairSize" + "\t" + stairSize + "\n";
        result += "roomSize" + "\t" + roomSize + "\n";
        result += "crucialSize" + "\t" + crucialParSize + "\n";

        try {
            FileWriter fw = new FileWriter(outputFile);
            fw.write(result);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

    }
}
