<%@ page import="java.io.*, java.util.*"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="ctb-web.tld" prefix="ctbweb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>
<netui-data:declareBundle bundlePath="helpResources" name="help"/>

<netui-template:template templatePage="/resources/jsp/template.jsp">
<netui-template:setAttribute name="title" value="Edit Framework"/>
<netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.viewFramework']}"/>
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

<netui:hidden dataSource="actionForm.byCustomerProfileVisible"/> 
<netui:hidden dataSource="actionForm.byCustomerBillingVisible"/> 
<netui:hidden dataSource="actionForm.byCustomerMailingVisible"/> 
<netui:hidden dataSource="actionForm.actionElement"/> 

<h1>
    <netui:content value="View Customer Framework"/>
</h1>

<!-- title -->



<!-- framework table -->
<p>
<ctbweb:frameworkTable levelList="<%=levelList%>" userAgent="<%=userAgent%>" width="510" />
</p>


<!-- buttons -->
<p>
    <netui:button  type="submit" value="Back" action="handleBackButton"/>
    <netui:button  type="submit" value="Edit" action="beginEditCustomer" disabled="${requestScope.disableEditButton}"/>
    <netui:button  type="submit" value="Cancel" action="beginFindCustomer"/>
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
