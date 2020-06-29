package dk.aau.cs.indoorqueries.common.indoorEntitity;

import dk.aau.cs.indoorqueries.common.utilities.DataGenConstant;
import dk.aau.cs.indoorqueries.common.utilities.RoomType;

import java.util.ArrayList;

/**
 * IndoorObject in indoor spaces
 *
 * @author Tiantian Liu
 */
public class IndoorObject {
    private int objectId;
    private int objectX;
    private int objectY;
    private int oFloor;
    private int oType; // 0 is static, 1 is moving
    private int parId; // the ID of partition in which the object is

    /**
     * construct static object
     *
     * @param x
     * @param y
     * @param floor
     */
    public IndoorObject(int x, int y, int floor) {
        this.objectId = DataGenConstant.mID_Object++;
        this.objectX = x;
        this.objectY = y;
        this.oFloor = floor;
        this.oType = 0;

    }

    /**
     * Construct object
     *
     * @param objectId
     * @param x
     * @param y
     * @param floor
     * @param parId
     */

    public IndoorObject(int objectId, int x, int y, int floor, int parId) {
        this.objectId = objectId;
        this.objectX = x;
        this.objectY = y;
        this.oFloor = floor;
        this.parId = parId;
        this.oType = 0;
    }

    public int getObjectId() {
        return objectId;
    }

    public int getObjectX() {
        return objectX;
    }

    public int getObjectY() {
        return objectY;
    }

    public int getoFloor() {
        return oFloor;
    }

    public int getParId() {
        return parId;
    }

    public void setParId(int parId) {
        this.parId = parId;
    }

    /**
     * find partition in which the object is
     *
     * @param ob
     */

    public void findParIdSYN(IndoorObject ob) {
        int x = ob.getObjectX();
        int y = ob.getObjectY();
        int floor = ob.getoFloor();
        ArrayList<Integer> partitions = IndoorSpace.iFloors.get(floor).getmPartitions();
        for (int i = 0; i < partitions.size(); i++) {
            int tempParId = partitions.get(i);
            Partition par = IndoorSpace.iPartitions.get(tempParId);
            if ((x >= par.getX1() && x <= par.getX2()) || (x <= par.getX1() && x >= par.getX2())) {
                if ((y >= par.getY1() && y <= par.getY2()) || (y <= par.getY1() && y >= par.getY2())) {
                    ob.setParId(tempParId);
                    par.addObject(ob.getObjectId());
                    break;
                }
            }
        }
    }

    /**
     * find partition in which the object is
     *
     * @param ob
     */

    public int findParId(IndoorObject ob) {
        int parId = -1;
        int x = ob.getObjectX();
        int y = ob.getObjectY();
        int floor = ob.getoFloor();
        ArrayList<Integer> partitions = IndoorSpace.iFloors.get(floor).getmPartitions();
//        System.out.println("mPartitions: " + partitions);
        for (int i = 0; i < partitions.size(); i++) {
            int tempParId = partitions.get(i);
            Partition par = IndoorSpace.iPartitions.get(tempParId);
            if (par.getmType() == RoomType.STAIRCASE || par.getmType() == RoomType.HALLWAY) continue;
            if ((x >= par.getX1() && x <= par.getX2()) || (x <= par.getX1() && x >= par.getX2())) {
                if ((y >= par.getY1() && y <= par.getY2()) || (y <= par.getY1() && y >= par.getY2())) {
                    ob.setParId(tempParId);
                    par.addObject(ob.getObjectId());
                    parId = tempParId;
                    return parId;
                }
            }
        }
        return parId;
    }

    public void creatObject() {

    }

    public void isExist(int objectX, int objectY) {

    }
}
