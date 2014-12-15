package com.mhe.ctb.oas.BMTSync.controller;

import java.util.Timer;
import java.util.TimerTask;

public class MyTimer extends TimerTask {
	Timer timer;
	int count = 0;
	
	public MyTimer() {
		
	}
	
	public MyTimer(Timer timer) {
		this.timer = timer;
	}
	
	
	public void toDo() {
		count++;
		System.out.println("Count:"+count+", Executing ToDo Method");
		StudentRestClient consumeService = new StudentRestClient();
		//consumeService.postStudentList();		

	}
	
	public void run() {
		toDo();
		if (count > 1) {
			timer.cancel();
		}
	}
}

