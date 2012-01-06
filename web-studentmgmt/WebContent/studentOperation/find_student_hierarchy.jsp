<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>   
<%@ taglib uri="label.tld" prefix="lb" %>
<lb:bundle baseName="studentApplicationResource" />

<input type="hidden" id="addStudentEnable" name="addStudentEnable" value='<%=session.getAttribute("addStudentEnable") %>'/>
<input type="hidden" id="deleteStudentEnable" name="deleteStudentEnable" value='<%=session.getAttribute("deleteStudentEnable") %>'/>
<input type="hidden" id="jqgFirstNameID" name="jqgFirstNameID" value=<lb:label key="stu.info.firstName" prefix="'" suffix="'"/>/>
<input type="hidden" id="jqgLastNameID" name="jqgLastNameID" value=<lb:label key="stu.info.lastName" prefix="'" suffix="'"/>/>
<input type="hidden" id="jqgMiddleIniID" name="jqgMiddleIniID" value=<lb:label key="stu.label.midInitial" prefix="'" suffix="'"/>/>
<input type="hidden" id="jqgGradeID" name="jqgGradeID" value=<lb:label key="stu.info.grade" prefix="'" suffix="'"/>/>
<input type="hidden" id="jqgOrgID" name="jqgOrgID" value=<lb:label key="stu.info.org" prefix="'" suffix="'"/>/>
<input type="hidden" id="jqgGenderID" name="jqgGenderID" value=<lb:label key="stu.info.gender" prefix="'" suffix="'"/>/>
<input type="hidden" id="jqgAccoID" name="jqgAccoID" value=<lb:label key="stu.label.acco" prefix="'" suffix="'"/>/>
<input type="hidden" id="jqgLoginID" name="jqgLoginID" value=<lb:label key="stu.info.loginID" prefix="'" suffix="'"/>/>
<input type="hidden" id="addStuID" name="addStuID" value=<lb:label key="stu.label.addStu" prefix="'" suffix="'"/>/>
<input type="hidden" id="editStuID" name="editStuID" value=<lb:label key="stu.label.editStu" prefix="'" suffix="'"/>/>
<input type="hidden" id="viewStuID" name="viewStuID" value=<lb:label key="stu.label.viewStu" prefix="'" suffix="'"/>/>
<input type="hidden" id="confirmID" name="confirmID" value=<lb:label key="stu.label.confirm" prefix="'" suffix="'"/>/>
<input type="hidden" id="delStuID" name="delStuID" value=<lb:label key="stu.label.delStu" prefix="'" suffix="'"/>/>
<input type="hidden" id="delStuSuccessID" name="delStuSuccessID" value=<lb:label key="stu.msg.stuDelSuccess" prefix="'" suffix="'"/>/>
<input type="hidden" id="stuEditAccoID" name="stuEditAccoID" value=<lb:label key="stu.label.editAcco" prefix="'" suffix="'"/>/>
<input type="hidden" id="stuNoAccoID" name="stuNoAccoID" value=<lb:label key="stu.msg.noAcco" prefix="'" suffix="'"/>/>
<input type="hidden" id="miID" name="miID" value=<lb:label key="stu.label.mi" prefix="'" suffix="'"/>/>
<input type="hidden" id="stuListID" name="stuListID" value=<lb:label key="stu.label.list" prefix="'" suffix="'"/>/>
<input type="hidden" id="mprofileID" name="mprofileID" value=<lb:label key="stu.label.mprofile" prefix="'" suffix="'"/>/>
<input type="hidden" id="bcastMsgID" name="bcastMsgID" value=<lb:label key="stu.label.bcastMsg" prefix="'" suffix="'"/>/>
<input type="hidden" id="invalidNumCharID" name="invalidNumCharID" value=<lb:label key="stu.msg.invalidNumChar" prefix="'" suffix="'"/>/>
<input type="hidden" id="invalidNameID" name="invalidNameID" value=<lb:label key="stu.msg.invalidName" prefix="'" suffix="'"/>/>
<input type="hidden" id="reqTextID" name="reqTextID" value=<lb:label key="stu.msg.reqText" prefix="'" suffix="'"/>/>
<input type="hidden" id="mReqTextID" name="mReqTextID" value=<lb:label key="stu.msg.mReqText" prefix="'" suffix="'"/>/>
<input type="hidden" id="alphaNumericCharsID" name="alphaNumericCharsID" value=<lb:label key="stu.msg.alphaNumericChars" prefix="'" suffix="'"/>/>
<input type="hidden" id="invalidNumFrmtID" name="invalidNumFrmtID" value=<lb:label key="stu.msg.invalidNumFrmt" prefix="'" suffix="'"/>/>
<input type="hidden" id="minLengthFormatID" name="minLengthFormatID" value=<lb:label key="stu.msg.minLengthFormat" prefix="'" suffix="'"/>/>
<input type="hidden" id="invalidDateID" name="invalidDateID" value=<lb:label key="stu.msg.invalidDate" prefix="'" suffix="'"/>/>
<input type="hidden" id="invalidCharID" name="invalidCharID" value=<lb:label key="stu.msg.invalidChar" prefix="'" suffix="'"/>/>
<input type="hidden" id="invalidBirthDayID" name="invalidBirthDayID" value=<lb:label key="stu.msg.invalidBirthDay" prefix="'" suffix="'"/>/>
<input type="hidden" id="dateOfBirthID" name="dateOfBirthID" value=<lb:label key="stu.info.dateOfBirth" prefix="'" suffix="'"/>/>
<input type="hidden" id="orgAssingID" name="orgAssingID" value=<lb:label key="stu.label.orgAssing" prefix="'" suffix="'"/>/>
<input type="hidden" id="missRequiredFieldID" name="missRequiredFieldID" value=<lb:label key="stu.msg.missRequiredField" prefix="'" suffix="'"/>/>
<input type="hidden" id="missRequiredFieldsID" name="missRequiredFieldsID" value=<lb:label key="stu.msg.missRequiredFields" prefix="'" suffix="s'"/>/>
<input type="hidden" id="middleNameID" name="middleNameID" value=<lb:label key="stu.info.middleName" prefix="'" suffix="'"/>/>
<input type="hidden" id="assignAccoID" name="assignAccoID" value=<lb:label key="stu.label.assignAcco" prefix="'" suffix="'"/>/>

<table> 
	<tr>
		<td style="padding-left:5px;">
    		<h1><lb:label key="stu.students.title" /></h1>
		</td>
	</tr>
</table>
<table class="transparent">

    <tr class="transparent">
        <td style="border-color : #2E6E9E;padding-left:5px;">
        
      	<div id="displayMessageMain" style="display:none; width:99.5%; height:55px; background-color: #FFFFFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: bold; border: 0px solid #A6C9E2;">
			<font style="color: red; font-size:12px; font-weight:bold"><div id="titleMain"></div></font>
			<div id= "contentMain">	</div>
			<div id= "messageMain">	</div>
		</div>
		
	 <table class="transparent">
	    <tr class="transparent">
	        <td style="vertical-align:top;">
	      	<div  id= "searchheader" class="ui-corner-tl ui-corner-tr ui-widget-header treeCtrlHeader">&nbsp;<lb:label key="stu.label.search" /></div>
	    	<script>populateTree();</script>

	    	<div id="outertreebgdiv" class="treeCtrl">
		    	<div id="orgNodeHierarchy" style="width:auto;height:auto;display:table">
				</div>
			</div> 
			
		 	</td>
	 		<td class="transparent" width="5px">&nbsp;</td>
		 	<td >
	      		<div  id= "searchresultheader" class="ui-corner-tl ui-corner-tr ui-widget-header treeCtrlHeader gridPos">
	      			&nbsp;<lb:label key="stu.label.list" />	      		
	      		</div>
	    		<table id="list2" class="gridTable"></table>
				<div id="pager2" class="gridTable"></div>			
		 </td>
	    </tr>
	</table>
	

        </td>
    </tr>
</table>

<jsp:include page="/studentOperation/add_edit_student_detail.jsp" />
<jsp:include page="/studentOperation/view_student_detail.jsp" />
<div id="confirmationPopup"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<table>
		<tr>
			<td colspan="2">
			<br/>
			<p><lb:label key="stu.msg.notsave" /></p>
			<br/>
			</td>
		</tr>
		<tr>
		<td>
				<center>
					<input type="button"  value="&nbsp;Yes&nbsp;" onclick="javascript:closeConfirmationPopup(); return false;" class="ui-widget-header">&nbsp;
					<input type="button"  value="&nbsp;No&nbsp;&nbsp;" onclick="javascript:closePopUp('confirmationPopup'); return false;" class="ui-widget-header">
				</center>
			<br>
		</td>
		</tr>
		
	</table>
</div>
<div id="confirmationPopupNavigation"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<table>
		<tr>
			<td colspan="2">
			<br/>
			<p><lb:label key="stu.msg.notsave" /></p>
			<br/>
			</td>
		</tr>
		<tr>
		<td >
				<center>
					<input type="button"  value="&nbsp;Yes&nbsp;" onclick="javascript:closeConfirmationPopup(); return false;" class="ui-widget-header">&nbsp;
					<input type="button"  value="&nbsp;No&nbsp;&nbsp;" onclick="javascript:closePopUp('confirmationPopupNavigation'); return false;" class="ui-widget-header">
				</center>
			<br>
		</td>
		
		</tr>
		
	</table>
</div>

<input type="hidden" id="deleteStatus" name="deleteStatus" value=""/>
<div id="deleteStudentPopup"
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<table border="0" width="100%">
		<tr align="left">
			<td>
			<br/>
			<p>
				<lb:label key="stu.msg.delete" />
			</p>
			<br/>
			</td>
		</tr>
		<tr align="center">
			<td>
				<input type="button"  value="&nbsp;OK&nbsp;" onclick="submitDeleteStudentPopup(); showDeleteStudentStatus();" class="ui-widget-header">&nbsp;
				<input type="button"  value="&nbsp;Cancel&nbsp;" onclick="closePopUp('deleteStudentPopup');" class="ui-widget-header">				
			</td>		
		</tr>
		
	</table>
</div>

