package com.ctb.lexington.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * <pre>class ScorableItemRecordIndex extends IndexSetMap {
 *     public ScorableItemRecordIndex() {
 *         super(String.class, ScorableItemRecord.class, new Mapper() {
 *             public Object getKeyFor(final Object value) {
 *                 return ((ScorableItemRecord) value).getScoreTypeCode();
 *             }
 *         });
 *     }
 * }</pre>
 *
 * @author <a href="mailto:boxley@thoughtworks.com">B. K. Oxley (binkley)</a>
 * @version $Id$
 */
public class IndexSetMap extends AutoHashMap {
    public static interface Mapper {
        public Object getKeyFor(final Object value);
    }

    private final Mapper mapper;

    public IndexSetMap(final Class keyClass, final Class valueClass,
            final Mapper mapper) {
        super(keyClass, Set.class, new SafeHashSet(valueClass).getClass());

        this.mapper = mapper;
    }

    public IndexSetMap(final Class keyClass, final Class valueClass,
            final Mapper mapper, final Collection values) {
        this(keyClass, valueClass, mapper);

        addAll(values);
    }

    public void add(final Object value) {
        ((Set) get(mapper.getKeyFor(value))).add(value);
    }

    public void addAll(final Collection values) {
        for (final Iterator it = values.iterator(); it.hasNext();)
            add(it.next());
    }

    public Set getAll(final Object key) {
        return (Set) get(key);
    }
}
