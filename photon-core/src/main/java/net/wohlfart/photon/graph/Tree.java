package net.wohlfart.photon.graph;

import java.util.Iterator;

public interface Tree<T> {

    T getValue();

    Tree<T> add(T value);

    void remove(T value);

    Iterator<? extends Tree<T>> getChildren();

}
