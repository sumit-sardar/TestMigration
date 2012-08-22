package fileRead;

import static fileUtil.FileUtil.getFilePath;
import static fileUtil.FileUtil.populatePValue;
import static fileUtil.FileUtil.writeInOASDB;
import fileUtil.DBUtil;

public class InsertFileData {
	public static void main(String[] args) {
		
		boolean writesuccessfulinIRS,writesuccessfulinOAS;
		String filePath = getFilePath();
		try {
			/*writesuccessfulinIRS = FileUtil.writeInIRSDB(filePath);
			if(writesuccessfulinIRS==true)
				System.out.println("Files are saved in IRS Database successfully.");
			*/
			//writesuccessfulinOAS = writeInOASDB(filePath);
			//if(writesuccessfulinOAS == true)
				//System.out.println("Files are saved in OAS Database successfully.");
			DBUtil.insertScoreLookup(populatePValue(filePath));
		} catch(Exception ie) {
			ie.printStackTrace();
		}
	}
}