<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.ctb.bean.testAdmin.ScorableItem"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>

<netui-data:declareBundle bundlePath="oasResources" name="oas" />
<netui-data:declareBundle bundlePath="webResources" name="web" />
<netui-data:declareBundle bundlePath="widgetResources" name="widgets" />
<netui-data:declareBundle bundlePath="helpResources" name="help" />
<netui-template:template templatePage="/resources/jsp/scoring_template.jsp">
<!-- 
template_find_student.jsp
-->
  <netui-template:setAttribute name="title" value="${bundle.web['individualStudentScoring.window.title']}"/>
    <netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.findStudent']}"/>
	
	<netui-template:section name="bodySection">
	


<!-- title -->

<h1>
<netui:content value="${pageFlow.pageTitle}" />
</h1>

<p><netui:content value="${bundle.web['StudentScoring.title.message']}" /></p><br/>

<netui:form action="findStudent">

<netui:hidden tagId="actionElement" dataSource="actionForm.actionElement"/> 
<netui:hidden tagId="currentAction" dataSource="actionForm.currentAction"/>
<netui:hidden  dataSource="actionForm.studentMaxPage"/> 
<netui:hidden tagId="testAdminId" dataSource="actionForm.testAdminId"/>
<netui:hidden tagId="testSessionName" dataSource="actionForm.testSessionName"/>
<netui:hidden tagId="testAccessCode" dataSource="actionForm.testAccessCode"/>

<h2><netui:content value="${bundle.web['individualStudentScoring.StudentDetails.title']}"/></h2>
<!-- message -->
<jsp:include page="/scorebystudent/show_message.jsp" />		
<table class="transparent" width="100%">

<tr class="transparent">
    <td class="transparent">
        <netui:content value="${bundle.web['individualStudentScoring.StudentDetails.AccessCode']}"/>
    </td>
    <td class="transparent">    
        <div class="formValueLarge"><netui:span value="${actionForm.testAccessCode}" styleClass="formValueLarge"/></div>
    </td>
</tr>

<tr class="transparent">
    <td class="transparent">
        <netui:content value="${bundle.web['individualStudentScoring.StudentDetails.TestSessionName']}"/>
    </td>
    <td class="transparent">
        <div class="formValueLarge"><netui:span value="${actionForm.testSessionName}" styleClass="formValueLarge"/></div>

    </td>
</tr> 
 
  
</table><br/><br/>

<!--  studentList table -->
<table class="sortable">

  <%
   	Boolean isStudentIdConfigurable = (Boolean)request.getAttribute("isStudentIdConfigurable"); // Change For CR - GA2011CR001
	
%> 
        
<netui-data:repeater dataSource="requestScope.studentList">
    <netui-data:repeaterHeader>
    
    <tr class="sortable">
        
        <ctb:tableSortColumnGroup columnDataSource="actionForm.studentSortColumn" orderByDataSource="actionForm.studentSortOrderBy" anchorName="resultEmpty">
            <th class="sortable alignLeft" width="20%" nowrap><ctb:tableSortColumn value="UserName">Login ID</ctb:tableSortColumn></th>
            <th class="sortable alignLeft" width="20%" nowrap><ctb:tableSortColumn value="LastName">Last Name</ctb:tableSortColumn></th>
            <th class="sortable alignLeft" width="20%" nowrap><ctb:tableSortColumn value="FirstName">First Name</ctb:tableSortColumn></th>
            
         <c:if test="${isStudentIdConfigurable}"> 
         <th class="sortable alignLeft" width="20%" nowrap>&nbsp;${studentIdArrValue[0]}</th>  
         
          </c:if>
          <c:if test="${!isStudentIdConfigurable}">   
      <th class="sortable alignLeft" width="20%" nowrap>&nbsp;Student ID</th>
         </c:if>
        
     
            
            <th class="sortable alignLeft" width=5%" nowrap><ctb:tableSortColumn value="Grade">Grade</ctb:tableSortColumn></th>
             
          <th class="sortable alignLeft" width="20%" nowrap><ctb:tableSortColumn value="ScoringStatus">Manual Scoring Status</ctb:tableSortColumn></th>
        
          
        <th class="sortable alignLeft" width="20%" nowrap>&nbsp;Online Test Status</th>
      
           
        </ctb:tableSortColumnGroup><br/>
    </tr>
    
    </netui-data:repeaterHeader>
   
    <netui-data:repeaterItem>
    
    <tr class="sortable">
    
        <td class="sortable"> 
             
              
             
             <netui:anchor href="beginDisplayStudItemList.do?loginName=${container.item.userName}&testAdminId=${container.item.testAdminId}&testRosterId=${container.item.testRosterId}&itemSetIdTC=${container.item.itemSetIdTC}">
             
			<netui:span value="${container.item.userName}" defaultValue="&nbsp;"/>
			</netui:anchor>
        </td>
        <td class="sortable">
            <netui:span value="${container.item.lastName}"/>
        </td>
         <td class="sortable">
            <netui:span value="${container.item.firstName}"/>
        </td>
       
        
        <td class="sortable">
            <netui:span value="${container.item.studentId}"/>
        </td>
        <td class="sortable">
            <netui:span value="${container.item.grade}"/>
        </td>
        <td class="sortable" align="center">
          <netui-data:getData resultId="scoringstatus" value="${container.item.scoringStatus}"/> 
         	 <c:if test="${scoringstatus =='IN'}">  
            <netui:span  value="Incomplete" />  
            </c:if>
             <c:if test="${scoringstatus =='CO'}">  
            <netui:span value="Complete" />   
            </c:if>     
        </td>
        <td class="sortable">
            <netui:span value="${container.item.testCompletionStatusDesc}"/>
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

<c:if test="${resultEmpty != null}">     
    <ctb:message title="Search Result" style="informationMessage" >
          <netui:content value="${requestScope.resultEmpty}"/>
    </ctb:message>
</c:if>           
   <br/>
    <netui:button type="submit" value="${bundle.web['common.button.home']}" action="goToHomePage"/>
   </netui:form>

		<!-- ********************************************************************************************************************* -->
		<!-- End Page Content -->
		<!-- ********************************************************************************************************************* -->
	</netui-template:section>
</netui-template:template>
