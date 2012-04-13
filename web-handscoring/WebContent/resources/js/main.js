
function submitPage()
{
	showLoading();
   	document.forms[0].submit();
}

function gotoAction(action)
{
	showLoading();
	if (action != null) {
    	document.forms[0].action = action;
	}
	document.forms[0].submit();
}

function gotoMenuAction(action, menuId)
{	
	var element = document.getElementById("menuId");
	if (action != null) {
    	if (menuId != null) {
    		element.value = menuId;
    	}
    	element.form.action = action;
    }
	showLoading();
    element.form.submit();   	
}    
 
function showLoading()
{	
    $(".sub_menu").hide();
	//document.body.style.cursor = 'wait';
    $(document).ajaxStop($.unblockUI); 
	$.blockUI({ message: '<img src="/ScoringWeb/resources/images/loading.gif" />',
		css: {
		border: '0px',
		backgroundColor: '#aaaaaa', 
		opacity:  0.5, width:'0px',  top:  ($(window).height() - 45) /2 + 'px', left: ($(window).width() - 45) /2 + 'px' 
	}, overlayCSS:  {  backgroundColor: '#aaaaaa', opacity:  0.5 }, baseZ:1050}); 		 
	
}

$().ready(function(){
 	var lastWindowHeight = $(window).height();
    var lastWindowWidth = $(window).width();
	$(window).resize(function() {
		if($(window).height()!=lastWindowHeight || $(window).width()!=lastWindowWidth){
			lastWindowHeight = $(window).height();
	        lastWindowWidth = $(window).width();
	        if( $("#studentScoringGrid").length > 0){
	         	$("#studentScoringGrid").setGridWidth($("#jqGrid-content-section").width());
	         }
	         if($("#sessionScoringGrid").length >0){
	         	$("#sessionScoringGrid").setGridWidth($("#jqGrid-content-section").width());
	         }
	         	       
			if($('.ui-dialog').length > 0){
				var left = (lastWindowWidth - $('.ui-dialog').outerWidth())/2 ;
				$('.ui-dialog').css({top:parseInt(lastWindowHeight*.1) + 'px',left:left + 'px'});
			}
			
		}
	});
	
});

//to add comma at every 1000th place
function addCommas(nStr)
{
	nStr += '';
	var rgx = /(\d+)(\d{3})/;
	while (rgx.test(nStr)) {
		nStr = nStr.replace(rgx, '$1' + ',' + '$2');
	}
	return nStr;
}
