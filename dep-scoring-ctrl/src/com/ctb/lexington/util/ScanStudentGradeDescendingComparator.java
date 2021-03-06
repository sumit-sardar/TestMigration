package com.ctb.lexington.util;

/*
 * ScanStudentGradeDescendingComparator.java
 *
 * Copyright CTB/McGraw-Hill, 2005
 * CONFIDENTIAL
 *
 */

import com.ctb.lexington.data.ScanStudentVO;

/**
 * ScanStudentGradeDescendingComparator
 *
 * Copyright CTB/McGraw-Hill, 2005
 * CONFIDENTIAL
 *
 * @author <a href="mailto:jonathan_becker@ctb.com">Jon Becker</a>
 */
public class ScanStudentGradeDescendingComparator extends ScanStudentBaseComparator
{
	protected int compareScanStudents(ScanStudentVO s1_, ScanStudentVO s2_){
		int result = compareGrades(s2_, s1_);
		if(result == 0)
			result = compareNames(s1_, s2_);
		return result;
	}
}