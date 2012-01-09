<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="label.tld" prefix="lb" %>
<netui-data:declareBundle bundlePath="webResources" name="web" />
<lb:bundle baseName="studentApplicationResource" />

<%
    String studentIdLabelName = (String)request.getAttribute("studentIdLabelName");
	Boolean supportAccommodations = (Boolean)request.getAttribute("supportAccommodations");
	
%>

<input type="hidden" id="studentIdLabelName"  value = '<%=studentIdLabelName %>' />
<input type="hidden" id="supportAccommodations" value = '<%=supportAccommodations %>' />
<input type="hidden" id="noStudentTitle" name = "noStudentTitle" value="${bundle.web['student.noStuSelected.title']}"/>
<input type="hidden" id="noStudentMsg" name = "noStudentMsg" value="${bundle.web['student.noStuSelected.message']}"/>
<input type="hidden" id="filterNoStuSelected" name = "filterNoStuSelected" value="${bundle.web['student.filterNoStuSelected.message']}"/>
	
<table> 
	<tr>
		<td style="padding-left:5px;">
    		<h1><lb:label key="stu.moveStu.title" /></h1>
		</td>
	</tr>
</table>	
<table class="transparent">

    <tr class="transparent">
        <td style="border-color : #2E6E9E;padding-left:5px;">
    	
		<table style="clear:both;float:left" width= "100%"> 
		   	<tr >
		   		<td >
	       			<div id="ShowButtons" style="width:1196px; min-height:25px;height: 25px;">
			    	<div  style="float:left;width:800px;white-space: nowrap;" class="transparent">
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
					</div>
					<div id="viewStatus" style="float:right;padding-left:5px;display:none;">
						<a href="#" id="bulkMoveButton" onclick="javascript:openBulkMovePopup(this); return false;" class="rounded {transparent} button"><lb:label key="stu.label.move" /></a>
					</div> 
					<div style="clear:both;">
					</div>
				</div> 
				</td>
		   	</tr>
	</table>
		
	 <table class="transparent">
	    <tr class="transparent">
	        <td style="vertical-align:top;">
	      	<div  id= "searchheader" class="ui-corner-tl ui-corner-tr ui-widget-header treeCtrlHeader">&nbsp;<lb:label key="stu.label.search" /></div>
	    	<script>populateBulkMoveTree();</script>
	    	<div id = "bulkStudentMoveOrgNode" class="treeCtrl" style="height:512px !important">
				
			</div> 
			
		 	</td>
	 		<td class="transparent" width="5px">&nbsp;</td>
		 	<td style="vertical-align:top;">
	      		<table id="studentBulkMoveGrid" class="gridTable"></table>
				<div id="studentBulkMovePager" class="gridTable"></div>			
		 </td>
	    </tr>
	</table>
	

        </td>
    </tr>
</table>


<div id="moveStudentPopup"
	style="width:97%; display: none; border:8px solid #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<table>
	<tr>
		<td>
			<div id="displayBMPopupMessage" class="roundedMessage" style="">
				<table>
					<tr>
						<td rowspan="3" valign="top">
	                   	<img  id="errorImgPopup" style="display: none;"  src="<%=request.getContextPath()%>/resources/images/messaging/icon_error.gif" border="0" width="16" height="16">&nbsp;&nbsp;
						</td>
						<td>
							<table>
								<tr>
									<td>
										<font style="color: red; font-size:12px; font-weight:bold">
											<div id="titleBulkMovePopup">
											</div>
										</font>
									</td>
								</tr>
								<tr><td><font style="font-size: 12px; font-weight: bold;"><div id= "contentBulkMovePopup">	</div></font></td></tr>
								<tr><td><div id= "messageBulkMovePopup"></div></td></tr>
							</table>
						</td>
					</tr>
				</table>
		</div>
		</td></tr>
		<tr>
			<td colspan="2">
			<br/>
				<jsp:include page="/bulkMoveOperation/moveOrgSelection.jsp" />
			<br/>
			</td>
		</tr>
		<tr>
		<td >
				<center>
					<input type="button"  value="&nbsp;Save&nbsp;" onclick="javascript:saveBulkMoveData(); return false;" class="ui-widget-header">&nbsp;
					<input type="button"  value="&nbsp;Cancel&nbsp;&nbsp;" onclick="javascript:closeBulkMovePopup(); return false;" class="ui-widget-header">
				</center>
		</td>
		
		</tr>
		
	</table>
</div>

<div id="unSaveBulkConfirmationPopup"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<table>
		<tr>
			<td colspan="2">
			<br/>
			<p><lb:label key="stu.msg.moveCancelConfirm" /></p>
			<br/>
			</td>
		</tr>
		<tr>
		<td >
				<center>
					<input type="button"  value="&nbsp;OK&nbsp;" onclick="javascript:closeUnsaveBulkConfirmationPopup(); return false;" class="ui-widget-header">&nbsp;
					<input type="button"  value="&nbsp;Cancel&nbsp;&nbsp;" onclick="javascript:closePopUp('unSaveBulkConfirmationPopup'); return false;" class="ui-widget-header">
				</center>
			<br>
		</td>
		
		</tr>
		
	</table>
</div>