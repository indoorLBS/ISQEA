/**
 *
 */
package dk.aau.cs.indoorqueries.common.iDModel;

/**
 * @author feng zijin
 *
 */
public class TopologyElement {
	private int mDoorID1 = -1;                // the through door to touch one partition

	private int mDoorID2 = -1;        // for floor object

	private int mParID;                    // the direct-touchable partition

	private boolean isPartition;

	private boolean isIn;        // true - in, false - out

	/**
	 * Constructor
	 *
	 */
	public TopologyElement(int mDoorID1, int mParID, boolean isPartition, boolean isIn) {
		if (!isPartition) {
			System.out.println("something wrong_ConnectivityElement_ConnectivityElement_1");
			return;
		}
		this.isPartition = isPartition;
		this.mDoorID1 = mDoorID1;
		this.mParID = mParID;
		this.isIn = isIn;
	}

	/**
	 * Constructor
	 *
	 */
	public TopologyElement(int mDoorID1, int mDoorID2, int mParID, boolean isPartition, boolean isIn) {
		if (isPartition) {
			System.out.println("something wrong_ConnectivityElement_ConnectivityElement_2");
			return;
		}
		this.isPartition = isPartition;
		this.mDoorID1 = mDoorID1;
		this.mDoorID2 = mDoorID2;
		this.mParID = mParID;
		this.isIn = isIn;
	}

	/**
	 * @return the mDoorID1
	 */
	public int getmDoorID1() {
		return mDoorID1;
	}

	/**
	 * @param mDoorID1 the mDoorID1 to set
	 */
	public void setmDoorID1(int mDoorID1) {
		this.mDoorID1 = mDoorID1;
	}

	/**
	 * @return the mDoorID2
	 */
	public int getmDoorID2() {
		return mDoorID2;
	}

	/**
	 * @param mDoorID2 the mDoorID2 to set
	 */
	public void setmDoorID2(int mDoorID2) {
		this.mDoorID2 = mDoorID2;
	}

	/**
	 * @return the mParID, the direct-touchable partition
	 */
	public int getmParID() {
		return mParID;
	}

	/**
	 * @param mParID the mParID to set
	 */
	public void setmParID(int mParID) {
		this.mParID = mParID;
	}

	/**
	 * @return the isPartition
	 */
	public boolean getIsPartition() {
		return isPartition;
	}

	/**
	 * @return the isIn
	 */
	public boolean getIsIn() {
		return isIn;
	}
}
