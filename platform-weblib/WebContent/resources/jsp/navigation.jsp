<%@ page import="java.io.*, java.util.*"%>

<div id="legacyNav">
    <table class="legacyNavLayout">
    <tr>
        <td>
            <table class="legacyNavLinks floatLeft">
            <tr>
                <td class="currentNav">home</td>
                <%-- %><td>| <a href="<%=request.getContextPath()%>">my test library</a></td> --%>
                <td>| <a href="<%=request.getContextPath()%>">culpa</a></td>
                <td>| <a href="<%=request.getContextPath()%>">officia</a></td>
                <td>| <a href="<%=request.getContextPath()%>">mollitia</a></td>
            </tr>
            </table>
        </td> 
        <td>
            <table class="legacyNavLinks floatRight">
            <tr>
                <td><a href="<%=request.getContextPath()%>" onclick="showHelpWindow(this.href); return false;">help</a></td>
                <td>| <a href="<%=request.getContextPath()%>">logout</a></td>
            </tr>
            </table>
        </td>
    </tr>
    </table>
</div>
