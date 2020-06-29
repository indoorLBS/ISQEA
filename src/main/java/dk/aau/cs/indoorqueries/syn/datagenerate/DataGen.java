/**
 *
 */
package dk.aau.cs.indoorqueries.syn.datagenerate;

import dk.aau.cs.indoorqueries.common.indoorEntitity.*;
import dk.aau.cs.indoorqueries.common.utilities.DataGenConstant;
import dk.aau.cs.indoorqueries.common.utilities.DoorType;
import dk.aau.cs.indoorqueries.common.utilities.RoomType;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * <h>DataGen</h>
 * to generate the data
 *
 * @author feng zijin
 *
 */
public class DataGen {

	public static String outputPath = System.getProperty("user.dir");

	/**
	 * generates all the data
	 * @param dataType : 0 means Less doors, 1 means regular, 2 means more doors
	 */
	public void genAllData(int dataType, int divisionType) {
		if (divisionType == 1) {
			genIndoorSpace(dataType);

			duplicateIndoorSpace(DataGenConstant.nFloor);

			linkFloors();

			System.out.println();

			if (true == saveDP()) {
				System.out.println("Partitions & Doors Generating Finished! " + " partitions = "
						+ IndoorSpace.iPartitions.size() + " " + " doors = " + IndoorSpace.iDoors.size());
			}
		}
		if (divisionType == 0 || divisionType == 2) {
			genIndoorSpaceforDivision();
			duplicateIndoorSpace(DataGenConstant.nFloor);

			linkFloors();

			System.out.println();

			if (true == saveDP()) {
				System.out.println("Partitions & Doors Generating Finished! " + " partitions = "
						+ IndoorSpace.iPartitions.size() + " " + " doors = " + IndoorSpace.iDoors.size());
			}
		}
	}


	public void linkFloors() {
		for (int i = 1; i < DataGenConstant.nFloor; i++) {
			List<Integer> doors = IndoorSpace.iFloors.get(i).getmDoors();
			for (int j = 0; j < doors.size(); j++) {
				int doorId = doors.get(j);
				Door door = IndoorSpace.iDoors.get(doorId);
				ArrayList<Integer> parts = door.getmPartitions();
				if (parts.size() > 1) {
					System.out.println("something wrong with exit door's partition: dk.aau.cs.indoorqueries.datagenerate.DataGen.linkFloors");
				}
				int partId = parts.get(0);
				int downPartId = partId - IndoorSpace.iNumberParsPerFloor;
				Partition downPar = IndoorSpace.iPartitions.get(downPartId);
				ArrayList<Integer> mDoors = downPar.getmDoors();
				mDoors.add(doorId);
				downPar.setmDoors(mDoors);

				parts.add(downPartId);
				door.setmPartitions(parts);
			}
		}
	}


	/**
	 * duplicates a number of floors
	 *
	 * @param floorNumber
	 *            the number of floors in the whole building
	 */
	private void duplicateIndoorSpace(int floorNumber) {
		int curSizePar = IndoorSpace.iPartitions.size();
		int curSizeDoor = IndoorSpace.iDoors.size();
		IndoorSpace.iNumberDoorsPerFloor = curSizeDoor;
		IndoorSpace.iNumberParsPerFloor = curSizePar;
		IndoorSpace.iNumberFloorPerMall = DataGenConstant.nFloor;

		System.out.println("per floor door num = " + curSizeDoor + " partition num = " + curSizePar);

		// ground floor
		Floor tempFloor = new Floor(0);
		for (int parIndex = 0; parIndex < curSizePar; parIndex++) {
			tempFloor.addPartition(IndoorSpace.iPartitions.get(parIndex).getmID());
		}

		tempFloor.addDoor(IndoorSpace.iDoors.get(IndoorSpace.iNumberDoorsPerFloor - 6).getmID());
		IndoorSpace.iDoors.get(IndoorSpace.iNumberDoorsPerFloor - 6).addFloor(0);
//		IndoorSpace.iDoors.get(210).addFloor(1);
//		System.out.println((IndoorSpace.iDoors.get(210).getmID() == 210) + " " + 210);

		tempFloor.addDoor(IndoorSpace.iDoors.get(IndoorSpace.iNumberDoorsPerFloor - 4).getmID());
		IndoorSpace.iDoors.get(IndoorSpace.iNumberDoorsPerFloor - 4).addFloor(0);
//		IndoorSpace.iDoors.get(212).addFloor(1);
//		System.out.println((IndoorSpace.iDoors.get(212).getmID() == 212) + " " + 212);

		tempFloor.addDoor(IndoorSpace.iDoors.get(IndoorSpace.iNumberDoorsPerFloor - 3).getmID());
		IndoorSpace.iDoors.get(IndoorSpace.iNumberDoorsPerFloor - 3).addFloor(0);
//		IndoorSpace.iDoors.get(213).addFloor(1);
//		System.out.println((IndoorSpace.iDoors.get(213).getmID() == 213) + " " + 213);

		tempFloor.addDoor(IndoorSpace.iDoors.get(IndoorSpace.iNumberDoorsPerFloor - 2).getmID());
		IndoorSpace.iDoors.get(IndoorSpace.iNumberDoorsPerFloor - 2).addFloor(0);
//		IndoorSpace.iDoors.get(214).addFloor(1);
//		System.out.println((IndoorSpace.iDoors.get(214).getmID() == 214) + " " + 214);
//		System.out.println("floor " + tempFloor.getmID() + " has " + tempFloor.getmPartitions().size() + " partition and " + tempFloor.getmDoors().size() + ""
//				+ " doors");

		tempFloor.updateCorner();
		IndoorSpace.iFloors.add(tempFloor);
		Graph.Floors.put(tempFloor.getmID(), tempFloor);


		// other than ground floor
		for (int floorIndex = 1; floorIndex < floorNumber; floorIndex++) {
			Floor floor = new Floor(floorIndex);

			for (int parIndex = 0; parIndex < curSizePar; parIndex++) {
				Partition curPar = IndoorSpace.iPartitions.get(parIndex);
				Partition newPar = new Partition(curPar);
				newPar.setmFloor(floorIndex);
				newPar.setmID(parIndex + floorIndex * curSizePar);
//				System.out.println("partition " + curPar.getmID() + " has size = " + curPar.getD2dHashMap().size());
//				newPar.setD2dHashMap(curPar.getD2dHashMap());

//				System.out.println("partition generated with id = " + (newPar.getmID() + 1) + " type = " + newPar.getmType() + ""
//						+ " on floor = " + newPar.getmFloor() + " has size = " + newPar.getD2dHashMap().size());

				ArrayList<Integer> mDoors = new ArrayList<Integer>();
				for (int mDoor : curPar.getmDoors()) {
					mDoors.add(mDoor + floorIndex * curSizeDoor);
				}
				newPar.setmDoors(mDoors);
				floor.addPartition(newPar.getmID());
				IndoorSpace.iPartitions.add(newPar);
				Graph.Partitions.put(newPar.getmID(), newPar);
			}

			for (int doorIndex = 0; doorIndex < curSizeDoor; doorIndex++) {
				Door curDoor = IndoorSpace.iDoors.get(doorIndex);
				Door newDoor = new Door(curDoor);
				newDoor.setmFloor(floorIndex);
				newDoor.setmID(doorIndex + floorIndex * curSizeDoor);

//				System.out.println("door generated with id = " + (newDoor.getmID() + 1) + " on floor = " + newDoor.getmFloor());

				ArrayList<Integer> mPars = new ArrayList<Integer>();
				for (int mPar : curDoor.getmPartitions()) {
					mPars.add(mPar + floorIndex * curSizePar);
				}
				newDoor.setmPartitions(mPars);
				IndoorSpace.iDoors.add(newDoor);
				Graph.Doors.put(newDoor.getmID(), newDoor);
			}

			floor.addDoor(IndoorSpace.iDoors.get(IndoorSpace.iNumberDoorsPerFloor - 6 + floorIndex * curSizeDoor).getmID());
			IndoorSpace.iDoors.get(IndoorSpace.iNumberDoorsPerFloor - 6 + floorIndex * curSizeDoor).addFloor(floorIndex - 1);
			IndoorSpace.iDoors.get(IndoorSpace.iNumberDoorsPerFloor - 6 + floorIndex * curSizeDoor).addFloor(floorIndex);
			if (floorIndex < (floorNumber - 1))
				IndoorSpace.iDoors.get(IndoorSpace.iNumberDoorsPerFloor - 6 + floorIndex * curSizeDoor).addFloor(floorIndex + 1);
//			System.out.println((IndoorSpace.iDoors.get(210 + floorIndex * curSizeDoor).getmID() == (210 + floorIndex * curSizeDoor)) + " " + (210 + floorIndex * curSizeDoor));

			floor.addDoor(IndoorSpace.iDoors.get(IndoorSpace.iNumberDoorsPerFloor - 4 + floorIndex * curSizeDoor).getmID());
			IndoorSpace.iDoors.get(IndoorSpace.iNumberDoorsPerFloor - 4 + floorIndex * curSizeDoor).addFloor(floorIndex - 1);
			IndoorSpace.iDoors.get(IndoorSpace.iNumberDoorsPerFloor - 4 + floorIndex * curSizeDoor).addFloor(floorIndex);
			if (floorIndex < (floorNumber - 1))
				IndoorSpace.iDoors.get(IndoorSpace.iNumberDoorsPerFloor - 4 + floorIndex * curSizeDoor).addFloor(floorIndex + 1);
//			System.out.println((IndoorSpace.iDoors.get(212 + floorIndex * curSizeDoor).getmID() == (212 + floorIndex * curSizeDoor)) + " " + (212 + floorIndex * curSizeDoor));

			floor.addDoor(IndoorSpace.iDoors.get(IndoorSpace.iNumberDoorsPerFloor - 3 + floorIndex * curSizeDoor).getmID());
			IndoorSpace.iDoors.get(IndoorSpace.iNumberDoorsPerFloor - 3 + floorIndex * curSizeDoor).addFloor(floorIndex - 1);
			IndoorSpace.iDoors.get(IndoorSpace.iNumberDoorsPerFloor - 3 + floorIndex * curSizeDoor).addFloor(floorIndex);
			if (floorIndex < (floorNumber - 1))
				IndoorSpace.iDoors.get(IndoorSpace.iNumberDoorsPerFloor - 3 + floorIndex * curSizeDoor).addFloor(floorIndex + 1);
//			System.out.println((IndoorSpace.iDoors.get(213 + floorIndex * curSizeDoor).getmID() == (213 + floorIndex * curSizeDoor)) + " " + (213 + floorIndex * curSizeDoor));

			floor.addDoor(IndoorSpace.iDoors.get(IndoorSpace.iNumberDoorsPerFloor - 2 + floorIndex * curSizeDoor).getmID());
			IndoorSpace.iDoors.get(IndoorSpace.iNumberDoorsPerFloor - 2 + floorIndex * curSizeDoor).addFloor(floorIndex - 1);
			IndoorSpace.iDoors.get(IndoorSpace.iNumberDoorsPerFloor - 2 + floorIndex * curSizeDoor).addFloor(floorIndex);
			if (floorIndex < (floorNumber - 1))
				IndoorSpace.iDoors.get(IndoorSpace.iNumberDoorsPerFloor - 2 + floorIndex * curSizeDoor).addFloor(floorIndex + 1);
//			System.out.println((IndoorSpace.iDoors.get(214 + floorIndex * curSizeDoor).getmID() == (214 + floorIndex * curSizeDoor)) + " " + (214 + floorIndex * curSizeDoor));
//			System.out.println("floor " + floor.getmID() + " has " + tempFloor.getmPartitions().size() + " partition and " + tempFloor.getmDoors().size() + ""
//					+ " doors");

			tempFloor.updateCorner();
			IndoorSpace.iFloors.add(floor);
			Graph.Floors.put(floor.getmID(), floor);
		}
	}

	/**
	 * generates the indoor space
	 * @param dataType: 0 means Less doors, 1 means regular, 2 means more doors
	 */
	private void genIndoorSpace(int dataType) {
		// partitions
		initParitions(1);

//		System.out.println("==> first reflect");
		parReflection(0, 684);

//		System.out.println("==> second reflect");
		parReflection(1, 684);

		genOtherHallway(1);
		genStaircase();

		// doors
		if (dataType == 0) {
			initDoorsLess();
		} else if (dataType == 1) {
			initDoors();
		} else if (dataType == 2) {
			initDoorsMore();
		}


//		System.out.println("==> first reflect");
		doorReflection(0, 684);

//		System.out.println("==> second reflect");
		doorReflection(1, 684);

		genOtherDoor();

		setDoorType();

		// link partition and door together
		linkGraph();

	}

	/**
	 * generates the indoor space for divisionType 0 or 2
	 *
	 */
	private void genIndoorSpaceforDivision() {
		// partitions
		initParitions(DataGenConstant.divisionType);

//		System.out.println("==> first reflect");
		parReflection(0, 684);

//		System.out.println("==> second reflect");
		parReflection(1, 684);

		genOtherHallway(DataGenConstant.divisionType);
		genStaircase();

		// doors
		initDoorsforDivide(DataGenConstant.divisionType);


//		System.out.println("==> first reflect");
		doorReflection(0, 684);

//		System.out.println("==> second reflect");
		doorReflection(1, 684);

		genOtherDoorforDivide();

		setDoorType();

		// link partition and door together
		linkGraph();

	}

	/**
	 * tests if the door is the entrance of the partition, if it is, link them
	 * together
	 *
	 */
	public void linkGraph() {
		if (DataGenConstant.divisionType == 1 || DataGenConstant.divisionType == 2) {
			Iterator<Partition> itrPar = IndoorSpace.iPartitions.iterator();

			while (itrPar.hasNext()) {
				Partition curPar = itrPar.next();
				Iterator<Door> itrDoor = IndoorSpace.iDoors.iterator();
				while (itrDoor.hasNext()) {
					Door curDoor = itrDoor.next();
					if (curPar.testDoor(curDoor) == true) {
						curPar.addDoor(curDoor.getmID());
						curDoor.addPar(curPar.getmID());
						//System.out.println((curPar.getmID() + 1) + " has door " + (curDoor.getmID() + 1));
					}
				}
			}
		}
		if (DataGenConstant.divisionType == 0) {
			Iterator<Partition> itrPar = IndoorSpace.iPartitions.iterator();

			while (itrPar.hasNext()) {
				Partition curPar = itrPar.next();
				Iterator<Door> itrDoor = IndoorSpace.iDoors.iterator();
				while (itrDoor.hasNext()) {
					Door curDoor = itrDoor.next();
					if (curPar.testDoor(curDoor) == true) {
						curPar.addDoor(curDoor.getmID());
						curDoor.addPar(curPar.getmID());
						//System.out.println((curPar.getmID() + 1) + " has door " + (curDoor.getmID() + 1));
					} else if ((curPar.getmType() == RoomType.HALLWAY) && ((curDoor.getX() <= curPar.getX2() && curDoor.getX() >= curPar.getX1())
							|| (curDoor.getX() >= curPar.getX2() && curDoor.getX() <= curPar.getX1())) && ((curDoor.getY() <= curPar.getY2()
							&& curDoor.getY() >= curPar.getY1()) || (curDoor.getY() >= curPar.getY2() && curDoor.getY() <= curPar.getY1()))) {
						if (curDoor.getmID() % 42 == 13 || curDoor.getmID() % 42 == 16 || curDoor.getmID() % 42 == 19 || curDoor.getmID() % 42 == 23 ||
								curDoor.getmID() % 42 == 26 || curDoor.getmID() % 42 == 29 || curDoor.getmID() % 42 == 38
								|| curDoor.getmID() % 42 == 40) continue;
						curPar.addDoor(curDoor.getmID());
						curDoor.addPar(curPar.getmID());
						//System.out.println((curPar.getmID() + 1) + " has door " + (curDoor.getmID() + 1));
					}
				}
			}
		}
	}

	/**
	 * generates the doors as entrances for each partition
	 *
	 */
	public void initDoors() {
		System.out.println("==> doors");

		Iterator<Partition> itr = IndoorSpace.iPartitions.iterator();

		while (itr.hasNext()) {
			Partition curPar = itr.next();
			Door aDoor = null;

			int partitionNo = curPar.getmID() + 1;

			// on the upper part
			if ((partitionNo == 7) || (partitionNo >= 12 && partitionNo <= 18) || (partitionNo == 20)
					|| (partitionNo >= 23 && partitionNo <= 24) || (partitionNo == 32)) {
//				System.out.print("upper part ");

				aDoor = new Door((curPar.getX1() + curPar.getX2()) / 2, curPar.getY1());

				if (IndoorSpace.iDoors.contains(aDoor)) ;
				IndoorSpace.iDoors.add(aDoor);
				System.out.println("partition " + partitionNo + " has door " + (IndoorSpace.iDoors.size()) + " at ("
						+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getX() + ", "
						+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getY() + ")");
			}

			// on the right part
			if ((partitionNo >= 7 && partitionNo <= 17) || (partitionNo == 19) || (partitionNo == 22)
					|| (partitionNo == 25) || (partitionNo == 27) || (partitionNo == 31)) {
//				System.out.print("right part ");

				aDoor = new Door(curPar.getX2(), (curPar.getY1() + curPar.getY2()) / 2);

				if (IndoorSpace.iDoors.contains(aDoor)) ;
				IndoorSpace.iDoors.add(aDoor);
				System.out.println("partition " + partitionNo + " has door " + (IndoorSpace.iDoors.size()) + " at ("
						+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getX() + ", "
						+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getY() + ")");
			}

			// on the bottom part
			if ((partitionNo >= 2 && partitionNo <= 6) || (partitionNo >= 13 && partitionNo <= 14)
					|| (partitionNo == 18) || (partitionNo == 20) || (partitionNo >= 24 && partitionNo <= 25)
					|| (partitionNo == 29) || (partitionNo == 32)) {
//				System.out.print("bottom part ");

				aDoor = new Door((curPar.getX1() + curPar.getX2()) / 2, curPar.getY2());

				if (IndoorSpace.iDoors.contains(aDoor)) ;
				IndoorSpace.iDoors.add(aDoor);
				System.out.println("partition " + partitionNo + " has door " + (IndoorSpace.iDoors.size()) + " at ("
						+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getX() + ", "
						+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getY() + ")");
			}

			// on the left part
			if ((partitionNo == 2) || (partitionNo == 12) || (partitionNo >= 16 && partitionNo <= 19)
					|| (partitionNo >= 21 && partitionNo <= 22) || (partitionNo == 31)) {
//				System.out.print("left part ");

				aDoor = new Door(curPar.getX1(), (curPar.getY1() + curPar.getY2()) / 2);

				if (IndoorSpace.iDoors.contains(aDoor)) ;
				IndoorSpace.iDoors.add(aDoor);
				System.out.println("partition " + partitionNo + " has door " + (IndoorSpace.iDoors.size()) + " at ("
						+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getX() + ", "
						+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getY() + ")");
			}
		}
	}

	public void initDoorsMore() {
		System.out.println("==> doors");

		Iterator<Partition> itr = IndoorSpace.iPartitions.iterator();

		while (itr.hasNext()) {
			Partition curPar = itr.next();
			Door aDoor = null;

			int partitionNo = curPar.getmID() + 1;

			// on the upper part
			if ((partitionNo == 7) || (partitionNo >= 12 && partitionNo <= 18) || (partitionNo == 20)
					|| (partitionNo >= 23 && partitionNo <= 24) || (partitionNo == 32)) {
//				System.out.print("upper part ");

				aDoor = new Door((curPar.getX1() + curPar.getX2()) / 2, curPar.getY1());

				if (IndoorSpace.iDoors.contains(aDoor)) ;
				IndoorSpace.iDoors.add(aDoor);
				System.out.println("partition " + partitionNo + " has door " + (IndoorSpace.iDoors.size()) + " at ("
						+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getX() + ", "
						+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getY() + ")");
			}

			// on the right part
			if ((partitionNo >= 2 && partitionNo <= 17) || (partitionNo == 19) || (partitionNo == 22)
					|| (partitionNo == 25) || (partitionNo == 27) || (partitionNo == 31)) {
//				System.out.print("right part ");

				aDoor = new Door(curPar.getX2(), (curPar.getY1() + curPar.getY2()) / 2);

				if (IndoorSpace.iDoors.contains(aDoor)) ;
				IndoorSpace.iDoors.add(aDoor);
				System.out.println("partition " + partitionNo + " has door " + (IndoorSpace.iDoors.size()) + " at ("
						+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getX() + ", "
						+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getY() + ")");
			}

			// on the bottom part
			if ((partitionNo >= 2 && partitionNo <= 11) || (partitionNo >= 13 && partitionNo <= 14)
					|| (partitionNo == 18) || (partitionNo == 20) || (partitionNo >= 24 && partitionNo <= 25)
					|| (partitionNo == 29) || (partitionNo == 32)) {
//				System.out.print("bottom part ");

				aDoor = new Door((curPar.getX1() + curPar.getX2()) / 2, curPar.getY2());

				if (IndoorSpace.iDoors.contains(aDoor)) ;
				IndoorSpace.iDoors.add(aDoor);
				System.out.println("partition " + partitionNo + " has door " + (IndoorSpace.iDoors.size()) + " at ("
						+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getX() + ", "
						+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getY() + ")");
			}

			// on the left part
			if ((partitionNo == 2) || (partitionNo == 12) || (partitionNo >= 16 && partitionNo <= 19)
					|| (partitionNo >= 21 && partitionNo <= 22) || (partitionNo == 31)) {
//				System.out.print("left part ");

				aDoor = new Door(curPar.getX1(), (curPar.getY1() + curPar.getY2()) / 2);

				if (IndoorSpace.iDoors.contains(aDoor)) ;
				IndoorSpace.iDoors.add(aDoor);
				System.out.println("partition " + partitionNo + " has door " + (IndoorSpace.iDoors.size()) + " at ("
						+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getX() + ", "
						+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getY() + ")");
			}
		}
	}


	public void initDoorsLess() {
		System.out.println("==> doors");

		Iterator<Partition> itr = IndoorSpace.iPartitions.iterator();

		while (itr.hasNext()) {
			Partition curPar = itr.next();
			Door aDoor = null;

			int partitionNo = curPar.getmID() + 1;

			// on the upper part
			if ((partitionNo == 7) || (partitionNo >= 12 && partitionNo <= 15) || (partitionNo == 29)
					|| (partitionNo == 23) || (partitionNo == 32)) {
//				System.out.print("upper part ");

				aDoor = new Door((curPar.getX1() + curPar.getX2()) / 2, curPar.getY1());

				if (IndoorSpace.iDoors.contains(aDoor)) ;
				IndoorSpace.iDoors.add(aDoor);
				System.out.println("partition " + partitionNo + " has door " + (IndoorSpace.iDoors.size()) + " at ("
						+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getX() + ", "
						+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getY() + ")");
			}

			// on the right part
			if ((partitionNo >= 7 && partitionNo <= 11) || (partitionNo == 15) || (partitionNo == 19) || (partitionNo == 22)
					|| (partitionNo >= 25 && partitionNo <= 27) || (partitionNo == 31)) {
//				System.out.print("right part ");

				aDoor = new Door(curPar.getX2(), (curPar.getY1() + curPar.getY2()) / 2);

				if (IndoorSpace.iDoors.contains(aDoor)) ;
				IndoorSpace.iDoors.add(aDoor);
				System.out.println("partition " + partitionNo + " has door " + (IndoorSpace.iDoors.size()) + " at ("
						+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getX() + ", "
						+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getY() + ")");
			}

			// on the bottom part
			if ((partitionNo >= 2 && partitionNo <= 6)
					|| (partitionNo == 18) || (partitionNo == 20) || (partitionNo >= 24 && partitionNo <= 25)
					|| (partitionNo == 29) || (partitionNo == 32)) {
//				System.out.print("bottom part ");

				aDoor = new Door((curPar.getX1() + curPar.getX2()) / 2, curPar.getY2());

				if (IndoorSpace.iDoors.contains(aDoor)) ;
				IndoorSpace.iDoors.add(aDoor);
				System.out.println("partition " + partitionNo + " has door " + (IndoorSpace.iDoors.size()) + " at ("
						+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getX() + ", "
						+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getY() + ")");
			}

			// on the left part
			if ((partitionNo == 2) || (partitionNo == 12) || (partitionNo >= 16 && partitionNo <= 18)
					|| (partitionNo == 21) || (partitionNo == 31)) {
//				System.out.print("left part ");

				aDoor = new Door(curPar.getX1(), (curPar.getY1() + curPar.getY2()) / 2);

				if (IndoorSpace.iDoors.contains(aDoor)) ;
				IndoorSpace.iDoors.add(aDoor);
				System.out.println("partition " + partitionNo + " has door " + (IndoorSpace.iDoors.size()) + " at ("
						+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getX() + ", "
						+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getY() + ")");
			}
		}
	}

	/**
	 * generate the rest of hallways
	 */
	private void genOtherHallway(int divideType) {
		System.out.println("==> other hallways");

		if (divideType == 1 || divideType == 2) {

			IndoorSpace.iPartitions.add(new Partition(648.0, 720.0, 288.0, 360.0, RoomType.HALLWAY));
//		System.out.println("Partition " + HSMDataGenConstant.mID_Par + " created " +
//				twoEndPointToString(IndoorSpace.iPartitions.get(IndoorSpace.iPartitions.size() - 1)));

			IndoorSpace.iPartitions.add(new Partition(648.0, 720.0, 360.0, 576.0, RoomType.HALLWAY));
//		System.out.println("Partition " + HSMDataGenConstant.mID_Par + " created " +
//				twoEndPointToString(IndoorSpace.iPartitions.get(IndoorSpace.iPartitions.size() - 1)));

			IndoorSpace.iPartitions.add(new Partition(576.0, 792.0, 576.0, 792.0, RoomType.HALLWAY));
//		System.out.println("Partition " + HSMDataGenConstant.mID_Par + " created " +
//				twoEndPointToString(IndoorSpace.iPartitions.get(IndoorSpace.iPartitions.size() - 1)));

			IndoorSpace.iPartitions.add(new Partition(648.0, 720.0, 792.0, 1008.0, RoomType.HALLWAY));
//		System.out.println("Partition " + HSMDataGenConstant.mID_Par + " created " +
//				twoEndPointToString(IndoorSpace.iPartitions.get(IndoorSpace.iPartitions.size() - 1)));

			IndoorSpace.iPartitions.add(new Partition(648.0, 720.0, 1008.0, 1080.0, RoomType.HALLWAY));
//		System.out.println("Partition " + HSMDataGenConstant.mID_Par + " created " +
//				twoEndPointToString(IndoorSpace.iPartitions.get(IndoorSpace.iPartitions.size() - 1)));

			IndoorSpace.iPartitions.add(new Partition(288.0, 360.0, 648.0, 720.0, RoomType.HALLWAY));
//		System.out.println("Partition " + HSMDataGenConstant.mID_Par + " created " +
//				twoEndPointToString(IndoorSpace.iPartitions.get(IndoorSpace.iPartitions.size() - 1)));

			IndoorSpace.iPartitions.add(new Partition(360.0, 576.0, 648.0, 720.0, RoomType.HALLWAY));
//		System.out.println("Partition " + HSMDataGenConstant.mID_Par + " created " +
//				twoEndPointToString(IndoorSpace.iPartitions.get(IndoorSpace.iPartitions.size() - 1)));

			IndoorSpace.iPartitions.add(new Partition(792.0, 1008.0, 648.0, 720.0, RoomType.HALLWAY));
//		System.out.println("Partition " + HSMDataGenConstant.mID_Par + " created " +
//				twoEndPointToString(IndoorSpace.iPartitions.get(IndoorSpace.iPartitions.size() - 1)));

			IndoorSpace.iPartitions.add(new Partition(1008.0, 1080.0, 648.0, 720.0, RoomType.HALLWAY));
//		System.out.println("Partition " + HSMDataGenConstant.mID_Par + " created " +
//				twoEndPointToString(IndoorSpace.iPartitions.get(IndoorSpace.iPartitions.size() - 1)));

		}

		if (divideType == 0) {
			IndoorSpace.iPartitions.add(new Partition(288, 1080, 288, 1080, RoomType.HALLWAY));
		}
	}

	/**
	 * generate staircase
	 */
	private void genStaircase() {
		System.out.println("==> staircases");

		IndoorSpace.iPartitions.add(new Partition(648.0, 720.0, 144.0, 288.0, RoomType.STAIRCASE));
//		System.out.println("Partition " + HSMDataGenConstant.mID_Par + " created " +
//				twoEndPointToString(IndoorSpace.iPartitions.get(IndoorSpace.iPartitions.size() - 1)));

		IndoorSpace.iPartitions.add(new Partition(648.0, 720.0, 1080.0, 1224.0, RoomType.STAIRCASE));
//		System.out.println("Partition " + HSMDataGenConstant.mID_Par + " created " +
//				twoEndPointToString(IndoorSpace.iPartitions.get(IndoorSpace.iPartitions.size() - 1)));

		IndoorSpace.iPartitions.add(new Partition(144.0, 288.0, 648.0, 720.0, RoomType.STAIRCASE));
//		System.out.println("Partition " + HSMDataGenConstant.mID_Par + " created " +
//				twoEndPointToString(IndoorSpace.iPartitions.get(IndoorSpace.iPartitions.size() - 1)));

		IndoorSpace.iPartitions.add(new Partition(1080.0, 1224.0, 648.0, 720.0, RoomType.STAIRCASE));
//		System.out.println("Partition " + HSMDataGenConstant.mID_Par + " created " +
//				twoEndPointToString(IndoorSpace.iPartitions.get(IndoorSpace.iPartitions.size() - 1)));

	}

	/**
	 * generates the rest of the doors
	 *
	 */
	private void genOtherDoor() {
		System.out.println("==> other doors");

		Iterator<Partition> itr = IndoorSpace.iPartitions.iterator();

		while (itr.hasNext()) {
			Partition curPar = itr.next();
			Door aDoor = null;

			int partitionNo = curPar.getmID() + 1;

			// on the upper part
			if ((partitionNo >= 129 && partitionNo <= 133)
					|| (partitionNo >= 138 && partitionNo <= 139)) {
//				System.out.print("upper part ");

				aDoor = new Door((curPar.getX1() + curPar.getX2()) / 2, curPar.getY1());

				if (IndoorSpace.iDoors.contains(aDoor)) ;
				IndoorSpace.iDoors.add(aDoor);
//				System.out.println("partition " + partitionNo + " has door " + (IndoorSpace.iDoors.size()) + " at ("
//						+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getX() + ", "
//						+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getY() + ")");
			}

			// on the right part
			if ((partitionNo == 141)) {
//				System.out.print("right part ");

				aDoor = new Door(curPar.getX2(), (curPar.getY1() + curPar.getY2()) / 2);

				if (IndoorSpace.iDoors.contains(aDoor)) ;
				IndoorSpace.iDoors.add(aDoor);
//				System.out.println("partition " + partitionNo + " has door " + (IndoorSpace.iDoors.size()) + " at ("
//						+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getX() + ", "
//						+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getY() + ")");
			}

			// on the bottom part
			if ((partitionNo == 139)) {
//				System.out.print("bottom part ");

				aDoor = new Door((curPar.getX1() + curPar.getX2()) / 2, curPar.getY2());

				if (IndoorSpace.iDoors.contains(aDoor)) ;
				IndoorSpace.iDoors.add(aDoor);
//				System.out.println("partition " + partitionNo + " has door " + (IndoorSpace.iDoors.size()) + " at ("
//						+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getX() + ", "
//						+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getY() + ")");
			}

			// on the left part
			if ((partitionNo == 131) || (partitionNo >= 134 && partitionNo <= 137)
					|| (partitionNo >= 140 && partitionNo <= 141)) {
//				System.out.print("left part ");

				aDoor = new Door(curPar.getX1(), (curPar.getY1() + curPar.getY2()) / 2);

				if (IndoorSpace.iDoors.contains(aDoor)) ;
				IndoorSpace.iDoors.add(aDoor);
//				System.out.println("partition " + partitionNo + " has door " + (IndoorSpace.iDoors.size()) + " at ("
//						+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getX() + ", "
//						+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getY() + ")");
			}
		}
	}

	/**
	 * set door type for exit door
	 */
	private void setDoorType() {
		int[] exitDoors = {IndoorSpace.iDoors.size() - 6, IndoorSpace.iDoors.size() - 4, IndoorSpace.iDoors.size() - 3, IndoorSpace.iDoors.size() - 2};
		for (int i = 0; i < exitDoors.length; i++) {
			Door door = IndoorSpace.iDoors.get(exitDoors[i]);
			door.setmType(DoorType.EXIT);
		}

	}

	/**
	 * generates the partitions from the left-top part of a single floor
	 *
	 */
	private void initParitions(int divideType) {

		// stores
		System.out.println("==> Stores");
		// add the large room x1, x2, y1, y2
		IndoorSpace.iPartitions.add(new Partition(0.0, 288.0, 0.0, 288.0, RoomType.STORE));
//		System.out.println("Partition " + HSMDataGenConstant.mID_Par + " created " +
//				twoEndPointToString(IndoorSpace.iPartitions.get(IndoorSpace.iPartitions.size() - 1)));

//		System.out.println("part 1");
		for (int i = 0; i < 5; i++) {
			IndoorSpace.iPartitions.add(new Partition(288.0 + 72 * i, 288.0 + 72 * (i + 1), 144.0, 288.0, RoomType.STORE));
//			System.out.println("Partition " + HSMDataGenConstant.mID_Par + " created " +
//					twoEndPointToString(IndoorSpace.iPartitions.get(IndoorSpace.iPartitions.size() - 1)));
		}

//		System.out.println("part 2");
		for (int i = 0; i < 5; i++) {
			IndoorSpace.iPartitions.add(new Partition(144.0, 288.0, 288.0 + 72 * i, 288.0 + 72 * (i + 1), RoomType.STORE));
//			System.out.println("Partition " + HSMDataGenConstant.mID_Par + " created " +
//					twoEndPointToString(IndoorSpace.iPartitions.get(IndoorSpace.iPartitions.size() - 1)));
		}

//		System.out.println("part 3");
		for (int i = 0; i < 4; i++) {
			IndoorSpace.iPartitions.add(new Partition(360.0 + 72 * i, 360.0 + 72 * (i + 1), 360.0, 432.0, RoomType.STORE));
//			System.out.println("Partition " + HSMDataGenConstant.mID_Par + " created " +
//					twoEndPointToString(IndoorSpace.iPartitions.get(IndoorSpace.iPartitions.size() - 1)));
		}

//		System.out.println("part 4");
		for (int i = 0; i < 3; i++) {
			IndoorSpace.iPartitions.add(new Partition(360.0, 432.0, 432.0 + 72 * i, 432.0 + 72 * (i + 1), RoomType.STORE));
//			System.out.println("Partition " + HSMDataGenConstant.mID_Par + " created " +
//					twoEndPointToString(IndoorSpace.iPartitions.get(IndoorSpace.iPartitions.size() - 1)));
		}

//		System.out.println("part 5");
		IndoorSpace.iPartitions.add(new Partition(576.0, 648.0, 432.0, 504.0, RoomType.STORE));
//		System.out.println("Partition " + HSMDataGenConstant.mID_Par + " created " +
//				twoEndPointToString(IndoorSpace.iPartitions.get(IndoorSpace.iPartitions.size() - 1)));

		IndoorSpace.iPartitions.add(new Partition(432.0, 504.0, 576.0, 648.0, RoomType.STORE));
//		System.out.println("Partition " + HSMDataGenConstant.mID_Par + " created " +
//				twoEndPointToString(IndoorSpace.iPartitions.get(IndoorSpace.iPartitions.size() - 1)));

		for (int i = 0; i < 2; i++) {
			IndoorSpace.iPartitions.add(new Partition(576.0 + 36 * i, 576.0 + 36 * (i + 1), 522.0, 576.0, RoomType.STORE));
//			System.out.println("Partition " + HSMDataGenConstant.mID_Par + " created " +
//					twoEndPointToString(IndoorSpace.iPartitions.get(IndoorSpace.iPartitions.size() - 1)));
		}

		for (int i = 0; i < 2; i++) {
			IndoorSpace.iPartitions.add(new Partition(522.0, 576.0, 576.0 + 36 * i, 576.0 + 36 * (i + 1), RoomType.STORE));
//			System.out.println("Partition " + HSMDataGenConstant.mID_Par + " created " +
//					twoEndPointToString(IndoorSpace.iPartitions.get(IndoorSpace.iPartitions.size() - 1)));
		}

		// hallways
		System.out.println("==> Hallways");

		if (divideType == 1 || divideType == 2) {
			IndoorSpace.iPartitions.add(new Partition(288.0, 360.0, 288.0, 360.0, RoomType.HALLWAY));
			//		System.out.println("Partition " + HSMDataGenConstant.mID_Par + " created " +
			//				twoEndPointToString(IndoorSpace.iPartitions.get(IndoorSpace.iPartitions.size() - 1)));

			//		System.out.println("part 1");
			if (divideType == 1) {
				for (int i = 0; i < 2; i++) {
					IndoorSpace.iPartitions.add(new Partition(360.0 + 144 * i, 360.0 + 144 * (i + 1), 288, 360.0, RoomType.HALLWAY));
					//			System.out.println("Partition " + HSMDataGenConstant.mID_Par + " created " +
					//					twoEndPointToString(IndoorSpace.iPartitions.get(IndoorSpace.iPartitions.size() - 1)));
				}


				//		System.out.println("part 2");
				for (int i = 0; i < 2; i++) {
					IndoorSpace.iPartitions.add(new Partition(288.0, 360.0, 360 + 144 * i, 360.0 + 144 * (i + 1), RoomType.HALLWAY));
					//			System.out.println("Partition " + HSMDataGenConstant.mID_Par + " created " +
					//					twoEndPointToString(IndoorSpace.iPartitions.get(IndoorSpace.iPartitions.size() - 1)));
				}
			}

			if (divideType == 2) {
				for (int i = 0; i < 4; i++) {
					IndoorSpace.iPartitions.add(new Partition(360.0 + 72 * i, 360.0 + 72 * (i + 1), 288, 360.0, RoomType.HALLWAY));
					//			System.out.println("Partition " + HSMDataGenConstant.mID_Par + " created " +
					//					twoEndPointToString(IndoorSpace.iPartitions.get(IndoorSpace.iPartitions.size() - 1)));
				}


				//		System.out.println("part 2");
				for (int i = 0; i < 4; i++) {
					IndoorSpace.iPartitions.add(new Partition(288.0, 360.0, 360 + 72 * i, 360.0 + 72 * (i + 1), RoomType.HALLWAY));
					//			System.out.println("Partition " + HSMDataGenConstant.mID_Par + " created " +
					//					twoEndPointToString(IndoorSpace.iPartitions.get(IndoorSpace.iPartitions.size() - 1)));
				}
			}

			//		System.out.println("part 3");
			IndoorSpace.iPartitions.add(new Partition(432.0, 576.0, 432.0, 576.0, RoomType.HALLWAY));
			//		System.out.println("Partition " + HSMDataGenConstant.mID_Par + " created " +
			//				twoEndPointToString(IndoorSpace.iPartitions.get(IndoorSpace.iPartitions.size() - 1)));

			IndoorSpace.iPartitions.add(new Partition(576.0, 648.0, 504.0, 522.0, RoomType.HALLWAY));
			//		System.out.println("Partition " + HSMDataGenConstant.mID_Par + " created " +
			//				twoEndPointToString(IndoorSpace.iPartitions.get(IndoorSpace.iPartitions.size() - 1)));

			IndoorSpace.iPartitions.add(new Partition(504.0, 522.0, 576.0, 648.0, RoomType.HALLWAY));
			//			System.out.println("Partition " + HSMDataGenConstant.mID_Par + " created " +
			//				twoEndPointToString(IndoorSpace.iPartitions.get(IndoorSpace.iPartitions.size() - 1)));
		}
//
	}

	/**
	 * generates the doors as entrances for each partition
	 *
	 */
	public void initDoorsforDivide(int divisionType) {
		System.out.println("==> doors");

		if (divisionType == 0) {

			Iterator<Partition> itr = IndoorSpace.iPartitions.iterator();

			while (itr.hasNext()) {
				Partition curPar = itr.next();
				Door aDoor = null;

				int partitionNo = curPar.getmID() + 1;

				// on the upper part
				if ((partitionNo == 7) || (partitionNo >= 12 && partitionNo <= 18) || (partitionNo == 20)
						|| (partitionNo >= 23 && partitionNo <= 24)) {
					//				System.out.print("upper part ");

					aDoor = new Door((curPar.getX1() + curPar.getX2()) / 2, curPar.getY1());

					if (IndoorSpace.iDoors.contains(aDoor)) ;
					IndoorSpace.iDoors.add(aDoor);
					System.out.println("partition " + partitionNo + " has door " + (IndoorSpace.iDoors.size()) + " at ("
							+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getX() + ", "
							+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getY() + ")");
				}

				// on the right part
				if ((partitionNo >= 7 && partitionNo <= 17) || (partitionNo == 19) || (partitionNo == 22)) {
					//				System.out.print("right part ");

					aDoor = new Door(curPar.getX2(), (curPar.getY1() + curPar.getY2()) / 2);

					if (IndoorSpace.iDoors.contains(aDoor)) ;
					IndoorSpace.iDoors.add(aDoor);
					System.out.println("partition " + partitionNo + " has door " + (IndoorSpace.iDoors.size()) + " at ("
							+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getX() + ", "
							+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getY() + ")");
				}

				// on the bottom part
				if ((partitionNo >= 2 && partitionNo <= 6) || (partitionNo >= 13 && partitionNo <= 14)
						|| (partitionNo == 18) || (partitionNo == 20) || (partitionNo == 24)) {
					//				System.out.print("bottom part ");

					aDoor = new Door((curPar.getX1() + curPar.getX2()) / 2, curPar.getY2());

					if (IndoorSpace.iDoors.contains(aDoor)) ;
					IndoorSpace.iDoors.add(aDoor);
					System.out.println("partition " + partitionNo + " has door " + (IndoorSpace.iDoors.size()) + " at ("
							+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getX() + ", "
							+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getY() + ")");
				}

				// on the left part
				if ((partitionNo == 2) || (partitionNo == 12) || (partitionNo >= 16 && partitionNo <= 19)
						|| (partitionNo >= 21 && partitionNo <= 22)) {
					//				System.out.print("left part ");

					aDoor = new Door(curPar.getX1(), (curPar.getY1() + curPar.getY2()) / 2);

					if (IndoorSpace.iDoors.contains(aDoor)) ;
					IndoorSpace.iDoors.add(aDoor);
					System.out.println("partition " + partitionNo + " has door " + (IndoorSpace.iDoors.size()) + " at ("
							+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getX() + ", "
							+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getY() + ")");
				}
			}
		}

		if (divisionType == 2) {
			Iterator<Partition> itr = IndoorSpace.iPartitions.iterator();

			while (itr.hasNext()) {
				Partition curPar = itr.next();
				Door aDoor = null;

				int partitionNo = curPar.getmID() + 1;

				// on the upper part
				if ((partitionNo == 7) || (partitionNo >= 12 && partitionNo <= 18) || (partitionNo == 20)
						|| (partitionNo >= 23 && partitionNo <= 24) || (partitionNo == 36)) {
					//				System.out.print("upper part ");

					aDoor = new Door((curPar.getX1() + curPar.getX2()) / 2, curPar.getY1());

					if (IndoorSpace.iDoors.contains(aDoor)) ;
					IndoorSpace.iDoors.add(aDoor);
					System.out.println("partition " + partitionNo + " has door " + (IndoorSpace.iDoors.size()) + " at ("
							+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getX() + ", "
							+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getY() + ")");
				}

				// on the right part
				if ((partitionNo >= 7 && partitionNo <= 17) || (partitionNo == 19) || (partitionNo == 22)
						|| (partitionNo >= 25 && partitionNo <= 29) || (partitionNo == 35)) {
					//				System.out.print("right part ");

					aDoor = new Door(curPar.getX2(), (curPar.getY1() + curPar.getY2()) / 2);

					if (IndoorSpace.iDoors.contains(aDoor)) ;
					IndoorSpace.iDoors.add(aDoor);
					System.out.println("partition " + partitionNo + " has door " + (IndoorSpace.iDoors.size()) + " at ("
							+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getX() + ", "
							+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getY() + ")");
				}

				// on the bottom part
				if ((partitionNo >= 2 && partitionNo <= 6) || (partitionNo >= 13 && partitionNo <= 14)
						|| (partitionNo == 18) || (partitionNo == 20) ||
						(partitionNo >= 24 && partitionNo <= 25) || (partitionNo >= 30 && partitionNo <= 33)
						|| (partitionNo == 36)) {
					//				System.out.print("bottom part ");

					aDoor = new Door((curPar.getX1() + curPar.getX2()) / 2, curPar.getY2());

					if (IndoorSpace.iDoors.contains(aDoor)) ;
					IndoorSpace.iDoors.add(aDoor);
					System.out.println("partition " + partitionNo + " has door " + (IndoorSpace.iDoors.size()) + " at ("
							+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getX() + ", "
							+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getY() + ")");
				}

				// on the left part
				if ((partitionNo == 2) || (partitionNo == 12) || (partitionNo >= 16 && partitionNo <= 19)
						|| (partitionNo >= 21 && partitionNo <= 22) || (partitionNo == 35)) {
					//				System.out.print("left part ");

					aDoor = new Door(curPar.getX1(), (curPar.getY1() + curPar.getY2()) / 2);

					if (IndoorSpace.iDoors.contains(aDoor)) ;
					IndoorSpace.iDoors.add(aDoor);
					System.out.println("partition " + partitionNo + " has door " + (IndoorSpace.iDoors.size()) + " at ("
							+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getX() + ", "
							+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getY() + ")");
				}
			}
		}
	}

	/**
	 * generates the rest of the doors for divisionType 0 or 2
	 *
	 */
	private void genOtherDoorforDivide() {
		System.out.println("==> other doors");

		if (DataGenConstant.divisionType == 0) {
			Iterator<Partition> itr = IndoorSpace.iPartitions.iterator();

			while (itr.hasNext()) {
				Partition curPar = itr.next();
				Door aDoor = null;

				int partitionNo = curPar.getmID() + 1;

				// on the upper part
				if ((partitionNo >= 97 && partitionNo <= 99)) {
					//				System.out.print("upper part ");

					aDoor = new Door((curPar.getX1() + curPar.getX2()) / 2, curPar.getY1());

					if (IndoorSpace.iDoors.contains(aDoor)) ;
					IndoorSpace.iDoors.add(aDoor);
					//				System.out.println("partition " + partitionNo + " has door " + (IndoorSpace.iDoors.size()) + " at ("
					//						+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getX() + ", "
					//						+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getY() + ")");
				}

				// on the right part
				if ((partitionNo == 101)) {
					//				System.out.print("right part ");

					aDoor = new Door(curPar.getX2(), (curPar.getY1() + curPar.getY2()) / 2);

					if (IndoorSpace.iDoors.contains(aDoor)) ;
					IndoorSpace.iDoors.add(aDoor);
					//				System.out.println("partition " + partitionNo + " has door " + (IndoorSpace.iDoors.size()) + " at ("
					//						+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getX() + ", "
					//						+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getY() + ")");
				}

				// on the bottom part
				if ((partitionNo == 99)) {
					//				System.out.print("bottom part ");

					aDoor = new Door((curPar.getX1() + curPar.getX2()) / 2, curPar.getY2());

					if (IndoorSpace.iDoors.contains(aDoor)) ;
					IndoorSpace.iDoors.add(aDoor);
					//				System.out.println("partition " + partitionNo + " has door " + (IndoorSpace.iDoors.size()) + " at ("
					//						+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getX() + ", "
					//						+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getY() + ")");
				}

				// on the left part
				if ((partitionNo == 97) || (partitionNo >= 100 && partitionNo <= 101)) {
					//				System.out.print("left part ");

					aDoor = new Door(curPar.getX1(), (curPar.getY1() + curPar.getY2()) / 2);

					if (IndoorSpace.iDoors.contains(aDoor)) ;
					IndoorSpace.iDoors.add(aDoor);
					//				System.out.println("partition " + partitionNo + " has door " + (IndoorSpace.iDoors.size()) + " at ("
					//						+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getX() + ", "
					//						+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getY() + ")");
				}
			}
		}

		if (DataGenConstant.divisionType == 2) {
			Iterator<Partition> itr = IndoorSpace.iPartitions.iterator();

			while (itr.hasNext()) {
				Partition curPar = itr.next();
				Door aDoor = null;

				int partitionNo = curPar.getmID() + 1;

				// on the upper part
				if ((partitionNo >= 145 && partitionNo <= 149)
						|| (partitionNo >= 154 && partitionNo <= 155)) {
					//				System.out.print("upper part ");

					aDoor = new Door((curPar.getX1() + curPar.getX2()) / 2, curPar.getY1());

					if (IndoorSpace.iDoors.contains(aDoor)) ;
					IndoorSpace.iDoors.add(aDoor);
					//				System.out.println("partition " + partitionNo + " has door " + (IndoorSpace.iDoors.size()) + " at ("
					//						+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getX() + ", "
					//						+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getY() + ")");
				}

				// on the right part
				if ((partitionNo == 157)) {
					//				System.out.print("right part ");

					aDoor = new Door(curPar.getX2(), (curPar.getY1() + curPar.getY2()) / 2);

					if (IndoorSpace.iDoors.contains(aDoor)) ;
					IndoorSpace.iDoors.add(aDoor);
					//				System.out.println("partition " + partitionNo + " has door " + (IndoorSpace.iDoors.size()) + " at ("
					//						+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getX() + ", "
					//						+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getY() + ")");
				}

				// on the bottom part
				if ((partitionNo == 155)) {
					//				System.out.print("bottom part ");

					aDoor = new Door((curPar.getX1() + curPar.getX2()) / 2, curPar.getY2());

					if (IndoorSpace.iDoors.contains(aDoor)) ;
					IndoorSpace.iDoors.add(aDoor);
					//				System.out.println("partition " + partitionNo + " has door " + (IndoorSpace.iDoors.size()) + " at ("
					//						+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getX() + ", "
					//						+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getY() + ")");
				}

				// on the left part
				if ((partitionNo == 147) || (partitionNo >= 150 && partitionNo <= 153)
						|| (partitionNo >= 156 && partitionNo <= 157)) {
					//				System.out.print("left part ");

					aDoor = new Door(curPar.getX1(), (curPar.getY1() + curPar.getY2()) / 2);

					if (IndoorSpace.iDoors.contains(aDoor)) ;
					IndoorSpace.iDoors.add(aDoor);
					//				System.out.println("partition " + partitionNo + " has door " + (IndoorSpace.iDoors.size()) + " at ("
					//						+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getX() + ", "
					//						+ IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1).getY() + ")");
				}
			}
		}
	}


	/**
	 * reflects the existing partitions in one axis(parameter), pivot is as well
	 * given
	 *
	 * @param axis
	 * @param pivot
	 */
	private void parReflection(int axis, int pivot) {
		int curSize = IndoorSpace.iPartitions.size();

		int xa, xb, ya, yb, x0, y0, x1, x2, y1, y2, type;

		for (int i = 0; i < curSize; i++) {

			x1 = (int) IndoorSpace.iPartitions.get(i).getX1();
			x2 = (int) IndoorSpace.iPartitions.get(i).getX2();
			y1 = (int) IndoorSpace.iPartitions.get(i).getY1();
			y2 = (int) IndoorSpace.iPartitions.get(i).getY2();
			type = IndoorSpace.iPartitions.get(i).getmType();

			if (0 == axis) {
				x0 = pivot;
				xa = x0 + (x0 - x2);
				xb = x0 + (x0 - x1);
				ya = y1;
				yb = y2;
			} else if (1 == axis) {
				y0 = pivot;
				xa = x1;
				xb = x2;
				ya = y0 + (y0 - y1);
				yb = y0 + (y0 - y2);
			} else {
				return;
			}

			IndoorSpace.iPartitions.add(new Partition(Math.min(xa, xb), Math.max(xa, xb), Math.min(ya,
					yb), Math.max(ya, yb), type));

//			System.out.println(twoEndPointToString(IndoorSpace.iPartitions.get(i)) + " with type " + type + ""
//					+ " reflected to " + twoEndPointToString(IndoorSpace.iPartitions.get(IndoorSpace.iPartitions.size() - 1))
//					+ " partition " + IndoorSpace.iPartitions.size());
		}

	}

	private void doorReflection(int axis, int pivot) {
		int curDoorsSize = IndoorSpace.iDoors.size();
		for (int i = 0; i < curDoorsSize; i++) {
			Door curDoor = IndoorSpace.iDoors.get(i);
			Door newDoor = new Door(curDoor.getX(), curDoor.getY());
			newDoor.reflection(axis, pivot);
			IndoorSpace.iDoors.add(newDoor);

//			System.out.println(pointToString(IndoorSpace.iDoors.get(i)) + " door " + (i + 1)
//					+ " reflected to " + pointToString(IndoorSpace.iDoors.get(IndoorSpace.iDoors.size() - 1))
//					+ " door " + IndoorSpace.iDoors.size());
		}


	}

	private String twoEndPointToString(Partition p) {
		return "(" + p.getX1() + ", " + p.getY1() + "), (" + p.getX2() + ", " + p.getY2() + ")";
	}

	private String pointToString(Door d) {
		return "(" + d.getX() + ", " + d.getY() + ")";
	}

	/**
	 * saves the generated doors and partitions
	 *
	 * @return boolean value if writing files is accomplished successfully.
	 * @exception IOException
	 */
	private boolean saveDP() {
		try {
			FileWriter fwPar = new FileWriter(outputPath + "/Par.txt");
			Iterator<Partition> itrPar = IndoorSpace.iPartitions.iterator();
			while (itrPar.hasNext()) {
				fwPar.write(itrPar.next().toString() + "\n");
			}
			fwPar.flush();
			fwPar.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		try {
			FileWriter fwDoor = new FileWriter(outputPath + "/Door.txt");
			Iterator<Door> itrDoor = IndoorSpace.iDoors.iterator();
			while (itrDoor.hasNext()) {
				fwDoor.write(itrDoor.next().toString() + "\n");
			}
			fwDoor.flush();
			fwDoor.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		try {
			FileWriter fwP2D = new FileWriter(outputPath + "/P2D.txt");
			Iterator<Partition> itrPar = IndoorSpace.iPartitions.iterator();
			while (itrPar.hasNext()) {
				fwP2D.write(itrPar.next().toString() + "\n");
			}
			fwP2D.flush();
			fwP2D.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		try {
			FileWriter fwD2P = new FileWriter(outputPath + "/D2P.txt");
			Iterator<Door> itrDoor = IndoorSpace.iDoors.iterator();
			while (itrDoor.hasNext()) {
				Door curDoor = itrDoor.next();
				fwD2P.write(curDoor.toString() + "\n");
			}
			fwD2P.flush();
			fwD2P.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		try {
			FileWriter fwD2D = new FileWriter(outputPath + "/D2D.txt");
			Iterator<Partition> itrPar = IndoorSpace.iPartitions.iterator();
			while (itrPar.hasNext()) {
				fwD2D.write(itrPar.next().d2DtoString() + "\n");
			}
			fwD2D.flush();
			fwD2D.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * draws one single floor
	 *
	 */
	public void drawFloor() {
		PaintingPanel pp = new PaintingPanel();
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(pp);
		frame.setBounds(0, 0, (int) ((DataGenConstant.floorRangeX + 3)
						* DataGenConstant.zoomLevel),
				(int) ((DataGenConstant.floorRangeY + 3)
						* DataGenConstant.zoomLevel));
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setResizable(true);
		frame.setVisible(true);
	}

	class PaintingPanel extends JPanel {

		private static final long serialVersionUID = 6050646791177968917L;

		/**
		 * paint function, paint the indoor space with a zoom-level parameter
		 * 1.paint the partitions(black-line & yellow BG color) 2.paint the
		 * doors(red) 3.paint the indoor objects(blue)
		 *
		 * @return
		 * @param
		 * @exception
		 */
		public void paint(Graphics g) {
			super.paint(g);
			paintPartitions(g);
			paintDoors(g);
//			paintIdrObjs(g);
		}

//		/**
//		 * paint indoor objects
//		 *
//		 * @return
//		 * @param g
//		 *            Graphics
//		 * @exception
//		 */
//		private void paintIdrObjs(Graphics g) {
//			// TODO Auto-generated method stub
//			g.setColor(Color.BLUE);
//			Iterator<IdrObj> itr3 = IndoorSpace.gIdrObjs.iterator();
//			while (itr3.hasNext()) {
//				IdrObj curIdrObj = itr3.next();
//				g.fillOval(
//						(int) (curIdrObj.getmTruePos().getX() * HSMDataGenConstant.zoomLevel),
//						(int) (curIdrObj.getmTruePos().getY() * HSMDataGenConstant.zoomLevel),
//						5, 5);
//			}
//		}

		/**
		 * paint doors
		 *
		 * @return
		 * @param g
		 *            Graphics
		 * @exception
		 */
		private void paintDoors(Graphics g) {
			// TODO Auto-generated method stub
			g.setColor(Color.RED);
			Iterator<Door> itr2 = IndoorSpace.iDoors.iterator();
			while (itr2.hasNext()) {
				Door curDoor = itr2.next();
				if (curDoor.getmID() >= IndoorSpace.iNumberDoorsPerFloor) break;
				g.fillOval((int) (curDoor.getX() * DataGenConstant.zoomLevel),
						(int) (curDoor.getY() * DataGenConstant.zoomLevel), 6,
						6);
//				g.drawString(String.valueOf(curDoor.getmID()),
//						(int) (curDoor.getX() * HSMDataGenConstant.zoomLevel),
//						(int) (curDoor.getY() * HSMDataGenConstant.zoomLevel));
			}
		}

		/**
		 * paint partitions
		 *
		 * @return
		 * @param g
		 *            Graphics
		 * @exception
		 */
		private void paintPartitions(Graphics g) {
			// TODO Auto-generated method stub

//			// four outdoor rectangles (is used in the is inBuilding function)
//			g.setColor(Color.WHITE);
//			g.fillRect(((int) HSMDataGenConstant.outRectTop.getX1())
//							* HSMDataGenConstant.zoomLevel,
//					((int) HSMDataGenConstant.outRectTop.getY1())
//							* HSMDataGenConstant.zoomLevel,
//					(int) HSMDataGenConstant.outRectTop.getWidth()
//							* HSMDataGenConstant.zoomLevel,
//					(int) HSMDataGenConstant.outRectTop.getHeight()
//							* HSMDataGenConstant.zoomLevel);
//			g.fillRect(((int) HSMDataGenConstant.outRectBottom.getX1())
//							* HSMDataGenConstant.zoomLevel,
//					((int) HSMDataGenConstant.outRectBottom.getY1())
//							* HSMDataGenConstant.zoomLevel,
//					(int) HSMDataGenConstant.outRectBottom.getWidth()
//							* HSMDataGenConstant.zoomLevel,
//					(int) HSMDataGenConstant.outRectBottom.getHeight()
//							* HSMDataGenConstant.zoomLevel);
//			g.fillRect(((int) HSMDataGenConstant.outRectLeft.getX1())
//							* HSMDataGenConstant.zoomLevel,
//					((int) HSMDataGenConstant.outRectLeft.getY1())
//							* HSMDataGenConstant.zoomLevel,
//					(int) HSMDataGenConstant.outRectLeft.getWidth()
//							* HSMDataGenConstant.zoomLevel,
//					(int) HSMDataGenConstant.outRectLeft.getHeight()
//							* HSMDataGenConstant.zoomLevel);
//			g.fillRect(((int) HSMDataGenConstant.outRectRight.getX1())
//							* HSMDataGenConstant.zoomLevel,
//					((int) HSMDataGenConstant.outRectRight.getY1())
//							* HSMDataGenConstant.zoomLevel,
//					(int) HSMDataGenConstant.outRectRight.getWidth()
//							* HSMDataGenConstant.zoomLevel,
//					(int) HSMDataGenConstant.outRectRight.getHeight()
//							* HSMDataGenConstant.zoomLevel);

			// all indoor partitions
			g.setColor(Color.BLACK);
			Iterator<Partition> itr = IndoorSpace.iPartitions.iterator();
			while (itr.hasNext()) {
				Partition curPartition = itr.next();
				if (curPartition.getmFloor() != 0) break;
				if (curPartition.getmType() != RoomType.HALLWAY) continue;
				if (curPartition.getmType() == RoomType.STORE) {
					Color c = new Color(218, 227, 243);
					g.setColor(c);
				}
				if (curPartition.getmType() == RoomType.HALLWAY) {
					g.setColor(Color.WHITE);
				}
				if (curPartition.getmType() == RoomType.STAIRCASE) {
					Color c = new Color(226, 240, 217);
					g.setColor(c);
				}
				g.fillRect(
						(int) (curPartition.getX1() * DataGenConstant.zoomLevel),
						(int) (curPartition.getY1() * DataGenConstant.zoomLevel),
						(int) ((curPartition.getX2() - curPartition.getX1())
								* DataGenConstant.zoomLevel),
						(int) ((curPartition.getY2() - curPartition.getY1())
								* DataGenConstant.zoomLevel));
				g.setColor(Color.BLACK);
				g.drawRect(
						(int) (curPartition.getX1() * DataGenConstant.zoomLevel),
						(int) (curPartition.getY1() * DataGenConstant.zoomLevel),
						(int) ((curPartition.getX2() - curPartition.getX1())
								* DataGenConstant.zoomLevel),
						(int) ((curPartition.getY2() - curPartition.getY1())
								* DataGenConstant.zoomLevel));
//				g.drawString(String.valueOf(curPartition.getmID()),
//						(int) ((curPartition.getX1() + curPartition.getX2()) / 2
//								* HSMDataGenConstant.zoomLevel),
//						(int) ((curPartition.getY1() + curPartition.getY2()) / 2
//								* HSMDataGenConstant.zoomLevel));
			}

			Iterator<Partition> itr1 = IndoorSpace.iPartitions.iterator();
			while (itr1.hasNext()) {
				Partition curPartition = itr1.next();
				if (curPartition.getmFloor() != 0) break;
				if (curPartition.getmType() == RoomType.HALLWAY) continue;
				if (curPartition.getmType() == RoomType.STORE) {
					Color c = new Color(218, 227, 243);
					g.setColor(c);
				}
				if (curPartition.getmType() == RoomType.HALLWAY) {
					g.setColor(Color.WHITE);
				}
				if (curPartition.getmType() == RoomType.STAIRCASE) {
					Color c = new Color(226, 240, 217);
					g.setColor(c);
				}
				g.fillRect(
						(int) (curPartition.getX1() * DataGenConstant.zoomLevel),
						(int) (curPartition.getY1() * DataGenConstant.zoomLevel),
						(int) ((curPartition.getX2() - curPartition.getX1())
								* DataGenConstant.zoomLevel),
						(int) ((curPartition.getY2() - curPartition.getY1())
								* DataGenConstant.zoomLevel));
				g.setColor(Color.BLACK);
				g.drawRect(
						(int) (curPartition.getX1() * DataGenConstant.zoomLevel),
						(int) (curPartition.getY1() * DataGenConstant.zoomLevel),
						(int) ((curPartition.getX2() - curPartition.getX1())
								* DataGenConstant.zoomLevel),
						(int) ((curPartition.getY2() - curPartition.getY1())
								* DataGenConstant.zoomLevel));
//				g.drawString(String.valueOf(curPartition.getmID()),
//						(int) ((curPartition.getX1() + curPartition.getX2()) / 2
//								* HSMDataGenConstant.zoomLevel),
//						(int) ((curPartition.getY1() + curPartition.getY2()) / 2
//								* HSMDataGenConstant.zoomLevel));
			}
		}
	}

	public static void main(String args[]) {
		DataGen dataGen = new DataGen();
		dataGen.genAllData(DataGenConstant.dataType, DataGenConstant.divisionType);

//		for(int i = 0; i < IndoorSpace.iDoors.size(); i ++) {
//			Door door = IndoorSpace.iDoors.get(i);
//			System.out.println("door " + door.getmID() + " floor " + door.getmType());
//		}

		dataGen.drawFloor();
	}

}
