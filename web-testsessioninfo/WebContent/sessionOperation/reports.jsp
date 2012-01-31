<%@ page import="java.io.*, java.util.*"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="label.tld" prefix="lb" %>
<lb:bundle baseName="testsessionApplicationResource" />

<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>
<netui-data:declareBundle bundlePath="helpResources" name="help"/>

<netui-template:template templatePage="/resources/jsp/oas_template.jsp">
    <netui-template:setAttribute name="title" value="${bundle.web['homepage.window.title']}"/>
    <netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.reports']}"/>
<netui-template:section name="bodySection">
 
<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->
<netui:form action="reports">
<input type="hidden" id="menuId" name="menuId" value="reportsLink" />


<table width="100%" border="0"> 
	<tr>
		<td style="padding-left:5px;">
    		<h1><lb:label key="reports.title" /></h1>
		</td>
	</tr>
	<tr>
		<td colspan="3" class="buttonsRow">
			<div id="showSaveTestMessage" class="errMsgs" style="display: none; width: 50%; float: left;">
				<table>
					<tr>
						<td width="18" valign="middle">
							<div id="errorIcon" style="display:none;">
		                   		<img src="<%=request.getContextPath()%>/resources/images/messaging/icon_error.gif" border="0" width="16" height="16">
							</div>
							<div id="infoIcon" style="display:none;">
								<img src="<%=request.getContextPath()%>/resources/images/messaging/icon_info.gif" border="0" width="16" height="16">
							</div>
						</td>
						<td class="saveMsgs" valign="middle">
							<div id= "saveTestTitle"></div>
						</td>
					</tr>
				</table>
			</div>	       			
		</td>
   	</tr>
	<tr height="400" align="center"> 
		<td>
			 <p align="center"><netui:content value="Content goes here"/></p>
		</td>	
	</tr>
</table>

</netui:form>

<script type="text/javascript">
$(document).ready(function(){
	setMenuActive("reports", null);
});
</script>

<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
    </netui-template:section>
</netui-template:template>


