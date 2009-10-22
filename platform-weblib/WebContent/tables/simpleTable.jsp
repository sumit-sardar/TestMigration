<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<netui-template:template templatePage="/resources/jsp/template.jsp">
    <netui-template:setAttribute name="title" value="Simple Table"/>
    <netui-template:section name="bodySection">

    <h1><netui:span value="Simple Table"/></h1>
    <p>
    The "simple" table is used to display a non-sortable table of data.
    </p>

    <table class="simple">
    <tr class="simple">
        <th class="simple alignLeft"><netui:span value="Student ID"/></th>
        <th class="simple alignLeft"><netui:span value="First Name"/></th>
        <th class="simple alignLeft"><netui:span value="Middle Name"/></th>
        <th class="simple alignLeft"><netui:span value="Last Name"/></th>
    </tr>
    <netui-data:repeater dataSource="pageFlow.students">
        <netui-data:repeaterItem>
        <tr class="simple">
            <td class="simple alignLeft"><netui:span value="${container.item.studentNumber}" defaultValue="&nbsp;"/></td>
            <td class="simple alignLeft"><netui:span value="${container.item.firstName}" defaultValue="&nbsp;"/></td>
            <td class="simple alignLeft"><netui:span value="${container.item.middleName}" defaultValue="&nbsp;"/></td>
            <td class="simple alignLeft"><netui:span value="${container.item.lastName}" defaultValue="&nbsp;"/></td>
        </tr>
        </netui-data:repeaterItem>
    </netui-data:repeater>
    </table>
    <br/><br/>


    </netui-template:section>
</netui-template:template>
