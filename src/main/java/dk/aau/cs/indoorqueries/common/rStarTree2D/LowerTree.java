/**
 *
 */
package dk.aau.cs.indoorqueries.common.rStarTree2D;

import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.RTree;
import com.github.davidmoten.rtree.geometry.*;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Floor;
import dk.aau.cs.indoorqueries.common.indoorEntitity.IndoorSpace;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Partition;
import dk.aau.cs.indoorqueries.common.utilities.DataGenConstant;
import rx.Observable;

import java.util.ArrayList;

/**
 * <h>LowerTree</h>
 * is the lower tree that make use of the R-tree
 *
 * @author feng zijin
 *
 */
public class LowerTree {
	private UpperNode upperNode;

	private RTree<LowerNode, Geometry> tree;
	private int maxChildNum = DataGenConstant.maxChildLower;
	private int mFloor = 0;        // the partitions of the floor stored

	/**
	 * constructor
	 *
	 */
	public LowerTree(int mFloor) {
		this.mFloor = mFloor;
		this.tree = RTree.star().maxChildren(maxChildNum).create();

		loadData();
	}

	/**
	 * load all floor into the tree
	 *
	 */
	public void loadData() {
		Floor floor = IndoorSpace.iFloors.get(mFloor);

		int partitionSize = floor.getmPartitions().size();
		for (int i = 0; i < partitionSize; i++) {
			Partition partition = IndoorSpace.iPartitions.get(floor.getmPartitions().get(i));

			LowerNode node = new LowerNode(partition.getX1(), partition.getX2(), partition.getY1(), partition.getY2(),
					partition.getmID(), partition.getdistMatrix(), partition.getTopology());

			tree = tree.add(node, Geometries.rectangle(partition.getX1(), partition.getY1()
					, partition.getX2(), partition.getY2()));
		}
	}

	/**
	 * @return the upperNode
	 */
	public UpperNode getUpperNode() {
		return upperNode;
	}

	/**
	 * @param upperNode
	 *            the upperNode to set
	 */
	public void setUpperNode(UpperNode upperNode) {
		this.upperNode = upperNode;
	}

	/**
	 * @return the mFloor
	 */
	public int getmFloor() {
		return mFloor;
	}

	/**
	 * @param mFloor
	 *            the mFloor to set
	 */
	public void setmFloor(int mFloor) {
		this.mFloor = mFloor;
	}

	/**
	 * @return the size of the tree
	 *
	 */
	public int size() {
		return this.tree.size();
	}

	/**
	 * @return the height of the tree
	 *
	 */
	public int height() {
		return this.tree.calculateDepth();
	}

	/**
	 * add a new node to the tree using node and geometry
	 * @param node
	 * @para geometry
	 */
	public void add(LowerNode node, Geometry geometry) {
		this.tree = tree.add(node, geometry);
	}

	/**
	 * add a new node to the tree using node and partition
	 * @param node
	 * @para partition
	 */
	public void add(LowerNode node, Partition partition) {
		this.tree = tree.add(node, Geometries.rectangle(partition.getX1(), partition.getY1()
				, partition.getX2(), partition.getY2()));
	}

	/**
	 * search for items in a rectangle region
	 * @param rectangle
	 * @return a list of lowerNode
	 */
	public ArrayList<LowerNode> search(Rectangle rectangle) {
		ArrayList<LowerNode> result = new ArrayList<LowerNode>();

		Observable<Entry<LowerNode, Geometry>> entries = tree.search(rectangle);

		entries.subscribe(
				e -> result.add(e.value())
		);

		return result;
	}

	/**
	 * search for items in a circle region
	 */
	public ArrayList<LowerNode> search(Circle circle) {
		ArrayList<LowerNode> result = new ArrayList<LowerNode>();

		Observable<Entry<LowerNode, Geometry>> entries = tree.search(circle);

		entries.subscribe(
				e -> result.add(e.value())
		);

		return result;
	}

	/**
	 * search for items with a point
	 * @param point
	 * @return a list of lowerNode
	 */
	public ArrayList<LowerNode> search(Point point) {
		ArrayList<LowerNode> result = new ArrayList<LowerNode>();

		Observable<Entry<LowerNode, Geometry>> entries = tree.search(point);

		entries.subscribe(
				e -> result.add(e.value())
		);

		return result;
	}


}
