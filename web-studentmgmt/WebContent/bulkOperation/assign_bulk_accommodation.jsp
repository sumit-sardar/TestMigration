<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<netui-data:declareBundle bundlePath="webResources" name="web" />

<%
    String studentIdLabelName = (String)request.getAttribute("studentIdLabelName");
	Boolean supportAccommodations = (Boolean)request.getAttribute("supportAccommodations");
	
%>

<input type="hidden" id="studentIdLabelName"  value ='Student ID' />
<input type="hidden" id="supportAccommodations" value = '<%=supportAccommodations %>' />




&nbsp;&nbsp;<netui:content value="${bundle.web['student.assignAccommodation.text']}"/>
<table class="transparent">

    <tr class="transparent">
        <td style="border-color : #2E6E9E">
    	<div id="displayBulkMessageMain" class="roundedMessage" style="display:none;"> 
			<table>
				<tr>
					<td rowspan="3" valign="top">
                   	<img  id="errorImg"  style="display:none;" src="<%=request.getContextPath()%>/resources/images/messaging/icon_error.gif" border="0" width="16" height="16">&nbsp;&nbsp;
					</td>
					<td>
						<table>
							<tr><td><font style="color: red; font-size:12px; font-weight:bold"><div id="titleBulkMain"></div></font></td></tr>
							<tr><td><font style="font-size: 12px; font-weight: bold;"><div id= "contentBulkMain">	</div></font></td></tr>
							<tr><td><div id= "messageBulkMain">	</div></td></tr>
						</table>
					</td>
				</tr>
			</table>
	</div>
		<table style="clear:both;float:left" width= "100%"> 
		   	<tr >
		   		<td >
	       			<div id="ShowButtons" style="width:1210px; display:none; float:left;">
						<div id="viewStatus" style="float:right;padding-left:5px;">
							<a href="#" id="assignAccommButton" onclick="javascript:openAssignAccommPopup(this); return false;" class="rounded {transparent} button"><netui:content value="${bundle.web['student.assignAccommodation.EditAccom.Button']}"/></a>
						</div> 
					   
					</div>  
				</td>
		   	</tr>
		   	<tr>
		   		
	 </tr>
	</table>
		
	 <table class="transparent">
	    <tr class="transparent">
	        <td class="transparent"  valign="bottom" style="vertical-align: bottom !important;">
	      	<div  id= "searchheader" class="ui-corner-tl ui-corner-tr ui-widget-header treeCtrlHeader">&nbsp;<netui:content value="${bundle.web['student.assignAccommodation.StudentSearch']}"/></div>
	    	<script>populateBulkAccommTree();</script>
	    	<div id = "studentBulkOrgNode" class="treeCtrl">
				
			</div> 
			
		 	</td>
	 		<td class="transparent" width="5px">&nbsp;</td>
		 	<td >
	      		<div  id= "searchresultheader" class="ui-corner-tl ui-corner-tr ui-widget-header treeCtrlHeader">
	      			&nbsp;<netui:content value="${bundle.web['student.assignAccommodation.StudentList']}"/>
	      		</div>
	    		<table id="studentAccommGrid" class="gridTable"></table>
				<div id="studentAccommpager" class="gridTable"></div>			
		 </td>
	    </tr>
	</table>
	

        </td>
    </tr>
</table>

				
<div id="AssignAccommPopup"
	style="width:97%; display: none; border:8px solid #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<table>
		<tr><td>
			<div id="displayPopupMessage" class="roundedMessage" style="display:none; border:2px solid #D4ECFF;"> 
				<table>
					<tr>
						<td rowspan="3" valign="top">
	                   	<img  id="errorImgPopup"  src="<%=request.getContextPath()%>/resources/images/messaging/icon_error.gif" border="0" width="16" height="16">&nbsp;&nbsp;
						</td>
						<td>
							<table>
								<tr><td><font style="color: red; font-size:12px; font-weight:bold"><div id="titleBulkPopup"></div></font></td></tr>
								<tr><td><font style="font-size: 12px; font-weight: bold;"><div id= "contentBulkPopup">	</div></font></td></tr>
								<tr><td><div id= "messageBulkPopup">	</div></td></tr>
							</table>
						</td>
					</tr>
				</table>
		</div>
		</td></tr>
		<tr>
			<td colspan="2">
			<br/>
				<jsp:include page="/bulkOperation/bulkAccommodationSelection.jsp" />
			<br/>
			</td>
		</tr>
		<tr>
		<td >
				<center>
					<input type="button"  value="&nbsp;Reset&nbsp;" onclick="javascript:resetRadioAccommodation(); return false;" class="ui-widget-header">&nbsp;
					<input type="button"  value="&nbsp;Submit&nbsp;" onclick="javascript:saveBulkStudentData(); return false;" class="ui-widget-header">&nbsp;
					<input type="button"  value="&nbsp;Cancel&nbsp;&nbsp;" onclick="javascript:closePopUp('AssignAccommPopup'); return false;" class="ui-widget-header">
				</center>
			<br>
		</td>
		
		</tr>
		
	</table>
</div>