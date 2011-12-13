 
 function selectTab(tabId, menuId) {
	var tabIndex = getTabIndex(tabId);
	$('table', $('#featureTabsContainer .tab')[tabIndex]).each(function(){$('.native').hide();});
	$('#featureTabsContainer').show();
	activateTab(tabIndex);
	activateMenu(tabId, menuId);
	disableTabLink(tabId);
}

function activateTab(tabIndex) {
	$('#featureTabsContainer .tab').removeClass('tab_selected');
	$($('#featureTabsContainer .tab')[tabIndex]).addClass('tab_selected');
}

function activateMenu(tabId, menuId) {
	$("#assessments").css("display", "none");
	$("#organizations").css("display", "none");
	$("#reports").css("display", "none");
	$("#services").css("display", "none");
	switch(tabId) {
		case "assessments":
			$("#assessments").css("display", "block");
			selectAssessmentsLink(menuId);
			break;
		case "organizations":
			$("#organizations").css("display", "block");
			selectOrganizationsLink(menuId);
			break;
		case "reports":
			$("#reports").css("display", "block");
			break;
		case "services":
			$("#services").css("display", "block");
			selectServicesLink(menuId);
		break;
	}
}

function getTabIndex(tabId) {

	var tabIndex = 0;	
	var theTabs = $('#featureTabsContainer .tab');
	for (var i=0 ; i<theTabs.length ; i++) {
		if (theTabs[i].textContent.toUpperCase() == tabId.toUpperCase()) {
			tabIndex = i;
			break;
		}
	} 
	return tabIndex;
}

function disableTabLink(tabId) {
	var tabLink = document.getElementById(tabId);
	tabLink.onclick = "return true;";
}

