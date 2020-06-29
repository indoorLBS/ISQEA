/**
 *
 */
package dk.aau.cs.indoorqueries.common.rStarTree2D;

import dk.aau.cs.indoorqueries.common.iDModel.DistMatrix;
import dk.aau.cs.indoorqueries.common.iDModel.Topology;
import dk.aau.cs.indoorqueries.common.indoorEntitity.IndoorSpace;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Partition;
import dk.aau.cs.indoorqueries.common.utilities.DataGenConstant;

import java.util.Comparator;

/**
 * <h> Block </h>
 * represent the block that will be put into the Rectangle
 *
 * @author feng zijin
 *
 */
public class LowerNode implements Comparator<Object>, SuperNode {
	private double x1, y1, x2, y2;        // the corner of the node

	private int mID;        // the mID of the node
	private int mPartition;        // the partition stored in the node
	private DistMatrix mDistMatrix;        // the distance matrix of the partition
	private Topology mTopology;        // the connectivity tier of the partition
	private int[] mKeywords = new int[0];

	// status of the sub-tree, 1 - candidate, 0 - not candidate
	private int status = 0;

	private Partition partition;

	/**
	 * constructor
	 * @param x1
	 * @param x2
	 * @param y1
	 * @param y2
	 */
	public LowerNode(double x1, double x2, double y1, double y2,
					 int mPartition, DistMatrix mDistMatrix, Topology mTopology) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.mID = mPartition;
		this.mPartition = mPartition;
		this.mDistMatrix = mDistMatrix;
		this.mTopology = mTopology;
		this.mKeywords = new int[DataGenConstant.mKeyworSize];

		this.partition = IndoorSpace.iPartitions.get(this.mPartition);

		// update mKeywordArr
		updatemKeywords();
	}

	public double getX1() {
		return x1;
	}

	public double getY1() {
		return y1;
	}

	public double getX2() {
		return x2;
	}

	public double getY2() {
		return y2;
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
	public int getmPartition() {
		return mPartition;
	}

	/**
	 * @param mPartition
	 *            the mPartition to set
	 */
	public void setmPartition(int mPartition) {
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
	 * @return the status
	 */
	public double getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * update keyword array according the the partition's keyword
	 */
	public void updatemKeywords() {
		this.mKeywords = new int[DataGenConstant.mKeyworSize];
//		mKeywords = null;

		int keywordSize = mKeywords.length;
		for (int i = 0; i < keywordSize; i++) {
			if (this.mKeywords[i] >= 2 || this.mKeywords[i] < 0)
				System.out.println("something wrong_Block_updatemKeywordArr");
		}
	}

	public String cornerToString() {
		return "(" + x1 + ", " + y1 + "), (" + x2 + ", " + y2 + ")";
	}

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.IndoorObject, java.lang.IndoorObject)
	 */
	@Override
	public int compare(Object arg0, Object arg1) {
		// TODO Auto-generated method stub
		LowerNode blockOne = (LowerNode) arg0;
		LowerNode blockTwo = (LowerNode) arg1;
		return blockOne.getmID() > blockTwo.getmID() ? 1 : (blockOne.getmID() == blockTwo.getmID() ? 0
				: -1);
	}

	/**
	 * @return the partition
	 */
	public Partition getpartition() {
		return partition;
	}

	/**
	 * @param partition
	 *            the partition to set
	 */
	public void setpartition(Partition partition) {
		this.partition = partition;
	}

}
