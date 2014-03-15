package net.wohlfart.photon.pov;

import javax.vecmath.Vector3f;

public class CanMoveImpl extends Vector3f implements CanMove {
    private static final long serialVersionUID = 1L;
    private final Vector3f negated = new Vector3f();

    @Override
    public void move(Vector3f delta) {
        this.x += delta.x;
        this.y += delta.y;
        this.z += delta.z;
    }

    @Override
    public Vector3f getPosition() {
        return this;
    }

    @Override
    public void setPosition(Vector3f vector) {
        this.x = vector.x;
        this.y = vector.y;
        this.z = vector.z;
    }

    @Override
    public void reset() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public Vector3f getNegPosition() {
        negated.x = -this.x;
        negated.y = -this.y;
        negated.z = -this.z;
        return negated;
    }

}
