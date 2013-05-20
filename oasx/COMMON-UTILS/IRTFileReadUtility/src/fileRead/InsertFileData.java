package fileRead;

import static fileUtil.FileUtil.getFilePath;
import static fileUtil.FileUtil.populatePValue;
import static fileUtil.FileUtil.writeInIRSDB;
import static fileUtil.FileUtil.writeInOASDB;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import fileUtil.DBUtil;
import fileUtil.ExtractUtil;
import fileUtil.LasFileUtil;


public class InsertFileData {
	
	private static final String terranova = "Terra Nova";
	private static final String laslink = "Laslink";
	
	public static void main(String[] args) {
		
		boolean writesuccessfulinIRS,writesuccessfulinOAS;
		String filePath = getFilePath();
		
		String product_type = ExtractUtil.getDetail("current.product.type").trim();		
		try {
			if(product_type.equals(terranova)) { // For Terra Nova Third Edition
				writesuccessfulinIRS = writeInIRSDB(filePath);
				if(writesuccessfulinIRS==true)
					System.out.println("Files are saved in IRS Database successfully.");
				
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Date date = new Date();
				System.out.println("START -> " + dateFormat.format(date));
				writesuccessfulinOAS = writeInOASDB(filePath);
				if(writesuccessfulinOAS == true)
					System.out.println("Files are saved in OAS Database successfully.");
				DBUtil.insertScoreLookup(populatePValue(filePath));
				Date date1 = new Date();
				System.out.println("END -> " + dateFormat.format(date1));
			} else if (product_type.equals(laslink)) { // For Laslink Second Edition
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Date date = new Date();
				System.out.println("START -> " + dateFormat.format(date));
				writesuccessfulinOAS = LasFileUtil.writeInOASDBLas(filePath);
				Date date1 = new Date();
				System.out.println("END -> " + dateFormat.format(date1));
			}
		} catch(Exception ie) {
			ie.printStackTrace();
		}
	}
}