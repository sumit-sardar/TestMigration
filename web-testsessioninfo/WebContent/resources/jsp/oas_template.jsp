<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page import="java.io.*, java.util.*"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>

<html>
<!--[if lt IE 8]>
<style type='text/css'>
#orgNodeHierarchy ul {
	float: left;
	clear: both;
}

#orgNodeHierarchy li {
	clear: both;
}	

#stuOrgNodeHierarchy ul {
float: left;
clear: both;
}

#stuOrgNodeHierarchy li {
	clear: both;
}
#proctorOrgNodeHierarchy ul {
float: left;
clear: both;
}

#proctorOrgNodeHierarchy li {
	clear: both;
}


#programStatusOrgNode li {
 	display:block !important;
 }
 
#programStatusOrgNode  a {
 	display:inline-block !important;
 }
 #programStatusOrgNode ul {
 	float:left;
 }
 
</style>
<![endif]-->
  <head>
    <title><netui-template:attribute name="title"/></title>
	<link href="<%=request.getContextPath()%>/resources/css/widgets.css" type="text/css" rel="stylesheet" />
	<link href="<%=request.getContextPath()%>/resources/css/jquery-ui-1.8.16.custom.css" type="text/css" rel="stylesheet" />
    <link href="<%=request.getContextPath()%>/resources/css/ui.jqgrid.css" type="text/css" rel="stylesheet" />
    <link href="<%=request.getContextPath()%>/resources/css/autosuggest.css" type="text/css" rel="stylesheet" />
    
    <link href="<%=request.getContextPath()%>/resources/css/roundCorners.css" type="text/css" rel="stylesheet" />
    <link href="<%=request.getContextPath()%>/resources/css/main.css" type="text/css" rel="stylesheet" />
    <link href="<%=request.getContextPath()%>/resources/css/menu.css" type="text/css" rel="stylesheet" />
    <link href="<%=request.getContextPath()%>/resources/css/popup_menu.css" rel="stylesheet" type="text/css" />
    <link href="<%=request.getContextPath()%>/resources/css/editSubtest.css" type="text/css" rel="stylesheet" />
    
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/json2.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery-1.6.2.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery-ui-1.8.16.custom.min.js"></script>
  	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery.blockUI.min.js"></script>
    
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/main.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/menu.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/tabs.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/dialogs.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery.corners.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/roundCorners.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery.dropdownPlain.js"></script>
    
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery.jstree.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/grid.locale-en.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/verifyLoginUser.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery.jqGrid.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/map.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/sessionList.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/editTestSession.js"></script>    
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/addStudent.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/addProctor.js"></script>
    
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/widgets.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/js_web.js"></script>   
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/printTicket.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/js_web.jsp"></script>  
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/programStatus.js"></script>  
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/editSubtestTest.js"></script>  
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/modifyStudentManifest.js"></script>  
  	
	<script type="text/javascript">
	var SelectedUserId;
	
		$(document).ready(function(){
			  
               $("#trail").click(function(){
              	  $(".panel").slideToggle("slow");
			  });
			
		});
		
			$(function(){
				// Accordion
				$("#profileAccordion").accordion({ header: "h3"});
				$( "#profileAccordion" ).accordion({
				   	change: function(event, ui) {
				   		$("#User_Info").css("height",'250px');
						$("#Contact_Info").css("height",'250px');
						$("#Change_Pwd").css("height",'250px'); 
					}
				});

				var wizardAcc = $("#accordion").accordion({ header: "h3" });
				//var wizardAcc = $("#accordion").accordion({ header: "h3",event:false});				
			
				$("h3", wizardAcc).each(function(index) {			
					$(this).click(function(e){
						//wizardAcc.accordion("activate", index);
						setAnchorButtonState('printTicketButton', true);
						setAnchorButtonState('viewStatusButton', true);
						setAnchorButtonState('modifyStdManifestButton', true);
						setAnchorButtonState('copySessionButton', true);
						
						if(document.getElementById('selectedTestSessionId').value != ""){
							var rId = document.getElementById('selectedTestSessionId').value;
							$("#list2 #"+rId).removeClass("ui-state-highlight");
							$("#list3 #"+rId).removeClass("ui-state-highlight");
						}
					});
				}); 
				
				var sessionWizard = $("#accordion").accordion({ header: "h3",event:false});
				$("h3", sessionWizard).each(function(index) { 				
					$(this).click(function(e){
						$("#list2").jqGrid('resetSelection');
						$("#list3").jqGrid('resetSelection');
						jQuery("#list2").trigger("reloadGrid");
						jQuery("#list3").trigger("reloadGrid");
						setSelectedTestAdminId("");
					})
				});
				
				var wizard = $("#ssAccordion").accordion({ header: "h3",event:false});
				$("h3", wizard).each(function(index) { 				
					$(this).click(function(e){
					if(!noTestExist){
						var divID = $(this).parent().attr('id');
						if(divID == "selectTestId"){
						
								if(state =="EDIT" ){
								   if( isSecondAccordSelected && !validateTestInformation()){
								   		verifyTestDetails();
										$('#displayMessage').show();
										e.stopPropagation();
								   } else {
								        $('#displayMessage').hide();
								   		populateSelectTestGrid(wizard,index);
								   		isFirstAccordSelected = true;
										isSecondAccordSelected = false;
										isSelectTestDetClicked = true;
								   }
									
								}else if (!isFirstAccordSelected && !isThirdAccordSelected && !isFourthAccordSelected && !validateTestInformation()) {
									verifyTestDetails();
									$('#displayMessage').show();
									e.stopPropagation();
								}else{
									if (isSelectingStudent) {
										setMessage("Action required commit", "", "errorMessage", "Click OK to commit adding students to continue.");       
										$('#displayMessage').show();
										e.stopPropagation();
									}
									else {
										$('#displayMessage').hide();
										$('#studentAddDeleteInfo').hide();
										$('#proctorAddDeleteInfo').hide();
										isFirstAccordSelected = true;
										isSecondAccordSelected = false;
										isThirdAccordSelected = false;
										isFourthAccordSelected = false;
										wizard.accordion("activate", index);
									} 
								}
						}else if(divID == "testDetailId"){
								if(state =="EDIT"){
								   if(isFirstAccordSelected && !validateTest()){
								   		$('#displayMessage').show();
										e.stopPropagation(); 
								   } else{
								   		$('#displayMessage').hide();
								   		wizard.accordion("activate", index);
								   		isFirstAccordSelected = false;
										isSecondAccordSelected = true;
								   }
								}else if(!isThirdAccordSelected && !isFourthAccordSelected && !validateTest()){
									$('#displayMessage').show();
									e.stopPropagation(); 
								}else{
									if (isSelectingStudent) {
										setMessage("Action required commit", "", "errorMessage", "Click OK to commit adding students to continue.");       
										$('#displayMessage').show();
										e.stopPropagation();
									}
									else {								
										$('#displayMessage').hide();
										$('#studentAddDeleteInfo').hide();
										$('#proctorAddDeleteInfo').hide();
										isFirstAccordSelected = false;
										isSecondAccordSelected = true;
										isThirdAccordSelected = false;
										isFourthAccordSelected = false;
										wizard.accordion("activate", index);
									}
								}
						}else if(divID == "addStudentId"){
								if(state =="EDIT"){
								    if(isFirstAccordSelected && !validateTest()){
								    	$('#displayMessage').show();
										e.stopPropagation(); 
								    } else if(isSecondAccordSelected && !validateTestInformation()){
								        verifyTestDetails();
								    	$('#displayMessage').show();
										e.stopPropagation();
								    } else {
								    	$('#displayMessage').hide();
								    	isStdDetClicked = true;
										populateStudentGrid(wizard,index);
										isFirstAccordSelected = false;
										isSecondAccordSelected = false;
								    } 
								}else if(isFirstAccordSelected && !isSecondAccordSelected && !validateTest()){
									$('#displayMessage').show();
									e.stopPropagation(); 
								}else if (!isFirstAccordSelected && isSecondAccordSelected && !validateTestInformation()) {
									verifyTestDetails();
									$('#displayMessage').show();
									e.stopPropagation();
								}else{
									$('#displayMessage').hide();
									isFirstAccordSelected = true;
									isSecondAccordSelected = false;
									isThirdAccordSelected = true;
									isFourthAccordSelected = false;
									wizard.accordion("activate", index);
								}
						}else if(divID == "addProctorId"){							
								if(state =="EDIT"){
									if(isFirstAccordSelected && !validateTest()){
								    	$('#displayMessage').show();
										e.stopPropagation(); 
								    } else if(isSecondAccordSelected && !validateTestInformation()){
								        verifyTestDetails();
								    	$('#displayMessage').show();
										e.stopPropagation();
								    } else {
								    	$('#displayMessage').hide();
										isProcDetClicked = true;
										populateProctorGrid(wizard,index);
										isFirstAccordSelected = false;
										isSecondAccordSelected = false;
									}
								} else if(isFirstAccordSelected && !isSecondAccordSelected && !validateTest()){
									$('#displayMessage').show();
									e.stopPropagation(); 
								}else if (!isFirstAccordSelected && isSecondAccordSelected && !validateTestInformation()) {
									verifyTestDetails();
									$('#displayMessage').show();
									e.stopPropagation();
								}else{
									if (isSelectingStudent) {
										setMessage("Action required commit", "", "errorMessage", "Click OK to commit adding students to continue.");       
										$('#displayMessage').show();
										e.stopPropagation();
									}
									else {								
										$('#displayMessage').hide();
										$('#studentAddDeleteInfo').hide();
										$('#proctorAddDeleteInfo').hide();
										isFirstAccordSelected = true;
										isSecondAccordSelected = false;
										isFourthAccordSelected = true;
										isThirdAccordSelected = false;
										wizard.accordion("activate", index);
									}
								}
						}
						}	
							
					});
				});
				
				$('#startDate').datepicker({
					inline: true,
					clickInput:true,
					dateFormat: 'mm/dd/y'				

				});
				
				$('#endDate').datepicker({
					inline: true,
					clickInput:true,
					dateFormat: 'mm/dd/y'

				});
				//$("#endDate").val(nextDate);
				//$("#endDate").datepicker( "setDate" , nextDate); 
				
				var startTime;
				var endTime;
				$( "#slider-range" ).slider({
					range: true,
					min: 0,
					max: 1425,
					values: [ 480, 1020 ],
					step: 5,
					disabled: false,
					slide: function( event, ui ) {
						minutes0 = parseInt(ui.values[0] % 60,10);
						hours0 = parseInt(ui.values[0] / 60 % 24,10);
						minutes1 = parseInt(ui.values[1] % 60,10);
						hours1 = parseInt(ui.values[1] / 60 % 24,10);
						if(ui.values[0] - ui.values[1] < 15 && ui.values[0] - ui.values[1] > - 15) {
							return false;
						}
						startTime = getTime(hours0, minutes0);
						endTime = getTime(hours1, minutes1);
				
						$( "#time" ).text( startTime + " - " + endTime );
					}
				});
				
				
			});

			$(function(){				
				$("h3", "#sessionGrid").each(function(index) { 				
					$(this).click(function(e){
						$('#showSaveTestMessage').hide();
					});
				});

			});
			
		
		
	</script>
	<style>
	.ui-jqgrid-titlebar-close{
		display:none !important;   //change to hide the circle-triangle in top
	}
	</style> 
</head>

 <body>

<!-- MAIN BODY -->
<table class="simpleBody" >
  
	<tr>
		<td align="center" valign="top" >
			<table class="bodyLayout">

				<!-- HEADER SECTION -->
				<tr class="bodyLayout">
					<td>
    					<jsp:include page="/resources/jsp/oas_header.jsp" />  					 
					</td>
				</tr>


				<!-- TABS SECTION -->
				<tr>
				  	<td align="left" valign="top">

					  <!-- TABS HEADERS -->
					  <jsp:include page="/resources/jsp/oas_navigation_menu.jsp" />
						<div class="feature" id="bodySection">
							<table width="100%" border="0" bgcolor="#FFFFFF" cellpadding="0" cellspacing="0" >
							<tr>
				  			<td valign="top">
							<netui-template:includeSection name="bodySection"/>
							</td>
							</tr>
							</table>
						</div>

					</td>
				</tr>
			
				<!-- FOOTER SECTION -->
				<tr>
				  	<td align="left" valign="top">
    					<jsp:include page="/resources/jsp/oas_footer.jsp" />  
				  	</td>
				</tr>

			</table>
		</td>
	</tr>


</table>
 
</body>

</html>
