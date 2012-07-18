package fileUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class FileUtil {
	private static String inserQuery="INSERT INTO IRT_SCORE_LOOKUP_FILES VALUES(?,?,?,?,?)";
	private static String getDisplayNameQuery="SELECT INTERNAL_DISPLAY_NAME FROM PRODUCT WHERE PRODUCT_ID = ?";
	private static String insertScore_lookupQuery="INSERT INTO score_lookup(Source_score_type_code,dest_Score_type_code,score_lookup_id," +
			"source_score_value,dest_score_value,test_form,test_level,content_area,framework_code,product_internal_display_name) " +
			"VALUES(?,?,?,?,?,?,?,?,?,?)";
	private static String itemSetIDQuery=" select iset.item_set_id from item_set iset, item_set_ancestor isa, test_catalog tc " +
			"where tc.product_id = ? and tc.item_set_id = isa.ancestor_item_Set_id and isa.item_set_id = iset.item_set_id " +
			"and iset.item_set_type = 'TD' and iset.sample = 'F' and iset.subject = ? and iset.item_set_level = ? ";
	private static String insertScore_lookup_item_setQuery="INSERT INTO SCORE_LOOKUP_ITEM_SET VALUES(?,?)";
	private static PreparedStatement ps = null;
	private static ResultSet rs=null;
	private static Connection con=null;

	public static String getFilePath()
	{
		String filePath=ExtractUtil.getDetail("file.location").trim();
		return filePath;
	}
	public static boolean writeInIRSDB(String path) throws FileNotFoundException
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
				//	System.out.println("File_name = "+ inFile.getName()+"  "+"Content_area_initial = "+Content_area_initial+"  "+"Content_area = "+Content_area +"  "+"Test_Level = "+Test_Level+"  "+"Product_type ="+ Product_type+ "  "+" product_id = "+product_id);

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
			SqlUtil.close(con,ps,rs);
			
		}       
		return (save==1)? true :  false;
	}
	public static boolean writeInOASDB(String path) throws IOException
	{
		String File_name,file_location,Content_area_initial,Product_type,product_id;
		String Source_score_type_code,dest_Score_type_code,score_lookup_id,test_form,Test_Level,Content_area,framework_code,product_internal_display_name;
		List<String> contentOfFile = new ArrayList<String>();
		List<String> itemSetIdList = new ArrayList<String>();
		boolean successInSCORE_LOOKUP=false,successInScore_lookup_item_set=false;
		File file = new File(path);                
		File[] files = file.listFiles(); 
		System.out.println("Processing " + path + "... "); 
			for (File inFile: files ) 
			{    
				if(inFile.getName().substring(0,2).equals("NS") || inFile.getName().substring(0,2).equals("SE"))
				{
					File_name="";Content_area_initial  = "";  Product_type = ""; product_id = "";
					
					Source_score_type_code="";dest_Score_type_code="";score_lookup_id="";
					test_form = " ";Test_Level = "";Content_area="";framework_code = "";product_internal_display_name =" ";
										
					File_name = inFile.getName();
					file_location=path+"\\"+File_name;
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
					Product_type=File_name.substring(6, File_name.length());
					if(Product_type.equals("SV"))
						product_id="3710";
					if(Product_type.equals("CB"))
						product_id="3720";
					if(Product_type.equals("MA"))
						product_id="3700";
					
					if(inFile.getName().substring(0,2).equals("NS")) {
						Source_score_type_code = "NSC";
						dest_Score_type_code = "SCL";
					} else {
						Source_score_type_code = "SCL";
						dest_Score_type_code = "SEM";
					}
					
					Test_Level=File_name.substring(4, 6);
					score_lookup_id	="TERRAB3"+"_"+Product_type+"_"+Test_Level+"_"+"G"+"_"+Content_area_initial;
					
					test_form = "G";framework_code = "TERRAB3";
					product_internal_display_name=getDisplayName(product_id);
					System.out.println("file_location = "+file_location);
					
					contentOfFile=readFileData(file_location);
					
					successInSCORE_LOOKUP=writeInSCORE_LOOKUP(contentOfFile,Source_score_type_code,dest_Score_type_code,score_lookup_id,test_form,Test_Level,Content_area,framework_code,product_internal_display_name);
					if(inFile.getName().substring(0,2).equals("NS")) {
						itemSetIdList=getItemSetID(product_id,Content_area,Test_Level);
						successInScore_lookup_item_set=writeInScore_lookup_item_set(score_lookup_id,itemSetIdList);
					}
					
				}
				
			}
			if(successInSCORE_LOOKUP==true && successInScore_lookup_item_set==true)
				return true;
			else
				return false;
		     
	}
	
	public static String getDisplayName(String product_id)
	{
		String name="";
		try {
			con=SqlUtil.openOASDBcon();
			ps = con.prepareStatement(getDisplayNameQuery);
			ps.setInt(1, Integer.parseInt(product_id));
			rs=ps.executeQuery();
			while(rs.next())
				name=rs.getString(1);
			
		}catch(SQLException sq) {
			sq.printStackTrace();
		} finally {
			SqlUtil.close(con,ps,rs);
		}
		return name;
	}
	public static List<String> readFileData(String fileName) throws IOException
	{
		List<String> list = new ArrayList<String>();
		String strLine; 
		int line = 0;
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		while ((strLine = br.readLine()) != null) 
		{  line++;		
		   if(line > 1){
			list.add(strLine); 
		   }                    
		}  
		return list;
	}
	public static boolean writeInSCORE_LOOKUP(List<String> contentOfFile,String Source_score_type_code,String dest_Score_type_code,
			String score_lookup_id,String test_form,String Test_Level,String Content_area,String framework_code,
			String product_internal_display_name)
	{
		Iterator<String> itr;
		int save=0;
		try{
		con=SqlUtil.openOASDBcon();
		con.setAutoCommit(false);
		for ( itr = contentOfFile.iterator(); itr.hasNext(); ) 
		{   
			String str = itr.next().toString();                                
			String[] splitSt = str.split("    "); 
			String source_score_value="",dest_score_value="";
			source_score_value = splitSt[0];                                        
			dest_score_value = splitSt[1]; 
			
			ps = con.prepareStatement(insertScore_lookupQuery);
			ps.setString(1, Source_score_type_code);
			ps.setString(2, dest_Score_type_code);
			ps.setString(3, score_lookup_id);
			ps.setInt(4, Integer.parseInt(source_score_value.trim()));
			ps.setInt(5, Integer.parseInt(dest_score_value.trim()));
			ps.setString(6, test_form);
			ps.setString(7, Test_Level);
			ps.setString(8, Content_area);
			ps.setString(9, framework_code);
			ps.setString(10,product_internal_display_name);
			save=ps.executeUpdate();
			SqlUtil.close(ps);
		}
		con.commit();
		}catch(SQLException e) {
			try {
				con.rollback();
				save=0;
				System.out.println("Data are not saved in SCORE_LOOKUP table.");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		e.printStackTrace();
		}finally {
			SqlUtil.close(con,ps,rs);
		}
		
		return (save==1)? true :  false;
	}
	public static List<String> getItemSetID(String product_id,String Content_area,String Test_Level)
	{
		List<String> itemSetIdList = new ArrayList<String>();
		Iterator<String> itr;
		String name="";
		try {
			con=SqlUtil.openOASDBcon();
			ps = con.prepareStatement(itemSetIDQuery);
			ps.setInt(1, Integer.parseInt(product_id));
			ps.setString(2,Content_area );
			ps.setString(3,Test_Level );
			rs=ps.executeQuery();
			while(rs.next())
			{
				name=rs.getString(1);
				itemSetIdList.add(name);
			}
			
		}catch(SQLException sq) {
			sq.printStackTrace();
		} finally {
			SqlUtil.close(con,ps,rs);
		}
		//System.out.println("product_id= "+product_id+" Content_area= "+Content_area+"  Test_Level="+Test_Level);
		//System.out.println("item SetIdList= ");
		for ( itr = itemSetIdList.iterator(); itr.hasNext(); ) 
		{
			String str = itr.next().toString(); 
			System.out.println(str);
		}
		return itemSetIdList;
	}
	public static boolean writeInScore_lookup_item_set(String score_lookup_id,List<String> itemSetIdList)
	{
		Iterator<String> itr;
		int save=0;
		try{
		con=SqlUtil.openOASDBcon();
		con.setAutoCommit(false);
		for ( itr = itemSetIdList.iterator(); itr.hasNext(); ) 
		{   
			String item_set_id="";
			item_set_id = itr.next().toString();                                
			
			ps = con.prepareStatement(insertScore_lookup_item_setQuery);
			ps.setString(1, score_lookup_id);
			ps.setInt(2, Integer.parseInt(item_set_id));
			save=ps.executeUpdate();
			SqlUtil.close(ps);
		}
		con.commit();
		}catch(SQLException e) {
			try {
				con.rollback();
				save=0;
				System.out.println("Data are not saved in Score_lookup_item_set table.");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		e.printStackTrace();
		}finally {
			SqlUtil.close(con,ps,rs);
		}
		return (save==1)? true :  false;
	}
	
}
	
	

