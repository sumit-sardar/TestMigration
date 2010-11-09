<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>
<%
   	Boolean isStudentIdConfigurable = (Boolean)request.getAttribute("isStudentIdConfigurable"); // Change For CR - GA2011CR001
	Boolean isABECustomer = (Boolean)request.getAttribute("isABECustomer");
%>

<table class="sortable">
    <tr class="sortable">
        <td class="sortableControls">
<br/>
		<table class="tableFilter">
			<tr class="tableFilter">

				<c:if test="${isABECustomer}">
					<td class="tableFilter" width="170" align="right">Social Security Number/Student ID:</td>
					<td class="tableFilter" width="*"><netui:textBox tagId="studentNumber"
						dataSource="actionForm.studentProfile.studentNumber" tabindex="1" /></td>
				</c:if>

				<c:if test="${!isABECustomer}">
					<td class="tableFilter" width="100" align="right">Student ID:</td>
					<td class="tableFilter" width="*"><netui:textBox tagId="studentNumber"
						dataSource="actionForm.studentProfile.studentNumber" tabindex="1" /></td>
				</c:if>

				<c:if test="${isABECustomer}">
					<td class="tableFilter" width="110" align="right">Instructor First Name:</td>
					<td class="tableFilter" width="200"><netui:textBox tagId="lastName"
						dataSource="actionForm.studentProfile.lastName" tabindex="4" /></td>
				</c:if>

				<c:if test="${!isABECustomer}">
					<td class="tableFilter" width="100" align="right">Login ID:</td>
					<td class="tableFilter" width="*"><netui:textBox tagId="userName"
						dataSource="actionForm.studentProfile.userName" tabindex="5" /></td>

				</c:if>
			</tr>
			<tr class="tableFilter">

				<c:if test="${isABECustomer}">
					<td class="tableFilter" width="170" align="right">First Name:</td>
					<td class="tableFilter" width="200"><netui:textBox tagId="firstName"
						dataSource="actionForm.studentProfile.firstName" tabindex="2" /></td>
				</c:if>

				<c:if test="${!isABECustomer}">
					<td class="tableFilter" width="100" align="right">First Name:</td>
					<td class="tableFilter" width="200"><netui:textBox tagId="firstName"
						dataSource="actionForm.studentProfile.firstName" tabindex="2" /></td>
				</c:if>

				<c:if test="${isABECustomer}">
					<td class="tableFilter" width="110" align="right">Instructor Last Name:</td>
					<td class="tableFilter" width="200"><netui:textBox tagId="lastName"
						dataSource="actionForm.studentProfile.lastName" tabindex="4" /></td>
				</c:if>

				<c:if test="${!isABECustomer}">
					<td class="tableFilter" width="100" align="right">Grade:</td>
					<td class="tableFilter" width="*"><netui:select optionsDataSource="${pageFlow.gradeOptions}"
						dataSource="actionForm.studentProfile.grade" size="1" style="width:155px" tabindex="6" /></td>
				</c:if>

			</tr>
			<tr class="tableFilter">
				<c:if test="${isABECustomer}">
				<td class="tableFilter" width="170" align="right">Middle Name:</td>
				<td class="tableFilter" width="200"><netui:textBox tagId="middleName"
					dataSource="actionForm.studentProfile.middleName" tabindex="3" /></td>
				</c:if>	
			
				<c:if test="${!isABECustomer}">
				<td class="tableFilter" width="100" align="right">Middle Name:</td>
				<td class="tableFilter" width="200"><netui:textBox tagId="middleName"
					dataSource="actionForm.studentProfile.middleName" tabindex="3" /></td>
				</c:if>
					
				<c:if test="${isABECustomer}">
					<td class="tableFilter" width="110" align="right">Grade:</td>
					<td class="tableFilter" width="*"><netui:select optionsDataSource="${pageFlow.gradeOptions}"
						dataSource="actionForm.studentProfile.grade" size="1" style="width:155px" tabindex="6" /></td>
				</c:if>
				<c:if test="${!isABECustomer}">
					<td class="tableFilter" width="100" align="right">Gender:</td>
					<td class="tableFilter" width="*"><netui:select optionsDataSource="${pageFlow.genderOptions}"
						dataSource="actionForm.studentProfile.gender" size="1" style="width:155px" tabindex="7" /></td>
				</c:if>
			</tr>
			<tr class="tableFilter">
				<c:if test="${isABECustomer}">
					<td class="tableFilter" width="170" align="right">Last Name:</td>
					<td class="tableFilter" width="200"><netui:textBox tagId="lastName"
						dataSource="actionForm.studentProfile.lastName" tabindex="4" /></td>
				</c:if>
				<c:if test="${!isABECustomer}">
					<td class="tableFilter" width="100" align="right">Last Name:</td>
					<td class="tableFilter" width="200"><netui:textBox tagId="lastName"
						dataSource="actionForm.studentProfile.lastName" tabindex="4" /></td>
				</c:if>
				<c:if test="${isABECustomer}">
					<td class="tableFilter" width="110" align="right">Gender:</td>
					<td class="tableFilter" width="*"><netui:select optionsDataSource="${pageFlow.genderOptions}"
						dataSource="actionForm.studentProfile.gender" size="1" style="width:155px" tabindex="7" /></td>
				</c:if>
				<c:if test="${!isABECustomer}">
					<td class="tableFilter" width="100" align="right">&nbsp;</td>
					<td class="tableFilter" width="*"><netui:button styleClass="button" value="Search" type="submit"
						onClick="setElementValue('currentAction', 'applySearch');" tabindex="8" />
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <netui:button styleClass="button" value="Clear All" type="button"
						onClick="setElementValueAndSubmit('currentAction', 'clearSearch');" tabindex="9" />&nbsp;</td>
				</c:if>

			</tr>
			<tr class="tableFilter">
				<c:if test="${isABECustomer}">
					<td class="tableFilter" width="170" align="right">Login ID:</td>
					<td class="tableFilter" width="*"><netui:textBox tagId="userName"
						dataSource="actionForm.studentProfile.userName" tabindex="5" /></td>
				</c:if>
				<c:if test="${isABECustomer}">
					<td class="tableFilter" width="110" align="right">Follow Up Status:</td>
					<td class="tableFilter" width="*"><select style="width: 155px">
					<Option selected="true">Please Select</Option>
					<Option>Pending</Option>
					<Option>Completed</Option>
				</select></td>
				</c:if>
			</tr>
			<c:if test="${isABECustomer}">
			<tr class="tableFilter">
				<td class="tableFilter" width ="170" align="right"></td>
				<td class="tableFilter" width="*"></td>
				<td class="tableFilter" width="100" align="right">&nbsp;</td>
					<td class="tableFilter" width="*"><netui:button styleClass="button" value="Search" type="submit"
						onClick="setElementValue('currentAction', 'applySearch');" tabindex="8" />
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <netui:button styleClass="button" value="Clear All" type="button"
						onClick="setElementValueAndSubmit('currentAction', 'clearSearch');" tabindex="9" />&nbsp;</td>
			</tr>
		    </c:if>
		</table>
		<br/>
        </td>
    </tr>
</table>



        
