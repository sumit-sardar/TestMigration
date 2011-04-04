<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>

<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>
<netui-data:declareBundle bundlePath="helpResources" name="help"/>

<script type="text/javascript" src="/HandScoringWeb/resources/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="/HandScoringWeb/resources/js/jquery-ui-1.8.10.custom.min.js"></script>
<script src="dtfx.js"></script>
<link type="text/css" href="/HandScoringWeb/resources/css/jquery-ui-1.8.10.custom.css" rel="stylesheet" />	

<script>

function formSubmit(itemId, itemType, itemSetId, itemNumber) {

var param = "&itemId="+itemId+"&itemType="+itemType+"&itemSetId="+itemSetId+"&rosterId="+$("#rosterId").val();

document.getElementById("itemId").value = itemId;
document.getElementById("itemSetId").value = itemSetId;
document.getElementById("itemNumber").value = itemNumber;
//updateMaxPoints();

	//alert($("#formid").serialize());
	$.ajax(
		{
				async:		true,
				beforeSend:	function(){
								//show progress bar
								//$("#showSuccessmessage").hide();
								//$("#invalidRubricCharacterMessage").hide();
								//blockUI();
								//alert('before send....');
							},
				url:		'beginCRResponseDisplay.do',
				type:		'POST',
				data:		param,
				dataType:	'json',
				success:	function(data, textStatus, XMLHttpRequest){	
							
								var isAudioItem = data.answer.isAudioItem;
								if(isAudioItem){
								
								document.getElementById("itemType").value = "AI";
								var audioResponseString = data.answer.audioItemContent;
								audioResponseString = audioResponseString.substr(13);
								audioResponseString = audioResponseString.split("%3C%2F");
								document.getElementById("audioResponseString").value = audioResponseString[0];
								openPopup();
								$("#crText").hide();
								document.getElementById("crText").style.display='none';
								document.getElementById("audioPlayer").style.display='inline';							
								$("#audioPlayer").show();								
								
								}
								else{
								document.getElementById("itemType").value = "CR";								
								document.getElementById("audioPlayer").style.display='none';
								document.getElementById("crText").style.display='inline';
								var crTextResponse = data.answer.cRItemContent;
								    openPopup();
								    $("#audioPlayer").hide();
									$("#crText").show();
									$("#crText").val(crTextResponse);
				
								}									
								//alert(data.var1);
								//$("#fName").val(data.var1);
							},
				error  :    function(XMLHttpRequest, textStatus, errorThrown){
								//alert(XMLHttpRequest.responseText+" http code"+XMLHttpRequest.statusCode);
								//alert('XMLHttpRequest:'+XMLHttpRequest+'===>> textStatus:'+textStatus+'==>>errorThrown:'+errorThrown);
							},
				complete :  function(){
								//alert('after complete....');
								//unblockUI();
							}
		}
	);
	}
	
function formSave() {
var itemId =  document.getElementById("itemId").value ;
var itemSetId = document.getElementById("itemSetId").value  ;
var itemNumber = document.getElementById("itemNumber").value;
var param = "&itemId="+itemId+"&itemSetId="+itemSetId+"&rosterId="+$("#rosterId").val() + "&score="+$("#pointsDropDown option:selected").val();    

alert(param);
	$.ajax(
		{
				async:		true,
				beforeSend:	function(){
								//show progress bar
								//$("#showSuccessmessage").hide();
								//$("#invalidRubricCharacterMessage").hide();
							//	blockUI();
								//alert('before send....');
							},
				url:		'saveDetails.do',
				type:		'POST',
				data:		param,
				dataType:	'json',
				success:	function(data, textStatus, XMLHttpRequest){	
									
									var isSuccess = data.boolean;	
									var spanElement = document.getElementById("messageSpan");
									var scorePointsElement = document.getElementById("scorePoints"+itemNumber);
									alert(isSuccess);
									if(isSuccess){
								
									scorePointsElement.firstChild.nodeValue = $("#pointsDropDown option:selected").val();
									document.getElementById("messageStatus").value = isSuccess;
									document.getElementById("message").style.display = 'inline';									
									spanElement.innerHtml = "Item Scored Successfully";
									
									}
									else{				
									spanElement.innerHtml = "Item Not Scored";
									
									}
								
							},
				error  :    function(XMLHttpRequest, textStatus, errorThrown){
								//alert(XMLHttpRequest.responseText+" http code"+XMLHttpRequest.statusCode);
								//alert('XMLHttpRequest:'+XMLHttpRequest+'===>> textStatus:'+textStatus+'==>>errorThrown:'+errorThrown);
							},
				complete :  function(){
								//alert('after complete....');
								//unblockUI();
							}
		}
	);
	}
	
	function createDiv(){
	var fxVal = 'javafx({archive: "JavaFXApplication1.jar",width: 250,height: 80,code: "javafxapplication1.Main",name: "fxApp",id: "fxApp"});';
	var mainDiv = document.getElementById('dialogIdDiv');
	var newDiv = document.createElement('div');
	newDiv.id = "audioPlayer";
	newDiv.innerHTML = '<script>'+ fxVal +'</script'+'>';
	mainDiv.appendChild(newDiv);					
																
	//var newSc = document.createElement('script');
	//newSc.text = fxVal;
	//var tt = document.createTextNode(scr);
	//newSc.appendChild(tt);								
	//var ss = document.getElementById('audioPlayer');
	//ss.appendChild(newSc);
	}
	
	function openPopup() {
				//alert("....................."+document.getElementById("dialogID"));
				//alert($("#dialogID"));
				$("#dialogID").dialog();
				
			}
			function blockUI()
			{	
				$("body").append('<div id="blockDiv" style="background:url(/HandScoringWeb/resources/images/transparent.gif);position:fixed;top:0;left:0;width:100%;height:100%;z-index:9999"><img src="/HandScoringWeb/resources/images/loading.gif" style="left:50%;top:40%;position:absolute;"/></div>');
				$("#blockDiv").css("cursor","wait");
				openPopup();
			}
			
			function unblockUI()
			{
				$("#blockDiv").css("cursor","normal");
				$("#blockDiv").remove();
			}
			function closePopUp(){
			
			$("#dialogID").dialog("close");
			}
<!--This Function  will be called by javafx at runtime-->
    function show_alert() {
    	var audioResponseString = document.getElementById("audioResponseString").value;
    	//alert("audio" + audioResponseString);
    	return audioResponseString;
	}
	
	function getPlayer(){
	//alert(document.getElementById("itemType").value);
	//if(document.getElementById("itemType").value == "AI"){
	javafx(
        {
              archive: "JavaFXApplication1.jar",
              width: 250,
              height: 80,
              code: "javafxapplication1.Main",
              name: "fxApp",
              id: "fxApp"
        }
    );
   
	//}
	}	
function addOption(selectbox,text,value )
{
var optn = document.createElement("OPTION");
optn.text = text;
optn.value = value;
selectbox.options.add(optn);
}

</script>
<%

//Start Change For CR - GA2011CR001
//Boolean isStudentIdConfigurable = (Boolean)request.getAttribute("isStudentIdConfigurable"); 
//String []studentIdArrValue = (String[])request.getAttribute("studentIdArrValue");


%>
<netui-template:template templatePage="/resources/jsp/template.jsp">
    <netui-template:setAttribute name="title" value="${bundle.web['individualStudentScoring.window.title']}"/>
    <netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.findStudent']}"/>
<netui-template:section name="bodySection">

<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->
             
<h1><netui:content value="${bundle.web['individualStudentScoring.title']}"/></h1>
<p><netui:content value="${bundle.web['individualStudentScoring.title.message']}"/></p>

<netui:form action="beginDisplayStudItemList">
<netui:hidden tagId="actionElement" dataSource="actionForm.actionElement"/> 
<netui:hidden tagId="currentAction" dataSource="actionForm.currentAction"/>
<netui:hidden tagId="accessCode" dataSource="actionForm.accessCode"/>
<netui:hidden tagId="userName" dataSource="actionForm.userName"/>
<netui:hidden tagId="testAdminId" dataSource="actionForm.testAdminId"/>
<netui:hidden tagId="rosterId" dataSource="actionForm.rosterId"/>
<netui:hidden tagId="itemSetIdTC" dataSource="actionForm.itemSetIdTC"/>
<netui:hidden  dataSource="actionForm.itemMaxPage"/>
<input type="hidden" id="itemId"/>
<input type="hidden" id="itemNumber"/>
<input type="hidden" id="messageStatus"/> 
<input type="hidden" id="audioResponseString"/>
<input type="hidden" id="itemType"/>
<h2><netui:content value="${bundle.web['individualStudentScoring.StudentDetails.title']}"/></h2>
<table class="transparent" width="100%">

<tr class="transparent">
    <td class="transparent">
        <netui:content value="${bundle.web['individualStudentScoring.StudentDetails.AccessCode']}"/>
    </td>
    <td class="transparent">    
        <div class="formValueLarge"><netui:span value="${requestScope.accessCode}" styleClass="formValueLarge"/></div>
    </td>
</tr>


<tr class="transparent">
    <td class="transparent">
        <netui:content value="${bundle.web['individualStudentScoring.StudentDetails.StudentLoginName']}"/>
    </td>
    <td class="transparent">
        <div class="formValueLarge"><netui:span value="${requestScope.userName}" styleClass="formValueLarge"/></div>
    </td>
</tr>
<tr class="transparent">
    <td class="transparent">
        <netui:content value="${bundle.web['individualStudentScoring.StudentDetails.TestSessionName']}"/>
    </td>
    <td class="transparent">
        <div class="formValueLarge"><netui:span value="${requestScope.testSessionName}" styleClass="formValueLarge"/></div>

    </td>
</tr> 
 
  
</table><br/>
 <table class="sortable" width="100%">       
<netui-data:repeater dataSource="requestScope.itemList">
    <netui-data:repeaterHeader>
    
    <tr class="sortable">
        <ctb:tableSortColumnGroup columnDataSource="actionForm.itemSortColumn" orderByDataSource="actionForm.itemSortOrderBy" anchorName="studentSearchResult">
            <th class="sortable alignLeft" width="5%" nowrap><ctb:tableSortColumn value="ItemSetOrder">Item No</ctb:tableSortColumn></th>
            <th class="sortable alignLeft" width="5%" nowrap>&nbsp;&nbsp;View Rubric </th>
            <th class="sortable alignLeft" width="20%" nowrap><ctb:tableSortColumn value="ItemSetName">Subtest Name</ctb:tableSortColumn></th>
            <th class="sortable alignLeft" width="5%" nowrap>&nbsp;&nbsp;ViewQuestion</th> 
            <th class="sortable alignLeft" width="5%" nowrap>&nbsp;&nbsp;Response</th>
            <th class="sortable alignLeft" width="20%" nowrap><ctb:tableSortColumn value="Answered">Status</ctb:tableSortColumn></th>
            <th class="sortable alignLeft" width="10%" nowrap><ctb:tableSortColumn value="ScoreStatus">Manual Scoring Status</ctb:tableSortColumn></th>
            <th class="sortable alignLeft" width="5%" nowrap>&nbsp;&nbsp;Maximum Score</th>
            <th class="sortable alignLeft" width="5%" nowrap>&nbsp;&nbsp;Score Obtained</th>
        </ctb:tableSortColumnGroup>
    </tr>
    
    </netui-data:repeaterHeader>
    <netui-data:repeaterItem>
    
    <tr class="sortable">
         
        <td class="sortable">
            <netui:span value="${container.item.itemSetOrder}"/>
        </td>
        <td class="sortable">
                    <% String href = "beginDisplayStudItemList.do"; %> 
                    <netui:anchor href="<%= href %>">
                        <netui:span value="View" defaultValue="&nbsp;"/>
                    </netui:anchor>
        </td>  
        <td class="sortable">
            <netui:span value="${container.item.itemSetName}"/>
        </td>
        <td class="sortable">
           	<netui-data:getData resultId="itemNumber" value="${container.item.itemSetOrder}"/>  
           		<%Integer itemNumber = (Integer)pageContext.getAttribute("itemNumber"); %>
             <input name="ViewQuestion" type="button" value="View Question" onclick="openViewQuestionWindow(<%=itemNumber%>); return true;"/>
                      
        </td>
         <td class="sortable">
          <netui-data:getData resultId="itemtype" value="${container.item.itemType}"/> 
          	<% String itemtype = (String)pageContext.getAttribute("itemtype"); %>	
         <netui-data:getData resultId="itemSetId" value="${container.item.itemSetId}"/> 
          	<% Integer itemSetId = (Integer)pageContext.getAttribute("itemSetId"); %>	
          	   <netui-data:getData resultId="itemId" value="${container.item.itemId}"/> 
          	<% String itemId = (String)pageContext.getAttribute("itemId");    	%>	
     	 <netui-data:getData resultId="answered" value="${container.item.answered}"/> 
         	 <c:if test="${itemtype =='AI'}">  
         	  <c:if test="${answered == 'NA'}">
           		   <input type="button" value="Audio Response" disabled="true"/>  
           	  </c:if>
           	  <c:if test="${answered == 'A'}">
           		  <input type="button" value="Audio Response" onclick="formSubmit('<%=itemId%>','<%=itemtype%>',<%=itemSetId%>,<%=itemNumber%>)" />  
           	  </c:if>
            </c:if>
             <c:if test="${itemtype =='CR'}">  
             	<c:if test="${answered == 'NA'}">
           		 <input type="button" value="Text Response"  disabled="true"/>   
           	    </c:if>
           	    <c:if test="${answered == 'A'}">
           		 <input type="button" value="Text Response" onclick="formSubmit('<%=itemId%>','<%=itemtype%>',<%=itemSetId%>,<%=itemNumber%>)"/>   
           	    </c:if>
           
            </c:if>     
        </td>
        <td class="sortable">
        <netui-data:getData resultId="isanswered" value="${container.item.answered}"/> 
        	<c:if test="${isanswered =='NA'}">  
            <netui:span value="Not Answered"/>
            </c:if>
             <c:if test="${isanswered =='A'}">  
           <netui:span value="Answered"/>
            </c:if>     
        </td>
        <td class="sortable">
            <netui:span value="${container.item.scoreStatus}"/>
        </td>
        <td class="sortable">
            <netui:span tagId="maxPoints${container.item.itemSetOrder}" value="${container.item.maxPoints}"/>
        </td>
        <td class="sortable">
            <netui:span tagId="scorePoints${container.item.itemSetOrder}" value="${container.item.scorePoint}"/>
        </td>
    </tr>
    
    </netui-data:repeaterItem>
    <netui-data:repeaterFooter>
    
        <tr class="sortable">
            <td class="sortableControls" colspan="9">
                <ctb:tablePager dataSource="actionForm.itemPageRequested" summary="request.itemPagerSummary" objectLabel="${bundle.oas['object.items']}" foundLabel="Found" id="itemSearchResult" anchorName="itemSearchResult"/>
            </td>
        </tr>         
            
    </netui-data:repeaterFooter>
</netui-data:repeater>   
</table>

<c:if test="${itemSearchResultEmpty != null}">     
    <ctb:message title="Search Result" style="informationMessage" >
          <netui:content value="${requestScope.itemSearchResultEmpty}"/>
    </ctb:message>
</c:if>

<!-- buttons -->
<p>
  
    <netui:button type="submit" value="Back" action="returnToFindStudent"/>
</p>
		<div id="dialogID" style="display: none; width:700px; height:700px; background-color: #FFFFCC; font-family: Arial, Verdana, Sans Serif; font-size: 12px;  font-style: normal;  font-weight: normal;" >
<table border="1" width="100%">

	<tr width="100%">
		<td class="transparent alignRight" style="width:10%;"><span>&nbsp;<b> Answer :</b></span></td>
		<td class="transparent" style="width:90%;" id="dialogIdDiv">
		<textarea id="crText" width="70%" cols="100" rows="4"></textarea> 
		<div id="audioPlayer" width="200" height="200">
			<script>
				getPlayer();//javafx({archive: "JavaFXApplication1.jar",width: 250,height: 80,code: "javafxapplication1.Main",name: "fxApp",id: "fxApp"});
			</script>
		
		</div>
		
		</td>
	</tr>
	 <tr width="100%">
		<td class="transparent alignRight" style="width:10%;"><span>&nbsp;<b> Score :</b></span></td>
		<td class="transparent" style="width:90%;">

		<div ><select><option selected="selected">&nbsp;</option>
		<option>1</option>
		<option>2</option>
		<option>3</option>
		<option>4</option>
		<option>5</option>
		<option>6</option>
		</select></div>
		</td>
	</tr> 

	  <table class="transparent">
		<tr class="transparent" width="100%">
			<td class="transparent"><netui:button type="submit" tagId="Back" value="Cancel" onClick="document.getElementById('dialogID').style.display='none';"/></td>
			<td class="transparent"><input type="button" name="Question" value="Save"></td>
		</tr>
	</table> 
	
	

</table>
</td>
</tr>
</table>

</div>
</netui:form>
<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
        
    </netui-template:section>
</netui-template:template>
