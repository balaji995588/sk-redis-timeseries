package com.redis_timeseries.feed;

import java.io.Serializable;
import java.util.Vector;

public class Queue<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Vector<T> queue = new Vector<T>();
	boolean flag = true;

	public synchronized void EnQueue(T item) {
		queue.addElement(item);
		notify(); // notifyAll();
	}

	public synchronized T DeQueue() {
		while (queue.isEmpty() && flag) {
			try {
				wait();
			} catch (InterruptedException ie) {
				ie.printStackTrace();
				// System.exit(0);
			}
		}

		if (flag) {
			T item = queue.firstElement();
			queue.removeElement(item);
			return item;
		} else
			return null;
	}

	public void StopThreads() {
		flag = false;
		notifyAll();
	}

	public int Count() {
		return queue.size();
	}

	public void clear() {
		synchronized (queue) {
			queue.clear();
		}

	}
}
