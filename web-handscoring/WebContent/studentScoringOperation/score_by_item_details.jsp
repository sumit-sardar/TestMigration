<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="label.tld" prefix="lb" %>
<lb:bundle baseName="studentScoringResources" />
<input type="hidden" id="grdSessionName" value=<lb:label key="scoring.common.itemNumber" prefix="'" suffix="'"/>/>
<input type="hidden" id="grdSessionName" value=<lb:label key="scoring.common.subtestName" prefix="'" suffix="'"/>/>
<input type="hidden" id="grdSessionName" value=<lb:label key="scoring.common.itemType" prefix="'" suffix="'"/>/>
<input type="hidden" id="grdSessionName" value=<lb:label key="scoring.common.maxScore" prefix="'" suffix="'"/>/>
<input type="hidden" id="itemListGridCaption" value=<lb:label key="scoring.itemGrid.caption" prefix="'" suffix="'"/>/>

<div id="itempopupid" style="background-color: #FFFFFF; padding:20px; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	
  <table class="transparent" width="100%" style="margin:15px auto;">
	<tr class="transparent">
        <td>
		<table class="transparent">
			<tr> 
				<td class="subtitle">  
					<lb:label key="scoring.itempopup.message" />
				</td>	
			</tr>
		</table>
		</td>
	</tr>
	<tr class="transparent">
		<td>
	  	<table width="100%">
	  		<tr class="transparent">
					<td class="transparent">Test Access Code:</td>
					<td class="transparent">
						<div class="formValueLarge"><span class="formValueLarge" id="testaccesscode"></span></div>
					</td>
			</tr>
			<tr class="transparent">
					<td class="transparent">Test Session Name:</td>
					<td class="transparent">
						<div class="formValueLarge"><span class="formValueLarge" id="testsessionname" ></span></div>
					</td>
			</tr>
	  	</table>
	  	</td>
	 </tr> 		
	<tr class="transparent">
    	<td>
    	<table width="100%">
				<tr>
					<td colspan="3" class="buttonsRow">
				        <div id="displayMessageMain" style="display:none; width:99.5%;" class="errMsgs">
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
									<div id="contentMain"></div>
								</td>
							</tr>
						</table>	
						</div>
					</td>
				</tr>
				
				<tr class="transparent">
		            	 	
					<td style="vertical-align:top;"  id="jqGrid-content-section">
						<div id="itemView">
				      		<table id="itemListGrid" class="gridTable"></table>
							<div id="itemListPager" class="gridTable"></div>
						</div>				
					</td>
				</tr>
		</table>
        </td>
    </tr>        
  </table>	
		
</div>