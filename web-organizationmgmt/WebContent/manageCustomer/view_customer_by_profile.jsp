<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<netui-data:declareBundle bundlePath="oasResources" name="oas" />
<netui-data:declareBundle bundlePath="webResources" name="web" />
<netui-data:declareBundle bundlePath="widgetResources" name="widgets" />

<% 	
Boolean isLasLinkProduct = new Boolean((String)request.getParameter("isLasLinkProduct")); 
pageContext.setAttribute("isLasLinkProduct",isLasLinkProduct);
%>


<table class="simple">
	<tr class="transparent">

		<!-- column 1 -->
		<td class="transparent-top" width="50%">
		<table class="transparent">
			<tr class="transparent">
				<td class="transparent" width="150" nowrap><netui:content value="Customer Type:" /></td>
				<td class="transparent"><netui:label value="${customerProfileData.customerType}" /></td>
			</tr>
<!-- //For checkbox Start Sprint -46 --Laslink 66 -->
			<tr class="transparent">
				<c:if test="${!isLasLinkProduct}">
					<td class="transparent" width="150" nowrap id="LLProductTypeTD" style="display: none;">
					<span style="display: none;" id="LLProductTypeLabel">Laslink Product Type:</span></td>
				</c:if>

				<div></div>
				<c:if test="${isLasLinkProduct}">

					<td class="transparent" width="150" nowrap id="LLProductTypeTD" style="display: inline;">
					<span style="display: inline;" id="LLProductTypeLabel">Laslink Product Type:</span></td>
				</c:if>
				<td class="transparent">
				<c:if test="${!isLasLinkProduct}">
					<div id="LaslinkProductChecboxGroup" style="display: none;">
					<netui:checkBoxGroup
						dataSource="actionForm.customerProfile.userSelections" disabled="<%=isLasLinkProduct%>">
						<netui:checkBoxOption value="Form A" tagId="FormAcheckBox" />
						<netui:checkBoxOption value="Form B" tagId="FormBcheckBox" />
						<netui:checkBoxOption value="Espanol" tagId="EspanolcheckBox" />
					</netui:checkBoxGroup></div>
				</c:if> <c:if test="${isLasLinkProduct}">
					<div id="LaslinkProductChecboxGroup" style="display: block;">
					<netui:checkBoxGroup
						dataSource="actionForm.customerProfile.userSelections" disabled="<%=isLasLinkProduct%>">
						<netui:checkBoxOption value="Form A" tagId="FormAcheckBox" />
						<netui:checkBoxOption value="Form B" tagId="FormBcheckBox" />
						<netui:checkBoxOption value="Espanol" tagId="EspanolcheckBox" />
					</netui:checkBoxGroup></div>
				</c:if></td>
			</tr>

			<!-- //For checkbox End Sprint -46 --Laslink 66 -->
			<tr class="transparent">
				<td class="transparent" width="150" nowrap><netui:content value="Customer Name:" /></td>
				<td class="transparent"><netui:label value="${customerProfileData.name}" /></td>
			</tr>
			<tr class="transparent">
				<td class="transparent" width="150" nowrap><netui:content value="Customer ID:" /></td>
				<td class="transparent"><netui:label value="${customerProfileData.code}" /></td>
			</tr>
			<tr class="transparent">
				<td class="transparent" width="150" nowrap><netui:content value="State:" /></td>
				<td class="transparent"><netui:label value="${customerProfileData.state}" /></td>
			</tr>
			<c:if test="${isLasLinkProduct}">
				<tr class="transparent">
					<td class="transparent" width="150" nowrap><netui:content value="MDR Number:" /></td>
					<td class="transparent"><netui:label value="${customerProfileData.mdrNumber}" /></td>
				</tr>
			</c:if>
		</table>
		</td>




		<!-- column 2 -->
		<td class="transparent-top" width="50%">
		<table class="transparent">
			<tr class="transparent">
				<td class="transparent" width="180" nowrap><netui:content value="CTB Contact:" /></td>
				<td class="transparent"><netui:label value="${customerProfileData.ctbContact}" /></td>
			</tr>
			<tr class="transparent">
				<td class="transparent" width="180" nowrap><netui:content value="CTB Contact Email:" /></td>
				<td class="transparent"><netui:label value="${customerProfileData.ctbContactEmail}" /></td>
			</tr>
			<tr class="transparent">
				<td class="transparent" width="180" nowrap><netui:content value="Customer Contact:" /></td>
				<td class="transparent"><netui:label value="${customerProfileData.customerContact}" /></td>
			</tr>
			<tr class="transparent">
				<td class="transparent" width="180" nowrap><netui:content value="Customer Contact Email:" /></td>
				<td class="transparent"><netui:label value="${customerProfileData.customerContactEmail}" /></td>
			</tr>
			<tr class="transparent">
				<td class="transparent" width="180" nowrap><netui:content value="Customer Contact Phone:" /></td>
				<td class="transparent"><netui:label value="${customerProfileData.conatctPhone}" /></td>
			</tr>
		</table>
		</td>


	</tr>
</table>

<br />
