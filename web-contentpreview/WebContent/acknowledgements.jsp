<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%>
<netui:html>
    <head>
        <title>
            Acknowledgements
        </title>
    </head>
    <body>
    <p> <netui:anchor action="backToOption">Back</netui:anchor></p>
        <p>
        <netui-data:repeater dataSource="{pageFlow.acknowledgements}">
            <netui-data:repeaterHeader>
                <table class="tablebody" border="0">
        <%--        <tr class="tablehead" valign="top">
                    <th>ItemId</th>
                    <th>Text</th>
                </tr> --%>
            </netui-data:repeaterHeader>
            <netui-data:repeaterItem choiceValue="{container.item.text.length() > 0}">
                <tr valign="top">
        <%--            <td><netui:label value="{container.item.itemId}" defaultValue="&nbsp;"></netui:label></td>
        --%>
                    <td><netui:content value="{container.item.text}" defaultValue="&nbsp;"></netui:content></td>
                </tr>
                <tr valign="top"><td>&nbsp;</td></tr>
            </netui-data:repeaterItem>
            <netui-data:repeaterFooter></table></netui-data:repeaterFooter>
        </netui-data:repeater>
        </p>
        <p><netui:anchor action="backToOption">Back</netui:anchor></p>        
    </body>
</netui:html>
