/**
 * 
 */
package com.ctb.contentBridge.core.audit.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Calendar;
import java.text.SimpleDateFormat;

import com.ctb.contentBridge.core.domain.Configuration;
import com.ctb.contentBridge.core.domain.JobBean;
import com.ctb.contentBridge.core.exception.SystemException;
import com.ctb.contentBridge.core.util.ClosableHelper;
import com.ctb.contentBridge.core.util.ConnectionManager;
import com.ctb.contentBridge.core.util.ConnectionUtil;

/**
 * @author
 * 
 */
public class JobDAO {
	private static final String LINE_SEP = "\n";
	static ResultSet rs = null;

	public static List<JobBean> jobStatus(JobBean bean) {
		List<JobBean> jobList = new ArrayList<JobBean>();
		Statement stmt = null;
		Connection currentCon = null;
		try {

			String sPropFilePath = System.getProperty("PROPERTIES_FILE_PATH");
			Configuration configuration = new Configuration();
			configuration.load(new File(sPropFilePath));
			String dateDisplayFormat = configuration.getDateDisplay();
			String dateFormat = configuration.getDateFormat();
			// preparing some objects for connection
			// boolean isNoParam = false;

			String jobID = bean.getJobID();
			String jobName = bean.getJobName();
			/*
			 * Date jobDateFrom = bean.getJobDateFrom(); Date jobDateTo =
			 * bean.getJobDateTo();
			 */
			String jobDateFrom = bean.getStrJobDateFrom();
			String jobDateTo = bean.getStrJobDateTo();
			String jobRunStatus = bean.getJobRunStatus();
			String targetEnv = bean.getTargetEnv();

			if (jobRunStatus.equalsIgnoreCase("ALL")) {
				jobRunStatus = "%";
			}
			if (targetEnv.equalsIgnoreCase("ALL")) {
				targetEnv = "%";
			}

			String searchQuery = searchCriteria(jobID, jobName, jobRunStatus,
					jobDateFrom, jobDateTo, targetEnv, dateFormat/*
																 * , isNoParam
																 */);

			// connect to DB
			currentCon = ConnectionUtil.getContBrgConnection(configuration);
			stmt = currentCon.createStatement();
			System.out.println("Query: " + searchQuery);

			rs = stmt.executeQuery(searchQuery);

			SimpleDateFormat fomatter = new SimpleDateFormat(dateDisplayFormat);

			while (rs.next()) {

				String jbID = rs.getString("job_ID");
				String jbNm = rs.getString("job_name");
				String jbRunStatus = rs.getString("job_status");

				Date jbDateTo = rs.getDate("date_Last_Upd");
				String sToDate = "";
				if (jbDateTo != null) {
					sToDate = fomatter.format(jbDateTo);
				}
				Date jbDateFrm = rs.getDate("date_Created");
				String sFromDate = "";
				if (jbDateFrm != null) {
					sFromDate = fomatter.format(jbDateFrm);
				}

				String targetEnvironment = rs.getString("Target_Env");
				if (targetEnvironment.equalsIgnoreCase("DEV")) {
					targetEnvironment = "DEV";
				} else if (targetEnvironment.equalsIgnoreCase("NEWQA")) {
					targetEnvironment = "NEWQA";
				} else if (targetEnvironment.equalsIgnoreCase("STAGE")) {
					targetEnvironment = "STAGE";
				} else if (targetEnvironment.equalsIgnoreCase("CQA")) {
					targetEnvironment = "CQA";
				} else {
					targetEnvironment = "PRODUCTION";
				}

				bean = new JobBean();
				bean.setJobID(jbID);
				bean.setJobName(jbNm);
				bean.setJobDateFrom(jbDateFrm);
				bean.setStrJobDateFrom(sFromDate);
				bean.setJobDateTo(jbDateTo);
				bean.setStrJobDateTo(sToDate);
				bean.setJobRunStatus(jbRunStatus);
				bean.setTargetEnv(targetEnvironment);
				jobList.add(bean);

				/*
				 * System.out.println("Job id is " + jbID);
				 * System.out.println("Job Name is " + jbNm);
				 * System.out.println("Job Date Start is " + jbDateFrm);
				 * System.out.println("Job Date End is " + jbDateTo);
				 * System.out.println("Job Run Status is " + jbRunStatus);
				 * System.out.println("Job Run Status is " + targetEnvironment);
				 */
			}

			if (jobList == null || jobList.isEmpty()) {
				System.out.println("Data Does not exist for this Input");
				bean.setValid(false);
			} else {
				bean.setValid(true);
			}
		}

		catch (Exception ex) {
			System.out.println("Search failed: An Exception has occurred! "
					+ ex);
		}

		// some exception handling
		finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
				}
				rs = null;
			}

			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception e) {
				}
				stmt = null;
			}

			if (currentCon != null) {
				try {
					currentCon.close();
				} catch (Exception e) {
				}

				currentCon = null;
			}
		}

		return jobList;

	}
	

	public static String searchCriteria(String jobID1, String jobName1,
			String jobRunStatus1, String jobDateFrom1, String jobDateTo1,
			String targetEnv1, String dateFormat) {
		String searchQuery = "select * from content_publish_job where ";
		String orderQuery = "order by date_last_upd desc";
		String dynamicResult = "";
		if (jobID1 == null || "".equals(jobID1)) {
			dynamicResult = dynamicResult + "Job_Id like '%'";
		} else {
			dynamicResult = dynamicResult + "Job_Id like '" + jobID1 + "'";
		}
		if (jobName1 == null || "".equals(jobName1)) {
			dynamicResult = dynamicResult + " AND Job_Name like '%'";
		} else {
			dynamicResult = dynamicResult + " AND Job_Name like '" + jobName1
					+ "'";
		}
		if (jobRunStatus1 == null || "".equals(jobRunStatus1)
				|| "%".equals(jobRunStatus1)) {
			dynamicResult = dynamicResult + " AND Job_Status like '%'";
		} else {
			dynamicResult = dynamicResult + " AND Job_Status like '"
					+ jobRunStatus1 + "'";
		}
		if (jobDateFrom1 == null || "".equals(jobDateFrom1)
				|| "%".equals(jobDateFrom1)) {
			dynamicResult = dynamicResult + " AND Date_Created like '%'";
		} else {

			/*
			 * dynamicResult = dynamicResult + " AND Date_Created >= TO_DATE( '"
			 * + jobDateFrom1 + "','YYYY-MM-DD')";
			 */
			dynamicResult = dynamicResult + " AND Date_Created >= TO_DATE( '"
					+ jobDateFrom1 + "','" + dateFormat + "')";
		}
		if (targetEnv1 == null || "".equals(targetEnv1)
				|| "%".equals(targetEnv1)) {
			dynamicResult = dynamicResult + " AND Target_Env like '%'";
		} else {
			dynamicResult = dynamicResult + " AND Target_Env like '"
					+ targetEnv1 + "'";

		}
		if (jobDateTo1 == null || "".equals(jobDateTo1)
				|| "%".equals(jobDateTo1)) {
			dynamicResult = dynamicResult
					+ " AND (Date_Created IS NULL OR Date_Created like '%')";
		} else {
			/*
			 * dynamicResult = dynamicResult + " AND date_Last_Upd <= '" +
			 * jobDateTo1 + "','YYYY-MM-DD'"; + jobDateTo1 + "','" + dateFormat
			 * + "')";
			 */
			dynamicResult = dynamicResult + " AND Date_Created <= TO_DATE( '"
					+ jobDateTo1 + "','" + dateFormat + "')";
		}
		searchQuery = searchQuery + dynamicResult + "\n" + orderQuery;

		return searchQuery;
	}

	/*
	 * public static String searchCriteria(String jobID1, String jobName1,
	 * String jobRunStatus1,Date jobDateFrom1,Date jobDateTo1,String targetEnv1)
	 * { String searchQuery =
	 * "select * from (SELECT JOB_PK,JOB_ID,JOB_NAME,JOB_STATUS,TARGET_ENV,DATE_CREATED,DATE_LAST_UPD,DECODE(TARGET_ENV, 'QA', 'QA','DEV', 'Development','prod') res FROM CONTENT_PUBLISH_JOB)WHERE "
	 * ; String dynamicResult = ""; if (jobID1 == null || "".equals(jobID1)) {
	 * dynamicResult = dynamicResult + "Job_Id like '%'"; } else { dynamicResult
	 * = dynamicResult + "Job_Id like '" + jobID1 + "'"; } if (jobName1 == null
	 * || "".equals(jobName1)) { dynamicResult = dynamicResult +
	 * " AND Job_Name like '%'"; } else { dynamicResult = dynamicResult +
	 * " AND Job_Name like '" + jobName1 + "'"; } if (jobRunStatus1 == null ||
	 * "".equals(jobRunStatus1) || "%".equals(jobRunStatus1)) { dynamicResult =
	 * dynamicResult + " AND Job_Status like '%'"; } else { dynamicResult =
	 * dynamicResult + " AND Job_Status like '" + jobRunStatus1 + "'"; } if
	 * (jobDateFrom1 == null || "".equals(jobDateFrom1) ||
	 * "%".equals(jobDateFrom1)) { dynamicResult = dynamicResult +
	 * " AND Date_Created like '%'"; } else { dynamicResult = dynamicResult +
	 * " AND Date_Created = TO_DATE( '" + jobDateFrom1 + "','YYYY-MM-DD')";
	 * 
	 * } if (targetEnv1 == null || "".equals(targetEnv1) ||
	 * "%".equals(targetEnv1)) { dynamicResult = dynamicResult +
	 * " AND Target_Env like '%'"; } else { dynamicResult = dynamicResult +
	 * " AND Target_Env like '" + targetEnv1 + "'";
	 * 
	 * } if (jobDateTo1 == null || "".equals(jobDateTo1) ||
	 * "%".equals(jobDateTo1)) { dynamicResult = dynamicResult +
	 * " AND (date_Last_Upd IS NULL OR date_Last_Upd like '%')"; } else {
	 * dynamicResult = dynamicResult + " AND date_Last_Upd = '" + jobDateTo1 +
	 * "','YYYY-MM-DD'"; } searchQuery = searchQuery + dynamicResult;
	 * 
	 * return searchQuery; }
	 */

	public static Map getOrderParameter(Connection conn, Long jobPk)
			throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map paramMap = new HashMap();
		try {
			StringBuffer sbufQuery = new StringBuffer("SELECT pjp.param_name");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",pjp.param_value");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("FROM publish_job_param pjp");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("WHERE pjp.job_pk = ?");

			ps = conn.prepareStatement(sbufQuery.toString());
			ps.setLong(1, jobPk);
			rs = ps.executeQuery();

			String paramName = "";
			String paramValue = "";
			while (rs.next()) {
				paramName = rs.getString("param_name");
				paramValue = rs.getString("param_value");

				paramMap.put(paramName, paramValue);
			}
		} finally {
			ClosableHelper.close(ps, rs);
		}
		return paramMap;
	}
	public static String[] getJobIdEnv(Connection conn, Long jobPk,String[] envDe)
			throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
	
		
		try {
			StringBuffer sbufQuery = new StringBuffer("SELECT cpj.job_id");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",cpj.target_env");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("FROM content_publish_job cpj");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("WHERE cpj.job_pk = ?");

			ps = conn.prepareStatement(sbufQuery.toString());
			ps.setLong(1, jobPk);
			rs = ps.executeQuery();

			
			
			if (rs.next()) {
				envDe[0] = rs.getString("job_id");
				envDe[1] = rs.getString("target_env");

				
			}
		} finally {
			ClosableHelper.close(ps, rs);
		}
		return envDe;
	}
	public static Collection<JobBean> getPublishOrderJob(Connection conn)
			throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Collection<JobBean> jobList = new ArrayList();
		try {
			StringBuffer sbufQuery = new StringBuffer("SELECT cpj.job_pk");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",cpj.target_env");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",cpj.command");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("FROM content_publish_job cpj");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("WHERE cpj.job_status =  ?");

			ps = conn.prepareStatement(sbufQuery.toString());
			ps.setString(1, "New");
			rs = ps.executeQuery();

			JobBean vJobBean = null;
			while (rs.next()) {
				vJobBean = new JobBean();
				vJobBean.setJobPk(rs.getLong("job_pk"));
				vJobBean.setTargetEnv(rs.getString("target_env"));
				vJobBean.setCommand(rs.getString("command"));

				vJobBean.setParameters(getOrderParameter(conn,
						vJobBean.getJobPk()));

				jobList.add(vJobBean);

				/*updateJobStatus(conn, vJobBean.getJobPk(), "In Progress", "");*/
			}
		} finally {
			ClosableHelper.close(ps, rs);
		}
		return jobList;
	}

	public static void updateJobStatus(Connection conn, Long jobPk,
			String status, String errMsg) throws SystemException {
		PreparedStatement ps = null;
		try {
			StringBuffer sbufQuery = new StringBuffer(
					"UPDATE content_publish_job job");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("SET job.job_status = ?");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",job.error_msg = ?");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",job.date_last_upd = SYSDATE");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("WHERE job.job_pk = ?");

			ps = conn.prepareStatement(sbufQuery.toString());
			ps.setString(1, status);
			ps.setString(2, errMsg);
			ps.setLong(3, jobPk);
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new SystemException(e);
		} finally {
			ClosableHelper.close(ps);
		}
	}

	public static void jobRecordProcess(String[] paramName) throws Exception {
		PreparedStatement ps = null;
		Connection conn = null;
		try {
			ArrayList<String> data = new ArrayList<String>();
			data.add("command");
			data.add("env");
			data.add("localimagearea");
			data.add("itemfile");
			data.add("imagearea");
			data.add("jobId");

			String assesmentPath = paramName[3];
			// String assesmentXml = assesmentPath
			// .substring(assesmentPath.lastIndexOf("/") + 1,
			// assesmentPath.length());
			String assesmentXml = assesmentPath.substring(
					assesmentPath.lastIndexOf(File.separator) + 1,
					assesmentPath.length());

			String env = paramName[1].substring(
					paramName[1].lastIndexOf("/") + 1, paramName[1].length());

			String sPropFilePath = System.getProperty("PROPERTIES_FILE_PATH");
			Configuration configuration = new Configuration();
			configuration.load(new File(sPropFilePath));
			// connect to DB
			conn = ConnectionUtil.getContBrgConnection(configuration);
			// conn = ConnectionUtil.getOASConnection(configuration);
			// connect to DB
			// currentCon = ConnectionManager.getConnection();
			Long jobPk = getSeqPk(conn);
			StringBuffer sbufQuery = new StringBuffer(
					"INSERT INTO content_publish_job (job_pk,job_id,job_name,job_status,target_env,command,date_created) VALUES (?,?,?,?,?,?,SYSDATE)");

			ps = conn.prepareStatement(sbufQuery.toString());
			ps.setLong(1, jobPk);
			ps.setString(2, paramName[5]);
			ps.setString(3, "JOB_" + assesmentXml);
			ps.setString(4, "New");
			ps.setString(5, env);
			ps.setString(6, paramName[0]);
			ps.executeUpdate();

			ClosableHelper.close(ps);

			StringBuffer sbufQuerPublish = new StringBuffer(
					"INSERT INTO publish_job_param (param_pk,job_pk,param_name,param_value,date_created) VALUES (?,?,?,?,SYSDATE)");

			ps = conn.prepareStatement(sbufQuerPublish.toString());
			for (int i = 1; i < paramName.length - 1; i++) {

				ps.setLong(1, getSeqPk(conn));
				ps.setLong(2, jobPk);
				ps.setString(3, (String) data.get(i));
				ps.setString(4, paramName[i]);
				ps.executeUpdate();
			}

		} catch (SQLException e) {
			throw new SystemException(e);
		} finally {
			ClosableHelper.close(ps);
			ConnectionUtil.closeContBrgConnection();
		}
	}

	public static Long getSeqPk(Connection conn) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Long lSeqPk = new Long(0);
		try {
			StringBuffer sbufQuery = new StringBuffer(
					"SELECT seq_content_bridge_job_id.NEXTVAL");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("FROM dual");

			ps = conn.prepareStatement(sbufQuery.toString());
			rs = ps.executeQuery();

			JobBean vJobBean = null;
			while (rs.next()) {
				lSeqPk = rs.getLong(1);
			}
		} finally {
			ClosableHelper.close(ps, rs);
		}
		return lSeqPk;
	}
}
