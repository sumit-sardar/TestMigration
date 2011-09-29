<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>   
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery.jstree.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/grid.locale-en.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery.jqGrid.min.js"></script>	
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/verifystudentinfo.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/hierarchy.js"></script>
<script type="text/javascript" src="/StudentManagementWeb/resources/js/studentregistration.js"></script>

	





<table class="sortable">

    <tr class="sortable">
        <td style="background-color : #CCCC99">
        
      
		
	 <table class="transparent">
		<tr class="transparent">
	        <td  colspan="12" style="height:5px; color: #336699; font-family: Arial,Verdana,Sans Serif; font-size: 13px;  font-style: normal;  font-weight: bold;">&nbsp;
	             
	        </td>
   	 	</tr>
	    <tr class="transparent">
	        <td class="transparent"  valign="middle">
	      	<div  id= "searchheader" style="visibility:hidden; background:#990000; height:25px;  color: #FFFFFF; font-family: Arial,Verdana,Sans Serif; font-size: 13px;  font-style: normal;  font-weight: bold; vertical-align:middle;">&nbsp;Student Search</div>
	    	<script>populateTree();</script>
       		 <div id = "orgNodeHierarchy" style="visibility:hidden; background:#ffffee; overflow:auto; height: 270px;  width: 250px; font-family: Arial, Verdana, Sans Serif; font-size: 13px; font-style: normal; font-weight: normal;">
				
			</div> 
			
		 </td>
		 	<td class="transparent" width="100px">&nbsp;</td>
		 	 <td >
	      
	    	<table id="list2" ></table>
			<div id="pager2"  ></div>
			
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
	style="display: none; background-color: #FFFFCC; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<table>
		<tr>
			<td colspan="2">
			<p>You have not saved this student. Are you sure you want to cancel?</p>
			</td>
		</tr>
		<tr>
		<td >
				<center>
					<input type="button"  value="&nbsp;Yes&nbsp;" onclick="javascript:closeConfirmationPopup(); return false;" class="buttonPopup">
					<input type="button"  value="&nbsp;No&nbsp;&nbsp;" onclick="javascript:closePopUp('confirmationPopup'); return false;" class="buttonPopup">
				</center>
			<br>
		</td>
		
		</tr>
		
	</table>
</div>
