
  $(document).ready(function(){
    roundCornerFeatureIE();    
    $('.rounded').corners();
    $('.roundedHeader').corners(); 
    $('.roundedPrivacy').corners(); 
    $('.menuText').corners('top');    
    $('.roundedMessage').corners(); 
    $('.treeHeader').corners('top');
  });

function roundCornerFeatureIE(){
 	$('.feature').corners();
 	if($("#bodySection").length>0 && $("#bodySection").children().length == 3){
 		$("#bodySection").css('padding-top','0px');
 		$("#bodySection").children().eq(1).css('padding-top','10px');
 	}
 } 
function setAnchorButtonState(elementId, disabled) {
	var element = document.getElementById(elementId);
	if (element) {
		if (disabled) {
			element.className = 'rounded {transparent} buttonDisabled';
		}
		else {
			element.className = 'rounded {transparent} button';
		}
	}
}
			
function isButtonDisabled(element) {
	if (element.className.indexOf('buttonDisabled') > 0) {
		return true;
	}
	else { 
		return false;
	}
}		
 