package com.ctb.processor;

public class ReportingDataProcessor {

	
	public void process(int customerId){
		
		generateFile(customerId);
		transferFile();
		
		
	}
	
	private void transferFile() {
		// method to call ftp file
		
	}

	private void generateFile(int customerId){
		FileGenerator fileGenerator = new FileGenerator();
		fileGenerator.execute(customerId);
	}
	@SuppressWarnings("unused")
	private void emailNotifier() {
		// method to notify status by email
		
	}
	
}
