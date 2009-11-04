<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>
<netui-data:declareBundle bundlePath="helpResources" name="help"/>




<table class="simple">
    <tr class="transparent">

<!-- column 1 -->
<td class="transparent-top" width="310" valign="top">
<table class="transparent">
    <tr class="transparent">
        <td class="transparent alignRight" width="110"><netui:content value="Customer Name:"/></td>
        <td class="transparent"><netui:label value="${customerProfileData.name}"/></td>
    </tr>   
    <tr class="transparent">
        <td class="transparent alignRight" width="110"><netui:content value="Product Name:"/></td>
        <td class="transparent" nowrap><netui:label value="${licneseNodeData.productName}"/></td>
    </tr>    
</table>
</td>

<!-- column 2 -->
<td class="transparent-top" width="*" valign="top">
<table class="transparent">
    <tr class="transparent">
        <td class="transparent alignRight" width="150"><span class="asterisk">*</span>&nbsp;<netui:content value="Available:"/></td>
        <td class="transparent">
            <netui:textBox tagId="available" dataSource="actionForm.licenseNode.available" maxlength="9" style="width:100px" onKeyPress="return constrainNumericChar(event);"/>
        </td>
    </tr>
    <tr class="transparent">
        <td class="transparent alignRight" width="150"><netui:content value="Scheduled:"/></td>
        <td class="transparent" nowrap><netui:label value="${licneseNodeData.reserved}"/></td>
    </tr>
    <tr class="transparent">
        <td class="transparent alignRight" width="150" nowrap><netui:content value="Consumed:"/></td>
        <td class="transparent" nowrap>
            <netui:textBox tagId="consumed" dataSource="actionForm.licenseNode.consumed" maxlength="9" style="width:100px" onKeyPress="return constrainNumericChar(event);"/>
        </td>
    </tr>
</table>
</td>


</tr>
</table>


<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->

