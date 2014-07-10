<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>LTI authentication test client</title>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery-1.6.2.min.js"></script>
<script>
function signRequest()
{

alert("Values sent to server..."+$("#ltiLaunchForm").serialize());
$.ajax({
  type: "POST",
  url: "LTISignServlet",
  data: $("#ltiLaunchForm").serialize()
})
  .done(function( sign ) {
    alert( "Oauth signature " + sign );
    $("#oauth_signature").val(sign);
  });
}
function test()
{
alert("test");
}
</script>
</head>
<body><h1>LTI authentication test client</h1><br/>
<h2>Only  consumer key, user id and signature can be modified.</h2>
<form action="<%=application.getInitParameter("OAS-URL") %>" name="ltiLaunchForm" id="ltiLaunchForm" method="post" encType="application/x-www-form-urlencoded" name="ltiLaunchForm" id="ltiLaunchForm" method="post" encType="application/x-www-form-urlencoded">
<table><tr><td>OAuth Version</td><td>
<input type="text" name="oauth_version" value="1.0" readonly="readonly"/></td>
</tr><tr>
<td>OAuth once</td><td>
<input type="text" name="oauth_nonce" value="c8350c0e47782d16d2fa48b2090c1d8f" readonly="readonly"/></td>
</tr><tr>
<td>oauth_timestamp</td><td>
<input type="text" name="oauth_timestamp" value="1251600739" readonly="readonly"/></td>
</tr><tr>
<td><b>oauth consumer key*</b></td><td>
<input type="text" name="oauth_consumer_key" value="14719" /></td></tr>
<tr>
<td><b>user id*</b></td><td>
<input type="text" name="user_id" value="221483"/></td>
</tr><tr>
<td>Roles</td><td>
<input type="text" name="roles" value="Instructor" readonly="readonly"/></td></tr><tr>
<td>Full Name</td><td>
<input type="text" name="lis_person_name_full" value="Jane Q. Public"  readonly="readonly"/></td></tr><tr>
<td>LTI Version</td><td>
<input type="text" name="lti_version" value="LTI-1p0" readonly="readonly"/></td></tr><tr>
<td>LTI message type</td><td>
<input type="text" name="lti_message_type" value="basic-lti-launch-request" readonly="readonly"/></td></tr><tr>
<td>OAuth signature method</td><td>
<input type="text" name="oauth_signature_method" value="HMAC-SHA1" readonly="readonly"/></td></tr><tr>
<td>Callback page</td><td>
<input type="text" name="oauth_callback" value="about:blank" readonly="readonly"/></td></tr><tr>
<td>
OAuth Signature</td><td>
<input type="text" name="oauth_signature" id="oauth_signature" value="21807ea2ff90a12f65f2238d0373a47292ab2092" /></td></tr><tr>
<td><button onclick="javascript:signRequest()" type="button">Calculate signature</button></td>
<td  align="center">
<button >Authenticate</button></td></tr>
</table>
</form>
</body>
</html>