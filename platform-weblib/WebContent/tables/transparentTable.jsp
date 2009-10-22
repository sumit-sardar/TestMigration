<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>

<netui-template:template templatePage="/resources/jsp/template.jsp">
    <netui-template:setAttribute name="title" value="Transparent Table"/>
    <netui-template:section name="bodySection">

        <h1><netui:span value="Transparent Table"/></h1>
        <p>
        The "transparent" table can be used as a nested table for grid layout of
        information to be displayed to the end user.
        </p>

        <table class="transparent">
        <tr class="transparent">
            <th class="transparent"><netui:span value="Column 1"/></th>
            <th class="transparent"><netui:span value="Column 2"/></th>
            <th class="transparent"><netui:span value="Column 3"/></th>
            <th class="transparent"><netui:span value="Column 4"/></th>
        </tr>
        <tr class="transparent">
            <td class="transparent"><netui:span value="Data 1"/></td>
            <td class="transparent"><netui:span value="Data 2"/></td>
            <td class="transparent"><netui:span value="Data 3"/></td>
            <td class="transparent"><netui:span value="Data 4"/></td>
        </tr>
        <tr class="transparent">
            <td class="transparent"><netui:span value="Data 5"/></td>
            <td class="transparent"><netui:span value="Data 6"/></td>
            <td class="transparent"><netui:span value="Data 7"/></td>
            <td class="transparent"><netui:span value="Data 8"/></td>
        </tr>
        </table>
        <br/><br/>
    
    </netui-template:section>
</netui-template:template>