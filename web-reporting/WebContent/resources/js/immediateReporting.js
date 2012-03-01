

function populateContentArea(element) {
	var catalogName = $(element).val();
	var postDataObject = {};
 	postDataObject.catalogName = catalogName;
 		
		$.ajax({
		async:		true,
		beforeSend:	function(){
						
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
						
											
					},
		error  :    function(XMLHttpRequest, textStatus, errorThrown){
						$.unblockUI();  
						window.location.href="/TestSessionInfoWeb/logout.do";
						
					}
		
	});
	
	
}