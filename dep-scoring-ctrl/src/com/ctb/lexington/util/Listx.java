/*
 * Created on Apr 15, 2004
 *
 */
package com.ctb.lexington.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * @author arathore
 */
/**
 * Listx understands how to create lists from multiple arguments,
 * allowing not quite so compact syntax such as Listx.list("option1", "option2")
 * instead of the wordier syntax: Arrays.asList(new Object[] { "option1", "option2" }); or the ultra compact O.l("foo", "bar");
 */
public class Listx {
	private Listx() { } // no instances

	public static List list() {
		return new ArrayList();
	}

	public static List list(Object[] o) {
		return new ArrayList(Arrays.asList(o));
	}

	public static List listEmptyIfNull(Object[] o) {
		return o == null ? list() : list(o);
	}

	public static List list(Collection c) {
		return new ArrayList(c);
	}

	public static List list(Object o1) {
		return list(new Object[] {o1});
	}

	public static List list(Object o1, Object o2) {
		return list(new Object[] {o1, o2});
	}

	public static List list(Object o1, Object o2, Object o3) {
		return list(new Object[] {o1, o2, o3});
	}

	public static List list(Object o1, Object o2, Object o3, Object o4) {
		return list(new Object[] {o1, o2, o3, o4});
	}

	public static List list(Object o1, Object o2, Object o3, Object o4, Object o5) {
		return list(new Object[] {o1, o2, o3, o4, o5});
	}

	public static List list(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6) {
		return list(new Object[] {o1, o2, o3, o4, o5, o6});
	}

	public static List list(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7) {
		return list(new Object[] {o1, o2, o3, o4, o5, o6, o7});
	}

	public static List list(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8) {
		return list(new Object[] {o1, o2, o3, o4, o5, o6, o7, o8});
	}

	public static List list(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8, Object o9) {
		return list(new Object[] {o1, o2, o3, o4, o5, o6, o7, o8, o9});
	}

	public static List list(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8, Object o9, Object o10) {
		return list(new Object[] {o1, o2, o3, o4, o5, o6, o7, o8, o9, o10});
	}

	public static List list(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8, Object o9, Object o10, Object o11) {
		return list(new Object[] {o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11});
	}

	public static List list(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8, Object o9, Object o10, Object o11, Object o12) {
		return list(new Object[] {o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12});
	}

	public static List list(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8, Object o9, Object o10, Object o11, Object o12, Object o13) {
		return list(new Object[] {o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13});
	}

	public static List list(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8, Object o9, Object o10, Object o11, Object o12, Object o13, Object o14) {
		return list(new Object[] {o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14});
	}

	public static List list(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8, Object o9, Object o10, Object o11, Object o12, Object o13, Object o14, Object o15) {
		return list(new Object[] {o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15});
	}

	public static List list(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8, Object o9, Object o10, Object o11, Object o12, Object o13, Object o14, Object o15, Object o16) {
		return list(new Object[] {o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16});
	}

	public static List list(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8, Object o9, Object o10, Object o11, Object o12, Object o13, Object o14, Object o15, Object o16, Object o17) {
		return list(new Object[] {o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17});
	}

	public static List list(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8, Object o9, Object o10, Object o11, Object o12, Object o13, Object o14, Object o15, Object o16, Object o17,  Object o18) {
		return list(new Object[] {o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18});
	}

	public static List list(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8, Object o9, Object o10, Object o11, Object o12, Object o13, Object o14, Object o15, Object o16, Object o17,  Object o18, Object o19) {
		return list(new Object[] {o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19});
	}

	public static List list(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8, Object o9, Object o10, Object o11, Object o12, Object o13, Object o14, Object o15, Object o16, Object o17,  Object o18, Object o19, Object o20) {
		return list(new Object[] {o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20});
	}

	public static List list(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8, Object o9, Object o10, Object o11, Object o12, Object o13, Object o14, Object o15, Object o16, Object o17,  Object o18, Object o19, Object o20, Object o21) {
		return list(new Object[] {o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21});
	}

	public static List list(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8, Object o9, Object o10, Object o11, Object o12, Object o13, Object o14, Object o15, Object o16, Object o17,  Object o18, Object o19, Object o20, Object o21, Object o22) {
		return list(new Object[] {o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22});
	}

	public static List list(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8, Object o9, Object o10, Object o11, Object o12, Object o13, Object o14, Object o15, Object o16, Object o17,  Object o18, Object o19, Object o20, Object o21, Object o22, Object o23) {
		return list(new Object[] {o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22, o23});
	}

	public static List list(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8, Object o9, Object o10, Object o11, Object o12, Object o13, Object o14, Object o15, Object o16, Object o17,  Object o18, Object o19, Object o20, Object o21, Object o22, Object o23, Object o24) {
		return list(new Object[] {o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22, o23, o24});
	}

	public static List list(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8, Object o9, Object o10, Object o11, Object o12, Object o13, Object o14, Object o15, Object o16, Object o17,  Object o18, Object o19, Object o20, Object o21, Object o22, Object o23, Object o24, Object o25) {
		return list(new Object[] {o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22, o23, o24, o25});
	}

	public static List list(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8, Object o9, Object o10, Object o11, Object o12, Object o13, Object o14, Object o15, Object o16, Object o17,  Object o18, Object o19, Object o20, Object o21, Object o22, Object o23, Object o24, Object o25, Object o26) {
		return list(new Object[] {o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22, o23, o24, o25, o26});
	}

	public static List list(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8, Object o9, Object o10, Object o11, Object o12, Object o13, Object o14, Object o15, Object o16, Object o17, Object o18, Object o19, Object o20, Object o21, Object o22, Object o23, Object o24, Object o25, Object o26, Object o27) {
		return list(new Object[] {o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22, o23, o24, o25, o26, o27 });
	}

	public static List list(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8, Object o9, Object o10, Object o11, Object o12, Object o13, Object o14, Object o15, Object o16, Object o17, Object o18, Object o19, Object o20, Object o21, Object o22, Object o23, Object o24, Object o25, Object o26, Object o27, Object o28) {
		return list(new Object[] {o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22, o23, o24, o25, o26, o27, o28 });
	}

	public static List list(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8, Object o9, Object o10, Object o11, Object o12, Object o13, Object o14, Object o15, Object o16, Object o17, Object o18, Object o19, Object o20, Object o21, Object o22, Object o23, Object o24, Object o25, Object o26, Object o27, Object o28, Object o29) {
		return list(new Object[] {o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22, o23, o24, o25, o26, o27, o28, o29});
	}

	/**
	 * Return the first element of a list.
	 * @param l the list
	 * @return the first element
	 */
	public static Object first(List l) {
		if (l.isEmpty())
			throw new RuntimeException("Can't get first element of empty list!");
		return l.get(0);
	}

	public static Object car(List l) {
	    return first(l);
	}

	/**
	 * Return the only element in a collection.
	 * @param c the collection
	 * @return the only element
	 */
	public static Object only(Collection c) {
		if (c.size() != 1)
			throw new RuntimeException("expected single element, collection contains " + c.size());
		return first(Listx.list(c));
	}

	/**
	 * Return a copy of a list consisting of the first "max" elements.
	 * @param list to be constrained
	 * @param max the max size
	 * @return the list after constraint
	 */
	private static List truncatedList(List list, int max) {
		List results = Listx.list(list);
		if (results.size() <= max) return results;
		return results.subList(0, max);
	}

	/**
	 * Return an iterator over a copy of a list consisting of the first "max" elements.
	 * @param list to be constrained
	 * @param max the max size
	 * @return the constrained iterator
	 */
	public static Iterator truncatedIterator(List list, int max) {
		return truncatedList(list, max).iterator();
	}

	/**
	 * @param l
	 * @return
	 */
	public static Object last(List l) {
		if (l.isEmpty())
			throw new RuntimeException("Can't get last element of empty list!");
		return l.get(l.size() - 1);
	}

	/**
	 * @param contents the list to be searched
	 * @param toBeFound the list of search items
	 * @return true if the contents list has any of the toBeFound elements
	 */
	public static boolean containsAny(List contents, List toBeFound) {
		List copy = Listx.list(contents);
		copy.removeAll(toBeFound);
		return !copy.equals(contents);
	}

	public static void addInto(List l, BigDecimal d) {
		for (ListIterator i = l.listIterator(); i.hasNext();) {
			BigDecimal value = new BigDecimal((String) i.next());
			value = value.add(d).max(new BigDecimal(0).setScale(2));
			i.set(value.toString());
		}
	}

	/**
	 * Calculates vector A - vector B
	 * @param minuends the A elements
	 * @param subtrahends the B elements
	 * @return the vector formed by subtracting the minuends from the subtrahends.
	 */
	public static List subtract(List minuends, List subtrahends) {
		if (minuends.size() != subtrahends.size())
			throw new RuntimeException(
				"Cannot call subtract with diffently sized lists" +
				" (m: " + minuends + " s:" + subtrahends);
		List results = new ArrayList();

		for (Iterator m = minuends.iterator(), s = subtrahends.iterator(); m.hasNext();) {
			BigDecimal minuend = new BigDecimal((String) m.next());
			BigDecimal subtrahend = new BigDecimal((String) s.next());
			results.add(minuend.subtract(subtrahend).toString());
		}
		return results;
	}

	/**
	 * @param results
	 * @param fixedPortion
	 */
	public static void addTo(List results, BigDecimal fixedPortion) {
		addInto(results, fixedPortion);
		for (ListIterator i = results.listIterator(); i.hasNext();)
			i.set(new BigDecimal((String) i.next()));
	}

	/**
	 * Remove the last element of a list.
	 * @param list the list
	 */
	public static void removeLast(List list) {
		if (list.isEmpty())
			throw new RuntimeException("Can't remove last element of empty list!");
		list.remove(list.size() - 1);
	}

	public static String toString(List list) {
		if (list.size() < 2) return list.toString();
		return "\n" + Stringx.join(list, "\n") + "\n";
	}

	public static Object shift(List words) {
		return words.remove(0);
	}

	public static void unshift(List words, Object object) {
		words.add(0, object);
	}

	public static List allButFirst(List lat) {
		lat.remove(0);
		return lat;
	}

	public static Object maybeOnly(List list) {
		if (list.size() > 1) throw new RuntimeException("List too big for only!");
		if (list.isEmpty()) return null;
		return only(list);
	}

    public static List buildList(Object object, List list) {
        list.add(0, object);
        return list;
    }

}