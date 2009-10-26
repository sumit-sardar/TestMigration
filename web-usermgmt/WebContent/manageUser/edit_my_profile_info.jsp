<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="dto.PathNode"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="netui-tags-databinding.tld" prefix="netui-compat-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui-compat"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-compat-template"%>
 
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>


<table class="simple">
    <tr class="transparent">

<!-- column 1 -->
<td class="transparent-top" width="310" valign="top">
<table class="transparent">
    <tr class="transparent">
        <td class="transparent alignRight" width="110"><span class="asterisk">*</span>&nbsp;<netui:content value="First Name:"/></td>
        <td class="transparent">
            <netui:textBox tagId="userFirstName" dataSource="actionForm.userProfile.firstName" maxlength="32" style="width:200px" tabindex="0"/>
        </td>
    </tr>
    <tr class="transparent">
        <td class="transparent alignRight" width="110"><netui:content value="Middle Name:"/></td>
        <td class="transparent">
            <netui:textBox dataSource="actionForm.userProfile.middleName" maxlength="32" style="width:200px"/>
        </td>
    </tr>
    <tr class="transparent">
        <td class="transparent alignRight" width="110"><span class="asterisk">*</span>&nbsp;<netui:content value="Last Name:"/></td>
        <td class="transparent">
            <netui:textBox tagId="userLastName" dataSource="actionForm.userProfile.lastName" maxlength="32" style="width:200px"/>
        </td>
    </tr>
    <tr class="transparent">
        <td class="transparent alignRight" width="110"><netui:content value="Login ID:"/></td>
        <td class="transparent"><netui-compat:label value="{actionForm.userProfile.loginId}"/></td>
    </tr>
    <%--CR Dex --%>
    <tr class="transparent">
        <td class="transparent-top alignRight" width="110"><netui:content value="Organization:"/></td>
        <td class="transparent-top">
            <table class="transparent">
            <netui-data:repeater dataSource="actionForm.userProfile.organizationNodes">
                <netui-data:repeaterItem>            
                <tr class="transparent">
                    <td class="transparent-small"><netui:content value="${container.item.orgNodeName}"/></td>
                </tr>
                </netui-data:repeaterItem>
            </netui-data:repeater>
            </table>        
        </td>
    </tr>
</table>
</td>




<!-- column 2 -->
<td class="transparent-top" width="*" valign="top">
<table class="transparent">
    <tr class="transparent">
        <td class="transparent alignRight" width="110"><netui:content value="Email:"/></td>
        <td class="transparent">
            <netui:textBox tagId="userEmail" dataSource="actionForm.userProfile.email" maxlength="64" style="width:200px"/>
        </td>
    </tr>
    <tr class="transparent">
        <td class="transparent alignRight" width="110"><span class="asterisk">*</span>&nbsp;<netui:content value="Time Zone:"/></td>
        <td class="transparent">
            <netui:select optionsDataSource="${pageFlow.timeZoneOptions}" dataSource="actionForm.userProfile.timeZone" size="1" style="width:200px"/>
        </td>                                
    </tr>
    <tr class="transparent">
        <td class="transparent alignRight" width="110"><netui:content value="Role:"/></td>
        <td class="transparent">
        <netui-compat:label value=""/>
        <!--netui:label value="{actionForm.userProfile.role}"/-->
        <!--netui-data:getData resultId="userRole" value="{actionForm.userProfile.role}" /-->
        <%--String role = (String)request.getSession().getAttribute("userRole");--%>
        <netui-compat:label value="{actionForm.userProfile.role}"/>
        </td>
    </tr>
    <%--CR Dex --%>
    <tr class="transparent">
        <td class="transparent alignRight" width="110" nowrap><netui:content value="External User Id:"/></td>
        <td class="transparent"><netui-compat:label value="{actionForm.userProfile.extPin1}"/></td>
    </tr>
</table>
</td>


</tr>
</table>



<br/>
