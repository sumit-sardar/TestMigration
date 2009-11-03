<%@ page import="java.io.*, java.util.*"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="netui-tags-databinding.tld" prefix="netui-compat-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui-compat"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-compat-template"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%
    String pageError = ( String )request.getAttribute( "PAGE_ERROR" );
%>

<netui:html>
    <head>
        <title>
            User Login
        </title>
          
    </head>
    <body>
    <jsp:include page="./HeaderLogin.jsp" />  
    <p>To verify your test layout and appearance...<br>
    <strong>Login to OAS Content Review</strong></p>
        <table border="1" cellspacing="0" cellpadding="0" bgcolor="#FEFCD5" bordercolor="#FFCC66">
            <tr>
                <td>
                <!--Begin content-->
                <!--Begin content-->
                    <netui:form method="post" action="userLogin">
                        <table border="0" cellspacing="0" cellpadding="0" width="440">
                        <%
                            if( pageError != null )
                            {
                        %>
                        <tr>
                            <td colspan="4" align="center">
                                
                                <br>
                                <span style="color:#ff0000;" ><%= pageError %></span>
                                
                            </td>
                        </tr>
                        <% 
                            } 
                        %>
                        <tr><td height="20" colspan="4">&nbsp;</td></tr>
                        <tr>
                            <td width="24">&nbsp;</td>
                            <td>Login ID:</td>
                            <td><netui:image src=".././resources/images/transparent.gif" width="6" height="24" border="0"/></td>
                            <td><netui:textBox dataSource="pageFlow.userName" defaultValue=""/></td>
                        </tr>
                        <tr><td height="20" colspan="4">&nbsp;</td></tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td>Password:</td>
                            <td><netui:image src=".././resources/images/transparent.gif" width="6" height="24" border="0"/></td>
                            <td><input type="password" name="pwdPassword"></td>
                        </tr>
                        <tr><td height="20" colspan="4">&nbsp;</td></tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                            <td><netui:image src=".././resources/images/transparent.gif" width="6" height="24" border="0"/></td>
                            <td><input type="submit" name="do" value="Login" ></td>
                        </tr>
                        </table>
                    </netui:form>
                </td></tr>
        </table>
    </body>
</netui:html>
