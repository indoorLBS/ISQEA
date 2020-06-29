package dk.aau.cs.indoorqueries.common.iPTree;

import dk.aau.cs.indoorqueries.common.indoorEntitity.Door;
import dk.aau.cs.indoorqueries.common.indoorEntitity.IndoorSpace;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Point;
import dk.aau.cs.indoorqueries.common.utilities.Constant;
import dk.aau.cs.indoorqueries.common.utilities.DataGenConstant;
import dk.aau.cs.indoorqueries.common.utilities.RoomType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class NonLeafNode implements Node {
	public ArrayList<Integer> mChildren = new ArrayList<Integer>();

	private boolean hasCrucial = false;

	private double x1 = Constant.large;
	private double y1 = Constant.large;
	private double z1 = Constant.large;
	private double x2 = -Constant.large;
	private double y2 = -Constant.large;
	private double z2 = -Constant.large;

	private int nodeID;
	private int degree;
	private int height;
	private int parentNodeID;
	private String type;

	private ArrayList<Integer> adjacentNodes = new ArrayList<Integer>(); // set of adjacent nodes

	private ArrayList<Integer> mPartitions = new ArrayList<>(); // partitions stored in the node
	private ArrayList<Integer> accessDoors = new ArrayList<Integer>(); // access doors to other brother nodes
	private ArrayList<Integer> internalDoors = new ArrayList<Integer>(); // interal doors to partitions in the same node
	private HashMap<String, String> disMatrix = new HashMap<>();
	private ArrayList<Integer> objects = new ArrayList<>(); // objects in this node

//	private DistanceCube distanceCube;

	public NonLeafNode() {
		this.nodeID = DataGenConstant.totalNodeSize++;
		this.type = "NonLeafNode";
	}

	public NonLeafNode(NonLeafNode another) {
		this.nodeID = another.getNodeID();
		this.degree = another.getDegree();
		this.parentNodeID = another.getParentNodeID();
		this.type = another.getType();
		this.adjacentNodes = (ArrayList<Integer>) another.getAdjacentNodes().clone();
		this.mPartitions = (ArrayList<Integer>) another.getmPartitions().clone();
		this.accessDoors = (ArrayList<Integer>) another.getAccessDoors().clone();
		this.internalDoors = (ArrayList<Integer>) another.getInternalDoors().clone();
		this.hasCrucial = another.hasCrucial();
		this.mChildren = (ArrayList<Integer>) another.getmChildren().clone();
	}

	public void addChildren(int nodeID) {
		if (!mChildren.contains(nodeID)) {
			mChildren.add(nodeID);

			// update boundary
			updateBoundary(nodeID);
		}
	}

	public int getChildrenSize() {
		return mChildren.size();
	}

	public ArrayList<Integer> getmChildren() {
		return this.mChildren;
	}

	public void emptymChildren() {
		this.mChildren = new ArrayList<Integer>();
	}

	/**
	 * @param mID
	 */
	private void updateBoundary(int mID) {
		Node node = VIPTree.getNode(mID);

		if (node != null) {
			if (node.getX1() < x1)
				x1 = node.getX1();
			if (node.getY1() < y1)
				y1 = node.getY1();
			if (node.getZ1() < z1)
				z1 = node.getZ1();
			if (node.getX2() > x2)
				x2 = node.getX2();
			if (node.getY2() > y2)
				y2 = node.getY2();
			if (node.getZ2() > z2)
				z2 = node.getZ2();
		} else
			System.out.println("something wrong_NonLeafNode_updateBoundary");

	}

	// merge node_1 to node_2
	public void mergeNodes(Node node_1, Node node_2, HashMap<Integer, ArrayList<Integer>> parNodeId, int k,
						   ArrayList<String> crossFloorDoors) {
//		System.out.println("1merger " + node_1);
//		System.out.println("and " + node_2);

		this.setParentNodeID(node_1.getParentNodeID());
		this.addChildren(node_1.getNodeID());
		this.addChildren(node_2.getNodeID());
		this.setDegree(node_1.getDegree() + node_2.getDegree());
		if (node_1.hasCrucial() || node_2.hasCrucial())
			this.setHasCrucial();

		ArrayList<Integer> accessDoors = new ArrayList<Integer>();
		ArrayList<Integer> partitions = new ArrayList<Integer>();
		ArrayList<Integer> internalDoors = new ArrayList<Integer>();

		ArrayList<Integer> accessDoors_1 = new ArrayList<Integer>();
		ArrayList<Integer> accessDoors_2 = new ArrayList<Integer>();
		ArrayList<Integer> canAccessDoors = new ArrayList<Integer>();

		ArrayList<Integer> partitions_1 = new ArrayList<Integer>();
		ArrayList<Integer> partitions_2 = new ArrayList<Integer>();

		accessDoors_1 = (ArrayList<Integer>) node_1.getAccessDoors().clone();
		accessDoors_2 = (ArrayList<Integer>) node_2.getAccessDoors().clone();

		partitions_1 = (ArrayList<Integer>) node_1.getmPartitions().clone();
		partitions_2 = (ArrayList<Integer>) node_2.getmPartitions().clone();

		canAccessDoors.addAll((Collection<? extends Integer>) accessDoors_1.clone());
		for (int i = 0; i < accessDoors_2.size(); i++) {
			if (!canAccessDoors.contains(accessDoors_2.get(i)))
				canAccessDoors.add(accessDoors_2.get(i));
		}

//		System.out.println(accessDoors_1 + " and " + accessDoors_2 + " to " + canAccessDoors);

		for (int i = 0; i < partitions_1.size(); i++) {
			if (!partitions.contains(partitions_1.get(i))) {
				partitions.add(partitions_1.get(i));

				if (parNodeId.get(partitions_1.get(i)) == null) {
					System.out.println("something wrong_NonLeafNode_mergeNodes_not exist parNodeId");
				} else {
					ArrayList<Integer> arr = parNodeId.get(partitions_1.get(i));
					if (!arr.contains(this.getNodeID())) {
						if (arr.size() >= k)
							arr.add(k, this.getNodeID());
						else
							arr.add(this.getNodeID());
					}
					parNodeId.put(partitions_1.get(i), arr);
				}
			}
		}

		for (int i = 0; i < partitions_2.size(); i++) {
			if (!partitions.contains(partitions_2.get(i))) {
				partitions.add(partitions_2.get(i));

				if (parNodeId.get(partitions_2.get(i)) == null) {
					System.out.println("something wrong_NonLeafNode_mergeNodes_not exist parNodeId");
				} else {
					ArrayList<Integer> arr = parNodeId.get(partitions_2.get(i));
					if (!arr.contains(this.getNodeID())) {
						if (arr.size() >= k)
							arr.add(k, this.getNodeID());
						else
							arr.add(this.getNodeID());
					}
					parNodeId.put(partitions_2.get(i), arr);
				}
			}
		}

//		System.out.println(partitions_1 + " and " + partitions_2 + " to " + partitions);

		// internalDoors.addAll((Collection<? extends Integer>)
		// internalDoors_1.clone());
		// for (int i = 0; i < internalDoors_2.size(); i++) {
		// if (!internalDoors.contains(internalDoors_2.get(i)))
		// internalDoors.add(internalDoors_2.get(i));
		// }

		if (crossFloorDoors.size() > 0) {
			for (String str : crossFloorDoors) {
				String[] temp = str.split("-");
				int door_1 = Integer.parseInt(temp[1]);
				int door_2 = Integer.parseInt(temp[3]);
				if (!internalDoors.contains(door_1))
					internalDoors.add(door_1);
				if (!internalDoors.contains(door_2))
					internalDoors.add(door_2);
			}

			for (int door : canAccessDoors) {
				if (!internalDoors.contains(door)) {
					if (!accessDoors.contains(door))
						accessDoors.add(door);
				}
			}

//			System.out.println("accessDoors: " + accessDoors + " internalDoors: " + internalDoors);

		} else {
			// for each candidate access door, check whether it does
			for (int j = 0; j < canAccessDoors.size(); j++) {
				Door door = IndoorSpace.iDoors.get(canAccessDoors.get(j));

				if (isAccessDoor(door, partitions)) {
					if (!accessDoors.contains(door.getmID()))
						accessDoors.add(door.getmID());
				} else {
					if (!internalDoors.contains(door.getmID()))
						internalDoors.add(door.getmID());
				}
			}
		}

//		System.out.println(canAccessDoors + " accessDoors: " + accessDoors + " internalDoors: " + internalDoors);

		this.setAccessDoors(accessDoors);
		this.setPartitions(partitions);
		this.setInternalDoors(internalDoors);

//		System.out.println("after: to" + this);
//		System.out.println("merger " + node_1);
//		System.out.println("and " + node_2);

		// // check partition lack
		// for (int i = 0; i < node_1.getmPartitions().size(); i++) {
		// if (!partitions.contains(node_1.getmPartitions().get(i)))
		// System.out.println("lack v" + node_1.getmPartitions().get(i));
		// }
		//
		// for (int i = 0; i < node_2.getmPartitions().size(); i++) {
		// if (!partitions.contains(node_2.getmPartitions().get(i)))
		// System.out.println("lack v" + node_2.getmPartitions().get(i));
		// }
		//
		// ArrayList<Integer> doors = new ArrayList<Integer>();
		// for (int i = 0; i < accessDoors.size(); i++) {
		// doors.add(accessDoors.get(i));
		// }
		// for (int i = 0; i < internalDoors.size(); i++) {
		// doors.add(internalDoors.get(i));
		// }
		//
		// // check door lack
		// for (int i = 0; i < node_1.getAccessDoors().size(); i++) {
		// if (!doors.contains(node_1.getAccessDoors().get(i)))
		// System.out.println("lack d" + node_1.getAccessDoors().get(i));
		// }
		//
		// for (int i = 0; i < node_1.getInternalDoors().size(); i++) {
		// if (!doors.contains(node_1.getInternalDoors().get(i)))
		// System.out.println("lack d" + node_1.getInternalDoors().get(i));
		// }
		//
		// for (int i = 0; i < node_2.getAccessDoors().size(); i++) {
		// if (!doors.contains(node_2.getAccessDoors().get(i)))
		// System.out.println("lack d" + node_2.getAccessDoors().get(i));
		// }
		//
		// for (int i = 0; i < node_2.getInternalDoors().size(); i++) {
		// if (!doors.contains(node_2.getInternalDoors().get(i)))
		// System.out.println("lack d" + node_2.getInternalDoors().get(i));
		// }
//		System.out.println();
	}

	// merge node_1 to this node
	public void mergeNodes(Node node_1, HashMap<Integer, ArrayList<Integer>> parNodeId, int CASE, int k,
						   ArrayList<String> crossFloorDoors) {
//		System.out.println("2merger " + node_1);
//		System.out.println("to " + this);

		if (CASE == 1) { // merge N1 to this node
			this.addChildren(node_1.getNodeID());

		} else if (CASE == 2) { // merge N1's child node to this Node
			ArrayList<Integer> children = new ArrayList<Integer>();
			children = ((NonLeafNode) node_1).getmChildren();

			for (int i = 0; i < children.size(); i++) {
				this.addChildren(children.get(i));
				;
			}
		}

		this.setDegree(node_1.getDegree() + this.getDegree());
		if (node_1.hasCrucial() || this.hasCrucial())
			this.setHasCrucial();

		ArrayList<Integer> accessDoors = new ArrayList<Integer>();
		ArrayList<Integer> partitions = new ArrayList<Integer>();
		ArrayList<Integer> internalDoors = new ArrayList<Integer>();

		ArrayList<Integer> accessDoors_1 = new ArrayList<Integer>();
		ArrayList<Integer> accessDoors_2 = new ArrayList<Integer>();
		ArrayList<Integer> canAccessDoors = new ArrayList<Integer>();

		ArrayList<Integer> partitions_1 = new ArrayList<Integer>();
		ArrayList<Integer> partitions_2 = new ArrayList<Integer>();

		accessDoors_1 = (ArrayList<Integer>) node_1.getAccessDoors().clone();
		accessDoors_2 = (ArrayList<Integer>) this.getAccessDoors().clone();

		partitions_1 = (ArrayList<Integer>) node_1.getmPartitions().clone();
		partitions_2 = (ArrayList<Integer>) this.getmPartitions().clone();

		canAccessDoors.addAll((Collection<? extends Integer>) accessDoors_1.clone());
		for (int i = 0; i < accessDoors_2.size(); i++) {
			if (!canAccessDoors.contains(accessDoors_2.get(i)))
				canAccessDoors.add(accessDoors_2.get(i));
		}

//		System.out.println(accessDoors_1 + " and " + accessDoors_2 + " to " + canAccessDoors);

		for (int i = 0; i < partitions_1.size(); i++) {
			if (!partitions.contains(partitions_1.get(i))) {
				partitions.add(partitions_1.get(i));

				if (parNodeId.get(partitions_1.get(i)) == null) {
					System.out.println("something wrong_NonLeafNode_mergeNodes_not exist parNodeId");
				} else {
					ArrayList<Integer> arr = parNodeId.get(partitions_1.get(i));
					if (!arr.contains(this.getNodeID())) {
						if (arr.size() >= k)
							arr.add(k, this.getNodeID());
						else
							arr.add(this.getNodeID());
					}
					parNodeId.put(partitions_1.get(i), arr);
				}
			}
		}

		for (int i = 0; i < partitions_2.size(); i++) {
			if (!partitions.contains(partitions_2.get(i))) {
				partitions.add(partitions_2.get(i));

				if (parNodeId.get(partitions_2.get(i)) == null) {
					System.out.println("something wrong_NonLeafNode_mergeNodes_not exist parNodeId");
				} else {
					ArrayList<Integer> arr = parNodeId.get(partitions_2.get(i));
					if (!arr.contains(this.getNodeID()))
						arr.add(k, this.getNodeID());
					parNodeId.put(partitions_2.get(i), arr);
				}
			}
		}

//		System.out.println(partitions_1 + " and " + partitions_2 + " to " + partitions);

		// internalDoors.addAll((Collection<? extends Integer>)
		// internalDoors_1.clone());
		// for (int i = 0; i < internalDoors_2.size(); i++) {
		// if (!internalDoors.contains(internalDoors_2.get(i)))
		// internalDoors.add(internalDoors_2.get(i));
		// }

		if (crossFloorDoors.size() > 0) {
			for (String str : crossFloorDoors) {
				String[] temp = str.split("-");
				int door_1 = Integer.parseInt(temp[1]);
				int door_2 = Integer.parseInt(temp[3]);
				if (!internalDoors.contains(door_1))
					internalDoors.add(door_1);
				if (!internalDoors.contains(door_2))
					internalDoors.add(door_2);
			}

			for (int door : canAccessDoors) {
				if (!internalDoors.contains(door)) {
					if (!accessDoors.contains(door))
						accessDoors.add(door);
				}
			}

//			System.out.println("accessDoors: " + accessDoors + " internalDoors: " + internalDoors);

		} else {
			// for each candidate access door, check whether it does
			for (int j = 0; j < canAccessDoors.size(); j++) {
				Door door = IndoorSpace.iDoors.get(canAccessDoors.get(j));

				if (isAccessDoor(door, partitions)) {
					if (!accessDoors.contains(door.getmID()))
						accessDoors.add(door.getmID());
				} else {
					if (!internalDoors.contains(door.getmID()))
						internalDoors.add(door.getmID());
				}
			}
		}

//		System.out.println(canAccessDoors + " accessDoors: " + accessDoors + " internalDoors: " + internalDoors);

		this.setAccessDoors(accessDoors);
		this.setPartitions(partitions);
		this.setInternalDoors(internalDoors);

//		System.out.println("after: to" + this);
//		System.out.println("merger " + node_1);

		// // check partition lack
		// for (int i = 0; i < node_1.getmPartitions().size(); i++) {
		// if (!partitions.contains(node_1.getmPartitions().get(i)))
		// System.out.println("lack v" + node_1.getmPartitions().get(i));
		// }
		//
		// for (int i = 0; i < this.getmPartitions().size(); i++) {
		// if (!partitions.contains(this.getmPartitions().get(i)))
		// System.out.println("lack v" + this.getmPartitions().get(i));
		// }
		//
		// ArrayList<Integer> doors = new ArrayList<Integer>();
		// for (int i = 0; i < accessDoors.size(); i++) {
		// doors.add(accessDoors.get(i));
		// }
		// for (int i = 0; i < internalDoors.size(); i++) {
		// doors.add(internalDoors.get(i));
		// }
		//
		// // check door lack
		// for (int i = 0; i < node_1.getAccessDoors().size(); i++) {
		// if (!doors.contains(node_1.getAccessDoors().get(i)))
		// System.out.println("lack d" + node_1.getAccessDoors().get(i));
		// }
		//
		// for (int i = 0; i < node_1.getInternalDoors().size(); i++) {
		// if (!doors.contains(node_1.getInternalDoors().get(i)))
		// System.out.println("lack d" + node_1.getInternalDoors().get(i));
		// }
		//
		// for (int i = 0; i < this.getAccessDoors().size(); i++) {
		// if (!doors.contains(this.getAccessDoors().get(i)))
		// System.out.println("lack d" + this.getAccessDoors().get(i));
		// }
		//
		// for (int i = 0; i < this.getInternalDoors().size(); i++) {
		// if (!doors.contains(this.getInternalDoors().get(i)))
		// System.out.println("lack d" + this.getInternalDoors().get(i));
		// }
//		System.out.println();
	}

	/**
	 * return whether a door is an access door
	 */
	private boolean isAccessDoor(Door door, ArrayList<Integer> partitions) {
		ArrayList<Integer> parts = new ArrayList<Integer>();
		parts = door.getmPartitions();
		boolean notContains = false;

		for (int i = 0; i < parts.size(); i++) {
			if (partitions.contains(parts.get(i)))
				continue;
			else {
				notContains = true;
				break;
			}
		}

		return notContains;
	}

	public int getNodeID() {
		return this.nodeID;
	}

	public void setNodeID(int ID) {
		this.nodeID = ID;
	}

	public void setDegree(int degree) {
		this.degree = degree;
	}

	public int getDegree() {
		return degree;
	}

	public void setParentNodeID(int parentNodeID) {
		this.parentNodeID = parentNodeID;
	}

	public int getParentNodeID() {
		return this.parentNodeID;
	}

	public void addAdjacentNode(int nodeID) {
		if (!adjacentNodes.contains(nodeID)) {
			adjacentNodes.add(nodeID);
		}
	}

	public int getAdjacentNodeNo() {
		return adjacentNodes.size();
	}

	public ArrayList<Integer> getAdjacentNodes() {
		return this.adjacentNodes;
	}

	public void setPartitions(ArrayList<Integer> partitions) {
		this.mPartitions = partitions;

		// check crucial
		for (int i = 0; i < partitions.size(); i++) {
			if (IndoorSpace.iPartitions.get(partitions.get(i)).gettType() == RoomType.CRUCIALPASS) {
				hasCrucial = true;
				break;
			}
		}
	}

	public void addPartition(int mID) {
		if (!mPartitions.contains(mID)) {
			mPartitions.add(mID);

			// check crucial
			if (IndoorSpace.iPartitions.get(mID).gettType() == RoomType.CRUCIALPASS)
				hasCrucial = true;
		}
	}

	public ArrayList<Integer> getmPartitions() {
		return mPartitions;
	}

	public void addAccessDoor(int doorID) {
		if (!accessDoors.contains(doorID))
			this.accessDoors.add(doorID);
	}

	public void removeAccessDoor(int doorID) {
		int index = accessDoors.indexOf(doorID);
		accessDoors.remove(index);
	}

	public void setAccessDoors(ArrayList<Integer> accessDoors) {
		this.accessDoors = accessDoors;
	}

	public ArrayList<Integer> getAccessDoors() {
		return this.accessDoors;
	}

	public void addInternalDoor(int doorID) {
		if (!internalDoors.contains(doorID))
			this.internalDoors.add(doorID);
	}

	public void setInternalDoors(ArrayList<Integer> internalDoors) {
		this.internalDoors = internalDoors;
	}

	public ArrayList<Integer> getInternalDoors() {
		return this.internalDoors;
	}

	public boolean hasCrucial() {
		return hasCrucial;
	}

	public void setHasCrucial() {
		this.hasCrucial = true;
		;
	}

	public String getType() {
		return this.type;
	}

	public void setDisMatrix(String doorPair, String dis) {
		this.disMatrix.put(doorPair, dis);
	}

	public void setDistMatrix(HashMap<String, String> DM) {
		this.disMatrix = DM;
	}

	public String getDist(int d1, int d2) {
		String result = this.disMatrix.get(d1 + "-" + d2);
		if (result == null) {
			System.out.println("something wrong with node.getDist");
		}
		return result;
	}

//	public String convertPath(String distPath) {
//		String result = "";
//		String [] distPathArr = distPath.split("\t");
//		String [] resultArr = new String[distPathArr.length];
////		System.out.println(distPathArr.length);
//		for (int i = 1; i < distPathArr.length; i++) {
//			resultArr[distPathArr.length - i] = distPathArr[i];
//		}
//		resultArr[0] = distPathArr[0];
//		result = arrToString(resultArr, 0, resultArr.length - 1);
//		return result;
//	}
//
//	public String arrToString(String [] arr, int start, int end) {
//		String result = "";
//		for (int i = start; i <= end; i++) {
//			result += arr[i] + "\t";
//		}
////		System.out.println("arrToString: " + result);
//		return  result;
//	}

	public HashMap<String, String> getDistMatrix() {
		return this.disMatrix;
	}

	public int compareTo(Node another) {
		if (this.getDegree() > another.getDegree()) {
			return 1;
		} else if (this.getDegree() == another.getDegree()) {
			if (this.getAdjacentNodeNo() > another.getAdjacentNodeNo()) {
				return 1;
			} else if (this.getAdjacentNodeNo() == another.getAdjacentNodeNo()) {
				return 0;
			} else {
				return -1;
			}
		} else {
			return -1;
		}
	}

	// toString short
	public String toStringShort() {
		return "leaf_N_" + this.getNodeID() + " degree: " + this.getDegree() + " adjacentNodeNo: "
				+ this.getAdjacentNodeNo();
	}

	// toString
	public String toString() {
		return "nonLeaf_N_" + this.getNodeID() + " parent:" + this.getParentNodeID() + " accessDoors: "
				+ this.getAccessDoors() + " internalDoors: " + this.getInternalDoors() + " partitions: "
				+ this.getmPartitions() + "objects" + this.getAllObjects() + " adjacentNodes: " + this.getAdjacentNodes() + " degree: " + this.getDegree()
				+ " children size: " + this.getmChildren().size() + " children: " + this.getmChildren()
				+ " adjacentNodes size: " + this.getAdjacentNodeNo() + " boundary: " + this.getBoundary();
	}

	@Override
	public void setHeight(int height) {
		this.height = height;
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return this.height;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see dk.aau.cs.indoorqueries.iPTree.Node#getX1()
	 */
	@Override
	public double getX1() {
		// TODO Auto-generated method stub
		return this.x1;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see dk.aau.cs.indoorqueries.iPTree.Node#getY1()
	 */
	@Override
	public double getY1() {
		// TODO Auto-generated method stub
		return this.y1;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see dk.aau.cs.indoorqueries.iPTree.Node#getZ1()
	 */
	@Override
	public double getZ1() {
		// TODO Auto-generated method stub
		return this.z1;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see dk.aau.cs.indoorqueries.iPTree.Node#getX2()
	 */
	@Override
	public double getX2() {
		// TODO Auto-generated method stub
		return this.x2;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see dk.aau.cs.indoorqueries.iPTree.Node#getY2()
	 */
	@Override
	public double getY2() {
		// TODO Auto-generated method stub
		return this.y2;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see dk.aau.cs.indoorqueries.iPTree.Node#getZ2()
	 */
	@Override
	public double getZ2() {
		// TODO Auto-generated method stub
		return this.z2;
	}

	@Override
	public String getBoundary() {
		return "x1 = " + this.getX1() + ", y1 = " + this.getY1() + ", z1 = " + this.getZ1() + ", x2 = " + this.getX2()
				+ ", y2 = " + this.getY2() + " z2 = " + this.getZ2();
	}

	// check whether a point is in a node
	@Override
	public boolean isInNode(Point point) {
		double x = point.getX();
		double y = point.getY();
		double z = point.getmFloor();

		if (x1 <= x && x <= x2) {
			if (y1 <= y && y <= y2) {
				if (z1 <= z && z <= z2) {
					return true;
				} else
					return false;
			} else
				return false;
		} else
			return false;
	}

	public void addObject(int obId) {
		this.objects.add(obId);
	}

	public void addAllObjects(ArrayList<Integer> objects) {
		this.objects.addAll(objects);
	}

	public ArrayList<Integer> getAllObjects() {
		return this.objects;
	}

	public void setObjectDist(HashMap<Integer, ArrayList<ArrayList<Double>>> objectDist) {
		return;
//		this.objectDist = objectDist;
	}

//	public void constructDistCube(int level, boolean isRead) {
//		this.distanceCube = new DistanceCube(this, level, isRead);
//	}

//	public DistanceCube getDistanceCube() {
//		return this.distanceCube;
//	}
}
