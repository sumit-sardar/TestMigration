<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 

<table class="simple">

    <tr class="simple">
        <td class="sortableControls" colspan="3" height="30">
            &nbsp;<netui:button tagId="download" type="submit" value="Download" action="downloadTemplate" disabled="true"/>
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
                &nbsp;<netui:radioButtonOption value="UserTemplate.xls" onClick="return enableDownload();">&nbsp;</netui:radioButtonOption>                
            </netui:radioButtonGroup>        
        </td>
        <td class="sortable">
            User Template
        </td>        
        <td class="sortable">
            Format for user profiles to upload.
        </td>        
    </tr>
    <tr class="sortable">
        <td class="sortable alignCenter">
            <netui:radioButtonGroup dataSource="actionForm.fileName">
                &nbsp;<netui:radioButtonOption value="StudentTemplate.xls" onClick="return enableDownload();">&nbsp;</netui:radioButtonOption>                
            </netui:radioButtonGroup>        
        </td>
        <td class="sortable">
            Student Template
        </td>        
        <td class="sortable">
            Format for student profiles to upload.
        </td>        
    </tr>

</table>

    
