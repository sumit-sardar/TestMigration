<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="label.tld" prefix="lb" %>
<lb:bundle baseName="studentRegistrationResource" />

<div id="studentConfirmation" 
	style="display: none; background-color: #D4ECFF; font-family: Arial, Verdana, Sans Serif; font-size: 12px; font-style: normal; font-weight: normal;">
	<div id="studRegInfo" style ="background-color: #FFFFFF;">
	 <table width="100%" cellpadding="0" cellspacing="0" class="transparent">
	    <tr>
	        <td nowrap="">
	            <h1>
	             Register Student: Congratulations!
	            </h1>
	        </td>
	    
	        <td width="65%" class="transparent"></td>
	        <td width="100%" rowspan="2" align="right" valign="top">
	        	&nbsp;
	        </td>
	    
	        <td rowspan="2">
	            <table width="25"><tr><td></td></tr></table>
	        </td>
	    </tr>
	    
	    <tr>   
	        <td width="65%" class="transparent">
	            <p>
	                You've successfully registered this student. Review the student's assignments for accuracy.<br/>
	            </p>
	        </td>
	        <td width="62%" class="transparent"></td> 
	    </tr>
	</table> 
<!--End of change for licnese-->

<%-- message 
<jsp:include page="/registration/show_message.jsp" /> --%>


<!-- student section -->
	<p>
	<div id="StudentDetail">
		<table align="center" width="95%">
			<tr>
				<td>
					<div class="ui-widget-header"><h3><lb:label key="student.registration.showBy.student" /></h3></div>
					<table class="sortable" width="100%">
						<tr class="transparent">
							<td class="transparent" width="50%">
							    <table class="transparent">
							        <tr class="transparent">
							            <td class="transparent" width="120"><span><lb:label key="stu.label.stdName" /></span></td>
							            <td class="transparent" width="*"><span> 01, Tabe</span></td>
							        </tr>
							        <tr class="transparent">
							            <td class="transparent" width="120"><span><lb:label key="stu.label.accessCode" /></span></td>
							            <td class="transparent" width="*"><span> BOTANIST 89</span></td>      
							            <td class="transparent" width="*"><span></span></td>
							        </tr>
							    </table>
							</td>
							<td class="transparent" width="50%">
							    <table class="transparent">
							        <tr class="transparent">
							            <td class="transparent" width="120"><span><lb:label key="stu.label.loginID" /></span></td>
							            <td class="transparent" width="*"><span>TABE-01-0102</span></td>
							        </tr>
							        <tr class="transparent">
							            <td class="transparent" width="120"><span><lb:label key="stu.label.password" /></span></td>
							            <td class="transparent" width="*"><span>SPECK5</span></td>
							        </tr>
							    </table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</div>
	<p/>
	
	<!-- test section -->
	<p>
	<div id="StudentDetail">
	<table align="center" width="95%">
		<tr>
			<td>
				<div class="ui-widget-header"> <h3><lb:label key="stu.label.testSession" /></h3></div>
					<table class="sortable" width="100%">
						<tr class="transparent">
							<td class="transparent" width="50%">
							    <table class="transparent">
							        <tr class="transparent">
							            <td class="transparent" width="120"><span><lb:label key="stu.label.testName" /></span></td>
							            <td class="transparent" width="*"><span>TABE 10 Online Complete Battery</span></td>
							        </tr>
							        <tr class="transparent">
							            <td class="transparent" width="120"><span><lb:label key="stu.label.organization" /></span></td>
							            <td class="transparent" width="*"><span>QA TABE4</span></td>
							        </tr>
							        <tr class="transparent">
							            <td class="transparent" width="120"><span><lb:label key="stu.label.startDate" /></span></td>
							            <td class="transparent" width="*"><span>07/07/2012</span></td>
							        </tr>
							        <tr class="transparent">
							            <td class="transparent" width="120"><span><lb:label key="stu.label.loginStartTime" /></span></td>
							            <td class="transparent" width="*"><span>08:00 AM</span></td>
							        </tr>
							        <tr class="transparent">
							            <td class="transparent" width="120"><span><lb:label key="stu.label.locatorTest" /></span></td>
							            <td class="transparent" width="*"><span>yes</span></td>
							        </tr>
							    </table>
							</td>
							<td class="transparent" width="50%">
							    <table class="transparent">
							        <tr class="transparent">
							            <td class="transparent" width="120"><span><lb:label key="stu.label.testSessionName" /></span></td>
							            <td class="transparent" width="*"><span>TABE-01-0102</span></td>
							        </tr>
							        <tr class="transparent">
							            <td class="transparent" width="120"><span><lb:label key="stu.label.testSessionID" /></span></td>
							            <td class="transparent" width="*"><span>361738</span></td>
							        </tr>
							        <tr class="transparent">
							            <td class="transparent" width="120"><span><lb:label key="stu.label.endDate" /></span></td>
							            <td class="transparent" width="*"><span>03/30/2013</span></td>
							        </tr>
							        <tr class="transparent">
							            <td class="transparent" width="120"><span><lb:label key="stu.lable.loginEndTime" /></span></td>
							            <td class="transparent" width="*"><span>05:00 PM</span></td>
							        </tr>
							        <tr class="transparent">
							            <td class="transparent" width="120"><span><lb:label key="stu.label.allowsBreaks" /></span></td>
							            <td class="transparent" width="*"><span>No</span></td>
							        </tr>
							    </table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</div>
	<p/>
	
	<!-- Test Structure Section -->
	<p>
		<div>
		<table align="center" width="95%">
			<tr>
				<td>
					<div class="ui-widget-header"> <h3><lb:label key="stu.label.testStructure" /></h3></div>
						<table class="sortable">
					    	<tr class="sortable">
					        	<th class="sortable alignLeft" height="25" width="*">&nbsp;<span><lb:label key="stu.label.subtest" /></span></th>
					    	</tr>
					    	<tr class="sortable">
					        	<td class="sortable alignLeft" width="*"><span>TABE 9 & 10 Online Locator</span></td>
					    	</tr>
						</table>
					</div>
				</td>
			</tr>
		</table>
		</div>
	</p>
	<br/>
	
	<div>
	<table align="center" width="95%">
			<tr>
				<td>
					<table class="sortable"> 
			    		<tr class="sortable">
					        <th class="sortable alignCenter" height="25"><lb:label key="stu.label.order" /></th>                
					        <th class="sortable alignLeft" height="25">&nbsp;&nbsp;<lb:label key="stu.label.subtests" /></th>
			    		</tr>
			    		<tr class="sortable">
			    			<td class="sortable alignCenter">1</td>
			    			<td class="sortable">TABE Reading</td>
			    		</tr>
			    		<tr class="sortable">
			    			<td class="sortable alignCenter">2</td>
			    			<td class="sortable">TABE Mathematics Computation</td>
			    		</tr>
			    		<tr class="sortable">
			    			<td class="sortable alignCenter">3</td>
			    			<td class="sortable">TABE Applied Mathematics</td>
			    		</tr>
			    		<tr class="sortable">
			    			<td class="sortable alignCenter">4</td>
			    			<td class="sortable">TABE Language</td>
			    		</tr>
			    		<tr class="sortable">
			    			<td class="sortable alignCenter">5</td>
			    			<td class="sortable">TABE Vocabulary</td>
			    		</tr>
			    		<tr class="sortable">
			    			<td class="sortable alignCenter">6</td>
			    			<td class="sortable">TABE Language Maechanics</td>
			    		</tr>
			    		<tr class="sortable">
			    			<td class="sortable alignCenter">7</td>
			    			<td class="sortable">TABE Spelling</td>
			    		</tr>
			    	</table>
			    </td>
			</tr>
		</table>
	</div>
	<br/>
	<p>
		Do you want to print the Test Access Code on your Individual or Multiple Test Tickets?<br/>
		<input type="radio" id="allow" name="individualAccess" value="Yes" onclick=""><lb:label key="common.button.yes" /></input>
		<input type="radio" id="deny" name="individualAccess" value="No"  onclick="" checked="checked"><lb:label key="common.button.no" /></input>
	</p>
	<p align="right">
	    <a href="#" style='color:blue; text-decoration:underline;' onClick=""><lb:label key="stu.label.testTicket" /></a>
	    &nbsp;<img src="/RegistrationWeb/resources/images/logo_pdf.gif" border="0">
	</p>
	<br/>
	
	</div>
	<br/>
	<div id="StudConfClose">
		<center>
			<input type="button"  id="sData" value="Done" onclick="javascript:closePopUp('studentConfirmation'); return false;" class="ui-widget-header">
		</center>
		<br>
	</div>	
</div> 

