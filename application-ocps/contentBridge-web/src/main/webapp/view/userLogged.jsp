
<%
	/* response.setHeader("Cache-Control","no-store"); 
response.setHeader("Pragma","no-cache"); 
response.setDateHeader ("Expires", 0); */
%>
<%@ page
	import="com.ctb.contentBridge.core.domain.UserBean,com.ctb.contentBridge.core.domain.JobBean,java.util.Date,java.util.ArrayList"%>



<%@ page language="java" contentType="text/html; charset=windows-1256"
	pageEncoding="windows-1256"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
   "http://www.w3.org/TR/html4/loose.dtd">
<%
	String jid = "";
	String jname = "";
	String jFrmDate = ""; 
	String jToDate = ""; 
	String jRunSts = "";
	String targetEnv= "";
%>
<html>

<head>

<meta http-equiv="Content-Type"
	content="text/html; charset=windows-1256">

<title>Audit Publish Request</title>

<link rel="stylesheet" type="text/css"
	href="styles/jquery-ui-1.9.2.custom.css" />
<link rel="stylesheet" type="text/css"
	href="styles/jquery-ui-1.9.2.custom.min.css" />
<link rel="stylesheet" type="text/css" href="styles/style.css" />
<link rel="stylesheet" type="text/css" href="styles/custom.css" />
<script src="scripts/jquery-1.7.2.min.js" type="text/javascript"></script>
<script src="scripts/jquery-ui-1.8.21.custom.min.js"
	type="text/javascript"></script>
<script src="scripts/calendar1.js" ></script>
<script src="scripts/parseDateUtil.js"></script>
<script>
	$(function() {
		$("#datepicker").datepicker();
	});
	$(document).ready(function() {
		$(".dataTable tbody tr:odd").css({
			'background-color' : '#fff',
			'color' : '#666',
			'font' : 'bold 13px Arial'
		});
		$(".dataTable tbody tr:even").css({
			'background-color' : '#82BAF2',
			'color' : '#fff',
			'font' : 'normal 13px Arial'
		});
		$("#dataTable").css({
			'border' : '1px solid #999'
		});
		$("#dataTable thead tr td").css({
			'background-color' : '#336699',
			'color' : '#fff',
			'font' : 'bold 14px Arial'
		});
		
	});
	
	
</script>
</head>
<%--<%
	if ((session.getAttribute("currentSessionUser")) != null) {
%>--%>
<input type="hidden" name="contextPath" id="contextPath" value="<%=request.getContextPath()%>">
<body style="background: #CCC;">

	<center>
		<%
			UserBean currentUser = (UserBean) (session
						.getAttribute("currentSessionUser"));
		%>


	</center>
	<!--  <h3 align="right">
		<a href="view/logout.jsp" style="color: white;">logout</a>
	</h3>-->


	<form action="JobServlet">
		
		<table width="100%" cellspacing="0" cellpadding="0" border="0">
			<tbody>
				<tr>
					<td id="pageTitleId" class="pageTitle" width="72%" valign="middle"
						height="30px">Search Job Log</td>
					<td class="pageTitle" width="6%" valign="middle" align="right">
						<!--   <input class="button" type="submit" value="Search" name="submitButton">-->
						<input class="button" type="submit" value="Search"></td>
					<!--   <td align="center" style="background-color: #336699; width:6%;"><a href="view/logout.jsp" style="color: #ffffff;" class="nav" ><h2>logout</h2></td>-->
				</tr>
			</tbody>
		</table>
		<table border="0" width="100%" cellpadding="1" cellspacing="1">

			<tr>
				<td class="prompt" colspan="4">&nbsp;</td>
			</tr>
			<tr>
				<td class="prompt" align="right">Job Id : &nbsp;</td>
				<td class="prompt" align="left"><input type="text" name="jid"
					size="23" style="color: BLACK;" /></td>
				<td class="prompt" align="right">Job Name : &nbsp;</td>
				<td class="prompt" align="left"><input type="text" name="jname"
					size="23" style="color: BLACK;" /></td>
			</tr>
			<tr>
				<td class="prompt" align="right">Date From : &nbsp;</td>
				<td class="prompt" align="left"><input class="formInputField"
					type="text" style="width: 160px" value="" name="Dfrm" id="Dfrm"> <a
					href="javascript:initializeCalendar(document.getElementById('Dfrm')).popup();">
						<img width="16" height="16" border="0" alt="Calendar"
						src="image/cal.gif"> </a></td>
				<td class="prompt" align="right">Date To : &nbsp;</td>
				<td class="prompt" align="left"><input class="formInputField"
					type="text" style="width: 160px" value="" name="Dto" id="Dto"> <a
					href="javascript:initializeCalendar(document.getElementById('Dto')).popup();">
						<img width="16" height="16" border="0" alt="Calendar"
						src="image/cal.gif"> </a></td>
			</tr>
			<tr>
				<td class="prompt" align="right">Status : &nbsp;</td>
				<td class="prompt" align="left"><select name="jbRunStatus"
					id="StatusID" ><option value=""
							selected="selected">All</option>
						<option value="New">New</option>
						<option value="In Progress">In Progress</option>	
						<option value="Success">Success</option>
						<option value="Error">Error</option>
				</select></td>
				<!--  <td class="prompt" align="right">Submitted By User : &nbsp;</td>
				<td class="prompt" align="left"><input type="text"
					name="submittedByUser" value="" class="formInputField">
				</td>-->
				<td class="prompt" align="right">Target Environment : &nbsp;</td>
				<td class="prompt" align="left"><select name="targetEnv"
					id="targetEnv" ><option value=""
							selected="selected">All</option>
						<option value="DEV">Development</option>
						<option value="NEWQA">NEWQA</option>
						<option value="PRODUCTION">Production</option>
						<option value="STAGE">Staging</option>
						<option value="CQA">CQA</option>
				</select></td>
			</tr>
		</table>
		<!-- <input type="submit" value="Search"> -->
		<table border="0" width="100%" ID="Table2">
			<tr>
				<td width="1%" align="left" class="sectionHeader1" nowrap="nowrap">
					&nbsp; <b>Search Results</b>&nbsp;&nbsp;
				<td><hr></td>
			</tr>
		</table>

		<%
			ArrayList<JobBean> jBean = (ArrayList<JobBean>) request
						.getAttribute("jobDetails");
		
		%>

		<!--  <table border="0" width="95%" cellspacing="1" cellpadding="1"
			align="center"  style="margin-top: 20px;"id="dataTable" class="dataTable">
			<thead>
				<tr>
					<td align="center" class="tableSmallHeaderCell" width="10%"
						height="20px">Job Id</td>
					<td align="center" class="tableSmallHeaderCell" width="15%"
						height="20px">Job Name</td>
					<td align="center" class="tableSmallHeaderCell" width="15%"
						height="20px">Job Start Date</td>
					<td align="center" class="tableSmallHeaderCell" width="10%"
						height="20px">Job End Date</td>
					<td align="center" class="tableSmallHeaderCell" width="22%"
						height="20px">Job Run Status</td>
				</tr>
			</thead>-->

		<%
			if (jBean != null) {
				
			
		%>
		<!--  //JobBean job=null;
						//for(JobBean jBeanIterator : jBean){-->
		<table border="0" width="95%" cellspacing="1" cellpadding="1"
			align="center" style="margin-top: 20px;" id="dataTable"
			class="dataTable">
			<thead>
				<tr>
					<td align="center" class="tableSmallHeaderCell" width="10%"
						height="20px">Job Id</td>
					<td align="center" class="tableSmallHeaderCell" width="15%"
						height="20px">Job Name</td>
					<td align="center" class="tableSmallHeaderCell" width="15%"
						height="20px">Job Run Status</td>
					<td align="center" class="tableSmallHeaderCell" width="10%"
						height="20px">Target Environment</td>
					<td align="center" class="tableSmallHeaderCell" width="22%"
						height="20px">Date Created</td>
				    <td align="center" class="tableSmallHeaderCell" width="22%"
						height="20px">Date Last Updated</td>
				</tr>
			</thead>
			<%
				for (int i = 0; i < jBean.size(); i++) {
							JobBean job = new JobBean();
							job = jBean.get(i);
							jid = job.getJobID();
							jname = job.getJobName();
							jFrmDate = job.getStrJobDateFrom();
							jToDate = job.getStrJobDateTo();
							jRunSts = job.getJobRunStatus();
							//out.print("jobname "+jname);
							targetEnv=job.getTargetEnv();
			%>


			<tbody>
				<tr>
					<td align="center" class="tableSmallHeaderCell" width="10%"
						height="20px"><%=jid%></td>
					<td align="center" class="tableSmallHeaderCell" width="15%"
						height="20px"><%=jname%></td>
					<td align="center" class="tableSmallHeaderCell" width="15%"
						height="20px"><%=jRunSts%></td>
					  <td align="center" class="tableSmallHeaderCell" width="10%"
						height="20px"><%=targetEnv%></td>
					<td align="center" class="tableSmallHeaderCell" width="22%"
						height="20px"><%=jFrmDate%></td>
						<%
						if(jToDate!=null){%>
						<td align="center" class="tableSmallHeaderCell" width="22%"
						height="20px"><%=jToDate%></td>
						<%}else{ %>
							<td align="center" class="tableSmallHeaderCell" width="22%"
								height="20px"></td>
						
						<%} %>	
						
						
						
					
				</tr>
			</tbody>
			<%
				}
					}
			%>

		</TABLE>
<%

String status= (String)request.getAttribute("jobStatus");
if(status!= null){
System.out.println("status");
%>

<table border="0" width="100%" ID="Table2" id="searchResult">
			<tr>
				<td width="1%" align="left" class="searchResult" nowrap="nowrap">
					&nbsp; <b><%= status %></b>&nbsp;&nbsp;
				
			</tr>
		</table>
		
		<%} %>
	</form>
</body>

<%--<%
	} else {
%>
<jsp:forward page="LoginPage.jsp" />
<%
	}
%>--%>
</html>