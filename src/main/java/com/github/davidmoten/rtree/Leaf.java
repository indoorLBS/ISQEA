package com.github.davidmoten.rtree;

import com.github.davidmoten.rtree.geometry.Geometry;

import java.util.List;

public interface Leaf<T, S extends Geometry> extends Node<T, S> {

    List<Entry<T, S>> entries();

    /**
     * Returns the ith entry (0-based). This method should be preferred for
     * performance reasons when only one entry is required (in comparison to
     * {@code entries().get(i)}).
     *
     * @param i 0-based index
     * @return ith entry
     */
    Entry<T, S> entry(int i);

}