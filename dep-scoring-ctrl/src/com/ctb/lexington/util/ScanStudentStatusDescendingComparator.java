package com.ctb.lexington.util;

/*
 * ScanStudentStatusDescendingComparator.java
 *
 * Copyright CTB/McGraw-Hill, 2005
 * CONFIDENTIAL
 *
 */

import com.ctb.lexington.data.ScanStudentVO;

/**
 * ScanStudentStatusDescendingComparator
 *
 * Copyright CTB/McGraw-Hill, 2005
 * CONFIDENTIAL
 *
 * @author <a href="mailto:jonathan_becker@ctb.com">Jon Becker</a>
 */
public class ScanStudentStatusDescendingComparator extends ScanStudentBaseComparator
{
	protected int compareScanStudents(ScanStudentVO s1_, ScanStudentVO s2_){
	    String str1 = s1_.getScanStudentStatus().equals(ScanStudentVO.STUDENT_STATUS_SUCCESS) ? "matched" : "unmatched";
	    String str2 = s2_.getScanStudentStatus().equals(ScanStudentVO.STUDENT_STATUS_SUCCESS) ? "matched" : "unmatched";
		int result = str2.compareTo(str1);
		if(result == 0)
			result = compareNames(s1_, s2_);
		return result;
	}
}