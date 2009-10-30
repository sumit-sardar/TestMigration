<%@ page import="java.io.*, java.util.*"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="netui-tags-databinding.tld" prefix="netui-compat-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui-compat"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>

<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>

<netui-data:getData resultId="action" value="${pageFlow.action}"/>
<netui-data:getData resultId="hasBreak" value="${actionForm.hasBreak}"/>
<netui-data:getData resultId="isRandomize" value="${actionForm.testAdmin.isRandomize}"/>

<netui-data:getData resultId="showAccommodations" value="${actionForm.testRosterFilter.showAccommodations}"/>
<netui-data:getData resultId="hasStudentLoggedIn" value="${pageFlow.condition.hasStudentLoggedIn}"/>
<netui-data:getData resultId="testSessionExpired" value="${pageFlow.condition.testSessionExpired}"/>
<netui-data:getData resultId="isCopyTest" value="${pageFlow.condition.isCopyTest}"/>
<netui-data:getData resultId="showLevelOrGrade" value="${pageFlow.showLevelOrGrade}"/>
<netui-compat-data:getData resultId="autoLocator" value="{actionForm.autoLocator}"/>

  
<netui-compat-data:getData resultId="studentCount" value="{request.studentCount}"/>
<netui-compat-data:getData resultId="isFormEditable" value="{request.isFormEditable}"/>
<netui-compat-data:getData resultId="displayFormList" value="{request.displayFormList}"/>
<netui-compat-data:getData resultId="showSelectOrganization" value="{request.showSelectOrganization}"/>
<netui-compat-data:getData resultId="productType" value="{request.productType}"/>
<netui-compat-data:getData resultId="isTabeProduct" value="{request.isTabeProduct}"/>
<netui-compat-data:getData resultId="isTabeBatterySurveyProduct" value="{request.isTabeBatterySurveyProduct}"/>
<netui-compat-data:getData resultId="isTabeLocatorProduct" value="{request.isTabeLocatorProduct}"/>
<netui-compat-data:getData resultId="showLocatorSubtest" value="{request.showLocatorSubtest}"/>
<netui-compat-data:getData resultId="tabeWithSingleSubtest" value="{request.tabeWithSingleSubtest}"/>
<netui-compat-data:getData resultId="overrideStartDate" value="{request.overrideStartDate}"/>


<!--change for license-->

<netui-data:getData resultId="licenseConfig" value="${sessionScope.disableAvailableBarTestSeesion}"/>
<netui-data:getData resultId="licenseAdminConfig" value="${sessionScope.disableAvailableBarSubtest}"/>
<netui-data:getData resultId="displayLicenseBar" value="${sessionScope.displayLicenseBar}"/>
<netui-data:getData resultId="licensebarColor" value="{pageFlow.licenseBarColor}"/>

<netui:hidden dataSource="actionForm.creatorOrgNodeId"/>  
<table border="0" cellpadding="0" cellspacing="0" width="100%">
<tr>
<td valign="top" width="75%">

<h3><netui:span value="${bundle.web['selectsettings.tests.title']}"/></h3>

<ctb:auth roles="root, Account Manager, Administrator, Administrative Coordinator, Coordinator">
    <c:if test="${action=='edit' && !hasStudentLoggedIn && !testSessionExpired}">    
        <netui:button type="submit" value="${bundle.web['common.button.change']}" action="goToSelectTest"/>          
        <br/>
    </c:if>
    <c:if test="${action=='schedule' && isCopyTest}">    
        <netui:button type="submit" value="${bundle.web['common.button.change']}" action="goToSelectTest"/>          
        <br/>
    </c:if>
</ctb:auth>



<!-- Tests -->
<table class="transparent">
    <tr class="transparent">
        <td class="transparent" width="250"><netui:span value="${bundle.web['selectsettings.label.testName']}"/></td>
        <td class="transparent"><div class="formValueLarge"><netui-compat:label value="{actionForm.testAdmin.testName}" styleClass="formValueLarge"/></div></td>
    </tr>
     
    
<c:if test="${! isTabeProduct}">                     
    <c:if test="${showLevelOrGrade == 'level'}"> 
        <tr class="transparent">
            <td class="transparent" width="250"><netui:span value="${bundle.web['selectsettings.label.level']}"/></td>
            <td class="transparent"><div class="formValueLarge"><netui-compat:label value="{actionForm.testAdmin.level}" styleClass="formValueLarge"/></div></td>
        </tr>
    </c:if>        
    <c:if test="${showLevelOrGrade == 'grade'}"> 
        <tr class="transparent">
            <td class="transparent" width="250"><netui:span value="${bundle.web['selectsettings.label.grade']}"/></td>
            <td class="transparent"><div class="formValueLarge"><netui-compat:label value="{actionForm.testAdmin.level}" styleClass="formValueLarge"/></div></td>
        </tr>
    </c:if>        
</c:if>        

    
    <c:if test="${action=='view'}">    
    <tr class="transparent">
        <td class="transparent" width="250"><netui:span value="${bundle.web['selectsettings.label.testSessionId']}"/></td>
        <td class="transparent"><div class="formValueLarge"><netui-compat:label value="{pageFlow.testSessionId}" styleClass="formValueLarge"/></div></td>
    </tr>
    </c:if>
    
<c:if test="${! isTabeProduct}">                     
    <c:if test="${! hasMultipleSubtests}"> 
        <tr class="transparent">
            <td class="transparent" width="250"><netui:span value="${bundle.web['selectsettings.label.testAccessCode']}"/></td>
            <td class="transparent"><div class="formValueLarge"><netui-compat:label value="{actionForm.testAdmin.accessCode}" styleClass="formValueLarge"/></div></td>
        </tr>
    </c:if>
</c:if>
    
    
    
<c:if test="${isTabeProduct}"> 
      
    <c:if test="${productType != 'tabeLocatorProductType'}">                     
        <tr class="transparent">
            <td class="transparent" width="250"><netui:span value="Locator Test"/></td>
            <td class="transparent"><div class="formValueLarge"><netui-compat:label value="{actionForm.autoLocatorDisplay}" styleClass="formValueLarge"/></div></td>
        </tr>
    </c:if>
</c:if>


        
<c:if test="${tabeWithSingleSubtest}"> 
        <tr class="transparent">
            <td class="transparent"  width="250" nowrap>
                <netui:span value="${bundle.web['selectsettings.tests.message1']}"/>
            </td> 
            <c:if test="${hasBreak}"> 
                <td class="transparent"><div class="formValueLarge"><netui:span value="${bundle.web['selectsettings.label.yes']}" styleClass="formValueLarge"/></div></td>
            </c:if>
            <c:if test="${! hasBreak}"> 
                <td class="transparent"><div class="formValueLarge"><netui:span value="${bundle.web['selectsettings.label.no']}" styleClass="formValueLarge"/></div></td>
            </c:if>
        </tr>
        <c:if test="${! hasBreak}"> 
            <tr class="transparent">
                <td class="transparent" width="250"><netui:span value="${bundle.web['selectsettings.label.testAccessCode']}"/></td>
                <td class="transparent"><div class="formValueLarge"><netui-compat:label value="${actionForm.testAdmin.accessCode}" styleClass="formValueLarge"/></div></td>
            </tr>    
        </c:if>
</c:if>


  
     
<c:if test="${isTabeLocatorProduct}"> 
        <tr class="transparent">
            <td class="transparent" width="250"><netui:span value="${bundle.web['selectsettings.label.testAccessCode']}"/></td>
            <td class="transparent"><div class="formValueLarge"><netui-compat:label value="{actionForm.testAdmin.accessCode}" styleClass="formValueLarge"/></div></td>
        </tr>    
</c:if>
        
<c:if test="${hasMultipleSubtests}"> 
        <tr class="transparent">
            <td class="transparent"  width="250" nowrap>
                <netui:span value="${bundle.web['selectsettings.tests.message1']}"/>
            </td> 
            <c:if test="${hasBreak}"> 
                <td class="transparent"><div class="formValueLarge"><netui:span value="${bundle.web['selectsettings.label.yes']}" styleClass="formValueLarge"/></div></td>
            </c:if>
            <c:if test="${! hasBreak}"> 
                <td class="transparent"><div class="formValueLarge"><netui:span value="${bundle.web['selectsettings.label.no']}" styleClass="formValueLarge"/></div></td>
            </c:if>
        </tr>
        <c:if test="${! hasBreak}"> 
            <tr class="transparent">
                <td class="transparent" width="250"><netui:span value="${bundle.web['selectsettings.label.testAccessCode']}"/></td>
                <td class="transparent"><div class="formValueLarge"><netui-compat:label value="{actionForm.testAdmin.accessCode}" styleClass="formValueLarge"/></div></td>
            </tr>    
        </c:if>
</c:if>
<!-- Add logic for RD -->
    <netui-compat-data:getData resultId="randomDistractorValue" value="{actionForm.testAdmin.isRandomize}"/>       
<c:if test="${randomDistractorValue != null && randomDistractorValue != '' }">    
          <tr class="transparent">
                <td class="transparent"  width="250" nowrap>
                    <netui:span value="${bundle.web['selecttest.rdOptions.message1']}"/>
                </td>
                <td class="transparent"><div class="formValueLarge"><netui-compat:label value="{actionForm.testAdmin.displayRandomize}" styleClass="formValueLarge"/></div></td> 
               
</c:if> 
          
        
<c:if test="${! isTabeProduct}">                     
    <c:if test="${hasMultipleSubtests}">  
        <tr class="transparent">                    
            <td class="transparent" colspan="2">
                <netui:span value="${bundle.web['selectsettings.subtestDetails.title']}"/>
                <table class="sortable">
                <netui-data:repeater dataSource="pageFlow.defaultSubtests">
                <netui-data:repeaterHeader>
                
                <tr class="sortable">
                    <th class="sortable alignCenter" height="25">&nbsp;<netui:span value="${bundle.web['common.column.sequence']}"/></th>
                    <th class="sortable alignLeft" height="25">&nbsp;<netui:span value="${bundle.web['common.column.subtestName']}"/></th>
                    <th class="sortable alignLeft" height="25">&nbsp;<netui:span value="${bundle.web['common.column.duration']}"/></th>
                    <c:if test="${hasBreak}"> 
                    <th class="sortable alignCenter" height="25">&nbsp;<netui:span value="${bundle.web['common.column.testAccessCode']}"/></th>
                    </c:if>
                    
                </tr>
                </netui-data:repeaterHeader>
                <netui-data:repeaterItem>
                <tr class="sortable">
                    <td class="sortable alignCenter" width="80"><netui:span value="${container.item.sequence}" defaultValue="&nbsp;"/></td>
                    <td class="sortable alignLeft" width="*"><netui:span value="${container.item.subtestName}" defaultValue="&nbsp;"/></td>
                    <td class="sortable alignLeft" width="100"><netui:span value="${container.item.duration}" defaultValue="&nbsp;"/></td>
                    <c:if test="${hasBreak}"> 
                    <td class="sortable alignCenter" width="120"><netui:span value="${container.item.testAccessCode}" defaultValue="&nbsp;"/></td>
                    </c:if>
                </tr>
                </netui-data:repeaterItem>
            
                </netui-data:repeater>
                </table>
                
            </td>
        </tr>    
    </c:if>
</c:if>




<c:if test="${isTabeBatterySurveyProduct}"> 
        <tr class="transparent">
            <td class="transparent" colspan="2">&nbsp;</td>
        </tr>          
    <c:if test="${showLocatorSubtest != null}">                              
        <tr class="transparent">
            <td class="transparent" colspan="2">
            <table class="sortable">
                <tr class="sortable">
                    <th class="sortable alignLeft" height="25">&nbsp;<netui:span value="${bundle.web['common.column.subtestName']}"/></th>
                    <th class="sortable alignLeft" height="25">&nbsp;<netui:span value="${bundle.web['common.column.duration']}"/></th>
            <c:if test="${hasBreak}"> 
                    <th class="sortable alignCenter" height="25">&nbsp;<netui:span value="${bundle.web['common.column.testAccessCode']}"/></th>
            </c:if>                  
                </tr>
                <tr class="sortable">
                    <td class="sortable alignLeft" width="*"><netui-compat:label value="{pageFlow.locatorSubtest.subtestName}" defaultValue="&nbsp;"/></td>
                    <td class="sortable alignLeft" width="100"><netui-compat:label value="{pageFlow.locatorSubtest.duration}" defaultValue="&nbsp;"/></td>
            <c:if test="${hasBreak}"> 
                    <td class="sortable alignCenter" width="120"><netui-compat:label value="{pageFlow.locatorSubtest.testAccessCode}" defaultValue="&nbsp;"/></td>
            </c:if>                  
                </tr>
            </table>
            <br/>
            </td>
        </tr>    
    </c:if>
        
        
            
        <tr class="transparent">                    
            <td class="transparent" colspan="2">
                <netui:span value="${bundle.web['selectsettings.subtestDetails.title']}"/>
                <table class="sortable">
                <netui-data:repeater dataSource="pageFlow.defaultSubtests">
                <netui-data:repeaterHeader>
                
                <tr class="sortable">
                    <th class="sortable alignCenter" height="25">&nbsp;<netui:span value="${bundle.web['common.column.sequence']}"/></th>
                    <th class="sortable alignLeft" height="25">&nbsp;<netui:span value="${bundle.web['common.column.subtestName']}"/></th>
                    <th class="sortable alignLeft" height="25">&nbsp;<netui:span value="${bundle.web['common.column.duration']}"/></th>
                    <c:if test="${hasBreak}"> 
                    <th class="sortable alignCenter" height="25">&nbsp;<netui:span value="${bundle.web['common.column.testAccessCode']}"/></th>
                    </c:if>
                    
                </tr>
                </netui-data:repeaterHeader>
                <netui-data:repeaterItem>
                <tr class="sortable">
                    <td class="sortable alignCenter" width="80"><netui:span value="${container.item.sequence}" defaultValue="&nbsp;"/></td>
                    <td class="sortable alignLeft" width="*"><netui:span value="${container.item.subtestName}" defaultValue="&nbsp;"/></td>
                    <td class="sortable alignLeft" width="100"><netui:span value="${container.item.duration}" defaultValue="&nbsp;"/></td>
                    <c:if test="${hasBreak}"> 
                    <td class="sortable alignCenter" width="120"><netui:span value="${container.item.testAccessCode}" defaultValue="&nbsp;"/></td>
                    </c:if>
                </tr>
                </netui-data:repeaterItem>
            
                </netui-data:repeater>
                </table>
                
            </td>
        </tr>    

        
</c:if>
    
</table>
</td>



<td valign="top" width="5%">
<h3>&nbsp;</h3>
</td>


<!-- reserve for license info -->
<td valign="top" width="20%">
    <netui:content value="&nbsp;"/>   
</td>


</tr>
</table>
        
<br/>

<h3><netui:span value="${bundle.web['selectsettings.options.title']}"/></h3>
<table class="transparent">
    <tr class="transparent">
        <td class="transparent" style="vertical-align: top"><span class="asterisk">*</span>&nbsp;<netui:span value="${bundle.web['selectsettings.label.testSessionName']}"/></td>
        <td class="transparent" colspan="3">
        <c:if test="${action=='view'}">    
            <div class="formValueLarge"><netui-compat:label value="{actionForm.testAdmin.sessionName}" styleClass="formValueLarge"/></div>
            <netui:hidden dataSource="actionForm.testAdmin.sessionName"/>   
        </c:if>    
        <c:if test="${action=='schedule'}">    
            <netui:textBox dataSource="actionForm.testAdmin.sessionName" maxlength="64" style="width:400px;" onKeyPress="return constrainEnterKeyEvent(event);"/>
            <br/><netui:span style="color:#666666;" value="${bundle.web['selectsettings.options.message1']}"/> 
        </c:if>    
        <c:if test="${action=='edit'}">
            <ctb:auth roles="root, Account Manager, Administrator, Administrative Coordinator, Coordinator">
                <c:if test="${!testSessionExpired && !hasStudentLoggedIn}">
                    <netui:textBox dataSource="actionForm.testAdmin.sessionName" maxlength="64" style="width:400px;" onKeyPress="return constrainEnterKeyEvent(event);"/>
                    <br/><netui:span style="color:#666666;" value="${bundle.web['selectsettings.options.message1']}"/> 
                </c:if>    
                <c:if test="${testSessionExpired || hasStudentLoggedIn}">
                    <div class="formValueLarge"><netui:span value="${actionForm.testAdmin.sessionName}" styleClass="formValueLarge"/></div>
                    <netui:hidden dataSource="actionForm.testAdmin.sessionName"/>   
                </c:if>    
            </ctb:auth>                
            <ctb:auth roles="Proctor">
                <div class="formValueLarge"><netui:span value="${actionForm.testAdmin.sessionName}" styleClass="formValueLarge"/></div>
                <netui:hidden dataSource="actionForm.testAdmin.sessionName"/>   
            </ctb:auth>                
        </c:if> 
        <br/>   
        </td>
    </tr>
    <tr class="transparent">
        <td class="transparent" style="vertical-align: top"><span class="asterisk">*</span>&nbsp;<netui:span value="${bundle.web['selectsettings.label.startDate']}"/></td>
        <td class="transparent">
        <c:if test="${action=='view'}">    
            <div class="formValue"><netui-compat:label value="{actionForm.startDate}" styleClass="formValue"/></div>
            <netui:hidden dataSource="actionForm.startDate"/>   
        </c:if>    
        <c:if test="${action=='schedule'}">    
            <netui:textBox tagId="startDate" dataSource="actionForm.startDate" maxlength="8" styleClass="textFieldDate" onKeyPress="return constrainEnterKeyEvent(event);"/>
            <a href="#" onclick="showCalendar(document.getElementById('startDate'), document.getElementById('overrideStartDate')); return false;"><img src="<%=request.getContextPath()%>/resources/images/calendar/show_calendar.gif" border="0" width="24" height="22" ></a>
        </c:if>    
        <c:if test="${action=='edit'}">    
            <ctb:auth roles="root, Account Manager, Administrator, Administrative Coordinator, Coordinator">
                <c:if test="${!hasStudentLoggedIn && !testSessionExpired}">
                    <netui:textBox tagId="startDate" dataSource="actionForm.startDate" maxlength="8" styleClass="textFieldDate" onKeyPress="return constrainEnterKeyEvent(event);"/>
                    <a href="#" onclick="showCalendar(document.getElementById('startDate'), document.getElementById('overrideStartDate'); return false;"><img src="<%=request.getContextPath()%>/resources/images/calendar/show_calendar.gif" border="0" width="24" height="22" ></a>
                </c:if>    
                <c:if test="${hasStudentLoggedIn || testSessionExpired}">
                    <div class="formValue"><netui:span value="${actionForm.startDate}" styleClass="formValue"/></div>
                    <netui:hidden dataSource="actionForm.startDate"/>   
                </c:if>    
            </ctb:auth>                
            <ctb:auth roles="Proctor">
                <div class="formValue"><netui:span value="${actionForm.startDate}" styleClass="formValue"/></div>
                <netui:hidden dataSource="actionForm.startDate"/>   
            </ctb:auth>                
        </c:if> 
        <br/>
        <netui:span value="${bundle.web['selectsettings.label.dateFormat']}" style="color:#666666;"/>
        <p/>
        </td>   
        <td class="transparent" style="vertical-align: top"><span class="asterisk">*</span>&nbsp;<netui:span value="${bundle.web['selectsettings.label.endDate']}"/></td>
        <td class="transparent">
        <c:if test="${action=='view'}">    
            <div class="formValue"><netui-compat:label value="{actionForm.endDate}" styleClass="formValue"/></div>
            <netui:hidden dataSource="actionForm.endDate"/>   
        </c:if>    
        <c:if test="${action=='schedule'}">    
            <netui:textBox tagId="endDate" dataSource="actionForm.endDate" maxlength="8" styleClass="textFieldDate" onKeyPress="return constrainEnterKeyEvent(event);"/>
            <a href="#" onclick="showCalendar(document.getElementById('endDate'), document.getElementById('overrideStartDate')); return false;"><img src="<%=request.getContextPath()%>/resources/images/calendar/show_calendar.gif" border="0" height="22" width="24" ></a>
        </c:if>    
        <c:if test="${action=='edit'}">
            <ctb:auth roles="root, Account Manager, Administrator, Administrative Coordinator, Coordinator">
                <netui:textBox tagId="endDate" dataSource="actionForm.endDate" maxlength="8" styleClass="textFieldDate" onKeyPress="return constrainEnterKeyEvent(event);"/>
                <a href="#" onclick="showCalendar(document.getElementById('endDate'), document.getElementById('overrideStartDate')); return false;"><img src="<%=request.getContextPath()%>/resources/images/calendar/show_calendar.gif" border="0" width="24" height="22" ></a>
            </ctb:auth>                
            <ctb:auth roles="Proctor">
                <div class="formValue"><netui:span value="${actionForm.endDate}" styleClass="formValue"/></div>
                <netui:hidden dataSource="actionForm.endDate"/>   
            </ctb:auth>                
        </c:if>
        <br/>
        <netui:span value="${bundle.web['selectsettings.label.dateFormat']}" style="color:#666666;"/>
        <p/>
        </td>    
    </tr>
    <tr class="transparent">
        <td class="transparent" style="vertical-align: top"><span class="asterisk">*</span>&nbsp;<netui:span value="${bundle.web['selectsettings.label.startTime']}"/></td>
        <td class="transparent">
        <c:if test="${action=='view'}">    
            <div class="formValue"><netui-compat:label value="{actionForm.startTime}" styleClass="formValue"/></div>
            <netui:hidden dataSource="actionForm.startTime"/>   
        </c:if>    
        <c:if test="${action=='schedule'}">    
            <netui:select dataSource="actionForm.startTime" optionsDataSource="${pageFlow.timeList}" size="1" multiple="false">
            </netui:select>
        </c:if>    
        <c:if test="${action=='edit'}">
            <ctb:auth roles="root, Account Manager, Administrator, Administrative Coordinator, Coordinator">
                <c:if test="${!testSessionExpired}">
                    <netui:select dataSource="actionForm.startTime" optionsDataSource="${pageFlow.timeList}" size="1" multiple="false">
                    </netui:select>
                </c:if>    
                <c:if test="${testSessionExpired}">
                    <div class="formValue"><netui:span value="${actionForm.startTime}" styleClass="formValue"/></div>
                    <netui:hidden dataSource="actionForm.startTime"/>   
                </c:if>    
            </ctb:auth>                
            <ctb:auth roles="Proctor">
                <div class="formValue"><netui:span value="${actionForm.startTime}" styleClass="formValue"/></div>
                <netui:hidden dataSource="actionForm.startTime"/>   
            </ctb:auth>                
        </c:if>  
        <p/>  
        </td>
        <td class="transparent" style="vertical-align: top"><span class="asterisk">*</span>&nbsp;<netui:span value="${bundle.web['selectsettings.label.endTime']}"/></td>
        <td class="transparent">
        <c:if test="${action=='view'}">    
            <div class="formValue"><netui-compat:label value="{actionForm.endTime}" styleClass="formValue"/></div>
            <netui:hidden dataSource="actionForm.endTime"/>   
        </c:if>    
        <c:if test="${action=='schedule'}">    
            <netui:select dataSource="actionForm.endTime" optionsDataSource="${pageFlow.timeList}" size="1" multiple="false">
            </netui:select>
        </c:if>    
        <c:if test="${action=='edit'}">
            <ctb:auth roles="root, Account Manager, Administrator, Administrative Coordinator, Coordinator">
                <c:if test="${!testSessionExpired}">
                    <netui:select dataSource="actionForm.endTime" optionsDataSource="${pageFlow.timeList}" size="1" multiple="false">
                    </netui:select>
                </c:if>    
                <c:if test="${testSessionExpired}">
                    <div class="formValue"><netui:span value="${actionForm.endTime}" styleClass="formValue"/></div>
                    <netui:hidden dataSource="actionForm.endTime"/>   
                </c:if>    
            </ctb:auth>                
            <ctb:auth roles="Proctor">
                <div class="formValue"><netui:span value="${actionForm.endTime}" styleClass="formValue"/></div>
                <netui:hidden dataSource="actionForm.endTime"/>   
            </ctb:auth>                
        </c:if>   
        <p/> 
        </td>
    </tr>
    <tr class="transparent">
        <td class="transparent" style="vertical-align: top"><netui:span value="${bundle.web['selectsettings.label.timeZone']}"/></td>
        <td class="transparent" colspan="3">
        <c:if test="${action=='view'}">    
            <div class="formValue"><netui-compat:label value="{actionForm.testAdmin.timeZone}" styleClass="formValue"/></div>
            <netui:hidden dataSource="actionForm.testAdmin.timeZone"/>   
        </c:if>    
        <c:if test="${action=='schedule'}">    
            <netui:select dataSource="actionForm.testAdmin.timeZone" optionsDataSource="${pageFlow.timeZoneList}" size="1" multiple="false">
            </netui:select>
        </c:if>    
        <c:if test="${action=='edit'}">
            <ctb:auth roles="root, Account Manager, Administrator, Administrative Coordinator, Coordinator">
                <c:if test="${!testSessionExpired}">
                    <netui:select dataSource="actionForm.testAdmin.timeZone" optionsDataSource="${pageFlow.timeZoneList}" size="1" multiple="false">
                    </netui:select>
                </c:if>    
                <c:if test="${testSessionExpired}">
                    <div class="formValue"><netui:span value="${actionForm.testAdmin.timeZone}" styleClass="formValue"/></div>
                    <netui:hidden dataSource="actionForm.testAdmin.timeZone"/>   
                </c:if>    
            </ctb:auth>                
            <ctb:auth roles="Proctor">
                <div class="formValue"><netui:span value="${actionForm.testAdmin.timeZone}" styleClass="formValue"/></div>
                <netui:hidden dataSource="actionForm.testAdmin.timeZone"/>   
            </ctb:auth>                
        </c:if>   
        <p/>          
        </td>
    </tr>
    <tr class="transparent">
        <td class="transparent" style="vertical-align: top">
        <c:if test="${action!='view' || !hideTestLocation}">
            <netui:span value="${bundle.web['selectsettings.label.testLocation']}"/>
        </c:if> 
        </td>
        <td class="transparent" colspan="3">
        <c:if test="${action=='view'}"> 
            <netui-data:getData resultId="hideTestLocation" value="${requestScope.hideTestLocation}"/>
            <c:if test="${!hideTestLocation}">            
                <div class="formValue"><netui-compat:label value="{actionForm.testAdmin.location}" styleClass="formValue"/></div>
                <netui:hidden dataSource="actionForm.testAdmin.location"/>   
            </c:if>    
        </c:if>    
        <c:if test="${action=='schedule'}">    
            <netui:textBox dataSource="actionForm.testAdmin.location" maxlength="64" onKeyPress="return constrainEnterKeyEvent(event);"/>
            <br/><netui:span value="(Computer Lab, Room 101)" style="color:#666666;"/>
        </c:if>    
        <c:if test="${action=='edit'}">
            <ctb:auth roles="root, Account Manager, Administrator, Administrative Coordinator, Coordinator">
                <c:if test="${!testSessionExpired}">
                    <netui:textBox dataSource="actionForm.testAdmin.location" maxlength="64" onKeyPress="return constrainEnterKeyEvent(event);"/>
                    <br/><netui:span value="(Computer Lab, Room 101)" style="color:#666666;"/>
                </c:if>    
                <c:if test="${testSessionExpired}">
                    <div class="formValue"><netui:span value="${actionForm.testAdmin.location}" styleClass="formValue"/></div>
                    <netui:hidden dataSource="actionForm.testAdmin.location"/>   
                </c:if>    
            </ctb:auth>                
            <ctb:auth roles="Proctor">
                <div class="formValue"><netui:span value="${actionForm.testAdmin.location}" styleClass="formValue"/></div>
                <netui:hidden dataSource="actionForm.testAdmin.location"/>   
            </ctb:auth>                
        </c:if>    
        </td>
    </tr>
    
<c:if test="${action=='view'}">    
    <tr class="transparent">
        <td class="transparent" colspan="4">
        <netui:span value="${bundle.web['selectsettings.label.selectOrganization']}"/>
        <br/>
        <div class="formValue"><netui-compat:label value="{actionForm.creatorOrgNodeName}" styleClass="formValue"/></div>
        </td>
    </tr>
    <netui:hidden dataSource="actionForm.creatorOrgNodeId"/>   
</c:if>    
<c:if test="${action=='schedule' || action=='edit'}"> 
    <c:if test="${showSelectOrganization == 'editable'}">     
        <tr class="transparent">
            <td class="transparent" colspan="4">
            <netui:span value="${bundle.web['selectsettings.label.selectOrganization']}"/>
            <br/>
                <netui:select dataSource="actionForm.creatorOrgNodeId" optionsDataSource="${pageFlow.topNodesMap}" size="1" multiple="false">
                </netui:select>
            </td>
        </tr>
    </c:if>    
    <c:if test="${showSelectOrganization=='noneditable'}">     
        <tr class="transparent">
            <td class="transparent" colspan="4">
            <netui:span value="${bundle.web['selectsettings.label.selectOrganization']}"/>
            <br/>
            <div class="formValue"><netui:span value="${actionForm.creatorOrgNodeName}" styleClass="formValue"/></div>
            </td>
        </tr>
        <netui:hidden dataSource="actionForm.creatorOrgNodeId"/>   
    </c:if>    
    <c:if test="${showSelectOrganization=='hidden'}">     
        <netui:hidden dataSource="actionForm.creatorOrgNodeId"/>   
    </c:if>    
</c:if>    

</table>
<br/>
