<%@ page import="java.io.*, java.util.*"%>
<%@ page import="com.ctb.util.web.sanitizer.JavaScriptSanitizer"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>

<head>
  	<title>My Profile</title>

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
    <h1><netui:content value="My Profile"/></h1>
</td>
</tr>
<tr align="left">
<td>
    <p align="left">
		This is my profile page
    </p>
</td>    
</tr>
</table>
    

</body>
</html>
