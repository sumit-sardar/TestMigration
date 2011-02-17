package com.ctb.util;


import java.util.*;

import org.jdom.*;


/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Sep 24, 2003
 * Time: 11:34:16 AM
 * Careful with this multi-set
 */
public class SortedMultiSet implements SortedSet {

    private JDOMElementComparator comparator = null;
    private SortedMap underlyingMap = new TreeMap();
    final static public int PERFORMANCE_THRESHOLD = 100;
    public SortedMultiSet(Comparator comparator) {
        this.comparator = (JDOMElementComparator) comparator;
    }

    /* setMethods*/
    public int size() {
        return flattenValues().size();
    }

    public boolean isEmpty() {
        return underlyingMap.isEmpty();
    }

    public boolean contains(Object obj) {

        if (underlyingMap.isEmpty()) {
            return false;
        }
        Object aKey = underlyingMap.keySet().iterator().next();
        Object listObject = ((List) underlyingMap.get(aKey)).iterator().next();

        if (obj.getClass().isInstance(listObject)
                || listObject.getClass().isInstance(obj)) {
            return (underlyingMap.get(comparator.getComparisonString((Element) listObject))
                    != null);
        }
        return false;
    }

    /**
     *
     * @return Iterator over all values in the Multiset, including duplications
     */
    public Iterator iterator() {
        return flattenValues().iterator();
    }

    /**
     *
     * @return Iterator over every 'element' in the Multiset, represented as SortedMultiSets of n quantity for an element
     */
    public Iterator subsetIterator() {
        List subsetList = new ArrayList();

        if (isEmpty()) {
            return subsetList.iterator();
        }
        String currentKey = (String) underlyingMap.firstKey();
        String successor = (String) successor(currentKey);

        while (successor != null) {
            subsetList.add(subSet(currentKey, successor));
            currentKey = successor;
            successor = (String) successor(currentKey);
        }
        subsetList.add(last());
        return subsetList.iterator();
    }

    public Object[] toArray() {
        return flattenValues().toArray();
    }

    public Object[] toArray(Object a[]) {
        return flattenValues().toArray(a);
    }

    public boolean add(Object obj) {
        return add((Element) obj);
    }

    public boolean add(Element element) {
        if (underlyingMap.isEmpty()) {
            return addNewBucket(element);
        }
        List bucket = (List) underlyingMap.get(comparator.getComparisonString(element));

        if (bucket == null) {
            return addNewBucket(element);
        }
        bucket.add(element);
        return true;
    }

    private boolean addNewBucket(Element element) {
        List newList = new ArrayList();

        newList.add(element);
        underlyingMap.put(comparator.getComparisonString(element), newList);
        return true;
    }

    public boolean remove(Object obj) {
        if (underlyingMap.remove(comparator.getComparisonString((Element) obj))
                != null) {
            return true;
        }
        return false;
    }

    public boolean containsAll(Collection c) {
        for (Iterator iter = c.iterator(); iter.hasNext();) {
            if (!contains(iter.next())) {
                return false;
            }
        }
        return true;
    }

    public boolean addAll(Collection c) {
        boolean added = false;

        for (Iterator iter = c.iterator(); iter.hasNext();) {
            if (add(iter.next())) {
                added = true;
            }
        }
        return added;
    }

    public boolean retainAll(Collection c) {
        throw new UnsupportedOperationException("This method has not been implemented");
    }

    public boolean removeAll(Collection c) {
        boolean changed = false;

        for (Iterator iter = c.iterator(); iter.hasNext();) {
            if (remove(iter.next())) {
                changed = true;
            }
        }
        return changed;
    }

    public void clear() {
        underlyingMap.clear();
    }

    /* sorted set methods*/
    public Comparator comparator() {
        return comparator;
    }

    public SortedSet subSet(Object fromElement, Object toElement) {
        return subSet(comparator.getComparisonString((Element) fromElement),
                comparator.getComparisonString((Element) toElement));
    }

    private SortedSet subSet(String keyStartInclusive, String keyEndExclusive) {
        SortedMultiSet sms = new SortedMultiSet(comparator());

        sms.underlyingMap = (SortedMap) underlyingMap.subMap(keyStartInclusive,
                keyEndExclusive);
        return sms;
    }

    public SortedSet headSet(Object toElement) {
        return headSet(comparator.getComparisonString((Element) toElement));
    }

    private SortedSet headSet(String key) {
        SortedMultiSet sms = new SortedMultiSet(comparator());

        sms.underlyingMap = underlyingMap.headMap(key);
        return sms;
    }

    public SortedSet tailSet(Object fromElement) {
        return tailSet(comparator.getComparisonString((Element) fromElement));
    }

    private SortedSet tailSet(String key) {
        SortedMultiSet sms = new SortedMultiSet(comparator());

        sms.underlyingMap = underlyingMap.tailMap(key);
        return sms;
    }

    /**
     * This actually returns the multi-element sub set of the first element
     * @return SortedMultiSet containing a quantity of the first element in multiset
     */
    public Object first() {
        if (underlyingMap.isEmpty()) {
            return this;
        }
        // Get the second key
        Iterator keyIterator = underlyingMap.keySet().iterator();

        keyIterator.next();
        String secondKey = (String) keyIterator.next();

        return headSet(secondKey);
    }

    /**
     * This actually returns the multi-element sub set of the first element
     * @return SortedMultiSet of one element represented n times
     */
    public Object last() {
        if (underlyingMap.isEmpty()) {
            return this;
        }
        return tailSet((String) underlyingMap.lastKey());
    }

    private List flattenValues() {
        ArrayList list = new ArrayList();

        for (Iterator iter = underlyingMap.values().iterator(); iter.hasNext();) {
            for (Iterator listIter = ((Collection) iter.next()).iterator(); listIter.hasNext();) {
                list.add(listIter.next());
            }
        }
        return list;
    }

    private Object successor(Object key) {
        Object successor = null;

        for (Iterator iter = underlyingMap.keySet().iterator(); iter.hasNext();) {
            if (key.equals(iter.next()) && iter.hasNext()) {
                successor = iter.next();
            }
        }
        return successor;
    }

    // public void printMultiSetInfo(SortedMultiSet set) {
    // System.out.println("******");
    // System.out.println("Set size: " + set.size() + "{");
    // for (Iterator setIter = set.iterator();setIter.hasNext();) {
    // Element curEle = (Element) setIter.next();
    // System.out.println("\t" + curEle.getAttributeValue("ID"));
    // }
    // System.out.println("}");
    // System.out.println("******");
    // }
    //
    // public void printAddingInfo(Object obj) {
    // Element ele = (Element) obj;
    // System.out.println("Trying to add" + ele.getAttributeValue("ID"));
    // }
    //

    // private void performanceAdd(Collection c) {
    // TreeMap map = new TreeMap();
    // int count = 0;
    // for (Iterator iter = c.iterator(); iter.hasNext();){
    // Element ele = (Element)iter.next();
    // String key = comparator.getComparisonString(ele);
    // if (!map.containsKey(key))
    // map.put(key,new ArrayList());
    // List bucket = (List)map.get(key);
    // bucket.add(ele);
    // count++;
    // }
    // System.out.println("Finished adding to buckets: " + count);
    // int items = 0;
    // for (Iterator iter = map.values().iterator();iter.hasNext();)
    // items += ((List)iter.next()).size();
    // System.out.println("Size of values: " + items);
    // System.out.println("Size of keys: " + map.keySet().size());
    // for (Iterator iter = map.keySet().iterator();iter.hasNext();)
    // System.out.println("\t\tkey:  " + iter.next());
    // underlyingMap.putAll(map);
    // }
}
