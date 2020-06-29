package com.github.davidmoten.rtree;

import com.github.davidmoten.rtree.geometry.Geometry;

import java.util.List;

public interface LeafFactory<T, S extends Geometry> {
    Leaf<T, S> createLeaf(List<Entry<T, S>> entries, Context<T, S> context);
}
