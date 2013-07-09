package fileUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LasFileUtil {
	
	private static final String lasProductDisplayName = "LASLINKS";
	private static final String lasFrameWorkCode = "LL2ND";
	
	private static PreparedStatement ps = null;
	private static ResultSet rs=null;
	private static Connection con=null;
	
	private static String insertScore_lookupQuery_PL="INSERT INTO score_lookup(Source_score_type_code,dest_Score_type_code,score_lookup_id," +
	"source_score_value,dest_score_value,test_form,test_level,grade,content_area,norm_year,framework_code,product_internal_display_name) " +
	"VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
	
	public static boolean writeInOASDBLas(String path) throws IOException {
		boolean success = false, successInScore_lookup_item_set = false;
		File file = new File(path);                
		File[] files = file.listFiles(); 
		System.out.println("Processing " + path + "... ");
		List<String> itemSetIdList = new ArrayList<String>();
		String File_name,file_location,Content_area_initial,Product_type, grade, product_id;
		String Source_score_type_code,dest_Score_type_code,score_lookup_id,test_form,Test_Level,Content_area;
		List<String> contentOfFile = new ArrayList<String>();
		
		for (File inFile: files ) {
			if(inFile.getName().substring(0,2).equals("NS")) { // For Raw Score to Scale Score Conversion
				File_name="";Content_area_initial  = "";  Product_type = "";  product_id = "";
				Source_score_type_code="";dest_Score_type_code="";score_lookup_id="";
				test_form = " ";Test_Level = "";Content_area="";
				File_name = inFile.getName();
				System.out.println("File_name -> " + File_name);
				file_location=path+"\\"+File_name;
				Content_area_initial=File_name.substring(2, 4);
				Content_area = processContentAreaName(Content_area_initial);
				Product_type=File_name.substring(6 , File_name.length());
				if(Product_type.equalsIgnoreCase("EB")) {
					test_form = "T";
				} else {
					test_form = Product_type;
				}
				Source_score_type_code = "NSC";
				dest_Score_type_code = "SCL";
				Test_Level=File_name.substring(4, 6);
				contentOfFile=FileUtil.readFileData(file_location);
				String firstLine = getFirstLine(file_location);
				if(firstLine.contains("Grade")) {
					//Test_Level = "10";
					grade = firstLine.split("Grade:")[1].substring(0,2);
				}
				Test_Level = processTestLevel(Test_Level); 
				score_lookup_id	= lasFrameWorkCode+"_2012_"+test_form+"_"+Test_Level+"_"+Content_area_initial;
				success=FileUtil.writeInSCORE_LOOKUP(contentOfFile,Source_score_type_code,dest_Score_type_code,score_lookup_id,test_form,Test_Level,Content_area,lasFrameWorkCode,lasProductDisplayName);
				if(Product_type.equalsIgnoreCase("EB")) {
					product_id = "7502";
				}
				if(Product_type.equalsIgnoreCase("C")) {
					product_id = "7501";
				}
				
				itemSetIdList=getItemSetID(product_id,Content_area,Test_Level);
				successInScore_lookup_item_set=FileUtil.writeInScore_lookup_item_set(score_lookup_id,itemSetIdList);
			} 
			
			else if (inFile.getName().substring(0,2).equals("SS")) { // For Proficiency Level
				File_name="";Content_area_initial  = "";  Product_type = "";
				Source_score_type_code="";dest_Score_type_code="";score_lookup_id="";
				test_form = " ";Test_Level = "";Content_area="";
				File_name = inFile.getName();
				System.out.println("File_name -> " + File_name);
				file_location=path+"\\"+File_name;
				Product_type=File_name.substring(5 , File_name.length());
				if(Product_type.equalsIgnoreCase("EB")) {
					test_form = "T";
				} else {
					test_form = Product_type;
				}
				Source_score_type_code = "SCL";
				dest_Score_type_code = "PL";
				score_lookup_id	= lasFrameWorkCode+"_2012_"+test_form+"_";
				contentOfFile=FileUtil.readFileData(file_location);
				success = writeInSCORE_LOOKUP_PL(contentOfFile,Source_score_type_code,dest_Score_type_code,
						score_lookup_id,test_form,lasFrameWorkCode,
						lasProductDisplayName);
			} 
			
			else if (inFile.getName().substring(0,2).equals("SN")) { // For Normal CUrve Equivalent
				File_name="";Content_area_initial  = "";  Product_type = "";
				Source_score_type_code="";dest_Score_type_code="";score_lookup_id="";
				test_form = " ";Test_Level = "";Content_area="";
				File_name = inFile.getName();
				System.out.println("File_name -> " + File_name);
				file_location=path+"\\"+File_name;
				Content_area_initial=File_name.substring(2, 4);
				Content_area = processContentAreaName(Content_area_initial);
				Product_type=File_name.substring(6 , File_name.length());
				if(Product_type.equalsIgnoreCase("EB")) {
					test_form = "T";
				} else {
					test_form = Product_type;
				}
				Source_score_type_code = "SCL";
				dest_Score_type_code = "NCE";
				Test_Level=File_name.substring(4, 6);
				contentOfFile=FileUtil.readFileData(file_location);
				String firstLine = getFirstLine(file_location);
				if(firstLine.contains("Grade")) {
					//Test_Level = "10";
					grade = firstLine.split("Grade:")[1].substring(0,2);
				}
				Test_Level = processTestLevel(Test_Level);
				score_lookup_id	= lasFrameWorkCode+"_2012_NCE_"+test_form+"_"+Test_Level+"_"+Content_area_initial;
				contentOfFile=FileUtil.readFileData(file_location);
				success = writeInSCORE_LOOKUP_NCE(contentOfFile,Source_score_type_code,dest_Score_type_code,score_lookup_id,test_form,Test_Level,Content_area,lasFrameWorkCode,lasProductDisplayName);
			}
			
			else if (inFile.getName().substring(0,2).equals("SP")) { // For percentile Rank
				File_name="";Content_area_initial  = "";  Product_type = "";
				Source_score_type_code="";dest_Score_type_code="";score_lookup_id="";
				test_form = " ";Test_Level = "";Content_area="";
				File_name = inFile.getName();
				System.out.println("File_name -> " + File_name);
				file_location=path+"\\"+File_name;
				Content_area_initial=File_name.substring(2, 4);
				Content_area = processContentAreaName(Content_area_initial);
				Product_type=File_name.substring(6 , File_name.length());
				if(Product_type.equalsIgnoreCase("EB")) {
					test_form = "T";
				} else {
					test_form = Product_type;
				}
				Source_score_type_code = "SCL";
				dest_Score_type_code = "PR";
				Test_Level=File_name.substring(4, 6);
				contentOfFile=FileUtil.readFileData(file_location);
				String firstLine = getFirstLine(file_location);
				if(firstLine.contains("Grade")) {
					//Test_Level = "10";
					grade = firstLine.split("Grade:")[1].substring(0,2);
				}
				Test_Level = processTestLevel(Test_Level);
				score_lookup_id	= lasFrameWorkCode+"_2012_PR_"+test_form+"_"+Test_Level+"_"+Content_area_initial;
				contentOfFile=FileUtil.readFileData(file_location);
				success = writeInSCORE_LOOKUP_NCE(contentOfFile,Source_score_type_code,dest_Score_type_code,score_lookup_id,test_form,Test_Level,Content_area,lasFrameWorkCode,lasProductDisplayName);
			
			}
			else if (inFile.getName().substring(0,2).equals("SX")) { // For Lexile
				File_name="";Content_area_initial  = "";  Product_type = "";
				Source_score_type_code="";dest_Score_type_code="";score_lookup_id="";
				test_form = " ";Test_Level = "";Content_area="";
				File_name = inFile.getName();
				System.out.println("File_name -> " + File_name);
				file_location=path+"\\"+File_name;
				Product_type=File_name.substring(4,File_name.length());
				if(Product_type.equals("EB")){
					test_form = "T";
				}else{
					test_form = Product_type;
				}
				Source_score_type_code = "SCL";
				dest_Score_type_code = "LXL";
				Test_Level= File_name.substring(2,4);
				Test_Level = processTestLevel(Test_Level);
				String firstLine = getFirstLine(file_location);
				contentOfFile=FileUtil.readFileData(file_location);
				Content_area_initial=firstLine.split("Content:")[1].substring(0,2);
				Content_area = processContentAreaName(Content_area_initial);
				score_lookup_id = lasFrameWorkCode+"_2012_LXL_"+test_form+"_"+Test_Level+"_"+Content_area_initial;
				contentOfFile=FileUtil.readFileData(file_location);
				success = writeInSCORE_LOOKUP_LXL(contentOfFile,Source_score_type_code,dest_Score_type_code,score_lookup_id,test_form,Test_Level,Content_area,lasFrameWorkCode,lasProductDisplayName);
				
				
			}
		}
		
		if(success == true && successInScore_lookup_item_set== true)
			return true;
		else
			return false;
	}
	
	public static String processGrade(String gradeCode) {
		String grade = "";
		if("00".equals(gradeCode)) {
			grade = "K";
		} else if ("01".equals(gradeCode)) {
			grade = "1";
		} else if ("02".equals(gradeCode)) {
			grade = "2";
		} else if ("03".equals(gradeCode)) {
			grade = "3";
		} else if ("04".equals(gradeCode)) {
			grade = "4";
		} else if ("05".equals(gradeCode)) {
			grade = "5";
		} else if ("06".equals(gradeCode)) {
			grade = "6";
		} else if ("07".equals(gradeCode)) {
			grade = "7";
		} else if ("08".equals(gradeCode)) {
			grade = "8";
		} else if ("09".equals(gradeCode)) {
			grade = "9";
		} else 
			grade = gradeCode;
		return grade;
	}
	
	/*public static Integer processProductType(String type) {
		Integer productId = 0;
		if(type.equals("C"))
			productId = 7004;
		else if(type.equals("EB"))
			productId = 7006;
		
		return productId;
	}*/
	
	
	//Need to complete after clarifications are received
	public static String processTestLevel(String level) {
		
		String testLevel = "";
		if("00".equals(level)){
			testLevel = "K";
		}else if("01".equals(level)){
			testLevel = "1";
		}else if("10".equals(level)){
			testLevel = "K-1";
		}else if("20".equals(level)){
			testLevel = "2-3";
		}else if("30".equals(level)){
			testLevel = "4-5";
		}else if("40".equals(level)){
			testLevel = "6-8";
		}else if("50".equals(level)){
			testLevel = "9-12";
		}
			
		return testLevel;
	}
	
	public static String getFirstLine(String location) {
		String strLine = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(location));
			while ((strLine = br.readLine()) != null) 
			{  
				return strLine;
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return strLine;
	}
	
public static String processContentAreaName(String caShortName) {
		
		String caName = null;		
		if(caShortName.equals("LI"))
			caName="Listening";
		if(caShortName.equals("RD"))
			caName="Reading";
		if(caShortName.equals("SK"))
			caName="Speaking";
		if(caShortName.equals("WR"))
			caName="Writing";
		if(caShortName.equals("CO"))
			caName="Comprehension";
		if(caShortName.equals("OR"))
			caName="Oral";
		if(caShortName.equals("OV"))
			caName="Overall";
		if(caShortName.equals("PR"))
			caName="Productive";
		if(caShortName.equals("LT"))
			caName="Literacy";
		return caName;
	}

	public static boolean writeInSCORE_LOOKUP_PL(List<String> contentOfFile,String Source_score_type_code,String dest_Score_type_code,
			String score_lookup_id,String test_form,String framework_code,
			String product_internal_display_name)
	{
		Iterator<String> itr;
		int save=0;
		String contentArea = null;
		String grade = null;
		String normYear = "2012";
		String test_level="";
		try{
		con=SqlUtil.openOASDBcon();
		con.setAutoCommit(false);
		for ( itr = contentOfFile.iterator(); itr.hasNext(); ) 
		{   
			String scoreLookupId = score_lookup_id;
			String str = itr.next().toString();                                
			String[] splitSt = str.split("  ");
			String caGrade = splitSt[0];
			String lookupVal = splitSt[1];
			
			contentArea = caGrade.split(" ")[0];
			grade = caGrade.split(" ")[1];
			grade = processGrade(grade);
			test_level = processTestLevelFromGrade(grade);
			scoreLookupId = scoreLookupId + grade + "_" + contentArea;
			//System.out.println("Score Lookup ID--->>"+scoreLookupId);
			contentArea = processContentAreaName(contentArea);
			String source_score_value="",dest_score_value="";
			String[] plLookupVal = lookupVal.split(" ");
			if(plLookupVal != null && plLookupVal.length > 0) {
				for(int k = 0; k < plLookupVal.length; k++) {
					source_score_value = plLookupVal[k].split(":")[0].split("-")[1];
					dest_score_value = plLookupVal[k].split(":")[1];
					//System.out.println("source_score_value -> " + source_score_value);
					//System.out.println("dest_score_value -> " + dest_score_value);
					ps = con.prepareStatement(insertScore_lookupQuery_PL);
					ps.setString(1, Source_score_type_code);
					ps.setString(2, dest_Score_type_code);
					ps.setString(3, scoreLookupId);
					ps.setInt(4, Integer.parseInt(source_score_value.trim()));
					ps.setInt(5, Integer.parseInt(dest_score_value.trim()));
					ps.setString(6, test_form);
					ps.setString(7, test_level);
					ps.setString(8, grade);
					ps.setString(9, contentArea);
					ps.setString(10, normYear);
					ps.setString(11, framework_code);
					ps.setString(12,product_internal_display_name);
					save=ps.executeUpdate();
					SqlUtil.close(ps);
				}
				con.commit();
			}
		}
		
		}catch(SQLException e) {
			try {
				con.rollback();
				save=0;
				System.out.println("Data are not saved in SCORE_LOOKUP table for Proficiency Level.");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		e.printStackTrace();
		}finally {
			SqlUtil.close(con,ps,rs);
		}
		
		return (save==1)? true :  false;
	}
	
	private static String processTestLevelFromGrade(String gradeCode) {
		String level="";
		if("K".equals(gradeCode)) {
			level = "K";
		} else if ("1".equals(gradeCode)) {
			level = "1";
		} else if ("2".equals(gradeCode)) {
			level = "2-3";
		} else if ("3".equals(gradeCode)) {
			level = "2-3";
		} else if ("4".equals(gradeCode)) {
			level = "4-5";
		} else if ("5".equals(gradeCode)) {
			level = "4-5";
		} else if ("6".equals(gradeCode)) {
			level = "6-8";
		} else if ("7".equals(gradeCode)) {
			level = "6-8";
		} else if ("8".equals(gradeCode)) {
			level = "6-8";
		} else if ("9".equals(gradeCode)) {
			level = "9-12";
		} else if ("10".equals(gradeCode)) {
			level = "9-12";
		}else if ("11".equals(gradeCode)) {
			level = "9-12";
		}else if ("12".equals(gradeCode)) {
			level = "9-12";
		}
		return level;
	}

	public static boolean writeInSCORE_LOOKUP_NCE(List<String> contentOfFile,String Source_score_type_code,String dest_Score_type_code,
	String score_lookup_id,String test_form,String Test_Level,String Content_area,String framework_code,
	String product_internal_display_name)
	{
		int save=0;
		Map<Integer, Integer> scaleToNce = new LinkedHashMap<Integer, Integer>();
		Integer previousValue = Integer.parseInt(contentOfFile.get(0).split("     ")[1].trim());
		try{
			con=SqlUtil.openOASDBcon();
			con.setAutoCommit(false);
			for(int i = 1; i < contentOfFile.size(); i++) {
				String nceLine = contentOfFile.get(i);
				String[] splitSt = nceLine.split("     "); 
				String scaleScore = splitSt[0].trim();
				scaleToNce.put(Integer.parseInt(scaleScore) - 1, previousValue);
				previousValue = Integer.parseInt(splitSt[1].trim());
			}
			scaleToNce.put(Integer.parseInt(contentOfFile.get(contentOfFile.size() - 1).split("     ")[0].trim()), Integer.parseInt(contentOfFile.get(contentOfFile.size() - 1).split("     ")[1].trim()));
			for (Map.Entry<Integer, Integer> entry : scaleToNce.entrySet()) {
			    //System.out.println(entry.getKey() + " - " + entry.getValue());			
				ps = con.prepareStatement(FileUtil.insertScore_lookupQuery);
				ps.setString(1, Source_score_type_code);
				ps.setString(2, dest_Score_type_code);
				ps.setString(3, score_lookup_id);
				ps.setInt(4, entry.getKey());
				ps.setInt(5, entry.getValue());
				ps.setString(6, test_form);
				ps.setString(7, Test_Level);
				ps.setString(8, Content_area);
				ps.setString(9, "2012");
				ps.setString(10, framework_code);
				ps.setString(11,product_internal_display_name);
				save=ps.executeUpdate();
				SqlUtil.close(ps);
			}
			con.commit();
		}catch(SQLException e) {
			try {
				con.rollback();
				save=0;
				System.out.println("Data are not saved in SCORE_LOOKUP table for Proficiency Level.");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		e.printStackTrace();
		}finally {
			SqlUtil.close(con,ps,rs);
		}
		
		return (save==1)? true :  false;
	}
	
	public static boolean writeInSCORE_LOOKUP_LXL(List<String> contentOfFile,String Source_score_type_code,String dest_Score_type_code,
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
				String[] splitSt = str.split("     "); 
				String source_score_value="",dest_score_value="";
				source_score_value = splitSt[0].trim();                                        
				dest_score_value = splitSt[1].trim(); 
				
				ps = con.prepareStatement(FileUtil.insertScore_lookupQuery);
				ps.setString(1, Source_score_type_code);
				ps.setString(2, dest_Score_type_code);
				ps.setString(3, score_lookup_id);
				ps.setInt(4, Integer.parseInt(source_score_value.trim()));
				ps.setInt(5, Integer.parseInt(dest_score_value.trim()));
				ps.setString(6, test_form);
				ps.setString(7, Test_Level);
				ps.setString(8, Content_area);
				ps.setString(9, "");
				ps.setString(10, framework_code);
				ps.setString(11,product_internal_display_name);
				save=ps.executeUpdate();
				SqlUtil.close(ps);
			}
			con.commit();
		}catch(SQLException e) {
			try {
				con.rollback();
				save=0;
				System.out.println("Data are not saved in SCORE_LOOKUP table for Lexile Level.");
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
		String[] test_level= new String[2];			
		int count=0;
		if(Test_Level == "K-1"){
			count = 1;
			test_level[0]="K";
			test_level[1]="1";
		}else{
			test_level[0]=Test_Level;
		}
		for(int ii=0; ii<=count;ii++){
			String name="";
			try {
				con=SqlUtil.openOASDBcon();
				ps = con.prepareStatement(FileUtil.itemSetIDQuery);
				ps.setInt(1, Integer.parseInt(product_id));
				ps.setString(2,Content_area.toUpperCase() );
				ps.setString(3,test_level[ii] );
				rs=ps.executeQuery();
				while(rs.next())
				{
					name=rs.getString(1);
					itemSetIdList.add(name);
				}
				
			}
			catch(SQLException sq) {
				sq.printStackTrace();
			} finally {
				SqlUtil.close(con,ps,rs);
			}
		}
		return itemSetIdList;
	}
	
}
