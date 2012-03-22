$(document).ready(function() {  
       var questionNumber = $("#itemNumber").val();	
	$("#rubricDialogID").dialog({   
		 title:"Item No. "+questionNumber, 
		 resizable:false,	              
		 autoOpen : false			    
 	});
});

function formSubmit(itemId, itemType, itemSetId, itemNumber, rowno) {

var isHidden = $('#dialogID').is(':hidden');  
if(isHidden){		
			var param = "&itemId="+itemId+"&itemType="+itemType+"&itemSetId="+itemSetId+"&rosterId="+$("#rosterId").val();
			document.getElementById("itemId").value = itemId;
			document.getElementById("itemSetId").value = itemSetId;
			document.getElementById("itemNumber").value = itemNumber;
			document.getElementById("message").style.display = 'none';
			document.getElementById("rowNo").value = rowno;

			$.ajax(
				{
						async:		true,
						beforeSend:	function(){
										blockUI();
										$("#audioPlayer").hide();
										$("#crText").hide();
										$("#iframeDiv").hide();
										
									},
						url:		'beginCRResponseDisplay.do',
						type:		'POST',
						data:		param,
						dataType:	'json',
						success:	function(data, textStatus, XMLHttpRequest){	
										var crTextResponse = "";
										var isAudioItem = data.answer.isAudioItem;
										var linebreak ="\n\n";
										
										if(isAudioItem){
											document.getElementById("itemType").value = "AI";
											var audioResponseString = data.answer.audioItemContent;
											audioResponseString = audioResponseString.substr(13);
											audioResponseString = audioResponseString.split("%3C%2F");
											document.getElementById("audioResponseString").value = audioResponseString[0];
											document.getElementById("pointsDropDown").setAttribute("disabled",true);
											document.getElementById("Question").setAttribute("disabled",true);
											//$("#crText").hide();
									
											if(navigator.userAgent.indexOf('Win') != -1) {

												$("#iframeDiv").hide();
												var iframe = $("#iframeAudio");
												$(iframe).attr('src', "about:blank");
												
												$("#audioPlayer").show();
												getAudioPlayer('audioPlayer');
											
											}else {

												$("#audioPlayer").hide();
												$("#iframeDiv").show();
												var iframe = $("#iframeAudio");
												$(iframe).attr('src', "audioPlayer.jsp");
											}
											
											openPopup(rowno, itemNumber);
											updateScore(rowno);  //Changes for defect #66157
										}
										else{
											document.getElementById("itemType").value = "CR";								
											var crResponses =data.answer.cRItemContent.string.length;
											for(var i = 0; i < crResponses; i++){
											if( i == (crResponses-1)){
											linebreak ="";
											document.getElementById("pointsDropDown").removeAttribute("disabled");
											document.getElementById("Question").removeAttribute("disabled");
										}
										 crTextResponse = crTextResponse + data.answer.cRItemContent.string[i] + linebreak;

										}

										openPopup(rowno, itemNumber);
										//$("#audioPlayer").hide();
										$("#crText").show();
										$("#crText").val(crTextResponse);
										updateScore(rowno);
										}									
									},
						error  :    function(XMLHttpRequest, textStatus, errorThrown){
										//changes for defect #66003
										window.location.href="/TestSessionInfoWeb/logout.do";
										
									},
						complete :  function(){
										//alert('after complete....');
										unblockUI();
									}
				}
			);
			}
			}
	var data1;
	function viewRubric (itemIdRubric, itemNumber) {
	
	var param = "&itemId="+itemIdRubric+"&itemNumber="+itemNumber;
	$("#itemId").val(itemIdRubric);
	$("#itemNumber").val(itemNumber);
	
	var isHidden = $('#rubricDialogID').is(':hidden');  
	
	
	if(isHidden){
		$.ajax(
		{
				async:		false,
				beforeSend:	function(){
								blockUI();

							},
				url:		'rubricViewDisplay.do',
				type:		'POST',
				data:		param,
				dataType:	'json',
				success:	function(data, textStatus, XMLHttpRequest){									
								 var questionNumber = $("#itemNumber").val();
								 
								 data1 = data;
								 populateTable();
								 var width = '55%';
								 //$("#rubricDialogID").dialog("open");
								 $("#rubricDialogID").dialog({   
									 title:"Item No. "+questionNumber, 
									 resizable:false,
									 autoOpen: true,
									 width: width
 									});
								 if(data.rubricData.entry)
								 $("#rubricDialogID").css('height',300);
								 else
								 $("#rubricDialogID").css("height",'auto');	
								 						 						
							},
				error  :    function(XMLHttpRequest, textStatus, errorThrown){
							},
				complete :  function(){
								unblockUI();
							}
				}
			);
			
		}	
	}
	
	function populateTable () {
	
		var subIframe = $('#subIframe'); 
		var iFrameObj = subIframe.contents();
											
		var counter = 0;
		var rowCountRubric = iFrameObj.find("#rubricTable tr").length;
											
		var rowCountExemplar = iFrameObj.find("#exemplarsTable tr").length;								
		var cellCountExemplar = iFrameObj.find("#exemplarsTable tr td").length;		
																			
		//Rows needs to be deleted, since dynamically new rows are added everytime
		iFrameObj.find("#rubricTable tr:not(:first)").remove();
		iFrameObj.find("#exemplarsTable tr:not(:first)").remove();
		$("#rubricDialogID").css('height',300);
											
											 if(cellCountExemplar == 1)	 {
											 	iFrameObj.find("#rubricExemplarId").hide();
											 	iFrameObj.find("#exemplarNoDataId").show();
											 } else {
											 	iFrameObj.find("#rubricExemplarId").show();
											 	iFrameObj.find("#exemplarNoDataId").hide();
											 }
											 	//alert(data.rubricData.entry);							 
											 if(data1.rubricData.entry) {
											 	//alert("...."+data.rubricData.entry);		
											 	iFrameObj.find("#rubricNoDataId").hide();
											 	iFrameObj.find("#rubricTableId").show();								 								 
											 	for(var i=0;i<data1.rubricData.entry.length;i++) {									
													var description = handleSpecialCharacters(data1.rubricData.entry[i].rubricDescription);								
													iFrameObj.find("#rubricTable tr:last").
														after('<tr><td><center><small>'+
															data1.rubricData.entry[i].score+
																'</small></center></td><td><small>'+description+'</small></td></tr>');
			
													if(data1.rubricData.entry[i].rubricExplanation){
														var explanation = handleSpecialCharacters(data1.rubricData.entry[i].rubricExplanation);
														var response = handleSpecialCharacters(data1.rubricData.entry[i].sampleResponse);
														iFrameObj.find("#exemplarsTable tr:last").
															after('<tr><td><center><small>'+
																data1.rubricData.entry[i].score+
																	'</small></center></td><td><small>'+
																		response+
																			'</small></td><td><small>'+
																				explanation+
																					'</small></td></tr>');																		
													} else {
														counter++;
														if(counter==data1.rubricData.entry.length) {
															iFrameObj.find("#exemplarNoDataId").show();
															iFrameObj.find("#rubricExemplarId").hide();
														}
													}
												}
											} else {
												iFrameObj.find("#exemplarNoDataId").show();
												iFrameObj.find("#rubricNoDataId").show();
												iFrameObj.find("#rubricExemplarId").hide();
												iFrameObj.find("#rubricTableId").hide();									
											}										
								  		  
	
	}
	
	function ItemformSubmit(itemId, itemType, itemSetId, itemNumber, rowno, loginId ,rosterId) {

var isHidden = $('#dialogID').is(':hidden');  
if(isHidden){
			//changes for defect #66156 & #66159   
			$("#rosterId").val(rosterId);		
			var param = "&itemId="+itemId+"&itemType="+itemType+"&itemSetId="+itemSetId+"&rosterId="+rosterId;
			document.getElementById("itemId").value = itemId;
			document.getElementById("itemSetId").value = itemSetId;
			document.getElementById("itemNumber").value = itemNumber;
			document.getElementById("message").style.display = 'none';
			if(rowno != null)
				document.getElementById("rowNo").value = rowno;
			
			

			$.ajax(
				{
						async:		true,
						beforeSend:	function(){
										blockUI();
										$("#audioPlayer").hide();
										$("#crText").hide();
										$("#iframeDiv").hide();
										
									},
						url:		'beginCRResponseDisplay.do',
						type:		'POST',
						data:		param,
						dataType:	'json',
						success:	function(data, textStatus, XMLHttpRequest){	
										var crTextResponse = "";
										var isAudioItem = data.answer.isAudioItem;
										var linebreak ="\n\n";
										
										if(isAudioItem){
											document.getElementById("itemType").value = "AI";
											var audioResponseString = data.answer.audioItemContent;
											audioResponseString = audioResponseString.substr(13);
											audioResponseString = audioResponseString.split("%3C%2F");
											document.getElementById("audioResponseString").value = audioResponseString[0];
											document.getElementById("pointsDropDown").setAttribute("disabled",true);
											document.getElementById("Question").setAttribute("disabled",true);
											//$("#crText").hide();
											if(navigator.userAgent.indexOf('Win') != -1) {

												$("#iframeDiv").hide();
												var iframe = $("#iframeAudio");
												$(iframe).attr('src', "about:blank");
												
												$("#audioPlayer").show();
												getAudioPlayer('audioPlayer');
											
											}else {

												$("#audioPlayer").hide();
												$("#iframeDiv").show();
												var iframe = $("#iframeAudio");
												$(iframe).attr('src', "audioPlayer.jsp");
											}
											
											if(rowno != null){
												openPopupForItem(loginId);
												updateScore(rowno);
											}
											if(rowno == null){
												openPopupForItem(loginId);
												
											}
											
										}
										else{
										document.getElementById("itemType").value = "CR";								
											var crResponses =data.answer.cRItemContent.string.length;
											for(var i = 0; i < crResponses; i++){
											if( i == (crResponses-1)){
											linebreak ="";
											document.getElementById("pointsDropDown").removeAttribute("disabled");
											document.getElementById("Question").removeAttribute("disabled");
										}
										 crTextResponse = crTextResponse + data.answer.cRItemContent.string[i] + linebreak;

										}

										
										//$("#audioPlayer").hide();
										$("#crText").show();
										$("#crText").val(crTextResponse);
										
										if(rowno != null){
												openPopupForItem(loginId);
												updateScore(rowno);
											}
											if(rowno == null){
												openPopupForItem(loginId);
												
											}
										}									
									},
						error  :    function(XMLHttpRequest, textStatus, errorThrown){
										//changes for defect #66003
										window.location.href="/TestSessionInfoWeb/logout.do";
										
									},
						complete :  function(){
										//alert('after complete....');
										unblockUI();
									}
				}
			);
			}
			}
	
	
	
	function handleSpecialCharacters(s) {
		s= s.replace(/&nbsp;/g,' ').split('');
		var k;
		for(var i= 0; i<s.length; i++){
			k= s[i];
			c= k.charCodeAt(0);
			s[i]= (function(){
				switch(c){
					case 60: return "&lt;";
					case 62: return "&gt;";
					case 34: return "&quot;";
					case 38: return "&amp;";
					case 39: return "&#39;";
					//For IE
					case 65535: {
						if(!((s[i-1].charCodeAt(0)>=65 && s[i-1].charCodeAt(0)<=90) || (s[i-1].charCodeAt(0)>=97 && s[i-1].charCodeAt(0)<=122)) || !((s[i+1].charCodeAt(0)>=65 && s[i+1].charCodeAt(0)<=90) || (s[i+1].charCodeAt(0)>=97 && s[i+1].charCodeAt(0)<=122)))
							return "&quot;";
						else
							return "&#39;";
					}
					//For Mozila and Safari
					case 65533: {
						if(!((s[i-1].charCodeAt(0)>=65 && s[i-1].charCodeAt(0)<=90) || (s[i-1].charCodeAt(0)>=97 && s[i-1].charCodeAt(0)<=122)) || !((s[i+1].charCodeAt(0)>=65 && s[i+1].charCodeAt(0)<=90) || (s[i+1].charCodeAt(0)>=97 && s[i+1].charCodeAt(0)<=122)))
							return "&quot;";
						else
							return "&#39;";
					}
					default: return k;
				}
			})();
		}
		return s.join('');
	}
	
		function formSave() {
			var itemId =  document.getElementById("itemId").value ;
			var itemSetId = document.getElementById("itemSetId").value  ;
			var itemNumber = document.getElementById("itemNumber").value;
			var rowno = document.getElementById("rowNo").value;
			var optionValue = $("#pointsDropDown option:selected").val();
			var itemSetIdTC = document.getElementById("itemSetIdTC").value;
			var param = "&itemId="+itemId+"&itemSetId="+itemSetId+"&rosterId="+$("#rosterId").val() + "&score="+$("#pointsDropDown option:selected").val()+ "&itemSetIdTC="+itemSetIdTC;    
			
			
			if(optionValue == null || optionValue == "" ){
				document.getElementById("message").style.display = 'inline';
				var spanElement = document.getElementById("messageSpan");
				spanElement.innerHTML = "<b> Please select a valid Score. </b>";
			}
		
			if($("#pointsDropDown option:selected").val() != ''){
					$.ajax(
						{
								async:		true,
								beforeSend:	function(){
											
												blockUI();
												//alert('before send....');
											},
								url:		'saveDetails.do',
								type:		'POST',
								data:		param,
								dataType:	'json',
								success:	function(data, textStatus, XMLHttpRequest){	
													
													var isSuccess = data.SaveStatus.isSuccess;	
													
													var completionStatus = null;
													var completionStatusTD = null;
													var processScores;
													var immediateScoreReport = null;
													if(data.SaveStatus.completionStatus!=null && data.SaveStatus.completionStatus != "FromItem" )
													{
													 completionStatus = data.SaveStatus.completionStatus;
													 processScores = document.getElementById("rescoreStudent");
													 immediateScoreReport = document.getElementById("ImmediateScoreReportBN");
													}
													if(data.SaveStatus.completionStatusTD!=null && data.SaveStatus.completionStatusTD=="CO" 
														&& data.SaveStatus.completionStatus != "FromItem" )
													{
														completionStatusTD = data.SaveStatus.completionStatusTD;
														immediateScoreReport = document.getElementById("ImmediateScoreReportBN");
													}
													var spanElement = document.getElementById("messageSpan");
													var scorePointsElement = document.getElementById("scorePoints"+rowno);
													var scoreStatusElement = document.getElementById("scoreStatus"+rowno);
								                      
													
													if(isSuccess){
														scorePointsElement.firstChild.nodeValue = $("#pointsDropDown option:selected").val();
														scoreStatusElement.innerHTML = "Complete"; 
														document.getElementById("messageStatus").value = isSuccess;
														document.getElementById("message").style.display = 'inline';	
														spanElement.innerHTML = "<b> Item scored successfully. </b>";
												        if(completionStatus !=null && completionStatus=="CO" && processScores != null)    // END- Change for  #66660 enhancement
												        {
												         if(processScores != null && processScores != undefined) {
												         	processScores.removeAttribute("disabled"); 
												         }
												         if(immediateScoreReport != null && immediateScoreReport != undefined ) {
												         	immediateScoreReport.removeAttribute("disabled"); 
												         }
												          
												        }
												        if(completionStatusTD !=null && completionStatusTD=="CO" && immediateScoreReport != null)
												        {
												        	if(immediateScoreReport != undefined ) {
												         		immediateScoreReport.removeAttribute("disabled"); 
												         }
												          
												        }
													}
													else{				
														spanElement.innerHTML = "<b> Item not scored. </b>";
													}
													
												
											},
								error  :    function(XMLHttpRequest, textStatus, errorThrown){
												//changes for defect #66003
												window.location.href="/TestSessionInfoWeb/logout.do";
											},
								complete :  function(){
												//alert('after complete....');
												unblockUI();
											}
						}
					);
				}
			}
	
	
		function openPopupForItem( loginId) {
				var maxPointsElement = document.getElementById("maxPoints");
				var scoreCutOff = maxPointsElement.value;
		        var titleString = "Scoring for "+ loginId ;    //Changes for defect #66161 
		      $("#dialogID").dialog({title:titleString, resizable:false, beforeclose: function(event, ui) { stopAudio(); showScoreSelect("true");} });
		        //$("#dialogID").dialog({title:titleString});
		        updateMaxPoints(scoreCutOff);
				
		}
		function openPopup(rowno, itemNumber) {
				var maxPointsElement = document.getElementById("maxPoints"+rowno);
		        var scoreCutOff = maxPointsElement.firstChild.nodeValue;
		        var titleString = "Scoring for Item No. "+ itemNumber ;   //Changes for defect #66161
		      $("#dialogID").dialog({title:titleString, resizable:false, beforeclose: function(event, ui) { stopAudio(); showScoreSelect("true");} });
		        //$("#dialogID").dialog({title:titleString});
		        updateMaxPoints(scoreCutOff);
				
		}
		function blockUI()
		{	
			$("body").append('<div id="blockDiv" style="background:url(/HandScoringWeb/resources/images/transparent.gif);position:fixed;top:0;left:0;width:100%;height:100%;z-index:9999"><img src="/HandScoringWeb/resources/images/loading.gif" style="left:50%;top:40%;position:absolute;"/></div>');
			$("#blockDiv").css("cursor","wait");
			
		}
			
		function unblockUI()
		{
			$("#blockDiv").css("cursor","normal");
			$("#blockDiv").remove();
		}
		
		
		function closePopUp(){
			//alert("closed");
			stopAudio();
			//document.getElementById("pointsDropDown").setAttribute("disabled",true);
			$("#dialogID").dialog("close");
		}
			
	function updateMaxPoints(scoreCutOff){
		var select = document.getElementById('pointsDropDown');
		 select.options.length = 0; 
		 addOption(select , "Please Select", "" );
		
		  for(var i=0; i <= scoreCutOff; i++) {  
		    addOption(select,i,i);
		     } 
		}
	
		function updateScore(rowno){
		
			var scoreStatusElement = document.getElementById("scoreStatus"+rowno);
			var scorePointsElement = document.getElementById("scorePoints"+rowno);
			var complete = scoreStatusElement.firstChild.nodeValue;
				complete  = trim(complete);
			if(complete =="Complete"){
		
				var select = document.getElementById('pointsDropDown');
				
				for(var i=0; i< select.options.length; i++){
				if(select.options[i].value == trim(scorePointsElement.firstChild.nodeValue)){
					select.options[i].selected = 'true';
					}
				
				}
			}
		}
	
		function addOption(selectbox,text,value )
		{
			var optn = document.createElement("OPTION");
			optn.text = text;
			optn.value = value;
			selectbox.options.add(optn);
		}
		
		
			 
	function trim(str, chars) {
		return ltrim(rtrim(str, chars), chars);
	}
 
	function ltrim(str, chars) {
		chars = chars || "\\s";
		return str.replace(new RegExp("^[" + chars + "]+", "g"), "");
	}
 
	function rtrim(str, chars) {
		chars = chars || "\\s";
		return str.replace(new RegExp("[" + chars + "]+$", "g"), "");
	}
	
	function hideMessage(){
		document.getElementById("messageSpan").innerHTML = "";
	}		
	function enable(){
	document.getElementById("rescoreStudent").setAttribute("disabled",false);
	}
				
	function isWindows() {
		return navigator.userAgent.indexOf('Win') > -1;
	}	
	
	