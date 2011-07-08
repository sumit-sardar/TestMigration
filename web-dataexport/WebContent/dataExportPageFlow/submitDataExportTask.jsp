<%@ page import="java.io.*, java.util.*"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
 
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>
<netui-data:declareBundle bundlePath="helpResources" name="help"/>



<!--Change MQC defect  55837 -->

<%String templatePage = "/resources/jsp/template.jsp";%>


<netui-template:template templatePage="<%=templatePage%>" > 
<netui-template:setAttribute name="title" value="${bundle.web['selecttest.window.title']}"/>
<netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.scheduleTestSessionSelectTest']}"/>
<netui-template:section name="bodySection">

<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->

<netui-data:getData resultId="action" value="${pageFlow.action}"/>

<netui:form action="generateReport">
<netui:button styleClass="button" type="submit" tagId="Next" value="${bundle.web['common.button.submit']}"  />

</netui:form>

       
<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
</netui-template:section>
</netui-template:template>
