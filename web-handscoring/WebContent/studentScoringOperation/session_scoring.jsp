<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="label.tld" prefix="lb" %>
<lb:bundle baseName="studentScoringResources" />

<div id="sessionScoringId"	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<br>
			<div id="displayMessageSession" class="errMsgs" style="display: none; width: 50%; float: left;">
						<table>
							<tr>
								<td width="18" valign="middle">
									<div id="errorIconSession" style="display:none;">
				                   		<img src="<%=request.getContextPath()%>/resources/images/messaging/icon_error.gif" border="0" width="16" height="16">
									</div>
									<div id="infoIconSession" style="display:none;">
										<img src="<%=request.getContextPath()%>/resources/images/messaging/icon_info.gif" border="0" width="16" height="16">
									</div>
								</td>
								<td class="saveMsgs" valign="middle">
									<div id= "contentMainSession"></div>
								</td>
							</tr>
						</table>
					</div>	  
	<div id="sessionScoringAccordion" style="width:99.5%;">
		<div>
			<div id="scoreByStudentId">
				<h3><a href="#" ><lb:label key="scoring.accordion.scoreByStudent" /></a></h3>
				<div id="score_by_student_details_div" style="background-color: #FFFFFF; overflow-y: scroll !important; overflow-x: hidden !important; height: 535px !important;">
					<div align="center" style ="width:98%;">
						<jsp:include page="/studentScoringOperation/score_by_student.jsp" />
					</div>
				</div>
			</div>
	    </div>
	    <div>
			<div id="scoreByItemId">
				<h3><a href="#" ><lb:label key="scoring.accordion.scoreByItem" /></a></h3>
				<div id="score_by_item_details_div" style="background-color: #FFFFFF; overflow-y: scroll !important; overflow-x: hidden !important; height: 535px !important;">
					<div align="center" style ="width:98%;">
						<jsp:include page="/studentScoringOperation/score_by_item_details.jsp" />
					</div>
				</div>
			</div>
		</div>
	</div>
	<br>
	<center>
		<input type="button"  id="popupCloseBtnSBS" value=<lb:label key="common.button.close" prefix="'" suffix="'" /> onclick="javascript:closePopUp('sessionScoringId'); return false;" class="ui-widget-header" style="width:60px">
	</center>
	<br>
</div>