

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
			if (select)
				menu.className = "selected";
			else 
				menu.className = "normal";
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
}

function selectOrganizationsLink(menuId) 
{
	selectLink("organizationsLink", false); 
	selectLink("usersLink", false); 
	selectLink("studentsLink", false); 

	selectLink(menuId, true); 
}

function selectServicesLink(menuId) 
{
	selectLink("manageLicensesLink", false); 
	
	selectLink(menuId, true); 
}

