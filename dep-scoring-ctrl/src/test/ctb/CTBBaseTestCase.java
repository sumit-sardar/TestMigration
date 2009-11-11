package test.ctb;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import junit.framework.TestCase;

public class CTBBaseTestCase extends TestCase {
    public CTBBaseTestCase(final String name) {
        super(name);
    }

    public String getName() {
        return getClass().getName() + "." + super.getName();
    }

    protected void setUp()
            throws Exception {
        super.setUp();

        System.setProperty("testRun", "true");
    }

    public static final Date createDate(final int year, final int month, final int day) {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        return new Date(cal.getTimeInMillis());
    }

    public static void assertNotEquals(final Object a, final Object b) {
        assertNotEquals(null, a, b);
    }

    public static void assertNotEquals(final String msg, final Object a, final Object b) {
        if (null == a) assertNull(msg, b);
        assertTrue(msg, !a.equals(b));
    }

    /**
     * @todo Validate this works and breaks nothing --bko
     */
    public static void XXXassertEquals(final List list1, final List list2) {
        assertEquals(null, list1, list2);
    }

    /**
     * @todo Validate this works and breaks nothing --bko
     */
    public static void XXXassertEquals(final String msg, final List list1,
            final List list2) {
        if (null == list1 && null == list2)
            return;

        assertNotNull(msg, list1);
        assertNotNull(msg, list2);
        assertEquals(msg, list1.size(), list2.size());

        for (int i = 0, x = list2.size(); i < x; ++i) {
            assertEquals(msg, list1.get(i), list2.get(i));
        }
    }

    /**
     * Uncovers any throwable thrown during {@link #runTest()} before calling
     * {@link #tearDown()}.  The default {@link TestCase#runBare()} swallows
     * exceptions.
     */
    public void runBare() throws Throwable {
        Throwable thrown = null;

        // for devs: we want some output to tbe flushed to the console when running from a cmd line
        System.out.println("Running: " + getName());

        setUp();

        try {
            runTest();
        } catch (final Throwable t) {
            thrown = t;
        } finally {
            try {
                tearDown();

                if (null != thrown)
                    throw thrown;

            } catch (Throwable t) {
                if (null != thrown)
                    throw thrown;
                else
                    throw t;
            }
        }
    }
}