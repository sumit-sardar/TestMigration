<%@ page import="java.io.*, java.util.*"%>
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
                <td><netui:anchor action="homepage" onClick="return verifyExitScheduleTest();">home</netui:anchor></td>
                <c:if test="${ sessionScope.userHasReports}">                
                <td>| <a href="/TestSessionInfoWeb/homepage/viewReports.do" onclick="return verifyExitScheduleTest();">reports</a></td>
                </c:if>
                <td>| <a href="/UserManagementWeb/manageUser/beginMyProfile.do" onclick="return verifyExitScheduleTest();">my profile</a></td>
                <ctb:auth roles="Administrator">
                    <td>| <a href="/OrganizationManagementWeb/administration/begin.do" onclick="return verifyExitScheduleTest();">system administration</a></td>
                </ctb:auth>
            </tr>
            </table>
        </td>
        <td>
            <table class="legacyNavLinks floatRight">
            <tr>
                <c:if test="${ requestScope.helpLink == null }">            
                <td><a href="<netui-template:attribute name="helpLink"/>" onClick="showHelpWindow(this.href); return false;">help</a></td>
                </c:if>
                <c:if test="${ requestScope.helpLink != null }">            
                <td><a href="<%= request.getAttribute("helpLink") %>" onClick="showHelpWindow(this.href); return false;">help</a></td>
                </c:if>
                <td>| <netui:anchor action="logout" onClick="return verifyExitScheduleTest();">logout</netui:anchor></td>
            </tr>
            </table>
        </td>
    </tr>
    </table>
</div>

