

function populateContentArea(element) {
	var catalogName = $(element).val();
	var postDataObject = {};
 	postDataObject.catalogName = catalogName;
 		
		$.ajax({
		async:		true,
		beforeSend:	function(){
						blockUI();
					},
		url:		'getContentAreasForCatalog.do', 
		type:		'POST',
		dataType:	'json',
		data:		postDataObject,
		success:	function(data, textStatus, XMLHttpRequest){	
						
						var optionValues = "<option value='Any content area'>Any content area</option>";
						if(data != undefined && data != null && data.length > 0) {
							for(var i = 0; i < data.length; i++) {
								optionValues = optionValues + "<option value='"+data[i].itemId+"'>"+data[i].itemSetName+"</option>"
							}
						}
						$("#contentAreas").html(optionValues);
						unblockUI();
											
					},
		error  :    function(XMLHttpRequest, textStatus, errorThrown){
						$.unblockUI();  
						window.location.href="/TestSessionInfoWeb/logout.do";
						
					},
					
		complete :  function(){
					unblockUI();
											}
		
	});
	
	
}

function blockUI()
		{	
			$("body").append('<div id="blockDiv" style="background:url(/ReportingWeb/resources/images/transparent.gif);position:fixed;top:0;left:0;width:100%;height:100%;z-index:9999"><img src="/HandScoringWeb/resources/images/loading.gif" style="left:50%;top:40%;position:absolute;"/></div>');
			$("#blockDiv").css("cursor","wait");
			
		}
			
		function unblockUI()
		{
			$("#blockDiv").css("cursor","normal");
			$("#blockDiv").remove();
		}