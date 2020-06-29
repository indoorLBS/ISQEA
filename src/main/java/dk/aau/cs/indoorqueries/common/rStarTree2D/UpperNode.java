/**
 *
 */
package dk.aau.cs.indoorqueries.common.rStarTree2D;

import dk.aau.cs.indoorqueries.common.indoorEntitity.IndoorSpace;

import java.util.ArrayList;

/**
 * @author feng zijin
 *
 */
public class UpperNode implements SuperNode {
	private double x1, x2, y1, y2;
	private int mFloor;
	private LowerTree lowerTree;
	private ArrayList<Integer> mPartitions = new ArrayList<Integer>();
	;
	private int[] mKeywords = new int[0];

	// status of the sub-tree, 1 - candidate, 0 - not candidate
	private int status = 0;

	public UpperNode(double x1, double x2, double y1, double y2, int mFloor) {
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
		this.mFloor = mFloor;
		this.mPartitions = (ArrayList<Integer>) IndoorSpace.iFloors.get(mFloor).getmPartitions();
		this.mKeywords = IndoorSpace.iFloors.get(mFloor).getmKeywords();

		createLowerTree();
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
	 * @return the lowerTree
	 */
	public LowerTree getLowerTree() {
		return lowerTree;
	}

	/**
	 * @param lowerTree
	 *            the lowerTree to set
	 */
	public void setLowerTree(LowerTree lowerTree) {
		this.lowerTree = lowerTree;
	}

	/**
	 * @return the mPartitions
	 */
	public ArrayList<Integer> getmPartitions() {
		return mPartitions;
	}

	/**
	 * add all partitions to this floor 
	 *
	 * @param mPartitions
	 *            the mPartitions to set
	 */
	public void setPartitions(ArrayList<Integer> mPartitions) {
		this.mPartitions = mPartitions;
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
	 * @return the x1
	 */
	public double getX1() {
		return x1;
	}

	/**
	 * @param x1
	 *            the x1 to set
	 */
	public void setX1(double x1) {
		this.x1 = x1;
	}

	/**
	 * @return the x2
	 */
	public double getX2() {
		return x2;
	}

	/**
	 * @param x2
	 *            the x2 to set
	 */
	public void setX2(double x2) {
		this.x2 = x2;
	}

	/**
	 * @return the y1
	 */
	public double getY1() {
		return y1;
	}

	/**
	 * @param y1
	 *            the y1 to set
	 */
	public void setY1(double y1) {
		this.y1 = y1;
	}

	/**
	 * @return the y2
	 */
	public double getY2() {
		return y2;
	}

	/**
	 * @param y2
	 *            the y2 to set
	 */
	public void setY2(double y2) {
		this.y2 = y2;
	}

	/**
	 * create and connect a lower tree
	 *
	 */
	public void createLowerTree() {
		this.lowerTree = new LowerTree(this.mFloor);
	}
}
