package com.mhe.ctb.oas.BMTSync.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.mhe.ctb.oas.BMTSync.spring.jms.StudentMessageType;

public class OASClient {
    
	public static void main(String args[]) {
		StudentRestClient studentRestClient = new StudentRestClient();
		StudentMessageType message = new StudentMessageType();
		message.setCustomerId(15357);
		message.setStudentId(15918893);
		message.setUpdatedDateTime(Calendar.getInstance());
		List<StudentMessageType> messageList = new ArrayList<StudentMessageType>(1);
		messageList.add(message);
		studentRestClient.postStudentList(messageList);
	}
		
}
