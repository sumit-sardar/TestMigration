	
	
	<%
	 Boolean canRegisterStudent = (Boolean) session.getAttribute("canRegisterStudent");
	 Boolean userScheduleAndFindSessionPermission = (Boolean) session.getAttribute("userScheduleAndFindSessionPermission");
	
	%>
	
	<input type="hidden" id="canRegisterStudent" name = "canRegisterStudent" value='<%=canRegisterStudent %>'/>
	<input type="hidden" id="userScheduleAndFindSessionPermission" name = "userScheduleAndFindSessionPermission" value='<%=userScheduleAndFindSessionPermission %>'/>
	 
	 <div style="clear:both;float:left;width:100%;text-align: left;"> 
			<p style = "font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal;margin-bottom: 0;">&nbsp;&nbsp;&nbsp;Select one of your scheduled test sessions to view its status or to change its settings.
				 <%if(canRegisterStudent) { %>
					To quickly register a student for a test session, select the test session, and then click Register Student.
			</p>
			<p style = "font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal;margin-bottom: 0;"> 
				<%} if (userScheduleAndFindSessionPermission) {%>
					&nbsp;&nbsp;&nbsp;Open the Organization Search to view related organizations and their test sessions.
				<% } %>
			</p>
	</div>	
	 <%if(userScheduleAndFindSessionPermission) { %>
	<table width="100%" > 
		   	<tr >
		   		<td width="12%" height="3%">
		      		<div  >&nbsp;
		      			<a id="show" href="#" onclick="showTreeSlider();" style="display:inline-block; background-image:url('/SessionWeb/resources/images/show.PNG');width :115px; height:54px"> 
		      			</a>
		      			<a id="hide" href="#" onclick="hideTreeSlider();" style="display:none; background-image:url('/SessionWeb/resources/images/hide.PNG');width :115px; height:54px"></a>
		      		</div>
		    	</td>
		       <td align="right" colspan="12" style="padding: 0 0 5px 8px; clear:both">
		       
		                		     	        
	     	   </td>
		   	</tr>
	</table>
	<%} %>
	<div style="clear:both;float:left;width:100%;">
		<div id="orgSlider" style="float:left;width:0%;display:none" class="transparent">
			<div  id= "searchheader" class="ui-corner-tl ui-corner-tr ui-widget-header treeCtrlHeader">&nbsp;Session Search</div>
	    	<div id = "orgNodeHierarchy" style="text-align: left !important;" class="treeCtrl"></div> 
		</div>  	     
		<div id="sessionGrid" style="float:left;width:100%;"> 		      		    				
					<div id="accordion" style="width:100%;">							
						<div>
						  	<h3><a href="#">Current / Future</a></h3>
							<div id="CurrentFuture" style="background-color: #FFFFFF;">
								<div id="ShowButtons" style="width:98%;display:none;">
						       		<div id="viewStatus" style="float:right;padding:5px;">
						              	<a href="#" id="viewStatusButton" onclick="" class="rounded {transparent} button">View Status</a>
						            </div> 
						            <%if(canRegisterStudent) { %>
							            <div id="registerStudent" style="float:right;padding:5px;">
							              	<a href="#" id="registerStudentButton" onclick="" class="rounded {transparent} button">Register Student</a>
							            </div>
						            <%} if (userScheduleAndFindSessionPermission) {%>
						            <div id="scSession" style="float:right;padding:5px;">
						              	<a href="#" id="scSessionButton" onclick="" class="rounded {transparent} button">Schedule Session</a>
						            </div> 
						            <%} %>
						            <div style="clear:both;"></div>     
						        </div>  
								<table id="list2" class="gridTable"></table>
								<div id="pager2" class="gridTable"></div>		
							</div>								
						</div>
						<div>
							<h3><a href="#">Completed</a></h3>
							<div id="Completed" style="overflow-y: scroll !important; overflow-x: hidden !important;">
								<div id="ShowButtonsPA" style="width:98%;display:none;">
						       		<div id="viewStatusPA" style="float:right;padding:5px;">
						              	<a href="#" id="viewStatusButtonPA" onclick="" class="rounded {transparent} button">View Status</a>
						            </div> 
						            <%if(canRegisterStudent) { %>
							            <div id="registerStudentPA" style="float:right;padding:5px;">
							              	<a href="#" id="registerStudentButtonPA" onclick="" class="rounded {transparent} button">Register Student</a>
							            </div>
						            <%} %>
						             <div style="clear:both;"></div>    
						        </div>  
								<table id="list3" class="gridTable"></table>
								<div id="pager3" class="gridTable"></div>	
							</div>									
						</div>							
					</div>
				
		</div>
	</div>