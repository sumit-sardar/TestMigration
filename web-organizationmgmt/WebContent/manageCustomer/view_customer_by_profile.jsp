<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

 
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>



<table class="simple">
    <tr class="transparent">

<!-- column 1 -->
<td class="transparent-top" width="50%">
<table class="transparent">
    <tr class="transparent">
        <td class="transparent" width="100" nowrap><netui:content value="Customer Type:"/></td>
        <td class="transparent"><netui:label value="${customerProfileData.customerType}"/></td>
    </tr>
    <tr class="transparent">
        <td class="transparent" width="100" nowrap><netui:content value="Customer Name:"/></td>
        <td class="transparent"><netui:label value="${customerProfileData.name}"/></td>
    </tr>
    <tr class="transparent">
        <td class="transparent" width="100" nowrap><netui:content value="Customer ID:"/></td>
        <td class="transparent"><netui:label value="${customerProfileData.code}"/></td>
    </tr>
    <tr class="transparent">
        <td class="transparent" width="100" nowrap><netui:content value="State:"/></td>
        <td class="transparent"><netui:label value="${customerProfileData.state}"/></td>
    </tr>
     <tr class="transparent">
        <td class="transparent" width="100" nowrap><netui:content value="MDR Number:"/></td>
        <td class="transparent"><netui:label value="${customerProfileData.mdrNumber}"/></td>
    </tr>
</table>
</td>




<!-- column 2 -->
<td class="transparent-top" width="50%">
<table class="transparent">
    <tr class="transparent">
        <td class="transparent" width="180" nowrap><netui:content value="CTB Contact:"/></td>
        <td class="transparent"><netui:label value="${customerProfileData.ctbContact}"/></td>
    </tr>
    <tr class="transparent">
        <td class="transparent" width="180" nowrap><netui:content value="CTB Contact Email:"/></td>
        <td class="transparent"><netui:label value="${customerProfileData.ctbContactEmail}"/></td>
    </tr>
    <tr class="transparent">
        <td class="transparent" width="180" nowrap><netui:content value="Customer Contact:"/></td>
        <td class="transparent"><netui:label value="${customerProfileData.customerContact}"/></td>
    </tr>
     <tr class="transparent">
        <td class="transparent" width="180" nowrap><netui:content value="Customer Contact Email:"/></td>
        <td class="transparent"><netui:label value="${customerProfileData.customerContactEmail}"/></td>
    </tr>
    <tr class="transparent">
        <td class="transparent" width="180" nowrap><netui:content value="Customer Contact Phone:"/></td>
        <td class="transparent"><netui:label value="${customerProfileData.conatctPhone}"/></td>
    </tr>
</table>
</td>


</tr>
</table>

<br/>
