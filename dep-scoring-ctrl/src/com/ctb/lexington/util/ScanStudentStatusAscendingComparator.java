package com.ctb.lexington.util;

/*
 * ScanStudentStatusAscendingComparator.java
 *
 * Copyright CTB/McGraw-Hill, 2005
 * CONFIDENTIAL
 *
 */

import com.ctb.lexington.data.ScanStudentVO;

/**
 * ScanStudentStatusAscendingComparator
 *
 * Copyright CTB/McGraw-Hill, 2005
 * CONFIDENTIAL
 *
 * @author <a href="mailto:jonathan_becker@ctb.com">Jon Becker</a>
 */
public class ScanStudentStatusAscendingComparator extends ScanStudentBaseComparator
{
	protected int compareScanStudents(ScanStudentVO s1_, ScanStudentVO s2_){
	    String str1 = s1_.getScanStudentStatus().equals(ScanStudentVO.STUDENT_STATUS_SUCCESS) ? "matched" : "unmatched";
	    String str2 = s2_.getScanStudentStatus().equals(ScanStudentVO.STUDENT_STATUS_SUCCESS) ? "matched" : "unmatched";
		int result = str1.compareTo(str2);
		if(result == 0)
			result = compareNames(s1_, s2_);
		return result;
	}
}