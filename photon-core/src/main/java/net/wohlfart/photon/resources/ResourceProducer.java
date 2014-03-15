package net.wohlfart.photon.resources;

public interface ResourceProducer<P, K> {
    
    P produce(K key);
    
}
