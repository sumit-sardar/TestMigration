<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<netui-template:template templatePage="/resources/jsp/template.jsp">
    <netui-template:setAttribute name="title" value="Grouping Table"/>
    <netui-template:section name="bodySection">

    <h1><netui:span value="Grouping Table"/></h1>
    <p>
    The "grouping" table is used to logically group information.  Most typically used
    with the nested "transparent" table.
    </p>


    <table class="grouping">
    <tr class="grouping">
        <th class="grouping"><netui:span value="Grouping Table"/></th>
    </tr>
    <tr class="grouping">
        <td class="grouping">
            <netui:span value="Table  used to provide a visual container around a group of text, forms, tables, etcetera."/>
        </td>
    </tr>
    </table>
    <br/><br/>


    <table class="grouping">
    <tr class="grouping">
        <th class="grouping"><netui:span value="Grouping Table with Transparent Table"/></th>
    </tr>
    <tr class="grouping">
        <td class="grouping">
        
            <table class="transparent">
            <tr class="transparent">
                <td class="transparent"><netui:span value="Name:"/></td>
                <td class="transparent"><netui:span value="Value"/></td>
            </tr>
            <tr class="transparent">
                <td class="transparent"><netui:span value="Another:"/></td>
                <td class="transparent"><netui:span value="Some Information"/></td>
            </tr>
            </table>
        
        </td>
    </tr>
    </table>
    <br/><br/>


    <netui:form action="viewGroupingTable">
    <table class="grouping">
    <tr class="grouping">
        <th class="grouping"><netui:span value="Grouping Table with Form Elements"/></th>
    </tr>
    <tr class="grouping">
        <td class="grouping">
        
            <table class="transparent">
            <tr class="transparent">
                <td class="transparent"><netui:span value="Name:" styleClass="text"/></td>
                <td class="transparent">
                    <netui:textBox dataSource="actionForm.textFieldValue" styleClass="textField"/>
                </td>
            </tr>
            <tr class="transparent">
                <td class="transparent"><netui:span value="Color:" styleClass="text"/></td>
                <td class="transparent">
                    <netui:radioButtonGroup dataSource="actionForm.radioButtonValue">
                        <netui:radioButtonOption value="Red" labelStyleClass="radioButton"/>
                        <netui:radioButtonOption value="Green" labelStyleClass="radioButton"/>
                        <netui:radioButtonOption value="Blue" labelStyleClass="radioButton"/>
                    </netui:radioButtonGroup>
                </td>
            </tr>
            </table>
        
        </td>
    </tr>
    </table>
    </netui:form>
    <br/><br/>

    </netui-template:section>
</netui-template:template>