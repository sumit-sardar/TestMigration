<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>

<%
	Boolean isMandatoryBirthDate = (Boolean)request.getAttribute("isMandatoryBirthDate"); //GACRCT2010CR007 - Disable Mandatory Birth Date

%>


<input type="hidden" name="firstFocusId" id="firstFocusId" value="{actionForm.studentProfile.firstName}" />


<table class="simpleForm">
    <tr class="simpleForm" valign="top">
        <td class="simpleFormNarrow" width="280">
          
        
<!-- Student Information -->
<table class="transparent">
    <tr class="transparent">
        <td class="transparent alignRight" width="100"><span class="asterisk">*</span>&nbsp;<netui:content value="First Name:"/></td>
        <td class="transparent"><netui:textBox dataSource="actionForm.studentProfile.firstName" maxlength="32" style="width:180px" tabindex="1"/></td>
    </tr>
    <tr class="transparent">
        <td class="transparent alignRight" width="100"><netui:content value="Middle Name:"/></td>
        <td class="transparent"><netui:textBox dataSource="actionForm.studentProfile.middleName" maxlength="32" style="width:180px" tabindex="2"/></td>
    </tr>
    <tr class="transparent">
        <td class="transparent alignRight" width="100"><span class="asterisk">*</span>&nbsp;<netui:content value="Last Name:"/></td>
        <td class="transparent"><netui:textBox dataSource="actionForm.studentProfile.lastName" maxlength="32" style="width:180px" tabindex="3"/></td>
    </tr>
    <tr class="transparent">
        <td class="transparent alignRight" width="120">
        <c:if test="${!isMandatoryBirthDate }">   
        	<span class="asterisk">*</span>&nbsp;
        </c:if>
        <netui:content value="Date of Birth:"/>
        </td>
        <td class="transparent" nowrap>    
            <netui:select optionsDataSource="${pageFlow.monthOptions}" dataSource="actionForm.studentProfile.month" size="1" style="width:60px" tabindex="4"/>
            <netui:select optionsDataSource="${pageFlow.dayOptions}" dataSource="actionForm.studentProfile.day" size="1" style="width:45px" tabindex="5"/>
            <netui:select optionsDataSource="${pageFlow.yearOptions}" dataSource="actionForm.studentProfile.year" size="1" style="width:68px" tabindex="6"/>
        </td>                    
    </tr>    
    <tr class="transparent">
        <td class="transparent alignRight" width="100"><span class="asterisk">*</span>&nbsp;<netui:content value="Grade:"/></td>
        <td class="transparent">
            <netui:select optionsDataSource="${pageFlow.gradeOptions}" dataSource="actionForm.studentProfile.grade" size="1" style="width:180px" tabindex="7"/>
        </td>                                
    </tr>
    <tr class="transparent">
        <td class="transparent alignRight" width="120"><span class="asterisk">*</span>&nbsp;<netui:content value="Gender:"/></td>
        <td class="transparent">
            <netui:select optionsDataSource="${pageFlow.genderOptions}" dataSource="actionForm.studentProfile.gender" size="1" style="width:180px" tabindex="8"/>
        </td>                                
    </tr>    
    <tr class="transparent">
        <td class="transparent alignRight" width="100"><netui:content value="Student ID:"/></td>
        <td class="transparent"><netui:textBox dataSource="actionForm.studentProfile.studentNumber" maxlength="32" style="width:180px" tabindex="9"/></td>
    </tr>
   
    	<tr class="transparent">
        <td class="transparent alignRight" width="110"><netui:content value="Instructor Name:"/></td>
        <td class="transparent"><input type="text" maxlength="64" style="width:180px"/></td>
    </tr>
    <tr class="transparent">
        <td class="transparent alignRight" width="110"><netui:content value="Street Address:"/></td>
        <td class="transparent"><input type="text" maxlength="64" style="width:180px"/></td>
    </tr>
    <tr class="transparent">
        <td class="transparent alignRight" width="110"><netui:content value="City:"/></td>
        <td class="transparent"><input type="text" maxlength="64" style="width:180px"/></td>
    </tr>
    <tr class="transparent">
        <td class="transparent alignRight" width="110"><netui:content value="State:"/></td>
      	<td class="transparent"><input type="text" maxlength="64" style="width:180px"/></td>                               
    </tr>
    <tr class="transparent">
        <td class="transparent alignRight" width="110"><netui:content value="Zip:"/></td>
        <td class="transparent">
            <input type="text"  maxlength="5" style="width:50px" />
            -
            <input type="text"  maxlength="5" style="width:50px" />
        </td>
    </tr>
     <tr class="transparent">
        <td class="transparent alignRight" width="210"><netui:content value="Phone Number:"/></td>
        <td class="transparent" width="400">
            <input type="text" maxlength="3" style="width:35px" />
            -
            <input type="text"  maxlength="3" style="width:35px"/>
            -
            <input type="text"  maxlength="4" style="width:35px"/>
            Ext:
            <input type="text"  maxlength="4" style="width:35px" />
        </td>
    </tr>
    <tr class="transparent">
        <td class="transparent alignRight" width="210"><netui:content value="Social Security Number/Student ID :"/></td>
         <td class="transparent"><input type="text"  maxlength="64" style="width:180px"/></td>
    </tr>
    <tr class="transparent">
        <td class="transparent alignRight" width="210"><netui:content value="Is above id a Social Security Number:"/></td>
        <td class="transparent-small" width="200">
	        <input type="radio" value="Yes" checked="true">&nbsp;&nbsp;&nbsp;Yes</input>
	        <input type="radio" value="No"/>&nbsp;&nbsp;&nbsp;No</input>
        </td>
     </tr>
     <tr class="transparent">
        <td class="transparent alignRight" width="210"><netui:content value="Is PBA Consent form signed:"/></td>
        <td class="transparent-small" width="200">
	        <input type="radio" value="Yes" checked="true">&nbsp;&nbsp;&nbsp;Yes</input>
	        <input type="radio" value="No"/>&nbsp;&nbsp;&nbsp;No</input>
        </td>
    </tr>
    
  
</table>
</td>

<td class="simpleFormNoBorder" width="*">

<!-- OrgNode PathList -->
<table class="sortable">

    <tr class="sortable">
        <td class="sortableControls" colspan="2">
            <ctb:tablePathList valueDataSource="actionForm.orgNodeId" labelDataSource="actionForm.orgNodeName" pathList="request.orgNodePath" />
        </td>
    </tr>

<netui-data:repeater dataSource="requestScope.orgNodes">
    <netui-data:repeaterHeader>
    
    <tr class="sortable">
        <ctb:tableSortColumnGroup columnDataSource="actionForm.orgSortColumn" orderByDataSource="actionForm.orgSortOrderBy" >
            <th class="sortable alignCenter" nowrap width="5%">&nbsp;<netui:content value="${bundle.web['common.column.select']}"/>&nbsp;</th>                
            <th class="sortable alignLeft" nowrap width="95%"><ctb:tableSortColumn value="OrgNodeName"><netui:content value="${requestScope.orgCategoryName}"/></ctb:tableSortColumn></th>
        </ctb:tableSortColumnGroup>
    </tr>
    
    </netui-data:repeaterHeader>
    <netui-data:repeaterItem>
    
    <tr class="sortable">
        <td class="sortable alignCenter" width="5%">
            <netui-data:getData resultId="isSelectable" value="${container.item.selectable}"/>
        
            <c:if test="${isSelectable == 'false'}">                    
                <netui:radioButtonGroup dataSource="actionForm.selectedOrgNodeId" disabled="true">
                    &nbsp;<netui:radioButtonOption value="${container.item.id}">&nbsp;</netui:radioButtonOption>                
                </netui:radioButtonGroup>
            </c:if>
            <c:if test="${isSelectable == 'true'}">                    
                <netui:radioButtonGroup dataSource="actionForm.selectedOrgNodeId">
                    &nbsp;<netui:radioButtonOption value="${container.item.id}">&nbsp;</netui:radioButtonOption>                
                </netui:radioButtonGroup>
            </c:if>
        </td>        
        <td class="sortable alignLeft" width="95%">     
            <ctb:tablePathEntry srcLabelDataSource="${container.item.name}" srcValueDataSource="${container.item.id}" dstLabelDataSource="actionForm.orgNodeName" dstValueDataSource="actionForm.orgNodeId" shownAsLink="${container.item.hasChildren}"/>
        </td>
    </tr>
    
    </netui-data:repeaterItem>
    <netui-data:repeaterFooter>
    
        <tr class="sortable">
            <td class="sortableControls" colspan="2">
                <ctb:tablePager dataSource="actionForm.orgPageRequested" summary="request.orgPagerSummary" objectLabel="${bundle.oas['object.organizations']}" id="tablePathListAnchor" anchorName="tablePathListAnchor"/>
            </td>
        </tr>         
            
    </netui-data:repeaterFooter>
</netui-data:repeater>

</table>


</td>

</tr>

</table>

<br/>







