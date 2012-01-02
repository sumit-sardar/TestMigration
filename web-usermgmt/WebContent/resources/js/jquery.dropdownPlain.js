$(function(){

    $("ul.dropdown li").hover(function(){
    	$(this).addClass("hover");
    	if($.browser.msie && $(this).hasClass('simpleMenu')){
    		$(".ui-accordion").parent().addClass('posStatAcord');
    		$(".ui-jqgrid").parent().addClass("posStat");
    		$('.ui-jqgrid-sortable').addClass('posStatHeader');
    	}    	
        $('ul:first',this).show();
    
    }, function(){
        $(this).removeClass("hover");
       	if($.browser.msie && $(this).hasClass('simpleMenu')){
    		$(".ui-jqgrid").parent().removeClass("posStat");
    		$('.ui-jqgrid-sortable').removeClass('posStatHeader');
    		$(".ui-accordion").parent().removeClass('posStatAcord');
    	}
        $('ul:first',this).hide();
    
    });
    
    //$("ul.dropdown li ul li:has(ul)").find("a:first").append("&nbsp;&nbsp;&nbsp;<img src='images/arrow_right.png' />");

    //$("ul.dropdown li ul li:has(ul)").find("a:first").append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class='ui-menuicon ui-icon-triangle-1-e' ></span>");

});