<%@ page import="java.io.*, java.util.*"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>

<netui-data:declareBundle bundlePath="oasResources" name="oas" />
<netui-data:declareBundle bundlePath="webResources" name="web" />
<netui-data:declareBundle bundlePath="widgetResources" name="widgets" />
<netui-data:declareBundle bundlePath="helpResources" name="help" />

<netui-template:template templatePage="/resources/jsp/oas_template.jsp">
	<netui-template:setAttribute name="title" value="${bundle.web['reopen.testSession.window.title']}" />
	<netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.reopenTestSession']}" />
	<netui-template:section name="bodySection">
		<%@ taglib uri="label.tld" prefix="lb"%>
		<lb:bundle baseName="organizationApplicationResource" />


		<!-- ********************************************************************************************************************* -->
		<!-- Start Page Content -->
		<!-- ********************************************************************************************************************* -->

		<netui:form action="resetTestSession">

			<input type="hidden" id="menuId" name="menuId" value="hasResetTestSessionLink" />
			<% String studentIdLabelName = (String)request.getAttribute("studentIdLabelName");%>
			<input type="hidden" id="studentIdLabelName"  value = '<%=studentIdLabelName %>' />
			<input type="hidden" id="confirmAlrt" value=<lb:label key="reset.test.by.session.confirmation.title" prefix="'" suffix="'"/>/>
			<input type="hidden" id="resetTestBySessionSuccessMessage" value=<lb:label key="reset.test.by.session.reset.test.success.message" prefix="'" suffix="'"/>/>
			<input type="hidden" id="resetTestBySessionStudentNotFound" value=<lb:label key="reset.test.by.session.find.no.student" prefix="'" suffix="'"/>/>
			<input type="hidden" id="resetTestByStudentStudentNotFound" value=<lb:label key="reset.test.by.student.find.no.student" prefix="'" suffix="'"/>/>
			<input type="hidden" id="resetTestBySessionAccessCodeNotFound" value=<lb:label key="reset.test.by.session.find.no.access.code" prefix="'" suffix="'"/>/>
			<input type="hidden" id="resetTestByStudentSuccessMessage" value=<lb:label key="reset.test.by.student.reset.test.success.message" prefix="'" suffix="'"/>/>
			<input type="hidden" id="resetTestTitle" value=<lb:label key="resetTestSession.title" prefix="'" suffix="'"/>/>
			<input type="hidden" id="resetTestSearchResultTitle" value=<lb:label key="reset.test.search.result" prefix="'" suffix="'"/>/>
			
			<table class="transparent" width="97%" style="margin: 15px auto;">
				<tr class="transparent">
					<td>
					<table class="transparent">
						<tr class="transparent">
							<td>
							<h1><lb:label key="resetTestSession.title" /></h1>
							</td>
						</tr>
						<tr>
							<td class="subtitle"><lb:label key="resetTestSession.message" />
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

			<table width="99%" align="center" class="transparent"  style="margin: 15px auto;" >
				<tr>
					<td width="100%">
						<div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix"
							style="width: 98%; padding-left: 5px; padding-right: 5px;">
	
							<table width="99%">
								<tbody>
									<tr>
										<td align="right">
											<span style="float: right;"><b>Show By&nbsp;</b>:&nbsp;&nbsp;
												<select onchange="updateView();"	id="resetTestBy">
													<option value="ses">&nbsp;Session&nbsp;&nbsp;</option>
													<option value="std">&nbsp;Student&nbsp;&nbsp;</option>
												</select>&nbsp;&nbsp;
											</span>
										</td>
									</tr>
								</tbody>
							</table>
						</div>
					</td>
				</tr>
				<tr >
					<td>
						<jsp:include page="reset_test_by_session.jsp" />
					 	<jsp:include page="reset_test_by_student.jsp" />
					</td>
				</tr>
			</table>
			
			<div id="confirmResetTestBySessionPopup"
				style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
			<div style="padding: 10px; text-align: center;">
			<div style="text-align: left;"><lb:label key="reset.test.by.session.confirmation.message" /></div>
			</div>
			<div style="padding: 10px;">
			<center><input type="button" value=<lb:label key="common.button.yes" prefix="'&nbsp;" suffix="&nbsp;'"/>
				onclick="javascript:resetTest(); return false;" class="ui-widget-header">&nbsp; <input
				type="button" value=<lb:label key="common.button.cancel" prefix="'&nbsp;" suffix="&nbsp;'"/>
				onclick="javascript:closePopUp('confirmResetTestBySessionPopup'); return false;" class="ui-widget-header"></center>
			</div>
			</div>



		</netui:form>

		<script type="text/javascript">
$(document).ready(function(){
	setMenuActive("services", "hasResetTestSessionLink");
}); 
</script>

		<!-- ********************************************************************************************************************* -->
		<!-- End Page Content -->
		<!-- ********************************************************************************************************************* -->
	</netui-template:section>
</netui-template:template>


