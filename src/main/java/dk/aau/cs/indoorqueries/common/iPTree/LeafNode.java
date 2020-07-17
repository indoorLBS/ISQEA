package dk.aau.cs.indoorqueries.common.iPTree;

import dk.aau.cs.indoorqueries.common.indoorEntitity.IndoorSpace;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Partition;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Point;
import dk.aau.cs.indoorqueries.common.utilities.Constant;
import dk.aau.cs.indoorqueries.common.utilities.DataGenConstant;
import dk.aau.cs.indoorqueries.common.utilities.RoomType;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * leafNode of VIP/IP-Tree
 * @author ZijinFeng, Tiantian Liu
 */
public class LeafNode implements Node {

	private boolean hasCrucial = false;

	private double x1 = Constant.large;
	private double y1 = Constant.large;
	private double z1 = Constant.large;
	private double x2 = -Constant.large;
	private double y2 = -Constant.large;
	private double z2 = -Constant.large;

	private int nodeID;
	private int degree = 0;
	private int height;
	private int parentNodeID;
	private String type;

	private ArrayList<Integer> adjacentNodes = new ArrayList<Integer>(); // set of adjacent nodes

	private ArrayList<Integer> mPartitions = new ArrayList<>(); // partitions stored in the node
	private ArrayList<Integer> accessDoors = new ArrayList<Integer>(); // access doors to other brother nodes
	private ArrayList<Integer> internalDoors = new ArrayList<Integer>(); // interal doors to partitions in the same node
	private HashMap<String, String> disMatrix = new HashMap<>(); // distance matrix in this node
	private HashMap<String, String> disMatrixVIP = new HashMap<>(); // distance matrix (doors in this node and ancestors' access doors)
	private ArrayList<Integer> objects = new ArrayList<>(); // objects in this node
	private HashMap<Integer, ArrayList<ArrayList<Double>>> objectDist = new HashMap<>(); // sorted (from small to large) object with distance to access doors, where key is access door, value is {{objectID, distance}, ..., {}}

//	private DistanceCube distanceCube;

	// constructor
	public LeafNode() {
		this.nodeID = DataGenConstant.totalNodeSize++;
		this.type = "LeafNode";
		this.setDegree(1);
	}

	public LeafNode(LeafNode another) {
		this.nodeID = another.getNodeID();
		this.degree = another.getDegree();
		this.parentNodeID = another.getParentNodeID();
		this.type = another.getType();
		this.adjacentNodes = (ArrayList<Integer>) another.getAdjacentNodes().clone();
		this.mPartitions = (ArrayList<Integer>) another.getmPartitions().clone();
		this.accessDoors = (ArrayList<Integer>) another.getAccessDoors().clone();
		this.internalDoors = (ArrayList<Integer>) another.getInternalDoors().clone();
		this.hasCrucial = another.hasCrucial();
	}

	/**
	 * get node ID
	 * @return
	 */
	public int getNodeID() {
		return this.nodeID;
	}

	/**
	 * set node ID
	 * @param ID
	 */
	public void setNodeID(int ID) {
		this.nodeID = ID;
	}

	/**
	 * set degree
	 * @param degree
	 */
	public void setDegree(int degree) {
		this.degree = degree;
	}

	/**
	 * get degree
	 * @return
	 */
	public int getDegree() {
		return degree;
	}

	/**
	 * set parent node ID
	 * @param parentNodeID
	 */
	public void setParentNodeID(int parentNodeID) {
		this.parentNodeID = parentNodeID;
	}

	/**
	 * get parent node ID
	 * @return
	 */
	public int getParentNodeID() {
		return this.parentNodeID;
	}

	/**
	 * add adjacent node
	 * @param nodeID
	 */
	public void addAdjacentNode(int nodeID) {
		if (!adjacentNodes.contains(nodeID))
			adjacentNodes.add(nodeID);
	}

	/**
	 * get number of adjacent nodes
	 * @return
	 */
	public int getAdjacentNodeNo() {
		return adjacentNodes.size();
	}

	/**
	 * get adjacent nodes
	 * @return
	 */
	public ArrayList<Integer> getAdjacentNodes() {
		return this.adjacentNodes;
	}

	/**
	 * set partitions
	 * @param partitions
	 */
	public void setPartitions(ArrayList<Integer> partitions) {
		this.mPartitions = partitions;

		// check crucial
		for (int i = 0; i < partitions.size(); i++) {
			if (IndoorSpace.iPartitions.get(partitions.get(i)).gettType() == RoomType.CRUCIALPASS) {
				hasCrucial = true;
				break;
			}
		}

		// update boundary
		for (int i = 0; i < this.mPartitions.size(); i++) {
			updateBoundary(this.mPartitions.get(i));
		}
	}

	/**
	 * add partition to this node
	 * @param mID
	 */
	public void addPartition(int mID) {
		if (!mPartitions.contains(mID)) {
			mPartitions.add(mID);

			// check crucial
			if (IndoorSpace.iPartitions.get(mID).gettType() == RoomType.CRUCIALPASS)
				hasCrucial = true;

			// update boundary
			updateBoundary(mID);
		}
	}

	/**
	 * get partitions
	 * @return
	 */
	public ArrayList<Integer> getmPartitions() {
		return mPartitions;
	}

	/**
	 * update boundary
	 * @param mID
	 */
	private void updateBoundary(int mID) {
		Partition partition = IndoorSpace.iPartitions.get(mID);

		if (partition.getX1() < x1)
			x1 = partition.getX1();
		if (partition.getY1() < y1)
			y1 = partition.getY1();
		if (partition.getmFloor() < z1)
			z1 = partition.getmFloor();
		if (partition.getX2() > x2)
			x2 = partition.getX2();
		if (partition.getY2() > y2)
			y2 = partition.getY2();
		if (partition.getmFloor() > z2)
			z2 = partition.getmFloor();
	}

	/**
	 * add access door
	 * @param doorID
	 */
	public void addAccessDoor(int doorID) {
		if (!accessDoors.contains(doorID))
			this.accessDoors.add(doorID);
	}

	/**
	 * remove access door
	 * @param doorID
	 */
	public void removeAccessDoor(int doorID) {
		int index = accessDoors.indexOf(doorID);
		accessDoors.remove(index);
	}

	/**
	 * set access doors
	 * @param accessDoors
	 */
	public void setAccessDoors(ArrayList<Integer> accessDoors) {
		this.accessDoors = accessDoors;
	}

	/**
	 * get access doors
	 * @return
	 */
	public ArrayList<Integer> getAccessDoors() {
		return this.accessDoors;
	}

	/**
	 * add internal door
	 * @param doorID
	 */
	public void addInternalDoor(int doorID) {
		if (!internalDoors.contains(doorID))
			this.internalDoors.add(doorID);
	}

	/**
	 * set internal doors
	 * @param internalDoors
	 */
	public void setInternalDoors(ArrayList<Integer> internalDoors) {
		this.internalDoors = internalDoors;
	}

	/**
	 * get internal doors
	 * @return
	 */
	public ArrayList<Integer> getInternalDoors() {
		return this.internalDoors;
	}

	/**
	 * whether has crucial partition
	 * @return
	 */
	public boolean hasCrucial() {
		return hasCrucial;
	}

	/**
	 * set the node having crucial partition
	 */
	public void setHasCrucial() {
		this.hasCrucial = true;
		;
	}

	/**
	 * get node's type
	 * @return
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * set distance matrix
	 * @param doorPair
	 * @param dis
	 */
	public void setDisMatrix(String doorPair, String dis) {
		this.disMatrix.put(doorPair, dis);
	}

	/**
	 * set distance matrix
	 * @param DM
	 */
	public void setDistMatrix(HashMap<String, String> DM) {
		this.disMatrix = DM;
	}

	/**
	 * set distance matrix for VIP-tree
	 * @param DM
	 */
	public void setDistMatrixVIP(HashMap<String, String> DM) {
		this.disMatrixVIP = DM;
	}

	/**
	 * get distance matrix for VIP-tree
	 * @return
	 */
	public HashMap<String, String> getDistMatrixVIP() {
		return this.disMatrixVIP;
	}

	/**
	 * get distance matrix of VIP-tree
	 * @param d1
	 * @param d2
	 * @return
	 */
	public String getDistVIP(int d1, int d2) {
		String result = this.disMatrixVIP.get(d1 + "-" + d2);
		if (result == null) {
			System.out.println("something wrong with node.getDist VIP");
			System.out.println("nodeId" + this.getNodeID());
			System.out.println("d1: " + d1 + "; d2" + d2);
			System.out.println(this.toString());
		}
		return result;
	}

	/**
	 * get the distance between two doors in this node
	 * @param d1
	 * @param d2
	 * @return
	 */
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
//		return  result;
//	}

	/**
	 * get distance matrix
	 * @return
	 */
	public HashMap<String, String> getDistMatrix() {
		return this.disMatrix;
	}

	/**
	 * compare to another node
	 * @param another
	 * @return
	 */
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

	/**
	 * add children
	 * @param nodeID
	 */
	public void addChildren(int nodeID) {
	}

	/**
	 * get children
	 * @return
	 */
	public int getChildrenSize() {
		return -1;
	}

	public ArrayList<Integer> getmChildren() {
		return null;
	}

	public void emptymChildren() {
	}

	// toString short
	public String toStringShort() {
		return "leaf_N_" + this.getNodeID() + " degree: " + this.getDegree() + " adjacentNodeNo: "
				+ this.getAdjacentNodeNo();
	}

	// toString
	public String toString() {
		return "leaf_N_" + this.getNodeID() + " parent:" + this.getParentNodeID() + " accessDoors: "
				+ this.getAccessDoors() + " internalDoors: " + this.getInternalDoors() + " partitions: "
				+ this.getmPartitions() + "objects" + this.getAllObjects() + " adjacentNodes: " + this.getAdjacentNodes() + " degree: " + this.getDegree()
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

	public String getBoundary() {
		return "x1 = " + this.getX1() + ", y1 = " + this.getY1() + ", z1 = " + this.getZ1() + ", x2 = " + this.getX2()
				+ ", y2 = " + this.getY2() + " z2 = " + this.getZ2();
	}

	// check whether a point is in a node
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
		this.objectDist = objectDist;
	}

	public HashMap<Integer, ArrayList<ArrayList<Double>>> getObjectDist() {
		return this.objectDist;
	}

//	public void constructDistCube(int level, boolean isRead) {
//		this.distanceCube = new DistanceCube(this, level, isRead);
//	}

//	public DistanceCube getDistanceCube() {
//		return this.distanceCube;
//	}
}
