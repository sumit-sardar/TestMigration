package com.ctb.contentcreator.processor;

import java.util.LinkedList;

public class CTBQueue {
	private LinkedList list = new LinkedList();

	/**
	 * Puts name value pair in queue.
	 */
	public void put(String id, Object val) {
		list.addLast(new Element(id, val));
	}
	public void put( Object val) {
		list.addLast(new Element(null, val));
	}

	/**
	 * Returns an element (name value) from queue.
	 * 
	 * @return element from queue or null if queue is empty
	 */
	public Element get() {
		if (list.isEmpty()) {
			return null;
		}
		return (Element) list.removeFirst();
	}

	/**
	 * Peeks an element in the queue. Returned elements is not removed from the
	 * queue.
	 */
	public Element peek() {
		return (Element) list.getFirst();
	}

	/**
	 * Returns true if queue is empty, otherwise false
	 */
	public boolean isEmpty() {
		return list.isEmpty();
	}

	/**
	 * Returns queue size.
	 */
	public int size() {
		return list.size();
	}

	class Element {
		private String id;
		private Object val;

		Element(String id, Object val) {
			this.id = id;
			this.val = val;

		}

		/**
		 * @return the id
		 */
		public String getId() {
			return id;
		}

		/**
		 * @return the val
		 */
		public Object getVal() {
			return val;
		}

	}

}
