<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="label.tld" prefix="lb" %>
<lb:bundle baseName="studentScoringResources" />


<div id="questionAnswerDetail"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">

	<div id="displayMessageForQues" class="roundedMessage"> 
			<table>
				<tr>
					<td rowspan="3" valign="top">
                   	<img src="<%=request.getContextPath()%>/resources/images/messaging/icon_error.gif" border="0" width="16" height="16">&nbsp;&nbsp;
					</td>
					<td>
						<table>
							<tr><td><font style="color: red; font-size:12px; font-weight:bold"><div id="title"></div></font></td></tr>
							<tr><td><div id= "content">	</div></td></tr>
							<tr><td><div id= "message">	</div></td></tr>
						</table>
					</td>
				</tr>
			</table>
	</div>	
	
	<div id="quesAnsAccordion" style="width:99.5%;">
			
			<div id="scoringQues">
				<h3><a href="#">Question</a></h3>
				
					<div id="questionInformation" style="background-color: #FFFFFF;height: 535px !important;">
					
					</div>
				
			</div>
			<div id="scoringRubric">
				<h3><a href="#">Rubric</a></h3>
				<div id="rubricInformation" style="overflow-y: scroll !important; overflow-x: hidden !important;"><!-- changes for defect #66994 -->
						<iframe id="rubricIframe" src="<%=request.getContextPath() %>/studentScoringOperation/rubricNew.jsp" style=" font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal; width: 100%; height: 95%; " frameborder="1" scrollable="yes"></iframe>
				</div>
			</div>
			<div id="scoringAns">
				<h3><a href="#">Answer</a></h3>
				<div id="answerInformation" style="overflow-y: scroll !important; overflow-x: hidden !important;"><!-- changes for defect #66994 -->
					
				</div>
			</div>			
	</div>
	<br>
	<center>
		<input type="button"  id="popupCancelBtnSBS" value=<lb:label key="common.button.cancel" prefix="'" suffix="'" /> onclick="javascript:closePopUp('questionAnswerDetail'); return false;" class="ui-widget-header" style="width:60px">
	</center>
	<br>
		
</div>