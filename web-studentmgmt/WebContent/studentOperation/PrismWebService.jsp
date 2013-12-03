<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script src="/StudentWeb/resources/js/jquery-1.4.4.min.js" type="text/javascript"></script>
<script type="text/javascript">
$( document ).ready(function() {
	$( "#editStd").click(function() {
		alert( "Edit Student Web Service called." );
		$.ajax({
			url: "editStudentWS.do?studentID="+$("#studentId").val(),
			type: "POST",
			success : function(data, textStatus, XMLHttpReques){
				alert("success");
			},
			error : function(XMLHttpRequest, textStatus, errorThrown){
				alert("Error");
			}
		});
	});
	
	$( "#scoring").click(function() {
		alert( "Scoring Web Service called." );
		$.ajax({
			url: "scoringWS.do?rosterId="+$("#rosterId").val()+"&stdID="+$('#stdID').val()+"&sessionId="+$('#sessionId').val(),
			type: "POST",
			success : function(data, textStatus, XMLHttpReques){
				alert("success");
			},
			error : function(XMLHttpRequest, textStatus, errorThrown){
				alert("Error");
			}
		});
	});
});
</script>
</head>
<body>
<h1>Edit Student</h1>
Student Id : <input type="text"
id="studentId"/>
<input type="button" id="editStd" value="Edit Student"/>
<div>---------------------------------------------------------------------</div>
<h1>Score</h1>
Student Id : <input type="text"
id="stdID"/><br/>
Roster Id : <input type="text"
id="rosterId"/><br/>
Session Id : <input type="text"
id="sessionId"/>
<input type="button" id="scoring" value="Score"/>
</body>
</html>