<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="label.tld" prefix="lb"%>
<lb:bundle baseName="organizationApplicationResource" />
<div id="reset_show_by_student" style="display: none; width: 99%;">


<table class="sortable">

	<tr>
	 	<td width="10px;"> </td>
		<td><BR />
		<BR />
		</td>
	</tr>
	<tr>
		<td width="10px;"> </td>
		<td class="transparent" ><b>Step1:</b> Specify student login (required) and test access code (optional) and click <b>Search</b>. <BR />
		<BR />
		<table class="transparent">
			<tr class="transparent">
					<td class="transparent"><span >*</span>&nbsp;Student Login:</td>
					<td class="transparent" width="*">
						<input type="text" id="byStudentLoginnID" tabindex="1" maxlength="32" name="studentLoginId"> </input>
					</td>
				</tr>
				<tr class="transparent">
					<td class="transparent">Access Code:</td>
					<td class="transparent"  width="*">
						<input type="text" id="byStudentTestAccessCode" tabindex="2" maxlength="32" name="studentTestAccessCode"> </input>
					</td>
					 <td  class="transparent" width="*">
					 &nbsp;&nbsp;<input class="ui-widget-header" type="button"  value="Search"	type="submit" onClick="getTestSessionListByStudentStep2(); return false;" tabindex="3" />
					
					</td>
				</tr>
		</table>
		<BR />
		</td>
	</tr>
	
	<!--  Steps 2 Starts Here -->
	<tr id="reset_by_student_step2" style="display: none;">
		<td width="10px;"> </td>
		<td class="transparent"><b>Step2:</b>  Select a test session to view its subtest. <BR />
			<BR />
			 	<div id="reset_by_student_step2_student_list_div" style=" background-color: #FFFFFF; overflow-y: hidden !important; overflow-x: hidden !important;">
					<table id="by_student_step2_student_list" class="gridTable"></table>
					<div id="by_student_step2_student_list_pager" class="gridTable"></div>
					<BR />
				</div>
			<BR />
		</td>
	</tr>
	<!--  Steps 2 ends Here -->
	
	<!--  Steps 3 Starts Here -->
	<tr id="reset_by_student_step3" style="display: none;">
		<td width="10px;"> </td>
		<td class="transparent"><b>Step3:</b>  Select a subtest to reset. <BR />
			<BR />
			 	<div id="reset_by_student_step3_student_subtest_list_div" style=" background-color: #FFFFFF; overflow-y: hidden !important; overflow-x: hidden !important;">
					<table id="by_student_step3_student_subtest_list" class="gridTable"></table>
					<div id="by_student_step3_student_subtest_list_pager" class="gridTable"></div>
					<BR />
				</div>
			<BR />
		</td>
	</tr>
	<!--  Steps 3 ends Here -->
	<!--  Steps 4 Starts Here -->
	<tr id="reset_by_student_step4" style="display: none;">
		<td width="10px;"> </td>
		<td class="transparent"><b>Step4:</b>   Provide reset request information. Enter a brief description of the reason for the request to reset. <BR />
			<BR />

			<table  width="98%;">
				<tr>
					<td  >
					<div style="background-color: rgb(212, 236, 255); font-family: Arial,Verdana,Sans Serif; font-size: 12px; font-style: normal; font-weight: normal; width:100%;" >
					<table  width="100%">
						<tr >
							<td >
								<table  class="transparent" width="300">
									<tr class="transparent">
										<td class="transparent" align="right" width="100">Ticket ID:</td>
										<td  class="transparent" width="*">
											<input type="text" tabindex="3" maxlength="32" id="reset_by_student_ticket_id" name="resetBySessionTicketId">
										</td>
									</tr>
									<tr class="transparent">
										<td  class="transparent" align="right" width="100">Requestor:</td>
										<td  class="transparent" width="*">
												<input type="text" tabindex="4" maxlength="32" id="reset_by_student_service_requestor" name="resetBySessionServiceRequestor">
										</td>
									</tr>

									<tr class="transparent">
										<td class="transparent" colspan="3">
										
										</td>
									</tr>
						</table>
								
							</td>
							<td  valign="top">
								<table >
									<tr class="transparent">
										<td  class="transparent"align="right" width="100">Reason for reset:</td>
										<td  class="transparent" rowspan="3" width="*" valign="top">
											<textarea onkeypress="return blockEventOnMaxLength(this,255)" onkeyup="checkAndTruncate(this,255)" 
												onblur="return checkAndTruncate(this,255)" style="font-family: Arial; font-size: 10pt;" tabindex="5" cols="60" rows="4" id="reset_by_student_request_description" name="resetByStudentRequestDescription"></textarea>
										</td>
									</tr>
									<tr class="transparent"><td class="tableFilter">&nbsp;</td></tr>
								</table>
							</td>
							<td valign="bottom">
								<input id="reset_by_student_step4_reset"   type="button"  class="ui-widget-header" value="Reset"	type="submit" onClick="confirmAndResetTestBySession(); return false;" />
							</td>
							
						</tr>
						<tr class="transparent">
							<td class="transparent" colspan="3">
								<!--  -->
											<table class="sortable" width="100%">
			
												<tbody>
													<tr class="transparent">
														<th nowrap="" width="30%" style="height: 25px;" class="ui-state-default alignLeft">&nbsp;&nbsp;<span>Student</span></th>
														<th nowrap="" width="30%" style="height: 25px;" class="ui-state-default alignLeft">&nbsp;&nbsp;<span>Test Session</span></th>
														<th nowrap="" width="40%" style="height: 25px;" class="ui-state-default alignLeft">&nbsp;&nbsp;<span>Subtest</span></th>
													</tr>
			
													<tr class="sortable">
														<td width="30%" class="sortable">
														<table class="transparent">
															<tbody>
																<tr valign="top" class="transparent">
																	<td class="transparent"><span>Name:</span></td>
																	<td class="transparent"><span id="byStudentStudentNameStep4" > </span></td>
																</tr>
																<tr valign="top" class="transparent">
																	<td class="transparent"><span>Login:</span></td>
																	<td class="transparent"><span id="byStudentStudentLogInNameStep4" ></span></td>
																</tr>
																<tr valign="top" class="transparent">
			
			
																	<td class="transparent"><span id="byStudentConfIdStep4"> </span></td>
			
																	<td class="transparent"><span id="byStudentStudentIdStep4"></span></td>
																</tr>
															</tbody>
														</table>
														</td>
			
														<td width="30%" class="sortable">
														<table class="transparent">
															<tbody>
																<tr class="transparent">
																	<td valign="top" class="transparent"><span>Name:</span></td>
																	<td nowrap="true" class="transparent"><span id="byStudentSessionNameStep4"> </span></td>
																</tr>
																<tr valign="top" class="transparent">
																	<td class="transparent"><span>ID:</span></td>
																	<td class="transparent"><span id="byStudentSessionId" > </span></td>
																</tr>
																<tr valign="top" class="transparent">
																	<td class="transparent">&nbsp;</td>
																	<td class="transparent">&nbsp;</td>
																</tr>
															</tbody>
														</table>
														</td>
			
														<td width="40%" class="sortable">
														<table class="transparent">
															<tbody>
																<tr valign="top" class="transparent">
																	<td class="transparent"><span>Name:</span></td>
																	<td nowrap="true" colspan="3" class="transparent"><span id="byStudentSubtestNameStep4" > </span></td>
																</tr>
																<tr valign="top" class="transparent">
																	<td class="transparent"><span>Status:</span></td>
																	<td colspan="3" class="transparent"><span id="byStudentSubtestStatus"></span></td>
																</tr>
																<tr valign="top" class="transparent">
																	<td nowrap="true" align="left" width="25%" class="transparent"><span>Access Code:</span></td>
																	<td align="left" width="30%" class="transparent"><span id="byStudentSubtestAccessCodeStep4" > </span></td>
																	<td align="right" width="25%" class="transparent"><span>Order:</span></td>
																	<td align="left" width="20%" class="transparent"><span id="byStudentSubtestOrderStep4"></span></td>
																</tr>
															</tbody>
														</table>
														</td>
													</tr>
			
												</tbody>
											</table>


								<!--  -->
								
								
								
								
								
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