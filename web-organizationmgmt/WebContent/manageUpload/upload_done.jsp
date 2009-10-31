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
        
    File <b><netui:content value="${pageFlow.strFileName}"/></b> was uploaded successfully. 
    <br/>
    Click 'View Uploads' tab to view your uploads or click 'Upload Another' button to upload more files.
    <br/><br/>
    <p>
        <netui:button tagId="uploadAgain" type="submit" value="Upload Another" action="manageUpload"/>
    </p>
        </td>        
    </tr>
</table>

