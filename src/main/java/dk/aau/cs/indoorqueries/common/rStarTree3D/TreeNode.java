/**
 *
 */
package dk.aau.cs.indoorqueries.common.rStarTree3D;

import dk.aau.cs.indoorqueries.common.iDModel.DistMatrix;
import dk.aau.cs.indoorqueries.common.iDModel.Topology;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Partition;

import java.io.IOException;

/**
 * @author feng zijin
 *
 */
public class TreeNode {
	private double x1, y1, x2, y2, z1, z2;        // the corner of the node

	private int mID;        // the mID of the node
	private Partition mPartition;        // the partition stored in the node
	private DistMatrix mDistMatrix;        // the distance matrix of the partition
	private Topology mTopology;        // the connectivity tier of the partition


	/**
	 * constructor
	 */
	public TreeNode(double x1, double y1, double z1, double x2, double y2, double z2,
					Partition mPartition, DistMatrix mDistMatrix, Topology mTopology) {
		this.x1 = x1;
		this.y1 = y1;
		this.z1 = z1;
		this.x2 = x2;
		this.y2 = y2;
		this.z2 = z2;
		this.mID = mPartition.getmID();
		this.mPartition = mPartition;
		this.mDistMatrix = mDistMatrix;
		this.mTopology = mTopology;
	}

	public double x1() {
		return x1;
	}

	public double y1() {
		return y1;
	}

	public double x2() {
		return x2;
	}

	public double y2() {
		return y2;
	}

	public double z1() {
		return z1;
	}

	public double z2() {
		return z2;
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
	 * @return the partition
	 */
	public Partition getmPartition() {
		return mPartition;
	}

	/**
	 * @param mPartition
	 *            the mPartition to set
	 */
	public void setmPartition(Partition mPartition) {
		this.mPartition = mPartition;
	}

	/**
	 * @return the mDistMatrix
	 */
	public DistMatrix getmDistMatrix() {
		return mDistMatrix;
	}

	/**
	 * @param mDistMatrix
	 *            the mDistMatrix to set
	 */
	public void setmDistMatrix(DistMatrix mDistMatrix) {
		this.mDistMatrix = mDistMatrix;
	}

	/**
	 * @return the mTopology
	 */
	public Topology getmTopology() {
		return mTopology;
	}

	/**
	 * @param mTopology
	 *            the mTopology to set
	 */
	public void setmTopology(Topology mTopology) {
		this.mTopology = mTopology;
	}

	public String cornerToString() {
		return "(" + x1 + ", " + y1 + ", " + z1 + "), (" + x2 + ", " + y2 + ", " + z2 + ")";
	}

	public static void main(String[] arg) throws IOException {

	}
}
