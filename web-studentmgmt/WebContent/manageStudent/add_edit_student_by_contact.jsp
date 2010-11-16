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
        
        
<!-- column 1 -->
<td class="transparent-top" width="50%" valign="top">
<table class="transparent alignRight" >
        <tr class="transparent">
        <td class="transparent alignRight" width="110"><span class="asterisk">*</span>&nbsp;<netui:content value="Address Line 1:"/></td>
        <td class="transparent"><netui:textBox tagId="addressLine1" dataSource="actionForm.studentProfile.studentContact.addressLine1"  maxlength="64" style="width:200px"/></td>
    </tr>
    <tr class="transparent">
        <td class="transparent alignRight" width="110"><netui:content value="Address Line 2:"/></td>
        <td class="transparent"><netui:textBox tagId="addressLine2" dataSource="actionForm.studentProfile.studentContact.addressLine2" maxlength="64" style="width:200px"/></td>
    </tr>
    <tr class="transparent">
        <td class="transparent alignRight" width="110"><netui:content value="City:"/></td>
        <td class="transparent"><netui:textBox  tagId="city" dataSource="actionForm.studentProfile.studentContact.city"  maxlength="64" style="width:200px"/></td>
    </tr>
    <tr class="transparent">
        <td class="transparent alignRight" width="110"><span class="asterisk">*</span>&nbsp;<netui:content value="State:"/></td>
        <td class="transparent">
        	<netui:select optionsDataSource="${pageFlow.stateOptions}" dataSource="actionForm.studentProfile.studentContact.state" size="1" style="width:200px" defaultValue="${actionForm.studentProfile.studentContact.state}"/>
        </td>                  
    </tr>
  
</table>
</td>


<!-- column 2 -->
<td class="transparent-top" width="50%" valign="top">
<table class="transparent">
   <tr class="transparent">
        <td class="transparent alignRight" width="110"><span class="asterisk">*</span>&nbsp;<netui:content value="Zip Code:"/></td>
        <td class="transparent">
            <netui:textBox tagId="zipCode1" dataSource="actionForm.studentProfile.studentContact.zipCode1" maxlength="5" style="width:50px" onKeyPress="return constrainNumericChar(event);" onKeyUp="focusNextControl(this); "/>
            -
            <netui:textBox tagId="zipCode2" dataSource="actionForm.studentProfile.studentContact.zipCode2" maxlength="5" style="width:50px" onKeyPress="return constrainNumericChar(event);"/>
        </td>
    </tr>
     <tr class="transparent">
        <td class="transparent alignRight" width="110"><span class="asterisk">*</span>&nbsp;<netui:content value="Primary Phone:"/></td>
        <td class="transparent">
            <netui:textBox tagId="primaryPhone1" dataSource="actionForm.studentProfile.studentContact.primaryPhone1" maxlength="3" onKeyPress="return constrainNumericChar(event);" style="width:40px" onKeyUp="focusNextControl(this); "/>
            -
            <netui:textBox tagId="primaryPhone2" dataSource="actionForm.studentProfile.studentContact.primaryPhone2"  maxlength="3" onKeyPress="return constrainNumericChar(event);" style="width:40px" onKeyUp="focusNextControl(this); "/>
            -
            <netui:textBox tagId="primaryPhone3" dataSource="actionForm.studentProfile.studentContact.primaryPhone3"  maxlength="4"onKeyPress="return constrainNumericChar(event);" style="width:40px" onKeyUp="focusNextControl(this); "/>
            Ext:
            <netui:textBox tagId="primaryPhone4" dataSource="actionForm.studentProfile.studentContact.primaryPhone4" maxlength="4" onKeyPress="return constrainNumericChar(event);" style="width:40px" onKeyUp="focusNextControl(this); "/>
        </td>
    </tr>
    <tr class="transparent">
        <td class="transparent alignRight" width="110"><netui:content value="Secondary Phone:"/></td>
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
        <td class="transparent alignRight" width="110"><netui:content value="Email:"/></td>
        <td class="transparent">
        <netui:textBox tagId="email" datasource="actionForm.studentProfile.studentContact.email" maxlength="64" style="width:200px" />
      </td>
    </tr>
</table>
</td>

</tr>
</table>

