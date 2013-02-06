package com.ctb.contentBridge.core.upload.processor;

public class StopableThread extends Thread {
	public StopableThread(String name) {
		super(name);
	}

	public static boolean foreStopped = false;

}
