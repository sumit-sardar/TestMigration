package com.ctb.contentcreator.processor;

public class StopableThread extends Thread {
	public StopableThread(String name) {
		super(name);
	}

	public static boolean foreStopped = false;

}
