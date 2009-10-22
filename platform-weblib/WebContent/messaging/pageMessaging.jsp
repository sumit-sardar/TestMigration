<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>

<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>


<netui-template:template templatePage="/resources/jsp/template.jsp">
    <netui-template:setAttribute name="title" value="Messaging Within a Page"/>
    <netui-template:section name="bodySection">

        <h1><netui:span value="Messaging - Within a Page"/></h1>

        <br/>
        <ctb:message title="Information Message." style="informationMessage">
            Use the Add Proctors button to add proctors to this test session.
        </ctb:message>

        <br/>
        <ctb:message title="Alert Message" style="alertMessage">
            Use the Add Proctors button to add proctors to this test session.
        </ctb:message>

        <br/>
        <ctb:message title="Error Message." style="errorMessage">
            Use the Add Proctors button to add proctors to this test session.
        </ctb:message>
    
    </netui-template:section>
</netui-template:template>
