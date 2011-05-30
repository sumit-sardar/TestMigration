<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>
<link href="/StudentManagementWeb/resources/css/legacy.css" type="text/css" rel="stylesheet" />
<link href="/StudentManagementWeb/resources/css/widgets.css" type="text/css" rel="stylesheet" />


<%
    Boolean isStudentIdConfigurable = (Boolean)request.getAttribute("isStudentIdConfigurable"); //Start Change For CR - GA2011CR001
%>
<input type="hidden" id="studentId" />

<!--  studentList table -->
<table class="sortable">

    <tr class="sortable">
        <td class="sortableControls" colspan="12" height="30">&nbsp;
            <netui:button tagId="View" type="submit" value="View" onClick="setElementValue('currentAction', 'viewStudent');" disabled="${requestScope.disableButtons}"/>              
        <c:if test="${showEditButton == 'true'}">                 
            <netui:button tagId="Edit" type="submit" value=" Edit " onClick="setElementValue('currentAction', 'editStudent');" disabled="${requestScope.disableButtons}"/>              
        </c:if>
        <c:if test="${showDeleteButton == 'true'}">                 
            <netui:button tagId="Delete" type="submit" value="Delete" onClick="return verifyDeleteStudent();" disabled="${requestScope.disableButtons}"/>
        </c:if>
        <c:if test="${sessionScope.canRegisterStudent}">
			<netui:button tagId="RegisterStudent" type="button" value="Register Student" onclick="viewStudentDetail();" disabled="${requestScope.disableButtons}"/>
        </c:if> 
        
        </td>
    </tr>
        
        
<netui-data:repeater dataSource="requestScope.studentList">
    <netui-data:repeaterHeader>
    
    <tr class="sortable">
        <ctb:tableSortColumnGroup columnDataSource="actionForm.studentSortColumn" orderByDataSource="actionForm.studentSortOrderBy" anchorName="studentSearchResult">
            <th class="sortable alignCenter" nowrap><netui:content value="${bundle.web['common.column.select']}"/></th>                
            <th class="sortable alignLeft" width="30%" nowrap><ctb:tableSortColumn value="StudentName">Student Name</ctb:tableSortColumn></th>
            <th class="sortable alignLeft" width="30%" nowrap>&nbsp;&nbsp;Organization</th>
            <th class="sortable alignLeft" width="30%" nowrap><ctb:tableSortColumn value="LoginId">Login ID</ctb:tableSortColumn></th>
            <th class="sortable alignLeft" width="30%" nowrap><ctb:tableSortColumn value="Grade">Grade</ctb:tableSortColumn></th>
           <c:if test="${isStudentIdConfigurable}">   
          <th class="sortable alignLeft" width="30%" nowrap><ctb:tableSortColumn value="StudentIdNumber">${studentIdArrValue[0]}</ctb:tableSortColumn></th>
          </c:if>
          <c:if test="${!isStudentIdConfigurable}">   
        <th class="sortable alignLeft" width="30%" nowrap><ctb:tableSortColumn value="StudentIdNumber">Student ID</ctb:tableSortColumn></th>
         </c:if>
           
        </ctb:tableSortColumnGroup>
    </tr>
    
    </netui-data:repeaterHeader>
    <netui-data:repeaterItem>
    
    <tr class="sortable">
        <td class="sortable alignCenter">
            <netui:radioButtonGroup dataSource="actionForm.selectedStudentId">
            <netui-data:getData resultId="studentId" value="${container.item.studentId}" />
             <%
 				Integer studentId = (Integer) pageContext.getAttribute("studentId");
             %>
                &nbsp;<netui:radioButtonOption value="${container.item.studentId}" onClick="enableElementById('View'); enableElementById('Edit'); enableElementById('Delete'); enableElementById('RegisterStudent'); setFocus('View'); setStudentId(${studentId});">&nbsp;</netui:radioButtonOption>                
            </netui:radioButtonGroup>
        </td>        
        <td class="sortable">
            <netui:span value="${container.item.displayName}"/>
        </td>
        <td class="sortable">
            <netui:span value="${container.item.orgNodeNamesString}"/>
        </td>
        <td class="sortable">
            <netui:span value="${container.item.userName}"/>
        </td>
        <td class="sortable">
            <netui:span value="${container.item.grade}"/>
        </td>
        <td class="sortable">
            <netui:span value="${container.item.studentNumber}"/>
        </td>
    </tr>
    
    </netui-data:repeaterItem>
    <netui-data:repeaterFooter>
    
        <tr class="sortable">
            <td class="sortableControls" colspan="7">
                <ctb:tablePager dataSource="actionForm.studentPageRequested" summary="request.studentPagerSummary" objectLabel="${bundle.oas['object.students']}" foundLabel="Found" id="studentSearchResult" anchorName="studentSearchResult"/>
            </td>
        </tr>         
            
    </netui-data:repeaterFooter>
</netui-data:repeater>
    
</table>

			<div id="recommendedDialogID" style="display: none; position: absolute; height: 380; width: 300; background-color: #cccccc; font-family: Arial,Helvetica,Sans Serif;" >
				<div style=" height: 10%; width: 100%">
					<table cellpadding="2">
						<tr>
							<td style=" height: 100%; width: 10%; ">
								<div  style=" height: 100%; width: 100%;">
									<img id="icon" src="/StudentManagementWeb/resources/images/bullet_question.png" width="23" heigh="23" >
								</div>
							</td>
							<td style="height: 100%; width: 80%;" align="center">
								<div style=" height: 100%; width: 100%;  background-color: deepskyblue; " >
									<span>
										<font color="white" size="2">&nbsp;&nbsp; <b> Online Assessment System </b>&nbsp;&nbsp; 
										</font>
									</span>
								</div>
							</td>
							<td style="height: 100%; width: 10%;">
								<div style=" height: 100%; width: 100%;"> <font color="darkorange"><b>&nbsp;&nbsp;X&nbsp;&nbsp;</b></font>
								</div>
							</td>
						</tr>
					</table>
				</div>
				<div align="left" style=" font-size: 80%;">
					<span >&nbsp; Student 
					</span>
					<b>
						<span id="studentName"> 
						</span>
					</b>
					<span>most recently took :
					</span>
				</div>
				<br>
				<div style=" font-size: 82%;">
					<table style=" font-size: 80%;">
						<tr>
							<td width="30%">
								<span>&nbsp; Session Name: 
								</span>
							</td>
							<td>
								<b>
									<span id="sessionName"> 
									</span>
								</b>
							</td>
						</tr>
						<tr>
							<td width="30%">
								<span>&nbsp; Test Name: 
								</span>
							</td>
							<td>
								<b>
									<span id="testName" > 
									</span>
								</b>
							</td>
						</tr>
						<tr>
							<td width="30%">
							</td>
							<td>
								<table id ="subtestList">
									<tr><hr></tr>
								</table>
							</td>
						</tr>
						<tr>
							<td width="30%">
								<span>&nbsp; CompletedDate: 
								</span>
							</td>
							<td>
								<b>
									<span id="completedDate"> 
									</span>
								</b>
							</td>
						</tr>
					</table>
					<br>
					

					<div align="left" style=" font-size: 81.2%;">
					<span>&nbsp; Do you want to register this student to the post-test?
					</span>
					<br>
					<br>
					<span>&nbsp;  Click <b>Yes</b> to use <b><span id="recommendedTest"></span></b>
					</span>
					<br>
					<span>&nbsp; Click <b>No</b> to continue with the currently selected test.
					</span>
					</div>
				</div>
				<br>
				<center>
					<input type="submit" id="Yes" value="Yes">
					<input type="submit" id="No"  value="No">
				</center>
			</div>




