package dto; 

import java.util.ArrayList;
import java.util.List;

public class NavigationPath implements java.io.Serializable 
{ 
    static final long serialVersionUID = 1L;
    private static final String ACTION_DEFAULT        = "defaultAction";
    
    private List actionList = null;

    public NavigationPath() 
    {
        this.actionList = new ArrayList(); 
    }

	public void reset(String action) 
    {  
        this.actionList = new ArrayList(); 
        addCurrentAction(action);
    }

	public void addCurrentAction(String action) 
    {        
        if (this.actionList.size() > 0) {
            String currentAction = (String)this.actionList.get(0);
            if (currentAction.equals(action))
                return;
        }
        this.actionList.add(0, action);        
	}
        
	public String getCurrentAction() 
    {        
        String action = ACTION_DEFAULT;
        if (this.actionList.size() > 0) {
            action = (String)this.actionList.get(0);
        }
        return action;        
	}

	public String getPreviousAction() 
    {        
        String action = ACTION_DEFAULT;
        if (this.actionList.size() > 1) {
            action = (String)this.actionList.get(1);
        }
        return action;        
	}

	public String resetToPreviousAction() 
    {        
        String action = ACTION_DEFAULT;
        if (this.actionList.size() >= 2) {
            this.actionList.remove(0);
            action = (String)this.actionList.get(0);
        }
        return action;
	}
    
	public void setReturnActions(String firstLevel, String secondLevel) 
    {  
        this.actionList = new ArrayList(); 
        this.actionList.add(ACTION_DEFAULT);        
        this.actionList.add(firstLevel);        
        this.actionList.add(secondLevel);        
    }
    
	public void removeAction(String action) 
    {        
        for (int i=0 ; i<this.actionList.size() ; i++) {
            String str = (String)this.actionList.get(i);
            if (str.equals(action)) {                
                this.actionList.remove(str);
            }
        }
	}
    
	public boolean findAction(String action) 
    {        
        for (int i=0 ; i<this.actionList.size() ; i++) {
            String str = (String)this.actionList.get(i);
            if (str.equals(action)) {                
                return true;
            }
        }
        return false;
	}
} 
