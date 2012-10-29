<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<% String currentAction = (String)request.getParameter("currentAction"); 
	Boolean isLasLinkProduct = new Boolean((String)request.getParameter("isLasLinkProduct")); 
	pageContext.setAttribute("isLasLinkProduct",isLasLinkProduct);
	Boolean isCheckBoxDisabled = ("editCustomer".equals(currentAction)) ? true:false;
%>

<table class="simple">
    <tr class="transparent">
        <td valign="top">
         <table class="transparent">
            <tr class="transparent">
            <netui-data:getData resultId="currentAction" value= "<%= currentAction %>" />
            
                <td class="transparent alignRight" width="130" valign="top" nowrap><span class="asterisk">*</span>&nbsp;Customer Type:</td>
              <c:if test="${currentAction == 'defaultAction'}">
               <td class="transparent">	
               <netui:select tagId="selectedCustomerTypeId" optionsDataSource="${pageFlow.customerOptions}" dataSource="actionForm.customerProfile.customerTypeId" size="1" style="width:200px" defaultValue="${actionForm.customerProfile.customerTypeId}" onChange="toogleMDRNoTextBox('selectedCustomerTypeId');"/>
               </td>
              </c:if>
              <c:if test="${currentAction == 'editCustomer'}"> 
                <td class="transparent"><netui:label value="${customerProfileData.customerType}"/></td>
              </c:if> 
            </tr>
            <!-- //For checkbox Start Sprint -46 --Laslink 66 -->
			<tr class="transparent">
				<c:if test="${!isLasLinkProduct}">
					<td class="transparent alignRight" width="130" valign="top" nowrap id="LLProductTypeTD" style="display: none;"><span class="asterisk"
						style="display: none;"  id="LLProductTypeAsterisk">*</span>&nbsp;<span style="display: none;"
						id="LLProductTypeLabel">Laslink Product Type:</span></td>
				</c:if>
				
				</div>
				<c:if test="${isLasLinkProduct}">
					 
					<td class="transparent alignRight" width="130" valign="top" nowrap id="LLProductTypeTD" style="display: inline;"><span class="asterisk"
						style="display: inline;" id="LLProductTypeAsterisk">*</span>&nbsp;<span style="display: inline;"
						id="LLProductTypeLabel">Laslink Product Type:</span></td>
				</c:if>			
					<td class="transparent">
					<c:if test="${!isLasLinkProduct}">
						<div id="LaslinkProductChecboxGroup" style="display: none;" >
						<netui:checkBoxGroup dataSource="actionForm.customerProfile.userSelections" disabled="<%=isCheckBoxDisabled %>">
							<netui:checkBoxOption value="Form A" tagId="FormAcheckBox" />
							<netui:checkBoxOption value="Form B" tagId="FormBcheckBox" />
							<netui:checkBoxOption value="Espanol" tagId="EspanolcheckBox" />
						</netui:checkBoxGroup></div>
					</c:if>
					<c:if test="${isLasLinkProduct}">
						<div id="LaslinkProductChecboxGroup" style="display: block;" >
						<netui:checkBoxGroup dataSource="actionForm.customerProfile.userSelections"  disabled="<%=isCheckBoxDisabled %>">
							<netui:checkBoxOption value="Form A" tagId="FormAcheckBox" />
							<netui:checkBoxOption value="Form B" tagId="FormBcheckBox" />
							<netui:checkBoxOption value="Espanol" tagId="EspanolcheckBox" />
						</netui:checkBoxGroup></div>
					</c:if>
					</td>							
			</tr>

			<!-- //For checkbox End Sprint -46 --Laslink 66 -->
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
            <tr class="transparent">
            	
            	<c:if test="${!isLasLinkProduct}">
                <td class="transparent alignRight" width="130" valign="top" nowrap><span class="asterisk" style="display: none;" id="MdrAsterisk">*</span>&nbsp;<span style="display: none;" id="MdrLabel">MDR Number:</span></td>
                </c:if>
                <c:if test="${isLasLinkProduct}">
                <td class="transparent alignRight" width="130" valign="top" nowrap><span class="asterisk" style="display: inline;" id="MdrAsterisk">*</span>&nbsp;<span style="display: inline;" id="MdrLabel">MDR Number:</span></td>
                </c:if>
                <td class="transparent">
	             <c:if test="${!isLasLinkProduct}">
                 <netui:textBox tagId="MDRNoTextBox" dataSource="actionForm.customerProfile.mdrNumber" maxlength="8"  style="width:200px; display: none;"  onKeyPress="return constrainNumericChar(event);"  onBlur="IsNumeric();" onKeyUp="IsNumeric();"/>
                 </c:if>
                 <c:if test="${isLasLinkProduct}">
                 <netui:textBox tagId="MDRNoTextBox" dataSource="actionForm.customerProfile.mdrNumber" maxlength="8"  style="width:200px; display: block;"  onKeyPress="return constrainNumericChar(event);"  onBlur="IsNumeric();" onKeyUp="IsNumeric();"/>
        		 </c:if>
                 
                 
                </td>        
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
