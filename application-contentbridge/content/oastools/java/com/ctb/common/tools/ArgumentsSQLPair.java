package com.ctb.common.tools;

/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Nov 12, 2003
 * Time: 4:14:02 PM
 * To change this template use Options | File Templates.
 */
public class ArgumentsSQLPair {
    Object[] arguments;
    String SQL;

    public ArgumentsSQLPair(Object[] arguments, String SQL) {
        this.arguments = arguments;
        this.SQL = SQL;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }

    public String getSQL() {
        return SQL;
    }

    public void setSQL(String SQL) {
        this.SQL = SQL;
    }
}
