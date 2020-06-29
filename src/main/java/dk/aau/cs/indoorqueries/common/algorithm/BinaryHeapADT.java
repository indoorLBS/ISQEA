package dk.aau.cs.indoorqueries.common.algorithm;

interface BinaryHeapADT<T> {
    void insert(T element, int id);

    String extract_min();

    String delete_min();

    void heapifyUp(int index);

    void heapifyDown(int i);
}
