<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
	Boolean bulkAcc = (Boolean)session.getAttribute("isBulkAccommodationConfigured");
	if (bulkAcc == null) bulkAcc = new Boolean(false);
	Boolean hasReport = (Boolean)session.getAttribute("userHasReports");
	if (hasReport == null) hasReport = new Boolean(false);
	String scheduleTestURL = "/TestAdministrationWeb/scheduleTestPageflow/ScheduleTestController.jpf" + 
							"?bulkAcc=" + bulkAcc.toString() + "&hasReport=" + hasReport.toString();
%>
    <h1>Tests</h1>
    <ul>
        <li><a href="<%= scheduleTestURL  %>"><span>Schedule Test Session</span></a></li>
        <li><a href="/web-demo/homepage/HomePageController.jpf"><span>Find Test Session</span></a></li>
    </ul>
   

<h1>Students</h1>
<ul>
    <li><a href="/web-demo/tableList/beginAddStudent.do" ><span>Add Student</span></a></li>
    <li><a href="/web-demo/tableList/beginFindStudents.do" ><span>Find Student</span></a></li>
</ul>
  

<h1>Users</h1>
<ul>
    <li><a href="/web-demo/tableList/beginAddStudent.do"><span>Add User</span></a></li>
    <li><a href="/web-demo/tableList/beginFindStudents.do"><span>Find User</span></a></li>
</ul>


<h1>Workstation Setup</h1>
<ul>
    <c:if test="${ requestScope.isDownloadClient }">
        <li><span class="navleft-unclickable">Install Software</span></li>
    </c:if>
    <c:if test="${ !requestScope.isDownloadClient }">    
        <li><a href="/web-demo/homepage/HomePageController.jpf"><span>Install Software</span></a></li>
    </c:if>

    <c:if test="${ requestScope.isDownloadTest }">
        <li><span class="navleft-unclickable">Download Test</span></li>
    </c:if>
    <c:if test="${ !requestScope.isDownloadTest }">    
        <li><a href="/web-demo/homepage/HomePageController.jpf"><span>Download Test</span></a></li>
    </c:if>
</ul>


