<%@ page import="java.io.*"%>
<%@ page import=" java.util.*"%>
<% 
// Fake mime type.
response.setContentType("text/xml"); 

String startingQuestionNumber = (String)request.getAttribute( "startingQuestionNumber" );

String title = (String) request.getAttribute("title");

String itemReferences = (String) request.getAttribute("itemReferences");
String orderReferences = (String) request.getAttribute("orderReferences");

%>

	<ob_assessment <%= startingQuestionNumber %>>
		<assessment_title><%= title %></assessment_title>
		<%= itemReferences %>
        <%= orderReferences %>
	</ob_assessment>
