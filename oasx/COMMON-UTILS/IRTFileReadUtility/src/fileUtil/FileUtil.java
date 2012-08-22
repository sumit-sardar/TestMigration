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
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class FileUtil {
	private static String inserQuery="INSERT INTO IRT_SCORE_LOOKUP_FILES VALUES(?,?,?,?,?)";
	private static String getDisplayNameQuery="SELECT INTERNAL_DISPLAY_NAME FROM PRODUCT WHERE PRODUCT_ID = ?";
	private static String insertScore_lookupQuery="INSERT INTO score_lookup(Source_score_type_code,dest_Score_type_code,score_lookup_id," +
			"source_score_value,dest_score_value,test_form,test_level,content_area,framework_code,product_internal_display_name) " +
			"VALUES(?,?,?,?,?,?,?,?,?,?)";
	private static String insertScore_lookupQuery_withNorms="INSERT INTO score_lookup(Source_score_type_code,dest_Score_type_code,score_lookup_id," +
		"source_score_value,dest_score_value,test_form,test_level,grade,content_area,norm_group,norm_year,framework_code,product_internal_display_name) " +
		"VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
	private static String insertScore_lookupQuery_withNorms_GE="INSERT INTO score_lookup(Source_score_type_code,dest_Score_type_code,score_lookup_id," +
		"source_score_value,dest_score_value,test_form,test_level,grade,content_area,norm_group,norm_year,framework_code,product_internal_display_name" +
		", extended_flag) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	private static String itemSetIDQuery=" select iset.item_set_id from item_set iset, item_set_ancestor isa, test_catalog tc " +
			"where tc.product_id = ? and tc.item_set_id = isa.ancestor_item_Set_id and isa.item_set_id = iset.item_set_id " +
			"and iset.item_set_type = 'TD' and iset.sample = 'F' and iset.subject = ? and iset.item_set_level = ? ";
	private static String itemSetIdGEQuery = "SELECT iset.item_set_id FROM item_set iset, item_set_ancestor isa, test_catalog tc, " +
			"product prod WHERE prod.parent_product_id = 3700 AND prod.product_id = tc.product_id AND tc.item_set_id = isa.ancestor_item_set_id AND " +
			"isa.item_set_id = iset.item_set_id AND iset.item_set_type = 'TD' AND iset.SAMPLE = 'F' AND iset.subject = ?";
	private static String insertScore_lookup_item_setQuery="INSERT INTO SCORE_LOOKUP_ITEM_SET VALUES(?,?)";
	private static String get_objectives_values = "SELECT DISTINCT ISET.ITEM_SET_ID   AS ITEMSETID, ISET.ITEM_SET_NAME AS OBJECTIVENAME " +
			"FROM TEST_CATALOG TC, ITEM_SET_ANCESTOR ISA, ITEM_SET ISETTD, ITEM_SET_ITEM ISI, ITEM, ITEM_SET_ITEM ISIRE, " +
			"ITEM_SET_ANCESTOR ISARE, ITEM_SET ISET, PRODUCT PROD, ITEM_SET_CATEGORY ISCAT " +
			"WHERE TC.PRODUCT_ID = ? AND TEST_LEVEL = ? AND ISA.ANCESTOR_ITEM_SET_ID = TC.ITEM_SET_ID " +
			"AND ISETTD.ITEM_SET_ID = ISA.ITEM_SET_ID AND ISETTD.SUBJECT = ? " +
			"AND ISI.ITEM_SET_ID = ISETTD.ITEM_SET_ID AND ITEM.ITEM_ID = ISI.ITEM_ID " +
			"AND ISIRE.ITEM_ID = ITEM.ITEM_ID AND ISARE.ITEM_SET_ID = ISIRE.ITEM_SET_ID " +
			"AND ISARE.ITEM_SET_TYPE = 'RE' AND ISET.ITEM_SET_ID = ISARE.ANCESTOR_ITEM_SET_ID " +
			"AND ISET.ITEM_SET_CATEGORY_ID = ISCAT.ITEM_SET_CATEGORY_ID " +
			"AND ISCAT.ITEM_SET_CATEGORY_LEVEL = PROD.SCORING_ITEM_SET_LEVEL " +
			"AND TC.PRODUCT_ID = PROD.PRODUCT_ID";
	private static PreparedStatement ps = null;
	private static ResultSet rs=null;
	private static Connection con=null;
	public static Map<String, Integer> _OBJECTIVEMAP = new HashMap<String, Integer>();
	public static List<String> _GRADES = new ArrayList<String>();
	public static Map<Integer, String> productName = new HashMap<Integer, String>();
	
	private static enum NORMS_GROUP {F, S, W};

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
					System.out.println("File_name -> " + File_name);
					Content_area_initial=File_name.substring(2, 4);
					Content_area = processContentAreaName(Content_area_initial);
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
		String levels = ExtractUtil.getDetail("file.testLevel").trim();
		
		String File_name,file_location,Content_area_initial,Product_type,product_id,matchingFileName=null,matchingFile_location=null;
		String Source_score_type_code,dest_Score_type_code,score_lookup_id,test_form,Test_Level,Content_area,framework_code,product_internal_display_name;
		List<String> contentOfFile = new ArrayList<String>();
		List<String> itemSetIdList = new ArrayList<String>();
		List<String> tempList = null;
		Map<String,List<String>> matchingFileMap = new HashMap<String,List<String>>();
		Iterator<String> itr;

		boolean successInSCORE_LOOKUP=false,successInScore_lookup_item_set=false;
		File file = new File(path);                
		File[] files = file.listFiles(); 
		System.out.println("Processing " + path + "... "); 
			for (File inFile: files ) 
			{    
				if(inFile.getName().substring(0,2).equals("NS") && levels.contains(inFile.getName().substring(4,6)))
				{/*
					File_name="";Content_area_initial  = "";  Product_type = ""; product_id = "";
					
					Source_score_type_code="";dest_Score_type_code="";score_lookup_id="";
					test_form = " ";Test_Level = "";Content_area="";framework_code = "";product_internal_display_name =" ";
										
					File_name = inFile.getName();
					System.out.println("File_name -> " + File_name);
					file_location=path+"\\"+File_name;
					Content_area_initial=File_name.substring(2, 4);
					Content_area = processContentAreaName(Content_area_initial);
					Product_type=File_name.substring(6, File_name.length());
					if(Product_type.equals("SV"))
						product_id="3710";
					if(Product_type.equals("CB"))
						product_id="3720";
					if(Product_type.equals("MA"))
						product_id="3700";
					
					Source_score_type_code = "NSC";
					dest_Score_type_code = "SCL";
										
					Test_Level=File_name.substring(4, 6);
					String testToBe = ResourceBundle.getBundle("config").getString("file.testLevel");
					if(testToBe.contains(Test_Level)) {
						score_lookup_id	="TERRAB3"+"_"+Product_type+"_"+Test_Level+"_"+"G"+"_"+Content_area_initial;
						
						test_form = "G";framework_code = "TERRAB3";
						product_internal_display_name=getDisplayName(product_id);
						
						contentOfFile=readFileDataNS(file_location);
						matchingFileName="SE"+File_name.substring(2, 8);
						matchingFile_location=path+"\\"+matchingFileName;
						tempList = new ArrayList<String>();
						for ( itr = contentOfFile.iterator(); itr.hasNext(); ) 
						{   
							String str = itr.next().toString();                                
							String[] splitSt = str.split("    "); 
							String second_column="";
							second_column = splitSt[1].trim(); 
							tempList.add(second_column);
						}
						matchingFileMap.put(matchingFile_location, tempList);
						successInSCORE_LOOKUP=writeInSCORE_LOOKUP(contentOfFile,Source_score_type_code,dest_Score_type_code,score_lookup_id,test_form,Test_Level,Content_area,framework_code,product_internal_display_name);
						
						itemSetIdList=getItemSetID(product_id,Content_area,Test_Level);
						successInScore_lookup_item_set=writeInScore_lookup_item_set(score_lookup_id,itemSetIdList);
					}
					
				*/} else if(inFile.getName().substring(0,2).equals("SE") && levels.contains(inFile.getName().substring(4,6))) {/*
					
					File_name="";Content_area_initial  = "";  Product_type = ""; product_id = "";
					
					Source_score_type_code="";dest_Score_type_code="";score_lookup_id="";
					test_form = " ";Test_Level = "";Content_area="";framework_code = "";product_internal_display_name =" ";
										
					File_name = inFile.getName();
					System.out.println("File_name -> " + File_name);
					file_location=path+"\\"+File_name;
					Content_area_initial=File_name.substring(2, 4);
					Content_area = processContentAreaName(Content_area_initial);
					Product_type=File_name.substring(6, File_name.length());
					if(Product_type.equals("SV"))
						product_id="3710";
					if(Product_type.equals("CB"))
						product_id="3720";
					if(Product_type.equals("MA"))
						product_id="3700";
					
					Source_score_type_code = "SCL";
					dest_Score_type_code = "SEM";
					
					Test_Level=File_name.substring(4, 6);
					String testToBe = ResourceBundle.getBundle("config").getString("file.testLevel");
					if(testToBe.contains(Test_Level)) {
						score_lookup_id	="TERRAB3"+"_"+Product_type+"_"+Test_Level+"_"+"G"+"_"+Content_area_initial;
						
						test_form = "G";framework_code = "TERRAB3";
						product_internal_display_name=getDisplayName(product_id);
						System.out.println("file_location = "+file_location);
						
						contentOfFile=readFileDataSE(file_location,matchingFileMap);
						
						successInSCORE_LOOKUP=writeInSCORE_LOOKUP(contentOfFile,Source_score_type_code,dest_Score_type_code,score_lookup_id,test_form,Test_Level,Content_area,framework_code,product_internal_display_name);
					}
				*/} else if(inFile.getName().substring(0,3).equals("NCE")) {/*
						File_name = inFile.getName();
						System.out.println("File_name -> "+File_name);
						file_location=path+"\\"+File_name;
						Content_area_initial=File_name.substring(3, 5);
						Source_score_type_code = "SCL";
						dest_Score_type_code = "NCE";
						test_form = "G";
						framework_code = "TERRAB3";
						score_lookup_id	= framework_code;
						Content_area = processContentAreaName(Content_area_initial);
						contentOfFile = readFileData(file_location);
						writeInSCORE_LOOKUP_NCENP(contentOfFile, Source_score_type_code, dest_Score_type_code, score_lookup_id, 
								null, null, Content_area, framework_code, null, Content_area_initial);
				*/} else if (inFile.getName().substring(0,2).equals("NP")) {/*
					File_name = inFile.getName();
					System.out.println("File_name -> "+File_name);
					file_location=path+"\\"+File_name;
					Content_area_initial=File_name.substring(2, 4);
					Source_score_type_code = "SCL";
					dest_Score_type_code = "NP";
					test_form = "G";
					framework_code = "TERRAB3";
					score_lookup_id	= framework_code;
					Content_area = processContentAreaName(Content_area_initial);
					contentOfFile = readFileData(file_location);
					writeInSCORE_LOOKUP_NCENP(contentOfFile, Source_score_type_code, dest_Score_type_code, score_lookup_id, 
							null, null, Content_area, framework_code, null, Content_area_initial);
				*/} else if (inFile.getName().substring(0,2).equals("GE")) {/*
					File_name = inFile.getName();
					System.out.println("File_name -> "+File_name);
					file_location=path+"\\"+File_name;
					Content_area_initial=File_name.substring(2, 4);
					Source_score_type_code = "SCL";
					dest_Score_type_code = "GE";
					test_form = "G";
					framework_code = "TERRAB3";
					score_lookup_id	= framework_code;
					Content_area = processContentAreaName(Content_area_initial);
					contentOfFile = readFileDataGE(file_location);
					writeInSCORE_LOOKUP_GE(contentOfFile,Source_score_type_code,dest_Score_type_code,
							score_lookup_id,test_form,null,Content_area,framework_code,
							null, Content_area_initial);
				*/} else if (inFile.getName().substring(4,7).equals("OPI")) {/*
					File_name = inFile.getName();
					System.out.println("File_name -> "+File_name);
					file_location=path+"\\"+File_name;
					Content_area_initial=File_name.substring(0,2);
					Content_area = processContentAreaName(Content_area_initial);
					Product_type=File_name.substring(2,4);
					product_id = getProductIdFromType(Product_type);
					Source_score_type_code = "SCL";
					dest_Score_type_code = "HMR";
					test_form = "G";
					framework_code = "TERRAB3";
					score_lookup_id	= framework_code;
					contentOfFile = readFileData(file_location);
					writeInSCORE_LOOKUP_Mastery_Range(contentOfFile,Source_score_type_code, dest_Score_type_code,
							score_lookup_id, test_form,Content_area, framework_code, Content_area_initial, 
							Integer.parseInt(product_id), Product_type);
				*/}
				
			}
			if(successInSCORE_LOOKUP==true && successInScore_lookup_item_set==true)
				return true;
			else
				return false;
		     
	}
	
	public static String getProductIdFromType(String Product_type) {
		
		if(Product_type.equals("SV")) {
			return "3710";
		} else if(Product_type.equals("CB")) {
			return "3720";
		} else {
			return "3700";
		}
	}
	
	public static String processContentAreaName(String caShortName) {
		
		String caName = null;		
		if(caShortName.equals("LA"))
			caName="Language";
		if(caShortName.equals("RD"))
			caName="Reading";
		if(caShortName.equals("SS"))
			caName="Social Studies";
		if(caShortName.equals("SC"))
			caName="Science";
		if(caShortName.equals("MA"))
			caName="Mathematics";
		if(caShortName.equals("SP"))
			caName="Spelling";
		if(caShortName.equals("VO"))
			caName="Vocabulary";
		if(caShortName.equals("MC"))
			caName="Math Computation";
		if(caShortName.equals("WA"))
			caName="Word Analysis";
		if(caShortName.equals("LM"))
			caName="Language Mechanics";
		if(caShortName.equals("TL"))
			caName="Total Language";
		if(caShortName.equals("TR"))
			caName="Total Reading";
		if(caShortName.equals("TM"))
			caName="Total Math";
		if(caShortName.equals("TB"))
			caName="Total Score";
		return caName;
	}
	
	public static String processNongroupName(String nonGroupCode) {
		if(nonGroupCode.equals("F"))
			return "FALL";
		else if(nonGroupCode.equals("W"))
			return "WINTER";
		else if(nonGroupCode.equals("S"))
			return "SPRING";
		return "";
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
	
	public static List<String> readFileDataGE(String fileName) throws IOException
	{
		List<String> list = new ArrayList<String>();
		String strLine; 
		int line = 0;
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		while ((strLine = br.readLine()) != null) 
		{  line++;		
		   if(line > 2){
			list.add(strLine); 
		   }
		}  
		return list;
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
	public static List<String> readFileDataNS(String fileName) throws IOException
	{
		List<String> list = new ArrayList<String>();
		String strLine,tempLine=null; 
		int line = 0;
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		
		while ((strLine = br.readLine()) != null) 
		{  line++;		
		   if(line == 2){
			list.add(strLine); 
		   }
		   tempLine=strLine;
		}  
		String lastLine = tempLine;
		list.add(lastLine);
		return list;
	}
	public static List<String> readFileDataSE(String fileName, Map<String,List<String>> matchingFileMap) throws IOException
	{
		List<String> list = new ArrayList<String>();
		String strLine; 
		int line = 0;
		List<String> tempList = matchingFileMap.get(fileName);
		//String[] tempHolder = null; 
		ArrayList<String> tempHolder = new ArrayList<String>();
		for(Iterator<String> itr = tempList.iterator(); itr.hasNext();){
			String temp=itr.next();
			tempHolder.add(temp.trim());
		}
		int lowerLimit = Integer.parseInt(tempHolder.get(0));
		int upperLimit = Integer.parseInt(tempHolder.get(1));
		
		
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		while ((strLine = br.readLine()) != null) 
		{  line++;		
		   if(line > 1){
			    String[] splitSt = strLine.split("    "); 
				int first_column;
				first_column = Integer.parseInt(splitSt[0].trim());
				if(first_column >= lowerLimit && first_column <= upperLimit){
					list.add(strLine); 
				}
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
		return itemSetIdList;
	}
	
	public static List<String> getItemSetIDGE(String Content_area)
	{
		List<String> itemSetIdList = new ArrayList<String>();
		String name="";
		try {
			con=SqlUtil.openOASDBcon();
			ps = con.prepareStatement(itemSetIdGEQuery);
			ps.setString(1,Content_area);
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
	
	public static boolean writeInSCORE_LOOKUP_NCENP(List<String> contentOfFile,String Source_score_type_code,String dest_Score_type_code,
			String score_lookup_id,String test_form,String Test_Level,String Content_area,String framework_code,
			String product_internal_display_name, String Content_area_initial)
	{
		Iterator<String> itrFile = contentOfFile.iterator();
		Iterator<String> itr;
		String firstLine = "";
		int save=0;
		int columnIndex = 0;
		if(itrFile.hasNext()) {
			firstLine = itrFile.next().toString();
		}
		if (!firstLine.equals("")) {
			String[] gradeVal = firstLine.split(" ");
			int grade = 0;
			String gradeQmVal;
			String normsGroup;
			Map<Integer, String> columnNumber = new LinkedHashMap<Integer, String>();
			//Store in a hash map
			int qmNumber = 0;
			for(int i = 1; i < gradeVal.length; i++) {
				String[] gradeQm = gradeVal[i].split("\\.");
				grade = Integer.parseInt(gradeQm[0]);
				qmNumber = Integer.parseInt(gradeQm[1]);
				if(grade != 0 && (qmNumber == 175 || qmNumber == 475 || qmNumber == 775)) {
					columnNumber.put(new Integer(i), gradeVal[i]);
				}
			}
			try {
				con=SqlUtil.openOASDBcon();
				con.setAutoCommit(false);
				for(itr = contentOfFile.iterator(); itr.hasNext();) {
					String str = itr.next().toString();
					if(str.startsWith("Grade")) {
						continue;
					}
					String[] splitSt = str.split("\\s+"); 
					String source_score_value="",dest_score_value="";
					source_score_value = splitSt[0];
					for (Map.Entry<Integer, String> entry : columnNumber.entrySet()) {
						columnIndex = entry.getKey();
						gradeQmVal = entry.getValue();
						if(gradeQmVal.endsWith("175")) {
							normsGroup = "FALL";
						} else if (gradeQmVal.endsWith("475")) {
							normsGroup = "WINTER";
						} else if (gradeQmVal.endsWith("775")) {
							normsGroup = "SPRING";
						} else {
							normsGroup = null;
						}
						grade = Integer.parseInt(gradeQmVal.substring(0, 2));
						dest_score_value = splitSt[columnIndex];
						insertScoreLookupNCENP(Source_score_type_code, dest_Score_type_code, grade, source_score_value, 
								dest_score_value, null, null, Content_area, framework_code, null, normsGroup, Content_area_initial);
					}
				}
			} catch(SQLException se) {

				try {
					con.rollback();
					save=0;
					System.out.println("Data are not saved in SCORE_LOOKUP table.");
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				se.printStackTrace();
			}finally {
				SqlUtil.close(con,ps,rs);
			}
		}
		
		return (save==1)? true :  false;
	}
	
	private static boolean insertScoreLookupNCENP(String Source_score_type_code,String dest_Score_type_code, int grade, 
			String source_score_value, String dest_score_value, String test_form, String Test_Level, String Content_area,
			String framework_code, String product_internal_display_name, String normGroup, String Content_area_initial) {
		int save = 0;
		try {
			
			String score_lookup_id = framework_code + "_" + normGroup + "_" + grade + "_" + Content_area_initial;
			ps = con.prepareStatement(insertScore_lookupQuery_withNorms);
			ps.setString(1, Source_score_type_code);
			ps.setString(2, dest_Score_type_code);
			ps.setString(3, score_lookup_id);
			ps.setInt(4, Integer.parseInt(source_score_value.trim()));
			ps.setInt(5, Integer.parseInt(dest_score_value.trim()));
			ps.setString(6, test_form);
			ps.setString(7, Test_Level);
			ps.setString(8, String.valueOf(grade));
			ps.setString(9, Content_area);
			ps.setString(10, normGroup);
			ps.setString(11, "2011");
			ps.setString(12, framework_code);
			ps.setString(13,product_internal_display_name);
			save=ps.executeUpdate();
			SqlUtil.close(ps);
			
		}catch(SQLException e) {
			try {
				con.rollback();
				save=0;
				System.out.println("Data are not saved in SCORE_LOOKUP table.");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		e.printStackTrace();
		}
		return (save==1)? true :  false;
	}
	
	public static boolean writeInSCORE_LOOKUP_GE(List<String> contentOfFile,String Source_score_type_code,String dest_Score_type_code,
			String score_lookup_id,String test_form,String Test_Level,String Content_area,String framework_code,
			String product_internal_display_name, String Content_area_initial)
	{
		boolean save = false;
		String str;
		String source_score_value="",dest_score_value="";
		Map<String,String> isetMap = new HashMap<String, String>();
		List<String> itemSetIdList = new ArrayList<String>();
		Iterator<String> itr;
		try {
			con=SqlUtil.openOASDBcon();
			con.setAutoCommit(false);
			for(itr = contentOfFile.iterator(); itr.hasNext();) {
				str = itr.next().toString();
				String[] splitSt = str.split("\\s+"); 
				source_score_value = splitSt[0];
				dest_score_value = splitSt[1];
				score_lookup_id = framework_code + "_FALL_" + Content_area_initial;
				insertScoreLookupGE(Source_score_type_code, dest_Score_type_code, source_score_value, 
						dest_score_value, null, null, Content_area, framework_code, null, "FALL", 
						Content_area_initial, score_lookup_id);
				
				isetMap.put(score_lookup_id, Content_area);
				
				dest_score_value = splitSt[2];
				score_lookup_id = framework_code + "_WINTER_" + Content_area_initial;
				insertScoreLookupGE(Source_score_type_code, dest_Score_type_code, source_score_value, 
						dest_score_value, null, null, Content_area, framework_code, null, "WINTER", 
						Content_area_initial, score_lookup_id);
				
				isetMap.put(score_lookup_id, Content_area);
				
				dest_score_value = splitSt[3];
				score_lookup_id = framework_code + "_SPRING_" + Content_area_initial;
				save = insertScoreLookupGE(Source_score_type_code, dest_Score_type_code, source_score_value, 
						dest_score_value, null, null, Content_area, framework_code, null, "SPRING", 
						Content_area_initial, score_lookup_id);
				
				isetMap.put(score_lookup_id, Content_area);
				
			}
	} catch(SQLException e) {
		try {
			con.rollback();
			System.out.println("Data are not saved in SCORE_LOOKUP table.");
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		e.printStackTrace();
	}finally {
		SqlUtil.close(con,ps,rs);
	}
			itemSetIdList=getItemSetIDGE(Content_area);
			for (Map.Entry<String, String> entry : isetMap.entrySet()) {
				save = writeInScore_lookup_item_set(entry.getKey(),itemSetIdList);
			}
		
		return save;
	}
	
	private static boolean insertScoreLookupGE(String Source_score_type_code,String dest_Score_type_code,  
			String source_score_value, String dest_score_value, String test_form, String Test_Level, String Content_area,
			String framework_code, String product_internal_display_name, String normGroup, 
			String Content_area_initial, String score_lookup_id) {
		int save = 0;
		try {
			
			ps = con.prepareStatement(insertScore_lookupQuery_withNorms_GE);
			ps.setString(1, Source_score_type_code);
			ps.setString(2, dest_Score_type_code);
			ps.setString(3, score_lookup_id);
			ps.setInt(4, Integer.parseInt(source_score_value.trim()));
			ps.setDouble(5, Double.parseDouble(dest_score_value.trim()));
			ps.setString(6, test_form);
			ps.setString(7, Test_Level);
			ps.setString(8, null);
			ps.setString(9, Content_area);
			ps.setString(10, normGroup);
			ps.setString(11, "2011");
			ps.setString(12, framework_code);
			ps.setString(13,product_internal_display_name);
			if(dest_score_value.trim().contains("13")) {
				ps.setString(14, "+");
			} else {
				ps.setString(14, null);
			}
			save=ps.executeUpdate();
			SqlUtil.close(ps);
			
		}catch(SQLException e) {
			try {
				con.rollback();
				save=0;
				System.out.println("Data are not saved in SCORE_LOOKUP table.");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		e.printStackTrace();
		}
		return (save==1)? true :  false;
	}
	
	public static boolean writeInSCORE_LOOKUP_Mastery_Range(List<String> contentOfFile,String Source_score_type_code,String dest_Score_type_code,
			String score_lookup_id,String test_form,String Content_area,String framework_code,
			String Content_area_initial, Integer product_id, String Product_type)
	{
		int save = 0;
		String str;
		String source_score_value="",dest_score_value="";
		Map<String,String> isetMap = new HashMap<String, String>();
		List<String> itemSetIdList = new ArrayList<String>();
		Iterator<String> itr;
		String currentLevel = "";
		String levelsPopulated = ",";
		Integer objectiveId = 0;
		String objectiveName = "";
		String objectiveCode = "";
		String objectiveCodeFile = "";
		Integer destScore = 0;
		String product_internal_display_name = "";
		Float destVal = new Float(0);
		try {
			con=SqlUtil.openOASDBcon();
			con.setAutoCommit(false);
			for(itr = contentOfFile.iterator(); itr.hasNext();) {
				if(con == null || con.isClosed()) {
					con=SqlUtil.openOASDBcon();
				}
				str = itr.next().toString();
				String[] splitSt = str.split("\\s+");
				if(Content_area_initial.equalsIgnoreCase("SC")) {
					currentLevel = splitSt[1];
				} else {
					currentLevel = splitSt[2];
				}
				if(!levelsPopulated.contains(currentLevel)) {
					ps = con.prepareStatement(get_objectives_values);
					ps.setInt(1, product_id);
					ps.setString(2, currentLevel);
					ps.setString(3, Content_area);
					rs=ps.executeQuery();
					while(rs.next())
					{
						objectiveId = rs.getInt("ITEMSETID");
						objectiveName = rs.getString("OBJECTIVENAME");
						objectiveCode = objectiveName.substring(0, 2);
						objectiveName = objectiveName.substring(3).trim();
						//System.out.println(objectiveId + " - " + objectiveCode + " - " + objectiveName);
						if(!_OBJECTIVEMAP.containsKey(objectiveCode + currentLevel)) {
							_OBJECTIVEMAP.put(objectiveCode + currentLevel, objectiveId);
						}
					}
					SqlUtil.close(rs);
					SqlUtil.close(ps);
				}
				if(con == null || con.isClosed()) {
					con=SqlUtil.openOASDBcon();
				}
				levelsPopulated = levelsPopulated + currentLevel;
				if(Content_area_initial.equalsIgnoreCase("SC")) {
					objectiveCodeFile = splitSt[3];
				} else {
					objectiveCodeFile = splitSt[4];
				}
				
				if(Content_area_initial.equalsIgnoreCase("SC")) {
					destVal =  Float.valueOf(splitSt[4]) * 100;
				} else {
					destVal =  Float.valueOf(splitSt[5]) * 100;
				}
				
				destScore = destVal.intValue();
				if(!productName.containsKey(product_id)) {
					product_internal_display_name=getDisplayName(product_id.toString());
					productName.put(product_id, product_internal_display_name);
					con=SqlUtil.openOASDBcon();
				}
				dest_Score_type_code = "LMR";
				product_internal_display_name = productName.get(product_id);
				score_lookup_id = framework_code;
				score_lookup_id = score_lookup_id + "_" + dest_Score_type_code + "_" + Product_type + "_" + currentLevel + "_" + Content_area_initial + "_" + objectiveCodeFile;
				System.out.println("score_lookup_id -> " + score_lookup_id);
				ps = con.prepareStatement(insertScore_lookupQuery);
				ps.setString(1, Source_score_type_code);
				ps.setString(2, dest_Score_type_code);
				ps.setString(3, score_lookup_id);
				ps.setInt(4, new Integer(0));
				ps.setInt(5, destScore);
				ps.setString(6, test_form);
				ps.setString(7, currentLevel);
				ps.setString(8, Content_area);
				ps.setString(9, framework_code);
				ps.setString(10,product_internal_display_name);
				save=ps.executeUpdate();
				con.commit();
				SqlUtil.close(ps);
				
				if(_OBJECTIVEMAP.get(objectiveCodeFile+currentLevel) != null) {
					if(con == null || con.isClosed()) {
						con=SqlUtil.openOASDBcon();
					}
					itemSetIdList.add(_OBJECTIVEMAP.get(objectiveCodeFile+currentLevel).toString());
					writeInScore_lookup_item_set(score_lookup_id,itemSetIdList);
				}
				if(con == null || con.isClosed()) {
					con=SqlUtil.openOASDBcon();
				}
				itemSetIdList = new ArrayList<String>();
				score_lookup_id = framework_code;
				dest_Score_type_code = "HMR";
				score_lookup_id = score_lookup_id + "_" + dest_Score_type_code + "_" + Product_type + "_" + currentLevel + "_" + Content_area_initial + "_" + objectiveCodeFile;
				System.out.println("score_lookup_id -> " + score_lookup_id);
				if(Content_area_initial.equalsIgnoreCase("SC")) {
					destVal =  Float.valueOf(splitSt[5]) * 100;
				} else {
					destVal =  Float.valueOf(splitSt[6]) * 100;
				}
				
				destScore = destVal.intValue();
				ps = con.prepareStatement(insertScore_lookupQuery);
				ps.setString(1, Source_score_type_code);
				ps.setString(2, dest_Score_type_code);
				ps.setString(3, score_lookup_id);
				ps.setInt(4, new Integer(0));
				ps.setInt(5, destScore);
				ps.setString(6, test_form);
				ps.setString(7, currentLevel);
				ps.setString(8, Content_area);
				ps.setString(9, framework_code);
				ps.setString(10,product_internal_display_name);
				save=ps.executeUpdate();
				con.commit();
				SqlUtil.close(ps);
				if(_OBJECTIVEMAP.get(objectiveCodeFile+currentLevel) != null) {
					if(con == null || con.isClosed()) {
						con=SqlUtil.openOASDBcon();
					}
					itemSetIdList.add(_OBJECTIVEMAP.get(objectiveCodeFile+currentLevel).toString());
					writeInScore_lookup_item_set(score_lookup_id,itemSetIdList);
				}
			}
	} catch(SQLException e) {
		try {
			con.rollback();
			System.out.println("Data are not saved in SCORE_LOOKUP table.");
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		e.printStackTrace();
	} finally {
		SqlUtil.close(con,ps,rs);
	}
		return save == 1 ? true : false;
	}
	

	private static List<PVALFileData> readPVALFile (String path) {
		File allFile = new File(path);                
		File[] files = allFile.listFiles();
		PVALFileData pvalData = new PVALFileData();
		BufferedReader br = null;
		List<PVALFileData> pvalDataList = new ArrayList<PVALFileData>();
		List<CodeValue> dataList = null;
		String strLine = null;
		
		try {
			for(File file: files) {
				String fileName = file.getName();
				if(fileName.startsWith("PVAL")) {
					System.out.println("fileName -> " + fileName);
					br = new BufferedReader(new FileReader(file));
					dataList = new ArrayList<CodeValue>();
					int startIndex = 0;
					int endIndex = 2;
					
					while ((strLine = br.readLine()) != null) { 
						strLine = strLine.trim();
						if(strLine.trim().length() == 0) {
							pvalData.setDataList(dataList);
							String normsGroup = fileName.substring(fileName.indexOf(".") - 1, fileName.indexOf("."));
							pvalData.setNormsGroup(normsGroup);
							pvalDataList.add(pvalData);
							pvalData = new PVALFileData();
							startIndex = 0;
							endIndex = 2;
							dataList = new ArrayList<CodeValue>();
							continue;
						}
						String[] generalData = strLine.split(" ");
						if(generalData.length == 1) {
							String[] objCode = strLine.split("``");
							if(objCode.length == 1) {
								int index = 0;
								while (endIndex <= strLine.length()) {
									dataList.get(index).setValue(Double.valueOf(strLine.substring(startIndex, endIndex)) / 100);
									startIndex += 2;
									endIndex += 2;
									index++;
								}
							} else {
								int length = objCode.length * 2;
								for(int i=1; i<length; i++) {
									CodeValue val = new CodeValue();
									if(i < 10) {
										val.setCode("0" + i);
									} else {
										val.setCode(String.valueOf(i));
									}
									dataList.add(val);
								}
							}
						} else {
							pvalData.setLevel(generalData[3].split(":")[1]);
							pvalData.setGrade(generalData[4].split(":")[1]);
							pvalData.setForm(generalData[5].split(":")[1]);
							pvalData.setOther(generalData[6].split(":")[1]);
							pvalData.setContent(generalData[8].split(":")[1]);
						}
					}
					br.close();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pvalDataList;
	}
	
	public static List<PVALFileData> populatePValue (String filePath) 
	throws CloneNotSupportedException {
		
		List<PVALFileData> pvalFileDataList = readPVALFile(filePath);
		ResourceBundle rb = ResourceBundle.getBundle("config");
		String levels = rb.getString("file.testLevel");
		for(PVALFileData pvalFileData: pvalFileDataList) {
			if(!(pvalFileData.getContent() == null || pvalFileData.getGrade() == null || pvalFileData.getLevel() == null)) {
				if(levels.contains(pvalFileData.getLevel().trim())) {
					List<String> itemSetList = DBUtil.getAllItemSet(processContentAreaName(pvalFileData.getContent()), 
							pvalFileData.getLevel().trim(), getProductIdFromType(pvalFileData.getOther().trim()));
					if(itemSetList != null && itemSetList.size() > 0) {
						Map<String, String> itemMap = DBUtil.getAllItemForItemSet(itemSetList, pvalFileData);
						Map<String, Double> objSumMap = new HashMap<String, Double>();
						Map<String, String> itemObjMap = pvalFileData.getItemObjectiveMap();
						for(CodeValue codeVal : pvalFileData.getDataList()) {
							if(itemMap.get(codeVal.getCode()) != null) {
								codeVal.setItemId(itemMap.get(codeVal.getCode()));
								String objName = itemObjMap.get(codeVal.getItemId());
								if(objSumMap.containsKey(objName)) {
									Double oldValue = objSumMap.get(objName);
									objSumMap.put(objName, oldValue + codeVal.getValue());
								} else {
									objSumMap.put(objName, codeVal.getValue());
								}
							}
						}
						pvalFileData.setObjectiveItemSumMap(objSumMap);
					}
				}
			}
		}
		System.out.println("Total " + pvalFileDataList.size());
		return pvalFileDataList;
	}
}