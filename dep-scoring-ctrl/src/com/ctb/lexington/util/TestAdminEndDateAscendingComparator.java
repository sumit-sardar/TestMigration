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
public class TestAdminEndDateAscendingComparator extends TestAdminBaseComparator
{
	protected int compareTestAdmins(TestAdminVO admin1_, TestAdminVO admin2_){
		int result = compareCalendar(admin1_.getLoginEndDate(), admin2_.getLoginEndDate());
		if(result == 0)
			result = compareTestAdminName(admin1_, admin2_);
		return result;
	}
}