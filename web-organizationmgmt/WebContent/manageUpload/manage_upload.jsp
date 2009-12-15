<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
 
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>
<netui-data:declareBundle bundlePath="helpResources" name="help"/>

<netui-template:template templatePage="/resources/jsp/template_upload_download.jsp">
<netui-template:setAttribute name="title" value="${bundle.web['upload.window.title']}"/>
<netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.uploadData']}"/>
<netui-template:setAttribute name="templateHelpLink" value="${bundle.help['help.topic.howToUseTemplate']}"/>
<netui-template:section name="bodySection">

<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->

<h1><netui:content value="Upload Data"/></h1>

<!-- title -->
<c:if test="${selectedModule == 'moduleDownloadTemplate'}">     
    <p>
        <netui:content value="Select a template to download and save. Use the template to format the user or student data file, and then upload the file."/><br/>
        <netui:content value="View the upload status to verify that the import was successful, or to find and save an error file to correct and re-upload."/><br/>
    </p>
    <p align="right">
        <a href="<netui-template:attribute name="templateHelpLink"/>" onClick="showHelpWindow(this.href); return false;">How to use the templates</a>
    </p>
</c:if> 
<c:if test="${selectedModule == 'moduleUploadFile'}">     
    <p>
        <netui:content value='Select a data file, and then click "Upload" to import the data.'/><br/>
    </p>
</c:if> 
<c:if test="${selectedModule == 'moduleViewUploads'}">     
    <p>
        <netui:content value="Select a successfully loaded file to delete from the list, or select a file with errors to download a file of the records that failed to load. You can correct any failed records and then upload them."/><br/><br/>
    </p>
</c:if> 


<!-- start form -->
<netui:form action="manageUpload" enctype="multipart/form-data" method="post">
<!-- hidden parameters-->
<netui:hidden  dataSource="actionForm.actionElement"/> 
<netui:hidden  dataSource="actionForm.currentAction"/>
<netui:hidden dataSource="actionForm.fileMaxPage"/> 
<netui:hidden tagId="uploadStatus" dataSource="uploadStatus"/>

<jsp:include page="/manageUpload/show_message.jsp" />

<!-- tabs -->      
<p>
<ctb:tableTabGroup dataSource="actionForm.selectedTab">
    <ctb:tableTab value="moduleDownloadTemplate"><netui:content value="Templates"/></ctb:tableTab>
    <ctb:tableTab value="moduleUploadFile"><netui:content value="Upload File"/></ctb:tableTab>
    <ctb:tableTab value="moduleViewUploads"><netui:content value="View Uploads"/></ctb:tableTab>
</ctb:tableTabGroup>


<!-- include pages -->      
<ctb:switch dataSource="${actionForm.selectedTab}">
    <ctb:case value="moduleDownloadTemplate">
        <jsp:include page="download_template.jsp" />
    </ctb:case>
    <ctb:case value="moduleUploadFile">
        <c:if test="${uploadStatus == 'uploadFile'}">     
            <jsp:include page="upload_file.jsp" />
        </c:if> 
        <c:if test="${uploadStatus == 'uploadDone'}">     
            <jsp:include page="upload_done.jsp" />
        </c:if> 
    </ctb:case>
    <ctb:case value="moduleViewUploads">
        <jsp:include page="view_uploads.jsp" />
    </ctb:case>
</ctb:switch>

</p>

</netui:form>

<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
</netui-template:section>
</netui-template:template>
