
<% 
/* response.setHeader("Cache-Control","no-store"); 
response.setHeader("Pragma","no-cache"); 
response.setDateHeader ("Expires", 0); */  
%>
<%@ page import="java.sql.*,com.ctb.contentBridge.core.domain.UserBean,com.ctb.contentBridge.core.domain.JobBean"%>


<%@ page language="java" contentType="text/html; charset=windows-1256"
	pageEncoding="windows-1256" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
   "http://www.w3.org/TR/html4/loose.dtd">

<html>

<head>

<meta http-equiv="Content-Type"
	content="text/html; charset=windows-1256">
<title>Job Search page</title>
</head>

<body>
<input type="text" value="Search" >     
</body>

<%-- 
<%
if((session.getAttribute("currentSessionUser"))!=null)
{
%>
<body bgcolor="BLUE" text="WHITE">

	<form action="JobServlet"  >

	Job ID:<input type="text" name="jid" size="20" style="color: white;" /> 
	Job Name :<input type="text" name="jname" size="22" style="color: white;" /> 
	<!-- Date from :<input type="date" name="Dfrm" /> 
	Date to :<input type="week" name="Dto" />  -->
	Job Run Status <select name="jbRunStatus" id="StatusID" size="1">
		<option>All</option>
		<option>Success</option>
		<option>In Progress</option>
		<option>Error</option>
	</select><br><br>
<input type="submit" value="Search" >      
	</form>	
<center>
		<% JobBean jobSts = (JobBean)(request.getAttribute("jobDetails"));%>

		Welcome
		<%= jobSts.getJobID() + " " + jobSts.getJobName() %>	
	
	
	<TABLE BORDER="1">
		<TR>
			<TH>JOBID</TH>
			<TH>JOBName</TH>
		</TR>
		<TR>
			<TD><%= jobSts.getJobID() %></TD>
			<TD><%= jobSts.getJobName() %></TD>
		</TR>
	</TABLE></center>
</body>
<%
    }
    else
    {
    %>
<jsp:forward page="userLogged.jsp" />
<%
     }
    %> --%>
</html>