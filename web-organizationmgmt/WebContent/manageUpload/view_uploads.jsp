<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>

 
<table class="simple">

    <tr class="simple">
        <td class="sortableControls" colspan="6" height="30">
            <netui:button tagId="deleteFile" type="submit" value="Delete" action="deleteErrorDataFile" disabled="${requestScope.disableDeleteButton}" onClick="return verifyDeleteFile();"/>
            <netui:button tagId="getFailedRecords" type="submit" value="Download Error File" action="getErrorDataFile" disabled="${requestScope.disableFailedRecordButton}"/>
            <netui:button tagId="refresh" type="submit" value="Refresh List" action="manageUpload"/>

        </td>        
    </tr>
    
  <c:if test="${searchResultEmpty == null}">  
    <netui-data:repeater dataSource="pageFlow.fileList">
        <netui-data:repeaterHeader>
        <tr class="sortable">
       <ctb:tableSortColumnGroup columnDataSource="actionForm.fileSortColumn" orderByDataSource="actionForm.fileSortOrderBy" anchorName="fileSearchResult">
            <th class="sortable alignCenter">&nbsp;<netui:content value="${bundle.web['common.column.select']}"/>&nbsp;</th>                
            <th class="sortable alignLeft" width="25%" nowrap><ctb:tableSortColumn value="CreatedDateTime">Upload Date</ctb:tableSortColumn></th>
            <th class="sortable alignLeft" width="35%" nowrap><ctb:tableSortColumn value="UploadFileName">File Name</ctb:tableSortColumn></th>
            <th class="sortable alignLeft" width="10%" nowrap><ctb:tableSortColumn value="UploadFileRecordCount">Records Uploaded</ctb:tableSortColumn></th>
            <th class="sortable alignLeft" width="10%" nowrap><ctb:tableSortColumn value="FailedRecordCount">Records Failed</ctb:tableSortColumn></th>
            <th class="sortable alignLeft" width="20%" nowrap><ctb:tableSortColumn value="Status">Status</ctb:tableSortColumn></th>
        </ctb:tableSortColumnGroup>
    </tr>
    
    </netui-data:repeaterHeader>
    <netui-data:repeaterItem>
       <tr class="sortable">
            <netui-data:getData resultId="status" value="${container.item.status}"/>
            <td class="sortable alignCenter">
                <c:if test="${status == 'In Progress'}">   
                    <input type="radio" disabled>
                </c:if>    
                <c:if test="${status != 'In Progress'}"> 
                    <netui:radioButtonGroup dataSource="actionForm.selectedAuditId">
                       &nbsp;<netui:radioButtonOption value="${container.item.dataFileAuditId}" alt="${container.item.actionPermission}" onClick="enableFileButtons(this.alt); showLegend('legendDiv', this.alt);">&nbsp;</netui:radioButtonOption>   
                    </netui:radioButtonGroup>
                </c:if>
            </td>        
            <td class="sortable">
                <netui:span value="${container.item.createdDateTime}"/>
            </td>
            <td class="sortable" nowrap>
                <netui:span value="${container.item.uploadFileName}"/>
            </td>
            <td class="sortable">
                <netui:span value="${container.item.uploadFileRecordCount}"/>
           </td>
           
           <c:if test="${status == 'Error'}">
               <td class="sortableRed">
                    <netui:span value="${container.item.failedRecordCount}"/>
               </td>
           </c:if>
           <c:if test="${status != 'Error'}">
               <td class="sortable">
                    <netui:span value="${container.item.failedRecordCount}"/>
               </td>
           </c:if>
           <td class="sortable" nowrap>
                <netui:span value="${container.item.status}"/>    
           </td>
           
       </tr>
    </netui-data:repeaterItem>
    <netui-data:repeaterFooter>
        <tr class="sortable">
            <td class="sortableControls" colspan="7">
                <ctb:tablePager dataSource="actionForm.filePageRequested" summary="request.filePagerSummary" objectLabel="${bundle.oas['object.files']}" foundLabel="Found" id="fileSearchResult" anchorName="fileSearchResult"/>
            </td>
        </tr>    
    </netui-data:repeaterFooter>
    </netui-data:repeater>
 </c:if>
 <c:if test="${searchResultEmpty != null}"> 
        <tr class ="sortable">    
            <td class ="sortable" colspan="6">
                <ctb:message title="{bundle.web['viewupload.nofile.title']}" style="tableMessage" >
                    <netui:content value="${bundle.web['viewupload.nofile.message']}"/>
                </ctb:message>
            </td>
        </tr>    
 </c:if>   
    
</table>

<c:if test="${requestScope.disableFailedRecordButton}" > 
<div id="legendDiv" style="display: none">
</c:if>
<c:if test="${! requestScope.disableFailedRecordButton}" > 
<div id="legendDiv" style="display: block">
</c:if>

<br/>
<p>
<netui:content value="Errors in the downloaded error file are highlighted in different colors."/><br/>
<netui:content value="Use this key to help decide how to correct the data."/><br/>
</p>

<table class="simpleSmall" width="40%">
    <tr class="simple">
        <td class="simpleHeader">Error Description</td>
        <td class="simpleHeader">Error Color</td> 
    </tr>
    <tr class="simple">
        <td class="simple">More characters than allowed</td>
        <td class="simpleColored" bgcolor="#FBFF73">&nbsp;&nbsp;</td>
    </tr>
    <tr class="simple">
        <td class="simple">Invalid characters</td>
        <td class="simpleColored" bgcolor="#99FF99">&nbsp;&nbsp;</td>
    </tr>
    <tr class="simple">
        <td class="simple">Missing required values</td>
        <td class="simpleColored" bgcolor="#00CCFF">&nbsp;&nbsp;</td>
    </tr>
    <tr class="simple">
        <td class="simple">Logical errors</td>
        <td class="simpleColored" bgcolor="#FF9900">&nbsp;&nbsp;</td>
    </tr>
</table>
</div>



