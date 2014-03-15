package net.wohlfart.photon.events;

import java.io.Serializable;

import javax.annotation.Nullable;
import javax.vecmath.Vector3f;

import net.wohlfart.photon.tools.MathTool;
import net.wohlfart.photon.tools.ObjectPool;
import net.wohlfart.photon.tools.ObjectPool.PoolableObject;
import net.wohlfart.photon.tools.OutOfResourcesException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jogamp.opengl.math.Quaternion;

public class RotateEvent extends Quaternion implements PoolableObject, Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(RotateEvent.class);
    private static final int POOL_SIZE = 10;

    private static final float ROTATION_SPEED = MathTool.TWO_PI; // one rotation per sec

    private static final ObjectPool<RotateEvent> POOL = new ObjectPool<RotateEvent>(POOL_SIZE) {
        @Override
        protected RotateEvent newObject() {
            return new RotateEvent();
        }
    };

    @Override
    public void reset() {
        LOGGER.debug("reset rotate: " + this);
        setIdentity();
        POOL.returnObject(this);
    }

    // only the pool may create an instance
    private RotateEvent() {}

    public static RotateEvent rotateLeft(float time) {
        return rotate(time * ROTATION_SPEED / MathTool.TWO_PI, new Vector3f(0, 1, 0));
    }

    public static RotateEvent rotateRight(float time) {
        return rotate(time * ROTATION_SPEED / MathTool.TWO_PI, new Vector3f(0, -1, 0));
    }

    public static RotateEvent rotateUp(float time) {
        return rotate(time * ROTATION_SPEED / MathTool.TWO_PI, new Vector3f(1, 0, 0));
    }

    public static RotateEvent rotateDown(float time) {
        return rotate(time * ROTATION_SPEED / MathTool.TWO_PI, new Vector3f(-1, 0, 0));
    }

    public static RotateEvent rotateClockwise(float time) {
        return rotate(time * ROTATION_SPEED / MathTool.TWO_PI, new Vector3f(0, 0, 1));
    }

    public static RotateEvent rotateCounterClockwise(float time) {
        return rotate(time * ROTATION_SPEED / MathTool.TWO_PI, new Vector3f(0, 0, -1));
    }

    // might return null if we are out of resources
    @Nullable static RotateEvent rotate(float rad, Vector3f axis) {
        try {
            final RotateEvent result = POOL.borrowObject();
            result.setIdentity();
            MathTool.rotate(result, rad, axis);
            LOGGER.debug("created rotate: " + result);
            return result;
        } catch (OutOfResourcesException ex) {
            LOGGER.warn("returning null", ex);
            return null;
        }
    }

}
