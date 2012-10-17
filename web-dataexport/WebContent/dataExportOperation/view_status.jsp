<%@ page import="java.io.*, java.util.*"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<netui-template:template templatePage="/resources/jsp/oas_template.jsp">

<netui-template:section name="bodySection">
	<%@ taglib uri="label.tld" prefix="lb" %>
	<lb:bundle baseName="dataExportApplicationResource" />
		
	<netui:form action="beginViewStatus">
			
	
	<input type="hidden" id="jqgJobID" name="jqgJobID" value=<lb:label key="dataexport.info.jobid" prefix="'" suffix="'"/>/>
	<input type="hidden" id="jqgJobSubmissionDate" name="jqgJobSubmissionDate" value=<lb:label key="dataexport.info.jobsubmissiondate" prefix="'" suffix="'"/>/>
	<input type="hidden" id="jqgStudentCount" name="jqgStudentCount" value=<lb:label key="dataexport.info.studentcount" prefix="'" suffix="'"/>/>
	<input type="hidden" id="jqgJobStatus" name="jqgJobStatus" value=<lb:label key="dataexport.info.jobstatus" prefix="'" suffix="'"/>/>
	<input type="hidden" id="statusListID" name="statusListID" value=<lb:label key="dataexport.info.statusListID" prefix="'" suffix="'"/>/>
	<input type="hidden" id="menuId" name="menuId" value="viewStatusLink" />

			
			
			<table class="transparent" width="97%" style="margin: 15px auto;">
				<tr class="transparent">
					<td>
					<table class="transparent">
						<tr class="transparent">
							<td>
							<h1><lb:label key="dataExport.viewStatus.title" /></h1>
							</td>
						</tr>
						<tr>
							<td class="subtitle"><lb:label key="dataExport.viewStatus.message" />
						</tr>
					</table>
					</td>
				</tr>

			</table>

			<div id = "displayMessage" 
					style="display: none; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal; margin-bottom:5px; padding:2px 2px 2px 10px; color: black;">	
				<table width="99.5%">
					<tbody>
						<tr>
							<td valign="middle" width="18">
								<img height="16" src="<%=request.getContextPath()%>/resources/images/messaging/icon_info.gif">
							</td>
							<td valign="middle" >
								<div id="messageTitle" style="display:none;font-weight:bold;"></div>
								<div id="message" style="display:none;"></div>
								
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<table width="97%" align="center" class="transparent"  style="margin: 15px auto;" >
				<tr height = "25px;">
				</tr>
				<tr>
					<td width="100%">
						<p id="viewStstusDesc"><lb:label key="data.export.viewStatus.message" /></p>
						<table id="list2"></table>
						<div id="pager2"></div>
					</td>
				</tr>
			</table>









</netui:form>
 
<script type="text/javascript">
$(document).ready(function(){
	setMenuActive("services", "usersLink");
});
</script>
        
<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
</netui-template:section>
</netui-template:template>
 
