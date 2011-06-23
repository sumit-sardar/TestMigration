<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>
<netui-data:declareBundle bundlePath="helpResources" name="help"/>

<netui-template:template templatePage="/resources/jsp/template.jsp">

<netui-template:setAttribute name="title" value="${bundle.web['system.window.title']}"/>
<netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.systemAdministration']}"/>
<netui-template:section name="bodySection">

<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->

<h1><netui:content value="${bundle.web['administration.title']}"/></h1>
<br/>

 <c:if test="${ sessionScope.hasUploadDownloadConfig}"> 
    <p>
    <netui:anchor action="manageUpload"><netui:content value="${bundle.web['administration.upload.title']}"/></netui:anchor>
    <br/>
    <netui:content value="${bundle.web['administration.upload.text']}"/>
    </p>
    
    <p>
    <netui:anchor action="manageDownload"><netui:content value="${bundle.web['administration.download.title']}"/></netui:anchor>
    <br/>
    <netui:content value="${bundle.web['administration.download.text']}"/>
    </p>
  </c:if>
  <c:if test="${ sessionScope.hasProgramStatusConfig}"> 
    <p>
    <netui:anchor action="manageProgram"><netui:content value="${bundle.web['administration.programStatus.title']}"/></netui:anchor>
    <br/>
    <netui:content value="${bundle.web['administration.programStatus.text']}"/>
    </p>
  </c:if>
   <c:if test="${ !(sessionScope.hasUploadDownloadConfig || sessionScope.hasProgramStatusConfig)}"> 
    <p>
    <netui:content value="${bundle.web['administration.no.features']}"/>    
    <br/>
    </p>
  </c:if>
  <c:if test="${ sessionScope.hasLicenseConfig}"> 
    <p>
  	<c:if test="${! sessionScope.isAdminUserAtLeafNode}"> 
    	<netui:anchor action="manageLicense"><netui:content value="${bundle.web['manageLicense.title']}"/></netui:anchor>
    	<br/>
    	<netui:content value="${bundle.web['manageLicense.menu.message']}"/>
  	</c:if>
  	<c:if test="${sessionScope.isAdminUserAtLeafNode}"> 
    	<netui:anchor action="viewLicense"><netui:content value="${bundle.web['license.title']}"/></netui:anchor>
    	<br/>
    	<netui:content value="${bundle.web['license.menu.message']}"/>
  	</c:if>
    </p>
  </c:if>
  <c:if test="${ sessionScope.canGenerateReportFile}"> 
    <p>
    <netui:anchor action="generateReportFile"><netui:content value="${bundle.web['generatereport.title']}"/></netui:anchor>
    <br/>
    <netui:content value="${bundle.web['generatereport.text']}"/>
    </p>
  </c:if>
  
<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
</netui-template:section>
</netui-template:template>
 