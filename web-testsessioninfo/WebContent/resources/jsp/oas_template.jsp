<%@ page import="java.io.*, java.util.*"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>

  <head>
    <title><netui-template:attribute name="title"/></title>
	<link href="<%=request.getContextPath()%>/resources/css/widgets.css" type="text/css" rel="stylesheet" />
	<link href="<%=request.getContextPath()%>/resources/css/jquery-ui-1.8.16.custom.css" type="text/css" rel="stylesheet" />
    <link href="<%=request.getContextPath()%>/resources/css/ui.jqgrid.css" type="text/css" rel="stylesheet" />
    <link href="<%=request.getContextPath()%>/resources/css/autosuggest.css" type="text/css" rel="stylesheet" />
    
    <link href="<%=request.getContextPath()%>/resources/css/roundCorners.css" type="text/css" rel="stylesheet" />
    <link href="<%=request.getContextPath()%>/resources/css/main.css" type="text/css" rel="stylesheet" />
    <link href="<%=request.getContextPath()%>/resources/css/menu.css" type="text/css" rel="stylesheet" />
    <link href="<%=request.getContextPath()%>/resources/css/tabs.css" type="text/css" rel="stylesheet" />
    
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
    
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery.jstree.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/grid.locale-en.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery.jqGrid.min.js"></script>	
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/sessionList.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/sessionConstants.js"></script>
    
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/widgets.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/js_web.js"></script>   
  	
	<script type="text/javascript">
	var SelectedUserId;
	
		$(document).ready(function(){
			  
               $("#trail").click(function(){
              	  $(".panel").slideToggle("slow");
			  });
			
		});
		
			$(function(){
				// Accordion
				$("#accordion").accordion({ header: "h3" });
				var wizard = $("#ssAccordion").accordion({ header: "h3",event:false});
				$("h3", wizard).each(function(index) { 
					$(this).click(function(e){
						var divID = $(this).parent().attr('id');
						if(divID == "selectTestId"){
							if (!isFirstAccordSelected && !isThirdAccordSelected && !isFourthAccordSelected && !validateTestInformation()) {
								verifyTestDetails();
								$('#displayMessage').show();
								e.stopPropagation();
							}else{
								$('#displayMessage').hide();
								isFirstAccordSelected = true;
								isSecondAccordSelected = false;
								isThirdAccordSelected = false;
								isFourthAccordSelected = false;
								wizard.accordion("activate", index);
							}
						}else if(divID == "testDetailId"){
							if(!isThirdAccordSelected && !isFourthAccordSelected && !validateTest()){
								$('#displayMessage').show();
								e.stopPropagation(); 
							}else{
								$('#displayMessage').hide();
								isFirstAccordSelected = false;
								isSecondAccordSelected = true;
								isThirdAccordSelected = false;
								isFourthAccordSelected = false;
								wizard.accordion("activate", index);
							}
						}else if(divID == "addStudentId"){
							if(isFirstAccordSelected && !isSecondAccordSelected && !validateTest()){
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
							if(isFirstAccordSelected && !isSecondAccordSelected && !validateTest()){
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
								isFourthAccordSelected = true;
								isThirdAccordSelected = false;
								wizard.accordion("activate", index);
							}
						}			
					});
				});
				
				$('#startDate').datepicker({
					inline: true,
					clickInput:true,
					minDate: new Date(currDate)

				});
				
				$('#endDate').datepicker({
					inline: true,
					clickInput:true,					
					minDate: new Date(nextDate)

				});
				//$("#endDate").val(nextDate);
				//$("#endDate").datepicker( "setDate" , nextDate); 
				
				var startTime;
				var endTime;
				$("#slider-range").slider({
					//range: true, min: 0, max: 2879, values: [540, 1020], step:5, slide: slideTime, change: checkMax
					range: true, min: 0, max: 1425, values: [540, 1020], step:5, slide: slideTime, stop: stopSlide
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
					  <div id="featureTabsBody">
					  
					    <div id="featureTabsContainer">
					      <a href="#" id="assessmentsTabLink" onClick="gotoAction('assessments.do');" class="tab rounded {top transparent}">Assessments</a>
					      <a href="#" id="organizationsTabLink" onClick="gotoAction('organizations.do');" class="tab rounded {top transparent}">Organizations</a>
                		<c:if test="${sessionScope.showReportTab}">
					      <a href="#" id="reportsTabLink" onClick="gotoAction('reports.do');" class="tab rounded {top transparent}">Reports</a>
                		</c:if>					    
					      <a href="#" id="servicesTabLink" onClick="gotoAction('services.do');" class="tab rounded {top transparent}">Services</a>
					    </div>
 
					  	<!-- TABS BODY -->
    					<div id="featureElementsContainer" class="rounded {right bottom}">

							<!-- ASSESSMENT MENU -->	
							<div id="assessments" style="display: none">					
								<table class="toolbar">
								<tr class="toolbar">
									<td class="toolbar" width="120">
										<a href="#" id="sessionsLink" onClick="gotoMenuAction('assessments.do', 'sessionsLink');"><b>Sessions</b></a>						
									</td>
								  <c:if test="${sessionScope.hasScoringConfigured}">			      
									<td class="toolbar" width="120">
										<a href="#" id="studentScoringLink" onClick="gotoMenuAction('assessments.do', 'studentScoringLink');"><b>Student Scoring</b></a>						
									</td>
	                			  </c:if>
								  <c:if test="${sessionScope.hasProgramStatusConfigured}">			      
									<td class="toolbar" width="120">
										<a href="#" id="programStatusLink" onClick="gotoMenuAction('assessments.do', 'programStatusLink');"><b>Program Status</b></a>						
									</td>
	                			  </c:if>
									<td width="*">&nbsp;</td>		
								</tr>
								</table>						
							</div>

							<!-- ORGANIZATION MENU -->
							<div id="organizations" style="display: none">					
							</div>

							<!-- REPORTS MENU -->
							<div id="reports" style="display: none">					
							</div>

							<!-- SERVICES MENU -->
							<div id="services" style="display: none">	
							<table class="toolbar">
							<tr class="toolbar">
							<c:if test="${sessionScope.hasUploadDownloadConfigured}">			      							
								<td  class="toolbar-alignleft" width="300">
                			</c:if>					    
							<c:if test="${! sessionScope.hasUploadDownloadConfigured}">			      							
								<td  class="toolbar-alignleft" width="150">
                			</c:if>					    
									<div id="service-menu">
									<ul>
										<li>
											<a href="#"><span style="color: blue">Workstation Setup&nbsp;
												<em>
													<img src="<%=request.getContextPath()%>/resources/images/zonebar-downarrow.png" alt="dropdown" />
												</em>
											</span></a>
											<ul class="submenu" style="background: #DEECF6; display:none;">
												<li><a href="#" style="color: #0000ff" id="installSoftwareLink" onClick="gotoMenuAction('services.do', 'installSoftwareLink');">Install Software</a></li>
												<li><a href="#" style="color: #0000ff" id="downloadtestLink" onClick="gotoMenuAction('services.do', 'downloadTestLink');">Download Test</a></li>
											</ul>
										</li>
										<c:if test="${sessionScope.hasUploadDownloadConfigured}">			      							
										<li>
											<a href="#"><span style="color: blue">User/Student Load&nbsp;&nbsp;
												<em>
													<img src="<%=request.getContextPath()%>/resources/images/zonebar-downarrow.png" alt="dropdown" />
												</em>
											</span></a>
											<ul class="submenu" style="background: #DEECF6; display:none;">
												<li align="left"><a href="#" style="color: #0000ff" id="uploadDataLink" onClick="gotoMenuAction('services.do', 'uploadDataLink');">Import</a></li>
												<li><a href="#" style="color: #0000ff" id="downloadDataLink" onClick="gotoMenuAction('services.do', 'downloadDataLink');">Export</a></li>
											</ul>
										</li>
                						</c:if>					    
									</ul>
									</div>
								
								</td>
								
							  <c:if test="${sessionScope.hasLicenseConfigured}">			      
								<td class="toolbar" width="120">
									<a href="#" id="manageLicensesLink" onClick="gotoMenuAction('services.do', 'manageLicensesLink');"><b>Manage Licenses</b></a>						
								</td>
                			  </c:if>
								
								<td width="*">&nbsp;</td>		
								
								</tr>
								</table>
							</div>
					
							
					      	<div class="feature" id="bodySection">
					            <netui-template:includeSection name="bodySection"/>      	
					      	</div>

    					</div> <!-- End of TABS BODY -->
  					</div> <!-- End of TABS HEADERS -->

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
