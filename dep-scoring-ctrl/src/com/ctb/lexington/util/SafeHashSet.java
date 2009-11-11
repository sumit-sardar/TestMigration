package com.ctb.lexington.util;

import java.util.Collection;
import java.util.HashSet;

/**
 * @todo javadocs
 */
public class SafeHashSet extends HashSet {
    private final Class valueClass;

    public SafeHashSet() {
        this(Object.class);
    }

    public SafeHashSet(final Class valueClass) {
        this.valueClass = valueClass;
    }

    public SafeHashSet(final Collection ts) {
        this(Object.class, ts);
    }

    public SafeHashSet(final Class valueClass, final Collection ts) {
        this.valueClass = valueClass;

        addAll(ts);
    }

    public SafeHashSet(final int initialCapacity) {
        this(Object.class, initialCapacity);
    }

    public SafeHashSet(final Class valueClass, final int initialCapacity) {
        super(initialCapacity);

        this.valueClass = valueClass;
    }

    public SafeHashSet(final int initialCapacity, final float loadFactor) {
        this(Object.class, initialCapacity, loadFactor);
    }

    public SafeHashSet(final Class valueClass, final int initialCapacity,
            final float loadFactor) {
        super(initialCapacity, loadFactor);

        this.valueClass = valueClass;
    }

    public boolean add(final Object t) {
        validateValue(t);

        return super.add(t);
    }

    public boolean contains(final Object o) {
        validateValue(o);

        return super.contains(o);
    }

    public boolean remove(final Object o) {
        validateValue(o);

        return super.remove(o);
    }

    private void validateValue(final Object t) {
        if (null == t) throw new NullPointerException();
        if (!valueClass.isAssignableFrom(t.getClass()))
            throw new ClassCastException();
    }
}
