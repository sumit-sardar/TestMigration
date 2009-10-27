<%@ page import="java.io.*, java.util.*"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>

<table id="legacyHeader">
<tr>
    <td class="welcome">
        <img src="<%=request.getContextPath()%>/resources/images/red_square.gif" height="7" width="7" >
        <a href="http://www.ctb.com">CTB/McGraw-Hill</a>
    </td>
    <td class="welcome alignRight">
        Welcome, <b><%=session.getAttribute("userName")%></b> (if you're not <%=session.getAttribute("userName")%>,
        <netui:anchor action="logout" styleClass="logout">logout</netui:anchor>)
    </td>
    <td class="welcome alignRight"><img src="<%=request.getContextPath()%>/resources/images/legacy/McGrawHillCompanies_fade.gif" width="192" height="16"></td>
</tr>
<tr>
    <td><img src="<%=request.getContextPath()%>/resources/images/legacy/multiple_products.gif" width="375" height="53"></td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
</tr>
</table>

