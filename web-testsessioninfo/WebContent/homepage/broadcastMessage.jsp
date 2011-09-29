<%@ page import="java.io.*, java.util.*"%>
<%@ page import="com.ctb.util.web.sanitizer.JavaScriptSanitizer"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>

<head>
  	<title>Broadcast Message</title>

	<link href="<%=request.getContextPath()%>/resources/css/widgets.css" rel="stylesheet" type="text/css" />
	<link href="<%=request.getContextPath()%>/resources/css/main.css" rel="stylesheet" type="text/css" />
	<link href="<%=request.getContextPath()%>/resources/css/roundCorners.css" rel="stylesheet" type="text/css" />
          
  	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/js_web.js"></script>  
  	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/main.js"></script>    
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery.js"></script>
  	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery.corners.js"></script> 
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/roundCorners.js"></script>
     
</head>

<body>

<table width="100%" border="0">
<tr>
<td>
    <h1><netui:content value="Broadcast Message: Home"/></h1>
</td>
</tr>
<tr align="left">
<td>
    <p align="left">
		(Sunday May 2, 12:00 noon EST) The ISTEP+ OAS online testing system network is now available.  
		Students can start testing or go back into the system and continue testing where they left off.  Please check back to this page for updates.  
		If you have additional questions you can contact the CTB/Indiana Help Desk at 800-282-1132 option 2.
    </p>
</td>    
</tr>
</table>
    

</body>
</html>
