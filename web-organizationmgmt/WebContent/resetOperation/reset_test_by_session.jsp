<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="label.tld" prefix="lb"%>
<lb:bundle baseName="organizationApplicationResource" />
<div id="reset_show_by_session" style="">


<table class="sortable" id="reset_show_by_session_table">
	<tr>
		<td width="10px;"> </td>
		<td>
			<BR /><BR />
		</td>
	</tr>
	<tr>
		<td width="10px;"> </td>
		<td class="transparent"><lb:label key="reset.test.by.session.step1.message" />  <BR />
			<BR />
			<table class="transparent">
				<tr class="transparent">
					<td class="transparent alignRight"><span>*</span>&nbsp;<lb:label key="reset.test.title.access.code" /> </td>
					<td class="transparent">
						<input type="text" tabindex="1" maxlength="32" id="bySessionTestAccessCode"> </input> </td>
						<td class="transparent" width="*">
							<input type="button"  class="ui-widget-header" value=<lb:label key="reset.test.title.search" prefix="'&nbsp;&nbsp;&nbsp;" suffix="&nbsp;&nbsp;&nbsp;'"/>	type="submit" onClick="getTDAndStudentList(); return false;" tabindex="2" />
						</td>
				</tr>
			</table>
			<BR />
		</td>
	</tr>
	<!--  Steps 2 Starts Here -->
	<tr id="reset_by_session_step2" style="display: none;">
		<td width="10px;"> </td>
		<td class="transparent"><lb:label key="reset.test.by.session.step2.message" />  <BR />
			<BR />
			 <table class="transparent">
				<tr class="transparent">
					<td class="transparent alignRight">
						<span> <lb:label key="reset.test.column.title.subtest.name" /></span>
						
					</td>
					<td class="transparent" id="reset_by_session_step2_subtest_name">
						
					</td>
				</tr>
			</table>
			<BR />
		</td>
	</tr>
	<!--  Steps 2 ends Here -->
	<!--  Steps 3 Starts Here -->
	<tr id="reset_by_session_step3" style="display: none;">
		<td width="10px;"> </td>
		<td class="transparent"><lb:label key="reset.test.by.session.step3.message" /> <BR />
			<BR />
				<div id="reset_by_session_step3_student_list_div" style=" background-color: #FFFFFF; overflow-y: hidden !important; overflow-x: hidden !important;">
					<table id="by_session_step3_student_list" class="gridTable"></table>
					<div id="by_session_step3_student_list_pager" class="gridTable"></div>
					<BR />
					<input id="reset_by_session_step3_next"   type="button"  class="ui-widget-header" value=<lb:label key="reset.test.title.next" prefix="'&nbsp;&nbsp;&nbsp;" suffix="&nbsp;&nbsp;&nbsp;'"/>	type="submit" onClick="populateAndDisplayStep4BySession(); return false;" />
				</div>
			<BR />
		</td>
	</tr>
	
	<!--  Steps 3 ends Here -->
	<!--  Steps 4 Starts Here -->
	<tr id="reset_by_session_step4" style="display: none;">
		<td width="10px;"> </td>
		<td class="transparent"><lb:label key="reset.test.by.session.step4.message" />  <BR />
			<table >
				<tr>
					<td align="right" style="padding-bottom: 5px">
						<input id="reset_by_session_step4_reset"   type="button"  class="ui-widget-header" value=<lb:label key="reset.test.title.reset" prefix="'&nbsp;&nbsp;&nbsp;" suffix="&nbsp;&nbsp;&nbsp;'"/>	type="submit" onClick="confirmAndResetTestBySession(); return false;" />
					</td>
				</tr>
				<tr>
					<td  >
					<div style="background-color: rgb(212, 236, 255); font-family: Arial,Verdana,Sans Serif; font-size: 12px; font-style: normal; font-weight: normal; width: auto;" >
					<table >
						<tr >
							<td >
								<table  class="transparent" width="300">
									<tr class="transparent">
										<td class="transparent" align="right" width="100"><lb:label key="reset.test.column.title.ticket.id" /> </td>
										<td  class="transparent" width="*">
											<input type="text" tabindex="3" maxlength="32" id="reset_by_session_ticket_id" name="resetBySessionTicketId">
										</td>
									</tr>
									<tr class="transparent">
										<td  class="transparent" align="right" width="100"><lb:label key="reset.test.column.title.requester" /> </td>
										<td  class="transparent" width="*">
												<input type="text" tabindex="4" maxlength="32" id="reset_by_session_service_requestor" name="resetBySessionServiceRequestor">
										</td>
									</tr>
									
								</table>
							</td>
							<td  valign="top">
								<table >
									<tr class="transparent">
										<td  class="transparent"align="right" width="100"><lb:label key="reset.test.column.title.requester" /> </td>
										<td  class="transparent" rowspan="3" width="*" valign="top">
											<textarea onkeypress="return blockEventOnMaxLength(this,255)" onkeyup="checkAndTruncate(this,255)" 
												onblur="return checkAndTruncate(this,255)" style="font-family: Arial; font-size: 10pt;" tabindex="5" cols="60" rows="4" id="reset_by_session_request_description" name="resetBySessionRequestDescription"></textarea>
										</td>
									</tr>
									<tr class="transparent"><td class="tableFilter">&nbsp;</td></tr>
								</table>
							</td>
							<td valign="bottom">
								<!-- input id="reset_by_session_step4_reset"   type="button"  class="ui-widget-header" value="Reset"	type="submit" onClick="confirmAndResetTestBySession(); return false;" /-->
							</td>
							
						</tr>
						<tr class="transparent">
							<td class="transparent" colspan="3">
								<div id="reset_by_session_step4_student_list_div" style=" background-color: #FFFFFF; overflow-y: hidden !important; overflow-x: hidden !important;">
									<table id="by_session_step4_student_list" class="gridTable"></table>
									<div id="by_session_step4_student_list_pager" class="gridTable"></div>
								</div>
							</td>
						</tr>
					</table>
					</div>
					</td>
				</tr>
			
			
		</table>
			
			<BR />
		</td>
	</tr>
	
	<!--  Steps 4 ends Here -->
</table>
</div>