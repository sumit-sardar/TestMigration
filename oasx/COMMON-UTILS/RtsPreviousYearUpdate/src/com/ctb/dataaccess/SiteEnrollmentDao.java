package com.ctb.dataaccess;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ctb.bean.SiteSurvey;
import com.ctb.bean.SiteSurveyEnrollment;

import java.util.ArrayList;
import java.util.List;
public class SiteEnrollmentDao implements SiteEnrollmentSQL {
	private static PreparedStatement ps = null;
	private static ResultSet rs = null;
	public static List<SiteSurvey> getSchoolData(int customerId, int productId,String path,Connection con) throws SQLException {
		List<SiteSurvey> list = new ArrayList<SiteSurvey>();
		SiteSurvey siteSurvey = null;
		String sitePath=null;
		
		ps = con.prepareStatement(GET_SCHOOL_DETAILS_FROM_OAS);
		ps.setInt(1,productId );
		ps.setInt(2, customerId);
		rs = ps.executeQuery();
		
		while (rs.next()) {
			siteSurvey = new SiteSurvey();
			siteSurvey.setSiteId(rs.getString("SCHOOL_ID"));
			siteSurvey.setGrade(rs.getString("GRADE"));
			sitePath=path+rs.getString("DISTRICT_NAME")+"/"+rs.getString("SCHOOL_NAME");
			siteSurvey.setSitePath(sitePath);
			list.add(siteSurvey);
		}
	return list;
	}
	
	public static List<SiteSurveyEnrollment> getEnrollmentData(List<SiteSurvey> surveyList,int customerId,Connection con)throws SQLException {
		List<SiteSurveyEnrollment> list = new ArrayList<SiteSurveyEnrollment>();
		SiteSurveyEnrollment siteEnrollment=null;
		String grade=null;
		String siteId=null;
		String sitePath=null;
		for(int i=0; i< surveyList.size(); i++)
		{
			siteId=surveyList.get(i).getSiteId();
			//System.out.println(" Site_Id= "+siteId);
			sitePath=surveyList.get(i).getSitePath();
			//System.out.println(" Site_Path = "+sitePath);
			grade=surveyList.get(i).getGrade();
			ps = con.prepareStatement(GET_SITE_SURVEY_ID);
			ps.setString(1, siteId);
			ps.setString(2, sitePath);
			ps.setInt(3, customerId);
			rs = ps.executeQuery();
			if (rs.next()) {
				siteEnrollment = new SiteSurveyEnrollment();
				siteEnrollment.setSiteSurveyId(rs.getLong("SITE_SURVEY_ID"));
				System.out.println(" SITE_SURVEY_ID = "+rs.getLong("SITE_SURVEY_ID") + " and  Grade = "+grade);
				siteEnrollment.setGrade(grade);
				
				list.add(siteEnrollment);
			}
		}
		return list;
	}
	
	public static void updateSiteEnrollment(List<SiteSurveyEnrollment> enrillmentList,Connection con)throws SQLException {
		long siteSurveyId=0;
		String grade=null;
		long siteSurveyIdLast=0;
		String third=null, fourth=null, fifth=null,sixth=null,seventh=null,eight=null;
		
		
	
		for(int i=0; i< enrillmentList.size(); i++)
		{
			siteSurveyId=enrillmentList.get(i).getSiteSurveyId();
			if(siteSurveyIdLast != siteSurveyId){
				 third="F"; fourth="F"; fifth="F";sixth="F";seventh="F";eight="F";
			}
			siteSurveyIdLast = siteSurveyId;
			int updateStatus=0;
			grade=enrillmentList.get(i).getGrade();
			if(grade.equals("03"))
				third="T";
			else if(grade.equals("04"))
				fourth="T";
			else if(grade.equals("05"))
				fifth="T";
			else if(grade.equals("06"))
				sixth="T";
			else if(grade.equals("07"))
				seventh="T";
			else if(grade.equals("08"))
				eight="T";
				
			ps = con.prepareStatement(UPDATE_SITE_SURVEY_ENROLLMENT);
			ps.setString(1, third);
			ps.setString(2, fourth);
			ps.setString(3, fifth);
			ps.setString(4, sixth);
			ps.setString(5, seventh);
			ps.setString(6, eight);
			ps.setLong(7, siteSurveyId);
			updateStatus=ps.executeUpdate();
					
			if(updateStatus==1)
			{
				con.commit();
				System.out.println("Following Site_Survey_ID  "+siteSurveyId + "  is updated for Grade "+grade +".");
				
			}
		}
	}
	
}

