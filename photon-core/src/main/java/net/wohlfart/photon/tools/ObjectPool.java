package net.wohlfart.photon.tools;

import java.util.ArrayList;

import net.wohlfart.photon.tools.ObjectPool.PoolableObject;

public abstract class ObjectPool<T extends PoolableObject> implements IObjectPool<T> {

	static public interface PoolableObject {
		public void reset();
	}

	private final Object lock = new Object();

	private final int capacity;
	private final ArrayList<T> freeObjects;
	private int created = 0;

	public ObjectPool(int capacity) {
		freeObjects = new ArrayList<T>();
		this.capacity = capacity;
	}

	protected abstract T newObject();

	@Override
	public T borrowObject() {
		synchronized(lock) {
			if (freeObjects.size() > 0) {
				return freeObjects.remove(freeObjects.size() - 1);
			} else if (created < capacity) {
				created++;
				return newObject();
			} else {
				// TODO: since we are multithreaded this is a good point to do a yield,
				// however there seem to be some locking issues since neither a yield() nor a sleep()
				// here did anything to get a better performance...
				throw new OutOfResourcesException("running out of resources in " + this
						+ " capacity is '" + capacity + "'"
						+ " created: '" + created + "'"
						+ " freeObjects count: '" + freeObjects.size() + "'");
			}
		}
	}

	@Override
	public void returnObject(T object) {
		synchronized(lock) {
			// check if we already freed the object
			for (T o : freeObjects) {
				if (o == object) {
					throw new IllegalStateException("object already free: '" + object + "'");
				}
			}
			freeObjects.add(object);
		}
	}

}
