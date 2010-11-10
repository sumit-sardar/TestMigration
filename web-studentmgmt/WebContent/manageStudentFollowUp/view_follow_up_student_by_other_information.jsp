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
				<td class="transparent-small" width="210"><b>Instructional Program </b></td>
			</tr>
			<tr class="transparent">

				<td class="transparent">
				<table class="transparent">

					<tr class="transparent">
						<td class="transparent-small"><input type="checkbox" name="" id="ABE" value="ABE" disabled="true" >ABE</td>
					</tr>
					<tr class="transparent">
						<td class="transparent-small"><input type="checkbox" name="" id="ESL" value="ESL" disabled="true">ESL</td>
					</tr>
					<tr class="transparent">
						<td class="transparent-small"><input type="checkbox" name="" id="ESL/Citizenship" value="ESL/Citizenship" disabled="true">ESL/Citizenship</td>
					</tr>
					<tr class="transparent">
						<td class="transparent-small"><input type="checkbox" name="" id="High School Diploma"
							value="High School Diploma" disabled="true" checked="checked">High School Diploma</td>
					</tr>
					<tr class="transparent">
						<td class="transparent-small"><input type="checkbox" name="" id="GED" value="GED" disabled="true">GED</td>
					</tr>
					<tr class="transparent">
						<td class="transparent-small"><input type="checkbox" name="" id="Spanish GED" value="Spanish GED" disabled="true">Spanish
						GED</td>
					</tr>
					<tr class="transparent">
						<td class="transparent-small"><input type="checkbox" name="" id="Voc./Occupational skills"
							value="Voc./Occupational skills" disabled="true">Voc./Occupational skills</td>
					</tr>
					<tr class="transparent">
						<td class="transparent-small"><input type="checkbox" name="" id="Workforce Readiness"
							value="Workforce Readiness" disabled="true">Workforce Readiness</td>
					</tr>
					<tr class="transparent">
						<td class="transparent-small"><input type="checkbox" name="" id="Adults with Disabilities"
							value="Adults with Disabilities" disabled="true">Adults with Disabilities</td>
					</tr>
					<tr class="transparent">
						<td class="transparent-small"><input type="checkbox" name="" id="Health & Safety" value="Health & Safety" disabled="true">Health
						& Safety</td>
					</tr>
					<tr class="transparent">
						<td class="transparent-small"><input type="checkbox" name="" id="Home Economics" value="Home Economics" disabled="true">Home
						Economics</td>
					</tr>
					<tr class="transparent">
						<td class="transparent-small"><input type="checkbox" name="" id="Parent Education" value="Parent Education" disabled="true">Parent
						Education</td>
					</tr>
					<tr class="transparent">
						<td class="transparent-small"><input type="checkbox" name="" id="Other Adults" value="Other Adults" disabled="true">Other
						Adults</td>
					</tr>
					<tr class="transparent">
						<td class="transparent-small"><input type="checkbox" name="" id="Other" value="Other" disabled="true">Other</td>
					</tr>
				</table>
				</td>
			</tr>
			<tr><td>&nbsp;</td></tr>
			<tr class="transparent">
				<td class="transparent " width="300"><b>Status</b></td>

			</tr>
			<tr class="transparent">
				<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <select disabled="true" style="width: 280px">
					<Option selected="true" >Retain in Program</Option>
					<Option>Left Program</Option>
					<Option>No show or did not attend at least 12 hours</Option>
				</select></td>
			</tr>
			<tr><td>&nbsp;</td></tr>
			<tr class="transparent">
				<td class="transparent " width="300"><b>Progress</b></td>

			</tr>
			
			<tr class="transparent">
				<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <select disabled="true" style="width: 280px">
									<Option selected="true">Progressed within level or program</Option>
					<Option>Completed level or program</Option>
					<Option>Advance to a higher level or program</Option>
				</select></td>
			</tr>
			<tr><td>&nbsp;</td></tr>
			<tr class="transparent">
				<td class="transparent " width="300"><b>Attainable Goal within Program Year – Primary </b></td>

			</tr>
			<tr class="transparent">
				<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <select disabled="true" style="width: 190px">
					
					<Option selected="true">Improve basic skills</Option>
					<Option>Improve English skills</Option>
					<Option>H.S. Diploma/GED</Option>
					<Option>Get a Job</Option>
					<Option>Retain a Job</Option>
					<Option>Enter college or training</Option>
					<Option>Work-based project</Option>
					<Option>Family goal</Option>
					<Option>U.S. Citizenship</Option>
					<Option>Military</Option>
					<Option>Personal goal</Option>
					<Option>Other</Option>
				</select> &nbsp;&nbsp;&nbsp; <input disabled="true" type="checkbox" name="" id="Yes" value="Yes" checked="checked"/>Yes
				<input disabled="true" type="checkbox" name="" id="No" value="No" />No</td>
			</tr>
			<tr><td>&nbsp;</td></tr>
			<tr class="transparent">
				<td class="transparent " width="300"><b>Attainable Goal within Program Year – Secondary </b></td>

			</tr>
			<tr class="transparent">
				<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <select disabled="true" style="width: 190px">
					
					<Option selected="true">Improve basic skills</Option>
					<Option>Improve English skills</Option>
					<Option>H.S. Diploma/GED</Option>
					<Option>Get a Job</Option>
					<Option>Retain a Job</Option>
					<Option>Enter college or training</Option>
					<Option>Work-based project</Option>
					<Option>Family goal</Option>
					<Option>U.S. Citizenship</Option>
					<Option>Military</Option>
					<Option>Personal goal</Option>
					<Option>None</Option>
					<Option>Other</Option>
				</select> &nbsp;&nbsp;&nbsp;<input disabled="true" type="checkbox" name="" id="Yes" value="Yes" checked="checked" />Yes
				<input disabled="true" type="checkbox" name="" id="No" value="No" />No</td>
			</tr>

			<tr><td>&nbsp;</td></tr>
			<tr class="transparent">
				<td class="transparent " width="300"><b>Class Number</b></td>

			</tr>
			<tr class="transparent">
				<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <input disabled="true" type="text" style="width: 280px;" value="8"/></td>
			</tr>
			<tr><td>&nbsp;</td></tr>
			<tr class="transparent">
				<td class="transparent " width="210"><b>Instructional Level </b></td>
			</tr>
			<tr class="transparent">
				<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <select disabled="true" style="width: 280px">
					<Option >Please Select</Option>
					<Option>ESL Beginning Literacy</Option>
					<Option>ESL Beginning</Option>
					<Option selected="true">ESL Intermediate Low</Option>
					<Option>ESL Intermediate High</Option>
					<Option>ESL Advanced Low</Option>
					<Option>ESL Advanced High</Option>
					<Option>ABE Beginning Literacy</Option>
					<Option>ABE Beginning</Option>
					<Option>ABE Intermediate Low</Option>
					<Option>ABE Intermediate High</Option>
					<Option>ASE Low</Option>
					<Option>ASE High</Option>
				</select></td>
			</tr>
			<tr><td>&nbsp;</td></tr>
			<tr class="transparent">
				<td class="transparent" width="100"><b>Skill Level</b></td>

			</tr>
			<tr class="transparent">
				<td>
				<table>
					<tr>
						<td class="transparent-small" align="right">&nbsp;&nbsp;&nbsp;Technology:</td>
						<td><select disabled="true">
							<Option>Please Select</Option>
							<Option>0</Option>
							<Option>1</Option>
							<Option>2</Option>
							<Option>3</Option>
							<Option>4</Option>
							<Option>5</Option>
							<Option selected="true">6</Option>
							<Option>7</Option>
							<Option>8</Option>
							<Option>9</Option>
						</select></td>
					</tr>
					<tr>
						<td class="transparent-small" align="right">&nbsp;&nbsp;&nbsp;Speaking:</td>
						<td><select disabled="true">
							<Option>Please Select</Option>
							<Option>0</Option>
							<Option>1</Option>
							<Option>2</Option>
							<Option>3</Option>
							<Option>4</Option>
							<Option>5</Option>
							<Option>6</Option>
							<Option selected="true">7</Option>
							<Option>8</Option>
							<Option>9</Option>
						</select></td>

					</tr>
					<tr class="transparent">
						<td class="transparent-small" align="right">&nbsp;&nbsp;&nbsp;Reading:</td>

						<td><select style="float: right;" disabled="true">
							<Option>Please Select</Option>
							<Option>0</Option>
							<Option>1</Option>
							<Option>2</Option>
							<Option>3</Option>
							<Option selected="true">4</Option>
							<Option>5</Option>
							<Option>6</Option>
							<Option>7</Option>
							<Option>8</Option>
							<Option>9</Option>
						</select></td>

					</tr>
					<tr class="transparent">
						<td class="transparent-small" align="right">&nbsp;&nbsp;&nbsp;Writing:</td>
						<td><select style="float: right;" disabled="true">
							<Option>Please Select</Option>
							<Option>0</Option>
							<Option>1</Option>
							<Option>2</Option>
							<Option>3</Option>
							<Option>4</Option>
							<Option selected="true">5</Option>
							<Option>6</Option>
							<Option>7</Option>
							<Option>8</Option>
							<Option>9</Option>
						</select></td>

					</tr>
					<tr class="transparent">
						<td class="transparent-small" align="right">&nbsp;&nbsp;&nbsp;Math:</td>
						<td><select style="float: right;" disabled="true">
							<Option>Please Select</Option>
							<Option>0</Option>
							<Option>1</Option>
							<Option>2</Option>
							<Option>3</Option>
							<Option>4</Option>
							<Option>5</Option>
							<Option selected="true">6</Option>
							<Option>7</Option>
							<Option>8</Option>
							<Option>9</Option>
						</select></td>
					</tr>
				</table>
				</td>
			</tr>
			<tr><td>&nbsp;</td></tr>
			<tr class="transparent">
				<td class="transparent" width="100"><b>Reason for Leaving</b></td>

			</tr>
			<tr class="transparent">
				<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <select disabled="true" style="width: 280px">
					<Option selected="true">Please Select</Option>
					<Option>Changed Class or Program</Option>
					<Option>Completed Program</Option>
					<Option>Met goal</Option>
					<Option>End of program year</Option>
					<Option>Got a job</Option>
					<Option>Moved</Option>
					<Option>Schedule conflict</Option>
					<Option>Lack of transportation</Option>
					<Option>Lack of child care</Option>
					<Option>Family problems</Option>
					<Option>Own health problems</Option>
					<Option>Lack of interest</Option>
					<Option>Public safety</Option>
					<Option>Administratively separated</Option>
					<Option>Unknown reason</Option>
					<Option>Other known reason</Option>
				</select></td>
			</tr>
			<tr><td>&nbsp;</td></tr>
			<tr class="transparent">
				<td class="transparent" width="100"><b>High School Credits Earned (0.5
				– 60.0) </b></td>

			</tr>
			<tr class="transparent">
				<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <input disabled="true" type="text" style="width: 280px;" value="20.0"/></td>
			</tr>





		</table>
		</td>



		<!-- OrgNode PathList -->
		<td class="transparent-top" width="50%">
		<table class="transparent">
			<tr class="transparent">
				<td class="transparent " width="300"><b>Sub-Sections of GED passed</b></td>

			</tr>
			<tr>
				<td>
				<table class="transparent">

					<tr class="transparent">
						<td class="transparent-small">&nbsp;&nbsp;&nbsp;<input disabled="true" type="checkbox" name="">Language, Arts, Writing</td>
					</tr>
					<tr class="transparent">
						<td class="transparent-small">&nbsp;&nbsp;&nbsp;<input disabled="true" type="checkbox">Science</td>
					</tr>

					<tr class="transparent">
						<td class="transparent-small">&nbsp;&nbsp;&nbsp;<input disabled="true" type="checkbox" checked="checked">Social Studies</td>
					</tr>
					<tr class="transparent">
						<td class="transparent-small">&nbsp;&nbsp;&nbsp;<input disabled="true" type="checkbox">Language Arts Reading</td>
					</tr>
					<tr class="transparent">
						<td class="transparent-small">&nbsp;&nbsp;&nbsp;<input disabled="true" type="checkbox" checked="checked">Math</td>
					</tr>

				</table>
				</td>
			</tr>

			<tr class="transparent">
				<td class="transparent " width="500"><b>Learner Results</b></td>

			</tr>

			<tr class="transparent">
				<td>
				<table>
					<tr>
						<td class="transparent-small">&nbsp;Work</td>
					</tr>
					<tr>
						<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="" value="Work" disabled="true">Got a Job
						</td>
					</tr>
					<tr>
						<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="" value="Work" disabled="true" checked="checked">Retained
						Job</td>
					</tr>
					<tr>
						<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="" value="Work" disabled="true">Earned
						Job Training</td>
					</tr>
					<tr>
						<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="" value="Work" disabled="true">Entered
						Apprenticeship</td>
					</tr>
					<tr>
						<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="" value="Work" disabled="true">Entered
						Military</td>
					</tr>
					<tr>
						<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="" value="Work" disabled="true">Acquired
						workforce readiness skills</td>
					</tr>
					<tr>
						<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="" value="Work" disabled="true" checked="checked">Reduced
						Public Assistance</td>

					</tr>
					<tr>
						<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="" value="Work" disabled="true">Other</td>
					</tr>
					<tr>
						<td class="transparent-small">&nbsp;Personal/Family</td>
					</tr>
					<tr>
						<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="" value="Work" disabled="true">Increased
						Involvement in Children’s Education</td>
					</tr>
					<tr>
						<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="" value="Work" disabled="true">Increased
						Involvement in children’s literacy related activities</td>

					</tr>
					<tr>
						<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="" value="Work" disabled="true">Met Other
						Family Goal</td>
					</tr>
					<tr>
						<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="" value="Work" disabled="true">Other</td>
					</tr>


					<tr>
						<td class="transparent-small">&nbsp;Community</td>
					</tr>
					<tr>
						<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="" value="Work" disabled="true">Achieved
						U.S. Citizenship</td>
					</tr>
					<tr>
						<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="" value="Work" disabled="true" checked="checked">Registered
						to vote or voted for the first time</td>

					</tr>
					<tr>
						<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="" value="Work" disabled="true">Increased
						involvement in community activities</td>
					</tr>
					<tr>
						<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="" value="Work" disabled="true">Other</td>
					</tr>
					<tr>
						<td class="transparent-small">&nbsp;Education</td>
					</tr>
					<tr>
						<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="" value="Work" disabled="true" checked="checked">Returned
						to K-12</td>
					</tr>
					<tr>
						<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="" value="Work" disabled="true">Passed
						GED</td>

					</tr>
					<tr>
						<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="" value="Work" disabled="true">Earned
						certificate</td>
					</tr>
					<tr>
						<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="" value="Work" disabled="true">Earned
						High School Diploma</td>
					</tr>
					<tr>
						<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="" value="Work" disabled="true">Entered
						college</td>

					</tr>
					<tr>
						<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="" value="Work" disabled="true">Entered
						Training Program</td>
					</tr>
					<tr>
						<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="" value="Work" disabled="true">Gained
						computer or tech skills</td>
					</tr>
					<tr>
						<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="" value="Work" disabled="true" checked="checked">Mastered
						Course Competencies/education plan</td>
					</tr>

					<tr>
						<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="" value="Work" disabled="true">Other</td>
					</tr>
				</table>
				</td>
			</tr>


		</table>


		</td>
	</tr>


</table>

<br />
