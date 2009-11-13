<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>

<netui-template:template templatePage="/resources/jsp/template.jsp">
    <netui-template:section name="bodySection">
 
<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->

<h1><netui:content value="${bundle.web['loadTest.loadTestToWorkstation.title']}"/></h1>
<p><netui:content value="${bundle.web['loadTest.loadProgress.title.message1']}"/></p>
<ctb:message title="${bundle.web['loadTest.cancelDownload.title']}" style="alertMessage">
    <netui:content value="${bundle.web['loadTest.cancelDownload.message']}"/>
</ctb:message>
    <br/>
    <netui:button type="button" value="Restart" onClick="document.location.href='goto_load_progress.do'"/>
    &nbsp;&nbsp;
    <netui:button type="button" value="Finish" onClick="document.location.href='goto_homepage.do'"/>

<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
    </netui-template:section>
</netui-template:template>

