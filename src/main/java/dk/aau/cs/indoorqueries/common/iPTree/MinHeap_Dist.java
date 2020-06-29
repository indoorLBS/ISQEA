package dk.aau.cs.indoorqueries.common.iPTree;

import java.util.ArrayList;

/**
 * @author feng zijin
 */
public class MinHeap_Dist<T> {

//	int size;
//	T[] heapArray;
//	int[] idArray;
//	public int heapSize;

    int size = 200;
    Dist[] heapArray;
    public int heapSize;

    public MinHeap_Dist() {
        heapArray = new Dist[size];
        heapSize = 0;
    }

    public Dist getParent(int index) {
        return heapArray[(index - 1) / 2];
    }

    public void insert(Dist element) {
        if (heapSize >= size) {
            size = size * 2;
            Dist[] temp = heapArray.clone();
            heapArray = new Dist[size];

            for (int i = 0; i < temp.length; i++) {
                heapArray[i] = temp[i];
            }
        }

        heapArray[heapSize] = element;
        heapifyUp(heapSize);
        heapSize++;
    }

    public String extract_min() {
        return heapArray[0].toString();
    }

    public Dist get_min() {
        return heapArray[0];
    }

    public Dist delete_min() {
        Dist deleteElement = heapArray[0];
        int newHeapSize = --heapSize;

        heapArray[0] = heapArray[newHeapSize];

        heapifyDown(0);

        return deleteElement;
    }

    public Dist getRightChild(int index) {
        if (heapSize >= ((2 * index) + 1)) {
            return heapArray[((2 * index) + 1)];
        }
        return null;
    }

    public Dist getLeftChild(int index) {
        if (heapSize >= ((2 * index) + 2)) {
            return heapArray[((2 * index) + 2)];
        }
        return null;
    }

    public void heapifyUp(int index) {
        Dist element = heapArray[index];
        Dist temp;
        int ind = index;

        while (ind > 0) {
            if (heapArray[ind].compareTo(getParent(ind)) < 0) {
                temp = heapArray[(ind - 1) / 2];

                heapArray[(ind - 1) / 2] = heapArray[ind];
                heapArray[ind] = temp;
            }

            ind--;
        }
    }

    public void heapifyDown(int i) {
        int ind = i;

        if (ind >= 0 && getLeftChild(ind) != null && getRightChild(ind) != null) {
            if (getRightChild(ind).compareTo(getLeftChild(ind)) > 0) {
                while (ind < heapSize) {
                    if (getLeftChild(ind) != null && heapArray[ind].compareTo(getLeftChild(ind)) > 0) {
                        swapChildParent(((2 * ind) + 2), ind);
                    }
                    if (getRightChild(ind) != null && heapArray[ind].compareTo(getRightChild(ind)) > 0) {
                        swapChildParent(((2 * ind) + 1), ind);
                    }
                    ind++;
                }
            } else {
                while (ind < heapSize) {
                    if (getRightChild(ind) != null && heapArray[ind].compareTo(getRightChild(ind)) > 0) {
                        swapChildParent(((2 * ind) + 1), ind);
                    }
                    if (getLeftChild(ind) != null && heapArray[ind].compareTo(getLeftChild(ind)) > 0) {
                        swapChildParent(((2 * ind) + 2), ind);
                    }
                    ind++;
                }
            }
        }
    }

    public void swapChildParent(int index1, int index2) {
        Dist temp = heapArray[index1];

        heapArray[index1] = heapArray[index2];

        heapArray[index2] = temp;
    }

    public void print() {
        if (heapSize == 0) {
            System.out.println("Heap is empty");
        } else {
            for (int i = 0; i < heapSize; i++) {
                System.out.println(heapArray[i].toString());
            }
        }
    }

    public ArrayList<Dist> getAll() {
        ArrayList<Dist> nodes = new ArrayList<Dist>();

        for (int i = 0; i < heapSize; i++) {
            nodes.add(heapArray[i]);
        }

        return nodes;
    }

    public Dist getDist(int mID) {
        int index = getDistIndex(mID);

        if (index != -1) return heapArray[index];
        else return null;
    }

    public int getDistIndex(int mID) {
        for (int i = 0; i < this.heapSize; i++) {
            if (heapArray[i].mID == mID) return i;
        }

        return -1;
    }

    public boolean exists(Dist s) {
        boolean result = false;
        for (int i = 0; i < heapSize; i++) {
            if (heapArray[i].equals(s)) {
                result = true;
                break;
            }
        }

        return result;
    }

    public void updateNode(int mID, Dist elementNew) {

        for (int i = 0; i < heapSize; i++) {
            if (heapArray[i].mID == mID) {
                int newHeapSize = --heapSize;
                heapArray[i] = heapArray[newHeapSize];
                heapifyDown(i);
                break;
            }
        }

        insert(elementNew);
    }

}
