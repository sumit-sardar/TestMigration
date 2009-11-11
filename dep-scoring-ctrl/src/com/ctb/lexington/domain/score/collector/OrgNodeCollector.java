package com.ctb.lexington.domain.score.collector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ctb.lexington.db.ConnectionFactory;
import com.ctb.lexington.db.data.OrgNodeData;
import com.ctb.lexington.db.data.OrgNodeDetails;
import com.ctb.lexington.exception.DataException;
import com.ctb.lexington.util.SQLUtil;

/**
 * $Id$
 */
public class OrgNodeCollector {
    private final Connection conn;

    public OrgNodeCollector(Connection conn) {
        this.conn = conn;
    }

    public OrgNodeData collectOrgNodeData(Long oasRosterId) throws SQLException, DataException{
        final String sql = 
            "select " +
            "    cus.customer_id as customerId, " +
            "    cus.customer_name as customerName, " +
            "    node.org_node_id as orgNodeId, " +
            "    node.org_node_name as orgNodeName, " +
            "    onc.category_name as type " +
            "from " + 
            "    org_node_ancestor ona, " +
            "    org_node node, " + 
            "    test_roster ros, " + 
            "    org_node_category onc, " + 
            "    customer cus " +
            "where " +
            "    ona.org_node_id = ros.org_node_id " +
            "    and node.org_node_id = ona.ancestor_org_node_id " +
            "    and cus.customer_id = node.customer_id " +
            "    and onc.org_node_category_id = node.org_node_category_id " +
            "    and ros.test_roster_id = ? " +
            "    and cus.customer_id not in (1,2) " +
            "order by " +
            "    ona.number_of_levels desc";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setLong(1, oasRosterId.longValue());

            rs = ps.executeQuery();

            List nodes = new ArrayList();
            while (rs.next()) {
                OrgNodeDetails orgNodeDetails = new OrgNodeDetails();
                orgNodeDetails.setCustomerId(SQLUtil.getLong(rs, "customerId"));
                orgNodeDetails.setCustomerName(SQLUtil.getString(rs, "customerName"));
                orgNodeDetails.setOrgNodeId(SQLUtil.getLong(rs, "orgNodeId"));
                orgNodeDetails.setOrgNodeName(SQLUtil.getString(rs, "orgNodeName"));
                orgNodeDetails.setType(SQLUtil.getString(rs, "type"));
                nodes.add(orgNodeDetails);
            }
            OrgNodeData data = new OrgNodeData();
            data.setNodes((OrgNodeDetails[]) nodes.toArray(new OrgNodeDetails[0]));
            return data;
        } finally {
            SQLUtil.close(rs);
            ConnectionFactory.getInstance().release(ps);
        }
    }
}
/**
 * $Log$
 * Revision 1.2  2007/07/25 23:44:12  ncohen
 * merge OAS52 to trunk
 *
 * Revision 1.1.4.2  2007/06/14 23:58:53  ncohen
 * pulling it all together . . .
 *
 * Revision 1.1.4.1  2007/06/11 18:57:10  ncohen
 * Re-vamp org node persistence for IRS schema
 *
 * Revision 1.1  2007/01/30 01:31:48  ncohen
 * port scoring to 4.x platform
 *
 * Revision 1.1  2006/02/23 20:47:41  ncohen
 * create new module for legacy OAS app
 *
 * Revision 1.3  2005/05/03 21:29:09  ncohen
 * replace HEAD with iknow-millbarge
 *
 * Revision 1.1.2.7.2.3.4.4  2005/03/17 19:56:11  ncohen
 * collect and persist is_group value
 *
 * Revision 1.1.2.7.2.3.4.3  2005/02/18 22:00:44  ncohen
 * use faster non-union query for org node hierarchy retrieval
 *
 * Revision 1.1.2.7.2.3.4.2  2005/02/17 00:02:23  gawetski
 * Student MultiNode Assignments defect fix - GroupNode might be used.
 *
 * Revision 1.1.2.7.2.3.4.1  2005/02/15 20:51:44  gawetski
 * Student multiNode assignment defect fix - Use roster.org_node_id instead of the must recent student orgNode.
 *
 */