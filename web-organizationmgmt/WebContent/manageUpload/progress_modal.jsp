<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>

<netui:html>
  <head>
    <netui:base/>
    <title>Upload Data</title>
    <link href="/OrganizationManagementWeb/resources/css/progress_modal.css" type="text/css" rel="stylesheet" />    
    <script type="text/javascript" src="/OrganizationManagementWeb/resources/js/progress_bar.js"></script>
  </head>
   
  <body onload="startFileUpload();">

    <br/>
    <div id="titleDiv" style="display: block" align="center">
        File Uploading ...
    </div>
    <p align="center">
        <img src="/OrganizationManagementWeb/resources/images/status/progress.gif" id="bar">        
	</p>
    <!--
    <p align="center">
		<button onclick="window.parent.hidePopWin(true)">Cancel</button>
	</p>
    -->
  
  </body>
   
</netui:html>
