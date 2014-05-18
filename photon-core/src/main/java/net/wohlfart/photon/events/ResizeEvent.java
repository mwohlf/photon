package net.wohlfart.photon.events;

import java.io.Serializable;

import javax.annotation.Nullable;

import net.wohlfart.photon.tools.Dimension;
import net.wohlfart.photon.tools.ObjectPool;
import net.wohlfart.photon.tools.ObjectPool.PoolableObject;
import net.wohlfart.photon.tools.OutOfResourcesException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ResizeEvent implements PoolableObject, Serializable {
    private static final long serialVersionUID = 1L;
    protected static final Logger LOGGER = LoggerFactory.getLogger(ResizeEvent.class);
    protected static final int POOL_SIZE = 5;

    private final Dimension dim = new Dimension();


    private static final ObjectPool<ResizeEvent> POOL = new ObjectPool<ResizeEvent>(POOL_SIZE) {
        @Override
        protected ResizeEvent newObject() {
            return new ResizeEvent();
        }
    };

    @Nullable
	public static ResizeEvent create(int width, int height) {
        try {
            final ResizeEvent result = POOL.borrowObject();
            result.dim.set(width, height);
            return result;
        } catch (OutOfResourcesException ex) {
            LOGGER.warn("returning null", ex);
            return null;
        }
    }

    @Override
    public void reset() {
        LOGGER.info("reset PointEvent: " + this);
        dim.set(0, 0);
        POOL.returnObject(this);
    }

    // only the pool may create an instance
    private ResizeEvent() {}


    public Dimension getDimension() {
        return dim;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName()
        		+ " [x=" + dim.getWidth()
                + ", y=" + dim.getHeight() + "]";
    }

}
