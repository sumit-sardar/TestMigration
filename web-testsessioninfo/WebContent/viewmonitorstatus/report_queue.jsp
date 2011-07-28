<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>

<html>

<head>
    <link href="<%=request.getContextPath()%>/resources/css/legacy.css" type="text/css" rel="stylesheet" />
    <link href="<%=request.getContextPath()%>/resources/css/widgets.css" type="text/css" rel="stylesheet" />
</head>
  
<body>


<%
    String url = (String)request.getAttribute("url");
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



<tr>
    <td style="background-color: #fff;; width: 100%; height: 100%; vertical-align: top; margin: 0px; padding: 0px;">
        <iframe name="queue_frame" src="<%= url %>" style="width: 100%; height: 100%;" frameborder="5"></iframe>
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
