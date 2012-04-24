<%@ page import="java.io.*, java.util.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="ctb-web.tld" prefix="ctbweb"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<lb:bundle baseName="testsessionApplicationResource" />

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html lang="en">

<head>
  	<title>Report</title>

	<link href="<%=request.getContextPath()%>/resources/css/jquery-ui-1.8.16.custom.css" rel="stylesheet" type="text/css" />
	<link href="<%=request.getContextPath()%>/resources/css/main.css" rel="stylesheet" type="text/css" />
	<link href="<%=request.getContextPath()%>/resources/css/roundCorners.css" rel="stylesheet" type="text/css" />
    <link href="<%=request.getContextPath()%>/resources/css/widgets_NEW.css" type="text/css" rel="stylesheet" />
          
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery-1.6.2.min.js"></script>
  	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery.blockUI.min.js"></script>    
  	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery.corners.js"></script> 
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/roundCorners.js"></script>
  	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/main.js"></script>    

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
								<td align="left" width="100%"><img src="<%=request.getContextPath()%>/resources/images/ctb_oas_logo.png"></td>
							</tr>
						</table>
					</td>
				</tr>


				<!-- BODY SECTION -->
				<tr>
				  	<td align="left" valign="top">

<div class="feature">

  	<div class="feature" style="background-color: #ffffff; border:0px; padding: 10px;">
      	
<table border=0 width="100%">
<tr>
    <td style="padding: 0px;">
        <!-- Begin content-->
        
<table style="border-collapse: collapse; border-style: none; border-width: 0px; width: 100%; height: 100%; margin: 0px; padding: 0px; ">

<tr>
    <td style="vertical-align: top; margin: 0px; padding: 0px;">
    
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
        <iframe src="<%= reportUrl %>" style="width: 100%; height: 500px;" frameborder="0" ></iframe>
    </td>
</tr>

</table>
        
	</td>
</tr>



</table>

	</div>
</div>

</td></tr>


				<tr>
				  	<td align="left" valign="top">
    					<jsp:include page="/resources/jsp/oas_footer.jsp" />  
				  	</td>
				</tr>

</table>

</td></tr>




</table>


    
    
</body>
</html>
