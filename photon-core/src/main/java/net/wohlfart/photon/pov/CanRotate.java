package net.wohlfart.photon.pov;

import java.io.Serializable;

import javax.vecmath.Vector3f;

import net.wohlfart.photon.tools.Quaternion;


public interface CanRotate extends Serializable {

    public void rotate(float deltaAngle, Vector3f axis);

    public void reset();

    public Quaternion getRotation();

    public void setRotation(Quaternion quaternion);

    public Vector3f getRght(Vector3f vector3f);

    public Vector3f getUp(Vector3f result);

    public Vector3f getForward(Vector3f result);

}
