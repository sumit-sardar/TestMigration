<%@ page import="java.io.*,java.util.*"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="dto.PathNode"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>

<%
    Boolean profileEditable = (Boolean)request.getAttribute("profileEditable"); 
	Boolean isMandatoryBirthDate = (Boolean)request.getAttribute("isMandatoryBirthDate"); //GACRCT2010CR007 - Disable Mandatory Birth Date 
	
	//Start Change For CR - GA2011CR001
	Boolean isStudentIdConfigurable = (Boolean)request.getAttribute("isStudentIdConfigurable"); 
	Boolean isStudentId2Configurable = (Boolean)request.getAttribute("isStudentId2Configurable"); 
	String []studentIdArrValue = (String[])request.getAttribute("studentIdArrValue");
	String []studentId2ArrValue = (String[])request.getAttribute("studentId2ArrValue");
	boolean isMandatoryStudentId = false;
	if(studentIdArrValue != null && studentIdArrValue[2] != null && studentIdArrValue[2]!= "") {
		if (studentIdArrValue[2].equalsIgnoreCase("T")) {
			isMandatoryStudentId = true;
		}
	}
	
	pageContext.setAttribute("gtidMandatory",new Boolean(isMandatoryStudentId));

	// End of Change CR - GA2011CR001 

%>


<table class="simple">
    <tr class="transparent">
        
        
<!-- Student Information -->
<td class="simple" width="450">


		<table class="transparent">
			<tr class="transparent">
				<td class="transparent alignRight" width="250"><span class="asterisk">*</span>&nbsp;<netui:content
					value="First Name:" /></td>
				<td class="transparent"><c:if test="${ profileEditable }">
					<!-- Added tagId to resolve javascript isssue occured in mozilla  for webLogic 10.3-->
					<netui:textBox tagId="firstName" dataSource="actionForm.studentProfile.firstName" maxlength="32"
						style="width:180px" tabindex="0" />
				</c:if> <c:if test="${ !profileEditable }">
					<netui:textBox tagId="firstName" dataSource="actionForm.studentProfile.firstName" maxlength="32"
						style="width:180px" disabled="true" />
				</c:if></td>
			</tr>
			<tr class="transparent">
				<td class="transparent alignRight" width="250"><netui:content value="Middle Name:" /></td>
				<td class="transparent"><c:if test="${ profileEditable }">
					<netui:textBox dataSource="actionForm.studentProfile.middleName" maxlength="32" style="width:180px" />
				</c:if> <c:if test="${ !profileEditable }">
					<netui:textBox dataSource="actionForm.studentProfile.middleName" maxlength="32" style="width:180px" disabled="true" />
				</c:if></td>
			</tr>
			<tr class="transparent">
				<td class="transparent alignRight" width="250"><span class="asterisk">*</span>&nbsp;<netui:content
					value="Last Name:" /></td>
				<td class="transparent"><c:if test="${ profileEditable }">
					<!-- Added tagId to resolve javascript isssue occured in mozilla  for webLogic 10.3-->
					<netui:textBox tagId="lastName" dataSource="actionForm.studentProfile.lastName" maxlength="32" style="width:180px" />
				</c:if> <c:if test="${ !profileEditable }">
					<netui:textBox tagId="lastName" dataSource="actionForm.studentProfile.lastName" maxlength="32" style="width:180px"
						disabled="true" />
				</c:if></td>
			</tr>
			<tr class="transparent">
				<td class="transparent alignRight" width="250"><c:if test="${!isMandatoryBirthDate }">
					<span class="asterisk">*</span>&nbsp;
        </c:if> <netui:content value="Date of Birth:" /></td>
				<td class="transparent" nowrap><c:if test="${ profileEditable }">
					<netui:select optionsDataSource="${pageFlow.monthOptions}" dataSource="actionForm.studentProfile.month" size="1"
						style="width:60px" />
					<netui:select optionsDataSource="${pageFlow.dayOptions}" dataSource="actionForm.studentProfile.day" size="1"
						style="width:45px" />
					<netui:select optionsDataSource="${pageFlow.yearOptions}" dataSource="actionForm.studentProfile.year" size="1"
						style="width:68px" />
				</c:if> <c:if test="${ !profileEditable }">
					<netui:select optionsDataSource="${pageFlow.monthOptions}" dataSource="actionForm.studentProfile.month" size="1"
						style="width:60px" disabled="true" />
					<netui:select optionsDataSource="${pageFlow.dayOptions}" dataSource="actionForm.studentProfile.day" size="1"
						style="width:45px" disabled="true" />
					<netui:select optionsDataSource="${pageFlow.yearOptions}" dataSource="actionForm.studentProfile.year" size="1"
						style="width:68px" disabled="true" />
				</c:if></td>
			</tr>
			<tr class="transparent">
				<td class="transparent alignRight" width="250"><span class="asterisk">*</span>&nbsp;<netui:content
					value="Grade:" /></td>
				<td class="transparent"><c:if test="${ profileEditable }">
					<netui:select optionsDataSource="${pageFlow.gradeOptions}" dataSource="actionForm.studentProfile.grade" size="1"
						style="width:180px" />
				</c:if> <c:if test="${ !profileEditable }">
					<netui:select optionsDataSource="${pageFlow.gradeOptions}" dataSource="actionForm.studentProfile.grade" size="1"
						style="width:180px" disabled="true" />
				</c:if></td>
			</tr>
			<tr class="transparent">
				<td class="transparent alignRight" width="250"><span class="asterisk">*</span>&nbsp;<netui:content
					value="Gender:" /></td>
				<td class="transparent"><c:if test="${ profileEditable }">
					<netui:select optionsDataSource="${pageFlow.genderOptions}" dataSource="actionForm.studentProfile.gender" size="1"
						style="width:180px" />
				</c:if> <c:if test="${ !profileEditable }">
					<netui:select optionsDataSource="${pageFlow.genderOptions}" dataSource="actionForm.studentProfile.gender" size="1"
						style="width:180px" disabled="true" />
				</c:if></td>
			</tr>
			<tr class="transparent">
				<td class="transparent alignRight" width="250"><span class="asterisk">*</span>&nbsp;<netui:content
					value="Instructor First Name:" /></td>
				<td class="transparent"><netui:textBox tagId="instructorFirstName"
					dataSource="actionForm.studentProfile.instructorFirstName" maxlength="32" style="width:180px" /></td>
			</tr>
			<tr class="transparent">
				<td class="transparent alignRight" width="250"><span class="asterisk">*</span>&nbsp;<netui:content
					value="Instructor Last Name:" /></td>
				<td class="transparent"><netui:textBox tagId="instructorLastName"
					dataSource="actionForm.studentProfile.instructorLastName" maxlength="32" style="width:180px" /></td>
			</tr>
			<tr class="transparent">
				<td class="transparent alignRight" width="250"><span class="asterisk">*</span>&nbsp;<netui:content
					value="Social Security Number/Student ID:" /></td>
				<td class="transparent"><netui:textBox dataSource="actionForm.studentProfile.studentNumber" maxlength="32"
					style="width:180px" /></td>
			</tr>
			<tr class="transparent">
				<td class="transparent alignRight" width="250"><span class="asterisk">*</span>&nbsp;<netui:content
					value="Is the above id a Social Security Number:" /></td>
				<td class="transparent" width="180"><netui:radioButtonGroup dataSource="actionForm.studentProfile.isSSN">
					<netui:radioButtonOption value="Yes">Yes</netui:radioButtonOption>
					<netui:radioButtonOption value="No">No</netui:radioButtonOption>
				</netui:radioButtonGroup></td>
			</tr>
			<tr class="transparent">
				<td class="transparent alignRight" width="250"><span class="asterisk">*</span>&nbsp;<netui:content
					value="Is PBA Consent form signed:" /></td>
					<td class="transparent" width="180"><netui:radioButtonGroup dataSource="actionForm.studentProfile.isPBAFormSigned">
					<netui:radioButtonOption value="Yes">Yes</netui:radioButtonOption>
					
					<netui:radioButtonOption value="No">No</netui:radioButtonOption>
				
				</netui:radioButtonGroup></td>
			</tr>



			<tr class="transparent">
        <td class="transparent-top alignRight" width="250"><span class="asterisk">*</span>&nbsp;<netui:content value="Organization:"/></td>
        <td class="transparent-top">
            <table id="orgTable" class="transparent">
            
<%            
            List selectedOrgNodes = (List)request.getAttribute("selectedOrgNodes"); 
            List orgNodesForSelector = (List)request.getAttribute("orgNodesForSelector"); 
            
            String showMessage = "display: block";
            String userAgent = request.getHeader("User-Agent").toLowerCase();
            if (userAgent.indexOf("firefox") != -1) {
                showMessage = "display: table-row";
            }
            if (userAgent.indexOf("mac") != -1) {
                showMessage = "display: table-row";
            }
            
            if (selectedOrgNodes.size() > 0) 
                showMessage = "display: none";            
%>
                <tr id="message" class="transparent" style="<%= showMessage %>">
                    <td class="transparent-small">
                        <font color="gray">None selected. Use the control on the right to select.</font>                                   
                    </td>
                </tr>
<%                
            for (int i=0 ; i<orgNodesForSelector.size() ; i++) {
                PathNode node = (PathNode)orgNodesForSelector.get(i);
                String orgId = node.getId().toString();
                String orgName = node.getName();                
                String fullpath = node.getFullPathName();
                String selectable = node.getSelectable();                
                String showRow = "display: block";
                
                if (userAgent.indexOf("firefox") != -1) {
                    showRow = "display: table-row";
                }
                if (userAgent.indexOf("mac") != -1) {
                    showRow = "display: table-row";
                }
                if (selectable.equalsIgnoreCase("true")) {
                    showRow = "display: none";
                }
                
%>            
                <tr id="<%= orgId %>" class="transparent" style="<%= showRow %>">
                    <td class="transparent-small">
                        <a href="#" style="text-decoration: none" title="<%= fullpath %>" onclick="return setupOrgNodePath('<%= orgId %>');"><%= orgName %></a>
                    </td>
                </tr>
<%
            }
%>                
            </table>
            
        </td>
    </tr>

			<tr class="transparent">
				<td class="transparent alignRight" width="250"><span class="asterisk">*</span>&nbsp;<netui:content
					value="Make student visible across organizations:" /></td>
				<td class="transparent" width="180"><netui:radioButtonGroup dataSource="actionForm.studentProfile.visibleAcrossOrganization" defaultValue="No">
					<netui:radioButtonOption value="Yes">Yes</netui:radioButtonOption>
					<netui:radioButtonOption value="No">No</netui:radioButtonOption>
					
				</netui:radioButtonGroup></td>

		</table>
		</td>


    
<!-- OrgNode PathList -->
<td class="transparent-control" width="550">
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
            <th class="sortable alignCenter" width="5%">&nbsp;<netui:content value="${bundle.web['common.column.select']}"/>&nbsp;</th>                
            <th class="sortable alignLeft" width="*"><ctb:tableSortColumn value="OrgNodeName"><netui:content value="${requestScope.orgCategoryName}"/></ctb:tableSortColumn></th>
        </ctb:tableSortColumnGroup>
    </tr>
    
    </netui-data:repeaterHeader>
    <netui-data:repeaterItem>
    
    <tr class="sortable">
        <td class="sortable alignCenter" >
            <netui-data:getData resultId="isSelectable" value="${container.item.selectable}"/>
                
            
            <c:if test="${isSelectable == 'false'}">                            
                <netui:checkBoxGroup dataSource="pageFlow.currentOrgNodeIds" disabled="true">
                    &nbsp;<netui:checkBoxOption value="${container.item.id}">&nbsp;</netui:checkBoxOption>                
                </netui:checkBoxGroup>
            </c:if>
            <c:if test="${isSelectable == 'true'}">   
            
                <netui-data:getData resultId="orgNodeName" value="${container.item.name}"/>
                <netui-data:getData resultId="orgNodeId" value="${container.item.id}"/>
                <% 
                    String orgNodeName = (String)pageContext.getAttribute("orgNodeName"); 
                    Integer orgNodeId = (Integer)pageContext.getAttribute("orgNodeId"); 
                    String orgNodeIdStr = orgNodeId.toString(); 
                %> 
                <input type="hidden" id="<%=orgNodeId%>" name="<%=orgNodeId%>" value="<%=orgNodeName%>">
                <netui:checkBoxGroup dataSource="pageFlow.currentOrgNodeIds">
                    &nbsp;<netui:checkBoxOption value="${container.item.id}" onClick="updateOrgNodeSelection(this);">&nbsp;</netui:checkBoxOption>                
                </netui:checkBoxGroup>
                
            </c:if>
        </td>        
        <td class="sortable alignLeft" >     
            <ctb:tablePathEntry srcLabelDataSource="${container.item.name}" srcValueDataSource="${container.item.id}" dstLabelDataSource="actionForm.orgNodeName" dstValueDataSource="actionForm.orgNodeId" shownAsLink="${container.item.hasChildren}"/>
        </td>
    </tr>
    
    </netui-data:repeaterItem>
    <netui-data:repeaterFooter>
    
        <tr class="sortable">
            <td class="sortableControls" colspan="2">
            <!-- Removed "request."  from request.orgPagerSummary  to make code compatible with webLogic 10.3-->
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
