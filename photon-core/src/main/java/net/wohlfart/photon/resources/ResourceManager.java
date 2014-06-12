package net.wohlfart.photon.resources;

import java.util.HashMap;
import java.util.Map;

import net.wohlfart.photon.shader.IShaderProgram;
import net.wohlfart.photon.shader.IShaderProgram.IShaderProgramIdentifier;
import net.wohlfart.photon.shader.ShaderFactory;
import net.wohlfart.photon.texture.TextureFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * responsible for caching the resources,
 * loading or creating is delegated to the factories
 *
 * TODO: somehow we need to keep track which resources are loaded in the GPU's memory...
 * also check if we can do some async magic here
 */
public enum ResourceManager {
	INSTANCE; // todo: remove the singleton

	protected static final Logger LOGGER = LoggerFactory.getLogger(ResourceManager.class);

    private final Map<Class<?>, ResourceProducer<?, ?>> delegates = new HashMap<Class<?>, ResourceProducer<?, ?>>();

    private final HashMap<HashKey<?,?>, Object> resourceCache = new HashMap<HashKey<?,?>, Object>();

    private ResourceManager() {
    	register(new ShaderFactory());
    	resourceCache.put(
    			new HashKey<IShaderProgram, IShaderProgramIdentifier>(IShaderProgram.class, IShaderProgram.NULL_SHADER_ID),
    		    IShaderProgram.NULL_SHADER);
    	register(new TextureFactory());
    };

    public final void register(ResourceProducer<?,?> producer) {
    	delegates.put(producer.flavour(), producer);
    }

    public static <P,K> P loadResource(Class<P> clazz, K key) {
    	return INSTANCE.load(clazz, key);
    }


    @SuppressWarnings("unchecked")
    public <R,K> R load(Class<R> clazz, K key) {
        HashKey<R, K> lookup = new HashKey<R,K>(clazz, key);
        R result = clazz.cast(resourceCache.get(lookup));
        if (result != null) {
        	LOGGER.debug("returning cached resource for '{}'", key);
            return result;
        }

    	LOGGER.debug("using producer to create resource for '{}'", key);
        ResourceProducer<R,K> producer = (ResourceProducer<R,K>) delegates.get(clazz);
        if (producer == null) {
            throw new ResourceException("no delegate found to load class of type " + clazz);
        }
        result = producer.produce(key);
        if (result == null) {
            throw new ResourceException("producer returned null for class of type " + clazz + " with key " + key);
        }

        resourceCache.put(lookup, result);
        return clazz.cast(result);
    }

    protected static class HashKey<R, K> {

        final Class<R> clazz;
        final K key;

        HashKey(Class<R> clazz, K key) {
            assert clazz != null;
            assert key != null : "unable to load " + clazz + " since key is null";
            this.clazz = clazz;
            this.key = key;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + clazz.hashCode();
            result = prime * result + key.hashCode();
            return result;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object)
                return true;
            if ((object == null) || (getClass() != object.getClass()))
                return false;

            HashKey<?,?> that = (HashKey<?,?>) object;

            if (!clazz.equals(that.clazz))
                return false;

            if (!key.equals(that.key))
                return false;

            return true;
        }
    }

}
