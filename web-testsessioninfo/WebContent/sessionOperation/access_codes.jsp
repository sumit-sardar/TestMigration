<%@ page import="java.io.*, java.util.*"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 
<%
	List accessCodes = (List) session.getAttribute("accessCodes");
	System.out.println(accessCodes);		
%>

<table class="simpletable" width="100%">
<netui-data:repeater dataSource="sessionScope.accessCodes">
    <netui-data:repeaterHeader>
       <tr class="tableHeader">
                <th width="70%" style="padding-left:5px;" align="left">
                    <div class="notCurrentSort"><span>Subtest Name</span></div>
                </th>
                <th width="30%" style="padding-left:5px;" align="left">
                    <div class="notCurrentSort"><span>Access Code</span></div>
                </th>
        </tr>
    </netui-data:repeaterHeader>
    <netui-data:repeaterItem>
        <tr class="tableHeader">
            <td class="simpletable">
            	<netui:content value="${container.item.itemSetName}" defaultValue="&nbsp;"/>
            </td>
            <td class="simpletable">
            	<netui:content value="${container.item.accessCode}" defaultValue="&nbsp;"/>
            </td>
        </tr>
    </netui-data:repeaterItem>
</netui-data:repeater>
</table>
