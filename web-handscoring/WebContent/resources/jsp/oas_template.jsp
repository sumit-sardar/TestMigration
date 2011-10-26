<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.io.*, java.util.*"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>

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
    
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery-1.4.4.min.js"></script>
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
    
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/widgets.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/js_web.jsp"></script>   
  	
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
			});
		
		
	</script>
     
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
