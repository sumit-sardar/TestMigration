<%@ page import="java.io.*, java.util.*"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ page import="java.net.URLEncoder"%>

                 
                 
<%	
	String itemNumber = (String)request.getAttribute("itemNumber");
    
        
    String previewUrl = request.getContextPath() + "/itemPlayer/index.jsp";
    previewUrl += "?itemNumber=" + URLEncoder.encode(itemNumber, "UTF-8");
    

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
  <title>View Question</title>
    <link href="<%=request.getContextPath()%>/resources/css/legacy.css" type="text/css" rel="stylesheet" />
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/widgets.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/js_web.jsp"></script>
</head>
<body>



<table class="legacyBodyLayout">
<tr><td id="legacyBodyViewer">


<netui:form action="ViewQuestion">

<input type="hidden" name="itemNumber" id="itemNumber" value="<%=itemNumber%>">


<table>

</table>
<table >
<tr>
  <td >
      <iframe src="<%=previewUrl%>" frameborder="0" style="border-color: #000; border-style: solid; border-width: 0px; margin: 0px; width: 900; height: 700;" ></iframe>
  </td>
</tr>
</table>

</netui:form>

</td></tr></table>

</body>
</html>
