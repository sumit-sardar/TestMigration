<%@ page import="java.io.*, java.util.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="ctb-web.tld" prefix="ctbweb"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>

<html lang="en">

<head>
    <link href="<%=request.getContextPath()%>/resources/css/legacy.css" type="text/css" rel="stylesheet" />
    <link href="<%=request.getContextPath()%>/resources/css/widgets.css" type="text/css" rel="stylesheet" />
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/widgets.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/js_web.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/embed.js"></script>    
</head>
  
<body>


<%
    String reportUrl = (String)request.getAttribute("reportUrl");
%>

<table style="border-collapse: collapse; border-style: none; border-width: 0px; width: 100%; height: 100%; margin: 0px; padding: 0px; ">

<tr>
    <td style="vertical-align: top; margin: 0px; padding: 0px;">
    
        <!-- HEADER -->
        <jsp:include page="/resources/jsp/report_header.jsp" />        


        <!-- NAVIGATION -->
        <jsp:include page="/resources/jsp/report_navigation.jsp" />        
        
    </td>    
</tr>


<!-- TURNLEAF REPORT CONTENT -->
<tr>
    <td style="background-color: #fff;; width: 0%; height: 0%; vertical-align: top; margin: 0px; padding: 0px;">
        <iframe name="report_frame" src="<%= reportUrl %>" style="width: 0%; height: 0%;" frameborder="0"  ></iframe>
    </td>
</tr>

<script>
	document.location.href='/TestSessionInfoWeb/viewmonitorstatus/reportQueue.do';
</script>
 
    
<!-- FOOTER -->
<tr>
    <td style="vertical-align: bottom; margin: 0px; padding: 0px;">
        <jsp:include page="/resources/jsp/footer.jsp" />  
    </td>
</tr>

</table>
    
</body>
</html>
