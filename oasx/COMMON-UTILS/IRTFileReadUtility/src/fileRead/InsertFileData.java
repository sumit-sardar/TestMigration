package fileRead;

import java.io.*;
import fileUtil.FileUtil;

public class InsertFileData {
	public static void main(String[] args) 
	{
			boolean writesuccessful;
			String filePath=FileUtil.getFilePath();
			try{
			writesuccessful=FileUtil.writeInDB(filePath);
			if(writesuccessful==true)
				System.out.println("Files are saved in IRS Database successfully.");
		}
		catch(FileNotFoundException ie){
			ie.printStackTrace();
		}
	}
}
