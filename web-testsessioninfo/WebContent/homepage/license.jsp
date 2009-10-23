<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>


<table class="simple">
    <netui-data:repeater dataSource="requestScope.customerLicenses">
        <netui-data:repeaterItem>
            <tr class="simple">
                <th class="simple"colspan="3" align="left">
                    <netui:content value="${container.item.productName}" defaultValue="&nbsp;"/>
                </th>
            </tr>
            <tr class="transparent">    
                <td class="transparent-medium">&nbsp;</td>
                <td class="transparent-medium"><netui:content value="${bundle.web['licenses.reserved']}"/>:</td>
                <td class="transparent-medium"><netui:content value="${container.item.reservedLicense}" defaultValue="&nbsp;"/></td>
            </tr>
            <tr class="transparent">    
                <td class="transparent-medium">&nbsp;</td>
                <td class="transparent-medium"><netui:content value="${bundle.web['licenses.consumed']}"/>:</td>
                <td class="transparent-medium"><netui:content value="${container.item.consumedLicense}" defaultValue="&nbsp;"/></td>
            </tr>
            <tr class="transparent">    
                <td class="transparent-medium">&nbsp;</td>
                <td class="transparent-medium"><netui:content value="${bundle.web['licenses.available']}"/>:</td>
                <td class="transparent-medium"><netui:content value="${container.item.available}" defaultValue="&nbsp;"/></td>
            </tr>
        </netui-data:repeaterItem>
    </netui-data:repeater>
</table>
