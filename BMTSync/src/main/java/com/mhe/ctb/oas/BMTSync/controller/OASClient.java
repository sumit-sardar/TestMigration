package com.mhe.ctb.oas.BMTSync.controller;

import java.util.Calendar;
import java.util.Date;

public class OASClient {
    
	public static void main(String args[]) {
		Calendar date = Calendar.getInstance();
		StudentRestClient studentRestClient = new StudentRestClient();
		studentRestClient.postStudentList(15357, 15918893, date);
	}
		
}
