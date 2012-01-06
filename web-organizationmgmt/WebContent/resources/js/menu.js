
 
var serviceMenuOpenned = false;

$(document).ready(function(){
	
   $("#service-menu li span").click(function() {

   		var hidden = $(this).parents("li").children("ul").is(":hidden");

		$("#service-menu>ul>li>ul").hide();
	   	$("#service-menu>ul>li>a").removeClass();

	   	if (hidden) {
	   		$(this)
		   		.parents("li").children("ul").toggle()
		   		.parents("li").children("a").addClass("zoneCur");
		   	}
	   	
	   	serviceMenuOpenned = true;
	   	
	   });

   $("#featureTabsBody").click(function() {
	   
	   if (! serviceMenuOpenned) {
		   $("#service-menu>ul>li>ul").hide();
	   		$("#service-menu>ul>li>a").removeClass();
	   }

	   serviceMenuOpenned = false;
	   
	   });

});
 
  
function selectLink(menuId, select) 
{
	if (menuId != null) {
		var menu = document.getElementById(menuId);
		if (menu != null) {
			if (select) {
				menu.className = "selected";
				menu.onclick = "return true;";				
			}
			else { 
				menu.className = "normal";
			}
		}
	}
}

function selectAssessmentsLink(menuId) 
{
	selectLink("sessionsLink", false); 
	selectLink("scheduleSessionLink", false); 
	selectLink("studentScoringLink", false); 
	selectLink("programStatusLink", false); 

	selectLink(menuId, true); 
	
	selectAssessmentsContent(menuId);
	
}

function selectOrganizationsLink(menuId) 
{
	selectLink("organizationsLink", false); 
	selectLink("usersLink", false); 
	selectLink("studentsLink", false); 
	selectLink("bulkAccomLink", false);
	
	selectLink(menuId, true); 
	
	selectOrganizationsContent(menuId);
	
}

function selectServicesLink(menuId) 
{
	selectLink("manageLicensesLink", false); 
	
	selectLink(menuId, true); 
	
	selectServicesContent(menuId); 
	
}

function selectAssessmentsContent(menuId) 
{
	var element = document.getElementById("assessmentsContentImage");
	if (element != null) {
		switch(menuId){
			case "sessionsLink":
				element.src = "images/sessions.jpg";
	    		break;
			case "scheduleSessionLink":
				element.src = "images/scheduletest.jpg";
	    		break;
			case "studentScoringLink":
				element.src = "images/student.jpg";
	    		break;
			case "programStatusLink":
				element.src = "images/programstatus.jpg";
	    		break;
		}
	}
}

function selectOrganizationsContent(menuId) 
{
	var element = document.getElementById("organizationsContentImage");
	if (element != null) {	
		switch(menuId){
			case "organizationsLink":
				element.src = "images/organization.jpg";
	    		break;
			case "usersLink":
				element.src = "images/users.jpg";
	    		break;
			case "studentsLink":
				element.src = "images/student.jpg";
	    		break;
		}
	}
}

function selectServicesContent(menuId) 
{
	var element = document.getElementById("servicesContentImage");
	if (element != null) {		
		switch(menuId){
			case "manageLicensesLink":
				element.src = "images/managelicense.jpg";
	    		break;
			case "installSoftwareLink":
				element.src = "images/downloadsoftware.jpg";
	    		break;
			case "downloadtestLink":
				element.src = "images/downloadtest.jpg";
	    		break;
			case "uploadDataLink":
				element.src = "images/uploaddata.jpg";
	    		break;
			case "downloadDataLink":
				element.src = "images/downloaddata.jpg";
	    		break;
		}
	}
}

function setMenuActive(topMenuId, menuId) 
{
	var assessmentsObj = document.getElementById("assessments");
	var organizationsObj = document.getElementById("organizations");
	var reportsObj = document.getElementById("reports");
	var servicesObj = document.getElementById("services");

	if(assessmentsObj != null) { assessmentsObj.className = "simpleMenu"; }
	if(organizationsObj != null) { organizationsObj.className = "simpleMenu"; }
	if(reportsObj != null) { reportsObj.className = "simpleMenu"; }
	if(servicesObj != null) { servicesObj.className = "simpleMenu"; }
	
	var topMenu = document.getElementById(topMenuId);
	topMenu.className = "simpleMenu active";
	$("#"+topMenuId+" a").addClass("tab_selected");
	$('.simpleMenu').corners('top');
}


