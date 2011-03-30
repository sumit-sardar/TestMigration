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
  <title>Color/Font Preview</title>
    <link href="<%=request.getContextPath()%>/resources/css/legacy.css" type="text/css" rel="stylesheet" />
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/widgets.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/js_web.jsp"></script>
</head>
<body>



<table class="legacyBodyLayout">
<tr><td id="legacyBody">


<netui:form action="colorFontPreview">

<input type="hidden" name="itemNumber" id="itemNumber" value="<%=itemNumber%>">


<table class="transparent" width="800">
<tr>
<td>
    <h1><netui:content value="View Question"/></h1>
</td>
<td align="right">
    <input type="button" name="Close" value="Close" onClick="self.close();" >
</td>
</tr>
</table>
<table class="transparent">
<tr class="transparent">
  <td class="transparent">
      <iframe src="<%=previewUrl%>" frameborder="0" style="border-color: #000; border-style: solid; border-width: 1px; margin: 0px; width: 800px; height: 600px;" ></iframe>
  </td>
</tr>
</table>

</netui:form>

</td></tr></table>

</body>
</html>
