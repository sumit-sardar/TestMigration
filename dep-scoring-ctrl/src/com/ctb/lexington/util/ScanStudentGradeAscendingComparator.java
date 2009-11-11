package com.ctb.lexington.util;

/*
 * ScanStudentGradeAscendingComparator.java
 *
 * Copyright CTB/McGraw-Hill, 2005
 * CONFIDENTIAL
 *
 */

import com.ctb.lexington.data.ScanStudentVO;

/**
 * ScanStudentGradeComparator
 *
 * Copyright CTB/McGraw-Hill, 2005
 * CONFIDENTIAL
 *
 * @author <a href="mailto:jonathan_becker@ctb.com">Jon Becker</a>
 */
public class ScanStudentGradeAscendingComparator extends ScanStudentBaseComparator
{
	protected int compareScanStudents(ScanStudentVO s1_, ScanStudentVO s2_){
		int result = compareGrades(s1_, s2_);
		if(result == 0)
			result = compareNames(s1_, s2_);
		return result;
	}
}