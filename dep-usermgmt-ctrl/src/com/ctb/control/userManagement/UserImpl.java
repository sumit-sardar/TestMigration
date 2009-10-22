package com.ctb.control.userManagement;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.apache.beehive.controls.api.bean.Control;

import com.ctb.bean.testAdmin.Role;
import com.ctb.exception.CTBBusinessException;
import com.ctb.exception.UserDataRetrivalException;
import com.ctb.exception.userManagement.UserDataNotFoundException;
import com.ctb.util.SQLutils;
import com.ctb.util.SimpleCache;
import com.ctb.util.userManagement.CTBConstants;

public class UserImpl {
	
	@Control()
	private com.ctb.control.db.Roles roles;
	
public Role[] getRoles() throws CTBBusinessException {
        
        Role admin = null;
        Role coord = null;
        Role accommodationCoordinator = null;
        Role proctor = null;
        Role[] allRole = null;
                
        try {
            
            String key = "USER_ROLES";
            allRole = (Role[]) SimpleCache.checkCache("roleArray", key, "manageUser");
            if (allRole == null) {
                String[] roleNames = {CTBConstants.ROLE_NAME_ADMINISTRATOR
                                    , CTBConstants.ROLE_NAME_ACCOMMODATIONS_COORDINATOR
                                    , CTBConstants.ROLE_NAME_COORDINATOR
                                    , CTBConstants.ROLE_NAME_PROCTOR};
                 
                String roleNamesArgs = SQLutils.convertArraytoString(roleNames);
                allRole = roles.getRoles(roleNamesArgs);
                final Map roleCmpMap = new HashMap();
                roleCmpMap.put(CTBConstants.ROLE_NAME_ADMINISTRATOR, new Integer(1));
                roleCmpMap.put(CTBConstants.ROLE_NAME_ACCOMMODATIONS_COORDINATOR, new Integer(2));
                roleCmpMap.put(CTBConstants.ROLE_NAME_COORDINATOR, new Integer(3));
                roleCmpMap.put(CTBConstants.ROLE_NAME_PROCTOR, new Integer(4));
                
                Comparator comparator = new Comparator() {
                        public int compare(Object o1, Object o2) {
                            
                            Role r1 = (Role) o1;
                            Role r2 = (Role) o2;
                            
                            Integer ind1 = (Integer) roleCmpMap.get(r1.getRoleName().toUpperCase());
                            Integer ind2 = (Integer) roleCmpMap.get(r2.getRoleName().toUpperCase());
                            
                            return ind1.intValue() - ind2.intValue();
                            
                        }
                    };
                    
                Arrays.sort(allRole, comparator);
                SimpleCache.cacheResult("roleArray", key, allRole, "manageUser");
            }
                        
            return allRole;
            
        } catch (SQLException se) {
                              
            UserDataNotFoundException dataNotfoundException = 
                                        new UserDataNotFoundException(
                                                "UserManagement.Failed");
                    
            dataNotfoundException.setStackTrace(se.getStackTrace());
            throw dataNotfoundException;
        } catch (Exception se) {
             UserDataRetrivalException dataRetrivalException = 
                                        new UserDataRetrivalException(
                                                "UserManagement.Failed");
            dataRetrivalException.setStackTrace(se.getStackTrace());
            throw dataRetrivalException;
        }
        
    }

}
