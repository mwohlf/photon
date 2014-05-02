package net.wohlfart.photon.time;


/**
 * A specific clock implementation.
 */
public class ClockImpl implements Clock {

    @Override
    public long getTicks() {
        return System.nanoTime(); // this wraps around
    }

    @Override
    public long getTicksPerSecond() {
        return 1000 * 1000;
    }

    @Override
    public long getMaxValidCount() {
        return Long.MAX_VALUE;
    }

}
