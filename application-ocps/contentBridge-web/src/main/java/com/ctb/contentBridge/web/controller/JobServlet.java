package com.ctb.contentBridge.web.controller;

/**
 * @author 543559
 *
 */
import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import java.util.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ctb.contentBridge.core.audit.dao.JobDAO;
import com.ctb.contentBridge.core.domain.Configuration;
import com.ctb.contentBridge.core.domain.JobBean;


/**
 * Servlet implementation class JobServlet
 */
public class JobServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public JobServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {

		try {
			Date mydate= null;
			JobBean job = new JobBean();
			job.setJobID(request.getParameter("jid"));
			job.setJobName(request.getParameter("jname"));
			
			String dFrm=request.getParameter("Dfrm");
			String dTo=request.getParameter("Dto");
						
			if(dFrm != null && dFrm.length() > 0){
				job.setStrJobDateFrom(dFrm);
			}/*else{
				dFrm=null;
			}*/
			if(dTo != null && dTo.length() > 0){
				job.setStrJobDateTo(dTo);
			}/*else{
				dTo=null;
			}*/
			
			
			//Date dTo=request.getParameter("Dto");
			//dateFormat.
			//String convertedDateFrm =dateFormat.format(dFrm);
			//mydate = new Date(convertedDateFrm.g);
			
			//String convertedDateTo = dateFormat.format(dTo);
			//job.setJobDateFrom(convertedDateFrm);
			//job.setJobDateFrom(convertedDateTo);
			job.setJobRunStatus(request.getParameter("jbRunStatus"));
			job.setTargetEnv(request.getParameter("targetEnv"));
			
			List<JobBean> jobList = new ArrayList<JobBean>();
			//job = JobDAO.jobStatus(job);
			jobList = (List<JobBean>) JobDAO.jobStatus(job);
			if(jobList.isEmpty()){
				request.setAttribute("jobStatus", "DATA NOT FOUND");
				request.setAttribute("jobDetails", null);
			} else {
				request.setAttribute("jobDetails", jobList);
			}
			RequestDispatcher rd= getServletContext().getRequestDispatcher("/view/userLogged.jsp");
	        rd.forward(request, response);
			/*if (job.getJobID() != null ) {
				if (!job.isValid()) {

					//HttpSession session = request.getSession(true);
					HttpSession session = request.getSession();
					session.setAttribute("jobDetails", job);
					request.setAttribute("jobDetails", null);
					//response.sendRedirect("userLogged.jsp"); // logged-in page
					RequestDispatcher rd= getServletContext().getRequestDispatcher("/view/userLogged.jsp");
			        rd.forward(request, response);  
			      } else {
			        	//HttpSession session = request.getSession(true);
						HttpSession session = request.getSession();
						session.setAttribute("jobDetails", job);
						request.setAttribute("jobDetails", jobListjob);
						//response.sendRedirect("userLogged.jsp"); // logged-in page
						RequestDispatcher rd= getServletContext().getRequestDispatcher("/view/userLogged.jsp");
				        rd.forward(request, response);
			        }
			}

			else
				response.sendRedirect("view/invalidLogin.jsp"); // error page
*/		}

		catch (Throwable theException) {
			System.out.println(theException);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	
	private static java.util.Date parseDate(String asDate, String asFormat)
			throws Exception {
		SimpleDateFormat mvDateFormat = null;
		java.util.Date mvDate = null;
		try {
			if (asFormat != null) {
				mvDateFormat = new SimpleDateFormat(asFormat);
				mvDateFormat.setLenient(false);
				if (asDate == null || asDate.length() == 0
						|| asDate.trim().equals("")) {
					mvDate = null;
				} else if (asFormat.length() > asDate.trim().length()) {
					throw new Exception();
				} else
					mvDate = mvDateFormat.parse(asDate);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		return mvDate;
	}

}
