package com.github.davidmoten.rtree;

import com.github.davidmoten.rtree.geometry.Geometry;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Serializer<T, S extends Geometry> {

    void write(RTree<T, S> tree, OutputStream os) throws IOException;

    RTree<T, S> read(InputStream is, long sizeBytes, InternalStructure structure)
            throws IOException;

}