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
    List reportList = (List)request.getAttribute("reportList");
    String selectedReport = (String)request.getAttribute("selectedReport");
    String reportUrl = (String)request.getAttribute("reportUrl");
    String testAdminId = (String)request.getAttribute("testAdminId");
    
    System.out.println("reportUrl = " + reportUrl + "       selectedReport = " + selectedReport);    
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



<!-- TURNLEAF REPORT LIST -->
<tr>
    <td>
        <table class="reportNavLayout">
        <tr>
            <td>
                <ctbweb:reporNavigation reportList="<%= reportList %>" selectedReport="<%= selectedReport %>" testAdminId="<%= testAdminId %>"/>
            </td>
        </tr>
        </table>
    </td>
</tr>



<!-- TURNLEAF REPORT CONTENT -->
<tr>
    <td style="background-color: #fff; width: 100%; height: 100%; vertical-align: top; margin: 0px; padding: 0px;">
        <iframe src="<%= reportUrl %>" style="width: 100%; height: 100%;" frameborder="0" ></iframe>
    </td>
</tr>

 
    
<!-- FOOTER -->
<tr>
    <td style="vertical-align: bottom; margin: 0px; padding: 0px;">
        <jsp:include page="/resources/jsp/footer.jsp" />  
    </td>
</tr>

</table>
    
    
</body>
</html>
