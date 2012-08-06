<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="label.tld" prefix="lb"%>
<lb:bundle baseName="studentRegistrationResource" />

<div id="recommendedDialogID" style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
<input type="hidden" id= "recommendedProductId" />
	<div  >
	    <span>&nbsp;&nbsp;Student&nbsp; </span> 
		<b> <span id="studentName"></span> </b>  
		<span> &nbsp;most recently took : </span>
	</div>

	<div  style="float: left; width: 100%; padding-left: 10px; padding-top: 5px;">
		<table cellpadding="0" style="float: left;" width="95%">
			<tr>
				<td width="30%"><lb:label key="registration.SessionName" prefix=" " suffix=" " /> </td>
				<td><b> <span id="sessionName" > </span> </b></td>
			</tr>
			<tr>
				<td width="30%"> <lb:label key="registration.testName" prefix=" " suffix=" " /> </td>
				<td><b> <span id="testName" > </span> </b></td>
			</tr>
			<tr>
				<td width="30%"></td>
				<td>
				<table width="100%">
					<tr>
						<hr size=1 />
					</tr>
				</table>
				</td>
			</tr>
			<tr>
				<td width="30%"></td>
				<td>
					<div style="overflow-y: auto; height: 170px">
						<table id="subtestList" width="95%">
			<tr>
			</tr>
		 		</table>
				
				</div>
				</td>
			</tr>
			<tr>
			    <td width="30%">
			    </td>
				<td>
					<table width="100%">
					<tr> <hr size=1 />	</tr>
				    </table>
				</td>
			</tr>
			<tr>
				<td width="30%"> <lb:label key="registration.completedDate" prefix=" " suffix=" " /> </td>
				<td><b> <span id="completedDate" > </span> </b></td>
			</tr>
		</table>
		<br>
	</div>

	<div style="float: left; padding-top: 5px;"><lb:label key="registration.registerStudentQuestion" prefix="  " suffix=" " /> <br>
		<br>
		<span >
			<lb:label key="registration.recommendation.yes.info" prefix="  " suffix=" " /> 
			<span id="recommendedTest"></span></b> 
	    </span> <br>
		<span ><lb:label key="registration.recommendation.no.info" prefix="  " suffix=" " />  </span>
		<br>
	</div>
	
  	<div id="showBySessionFRButtonDiv" style="float: left; clear: both; padding-top: 20px; padding-bottom: 15px; visibility: none;width: 100%;">
		<div style="float: none">
		<center>
		    <input type="button"  id="Yes" value="&nbsp;Yes&nbsp;"      onclick="javascript:sessionListPopupOnFRAcceptForSession(); return false;" class="ui-widget-header" style="width:60px">
			<input type="button"  id="No"  value="&nbsp;No&nbsp;&nbsp;" onclick="javascript:openModifyTestPopup(this); return false;" class="ui-widget-header" style="width:60px">
   		</center>
   		</div>		
  </div>
    <!-- added by sumit : start --> 
    <div id="showByStudentFRButtonDiv" style="float: left; clear: both; padding-top: 20px; padding-bottom: 15px; visibility: none; width: 100%;">
		<div style="float: none">
		<center>
		    <input type="button"  id="showByStudentYes" value="&nbsp;Yes&nbsp;"      onclick="javascript:sessionListPopupOnFRAccept(); return false;" class="ui-widget-header" style="width:60px">
			<input type="button"  id="showByStudentNo"  value="&nbsp;No&nbsp;&nbsp;" onclick="javascript:sessionListPopupOnFRNotAccept(); return false;" class="ui-widget-header" style="width:60px">
   		</center>
   		</div>
  </div>
 <!-- added by sumit : end --> 
<br>
</div>