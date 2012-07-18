package fileRead;

import java.io.*;
import fileUtil.FileUtil;

public class InsertFileData {
	public static void main(String[] args) 
	{
		boolean writesuccessfulinIRS,writesuccessfulinOAS;
		String filePath=FileUtil.getFilePath();
		try{
			
			writesuccessfulinIRS = FileUtil.writeInIRSDB(filePath);
			if(writesuccessfulinIRS==true)
				System.out.println("Files are saved in IRS Database successfully.");
			
			writesuccessfulinOAS=FileUtil.writeInOASDB(filePath);
			if(writesuccessfulinOAS==true)
				System.out.println("Files are saved in OAS Database successfully.");
		}
		catch(IOException ie){
			ie.printStackTrace();
		}
	}
}
