<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 

<table class="simple">

    <tr class="simple">
        <td class="sortableControls" colspan="7" height="30">
            &nbsp;
        </td>        
    </tr>

    <tr class="sortable">
        <td class="sortable">
       
        
<div id="browseFile" style="display: block" >
<c:if test="${ requestScope.noFileSelected }">
    <ctb:message title="Upload Data" style="errorMessage" >
          <netui:content value="Please select a valid path and .xls data file to continue."/>
    </ctb:message>
</c:if>
<c:if test="${ requestScope.failToUpload }">
    <ctb:message title="Upload Data" style="errorMessage" >
          <netui:content value="Failed to upload this file. Please try again."/>
    </ctb:message>
</c:if>
            <br/>
            <netui:content value='Click "Browse" to select a data file and click "Upload" to import the data.'/><br/>
            <br/><br/>
            <netui:fileUpload tagId="inputbox" dataSource="actionForm.theFile" size="64" style="height:24" onKeyPress="return constrainEnterKeyEvent();" onChange="return enableUpload();" onKeyUp="return enableUpload();"/>
            &nbsp;
            
            <netui:button tagId="upload" type="submit" value="Upload" action="uploadData" onClick="return checkFileType();" disabled="true"/>
            <br/><br/>
</div>
 
        </td>        
    </tr>
</table>

