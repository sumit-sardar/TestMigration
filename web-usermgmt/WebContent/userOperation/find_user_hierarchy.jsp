<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>   

<input type="hidden" id="roleNameID" name = "roleName" value='<%=session.getAttribute("userRole") %>'/>
<table> 
	<tr> 
		<td>  
			<span style = "font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal;">&nbsp;&nbsp;&nbsp;Select an organization to see a list of users associated with that organization.  
			View, edit, delete or add users using the buttons at the bottom of the User List.
			</span>
		</td>	
	</tr>
</table>
<br/>
<table class="transparent">
    <tr class="transparent">
        <td style="border-color : #2E6E9E;">
        
      	<!-- <div id="displayMessageMain" style="display:none; width:99.5%; height:55px; background-color: #FFFFFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: bold; border: 0px solid #A6C9E2;">
			<font style="color: red; font-size:12px; font-weight:bold"><div id="titleMain"></div></font>
			<div id= "contentMain">	</div>
			<div id= "messageMain">	</div>
		</div>  -->
		<table> 
		<!-- 
		   	<tr width="100%" >
		       <td align="right" colspan="12">
	     	    <div id="changePW" style="display:none">
	     	        	<input id="changePWDBtn" style="padding: 2px; background: url(&quot;images/ui-bg_glass_85_dfeffc_1x400.png&quot;) repeat-x scroll 50% 50% rgb(223, 239, 252);" class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" onclick="javascript:changePwdForUser(); return false;" value=" Change Password " type="button">
	     	        </div>
	     	   </td>
		   	</tr>
		 -->
		  
		   	<tr width="100%" >
		       <td align="right" colspan="12" style="padding: 0 0 5px 8px; clear:both">
		       <div id="displayMessageMain" style="display:none; width:50%; float:left; text-align:left; background-color: #FFFFFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: bold; border: 0px solid #A6C9E2;">
					<div id= "contentMain" style="padding-top: 5px;">	</div>
				</div>
		       	<div id="changePW" style="display:none;">
		       		<div id="cpw">
                		<a href="#" id="changePWButton" onclick="changePwdForUser(this);" class="rounded {transparent} button">Change Password</a>
                	</div>      
                </div>          		     	        
	     	   </td>
		   	</tr>
		  
	      <tr class="transparent">
	        <td class="transparent" valign="middle">
		      	<div  id= "searchheader" class="ui-corner-tl ui-corner-tr ui-widget-header treeCtrlHeader">&nbsp;User Search</div>
		    	  <script>populateTree();</script>
		    	  <div id = "orgNodeHierarchy"  class="treeCtrl">
				</div> 
		    </td>
		    
		 	<td class="transparent" width="8px">&nbsp;</td>
		 	 
		 	<td >	      
		    	<table id="list2" class="gridTable"></table>
				<div id="pager2"  class="gridTable"></div>
			</td>
			
	      </tr>
	      <!-- 
	      <tr class="transparent">
	        <td  colspan="12" style="height:5px; color: #336699; font-family: Arial,Verdana,Sans Serif; font-size: 13px;  font-style: normal;  font-weight: bold;">&nbsp;</td>
   	 	  </tr>
   	 	   -->
	    </table>
        </td>
    </tr>
</table>

<jsp:include page="/userOperation/add_edit_user_detail.jsp" />
<jsp:include page="/userOperation/change_password.jsp" />
			
<div id="confirmationPopup"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<table>
		<tr>
			<td colspan="2">
			<br/>
			<p>You have not saved the user record. Are you sure you want to leave this page?</p>
			<br/>
			</td>
		</tr>
		<tr>
		<td >
				<center>
					<input type="button"  value="&nbsp;Yes&nbsp;" onclick="javascript:closeConfirmationPopup(); return false;" class="ui-widget-header">&nbsp;
					<input type="button"  value="&nbsp;No&nbsp;&nbsp;" onclick="javascript:closePopUp('confirmationPopup'); return false;" class="ui-widget-header">
				</center>
			<br>
		</td>
		
		</tr>
		
	</table>
</div>

<div id="confirmationPopupNavigation"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<table>
		<tr>
			<td colspan="2">
			<br/>
			<p>You have not saved the user record. Are you sure you want to leave this page?</p>
			<br/>
			</td>
		</tr>
		<tr>
		<td >
				<center>
					<input type="button"  value="&nbsp;Yes&nbsp;" onclick="javascript:closeConfirmationPopup(); return false;" class="ui-widget-header">&nbsp;
					<input type="button"  value="&nbsp;No&nbsp;&nbsp;" onclick="javascript:closePopUp('confirmationPopupNavigation'); return false;" class="ui-widget-header">
				</center>
			<br>
		</td>
		
		</tr>
		
	</table>
</div>


<div id="EmailWarning"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<table>
		<tr>
			<td colspan="2">
			<p>An email address was not provided for this new user. Therefore, the welcome email message with login information cannot be sent directly to the user. Do you want to add an email address now?</p>
			</td>
		</tr>
		<tr>
		<td >
				<center>
					<input type="button"  value="&nbsp;Yes&nbsp;" onclick="javascript:closePopUp('EmailWarning'); return false;" class="ui-widget-header">&nbsp;
					<input type="button"  value="&nbsp;No&nbsp;&nbsp;" onclick="javascript:closeEmailWarningPopup(); return false;" class="ui-widget-header">
				</center>
			<br>
		</td>
		
		</tr>
		
	</table>
</div>
				