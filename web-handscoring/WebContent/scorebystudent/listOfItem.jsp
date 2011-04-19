<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>

<netui-data:declareBundle bundlePath="oasResources" name="oas" />
<netui-data:declareBundle bundlePath="webResources" name="web" />
<netui-data:declareBundle bundlePath="widgetResources" name="widgets" />
<netui-data:declareBundle bundlePath="helpResources" name="help" />

<script type="text/javascript" src="/HandScoringWeb/resources/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="/HandScoringWeb/resources/js/jquery-ui-1.8.10.custom.min.js"></script>
<script src="/HandScoringWeb/resources/fxResources/dtfx.js"></script>
<script type="text/javascript" src="/HandScoringWeb/resources/js/scoring.js"></script>
<link type="text/css" href="/HandScoringWeb/resources/css/jquery-ui-1.8.10.custom.css" rel="stylesheet" />
<link type="text/css" href="/HandScoringWeb/resources/css/style.css" rel="stylesheet" />

<script>
var playCompleted = false;
function stopAudio(){
	try {
        var myApp = document.getElementById("myApp");
       	myApp.script.stopAudio("");
	}catch (e) {
        
    }
       
 }
 

	 //This Function  will be called by javafx at runtime
    function show_alert() {
    	var audioResponseString = document.getElementById("audioResponseString").value;
    	var regExp = /\s+/g;
		var stringFX = audioResponseString.replace(regExp,'');
    	//alert("audio" + audioResponseString);
    	return stringFX;
	}
	
		
	
	function getAudioPlayer(parentObj) {
	  var fxstring = javafxString(
	    {
	            archive: "/HandScoringWeb/resources/fxResources/JavaFXApplication1.jar",
	            visible: false,	            
	            width: 250,
	            height: 80,
	            code: "javafxapplication1.Main",
	            name: "myApplet",
	            id: "myApp"
	    }
	  );
	  document.getElementById(parentObj).innerHTML = fxstring;
	  playCompleted = false;
	}


	function showScoreSelect(disableStatus){
		if(document.getElementById("itemType").value == "AI"){
			if(disableStatus == "true"){
				document.getElementById("pointsDropDown").setAttribute("disabled",true);
				document.getElementById("Question").setAttribute("disabled",true);
			}else{
				document.getElementById("pointsDropDown").removeAttribute("disabled");
				document.getElementById("Question").removeAttribute("disabled");
			}
		}
	}
		
		
	
	
	function getPlayCompleted(playStatus){
		//alert("getPlayCompleted"+playStatus);
		/*try {
			var myApp = document.getElementById("myApp"); 
			if(playStatus == true && myApp.script.playCalledflag){			
        			myApp.script.stop.disable = true;
       				myApp.script.pause.disable = true;
       			}			
			}
		}catch (e) {}*/
		playCompleted = playStatus;
		stopAudio();//to retain the default state of player
		//alert("inside getPlayCompleted");
	}
	
	function checkPlay(){
		if(document.getElementById("itemType").value == "AI"){			
				if(playCompleted == true){			
					formSave();
				}else{
					var confSave = confirm("Response is not completed yet.Are you sure you want to save?");
					if(confSave == true){
						formSave();
					}
				}
		}else{
			formSave();
		}
	}
	
	
</script>
<%
	//Start Change For CR - GA2011CR001
	//Boolean isStudentIdConfigurable = (Boolean)request.getAttribute("isStudentIdConfigurable"); 
	//String []studentIdArrValue = (String[])request.getAttribute("studentIdArrValue");
%>
<netui-template:template templatePage="/resources/jsp/scoring_template.jsp">
	<netui-template:setAttribute name="title" value="${bundle.web['individualStudentScoring.window.title']}" />
	<netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.findStudent']}" />
	<netui-template:section name="bodySection">

		<!-- ********************************************************************************************************************* -->
		<!-- Start Page Content -->
		<!-- ********************************************************************************************************************* -->

		<h1><netui:content value="${bundle.web['individualStudentScoring.title']}" /></h1>
		<p><netui:content value="${bundle.web['individualStudentScoring.title.message']}" /></p>

		<netui:form action="beginDisplayStudItemList">
			<netui:hidden tagId="actionElement" dataSource="actionForm.actionElement" />
			<netui:hidden tagId="currentAction" dataSource="actionForm.currentAction" />
			<netui:hidden tagId="accessCode" dataSource="actionForm.accessCode" />
			<netui:hidden tagId="userName" dataSource="actionForm.userName" />
			<netui:hidden tagId="testAdminId" dataSource="actionForm.testAdminId" />
			<netui:hidden tagId="rosterId" dataSource="actionForm.testRosterId" />
			<netui:hidden tagId="itemSetIdTC" dataSource="actionForm.itemSetIdTC" />
			<netui:hidden tagId="testAccessCode" dataSource="actionForm.testAccessCode" />
			<netui:hidden tagId="testSessionName" dataSource="actionForm.testSessionName" />
			<netui:hidden tagId="loginName" dataSource="actionForm.loginName" />

			<netui:hidden dataSource="actionForm.itemMaxPage" />
			<input type="hidden" id="itemSetId" />
			<input type="hidden" id="itemId" />
			<input type="hidden" id="itemNumber" />
			<input type="hidden" id="messageStatus" />
			<input type="hidden" id="itemType" />
			<input type="hidden" id="audioResponseString" />
			<input type="hidden" id="rowNo" />

			<h2><netui:content value="${bundle.web['individualStudentScoring.StudentDetails.title']}" /></h2>
			<table class="transparent" width="100%">

				<tr class="transparent">
					<td class="transparent"><netui:content
						value="${bundle.web['individualStudentScoring.StudentDetails.AccessCode']}" /></td>
					<td class="transparent">
					<div class="formValueLarge"><netui:span value="${actionForm.testAccessCode}" styleClass="formValueLarge" /></div>
					</td>
				</tr>


				<tr class="transparent">
					<td class="transparent"><netui:content
						value="${bundle.web['individualStudentScoring.StudentDetails.StudentLoginName']}" /></td>
					<td class="transparent">
					<div class="formValueLarge"><netui:span value="${actionForm.loginName}" styleClass="formValueLarge" /></div>
					</td>
				</tr>
				<tr class="transparent">
					<td class="transparent"><netui:content
						value="${bundle.web['individualStudentScoring.StudentDetails.TestSessionName']}" /></td>
					<td class="transparent">
					<div class="formValueLarge"><netui:span value="${actionForm.testSessionName}" styleClass="formValueLarge" /></div>

					</td>
				</tr>


			</table>
			<br /><br/>
			<table class="sortable" width="100%">
			<% int rowId = 0; %>
				<netui-data:repeater dataSource="requestScope.itemList">
					<netui-data:repeaterHeader>

						<tr class="sortable">
							<ctb:tableSortColumnGroup columnDataSource="actionForm.itemSortColumn"
								orderByDataSource="actionForm.itemSortOrderBy" anchorName="studentSearchResult">
								<th class="sortable alignLeft" width="5%" nowrap><ctb:tableSortColumn value="ItemSetOrder">Item No</ctb:tableSortColumn></th>
								<th class="sortable alignLeft" width="5%" nowrap>&nbsp;&nbsp;View Rubric</th>
								<th class="sortable alignLeft" width="10%" nowrap><ctb:tableSortColumn value="ItemSetName">Subtest Name</ctb:tableSortColumn></th>
								<th class="sortable alignLeft" width="5%" nowrap>&nbsp;&nbsp;ViewQuestion</th>
								<th class="sortable alignLeft" width="5%" nowrap>&nbsp;&nbsp;Response</th>
								<th class="sortable alignLeft" width="20%" nowrap><ctb:tableSortColumn value="Answered">&nbsp;&nbsp;Status&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</ctb:tableSortColumn></th>
								<th class="sortable alignLeft" width="10%" nowrap><ctb:tableSortColumn value="ScoreStatus">Manual Scoring Status</ctb:tableSortColumn></th>
								<th class="sortable alignLeft" width="5%" nowrap>&nbsp;&nbsp;Maximum Score</th>
								<th class="sortable alignLeft" width="5%" nowrap>&nbsp;&nbsp;Score Obtained</th>
							</ctb:tableSortColumnGroup>
						</tr>

					</netui-data:repeaterHeader>
					<netui-data:repeaterItem>
                       <%  rowId = rowId+1; %>
						<tr class="sortable">

							<td class="sortable"><netui:span value="${container.item.itemSetOrder}" /></td>
							<td class="sortable">
							<netui-data:getData resultId="itemId" value="${container.item.itemId}" /> <%
 	String itemId = (String) pageContext
 								.getAttribute("itemId");
 %>
							<netui-data:getData resultId="itemNumber" value="${container.item.itemSetOrder}" /> <%
 	Integer itemNumber = (Integer) pageContext
 								.getAttribute("itemNumber");
 %>
							<a href="javascript:viewRubric('<%=itemId%>', <%=itemNumber%>)">View</a> 
							<td class="sortable"><netui:span value="${container.item.itemSetName}" /></td>
							<td class="sortable">
							<input name="ViewQuestion" type="button" value="View Question"
									onclick="openViewQuestionWindow('<%=itemId%>'); return true;" /></td>
							
							<td class="sortable"><netui-data:getData resultId="itemtype" value="${container.item.itemType}" /> <%
 	String itemtype = (String) pageContext
 								.getAttribute("itemtype");
 %>
							<netui-data:getData resultId="itemSetId" value="${container.item.itemSetId}" /> <%
 	Integer itemSetId = (Integer) pageContext
 								.getAttribute("itemSetId");
 %>
							
							<netui-data:getData resultId="answered" value="${container.item.answered}" /> <c:if test="${itemtype =='AI'}">
								<c:if test="${answered == 'NA'}">
									<input type="button" value="Audio Response" disabled="true" />
								</c:if>
								<c:if test="${answered == 'A'}">
									<input type="button" value="Audio Response"
										onclick="formSubmit('<%=itemId%>','<%=itemtype%>',<%=itemSetId%>,<%=itemNumber%>, <%=rowId%>)" />
								</c:if>
							</c:if> 
							<c:if test="${itemtype =='CR'}">
								<c:if test="${answered == 'NA'}">
									<input type="button" value="&nbsp;Text Response&nbsp;" disabled="true" />
								</c:if>
								<c:if test="${answered == 'A'}">
									<input type="button" value="&nbsp;Text Response&nbsp;"
										onclick="formSubmit('<%=itemId%>','<%=itemtype%>',<%=itemSetId%>,<%=itemNumber%>, <%=rowId%>)" />
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
		
								
						
							<%String scoreStatus= "scoreStatus"+rowId;%>
							<td class="sortable"><span id='<%=scoreStatus%>'
								 >${container.item.scoreStatus} </span></td>
							<%String maxPoints= "maxPoints"+rowId;%>
							<td class="sortable"><span id='<%=maxPoints%>'
								>${container.item.maxPoints}</span></td>
								
							<td class="sortable">
							<netui-data:getData resultId="isCompleted" value="${container.item.scoreStatus}" />
							<%String scorePoints= "scorePoints"+rowId;%>
							<c:if test="${isCompleted == 'Complete'}">
								<span id="<%=scorePoints%>">
								${container.item.scorePoint}</span>
							</c:if>
							<c:if test="${isCompleted =='Incomplete'}">
								<span id="<%=scorePoints%>"> -</span>
							</c:if>
							</td>
						</tr>

					</netui-data:repeaterItem>
					<netui-data:repeaterFooter>

						<tr class="sortable">
							<td class="sortableControls" colspan="9"><ctb:tablePager dataSource="actionForm.itemPageRequested"
								summary="request.itemPagerSummary" objectLabel="${bundle.oas['object.items']}" foundLabel="Found"
								id="itemSearchResult" anchorName="itemSearchResult" /></td>
						</tr>

					</netui-data:repeaterFooter>
				</netui-data:repeater>
			</table>

			<c:if test="${itemSearchResultEmpty != null}">
				<ctb:message title="Search Result" style="informationMessage">
					<netui:content value="${requestScope.itemSearchResultEmpty}" />
				</ctb:message>
			</c:if>
			<br>
			<p> <netui:button type="submit" value="${bundle.web['common.button.back']}" action="returnToFindStudent" /> </p>

			<!-- buttons -->
			<!-- <p><netui:button type="submit" value="Back" action="" /></p> -->
			<div id="dialogID"
				style="display: none; width: 1000px; height: 1000px; overflow:visible; background-color: #FFFFCC; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
			<table border="0" width="100%">

				<tr width="100%">
					<td class="transparent alignRight" style="width: 10%;"><span>&nbsp;<b> Answer :</b></span></td>
					<td class="transparent" style="width: 90%;padding-left:5px;" id="dialogIdDiv">
					
					<textarea id="crText" width="70%" cols="85" rows="8" readonly="readonly"></textarea>
					<div id="audioPlayer">
						<script>
							//getAudioPlayer('audioPlayer');//javafx({archive: "JavaFXApplication1.jar",width: 250,height: 80,code: "javafxapplication1.Main",name: "fxApp",id: "fxApp"});
						</script>
					</div>
					</td>
				</tr>
				<tr width="100%">
					<td class="transparent alignRight" style="width: 10%;"><span>&nbsp;<b> Score :</b></span></td>
					<td class="transparent" style="width: 90%;">

					<div><netui:select tagId="pointsDropDown" datasource="actionForm.scorePoints" onChange="hideMessage();" /></div>
					</td>
				</tr>
				<tr width="100%">
					<td colspan=2>
					<div id="message" style="display: none;"><span id="messageSpan"></span></div>
					</td>
				</tr>
				<tr>
					<td colspan="2">
					<table class="transparent">
						<tr class="transparent" width="100%">
							<td class="transparent"><netui:button type="button" tagId="cancel" value="Cancel" onClick="closePopUp();" /></td>
							<td class="transparent"><input type="button" id="Question" value="Save" onclick="checkPlay(); " /></td>
						</tr>
					</table>
					</td>
				</tr>
			</table>

			</div>
			
			<!-- RUBRIC POPUP -->
			
			<div class="scroll" id="rubricDialogID"
				style="display: none; background-color: #FFFFCC; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
				<span>
					<center>
						<font size='4'>Scoring Rubric
						</font>
					</center>
				</span>
				<div style="width: 100%; height: 90%; background-color: #FFFFCC; font-family: Arial,Verdana,Sans Serif; font-size: 8px; font-weight: normal;">
					<div id="rubricNoDataId" style="display: none; border: 10px; background-color: #FFFFFF;">
						<table border="1" style="width: 100%;">
							<tr>
								<td align="center" >No rubric data is present for the Item
								</td>
							</tr>
						</table>
					</div>
					<div id="rubricTableId" style="display: none;">
						<table border="1" bgcolor="#FFFFFF" id="rubricTable" style="width: 100%;">
							<tr bgcolor="#EAEAEA">
								<td style="width: 20%;" align="center">
									<font size='4'>Score
									</font>
								</td>
								<td align="center">
									<font size='4'>Description
									</font>
								</td>
							</tr>
						</table>
					</div>
					<span>
						<center>
							<font size='4'>Exemplars
							</font>
						</center>
					</span>
					<div id="exemplarNoDataId" style="display: none; border: 10px; background-color: #FFFFFF;">
						<table border="1" style="width: 100%;">
							<tr>
								<td align="center" >No exemplar data is present for the Item
								</td>
							</tr>
						</table>
					</div>
					<div id="rubricExemplarId" style="display: none;">
						<table border="1" bgcolor="#FFFFFF" id="exemplarsTable" style="width: 100%;">
							<tr bgcolor="#EAEAEA">
								<td style="width: 20%;" align="center">
									<font size='4'>Score
									</font>
								</td>
								<td style="width: 30%;" align="center">
									<font size='4'>Sample Response
									</font>
								</td>
								<td align="center">
									<font size='4'>Explanation
									</font>
								</td>
							</tr>
						</table>
					</div>
				</div>
			</div>						
		</netui:form>
		<!-- ********************************************************************************************************************* -->
		<!-- End Page Content -->
		<!-- ********************************************************************************************************************* -->

	</netui-template:section>
</netui-template:template>
