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
    Enter new order information or edit existing orders. Required fields are marked by an asterisk*.<br/>
</p>



<netui:form action="cancelCurrentAction">

<netui:hidden dataSource="actionForm.actionElement"/>
<netui:hidden dataSource="actionForm.currentAction"/>


<netui:hidden dataSource="actionForm.LASLicenseNode.customerId"/>
<netui:hidden dataSource="actionForm.LASLicenseNode.customerName"/>
<netui:hidden dataSource="actionForm.LASLicenseNode.productName"/>
<netui:hidden dataSource="actionForm.LASLicenseNode.orderNumber"/>
<netui:hidden dataSource="actionForm.LASLicenseNode.licenseQuantity"/>
<netui:hidden dataSource="actionForm.LASLicenseNode.purchaseOrder"/>
<netui:hidden dataSource="actionForm.LASLicenseNode.expiryDate"/>
<netui:hidden dataSource="actionForm.LASLicenseNode.purchaseDate"/>
 

<!-- message -->
    <jsp:include page="/manageCustomer/show_message.jsp" />

<p>


<h2 style="background-color: #CCCC99; color: #336699; padding: 5px;" >Customer Information</h2>

<table class="simple">

<tr class="transparent">

<td class="transparent">
<table class="transparent">
    <tr class="transparent">
        <td class="transparent">Customer Name:</td>
        <td class="transparent"><netui:label value="${actionForm.LASLicenseNode.customerName}"/></td>
    </tr>
    <tr class="transparent">
        <td class="transparent">Product Name:</td>
        <td class="transparent" nowrap><netui:label value="${actionForm.LASLicenseNode.productName}"/></td>
    </tr>
</table>
</td>
</tr>
</table>


<br/>
<h2 style="background-color: #CCCC99; color: #336699; padding: 5px;" >Place New Order</h2>

<table class="simple">

<tr class="transparent">

<td class="transparent">
<table class="transparent">
    <tr class="transparent">
        <td class="transparent">Order Number:</td>
        <td class="transparent">
            <netui:textBox tagId="orderNumber" dataSource="actionForm.LASLicenseNode.orderNumber" maxlength="9" style="width:100px" />
		</td>
    </tr>
    <tr class="transparent">
        <td class="transparent"><span class="asterisk">*</span>&nbsp;License Quantity:</td>
        <td class="transparent" nowrap>
            <netui:textBox tagId="licenseQuantity" dataSource="actionForm.LASLicenseNode.licenseQuantity" maxlength="9" style="width:100px" onKeyPress="return constrainNumericChar(event);"/>
        </td>
    </tr>
    <tr class="transparent">
        <td class="transparent"><span class="asterisk">*</span>&nbsp;Purchase Date:</td>
        <td class="transparent">
            <netui:textBox tagId="purchaseDate" dataSource="actionForm.LASLicenseNode.purchaseDate" maxlength="8" styleClass="textFieldDate" onKeyPress="return constrainEnterKeyEvent(event);"/>
            <a href="#" onclick="showCalendar(document.getElementById('purchaseDate'), document.getElementById('overrideStartDate')); return false;"><img src="<%=request.getContextPath()%>/resources/images/calendar/show_calendar.gif" border="0" width="24" height="22" ></a>
        </td>
    </tr>
    <tr class="transparent">
        <td class="transparent"><span class="asterisk">*</span>&nbsp;Expiry Date:</td>
        <td class="transparent">
            <netui:textBox tagId="expiryDate" dataSource="actionForm.LASLicenseNode.expiryDate" maxlength="8" styleClass="textFieldDate" onKeyPress="return constrainEnterKeyEvent(event);"/>
            <a href="#" onclick="showCalendar(document.getElementById('expiryDate'), document.getElementById('overrideEndDate')); return false;"><img src="<%=request.getContextPath()%>/resources/images/calendar/show_calendar.gif" border="0" width="24" height="22" ></a>
        </td>
    </tr>
        <td class="transparent"><span class="asterisk">*</span>&nbsp;Purchase Order:</td>
        <td class="transparent">
            <netui:textBox tagId="purchaseOrder" dataSource="actionForm.LASLicenseNode.purchaseOrder" maxlength="30" style="width:300px" />
        </td>
    <tr class="transparent">
        <td class="transparent" colspan="2">
    		<netui:button type="submit" value="Submit" action="addLASCustomerLicense"/>
        </td>
        
    </tr>
</table>
</td>
</tr>
</table>


<br/>
<h2 style="background-color: #CCCC99; color: #336699; padding: 5px;" >Edit Existing Orders</h2>

<table class="simple"><tr class="transparent"><td class="transparent">
    
<table class="sortable">
<netui-data:repeater dataSource="requestScope.licenses">
    <netui-data:repeaterHeader>
    
    <tr class="sortable">
        <th class="sortable alignLeft" width="15%" align="left"><div class="notCurrentSort">Order Number</div></th>
        <th class="sortable alignLeft" width="15%" align="left"><div class="notCurrentSort">Available</div></th>
        <th class="sortable alignLeft" width="15%" align="left"><div class="notCurrentSort">Date Purchase</div></th>
        <th class="sortable alignLeft" width="20%" align="left"><div class="notCurrentSort">Expired Date</div></th>
        <th class="sortable alignLeft" width="35%" align="left"><div class="notCurrentSort">PO</div></th>
    </tr>
    
    </netui-data:repeaterHeader>
    
    <netui-data:repeaterItem>
    
<netui-data:getData resultId="expiryStatus" value="${container.item.expiryStatus}" />
<% String expiryStatus = (String)pageContext.getAttribute("expiryStatus");  
System.out.println("expiryStatus = " + expiryStatus);

   if (expiryStatus.equals("EXPIRED")) {	 
%>
    <tr class="sortableRed">
<% } else {%>
    <tr class="sortable">
<% } %>
    
        <td class="sortable alignLeft">
              <netui:span value="${container.item.orderNumber}" defaultValue="&nbsp;"/>
         </td>    
        <td class="sortable alignLeft">
            <netui:textBox tagId="orderNumber" dataSource="container.item.licenseQuantity" maxlength="9" style="width:100px" />
         </td>    
        <td class="sortable alignLeft">
              <netui:span value="${container.item.purchaseDate}" defaultValue="&nbsp;"/>
         </td>    
        <td class="sortable alignLeft">
            <netui:textBox tagId="expiryDate" dataSource="container.item.expiryDate" maxlength="8" styleClass="textFieldDate" onKeyPress="return constrainEnterKeyEvent(event);"/>
            <a href="#" onclick="showCalendar(document.getElementById('expiryDate'), document.getElementById('overrideEndDate')); return false;"><img src="<%=request.getContextPath()%>/resources/images/calendar/show_calendar.gif" border="0" width="24" height="22" ></a>
         </td>    
        <td class="sortable alignLeft">
              <netui:span value="${container.item.purchaseOrder}" defaultValue="&nbsp;"/>
         </td>    
    </tr>    
    </netui-data:repeaterItem>
    
</netui-data:repeater>
    
</table>
<br/>
    		<netui:button type="submit" value="Save" action="editLASCustomerLicense"/>

</td></tr></table>


</p>
<p>
    <netui:button type="submit" value="Done" action="cancelCurrentAction"/>
</p>






</netui:form>
 
</netui-template:section>
</netui-template:template>