<%@ page import="java.io.*, java.util.*"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="label.tld" prefix="lb" %>
<lb:bundle baseName="immediateReportingResources" />
 
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>
<netui-data:declareBundle bundlePath="helpResources" name="help"/>

<netui-template:template templatePage="/resources/jsp/oas_template.jsp">
    <netui-template:setAttribute name="title" value="${bundle.web['homepage.window.title']}"/>
    <netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.manageSessions']}"/>
<netui-template:section name="bodySection">
 
<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->
 
<form method="post" action="begin.do">
<input type="hidden" id="menuId" name="menuId" value="" />
<table border="0" width="97%" style="margin:15px auto;">
<tr>
<td>
	<table>
		<tr>
			<td>
                <div style="background-color: #ffc; border-color: #000; border-style: solid; border-width: 1px; padding: 10px;">
                    <table>
                        <tr>
                            <td valign="center">
                                <img src="<%=request.getContextPath()%>/resources/images/messaging/icon_error.gif" border="0" width="16" height="16">&nbsp;&nbsp;
                            </td>
                            <td valign="center">
                                <font style="color: red; font-size:12px; font-weight:bold">
          							<netui:span value="${bundle.oas['system.error.message']}"/>
                                </font>
                            </td>
                        </tr>
                    </table>
                </div>
			</td>
		</tr>
	</table>
</td>    
</tr>
</table>
    
</form>

<script type="text/javascript">
$(document).ready(function(){
	setMenuActive("reports", "reportsLink");
});
</script>

<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
    </netui-template:section>
</netui-template:template>


