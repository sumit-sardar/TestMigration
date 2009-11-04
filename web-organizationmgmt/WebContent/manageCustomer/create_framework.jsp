<%@ page import="java.io.*, java.util.*"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>

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
<netui-template:setAttribute name="title" value="${bundle.web['addframework.window.title']}"/>
<netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.addFramework']}"/>
<netui-template:section name="bodySection">


<%            
    List levelList = (List)request.getAttribute("levelList"); 
    String userAgent = request.getHeader("User-Agent");
%>


<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->

<!-- start form -->
<netui:form action="createFramework">

<netui:hidden dataSource="actionForm.byCustomerProfileVisible"/> 
<netui:hidden dataSource="actionForm.byCustomerBillingVisible"/> 
<netui:hidden dataSource="actionForm.byCustomerMailingVisible"/> 
<netui:hidden tagId="actionElement" dataSource="actionForm.actionElement"/> 

<h1>
    <netui:content value="${pageFlow.pageTitle}"/>
</h1>

<!-- title -->
<p>
    <netui:content value="Build an organization structure from the top layer down. Name each layer to match the structure of the customer organization you are building."/>
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


<!-- message -->



<!-- framework table -->
<p>
<ctbweb:frameworkTable levelList="<%=levelList%>" userAgent="<%=userAgent%>" width="510" />
</p>

<!-- buttons -->
<p>
    <netui:button  type="submit" value="Back" action="addCustomer" onClick="return collectData();"/>
    <netui:button  type="submit" value="Save" action="saveAddEditCustomer" onClick="return collectData();"/>
    <netui:button  type="submit" value="Cancel" action="findCustomer"/>
</p>

<script type="text/javascript">
    initCreation();
</script>


</netui:form>
        
<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
</netui-template:section>
</netui-template:template>
