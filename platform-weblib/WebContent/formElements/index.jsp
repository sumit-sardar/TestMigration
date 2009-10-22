<%@ page import="java.io.*, java.util.*"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="java.security.Principal"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>

<netui-template:template templatePage="/resources/jsp/template.jsp">
    <netui-template:setAttribute name="title" value="Form Elements"/>
    <netui-template:section name="bodySection">
    
    <h1><netui:span value="Form Elements"/></h1>
    <p>
    This page demonstrates the use of form elements with the styles specifed within
    the <a href="<%=request.getContextPath()%>/resources/css/widgets.css" target="_blank">widgets.css</a>
    file.  Both enabled and disabled views of the form element are provided below.
    </p>
    
    <netui:form action="begin">
    <table class="simple">
    <tr class="simple">
        <th class="simple alignLeft"><netui:span value="Element" styleClass="text"/></th>
        <th class="simple"><netui:span value="Enabled" styleClass="text"/></th>
        <th class="simple"><netui:span value="Disabled" styleClass="text"/></th>
    </tr>
    <tr class="simple">
        <td class="simple"><netui:span value="Text:" styleClass="text"/></td>
        <td class="simple"><netui:span value="Normal Text" styleClass="text"/></td>
        <td class="simple"><netui:span value="Normal Text" styleClass="text"/></td>
    </tr>
    <tr class="simple">
        <td class="simple"><netui:span value="Form Value:" styleClass="text"/></td>
        <td class="simple"><netui:span value="Example Text"/></td>
        <td class="simple"><div class="formValue"><netui:span value="Example Text" styleClass="formValue"/></div></td>
    </tr>
    <tr class="simple">
        <td class="simple"><netui:span value="Text Field:" styleClass="text"/></td>
        <td class="simple"><netui:textBox dataSource="actionForm.textFieldValue" styleClass="textField"/></td>
        <td class="simple"><netui:textBox dataSource="actionForm.textFieldDisabledValue" disabled="true" styleClass="textField"/></td>
    </tr>
    <tr class="simple">
        <td class="simple"><span class="asterisk">*</span> <netui:span value="Text Field Required:" styleClass="text"/></td>
        <td class="simple"><netui:textBox dataSource="actionForm.textFieldValue" styleClass="textField"/></td>
        <td class="simple"><netui:textBox dataSource="actionForm.textFieldDisabledValue" disabled="true" styleClass="textField"/></td>
    </tr>
    <tr class="simple">
        <td class="simple"><netui:span value="Password Field:" styleClass="text"/></td>
        <td class="simple"><netui:textBox password="true" dataSource="actionForm.passwordFieldValue" styleClass="passwordField"/></td>
        <td class="simple"><netui:textBox password="true" dataSource="actionForm.passwordFieldValue" disabled="true" styleClass="passwordField"/></td>
    </tr>
    <tr class="simple">
        <td class="simple"><netui:span value="Text Area:" styleClass="text"/></td>
        <td class="simple"><netui:textArea dataSource="actionForm.textAreaValue" cols="40" rows="4" styleClass="textArea"/></td>
        <td class="simple"><netui:textArea dataSource="actionForm.textAreaValue" cols="40" rows="4" styleClass="textArea" disabled="true"/></td>
    </tr>
    <tr class="simple">
        <td class="simple"><netui:span value="Radio Buttons (horizontal):" styleClass="text"/></td>
        <td class="simple">
            <netui:radioButtonGroup dataSource="actionForm.radioButtonHorizontalValue">
                <netui:radioButtonOption value="Red" labelStyleClass="radioButton"/>
                <netui:radioButtonOption value="Green" labelStyleClass="radioButton"/>
                <netui:radioButtonOption value="Blue" labelStyleClass="radioButton"/>
            </netui:radioButtonGroup>
        </td>
        <td class="simple">
            <netui:radioButtonGroup dataSource="actionForm.radioButtonHorizontalValue" disabled="true">
                <netui:radioButtonOption value="Red" labelStyleClass="radioButton"/>
                <netui:radioButtonOption value="Green" labelStyleClass="radioButton"/>
                <netui:radioButtonOption value="Blue" labelStyleClass="radioButton"/>
            </netui:radioButtonGroup>
        </td>
    </tr>
    <tr class="simple">
        <td class="simple"><netui:span value="Radio Buttons (vertical):" styleClass="text"/></td>
        <td class="simple">
            <netui:radioButtonGroup dataSource="actionForm.radioButtonVerticalValue">
                <netui:radioButtonOption value="Cake" labelStyleClass="radioButton"/><br/>
                <netui:radioButtonOption value="Ice Cream" labelStyleClass="radioButton"/><br/>
                <netui:radioButtonOption value="Both Cake and Ice Cream" labelStyleClass="radioButton"/><br/>
            </netui:radioButtonGroup>
        </td>
        <td class="simple">
            <netui:radioButtonGroup dataSource="actionForm.radioButtonVerticalValue" disabled="true">
                <netui:radioButtonOption value="Cake" labelStyleClass="radioButton"/><br/>
                <netui:radioButtonOption value="Ice Cream" labelStyleClass="radioButton"/><br/>
                <netui:radioButtonOption value="Both Cake and Ice Cream" labelStyleClass="radioButton"/><br/>
            </netui:radioButtonGroup>
        </td>
    </tr>
    <tr class="simple">
        <td class="simple"><netui:span value="Check Boxes (horizontal):" styleClass="text"/></td>
        <td class="simple">
            <netui:checkBoxGroup dataSource="actionForm.checkBoxHorizontalValues" labelStyleClass="checkBox">
                <netui:checkBoxOption value="XML" labelStyleClass="checkBox"/>
                <netui:checkBoxOption value="DHTML" labelStyleClass="checkBox"/>
                <netui:checkBoxOption value="XHTML" labelStyleClass="checkBox"/>
            </netui:checkBoxGroup>
        </td>
        <td class="simple">
            <netui:checkBoxGroup dataSource="actionForm.checkBoxHorizontalValues" labelStyleClass="checkBox" disabled="true">
                <netui:checkBoxOption value="XML" labelStyleClass="checkBox"/>
                <netui:checkBoxOption value="DHTML" labelStyleClass="checkBox"/>
                <netui:checkBoxOption value="XHTML" labelStyleClass="checkBox"/>
            </netui:checkBoxGroup>
        </td>
    </tr>
    <tr class="simple">
        <td class="simple"><netui:span value="Check Boxes (vertical):" styleClass="text"/></td>
        <td class="simple">
            <netui:checkBoxGroup dataSource="actionForm.checkBoxVerticalValues" labelStyleClass="checkBox">
                <netui:checkBoxOption value="Apples" labelStyleClass="checkBox"/><br/>
                <netui:checkBoxOption value="Grapes" labelStyleClass="checkBox"/><br/>
                <netui:checkBoxOption value="Oranges" labelStyleClass="checkBox"/><br/>
                <netui:checkBoxOption value="Plums" labelStyleClass="checkBox"/>
            </netui:checkBoxGroup>
        </td>
        <td class="simple">
            <netui:checkBoxGroup dataSource="actionForm.checkBoxVerticalValues" labelStyleClass="checkBox" disabled="true">
                <netui:checkBoxOption value="Apples" labelStyleClass="checkBox"/><br/>
                <netui:checkBoxOption value="Grapes" labelStyleClass="checkBox"/><br/>
                <netui:checkBoxOption value="Oranges" labelStyleClass="checkBox"/><br/>
                <netui:checkBoxOption value="Plums" labelStyleClass="checkBox"/>
            </netui:checkBoxGroup>
        </td>
    </tr>
    <tr class="simple">
        <td class="simple"><netui:span value="Select (single line):" styleClass="text"/></td>
        <td class="simple">
            <netui:select dataSource="actionForm.selectSingleOptionValues" styleClass="selectOption">
                <netui:selectOption value="Excellent"/>
                <netui:selectOption value="Above Average"/>
                <netui:selectOption value="Average"/>
                <netui:selectOption value="Below Average"/>
                <netui:selectOption value="Underwhelming"/>
            </netui:select>
        </td>
        <td class="simple">
            <netui:select dataSource="actionForm.selectSingleOptionValues" styleClass="selectOption" disabled="true">
                <netui:selectOption value="Excellent"/>
                <netui:selectOption value="Above Average"/>
                <netui:selectOption value="Average"/>
                <netui:selectOption value="Below Average"/>
                <netui:selectOption value="Underwhelming"/>
            </netui:select>
        </td>
    </tr>
    <tr class="simple">
        <td class="simple"><netui:span value="Select (multiple line):" styleClass="text"/></td>
        <td class="simple">
            <netui:select dataSource="actionForm.selectMultiOptionValues" multiple="true" size="4" styleClass="selectOption">
                <netui:selectOption value="Web Servers"/>
                <netui:selectOption value="Application Servers"/>
                <netui:selectOption value="Report Servers"/>
                <netui:selectOption value="Database Servers"/>
                <netui:selectOption value="File Servers"/>
            </netui:select>
        </td>
        <td class="simple">
            <netui:select dataSource="actionForm.selectMultiOptionValues" multiple="true" size="4" disabled="true" styleClass="selectOption">
                <netui:selectOption value="Web Servers"/>
                <netui:selectOption value="Application Servers"/>
                <netui:selectOption value="Report Servers"/>
                <netui:selectOption value="Database Servers"/>
                <netui:selectOption value="File Servers"/>
            </netui:select>
        </td>
    </tr>
    <tr class="simple">
        <td class="simple"><netui:span value="Button:" styleClass="text"/></td>
        <td class="simple"><netui:button type="button" value="Button" styleClass="button"/></td>
        <td class="simple"><netui:button type="button" value="Button" styleClass="button" disabled="true"/></td>
    </tr>
    <tr class="simple">
        <td class="simple"><netui:span value="Button (submit):" styleClass="text"/></td>
        <td class="simple"><netui:button type="submit" value="Submit" styleClass="button"/></td>
        <td class="simple"><netui:button type="submit" value="Submit" styleClass="button" disabled="true"/></td>
    </tr>
    <tr class="simple">
        <td class="simple"><netui:span value="Button (reset):" styleClass="text"/></td>
        <td class="simple"><netui:button type="reset" value="Reset" styleClass="button"/></td>
        <td class="simple"><netui:button type="reset" value="Reset" styleClass="button" disabled="true"/></td>
    </tr>
    <!--
    <tr class="simple">
        <td class="simple"><netui:span value="Button (auxiliary):" styleClass="text"/></td>
        <td class="simple"><netui:button type="button" value="Auxilliary" styleClass="auxButton"/></td>
        <td class="simple"><netui:button type="button" value="Auxilliary" styleClass="auxButton" disabled="true"/></td>
    </tr>
    -->
    </table>
    </netui:form>


    </netui-template:section>
</netui-template:template>


