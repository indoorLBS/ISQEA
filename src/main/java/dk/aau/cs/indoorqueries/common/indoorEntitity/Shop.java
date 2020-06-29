/**
 *
 */
package dk.aau.cs.indoorqueries.common.indoorEntitity;

/**
 * <h>Shop</h>
 * represent shop
 * @author feng zijin
 *
 */
public class Shop {
	private int originalmID;
	private int mID;
	private String mName;
	private String mOpening_hours;
	private String mImg;
	private String mUrl;
	private String mPhone;
	private String mWebsite;
	private String mDescription;

	private Partition mPartition;
	private int mFloor;

	/**
	 * Constructor 
	 *
	 */
	public Shop(int originalmID, String mName, String mOpening_hours, String mImg,
				String mUrl, String mPhone, String mWebsite, String mDescription) {
		this.originalmID = originalmID;
		this.mName = mName;
		this.mOpening_hours = mOpening_hours;
		this.mImg = mImg;
		this.mUrl = mUrl;
		this.mPhone = mPhone;
		this.mWebsite = mWebsite;
		this.mDescription = mDescription;
		this.mID = 0;
		this.mPartition = null;
		this.mFloor = 0;

//		System.out.println("Shop generated " + this.mID + " " + this.mName);
	}

	/**
	 * Constructor
	 *
	 * @param another
	 */
	public Shop(Shop another) {
		this.setoriginalmID(another.getoriginalmID());
		this.setmName(another.getmName());
		this.setmOpening_hours(another.getmOpening_hours());
		this.setmImg(another.getmImg());
		this.setmUrl(another.getmUrl());
		this.setmPhone(another.getmPhone());
		this.setmWebsite(another.getmWebsite());
		this.setmDescription(another.getmDescription());
		this.setmFloor(another.getmFloor());
		this.mID = 0;
		this.mPartition = null;
	}

	/**
	 * @return the originalmID
	 */
	public int getoriginalmID() {
		return originalmID;
	}

	/**
	 * @param originalmID
	 *            the originalmID to set
	 */
	public void setoriginalmID(int originalmID) {
		this.originalmID = originalmID;
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
	 * @return the mName
	 */
	public String getmName() {
		return mName;
	}

	/**
	 * @param mName
	 *            the mName to set
	 */
	public void setmName(String mName) {
		this.mName = mName;
	}

	/**
	 * @return the mOpening_hours
	 */
	public String getmOpening_hours() {
		return mOpening_hours;
	}

	/**
	 * @param mOpening_hours
	 *            the mOpening_hours to set
	 */
	public void setmOpening_hours(String mOpening_hours) {
		this.mOpening_hours = mOpening_hours;
	}

	/**
	 * @return the mImg
	 */
	public String getmImg() {
		return mImg;
	}

	/**
	 * @param mImg
	 *            the mImg to set
	 */
	public void setmImg(String mImg) {
		this.mImg = mImg;
	}

	/**
	 * @return the mUrl
	 */
	public String getmUrl() {
		return mUrl;
	}

	/**
	 * @param mUrl
	 *            the mUrl to set
	 */
	public void setmUrl(String mUrl) {
		this.mUrl = mUrl;
	}

	/**
	 * @return the mPhone
	 */
	public String getmPhone() {
		return mPhone;
	}

	/**
	 * @param mPhone
	 *            the mPhone to set
	 */
	public void setmPhone(String mPhone) {
		this.mPhone = mPhone;
	}

	/**
	 * @return the mWebsite
	 */
	public String getmWebsite() {
		return mWebsite;
	}

	/**
	 * @param mWebsite
	 *            the mWebsite to set
	 */
	public void setmWebsite(String mWebsite) {
		this.mWebsite = mWebsite;
	}

	/**
	 * @return the mDescription
	 */
	public String getmDescription() {
		return mDescription;
	}

	/**
	 * @param mDescription
	 *            the mDescription to set
	 */
	public void setmDescription(String mDescription) {
		this.mDescription = mDescription;
	}

	/**
	 * @return the mPartition
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
	 * toString
	 *
	 * @return mID+moriginalID+mPartitionID+mFloor+mName+mOpening_hours+mImg+
	mUrl+mPhone+mWebsite+mDescription
	 */
	public String toString() {
		String outputString = this.getmID() + "\t" + this.getoriginalmID() + "\t"
				+ this.getmPartition().getmID() + "\t" + this.getmFloor() + "\t" + this.getmName() + "\t"
				+ this.getmOpening_hours() + "\t" + this.getmImg() + " \t" + this.getmUrl() + " \t" + this.getmPhone() + ""
				+ " \t" + this.getmWebsite() + " \t" + this.getmDescription();

		return outputString;
	}


}
