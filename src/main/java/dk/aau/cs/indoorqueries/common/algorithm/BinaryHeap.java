package dk.aau.cs.indoorqueries.common.algorithm;

@SuppressWarnings("unchecked")
public class BinaryHeap<T extends Comparable<T>> implements BinaryHeapADT<T> {

    int size;
    T[] heapArray;
    int[] idArray;
    public int heapSize;

    public BinaryHeap(int size) {
        this.size = size;
        heapArray = (T[]) new Comparable[size];
        idArray = new int[size];
        heapSize = 0;
    }

    public T getParent(int index) {
        return heapArray[(index - 1) / 2];
    }

    public void insert(T element, int id) {
        heapArray[heapSize] = element;
        idArray[heapSize] = id;
        heapifyUp(heapSize);
        heapSize++;
    }

    public String extract_min() {
        return heapArray[0] + ", " + idArray[0];
    }

    public String delete_min() {
        T deleteElement = heapArray[0];
        int deleteID = idArray[0];
        int newHeapSize = --heapSize;
        heapArray[0] = heapArray[newHeapSize];
        idArray[0] = idArray[newHeapSize];
        heapifyDown(0);
        return deleteElement + "," + deleteID;
    }

    public T getRightChild(int index) {
        if (heapSize >= ((2 * index) + 1)) {
            return heapArray[((2 * index) + 1)];
        }
        return null;
    }

    public T getLeftChild(int index) {
        if (heapSize >= ((2 * index) + 2)) {
            return heapArray[((2 * index) + 2)];
        }
        return null;
    }

    public void heapifyUp(int index) {
        T element = heapArray[index];
        T temp;
        int tempID = idArray[index];
        int ind = index;
        while (ind > 0) {
            if (heapArray[ind].compareTo(getParent(ind)) < 0) {
                temp = heapArray[(ind - 1) / 2];
                tempID = idArray[(ind - 1) / 2];
                heapArray[(ind - 1) / 2] = heapArray[ind];
                idArray[(ind - 1) / 2] = idArray[ind];
                heapArray[ind] = temp;
                idArray[ind] = tempID;
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
        T temp = heapArray[index1];
        int tempID = idArray[index1];
        heapArray[index1] = heapArray[index2];
        idArray[index1] = idArray[index2];
        heapArray[index2] = temp;
        idArray[index2] = tempID;
    }


    public void print() {
        if (heapSize == 0) {
            System.out.println("Heap is empty");
        } else {
            for (int i = 0; i < heapSize; i++) {
                System.out.println(heapArray[i] + "," + idArray[i]);
            }
        }
    }

    public T[] getAll() {
        return heapArray;
    }

    public int getHeapSize() {
        return heapSize;
    }

    public void updateNode(T element, int id, T elementNew, int idNew) {
//		System.out.println("from (" + element + ", " + id + ") to " + elementNew + " idNew = " + idNew);

        for (int i = 0; i < heapSize; i++) {
            if (idArray[i] == id && heapArray[i].equals(element)) {
                T deleteElement = heapArray[i];
                int deleteID = idArray[i];
                int newHeapSize = --heapSize;
                heapArray[i] = heapArray[newHeapSize];
                idArray[i] = idArray[newHeapSize];
                heapifyDown(i);
                break;
            }
        }

        insert(elementNew, idNew);
    }

    public static void main(String[] args) {
        BinaryHeap<Double> bh = new BinaryHeap<Double>(4000);
        bh.insert((double) 1, 1);
        bh.insert((double) 2, 2);
        bh.insert((double) 3, 3);
        bh.insert((double) 4, 4);
        bh.insert((double) 5, 4);
        bh.insert((double) 6, 4);
        bh.insert((double) 6, 4);
        bh.insert((double) 6, 5);
        bh.insert((double) 7, 6);
        bh.print();

        System.out.println("==============");
        bh.updateNode((double) 5, 4, (double) 100, 100);
        System.out.println("==============");
        bh.print();

        System.out.println("==============");
        bh.updateNode((double) 6, 4, (double) 200, 200);
        System.out.println("==============");
        bh.print();

        System.out.println("==============");
        bh.updateNode((double) 200, 200, (double) 1, 1);
        System.out.println("==============");
        bh.print();

        System.out.println("==============");
        System.out.println(bh.delete_min() + " is removed");
        System.out.println("==============");
        bh.print();
        System.out.println("==============");
        System.out.println(bh.delete_min() + " is removed");
        System.out.println("==============");
        bh.print();
        System.out.println("==============");
        System.out.println(bh.delete_min() + " is removed");
        System.out.println("==============");
        bh.print();
        System.out.println("==============");
        System.out.println(bh.delete_min() + " is removed");
        System.out.println("==============");
        bh.print();
        System.out.println("==============");
        System.out.println(bh.delete_min() + " is removed");
        System.out.println("==============");
        bh.print();
        System.out.println("==============");
        System.out.println(bh.delete_min() + " is removed");
        System.out.println("==============");
        bh.print();
        System.out.println("==============");
        System.out.println(bh.delete_min() + " is removed");
        System.out.println("==============");
        bh.print();
        System.out.println("==============");
        System.out.println(bh.delete_min() + " is removed");
        System.out.println("==============");
        bh.print();
        System.out.println("==============");
        System.out.println(bh.delete_min() + " is removed");
        System.out.println("==============");
        bh.print();
    }
}