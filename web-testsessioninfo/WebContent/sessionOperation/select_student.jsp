<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="label.tld" prefix="lb" %>
<lb:bundle baseName="testsessionApplicationResource" />

<netui-data:declareBundle bundlePath="webResources" name="web"/>

<div id="licenseInfoDiv" style="display: none">
<table width="910" border=0 cellpadding="0">
    <tr>
 		<td width="210"><div id="groupName"></div></td>
		<td width="200"><div id="licenseModel"></div></td>
	 	<td width="200"><div id="licenseAvailable"></div></td>
	 	<td width="300">
	 		<div id="licenseUsed" style="background-color:#347C17; color:#ffffff; padding:2px; width: 280px;" align="center"></div>
	 	</td> 
	</tr>
</table>	
</div>


<table class="transparent">

    <tr class="transparent">
        <td class="transparent"  valign="middle">
      	<div  id= "innerSearchheader" class="ui-corner-tl ui-corner-tr ui-widget-header innerTreeCtrlHeader"><lb:label key="sessionList.stuTab.gridTitle"/></div>
    	<div  id="innertreebgdivStu" class="innerTreeCtrl">
			<div id="stuOrgNodeHierarchy" style="width:auto;height:auto;display:table">
			</div>
		</div>
	 	</td>
	 	
 		<td class="transparent" width="3px">&nbsp;</td>
 		
	 	<td >
      		<table id="selectStudent" class="gridTable"></table>
			<div id="selectStudentPager" class="gridTable"></div>			
	    </td>
    </tr>
</table>
<!-- <table cellspacing="0" cellpadding="0" border="0" class="transparent">
		<tbody><tr class="transparent">
			<td colspan="2" class="transparent"><netui:content value="${bundle.web['selectstudentpage.Msg.statusKey']}"/></td>
		</tr>
		<tr class="transparent">
			<td width="20" class="transparent"><netui:content value="${bundle.web['selectstudentpage.Msg.completed']}"/></td>
			<td width="500" class="transparent"><netui:content value="${bundle.web['selectstudentpage.Msg.completedMsg']}"/></td>
		</tr>
		<tr class="transparent">
			<td width="20" class="transparent"><netui:content value="${bundle.web['selectstudentpage.Msg.inprogress']}"/></td>
			<td width="500" class="transparent"><netui:content value="${bundle.web['selectstudentpage.Msg.inprogressMsg']}"/></td>
		</tr>
		<tr class="transparent">
			<td width="20" class="transparent"><netui:content value="${bundle.web['selectstudentpage.Msg.schedule']}"/></td>
			<td width="500" class="transparent"><netui:content value="${bundle.web['selectstudentpage.Msg.scheduleMsg']}"/></td>
		</tr>
		<tr class="transparent">
			<td width="20" class="transparent"><netui:content value="${bundle.web['selectstudentpage.Msg.Preschedule']}"/></td>
			<td width="500" class="transparent"><netui:content value="${bundle.web['selectstudentpage.Msg.PrescheduleMsg']}"/></td>
		</tr>
	</tbody>
</table>-->
<table cellspacing="0" cellpadding="0" border="0" class="EditTable" width="100%">
	<tbody>
		<br/>
		<tr id="Act_Buttons" align="center">
			<td  width="100%">
				<center>
				<input type="button"  id="okData" value=<lb:label key="common.button.ok" prefix="'&nbsp;" suffix="&nbsp;'"/> onclick="javascript:returnSelectedStudent(); return false;" class="ui-widget-header">
				<input type="button"  id="cancelData" value=<lb:label key="common.button.back" prefix="'&nbsp;" suffix="&nbsp;'"/> onclick="javascript:hideSelectStudent(); return false;" class="ui-widget-header">
				</center>
				<br>
			</td>
		</tr>
	</tbody>
</table>


