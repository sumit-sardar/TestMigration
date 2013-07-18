<%@ page import="java.io.*, java.util.*"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%
	String userKey = (String)request.getAttribute("userKey");
	String userName = (String)request.getAttribute("userName");
	String password = (String)request.getAttribute("password");
	String orgNodeId = (String)request.getAttribute("orgNodeId");
	String sessionId = (String)request.getAttribute("sessionId");
	String resultText = (String)request.getAttribute("resultText");
%>

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
  	<title>Clicker Web Service Test</title>

    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate">
    <meta http-equiv="Expires" content="Sat, 01 Dec 2001 00:00:00 GMT">
    
	<link href="<%=request.getContextPath()%>/resources/css/widgets.css" rel="stylesheet" type="text/css" />
	<link href="<%=request.getContextPath()%>/resources/css/main.css" rel="stylesheet" type="text/css" />
	<link href="<%=request.getContextPath()%>/resources/css/roundCorners.css" rel="stylesheet" type="text/css" />
     
     
  	<script type="text/javascript" src="<%=request.getContextPath()%>/sanitize.js"></script>    
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery.js"></script>
  	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery.corners.js"></script> 
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/roundCorners.js"></script>
  	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/main.js"></script>    
     
</head>

<body>

<netui:form action="clickerService">

<input type="hidden" id="status" name="status"   value="submit"/>

<!-- MAIN BODY -->

<table class="simpleBody">
	<tr>
		<td align="center" valign="top" >
			<table class="bodyLayout">

				<!-- HEADER SECTION -->
				<tr class="bodyLayout">
					<td>
					 
						<table class="headerLayout" >
							<tr>
								<td align="left" width="40%"><img src="<%=request.getContextPath()%>/resources/images/ctb_oas_logo.png"></td>
								<td align="left" width="60%"><h1>Cliker Web Service Test Page</h1></td>
							</tr>
						</table>
					</td>
				</tr>


				<!-- BODY SECTION -->
				<tr>
				  	<td align="left" valign="top">

<div class="feature" style="background-color: #DEECF6; border:1px; padding: 10px;">

  	<div class="feature" style="background-color: #ffffff; border:1px; padding: 5px;">
      	
<table width="80%" cellpadding="5" cellspacing="5">
<tr>
	<td colspan="3">
		<li> authenticateUser and getUserTopNode are prerequisite for all calls.</li>
		<li> getSessionsForNode is prerequisite for submitStudentResponses.</li>	
	</td>
</tr>
<tr>
	<td colspan="3">Userkey:&nbsp;<input type="textbox" id="userKey" name="userKey" value="<%=userKey%>" size=105 /></td>
</tr>
<tr>
	<td width=200>Username:&nbsp;<input type="textbox" id="userName" name="userName" value="<%=userName%>" /></td>
	<td width=200>Password:<input type="textbox" id="password" name="password" value="<%=password%>" /></td>
	<td>
		<input type="button" value="authenticateUser" onclick="submitStatus('authenticateUser');" />&nbsp;&nbsp;
		<input type="button" value="getUserTopNode" onclick="submitStatus('getUserTopNode');" />&nbsp;&nbsp;
	</td>
</tr>
<tr>
	<td width=200>OrgNodeId:<input type="textbox" id="orgNodeId" name="orgNodeId" value="<%=orgNodeId%>" /></td>
	<td colspan="2">
		<input type="button" value="getChildrenNodes" onclick="submitStatus('getChildrenNodes');" />&nbsp;&nbsp;
		<input type="button" value="getSessionsForNode" onclick="submitStatus('getSessionsForNode');" />&nbsp;&nbsp;
	</td>
</tr>
<tr>
	<td width=200>SessionId:&nbsp;<input type="textbox" id="sessionId" name="sessionId" value="<%=sessionId%>" /></td>
	<td colspan="2">
		<input type="button" value="getRostersInSession" onclick="submitStatus('getRostersInSession');" />&nbsp;&nbsp;
		<input type="button" value="getTestStructure" onclick="submitStatus('getTestStructure');" />&nbsp;&nbsp;
	</td>
</tr>
<tr>
	<td width=200>Responses:&nbsp;
	<select id="responses" name="responses">
		<option value="A">All A</option>
		<option value="B">All B</option>
		<option value="C">All C</option>
		<option value="D">All D</option>
		<option value="ABCD">Alternate A|B|C|D</option>
	</select>
	</td>
	<td colspan="2">
		<input type="button" value="submitStudentResponses" onclick="submitStatus('submitStudentResponses');" />&nbsp;&nbsp;
	</td>
</tr>
</table>

</div>
</div>

</td>
</tr>

</table>

		<div id="resultText">
			<br/>
			<h2><%= resultText %></h2>
		</div>

</netui:form>

</body>
</html>