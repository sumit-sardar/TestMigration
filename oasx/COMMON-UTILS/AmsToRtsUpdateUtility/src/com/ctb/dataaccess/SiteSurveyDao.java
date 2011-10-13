package com.ctb.dataaccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ctb.bean.SiteSurvey;

public class SiteSurveyDao implements SiteSurveySQL {

	public static SiteSurvey getSiteSurveyData(String siteId, String siteName,
			Connection con) throws SQLException {
		PreparedStatement preStatement = null;
		SiteSurvey siteSurvey = null;
		ResultSet rs = null;

		try {
			preStatement = con.prepareStatement(GET_SITE_SURVEY_BY_ID_AND_NAME);
			preStatement.setString(1, siteId);
			preStatement.setString(2, siteName);
			rs = preStatement.executeQuery();

			if (rs.next()) {
				siteSurvey = new SiteSurvey();
				siteSurvey.setSiteId(rs.getString("SITE_ID"));
				siteSurvey.setSiteSurveyId(rs.getLong("SITE_SURVEY_ID"));
				siteSurvey.setSiteName(rs.getString("SITE_NAME"));
				siteSurvey.setSiteType(rs.getString("SITE_TYPE"));
				siteSurvey.setSitePath(rs.getString("SITE_PATH"));
			}
		} finally {

			ConnectionManager.close(preStatement);
			ConnectionManager.close(rs);
		}

		return siteSurvey;

	}

	public static int updateSiteSurvey(SiteSurvey newSiteSurvey, Connection con) throws SQLException {
		PreparedStatement preStatement = null;
		int rowcount = 0;

		try {
			preStatement = con
					.prepareStatement(UPDATE_SITE_SURVEY_ID_AND_NAME_PATH);
			preStatement.setString(1, newSiteSurvey.getSiteId());
			preStatement.setString(2, newSiteSurvey.getSiteName());
			preStatement.setString(3, newSiteSurvey.getSitePath());
			preStatement.setLong(4, newSiteSurvey.getSiteSurveyId());
			rowcount = preStatement.executeUpdate();
			return rowcount;
		} finally {
			ConnectionManager.close(preStatement);

		}

	}

	public static int updateAllSiteSurveySchoolBelowTheCorporation(
			SiteSurvey newSiteSurvey, SiteSurvey oldSiteSurvey, Connection con)
			throws SQLException {
		PreparedStatement preStatement = null;
		int roupdateCount = 0;
	/*	String SQL = UPDATE_SITE_SURVEY_SCHOOL_PATH_BELOW_THE_CORPORATION + "'"+oldSiteSurvey.getSitePath()+ "/%'" ;*/

		try {
		/*	preStatement = con
					.prepareStatement( SQL );*/
			preStatement = con
			.prepareStatement( UPDATE_SITE_SURVEY_SCHOOL_PATH_BELOW_THE_CORPORATION );
			preStatement.setString(1, oldSiteSurvey.getSitePath()+"/");
			preStatement.setString(2, newSiteSurvey.getSitePath()+"/");
			preStatement.setString(3, oldSiteSurvey.getSitePath()+"/%");
			roupdateCount = preStatement.executeUpdate();
			return roupdateCount;
		} finally {
			ConnectionManager.close(preStatement);

		}

	}

	public static SiteSurvey getSiteSurveyDataByIdAndName(String siteId, String siteName,
			boolean isCorporation, Connection con) throws SQLException {
		PreparedStatement preStatement = null;
		SiteSurvey siteSurvey = null;
		ResultSet rs = null;

		try {
			preStatement = con
					.prepareStatement(GET_SITE_SURVEY_BY_ID_AND_NAME1);
			preStatement.setString(1, siteId);
			preStatement.setString(2, siteName);
			if (isCorporation) {
				preStatement.setString(3, "CORPORATION");
			} else {
				preStatement.setString(3, "SCHOOL");
			}

			rs = preStatement.executeQuery();

			if (rs.next()) {
				siteSurvey = new SiteSurvey();
				siteSurvey.setSiteId(rs.getString("SITE_ID"));
				siteSurvey.setSiteSurveyId(rs.getLong("SITE_SURVEY_ID"));
				siteSurvey.setSiteName(rs.getString("SITE_NAME"));
				siteSurvey.setSiteType(rs.getString("SITE_TYPE"));
				siteSurvey.setSitePath(rs.getString("SITE_PATH"));
			}
			if (rs.next()){
				throw new SQLException("Too many record found for siteId["+siteId+"] and siteName["+siteName+"]");
			}
		} finally {

			ConnectionManager.close(preStatement);
			ConnectionManager.close(rs);
		}

		return siteSurvey;

	}

	public static SiteSurvey getSiteSurveyDataByIdNameAndCorpPath(String siteId,
			String siteName, String path, boolean isCorporation, Connection con) throws SQLException {
		

		PreparedStatement preStatement = null;
		SiteSurvey siteSurvey = null;
		ResultSet rs = null;

		try {
			preStatement = con
					.prepareStatement(GET_SITE_SURVEY_BY_ID_NAME_PATH);
			preStatement.setString(1, siteId);
			preStatement.setString(2, siteName);
			if (isCorporation) {
				preStatement.setString(3, "CORPORATION");
			} else {
				preStatement.setString(3, "SCHOOL");
			}
			preStatement.setString(4, path);
			
			rs = preStatement.executeQuery();

			if (rs.next()) {
				siteSurvey = new SiteSurvey();
				siteSurvey.setSiteId(rs.getString("SITE_ID"));
				siteSurvey.setSiteSurveyId(rs.getLong("SITE_SURVEY_ID"));
				siteSurvey.setSiteName(rs.getString("SITE_NAME"));
				siteSurvey.setSiteType(rs.getString("SITE_TYPE"));
				siteSurvey.setSitePath(rs.getString("SITE_PATH"));
			}
			if (rs.next()){
				throw new SQLException("Too many record found for siteId["+siteId+"] and siteName["+siteName+"]");
			}
		} finally {

			ConnectionManager.close(preStatement);
			ConnectionManager.close(rs);
		}

		return siteSurvey;

	
	}
}
