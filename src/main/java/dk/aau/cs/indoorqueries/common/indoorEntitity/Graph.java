/**
 *
 */
package dk.aau.cs.indoorqueries.common.indoorEntitity;

import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;


/**
 * <h>Graph</h>
 *
 * @author feng zijin
 *
 */
public class Graph {
    public static SortedMap<Integer, Door> Doors = new TreeMap<Integer, Door>();    // the Graph doors

    public static SortedMap<Integer, Partition> Partitions = new TreeMap<Integer, Partition>();        // the Graph Partition

    public static SortedMap<Integer, Floor> Floors = new TreeMap<Integer, Floor>();        // the Floor

    public static SortedMap<Integer, Shop> Shops = new TreeMap<Integer, Shop>();        // the Graph Partition

    /**
     * delete the door from the graph by doorID
     *
     * @param doorID
     * @return False as failure
     */
    public static boolean delNode(int doorID) {
        Iterator<Integer> itr = Doors.keySet().iterator();
        while (itr.hasNext()) {
            int index = itr.next();
            if (doorID == index) {
                Doors.remove(index);
                return true;
            }
        }
        return false;
    }

    /**
     * get the door from the graph by doorID
     *
     * @param doorID
     * @return the found door
     */
    public static Door findNode(int doorID) {
        Door findDoor = null;
        Iterator<Integer> itr = Doors.keySet().iterator();
        while (itr.hasNext()) {
            int index = itr.next();
            if (doorID == index) {
                findDoor = Doors.get(index);
                break;
            }
        }
        return findDoor;
    }

    /**
     * delete the partition from the graph by parID
     *
     * @param parID
     * @return False as failure
     */
    public static boolean delPar(int parID) {
        Iterator<Integer> itr = Partitions.keySet().iterator();
        while (itr.hasNext()) {
            int index = itr.next();
            if (parID == index) {
                Partitions.remove(index);
                return true;
            }
        }
        return false;
    }

    /**
     * get the partition from the graph by parID
     * @param parID
     * @return the found partition
     */
    public static Partition findPar(int parID) {
        Partition findPar = null;
        Iterator<Integer> itr = Partitions.keySet().iterator();
        while (itr.hasNext()) {
            int index = itr.next();
            if (parID == index) {
                findPar = Partitions.get(index);
                break;
            }
        }
        return findPar;
    }
}
