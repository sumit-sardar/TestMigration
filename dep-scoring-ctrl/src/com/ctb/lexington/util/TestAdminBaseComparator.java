package com.ctb.lexington.util;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

import com.ctb.lexington.data.TestAdminVO;

/**
 * TestAdminBaseComparator
 *
 * Copyright CTB/McGraw-Hill, 2005
 * CONFIDENTIAL
 *
 * @author <a href="mailto:Jonathan_Becker@ctb.com">Jon Becker</a>
 * @version
 */
public abstract class TestAdminBaseComparator implements Comparator
{
    public int compare( Object o1, Object o2 ){
    	if((o1 instanceof TestAdminVO) && (o2 instanceof TestAdminVO)){
	        return compareTestAdmins((TestAdminVO) o1, (TestAdminVO)o2);
    	}
    	else return 0;
    }
    
    protected abstract int compareTestAdmins(TestAdminVO admin1_, TestAdminVO admin2_);
    
    protected int compareTestAdminName(TestAdminVO tab1_, TestAdminVO tab2_){
    	return standardize(tab1_.getTestAdminName()).compareTo(standardize(tab2_.getTestAdminName()));
    }
    
    protected int compareStartDate(TestAdminVO tab1_, TestAdminVO tab2_){
    	return compareCalendar(tab1_.getLoginStartDate(), tab2_.getLoginStartDate());
    }
    
    protected int compareCalendar(Calendar c1_, Calendar c2_){
    	return c1_.getTime().compareTo(c2_.getTime());
    }
    
    protected String standardize(String value_){
    	String result = "";
    	if(value_ != null)
    		result = value_.toLowerCase();
    	return result;
    }
}