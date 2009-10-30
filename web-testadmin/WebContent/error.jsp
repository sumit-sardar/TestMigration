<%@ page import="java.io.*, java.util.*"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>

<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>

<netui-template:template templatePage="/resources/jsp/template.jsp">
    <netui-template:setAttribute name="title" value="Error"/>
    <netui-template:section name="bodySection">

        <br/>
        <ctb:message title="{bundle.oas['system.error.title']}" style="errorMessage">
          <netui:span value="${bundle.oas['system.error.message']}"/>
        </ctb:message>
    
        <!-- Some browsers will not display this page unless the response status code is 200. -->
        <% response.setStatus(404); %>

    </netui-template:section>
</netui-template:template>
