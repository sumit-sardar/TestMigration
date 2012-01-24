<%@ page import="java.io.*, java.util.*"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ page import="java.net.URLEncoder"%>

                 
                 
<%
    String studentName = (String)request.getAttribute("studentName");
    String question_bgrdColor = (String)request.getAttribute("question_bgrdColor");
    String question_fontColor = (String)request.getAttribute("question_fontColor");
    String answer_bgrdColor = (String)request.getAttribute("answer_bgrdColor");
    String answer_fontColor = (String)request.getAttribute("answer_fontColor");
    String fontSize = (String)request.getAttribute("fontSize");
        
    String previewUrl = request.getContextPath() + "/previewer/index.jsp";
    
    previewUrl += "?studentName=" + URLEncoder.encode(studentName, "UTF-8");
    previewUrl += "&answerBgColor=" + URLEncoder.encode( answer_bgrdColor, "UTF-8");
    previewUrl += "&answerFgColor=" + URLEncoder.encode( answer_fontColor, "UTF-8");
    previewUrl += "&answerFontSize=" + URLEncoder.encode( fontSize, "UTF-8" );
    previewUrl += "&questionBgColor=" + URLEncoder.encode( question_bgrdColor, "UTF-8");
    previewUrl += "&questionFgColor=" + URLEncoder.encode( question_fontColor, "UTF-8");
    previewUrl += "&questionFontSize=" + URLEncoder.encode( fontSize, "UTF-8" );
    
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

<input type="hidden" name="question_bgrdColor" id="question_bgrdColor" value="<%=question_bgrdColor%>">
<input type="hidden" name="question_fontColor" id="question_fontColor" value="<%=question_fontColor%>">
<input type="hidden" name="answer_bgrdColor" id="answer_bgrdColor" value="<%=answer_bgrdColor%>">
<input type="hidden" name="answer_fontColor" id="answer_fontColor" value="<%=answer_fontColor%>">
<input type="hidden" name="fontSize" id="fontSize" value="<%=fontSize%>">

<table class="transparent" width="800">
<tr>
<td>
    <h1><netui:content value="Adjust Student Color/Font Settings"/></h1>
</td>
<td align="right">
    <input type="button" class="ui-widget-header" name="Close" value="Close" onClick="self.close();" >
</td>
</tr>
</table>

<p>
    <netui:content value="To preview color and font selections in the sample test below, click Log in, and then Start. You can preview the whole three-question sample test, or<br>click Close at any time to return to the student record."/>
</p>


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
