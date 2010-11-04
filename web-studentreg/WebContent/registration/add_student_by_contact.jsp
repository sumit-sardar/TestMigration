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
        <td class="transparent"><input type="text"  maxlength="64" style="width:200px"/></td>
    </tr>
    <tr class="transparent">
        <td class="transparent alignRight" width="110"><netui:content value="Address Line 2:"/></td>
        <td class="transparent"><input type="text"  maxlength="64" style="width:200px"/></td>
    </tr>
    <tr class="transparent">
        <td class="transparent alignRight" width="110"><netui:content value="City:"/></td>
        <td class="transparent"><input type="text"  maxlength="64" style="width:200px"/></td>
    </tr>
    <tr class="transparent">
        <td class="transparent alignRight" width="110"><span class="asterisk">*</span>&nbsp;<netui:content value="State:"/></td>
        <td class="transparent">
            <select style="width:200px" >
	             <Option selected="true">Please Select</Option>
	             <Option>Alabama</Option>
	 			 <Option>Alaska</Option>
	 			 <Option>Arizona</Option>
	 			 <Option>Arkansas</Option>
	 			</select>
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
            <input type="text" maxlength="5" onKeyPress="return constrainNumericChar(event);" style="width:50px" onKeyUp="focusNextControl(this); "/>
            -
            <input type="text" maxlength="5" onKeyPress="return constrainNumericChar(event);" style="width:50px" onKeyUp="focusNextControl(this); "/>
        </td>
    </tr>
     <tr class="transparent">
        <td class="transparent alignRight" width="110"><span class="asterisk">*</span>&nbsp;<netui:content value="Primary Phone:"/></td>
        <td class="transparent">
            <input type="text"  maxlength="3" onKeyPress="return constrainNumericChar(event);" style="width:40px" onKeyUp="focusNextControl(this); "/>
            -
            <input type="text" maxlength="3" onKeyPress="return constrainNumericChar(event);" style="width:40px" onKeyUp="focusNextControl(this); "/>
            -
            <input type="text" maxlength="4" onKeyPress="return constrainNumericChar(event);" style="width:40px" onKeyUp="focusNextControl(this); "/>
            Ext:
            <input type="text" maxlength="4" onKeyPress="return constrainNumericChar(event);" style="width:40px" onKeyUp="focusNextControl(this); "/>
        </td>
    </tr>
    <tr class="transparent">
        <td class="transparent alignRight" width="110"><netui:content value="Secondary Phone:"/></td>
        <td class="transparent">
            <input type="text" maxlength="3" onKeyPress="return constrainNumericChar(event);" style="width:40px" onKeyUp="focusNextControl(this); "/>
            -
            <input type="text" maxlength="3" onKeyPress="return constrainNumericChar(event);" style="width:40px" onKeyUp="focusNextControl(this); "/>
            -
            <input type="text" maxlength="4" onKeyPress="return constrainNumericChar(event);" style="width:40px" onKeyUp="focusNextControl(this); "/>
            Ext:
            <input type="text" maxlength="4" onKeyPress="return constrainNumericChar(event);" style="width:40px" onKeyUp="focusNextControl(this); "/>
        </td>
    </tr>
</table>
</td>

</tr>
</table>

