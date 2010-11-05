<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="dto.PathNode"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<netui-data:declareBundle bundlePath="oasResources" name="oas" />
<netui-data:declareBundle bundlePath="webResources" name="web" />
<netui-data:declareBundle bundlePath="widgetResources" name="widgets" />


<table class="simple">
	<tr class="transparent">


		<!-- column 1 -->
		<td class="transparent-top" width="50%" valign="top">
		<table class="transparent">
			<tr class="transparent">
				<td class="transparent" width="210"><span class="asterisk">*</span>&nbsp;<b>Highest Year Of School
				Completed</b></td>

			</tr>
			<tr class="transparent">
				<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <select style="width: 280px" disabled="true">
					<Option selected="true">None</Option>
					<Option>6</Option>
					<Option>7</Option>
					<Option>8</Option>
					<Option>9</Option>
					<Option>10</Option>
					<Option>11</Option>
					<Option>12</Option>
				</select></td>
			</tr>

			<tr class="transparent">
				<td class="transparent " width="210"><span class="asterisk">*</span>&nbsp;<b>Highest Diploma Or Degree
				Earned </b></td>

			</tr>
			<tr class="transparent">
				<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <select style="width: 280px" disabled="true">
					<Option selected="true">None</Option>
					<Option>GED Certificate</Option>
					<Option>High School Diploma</Option>
					<Option>Technical/Certificate</Option>
					<Option>AA/AS Degree</Option>
					<Option>4 yr College Graduate</Option>
					<Option>Graduate Student</Option>
					<Option>Other</Option>
				</select></td>
			</tr>

			<tr class="transparent">
				<td class="transparent" width="210"><span class="asterisk">*</span>&nbsp;<b>Earned the above outside the
				U.S. </b></td>

			</tr>
			<tr class="transparent">
				<td class="transparent-small" width="280">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <input type="radio" value="Yes" disabled="true">&nbsp;&nbsp;&nbsp;Yes

				</td>
			</tr>
			<tr class="transparent">
				<td class="transparent-small" width="280">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <input type="radio" value="No"
					checked="true" disabled="true" /> &nbsp;&nbsp;&nbsp;No</td>

			</tr>


			<tr class="transparent">
				<td class="transparent" width="210"><span class="asterisk">*</span>&nbsp;<b>Class Number </b></td>

			</tr>
			<tr class="transparent">
				<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <input type="text" maxlength="64"
					style="width: 280px" disabled="true" /></td>
			</tr>

			<tr class="transparent">
				<td class="transparent" width="210"><span class="asterisk">*</span>&nbsp;<b>Date of Entry into this Class
				</b></td>

			</tr>
			<tr class="transparent">
				<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <netui:select disabled="true"
					optionsDataSource="${pageFlow.monthOptions}" dataSource="actionForm.studentProfile.month" size="1"
					style="width:60px" /> <netui:select optionsDataSource="${pageFlow.dayOptions}" disabled="true"
					dataSource="actionForm.studentProfile.day" size="1" style="width:45px" /> <netui:select disabled="true"
					optionsDataSource="${pageFlow.yearOptions}" dataSource="actionForm.studentProfile.year" size="1" style="width:68px" />
				</td>

			</tr>
			<tr class="transparent">
				<td class="transparent " width="210"><span class="asterisk">*</span>&nbsp;<b>Instructional Level </b></td>

			</tr>
			<tr class="transparent">
				<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <select style="width: 280px" disabled="true">
					<Option selected="true">Please Select</Option>
					<Option>ESL Beginning Literacy</Option>
					<Option>ESL Beginning</Option>
					<Option>ESL Intermediate Low</Option>
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

			<tr class="transparent">
				<td class="transparent " width="150"><span class="asterisk">*</span>&nbsp;<b>Skill Level</b></td>

			</tr>
			<tr class="transparent">
			<td>
				<table>
					<tr>
						<td class="transparent-small" width="100%">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Technology</td>
						<td><select disabled="true">
							<Option>Please Select</Option>
							<Option>0</Option>
							<Option>1</Option>
							<Option>2</Option>
							<Option>3</Option>
							<Option>4</Option>
							<Option>5</Option>
							<Option>6</Option>
							<Option>7</Option>
							<Option>8</Option>
							<Option>9</Option>
						</select></td>
					</tr>
					<tr>
						<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Speaking</td>
						<td><select disabled="true">
							<Option>Please Select</Option>
							<Option>0</Option>
							<Option>1</Option>
							<Option>2</Option>
							<Option>3</Option>
							<Option>4</Option>
							<Option>5</Option>
							<Option>6</Option>
							<Option>7</Option>
							<Option>8</Option>
							<Option>9</Option>
						</select></td>

					</tr>
					<tr class="transparent">
						<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Reading</td>

						<td><select style="float: right;" disabled="true">
							<Option>Please Select</Option>
							<Option>0</Option>
							<Option>1</Option>
							<Option>2</Option>
							<Option>3</Option>
							<Option>4</Option>
							<Option>5</Option>
							<Option>6</Option>
							<Option>7</Option>
							<Option>8</Option>
							<Option>9</Option>
						</select></td>

					</tr>
					<tr class="transparent">
						<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Writing</td>
						<td><select style="float: right;" disabled="true">
							<Option>Please Select</Option>
							<Option>0</Option>
							<Option>1</Option>
							<Option>2</Option>
							<Option>3</Option>
							<Option>4</Option>
							<Option>5</Option>
							<Option>6</Option>
							<Option>7</Option>
							<Option>8</Option>
							<Option>9</Option>
						</select></td>

					</tr>
					<tr class="transparent">
						<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Math</td>
						<td><select style="float: right;" disabled="true">
							<Option>Please Select</Option>
							<Option>0</Option>
							<Option>1</Option>
							<Option>2</Option>
							<Option>3</Option>
							<Option>4</Option>
							<Option>5</Option>
							<Option>6</Option>
							<Option>7</Option>
							<Option>8</Option>
							<Option>9</Option>
						</select></td>
					</tr>
				</table>
			</td>
		</table>
		</td>

		<!-- column 2 -->

		<td class="transparent-top" width="50%" valign="top">
		<table class="transparent">
			<tr class="transparent">
				<td class="transparent-small" width="210"><span class="asterisk">*</span>&nbsp;<b>Instructional Program </b></td>
			</tr>
			<tr class="transparent">

				<td class="transparent">
				<table class="transparent">

					<tr class="transparent">
						<td class="transparent-small"><input type="checkbox" name="" id="ABE" value="ABE" checked="false" disabled="true">ABE
						</td>
					</tr>
					<tr class="transparent">
						<td class="transparent-small"><input type="checkbox" name="" id="ESL" value="ESL" checked="false" disabled="true">ESL
						</td>
					</tr>
					<tr class="transparent">
						<td class="transparent-small"><input type="checkbox" name="" id="ESL/Citizenship" value="ESL/Citizenship"
							checked="false" disabled="true">ESL/Citizenship</td>
					</tr>
					<tr class="transparent">
						<td class="transparent-small"><input type="checkbox" name="" id="High School Diploma"
							value="High School Diploma" checked="false" disabled="true">High School Diploma</td>
					</tr>
					<tr class="transparent">
						<td class="transparent-small"><input type="checkbox" name="" id="GED" value="GED" checked="false" disabled="true">GED
						</td>
					</tr>
					<tr class="transparent">
						<td class="transparent-small"><input type="checkbox" name="" id="Spanish GED" value="Spanish GED"
							checked="true" disabled="true">Spanish GED</td>
					</tr>
					<tr class="transparent">
						<td class="transparent-small"><input type="checkbox" name="" id="Voc./Occupational skills"
							value="Voc./Occupational skills" checked="true" disabled="true">Voc./Occupational skills</td>
					</tr>
					<tr class="transparent">
						<td class="transparent-small"><input type="checkbox" name="" id="Workforce Readiness"
							value="Workforce Readiness" checked="false" disabled="true">Workforce Readiness</td>
					</tr>
					<tr class="transparent">
						<td class="transparent-small"><input type="checkbox" name="" id="Adults with Disabilities"
							value="Adults with Disabilities" checked="false" disabled="true">Adults with Disabilities</td>
					</tr>
					<tr class="transparent">
						<td class="transparent-small"><input type="checkbox" name="" id="Health & Safety" value="Health & Safety"
							checked="false" disabled="true">Health & Safety</td>
					</tr>
					<tr class="transparent">
						<td class="transparent-small"><input type="checkbox" name="" id="Home Economics" value="Home Economics"
							checked="false" disabled="true">Home Economics</td>
					</tr>
					<tr class="transparent">
						<td class="transparent-small"><input type="checkbox" name="" id="Parent Education" value="Parent Education"
							checked="false" disabled="true">Parent Education</td>
					</tr>
					<tr class="transparent">
						<td class="transparent-small"><input type="checkbox" name="" id="Other Adults" value="Other Adults"
							checked="false" disabled="true">Other Adults</td>
					</tr>
					<tr class="transparent">
						<td class="transparent-small"><input type="checkbox" name="" id="Other" value="Other" checked="false" disabled="true">Other
						</td>
					</tr>
				</table>
				</td>
			</tr>


		</table>
		</td>

	</tr>
</table>

