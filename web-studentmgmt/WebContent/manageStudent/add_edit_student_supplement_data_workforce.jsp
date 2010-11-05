<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="dto.PathNode"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>
<%
Boolean profileEditable = (Boolean)request.getAttribute("profileEditable"); 
%>

<table class="simple" border="1">
	<tr class="transparent">
	   	<td class="transparent" width="300">&nbsp;</td>
	</tr>
	<tr class="transparent">


		<!-- column 1 -->
		<td class="transparent-top" width="50%" valign="top">
		<table class="transparent">
			<tr class="transparent ">
				<td class="transparent"><span class="asterisk">*</span>&nbsp;<b>Annual Income</b>
				</td>
			</tr>
			<tr class="transparent">
				<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<select disabled="true" id="annualIncome"
					style="width: 280px">
					<Option selected="true">Please Select</Option>
					<Option>Single with Income Below $7,500 per year</Option>
					<Option>Married and Combined Income is below $15,000 per year</Option>
					<Option>None of the above</Option>
				</select></td>
			</tr>

			<tr class="transparent">
			   	<td class="transparent" width="300">&nbsp;</td>
			</tr>

			<tr class="transparent">
				<td class="transparent"><span class="asterisk">*</span>&nbsp;<b>Services Previously Received</b></td>

			</tr>
			<tr class="transparent">
				<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<select disabled="true" id="services" style="width: 280px">
					<Option selected="true">Please Select</Option>
					<Option>Assessment/Testing/Counseling</Option>
					<Option>Personal Development Training</Option>
					<Option>Job Development/Job Search Assistance</Option>
					<Option>Occupation Sills Training (non-On the Job)</Option>
					<Option>On-the-job training</Option>
					<Option>Work Experience</Option>
					<Option>Pre-employment skills job readiness training</Option>
					<Option>Postsecondary Academic Education</Option>
				</select></td>
			</tr>

			<tr class="transparent">
			   	<td class="transparent" width="300">&nbsp;</td>
			</tr>

			<tr class="transparent">
				<td class="transparent" width="210"><span class="asterisk">*</span>&nbsp;<b>Support Services Needed</b></td>
				
			</tr>
			
			<tr class="transparent">
				<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<select disabled="true" id="supportServices" style="width: 280px">
					<Option selected="true">Please Select</Option>
					<Option>Transportation</Option>
					<Option>Health Care and Mental Health Care</Option>
					<Option>Housing or Retail Assistance</Option>
					<Option>Personal, Financing or legal Counseling</Option>
					<Option>Supplemental Instructional Service</Option>
					<Option>Needs-based related payments</Option>
					<Option>Emergency Financial Services</Option>
					<Option>Federal Education Cash Assistance</Option>
					<Option>Other Support Services</Option>
				</select></td>
			</tr>
			
			<tr class="transparent">
			   	<td class="transparent" width="300">&nbsp;</td>
			</tr>
			
			<tr class="transparent">
				<td class="transparent" width="210"><span class="asterisk">*</span>&nbsp;<b>Pre-employment work maturity skills </b></td>
				
			</tr>
			
			<tr class="transparent">
				<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<select disabled="true" id="maturitySkills" style="width: 280px">
					<Option selected="true">Please Select</Option>
					<Option>Make career decisions</Option>
					<Option>Use labor market info</Option>
					<Option>Prepare a resume</Option>
					<Option>Write a cover letter</Option>
					<Option>Fill out an application</Option>
					<Option>Interview</Option>
					<Option>Being punctual</Option>
					<Option>Regular attendance</Option>
					<Option>Good interpersonal skills</Option>
					<Option>Positive attitude/behaviors</Option>
					<Option>Appropriate appearance</Option>
					<Option>Complete tasks effectively</Option>
				</select></td>
			</tr>
			
			<tr class="transparent">
			   	<td class="transparent" width="300">&nbsp;</td>
			</tr>
			
			<tr class="transparent">
				<td class="transparent" width="210"><span class="asterisk">*</span>&nbsp;<b>Workforce Readiness </b></td>
			</tr>
		
			<tr class = "transparent">
				<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<select disabled="true" id="workforceReadiness" style="width: 280px">
					<Option selected="true">Please Select</Option>
					<Option>Communications</Option>
					<Option>Think Skills</Option>
					<Option>Learning to Learn</Option>
					<Option>Personal Qualities</Option>
					<Option>Resources</Option>
					<Option>Interpersonal Skills</Option>
					<Option>Information</Option>
					<Option>Systems</Option>
					<Option>Technology</Option>

				</select></td>
			</tr>
			<tr class="transparent">
			   	<td class="transparent" width="300">&nbsp;</td>
			</tr>
			<tr class="transparent">
				<td class="transparent" width="210"><b> Provider Use </b></td>
			</tr>
			<tr class="transparent">
				<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input disabled="true" id="providerUse" type="text" maxlength="6" style="width: 280px" /></td>
			</tr>
		</table>
		</td>

		<!-- column 2 -->
		<td class="transparent-top" width="50%" valign="top">
		<table class="transparent">
			<tr class="transparent">
				<td class="transparent " width="210"><span class="asterisk">*</span>&nbsp;<b>Hourly Wage</b></td>
			</tr>
			<tr class="transparent">
				<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input disabled="true" id="HourlyWage" type="text" maxlength="7" style="width: 280px" /></td>
			</tr>
			
			<tr class="transparent">
			   	<td class="transparent" width="300">&nbsp;</td>
			</tr>
			
			<tr class="transparent">
				<td class="transparent" width="210"><span class="asterisk">*</span>&nbsp;<b>Scheduled Work Hours Per Week</b></td>
			</tr>
			<tr class="transparent">
				<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input disabled="true" id="ScheduledWorkHours" type="text" maxlength="5" style="width: 280px" /></td>
			</tr>
			<tr class="transparent">
			   	<td class="transparent" width="300">&nbsp;</td>
			</tr>
			<tr class="transparent">
				<td class="transparent-small" width="210"><span class="asterisk">*</span>&nbsp;<b>Special Programs  </b></td>
			</tr>
			<tr class="transparent">
				<td class="transparent">
				<table class="transparent">
					<tr class="transparent">
						<td class="transparent-small"><input disabled="true" type="checkbox" name="specialPrograms"
							id="specialPrograms2" value="Community Corrections">Community Corrections</input></td>
					</tr>
					<tr class="transparent">
						<td class="transparent-small"><input disabled="true" type="checkbox" name="specialPrograms"
							id="specialPrograms3" value="State Corrections">State Corrections</input></td>
					</tr>
					<tr class="transparent">
						<td class="transparent-small"><input disabled="true" type="checkbox" name="specialPrograms"
							id="specialPrograms4" value="Homeless Program">Homeless Program</input></td>
					</tr>
					<tr class="transparent">
						<td class="transparent-small"><input disabled="true" type="checkbox" name="specialPrograms"
							id="specialPrograms5" value="Workplace Ed.">Workplace Ed.</input></td>
					</tr>
					<tr class="transparent">
						<td class="transparent-small"><input disabled="true" type="checkbox" name="specialPrograms"
							id="specialPrograms6" value="Tutoring">Tutoring</input></td>
					</tr>
					<tr class="transparent">
						<td class="transparent-small"><input disabled="true" type="checkbox" name="specialPrograms"
							id="specialPrograms7" value="Distance Learning">Distance Learning</input></td>
					</tr>
					<tr class="transparent">
						<td class="transparent-small"><input disabled="true" type="checkbox" name="specialPrograms"
							id="specialPrograms8" value="Special Needs">Special Needs</input></td>
					</tr>
					<tr class="transparent">
						<td class="transparent-small"><input disabled="true" type="checkbox" name="specialPrograms"
							id="specialPrograms9" value="Alternative Ed. (K12)">Alternative Ed. (K12)</input></td>
					</tr>
					<tr class="transparent">
						<td class="transparent-small"><input disabled="true" type="checkbox" name="specialPrograms"
							id="specialPrograms10" value="Non-traditional Training">Non-traditional Training</input></td>
					</tr>
					<tr class="transparent">
						<td class="transparent-small"><input disabled="true" type="checkbox" name="specialPrograms"
							id="specialPrograms11" value="El Civics">El Civics</input></td>
					</tr>
					<tr class="transparent">
						<td class="transparent-small"><input disabled="true" type="checkbox" name="specialPrograms"
							id="specialPrograms12" value="Carl Perkins">Carl Perkins</input></td>
					</tr>
					<tr class="transparent">
						<td class="transparent-small"><input disabled="true" type="checkbox" name="specialPrograms"
							id="specialPrograms13" value="Other">Other</input></td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr class="transparent">
	   	<td class="transparent" width="300">&nbsp;</td>
	</tr>
</table>

