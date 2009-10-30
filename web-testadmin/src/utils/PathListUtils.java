package utils; 

import com.ctb.widgets.bean.PathListEntry;
import java.util.List;

public class PathListUtils 
{ 
    public static final String TOP = "Top";    
    
    public static boolean adjustOrgNodePath(List orgNodes, Integer orgNodeId, String orgNodeName)
    {
        boolean ret = false;

        if ((orgNodes == null) || (orgNodeId == null) || (orgNodeName == null))
            return ret;
            
        if (! findNodeId(orgNodes, orgNodeId)) {
            addNode(orgNodes, orgNodeId, orgNodeName);
            ret = true;
        }
        else {
            Integer lastNodeId = findLastNodeId(orgNodes);
            if (orgNodeId.intValue() != lastNodeId.intValue()) {
                removeDescendentNodes(orgNodes, orgNodeId);    
                ret = true;
            }
        }
        return ret;
    }
    public static boolean findNodeId(List orgNodes, Integer orgNodeId) 
    {
        for (int i=0 ; i<orgNodes.size() ; i++) {
            PathListEntry node = (PathListEntry)orgNodes.get(i);
            if (orgNodeId.equals(node.getValue())) 
                return true;
        }        
        return false;
    }
    public static void addNode(List orgNodes, Integer orgNodeId, String orgNodeName) 
    {
        PathListEntry node = new PathListEntry();
        node.setLabel(orgNodeName);
        node.setValue(orgNodeId);
        orgNodes.add(node);
    }
    public static Integer findLastNodeId(List orgNodes) 
    {
        PathListEntry node = findLastNode(orgNodes);
        return node.getValue();
    }
    public static PathListEntry findLastNode(List orgNodes) 
    {
        int index = orgNodes.size() - 1;
        PathListEntry node = (PathListEntry)orgNodes.get(index);
        return node;
    }
    public static void removeDescendentNodes(List orgNodes, Integer orgNodeId) 
    {
        for (int i=orgNodes.size()-1 ; i>=0 ; i--) {
            PathListEntry node = (PathListEntry)orgNodes.get(i);
            if (orgNodeId.equals(node.getValue())) 
                break;
            else 
                orgNodes.remove(i);
        }
    }
} 
