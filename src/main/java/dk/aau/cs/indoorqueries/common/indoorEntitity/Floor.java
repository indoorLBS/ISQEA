/**
 *
 */
package dk.aau.cs.indoorqueries.common.indoorEntitity;

import dk.aau.cs.indoorqueries.common.iDModel.Topology;
import dk.aau.cs.indoorqueries.common.utilities.DataGenConstant;
import dk.aau.cs.indoorqueries.common.utilities.Functions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * <h>Floor</h>
 * represent floor
 * @author feng zijin
 *
 */
public class Floor extends Rect {
	private double x1, y1 = 9999.0;
	private double x2, y2 = -9999.0;
	private int mID;

	private ArrayList<Integer> mPartitions = new ArrayList<Integer>();    // the partitions of this floor

	private ArrayList<Integer> mDoors = new ArrayList<Integer>();    // the doors of this floor

	private HashMap<String, D2Ddistance> d2dHashMap = new HashMap<String, D2Ddistance>();        // the distance between the relevant doors of this floor

	private Topology topology;  // the connectivity tier of this floor

	private int[] mKeywords = new int[0];


	/**
	 * Constructor 
	 *
	 * @param mID
	 */
	public Floor(int mID) {
		super(0, 0, 0, 0);
		this.mID = mID;
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
	 * add partition to this floor 
	 *
	 * @param partition
	 */
	public void addPartition(int partition) {
		mPartitions.add(partition);
	}

	/**
	 * @return the mPartitions
	 */
	public ArrayList<Integer> getmPartitions() {
		return mPartitions;
	}

	/**
	 * add all partitions to this floor 
	 *
	 * @param mPartitions
	 *            the mPartitions to set
	 */
	public void setPartitions(ArrayList<Integer> mPartitions) {
		this.mPartitions = mPartitions;
	}

	/**
	 * add door to this floor 
	 *
	 * @param door
	 */
	public void addDoor(int door) {
		mDoors.add(door);
	}

	/**
	 * @return the mDoors
	 */
	public List<Integer> getmDoors() {
		return mDoors;
	}

	/**
	 * add all Doors to this floor 
	 *
	 * @param mDoors
	 *            the mDoors to set
	 */
	public void setmDoors(ArrayList<Integer> mDoors) {
		this.mDoors = mDoors;
	}

	/**
	 * @return the mKeywords
	 */
	public int[] getmKeywords() {
		return mKeywords;
	}

	/**
	 * @param mKeywords
	 *            the mKeywords to set
	 */
	public void setmKeywords(int[] mKeywords) {
		this.mKeywords = mKeywords;
	}

	/**
	 * update the mKeywords
	 */
	public void updatemKeywords() {
		this.mKeywords = new int[DataGenConstant.mKeyworSize];

		int partitionSize = this.mPartitions.size();
		for (int i = 0; i < partitionSize; i++) {
			int[] tempArr = null;

			int tempArrSize = tempArr.length;
			for (int j = 0; j < tempArrSize; j++) {
				if (tempArr[j] == 1) {
					this.mKeywords[j] = 1;
				}
			}
		}

//		String str = "";
//		
//		for (int i = 0; i < mKeywords.length; i ++) {
//			if (mKeywords[i] == 1) str = str + i + "\t";
//		}
//		
//		System.out.println(this.mID + " " + str);
	}

	/**
	 * @return the topology
	 */
	public Topology getTopology() {
		return topology;
	}

	/**
	 * @param topology
	 *            the topology to set
	 */
	public void setTopology(Topology topology) {
		this.topology = topology;
	}

	/**
	 * update the corner point coordinate
	 */
	public void updateCorner() {
		int partitionSize = this.mPartitions.size();
		for (int i = 0; i < partitionSize; i++) {
			Partition partition = IndoorSpace.iPartitions.get(this.mPartitions.get(i));

			if (this.x1 > partition.getX1()) x1 = partition.getX1();
			if (this.y1 > partition.getY1()) y1 = partition.getY1();
			if (this.x2 < partition.getY2()) x2 = partition.getY2();
			if (this.y2 < partition.getY2()) y2 = partition.getY2();
		}

		super.setX1(x1);
		super.setX2(x2);
		super.setY1(y1);
		super.setY2(y2);

//		System.out.println("floor corner updated " + x1 + " " + y1 + " " + x2 + " " + y2);
	}

	/**
	 * @param doorID
	 * @return the index of the door in mDoors list
	 */
	public int getDoorIndex(int doorID) {
		int result = -1;

		int doorSize = this.mDoors.size();
		for (int i = 0; i < doorSize; i++) {
			if (doorID == this.mDoors.get(i)) {
				result = i;
				break;
			}

		}

		return result;
	}

	/**
	 * @return the d2dHashMap
	 */
	public HashMap<String, D2Ddistance> getD2dHashMap() {
		d2dHashMap = new HashMap<String, D2Ddistance>();
		Collections.sort(this.mDoors);

		int doorSize = this.mDoors.size();
		for (int i = 0; i < doorSize; i++) {
			int index_1 = this.mDoors.get(i);
			Door door1 = IndoorSpace.iDoors.get(index_1);

			int doorSize1 = this.mDoors.size();
			for (int j = i + 1; j < doorSize1; j++) {
				int index_2 = this.mDoors.get(j);
				Door door2 = IndoorSpace.iDoors.get(index_2);
				D2Ddistance d2dDist = new D2Ddistance(index_1, index_2, door1.eDist(door2));
				d2dHashMap.put(Functions.keyConventer(index_1, index_2), d2dDist);
			}
		}
		this.setD2dHashMap(d2dHashMap);

//		System.out.println("partition " + mID + " d2dHashMap size = " + d2dHashMap.size());

		return d2dHashMap;
	}

	/**
	 * @param d2dHashMap
	 *            the d2dHashMap to set
	 */
	public void setD2dHashMap(HashMap<String, D2Ddistance> d2dHashMap) {
		this.d2dHashMap = d2dHashMap;
	}

	/**
	 * @return the x1
	 */
	public double getX1() {
		return x1;
	}

	/**
	 * @return the x2
	 */
	public double getX2() {
		return x2;
	}

	/**
	 * @return the y1
	 */
	public double getY1() {
		return y1;
	}

	/**
	 * @return the y2
	 */
	public double getY2() {
		return y2;
	}
}
