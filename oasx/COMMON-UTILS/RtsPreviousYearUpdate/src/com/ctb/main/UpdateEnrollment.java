package com.ctb.main;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ctb.bean.SiteSurvey;
import com.ctb.bean.SiteSurveyEnrollment;
import com.ctb.util.ExtractUtil;
import com.ctb.util.SqlUtil;
import com.ctb.dataaccess.SiteEnrollmentDao;

public class UpdateEnrollment {
	private static Integer CUSTOMER_ID_OAS = new Integer(ExtractUtil.getDetail("oas.customerId"));
	private static Integer PRODUCT_ID = new Integer(ExtractUtil.getDetail("oas.productId"));
	private static Integer CUSTOMER_ID_RTS = new Integer(ExtractUtil.getDetail("rts.customerId"));
	private static String PATH = ExtractUtil.getDetail("sitePath.firstPart");
	
	private static Connection conOAS=null;
	private static Connection conAMS=null;
	
	public static void main(String args[])
	{
		
		List<SiteSurvey> surveyList = new ArrayList<SiteSurvey>();
		List<SiteSurveyEnrollment> enrollmentList = new ArrayList<SiteSurveyEnrollment>();
		try{
			conOAS= SqlUtil.openOASDBcon();
			surveyList= SiteEnrollmentDao.getSchoolData(CUSTOMER_ID_OAS,PRODUCT_ID,PATH,conOAS);
			conAMS = SqlUtil.openAMSDBcon();
			//System.out.println("Testing connection" + con2.toString());
			enrollmentList=SiteEnrollmentDao.getEnrollmentData(surveyList, CUSTOMER_ID_RTS,conAMS);
			//System.out.println("Hello");

			SiteEnrollmentDao.updateSiteEnrollment(enrollmentList, conAMS);
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		finally {
			SqlUtil.close(conOAS, null, null);
			SqlUtil.close(conAMS, null, null);
		}
		
	}
	
	

}
