package com.ctb.oas.normsdata;

import junit.framework.TestCase;

/**
 * @author Sreenivas  Ananthakrishna sreeni@thoughtworks.com
 */
public class TableTypeTest extends TestCase {
    private static final String NCToSEM = "NUMBER CORRECT - STANDARD ERROR OF MEASUREMENT";
    private static final String NCToSS = "NUMBER CORRECT - SCALE SCORE NORMS";

    public void testEnum() {
        assertEquals(TableType.NCToSEM, TableType.getTableType(NCToSEM));
        assertEquals(TableType.NCToSS, TableType.getTableType(NCToSS));
    }
}
