package com.ctb.lexington.util;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Provides a {@link Map} which forbids <code>null</code> keys and values and
 * requires that keys and values be of certain types.  This handles the large
 * majority of uses which map definite types (in contrast to generic
 * <code>Object</code> types) and which expect definite key and value instances
 * (in contrast to <code>null</code>).</p>
 *
 * <p>Of note is one particular common idiom:</p> <pre>
 * if (null == map.get(key)) {
 *     doSomethingForMissingKey(key);
 * }
 * </pre>
 *
 * <p>which will create a <code>null</code> value for <var>key</var> and return
 * it. <code>SafeHashMap</code> requires the more correct:</p> <pre>
 * if (!map.contains(key)) {
 *     doSomethingForMissingKey(key);
 * }
 * </pre>
 *
 * <p>Typical use:</p> <pre>
 * final SafeHashMap propertyMap = new SafeHashMap(String.class,
 * Something.class);
 * propertyMap.put("foo", new Something());
 * propertyMap.put("bar", new SubSomething()); // subclasses ok
 * propertyMap.put(null, new Something()); // NullPointerException
 * propertyMap.put("foo", new Integer(3)); // ClassCastException
 * </pre>
 *
 * @author <a href="boxley@thoughtworks.com">B. K. Oxley (binkley)</a>
 * @version 1.0
 */
public class SafeHashMap extends HashMap {
    private final Class keyClass;
    private final Class valueClass;

    /**
     * Constructs a new <code>SafeHashMap</code> which accepts any class of key
     * and value.
     *
     * @see SafeHashMap#SafeHashMap(Class, Class)
     */
    public SafeHashMap() {
        this(Object.class, Object.class);
    }

    /**
     * Constructs a new <code>SafeHashMap</code> for a given <var>keyClass</var>
     * and <var>valueClass</var>.
     *
     * @param keyClass the superclass of map keys
     * @param valueClass the superclass of map values
     *
     * @see HashMap#HashMap()
     */
    public SafeHashMap(final Class keyClass, final Class valueClass) {
        if (null == keyClass) throw new NullPointerException();
        if (null == valueClass) throw new NullPointerException();

        this.keyClass = keyClass;
        this.valueClass = valueClass;
    }

    /**
     * Constructs a new <code>SafeHashMap</code> which accepts any class of key
     * and value with the given <var>initialCapacity</var>.
     *
     * @param initialCapacity the initial capacity
     *
     * @see SafeHashMap#SafeHashMap(Class, Class, int)
     */
    public SafeHashMap(final int initialCapacity) {
        this(Object.class, Object.class, initialCapacity);
    }

    /**
     * Constructs a new <code>SafeHashMap</code> for a given <var>keyClass</var>
     * and <var>valueClass</var> with the given <var>initialCapacity</var>.
     *
     * @param keyClass the superclass of map keys
     * @param valueClass the superclass of map values
     * @param initialCapacity the initial capacity
     *
     * @see HashMap#HashMap(int)
     */
    public SafeHashMap(final Class keyClass, final Class valueClass,
            final int initialCapacity) {
        super(initialCapacity);

        if (null == keyClass) throw new NullPointerException();
        if (null == valueClass) throw new NullPointerException();

        this.keyClass = keyClass;
        this.valueClass = valueClass;
    }

    /**
     * Constructs a new <code>SafeHashMap</code> which accepts any class of key
     * and value with the given <var>initialCapacity</var> and
     * <var>loadFactor</var>.
     *
     * @param initialCapacity the initial capacity
     * @param loadFactor the load factor
     *
     * @see SafeHashMap#SafeHashMap(Class, Class, int, float)
     */
    public SafeHashMap(final int initialCapacity, final float loadFactor) {
        this(Object.class, Object.class, initialCapacity, loadFactor);
    }

    /**
     * Constructs a new <code>SafeHashMap</code> for a given <var>keyClass</var>
     * and <var>valueClass</var> with the given <var>initialCapacity</var> and
     * <var>loadFactor</var>.
     *
     * @param keyClass the superclass of map keys
     * @param valueClass the superclass of map values
     * @param initialCapacity the initial capacity
     * @param loadFactor the load factor
     *
     * @see HashMap#HashMap(int, float)
     */
    public SafeHashMap(final Class keyClass, final Class valueClass,
            final int initialCapacity, final float loadFactor) {
        super(initialCapacity, loadFactor);

        if (null == keyClass) throw new NullPointerException();
        if (null == valueClass) throw new NullPointerException();

        this.keyClass = keyClass;
        this.valueClass = valueClass;
    }

    /**
     * Constructs a new <code>SafeHashMap</code> which accepts any class of key
     * and value with the given <var>map</var>.
     *
     * @param map the map
     *
     * @see SafeHashMap#SafeHashMap(Class, Class, Map)
     */
    public SafeHashMap(final Map map) {
        this(Object.class, Object.class, map);
    }

    /**
     * Constructs a new <code>SafeHashMap</code> for a given <var>keyClass</var>
     * and <var>valueClass</var> with the given <var>map</var>.
     *
     * @param keyClass the superclass of map keys
     * @param valueClass the superclass of map values
     * @param map the map
     *
     * @see HashMap#HashMap(Map)
     */
    public SafeHashMap(final Class keyClass, final Class valueClass,
            final Map map) {
        this(keyClass, valueClass);

        putAll(map);
    }

    /**
     * {@inheritDoc}
     */
    public boolean containsKey(final Object key) {
        validateKey(key);

        return super.containsKey(key);
    }

    /**
     * {@inheritDoc}
     */
    public boolean containsValue(final Object value) {
        validateValue(value);

        return super.containsValue(value);
    }

    /**
     * {@inheritDoc}
     */
    public Object get(final Object key) {
        validateKey(key);

        if (!containsKey(key))
            throw new IllegalArgumentException("No such key: " + key);

        return super.get(key);
    }

    /**
     * {@inheritDoc}
     */
    public Object put(final Object key, final Object value) {
        validateKey(key);
        validateValue(value);

        return super.put(key, value);
    }

    /**
     * {@inheritDoc}
     */
    public Object remove(final Object key) {
        validateKey(key);

        if (!containsKey(key))
            throw new IllegalArgumentException("No such key: " + key);

        return super.remove(key);
    }

    private void validateKey(final Object key) {
        if (null == key) throw new NullPointerException("Null key");
        if (!keyClass.isAssignableFrom(key.getClass()))
            throw new ClassCastException(
                    "Key class not " + keyClass.getName() + ": " + key);
    }

    private void validateValue(final Object value) {
        if (null == value) throw new NullPointerException("Null value");
        if (!valueClass.isAssignableFrom(value.getClass()))
            throw new ClassCastException(
                    "Value class not " + valueClass.getName() + ": " + value);
    }
}
