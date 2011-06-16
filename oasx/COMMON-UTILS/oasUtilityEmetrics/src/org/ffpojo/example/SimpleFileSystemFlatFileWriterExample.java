package org.ffpojo.example;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.ffpojo.exception.FFPojoException;
import org.ffpojo.file.writer.FileSystemFlatFileWriter;
import org.ffpojo.file.writer.FlatFileWriter;

import com.ctb.dto.Address;
import com.ctb.dto.Customer;

public class SimpleFileSystemFlatFileWriterExample {

	//change here (make sure you have permission to write in the specified path):
	private static final String OUTPUT_TXT_OS_PATH = "C:/SimpleFileSystemFlatFileWriterExample.txt";
	

	
	public static void main(String[] args) {
		SimpleFileSystemFlatFileWriterExample example = new SimpleFileSystemFlatFileWriterExample();
		try {
			System.out.println("Making TXT from POJO...");
			example.writeCustomersToText();
			
			System.out.println("END !");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FFPojoException e) {
			e.printStackTrace();
		}
	}
	
	public void writeCustomersToText() throws IOException, FFPojoException {
		File file = new File(OUTPUT_TXT_OS_PATH);
		FlatFileWriter ffWriter = new FileSystemFlatFileWriter(file, true);
		List<Customer> customers = createCustomersMockList();
		/*List<Address> address = createAddressMockList();*/
		ffWriter.writeRecordList(customers);
		/*ffWriter.writeRecord(address);*/
		ffWriter.close();	
	}
	
	private static List<Customer> createCustomersMockList() {
		List<Customer> customers = new ArrayList<Customer>();
		{
			Customer cust = new Customer();
			cust.setId(98456L); 
			cust.setName("Axel Rose"); 
			cust.setEmail("axl@thehost.com");
			customers.add(cust);
		}
		{
			Customer cust = new Customer();
			cust.setId(65478L); 
			cust.setName("Bono Vox"); 
			cust.setEmail("bono@thehost.com");
			customers.add(cust);
		}
		{
			Customer cust = new Customer();
			cust.setId(78425L); 
			cust.setName("Bob Marley"); 
			cust.setEmail("marley@thehost.com");
			cust.setAddress(createAddressMockList());
			customers.add(cust);
		}
	
		
		return customers;
	}
	
	private static Set<Address> createAddressMockList(){
		
		Set<Address> address = new HashSet<Address>();
		
			Address addr = new Address();
			addr.setAddress1("sano");
			addr.setAddress2("CHENNAI");
			addr.setState("TN");
			address.add(addr);
			return address;
			
		}
		
			
		
		

	
}
