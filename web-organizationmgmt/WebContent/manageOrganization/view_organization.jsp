<%@ page import="java.io.*, java.util.*"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="ctb-web.tld" prefix="ctbweb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>
<netui-data:declareBundle bundlePath="helpResources" name="help"/>

<netui-template:template templatePage="/resources/jsp/template.jsp">
<netui-template:setAttribute name="title" value="${bundle.web['vieworg.window.title']}"/>
<netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.viewOrganization']}"/>
<netui-template:section name="bodySection">

<%            
    List selectedOrgNodes = (List)request.getAttribute("selectedOrgNodes"); 
    List orgNodesForSelector = (List)request.getAttribute("orgNodesForSelector");             
    String userAgent = request.getHeader("User-Agent");
%>

<!-- start form -->
<netui:form action="viewOrganization">


<netui:hidden tagId="actionElement" dataSource="actionForm.actionElement"/> 
<netui:hidden tagId="currentAction" dataSource="actionForm.currentAction"/>
<netui:hidden dataSource="actionForm.selectedOrgChildNodeId"/>
<netui:hidden dataSource="actionForm.selectedOrgNodeId"/>
<netui:hidden dataSource="actionForm.orgMaxPage"/>
<netui:hidden dataSource="actionForm.orgPageRequested"/>
<netui:hidden dataSource="actionForm.previousAction"/>
<netui:hidden dataSource="actionForm.orgNodeId"/>
<netui:hidden dataSource="actionForm.orgNodeName"/>
<netui:hidden dataSource="actionForm.previousParentId"/>
<netui:hidden dataSource="actionForm.previousParentName"/>
<netui:hidden dataSource="actionForm.selectedOrgNodeName"/>
<netui:hidden dataSource="actionForm.tempOrgChildNodeId"/>
<netui:hidden dataSource="actionForm.orgSortColumn"/>
<netui:hidden dataSource="actionForm.orgSortOrderBy"/>



<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->
<h1>
    <netui:content value="${pageFlow.pageTitle}"/>
</h1>      


<!-- title message -->
<p>
    <netui:content value="Review the organization information listed below. "/>
</p>


<netui-data:getData resultId="messageType" value="${pageMessage.type}"/>
<c:if test="${messageType != null}">     
<p>
    <% String style = (String)pageContext.getAttribute("messageType"); %> 
    <ctb:message title="${pageMessage.title}" style="<%= style %>" >
          <netui:content value="${pageMessage.content}"/>
    </ctb:message>
</p>
</c:if> 



<!-- buttons -->
<p>
    <netui:button type="submit" value="Back" action="backToPreviousAction.do"/>
    <netui:button type="submit" value="Edit" action="toEditOrganization.do" disabled="${requestScope.disableEditButton}"/>
    <netui:button type="submit" value="Delete" action="toDeleteOrganization.do" onClick="return verifyDeleteOrganization();" disabled="${requestScope.disableDeleteButton}"/>
    <netui:button type="submit" value="Add Organization" action="toAddOrganization.do" disabled="${requestScope.disableADDButton}"/>
</p>


<p>

<table class="Collapsible">
    <tr class="Collapsible">
        <td class="CollapsibleHeader">
        &nbsp;&nbsp;Organization Information
        </td>
    </tr>
</table>

<table class="simple">
    <tr class="transparent">
    <td>                
        <table class="transparent">
            <tr class="transparent">
                <td class="transparent alignRight" width="66"><netui:content value="Name:"/></td>
                <td class="transparent"><netui:content value="${actionForm.selectedOrgName}"/></td>
            </tr>
            <tr class="transparent">
                <td class="transparent alignRight" width="66"><netui:content value="Org Code:"/></td>
                <td class="transparent"><netui:content value="${actionForm.selectedOrgNodeCode}"/></td>
            </tr>
            <tr class="transparent">
                <td class="transparent alignRight" width="66"><netui:content value="Layer:"/></td>
                <td class="transparent"><netui:content value="${actionForm.selectedOrgNodeType}"/></td>
            </tr>
            <tr class="transparent">
                <td class="transparent alignRight" width="66" nowrap><netui:content value="Parent Org:"/></td>
                <td class="transparent"><netui:content value="${actionForm.selectedOrgNodeName}"/></td>
            </tr>
        </table>
    </td>
    </tr>
</table>
</p>


<!-- buttons -->
<p>
    <netui:button type="submit" value="Back" action="backToPreviousAction.do"/>
    <netui:button type="submit" value="Edit" action="toEditOrganization.do" disabled="${requestScope.disableEditButton}"/>
    <netui:button type="submit" value="Delete" action="toDeleteOrganization.do" onClick="return verifyDeleteOrganization();" disabled="${requestScope.disableDeleteButton}"/>
    <netui:button type="submit" value="Add Organization" action="toAddOrganization.do" disabled="${requestScope.disableADDButton}"/>
</p>

</netui:form>
        
<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
</netui-template:section>
</netui-template:template>
