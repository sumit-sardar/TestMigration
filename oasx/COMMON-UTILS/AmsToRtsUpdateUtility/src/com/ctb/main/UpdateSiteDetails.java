package com.ctb.main;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.TreeMap;

import com.ctb.bean.SiteSurvey;
import com.ctb.dataaccess.AbstractConnectionManager;
import com.ctb.dataaccess.ConnectionManager;
import com.ctb.dataaccess.SiteSurveyDao;
import com.ctb.util.CSVFileReader;

public class UpdateSiteDetails {

	/**
	 * @param args
	 */
	@SuppressWarnings("unchecked")
	public static void main(String... args) throws Exception {
		// TODO Auto-generated method stub

		Connection con = null;
		String dbprop = null;
		String csv = null;

		if (args.length != 2) {

			System.err.println("Incorrect argument....");
			System.out
					.println("Please provide the argument with order: <properties file> <csv file> <year>");
			throw new Exception("Incorrect argument....");
		} else {

			dbprop = args[0];
			csv = args[1];

			if (!dbprop.substring(dbprop.indexOf(".") + 1).equals("properties")) {

				System.out
						.println("First Argument is not a properties file...");
				System.out
						.println("Please provide the three argument with order: <properties file> <csv file> <year>");
				throw new Exception();

			} else if (!csv.substring(csv.indexOf(".") + 1).equals("csv")) {

				System.out.println("Secound Argument is not a CSV file...");
				System.out
						.println("Please provide the three argument with order: <properties file> <csv file> <year>");
				throw new Exception();

			}
			AbstractConnectionManager.processProperties(dbprop);

		}

		try {

			con = ConnectionManager.getConnection();
			con.setAutoCommit(false);

			List list = CSVFileReader.getFileContent(csv);
			String rowData[] = null;

			TreeMap<String, SiteSurvey> corpMap = new TreeMap<String, SiteSurvey>();

			for (int i = 0; i < list.size(); i++) {

				System.out.println("Processing started for row ==>" + (i + 1));

				rowData = (String[]) list.get(i);
				if (isCorporation(rowData)) {
					updateCorporation(con, corpMap, rowData);

				} else {
					updateSchool(con, corpMap, rowData);
				}

			}

			ConnectionManager.commitTransaction(con);

		} catch (Exception e) {
			e.printStackTrace();
			ConnectionManager.rollBackTransaction(con);

		} finally {
			ConnectionManager.close(con);
			System.out.println("Finished....");
		}

	}

	private static void updateSchool(Connection con,
			TreeMap<String, SiteSurvey> corpMap, String[] rowData)
			throws Exception {
		SiteSurvey corpoSiteSurvey = corpMap.get(rowData[4] + "$-$"
				+ rowData[5]);
		if (corpoSiteSurvey == null) {
			corpoSiteSurvey = SiteSurveyDao.getSiteSurveyDataByIdAndName(
					rowData[4].trim(), rowData[5].trim(), true, con);
		}
		if (corpoSiteSurvey == null) {
			throw new SQLException("No corporation record found for siteId["
					+ rowData[4] + "] and siteName[" + rowData[5] + "]");
		} else {
			corpMap.put(rowData[4] + "$-$" + rowData[5], corpoSiteSurvey);

			SiteSurvey oldSiteSurvey = SiteSurveyDao
					.getSiteSurveyDataByIdNameAndCorpPath(rowData[0].trim(),
							rowData[1].trim(), corpoSiteSurvey.getSitePath()
									+ "/" + rowData[1].trim(), false, con);
			if (oldSiteSurvey == null) {
				System.err.println("No school found for corpoeration["
						+ rowData[1].trim() + "].");
				throw new Exception("No school record found for siteId["
						+ rowData[4] + "] and siteName[" + rowData[5] + "]");
			}
			SiteSurvey newSiteSurvey = createNewSchoolSiteSurvey(oldSiteSurvey,
					corpoSiteSurvey, rowData);
			int roupdateCount = SiteSurveyDao.updateSiteSurvey(newSiteSurvey,
					con);

		}

	}

	private static void updateCorporation(Connection con,
			TreeMap<String, SiteSurvey> corpMap, String[] rowData)
			throws Exception {

		SiteSurvey oldSiteSurvey = SiteSurveyDao.getSiteSurveyDataByIdAndName(
				rowData[4].trim(), rowData[5].trim(), true, con);
		if (oldSiteSurvey == null) {
			System.err.println("No data found for corpoeration["
					+ rowData[1].trim() + "].");
			throw new Exception("No corporation record found for siteId["
					+ rowData[4] + "] and siteName[" + rowData[5] + "]");
			// return;

		}
		SiteSurvey newSiteSurvey = createNewCorporateSiteSurvey(oldSiteSurvey,
				rowData);
		int roupdateCount = SiteSurveyDao.updateSiteSurvey(newSiteSurvey, con);
		roupdateCount = SiteSurveyDao
				.updateAllSiteSurveySchoolBelowTheCorporation(newSiteSurvey,
						oldSiteSurvey, con);
		System.out.println(roupdateCount
				+ " scohools path are updated for corporation ["
				+ oldSiteSurvey.getSiteName() + "].");
		corpMap.put(oldSiteSurvey.getSiteId() + "$-$"
				+ oldSiteSurvey.getSiteName(), newSiteSurvey); // old

	}

	private static boolean isCorporation(String[] rowData) {

		if (rowData[0].trim().length() == 0 || rowData[1].trim().length() == 0
				|| rowData[2].trim().length() == 0
				|| rowData[3].trim().length() == 0) {
			System.out.println(" Record contains corporation data.");
			return true;
		}
		System.out.println(" Record contains school data.");
		return false;
	}

	private static SiteSurvey createNewCorporateSiteSurvey(
			SiteSurvey oldSiteSurvey, String[] rowData) {
		SiteSurvey siteSurvey = new SiteSurvey();
		String oldSitePath = oldSiteSurvey.getSitePath();
		String newSitePath = oldSitePath.substring(0, oldSitePath
				.lastIndexOf(oldSiteSurvey.getSiteName()))
				+ rowData[7];
		siteSurvey.setSiteId(rowData[6]);
		siteSurvey.setSiteName(rowData[7]);
		siteSurvey.setSitePath(newSitePath);
		siteSurvey.setSiteSurveyId(oldSiteSurvey.getSiteSurveyId());
		siteSurvey.setSiteType(oldSiteSurvey.getSiteType());

		return siteSurvey;
	}

	private static SiteSurvey createNewSchoolSiteSurvey(
			SiteSurvey oldSiteSurvey, SiteSurvey corpoSiteSurvey,
			String[] rowData) {
		SiteSurvey siteSurvey = new SiteSurvey();
		String newSitePath = corpoSiteSurvey.getSitePath() + "/" + rowData[3];

		siteSurvey.setSiteId(rowData[2]);
		siteSurvey.setSiteName(rowData[3]);
		siteSurvey.setSitePath(newSitePath);
		siteSurvey.setSiteSurveyId(oldSiteSurvey.getSiteSurveyId());
		siteSurvey.setSiteType(oldSiteSurvey.getSiteType());

		return siteSurvey;
	}

}
