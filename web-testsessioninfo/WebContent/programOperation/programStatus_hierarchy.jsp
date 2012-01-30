<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="label.tld" prefix="lb" %>
<netui-data:declareBundle bundlePath="webResources" name="web" />
<lb:bundle baseName="testsessionApplicationResource" />

<div>
	<input type="hidden" id="customerNamePS" name = "customerNamePS" value='<%=session.getAttribute("customerName")%>'/>
	<input type="hidden" id="programIdPS" name = "programIdPS" value=""/>
	<input type="hidden" id="programNamePS" name = "programNamePS" value=""/>
	<input type="hidden" id="selectedTestIdPS" name = "selectedTestIdPS" value=""/>
	<input type="hidden" id="testNamePS" name = "testNamePS" value=""/>
	<input type="hidden" id="subtestIdPS" name="subtestIdPS" value=""/>
	<input type="hidden" id="statusPS" name="statusPS" value=""/>
	<input type="hidden" id="searchSessionID" name = "searchSessionID" value=<lb:label key="testSession.label.searchSessID" prefix="'" suffix="'"/>/>
	
	<input type="hidden" id="clickableSubtestMsgID" name="clickableSubtestMsgID" value=<lb:label key="programStatus.clickableSubtest.message" prefix="'" suffix="'"/>/>
		<input type="hidden" id="noTestMsgID" name="noTestMsgID" value=<lb:label key="programStatus.noTest.message" prefix="'" suffix="'"/>/>
	<input type="hidden" id="tblHeaderTitle1" name="tblHeaderTitle1" value=<lb:label key="programStatus.label.testLbl" prefix="'" suffix="'"/>/>
	<input type="hidden" id="tblHeaderTitle2" name="tblHeaderTitle2" value=<lb:label key="programStatus.label.scheduledLbl" prefix="'" suffix="'"/>/>
	<input type="hidden" id="tblHeaderTitle3" name="tblHeaderTitle3" value=<lb:label key="programStatus.label.startedLbl" prefix="'" suffix="'"/>/>
	<input type="hidden" id="tblHeaderTitle4" name="tblHeaderTitle4" value=<lb:label key="programStatus.label.completedLbl" prefix="'" suffix="'"/>/>
	<input type="hidden" id="tblHeaderTitle5" name="tblHeaderTitle5" value=<lb:label key="programStatus.label.subtestsLbl" prefix="'" suffix="'"/>/>
	
	<input type="hidden" id="jqgSessionName" name = "jqgSessionName" value=<lb:label key="homepage.grid.sessionName" prefix="'" suffix="'"/>/>
	<input type="hidden" id="jqgSessionID" name = "jqgSessionID" value=<lb:label key="programStatus.grid.sessionIdLbl" prefix="'" suffix="'"/>/>
	<input type="hidden" id="jqgLoginID" name = "jqgLoginID" value=<lb:label key="viewStatus.text.loginId" prefix="'" suffix="'"/>/>
	<input type="hidden" id="jqgPassword" name = "jqgPassword" value=<lb:label key="viewStatus.text.password" prefix="'" suffix="'"/>/>
	<input type="hidden" id="jqgAccessCode" name = "jqgAccessCode" value=<lb:label key="programStatus.grid.accessCodeLbl" prefix="'" suffix="'"/>/>
<div>

<table class="transparent" width="97%" style="margin:15px auto;"> 
	<tr class="transparent">
		<td>
    		<table class="transparent">
				<tr class="transparent">
					<td>
			    		<h1><lb:label key="assessments.programStatus.title" /></h1>
					</td>
				</tr>
				<tr> 
				<td class="subtitle">  
					<lb:label key="programStatus.information.message" />
				</td>	
			</tr>
			</table>		
		</td>
	</tr>
    <tr class="transparent">
        <td align="center">        
		<table width="100%">
		   <tr>
				<td colspan="3" class="buttonsRow">
					<div id="displayPSMessageMain" class="errMsgs" style="display: none; width: 50%; float: left;">
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
									<div id= "contentPSMain"></div>
								</td>
							</tr>
						</table>
					</div>	       			
				</td>
		   	</tr>
	    	<tr class="transparent">
		        <td style="vertical-align:top; width:16%;" align="left">
			      	<div  id= "searchheader" class="ui-corner-tl ui-corner-tr ui-widget-header treeCtrlHeader">&nbsp;<lb:label key="programStatus.label.search" /></div>
			    	<script>populateProgramStatusTree();</script>
			
			    	<div id="outertreebcgdiv" class="treeCtrl" style="height:512px !important">
				    	<div id = "programStatusOrgNode" style="width:auto;height:auto;display:table">
						</div>
					</div>
			 	</td>
			 	
	 			<td class="transparent" width="5px">&nbsp;</td>
		 	
		 		<td style="vertical-align:top;" id="jqGrid-content-section">
	      			<table width="100%" align="left" style="text-align:left">
		      			<tr>
							<td>		      			
			      				<div id="programInfo" style="display:none">
			      					<table class="transparent" style="width:920px;border: 1px solid #A6C9E2;">
			      						<tr class="subtestHeader" >
								      		<th style="text-align:center;"><lb:label key="programStatus.label.programInfoHeader" /></th>
							      		</tr>
			      						<tr class="transparent">
			      							<td style ="vertical-align: top;"> 
				      							<table>
											 		<tbody>
											 			<tr class="transparent">
															<td nowrap="" width="110" class="transparent"><lb:label key="programStatus.label.customerLbl" prefix="" suffix="&nbsp;:" /></td>
															<td class="transparent"><label style="width: 200px;" id="customerNameId"></label></td>
														</tr>
														<tr class="transparent">
															<td nowrap="" width="110" class="transparent"><lb:label key="programStatus.label.programLbl" prefix="" suffix="&nbsp;:" /></td>
															<td class="transparent"><label style="width: 200px;" id="programNameId"></label></td>
														</tr>
														<tr class="transparent">
															<td nowrap="" width="110" class="transparent"><lb:label key="programStatus.label.orgLbl" prefix="" suffix="&nbsp;:" /></td>
															<td class="transparent"><label style="width: 200px;" id="orgNameId"></label></td>
														</tr>
														<tr class="transparent">
															<td width="110" class="transparent"><lb:label key="programStatus.label.testLbl" prefix="" suffix="&nbsp;:" /></td>
															<td class="transparent">
																<label style="width: 200px;" id="testNameId"></label>
																<select id="testNameOptions"  name="testNameOptions" style="width: 200px; display:none; font-size: 12px;" onchange="javascript: getSubtestDetailsForSelectedTest();"></select>
															</td>
														</tr>
											 		</tbody>
									 			</table>
			      							</td>
			      						</tr> 
			      					</table> 
			      				</div>
		      				</td>
		      			</tr>
		      			<tr>
		      				<td>
		      					<h2><div id="testStatusTitleID" style="display:none; margin-top:20px; color:#476CB5"></div></h2>
		      					<div id="clickableSubtestMsg" class="saveMsgs" style="display:none;margin-top:5px"></div>
		      				</td>
		      			</tr>
		      			<tr>
			      			<td>
			      				<div id="testStatusInfo" style="margin-top: 10px"> 
			      					<table id="subtestInfoList" style="width:920px;border: 1px solid #A6C9E2;bgcolor=#A6C9E2;" cellpadding="0" cellspacing="0"></table>
								</div>
			      			</td>	
		      			</tr>
		      			<tr> 
		      				<td>
		      					<div id="subtestStatusInfo" style="margin-top: 20px; display: none;">
			      					<table id="buttonTbl" style="width: 920px;  margin-bottom: 5px;">
										<tr>
											<td width="45%" class="buttonsRow">
												<div id="exportToExcel" style="float:right;padding-left:5px;display:none;">
													<a href="#" id="exportToExcelButton" onclick="javascript:exportToExcel();" class="rounded {transparent} button"><lb:label key="programStatus.button.exportToExcel" /></a>
												</div>
											</td>
										</tr>
									</table>
		      						<table id="subtestStatusInfoList" class="gridTable"></table>
		      							<div id="subtestStatusInfoPager" class="gridTable"></div>
		      					</div>
		      				</td>
		      			</tr>
	      			</table>
			
		 		</td>
	    	</tr>
		</table>
        </td>
    </tr>
</table>
<div id="searchGridByKeyword"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<div>
		<p><lb:label key="sessionList.search.info.message"/></p>
	</div>
	<div class="searchInputBoxContainer" id="searchInputBoxContainerGrid">
		<center>
			<input type="text" name="searchGridByKeywordInput" id="searchGridByKeywordInput" onkeypress="trapEnterKey(event);"/>
		</center>	
	</div>
	<div style="padding-bottom:20px;">
		<center>
			<input type="button"  value="Reset" onclick="javascript:resetSearchList(); return false;" class="ui-widget-header">&nbsp;
			<input type="button"  value="Search" onclick="javascript:searchGridByKeyword(); return false;" class="ui-widget-header">
		</center>
	</div>
</div>