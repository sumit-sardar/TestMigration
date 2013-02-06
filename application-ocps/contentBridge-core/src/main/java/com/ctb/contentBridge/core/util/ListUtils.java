package com.ctb.contentBridge.core.util;


import java.util.*;


/**
 * useful for performing diffs against mapping logs, qa reports, etc.
 */
public class ListUtils {

    /**
     * @param list1
     * @param list2
     * @return a <code>List</code> containing items not in both list1 and list2
     */
    public static List diff(List list1, List list2) {
        List diff = new ArrayList();

        for (Iterator iterator = list1.iterator(); iterator.hasNext();) {
            Object o = (Object) iterator.next();

            if (!list2.contains(o)) {
                diff.add(o);
            }
        }
        for (Iterator iterator = list2.iterator(); iterator.hasNext();) {
            Object o = (Object) iterator.next();

            if (!list1.contains(o)) {
                diff.add(o);
            }
        }
        return diff;
    }

    /**
     * @param list
     * @return a <code>Set</code> containing any item appearing more than once in list
     */
    public static Set findDuplicates(List list) {
        Set set1 = new HashSet();
        Set duplicates = new HashSet();

        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            String mapping = (String) iterator.next();

            if (!set1.add(mapping)) {
                duplicates.add(mapping);
            }
        }
        return duplicates;
    }

    /**
     * @param list1
     * @param list2
     * @return a <code>List</code> containing any item found in both list1 and list2
     */
    public static List findIntersection(List list1, List list2) {
        List intersection = new ArrayList();

        for (Iterator iterator = list1.iterator(); iterator.hasNext();) {
            Object itemFrom1 = (Object) iterator.next();

            if (list2.contains(itemFrom1)) {
                intersection.add(itemFrom1);
            }
        }
        return intersection;
    }

    /**
     * @param map
     * @param items
     * @return a <code>List</code> containing any item in items which is a key in map
     */
    public static List findIntersectionWithMapKeys(Map map, List items) {
        List intersection = new ArrayList();

        for (Iterator iterator = map.keySet().iterator(); iterator.hasNext();) {
            Object o = (Object) iterator.next();

            if (items.contains(o)) {
                intersection.add(o);
            }
        }
        return intersection;
    }

    /**
     *
     * @param map
     * @param items
     * @return a <code>List</code> containing any item in items which is not a key in map
     */
    public static List findMissingFromMapKeys(Map map, List items) {
        List missing = new ArrayList();

        for (Iterator iterator = map.keySet().iterator(); iterator.hasNext();) {
            Object o = (Object) iterator.next();

            if (!items.contains(o)) {
                missing.add(o);
            }
        }
        return missing;
    }

    /**
     * @param values
     * @return a SQL 'IN' clause containing all values
     */
    public static String listToInClause(List values) {
        StringBuffer sql = new StringBuffer("in (");

        for (Iterator iterator = values.iterator(); iterator.hasNext();) {
            String entry = (String) iterator.next();

            sql.append("'" + entry + "'");
            if (iterator.hasNext()) {
                sql.append(", ");
            }
        }
        sql.append(")");
        return sql.toString();
    }

    public static String arrayToInClause(Long[] values) {
        StringBuffer sql = new StringBuffer("in (");

        for (int i = 0; i < values.length; i++){
            Long value = values[i];
            sql.append(value);
            if (i < values.length - 1) {
                sql.append(", ");
            }
        }
        sql.append(")");
        return sql.toString();
    }

}
