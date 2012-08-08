<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="label.tld" prefix="lb" %>
<lb:bundle baseName="studentRegistrationResource" />


<div id="sessionStudRegId"	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<br>
	  <div style=" background-color: #FFFFFF; padding: 10px 10px 5px;">
	  
	  <div>
	  	<table class="transparent" style="padding-left: 5px;">
	  		<tr class="transparent">
				<td>
			    	<h1><lb:label key="student.registration.menu" /></h1>
				</td>
			</tr>
			<tr> 
				<td class="subtitle">  
					<lb:label key="student.popup.subtitle" />
				</td>	
			</tr>
		</table>	
	  </div>
	  
	  
	  
		<table class="transparent" width="97%" style="margin:15px auto;">
		<tr class="transparent">
		        <td style="vertical-align:top; width:16%;" align="left">
			      	<div  id= "stdFromSessionOrgSearchHeader" class="ui-corner-tl ui-corner-tr ui-widget-header treeCtrlHeader">&nbsp;
			      	  <lb:label key="stdt.reg.label.org.search" />
			      	</div>
			    	<div id="outerTreeForStudentFromSessionDiv" class="treeCtrl">	
				    	<div id="innerOrgNodeHierarchyForStd"  style="width:auto;height:auto;display:table;">
						</div>
					</div>
				 </td>
				 
			 	 <td class="transparent" width="5px">&nbsp;</td>
			 	
				 <td style="vertical-align:top;"  id="jqGrid-content-section">
			      	<table id="list2" class="gridTable"></table>
					<div id="pager2" class="gridTable"></div>			
				</td>
				
				</tr>
			</table>
	</div>		
	<br>
	<center>
		<input type="button"  id="closeButtonSPF" value=<lb:label key="common.button.close" prefix="'" suffix="'" /> onclick="javascript:closePopUp('sessionStudRegId'); return false;" class="ui-widget-header" style="width:60px">
		
		<input type="button" disabled="disabled" id="nextButtonStdPopup" value=<lb:label key="common.button.next" prefix="'" suffix="'" /> onclick="javascript:onNextFromShowBySessionPopUp(); return false;" class="ui-widget-header" style="width:60px">
	</center>
	<br>
</div>
<!--added on 30.07.2012  -->
<div id="searchInSessionPopupByKeyword"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<div>
		<p><lb:label key="ses.search.info.message"/></p>
	</div>
	<div class="searchInputBoxContainer" id="searchInputBoxContainerPopup">
		<center>
			<input type="text" name="searchInSessionPopupByKeywordInput" id="searchInSessionPopupByKeywordInput" onkeypress="trapEnterKeyInSessionPopup(event);"/>
		</center>	
	</div>
	<div style="padding-bottom:20px;"> 
		<center>
			<input type="button"  value=<lb:label key="common.button.clear" prefix="'" suffix="'"/> onclick="javascript:resetSearchInSessionPopup(); return false;" class="ui-widget-header">&nbsp;
			<input type="button"  value=<lb:label key="common.button.search" prefix="'" suffix="'"/> onclick="javascript:searchInSessionPopupByKeyword(); return false;" class="ui-widget-header">
		</center>
	</div>
</div>