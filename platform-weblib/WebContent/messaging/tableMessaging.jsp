<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>

<netui-template:template templatePage="/resources/jsp/template.jsp">
    <netui-template:setAttribute name="title" value="Messaging Within a Table"/>
    <netui-template:section name="bodySection">

        <h1><netui:span value="Messaging - Within a Table"/></h1>
        <br/><br/>

        <table class="simple">
        <tr class="simple">
            <th class="simple alignLeft"><div class="notCurrentSort"><netui:span value="Student ID"/></div></th>
            <th class="simple alignLeft"><div class="notCurrentSort"><netui:span value="First Name"/></div></th>
            <th class="simple alignLeft"><div class="notCurrentSort"><netui:span value="Middle Name"/></div></th>
            <th class="simple alignLeft"><div class="notCurrentSort"><netui:span value="Last Name"/></div></th>
        </tr>
        <tr class="simple">
            <td class="simple" colspan="4">
                <ctb:message style="tableMessage" title="No students have been selected to take this test.">
                    <netui:span value="Use the Add Students button to add students to this test session."/>
                </ctb:message>
            </td>
        </tr>
        </table>

    </netui-template:section>
</netui-template:template>
