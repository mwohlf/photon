package net.wohlfart.photon.pov;

import java.io.Serializable;

import javax.vecmath.Vector3f;


public interface CanMove extends Serializable {

    public void move(Vector3f vector);

    public void reset();

    public Vector3f getPosition();

    public void setPosition(Vector3f vector);

}
