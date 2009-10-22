<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="org.apache.struts.action.ActionForm"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>

<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>

<netui-template:template templatePage="/resources/jsp/template.jsp">
    <netui-template:setAttribute name="title" value="Path and List Table"/>
    <netui-template:section name="bodySection">

    <h1><netui:span value="Path and List Table"/></h1>
    <p>
    The "path and list" table is typically used for organizational traversing or any other
    bread-crumb navigation.
    </p>

    <netui:form action="viewPathListTable">    

        <table class="sortable">
        <tr class="sortable">
            <td class="sortableControls" colspan="4">
            
                <ctb:tablePathList valueDataSource="${actionForm.orgNodeId}" labelDataSource="${actionForm.orgNodeName}" pathList="${request.orgNodePath}" />
                
            </td>
        </tr>
        </table>
    </netui:form>


    </netui-template:section>
</netui-template:template>
