<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="java.io.*, java.util.*"%>

<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="ctb-web.tld" prefix="ctbweb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>

<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>
<netui-data:declareBundle bundlePath="helpResources" name="help"/>

<netui-template:template templatePage="/resources/jsp/template_add_edit_customer.jsp">
<netui-template:setAttribute name="title" value="${bundle.web['editframework.window.title']}"/>
<netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.editFramework']}"/>
<netui-template:section name="bodySection">

<%            
    List levelList = (List)request.getAttribute("levelList"); 
	String userAgent = request.getHeader("User-Agent");
   
%>


<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->
<!-- start form -->
<netui:form action="editFramework">

	<netui:hidden dataSource="actionForm.selectedCustomerId"/>
	<netui:hidden tagId="customerMaxPage" dataSource="actionForm.customerMaxPage"/>
	<netui:hidden tagId="customerPageRequested" dataSource="actionForm.customerPageRequested"/>
	<netui:hidden dataSource="actionForm.customerSortColumn"/>
	<netui:hidden dataSource="actionForm.customerSortOrderBy"/>
	<netui:hidden dataSource="actionForm.byCustomerProfileVisible"/> 
	<netui:hidden dataSource="actionForm.byCustomerBillingVisible"/> 
	<netui:hidden dataSource="actionForm.byCustomerMailingVisible"/>
	<netui:hidden tagId="actionElement" dataSource="actionForm.actionElement" /> 
	
	
	
	<netui:hidden dataSource="actionForm.enableLicense"/> 
	
	<!-- title -->
	<h1>
	    <netui:content value="${pageFlow.pageTitle}"/>
	</h1> 
	
		
	<p>
	    <netui:content value="Select a layer to edit. Click Save to commit your changes. You cannot delete a layer which contains one or more suborganizations."/>
	</p>
	
	
	<div id="errorDiv" style="display: none">
	<p>
	    <ctb:message title="One or more names contain invalid values:" style="errorMessage" >
	          <netui:content value="Please re-enter name with only these characters: A-Z, a-z, 0-9, /, \, -, ', (, ), &, +, comma, period, space. Name cannot be empty or contain all spaces."/>
	    </ctb:message>
	</p>    
	</div>
	
	<div id="errorDupNameDiv" style="display: none">
	<p>
	    <ctb:message title="One or more fields contain invalid formats or invalid values:" style="errorMessage" >
	          <netui:content value="Please do not enter duplicate layers."/>
	    </ctb:message>
	</p>    
	</div>
	
	<div id="errorLengthDiv" style="display: none">
	<p>
	    <ctb:message title="One or more names contain invalid values:" style="errorMessage" >
	          <netui:content value="Please re-enter name with not more than 32 charecters."/>
	    </ctb:message>
	</p>    
	</div>
	
	<div id="messageDiv" style="display: block">
    	
    	
	</div>
    
	    <!-- framework table -->
	<p>
		<ctbweb:frameworkTable levelList="<%=levelList%>" userAgent="<%=userAgent%>" width="510" />
	</p>
	

	<!-- buttons -->
	<p>
	     <netui:button type="submit" value="Save" action="saveFramework" onClick="return collectData();"/>
	     <netui:button type="submit" value="Cancel" action="cancelCurrentAction"/>
	   
	</p>
	
	<script type="text/javascript">
    	setButtonState();
	</script>

</netui:form>
<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
</netui-template:section>
</netui-template:template>