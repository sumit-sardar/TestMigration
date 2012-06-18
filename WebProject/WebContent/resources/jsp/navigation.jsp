<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
 
<div id="legacyNav">
    <table class="legacyNavLayout">
    <tr>
        <td>
            <table class="legacyNavLinks floatLeft">
            <tr>
            	<td><a href="/web-demo/homepage/HomePageController.jpf">home</a></td>
                <td>|&nbsp;&nbsp;<a href="/web-demo/tableList/flexigrid.do">flexigrid</a></td>
                <td>|&nbsp;&nbsp;<a href="/web-demo/tableList/tabs.do">tabs</a></td>
                <td>|&nbsp;&nbsp;<a href="/web-demo/tableList/beginHierarchy.do">hierarchy</a></td>
                <td>|&nbsp;&nbsp;<a href="/web-demo/tableList/accordion.do">accordion</a></td>
                <td>|&nbsp;&nbsp;<a href="/web-demo/tableList/modalDialog.do">modal dialog</a></td>
                <td>|&nbsp;&nbsp;<a href="/web-demo/tableList/tooltip.do">tooltip</a></td>
                <td>|&nbsp;&nbsp;<a href="/web-demo/tableList/autocomplete.do">autocomplete</a></td>
                <td>|&nbsp;&nbsp;<a href="/web-demo/tableList/datePicker.do">date picker</a></td>
                <td>|&nbsp;&nbsp;<a href="/web-demo/tableList/treeView.do">tree</a></td>
            </tr>
            </table>
        </td>
        <td>
            <table class="legacyNavLinks floatRight">
            <tr>
                <td><a href="<netui-template:attribute name="helpLink"/>" onClick="return showHelpWindow(this.href);">help</a></td>
                <td>| <a href="logout" styleClass="logout">logout</a></td>
            </tr>
            </table>
        </td>
    </tr>
    </table>
</div>

