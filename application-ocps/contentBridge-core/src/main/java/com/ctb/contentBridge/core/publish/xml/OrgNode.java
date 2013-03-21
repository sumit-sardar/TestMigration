package com.ctb.contentBridge.core.publish.xml;

import java.io.InvalidObjectException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author wmli
 */
public class OrgNode implements Serializable {
    private static Map orgNodes = new HashMap();
    private Long orgNodeId;
    private Long customerId;
    
    static final OrgNode ROOT = new OrgNode(new Long(1), new Long(1));
	static final OrgNode CTB = new OrgNode(new Long(2), new Long(2));

    public OrgNode(Long orgNodeId, Long customerId) {
        this.orgNodeId = orgNodeId;
        this.customerId = customerId;

        orgNodes.put(orgNodeId, this);
    }

    static public Iterator getAllOrgNodes() {
        return orgNodes.values().iterator();
    }

    static public OrgNode getOrgNode(Long orgNodeId) {
        return (OrgNode) orgNodes.get(orgNodeId);
    }

    private Object readResolve() throws InvalidObjectException {
        OrgNode item = getOrgNode(orgNodeId);

        if (item != null) {
            return item;
        } else {
            String msg = "Invalid deserialized orgNode:  orgNodeId = ";
            throw new InvalidObjectException(msg + orgNodeId);
        }
    }

    public boolean equals(Object obj) {
        if (obj instanceof OrgNode) {
            return this.orgNodeId.equals(((OrgNode) obj).getOrgNodeId());
        }

        return false;

    }
    public Long getCustomerId() {
        return customerId;
    }

    public Long getOrgNodeId() {
        return orgNodeId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public void setOrgNodeId(Long orgNodeId) {
        this.orgNodeId = orgNodeId;
    }

}
