<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>

<%  
	String isUsrAcctMgr = null;
	String hasExtSchoolIdConfigurable = null;
	if( null!= session.getAttribute("isUsrAcctMgr") && null != request.getAttribute("hasExtSchoolIdConfigurable")) {
		isUsrAcctMgr = (String)session.getAttribute("isUsrAcctMgr").toString();
		hasExtSchoolIdConfigurable = (String)request.getAttribute("hasExtSchoolIdConfigurable").toString();
	}
%>

<table class="simple">
    <tr class="transparent">

<!-- column 1 -->
<td class="transparent-top" width="50%">
<table class="transparent">
    <tr class="transparent">
        <td class="transparent" width="100"><netui:content value="First Name:"/></td>
        <td class="transparent"><netui:label value="${userProfileData.firstName}"/></td>
    </tr>
    <tr class="transparent">
        <td class="transparent" width="100"><netui:content value="Middle Name:"/></td>
        <td class="transparent"><netui:label value="${userProfileData.middleName}"/></td>
    </tr>
    <tr class="transparent">
        <td class="transparent" width="100"><netui:content value="Last Name:"/></td>
        <td class="transparent"><netui:label value="${userProfileData.lastName}"/></td>
    </tr>
    <tr class="transparent">
        <td class="transparent" width="100"><netui:content value="Login ID:"/></td>
        <td class="transparent"><netui:label value="${userProfileData.loginId}"/></td>
    </tr>
    <%--CR Dex --%>
    <tr class="transparent">
        <td class="transparent-top" width="100"><netui:content value="Organization:"/></td>
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




<!-- column 2 -->
<td class="transparent-top" width="50%">
<table class="transparent">
    <tr class="transparent">
        <td class="transparent" width="100"><netui:content value="Email:"/></td>
        <td class="transparent"><netui:label value="${userProfileData.email}"/></td>
    </tr>
    <tr class="transparent">
        <td class="transparent" width="100"><netui:content value="Time Zone:"/></td>
        <td class="transparent"><netui:label value="${userProfileData.timeZoneDesc}"/></td>
    </tr>
    <tr class="transparent">
        <td class="transparent" width="100"><netui:content value="Role:"/></td>
        <td class="transparent"><netui:label value="${userProfileData.roleInitCap}"/></td>
    </tr>
     <%--CR Dex --%>
    <tr class="transparent">
        <td class="transparent" width="100" nowrap><netui:content value="External User Id:"/></td>
        <td class="transparent"><netui:label value="${userProfileData.extPin1}"/></td>
    </tr>
    <%-- LAUSD Story for External School Id --%>
    <c:if test="${ null != isUsrAcctMgr && isUsrAcctMgr == 'true' && null != hasExtSchoolIdConfigurable && hasExtSchoolIdConfigurable == 'true' }">
	    <tr class="transparent">
	        <td class="transparent" width="110" nowrap><netui:content value="External School ID:"/></td>
	        <td class="transparent"><netui:label value="${userProfileData.extSchoolId}"/></td>
	    </tr>
    </c:if>
</table>
</td>


</tr>
</table>

<br/>
