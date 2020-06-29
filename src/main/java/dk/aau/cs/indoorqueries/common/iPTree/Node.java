package dk.aau.cs.indoorqueries.common.iPTree;

import dk.aau.cs.indoorqueries.common.indoorEntitity.Point;

import java.util.ArrayList;
import java.util.HashMap;

public interface Node extends Comparable<Node> {


    public int getNodeID();

    public void setNodeID(int ID);

    public void setDegree(int degree);

    public int getDegree();

    public void setHeight(int height);

    public int getHeight();

    public void setParentNodeID(int parentNodeID);

    public int getParentNodeID();

    public void addAdjacentNode(int nodeID);

    public int getAdjacentNodeNo();

    public ArrayList<Integer> getAdjacentNodes();

    public void setPartitions(ArrayList<Integer> partitions);

    public void addPartition(int mID);

    public ArrayList<Integer> getmPartitions();

    public void addAccessDoor(int doorID);

    public void removeAccessDoor(int doorID);

    public void setAccessDoors(ArrayList<Integer> accessDoors);

    public ArrayList<Integer> getAccessDoors();

    public void addInternalDoor(int doorID);

    public void setInternalDoors(ArrayList<Integer> internalDoors);

    public ArrayList<Integer> getInternalDoors();

    public boolean hasCrucial();

    public void setHasCrucial();

    public String getType();

    // toString short
    public String toStringShort();

    public int compareTo(Node another);

    public void addChildren(int nodeID);

    public ArrayList<Integer> getmChildren();

    public int getChildrenSize();

    public void emptymChildren();

    public double getX1();

    public double getY1();

    public double getZ1();

    public double getX2();

    public double getY2();

    public double getZ2();

    public String getBoundary();

    public boolean isInNode(Point point);

    public String getDist(int d1, int d2);

    public HashMap<String, String> getDistMatrix();

    public void setDistMatrix(HashMap<String, String> DM);

    public void addObject(int obId);

    public void addAllObjects(ArrayList<Integer> objects);

    public ArrayList<Integer> getAllObjects();

    public void setObjectDist(HashMap<Integer, ArrayList<ArrayList<Double>>> objectDist);


//	public void constructDistCube(int level, boolean isRead);

//	public DistanceCube getDistanceCube();

}
