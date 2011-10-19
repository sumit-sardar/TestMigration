<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>   

<table class="transparent">

    <tr class="transparent">
        <td style="border-color : #2E6E9E; padding: 10px">
        
      	<div id="displayMessageMain" style="display:none; width:99.5%; height:55px; background-color: #FFFFFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: bold; border: 1px solid #A6C9E2;">
			<div id="titleMain"></div>
			<div id= "contentMain">	</div>
			<div id= "messageMain">	</div>
		</div>
		<table> 
			<!--  
			<tr class="transparent">
	        	<td  colspan="12" style="height:5px; color: #336699; font-family: Arial,Verdana,Sans Serif; font-size: 13px;  font-style: normal;  font-weight: bold;">&nbsp;</td>
   	 		</tr>
   	 		-->
		   	<tr width="100%" >
		       <td align="right" colspan="12">
	     	        <div id="changePW" style="display:none"><input style="padding: 2px; background: url(&quot;images/ui-bg_glass_85_dfeffc_1x400.png&quot;) repeat-x scroll 50% 50% rgb(223, 239, 252);" class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" onclick="javascript:changePwdForUser(); return false;" value=" Change Password " type="button"></div>
	     	   </td>
		   	</tr>
		  
	      <tr class="transparent">
	        <td class="transparent" valign="middle">
		      	<div  id= "searchheader" class="ui-corner-tl ui-corner-tr ui-widget-header treeCtrlHeader">&nbsp;User Search</div>
		    	  <script>populateTree();</script>
		    	  <div id = "orgNodeHierarchy"  class="treeCtrl">
				</div> 
		    </td>
		    
		 	<td class="transparent" width="20px">&nbsp;</td>
		 	 
		 	<td >	      
		    	<table id="list2" ></table>
				<div id="pager2"  ></div>
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
			
<div id="confirmationPopup"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<table>
		<tr>
			<td colspan="2">
			<p>You have not saved this user. Are you sure you want to cancel?</p>
			</td>
		</tr>
		<tr>
		<td >
				<center>
					<input type="button"  value="&nbsp;Yes&nbsp;" onclick="javascript:closeConfirmationPopup(); return false;" class="ui-widget-header">
					<input type="button"  value="&nbsp;No&nbsp;&nbsp;" onclick="javascript:closePopUp('confirmationPopup'); return false;" class="ui-widget-header">
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
					<input type="button"  value="&nbsp;Yes&nbsp;" onclick="javascript:closePopUp('EmailWarning'); return false;" class="ui-widget-header">
					<input type="button"  value="&nbsp;No&nbsp;&nbsp;" onclick="javascript:closeEmailWarningPopup(); return false;" class="ui-widget-header">
				</center>
			<br>
		</td>
		
		</tr>
		
	</table>
</div>
				