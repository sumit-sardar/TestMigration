<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%>
<netui:html>
    <head>
        <title>
            Answer Key
        </title>
    </head>
    <body>
        <p>
        <netui:anchor action="backToOption">Back</netui:anchor>
        </p>
        <br/>
        <p>
        <netui:label value="{pageFlow.assessmentTitle}"/></p>
        <p>
        <netui-data:repeater dataSource="{pageFlow.schedulableUnits}">
            <netui-data:repeaterHeader>
                <table class="tablebody" border="0">
            </netui-data:repeaterHeader>
            <netui-data:repeaterItem>
                <tr valign="top">
                    <td><netui:label value="{container.item.title}" defaultValue="&nbsp;"></netui:label></td>
                    <td>
                    <netui-data:repeater dataSource="{container.item.deliverableUnits}">
                        <netui-data:repeaterHeader>
                            <table class="tablebody" border="0">
                        </netui-data:repeaterHeader>
                        <netui-data:repeaterItem>
                            <tr valign="top">
                                <td><netui:label value="{container.item.title}" defaultValue="&nbsp;"></netui:label></td>
                                <td>
                                <netui-data:repeater dataSource="{container.item.items}">
                                    <netui-data:repeaterHeader>
                                        <table class="tablebody" border="1">
                                    </netui-data:repeaterHeader>
                                    <netui-data:repeaterItem>
                                        <tr valign="top">
                                            <td>
                                                <netui:anchor href="getItemXML.do?item={container.item.index}&subtest={container.container.item.index}" >
                                                    <netui:label value="{container.item.id}" defaultValue="&nbsp;"></netui:label>
                                                </netui:anchor>
                                            </td>
                                            <td>
                                            <netui-data:repeater dataSource="{container.item.answerChoices}">
                                                <netui-data:repeaterHeader>
                                                    <table class="tablebody" border="0" width="100%>
                                                </netui-data:repeaterHeader>
                                                <netui-data:repeaterItem>
                                                    <tr valign="top">
                                                        <td width="80%"><netui:label value="{container.item.text}" defaultValue="&nbsp;"></netui:label></td>
                                                        <td width="20%"><netui:label value="{container.item.type}" defaultValue="&nbsp;"></netui:label></td>
                                                    </tr>
                                                </netui-data:repeaterItem>
                                                <netui-data:repeaterFooter></table></netui-data:repeaterFooter>
                                            </netui-data:repeater>
                                            </td>
                                        </tr>
                                    </netui-data:repeaterItem>
                                    <br/>
                                    <netui-data:repeaterFooter></table></netui-data:repeaterFooter>
                                </netui-data:repeater>
                                </td>
                            </tr>
                        </netui-data:repeaterItem>
                        <br/>
                        <br/>
                        <netui-data:repeaterFooter></table></netui-data:repeaterFooter>
                    </netui-data:repeater>
                    </td>
                </tr>
            </netui-data:repeaterItem>
            <br/>
            <netui-data:repeaterFooter></table></netui-data:repeaterFooter>
        </netui-data:repeater>
        </p>
        <br/>
        <p>
        <netui:anchor action="backToOption">Back</netui:anchor>
        </p>
    </body>
</netui:html>
