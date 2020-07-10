/**
 *
 */
package dk.aau.cs.indoorqueries.common.iDModel;

import dk.aau.cs.indoorqueries.common.indoorEntitity.Direction;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Door;
import dk.aau.cs.indoorqueries.common.indoorEntitity.IndoorSpace;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Partition;

import java.util.ArrayList;
import java.util.List;

/**
 * <h>Topology</h>
 * represent the Topology for each of the node
 *
 * @author feng zijin
 *
 */
public class Topology {
	private ArrayList<TopologyElement> connectivityTierIn = new ArrayList<TopologyElement>();        // key is door id
	private ArrayList<TopologyElement> connectivityTierOut = new ArrayList<TopologyElement>();        // key is door id
	private int mID;            // it is partition id

	/**
	 * Constructor
	 *
	 */
	public Topology() {
	}

	/**
	 * Constructor
	 *
	 */
	public Topology(int mID) {
		this.mID = mID;
		this.loadData();
	}

	public void loadData() {
		Partition partition_1 = IndoorSpace.iPartitions.get(mID);
		List<Integer> doors_1 = new ArrayList<Integer>();
		doors_1 = partition_1.getmDoors();

		for (int i = 0; i < doors_1.size(); i++) {
			int doorID_1 = doors_1.get(i);
			Door door_1 = IndoorSpace.iDoors.get(doorID_1);
			List<Integer> partitions_2 = new ArrayList<Integer>();
			partitions_2 = door_1.getmPartitions();

			if (partitions_2 == null) continue;

			if (!partitions_2.contains(mID)) {
				System.out.println("something wrong_ConnectivityTier_loadData1" + " " + mID);
			}

			for (int j = 0; j < partitions_2.size(); j++) {
				int partitionID_2 = partitions_2.get(j);

				if (partitionID_2 != mID) {
					// out door
					TopologyElement element_out = new TopologyElement(doorID_1, partitionID_2, true, false);
					addConnectivityElementOut(element_out);

					// in door
					TopologyElement element_in = new TopologyElement(doorID_1, partitionID_2, true, true);
					addConnectivityElementIn(element_in);

					// add the direction to the door
					IndoorSpace.iDoors.get(doorID_1).addDirection(new Direction(mID, partitionID_2, doorID_1));
				}
			}

//
		}
	}

	/**
	 * @param topologyElement the TopologyElement to be added
	 *
	 */
	public void addConnectivityElementIn(TopologyElement topologyElement) {
		connectivityTierIn.add(topologyElement);
	}

	/**
	 * @return the connectivity tier in
	 *
	 */
	public ArrayList<TopologyElement> getconnectivityTierIn() {
		return connectivityTierIn;
	}

	/**
	 * @param topologyElement the TopologyElement to be added
	 *
	 */
	public void addConnectivityElementOut(TopologyElement topologyElement) {
		connectivityTierOut.add(topologyElement);
	}

	/**
	 * @return the connectivity tier out
	 *
	 */
	public ArrayList<TopologyElement> getconnectivityTierOut() {
		return connectivityTierOut;
	}

	/**
	 * @return the mID
	 */
	public int getmID() {
		return mID;
	}

	/**
	 * @param mID
	 *            the mID to set
	 */
	public void setmID(int mID) {
		this.mID = mID;
	}

	/**
	 * @return a list of door id that can leave the partition
	 */
	public ArrayList<Integer> getP2DLeave() {
		ArrayList<Integer> result = new ArrayList<Integer>();

		for (int i = 0; i < connectivityTierOut.size(); i++) {
			TopologyElement topologyElement = connectivityTierOut.get(i);
			result.add(topologyElement.getmDoorID1());
		}

//		Collections.sort(result);

		return result;
	}

	/**
	 * @return a list of door id that can enter the partition
	 */
	public ArrayList<Integer> getP2DEnter() {
		ArrayList<Integer> result = new ArrayList<Integer>();

		for (int i = 0; i < this.connectivityTierIn.size(); i++) {
			TopologyElement topologyElement = connectivityTierIn.get(i);

			if (topologyElement.getIsPartition()) {
				if (!result.contains(topologyElement.getmDoorID1())) result.add(topologyElement.getmDoorID1());
			} else {
				if (!result.contains(topologyElement.getmDoorID2())) result.add(topologyElement.getmDoorID2());
			}
		}

//		Collections.sort(result);

		return result;
	}

	/**
	 * @return a list of partition id that can leave from the partition
	 */
	public ArrayList<Integer> getP2PLeave() {
		ArrayList<Integer> result = new ArrayList<Integer>();

		for (int i = 0; i < connectivityTierOut.size(); i++) {
			TopologyElement topologyElement = connectivityTierOut.get(i);
			result.add(topologyElement.getmParID());
		}

		return result;
	}


}
