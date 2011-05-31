<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>


<c:if test="${studentInSession != null}">           
<p>
    <ctb:message title="" style="informationMessage" >
          <netui:content value="${requestScope.studentInSession}"/>
    </ctb:message>
</p>    
</c:if>

<!--  studentList table -->
<table class="sortable">

<netui-data:repeater dataSource="requestScope.studentList">
    <netui-data:repeaterHeader>
    
    <tr class="sortable">
        <ctb:tableSortColumnGroup columnDataSource="actionForm.studentSortColumn" orderByDataSource="actionForm.studentSortOrderBy" anchorName="studentSearchResult">
            <th class="sortable alignCenter" nowrap><netui:content value="${bundle.web['common.column.select']}"/></th>                
            <th class="sortable alignLeft" width="30%" nowrap><ctb:tableSortColumn value="StudentName">Student Name</ctb:tableSortColumn></th>
            <th class="sortable alignLeft" width="30%" nowrap>&nbsp;&nbsp;Organization</th>
            <th class="sortable alignLeft" width="30%" nowrap><ctb:tableSortColumn value="LoginId">Login ID</ctb:tableSortColumn></th>
            <th class="sortable alignLeft" width="30%" nowrap><ctb:tableSortColumn value="Grade">Grade</ctb:tableSortColumn></th>
            <th class="sortable alignLeft" width="30%" nowrap><ctb:tableSortColumn value="StudentIdNumber">Student ID</ctb:tableSortColumn></th>
        </ctb:tableSortColumnGroup>
    </tr>
    
    </netui-data:repeaterHeader>
    <netui-data:repeaterItem>
    
    <tr class="sortable">
        <td class="sortable alignCenter">
            <netui-data:getData resultId="isSelectable" value="${container.item.selectable}"/>
            <c:if test="${isSelectable == 'true'}">           
                <netui:radioButtonGroup dataSource="actionForm.selectedStudentId">
                <netui-data:getData resultId="studentId" value="${container.item.studentId}" />
	             <%
	 				Integer studentId = (Integer) pageContext.getAttribute("studentId");
	             %>
                    &nbsp;<netui:radioButtonOption value="${container.item.studentId}" onClick="enableElementById('nextToModify'); setFocus('nextToModify'); setStudentId(${studentId});">&nbsp;</netui:radioButtonOption>                
                </netui:radioButtonGroup>
            </c:if>
            <c:if test="${isSelectable == 'false'}">           
                <netui:radioButtonGroup dataSource="actionForm.selectedStudentId" disabled="true">
                <netui-data:getData resultId="studentId" value="${container.item.studentId}" />
	             <%
	 				Integer studentId = (Integer) pageContext.getAttribute("studentId");
	             %>
                    &nbsp;<netui:radioButtonOption value="${container.item.studentId}" onClick="enableElementById('nextToModify'); setFocus('nextToModify'); setStudentId(${studentId});">&nbsp;</netui:radioButtonOption>                
                </netui:radioButtonGroup>
            </c:if>            
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
<div id="recommendedDialogID" style="display: none; position: relative; height: 420; width: 340; background-color: #cccccc; font-family: Arial,Helvetica,Sans Serif;" >
				<div style=" height: 10%; width: 100%">
					<table>
						<tr>
							<td style=" height: 50%; width: 10%; ">
								<div  style=" height:100%; width: 100%;">
									<img id="icon" src="/StudentRegistrationWeb/resources/images/bullet_question.png" width="23" height="23" >
								</div>
							</td>
							<td style="height: 50%; width: 85%;" valign="center">
								<div style=" height: 100%; width: 100%;  background-color: #3399FF; " >
									<span>
										<font color="#FFFFFF" size="4"><b>&nbsp; Online Assessment System &nbsp; </b> 
										</font>
									</span>
								</div>
							</td>
							<td style="height: 100%; width: 5%;">
								<a id="close" href="javascript:closePopup();" style="text-decoration:none;"> <font color="darkorange"><b>&nbsp;&nbsp;X&nbsp;&nbsp;</b></font></a>

							</td>
						</tr>
					</table>
				</div>
				<div align="left" style=" font-size: 78%;">
					<span >&nbsp;&nbsp;Student 
					</span>
					<b>
						<span id="studentName"> 
						</span>
					</b>
					<span>&nbsp; most recently took :
					</span>
				</div>
				<br>
				<div width="100%">
					<table  cellpadding="0" style="float:left;" width="95%">
						<tr>
							<td width="30%">
								<span  style=" font-size: 78%;">&nbsp;Session Name: 
								</span>
							</td>
							<td>
								<b>
									<span id="sessionName"  style=" font-size: 78%;"> 
									</span>
								</b>
							</td>
						</tr>
						<tr>
							<td width="30%">
								<span  style=" font-size: 78%;">&nbsp;Test Name: 
								</span>
							</td>
							<td>
								<b>
									<span id="testName"  style=" font-size: 78%;"> 
									</span>
								</b>
							</td>
						</tr>
						<tr>
							<td width="30%">
							</td>
							<td>
								<table width="100%">
									<tr><hr size=1 /></tr>
								</table>
							</td>
						</tr>

						<tr>
							<td width="30%">
							</td>
							<td>
							<div style= "overflow-y:auto; height:100px">
								<table id ="subtestList">
									<tr></tr>
								</table>
								</div>
							</td>
						</tr>
						<tr>
							<td width="30%">
							</td>
							<td>
								<table width="100%">
									<tr><hr size=1 /></tr>
								</table>
							</td>
						</tr>
						<tr>
							<td width="30%">
								<span  style=" font-size: 78%;">&nbsp;CompletedDate: 
								</span>
							</td>
							<td>
								<b>
									<span id="completedDate"  style=" font-size: 78%;"> 
									</span>
								</b>
							</td>
						</tr>
					</table>
					<br>


					<div align="left">
					<span  style=" font-size: 78%;">&nbsp;&nbsp;Do you want to register this student to the post-test?
					</span>
					<br>
					<br>
					<span style=" font-size: 78%;">&nbsp;&nbsp;Click <b>Yes</b> to use <b><span id="recommendedTest"></span></b>
					</span>
					<br>
					<span style=" font-size: 78%;">&nbsp;&nbsp;Click <b>No</b> to continue with the currently selected test.
					</span>
					</div>
				</div>
				<br>
				<center>
					<input type="submit" id="Yes" value="&nbsp;Yes&nbsp;">
					<input type="submit" id="No"  value="&nbsp;No&nbsp;&nbsp;">
				</center>
				<br>
			</div>
			
					
					

