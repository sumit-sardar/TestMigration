<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>   

<input type="hidden" id="addStudentEnable" name="addStudentEnable" value='<%=session.getAttribute("addStudentEnable") %>'/>

<table class="transparent">

    <tr class="transparent">
        <td style="border-color : #2E6E9E">
        
      	<div id="displayMessageMain" style="display:none; width:99.5%; height:55px; background-color: #FFFFFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: bold; border: 0px solid #A6C9E2;">
			<font style="color: red; font-size:12px; font-weight:bold"><div id="titleMain"></div></font>
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
	 		<td class="transparent" width="5px">&nbsp;</td>
		 	<td >
	      		<div  id= "searchresultheader" class="ui-corner-tl ui-corner-tr ui-widget-header treeCtrlHeader">
	      			&nbsp;Student List
	      		<!-- 
	      		<div  id= "searchresultheader" class="ui-corner-tl ui-corner-tr ui-widget-header gridTableHeader">
	      			<table border="0" width="100%">
	    			<tr>
	    				<td width="90%">&nbsp;Student List</td>
	    				<td width="10%">
							<div class="search_input_box">
								<input class="search_input" type="text" name="searchControl" >
								<img src="/StudentWeb/resources/images/magnifier.png" class="search_image" onclick="alert('search');">
							 </div>
	    				</td>
	      			</tr>
	      			</table>
	      		 -->
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
			<br/>
			<p>You have not saved this student. Are you sure you want to cancel?</p>
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
			<p>You have not saved this student. Are you sure you want to move?</p>
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
<div id="deleteStudentPopup"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<table border="0" width="100%">
		<tr align="left">
			<td>
			<br/>
			<p>
				Click 'OK' to delete this student's profile from your organization<br/>
				The student will be removed and excluded from future tests.<br/>
			 	Previous test records will be retained.
			</p>
			<br/>
			</td>
		</tr>
		<tr align="center">
			<td>
				<input type="button"  value="&nbsp;OK&nbsp;" onclick="javascript:submitDeleteStudentPopup(); return false;" class="ui-widget-header">&nbsp;
				<input type="button"  value="&nbsp;Cancel&nbsp;" onclick="javascript:closePopUp('deleteStudentPopup'); return false;" class="ui-widget-header">				
			</td>		
		</tr>
		
	</table>
</div>

