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


<netui-template:template templatePage="/resources/jsp/template.jsp">
<netui-template:setAttribute name="title" value="${bundle.web['viewcustomer.window.title']}"/>
<netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.viewCustomer']}"/>
<netui-template:section name="bodySection">

 

<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->
<h1>
    <netui:content value="${pageFlow.pageTitle}"/>
</h1>      


<!-- title message -->
<p>
    <netui:content value="Review the customer information below."/> 
</p>

<!-- start form -->
<netui:form action="viewCustomer">

<netui:hidden dataSource="actionForm.actionElement"/> 
<netui:hidden dataSource="actionForm.currentAction"/>
<netui:hidden dataSource="actionForm.selectedCustomerId"/>
<netui:hidden dataSource="actionForm.customerPageRequested"/>
<netui:hidden dataSource="actionForm.customerSortColumn"/>
<netui:hidden dataSource="actionForm.customerSortOrderBy"/>
<netui:hidden dataSource="actionForm.customerMaxPage"/>
<netui:hidden dataSource="actionForm.enableLicense"/> 


<!-- message -->
<jsp:include page="/manageCustomer/show_message.jsp" />




<!-- buttons -->
<p>
    <netui:button type="submit" value="Back" action="handleBackButton"/>
    <netui:button type="submit" value="Edit" action="beginEditCustomer" disabled="${requestScope.disableEditButton}"/>
</p>



<!-- collapsible sections -->
<a name="moduleCustomerProfile"><!-- moduleCustomerProfile --></a>    
<p>
     <ctb:showHideSection sectionId="moduleCustomerProfile" sectionTitle="Profile Information" sectionVisible="actionForm.byCustomerProfileVisible">
        <jsp:include page="/manageCustomer/view_customer_by_profile.jsp" />
    </ctb:showHideSection>
</p>


<a name="moduleCustomerBilling"><!-- moduleCustomerBilling --></a>    
<p>
    <ctb:showHideSection sectionId="moduleCustomerBilling" sectionTitle="Billing Information" sectionVisible="actionForm.byCustomerBillingVisible">
        <jsp:include page="/manageCustomer/view_customer_by_billing_contact.jsp" />
    </ctb:showHideSection>
</p>

<!-- collapsible sections -->
<a name="moduleCustomerMailing"><!-- moduleCustomerMailing --></a>    
<p>
    <ctb:showHideSection sectionId="moduleCustomerMailing" sectionTitle="Mailing Information" sectionVisible="actionForm.byCustomerMailingVisible">
        <jsp:include page="/manageCustomer/view_customer_by_mailing_contact.jsp" />
    </ctb:showHideSection>
</p>

<!-- buttons -->
<p>
    <netui:button type="submit" value="Back" action="handleBackButton"/>
    <netui:button type="submit" value="Edit" action="beginEditCustomer" disabled="${requestScope.disableEditButton}"/>
</p>



</netui:form>
        
<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
</netui-template:section>
</netui-template:template>
