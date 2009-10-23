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

<netui-template:template templatePage="/resources/jsp/template.jsp">
    <netui-template:setAttribute name="title" value="${bundle.web['homepage.window.title']}"/>
    <netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.home']}"/>
<netui-template:section name="bodySection">
 
<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->

<h1><netui:content value="${bundle.web['homepage.title']}"/></h1>
<p><netui:content value="You logged in as an Account manager. To access Tests, Students, Reports, and Workstation Setup, log out and log in as an Administrator."/></p>

<netui:form action="home_page">
    <netui:hidden dataSource="actionForm.actionElement"/>


<!-- ********************************************************************************************************************* -->
<!-- Broadcast Message  -->
<!-- ********************************************************************************************************************* -->
<c:if test="${broadcastMessages != null}">
<p>
    <table class="grouping" width="100%">
    <netui-data:repeater dataSource="requestScope.broadcastMessages">
        <netui-data:repeaterHeader>
        <h2><netui:content value="${bundle.web['homepage.broadcastMessages.title']}"/></h2>
        <p><netui:content value="${bundle.web['homepage.broadcastMessages.information']}"/></p>
            <tr class="grouping">
                <th class="grouping"><netui:content value="${bundle.web['homepage.broadcastMessages.message']}"/></th>
                <th class="grouping"><netui:content value="${bundle.web['homepage.broadcastMessages.date']}"/></th>
            </tr>
        </netui-data:repeaterHeader>
        <netui-data:repeaterItem>
            <tr class="sortable">    
                <td class="sortable alignLeft"><netui:content value="${container.item.message}" defaultValue="&nbsp;"/></td>
                <td class="sortable alignLeft"><netui:span value="${container.item.createdDateTime}" defaultValue="&nbsp;"><netui:formatDate pattern="MM/dd/yy"/></netui:span></td>
            </tr>
        </netui-data:repeaterItem>
    </netui-data:repeater>
    </table><br/>
</p>    
</c:if>



</netui:form>

<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
    </netui-template:section>
</netui-template:template>

