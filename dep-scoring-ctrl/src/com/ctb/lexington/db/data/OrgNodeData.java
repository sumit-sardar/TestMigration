package com.ctb.lexington.db.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ctb.lexington.exception.DataException;
import com.ctb.lexington.util.SafeHashMap;

public class OrgNodeData {
    private Long orgNodeId;
    private OrgNodeDetails [] nodes;

    public Long getOrgNodeId() {
        return this.orgNodeId;
    }
    
    public void setOrgNodeId (Long orgNodeId) {
        this.orgNodeId = orgNodeId;
    }

    public void setNodes(OrgNodeDetails [] nodes) {
        this.nodes = nodes;
    }
    
    public OrgNodeDetails [] getNodes() {
        return this.nodes;
    }
}