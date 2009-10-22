<%@ page import="java.io.*, java.util.*"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>

<table id="legacyHeader">
<tr>
    <td class="welcome">
        <img src="<%=request.getContextPath()%>/resources/images/legacy/red_square.gif">
        <a href="http://www.ctb.com">CTB/McGraw-Hill</a>
    </td>
    <td class="welcome alignRight">
        Welcome, <b>oas_widget</b>
        (if you're not oas_widget, 
        <a href="<%=request.getContextPath()%>" class="logout">logout</a>
        )
    </td>
    <td class="welcome alignRight"><img src="<%=request.getContextPath()%>/resources/images/legacy/McGrawHillCompanies_fade.gif"></td>
</tr>
<tr>
    <td><img src="<%=request.getContextPath()%>/resources/images/legacy/multiple_products.gif"></td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
</tr>
</table>

