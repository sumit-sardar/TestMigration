<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui-compat"%>
 
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>



<table class="simple">
    <tr class="transparent">

<!-- column 1 -->
<td class="transparent-top" width="50%">
<table class="transparent">
    <tr class="transparent">
        <td class="transparent" width="80"><netui:content value="First Name:"/></td>
        <td class="transparent"><netui-compat:label value="{actionForm.studentProfile.firstName}"/></td>
    </tr>
    <tr class="transparent">
        <td class="transparent" width="80"><netui:content value="Middle Name:"/></td>
        <td class="transparent"><netui-compat:label value="{actionForm.studentProfile.middleName}"/></td>
    </tr>
    <tr class="transparent">
        <td class="transparent" width="80"><netui:content value="Last Name:"/></td>
        <td class="transparent"><netui-compat:label value="{actionForm.studentProfile.lastName}"/></td>
    </tr>
    <tr class="transparent">
        <td class="transparent" width="80"><netui:content value="Login ID:"/></td>
        <td class="transparent"><netui-compat:label value="{actionForm.studentProfile.userName}"/></td>
    </tr>
    <tr class="transparent">
        <td class="transparent" width="80"><netui:content value="Date of Birth:"/></td>
        <td class="transparent"><netui-compat:label value="{actionForm.studentProfile.birthdateString}"/></td>
    </tr>
</table>

</td>





<!-- column 2 -->
<td class="transparent-top" width="50%">
<table class="transparent">
    <tr class="transparent">
        <td class="transparent" width="80"><netui:content value="Grade:"/></td>
        <td class="transparent"><netui-compat:label value="{actionForm.studentProfile.grade}"/></td>
    </tr>
    <tr class="transparent">
        <td class="transparent" width="80"><netui:content value="Gender:"/></td>
        <td class="transparent"><netui-compat:label value="{actionForm.studentProfile.gender}"/></td>
    </tr>
    <tr class="transparent">
        <td class="transparent" width="80"><netui:content value="Student ID:"/></td>
        <td class="transparent"><netui-compat:label value="{actionForm.studentProfile.studentNumber}"/></td>
    </tr>
    <tr class="transparent">
        <td class="transparent-top" width="80"><netui:content value="Organization:"/></td>
        <td class="transparent-top">
            <table class="transparent">
            <netui-data:repeater dataSource="requestScope.organizationNodes">
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
</tr>
</table>

<br/>
