<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>   


<table class="transparent">

    <tr class="transparent">
        <td style="border-color : #2E6E9E">
        
      	<div id="displayMessageMain" style="display:none; width:99.5%; height:55px; background-color: #FFFFFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: bold; border: 1px solid #A6C9E2;">
			<div id="titleMain"></div>
			<div id= "contentMain">	</div>
			<div id= "messageMain">	</div>
		</div>
		
	 <table class="transparent">
	    <tr class="transparent">
	        <td class="transparent"  valign="middle">
	      	<div  id= "searchheader" class="ui-corner-tl ui-corner-tr ui-widget-header treeCtrlHeader">&nbsp;Student Search</div>
	    	<script>populateTree();</script>
	    	<div id = "orgNodeHierarchy" class="treeCtrl">
				
			</div> 
			
		 	</td>
	 		<td class="transparent" width="8px">&nbsp;</td>
		 	<td >
	      		<div  id= "searchresultheader" class="ui-corner-tl ui-corner-tr ui-widget-header gridTableHeader">
	      			<table border="0">
	    			<tr>
	    				<td width="100%">&nbsp;Student List</td>
	    				<td>
							<div class="search_input_box">
								<input class="search_input" type="text" name="searchControl" >
								<img src="/StudentManagementWeb/resources/images/magnifier.png" class="search_image" onclick="alert('search');">
							 </div>
	    				</td>
	      			</tr>
	      			</table>
	      		</div>
	    		<table id="list2" class="gridTable"></table>
				<div id="pager2" class="gridTable"></div>			
		 </td>
	    </tr>
	</table>
	

        </td>
    </tr>
</table>

<jsp:include page="/studentOperation/add_edit_student_detail.jsp" />
<jsp:include page="/studentOperation/view_student_detail.jsp" />
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
