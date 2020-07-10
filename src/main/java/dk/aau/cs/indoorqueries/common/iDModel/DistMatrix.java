/**
 *
 */
package dk.aau.cs.indoorqueries.common.iDModel;

import dk.aau.cs.indoorqueries.common.indoorEntitity.*;
import dk.aau.cs.indoorqueries.common.utilities.DataGenConstant;
import dk.aau.cs.indoorqueries.common.utilities.Functions;
import dk.aau.cs.indoorqueries.common.utilities.RoomType;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

/**
 * <h>DistMatrix</h>
 * represent the exit to exit (exit2exit) distance
 *
 * @author feng zijin, Tiantian Liu
 *
 */
public class DistMatrix {
	private double[][] matrix;
	private int mID;            // partition id
	private boolean isPartition;        // is partition or floor

	public DistMatrix(int mID, boolean isPartition) throws IOException {
		this.mID = mID;
		this.isPartition = isPartition;
		createMatrix();
	}

	/**
	 * create the exit2exit for this partition
	 */
	private void createMatrix() throws IOException {
		if (DataGenConstant.divisionType == 0) {
			Partition partition = IndoorSpace.iPartitions.get(mID);
			int doorNum = partition.getmDoors().size();
			int index;
			if (DataGenConstant.dataset.equals("MZB")) {
				if (partition.getmFloor() >= 2) {
					index = 2;
				}
				else {
					index = partition.getmFloor();
				}
			}
			else {
				index = 0;
			}

			matrix = new double[doorNum][doorNum];

//			System.out.println(partition.getmID() + " has door = " + IndoorSpace.iPartitions.get(partition.getmID()).getmDoors().size());
			//			System.out.println("D2dHashMap size = " + partition.getD2dHashMap().size() + " matrix size = " + matrix.length + " * "
			//					+ "" + matrix[0].length);
			if (partition.getmType() == RoomType.HALLWAY) {
				ArrayList<Integer> doors = partition.getmDoors();
				for (int i = 0; i < doors.size(); i++) {
					for (int j = i + 1; j < doors.size(); j++) {
						Door door1 = IndoorSpace.iDoors.get(doors.get(i));
						Door door2 = IndoorSpace.iDoors.get(doors.get(j));
//						System.out.println("door1: " + door1.getmID() + " door2: " + door2.getmID());
						double distance = (double) GenTopology.hallwayDistMatrix_floor.get(index).get(door1.getX() + "." + door1.getY() + "-" + door2.getX() + "." + door2.getY());
						matrix[i][j] = distance;
						matrix[j][i] = distance;
					}
				}
			}
			else {
				Iterator it = partition.getD2dHashMap().entrySet().iterator();
				while (it.hasNext()) {
					HashMap.Entry pair = (HashMap.Entry)it.next();

					// get the key and value
					String key = (String) pair.getKey();
					int[] doorID = Functions.keyReverseConventer(key);
					D2Ddistance d2Ddistance = (D2Ddistance) pair.getValue();
					double distance = d2Ddistance.getDistance();

					// find the index of the door stored in partition
					int index1 = partition.getDoorIndex(doorID[0]);
					int index2 = partition.getDoorIndex(doorID[1]);

					// put the distance into matrix
					if(index1 != -1 && index2 != -1 && matrix[index1][index2] == 0.0) {
						matrix[index1][index2] = distance;
						matrix[index2][index1] = distance;
					} else {
						System.out.println("something wrong_DistMatrix_createMatrix");
					}
				}
			}

		} else {

			if (isPartition) {
				Partition partition = IndoorSpace.iPartitions.get(mID);
				int doorNum = partition.getmDoors().size();

				matrix = new double[doorNum][doorNum];

				//			System.out.println(partition.getmID() + " has door = " + IndoorSpace.iPartitions.get(partition.getmID()).getmDoors().size());
				//			System.out.println("D2dHashMap size = " + partition.getD2dHashMap().size() + " matrix size = " + matrix.length + " * "
				//					+ "" + matrix[0].length);

				Iterator it = partition.getD2dHashMap().entrySet().iterator();
				while (it.hasNext()) {
					HashMap.Entry pair = (HashMap.Entry) it.next();

					// get the key and value
					String key = (String) pair.getKey();
					int[] doorID = Functions.keyReverseConventer(key);
					D2Ddistance d2Ddistance = (D2Ddistance) pair.getValue();
					double distance = d2Ddistance.getDistance();

					// find the index of the door stored in partition
					int index1 = partition.getDoorIndex(doorID[0]);
					int index2 = partition.getDoorIndex(doorID[1]);

					// put the distance into matrix
					if (index1 != -1 && index2 != -1 && matrix[index1][index2] == 0.0) {
						matrix[index1][index2] = distance;
						matrix[index2][index1] = distance;
					} else {
						System.out.println("something wrong_DistMatrix_createMatrix");
					}
				}

				//			System.out.println(Functions.print2Ddoublearray(matrix, 0, matrix.length, 0, matrix[0].length));

			} else {            // if it is floor

				Floor floor = IndoorSpace.iFloors.get(mID);
				int doorNum = floor.getmDoors().size();

				matrix = new double[doorNum][doorNum];

				//			System.out.println(floor.getmID() + " has door = " + IndoorSpace.iFloors.get(floor.getmID()).getmDoors().size());
				//			System.out.println("D2dHashMap size = " + floor.getD2dHashMap().size() + " matrix size = " + matrix.length + " * "
				//					+ "" + matrix[0].length);

				Iterator it = floor.getD2dHashMap().entrySet().iterator();
				while (it.hasNext()) {
					HashMap.Entry pair = (HashMap.Entry) it.next();

					// get the key and value
					String key = (String) pair.getKey();
					int[] doorID = Functions.keyReverseConventer(key);
					D2Ddistance d2Ddistance = (D2Ddistance) pair.getValue();
					double distance = d2Ddistance.getDistance();

					// find the index of the door stored in partition
					int index1 = floor.getDoorIndex(doorID[0]);
					int index2 = floor.getDoorIndex(doorID[1]);

					// put the distance into matrix
					if (index1 != -1 && index2 != -1 && matrix[index1][index2] == 0.0) {
						matrix[index1][index2] = distance;
						matrix[index2][index1] = distance;
					} else {
						System.out.println("something wrong_DistMatrix_createMatrix");
					}
				}

				//			System.out.println(Functions.print2Ddoublearray(matrix, 0, matrix.length, 0, matrix[0].length));
			}


		}
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
	 * @return the matrix
	 */
	public double[][] getMatrix() {
		return matrix;
	}

	/**
	 * @param matrix
	 *            the matrix to set
	 */
	public void setMatrix(double[][] matrix) {
		this.matrix = matrix;
	}

	/**
	 * @param d1
	 * @param d2
	 * @return the distance
	 */
	public double getDistance(int d1, int d2) {
		Partition partition = IndoorSpace.iPartitions.get(mID);
		int index1 = partition.getDoorIndex(d1);
		int index2 = partition.getDoorIndex(d2);

		if (index1 < 0 || index2 < 0 || index1 > matrix.length || index2 > matrix[0].length) return -1;

//		System.out.print("return " + matrix[index1][index2] + " ");
//		System.out.println(IndoorSpace.iDoors.get(d1).toString() + " to " + IndoorSpace.iDoors.get(d2).toString());

		return matrix[index1][index2];
	}
}
