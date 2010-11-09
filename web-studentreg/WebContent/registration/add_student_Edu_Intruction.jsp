<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="dto.PathNode"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>


<table class="simple">
	<tr class="transparent">
       	<td class="transparent" width="300">&nbsp;</td>
    </tr> 
    <tr class="transparent">
        
        
<!-- column 1 -->
		<td class="transparent-top" width="50%" valign="top">
		<table class="transparent">
			<tr class="transparent">
				<td class="transparent" width="210"><span class="asterisk">*</span>&nbsp;<b>Highest Year of School Completed</b></td>

			</tr>
			<tr class="transparent">
				<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <select style="width: 280px">
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
		       	<td class="transparent" width="300">&nbsp;</td>
		    </tr>  
			<tr class="transparent">
				<td class="transparent " width="210"><span class="asterisk">*</span>&nbsp;<b>Highest Diploma or Degree Earned </b></td>

			</tr>
			<tr class="transparent">
				<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <select style="width: 280px">
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
		       	<td class="transparent" width="300">&nbsp;</td>
		    </tr>  
			
			<tr class="transparent">
				<td class="transparent " width="210"><span class="asterisk">*</span>&nbsp;<b>Earned the above outside the U.S. </b></td>

			</tr>
			<tr class="transparent">
				<td class="transparent-small" width="280">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" value="Yes" checked="true">&nbsp;&nbsp;&nbsp;Yes</input>
				
			</tr>
			<tr class="transparent">
				<td class="transparent-small" width="280">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" value="No" />&nbsp;&nbsp;&nbsp;No</input></td>
	
			</tr>
		
			<tr class="transparent">
		       	<td class="transparent" width="300">&nbsp;</td>
		    </tr>  

			<tr class="transparent">
				<td class="transparent " width="210"><span class="asterisk">*</span>&nbsp;<b>Class Number </b></td>

			</tr>
			<tr class="transparent">
				<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" maxlength="64" style="width: 280px" /></td>
			</tr>

			<tr class="transparent">
		       	<td class="transparent" width="300">&nbsp;</td>
		    </tr>  

			<tr class="transparent">
				<td class="transparent " width="210"><span class="asterisk">*</span>&nbsp;<b>Date of Entry into this Class </b></td>

			</tr>
			<tr class="transparent">
				<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<netui:select optionsDataSource="${pageFlow.monthOptions}" dataSource="actionForm.studentProfile.month" size="1" style="width:60px" />
				 <netui:select	optionsDataSource="${pageFlow.dayOptions}" dataSource="actionForm.studentProfile.day" size="1" style="width:45px" />
				<netui:select optionsDataSource="${pageFlow.yearOptions}" dataSource="actionForm.studentProfile.year" size="1" style="width:68px" /></td>

			</tr>
			
			<tr class="transparent">
		       	<td class="transparent" width="300">&nbsp;</td>
		    </tr>  
			
			<tr class="transparent">
				<td class="transparent " width="210"><span class="asterisk">*</span>&nbsp;<b>Instructional Level </b></td>

			</tr>
			<tr class="transparent">
				<td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<select style="width: 280px">
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
		</table>
		</td>
		<!-- column 2 -->

<td class="transparent-top" width="50%" valign="top">
<table class="transparent">
    <tr class="transparent">
        <td class="transparent-small" width="210"><span class="asterisk">*</span>&nbsp;<b>Instructional Program </b></td>
    </tr>
    <tr class="transparent">
   
    	<td class="transparent" >   	
        	<table class="transparent">
        	
            <tr class="transparent">
	        	 <td class="transparent-small">
             <input type="checkbox" name="" id = "ABE" value ="ABE">ABE</input>
             </td>
            </tr>
             <tr class="transparent">
	        	 <td class="transparent-small">
             <input type="checkbox" name="" id = "ESL" value ="ESL">ESL</input>
             </td>
            </tr>
             <tr class="transparent">
	        	 <td class="transparent-small">
             <input type="checkbox" name="" id = "ESL/Citizenship" value ="ESL/Citizenship">ESL/Citizenship</input>
             </td>
            </tr>
             <tr class="transparent">
	        	 <td class="transparent-small">
             <input type="checkbox" name="" id = "High School Diploma" value ="High School Diploma">High School Diploma</input>
             </td>
            </tr>
             <tr class="transparent">
	        	 <td class="transparent-small">
             <input type="checkbox" name="" id = "GED" value ="GED">GED</input>
             </td>
            </tr>
             <tr class="transparent">
	        	 <td class="transparent-small">
             <input type="checkbox" name="" id = "Spanish GED" value ="Spanish GED">Spanish GED</input>
             </td>
            </tr>
             <tr class="transparent">
	        	 <td class="transparent-small">
             <input type="checkbox" name="" id = "Voc./Occupational skills" value ="Voc./Occupational skills">Voc./Occupational skills</input>
             </td>
            </tr>
             <tr class="transparent">
	        	 <td class="transparent-small">
             <input type="checkbox" name="" id = "Workforce Readiness" value ="Workforce Readiness">Workforce Readiness</input>
             </td>
            </tr>
             <tr class="transparent">
	        	 <td class="transparent-small">
             <input type="checkbox" name="" id = "Adults with Disabilities" value ="Adults with Disabilities">Adults with Disabilities</input>
             </td>
            </tr>
             <tr class="transparent">
	        	 <td class="transparent-small">
             <input type="checkbox" name="" id = "Health & Safety" value ="Health & Safety">Health & Safety</input>
             </td>
            </tr>
             <tr class="transparent">
	        	 <td class="transparent-small">
             <input type="checkbox" name="" id = "Home Economics" value ="Home Economics">Home Economics</input>
             </td>
            </tr>
             <tr class="transparent">
	        	 <td class="transparent-small">
             <input type="checkbox" name="" id = "Parent Education" value ="Parent Education">Parent Education</input>
             </td>
            </tr>
            <tr class="transparent">
	        	 <td class="transparent-small">
             <input type="checkbox" name="" id = "Other Adults" value ="Other Adults">Other Adults</input>
             </td>
            </tr>
            <tr class="transparent">
	        	 <td class="transparent-small">
             <input type="checkbox" name="" id = "Other" value ="Other">Other</input>
             </td>
            </tr>
        </table>        
        </td>
    </tr>
    <tr class="transparent">
      	<td class="transparent" width="300">&nbsp;</td>
   </tr>
    <tr class="transparent">
				<td class="transparent " width="100"><span class="asterisk">*</span>&nbsp;<b>Skill Level</b></td>

			</tr>
			<tr class="transparent">
			<td>
				<table>
					<tr>
						<td class="transparent-small" align="right">Technology:</td>
						<td><select>
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
						<td class="transparent-small" align="right">Speaking:</td>
						<td><select>
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
						<td class="transparent-small" align="right">Reading:</td>

						<td><select style="float: right;">
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
						<td class="transparent-small" align="right">Writing:</td>
						<td><select style="float: right;">
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
						<td class="transparent-small" align="right">Math:</td>
						<td><select style="float: right;">
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
		  </tr>
        </table>
    </td>
	<tr class="transparent">
       	<td class="transparent" width="300">&nbsp;</td>
    </tr>
  </tr>
</table>

