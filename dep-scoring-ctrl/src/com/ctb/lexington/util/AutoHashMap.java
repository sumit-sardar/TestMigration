package com.ctb.lexington.util;

import java.util.Map;

/**
 * @todo javadocs
 */
public class AutoHashMap extends SafeHashMap {
    private final Class createClass;

    /**
     * Constructs a new <code>AutoHashMap</code> for a given <var>keyClass</var>
     * and <var>valueClass</var> and which automatically creates
     * <var>valueClass</var> values.
     *
     * @param keyClass the superclass of map keys
     * @param valueClass the superclass of map values
     *
     * @see AutoHashMap#AutoHashMap(Class, Class, Class)
     */
    public AutoHashMap(final Class keyClass, final Class valueClass) {
        this(keyClass, valueClass, valueClass);
    }

    /**
     * Constructs a new <code>AutoHashMap</code> which accepts any class of key
     * and value and which automatically creates <var>createClass</var> values.
     *
     * @param createClass the class of automatically created values
     *
     * @see AutoHashMap#AutoHashMap(Class, Class, Class)
     */
    public AutoHashMap(final Class createClass) {
        this(Object.class, Object.class, createClass);
    }

    /**
     * Constructs a new <code>SafeMap</code> for a given <var>keyClass</var> and
     * <var>valueClass</var> and which automatically creates
     * <var>createClass</var> values.
     *
     * @param keyClass the superclass of map keys
     * @param valueClass the superclass of map values
     * @param createClass the class of auto-created values
     */
    public AutoHashMap(final Class keyClass, final Class valueClass,
            final Class createClass) {
        super(keyClass, valueClass);

        this.createClass = createClass;
    }

    public AutoHashMap(final Class keyClass, final Class valueClass,
            final int initialCapacity) {
        this(keyClass, valueClass, valueClass, initialCapacity);
    }

    public AutoHashMap(final Class createClass, final int initialCapacity) {
        this(Object.class, Object.class, createClass, initialCapacity);
    }

    public AutoHashMap(final Class keyClass, final Class valueClass,
            final Class createClass, final int initialCapacity) {
        super(keyClass, valueClass, initialCapacity);

        this.createClass = createClass;
    }

    public AutoHashMap(final Class keyClass, final Class valueClass,
            final int initialCapacity, final int loadFactor) {
        this(keyClass, valueClass, valueClass, initialCapacity, loadFactor);
    }

    public AutoHashMap(final Class createClass, final int initialCapacity,
            final int loadFactor) {
        this(Object.class, Object.class, createClass, initialCapacity,
                loadFactor);
    }

    public AutoHashMap(final Class keyClass, final Class valueClass,
            final Class createClass, final int initialCapacity,
            final int loadFactor) {
        super(keyClass, valueClass, initialCapacity, loadFactor);

        this.createClass = createClass;
    }

    public AutoHashMap(final Class keyClass, final Class valueClass,
            final Map map) {
        this(keyClass, valueClass, valueClass, map);
    }

    public AutoHashMap(final Class createClass, final Map map) {
        this(Object.class, Object.class, createClass, map);
    }

    public AutoHashMap(final Class keyClass, final Class valueClass,
            final Class createClass, final Map map) {
        this(keyClass, valueClass, createClass);

        putAll(map);
    }

    /**
     * {@inheritDoc}
     */
    public Object get(final Object key) {
        if (!containsKey(key)) put(key, create());

        return super.get(key);
    }

    private Object create() {
        try {
            return createClass.newInstance();

        } catch (InstantiationException e) {
            throw new RuntimeException("Cannot create instance of "
                    + createClass.getName(), e);

        } catch (IllegalAccessException e) {
            throw new RuntimeException("Cannot create instance of "
                    + createClass.getName(), e);
        }
    }
}
