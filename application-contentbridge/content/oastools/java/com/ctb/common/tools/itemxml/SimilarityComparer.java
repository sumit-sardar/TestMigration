/*
 * Created on Dec 4, 2003
 *
 */
package com.ctb.common.tools.itemxml;

import java.util.Comparator;

import org.apache.commons.lang.StringUtils;

class SimilarityComparer implements Comparator {

    private final String stimuliRoot;

    SimilarityComparer(String root) {
        this.stimuliRoot = root;
    }

    public int compare(Object o1, Object o2) {
        double s1 = getSimilarity(stimuliRoot, (String) o1);
        double s2 = getSimilarity(stimuliRoot, (String) o2);

        if (s1 == s2) {
            return ((String) o1).compareTo((String) o2);
        }

        if (s1 < s2)
            return -1;
        return 1;
    }

    public boolean equals(Object o1, Object o2) {
        return o1.equals(o2);
    }

    /**
     * Returns similarty on a scale of 0.0 to 1.0 where
     * Similarity = (maxLength(str1, str2) - LevenshteinDistance)/maxLength(str1, str2)
     */
//    private static double getDissimilarity(String str1, String str2) {
//        if ((str1 == null) || (str2 == null))
//            return 1.0;
//        double maxLength = Math.max(str1.length(), str2.length());
//        if (maxLength == 0.0)
//            return 0.0;
//        double dissimilarity =
//            (double) StringUtils.getLevenshteinDistance(str2, str1) / maxLength;
//        return dissimilarity;
//    }

    public static double getSimilarity(String str1, String str2) {
        if ((str1 == null) || (str2 == null))
            return 0.0;
        double maxLength = Math.max(str1.length(), str2.length());
        if (maxLength == 0.0)
            return 1.0;
        double similarity =
            ((double) (maxLength
                - StringUtils.getLevenshteinDistance(str2, str1)))
                / maxLength;
        return similarity;
    }
}