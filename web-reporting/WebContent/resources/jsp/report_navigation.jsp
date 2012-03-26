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
                <td><a href="/TestSessionInfoWeb/homepage/HomePageController.jpf">home |</a></td>
                <td class="currentNav">reports</td>
                <td>|&nbsp; <a href="/UserManagementWeb/manageUser/beginMyProfile.do">my profile</a></td>
                <ctb:auth roles="Administrator">
                <td>|&nbsp; <a href="/OrganizationManagementWeb/administration/begin.do">system administration</a></td>
                </ctb:auth>
            </tr>
            </table>
        </td>
        <td>
            <table class="legacyNavLinks floatRight">
            <tr>
                <td><a href="/help/index.html#about_the_home_page.htm" onClick="return showHelpWindow(this.href);">help</a></td>
                <td>|&nbsp; <netui:anchor action="logout">logout</netui:anchor></td>
            </tr>
            </table>
        </td>
    </tr>
    </table>
</div>
