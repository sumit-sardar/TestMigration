<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>

<netui-data:declareBundle bundlePath="webResources" name="web"/>

<table class="transparent">
    <tr class="transparent">
        <td class="transparent"  valign="middle">
      	<div  id= "innerSearchheader" class="ui-corner-tl ui-corner-tr ui-widget-header innerTreeCtrlHeader">&nbsp;Student Search</div>
    	<div id = "stuOrgNodeHierarchy" class="innerTreeCtrl">
			
		</div> 
		
	 	</td>
 		<td class="transparent" width="5px">&nbsp;</td>
	 	<td >
      		<table id="selectStudent" class="gridTable"></table>
			<div id="selectStudentPager" class="gridTable"></div>			
	    </td>
    </tr>
</table>
<table cellspacing="0" cellpadding="0" border="0" class="transparent">
		<tbody><tr class="transparent">
			<td colspan="2" class="transparent">Status Key:</td>
		</tr>
		<tr class="transparent">
			<td width="20" class="transparent">Cmp</td>
			<td width="500" class="transparent">Completed the test.</td>
		</tr>
		<tr class="transparent">
			<td width="20" class="transparent">Inp</td>
			<td width="500" class="transparent">In progress, taking the test.</td>
		</tr>
		<tr class="transparent">
			<td width="20" class="transparent">Org</td>
			<td width="500" class="transparent">Scheduled through other organization. Cannot override.</td>
		</tr>
		<tr class="transparent">
			<td width="20" class="transparent">Ses</td>
			<td width="500" class="transparent">Previously scheduled for this test in different test session.</td>
		</tr>
	</tbody>
</table>
<table cellspacing="0" cellpadding="0" border="0" class="EditTable" width="100%">
	<tbody>
		<br>
		<tr id="Act_Buttons" align="center">
			<td  width="100%">
				<center>
				<input type="button"  id="okData" value="&nbsp;Ok&nbsp;" onclick="javascript:returnSelectedStudent(); return false;" class="ui-widget-header">
				<input type="button"  id="cancelData" value="&nbsp;Cancel&nbsp;&nbsp;" onclick="javascript:hideSelectStudent(); return false;" class="ui-widget-header">
				</center>
				<br>
			</td>
		</tr>
	</tbody>
</table>


