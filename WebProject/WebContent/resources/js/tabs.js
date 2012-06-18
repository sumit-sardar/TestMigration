

function selectTab(tabId, menuId) 
{
	var tabIndex = getTabIndex(tabId);

    $('table', $('#featureTabsContainer .tab')[tabIndex]).each(function(){$('.native').hide();});
    $('#featureTabsContainer').show();
    
    activateTab(tabIndex);

	activateMenu(tabIndex, menuId);

}

function activateTab(tabIndex) {
    $('#featureTabsContainer .tab').removeClass('tab_selected');
    $($('#featureTabsContainer .tab')[tabIndex]).addClass('tab_selected');
}
    

function activateMenu(tabIndex, menuId) {
    
	$("#assessments").css("display", "none");
	$("#organizations").css("display", "none");
	$("#reports").css("display", "none");
	$("#services").css("display", "none");
	switch(tabIndex) {
		case 0:
			$("#assessments").css("display", "block");
			selectAssessmentsLink(menuId); 
    		break;
		case 1:
			$("#organizations").css("display", "block");
			selectOrganizationsLink(menuId); 
    		break;
		case 2:
			$("#reports").css("display", "block");
    		break;
		case 3:
			$("#services").css("display", "block");
			selectServicesLink(menuId); 
    		break;
    }
}

function getTabIndex(tabId) {
	var tabIndex = 0;
	
	switch(tabId) {
		case 'assessments':
			tabIndex = 0;
			break;
		case 'organizations':
			tabIndex = 1;
			break;
		case 'reports':
			tabIndex = 2;
			break;
		case 'services':
			tabIndex = 3;
			break;
	}	
	return tabIndex;		
}
  
