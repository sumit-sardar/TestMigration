<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="label.tld" prefix="lb" %>
<lb:bundle baseName="studentApplicationResource" />

<%
    String studentIdLabelName = (String)request.getAttribute("studentIdLabelName");
	Boolean supportAccommodations = (Boolean)request.getAttribute("supportAccommodations");
	
%>

<input type="hidden" id="studentIdLabelName"  value = '<%=studentIdLabelName %>' />
<input type="hidden" id="supportAccommodations" value = '<%=supportAccommodations %>' />
<input type="hidden" id="noStudentTitle" name = "noStudentTitle" value=<lb:label key="student.noStuSelected.title" prefix="'" suffix="'"/>/>
<input type="hidden" id="noStudentMsg" name = "noStudentMsg" value=<lb:label key="student.noStuSelected.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="filterNoStuSelected" name = "filterNoStuSelected" value=<lb:label key="student.filterNoStuSelected.message" prefix="'" suffix="'"/>/>
	
<table class="transparent" width="97%" style="margin:15px auto;"> 
	<tr class="transparent">
		<td>
    		<table class="transparent">
				<tr class="transparent">
					<td>
			    		<h1><lb:label key="stu.outOfSchool.title" /></h1>
					</td>
				</tr>
				<tr> 
					<td class="subtitle">  
						<lb:label key="stu.outOfSchool.subtitle" />
					</td>	
				</tr>
			</table>		
		</td>
	</tr>
    <tr class="transparent">
        <td align="center">        
		<table width="100%">
		   <tr>
				<td colspan="3" class="buttonsRow">
					<div id="displayMessageMain" class="errMsgs" style="display: none; width: 50%; float: left;">
						<table>
							<tr>
								<td width="18" valign="middle">
									<div id="errorIcon" style="display:none;">
				                   		<img src="<%=request.getContextPath()%>/resources/images/messaging/icon_error.gif" border="0" width="16" height="16">
									</div>
									<div id="infoIcon" style="display:none;">
										<img src="<%=request.getContextPath()%>/resources/images/messaging/icon_info.gif" border="0" width="16" height="16">
									</div>
								</td>
								<td class="saveMsgs" valign="middle">
									<div id= "contentMain"></div>
								</td>
							</tr>
						</table>
					</div>	       			
					<div id="viewStatus" style="float:right;visibility:hidden;">
						<a href="#" id="OOSButton" onclick="javascript:outOfSchoolOperation(this); return false;" class="rounded {transparent} button"><lb:label key="stu.label.toggleOutOfSchool" /></a>
					</div> 
					<div style="clear:both;">
				</td>
		   	</tr>
	    	<tr class="transparent">
		        <td style="vertical-align:top; width:16%;" align="left">
			      	<div  id= "searchheader" class="ui-corner-tl ui-corner-tr ui-widget-header treeCtrlHeader">&nbsp;<lb:label key="stu.label.search" /></div>
			    	<script>populateOutOfSchoolTree();</script>
			
			    	<div id="outertreebcgdiv" class="treeCtrl" style="height:512px !important">
				    	<div id = "outOfSchoolOrgNode" style="width:auto;height:auto;display:table">
						</div>
					</div>
			 	</td>
			 	
	 			<td class="transparent" width="5px">&nbsp;</td>
		 	
		 		<td style="vertical-align:top;" id="jqGrid-content-section">
	      			<table id="outOfSchoolGrid" class="gridTable"></table>
					<div id="outOfSchoolPager" class="gridTable"></div>			
		 		</td>
	    	</tr>
		</table>
        </td>
    </tr>
</table>
