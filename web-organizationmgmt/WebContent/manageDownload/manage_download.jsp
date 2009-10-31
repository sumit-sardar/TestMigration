<%@ page import="java.io.*, java.util.*"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="netui-tags-databinding.tld" prefix="netui-compat-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui-compat"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-compat-template"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
 
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>
<netui-data:declareBundle bundlePath="helpResources" name="help"/>

<netui-template:template templatePage="/resources/jsp/template.jsp">
<netui-template:setAttribute name="title" value="${bundle.web['download.window.title']}"/>
<netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.downloadData']}"/>
<netui-template:section name="bodySection">


<%
    String userName = (String)request.getSession().getAttribute("userName"); 
    String userFile = userName + "_User.xls";  
    String studentFile = userName + "_Student.xls";
%>


<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->

<h1><netui:content value="Download Data"/></h1>

<p>
    <netui:content value="Select either user or student profile data to save on your computer in spreadsheet format."/><br/>
</p>


<!-- message -->
<div id="message" style="display:none">
    <ctb:message title="Download User Data" style="informationMessage" >
          <netui:content value="download completed"/>
    </ctb:message>
</div>


<!-- start form -->
<netui:form action="manageDownload">

<jsp:include page="/manageDownload/show_message.jsp" />

<p>
<table class="simple">

    <tr class="simple">
        <td class="sortableControls" colspan="3" height="30">
            &nbsp;<netui:button tagId="download" type="submit" value="Download" action="downloadData" disabled="true"/>
        </td>        
    </tr>
    
    <tr class="sortable">
        <th class="sortable alignCenter" height="25" nowrap width="50">Select</th>                
        <th class="sortable alignLeft" height="25" nowrap>&nbsp;&nbsp;Name</th>
        <th class="sortable alignLeft" height="25" nowrap>&nbsp;&nbsp;Description</th>
    </tr>    
    <tr class="sortable">
        <td class="sortable alignCenter">
            <netui:radioButtonGroup dataSource="actionForm.fileName">
                &nbsp;<netui:radioButtonOption value="<%=userFile%>" onClick="return enableDownloadData();">&nbsp;</netui:radioButtonOption>                
            </netui:radioButtonGroup>        
        </td>
        <td class="sortable">
            User Data
        </td>        
        <td class="sortable">
            Users in your organization located at or below your organizational assignment.
        </td>        
    </tr>
    <tr class="sortable">
        <td class="sortable alignCenter">
            <netui:radioButtonGroup dataSource="actionForm.fileName">
                &nbsp;<netui:radioButtonOption value="<%=studentFile%>" onClick="return enableDownloadData();">&nbsp;</netui:radioButtonOption>                
            </netui:radioButtonGroup>        
        </td>
        <td class="sortable">
            Student Data
        </td>        
        <td class="sortable">
            Students in your organization associated with your user login.
        </td>        
    </tr>

</table>
</p>

</netui:form>

<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
</netui-template:section>
</netui-template:template>
