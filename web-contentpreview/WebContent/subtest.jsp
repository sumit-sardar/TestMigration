<% 
// Fake mime type.
response.setContentType("text/xml"); 

String startingQuestionNumber = (String)request.getAttribute( "startingQuestionNumber" );
String title = (String) request.getAttribute("title");
String timeLimit = (String) request.getAttribute("timeLimit");
String itemReferences = (String) request.getAttribute("itemReferences");
String orderReferences = (String) request.getAttribute("orderReferences");

%>
<ob_assessment <%= startingQuestionNumber %>>
		<assessment_title><%= title %></assessment_title>
		<ob_element_list>
		<%= itemReferences %>
		</ob_element_list>
        <ob_element_select_order>
        <%= orderReferences %>
        </ob_element_select_order>
</ob_assessment>

