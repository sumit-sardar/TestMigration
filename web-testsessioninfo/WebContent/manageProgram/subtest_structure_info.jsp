<%@ page import="java.io.*, java.util.*"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>

<a name="subtestTableAnchor"><!-- subtestTableAnchor --></a>    

<table class="sortable">
        <tr class="sortable">
            <th class="sortable alignLeft" height="25" width="*">&nbsp;&nbsp;<span><netui:content value="${bundle.web['common.column.test']}" defaultValue="&nbsp;"/></span></th>
            <th class="sortable alignCenter" height="25" width="10%">&nbsp;&nbsp;<span><netui:content value="${bundle.web['common.column.scheduled']}" defaultValue="&nbsp;"/></span></th>
            <th class="sortable alignCenter" height="25" width="10%">&nbsp;&nbsp;<span><netui:content value="${bundle.web['common.column.attempted']}" defaultValue="&nbsp;"/></span></th>
            <th class="sortable alignCenter" height="25" width="10%">&nbsp;&nbsp;<span><netui:content value="${bundle.web['common.column.completed']}" defaultValue="&nbsp;"/></span></th>
        </tr>
        <tr class="sortable">
            <td class="sortable alignLeft"><netui:content value="${requestScope.testStatus.name}" defaultValue="&nbsp;"/></td>
            <td class="sortable alignCenter"><netui:content value="${requestScope.testStatus.scheduled}" defaultValue="&nbsp;"/></td>
            <td class="sortable alignCenter"><netui:content value="${requestScope.testStatus.attempted}" defaultValue="&nbsp;"/></td>
            <td class="sortable alignCenter"><netui:content value="${requestScope.testStatus.completed}" defaultValue="&nbsp;"/></td>
        </tr>
<netui-data:repeater dataSource="requestScope.subtestList">
    <netui-data:repeaterHeader>
        <tr class="sortable">
            <th class="sortable alignLeft" height="25">&nbsp;&nbsp;<span><netui:content value="${bundle.web['common.column.subtestBreakdown']}" defaultValue="&nbsp;"/></span></th>
            <th class="sortable alignCenter">&nbsp;&nbsp;<span><netui:content value="&nbsp;"/></span></th>
            <th class="sortable alignCenter">&nbsp;&nbsp;<span><netui:content value="&nbsp;"/></span></th>
            <th class="sortable alignCenter">&nbsp;&nbsp;<span><netui:content value="&nbsp;"/></span></th>
        </tr>
    </netui-data:repeaterHeader>
    
    <netui-data:repeaterItem>
    
        <netui-data:getData resultId="isParent" value="${container.item.isParent}"/>  
        <netui-data:getData resultId="hasCompletedLink" value="${container.item.hasCompletedLink}"/>  
        <netui-data:getData resultId="hasScheduledLink" value="${container.item.hasScheduledLink}"/>  
        <netui-data:getData resultId="hasAttemptedLink" value="${container.item.hasAttemptedLink}"/>  
        <netui-data:getData resultId="subtestName" value="${container.item.name}"/>  
        <netui-data:getData resultId="subtestId" value="${container.item.id}"/>  
        <% 
            Boolean isParent = (Boolean)pageContext.getAttribute("isParent"); 
            String tdClass = "sortable";
            if (isParent.booleanValue()) {
                tdClass = "sortableGrey";
            }            
            Boolean hasScheduledLink = (Boolean)pageContext.getAttribute("hasScheduledLink");
            Boolean hasCompletedLink = (Boolean)pageContext.getAttribute("hasCompletedLink");
            Boolean hasAttemptedLink = (Boolean)pageContext.getAttribute("hasAttemptedLink");
            String subtestName = (String)pageContext.getAttribute("subtestName"); 
            String subtestId = (String)pageContext.getAttribute("subtestId"); 
        %> 
        
        <tr class="sortable">
            <td class="<%= tdClass %>">
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<netui:span value="${container.item.name}" defaultValue="&nbsp;"/>
            </td>
            <td class="<%= tdClass %>" align="center">
<c:if test="${hasScheduledLink}"> 
        <c:if test="${viewSubtestStatus == subtestName && currentStatus == 'Scheduled'}">                 
                    <font color="red"><netui:content value="${container.item.scheduled}" defaultValue="&nbsp;"/></font>
        </c:if>            
        <c:if test="${viewSubtestStatus != subtestName || currentStatus != 'Scheduled'}">                 
                <a href="#" onclick="return submitToAction('viewSubtestStatus.do#subtestTableAnchor', '<%= subtestId %>', 'Scheduled');">             
                    <netui:content value="${container.item.scheduled}" defaultValue="&nbsp;"/>
                </a>
        </c:if>    
</c:if>
<c:if test="${!hasScheduledLink}"> 
        <netui:content value="${container.item.scheduled}" defaultValue="&nbsp;"/>
</c:if>   
            </td>
            <td class="<%= tdClass %>" align="center">
<c:if test="${hasAttemptedLink}"> 
        <c:if test="${viewSubtestStatus == subtestName && currentStatus == 'Started'}">                 
                    <font color="red"><netui:content value="${container.item.attempted}" defaultValue="&nbsp;"/></font>
        </c:if>            
        <c:if test="${viewSubtestStatus != subtestName || currentStatus != 'Started'}">                             
                <a href="#" onclick="return submitToAction('viewSubtestStatus.do#subtestTableAnchor', '<%= subtestId %>', 'Started');">             
                    <netui:content value="${container.item.attempted}" defaultValue="&nbsp;"/>
                </a>
        </c:if>     
</c:if>       
<c:if test="${!hasAttemptedLink}"> 
        <netui:content value="${container.item.attempted}" defaultValue="&nbsp;"/>
</c:if>   
            </td>
            <td class="<%= tdClass %>" align="center">
<c:if test="${hasCompletedLink}"> 
        <c:if test="${viewSubtestStatus == subtestName && currentStatus == 'Completed'}">                 
                    <font color="red"><netui:content value="${container.item.completed}" defaultValue="&nbsp;"/></font>
        </c:if>            
        <c:if test="${viewSubtestStatus != subtestName || currentStatus != 'Completed'}">                             
                <a href="#" onclick="return submitToAction('viewSubtestStatus.do#subtestTableAnchor', '<%= subtestId %>', 'Completed');">             
                    <netui:content value="${container.item.completed}" defaultValue="&nbsp;"/>
                </a>
        </c:if>      
</c:if>
<c:if test="${!hasCompletedLink}"> 
        <netui:content value="${container.item.completed}" defaultValue="&nbsp;"/>     
</c:if>            
           </td>            
        </tr>
        </netui-data:repeaterItem>
    </netui-data:repeater>
</table>
