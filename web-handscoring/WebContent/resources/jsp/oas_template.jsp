<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.io.*, java.util.*"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="label.tld" prefix="lb" %>
<lb:bundle baseName="studentScoringResources" />
<netui-data:declareBundle bundlePath="studentScoringResources" name="scor"/>

<netui-template:setAttribute name="title" value="${bundle.scor['studentScoring.window.title']}"/>

<!--[if IE]><![endif]-->
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6">    <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8">    <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9">    <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--><html lang="en" class="no-js"><!--<![endif]-->
<!--[if lt IE 8]>
<style type='text/css'>
#scoringOrgNode ul {
	float: left;
	clear: both;
}

#scoringOrgNode li {
	clear: both;
}
</style>
<![endif]-->
  <head>
    <title><netui-template:attribute name="title"/></title>
	<link href="<%=request.getContextPath()%>/resources/css/widgets.css" type="text/css" rel="stylesheet" />
	<link href="<%=request.getContextPath()%>/resources/css/jquery-ui-1.8.16.custom.css" type="text/css" rel="stylesheet" />
    <link href="<%=request.getContextPath()%>/resources/css/ui.jqgrid.css" type="text/css" rel="stylesheet" />
   
    <link href="<%=request.getContextPath()%>/resources/css/autosuggest.css" type="text/css" rel="stylesheet" />
    <link href="<%=request.getContextPath()%>/resources/css/jquery.ui.dialog.css" type="text/css" rel="stylesheet" />
    <link href="<%=request.getContextPath()%>/resources/css/roundCorners.css" type="text/css" rel="stylesheet" />
    <link href="<%=request.getContextPath()%>/resources/css/main.css" type="text/css" rel="stylesheet" />
    <link href="<%=request.getContextPath()%>/resources/css/menu.css" type="text/css" rel="stylesheet" />
    <link href="<%=request.getContextPath()%>/resources/css/popup_menu.css" rel="stylesheet" type="text/css" />
    
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery-1.4.4.min.js"></script>
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
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery.jqGrid.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/verifyLoginUser.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/map.js"></script>
    
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/widgets.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/js_web.jsp"></script>   

  	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/hierarchy.js"></script>
  	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/verifyLoginUser.js"></script>
  	
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
				$("#profileAccordion").accordion({ header: "h3"});
				$( "#profileAccordion" ).accordion({
				   	change: function(event, ui) {
				   		$("#User_Info").css("height",'250px');
						$("#Contact_Info").css("height",'250px');
						$("#Change_Pwd").css("height",'250px'); 
					}
				});
				var scoringWizard = $("#sessionScoringAccordion").accordion({ header: "h3",event:false});
				$("h3", scoringWizard).each(function(index) { 				
					$(this).click(function(e){
					if(index == 1){
						scoringWizard.accordion("activate", index);
						enableScoreByItemGrid();
					} else {
						scoringWizard.accordion("activate", index);
					}
						
					});
				});
				
				$( "#sessionScoringAccordion" ).accordion({
				   	change: function(event, ui) {
				   		$("#score_by_student_details_div").css("height",'535px');
						$("#score_by_item_details_div").css("height",'535px');
					}
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
    					<%@include file="/resources/jsp/oas_header.jsp" %>  					 
					</td>
				</tr>


				<!-- TABS SECTION -->
				<tr>
				  	<td align="left" valign="top">

					  <!-- TABS HEADERS -->
					  <%@include file="/resources/jsp/oas_navigation_menu.jsp" %>
						<div class="feature" id="bodySection">
							<table width="100%" border="0" bgcolor="#FFFFFF" cellpadding="0" cellspacing="0" >
							<tr>
				  			<td  valign="top">
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
    					<%@include file="/resources/jsp/oas_footer.jsp" %>
				  	</td>
				</tr>

			</table>
		</td>
	</tr>


</table>
 
</body>

</html>
