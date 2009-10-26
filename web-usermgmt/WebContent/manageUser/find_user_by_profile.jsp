<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>


<table class="sortable">
    <tr class="sortable">
        <td class="sortableControls">
<br/>        
<table class="tableFilter">
    <tr class="tableFilter">
        <td class="tableFilter" width="100" align="right">First Name:</td>
        <td class="tableFilter" width="200"><netui:textBox tagId="firstName" dataSource="actionForm.userProfile.firstName" style="width:180px" tabindex="1" maxlength="32"/></td>
        <td class="tableFilter" width="100" align="right">Role:</td>
        <td class="tableFilter" width="*">
            <netui:select dataSource="actionForm.userProfile.role" optionsDataSource="${pageFlow.roleOptions}" defaultValue="${actionForm.userProfile.role}" size="1" style="width:180px" tabindex="4"/>
        </td>
    </tr>
    <tr class="tableFilter">
        <td class="tableFilter" width="100" align="right">Last Name:</td>
        <td class="tableFilter" width="200"><netui:textBox tagId="lastName" dataSource="actionForm.userProfile.lastName" style="width:180px" tabindex="2" maxlength="32"/></td>
        <td class="tableFilter" width="100" align="right">Email:</td>
        <td class="tableFilter" width="*"><netui:textBox tagId="email" dataSource="actionForm.userProfile.email" style="width:180px" tabindex="5" maxlength="64"/></td>
        
    </tr>
    <tr class="tableFilter">
        <td class="tableFilter" width="100" align="right">Login ID:</td>
        <td class="tableFilter" width="200"><netui:textBox tagId="loginId" dataSource="actionForm.userProfile.loginId" style="width:180px" tabindex="3" maxlength="32"/></td>
        <td class="tableFilter" width="100" align="right">&nbsp;</td>
        <td class="tableFilter" width="*">
            <netui:button styleClass="button" value="Search" type="submit" onClick="setElementValueAndSubmitWithAnchor('{actionForm.currentAction}', 'applySearch', 'userProfileResult');" tabindex="6"/>
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <netui:button styleClass="button" value="Clear All" type="submit" onClick="setElementValueAndSubmit('{actionForm.currentAction}', 'clearSearch');" tabindex="7"/>&nbsp;
        </td>
    </tr>
</table>    
<br/>
        </td>
    </tr>
</table>



        
