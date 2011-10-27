
  $(document).ready(function(){
    $('.feature').corners();
    $('.rounded').corners();
    $('.roundedHeader').corners(); 
    $('.roundedPrivacy').corners(); 
    $('.roundedMessage').corners(); 
    $('.treeHeader').corners('top'); 
  });

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
