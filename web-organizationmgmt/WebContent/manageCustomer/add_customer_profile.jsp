<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui-compat"%>
<%@ taglib uri="netui-tags-databinding.tld" prefix="netui-compat-data"%>


<table class="simple">
    <tr class="transparent">
        <td valign="top">
         <table class="transparent">
            <tr class="transparent">
            <netui-compat-data:getData resultId="currentAction" value="{actionForm.currentAction}"/>
                <td class="transparent alignRight" width="130" valign="top" nowrap><span class="asterisk">*</span>&nbsp;Customer Type:</td>
              <c:if test="${currentAction == 'defaultAction'}">
                <td class="transparent"><netui:select optionsDataSource="${pageFlow.customerOptions}" dataSource="actionForm.customerProfile.customerTypeId" size="1" style="width:200px" defaultValue="${actionForm.customerProfile.customerTypeId}"/></td>        
              </c:if>
              <c:if test="${currentAction == 'editCustomer'}"> 
                <td class="transparent"><netui-compat:label value="{actionForm.customerProfile.customerType}"/></td>
              </c:if> 
            </tr>
            <tr class="transparent">
                <td class="transparent alignRight" width="130" valign="top" nowrap><span class="asterisk">*</span>&nbsp;Customer Name:</td>
                <td class="transparent"><netui:textBox tagId="CustomerName" dataSource="actionForm.customerProfile.name" style="width:200px" tabindex="0" maxlength="32"/></td>        
            </tr>
            <tr class="transparent">
                <td class="transparent alignRight" width="130" valign="top" nowrap>Customer ID:</td>
                <td class="transparent"><netui:textBox dataSource="actionForm.customerProfile.code" style="width:200px" tabindex="0" maxlength="32"/></td>        
            </tr>
            <tr class="transparent">
                <td class="transparent alignRight" width="130" valign="top" nowrap><span class="asterisk">*</span>&nbsp;State:</td>
                <td class="transparent"><netui:select optionsDataSource="${pageFlow.stateOptions}" dataSource="actionForm.customerProfile.stateId" size="1" style="width:200px" defaultValue="${actionForm.customerProfile.stateId}"/></td>        
            </tr>
        </table>

        </td>
        
        <td valign="top">

        <table class="transparent">
            <tr class="transparent">
                <td class="transparent alignRight" width="160" valign="top" nowrap><span class="asterisk">*</span>&nbsp;CTB Contact:</td>
                <td class="transparent"><netui:textBox dataSource="actionForm.customerProfile.ctbContact" style="width:200px" tabindex="0" maxlength="64"/></td>        
            </tr>
            <tr class="transparent">
                <td class="transparent alignRight" width="160" valign="top" nowrap><span class="asterisk">*</span>&nbsp;CTB Contact Email:</td>
                <td class="transparent"><netui:textBox dataSource="actionForm.customerProfile.ctbContactEmail" style="width:200px" tabindex="0" maxlength="64"/></td>        
            </tr>
            <tr class="transparent">
                <td class="transparent alignRight" width="160" valign="top" nowrap><span class="asterisk">*</span>&nbsp;Customer Contact:</td>
                <td class="transparent"><netui:textBox dataSource="actionForm.customerProfile.customerContact" style="width:200px" tabindex="0" maxlength="64"/></td>        
            </tr>
            <tr class="transparent">
                <td class="transparent alignRight" width="160" valign="top" nowrap><span class="asterisk">*</span>&nbsp;Customer Contact Email:</td>
                <td class="transparent"><netui:textBox dataSource="actionForm.customerProfile.customerContactEmail" style="width:200px" tabindex="0" maxlength="64"/></td>        
            </tr>
            <tr class="transparent">
                <td class="transparent alignRight" width="160" valign="top" nowrap><span class="asterisk">*</span>&nbsp;Customer Contact Phone:</td>
                <td class="transparent">
                    <netui:textBox tagId="primaryPhone1" dataSource="actionForm.customerProfile.conatctPhone1" maxlength="3" style="width:30px" onKeyPress="return constrainNumericChar(event);" onKeyUp="focusNextControl(this);"/>
                    -
                    <netui:textBox tagId="primaryPhone2" dataSource="actionForm.customerProfile.conatctPhone2" maxlength="3" style="width:30px" onKeyPress="return constrainNumericChar(event);" onKeyUp="focusNextControl(this);"/>
                    -
                    <netui:textBox tagId="primaryPhone3" dataSource="actionForm.customerProfile.conatctPhone3" maxlength="4" style="width:40px" onKeyPress="return constrainNumericChar(event);" onKeyUp="focusNextControl(this);"/>
                    Ext:
                    <netui:textBox tagId="primaryPhone4" dataSource="actionForm.customerProfile.conatctPhone4" maxlength="4" style="width:40px" onKeyPress="return constrainNumericChar(event);"/>
                </td>
            </tr>
        </table>

        </td>
    </tr>
</table>
