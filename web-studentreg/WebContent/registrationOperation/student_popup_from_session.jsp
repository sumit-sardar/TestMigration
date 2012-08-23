<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="label.tld" prefix="lb" %>
<lb:bundle baseName="studentRegistrationResource" />


<div id="sessionStudRegId"	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<div style=" background-color: #FFFFFF; padding-left: 10px; padding-top: 10px;">
	  
	  <div>
	  	<table class="transparent" style="padding-left: 5px;">
	  		<tr class="transparent">
				<td>
				    
			    	<h1>
			    		<div id="sessionListTitle" style="display:none"><lb:label key="session.popup.title" /></div>
						<div id="studentListTitle" style="display:none"><lb:label key="student.popup.title" /></div>
			    	</h1>
				</td>
			</tr>
			<tr> 
				<td class="subtitle">  
					<div id="sessionListSubTitle" style="display:none"><lb:label key="session.popup.subtitle" /></div>
					<div id="studentListSubTitle" style="display:none"><lb:label key="student.popup.subtitle" /></div>
				</td>	
			</tr>
		</table>	
	  </div>
	  
	  
	  <div id="primaryJQGridDiv">
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
		
		<div id="secondaryJQGridDiv" style="display:none;">
			<table class="transparent" width="97%" style="margin:15px auto;">
				<tr class="transparent">
			        <td style="vertical-align:top; width:16%;" align="left">
				      	<div  id="secondaryOrgSearchHeader" class="ui-corner-tl ui-corner-tr ui-widget-header treeCtrlHeader">&nbsp;
				      	  <lb:label key="stdt.reg.label.org.search" />
				      	</div>
				    	<div id="secondaryOuterTreeFromSessionDiv" class="treeCtrl">	
					    	<div id="secondaryInnerOrgNodeHierarchy"  style="width:auto;height:auto;display:table;">
							</div>
						</div>
					 </td>
					 
				 	 <td class="transparent" width="5px">&nbsp;</td>
				 	
					 <td style="vertical-align:top;"  id="jqGrid-content-section">
				      	<table id="list3" class="gridTable"></table>
						<div id="pager3" class="gridTable"></div>			
					</td>					
				</tr>
			</table>
		</div>
			
	</div>		

	<center style="padding-top: 15px; padding-bottom: 10px;">
		<input type="button"  id="closeButtonSPF" value=<lb:label key="common.button.close" prefix="'" suffix="'" /> onclick="javascript:closePopUp('sessionStudRegId'); return false;" class="ui-widget-header" style="width:60px">
		<input type="button"  disabled="disabled" id="backButtonSPFPopup" value=<lb:label key="common.button.back" prefix="'" suffix="'" /> onclick="javascript:onBackFromShowBySessionPopUp(); return false;" class="ui-widget-header" style="width:60px">
		<input type="button" disabled="disabled" id="nextButtonStdPopup" value=<lb:label key="common.button.next" prefix="'" suffix="'" /> onclick="javascript:onNextFromShowBySessionPopUp(); return false;" class="ui-widget-header" style="width:60px">
	</center>

</div>
<!--added on 30.07.2012  -->
<div id="searchInSessionPopupByKeyword"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<div>
		<p><lb:label key="stu.search.info.message"/></p>
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
