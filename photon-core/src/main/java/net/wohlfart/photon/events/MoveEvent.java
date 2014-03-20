package net.wohlfart.photon.events;

import java.io.Serializable;

import javax.annotation.Nullable;
import javax.vecmath.Vector3f;

import net.wohlfart.photon.tools.ObjectPool;
import net.wohlfart.photon.tools.ObjectPool.PoolableObject;
import net.wohlfart.photon.tools.OutOfResourcesException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoveEvent implements PoolableObject, Serializable {
    private static final long serialVersionUID = 1L;
    protected static final Logger LOGGER = LoggerFactory.getLogger(MoveEvent.class);
    protected static final int POOL_SIZE = 10;
    private static final float MOVE_SPEED = 50f;
    private static final float WHEEL_SENSITIVITY = 0.05f;

    private final Vector3f delegate = new Vector3f();

    private static final ObjectPool<MoveEvent> POOL = new ObjectPool<MoveEvent>(POOL_SIZE) {
        @Override
        protected MoveEvent newObject() {
            return new MoveEvent();
        }
    };

    @Override
    public void reset() {
        LOGGER.debug("reset move: " + this);
        delegate.set(new float[] {0, 0, 0});
        POOL.returnObject(this);
    }

    // only the pool may create an instance
    private MoveEvent() {}

    // ---- package private static factory methods

    public static MoveEvent wheel(float time, int delta) {
        return move(0, 0, time * MOVE_SPEED * WHEEL_SENSITIVITY * delta);
    }

    public static MoveEvent moveRight(float time) {
        return move(+time * MOVE_SPEED, 0, 0);
    }

    public static MoveEvent moveLeft(float time) {
        return move(-time * MOVE_SPEED, 0, 0);
    }

    public static MoveEvent moveDown(float time) {
        return move(0, -time * MOVE_SPEED, 0);
    }

    public static MoveEvent moveUp(float time) {
        return move(0, +time * MOVE_SPEED, 0);
    }

    public static MoveEvent moveForward(float time) {
        return move(0, 0, -time * MOVE_SPEED);
    }

    public static MoveEvent moveBack(float time) {
        return move(0, 0, +time * MOVE_SPEED);
    }

    // package private for testing
    @Nullable static MoveEvent move(float x, float y, float z) {
        try {
            final MoveEvent result = POOL.borrowObject();
            result.delegate.set(new float[] {x, y, z});
            return result;
        } catch (OutOfResourcesException ex) {
            LOGGER.warn("returning null", ex);
            return null;
        }
    }

	public Vector3f get() {
		return delegate;
	}

}
