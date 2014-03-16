package net.wohlfart.photon.graph;

import java.util.Iterator;

public interface ITree<T> {

    T getValue();

    ITree<T> add(T value);

    void remove(T value);

    Iterator<? extends ITree<T>> getChildren();

}
