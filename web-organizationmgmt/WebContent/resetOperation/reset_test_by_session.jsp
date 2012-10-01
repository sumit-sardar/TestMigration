<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="label.tld" prefix="lb"%>
<lb:bundle baseName="organizationApplicationResource" />
<div id="reset_show_by_session" style="width: 99%;">


<table class="sortable" id="reset_show_by_session_table">
	<tr>
		<td width="10px;"> </td>
		<td>
			<BR /><BR />
		</td>
	</tr>
	<tr>
		<td width="10px;"> </td>
		<td class="transparent"><b>Step1:</b> Specify a test access code and click <b>Search</b>. <BR />
			<BR />
			<table class="transparent">
				<tr class="transparent">
					<td class="transparent alignRight"><span>*</span>&nbsp;Access Code:</td>
					<td class="transparent">
						<input type="text" tabindex="1" maxlength="32" id="bySessionTestAccessCode"> </input> </td>
						<td class="transparent" width="*">
							<input type="button"  class="ui-widget-header" value="Search"	type="submit" onClick="getTDAndStudentList(); return false;" tabindex="2" />
						</td>
				</tr>
			</table>
			<BR />
		</td>
	</tr>
	<!--  Steps 2 Starts Here -->
	<tr id="reset_by_session_step2" style="display: none;">
		<td width="10px;"> </td>
		<td class="transparent"><b>Step2:</b>  Select a subtest to reset. <BR />
			<BR />
			 <table class="transparent">
				<tr class="transparent">
					<td class="transparent alignRight">
						<span> Subtest Name :</span>
						
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
		<td class="transparent"><b>Step3:</b>   Select one or more students to reset for the selected section. <BR />
			<BR />
				<div id="reset_by_session_step3_student_list_div" style=" background-color: #FFFFFF; overflow-y: hidden !important; overflow-x: hidden !important;">
					<table id="by_session_step3_student_list" class="gridTable"></table>
					<div id="by_session_step3_student_list_pager" class="gridTable"></div>
					<BR />
					<input id="reset_by_session_step3_next"   type="button"  class="ui-widget-header" value="Next"	type="submit" onClick="populateAndDisplayStep4(); return false;" />
				</div>
			<BR />
		</td>
	</tr>
	
	<!--  Steps 3 ends Here -->
	<!--  Steps 4 Starts Here -->
	<tr id="reset_by_session_step4" style="display: none;">
		<td width="10px;"> </td>
		<td class="transparent"><b>Step4:</b>   Provide reset request information. Enter a brief description of the reason for the request to reset. <BR />
			<BR />
			<table >
				<tr>
					<td  >
					<div style="background-color: rgb(212, 236, 255); font-family: Arial,Verdana,Sans Serif; font-size: 12px; font-style: normal; font-weight: normal; width: auto;" >
					<table >
						<tr >
							<td >
								<table  width="300">
									<tr >
										<td align="right" width="100">Ticket ID:</td>
										<td  width="*">
											<input type="text" tabindex="3" maxlength="32" id="reset_by_session_ticket_id" name="resetBySessionTicketId">
										</td>
									</tr>
									<tr >
										<td  align="right" width="100">Requestor:</td>
										<td  width="*">
												<input type="text" tabindex="4" maxlength="32" id="reset_by_session_service_requestor" name="resetBySessionServiceRequestor">
										</td>
									</tr>
									<tr >
										<td  width="100" align="left" valign="baseline">
											<input id="reset_by_session_step4_reset"   type="button"  class="ui-widget-header" value="Reset"	type="submit" onClick="confirmAndResetTestBySession(); return false;" />
										</td>
										<td  width="*"></td>
									</tr>
								</table>
							</td>
							<td  valign="top">
								<table >
									<tr >
										<td  align="right" width="100">Reason for reset:</td>
										<td  rowspan="3" width="*" valign="top">
											<textarea onkeypress="return blockEventOnMaxLength(this,255)" onkeyup="checkAndTruncate(this,255)" 
												onblur="return checkAndTruncate(this,255)" style="font-family: Arial; font-size: 10pt;" tabindex="5" cols="60" rows="4" id="reset_by_session_request_description" name="resetBySessionRequestDescription"></textarea>
										</td>
									</tr>
									<tr><td class="tableFilter">&nbsp;</td></tr>
								</table>
							</td>
							
						</tr>
						<tr>
							<td colspan="2">
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