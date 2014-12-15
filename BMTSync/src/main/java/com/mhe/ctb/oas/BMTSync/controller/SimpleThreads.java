package com.mhe.ctb.oas.BMTSync.controller;

public class SimpleThreads {

	 // Display message, preceded by the thread name
	static void threadMessage(String message) {
		String threadName = Thread.currentThread().getName();
		System.out.println(threadName+": "+message);
	}
	
	
	private static class MessageLoop implements Runnable {
		
		public void run() {
			String messageInfo[] = {
				"First Sentence",
				"Second Sentence",
				"Third Sentence",
				"Fourth Sentence"
			};
			
			
			try {
				Thread.sleep(1000);
				StudentRestClient consumeService = new StudentRestClient();
				/*
				consumeService.postStudentList();		
				for (int i=0; i < messageInfo.length; i++) {
					//Pause for 4 seconds
					Thread.sleep(6000);
					//Print a message
					threadMessage(messageInfo[i]);
					
				}
				*/
				threadMessage("I am done...Thank You");
			} catch (InterruptedException ie) {
				threadMessage("I wasn't done...");
			}
			
			
		}
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	    //Default wait time 1 hour	
        long patience = 1000 * 60 * 60;
        
        //If command line argument is present
        // set the patience time as defined
        try {
        	patience = Long.parseLong(args[0]) * 1000;
        } catch (NumberFormatException ne) {
        	System.err.println("Argument should be an integer");
        	System.exit(1);
        }
        
        
        threadMessage("Starting MessageLoop Thread...");
        long startTime = System.currentTimeMillis();
        Thread t = new Thread(new MessageLoop ());
        t.start();
        
        threadMessage("Waiting for MessageLoop thread to finish");
        //Loop until MessageLoop thread exits
        
        while (t.isAlive()){
        	threadMessage("Still Waiting...");
        	//Wait maximium of 1 second for Message Loop Thread to finish
        	try {
				t.join(1000);
				
				if ( ( (System.currentTimeMillis() - startTime) > patience) 
						&& t.isAlive() ) {
					threadMessage("Tired of waiting");
					t.interrupt();
					
					//Should be long now, wait indefinitely
					t.join();
				}
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				threadMessage("I wasn't done, I was interrupted...");
			}
        }
        
   }
    
}
