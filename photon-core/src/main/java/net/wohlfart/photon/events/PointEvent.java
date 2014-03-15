package net.wohlfart.photon.events;

import java.io.Serializable;

import javax.annotation.Nullable;

import net.wohlfart.photon.tools.ObjectPool;
import net.wohlfart.photon.tools.ObjectPool.PoolableObject;
import net.wohlfart.photon.tools.OutOfResourcesException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PointEvent implements PoolableObject, Serializable {
    private static final long serialVersionUID = 1L;
    protected static final Logger LOGGER = LoggerFactory.getLogger(PointEvent.class);
    protected static final int POOL_SIZE = 100;

    private int x;
    private int y;
    private Key key;

    enum Key {
        PICK, CONTEXT;
    }

    private static final ObjectPool<PointEvent> POOL = new ObjectPool<PointEvent>(POOL_SIZE) {
        @Override
        protected PointEvent newObject() {
            return new PointEvent();
        }
    };

    @Nullable static PointEvent create(int screenX, int screenY, Key key) {
        try {
            final PointEvent result = POOL.borrowObject();
            result.x = screenX;
            result.y = screenY;
            result.key = key;
            return result;
        } catch (OutOfResourcesException ex) {
            LOGGER.warn("returning null", ex);
            return null;
        }
    }

    @Override
    public void reset() {
        LOGGER.info("reset PointEvent: " + this);
        key = null;
        x = y = -1;
        POOL.returnObject(this);
    }

    // only the pool may create an instance
    private PointEvent() {}


    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Key getKey() {
        return key;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [x=" + x
                + ", y=" + y
                + ", key=" + key + "]";
    }

}
