<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="label.tld" prefix="lb" %>
<lb:bundle baseName="studentRegistrationResource" />
<input type="hidden" id="dorgNodeId" name="orgNodeId" value="" />
<div id="studentConfirmation" 
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<div id="studRegInfo" style ="background-color: #FFFFFF; padding-left: 15px;">
	 <table width="100%" cellpadding="0" cellspacing="0" class="transparent">
	    <tr>
	        <td nowrap="">
	            <h1>
	            <lb:label key="register.stud.congrat.header.caption" />
	            </h1>
	        </td>
	    
	        <td width="65%" class="transparent"></td>
	        <td width="100%" rowspan="2" align="right" valign="top">
	        	&nbsp;
	        </td>
	    
	        <td rowspan="2">
	            <table width="25"><tr><td></td></tr></table>
	        </td>
	    </tr>
	    
	    <tr>   
	        <td width="65%" class="transparent">
	            <p>
	             <lb:label key="register.stud.congrat.header.info" />
	              <br/>
	            </p>
	        </td>
	        <td width="62%" class="transparent"></td> 
	    </tr>
	</table> 
<!--End of change for licnese-->

<%-- message 
<jsp:include page="/registration/show_message.jsp" /> --%>


<!-- student section -->
	<p>
	<div id="StudentDetail">
		<table align="center" width="95%">
			<tr>
				<td>
					<div class="ui-widget-header"><lb:label key="student.registration.showBy.student" /></div>
					<table class="sortable" width="100%">
						<tr class="transparent">
							<td class="transparent" width="50%">
							    <table class="transparent">
							        <tr class="transparent">
							            <td class="transparent" width="120"><span><lb:label key="stu.label.stdName" /></span></td>
							            <td class="transparent" width="*"><span id="dstudentName"> </span></td>
							        </tr>
							        <tr class="transparent">
							            <td class="transparent" width="120"><span><lb:label key="stu.label.accessCode" /></span></td>
							            <td class="transparent" width="*">
							             <span id="enforceBreakNo" style="display: none">  </span>
							             <span id="enforceBreakYes" style="display: none"> &nbsp; </span>
							            </td>      
							            <!--<td class="transparent" width="*"><span></span></td> -->
							            </tr>
							    </table>
							</td>
							<td class="transparent" width="50%">
							    <table class="transparent">
							        <tr class="transparent">
							            <td class="transparent" width="120"><span><lb:label key="stu.label.loginID" /></span></td>
							            <td class="transparent" width="*"><span id="dloginName"></span></td>
							        </tr>
							        <tr class="transparent">
							            <td class="transparent" width="120"><span><lb:label key="stu.label.password" /></span></td>
							            <td class="transparent" width="*"><span id="dpassword"></span></td>
							        </tr>
							    </table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</div>
	<p/>
	
	<!-- test section -->
	<p>
	<div id="StudentDetail">
	<table align="center" width="95%">
		<tr>
			<td>
				<div class="ui-widget-header"><lb:label key="stu.label.testSession" /></div>
				  <div id="StudentTableDetail"> <div>
					<table class="sortable" width="100%">
						<tr class="transparent">
							<td class="transparent" width="50%">
							    <table class="transparent">
							        <tr class="transparent">
							            <td class="transparent" width="120"><span><lb:label key="stu.label.testName" /></span></td>
							            <td class="transparent" width="*"><span id="dtestName"></span></td>
							        </tr>
							        <tr class="transparent">
							            <td class="transparent" width="120"><span><lb:label key="stu.label.organization" /></span></td>
							            <td class="transparent" width="*"><span id="dcreatorOrgNodeName"></span></td>
							        </tr>
							        <tr class="transparent">
							            <td class="transparent" width="120"><span><lb:label key="stu.label.startDate" /></span></td>
							            <td class="transparent" width="*"><span id="dstartDate"></span></td>
							        </tr>
							        <tr class="transparent">
							            <td class="transparent" width="120"><span><lb:label key="stu.label.loginStartTime" /></span></td>
							            <td class="transparent" width="*"><span id="dstartTime"></span></td>
							        </tr>
							        <tr class="transparent" id="dDisplayAutolocator">
							            <td class="transparent" width="120"><span><lb:label key="stu.label.locatorTest" /></span></td>
							            <td class="transparent" width="*"><span id="dautoLocatorDisplay"></span></td>
							        </tr>
							    </table>
							</td>
							<td class="transparent" width="50%">
							    <table class="transparent">
							        <tr class="transparent">
							            <td class="transparent" width="120"><span><lb:label key="stu.label.testSessionName" /></span></td>
							            <td class="transparent" width="*"><span id="dtestAdminName"></span></td>
							        </tr>
							        <tr class="transparent">
							            <td class="transparent" width="120"><span><lb:label key="stu.label.testSessionID" /></span></td>
							            <td class="transparent" width="*"><span id="dsessionNumber"></span></td>
							        </tr>
							        <tr class="transparent">
							            <td class="transparent" width="120"><span><lb:label key="stu.label.endDate" /></span></td>
							            <td class="transparent" width="*"><span id="dendDate"></span></td>
							        </tr>
							        <tr class="transparent">
							            <td class="transparent" width="120"><span><lb:label key="stu.lable.loginEndTime" /></span></td>
							            <td class="transparent" width="*"><span id="dendTime"></span></td>
							        </tr>
							        <tr class="transparent" id="dShowHideAllowBreak">
							            <td class="transparent" width="120"><span><lb:label key="stu.label.allowsBreaks" /></span></td>
							            <td class="transparent" width="*"><span id="denforceBreak"></span></td>
							        </tr>
							    </table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</div>
	<p/>
	
	<!-- Test Structure Section -->
	<p>
		<div id="locatorDiv">
		<table align="center" width="95%">
			<tr>
				<td>
					<div class="ui-widget-header"><lb:label key="stu.label.testStructure" /></div>
						<table class="sortable">
					    	<tr class="sortable">
					        	<th class="sortable alignLeft" height="25" width="*">
					        	&nbsp;<span><lb:label key="stu.label.subtest" /></span>
					        	</th>
					        	
					        	<th id="loc_accesscode_header"  class="sortable alignLeft" height="25" width="*">
					        	&nbsp;<span><lb:label key="stu.label.subtest" /></span>
					        	</th>
					        	
					    	</tr>
					    	<tr class="sortable">
					        	<td class="sortable alignLeft" width="*">
					        		<span id="loc_subtest_name"></span>
					        	</td>
					        	<td id="loc_accesscode_value" class="sortable alignLeft" width="*">
					        		<span id="loc_subtest_name"></span>
					        	</td>
					    	</tr>
					    	
						</table>
					</div>
				</td>
			</tr>
		</table>
		</div>
	</p>
	<br/>
 <div>
	
<div id="SubtestDetails"> </div>

<br/>
<br/>
	<div id="showAccessCode" style="display: none; float: left; padding: 5px;">
		<p>
		   <lb:label key="register.stud.congrat.testticket.instruction" />
			<br/>
			<input type="radio" id="allow" name="individualAccess" value="Yes" onclick=""><lb:label key="common.button.yes" /></input>
			<input type="radio" id="deny" name="individualAccess" value="No"  onclick="" checked="checked"><lb:label key="common.button.no" /></input>
		</p>
	</div>
		<p align="right" style="padding-right: 15px">
		    <a href="#" style='color:blue; text-decoration:underline;' onClick="return openTestTicketIndividual(this);">
		   	 <lb:label key="stu.label.testTicket" />
		    </a>
		    &nbsp;<img src="/RegistrationWeb/resources/images/logo_pdf.gif" border="0">
		</p>
	
	<br/>
	
	</div>
	<br/>
	<div id="StudConfClose">
		<center>
			<input type="button"  id="sData" value="Done" onclick="javascript:closePopUp('studentConfirmation'); return false;" class="ui-widget-header">
		</center>
		<br>
	</div>	
</div> 

