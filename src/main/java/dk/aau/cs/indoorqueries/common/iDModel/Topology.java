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

//			if (door_1.getmType() == DoorType.EXIT) {
//				int doorID_2 = doorID_1 + IndoorSpace.iNumberDoorsPerFloor;
//				System.out.println("EXIT door: " + doorID_1);
//				System.out.println("EXIT door upstair door: " + doorID_2);
//
//				if (doorID_2 <= IndoorSpace.iDoors.size()) {
//					Door door_2 = IndoorSpace.iDoors.get(doorID_2);
//					List<Integer> partitions_3 = door_2.getmPartitions();
//					System.out.println("upstair door mPartitions: " + partitions_3);
//					for (int j = 0; j < partitions_3.size(); j++) {
//						int partitionID_3 = partitions_3.get(j);
//						if (partitionID_3 != mID) {
//							// out door
//							TopologyElement element_out = new TopologyElement(doorID_1, doorID_2, partitionID_3, false, false);
//							addConnectivityElementOut(element_out);
//
//							// in door
//							TopologyElement element_in = new TopologyElement(doorID_2, doorID_1, partitionID_3, false, true);
//							addConnectivityElementIn(element_in);
//
//							// add the direction to the door
//							IndoorSpace.iDoors.get(doorID_1).addDirection(new Direction(mID, partitionID_3, doorID_1, doorID_2));
//							IndoorSpace.iDoors.get(doorID_1).addDirection(new Direction(partitionID_3, mID, doorID_2, doorID_1));
//						}
//					}
//
//				}
//
//				doorID_2 = doorID_1 - IndoorSpace.iNumberDoorsPerFloor;
//
//				if (doorID_2 >= 0) {
//					Door door_2 = IndoorSpace.iDoors.get(doorID_2);
//					List<Integer> partitions_3 = door_2.getmPartitions();
//					for (int j = 0; j < partitions_3.size(); j++) {
//						int partitionID_3 = partitions_3.get(j);
//						if (partitionID_3 != mID) {
//							// out door
//							TopologyElement element_out = new TopologyElement(doorID_1, doorID_2, partitionID_3, false, false);
//							addConnectivityElementOut(element_out);
//
//							// in door
//							TopologyElement element_in = new TopologyElement(doorID_2, doorID_1, partitionID_3, false, true);
//							addConnectivityElementIn(element_in);
//
//							// add the direction to the door
//							IndoorSpace.iDoors.get(doorID_1).addDirection(new Direction(mID, partitionID_3, doorID_1, doorID_2));
//							IndoorSpace.iDoors.get(doorID_1).addDirection(new Direction(partitionID_3, mID, doorID_2, doorID_1));
//						}
//					}
//				}
//
//			}
		}
	}

	/**
	 * @param topologyElement the TopologyElement to be added
	 *
	 */
	public void addConnectivityElementIn(TopologyElement topologyElement) {
//		boolean exist = false;

//		for (int i = 0; i < connectivityTierIn.size(); i ++) {
//			TopologyElement temp = connectivityTierIn.get(i);
//			
//			if (temp.getIsPartition() == topologyElement.getIsPartition()) {
//				if (temp.getIsIn() == topologyElement.getIsIn()
//						&& temp.getmDoorID1() == topologyElement.getmDoorID1()
//						&& temp.getmDoorID2() == topologyElement.getmDoorID2()
//						&& temp.getmParID() == topologyElement.getmParID()) {
//					exist = true;
//				} else exist = false;
//			} else exist = false;
//		}
//		
//		if (!exist) {
		connectivityTierIn.add(topologyElement);
//		}
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
//		boolean exist = false;
//		
//		for (int i = 0; i < connectivityTierOut.size(); i ++) {
//			TopologyElement temp = connectivityTierOut.get(i);
//			
//			if (temp.getIsPartition() == topologyElement.getIsPartition()) {
//				if (temp.getIsIn() == topologyElement.getIsIn()
//						&& temp.getmDoorID1() == topologyElement.getmDoorID1()
//						&& temp.getmDoorID2() == topologyElement.getmDoorID2()
//						&& temp.getmParID() == topologyElement.getmParID()) {
//					exist = true;
//					System.out.println("hey!!!");
//				} else exist = false;
//			} else exist = false;
//		}
//		
//		if (!exist) {
		connectivityTierOut.add(topologyElement);
//		}
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
