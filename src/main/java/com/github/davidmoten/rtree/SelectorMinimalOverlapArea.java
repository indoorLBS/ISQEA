package com.github.davidmoten.rtree;

import com.github.davidmoten.rtree.geometry.Geometry;
import com.github.davidmoten.rtree.internal.Comparators;

import java.util.List;

import static java.util.Collections.min;

public final class SelectorMinimalOverlapArea implements Selector {

    @Override
    public <T, S extends Geometry> Node<T, S> select(Geometry g, List<? extends Node<T, S>> nodes) {
        return min(nodes,
                Comparators.overlapAreaThenAreaIncreaseThenAreaComparator(g.mbr(), nodes));
    }

}
