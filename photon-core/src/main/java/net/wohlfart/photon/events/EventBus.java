package net.wohlfart.photon.events;


public interface EventBus<T> {

    void register(Object subscriber);
    
    void unregister(Object subscriber);

    // resets the event as soon as it is delivered
    void post(T event);
   
    // return true if there are some event waiting to be delivered
    boolean hasEvent();
    
    // fires the event in the current thread
    void fireEvent();
    
    // remove all events from the queue
    void flush();
   
}
