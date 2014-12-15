package ctb;

/***
 * A Basic Map Interface Interview Template
 * @author cparis
 *
 * @param <K> Key
 * @param <V> Value
 */
public interface BasicMap<K,V>
{
	/**
	 * Get's a value based on the Key.  
	 * @param key Key to retrieve
	 * @return Stored value, or null if not found
	 */
	V get(K key);
	
	/**
	 * Puts an object into the map.  
	 * 
	 * @param key Key to store
	 * @param value Value to store.  Must not be null.
	 * 
	 * @return (Question: Why does the java Map interface return a value on put, and how would you use it?
	 */
	V put(K key, V value);

	/**
	 * Removes a key from the map
	 * 
	 * @param key Key to remove
	 */
	void remove(K key);
	
	/**
	 * Check to see if a key exists.
	 * 
	 * @param key Key to check
	 * @return 
	 */
	boolean exists(K key);
}
