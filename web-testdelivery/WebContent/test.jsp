<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <title>
            Test Login
        </title>
    </head>
    <body>
        <netui:form action="begin">
            <table>
                <tr valign="top">
                    <td>RequestXML: <br/>
                    <netui:textArea dataSource="actionForm.requestXML" rows="15" cols="40"/>
                    </td>
                </tr>
            </table>
            <input type="hidden" name="startTime"/>
            <br/>&nbsp;
            <netui:button value="test" type="submit" />
        </netui:form>
    </body>
</netui:html>