package com.ctb.lexington.util;

import java.util.Collection;
import java.util.Iterator;

/**
 * <pre>class ItemIndex extends IndexMap {
 *     public ItemIndex() {
 *         super(String.class, ItemVO.class, new Mapper() {
 *             public Object getKeyFor(final Object value) {
 *                 return ((ItemVO) value).getItemId();
 *             }
 *         });
 *     }
 * }</pre>
 *
 * @author <a href="mailto:boxley@thoughtworks.com">B. K. Oxley (binkley)</a>
 * @version $Id$
 */
public class IndexMap extends SafeHashMap {
    public static interface Mapper {
        public Object getKeyFor(final Object value);
    }

    private final Mapper mapper;

    public IndexMap(final Class keyClass, final Class valueClass,
            final Mapper mapper) {
        super(keyClass, valueClass);

        this.mapper = mapper;
    }

    public IndexMap(final Class keyClass, final Class valueClass,
            final Mapper mapper, final Collection values) {
        this(keyClass, valueClass, mapper);

        addAll(values);
    }

    public void add(final Object value) {
        put(mapper.getKeyFor(value), value);
    }

    public void addAll(final Collection values) {
        for (final Iterator it = values.iterator(); it.hasNext();)
            add(it.next());
    }
}
