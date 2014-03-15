package net.wohlfart.photon.state;

public interface Event {

    Event START = new Event() {};

    Event END = new Event() {};

    Event QUIT = new Event() {};

}
