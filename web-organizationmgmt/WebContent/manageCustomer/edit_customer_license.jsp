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
<netui-template:setAttribute name="title" value="${bundle.web['editlicense.window.title']}"/>
<netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.editCustomer']}"/>
<netui-template:section name="bodySection">

<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->
<h1>
    <netui:content value="Manage Licenses"/>
</h1>      


<!-- title message -->
<p>
    <netui:content value="Enter changes to license quantities. Required fields are marked by an asterisk "/> <span class="asterisk">*</span>.<br/>
   <!--netui:content value="Enter information about the customer in the form below. Required fields are marked by an asterisk " /><span class="asterisk">*</span-->
</p>



<netui:form action="addEditCustomerLicense">

<netui:hidden dataSource="actionForm.customerMaxPage"/>
<netui:hidden dataSource="actionForm.customerPageRequested"/>
<netui:hidden dataSource="actionForm.customerSortColumn"/>
<netui:hidden dataSource="actionForm.customerSortOrderBy"/>
<netui:hidden dataSource="actionForm.actionElement"/>
<netui:hidden dataSource="actionForm.currentAction"/>

<netui:hidden dataSource="actionForm.customerProfile.name"/> 
<netui:hidden dataSource="actionForm.licenseNode.productName"/> 
<netui:hidden dataSource="actionForm.licenseNode.reserved"/>
<netui:hidden dataSource="actionForm.licenseNode.licenseAfterLastPurchase"/>
<netui:hidden dataSource="actionForm.licenseNode.productId"/>
<netui:hidden dataSource="actionForm.selectedCustomerId"/>
<netui:hidden dataSource="actionForm.enableLicense"/>
<!-- message -->
    <jsp:include page="/manageCustomer/show_message.jsp" />

<p>
<table class="simple">
    <tr class="transparent">

<!-- column 1 -->
<td class="transparent-top" width="310" valign="top">
<table class="transparent">
    <tr class="transparent">
        <td class="transparent alignRight" width="110"><netui:content value="Customer Name:"/></td>
        <td class="transparent"><netui:label value="${customerProfileData.name}"/></td>
    </tr>   
    <tr class="transparent">
        <td class="transparent alignRight" width="110"><netui:content value="Product Name:"/></td>
        <td class="transparent" nowrap><netui:label value="${licneseNodeData.productName}"/></td>
    </tr>    
</table>
</td>

<!-- column 2 -->
<td class="transparent-top" width="*" valign="top">
<table class="transparent">
    <tr class="transparent">
        <td class="transparent alignRight" width="150"><span class="asterisk">*</span>&nbsp;<netui:content value="Available:"/></td>
        <td class="transparent">
            <netui:textBox tagId="available" dataSource="actionForm.licenseNode.available" maxlength="9" style="width:100px" onKeyPress="return constrainNumericChar(event);"/>
        </td>
    </tr>
    <tr class="transparent">
        <td class="transparent alignRight" width="150"><netui:content value="Scheduled:"/></td>
        <td class="transparent" nowrap><netui:label value="${licneseNodeData.reserved}"/></td>
    </tr>
    <tr class="transparent">
        <td class="transparent alignRight" width="150" nowrap><netui:content value="Consumed:"/></td>
        <td class="transparent" nowrap><netui:label value="${actionForm.licenseNode.consumed}"/>
        </td>
    </tr>
</table>
</td>


</tr>
</table>
</p>
<p>
    <netui:button type="submit" value="Save" action="addEditCustomerLicense"/>
    <netui:button type="submit" value="Cancel" action="cancelCurrentAction"/>
</p>
</netui:form>

</netui-template:section>
</netui-template:template>