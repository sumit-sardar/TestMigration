<%@ page import="java.io.*, java.util.*"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
 
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>
<netui-data:declareBundle bundlePath="helpResources" name="help"/>

<%! String templatePage = null;%>
<ctb:switch dataSource="${pageFlow.action}">
    <ctb:case value="schedule">
        <% templatePage="/resources/jsp/template.jsp";%>
    </ctb:case>
    <ctb:case value="edit">
        <% templatePage="/resources/jsp/editTemplate.jsp";%>
    </ctb:case>
    <ctb:case value="view">
        <% templatePage="/resources/jsp/viewTemplate.jsp";%>
    </ctb:case>
</ctb:switch>
<netui-template:template templatePage="<%=templatePage%>">
    <netui-template:setAttribute name="title" value="${bundle.web['selectsettings.window.title']}"/>
    <netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.scheduleTestSessionSelectSettings']}"/>
<netui-template:section name="bodySection">

<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->

        
<netui:form action="selectSettings" onSubmit="return disableRemoveStudentButton();">

<netui:hidden dataSource="actionForm.selectedProductName"/>        
<netui:hidden dataSource="actionForm.selectedLevel"/>        
<netui:hidden dataSource="actionForm.hasBreak"/> <!-- Changes for defect 60393 -->
<netui:hidden dataSource="actionForm.testAdmin.testName"/>   
<netui:hidden dataSource="actionForm.testAdmin.level"/>   
<netui:hidden dataSource="actionForm.testAdmin.accessCode"/>   
<netui:hidden dataSource="actionForm.selectedTestId"/>   

<netui:hidden dataSource="actionForm.accommodationOperand"/>
<netui:hidden dataSource="actionForm.selectedGrade"/>
<netui:hidden dataSource="actionForm.selectedAccommodationElements"/>

<netui:hidden tagId="actionElement" dataSource="actionForm.actionElement"/>
<netui:hidden tagId="currentAction" dataSource="actionForm.currentAction"/>
<netui:hidden dataSource="actionForm.action"/>        

<netui:hidden dataSource="actionForm.testStatePathList.sortColumn"/>        
<netui:hidden dataSource="actionForm.testStatePathList.sortOrderBy"/>        

<netui:hidden dataSource="actionForm.creatorOrgNodeName"/>   

<netui:hidden dataSource="actionForm.studentStatePathList.maxPageRequested"/>
<netui:hidden dataSource="actionForm.proctorStatePathList.maxPageRequested"/>
<netui:hidden dataSource="actionForm.testAdmin.isRandomize"/>    <!--added for randomize distractor-->

<netui:hidden dataSource="actionForm.autoLocator"/>

<netui-data:getData resultId="action" value="${pageFlow.action}"/>
<netui-data:getData resultId="hasBreak" value="${requestScope.hasBreak}"/> <!-- Changes for defect 60393 -->
<netui-data:getData resultId="studentCount" value="${requestScope.studentCount}"/>
<netui-data:getData resultId="isFormEditable" value="${requestScope.isFormEditable}"/>
<netui-data:getData resultId="displayFormList" value="${requestScope.displayFormList}"/>
<netui-data:getData resultId="hasStudentLoggedIn" value="${pageFlow.condition.hasStudentLoggedIn}"/>
<netui-data:getData resultId="testSessionExpired" value="${pageFlow.condition.testSessionExpired}"/>
<netui-data:getData resultId="hideCopyButton" value="${requestScope.hideCopyButton}"/>
<netui-data:getData resultId="isCopyTest" value="${pageFlow.condition.isCopyTest}"/>
<netui:hidden dataSource="actionForm.testAdmin.productId"/>


<ctb:switch dataSource="${pageFlow.action}">


<table width="100%" cellpadding="0" cellspacing="0" class="transparent">
    
	<tr>
		<td width="62%">
			<font size="6">
			<ctb:case value="schedule">
				<c:if test="${!isCopyTest}">  
					<h1><netui:span value="${bundle.web['selectsettings.title.schedule']}"/></h1>    
   					<p><netui:content value="${bundle.web['selectsettings.selectSessionSettings.message.schedule']}"/></p>
   				</c:if>
    			<c:if test="${isCopyTest}">        
    				<h1><netui:span value="${bundle.web['selectsettings.title.copytest']}"/></h1>    
   					 <p><netui:content value="${bundle.web['selectsettings.selectSessionSettings.message.schedule']}"/></p>
   				</c:if>
			</ctb:case>  
			<ctb:case value="edit">
   				<h1><netui:span value="${bundle.web['selectsettings.title.edit']}"/></h1>    
    			<p>
    			<c:if test="${!hasStudentLoggedIn}">        
        			<netui:content value="${bundle.web['selectsettings.selectSessionSettings.message.edit.noStudentLoggedIn']}"/>
   				</c:if>
   				<netui:content value="${bundle.web['selectsettings.selectSessionSettings.message.edit']}"/></p>
			</ctb:case> 
			</font>
		</td>
		
		<td  class="transparent"></td>
		<td width="100%" rowspan="2" align="right" valign="top">
			&nbsp;
		</td>

		<td rowspan="2">
			<table width="25"><tr><td></td></tr></table>
		</td>

	</tr>

</table>


			<ctb:case value="view">
    			<h1><netui:span value="${bundle.web['selectsettings.title.view']}"/></h1>    
    			<p><netui:content value="${bundle.web['selectsettings.selectSessionSettings.message.view']}"/></p>
    			<p align="right">
     		    <netui:anchor formSubmit="true" action="goToPrintOptionsFromView"><netui:content value="${bundle.web['selectsettings.selectSessionSettings.link.printTickets']}"/></netui:anchor> | 
     		    <c:if test="${reportable != null}">        
     		    <netui:anchor action="gotoViewReport"><netui:content value="${bundle.web['selectsettings.selectSessionSettings.link.viewReports']}"/></netui:anchor> | 
     		    </c:if>
       			<netui:anchor action="gotoViewStatus"><netui:content value="${bundle.web['selectsettings.selectSessionSettings.link.viewStatus']}"/></netui:anchor>
               </p>    
          </ctb:case>    
         </ctb:switch>  	








  

<netui-data:getData resultId="hasAlert" value="${requestScope.hasAlert}"/>
<netui-data:getData resultId="informationMessage" value="${requestScope.informationMessage}"/>
<netui-data:getData resultId="alertMessage" value="${requestScope.alertMessage}"/>
<netui-data:getData resultId="errorMessage" value="${requestScope.errorMessage}"/>

<c:if test="${errorMessage!=null}">
<p><ctb:message title="" style="errorMessage">
    <netui:content value="${requestScope.errorMessage}"/>
</ctb:message></p>
</c:if>        
<c:if test="${hasAlert}">
<p><ctb:message title="" style="alertMessage">
    <netui:errors/>
</ctb:message></p>
</c:if>  
<c:if test="${alertMessage!=null}">
<p><ctb:message title="" style="alertMessage">
    <netui:content value="${requestScope.alertMessage}"/>
</ctb:message></p>
</c:if>        
<c:if test="${informationMessage!=null}">
<p><ctb:message title="" style="informationMessage">
    <netui:content value="${requestScope.informationMessage}"/>
</ctb:message></p>
</c:if>        

<c:if test="${studentCount==0 && action=='view'}">
<p>  
<ctb:message title="{bundle.web['common.message.noStudents.title']}" style="informationMessage">
    <netui:content value="${bundle.web['common.message.noStudents.message.view']}"/>
</ctb:message>
</p>
</c:if>


<p>
<table class="transparent" width="100%">
    <tr class="transparent">
        <td class="transparent" >
            <ctb:switch dataSource="${pageFlow.action}">
                <ctb:case value="schedule">
                    <c:if test="${!isCopyTest}">        
                        <netui:button type="submit" value="${bundle.web['common.button.back']}" action="goToSelectTest"/>          
                    </c:if>
                    <netui:button type="submit" value="${bundle.web['common.button.next']}" action="goToPrintOptions"/>
                    <netui:button type="submit" value="${bundle.web['common.button.save']}" action="saveTest"/>
                    <netui:button type="submit" value="${bundle.web['common.button.cancel']}" action="goToHomePage" onClick="return verifyExitScheduleTest();"/>           
                </ctb:case>    
                <ctb:case value="edit">
                    <netui:button type="submit" value="${bundle.web['common.button.next']}" action="goToPrintOptions"/>
                    <netui:button type="submit" value="${bundle.web['common.button.save']}" action="saveTest"/>
                    <netui:button type="submit" value="${bundle.web['common.button.cancel']}" action="goToHomePage" onClick="return verifyExitEditTest();"/>           
                    <ctb:auth roles="root, Account Manager, Administrator, Administrative Coordinator, Coordinator">
                        <c:if test="${!hasStudentLoggedIn}">
                            <netui:button type="submit" value="${bundle.web['common.button.delete']}" action="deleteTest" onClick="return verifyDeleteTest();"/>          
                        </c:if>
                        <c:if test="${hasStudentLoggedIn && (!testSessionExpired)}">
                            <netui:button type="submit" value="${bundle.web['common.button.endTestSession']}" action="endTestSession" onClick="return verifyEndTest();"/>          
                        </c:if>
                    </ctb:auth>
                </ctb:case>    
                <ctb:case value="view">
                    <netui:button type="submit" value="${bundle.web['common.button.home']}" action="goToHomePage"/>           
                    <netui:button type="submit" value="${bundle.web['common.button.edit']}" action="goToEditTest"/>          
                    <c:if test="${!hideCopyButton}">
                        <netui:button type="submit" value="${bundle.web['common.button.copy']}" action="goToCopyTest"/>          
                    </c:if>
                </ctb:case>    
            </ctb:switch>  
        </td>
         
        
    </tr>
</table> 
</p>

<!-- ********************************************************************************************************************* -->
<!-- Included JSPs -->
<!-- ********************************************************************************************************************* -->

<jsp:include page="/scheduleTestPageflow/selectSettingsTestInfo.jsp" />
<jsp:include page="/scheduleTestPageflow/selectSettingsStudentProctorInfo.jsp" />
       
<hr>
<br/>
<p>
<ctb:switch dataSource="${pageFlow.action}">
<ctb:case value="schedule">
    <c:if test="${!isCopyTest}">        
        <netui:button type="submit" value="${bundle.web['common.button.back']}" action="goToSelectTest"/>          
    </c:if>
    <netui:button type="submit" value="${bundle.web['common.button.next']}" action="goToPrintOptions"/>
    <netui:button type="submit" value="${bundle.web['common.button.save']}" action="saveTest"/>
    <netui:button type="submit" value="${bundle.web['common.button.cancel']}" action="goToHomePage" onClick="return verifyExitScheduleTest();"/>           
</ctb:case>    
<ctb:case value="edit">
    <netui:button type="submit" value="${bundle.web['common.button.next']}" action="goToPrintOptions"/>
    <netui:button type="submit" value="${bundle.web['common.button.save']}" action="saveTest"/>
    <netui:button type="submit" value="${bundle.web['common.button.cancel']}" action="goToHomePage" onClick="return verifyExitEditTest();"/>           
    <ctb:auth roles="root, Account Manager, Administrator, Administrative Coordinator, Coordinator">
        <c:if test="${!hasStudentLoggedIn}">
            <netui:button type="submit" value="${bundle.web['common.button.delete']}" action="deleteTest" onClick="return verifyDeleteTest();"/>          
        </c:if>
        <c:if test="${hasStudentLoggedIn && (!testSessionExpired)}">
            <netui:button type="submit" value="${bundle.web['common.button.endTestSession']}" action="endTestSession" onClick="return verifyEndTest();"/>          
        </c:if>
    </ctb:auth>
</ctb:case>    
<ctb:case value="view">
    <netui:button type="submit" value="${bundle.web['common.button.home']}" action="goToHomePage"/>           
    <netui:button type="submit" value="${bundle.web['common.button.edit']}" action="goToEditTest"/>          
    <c:if test="${!hideCopyButton}">
        <netui:button type="submit" value="${bundle.web['common.button.copy']}" action="goToCopyTest"/>          
    </c:if>
</ctb:case>    
</ctb:switch>   
</p>

</netui:form>
        
<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
</netui-template:section>
</netui-template:template>
