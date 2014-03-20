package net.wohlfart.photon.events;

import java.io.Serializable;

import javax.annotation.Nullable;

import net.wohlfart.photon.tools.IObjectPool;
import net.wohlfart.photon.tools.ObjectPool;
import net.wohlfart.photon.tools.ObjectPool.PoolableObject;
import net.wohlfart.photon.tools.OutOfResourcesException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * the high level commands, base class for all kind of high level events/commands
 */
public class CommandEvent implements PoolableObject, Serializable {
	private static final long serialVersionUID = 1L;
	protected static final Logger LOGGER = LoggerFactory.getLogger(CommandEvent.class);
    protected static final int POOL_SIZE = 20;
    private CommandKey key;

    private static final IObjectPool<CommandEvent> POOL = new ObjectPool<CommandEvent>(POOL_SIZE) {
        @Override
        protected CommandEvent newObject() {
            return new CommandEvent();
        }
    };

    @Override
    public void reset() {
        key = null;  // this is not really needed
        POOL.returnObject(this);
    }

    // only the pool may create an instance
    private CommandEvent() {}

    public CommandKey getKey() {
        return key;
    }

    @Nullable
    public static CommandEvent exit() {
        try {
            final CommandEvent result = POOL.borrowObject();
            result.key = CommandKey.EXIT;
            return result;
        } catch (OutOfResourcesException ex) {
            LOGGER.info("out of resources", ex);;
            return null;
        }
    }

    @Nullable
    public static CommandEvent dumpScene() {
        try {
            final CommandEvent result = POOL.borrowObject();
            result.key = CommandKey.DUMP_SCENE;
            return result;
        } catch (OutOfResourcesException ex) {
            LOGGER.info("out of resources", ex);;
            return null;
        }
    }

    @Nullable
    public static CommandEvent debugRenderer() {
        try {
            final CommandEvent result = POOL.borrowObject();
            result.key = CommandKey.DEBUG_RENDER;
            return result;
        } catch (OutOfResourcesException ex) {
            LOGGER.info("out of resources", ex);;
            return null;
        }
    }


    public enum CommandKey {
        EXIT,
        DUMP_SCENE,
        DEBUG_RENDER;
    }

}
