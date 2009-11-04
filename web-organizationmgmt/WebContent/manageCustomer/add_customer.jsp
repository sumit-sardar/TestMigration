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

<netui-template:template templatePage="/resources/jsp/template_add_edit_customer.jsp">
<netui-template:setAttribute name="title" value="${bundle.web['addcustomer.window.title']}"/>
<netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.addCustomer']}"/>
<netui-template:section name="bodySection">

<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->

<!-- start form -->
<netui:form action="addCustomer">
<input type="hidden" name="firstFocusId" id="firstFocusId" value="{actionForm.customerProfile.name}" />
<h1>
    <netui:content value="${pageFlow.pageTitle}"/>
</h1>

<!-- title -->
<p>
    <netui:content value="Enter information about the customer in the form below. Required fields are marked by an asterisk "/><span class="asterisk">*</span>.
</p>

<!-- message -->
<jsp:include page="/manageCustomer/show_message.jsp" />


<!-- buttons -->
<p>
    <netui:button type="submit" value="Next" action="beginCreateFramework"/>
    <netui:button type="submit" value="Cancel" action="beginFindCustomer"/>
</p>


<!-- collapsible sections -->
<a name="moduleCustomerProfile"><!-- moduleCustomerProfile --></a>    
<p>
    <ctb:showHideSection sectionId="moduleCustomerProfile" sectionTitle="Profile Information" sectionVisible="actionForm.byCustomerProfileVisible">
        <jsp:include page="/manageCustomer/add_customer_profile.jsp" />
    </ctb:showHideSection>
</p>


<!-- collapsible sections -->
<a name="moduleCustomerBilling"><!-- moduleCustomerBilling --></a>    
<p>
    <ctb:showHideSection sectionId="moduleCustomerBilling" sectionTitle="Billing Information" sectionVisible="actionForm.byCustomerBillingVisible">
        <jsp:include page="/manageCustomer/add_customer_billing_info.jsp" />
    </ctb:showHideSection>
</p>


<!-- collapsible sections -->
<a name="moduleCustomerMailing"><!-- moduleCustomerMailing --></a>    
<p>
    <ctb:showHideSection sectionId="moduleCustomerMailing" sectionTitle="Mailing Information" sectionVisible="actionForm.byCustomerMailingVisible">
        <jsp:include page="/manageCustomer/add_customer_mailing_info.jsp" />
    </ctb:showHideSection>
</p>

    
<!-- buttons -->
<p>
    <netui:button type="submit" value="Next" action="beginCreateFramework"/>
    <netui:button type="submit" value="Cancel" action="beginFindCustomer"/>
</p>

</netui:form>
        
<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
</netui-template:section>
</netui-template:template>
