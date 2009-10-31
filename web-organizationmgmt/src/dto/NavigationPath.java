package dto; 

/**
 *@author Tata Consultancy Services 
 *NavigationPath class maintains path list of organization.
 */

import java.util.ArrayList;
import java.util.List;
import manageOrganization.ManageOrganizationController.ManageOrganizationForm;

public class NavigationPath implements java.io.Serializable 
{ 
    static final long serialVersionUID = 1L;
    private static final String ACTION_DEFAULT = "defaultAction";
    
    private List actionList = null;
    private List orgNavInfoList = null;;
    
    public NavigationPath() {
        this.actionList = new ArrayList(); 
        this.orgNavInfoList = new ArrayList();
    }

	public void reset(String action) {  
        this.actionList = new ArrayList(); 
        addCurrentAction(action);
    }

	public void addCurrentAction(String action) {
                
        if ( this.actionList.size() > 0 ) {
            
            String currentAction = (String)this.actionList.get(0);
            
            if ( currentAction.equals(action) ) {
                
                return;
                
            }
            
        }
        
        this.actionList.add(0, action);        
	}

	public void addCurrentAction(String action, ManageOrganizationForm form) {
                
        addCurrentAction(action);

        setOrgNavInfo(action, form);
	}

    public void setOrgNavInfo(String action, ManageOrganizationForm form)
    {
        removeOrgNavInfo(action);
        
        addOrgNavInfo(action, form);
    }

    public void addOrgNavInfo(String action, ManageOrganizationForm form)
    {
        OrgNavInfo oni = new OrgNavInfo(action, 
                                        form.getSelectedOrgNodeId(), 
                                        form.getOrgSortColumn(),
                                        form.getOrgSortOrderBy(), 
                                        form.getOrgPageRequested(), 
                                        form.getOrgMaxPage());
        this.orgNavInfoList.add(oni);
    }
    
    public OrgNavInfo getOrgNavInfo(String action)
    {
        for ( int i = 0 ; i < this.orgNavInfoList.size() ; i++ ) {
            
            OrgNavInfo oni = (OrgNavInfo)this.orgNavInfoList.get(i);
            
            if ( oni.getAction().equals(action) ) { 
                               
                return oni;
                
            }
        }        
        return null;
    }

    public void removeOrgNavInfo(String action)
    {
        for ( int i = 0 ; i < this.orgNavInfoList.size() ; i++ ) {
            
            OrgNavInfo oni = (OrgNavInfo)this.orgNavInfoList.get(i);
            
            if ( oni.getAction().equals(action) ) { 
                               
                this.orgNavInfoList.remove(i);
                
            }
        }        
    }

	public void restoreOrgNavInfo(String action, ManageOrganizationForm form) {
        OrgNavInfo oni = getOrgNavInfo(action);
        
        if ( oni != null ) {
            
            form.setSelectedOrgNodeId(oni.getId());
            form.setOrgSortColumn(oni.getSortColumn());
            form.setOrgSortOrderBy(oni.getSortOrderBy());           
            form.setOrgPageRequested(oni.getPageRequested());
            form.setOrgMaxPage(oni.getMaxPage());            
    
        }        
    }
                
	public String getCurrentAction() {        
        String action = ACTION_DEFAULT;
        
        if (this.actionList.size() > 0) {
            
            action = (String)this.actionList.get(0);
            
        }
        
        return action;        
	}

	public String getPreviousAction() {        
        String action = ACTION_DEFAULT;
        
        if ( this.actionList.size() > 1 ) {
            
            action = (String)this.actionList.get(1);
            
        }
        
        return action;        
	}

	public String resetToPreviousAction() {        
        String action = ACTION_DEFAULT;
        
        if ( this.actionList.size() >= 2 ) {
            
            this.actionList.remove(0);
            action = (String)this.actionList.get(0);
            
        }
        
        return action;
	}
    
	public void setReturnActions(String firstLevel, String secondLevel) {  
        this.actionList = new ArrayList(); 
        this.actionList.add(ACTION_DEFAULT);        
        this.actionList.add(firstLevel);        
        this.actionList.add(secondLevel);        
    }
    
	public void removeAction(String action) {        
       
        for ( int i = 0 ; i < this.actionList.size() ; i++ ) {
            
            String str = (String)this.actionList.get(i);
            
            if ( str.equals(action) ) { 
                               
                this.actionList.remove(str);
                
            }
        }
	}
    
	public boolean findAction(String action) {        
        
        for ( int i = 0 ; i < this.actionList.size() ; i++ ) {
            
            String str = (String)this.actionList.get(i);
            
            if ( str.equals(action) ) { 
                               
                return true;
                
            }
            
        }
        
        return false;
	}
} 
