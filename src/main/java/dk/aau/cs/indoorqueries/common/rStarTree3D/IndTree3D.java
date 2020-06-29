/**
 *
 */
package dk.aau.cs.indoorqueries.common.rStarTree3D;

import com.github.davidmoten.rtree3d.Entry;
import com.github.davidmoten.rtree3d.RTree;
import com.github.davidmoten.rtree3d.geometry.Box;
import com.github.davidmoten.rtree3d.geometry.Point;
import rx.Observable;

import java.io.IOException;
import java.util.ArrayList;

/**
 * <h>IndTree3D</h>
 * represent the 3d indR-tree
 *
 * @author feng zijin
 *
 */
public class IndTree3D {

	private RTree<Integer, Box> tree;
	private final Box bounds = Box.create(0.0, 0.0, 0.0, 10000.0, 10000.0, 2.0);
	private int maxChildren = 4;        // the default maxChildren is 4

	/**
	 * Constructor
	 */
	public IndTree3D(int maxChildren) throws IOException {
		tree = RTree.star().minChildren((maxChildren) / 2)
				.maxChildren(maxChildren).bounds(bounds).create();
	}

	/**
	 *
	 */
	public void add(TreeNode mNode) {
		// create the box
		Box box = Box.create(mNode.x1(), mNode.y1(), mNode.z1(), mNode.x2(), mNode.y2(), mNode.z2());

		// put TreeNode into the box
		box.setmNode(mNode);

//		System.out.println("Box " + box.cornerToString() + " created");
//		System.out.println("Node " + mNode.cornerToString() + " id = " + mNode.getmID());
//		System.out.println("Partition " + mNode.getmPartition().cornerToString() + " id = " + mNode.getmPartition().getmID());

		// add box to the tree
		tree = tree.add(mNode.getmPartition().getmID(), box);
	}

	/**
	 * @param point
	 * @return the treeNode that the point is locate on
	 *
	 */
	public ArrayList<TreeNode> search(Point point) {
		ArrayList<TreeNode> result = new ArrayList<TreeNode>();

		Observable<Entry<Integer, Box>> entries = tree.search(point);

		entries.subscribe(
				e -> result.add(e.geometry().getmNode())
		);

		return result;
	}

	/**
	 * @param box
	 * @return the treeNode that the box is cover
	 *
	 */
	public ArrayList<TreeNode> search(Box box) {
		ArrayList<TreeNode> result = new ArrayList<TreeNode>();

		Observable<Entry<Integer, Box>> entries = tree.search(box);

		entries.subscribe(
				e -> result.add(e.geometry().getmNode())
		);

		return result;
	}

	/**
	 * @return the size of the tree
	 *
	 */
	public int size() {
		return tree.size();
	}


}
