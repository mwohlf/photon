package net.wohlfart.photon.resources;

public interface ResourceProducer<P, K> {

	Class<P> flavour();

    P produce(K key);

}
