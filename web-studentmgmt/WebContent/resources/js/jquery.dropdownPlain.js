$(function(){

    $("ul.dropdown li").hover(function(){
    	$(this).addClass("hover");    	   	
        $('ul:first',this).show();
    
    }, function(){
        $(this).removeClass("hover");
        $('ul:first',this).hide();
    
    });
    
    //$("ul.dropdown li ul li:has(ul)").find("a:first").append("&nbsp;&nbsp;&nbsp;<img src='images/arrow_right.png' />");

    //$("ul.dropdown li ul li:has(ul)").find("a:first").append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class='ui-menuicon ui-icon-triangle-1-e' ></span>");

});