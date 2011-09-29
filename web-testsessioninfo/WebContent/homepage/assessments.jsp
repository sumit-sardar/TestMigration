<%@ page import="java.io.*, java.util.*"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
 
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>
<netui-data:declareBundle bundlePath="helpResources" name="help"/>

<netui-template:template templatePage="/resources/jsp/oas_template.jsp">
    <netui-template:setAttribute name="title" value="${bundle.web['homepage.window.title']}"/>
    <netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.home']}"/>
<netui-template:section name="bodySection">
 
<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->



<netui:form action="assessments">


<table width="100%" border="0">
<tr valign="top">
<td valign="top">
    <h1><netui:content value="Assessments Home"/></h1>
    <p><netui:content value="Content goes here"/></p>
</td>
</tr>
</table>
    


</netui:form>

<script type="text/javascript">

$(document).ready(function(){
	selectTabAndMenu("assessments", "sessionsMenu");
});

</script>

<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
    </netui-template:section>
</netui-template:template>


