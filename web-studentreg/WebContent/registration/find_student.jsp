<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="netui-tags-databinding.tld" prefix="netui-compat-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui-compat"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
 
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>

<input type="hidden" name="firstFocusId" id="firstFocusId" value="{actionForm.studentProfile.studentNumber}" />


<table class="sortable">
    <tr class="sortable">
        <td class="sortableControls">
        
<br/>        
<table class="tableFilter">
    <tr class="tableFilter">
        <td class="tableFilter" width="100" align="right">Student ID:</td>
        <td class="tableFilter" width="*"><netui:textBox tagId="studentNumber" dataSource="actionForm.studentProfile.studentNumber" tabindex="1"/></td>
        <td class="tableFilter" width="100" align="right">Login ID:</td>
        <td class="tableFilter" width="*"><netui:textBox dataSource="actionForm.studentProfile.userName" tabindex="5"/></td>
    </tr>
    <tr class="tableFilter">
        <td class="tableFilter" width="100" align="right">First Name:</td>
        <td class="tableFilter" width="200"><netui:textBox dataSource="actionForm.studentProfile.firstName" tabindex="2"/></td>
        <td class="tableFilter" width="100" align="right">Grade:</td>
        <td class="tableFilter" width="*">
            <netui:select optionsDataSource="${pageFlow.gradeOptions}" dataSource="actionForm.studentProfile.grade" size="1" style="width:155px" tabindex="6"/>
        </td>
    </tr>
    <tr class="tableFilter">
        <td class="tableFilter" width="100" align="right">Middle Name:</td>
        <td class="tableFilter" width="200"><netui:textBox dataSource="actionForm.studentProfile.middleName" tabindex="3"/></td>
        <td class="tableFilter" width="100" align="right">Gender:</td>
        <td class="tableFilter" width="*">
            <netui:select optionsDataSource="${pageFlow.genderOptions}" dataSource="actionForm.studentProfile.gender" size="1" style="width:155px" tabindex="7"/>
        </td>
    </tr>
    <tr class="tableFilter">
        <td class="tableFilter" width="100" align="right">Last Name:</td>
        <td class="tableFilter" width="200"><netui:textBox dataSource="actionForm.studentProfile.lastName" tabindex="4"/></td>
        <td class="tableFilter" width="100" align="right">&nbsp;</td>
        <td class="tableFilter" width="*">
            <netui:button styleClass="button" value="Search" type="submit" onClick="setElementValueAndSubmitWithAnchor('currentAction', 'applySearch', 'studentProfileResult');" tabindex="8"/>
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <netui:button styleClass="button" value="Clear All" type="submit" onClick="setElementValueAndSubmit('currentAction', 'clearSearch');" tabindex="9"/>&nbsp;
        </td>
    </tr>
</table>    
<br/>

        </td>
    </tr>
</table>


        


<br/>
<!--  student search result -->
<a name="studentSearchResult"><!-- studentSearchResult --></a>    
<c:if test="${studentList != null}">     
    <p><netui:content value="The following students match your search criteria."/></p>
    <p><jsp:include page="/registration/find_student_result.jsp" /></p>
</c:if>


<c:if test="${searchResultEmpty != null}">     
    <ctb:message title="Search Result" style="informationMessage" >
          <netui:content value="${requestScope.searchResultEmpty}"/>
    </ctb:message><br/>
</c:if>




<!-- buttons -->
<p>
    <netui:button type="submit" value="Cancel" action="gotoHomePage" onClick="return verifyExitRegisterStudent();"/>
    <netui:button tagId="nextToModify" type="submit" value="Next" action="toModifyTestFromFind" disabled="${requestScope.disableNextButton}"/>
</p>

