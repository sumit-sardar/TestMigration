package utils; 

import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.testAdmin.User;
import com.ctb.bean.testAdmin.UserData;
import com.ctb.control.userManagement.UserManagement;
import com.ctb.exception.CTBBusinessException;
import com.ctb.widgets.bean.PagerSummary;
import dto.UserProfileInformation;
import java.util.ArrayList;
import java.util.List;

public class UserSearchUtils 
{ 
    private static int MAX_ITEMS = 10;
    
    /**
     * getTopOrgNodeIdsForUser
     */    
    public static Integer[] getTopOrgNodeIdsForUser(UserManagement userManagement, String userName)
    {    
        Integer[] orgNodeIds = null;/*
        try {    
           // orgNodeIds = userManagement.getTopOrgNodeIdsForUser(userName);   
        }      
        catch (Exception e) {
            e.printStackTrace();
        }*/
        return orgNodeIds;
    }
    
    
    /**
     * buildUserList
     */    
    public static List buildUserList(UserData uData) 
    {
        ArrayList userList = new ArrayList();
        if (uData != null) {
            User[] users = uData.getUsers();
            if(users != null){
                for (int i=0 ; i<users.length ; i++) {
                    User user = users[i];
                    if (user != null && user.getUserName() != null) {
                        UserProfileInformation userDetail = 
                                        new UserProfileInformation(user);
                        userList.add(userDetail);
                    }
                }
            }
        }
        return userList;
    }
    
    /**
     * buildUserPagerSummary
     */    
    public static PagerSummary buildUserPagerSummary(UserData userData, 
                                                Integer pageRequested) 
    {
        PagerSummary pagerSummary = new PagerSummary();
        pagerSummary.setCurrentPage(pageRequested);                
        pagerSummary.setTotalObjects(userData.getTotalCount());
        pagerSummary.setTotalPages(userData.getFilteredPages());
        pagerSummary.setTotalFilteredObjects(userData.getFilteredCount());  
                  
        return pagerSummary;
    }
    

    /**
     * searchAllUsersAtAndBelow
     */    
    public static UserData searchAllUsersAtAndBelow(
                                            UserManagement userManagement, 
                                            String userName, 
                                            PageParams page, 
                                            SortParams sort) throws CTBBusinessException
    {   
        UserData uData = null;
          
        uData = userManagement.getUsersVisibleToUser(userName, 
                                                    null, page, sort);
          
        return uData;
    }

    /**
     * searchUsersByProfile
     */    
    public static UserData searchUsersByProfile(UserManagement userManagement, 
                                            String userName, 
                                            FilterParams filter, 
                                            PageParams page, 
                                            SortParams sort) throws CTBBusinessException
    {   
        UserData userData = null;
            
        userData = userManagement.getUsersVisibleToUser(userName, 
                                                        filter, page, sort);
        return userData;
    }
    
    /**
     * searchUsersByOrgNode
     */    
    public static UserData searchUsersByOrgNode(
                                        UserManagement userManagement, 
                                        String userName, 
                                        Integer orgNodeId,
                                        FilterParams filter, 
                                        PageParams page, SortParams sort)
    {    
        UserData uData = null;
        
        try {    
             uData = userManagement.getUsersAtNode(userName, 
                                            orgNodeId, filter, page, sort);
        }
        catch (Exception e) {
            e.printStackTrace();
        }    
        return uData;
    }


     /**
     * getUserProfileInformation
     */    
    public static UserProfileInformation getUserProfileInformation(
                                        UserManagement userManagement, 
                                        String loginUserName, 
                                        String selectedUserName) throws CTBBusinessException
    {    
        UserProfileInformation userProfileInfo = new UserProfileInformation();         
        User user = userManagement.getUser(loginUserName, 
                                                selectedUserName);
        userProfileInfo = new UserProfileInformation(user);   
        
        return userProfileInfo;
    }

     /**
     * getUserProfileInformation
     */    
    public static UserProfileInformation getUserProfileInformation(
                                                Integer userId, 
                                                List userList)
    {    
        for (int i = 0 ; i < userList.size() ; i++) {
            UserProfileInformation upi = 
                            (UserProfileInformation) userList.get(i);
            if (upi.getUserId().equals(userId))
                return upi; 
        }          
        return null;
    }
    
    
    public static UserProfileInformation getUserNameFromList(Integer userId, List userList) {
        for (int i = 0; i < userList.size(); i++) {
            UserProfileInformation user = 
                                (UserProfileInformation) userList.get(i);
            if (user.getUserId().equals(userId))
                return user; 
        }
        return null;
    }
    
    public static void addUserInUserList(UserProfileInformation userProfile, 
                                            List userList) {
        userList.add(userProfile.createClone());
    }
    
} 
