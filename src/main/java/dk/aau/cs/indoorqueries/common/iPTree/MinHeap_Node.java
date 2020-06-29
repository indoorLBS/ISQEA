package dk.aau.cs.indoorqueries.common.iPTree;

import dk.aau.cs.indoorqueries.common.utilities.DataGenConstant;

import java.util.ArrayList;

/**
 * @author feng zijin
 */
public class MinHeap_Node<T> {

//	int size;
//	T[] heapArray;
//	int[] idArray;
//	public int heapSize;

    int size = DataGenConstant.leafNodeSize;
    Node[] heapArray;
    public int heapSize;

    public MinHeap_Node() {
        heapArray = new Node[size];
        heapSize = 0;
    }

    public Node getParent(int index) {
        return heapArray[(index - 1) / 2];
    }

    public void insert(Node element) {
        heapArray[heapSize] = element;
        heapifyUp(heapSize);
        heapSize++;
    }

    public String extract_min() {
        return heapArray[0].toString();
    }

    public Node get_min() {
        return heapArray[0];
    }

    public Node delete_min() {
        Node deleteElement = heapArray[0];
        int newHeapSize = --heapSize;

        heapArray[0] = heapArray[newHeapSize];

        heapifyDown(0);

        return deleteElement;
    }

    public Node getRightChild(int index) {
        if (heapSize >= ((2 * index) + 1)) {
            return heapArray[((2 * index) + 1)];
        }
        return null;
    }

    public Node getLeftChild(int index) {
        if (heapSize >= ((2 * index) + 2)) {
            return heapArray[((2 * index) + 2)];
        }
        return null;
    }

    public void heapifyUp(int index) {
        Node element = heapArray[index];
        Node temp;
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
        Node temp = heapArray[index1];

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

    public ArrayList<Node> getAll() {
        ArrayList<Node> nodes = new ArrayList<Node>();

        for (int i = 0; i < heapSize; i++) {
            nodes.add(heapArray[i]);
        }

        return nodes;
    }

    public void deleteNode(Node element) {

        for (int i = 0; i < heapSize; i++) {
            if ((heapArray[i]).getNodeID() == element.getNodeID()) {
                int newHeapSize = --heapSize;
                heapArray[i] = heapArray[newHeapSize];
                heapifyUp(i);
                heapifyDown(i);
                break;
            }
        }
    }

    public boolean exists(Node s) {
        boolean result = false;
        for (int i = 0; i < heapSize; i++) {
            if (heapArray[i].equals(s)) {
                result = true;
                break;
            }
        }

        return result;
    }


    public boolean existsID(int ID) {
        boolean result = false;

        for (int i = 0; i < heapSize; i++) {
            if ((heapArray[i]).getNodeID() == ID) {
                System.out.println(heapArray[i].toStringShort() + " equals " + ID);
                result = true;
                break;
            }
        }

        return result;
    }

    public Node copyNode(Node node) {
        Node result = null;

        if (node.getType() == "LeafNode") {
            result = new LeafNode((LeafNode) node);
        } else if (node.getType() == "NonLeafNode") {
            result = new NonLeafNode((NonLeafNode) node);
        }

        return result;
    }


}
