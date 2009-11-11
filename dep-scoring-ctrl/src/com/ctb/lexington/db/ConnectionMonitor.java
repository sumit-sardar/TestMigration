package com.ctb.lexington.db;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class ConnectionMonitor {
    
    private static Map prepareStatementStackTraceMap = new HashMap();

    public static void statementPrepared(PreparedStatementDecorator preparedStatement) {
        try{
            throw new ConnectionMonitorException();
        }catch(ConnectionMonitorException exc){
            prepareStatementStackTraceMap.put(preparedStatement , exc);
        }
    }

    public static void statementClosed(PreparedStatementDecorator preparedStatement)  {
        ConnectionMonitorException exception = (ConnectionMonitorException) prepareStatementStackTraceMap.get(preparedStatement);
        if(exception == null)
            System.err.println("preapred statement closed before its opened");
        prepareStatementStackTraceMap.remove(preparedStatement);
    }

    public static void reset() {
        prepareStatementStackTraceMap.clear();
    }

    public static void showAllOpenStatemntss() {
        if(prepareStatementStackTraceMap.isEmpty())
            return;
        System.err.println("Stack traces for all open statements................");
        for (Iterator iter = prepareStatementStackTraceMap.values().iterator(); iter.hasNext();) {
            ConnectionMonitorException exception = (ConnectionMonitorException) iter.next();
            exception.printStackTrace();
        }
        System.err.println(".....................End of All Open Statements");
    }
    
}
