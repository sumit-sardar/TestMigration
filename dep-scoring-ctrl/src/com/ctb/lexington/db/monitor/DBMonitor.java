package com.ctb.lexington.db.monitor;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

//import org.apache.commons.lang.StringPrintWriter;

import com.ctb.lexington.util.SafeHashMap;

/**
 * A database connection monitor class that holds a map of opened connections. At any point in time,
 * a list of open connections (that were opened after reset was called) can be obtained to check for
 * connection leaks.
 * 
 * @author Karthik Chandrasekaraiah
 */
public class DBMonitor {
    private static final Map dbObjectMonitorMaps = new SafeHashMap(Class.class, Map.class);

    private DBMonitor() {
        // no one should instantiate this class
    }

    public static void objectOpened(final Trackable trackable) {
        if (trackable == null) {
            return;
        }
        if (!dbObjectMonitorMaps.containsKey(trackable.getClass())) {
            dbObjectMonitorMaps.put(trackable.getClass(), new HashMap());
        }
        Map objectMonitor = (Map) dbObjectMonitorMaps.get(trackable.getClass());
        objectMonitor.put(trackable, new Exception("Open - " + trackable.getClass()));
    }

    public static void objectClosed(final Trackable trackable) {
        if (trackable == null) {
            return;
        }
        if (dbObjectMonitorMaps.containsKey(trackable.getClass())) {
            Map objectMonitor = (Map) dbObjectMonitorMaps.get(trackable.getClass());
            objectMonitor.remove(trackable);
        }
    }

    public static void showAllOpenObjects() {
        System.err.println(stackTracesForAllOpenObjects());
    }

    public static void showAllOpenObjects(final Class trackableClass) {
        System.err.println(stackTracesForAllOpenObjects(trackableClass));
    }

    public static void reset() {
        dbObjectMonitorMaps.clear();
    }

    public static void reset(final Class trackableClass) {
        if (dbObjectMonitorMaps.containsKey(trackableClass)) {
            Map objectMonitor = (Map) dbObjectMonitorMaps.get(trackableClass);
            objectMonitor.clear();
            dbObjectMonitorMaps.remove(trackableClass);
        }
    }

    public static String stackTracesForAllOpenObjects() {
        if (dbObjectMonitorMaps.isEmpty())
            return "";
        final StringBuffer stackTraces = new StringBuffer();
        for (Iterator objMonitorIter = dbObjectMonitorMaps.keySet().iterator(); objMonitorIter
                .hasNext();) {
            Class trackableClass = (Class) objMonitorIter.next();
            stackTraces.append(stackTracesForAllOpenObjects(trackableClass));
        }
        return stackTraces.toString();
    }

    public static String stackTracesForAllOpenObjects(final Class trackableClass) {
        if (!dbObjectMonitorMaps.containsKey(trackableClass)) {
            return "";
        }
        final Map objectMonitor = (Map) dbObjectMonitorMaps.get(trackableClass);
        if (objectMonitor.isEmpty())
            return "";
        final StringBuffer stackTraces = new StringBuffer();
        stackTraces.append("\nStack traces for all open objects of type " + trackableClass + "\n");
        int i = 0;
        for (Iterator iter = objectMonitor.values().iterator(); iter.hasNext();) {
            stackTraces.append("\nObject " + ++i);
            Exception exception = (Exception) iter.next();
            final PrintWriter printWriter = new PrintWriter(System.out);
            exception.printStackTrace(printWriter);
            stackTraces.append(printWriter.toString());
        }
        stackTraces.append("\nEnd of all open objects of type " + trackableClass + "\n");
        return stackTraces.toString();
    }
}