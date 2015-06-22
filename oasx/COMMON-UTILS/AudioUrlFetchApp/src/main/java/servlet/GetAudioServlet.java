package main.java.servlet;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.java.bean.ItemResponseCR;
import main.java.util.AWSStorageUtil;
import main.java.util.DBUtil;

import org.springframework.util.FileCopyUtils;

/**
 * Servlet implementation class for Servlet: GetAudioServlet
 * Gets Test Roster ID from user and retrieves corresponding audio response from S3 bucket
 *
 */
 public class GetAudioServlet extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
   static final long serialVersionUID = 1L;
   //HttpSession session = null;
	static ArrayList<ItemResponseCR> irList = null;
    /* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public GetAudioServlet() {
		super();
	}   	
	// Method to create Blank Error Page
	public void createBlankPageError(HttpServletResponse res){
		PrintWriter writer;
		try {
			writer = res.getWriter();
			writer.println("<html>");
			writer.println("<head>");
			writer.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">");
			writer.println("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1\">");
			writer.println("<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">");
			writer.println("<title>Blank Error Page</title>");
			writer.println("<link rel=\"stylesheet\" href=\"css/bootstrap.min.css\">");
			writer.println("<link rel=\"stylesheet\" href=\"css/custom.css\">");
			writer.println("<script src=\"js/jQuery1.11.3.js\"></script>");
			writer.println("<script src=\"js/bootstrap.min.js\"></script>");
			writer.println("<!--[if lt IE 9]>");
			writer.println("<script src=\"/js/ie8.js\" type=\"text/javascript\"></script>");
			writer.println("<script src=\"/js/respond.js\" type=\"text/javascript\"></script>");
			writer.println("<![endif]-->");
			writer.println("</head>");
			writer.println("<body bgcolor=white>");
			writer.println("<header>");
			writer.println("<nav class=\"navbar navbar-inverse\">");
			writer.println("<div class=\"container-fluid\">");
			writer.println("<div class=\"navbar-header\">");
			writer.println("<a class=\"navbar-brand\">Fetch Audio Response</a>");
			writer.println("</div>");
			writer.println("<div>");
			writer.println("<ul class=\"nav navbar-nav\">");
				
			writer.println("<li class=\"active\"><a href=\"index.html\">New Test Roster</a></li>");
			writer.println("</ul>	");
			writer.println("</div>");
			writer.println("<div>");
			writer.println("</nav>");
			writer.println("</header>");
			writer.println("<div class=\"container-fluid\">");
			writer.println("<div class=\"alert alert-warning\">");
			writer.println("<strong>Warning:</strong> Test Roster ID cannot be blank.");
			writer.println("<br>");
			writer.println(" <br>");
			writer.println("Please click on <span class=\"label label-primary\">New Test Roster</span> in navigation bar and enter valid Test Roster Id");
			writer.println("</div>");
			writer.println("</div>");
			writer.println("</body>");
			writer.println("</html>");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	// Method to create Error Page
	public void createErrorPage(HttpServletResponse res){
		PrintWriter writer;
		try {
			writer = res.getWriter();
			writer.println("<html>");
			writer.println("<head>");
			writer.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">");
			writer.println("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1\">");
			writer.println("<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">");
			writer.println("<title>Error Page</title>");
			writer.println("<link rel=\"stylesheet\" href=\"css/bootstrap.min.css\">");
			writer.println("<link rel=\"stylesheet\" href=\"css/custom.css\">");
			writer.println("<script src=\"js/jQuery1.11.3.js\"></script>");
			writer.println("<script src=\"js/bootstrap.min.js\"></script>");
			writer.println("<!--[if lt IE 9]>");
			writer.println("<script src=\"/js/ie8.js\" type=\"text/javascript\"></script>");
			writer.println("<script src=\"/js/respond.js\" type=\"text/javascript\"></script>");
			writer.println("<![endif]-->");
			writer.println("</head>");
			writer.println("<body bgcolor=white>");
			writer.println("<nav class=\"navbar navbar-inverse\">");
			writer.println("<div class=\"container-fluid\">");
			writer.println("<div class=\"navbar-header\">");
			writer.println("<a class=\"navbar-brand\">Fetch Audio Response</a>");
			writer.println("</div>");
			writer.println("<div>");
			writer.println("<ul class=\"nav navbar-nav\">");
				
			writer.println("<li class=\"active\"><a href=\"index.html\">New Test Roster</a></li>");
			writer.println("</ul>	");
			writer.println("</div>");
			writer.println("<div>");
			writer.println("</nav>");
			writer.println("<div class=\"container-fluid\">");
			writer.println("<div class=\"alert alert-warning\">");
			writer.println("<strong>Warning:</strong> Invalid Test Roster ID.");
			writer.println("<br>");
			writer.println(" <br>");
			writer.println("Please click on <span class=\"label label-primary\">New Test Roster</span> in navigation bar and enter valid Test Roster Id");
			writer.println("</div>");
			writer.println("</body>");
			writer.println("</html>");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//System.out.println("Do get called!!!!");
		String audioUrl = request.getParameter("audioUrl");
		//System.out.println("Audio URL*********"+audioUrl);
		String success = "false";
		ByteArrayInputStream inStream = null;
		OutputStream outStream = null;
	
		try {
			/* Connecting to S3 bucket to retrieve audio response */
			AWSStorageUtil aWSStorageUtil = AWSStorageUtil.getInstance();
			aWSStorageUtil.getObjectList();
			byte[] bytes = aWSStorageUtil.getBytesFromS3(audioUrl);
			System.out.println("Bytes length"+bytes.length);
			System.out.println(bytes.length);
			System.out.println("End");
			success = "true";
			
			String contentType = null;
			System.out.println("contentType-->"+contentType);
			inStream = new ByteArrayInputStream(bytes);
			String mimeType = audioUrl.substring(audioUrl.lastIndexOf(".")+1);
			// obtains ServletContext
			ServletContext context = getServletContext();
			//gets MIME type of the file
			//String mimeType = context.getMimeType(location);
			if (mimeType.equalsIgnoreCase("mp3")) {
			//set to binary type if MIME mapping not found
			    mimeType = "audio/mpeg3";
			 }
			else{
				mimeType = "audio/ogg";
			}
			//System.out.println("MIME type: " + mimeType);
			// modifies response
			response.setContentType(mimeType);
			response.setContentLength(bytes.length);
			// forces download
			String headerKey = "Content-Disposition";
			String downloadFileName = audioUrl.substring(audioUrl.lastIndexOf("/")+1);
			//String dirName = audioUrl.substring(0,location.lastIndexOf("/"));
			String headerValue = String.format("attachment; filename=\"%s\"", downloadFileName);
			
			response.setHeader(headerKey, headerValue);
			// obtains response's output stream
			 outStream = response.getOutputStream();
			byte[] buffer = new byte[4096];
			int bytesRead = -1;
			while ((bytesRead = inStream.read(buffer)) != -1) {
			   outStream.write(buffer, 0, bytesRead);
			}
			inStream.close();
			outStream.close();
			
			System.out.println("Done");
		
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			success = "false";
			e.printStackTrace();
		}finally{
			request.setAttribute("success", success);
			request.setAttribute("testRosterItemIdList",irList);
			if(success.equalsIgnoreCase("false")){
				RequestDispatcher view = request.getRequestDispatcher("/RosterItemList.jsp");
				view.forward(request, response);
			}
		
		}
		
	}  	
	
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		/*
		 * Retrieves data for a Test Roster ID. 
		 * */	
	
		String rosterId = request.getParameter("testRosterId");
		try{
		if((rosterId != null) && !("".equalsIgnoreCase(rosterId))) {
			int testRosterId = Integer.parseInt(rosterId);
			irList = DBUtil.fetchItemCRList(testRosterId);
			request.setAttribute("testRosterItemIdList",irList);
			
			RequestDispatcher view = request.getRequestDispatcher("/RosterItemList.jsp");
			 view.forward(request, response); 
		}else if("".equalsIgnoreCase(rosterId)){
			createBlankPageError(response);
		}
		}catch(NumberFormatException ne){
			System.out.println("Inside NumberFormatException****");
			createErrorPage(response);
			
		}catch(Exception e){
			System.out.println("Inside exception****");
			RequestDispatcher view = request.getRequestDispatcher("/blank_error.html");
			 view.forward(request, response); 
			
		}
	}
	

	
 }

