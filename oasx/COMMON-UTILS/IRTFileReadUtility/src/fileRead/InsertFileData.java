package fileRead;

import java.util.List;

import fileUtil.DBUtil;
import fileUtil.FileUtil;
import fileUtil.PVALFileData;

public class InsertFileData {
	public static void main(String[] args) {
		
		boolean writesuccessfulinIRS,writesuccessfulinOAS;
		String filePath = FileUtil.getFilePath();
		try {
			/*writesuccessfulinIRS = FileUtil.writeInIRSDB(filePath);
			if(writesuccessfulinIRS==true)
				System.out.println("Files are saved in IRS Database successfully.");
			*/
			writesuccessfulinOAS = FileUtil.writeInOASDB(filePath);
			if(writesuccessfulinOAS == true)
				System.out.println("Files are saved in OAS Database successfully.");
			List<PVALFileData> pvalFileData = FileUtil.readPVALFile(filePath);
			List<PVALFileData> tngFileData = FileUtil.readTNGFile(filePath);
			DBUtil.insertScoreLookup(FileUtil.populatePValue(tngFileData, pvalFileData));
		} catch(Exception ie) {
			ie.printStackTrace();
		}
	}
}