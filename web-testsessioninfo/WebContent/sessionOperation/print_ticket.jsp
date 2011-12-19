<%@ page import="java.io.*, java.util.*"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<netui-data:declareBundle bundlePath="webResources" name="web" />
<% 
 Integer scheduleUserOrgNode =  (Integer) session.getAttribute("schedulerUserOrgIds");
//String schedulerUserId = (String) session.getAttribute("schedulerUserId");
%>
<input type="hidden" id="scheduleUserOrgNode" name = "scheduleUserOrgNode" value='<%=scheduleUserOrgNode %>'/>
<input type="hidden" id="selectedTestSessionId" name = "selectedTestSessionId" />

<div id="printTestTicket"
	style="display: none; border:10px solid #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	
<p>To print session documents for a specific organization, select the organization below, then click the document you wish to print.<br/>To print session documents for all organizations, just click the documents you wish to print.</p>
<h3 style="border-width: 0px"><span>Test Information</span></h3>
			
<table class="transparent">
    <tr class="transparent">
        <td class="transparent" nowrap><span style="font-weight:bold;">Test name:</span></td>
        <td class="transparent" ><span id = "testName_val" style="background-color:white;"></span></td>
        <td class="transparent" nowrap><span style="font-weight:bold;">Test session name:</span></td>
        <td class="transparent" > <span id = "adminTestName_val" style="background-color:white;"></span></td>
	</tr>
</table>
</p>
<br/>
<div style="width:100%;clear:both;float:left;">
	<div style="width:50%;float:left; padding-right: 5px;" id="orgTktTreeDiv">
		
	</div>
	
	<div style="width:49%;float:right;">
	<h3 style="border-width: 0px; margin-bottom:15px;">Test session documents</h3>
	<p>	
	Click the links to display the documents you wish to print.<br>
	</p>
	<p>
	<a href="#" style="text-decoration:underline;color:blue;" onclick="return openTestTicketIndividual(this, document.getElementById('selectedTestSessionId').value, document.getElementById('scheduleUserOrgNode').value);">Individual Test Tickets</a>
	&nbsp;
	<img src="/SessionWeb/resources/images/logo_pdf.gif" border="0">
	<br/>
	These tickets have the login names and passwords your students need to take the test.
	<br>
	
	</p>
	
	<!--START - Added For CR ISTEP2011CR007 (Multiple Test Ticket)-->
	<p>
	<a href="#" style="text-decoration:underline;color:blue;" onclick="return openTestTicketMultiple(this, document.getElementById('selectedTestSessionId').value, document.getElementById('scheduleUserOrgNode').value);">Multiple Test Tickets</a>
	&nbsp;
	<img src="/SessionWeb/resources/images/logo_pdf.gif" border="0">
	<br/>
	This format allows printing of multiple individual test tickets on a sheet when using the pdf print options.
	</p>
	<!--END - Added For CR ISTEP2011CR007 (Multiple Test Ticket)-->
	
	<p>
	<a href="#" style="text-decoration:underline;color:blue;" onclick="return openTestTicketSummary(this, document.getElementById('selectedTestSessionId').value, document.getElementById('scheduleUserOrgNode').value);">Summary Test Ticket</a>
	&nbsp;<img src="/SessionWeb/resources/images/logo_pdf.gif" border="0">
	<br/>
	This summary has the information your proctor needs to administer the test: Student names, login IDs, passwords, and test access code(s).
	</p>
	</div>		
	</div>
	
			<table cellspacing="0" cellpadding="0" border="0" id="TblGrid_list2_2" class="EditTable" width="100%">
					<tr id="Act_Buttons" align="right">
						<td  width="100%" style="text-align:center;">
							
								<input type="button" id="cData" value="&nbsp;Cancel&nbsp;&nbsp;" onclick="javascript:closePopUp('printTestTicket'); return false;" class="ui-widget-header" style="width:60px">
							
							<br>
						</td>
					</tr>						
			</table>
		
</div>
