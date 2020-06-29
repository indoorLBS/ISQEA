package com.github.davidmoten.rtree3d;

import com.github.davidmoten.rtree3d.geometry.Geometry;

import java.util.List;

import static com.github.davidmoten.rtree3d.Comparators.*;
import static java.util.Collections.min;

public final class SelectorMinimalOverlapVolume implements Selector {

    @SuppressWarnings("unchecked")
    @Override
    public <T, S extends Geometry> Node<T, S> select(Geometry g, List<? extends Node<T, S>> nodes) {
        return min(
                nodes,
                compose(overlapVolumeComparator(g.mbb(), nodes), volumeIncreaseComparator(g.mbb()),
                        volumeComparator(g.mbb())));
    }

}
