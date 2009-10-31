<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<table class="simple">
    <tr class="transparent">
        <td valign="top">

<table class="transparent">
    <tr class="transparent">
        <td class="transparent alignRight" width="130" nowrap><netui:content value="Address Line 1:"/></td>
        <td class="transparent"><netui:textBox tagId="addressLine1" dataSource="actionForm.customerProfile.billingContact.addressLine1" maxlength="64" style="width:200px"/></td>
    </tr>
    <tr class="transparent">
        <td class="transparent alignRight" width="130" nowrap><netui:content value="Address Line 2:"/></td>
        <td class="transparent"><netui:textBox tagId="addressLine2" dataSource="actionForm.customerProfile.billingContact.addressLine2" maxlength="64" style="width:200px"/></td>
    </tr>
    <tr class="transparent">
        <td class="transparent alignRight" width="130" nowrap><netui:content value="Address Line 3:"/></td>
        <td class="transparent"><netui:textBox tagId="addressLine3" dataSource="actionForm.customerProfile.billingContact.addressLine3" maxlength="64" style="width:200px"/></td>
    </tr>
</table>

        </td>
        <td valign="top">

<table class="transparent">
    <tr class="transparent">
        <td class="transparent alignRight" width="160" nowrap><netui:content value="City:"/></td>
        <td class="transparent"><netui:textBox tagId="city" dataSource="actionForm.customerProfile.billingContact.city" maxlength="64" style="width:200px"/></td>
    </tr>
    <tr class="transparent">
        <td class="transparent alignRight" width="160" nowrap><netui:content value="State:"/></td>
        <td class="transparent">
            <netui:select optionsDataSource="${pageFlow.billingStateOptions}" dataSource="actionForm.customerProfile.billingContact.state" size="1" style="width:200px" defaultValue="${actionForm.customerProfile.billingContact.state}"/>
        </td>                                
    </tr>
    <tr class="transparent">
        <td class="transparent alignRight" width="160" nowrap><netui:content value="Zip:"/></td>
        <td class="transparent">
            <netui:textBox tagId="zipCode1" dataSource="actionForm.customerProfile.billingContact.zipCode1" maxlength="5" style="width:50px" onKeyPress="return constrainNumericChar(event);" onKeyUp="focusNextControl(this); "/>
            -
            <netui:textBox tagId="zipCode2" dataSource="actionForm.customerProfile.billingContact.zipCode2" maxlength="5" style="width:50px" onKeyPress="return constrainNumericChar(event);"/>
        </td>
    </tr>
</table>

        </td>
    </tr>
</table>
