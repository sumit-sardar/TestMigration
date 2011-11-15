	
	
	<%
	 Boolean canRegisterStudent = (Boolean) session.getAttribute("canRegisterStudent");
	 Boolean userScheduleAndFindSessionPermission = (Boolean) session.getAttribute("userScheduleAndFindSessionPermission");
	
	%>
	
	<input type="hidden" id="canRegisterStudent" name = "canRegisterStudent" value='<%=canRegisterStudent %>'/>
	<input type="hidden" id="userScheduleAndFindSessionPermission" name = "userScheduleAndFindSessionPermission" value='<%=userScheduleAndFindSessionPermission %>'/>
	 
	 <div style="clear:both;float:left;width:100%;text-align: left;"> 
			<p style = "font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal;margin-bottom: 0;">Select one of your scheduled test sessions to view its status or to change its settings.
				 <%if(canRegisterStudent) { %>
					To quickly register a student for a test session, select the test session, and then click Register Student.
			</p>
			<p style = "font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal;margin-bottom: 0;"> 
				<%} if (userScheduleAndFindSessionPermission) {%>
					Open the Organization Search to view related organizations and their test sessions.
				<% } %>
			</p>
	</div>	
	 <%if(userScheduleAndFindSessionPermission) { %>
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
						              	<a href="#" id="viewStatusButton" onclick="" class="rounded {transparent} button">View Status</a>
						            </div> 
						            <%if(canRegisterStudent) { %>
							            <div id="registerStudent" style="float:right;padding-left:5px;">
							              	<a href="#" id="registerStudentButton" onclick="" class="rounded {transparent} button">Register Student</a>
							            </div>
						            <%} if (userScheduleAndFindSessionPermission) {%>
						            <div id="scSession" style="float:right;padding-left:5px;">
						              	<a href="#" id="scSessionButton" onclick="" class="rounded {transparent} button">Schedule Session</a>
						            </div> 
						            <%} %>
						            <div style="clear:both;"></div>     
						        </div>  
		                		     	        
	     	   </td>
		   	</tr>
	</table>
	<%} %>
	<div style="clear:both;float:left;width:100%;">
		<div id="show" style="display: block;width:2%;float:left; padding: 3px 0 3px 3px;" class="ui-corner-tl ui-corner-tr ui-corner-bl ui-corner-br ui-widget-header " title="Show Organization">
   			<a href="#" onclick="showTreeSlider();" style=" width:100%; " >>></a>
   		</div>
		<div id="orgSlider" style="float:left;width:0%;display:none" class="transparent">
			<div  id= "searchheader" class="ui-corner-tl ui-corner-tr ui-widget-header treeCtrlHeader" style="text-align:left;">
				<div style="float:left;">&nbsp;Browse Organization</div> 
				<div style="float:right;" title="Hide Organization"><a id="hide" href="#" onclick="hideTreeSlider();" style="display: none; width:100%;" >&nbsp;&lt;&lt;&nbsp;</a></div>
				<div style="clear:both;"></div>     
			</div>
	    	<div id = "orgNodeHierarchy" style="text-align: left !important;" class="treeCtrl"></div> 
		</div>  	     
		<div id="sessionGrid" style="float:right;width:97%;"> 		      		    				
					<div id="accordion" style="width:100%;">							
						<div>
						  	<h3><a href="#">Current and Future</a></h3>
							<div id="CurrentFuture" style="background-color: #FFFFFF;">
								<table id="list2" class="gridTable"></table>
								<div id="pager2" class="gridTable"></div>		
							</div>								
						</div>
						<div>
							<h3><a href="#">Completed</a></h3>
							<div id="Completed" style="background-color: #FFFFFF;">
								<table id="list3" class="gridTable"></table>
								<div id="pager3" class="gridTable"></div>	
							</div>									
						</div>							
					</div>
				
		</div>
	</div>