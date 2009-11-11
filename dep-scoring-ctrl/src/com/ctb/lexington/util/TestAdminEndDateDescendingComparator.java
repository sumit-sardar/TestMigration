package com.ctb.lexington.util;

import com.ctb.lexington.data.TestAdminVO;

/**
 * TestAdminStartDateAscendingComparator
 *
 * Copyright CTB/McGraw-Hill, 2005
 * CONFIDENTIAL
 *
 * @author <a href="mailto:Jonathan_Becker@ctb.com">Jon Becker</a>
 * @version
 */
public class TestAdminEndDateDescendingComparator extends TestAdminBaseComparator
{
	protected int compareTestAdmins(TestAdminVO admin1_, TestAdminVO admin2_){
		int result = compareCalendar(admin2_.getLoginEndDate(), admin1_.getLoginEndDate());
		if(result == 0)
			result = compareTestAdminName(admin1_, admin2_);
		return result;
	}
}