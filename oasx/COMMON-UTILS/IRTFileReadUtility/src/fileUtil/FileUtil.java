package fileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FileUtil {
	private static String inserQuery="INSERT INTO IRT_SCORE_LOOKUP_FILES VALUES(?,?,?,?,?)";
	
	private static PreparedStatement ps = null;
	private static Connection con=null;
	
	public static String getFilePath()
	{
		String filePath=ExtractUtil.getDetail("file.location").trim();
		return filePath;
	}
	public static boolean writeInDB(String path) throws FileNotFoundException
	{
		String File_name,Content_area_initial,Content_area,Test_Level,Product_type,product_id;
		int save=0;
		File file = new File(path);                
		File[] files = file.listFiles();  
		System.out.println("Processing " + path + "... "); 
		con=SqlUtil.openIRSDBcon();
		try {
			con.setAutoCommit(false);
			for (File inFile: files ) 
			{                        
				if(inFile.getName().substring(0,2).equals("IP"))
				{
					File_name="";Content_area_initial  = ""; Content_area="";Test_Level = ""; Product_type = ""; product_id = "";
					File_name = inFile.getName();
					Content_area_initial=File_name.substring(2, 4);
					if(Content_area_initial.equals("LA"))
						Content_area="Language";
					if(Content_area_initial.equals("RD"))
						Content_area="Reading";
					if(Content_area_initial.equals("SS"))
						Content_area="Social Studies";
					if(Content_area_initial.equals("SC"))
						Content_area="Science";
					if(Content_area_initial.equals("MA"))
						Content_area="Mathematics";
					Test_Level=File_name.substring(4, 6);
					Product_type=File_name.substring(6, File_name.length());
					if(Product_type.equals("SV"))
						product_id="3710";
					if(Product_type.equals("CB"))
						product_id="3720";
					if(Product_type.equals("MA"))
						product_id="3700";
					//System.out.println("File_name = "+ inFile.getName()+"  "+"Content_area_initial = "+Content_area_initial+"  "+"Content_area = "+Content_area +"  "+"Test_Level = "+Test_Level+"  "+"Product_type ="+ Product_type+ "  "+" product_id = "+product_id);

					ps = con.prepareStatement(inserQuery);
					ps.setString(1, Content_area);
					ps.setString(2, Test_Level);
					ps.setString(3, Product_type);
					ps.setString(4, product_id);
					ps.setString(5, File_name);
					save=ps.executeUpdate();
				}
			}
			 con.commit();
		}catch(SQLException e) {
			try {
				con.rollback();
				save=0;
				System.out.println("Files are not saved in IRS Database.");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		e.printStackTrace();
		} finally {
			SqlUtil.close(con,ps);
			
		}       
		return (save==1)? true :  false;
	}
	
}
	
	

