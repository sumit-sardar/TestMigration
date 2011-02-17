<%@ page language = "java" isErrorPage="true" import = "com.ctb.servlet.AbstractCTBServlet,com.ctb.util.iknowxml.R2Entities,java.util.*, java.io.*" contentType="text/html;charset=windows-1252" %>
<html>
<head>
<META NAME="MEDIAGENERATOR" CONTENT="MEDIA ERRORS">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<TITLE>Media Generation Validation and Error page</TITLE>
</head>
<body>




<center><H2>Media Generation Processing Error Page</H2></center>


<center>
Error Message: <%=((Throwable)request.getAttribute(com.ctb.servlet.AbstractCTBServlet.ERROR_ATT)).getMessage()%>
</center>
<BR><BR><BR><BR><BR><BR>
<center><H2>if you do not understand the above message, please forward the below text to CCS-DEV@EPPG.COM</H2></center>
<center><H2>otherwise, PLEASE IGNORE THE REST OF THIS PAGE DURING THE TESTING PHASE</H2></center>
<BR><BR><BR><BR><BR><BR>
<%
    StringWriter writer = new StringWriter();
    PrintWriter prn = new PrintWriter(writer);

    ((Throwable)request.getAttribute(com.ctb.servlet.AbstractCTBServlet.ERROR_ATT)).printStackTrace(prn);
    prn.flush();

 %>
<%=writer.toString()%>
<BR><BR><BR>
</html>
