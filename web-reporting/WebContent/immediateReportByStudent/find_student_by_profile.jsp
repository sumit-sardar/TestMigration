<%@ page language="java" contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>
<%
   	Boolean isStudentIdConfigurable = (Boolean)request.getAttribute("isStudentIdConfigurable"); // Change For CR - GA2011CR001
	
%>

<table class="sortable">
    <tr class="sortable">
        <td class="sortableControls">
<br/>        
<table class="tableFilter">
    <tr class="tableFilter">
   
        
         <c:if test="${isStudentIdConfigurable}">   
         <td class="tableFilter" width="100" align="right">${studentIdArrValue[0]}:</td>
          </c:if>
          <c:if test="${!isStudentIdConfigurable}">   
       <td class="tableFilter" width="100" align="right">Student ID:</td>
         </c:if>
        <td class="tableFilter" width="*"><netui:textBox tagId="studentNumber" dataSource="actionForm.studentProfile.studentNumber" style="width:320px" tabindex="1"/></td>
        <td class="tableFilter" width="100" align="right">Login ID:</td>
        <td class="tableFilter" width="*"><netui:textBox tagId="userName" dataSource="actionForm.studentProfile.userName" tabindex="6" style="width:320px"/></td>
    </tr>
    <tr class="tableFilter">
        <td class="tableFilter" width="100" align="right">First Name:</td>
        <td class="tableFilter" width="200"><netui:textBox tagId="firstName" dataSource="actionForm.studentProfile.firstName" tabindex="2" style="width:320px"/></td>
        <td class="tableFilter" width="100" align="right">Grade:</td>
        <td class="tableFilter" width="*">
            <netui:select optionsDataSource="${pageFlow.gradeOptions}" dataSource="actionForm.studentProfile.grade" size="1" style="width:320px" tabindex="7"/>
            
            
        </td>
    </tr>
    <tr class="tableFilter">
        <td class="tableFilter" width="100" align="right">Middle Name:</td>
        <td class="tableFilter" width="200"><netui:textBox tagId="middleName" dataSource="actionForm.studentProfile.middleName" tabindex="3" style="width:320px"/></td>
        <td class="tableFilter" width="100" align="right">Gender:</td>
        <td class="tableFilter" width="*">
             <netui:select optionsDataSource="${pageFlow.genderOptions}" dataSource="actionForm.studentProfile.gender" size="1" style="width:320px" tabindex="8"/>
           
        </td>
    </tr>
    <tr class="tableFilter">
        <td class="tableFilter" width="100" align="right">Last Name:</td>
        <td class="tableFilter" width="200"><netui:textBox tagId="lastName" dataSource="actionForm.studentProfile.lastName" tabindex="4" style="width:320px"/></td>
        <td class="tableFilter" width="100" align="right"> *Test Name:</td>
        <td class="tableFilter" width="*">
             <netui:select tagId="testNameId" optionsDataSource="${pageFlow.testNameOptions}" dataSource="actionForm.studentProfile.productNameList" size="1" style="width:320px" tabindex="9" onChange="javascript: populateContentArea(this)"/>
           
        </td>
    </tr>
     <tr class="tableFilter">
        <td class="tableFilter" width="100" align="right">Scoring Status:</td>
         <td class="tableFilter" width="*">
             <netui:select optionsDataSource="${pageFlow.scoringStatusOptions}" dataSource="actionForm.studentProfile.scoringStatus" size="1" style="width:320px" tabindex="5"/>
        </td>
        <td class="tableFilter" width="100" align="right">Completed ContentArea:</td>
        <td class="tableFilter" width="*">
             <netui:select tagid= "contentAreas" optionsDataSource="${requestScope.contentAreaList}" dataSource="actionForm.studentProfile.completedContentArea" size="1" style="width:320px" tabindex="10"/>
        </td>
    </tr>
    <tr class="tableFilter" width = "100%" >
    	<td class="tableFilter" colspan="4" align="center">
    		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    		<netui:button styleClass="button" value="Search" type="submit" onClick="setElementValue('currentAction', 'applySearch');" tabindex="11"/>
    		&nbsp;&nbsp;&nbsp;&nbsp;
    		<netui:button styleClass="button" value="Clear All" type="button" onClick="setElementValueAndSubmit('currentAction', 'clearSearch');" tabindex="12"/>
    	</td>
    </tr>
</table>    
<br/>
        </td>
    </tr>
</table>



        
