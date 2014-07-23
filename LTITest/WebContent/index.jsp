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
<!-- 
<form action="<%=application.getInitParameter("OAS-URL") %>"  method="post" encType="application/x-www-form-urlencoded" name="ltiLaunchForm" id="ltiLaunchForm" method="post" encType="application/x-www-form-urlencoded">
<table><tr><td>OAuth Version</td><td>
<input type="text" name="oauth_version" value="1.0" readonly="readonly"/></td>
</tr><tr>
<td>OAuth once</td><td>
<input type="text" name="oauth_nonce" value="b530f608fa26f83579cb0586df44d240" /></td>
</tr><tr>
<td>oauth_timestamp</td><td>
<input type="text" name="oauth_timestamp" value="1405119940" /></td>
</tr><tr>
<td><b>oauth consumer key*</b></td><td>
<input type="text" name="oauth_consumer_key" value="14719" /></td></tr>
<tr>
<td><b>user id*</b></td><td>
<input type="text" name="user_id" value="10000000005946107"/></td>
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
<input type="text" name="oauth_signature" value="21807ea2ff90a12f65f2238d0373a47292ab2092" /></td></tr><tr>
<td><button onclick="javascript:signRequest()" type="button">Calculate signature</button></td>
<td  align="center">
<button >Authenticate</button></td></tr>
</table>
</form>
 -->
<form id="ltiLaunchForm" method="post" action="<%=application.getInitParameter("OAS-URL") %>" >
<table>
<tr><td>User id</td><td>
<textarea  name="user_id" >10000000005946109</textarea></td></tr>
<tr><td>Roles</td><td>
<textarea  name="roles" >Instructor</textarea></td></tr>
<tr><td>Lis Person Name</td><td>
<textarea  name="lis_person_name_full" >LASLinks Teacher 1</textarea></td></tr>
<tr><td>OAuth Consumer Key</td><td>
<textarea  name="oauth_consumer_key" >14719</textarea></td></tr>
<tr><td>Lti Message Type</td><td>
<textarea  name="lti_message_type" readonly="readonly">basic-lti-launch-request</textarea></td></tr>
<tr><td>Lti Version</td><td>
<textarea name="lti_version" >LTI-1p0</textarea></td></tr>
<tr><td>Custom app sessionid</td><td>
<textarea  name="custom_appsesid" >9000000000000163683cFvidhyn8fKT4rfrn6O71Lu1D3K5Ug1104ea7b1b7d758</textarea></td></tr>
<tr><td>Resource link id</td><td>
<textarea  name="resource_link_id" >5000007027887</textarea></td></tr>
<tr><td>Lis Person Contact Email primary</td><td>
<textarea  name="lis_person_contact_email_primary" >demo@engrade.com</textarea></td></tr>
<tr><td>Custom School ID</td><td>
<textarea  name="custom_schoolid" >10000000005946103</textarea></td></tr>
<tr><td>Customer District ID</td><td>
<textarea  name="custom_districtid" >10000000005946101</textarea></td></tr>
<tr><td>Context title</td><td>
<textarea  name="context_title" >7th Grade Math (demo)</textarea></td></tr>
<tr><td>Context ID</td><td>
<textarea  name="context_id" >5000007027887</textarea></td></tr>
<tr><td>Context Label</td><td>
<textarea  name="context_label" >7th Grade Math (demo)</textarea></td></tr>
<tr><td>OAuth Callback</td><td>
<textarea  name="oauth_callback" >about:blank</textarea></td></tr>
<tr><td>OAuth Version</td><td>
<textarea  name="oauth_version" >1.0</textarea></td></tr>
<tr><td>OAuth Nonce</td><td>
<textarea  name="oauth_nonce" >88fd64654ad9a44a32e2d86be97b9e78</textarea></td></tr>
<tr><td>OAuth Timestamp</td><td>
<textarea name="oauth_timestamp" >1405458214</textarea></td></tr>
<tr><td>OAuth Signature Method</td><td>
<textarea  name="oauth_signature_method" readonly="readonly">HMAC-SHA1</textarea></td></tr>
<tr><td>OAuth Signature</td><td>
<textarea  name="oauth_signature" id="oauth_signature" >vn+gvmKH2B0bOCdPRp9uySrajVk=</textarea></td></tr>
<tr><td><button onclick="javascript:signRequest()" type="button">Calculate signature</button></td><td>
<input id="fakesubmitbutton" type="submit" /></td></tr>
</form>
</body>
</html>