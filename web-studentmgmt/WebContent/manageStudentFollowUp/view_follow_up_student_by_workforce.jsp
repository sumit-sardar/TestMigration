<%@ page import="java.io.*, java.util.*"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="dto.PathNode"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<netui-data:declareBundle bundlePath="oasResources" name="oas" />
<netui-data:declareBundle bundlePath="webResources" name="web" />
<netui-data:declareBundle bundlePath="widgetResources" name="widgets" />

<%
    Boolean profileEditable = (Boolean)request.getAttribute("profileEditable"); 
	Boolean isMandatoryBirthDate = (Boolean)request.getAttribute("isMandatoryBirthDate"); //GACRCT2010CR007 - Disable Mandatory Birth Date 
	
	//Start Change For CR - GA2011CR001
	Boolean isStudentIdConfigurable = (Boolean)request.getAttribute("isStudentIdConfigurable"); 
	Boolean isStudentId2Configurable = (Boolean)request.getAttribute("isStudentId2Configurable"); 
	String []studentIdArrValue = (String[])request.getAttribute("studentIdArrValue");
	String []studentId2ArrValue = (String[])request.getAttribute("studentId2ArrValue");
	boolean isMandatoryStudentId = false;
	if(studentIdArrValue != null && studentIdArrValue[2] != null && studentIdArrValue[2]!= "") {
		if (studentIdArrValue[2].equalsIgnoreCase("T")) {
			isMandatoryStudentId = true;
		}
	}
	
	pageContext.setAttribute("gtidMandatory",new Boolean(isMandatoryStudentId));

	// End of Change CR - GA2011CR001 

%>


<table class="simple">
	<tr class="transparent">


		<!-- Student Information -->
		<td class="transparent-top" width="50%">

		<table class="transparent">
				<tr class="transparent">
				<td class="transparent"><b>Services Previously Received</b></td>

			</tr>
			<tr class="transparent">
				<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<select disabled="true" id="services" style="width: 280px">
					<Option >Please Select</Option>
					<Option>Assessment/Testing/Counseling</Option>
					<Option selected="true" >Personal Development Training</Option>
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
				<td class="transparent" width="210"><b>Support Services Needed</b></td>
				
			</tr>
			
			<tr class="transparent">
				<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<select disabled="true" id="supportServices" style="width: 280px">
					<Option >Please Select</Option>
					<Option>Transportation</Option>
					<Option>Health Care and Mental Health Care</Option>
					<Option selected="true">Housing or Retail Assistance</Option>
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
				<td class="transparent" width="210"><b>Pre-employment work maturity skills </b></td>
				
			</tr>
			
			<tr class="transparent">
				<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<select disabled="true" id="maturitySkills" style="width: 280px">
					<Option >Please Select</Option>
					<Option>Make career decisions</Option>
					<Option>Use labor market info</Option>
					<Option>Prepare a resume</Option>
					<Option>Write a cover letter</Option>
					<Option selected="true">Fill out an application</Option>
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
				<td class="transparent" width="210"><b>Workforce Readiness </b></td>
			</tr>
		
			<tr class = "transparent">
				<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<select disabled="true" id="workforceReadiness" style="width: 280px">
					<Option >Please Select</Option>
					<Option>Communications</Option>
					<Option>Think Skills</Option>
					<Option>Learning to Learn</Option>
					<Option>Personal Qualities</Option>
					<Option selected="true">Resources</Option>
					<Option>Interpersonal Skills</Option>
					<Option>Information</Option>
					<Option>Systems</Option>
					<Option>Technology</Option>

				</select></td>
			</tr>
			<tr class="transparent">
			   	<td class="transparent" width="300">&nbsp;</td>
			</tr>
			<tr class="transparent ">
				<td class="transparent"><b>Annual Income</b></td>
			</tr>
			<tr class="transparent">
				<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<select id="annualIncome"
					style="width: 280px" disabled="disabled">
					<Option >Please Select</Option>
					<Option selected="true">Single with Income Below $7,500 per year</Option>
					<Option >Married and Combined Income is below $15,000 per year</Option>
					<Option>None of the above</Option>
				</select></td>
			</tr>
			<tr class="transparent">
				<td class="transparent" width="300">&nbsp;</td>
			</tr>
		
			<tr class="transparent">
				<td class="transparent" width="210"><b> Provider Use </b></td>
			</tr>
			<tr class="transparent">
				<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input disabled="true" id="providerUse" type="text" maxlength="6" style="width: 280px" value="856"/></td>
			</tr>
		




		</table>
		</td>



		<!-- OrgNode PathList -->
		<td class="transparent-top" width="50%">
		<table class="transparent">
			<tr class="transparent">
				<td class="transparent " width="210"><b>Hourly Wage</b></td>
			</tr>
			<tr class="transparent">
				<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input id="HourlyWage"
					type="text" maxlength="7" style="width: 280px" value="8" disabled="disabled"/></td>
			</tr>

			<tr class="transparent">
				<td class="transparent" width="300">&nbsp;</td>
			</tr>

			<tr class="transparent">
				<td class="transparent" width="210"><b>Scheduled Work Hours Per Week</b></td>
			</tr>
			<tr class="transparent">
				<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input  id="ScheduledWorkHours"
					type="text" maxlength="5" style="width: 280px"  value="13" disabled="disabled"/></td>
			</tr>
			<tr class="transparent">
				<td class="transparent" width="300">&nbsp;</td>
			</tr>
			<tr class="transparent">
				<td class="transparent-small" width="210"><b>Special Programs  </b></td>
			</tr>
			<tr class="transparent">
				<td class="transparent">
				<table class="transparent">
					<tr class="transparent">
						<td class="transparent-small"><input type="checkbox" name="specialPrograms"
							id="specialPrograms2" value="Community Corrections" disabled="disabled">Community Corrections</input></td>
					</tr>
					<tr class="transparent">
						<td class="transparent-small"><input  type="checkbox" name="specialPrograms"
							id="specialPrograms3" value="State Corrections" checked="checked" disabled="disabled">State Corrections</input></td>
					</tr>
					<tr class="transparent">
						<td class="transparent-small"><input  type="checkbox" name="specialPrograms"
							id="specialPrograms4" value="Homeless Program" checked="checked" disabled="disabled">Homeless Program</input></td>
					</tr>
					<tr class="transparent">
						<td class="transparent-small"><input  type="checkbox" name="specialPrograms"
							id="specialPrograms5" value="Workplace Ed." checked="checked" disabled="disabled">Workplace Ed.</input></td>
					</tr>
					<tr class="transparent">
						<td class="transparent-small"><input  type="checkbox" name="specialPrograms"
							id="specialPrograms6" value="Tutoring" disabled="disabled">Tutoring</input></td>
					</tr>
					<tr class="transparent">
						<td class="transparent-small"><input type="checkbox" name="specialPrograms"
							id="specialPrograms7" value="Distance Learning" disabled="disabled">Distance Learning</input></td>
					</tr>
					<tr class="transparent">
						<td class="transparent-small"><input  type="checkbox" name="specialPrograms"
							id="specialPrograms8" value="Special Needs" disabled="disabled">Special Needs</input></td>
					</tr>
					<tr class="transparent">
						<td class="transparent-small"><input type="checkbox" name="specialPrograms"
							id="specialPrograms9" value="Alternative Ed. (K12)" disabled="disabled">Alternative Ed. (K12)</input></td>
					</tr>
					<tr class="transparent">
						<td class="transparent-small"><input  type="checkbox" name="specialPrograms"
							id="specialPrograms10" value="Non-traditional Training" disabled="disabled">Non-traditional Training</input></td>
					</tr>
					<tr class="transparent">
						<td class="transparent-small"><input  type="checkbox" name="specialPrograms"
							id="specialPrograms11" value="El Civics" disabled="disabled">El Civics</input></td>
					</tr>
					<tr class="transparent">
						<td class="transparent-small"><input  type="checkbox" name="specialPrograms"
							id="specialPrograms12" value="Carl Perkins" disabled="disabled">Carl Perkins</input></td>
					</tr>
					<tr class="transparent">
						<td class="transparent-small"><input  type="checkbox" name="specialPrograms"
							id="specialPrograms13" value="Other" disabled="disabled">Other</input></td>
					</tr>


		</table>


		</td>
	</tr>


</table>

</td>
</tr>
</table>
