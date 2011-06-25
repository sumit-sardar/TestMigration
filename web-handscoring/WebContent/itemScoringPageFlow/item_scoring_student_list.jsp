<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.ctb.bean.testAdmin.ScorableItem"%>
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
<script type="text/javascript" src="<%=request.getContextPath()%>/HandScoringWeb/resources/js/scoring.js"></script>
<link type="text/css" href="/HandScoringWeb/resources/css/jquery-ui-1.8.10.custom.css" rel="stylesheet" />
<link type="text/css" href="/HandScoringWeb/resources/css/style.css" rel="stylesheet" />
<script>
var playCompleted = false;
function stopAudio(){
	try {
       var subIframe = $('#iframeAudio');

		if(isWindows()) {
	        var myApp = document.getElementById("myApp");
	       	myApp.script.stopAudio("");
		}
		else {
			subIframe[0].contentWindow.stopAudio();
			//alert(subIframe);
		}
       	/*if(subIframe != '' || subIframe != null) {
		$(subIframe).attr('src', "#");
		}*/	
											
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
		//alert("getPlayCompleted in parent:"+playStatus);
		/*try {
			var myApp = document.getElementById("myApp"); 
			if(playStatus == true && myApp.script.playCalledflag){			
        			myApp.script.stop.disable = true;
       				myApp.script.pause.disable = true;
       			}			
			}
		}catch (e) {}*/
		playCompleted = playStatus;
		if(isWindows()) {
			stopAudio();//to retain the default state of player
		}
		//alert("inside getPlayCompleted");
	}
	
	function checkPlay(){
		//alert("playCompleted in checkplay : "+playCompleted);
		if(document.getElementById("itemType").value == "AI"){			
				if(playCompleted == true){			
					formSave();
				}else{
					var confSave = confirm("Are you sure you want to score before listening to the entire response?");
					if(confSave == true){
						formSave();
					}
				}
		}else{
			formSave();
		}
	}
	
	
	function passAudioString(){
		var audioResponseString = document.getElementById("audioResponseString").value;
		return audioResponseString;
	}
	
	
</script>
<netui-template:template templatePage="/resources/jsp/scoring_template.jsp">

<!-- 
template_find_student.jsp
-->

	<netui-template:setAttribute name="title" value="${bundle.web['StudentList.window.title']}" />
	<netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.findStudent']}" />
	<netui-template:section name="bodySection">

<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->
<h1>
<netui:content value="${pageFlow.pageTitle}" />
</h1>


<!-- title -->

<p>
<netui:content value="${bundle.web['StudentList.window.subHeading']}" />
</p>
<p align="right">

 
<a href="javascript:void(0)" onclick="openViewQuestionWindow('${requestScope.itemId}','${requestScope.itemNo}'); return true;">View Question</a>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

<netui:anchor formSubmit="true" href="javascript:viewRubric('${requestScope.itemId}',${requestScope.itemNo});"><netui:content value="View Rubric"/></netui:anchor>
 										
</p>
<%
   	Boolean isStudentIdConfigurable = (Boolean)request.getAttribute("isStudentIdConfigurable"); // Change For CR - GA2011CR001
	
%> 
<netui:form action="goto_student_list">
<netui:hidden tagId="actionElement" dataSource="actionForm.actionElement"/> 
<netui:hidden tagId="currentAction" dataSource="actionForm.currentAction"/>
<netui:hidden tagId="testAdminId" dataSource="actionForm.testAdminId"/>
<netui:hidden tagId="itemSetId" dataSource="actionForm.itemSetId"/>
<netui:hidden tagId="testSessionName" dataSource="actionForm.testSessionName"/>
<netui:hidden tagId="testAccessCode" dataSource="actionForm.testAccessCode"/>
<netui:hidden tagId="itemSetOrder" dataSource="actionForm.selectedItemNo"/>
<netui:hidden tagId="itemSetName" dataSource="actionForm.selectedItemSetName"/>
<netui:hidden tagId="maxPoints" dataSource="actionForm.selectedMaxPoints"/>
<netui:hidden tagId="itemType" dataSource="actionForm.selectedItemType"/>
<netui:hidden tagId="itemId" dataSource="actionForm.itemId"/>
<netui:hidden dataSource="actionForm.studentMaxPage" />
<netui:hidden tagId="itemNumber" dataSource="actionForm.selectedItemNo"/>
<input type="hidden" id="messageStatus" />
<input type="hidden" id="audioResponseString" />
<input type="hidden" id="rowNo" />
<input type="hidden" id="loginId" />
<input type="hidden" id="rosterId" />
<input type="hidden" id="itemSetIdTC"/>


<!-- message -->
	
    
			<table width="100%" cellpadding="0" cellspacing="0" class="transparent">
                    <tr class="transparent">
                   
					<td  nowrap valign="top" class="transparent" colspan="2"><h2><netui:content value="${bundle.web['individualStudentScoring.StudentDetails.title']}" /></h2></td>
				</tr>
				<tr class="transparent">
                <td class="transparent" width="25%">
                <netui:content value="${bundle.web['common.column.itemNo']}"/>
                </td>
                <td class="transparent">    
                <div class="formValueLarge"><netui:span value="${actionForm.selectedItemNo}" styleClass="formValueLarge"/></div>
               </td>
                </tr>
				<tr class="transparent">
                <td class="transparent">
               <netui:content value="${bundle.web['common.column.maximumMarks']}"/>
                </td>
                <td class="transparent">    
                <div class="formValueLarge"><netui:span tagId="maxPoints" value="${actionForm.selectedMaxPoints}" styleClass="formValueLarge"/></div>
               </td>
                </tr>
				<tr class="transparent">
                <td class="transparent">
               <netui:content value="${bundle.web['common.column.subtestName']}"/>
                </td>
                <td class="transparent">    
                <div class="formValueLarge"><netui:span value="${actionForm.selectedItemSetName}" styleClass="formValueLarge"/></div>
               </td>
                </tr>
				<tr class="transparent">
                <td class="transparent">
                <netui:content value="${bundle.web['common.column.accessCode']}"/>
                </td>
                <td class="transparent">    
                <div class="formValueLarge"><netui:span value="${actionForm.testAccessCode}" styleClass="formValueLarge"/></div>
               </td>
                </tr>
				<tr class="transparent">
                <td class="transparent">
              <netui:content value="${bundle.web['common.column.testSessionName']}"/>
                </td>
                <td class="transparent">    
                <div class="formValueLarge"><netui:span  value="${actionForm.testSessionName}" styleClass="formValueLarge"/></div>
               </td>
                </tr>
				
			</table>
			<br/>
			<table>
				<tr>
					<td valign="top" class="transparent" colspan="2"><h2><netui:content value="${bundle.web['individualStudentScoring.testRoster.title']}" /></h2></td>
				</tr>
						
			</table>
			<table class="sortable">
			<% int rowId = 0; %>
				<netui-data:repeater dataSource="requestScope.studentList">
					<netui-data:repeaterHeader>
						
						<tr class="sortable">
						<ctb:tableSortColumnGroup columnDataSource="actionForm.studentSortColumn" orderByDataSource="actionForm.studentSortOrderBy" anchorName="studentSearchResultEmpty">
                        <th class="sortable" width="20%" nowrap align="center"><ctb:tableSortColumn value="UserName">Login ID</ctb:tableSortColumn></th>
                        <th class="sortable" width="20%" nowrap align="center"><ctb:tableSortColumn value="LastName">Last Name</ctb:tableSortColumn></th>
                        <th class="sortable" width="20%" nowrap align="center"><ctb:tableSortColumn value="FirstName">First Name</ctb:tableSortColumn></th>
                       <c:if test="${isStudentIdConfigurable}"> 
                       <th class="sortable" width="20%" nowrap align="center">&nbsp;${studentIdArrValue[0]}</th>  
                       </c:if>
                       <c:if test="${!isStudentIdConfigurable}">   
                       <th class="sortable" width="20%" nowrap align="center">&nbsp;Student ID</th>
                       </c:if>
					    <th class="sortable" width="20%" nowrap align="center"><ctb:tableSortColumn value="Grade">Grade</ctb:tableSortColumn></th>
					    <th class="sortable" width="20%" nowrap align="center"><ctb:tableSortColumn value="ScoringStatus">Manual Scoring Status</ctb:tableSortColumn></th>
					    <th class="sortable" width="20%" nowrap align="center">&nbsp;Online Test Status</th>
					    <th class="sortable" width="20%" nowrap align="center" style="display: none;">&nbsp;Score Obtained</th>	<!--  to be used later -->
						</ctb:tableSortColumnGroup><br/>
 
						</tr>

					</netui-data:repeaterHeader>
					<netui-data:repeaterItem>
					    <%  rowId = rowId+1; %>
						<tr class="sortable">
							<td class="sortable alignLeft">
							<netui-data:getData resultId="rosterId" value="${container.item.testRosterId}" /><%
								//changes for defect #66156 & #66159
								Integer rosterId = (Integer) pageContext
 								.getAttribute("rosterId");
 								%>
								<netui-data:getData resultId="loginId" value="${container.item.userName}" /> <%
 									String loginId = (String) pageContext
 								.getAttribute("loginId");
							 %>
							<a href="javascript:ItemformSubmit('${requestScope.itemId}','${actionForm.selectedItemType}',${actionForm.itemSetId},${requestScope.itemNo},<%=rowId%>,'<%=loginId%>',<%=rosterId %>);">
								<netui:span  value="${container.item.userName}"/>
								
								
							</a> 						
							
																
						</td>						
							<td class="sortable" align="center"><netui:span value="${container.item.lastName}"  /></td>
							<td class="sortable" align="center"><netui:span value="${container.item.firstName}" /></td>
							<td class="sortable" align="center"><netui:span value="${container.item.extPin1}" /></td>
							<td class="sortable" align="center"><netui:span value="${container.item.grade}"  /></td>
							<%String scoreStatus= "scoreStatus"+rowId; %>
							<td class="sortable" align="center">
       							 <netui-data:getData resultId="scoringStatus" value="${container.item.scoringStatus}"/> 
        						 <c:if test="${scoringStatus =='CO'}">  
           						 <span id='<%=scoreStatus%>'>Complete</span>
            					 </c:if>
            					 <c:if test="${scoringStatus =='IN'}">  
           						<span id='<%=scoreStatus%>'>Incomplete</span>
            					</c:if>     
        			</td>
		
						
							<td class="sortable" align="center"><netui:span value="${container.item.testCompletionStatusDesc}" /></td>
								<td class="sortable" style="display: none;">
								<netui-data:getData resultId="isCompleted" value="${container.item.scoringStatus}" />
								<%String scorePoints= "scorePoints"+rowId;%>
								<c:if test="${isCompleted == 'CO'}">
									<span id="<%=scorePoints%>">
									${container.item.scorePoint}</span>
								</c:if>
								<c:if test="${isCompleted =='IN'}">
									<span id="<%=scorePoints%>"> -</span>
								</c:if>
								</td>
							
						</tr>

					</netui-data:repeaterItem>
					<netui-data:repeaterFooter>
            			<tr class="sortable">
               				<td class="sortableControls" colspan="7"> 
                    			<ctb:tablePager dataSource="actionForm.studentPageRequested" 
                    				summary="request.studentPagerSummary" 
                    				objectLabel="${bundle.oas['object.students']}" 
                    				anchorName="tableSessionAnchor" 
                    				id="tableSessionAnchor" />
                			</td>
            			</tr>    
        			</netui-data:repeaterFooter>
				</netui-data:repeater>
				
				
			</table>
			<c:if test="${studentSearchResultEmpty != null}">     
    <ctb:message title="Search Result" style="informationMessage" >
          <netui:content value="${requestScope.studentSearchResultEmpty}"/>
    </ctb:message>
</c:if>   
			<br>
			<table>
			<tr>
                	<td>    
                		<p> <netui:button type="submit" value="${bundle.web['common.button.back']}" action="returnToFindItem" /> </p>
                	</td>
                </tr>
             </table>
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
					<div id="iframeDiv">
					<iframe id="iframeAudio" src="about:blank" height="70" width="200" frameborder="0" scrolling="no">
					</iframe>
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
				<iframe id="subIframe" src="rubric.jsp" style=" background-color: #FFFFCC; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal; width: 100%; height: 90%; " frameborder="0" scrollable="yes"></iframe>
				
			</div>
		</netui:form>

		<!-- ********************************************************************************************************************* -->
		<!-- End Page Content -->
		<!-- ********************************************************************************************************************* -->
	</netui-template:section>
</netui-template:template>
