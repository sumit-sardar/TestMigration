package com.ctb.utils;

import java.util.ArrayList;
import java.util.List;

public class HeaderOrder {
	/**
	 * Returns the User Header Order
	 * 
	 * @return ArrayList<String>
	 */
		
	public static List<String> getUserHeaderOrderList() {
        
        List<String> headerArray = new ArrayList<String>();
        
        headerArray.add(Constants.REQUIREDFIELD_FIRST_NAME);
        headerArray.add(Constants.MIDDLE_NAME);
        headerArray.add(Constants.REQUIREDFIELD_LAST_NAME);
        headerArray.add(Constants.EMAIL);
        headerArray.add(Constants.REQUIREDFIELD_TIME_ZONE);
        headerArray.add(Constants.REQUIREDFIELD_ROLE);
        headerArray.add(Constants.EXT_SCHOOL_ID);
        headerArray.add(Constants.ADDRESS_LINE_1);
        headerArray.add(Constants.ADDRESS_LINE_2);
        headerArray.add(Constants.CITY);
        headerArray.add(Constants.STATE_NAME);
        headerArray.add(Constants.ZIP);
        headerArray.add(Constants.PRIMARY_PHONE);
        headerArray.add(Constants.SECONDARY_PHONE);
        headerArray.add(Constants.FAX);
        
        return headerArray;
    }
}
