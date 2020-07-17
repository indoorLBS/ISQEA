package dk.aau.cs.indoorqueries.common.iPTree;

import dk.aau.cs.indoorqueries.common.algorithm.BinaryHeap;
import dk.aau.cs.indoorqueries.common.indoorEntitity.*;
import dk.aau.cs.indoorqueries.common.utilities.Constant;
import dk.aau.cs.indoorqueries.common.utilities.DataGenConstant;
import dk.aau.cs.indoorqueries.common.utilities.RoomType;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * VIPTree
 * @author Zijin Feng, Tiantian Liu
 */
public class VIPTree {

	private final int M = 4;
	private final int MAX_CHILDREN_NUM = M; // child number constraint for internal node only
	private final int MIN_CHILDREN_NUM = M / 2; // child number constraint for internal node only

	private static Node root;

	public static HashMap<Integer, Level> levels = new HashMap<Integer, Level>();
	private HashMap<Integer, ArrayList<Integer>> parNodeId = new HashMap<>(); // map partition ID to its node ID

	public static ArrayList<ArrayList<Integer>> level_doors = new ArrayList<ArrayList<Integer>>();    // set of access doors at each levels

	public static VIPTree createTree() {
		root = new NonLeafNode();
		return new VIPTree();
	}

	/**
	 * tree's level
	 */
	private class Level {
		private int levelId;
		private ArrayList<Node> levelNodes = new ArrayList<>();

		private Level(int levelId) {
			this.levelId = levelId;
		}

		private void addNodeToLevel(Node node) {
			levelNodes.add(node);
		}

		private ArrayList<Node> getLevelNodes() {
			return levelNodes;
		}

		private Node getNode(int nodeID) {
			Node result = null;

			for (int i = 0; i < levelNodes.size(); i++) {
				if (levelNodes.get(i).getNodeID() == nodeID)
					result = levelNodes.get(i);
			}

			return result;
		}

		// update the node if already exist
		// ignore if not exist yet
		private void updateNode(Node new_node) {
			int index = levelNodes.indexOf(new_node);

			if (index != -1) {
				levelNodes.remove(index);
				levelNodes.add(index, new_node);
			}
		}
	}

	/**
	 * create IP-Tree with precomputed info
	 *
	 * @throws IOException
	 */
	public void initTree() throws IOException {
		if (DataGenConstant.dataset.equals("MZB")) {
			createLeafNodes_mzb();
		} else {
			createLeafNodes_new();
		}

		mergeNodes();

		readDM();
	}

	/**
	 * create IP-Tree
	 * @throws IOException
	 */
	public void initTreeConstruct() throws IOException {

		if (DataGenConstant.dataset.equals("MZB")) {
			createLeafNodes_mzb();
		} else {
			createLeafNodes_new();
		}

		mergeNodes();

		constructLeafDM();

		constructNonLeafDM();

		saveDM();
	}

	/**
	 * create VIP-Tree with precomputed info
	 * @throws IOException
	 */
	public void initVIPtree() throws IOException {
		initTree();

		readDMforVIPtree();
	}

	/**
	 * create VIP-Tree
	 * @throws IOException
	 */
	public void initVIPtreeConstruct() throws IOException {
//		initTree();
		initTreeConstruct();

		constructDMforVIPtree();

		saveDMforVIPtree();
	}

	/**
	 * create leaf nodes
	 */
	private void createLeafNodes_new() {
		Level level_1 = new Level(1);
		boolean[] isVisited = new boolean[IndoorSpace.iPartitions.size()];

		// for each unvisited crucial partition
		for (int i = 0; i < IndoorSpace.iCrucialPartitions.size(); i++) {
			Partition par = IndoorSpace.iCrucialPartitions.get(i);
//			System.out.println("crucialPartitionId: " + par.getmID());
			int parId = par.getmID();
			if (isVisited[parId]) continue;

			LeafNode leafNode = new LeafNode();

			// partitions of the node
			ArrayList<Integer> partitions = new ArrayList<Integer>();
			// access door of the node
			ArrayList<Integer> accessDoors = new ArrayList<Integer>();
			// internal door of the node
			ArrayList<Integer> internalDoors = new ArrayList<Integer>();

			partitions.add(par.getmID());
			isVisited[par.getmID()] = true; // mark partition is visited
			if (parNodeId.get(par.getmID()) == null) {
				ArrayList<Integer> arr = new ArrayList<Integer>();
				arr.add(leafNode.getNodeID());
				parNodeId.put(par.getmID(), arr);
			} else {
				System.out.println("something wrong_TempTree_createLeafNodes_ exist parNodeID");
			}

			ArrayList<Integer> parts = par.getTopology().getP2PLeave();

			for (int j = 0; j < parts.size(); j++) {
				int parConId = parts.get(j);
				Partition parCon = IndoorSpace.iPartitions.get(parConId);
				if (isVisited[parConId]) continue;

				if (parCon.gettType() == RoomType.CRUCIALPASS) continue;

//				if (parCon.getmFloor() != par.getmFloor()) continue;

				int commonDoorSize = findCommonDoorNo(parId, parConId);

				boolean isRightPar = true;

				ArrayList<Integer> parConNexts = parCon.getTopology().getP2PLeave();
				for (int k = 0; k < parConNexts.size(); k++) {
					int parConNextId = parConNexts.get(k);
					Partition parConNext = IndoorSpace.iPartitions.get(parConNextId);
					if (parConNext.gettType() == RoomType.CRUCIALPASS && findCommonDoorNo(parConId, parConNextId) > commonDoorSize) {
						isRightPar = false;
						break;
					}
				}

				if (isRightPar) {
					partitions.add(parConId);
					isVisited[parConId] = true; // mark partition is visited
					if (parNodeId.get(parConId) == null) {
						ArrayList<Integer> arr = new ArrayList<Integer>();
						arr.add(leafNode.getNodeID());
						parNodeId.put(parConId, arr);
					} else {
						System.out.println("something wrong_TempTree_createLeafNodes_ exist parNodeID");
					}

				}
			}

			ArrayList<Integer> canAccessDoors = new ArrayList<Integer>();

			for (int j = 0; j < partitions.size(); j++) {
				ArrayList<Integer> tempDoors = new ArrayList<Integer>();
				tempDoors = IndoorSpace.iPartitions.get(partitions.get(j)).getmDoors();

				for (int k = 0; k < tempDoors.size(); k++) {
					if (!canAccessDoors.contains(tempDoors.get(k)))
						canAccessDoors.add(tempDoors.get(k));
				}
			}
//			System.out.println("canAccessDoors: " + canAccessDoors);
//			System.out.println("partitions: " + partitions);

			// System.out.println("candidate doors: " + canAccessDoors);

			// for each candidate access door, check whether it does
			for (int j = 0; j < canAccessDoors.size(); j++) {
				Door door = IndoorSpace.iDoors.get(canAccessDoors.get(j));

				if (isAccessDoor(door, partitions)) {
					// System.out.println("check access. d" + door.getmID() + " is between " +
					// door.getmPartitions() + " and " + partitions);
					if (!accessDoors.contains(door.getmID()))
						accessDoors.add(door.getmID());
				} else {
					if (!internalDoors.contains(door.getmID()))
						internalDoors.add(door.getmID());
				}
			}

			leafNode.setPartitions(partitions);
			leafNode.setAccessDoors(accessDoors);
			leafNode.setInternalDoors(internalDoors);

			leafNode.setParentNodeID(root.getNodeID());

			level_1.addNodeToLevel(leafNode);

		}
		int visitedParNum = 0;
		while (visitedParNum < IndoorSpace.iPartitions.size()) {
//			System.out.println("visitedParNum: " + visitedParNum);
			int tempVisitedParNum = 0;

			// put each left partition to an adjacent node
			for (int i = 0; i < IndoorSpace.iPartitions.size(); i++) {
				if (!isVisited[i]) {
					Partition partition = IndoorSpace.iPartitions.get(i);
					//				ArrayList<Integer> parts = new ArrayList<Integer>();
					ArrayList<Integer> doors = new ArrayList<Integer>();
					//				parts = partition.getTopology().getP2PLeave();
					doors = partition.getmDoors();
					for (int h = 0; h < doors.size(); h++) {
						int doorId = doors.get(h);
//						System.out.println(doorId);
						Door door = IndoorSpace.iDoors.get(doorId);
						ArrayList<Integer> parts = door.getmPartitions();

						for (int j = 0; j < parts.size(); j++) {
							if (!isVisited[parts.get(j)])
								continue;
							if (parts.get(j) == i) continue;

							LeafNode leafNode = (LeafNode) level_1.getNode(parNodeId.get(parts.get(j)).get(0));

							// mark as visited
							isVisited[i] = true;
							tempVisitedParNum++;

							if (parNodeId.get(partition.getmID()) == null) {
								ArrayList<Integer> arr = new ArrayList<Integer>();
								arr.add(leafNode.getNodeID());
								parNodeId.put(partition.getmID(), arr);
							} else {
								System.out.println("something wrong_TempTree_createLeafNodes_ exist parNodeID");
							}

							// System.out.println("before: " + leafNode.getmPartitions());
							// add partition
							leafNode.addPartition(partition.getmID());

							// add internal doors and remove this door from access door
							leafNode.addInternalDoor(doorId);
							if (leafNode.getAccessDoors().contains(doorId)) {
								leafNode.removeAccessDoor(doorId);
							}

							// System.out.println("5. add v" + partition.getmID() + " add d" +
							// doors.get(j));

							// add access doors
							ArrayList<Integer> canAccessDoors = new ArrayList<Integer>();
							canAccessDoors = partition.getmDoors();

							for (int k = 0; k < canAccessDoors.size(); k++) {
								Door door1 = IndoorSpace.iDoors.get(canAccessDoors.get(k));

								if (isAccessDoor(door1, leafNode.getmPartitions())) {
									// System.out.println("check access. d" + door.getmID() + " is between " +
									// door.getmPartitions() + " and " + leafNode.getmPartitions());
									if (!leafNode.getAccessDoors().contains(door1.getmID()))
										leafNode.addAccessDoor(door1.getmID());
								} else {
									if (!leafNode.getInternalDoors().contains(door1.getmID()))
										leafNode.addInternalDoor(door1.getmID());
								}
							}

							// update the leafNodes
							level_1.updateNode(leafNode);

							// System.out.println("Node: " + (LeafNode)
							// level_1.getNode(parNodeId.get(parts.get(j))));

							break;
						}
						if (isVisited[i]) break;

					}


					//				if (parts.size() != doors.size())
					//					System.out.println("something wrong_TempTree_createLeafNode_parts size != doors size2");


				} else {
					tempVisitedParNum++;
				}
			}
			if (tempVisitedParNum == visitedParNum) {
				for (int i = 0; i < IndoorSpace.iPartitions.size(); i++) {
					Partition par = IndoorSpace.iPartitions.get(i);
					if (!isVisited[i]) {
						LeafNode leafNode = new LeafNode();

						// partitions of the node
						ArrayList<Integer> partitions = new ArrayList<Integer>();
						// access door of the node
						ArrayList<Integer> accessDoors = new ArrayList<Integer>();
						// internal door of the node
						ArrayList<Integer> internalDoors = new ArrayList<Integer>();

						partitions.add(par.getmID());
						isVisited[par.getmID()] = true; // mark partition is visited
						if (parNodeId.get(par.getmID()) == null) {
							ArrayList<Integer> arr = new ArrayList<Integer>();
							arr.add(leafNode.getNodeID());
							parNodeId.put(par.getmID(), arr);
						} else {
							System.out.println("something wrong_TempTree_createLeafNodes_ exist parNodeID");
						}
						accessDoors.addAll(par.getmDoors());
						leafNode.setPartitions(partitions);
						leafNode.setAccessDoors(accessDoors);
						leafNode.setInternalDoors(internalDoors);

						leafNode.setParentNodeID(root.getNodeID());

						level_1.addNodeToLevel(leafNode);
					}

				}
			} else {
				visitedParNum = tempVisitedParNum;
			}
		}

		for (int i = 0; i < IndoorSpace.iPartitions.size(); i++) {
			if (!isVisited[i]) {
				System.out.println("notVisitedPar: " + i);
				System.out.println("something wrong_TempTree_initTree_partition not merged");
			}
		}

		// update the adjacent node size for each node
		ArrayList<Node> nodes = new ArrayList<Node>();
		nodes = level_1.getLevelNodes();

		for (int i = 0; i < nodes.size(); i++) {
			Node node_1 = nodes.get(i);

			// System.out.println("Node: " + node_1);

			// the access doors of the node
			ArrayList<Integer> doors_1 = new ArrayList<Integer>();
			doors_1 = node_1.getAccessDoors();

			for (int j = 0; j < doors_1.size(); j++) {
				ArrayList<Integer> parts = new ArrayList<Integer>();
				parts = IndoorSpace.iDoors.get(doors_1.get(j)).getmPartitions();

				// System.out.println("d" + doors_1.get(j) + " between " + parts);

				for (int k = 0; k < parts.size(); k++) {
					int nodeID = parNodeId.get(parts.get(k)).get(0);

					if (nodeID != node_1.getNodeID()) {
						// System.out.println("v" + parts.get(k) + " is in Node: " +
						// level_1.getNode(nodeID));

						// nodeID is id of adjacent node
						level_1.levelNodes.get(i).addAdjacentNode(nodeID);
						level_1.getNode(nodeID).addAdjacentNode(node_1.getNodeID());
					}
				}
			}

			// System.out.println(level_1.levelNodes.get(i) + " has " +
			// level_1.levelNodes.get(i).getAdjacentNodeNo());
		}

		// add level_1 to levels
		levels.put(level_1.levelId, level_1);
		DataGenConstant.leafNodeSize = level_1.getLevelNodes().size();
		System.out.println("Leaf Node created. " + level_1.getLevelNodes().size());
//		System.out.println("totalnodesize: " + HSMDataGenConstant.totalNodeSize);

	}

	/**
	 * create leaf nodes for MZB
	 */
	private void createLeafNodes_mzb() {
		Level level_1 = new Level(1);
		boolean[] isVisited = new boolean[IndoorSpace.iPartitions.size()];

		// for each unvisited crucial partition
		for (int i = 0; i < IndoorSpace.iCrucialPartitions.size(); i++) {
			Partition par = IndoorSpace.iCrucialPartitions.get(i);
			int parId = par.getmID();
			if (isVisited[parId]) continue;

			LeafNode leafNode = new LeafNode();

			// partitions of the node
			ArrayList<Integer> partitions = new ArrayList<Integer>();
			// access door of the node
			ArrayList<Integer> accessDoors = new ArrayList<Integer>();
			// internal door of the node
			ArrayList<Integer> internalDoors = new ArrayList<Integer>();

			partitions.add(par.getmID());
			isVisited[par.getmID()] = true; // mark partition is visited
			if (parNodeId.get(par.getmID()) == null) {
				ArrayList<Integer> arr = new ArrayList<Integer>();
				arr.add(leafNode.getNodeID());
				parNodeId.put(par.getmID(), arr);
			} else {
				System.out.println("something wrong_TempTree_createLeafNodes_ exist parNodeID");
			}

			ArrayList<Integer> parts = par.getTopology().getP2PLeave();

			for (int j = 0; j < parts.size(); j++) {
				int parConId = parts.get(j);
				Partition parCon = IndoorSpace.iPartitions.get(parConId);
				if (isVisited[parConId]) continue;

				if (parCon.gettType() == RoomType.CRUCIALPASS) continue;

				if (parCon.getmFloor() != par.getmFloor()) continue;

				int commonDoorSize = findCommonDoorNo(parId, parConId);

				boolean isRightPar = true;

				ArrayList<Integer> parConNexts = parCon.getTopology().getP2PLeave();
				for (int k = 0; k < parConNexts.size(); k++) {
					int parConNextId = parConNexts.get(k);
					Partition parConNext = IndoorSpace.iPartitions.get(k);
					if (parConNext.gettType() == RoomType.CRUCIALPASS && parConNext.getmFloor() == parCon.getmFloor() && findCommonDoorNo(parConId, parConNextId) > commonDoorSize) {
						isRightPar = false;
						break;
					}
				}

				if (isRightPar) {
					partitions.add(parConId);
					isVisited[parConId] = true; // mark partition is visited
					if (parNodeId.get(parConId) == null) {
						ArrayList<Integer> arr = new ArrayList<Integer>();
						arr.add(leafNode.getNodeID());
						parNodeId.put(parConId, arr);
					} else {
						System.out.println("something wrong_TempTree_createLeafNodes_ exist parNodeID");
					}

				}
			}

			ArrayList<Integer> canAccessDoors = new ArrayList<Integer>();

			for (int j = 0; j < partitions.size(); j++) {
				ArrayList<Integer> tempDoors = new ArrayList<Integer>();
				tempDoors = IndoorSpace.iPartitions.get(partitions.get(j)).getmDoors();

				for (int k = 0; k < tempDoors.size(); k++) {
					if (!canAccessDoors.contains(tempDoors.get(k)))
						canAccessDoors.add(tempDoors.get(k));
				}
			}

			// System.out.println("candidate doors: " + canAccessDoors);

			// for each candidate access door, check whether it does
			for (int j = 0; j < canAccessDoors.size(); j++) {
				Door door = IndoorSpace.iDoors.get(canAccessDoors.get(j));

				if (isAccessDoor(door, partitions)) {
					// System.out.println("check access. d" + door.getmID() + " is between " +
					// door.getmPartitions() + " and " + partitions);
					if (!accessDoors.contains(door.getmID()))
						accessDoors.add(door.getmID());
				} else {
					if (!internalDoors.contains(door.getmID()))
						internalDoors.add(door.getmID());
				}
			}

			leafNode.setPartitions(partitions);
			leafNode.setAccessDoors(accessDoors);
			leafNode.setInternalDoors(internalDoors);

			leafNode.setParentNodeID(root.getNodeID());

			level_1.addNodeToLevel(leafNode);

		}

		// put each left partition to an adjacent node
		for (int i = 0; i < IndoorSpace.iPartitions.size(); i++) {
			if (!isVisited[i]) {
				Partition partition = IndoorSpace.iPartitions.get(i);
				ArrayList<Integer> parts = new ArrayList<Integer>();
				ArrayList<Integer> doors = new ArrayList<Integer>();
				parts = partition.getTopology().getP2PLeave();
				doors = partition.getTopology().getP2DLeave();

				if (parts.size() != doors.size())
					System.out.println("something wrong_TempTree_createLeafNode_parts size != doors size2");

				for (int j = 0; j < parts.size(); j++) {
					if (!isVisited[parts.get(j)])
						continue;

					LeafNode leafNode = (LeafNode) level_1.getNode(parNodeId.get(parts.get(j)).get(0));

					// mark as visited
					isVisited[i] = true;

					if (parNodeId.get(partition.getmID()) == null) {
						ArrayList<Integer> arr = new ArrayList<Integer>();
						arr.add(leafNode.getNodeID());
						parNodeId.put(partition.getmID(), arr);
					} else {
						System.out.println("something wrong_TempTree_createLeafNodes_ exist parNodeID");
					}

					// System.out.println("before: " + leafNode.getmPartitions());
					// add partition
					leafNode.addPartition(partition.getmID());

					// add internal doors and remove this door from access door
					leafNode.addInternalDoor(doors.get(j));
					leafNode.removeAccessDoor(doors.get(j));

					// System.out.println("5. add v" + partition.getmID() + " add d" +
					// doors.get(j));

					// add access doors
					ArrayList<Integer> canAccessDoors = new ArrayList<Integer>();
					canAccessDoors = partition.getmDoors();

					for (int k = 0; k < canAccessDoors.size(); k++) {
						Door door = IndoorSpace.iDoors.get(canAccessDoors.get(k));

						if (isAccessDoor(door, leafNode.getmPartitions())) {
							// System.out.println("check access. d" + door.getmID() + " is between " +
							// door.getmPartitions() + " and " + leafNode.getmPartitions());
							if (!leafNode.getAccessDoors().contains(door.getmID()))
								leafNode.addAccessDoor(door.getmID());
						}
						else {
							if (!leafNode.getInternalDoors().contains(door.getmID()))
								leafNode.addInternalDoor(door.getmID());
						}
					}

					// update the leafNodes
					level_1.updateNode(leafNode);

					// System.out.println("Node: " + (LeafNode)
					// level_1.getNode(parNodeId.get(parts.get(j))));

					break;
				}
			}
		}

		for (int i = 0; i < IndoorSpace.iPartitions.size(); i++) {
			if (!isVisited[i])
				System.out.println("something wrong_TempTree_initTree_partition not merged");
		}

		// update the adjacent node size for each node
		ArrayList<Node> nodes = new ArrayList<Node>();
		nodes = level_1.getLevelNodes();

		for (int i = 0; i < nodes.size(); i++) {
			Node node_1 = nodes.get(i);

			// System.out.println("Node: " + node_1);

			// the access doors of the node
			ArrayList<Integer> doors_1 = new ArrayList<Integer>();
			doors_1 = node_1.getAccessDoors();

			for (int j = 0; j < doors_1.size(); j++) {
				ArrayList<Integer> parts = new ArrayList<Integer>();
				parts = IndoorSpace.iDoors.get(doors_1.get(j)).getmPartitions();

				// System.out.println("d" + doors_1.get(j) + " between " + parts);

				for (int k = 0; k < parts.size(); k++) {
					int nodeID = parNodeId.get(parts.get(k)).get(0);

					if (nodeID != node_1.getNodeID()) {
						// System.out.println("v" + parts.get(k) + " is in Node: " +
						// level_1.getNode(nodeID));

						// nodeID is id of adjacent node
						level_1.levelNodes.get(i).addAdjacentNode(nodeID);
						level_1.getNode(nodeID).addAdjacentNode(node_1.getNodeID());
					}
				}
			}

			// System.out.println(level_1.levelNodes.get(i) + " has " +
			// level_1.levelNodes.get(i).getAdjacentNodeNo());
		}

		// add level_1 to levels
		levels.put(level_1.levelId, level_1);
		DataGenConstant.leafNodeSize = level_1.getLevelNodes().size();
		System.out.println("Leaf Node created. " + level_1.getLevelNodes().size());
//		System.out.println("totalnodesize: " + DataGenConstant.totalNodeSize);

	}


	/**
	 * merge nodes
	 */
	private void mergeNodes() {
		int k = 1;
		int nodeSize = levels.get(1).getLevelNodes().size();

		int h = 5;

		while (nodeSize > 1 && h > 0) {
//			System.out.println();
//			System.out.println("mergeNodes level: " + k);
			ArrayList<Node> nodes = new ArrayList<Node>();
//			System.out.println("level nodes size: " + levels.get(k).getLevelNodes().size());

			ArrayList<Node> nodeCopy = new ArrayList<Node>();

			for (int i = 0; i < levels.get(k).getLevelNodes().size(); i++) {
				if (levels.get(k).getLevelNodes().get(i).getClass().getSimpleName().equals("LeafNode")) {
					nodeCopy.add(new LeafNode((LeafNode) levels.get(k).getLevelNodes().get(i)));

				} else {
					nodeCopy.add(new NonLeafNode((NonLeafNode) levels.get(k).getLevelNodes().get(i)));
				}
			}

			nodes = createNextLevel(nodeCopy, MIN_CHILDREN_NUM, MAX_CHILDREN_NUM, k);
			nodeSize = nodes.size();

//			System.out.println("nodes size: " + nodeSize);

			ArrayList<Node> new_Nodes = new ArrayList<Node>();

			for (Node n : nodes) {
//				System.out.println(n.getNodeID() + " children: " + n.getmChildren());

				Node new_Node = null;

				if (n.getType() == "LeafNode")
					System.out.println("something wrong_TempTree_mergeNodes");
				else {
					Node temp_Node = new NonLeafNode();
					new_Node = new NonLeafNode((NonLeafNode) n);
					new_Node.setNodeID(temp_Node.getNodeID());
				}

				new_Node.emptymChildren();

				for (int nID : n.getmChildren()) {
					if (k > 1) {
						nID = levels.get(k - 1).getNode(nID).getParentNodeID();
					}

					if (!new_Node.getmChildren().contains(levels.get(k).getNode(nID).getNodeID()))
						new_Node.addChildren(levels.get(k).getNode(nID).getNodeID());
					levels.get(k).getNode(nID).setParentNodeID(new_Node.getNodeID());
					levels.get(k).getNode(nID).setHeight(k - 1);
				}

				new_Nodes.add(new_Node);
			}

			k++;
			Level level = new Level(k);
			for (int i = 0; i < new_Nodes.size(); i++) {
				level.addNodeToLevel(new_Nodes.get(i));
			}

			// add level_k to levels
			levels.put(level.levelId, level);

//			System.out.println(
//					"k: " + k + " nodeSize: " + levels.get(k).getLevelNodes().size() + " = " + new_Nodes.size());
		}

		levels.get(k).getLevelNodes().get(0).setHeight(k - 1);
		this.root = levels.get(k).getLevelNodes().get(0);
	}

	/**
	 * create next level
	 * @param levelNodes
	 * @param minDegree
	 * @param maxDegree
	 * @param k
	 * @return
	 */
	private ArrayList<Node> createNextLevel(ArrayList<Node> levelNodes, int minDegree, int maxDegree, int k) {
		ArrayList<Node> nextLevelNodes = new ArrayList<Node>();
		MinHeap_Node<Node> Q = new MinHeap_Node<>();

		// insert each node in levelNodes into a heap H, degree = 1
		for (int i = 0; i < levelNodes.size(); i++) {
//			
			if (levelNodes.get(i).getClass().getSimpleName().equals("LeafNode")) {
				levelNodes.get(i).setDegree(1);
				if (!Q.exists(levelNodes.get(i))) {
					Q.insert(new LeafNode((LeafNode) levelNodes.get(i)));
				}

			} else {
				levelNodes.get(i).setDegree(1);
				if (!Q.exists(levelNodes.get(i))) {
					Q.insert(new NonLeafNode((NonLeafNode) levelNodes.get(i)));
				}
			}
		}

		int h = 5;
//		System.out.println("Q.heapSize : " + Q.heapSize);

		while (Q.heapSize > 0 && Q.get_min().getDegree() < MIN_CHILDREN_NUM && h >= 0) {

			Node n_i = Q.delete_min();

//			System.out.println("dequeue " + n_i.toString());

			// find n_j from all nodes, with maximum common nodes with n_i
			Node n_j = null;
			// number of maximum amount of common node
			int maxComm = 0;
			// can skip the insertion
			boolean canSkip = true;
			// new node to be inserted into Q
			Node n_insert = null;
			// set of access doors that cross the floor
			ArrayList<String> crossFloorDoors = new ArrayList<String>();

			ArrayList<Node> n_js = new ArrayList<Node>();
			n_js = Q.getAll();

			for (int i = 0; i < n_js.size(); i++) {
				Node n_j_temp = n_js.get(i);

//				if (n_j_temp.getDegree() >= MAX_CHILDREN_NUM)
//					continue;

				if (n_i.getNodeID() == n_j_temp.getNodeID())
					continue;

				ArrayList<Integer> set_i = new ArrayList<Integer>();
				ArrayList<Integer> set_j = new ArrayList<Integer>();
				ArrayList<Integer> set_union = new ArrayList<Integer>();

				set_i = (ArrayList<Integer>) n_i.getAccessDoors().clone();
				set_j = (ArrayList<Integer>) n_j_temp.getAccessDoors().clone();
				set_union = setUnion(set_i, set_j);

//				System.out.println(set_i + " " + set_j + " " + set_union);

				if (set_union.size() > maxComm) {
					if (!n_i.getAdjacentNodes().contains(n_j_temp.getNodeID())
							|| !n_j_temp.getAdjacentNodes().contains(n_i.getNodeID())) {
//						System.out.println("something wrong_TempTree_createNextLevel_1");
					}

					canSkip = false;
					n_j = n_j_temp;
					maxComm = set_union.size();
				}
			}

			if (canSkip) {
				maxComm = 0;

				for (int i = 0; i < n_js.size(); i++) {
					Node n_j_temp = n_js.get(i);

					if (n_j_temp.getDegree() >= MAX_CHILDREN_NUM)
						continue;

					if (n_i.getNodeID() == n_j_temp.getNodeID())
						continue;

					ArrayList<Integer> set_i = new ArrayList<Integer>();
					ArrayList<Integer> set_j = new ArrayList<Integer>();
					ArrayList<Integer> set_union = new ArrayList<Integer>();

					set_i = (ArrayList<Integer>) n_i.getAccessDoors().clone();
					set_j = (ArrayList<Integer>) n_j_temp.getAccessDoors().clone();
					set_union = setUnion(set_i, set_j);

//					System.out.println(set_i + " " + set_j + " " + set_union);

					for (int door_i : set_i) {
						for (int door_j : set_j) {

//							System.out.println("door_i: " + door_i + " door_j: " + door_j + " Math.abs(door_i - door_j): " +Math.abs(door_i - door_j) + 
//									(Math.abs(door_i - door_j) - HSMDataGenConstant.curSizeDoor == 0));

							if (Math.abs(door_i - door_j) - DataGenConstant.curSizeDoor == 0) {
								crossFloorDoors.add(
										n_i.getNodeID() + "-" + door_i + "-" + n_j_temp.getNodeID() + "-" + door_j);
							}
						}
					}

					if (crossFloorDoors.size() > maxComm) {
						System.out.println("find cross floor doors");
						n_j = n_j_temp;
						maxComm = crossFloorDoors.size();
					}
				}

				if (maxComm == 0) {
					System.out.println("can Skip");
					continue;
				}
			}

			if (n_j == null)
				System.out.println("something wrong_TempTree_createNextLevel_2");

			int remove = -1;

			if (n_i.getClass().getSimpleName().equals("LeafNode")) {
				if (!n_j.getClass().getSimpleName().equals("NonLeafNode")) {
//					System.out.println("LeafNode-LeafNode");
					// create new node, merge Ni Nj to Nk
					NonLeafNode n_k = new NonLeafNode();

					n_k.mergeNodes(n_i, n_j, parNodeId, k, crossFloorDoors);

					remove = n_j.getNodeID();

					n_insert = n_k;

				} else {
//					System.out.println("LeafNode-NonLeafNode");
					// merge Ni to Nj
					((NonLeafNode) n_j).mergeNodes(n_i, parNodeId, 1, k, crossFloorDoors);

					remove = n_j.getNodeID();

					n_insert = n_j;
				}
			} else {
				if (n_j.getClass().getSimpleName().equals("NonLeafNode")) {
//					System.out.println("NonLeafNode-NonLeafNode");
					// merge Ni's leafNodes to Nj
					((NonLeafNode) n_j).mergeNodes(n_i, parNodeId, 2, k, crossFloorDoors);

					remove = n_j.getNodeID();

					n_insert = n_j;
				} else {
//					System.out.println("NonLeafNode-LeafNode");
					// merge Nj to Ni
					((NonLeafNode) n_i).mergeNodes(n_j, parNodeId, 1, k, crossFloorDoors);

					remove = n_j.getNodeID();

					n_insert = n_i;
				}
			}

			// remove Nj from Q and merge with Ni into a new node Nk
			ArrayList<Node> n_temp = new ArrayList<Node>();

//			System.out.println("Q.heapSize after: " + Q.heapSize);
//			for (int i = 0; i < Q.getAll().size(); i++) {
//				System.out.print(Q.getAll().get(i).getNodeID() + " ");
//			}
//			System.out.println();

			n_temp = Q.getAll();

			while (Q.heapSize > 0) {
				Q.delete_min();
			}
//			System.out.println();
//			System.out.println("n_temp size: " + n_temp.size());
//			for (int i = 0; i < n_temp.size(); i++) {
//				System.out.print(n_temp.get(i).getNodeID() + " ");
//			}
//			System.out.println();


			int index = -1; // index of node to be removed

//			System.out.println("remove: " + remove + " nj: " + n_j.getNodeID());

			if (remove != -1)
				index = getIndex(remove, n_temp);
			else {
				System.out.println("something wrong with remove");
			}


			// insert Nk to Q7
			n_temp.add(n_insert);

			if (index != -1) { // remove ni and nj
				if (n_insert.getNodeID() == remove) { // same node, replace n_temp.get(index) with "n_insert"
					n_temp.remove(index);
				} else { // different node, remove n_temp.get(index)
					n_temp.remove(index);
				}

			} else {
				System.out.println("something wrong_TempTree_createNextLevel_4: index = " + index);
			}

			Q = new MinHeap_Node<>();

			for (int i = 0; i < n_temp.size(); i++) {
				if (n_temp.get(i).getType() == "LeafNode") {
					Q.insert(new LeafNode((LeafNode) n_temp.get(i)));
				} else if (n_temp.get(i).getType() == "NonLeafNode") {
					Q.insert(new NonLeafNode((NonLeafNode) n_temp.get(i)));
				} else {
					System.out.println("something wrong_TempTree_createNextLevel_5");
				}
			}


//			for (int i = 0; i < Q.getAll().size(); i++) {
//				System.out.print(Q.getAll().get(i).getNodeID() + " ");
//			}
//			System.out.println();

		}

		while (Q.heapSize > 0) {
			nextLevelNodes.add(Q.delete_min());
		}

		return nextLevelNodes;
	}

	/**
	 * construct distance matrix for leaf node
	 */
	public void constructLeafDM() {
		ArrayList<Node> nodes = levels.get(1).getLevelNodes();
		String result;
		for (int i = 0; i < nodes.size(); i++) {
			Node n = nodes.get(i);
//			System.out.println("node: " + n.toString());
			ArrayList<Integer> doors1 = new ArrayList<>();
			ArrayList<Integer> doors2 = new ArrayList<>();
			ArrayList<Integer> doors = new ArrayList<>();
			doors1.addAll(n.getAccessDoors());
			doors2.addAll(n.getInternalDoors());

			for (int j = 0; j < doors1.size(); j++) {
				for (int k = 0; k < doors2.size(); k++) {
//					if (j == k) continue;
					if ((int) doors1.get(j) == (int) doors2.get(k)) {
						doors2.remove(k);
						k--;
					}
				}
			}
			doors.addAll(doors1);
			doors.addAll(doors2);

			for (int j = 0; j < doors.size(); j++) {
				for (int k = j + 1; k < doors.size(); k++) {
					int d1 = doors.get(j);
					int d2 = doors.get(k);
//					System.out.println("d1, d2: " + d1 + "," + d2);
					if (inSamePartition(d1, d2) != -1) {
						result = IndoorSpace.iPartitions.get(inSamePartition(d1, d2)).getdistMatrix().getDistance(d1, d2) + "\t" + d1 + "\t" + d2;
					} else {
						result = d2dDist(d1, d2);
					}
					((LeafNode) n).setDisMatrix(d1 + "-" + d2, result);
					((LeafNode) n).setDisMatrix(d2 + "-" + d1, convertPath(result));
				}
			}

		}
		System.out.println("distance matrix for leaf nodes is finished");
	}

	/**
	 * To see whether two doors are in a same partition
	 * @param d1
	 * @param d2
	 * @return
	 */
	public int inSamePartition(int d1, int d2) {
		Door door1 = IndoorSpace.iDoors.get(d1);
		ArrayList<Integer> partitions1 = door1.getmPartitions();
		Door door2 = IndoorSpace.iDoors.get(d2);
		ArrayList<Integer> partitions2 = door2.getmPartitions();

		for (int i = 0; i < partitions1.size(); i++) {
			for (int j = 0; j < partitions2.size(); j++) {
				if ((int) partitions1.get(i) == (int) partitions2.get(j)) {
					return partitions1.get(i);
				}
			}
		}
		return -1;
	}

	/**
	 * construct distance matrix for nonleaf node
	 */
	public void constructNonLeafDM() {
		for (int l = 2; l <= levels.size(); l++) {
			ArrayList<Node> nodes = this.getNodes(l);
			for (int i = 0; i < nodes.size(); i++) {
				Node n = nodes.get(i);
//				System.out.println("node " + n.toString());
				ArrayList<Integer> children = n.getmChildren();
				ArrayList<Integer> doors = new ArrayList<>();
				for (int j = 0; j < children.size(); j++) {
					Node n_child = levels.get(l - 1).getNode(children.get(j));
					if (n_child == null) {
						System.out.println("something wrong with constrctNonLeafDM");
					}
					doors.addAll(n_child.getAccessDoors());
				}
//				System.out.println("doors before: " + doors);
				for (int j = 0; j < doors.size(); j++) {
					for (int k = j + 1; k < doors.size(); k++) {
						if (j == k) continue;
						if ((int) doors.get(j) == (int) doors.get(k)) {
							doors.remove(k);
							k--;
						}
					}
				}
//				System.out.println("doors: " + doors);
				for (int j = 0; j < doors.size(); j++) {
					for (int k = j + 1; k < doors.size(); k++) {

						int d1 = doors.get(j);
						int d2 = doors.get(k);
						String result = d2dDist(d1, d2);
						if (result == null) {
							System.out.println(d1 + "-" + d2 + "is null");
						}
						((NonLeafNode) n).setDisMatrix(d1 + "-" + d2, result);
						((NonLeafNode) n).setDisMatrix(d2 + "-" + d1, convertPath(result));

					}
				}

			}

		}
		System.out.println("distance matrix for nonleaf nodes is finished");
	}

	/**
	 * convert path
	 * @param distPath
	 * @return
	 */
	public String convertPath(String distPath) {
		String result = "";
		String[] distPathArr = distPath.split("\t");
		String[] resultArr = new String[distPathArr.length];
//		System.out.println(distPathArr.length);
		for (int i = 1; i < distPathArr.length; i++) {
			resultArr[distPathArr.length - i] = distPathArr[i];
		}
		resultArr[0] = distPathArr[0];
		result = arrToString(resultArr, 0, resultArr.length - 1);
		return result;
	}

	/**
	 * change array to string
	 * @param arr
	 * @param start
	 * @param end
	 * @return
	 */
	public String arrToString(String[] arr, int start, int end) {
		String result = "";
		for (int i = start; i <= end; i++) {
			result += arr[i] + "\t";
		}
//		System.out.println("arrToString: " + result);
		return result;
	}

	/**
	 * calculate the indoor distance between two doors
	 *
	 * @param ds door id ds
	 * @param de door id de
	 * @return distance
	 */
	public String d2dDist(int ds, int de) {
//		System.out.println("ds = " + IndoorSpace.iDoors.get(ds).toString() + " de = " + IndoorSpace.iDoors.get(de).toString());
		String result = "";

		if (ds == de) return 0 + "\t";

		int size = IndoorSpace.iDoors.size();
		BinaryHeap<Double> H = new BinaryHeap<Double>(size);
		double[] dist = new double[size];        //stores the current shortest path distance
		// from source ds to a door de
		PrevPair[] prev = new PrevPair[size];        //stores the corresponding previous partition
		// and door pair (v,di) through which the dk.aau.cs.indoorqueries.algorithm
		// visits the current door de.
		boolean[] visited = new boolean[size];        // mark door as visited

		for (int i = 0; i < size; i++) {
			int doorID = IndoorSpace.iDoors.get(i).getmID();
			if (doorID != i) System.out.println("something wrong_Helper_d2dDist");
			if (doorID != ds) dist[i] = Constant.large;
			else dist[i] = 0;

			// enheap
			H.insert(dist[i], doorID);

			PrevPair pair = null;
			prev[doorID] = pair;
		}

		while (H.heapSize > 0) {
			String[] str = H.delete_min().split(",");
			int di = Integer.parseInt(str[1]);
			double dist_di = Double.parseDouble(str[0]);

//			System.out.println("dequeue <" + di + ", " + dist_di + ">");

			if (di == de) {
//				System.out.println("d2dDist_ di = " + di + " de = " + de);
				result += getPath(prev, ds, de);
				return result = dist_di + "\t" + result;
			}

			visited[di] = true;
//			System.out.println("d" + di + " is newly visited");

			Door door = IndoorSpace.iDoors.get(di);
			ArrayList<Integer> parts = new ArrayList<Integer>();        // list of leavable partitions
			parts = door.getmPartitions();

			int partSize = parts.size();

			for (int i = 0; i < partSize; i++) {
				ArrayList<Integer> doorTemp = new ArrayList<Integer>();
				int v = parts.get(i);        // partition id
				Partition partition = IndoorSpace.iPartitions.get(v);
				doorTemp = partition.getmDoors();

				// remove the visited doors
				ArrayList<Integer> doors = new ArrayList<Integer>();        // list of unvisited leavable doors
				int doorTempSize = doorTemp.size();
				for (int j = 0; j < doorTempSize; j++) {
					int index = doorTemp.get(j);
//					System.out.println("index = " + index + " " + !visited[index]);
					if (!visited[index]) {
						doors.add(index);
					}
				}

				int doorSize = doors.size();
//				System.out.println("doorSize = " + doorSize + ": " + Functions.printIntegerList(doors));

				for (int j = 0; j < doorSize; j++) {
					int dj = doors.get(j);
					if (visited[dj]) System.out.println("something wrong_Helper_d2dDist2");
//					System.out.println("for d" + di + " and d" + dj);

					double fd2d = partition.getdistMatrix().getDistance(di, dj);
					;

					if ((dist[di] + fd2d) < dist[dj]) {
						double oldDj = dist[dj];
						dist[dj] = dist[di] + fd2d;
						H.updateNode(oldDj, dj, dist[dj], dj);
						prev[dj] = new PrevPair(v, di);
						prev[dj].toString();
					}
				}
			}
		}
		return result;
	}

	public class PrevPair {
		public int par;
		public int door;

		public PrevPair(int par, int door) {
			this.par = par;
			this.door = door;
		}

		public String toString() {
			return par + ", " + door;
		}

	}


	/**
	 * @param prev
	 * @return a string path
	 */
	public static String getPath(PrevPair[] prev, int ds, int de) {
		String result = de + "";

//		System.out.println("ds = " + ds + " de = " + de + " " + prev[de].par + " " + prev[de].door);
//		int currp = prev[de].par;
		int currd = prev[de].door;

		while (currd != ds) {
			result = currd + "\t" + result;
//			System.out.println("current: " + currp + ", " + currd + " next: " + prev[currd].toString());
//			currp = prev[currd].par;
			currd = prev[currd].door;

		}

		result = currd + "\t" + result;

		return result;
	}


	/**
	 * return whether a door is an access door
	 *
	 * @param door
	 * @param partitions
	 * @return
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

	// find number of common doors between two partitions
	private int findCommonDoorNo(int partID_1, int partID_2) {
		int result = 0;
		ArrayList<Integer> doors_1 = new ArrayList<Integer>();
		ArrayList<Integer> doors_2 = new ArrayList<Integer>();

		doors_1 = IndoorSpace.iPartitions.get(partID_1).getmDoors();
		doors_2 = IndoorSpace.iPartitions.get(partID_2).getmDoors();

		for (int i = 0; i < doors_1.size(); i++) {
			int doorID_1 = doors_1.get(i);

			for (int j = 0; j < doors_2.size(); j++) {
				int doorID_2 = doors_2.get(j);

				if (doorID_1 == doorID_2)
					result++;
			}
		}

		return result;
	}

	// get the union of two set
	private ArrayList<Integer> setUnion(ArrayList<Integer> set_i, ArrayList<Integer> set_j) {
		ArrayList<Integer> result = new ArrayList<Integer>();

		for (int i = 0; i < set_i.size(); i++) {
			if (set_j.contains(set_i.get(i)))
				result.add(set_i.get(i));
		}

		return result;
	}

	/**
	 * get index of a node
	 * @param id
	 * @param list
	 * @return
	 */
	private int getIndex(int id, ArrayList<Node> list) {
		for (int i = 0; i < list.size(); i++) {
			// System.out.println(list.get(i) + " " + list.get(i).getNodeID());
			if (list.get(i).getNodeID() == id) {
				return i;
			}
		}

		return -1;
	}

	public int height() {
		return this.root.getHeight();
	}

	public static Node root() {
		return root;
	}

	public static Node getNode(int nodeID) {
		Node result = null;

		for (int i = 0; i < levels.size(); i++) {
			result = levels.get(i + 1).getNode(nodeID);
			if (result != null)
				return result;
		}

		return result;
	}

	// find the leaf node that contain the point
	public Node leaf(Point point) {
		Node result = null;
		Node currentNode = this.root();
		int partitionID = this.partition(point).getmID();

		// start from root
		if (currentNode.getmPartitions().contains(partitionID)) {
			ArrayList<Node> nodes = new ArrayList<Node>();
			nodes = this.getmChildren(currentNode);

			for (Node node : nodes) {
				Node temp = leaf(node, partitionID);
				if (temp != null) return temp;
			}
		}

		return result;
	}

	// find the leaf node that contain the point
	public Node leaf(int partitionID) {
		Node result = null;
		Node currentNode = this.root();

		// start from root
		if (currentNode.getmPartitions().contains(partitionID)) {
			ArrayList<Node> nodes = new ArrayList<Node>();
			nodes = this.getmChildren(currentNode);

			for (Node node : nodes) {
				Node temp = leaf(node, partitionID);
				if (temp != null) return temp;
			}
		}

		return result;
	}

	// find the leaf node that contain the point
	private Node leaf(Node n, int partitionID) {
		Node result = null;
		Node currentNode = n;

		// start from n
		if (currentNode.getmPartitions().contains(partitionID)) {
			if (currentNode.getType() == "NonLeafNode") {
				ArrayList<Node> nodes = new ArrayList<Node>();
				nodes = this.getmChildren(currentNode);

				for (Node node : nodes) {
					Node temp = leaf(node, partitionID);
					if (temp != null) return temp;
				}
			} else {
				return n;
			}
		}

		return result;
	}

	// find the partition that contain the point
	public Partition partition(Point point) {
		Partition result = null;

		for (Partition partition : IndoorSpace.iPartitions) {
			if (partition.isInPartition(point)) {
				if (DataGenConstant.dataset.equals("MZB") && partition.getmType() == RoomType.HALLWAY) continue;

				return partition;
			}
		}

		return result;
	}

	// get the set of child nodes of a node
	public ArrayList<Node> getmChildren(Node node) {
		if (node.getType() == "LeafNode")
			return null;

		ArrayList<Node> result = new ArrayList<Node>();
		ArrayList<Integer> node_IDs = new ArrayList<Integer>();

		node_IDs = node.getmChildren();

		for (int i = 0; i < node_IDs.size(); i++) {
			result.add(this.getNode(node_IDs.get(i)));
		}

		return result;
	}

	// get all the nodes of a level in the tree
	public static ArrayList<Node> getNodes(int level) {
		if (level < 1 || level > (root().getHeight() + 1))
			return null;

		ArrayList<Node> result = new ArrayList<Node>();

		for (Node node : levels.get(level).getLevelNodes()) {
			result.add(node);
		}

		return result;
	}

	// get all the nodes of a level in the tree that contains door id as AC
	public ArrayList<Node> getNodes(int level, int id) {
		if (level < 1 || level > (root().getHeight() + 1))
			return null;

		ArrayList<Node> result = new ArrayList<Node>();

		for (Node node : levels.get(level).getLevelNodes()) {
			if (node.getAccessDoors().contains(id)) result.add(node);
		}

		return result;
	}

	// save distance matrix
	public void saveDM() {
		for (int l = 1; l <= levels.size(); l++) {
			String result = "";

			ArrayList<Node> nodes = this.getNodes(l);
			for (int i = 0; i < nodes.size(); i++) {
				Node node = nodes.get(i);
				result += node.getNodeID() + " ";
				HashMap<String, String> DM = node.getDistMatrix();
				Iterator iterator = DM.keySet().iterator();
				while (iterator.hasNext()) {
					Object key = iterator.next();
					String value = DM.get(key);
					result += key + "\t" + value + " ";
				}
				result += "\n";
			}
			String objectsFile = System.getProperty("user.dir") + "/VIPTreeDist/" + DataGenConstant.dataset + "/DM_level_" + l + "_floor_" + DataGenConstant.nFloor + "_dataType_" + DataGenConstant.dataType + "_diType_" + DataGenConstant.divisionType + ".txt";
			try {
				FileWriter fw = new FileWriter(objectsFile);
				fw.write(result);
				fw.flush();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}

		}

	}

	// read DM
	public void readDM() throws IOException {
		for (int l = 1; l <= levels.size(); l++) {
			String objectsFile = System.getProperty("user.dir") + "/VIPTreeDist/" + DataGenConstant.dataset + "/DM_level_" + l + "_floor_" + DataGenConstant.nFloor + "_dataType_" + DataGenConstant.dataType + "_diType_" + DataGenConstant.divisionType + ".txt";

			Path path = Paths.get(objectsFile);
			Scanner scanner = new Scanner(path);

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] tempArr = line.split(" ");
				int nodeId = Integer.parseInt(tempArr[0]);
				Node node = this.getNode(nodeId);
				HashMap<String, String> dm = new HashMap<>();

				for (int i = 1; i < tempArr.length; i++) {
					String keyValue = tempArr[i];
					String[] keyValueArr = keyValue.split("\t");
					String key = keyValueArr[0];
					String value = arrToString(keyValueArr, 1, keyValueArr.length - 1);
					dm.put(key, value);
				}

				node.setDistMatrix(dm);

			}

			System.out.println("level_" + l + " DM is finished");

		}
	}

	// object processing
	public void objectPro() {
		ArrayList<Node> nodes = this.getNodes(1);

		for (int i = 0; i < nodes.size(); i++) {
			Node node = nodes.get(i);
			ArrayList<Integer> pars = node.getmPartitions();
			ArrayList<Integer> accDoors = node.getAccessDoors();

			HashMap<Integer, ArrayList<ArrayList<Double>>> objectDist_obKey = new HashMap<>();
			HashMap<Integer, ArrayList<ArrayList<Double>>> objectDist_accKey = new HashMap<>();

			for (int j = 0; j < pars.size(); j++) {
				int parId = pars.get(j);
				Partition par = IndoorSpace.iPartitions.get(parId);
				ArrayList<Integer> leaveDoors = par.getTopology().getP2DLeave();
				ArrayList<Integer> objects = par.getmObjects();

				for (int k = 0; k < objects.size(); k++) {
					int obId = objects.get(k);
					node.addObject(obId);
					objectDist_obKey.put(obId, getMinDistObjectToAcc(obId, node, leaveDoors, accDoors));
				}
			}
			objectDist_accKey = convertObjectDist(accDoors, objectDist_obKey);
			((LeafNode) node).setObjectDist(objectDist_accKey);

		}

		for (int l = 2; l <= levels.size(); l++) {
			ArrayList<Node> nonLeafnodes = this.getNodes(l);
			for (int i = 0; i < nonLeafnodes.size(); i++) {
				Node nonLeafNode = nonLeafnodes.get(i);
				ArrayList<Integer> children = nonLeafNode.getmChildren();
				for (int j = 0; j < children.size(); j++) {
					nonLeafNode.addAllObjects(this.getNode(children.get(j)).getAllObjects());
				}

			}
		}


	}

	/**
	 * convert object distance
	 * @param accDoors
	 * @param objectDist_obKey
	 * @return
	 */
	public HashMap<Integer, ArrayList<ArrayList<Double>>> convertObjectDist(ArrayList<Integer> accDoors, HashMap<Integer, ArrayList<ArrayList<Double>>> objectDist_obKey) {
		HashMap<Integer, ArrayList<ArrayList<Double>>> objectDist_accKey = new HashMap<>();
		for (int i = 0; i < accDoors.size(); i++) {
			int accDoorId = accDoors.get(i);
			ArrayList<ArrayList<Double>> obDists = new ArrayList<>();
			Iterator iterator = objectDist_obKey.keySet().iterator();
			while (iterator.hasNext()) {
				Object key = iterator.next();
				ArrayList<ArrayList<Double>> value = objectDist_obKey.get(key);
				for (int j = 0; j < value.size(); j++) {
					ArrayList<Double> accDist = value.get(j);
					if ((int) ((double) (accDist.get(0))) == accDoorId) {
						ArrayList<Double> temp = new ArrayList<>();
						temp.add((double) (int) key);
						temp.add(accDist.get(1));
						addToArrayListSorted(temp, obDists);

					}
				}
			}

			objectDist_accKey.put(accDoorId, obDists);
		}
		return objectDist_accKey;
	}

	/**
	 * add object distance to sorted arraylist
	 * @param obDist
	 * @param obDists
	 */
	public void addToArrayListSorted(ArrayList<Double> obDist, ArrayList<ArrayList<Double>> obDists) {
		if (obDists.size() == 0) {
			obDists.add(obDist);
		} else {
			double dist = obDist.get(1);
			for (int i = obDists.size() - 1; i >= 0; i--) {
				double tempDist = obDists.get(i).get(1);
				double tempObjectId = (int) ((double) obDists.get(i).get(0));

				if (dist > tempDist) {
					if (i + 1 == obDists.size()) {
						obDists.add(obDist);
						break;
					} else {
						ArrayList<Double> lastItem = obDists.get(obDists.size() - 1);
						obDists.add(lastItem);
						for (int j = obDists.size() - 2; j > i + 1; j--) {
							ArrayList<Double> tempItem = obDists.get(j - 1);
							obDists.set(j, tempItem);
						}
						obDists.set(i + 1, obDist);
						break;
					}
				}
				if (i == 0) {
					ArrayList<Double> lastItem = obDists.get(obDists.size() - 1);
					obDists.add(lastItem);
					for (int j = obDists.size() - 2; j > 0; j--) {
						ArrayList<Double> tempItem = obDists.get(j - 1);
						obDists.set(j, tempItem);
					}
					obDists.set(0, obDist);
					break;
				}
			}
		}
	}

	/**
	 * get min distance of an object to access doors
	 * @param obId
	 * @param node
	 * @param leaveDoors
	 * @param accDoors
	 * @return
	 */
	public ArrayList<ArrayList<Double>> getMinDistObjectToAcc(int obId, Node node, ArrayList<Integer> leaveDoors, ArrayList<Integer> accDoors) {
		ArrayList<ArrayList<Double>> objectDist = new ArrayList<>();

		for (int i = 0; i < accDoors.size(); i++) {
			int accDoorId = accDoors.get(i);
			double minDist = Constant.large;
			for (int j = 0; j < leaveDoors.size(); j++) {
				double dist = Constant.large;
				int leaveDoorId = leaveDoors.get(j);
//				System.out.println("leavedoor: " + leaveDoorId + "; accessdoor: " + accDoorId);
				double dist1 = distPointObject(IndoorSpace.iDoors.get(leaveDoorId), IndoorSpace.iObject.get(obId));

				if (accDoorId == leaveDoorId) {
					dist = dist1;
				} else {

					double dist2 = Double.parseDouble(node.getDist(leaveDoorId, accDoorId).split("\t")[0]);

					dist = dist1 + dist2;
				}

				if (dist < minDist) {
					minDist = dist;
				}

			}

			objectDist.add(new ArrayList<>(Arrays.asList((double) accDoorId, minDist)));
		}
		return objectDist;
	}

	/**
	 * calculate distance between a point and an Object
	 */
	public double distPointObject(Point point, IndoorObject object) {
		double dist = 0;
		dist = Math.sqrt(Math.pow(point.getX() - object.getObjectX(), 2) + Math.pow(point.getY() - object.getObjectY(), 2));
		return dist;
	}

	/**
	 * calculate distance matrix for VIP-Tree
	 */
	public void constructDMforVIPtree() {
		ArrayList<Node> leafNodes = this.getNodes(1);
		for (int i = 0; i < leafNodes.size(); i++) {
			Node node = leafNodes.get(i);
			HashMap<String, String> result = new HashMap<>();
			ArrayList<Integer> doors = new ArrayList<>();

			doors.addAll(node.getAccessDoors());
			doors.addAll(node.getInternalDoors());
			for (int j = 0; j < doors.size(); j++) {
				int doorId = doors.get(j);
				HashMap<String, String> doorToAncestorDoorDist = doorToAncestorDoorDist(doorId, node, this.root());
				result.putAll(doorToAncestorDoorDist);
			}
			((LeafNode) node).setDistMatrixVIP(result);

		}
	}

	/**
	 * save distance matrix for VIP-Tree
	 */
	public void saveDMforVIPtree() {
		String result = "";

		ArrayList<Node> nodes = this.getNodes(1);
		for (int i = 0; i < nodes.size(); i++) {
			Node node = nodes.get(i);
			result += node.getNodeID() + " ";
			HashMap<String, String> DM = ((LeafNode) node).getDistMatrixVIP();
			Iterator iterator = DM.keySet().iterator();
			while (iterator.hasNext()) {
				Object key = iterator.next();
				String value = DM.get(key);
				result += key + "\t" + value + " ";
			}
			result += "\n";
		}
		String vipFile = System.getProperty("user.dir") + "/VIPTreeDist/" + DataGenConstant.dataset + "/DM_VIP_level_" + 1 + "_floor_" + DataGenConstant.nFloor + "_dataType_" + DataGenConstant.dataType + "_diType_" + DataGenConstant.divisionType + ".txt";
		try {
			FileWriter fw = new FileWriter(vipFile);
			fw.write(result);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	/**
	 * read distance matrix of VIP-Tree
	 * @throws IOException
	 */
	public void readDMforVIPtree() throws IOException {
		String vipFile = System.getProperty("user.dir") + "/VIPTreeDist/" + DataGenConstant.dataset + "/DM_VIP_level_" + 1 + "_floor_" + DataGenConstant.nFloor + "_dataType_" + DataGenConstant.dataType + "_diType_" + DataGenConstant.divisionType + ".txt";

		Path path = Paths.get(vipFile);
		Scanner scanner = new Scanner(path);

		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String[] tempArr = line.split(" ");
			int nodeId = Integer.parseInt(tempArr[0]);
			Node node = this.getNode(nodeId);
			HashMap<String, String> dm = new HashMap<>();

			for (int i = 1; i < tempArr.length; i++) {
				String keyValue = tempArr[i];
				String[] keyValueArr = keyValue.split("\t");
				String key = keyValueArr[0];
				String value = arrToString(keyValueArr, 1, keyValueArr.length - 1);
				dm.put(key, value);
			}

			((LeafNode) node).setDistMatrixVIP(dm);

		}

		System.out.println("level_" + 1 + " VIP DM is finished");

	}

	/**
	 * calculte distance between a door to ancesstor doors
	 * @param doorId
	 * @param sNode
	 * @param n
	 * @return
	 */
	public HashMap<String, String> doorToAncestorDoorDist(int doorId, Node sNode, Node n) {
		Node childNode = sNode;
		Node parentNode = this.getNode(childNode.getParentNodeID());

		HashMap<String, String> result = new HashMap<>();

		if (childNode.getNodeID() == n.getNodeID()) {
			for (int i = 0; i < n.getAccessDoors().size(); i++) {
				int accDoorId = n.getAccessDoors().get(i);
				if (doorId == accDoorId) {
					result.put(doorId + "-" + accDoorId, 0 + "\t");
				} else {
					result.put(doorId + "-" + accDoorId, n.getDist(doorId, accDoorId));
				}
			}
		}

		HashMap<String, String> doorToChildNode = new HashMap<>();

		ArrayList<Integer> accDoors = childNode.getAccessDoors();
		for (int i = 0; i < accDoors.size(); i++) {
			int accDoorId = accDoors.get(i);
			if (accDoorId == doorId) {
				doorToChildNode.put(doorId + "-" + accDoorId, 0 + "\t" + accDoorId + "\t");
			} else {
				doorToChildNode.put(doorId + "-" + accDoorId, childNode.getDist(doorId, accDoorId));
			}
		}


		while (childNode.getNodeID() != n.getNodeID()) {
//			System.out.println("childNode: " + sNode.toString());
//			System.out.println("parentNode: " + parentNode.toString());

			HashMap<String, String> doorToParNode = new HashMap<>();
			ArrayList<Integer> parentAccDoors = parentNode.getAccessDoors();
			ArrayList<Integer> childAccDoors = childNode.getAccessDoors();
			Boolean[] isVisited = new Boolean[parentAccDoors.size()];

			for (int i = 0; i < parentAccDoors.size(); i++) {
				int parAccDoorId = parentAccDoors.get(i);
//				System.out.println("parAccDoorId: " + parAccDoorId);
				double minDist = Constant.large;
				String minPath = "";
				for (int j = 0; j < childAccDoors.size(); j++) {
					double dist = Constant.large;
					String path = "";

					int childAccDoorId = childAccDoors.get(j);
//					System.out.println("childAccDoorId: " + childAccDoorId);
					String doorToChildDoorStr = doorToChildNode.get(doorId + "-" + childAccDoorId);
//					System.out.println("doorToChildDoorStr: " + doorToChildDoorStr);

//                    if (parAccDoorId == childAccDoorId) {
//                        pointToParNode.put(parAccDoorId, pointToChildDoorStr);
//                        continue;
//                    }

					String[] doorToChildDoorArr = doorToChildDoorStr.split("\t");
					double dist1 = Double.parseDouble(doorToChildDoorArr[0]);
					String path1 = arrToString(doorToChildDoorArr, 1, doorToChildDoorArr.length - 1);

					if (parAccDoorId == childAccDoorId) {
						dist = dist1;
						path = path1;
					} else {

						String childDoorToParentDoorStr = parentNode.getDist(childAccDoorId, parAccDoorId);
						String[] childDoorToParentDoorArr = childDoorToParentDoorStr.split("\t");
						double dist2 = Double.parseDouble(childDoorToParentDoorArr[0]);
						String path2 = arrToString(childDoorToParentDoorArr, 2, childDoorToParentDoorArr.length - 1);

						dist = dist1 + dist2;
						path = path1 + path2;
					}

					if (dist < minDist) {
						minDist = dist;
						minPath = path;

					}
				}
				doorToParNode.put(doorId + "-" + parAccDoorId, minDist + "\t" + minPath);
				doorToParNode.put(parAccDoorId + "-" + doorId, convertPath(minDist + "\t" + minPath));

			}
			result.putAll(doorToParNode);
			childNode = parentNode;
			parentNode = this.getNode(parentNode.getParentNodeID());
			doorToChildNode = doorToParNode;

		}

		return result;
	}


}
