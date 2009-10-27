<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<% 
// Fake mime type.
response.setContentType("text/xml"); 

%>
	<ob_assessment>
	  <assessment_title>Color and Font Preview</assessment_title>
      
	  <ob_element_list>
	    <f id="1"  h="5E06A6534A1374C5368CB119966F" k="1" />
	    <f id="2"  h="5E06A6534A1374C5368CB119966F" k="1" />
	    <f id="3"  h="5E06A6534A1374C5368CB119966F" k="1" />
	  </ob_element_list>
      
    <ob_element_select_order>
        <e id="1"/>
        <e id="2"/>
        <e id="3"/>
    </ob_element_select_order>
      
	</ob_assessment>
    
