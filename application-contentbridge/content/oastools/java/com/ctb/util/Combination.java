package com.ctb.util;

import java.math.*;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: mwshort, djrice
 * Date: Sep 26, 2003
 * Time: 12:52:24 PM
 * Sample for input
 *
 */
public class Combination {

    private ArrayList originalCollection = new ArrayList();
    private TreeMap combinations = new TreeMap();

    private int maxSize;

    public Combination(List collection) {
        this(collection, collection.size());
    }

    public Combination(List collection, int i) {
        maxSize = i;
        this.originalCollection.addAll(collection);
        buildMasks(originalCollection.size());
    }

    void buildMasks(long size) {
        for (int i = 1; i <= maxSize; i++) {
            combinations.put(new Integer(i), new ArrayList());
        }
        BigInteger totalCombinations =
            new BigInteger("2").pow((int) size).subtract(new BigInteger("1"));
        BigInteger counter = new BigInteger("1");
        final BigInteger increment = new BigInteger("1");

        while (!counter.equals(totalCombinations)) {
            addMask(counter);
            counter = counter.add(increment);
        }
        addMask(counter);
    }

    public void addMask(BigInteger maskValue) {
        if (maskValue.bitCount() > maxSize)
            return;

        List combinationsOfBitCountSize =
            (List) combinations.get(new Integer(maskValue.bitCount()));

        combinationsOfBitCountSize.add(maskValue);
    }

    public Iterator iterator(int combinationsOfSize) {
        return getCombsOfSize(combinationsOfSize).iterator();
    }

    List getCombsOfSize(int combinationsOfSize) {
        List combsOfSize = new ArrayList();

        if (combinationsOfSize == 0) {
            return combsOfSize;
        }
        List masks = (List) combinations.get(new Integer(combinationsOfSize));

        for (Iterator maskIter = masks.iterator(); maskIter.hasNext();) {
            combsOfSize.add(maskCollection((BigInteger) maskIter.next()));
        }
        return combsOfSize;
    }

    List getCombsOfSize(Integer combinationsOfSize) {
        return getCombsOfSize(combinationsOfSize.intValue());
    }

    public Iterator iterator() {
        List allCombs = new ArrayList();

        for (Iterator iter = combinations.keySet().iterator();
            iter.hasNext();
            ) {
            allCombs.addAll(getCombsOfSize((Integer) iter.next()));
        }
        return allCombs.iterator();
    }

    List maskCollection(BigInteger mask) {
        List combination = new ArrayList();
        BigInteger currentMask = mask;
        int lowestSetBit = mask.getLowestSetBit();

        if (mask.getLowestSetBit() == -1) {
            return combination;
        }
        while (lowestSetBit != -1) {
            combination.add(originalCollection.get(lowestSetBit));
            currentMask = currentMask.flipBit(lowestSetBit);
            lowestSetBit = currentMask.getLowestSetBit();
        }
        return combination;
    }

    public String toString() {
        return combinations.toString();
    }

    public long getCombinationCount(int ofSize) {
    	if (ofSize > ((Integer)combinations.lastKey()).intValue())
    		return 0;
        return ((List) combinations.get(new Integer(ofSize))).size();
    }

    public long getCombinationCount() {
        long count = 0;

        for (Iterator iter = combinations.values().iterator();
            iter.hasNext();
            ) {
            count += ((List) iter.next()).size();
        }
        return count;
    }
}
