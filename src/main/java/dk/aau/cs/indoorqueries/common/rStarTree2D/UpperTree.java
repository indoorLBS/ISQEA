/**
 *
 */
package dk.aau.cs.indoorqueries.common.rStarTree2D;

import com.github.davidmoten.rtree.geometry.Geometries;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Floor;
import dk.aau.cs.indoorqueries.common.indoorEntitity.IndoorSpace;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Partition;

/**
 * <h> UpperTree </h>
 * is the upper tree that make use of the BTree
 *
 * @author feng zijin
 *
 */
public class UpperTree {
	public BTree<Integer, UpperNode> tree;        // node id and floor

	/**
	 * constructor
	 *
	 */
	public UpperTree() {
		tree = new BTree<Integer, UpperNode>();
	}

	/**
	 * load all floor into the tree
	 *
	 */
	public void loadData() {
		int floorSize = IndoorSpace.iFloors.size();
		for (int i = 0; i < floorSize; i++) {
			Floor floor = IndoorSpace.iFloors.get(i);
			tree.add(floor.getmID(), new UpperNode(floor.getX1(), floor.getX2(), floor.getY1(),
					floor.getY2(), floor.getmID()));
		}
	}

	/**
	 * @return the size of the tree
	 *
	 */
	public int size() {
		return tree.size();
	}

	/**
	 * @return the height of the tree
	 *
	 */
	public int height() {
		return tree.height();
	}

	/**
	 * @return the root of the tree
	 *
	 */
	public SuperNode root() {
		BTree.Node node = (BTree.Node) tree.root();
		return node;
	}

	/**
	 * Returns the value associated with the given key.
	 *
	 * @param  key the key
	 * @return the value associated with the given key if the key is in the symbol table
	 *         and {@code null} if the key is not in the symbol table
	 * @throws IllegalArgumentException if {@code key} is {@code null}
	 */
	public UpperNode get(int key) {
		if (key < 0) throw new IllegalArgumentException("argument to get() is less than 0");
		return this.tree.get(key);
	}

	/**
	 * Inserts the key-value pair into the symbol table, overwriting the old value
	 * with the new value if the key is already in the symbol table.
	 * If the value is {@code null}, this effectively deletes the key from the symbol table.
	 *
	 * @param  key the key
	 * @param  val the value
	 * @throws IllegalArgumentException if {@code key} is {@code null}
	 */
	public void put(Integer key, UpperNode val) {
		tree.add(key, val);
	}

	/**
	 * extract the lowerNode in the tree that store the partition
	 * @param parmID of a partition
	 * @return a partition
	 */
	public LowerNode extractLowerNode(int parmID) {
		LowerNode result = null;

		Partition partition = IndoorSpace.iPartitions.get(parmID);

		result = tree.get(partition.getmFloor()).getLowerTree().search(
				Geometries.point(partition.getcenterX(), partition.getcenterY())).get(0);

		if (parmID != result.getmID() && result.getmID() != result.getmPartition()) {
			System.out.println("something wrong_UpperTree_extract");
		}

		return result;
	}

	/**
	 * extract the partition according to the partition id
	 * @param parmID of a partition
	 * @return a partition
	 */
	public Partition extractPartition(int parmID) {
		Partition result = null;

		Partition partition = IndoorSpace.iPartitions.get(parmID);

		result = tree.get(partition.getmFloor()).getLowerTree().search(
				Geometries.point(partition.getcenterX(), partition.getcenterY())).get(0).getpartition();

		if (parmID != result.getmID()) {
			System.out.println("something wrong_UpperTree_extract");
		}

		return result;
	}
}
