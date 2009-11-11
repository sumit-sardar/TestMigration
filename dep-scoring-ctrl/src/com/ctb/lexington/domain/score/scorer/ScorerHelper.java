package com.ctb.lexington.domain.score.scorer;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author <a href="mailto:boxley@thoughtworks.com">B. K. Oxley (binkley)</a>
 * @version $Id$
 * @todo javadocs
 */
public final class ScorerHelper {
    /**
     * Calculates the ratio of <var>count</var> to <var>total</var> as an
     * integral percentage (quotient), the framtional portion (remainder) is
     * discarded.
     *
     * @param count the numerator
     * @param total the denominator
     *
     * @return the percentage ratio
     */
    public static int calculatePercentage(final int count, final int total) {
        // NB -- Must cast "count * 100" to float to ensure proper input for
        // round, else the result is too low for the boundary case of XX.5
        return Math.round((float) (count * 100) / total);
    }

    /**
     * Sum a collection of {@link Number} objects into an <code>int</code>.
     *
     * @param c the collection
     *
     * @return the integral sum
     */
    public static int sumAsInt(final Collection c) {
        int sumAsInt = 0;

        for (final Iterator it = c.iterator(); it.hasNext();) {
            sumAsInt += ((Number) it.next()).intValue();
        }

        return sumAsInt;
    }
}
