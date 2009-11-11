package com.ctb.lexington.db.data;

import java.sql.SQLException;

public interface OrgNodeDetailVisitor {
    public void visitOrgNodeDetail(OrgNodeDetails detail) throws SQLException;
}
