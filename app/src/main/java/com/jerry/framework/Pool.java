package com.jerry.framework;

import java.util.ArrayList;
import java.util.List;

//generic class
public class Pool<T> {

	public interface PoolObjectFactory<T> {
		public T createObject();
	}

	private final List<T> freeObjects;
	private final PoolObjectFactory<T> factory;
	private final int maxSize; // maximum number of objects it should store

	public Pool(PoolObjectFactory<T> factory, int maxSize) {
		this.factory = factory;
		this.maxSize = maxSize;
		// ArrayList with the capacity set to the maximum number of objects
		this.freeObjects = new ArrayList<T>(maxSize);
	}

	/*
	 * The newObject() method is responsible for either handing us a brand-new
	 * instance of the type held by the Pool, via the
	 * PoolObjectFactory.newObject() method, or it returns a pooled instance in
	 * case there�s one in the freeObjectsArrayList. If we use this method, we
	 * get recycled objects as long as the Pool has some stored in the
	 * freeObjects list. Otherwise, the method creates a new one via the
	 * factory.
	 */
	public T newObject() {
		T object = null;
		if (freeObjects.size() == 0) {
			object = factory.createObject();
		} else {
			object = freeObjects.remove(freeObjects.size() - 1);
		}
		return object;
	}

	/*
	 * The free() method lets us reinsert objects that we no longer use. It
	 * simply inserts the object into the freeObjects list if it is not yet
	 * filled to capacity. If the list is full, the object is not added, and it
	 * is likely to be consumed by the garbage collector the next time it
	 * executes.
	 */
	public void free(T object) {
		if (freeObjects.size() < maxSize) {
			freeObjects.add(object);
		}
	}
}
