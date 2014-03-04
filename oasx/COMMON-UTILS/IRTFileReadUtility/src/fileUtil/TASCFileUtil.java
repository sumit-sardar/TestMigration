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
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TASCFileUtil {


	
	private static final String tascProductDisplayName = "TASC";
	private static final String tascFrameWorkCode = "TASC";
	
	private static final Map<String,String> tascContentArea = new HashMap<String, String>();
	static{
		tascContentArea.put("MA", "Mathematics");
		tascContentArea.put("RD", "Reading");
		tascContentArea.put("SC", "Science");
		tascContentArea.put("SS", "Social Studies");
		tascContentArea.put("WR", "Writing");
		tascContentArea.put("LA", "ELA (Composite of Reading and Writing)");
		tascContentArea.put("OV", "Overall score");
		tascContentArea.put("MA01", "Number and Quantity");
		tascContentArea.put("MA02", "Algebra");
		tascContentArea.put("MA03", "Functions");
		tascContentArea.put("MA04", "Geometry");
		tascContentArea.put("MA05", "Statistics and Probability");
		tascContentArea.put("RD01", "Reading Literature");
		tascContentArea.put("RD02", "Reading Informational Text");
		tascContentArea.put("RD03", "Key Ideas and Details");
		tascContentArea.put("RD04", "Craft and Structure");
		tascContentArea.put("RD05", "Integration of Knowledge and Ideas");
		tascContentArea.put("RD06", "Vocabulary Acquisition and Use");
		tascContentArea.put("SC01", "Physical Sciences");
		tascContentArea.put("SC02", "Life Sciences");
		tascContentArea.put("SC03", "Earth and Space Sciences");
		tascContentArea.put("SS01", "US History");
		tascContentArea.put("SS02", "World History");
		tascContentArea.put("SS03", "Civics and Government");
		tascContentArea.put("SS04", "Geography");
		tascContentArea.put("SS05", "Economics");
		tascContentArea.put("WR07", "Editing and Revising");
		tascContentArea.put("WR08", "Essay Writing");
		
		
	}
	
	private static PreparedStatement ps = null;
	private static ResultSet rs=null;
	private static Connection con=null;
	
	private static String insertScore_lookupQuery_PL="INSERT INTO score_lookup(Source_score_type_code,dest_Score_type_code,score_lookup_id," +
	"source_score_value,dest_score_value,test_form,test_level,grade,content_area,norm_year,framework_code,product_internal_display_name) " +
	"VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
	
	public static boolean writeInOASDBTasc(String path) throws IOException {
		boolean success = false, successInScore_lookup_item_set = false;
		File file = new File(path);                
		File[] files = file.listFiles(); 
		System.out.println("Processing " + path + "... ");
		List<String> itemSetIdList = new ArrayList<String>();
		String File_name,file_location,Content_area_initial,Product_type, grade , product_id , Objective_code,Form;
		String Source_score_type_code,dest_Score_type_code,score_lookup_id,test_form,Test_Level,Content_area;
		String sourceFileType ="" ;
		int fileNameLength = 0 ;
		List<String> contentOfFile = new ArrayList<String>();
		ArrayList<String> forms;
		for (File inFile: files ) {
			forms = new ArrayList<String>();
			if(inFile.getName().substring(0,2).equals("NS")) {// For Raw-Score to Scale-Score Conversion
				File_name="";Content_area_initial  = "";  Product_type = "";  product_id = "";
				Source_score_type_code="";dest_Score_type_code="";score_lookup_id="";
				test_form = " ";Test_Level = "";Content_area="";
				File_name = inFile.getName();
				System.out.println("File_name -> " + File_name);
				file_location=path+"\\"+File_name;
				fileNameLength = File_name.length() ;				
				Content_area_initial=File_name.substring(2,4);
				Objective_code=File_name.substring(4,6);
				sourceFileType =File_name.substring((File_name.length()-1),File_name.length());
				if (!Objective_code.equalsIgnoreCase("AD")){
					Content_area_initial = Content_area_initial+Objective_code;
				}
				Content_area = processContentAreaNameTasc(Content_area_initial);
				test_form = File_name.substring(6,7);				
				product_id = ("E".equalsIgnoreCase(sourceFileType))?"4506":"S".equalsIgnoreCase(sourceFileType)?"4507":null;
				if (product_id == null){
					System.out.println("File extension is not E or S..Exit Called..");
					System.exit(0);
				}
				if ("A".equalsIgnoreCase(test_form)){
					/*forms.add("A2");
					forms.add("A3");*/
					/*forms.add("A4");
					forms.add("A1-S");*/
				}else if ("B".equalsIgnoreCase(test_form)){
					/*forms.add("B2");
					forms.add("B3");*/
					/*forms.add("B4");
					forms.add("B1-S");*/
				}else if ("C".equalsIgnoreCase(test_form)){
					forms.add("C2");
					forms.add("C3");
					/*forms.add("C4");*/
					/*forms.add("C1-S");*/
				}else {
					System.out.println("Form value is not correct..Exit Called..");
					System.exit(0);
				}
				for(String formValue:forms){
					test_form = formValue;
					Source_score_type_code = "NSC";
					dest_Score_type_code = "SCL";							
					contentOfFile=FileUtil.readFileData(file_location);
					grade ="AD";
					Test_Level = "21-22";
					score_lookup_id	= tascFrameWorkCode+"_2013_"+test_form+"_"+Content_area_initial+"_"+sourceFileType;				
					success=writeInSCORE_LOOKUP_TASC(contentOfFile,Source_score_type_code,dest_Score_type_code,score_lookup_id,test_form,Test_Level,Content_area,tascFrameWorkCode,tascProductDisplayName,grade);
					itemSetIdList=getItemSetID(product_id,Content_area,Test_Level,Objective_code,test_form);
					successInScore_lookup_item_set=FileUtil.writeInScore_lookup_item_set(score_lookup_id,itemSetIdList);
					success = true;
					successInScore_lookup_item_set = true;					
				}
				
			} 
			
			else if (inFile.getName().substring(0,2).equals("SC")) { // For Normal-Curve-Equivalent TASC
				File_name="";Content_area_initial  = "";  Product_type = "";
				Source_score_type_code="";dest_Score_type_code="";score_lookup_id="";
				test_form = " ";Test_Level = "";Content_area="";
				File_name = inFile.getName();
				System.out.println("File_name -> " + File_name);
				file_location=path+"\\"+File_name;
				fileNameLength = File_name.length() ;
				Content_area_initial=File_name.substring(2,4);
				Objective_code=File_name.substring(4,6);
				sourceFileType =File_name.substring((File_name.length()-1),File_name.length());
				if (!Objective_code.equalsIgnoreCase("AD")){
					Content_area_initial = Content_area_initial+Objective_code;
				}
				test_form = File_name.substring(6,7);
				Content_area = processContentAreaNameTasc(Content_area_initial);
				product_id = ("E".equalsIgnoreCase(sourceFileType))?"4506":"S".equalsIgnoreCase(sourceFileType)?"4507":null;
				if (product_id == null){
					System.out.println("File extension is not E or S..Exit Called..");
					System.exit(0);
				}
				if ("A".equalsIgnoreCase(test_form)){
					/*forms.add("A2");
					forms.add("A3");*/
					/*forms.add("A4");
					forms.add("A1-S");*/
				}else if ("B".equalsIgnoreCase(test_form)){
					/*forms.add("B2");
					forms.add("B3");*/
					/*forms.add("B4");
					forms.add("B1-S");*/
				}else if ("C".equalsIgnoreCase(test_form)){
					forms.add("C2");
					forms.add("C3");
					/*forms.add("C4");*/
					/*forms.add("C1-S");*/
				}else {
					System.out.println("Form value is not correct..Exit Called..");
					System.exit(0);
				}
				for(String formValue:forms){
					test_form = formValue;
					Source_score_type_code = "SCL";
					dest_Score_type_code = "NCE";
					Test_Level = "21-22";
					contentOfFile=FileUtil.readFileData(file_location);
					String firstLine = getFirstLine(file_location);
					if(firstLine.contains("Grade")) {
						//Test_Level = "10";
						grade = firstLine.split("Grade:")[1].substring(0,2);
					}
					score_lookup_id	= tascFrameWorkCode+"_2013_NCE_"+test_form+"_"+Content_area_initial+"_"+sourceFileType;
					contentOfFile=FileUtil.readFileData(file_location);
					success = writeInSCORE_LOOKUP_NCE(contentOfFile,Source_score_type_code,dest_Score_type_code,score_lookup_id,test_form,Test_Level,Content_area,tascFrameWorkCode,tascProductDisplayName);
				}
			}
			else if (inFile.getName().substring(0,2).equals("SP")) { // For National-Percentile TASC
				File_name="";Content_area_initial  = "";  Product_type = "";
				Source_score_type_code="";dest_Score_type_code="";score_lookup_id="";
				test_form = " ";Test_Level = "";Content_area="";
				File_name = inFile.getName();
				System.out.println("File_name -> " + File_name);
				file_location=path+"\\"+File_name;
				fileNameLength = File_name.length() ;
				Content_area_initial=File_name.substring(2,4);
				Objective_code=File_name.substring(4,6);
				sourceFileType =File_name.substring((File_name.length()-1),File_name.length());
				if (!Objective_code.equalsIgnoreCase("AD")){
					Content_area_initial = Content_area_initial+Objective_code;
				}
				Content_area = processContentAreaNameTasc(Content_area_initial);
				test_form = File_name.substring(6,7);
				product_id = ("E".equalsIgnoreCase(sourceFileType))?"4506":"S".equalsIgnoreCase(sourceFileType)?"4507":null;
				if (product_id == null){
					System.out.println("File extension is not E or S..Exit Called..");
					System.exit(0);
				}
				if ("A".equalsIgnoreCase(test_form)){
					/*forms.add("A2");
					forms.add("A3");*/
					/*forms.add("A4");
					forms.add("A1-S");*/
				}else if ("B".equalsIgnoreCase(test_form)){
					/*forms.add("B2");
					forms.add("B3");*/
					/*forms.add("B4");
					forms.add("B1-S");*/
				}else if ("C".equalsIgnoreCase(test_form)){
					forms.add("C2");
					forms.add("C3");
					/*forms.add("C4");*/
					/*forms.add("C1-S");*/
				}else {
					System.out.println("Form value is not correct..Exit Called..");
					System.exit(0);
				}
				for(String formValue:forms){
					test_form = formValue;
					Source_score_type_code = "SCL";
					dest_Score_type_code = "NP";
					Test_Level = "21-22";
					contentOfFile=FileUtil.readFileData(file_location);
					String firstLine = getFirstLine(file_location);
					if(firstLine.contains("Grade")) {
						//Test_Level = "10";
						grade = firstLine.split("Grade:")[1].substring(0,2);
					}
					score_lookup_id	= tascFrameWorkCode+"_2013_NP_"+test_form+"_"+Content_area_initial+"_"+sourceFileType;
					contentOfFile=FileUtil.readFileData(file_location);
					success = writeInSCORE_LOOKUP_NCE(contentOfFile,Source_score_type_code,dest_Score_type_code,score_lookup_id,test_form,Test_Level,Content_area,tascFrameWorkCode,tascProductDisplayName);
				}
			}
			else if (inFile.getName().substring(0,2).equals("SS")) { // For Proficiency Level for Content Area 
				File_name="";Content_area_initial  = "";  Product_type = "";
				Source_score_type_code="";dest_Score_type_code="";score_lookup_id="";
				test_form = " ";Test_Level = "";Content_area="";
				File_name = inFile.getName();
				System.out.println("File_name -> " + File_name);
				file_location=path+"\\"+File_name;
				sourceFileType =File_name.substring((File_name.length()-1),File_name.length());
				product_id = ("E".equalsIgnoreCase(sourceFileType))?"4506":"S".equalsIgnoreCase(sourceFileType)?"4507":null;
				if (product_id == null){
					System.out.println("File extension is not E or S..Exit Called..");
					System.exit(0);
				}
				forms.add("C2");
				forms.add("C3");
				/*forms.add("A4");
				forms.add("B2");
				forms.add("B3");*/
				/*forms.add("B4");
				forms.add("C2");
				forms.add("C3");
				forms.add("C4");*/
				/*forms.add("A1-S");
				forms.add("B1-S");
				forms.add("C1-S");*/
				for(String formValue:forms){
					Source_score_type_code = "SCL";
					dest_Score_type_code = "PL";
					score_lookup_id	= tascFrameWorkCode+"_2013_PL"+formValue+"_"+sourceFileType;
					contentOfFile=FileUtil.readFileData(file_location);
					success = writeInSCORE_LOOKUP_PL(contentOfFile,Source_score_type_code,dest_Score_type_code,
								score_lookup_id,formValue,tascFrameWorkCode,
								tascProductDisplayName);
				}
				
			}
			else if (inFile.getName().substring(0,2).equals("OS") || inFile.getName().substring(0,2).equals("NC")) { // For Proficiency Level for Objectives
				File_name="";Content_area_initial  = "";  Product_type = "";
				Source_score_type_code="";dest_Score_type_code="";score_lookup_id="";
				test_form = " ";Test_Level = "";Content_area="";
				File_name = inFile.getName();
				System.out.println("File_name -> " + File_name);
				file_location=path+"\\"+File_name;
				sourceFileType =File_name.substring((File_name.length()-1),File_name.length());
				product_id = ("E".equalsIgnoreCase(sourceFileType))?"4506":"S".equalsIgnoreCase(sourceFileType)?"4507":null;
				if (product_id == null){
					System.out.println("File extension is not E or S..Exit Called..");
					System.exit(0);
				}
				forms.add("C2");
				forms.add("C3");
				/*forms.add("A4");
				forms.add("B2");
				forms.add("B3");*/
				/*forms.add("B4");
				forms.add("C2");
				forms.add("C3");
				forms.add("C4");*/
				/*forms.add("A1-S");
				forms.add("B1-S");
				forms.add("C1-S");*/			
				for (String formValue :forms){
					Source_score_type_code = "SCL";
					dest_Score_type_code = "PL";
					score_lookup_id	= tascFrameWorkCode+"_2013_PL"+formValue+"_"+sourceFileType;
					contentOfFile=FileUtil.readFileData(file_location);
					success = writeInSCORE_LOOKUP_PL_For_Objectives(contentOfFile,Source_score_type_code,dest_Score_type_code,
							score_lookup_id,formValue,tascFrameWorkCode,
							tascProductDisplayName);
				}
			}
			
		}
		
		if(success == true && successInScore_lookup_item_set== true)
			return true;
		else
			return false;
	}
	
	public static String processGradeTASC(String gradeCode) {
		String grade = "";
		if("AD".equals(gradeCode)) {
			grade = "21-22";
		} 
		return grade;
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
	
	public static String processContentAreaNameTasc(String caShortName) {
	
		String contentArea = "";
		try {
			contentArea = tascContentArea.get(caShortName);
			
		}catch(Exception e){
			System.out.println("Exception occured in retriving Content Area name for TASC.");
			e.printStackTrace();
			
		}
		
		return contentArea;
	}

	public static boolean writeInSCORE_LOOKUP_PL(List<String> contentOfFile,String Source_score_type_code,String dest_Score_type_code,
			String score_lookup_id,String test_form,String framework_code,
			String product_internal_display_name)
	{
		Iterator<String> itr;
		int save=0;
		String contentArea = null;
		String grade = null;
		String normYear = "2013";
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
			test_level = processTestLevelFromGradeTASC(grade);
			scoreLookupId = scoreLookupId + grade + "_" + contentArea;
			//System.out.println("Score Lookup ID--->>"+scoreLookupId);
			contentArea = processContentAreaNameTasc(contentArea);
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
	
	public static boolean writeInSCORE_LOOKUP_PL_For_Objectives(List<String> contentOfFile,String Source_score_type_code,String dest_Score_type_code,
			String score_lookup_id,String test_form,String framework_code,
			String product_internal_display_name)
	{
		Iterator<String> itr;
		int save=0;
		String contentArea = null;
		String grade = null;
		String objectiveCode = null;
		String normYear = "2013";
		String test_level="";
		try{
		con=SqlUtil.openOASDBcon();
		con.setAutoCommit(false);
		for ( itr = contentOfFile.iterator(); itr.hasNext(); ) 
		{   
			String scoreLookupId = score_lookup_id;
			String str = itr.next().toString();                                
			String[] splitSt = str.split("         ");// 9 spaces are here for splitting, as per file format
			
			String caGrade = splitSt[0];
			String lookupVal = splitSt[1];	
			contentArea = caGrade.split(" ")[0];
			// caGrade.split(" ")[1] is not needed as it is PEID information and not required for OAS.
			grade = caGrade.split(" ")[2];
			objectiveCode =caGrade.split(" ")[3];
			test_level = processTestLevelFromGradeTASC(grade);
			contentArea = contentArea + objectiveCode;
			scoreLookupId = scoreLookupId + grade + "_" + contentArea;
			//System.out.println("Score Lookup ID--->>"+scoreLookupId);
			contentArea = processContentAreaNameTasc(contentArea);
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
				System.out.println("Data are not saved in SCORE_LOOKUP table for Proficiency Level for Objectives.");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		e.printStackTrace();
		}finally {
			SqlUtil.close(con,ps,rs);
		}
		
		return (save==1)? true :  false;
	}
	
	private static String processTestLevelFromGradeTASC(String gradeCode) {
		String level="";
		if("AD".equals(gradeCode)) {
			level = "21-22";
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
			ps = con.prepareStatement(FileUtil.insertScore_lookupQuery_withGrade);
			for (Map.Entry<Integer, Integer> entry : scaleToNce.entrySet()) {
			    //System.out.println(entry.getKey() + " - " + entry.getValue());			
				//ps = con.prepareStatement(FileUtil.insertScore_lookupQuery);
				ps.setString(1, Source_score_type_code);
				ps.setString(2, dest_Score_type_code);
				ps.setString(3, score_lookup_id);
				ps.setInt(4, entry.getKey());
				ps.setInt(5, entry.getValue());
				ps.setString(6, test_form);
				ps.setString(7, Test_Level);
				ps.setString(8, Content_area);
				ps.setString(9, "2013");
				ps.setString(10, framework_code);
				ps.setString(11,product_internal_display_name);
				ps.setString(12,"AD");
//				save=ps.executeUpdate();
//				SqlUtil.close(ps);
				ps.addBatch();
			}
			ps.executeBatch();
			con.commit();
			save =1;
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
	
	
	public static List<String> getItemSetID(String product_id,String Content_area,String Test_Level,String Objective_code,String form)
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
				
				if (Objective_code.equalsIgnoreCase("AD"))
				{
					ps = con.prepareStatement(FileUtil.itemSetIDQueryTASC);
					ps.setInt(1, Integer.parseInt(product_id));
					ps.setString(2,Content_area.toUpperCase() );
					ps.setString(3,test_level[ii] );
					ps.setString(4,form );
					
				}else{
					ps = con.prepareStatement(FileUtil.itemSetIDQueryTASCObjectiveLevel);
					ps.setInt(1, Integer.parseInt(product_id));
					ps.setString(2,Content_area.toUpperCase() );
					ps.setString(3,"17");
				}		
				
				
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
	
	
	public static boolean writeInSCORE_LOOKUP_TASC(List<String> contentOfFile,String Source_score_type_code,String dest_Score_type_code,
			String score_lookup_id,String test_form,String Test_Level,String Content_area,String framework_code,
			String product_internal_display_name , String grade)
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
			source_score_value = splitSt[0].trim();                                        
			dest_score_value = splitSt[1].trim(); 
			
			ps = con.prepareStatement(FileUtil.insertScore_lookupQuery_TASC);
			ps.setString(1, Source_score_type_code);
			ps.setString(2, dest_Score_type_code);
			ps.setString(3, score_lookup_id);
			ps.setInt(4, Integer.parseInt(source_score_value.trim()));
			ps.setInt(5, Integer.parseInt(dest_score_value.trim()));
			ps.setString(6, test_form);
			ps.setString(7, Test_Level);
			ps.setString(8, Content_area);
			ps.setString(9, "2013");
			ps.setString(10, framework_code);
			ps.setString(11,product_internal_display_name);
			ps.setString(12,grade);
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
	
}
