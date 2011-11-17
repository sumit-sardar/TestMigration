<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>

<netui-data:declareBundle bundlePath="webResources" name="web"/>	
	
	<%
	 Boolean canRegisterStudent = (Boolean) session.getAttribute("canRegisterStudent");
	 Boolean userScheduleAndFindSessionPermission = (Boolean) session.getAttribute("userScheduleAndFindSessionPermission");
	
	%>
	
	<input type="hidden" id="canRegisterStudent" name = "canRegisterStudent" value='<%=canRegisterStudent %>'/>
	<input type="hidden" id="userScheduleAndFindSessionPermission" name = "userScheduleAndFindSessionPermission" value='<%=userScheduleAndFindSessionPermission %>'/>
	 
	 <div style="clear:both;float:left;width:100%;text-align: left;"> 
			<p style = "font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal;margin-bottom: 0;"><netui:content value="${bundle.web['homepage.viewTestSessions.message']}"/>
				 <%if(canRegisterStudent) { %>
					<netui:content value="${bundle.web['homepage.rapidRegister.message']}"/>	
			</p>
			<p style = "font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal;margin-bottom: 0;"> 
				<%} if (userScheduleAndFindSessionPermission) {%>
					<netui:content value="${bundle.web['homepage.OrgSearchInfo.message']}"/>	
				<% } %>
			</p>
	</div>	
	 
	<table width="100%" > 
		   	<tr >
		   		<td width="13%" align="left" style="padding: 5px 0px 5px 0px; clear:both;">
		      		<div style="width:68%;">
		      			<!-- <a id="show" href="#" onclick="showTreeSlider();" style="display: block; width:100%;" class="rounded {transparent} button">Show Organization</a> -->
		      			<!-- <a id="hide" href="#" onclick="hideTreeSlider();" style="display: none; width:100%;" class="rounded {transparent} button">&nbsp;&nbsp;Hide Organization</a> -->
		      		</div>
		    	</td>
		        <td align="right" colspan="12" style="padding: 0 0 5px 0px; clear:both">
		       		<div id="ShowButtons" style="width:98%;display:none;">
						       		<div id="viewStatus" style="float:right;padding-left:5px;">
						              	<a href="#" id="viewStatusButton" onclick="" class="rounded {transparent} button"><netui:content value="${bundle.web['homepage.button.viewStatus']}"/></a>
						            </div> 
						            <%if(canRegisterStudent) { %>
							            <div id="registerStudent" style="float:right;padding-left:5px;">
							              	<a href="#" id="registerStudentButton" onclick="" class="rounded {transparent} button"><netui:content value="${bundle.web['homepage.button.registerStudent']}"/></a>
							            </div>
						            <%} if (userScheduleAndFindSessionPermission) {%>
						            <div id="scSession" style="float:right;padding-left:5px;">
						              	<a href="#" id="scSessionButton" onclick="" class="rounded {transparent} button"><netui:content value="${bundle.web['homepage.button.scheduleSession']}"/></a>
						            </div> 
						            <%} %>
						            <div style="clear:both;"></div>     
						        </div>  
		                		     	        
	     	   </td>
		   	</tr>
	</table>
	
	<div style="clear:both;float:left;width:1215px;">
	<%if(userScheduleAndFindSessionPermission) { %>
		<div id="show" style="display: block;width:25px;float:left; padding: 3px 0 3px 3px;" class="ui-corner-tl ui-corner-tr ui-corner-bl ui-corner-br ui-widget-header " title="${bundle.web['homepage.icon.showOrganization']}">
   			<a href="#" onclick="showTreeSlider();" style=" width:100%; " >>></a>
   		</div>
   	<%} %>
		<div id="orgSlider" style="float:left;width:0px;display:none;white-space: nowrap;" class="transparent">
			<div  id= "searchheader" class="ui-corner-tl ui-corner-tr ui-widget-header treeCtrlHeader" style="text-align:left;">
				<div style="float:left;" >&nbsp;<netui:content value="${bundle.web['homepage.hierarchy.title']}"/></div> 
				<div style="float:right;" title="${bundle.web['homepage.icon.hideOrganization']}"><a id="hide" href="#" onclick="hideTreeSlider();" style="display: none; width:100%;" >&nbsp;&lt;&lt;&nbsp;</a></div>
				<div style="clear:both;"></div>     
			</div>
	    	<div id = "orgNodeHierarchy" style="text-align: left !important;" class="treeCtrl"></div> 
		</div>  
	<%if(userScheduleAndFindSessionPermission) { %>		     
		<div id="sessionGrid" style="float:right;width:1180px;"> 
	<%} else {%>
		<div id="sessionGrid" style="float:right;width:1215px;"> 	
	<% } %>		      		    				
					<div id="accordion" style="width:100%;">							
						<div>
						  	<h3><a href="#"><netui:content value="${bundle.web['homepage.tab.currentAndFuture']}"/></a></h3>
							<div id="CurrentFuture" style="background-color: #FFFFFF;">
								<table id="list2" class="gridTable"></table>
								<div id="pager2" class="gridTable"></div>		
							</div>								
						</div>
						<div>
							<h3><a href="#"><netui:content value="${bundle.web['homepage.tab.completed']}"/></a></h3>
							<div id="Completed" style="background-color: #FFFFFF;">
								<table id="list3" class="gridTable"></table>
								<div id="pager3" class="gridTable"></div>	
							</div>									
						</div>							
					</div>
				
		</div>
	</div>
	
	
				
<div id="confirmationPopup"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<table>
		<tr>
			<td colspan="2">
			<br/>
			<p><netui:content value="${bundle.web['common.message.user.browse.topNodeSelection']}"/></p>
			<br/>
			</td>
		</tr>
		<tr>
		<td >
				<center>
					<input type="button"  value="&nbsp;${bundle.web['homepage.button.yes']}&nbsp;" onclick="javascript:fetchDataOnConfirmation(); return false;" class="ui-widget-header">&nbsp;
					<input type="button"  value="&nbsp;${bundle.web['homepage.button.no']}&nbsp;&nbsp;" onclick="javascript:closePopUp('confirmationPopup'); return false;" class="ui-widget-header">
				</center>
			<br>
		</td>
		
		</tr>
		
	</table>
</div>