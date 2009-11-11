/*
 * Created on Oct 1, 2004
 *
 * StudentItemResponseData.java
 */
package com.ctb.lexington.db.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ctb.lexington.data.ItemResponseVO;

/**
 * @author nate_cohen
 *
 * StudentItemResponseData
 */
public class StudentItemResponseData {
	private List studentItemResponseDetailList = new ArrayList();
	private Long studentTestHistoryId;
	

	public Long getStudentTestHistoryId() {
		return this.studentTestHistoryId;
	}

	public void setStudentTestHistoryId(Long studentTestHistoryId) {
		this.studentTestHistoryId = studentTestHistoryId;
		Iterator iterator = this.iterator();
		while (iterator.hasNext()) {
			StudentItemResponseDetails details = (StudentItemResponseDetails) iterator.next();
			details.setStudentTestHistoryId(studentTestHistoryId);
		}
	}

	public void setStudentItemResponseDetailList(
			List studentItemResponseDetailList) {
		this.studentItemResponseDetailList = studentItemResponseDetailList;
	}
	
	public Iterator iterator() {
		 return this.studentItemResponseDetailList.iterator();
	}
	
	public void addStudentItemResponseDetail(StudentItemResponseDetails details) {
		this.studentItemResponseDetailList.add(details);
	}
	
	public void addStudentItemResponseVO(ItemResponseVO response) {
		StudentItemResponseDetails details = new StudentItemResponseDetails();
		details.setConstructedResponse(response.getConstructedResponse());
		details.setItemId(response.getItemId());
		details.setItemSetId(response.getItemSetId());
		this.studentItemResponseDetailList.add(details);
	}
	
	public List getStudentItemResponseDetailList() {
		return this.studentItemResponseDetailList;
	}
}
