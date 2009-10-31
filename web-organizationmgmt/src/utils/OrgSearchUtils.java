package utils; 

import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.testAdmin.Node;
import com.ctb.bean.testAdmin.UserData;
import com.ctb.bean.testAdmin.UserNode;
import com.ctb.bean.testAdmin.UserNodeData;
import com.ctb.control.organizationManagement.OrganizationManagement;
import com.ctb.exception.CTBBusinessException;
import com.ctb.widgets.bean.PagerSummary;
import dto.Organization;
import java.util.ArrayList;
import java.util.List;

public class OrgSearchUtils 
{ 
  
    /**
     * getTopOrgNodeIdsForUser
     */    
    public static Integer[] getTopOrgNodeIdsForUser(OrganizationManagement orgManagement, 
                                                    String userName) {    
        Integer[] orgNodeIds = null;
        return orgNodeIds;
    }
    
    
    /**
     * buildOrgList
     */    
    public static List buildOrgList(UserNodeData uData) 
    {
        ArrayList orgList = new ArrayList();
        if ( uData != null ) {
           
            UserNode[] uNode = uData.getUserNodes();
            if( uNode != null ) {
                
                for ( int i = 0 ; i < uNode.length ; i++ ) {
                    
                    UserNode userNode = uNode[i];
                    if ( userNode != null && userNode.getOrgNodeId() != null ) {
                        
                        Organization org = new Organization(userNode);
                        orgList.add(org);
                        
                    }
                }
            }
        }
        return orgList;
    }
    
    /**
     * getOrganization
     */    
    public static Organization getOrganizationProfile(Integer orgNodeId, 
                                                List orgList)
    {    
        for ( int i = 0 ; i < orgList.size() ; i++ ) {
            
            Organization org = (Organization) orgList.get(i);
            if (org.getOrgId().equals(orgNodeId)) {
                
                return org; 
                
            }
        }          
        return null;
    }
    
    
}
    


