<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>

<netui-data:declareBundle bundlePath="webResources" name="web"/>

<table class="transparent">
    <tr class="transparent">
        <td class="transparent"  valign="middle">
      	<div  id= "innerProctorSearchheader" class="ui-corner-tl ui-corner-tr ui-widget-header innerTreeCtrlHeader">&nbsp;Proctor Search</div>
    	<div id = "proctorOrgNodeHierarchy" class="innerProctorTreeCtrl">
			
		</div> 
		
	 	</td>
 		<td class="transparent" width="5px">&nbsp;</td>
	 	<td >
      		<table id="selectProctor" class="gridTable"></table>
			<div id="selectProctorPager" class="gridTable"></div>			
	    </td>
    </tr>
</table>

<table cellspacing="0" cellpadding="0" border="0" class="EditTable" width="100%">
	<tbody>
		<br>
		<tr id="Act_Buttons_Proctor" align="center">
			<td  width="100%">
				<center>
				<input type="button"  id="okDataProctor" value="${bundle.web['homepage.button.ok']}" onclick="javascript:returnSelectedProctor(); return false;" class="ui-widget-header" style="width:60px">
				<input type="button"  id="cancelDataProctor" value="${bundle.web['homepage.button.back']}" onclick="javascript:hideSelectedProctor(); return false;" class="ui-widget-header" style="width:60px">
				</center>
				<br>
			</td>
		</tr>
	</tbody>
</table>


