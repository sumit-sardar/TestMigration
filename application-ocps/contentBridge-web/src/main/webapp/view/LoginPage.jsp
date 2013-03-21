
<%
	response.setHeader("Cache-Control", "no-store");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader("Expires", 0);
%>

<%@ page language="java" contentType="text/html; charset=windows-1256"
	pageEncoding="windows-1256"
	import="com.ctb.contentBridge.core.domain.UserBean"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<script type="text/javascript">
	window.history.forward();
	function noBack() {
		window.history.forward();
	}

	function resetForm() {
		//document.getElementById("homeForm").submit();
		document.getElementById("homeForm").reset();

	}
</script>
<meta http-equiv="Content-Type"
	content="text/html; charset=windows-1256">
<title>Login Page</title>
</head>
<%
	if ((session.getAttribute("username")) == null
			&& (session.getAttribute("password") == null)) {
		session.removeAttribute("username");
		session.removeAttribute("password");
		session.invalidate();
%>

<body onload="noBack();" background="image/index.png">
	<form action="LoginServlet" id="homeForm">

		<!-- Please enter your username <input type="text" name="un" onclick="this.value='';"/><br> -->

		<table cellpadding="2" cellspacing="2" width="334" border="0"
			align="center">
			<tr height="25">
				<td colspan="4">&nbsp;</td>
			</tr>
			<tr>
				<td align="center" colspan="4"></td>
			</tr>
			<tr height="25">
				<td colspan="4">&nbsp;</td>
			</tr>

			<tr>
				<td width="110" align=right class="specialtext"><b>User
						Name: </b></td>
				<td><input type="text" name="un" size="20" id="name" /></td>
			</tr>

			<tr>
				<td width="110" align=right class="specialtext"><b>Password:</b>
				</td>
				<td><input type="password" name="pw" size="22" id="pwd" /></td>
			</tr>

			<tr>
				<td width="182" align=left class="specialtext"><b>Target
						Environment: </b></td>
				<td><select name="environment">
						<option value="DEV">Development</option>
						<option value="NEWQA">NEWQA</option>
						<option value="PRODUCTION">Production</option>
						<option value="STAGE">Staging</option>
						<option value="CQA">CQA</option>
				</select></td>
			</tr>

			<tr>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td align="left">
					<!--input type="button" name="submit" value="Login" class="LoginBtn" onClick="javascript:parent.location.href('Notification.html');"
                            <a  href="MainMenuFrameSet.html" class="smallLink">
                              
                            </a> --> <input type="submit"
					name="submitButton" value="Sign In" class="BUTTON"></td>


			</tr>
			<tr>
				<td colspan="3">&nbsp;</td>
			</tr>
			<tr>
				<td colspan="3" class="LoginErrMsg" align="right"><strong>
				</strong>
				</td>
			</tr>
		</table>

	</form>
</body>
<%
	} else {
		String urlToRedirect = request.getScheme() + "://"
				+ request.getServerName() + "/"
				+ request.getServerPort() + "/MonarchServ";
		response.sendRedirect(urlToRedirect);
%>

<%
	}
%>
</html>
