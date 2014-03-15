package net.wohlfart.photon.time;


/**
 * A generic Timer interface, a timer is based on a clock.
 */
public interface Timer {

    /**
     * Returns the time in seconds since the last call this this method.
     * 
     * @return a float with the time in seconds since the last call to
     *         getDelta()
     */
    float getDelta();

}
