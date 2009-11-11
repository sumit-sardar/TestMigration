package com.ctb.lexington.db.utils;

import java.sql.Connection;

import javax.sql.DataSource;

public class DbUtils {
    private DbUtils() {
        // private ctor so that no one instantiates this class
    }

    public static DataSource createMockDataSource(final Connection connection) {
        return new MockDataSource(connection);
    }
}