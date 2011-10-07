<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>   


<table class="sortable">

    <tr class="sortable">
        <td style="border-color : #2E6E9E">
        
      	<div id="displayMessageMain" style="display:none; width:99.5%; height:55px; background-color: #FFFFFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: bold; border: 1px solid #A6C9E2;">
			<div id="titleMain"></div>
			<div id= "contentMain">	</div>
			<div id= "messageMain">	</div>
		</div>
		
	 <table class="transparent">
		<tr class="transparent">
	        <td  colspan="12" style="height:5px; color: #336699; font-family: Arial,Verdana,Sans Serif; font-size: 13px;  font-style: normal;  font-weight: bold;">&nbsp;
	             
	        </td>
   	 	</tr>
	    <tr class="transparent">
	        <td class="transparent"  valign="middle">
	      	<div  id= "searchheader" style="visibility:hidden; background:#4297D7; height:25px;  color: #FFFFFF; font-family: Arial,Verdana,Sans Serif; font-size: 13px;  font-style: normal;  font-weight: bold; vertical-align:middle;">&nbsp;Student Search</div>
	    	<script>populateTree();</script>
       		 <div id = "orgNodeHierarchy" style="visibility:hidden; background:#ffffee; overflow:auto; height: 485px;  width: 200px; font-family: Arial, Verdana, Sans Serif; font-size: 13px; font-style: normal; font-weight: normal;">
				
			</div> 
			
		 </td>
		 	<td class="transparent" width="20px">&nbsp;</td>
		 	 <td >
	      
	    	<table id="list2" ></table>
			<div id="pager2"  ></div>
			
		 </td>
	    </tr>
	    <tr class="transparent">
	        <td  colspan="12" style="height:5px; color: #336699; font-family: Arial,Verdana,Sans Serif; font-size: 13px;  font-style: normal;  font-weight: bold;">&nbsp;
	             
	        </td>
   	 	</tr>
	</table>
	

        </td>
    </tr>
</table>
</br>
</br>
<jsp:include page="/studentOperation/add_edit_student_detail.jsp" />
<div id="confirmationPopup"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<table>
		<tr>
			<td colspan="2">
			<p>You have not saved this student. Are you sure you want to cancel?</p>
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
