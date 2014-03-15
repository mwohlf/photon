package net.wohlfart.photon.tools;

public interface IObjectPool<T> {

    T borrowObject();

    void returnObject(T object);

}
