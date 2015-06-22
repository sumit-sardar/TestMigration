<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="java.util.List, java.util.ArrayList"%>


<%@page import="main.java.bean.ItemResponseCR"%>
<!-- <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> -->
<!DOCTYPE html>
<html lang="en">

<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>Roster Item List</title>
<link rel="stylesheet" href="css/bootstrap.min.css">
<link rel="stylesheet" href="css/custom.css">
<script src="js/jQuery1.11.3.js"></script>
<script src="js/bootstrap.min.js"></script>
<!--[if lt IE 9]>
 <script src="/js/ie8.js" type="text/javascript"></script>
 <script src="/js/respond.js" type="text/javascript"></script>
<![endif]-->
<script type="text/javascript">
         $(document).ready(function(){
        
	          $(".url").on('click',function(){
	    
	          	if($("#alert")){
	          		$("#alert").hide();
	          	}
	          });
	         
	         });
         
      </script> 
</head>
<body>
<nav class="navbar navbar-inverse">
 <div class="container-fluid">
 <div class="navbar-header">
		<a class="navbar-brand">Fetch Audio Response</a>
 </div>
<div>
      <ul class="nav navbar-nav">
		
        <li class="active"><a href="index.html">New Test Roster</a></li>
         
      </ul>	
	</div>
</div>
</nav>


<%
	List<ItemResponseCR> itemList = null;
	Object result = request.getAttribute("testRosterItemIdList");
	String success = (String) request.getAttribute("success");
	
	if(result !=null){
		itemList = (ArrayList<ItemResponseCR>) result;

	//boolean successFlag = (boolean) success;
	if(itemList != null && !(itemList.isEmpty())){
		

%>

<div class="container-fluid">
<pre class="heading"><b>Item List</b></pre> 
<table border="1" class="table table-bordered">
	<tr>
		<td> <b>Content Area</b> </td>
		<td> <b>Item ID</b> </td>
		<td>  <b>Audio Response Link</b> </td>
	</tr>
	<%
		for (ItemResponseCR irc : itemList) {
	%>
	<tr>
		<td><%=irc.getItemSetID()%></td>
		<td><%=irc.getItemId()%></td>
		<%
			String audioUrl = irc.getAudioUrl();
		%>
		
		<td>
		<%if(irc.getAudioUrl() != null){ 
			
		%>
			<a class="url" href="/AudioUrlFetchApp/GetAudioServlet?audioUrl=<%= irc.getAudioUrl()%>" >
		Download Audio Response </a>
		<% 
		
		}else{%>
			Download link not available
		<%}%>
		</td>
	</tr>
	<%
		}
	
	%>
</table>

<% 	}else{%>
<div class="alert alert-warning">
  <strong>Warning:</strong> No Data Found.
  <br>
  <br>
Please click on <span class="label label-primary">New Test Roster</span> in navigation bar and enter valid Test Roster Id
</div>
	<%
}	
}%>
</div>
<br>

<%if(success != null){ 

if(success.equalsIgnoreCase("false")){ 

%>

<div class="container">
<div class="alert alert-danger" id="alert">
    <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
    <strong>Error: </strong> Audio download failed. Please configure proper URL. 
  </div>
</div>
	
<%} 
}%>


</body>
</html>