<%@ page import="java.io.*, java.util.*"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="dto.PathNode"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>

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
<td class="transparent-top" width="40%">

<table class="transparent">
    <tr class="transparent">
        <td class="transparent alignRight" width="250"><span class="asterisk">*</span>&nbsp;<netui:content value="First Name:"/></td>
               
         <!-- Added tagId to resolve javascript isssue occured in mozilla  for webLogic 10.3-->       
          <td class="transparent"><input type="text"  maxlength="32" style="width:180px" value="Test"/>
         </td>
    </tr>
      <tr class="transparent">
        <td class="transparent alignRight" width="250"><netui:content value="Middle Name:"/></td>
                        
          <td class="transparent"><input type="text"  maxlength="32" style="width:180px"/>
         </td>
    </tr>
    <tr class="transparent">
         <td class="transparent alignRight" width="250"><span class="asterisk">*</span>&nbsp;<netui:content value="Last Name:"/></td>
         <td class="transparent"><input type="text"  maxlength="32" style="width:180px" value="Stu"/>
    </tr>
    <tr class="transparent">
        <td class="transparent alignRight" width="250">&nbsp;<netui:content value="Login ID:"/>
        <td class="transparent"><netui:label value="Test-Stu-0205"/>
    </tr>
    <tr class="transparent">
        <td class="transparent alignRight" width="250"><span class="asterisk">*</span>&nbsp;<netui:content value="Instructor First Name:"/></td>
            <!-- Added tagId to resolve javascript isssue occured in mozilla  for webLogic 10.3-->       
            <td class="transparent"><input type="text"  maxlength="32" style="width:180px" value="Test"/>
        </td>
    </tr>
    
       <tr class="transparent">
        <td class="transparent alignRight" width="250"><span class="asterisk">*</span>&nbsp;<netui:content value="Instructor Last Name:"/></td>
             <!-- Added tagId to resolve javascript isssue occured in mozilla  for webLogic 10.3-->       
            <td class="transparent"><input type="text"  maxlength="32" style="width:180px" value="Instructor"/>
       </td>
    </tr>
   
   
     <tr class="transparent">
        <td class="transparent alignRight" width="250" nowrap="nowrap"><span class="asterisk">*</span>&nbsp;<netui:content value="Student ID or Social Security Number:"/></td>
         <td class="transparent"> <netui:label value="STU0208"/></td>
    </tr>
    <tr class="transparent">
        <td class="transparent alignRight" width="250"><span class="asterisk">*</span>&nbsp;<netui:content value="Hours of Instruction:"/></td>
       <td class="transparent"><input type="text"  maxlength="32" style="width:180px"/></td>
     </tr>
</table>
</td>
  
<!-- OrgNode PathList -->
<td class="transparent-top" width="60%">
<table class="transparent">
 <tr class="transparent">
   <td class="transparent alignRight" width="200"><span class="asterisk">*</span>&nbsp;<netui:content value="Address Line 1:"/></td>
        <td class="transparent"><input type="text"  maxlength="64" style="width:205px" value="4 Parametta Springs"/></td>                    
    </tr>
      <tr class="transparent">
        <td class="transparent alignRight" width="200"><netui:content value="Address Line 2:"/></td>
        <td class="transparent"><input type="text" dataSource="actionForm.studentProfile.studentContact.addressLine2" maxlength="64" style="width:205px" value="California"/></td>
    </tr>
    <tr class="transparent" >
         <td class="transparent alignRight" width="200"><netui:content value="City:"/></td>
        <td class="transparent"><input type="text"  maxlength="64" style="width:205px" value="California"/></td>                           
    </tr>
     <tr class="transparent">
        <td class="transparent alignRight" width="200"><span class="asterisk">*</span>&nbsp;<netui:content value="State:"/></td>
        <td class="transparent">
        	<netui:select optionsDataSource="${pageFlow.stateOptions}" dataSource="actionForm.studentProfile.studentContact.state" size="1" style="width:200px" defaultValue="${actionForm.studentProfile.studentContact.state}"/>
        </td>                  
    </tr>
   
   <tr class="transparent">
        <td class="transparent alignRight" width="200"><span class="asterisk">*</span>&nbsp;<netui:content value="Zip Code:"/></td>
        <td class="transparent">
            <netui:textBox tagId="zipCode1" dataSource="actionForm.studentProfile.studentContact.zipCode1" maxlength="5" style="width:50px" onKeyPress="return constrainNumericChar(event);" onKeyUp="focusNextControl(this); " defaultValue="45632"/>
            -
            <netui:textBox tagId="zipCode2" dataSource="actionForm.studentProfile.studentContact.zipCode2" maxlength="5" style="width:50px" onKeyPress="return constrainNumericChar(event);" defaultValue="69856"/>
        </td>
    </tr>
    <tr class="transparent">
        <td class="transparent alignRight" width="200"><span class="asterisk">*</span>&nbsp;<netui:content value="Primary Phone:"/></td>
        <td class="transparent">
            <netui:textBox tagId="primaryPhone1" dataSource="actionForm.studentProfile.studentContact.primaryPhone1" maxlength="3" onKeyPress="return constrainNumericChar(event);" style="width:40px" onKeyUp="focusNextControl(this);" defaultValue="001"/>
            -
            <netui:textBox tagId="primaryPhone2" dataSource="actionForm.studentProfile.studentContact.primaryPhone1"  maxlength="3" onKeyPress="return constrainNumericChar(event);" style="width:40px" onKeyUp="focusNextControl(this); " defaultValue="831"/>
            -
            <netui:textBox tagId="primaryPhone3" dataSource="actionForm.studentProfile.studentContact.primaryPhone1"  maxlength="4"onKeyPress="return constrainNumericChar(event);" style="width:40px" onKeyUp="focusNextControl(this); " defaultValue="321"/>
            Ext:
            <netui:textBox tagId="primaryPhone4" dataSource="actionForm.studentProfile.studentContact.primaryPhone1" maxlength="4" onKeyPress="return constrainNumericChar(event);" style="width:40px" onKeyUp="focusNextControl(this); " defaultValue="4326"/>
        </td>
    </tr>
    <tr class="transparent">
        <td class="transparent alignRight" width="200"><netui:content value="Secondary Phone:"/></td>
        <td class="transparent">
            <netui:textBox tagId="secondaryPhone1" dataSource="actionForm.studentProfile.studentContact.secondaryPhone1" maxlength="3" onKeyPress="return constrainNumericChar(event);" style="width:40px" onKeyUp="focusNextControl(this); "/>
            -
            <netui:textBox tagId="secondaryPhone2" dataSource="actionForm.studentProfile.studentContact.secondaryPhone2" maxlength="3" onKeyPress="return constrainNumericChar(event);" style="width:40px" onKeyUp="focusNextControl(this); "/>
            -
            <netui:textBox tagId="secondaryPhone3" dataSource="actionForm.studentProfile.studentContact.secondaryPhone3" maxlength="4" onKeyPress="return constrainNumericChar(event);" style="width:40px" onKeyUp="focusNextControl(this); "/>
            Ext:
            <netui:textBox tagId="secondaryPhone4" dataSource="actionForm.studentProfile.studentContact.secondaryPhone4" maxlength="4" onKeyPress="return constrainNumericChar(event);" style="width:40px" onKeyUp="focusNextControl(this); "/>
        </td>
    </tr>
    <tr class="transparent">
        <td class="transparent alignRight" width="200"><netui:content value="Email:"/></td>
        <td class="transparent">
        <input type="text"  maxlength="64" style="width:205px" value=""/>
      </td>
    </tr>
    
    
</table>
</td>
</tr>
</table>

<br/>
