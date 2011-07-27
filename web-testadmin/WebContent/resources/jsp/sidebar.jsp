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

<ctb:auth roles="Administrator, Administrative Coordinator, Coordinator">
    <h1>Tests</h1>
    <ul>
        <c:if test="${ requestScope.isSelectTest }">
            <li><span class="navleft-unclickable">Schedule Test Session</span></li>
        </c:if>
        <c:if test="${ !requestScope.isSelectTest }">    
            <li><a href="<%= scheduleTestURL  %>" onclick="return verifyExitScheduleTest();"><span>Schedule Test Session</span></a></li>
        </c:if>
        <li><a href="/TestSessionInfoWeb/viewtestsessions/ViewTestSessionsController.jpf" onclick="return verifyExitScheduleTest();"><span>Find Test Session</span></a></li>
    </ul>
</ctb:auth>


<ctb:auth roles="Administrator, Administrative Coordinator, Coordinator, Proctor">
<h1>Students</h1>
<ul>
<ctb:auth roles="Administrator, Administrative Coordinator">
    <li><a href="/StudentManagementWeb/manageStudent/beginAddStudent.do" onclick="return verifyExitScheduleTest();"><span>Add Student</span></a></li>
</ctb:auth>
    <li><a href="/StudentManagementWeb/manageStudent/beginFindStudent.do" onclick="return verifyExitScheduleTest();"><span>Find Student</span></a></li>
<ctb:auth roles="Administrator, Administrative Coordinator">
	<c:if test="${ sessionScope.isBulkAccommodationConfigured}">    
		<li><a href="/StudentManagementWeb/manageBulkAccommodation/beginAddBulkStudent.do" ><span>Edit Accommodations</span></a></li>
	</c:if>
</ctb:auth>      
</ul>
</ctb:auth>


<ctb:auth roles="root, Account Manager, Administrator">
<h1>Users</h1>
<ul>
    <li><a href="/UserManagementWeb/manageUser/beginAddUser.do" onclick="return verifyExitScheduleTest();"><span>Add User</span></a></li>
    <li><a href="/UserManagementWeb/manageUser/beginFindUser.do" onclick="return verifyExitScheduleTest();"><span>Find User</span></a></li>
</ul>
</ctb:auth>


<ctb:auth roles="root, Account Manager, Administrator">
<h1>Organizations</h1>
<ul>
    <li><a href="/OrganizationManagementWeb/manageOrganization/beginAddOrganization.do" onclick="return verifyExitScheduleTest();"><span>Add Organization</span></a></li>
    <li><a href="/OrganizationManagementWeb/manageOrganization/beginFindOrganization.do" onclick="return verifyExitScheduleTest();"><span>Find Organization</span></a></li>
</ul>
</ctb:auth>


<ctb:auth roles="root, Account Manager">
<h1>Customers</h1>
<ul>
    <li><a href="/OrganizationManagementWeb/manageCustomer/beginAddCustomer.do"><span>Add Customer</span></a></li>
    <li><a href="/OrganizationManagementWeb/manageCustomer/beginFindCustomer.do"><span>Find Customer</span></a></li>
</ul>
</ctb:auth>


<ctb:auth roles="root, Account Manager">
<h1>Tests</h1>
<ul>
    <li><a href="/OrganizationManagementWeb/manageCustomerService/CustomerServiceManagementController.jpf" onclick="return verifyExitScheduleTest();"><span>Reset Test Session</span></a></li>
</ul>
</ctb:auth>
<c:if test="${ sessionScope.isScoringConfigured}">    
<ctb:auth roles="Administrator, Administrative Coordinator, Coordinator, Proctor">
<h1>Score</h1>
<ul>
     
   <li><a href="/HandScoringWeb/studentScoringPageFlow/beginIndivStudentScoring.do" onclick="return verifyExitScheduleTest();"><span>Student Scoring</span></a></li>
   
</ul>
</ctb:auth>
 </c:if>

<ctb:auth roles="Administrator, Administrative Coordinator, Coordinator, Proctor">
<h1>Workstation Setup</h1>
<ul>
    <li><a href="/TestSessionInfoWeb/downloadclient/DownloadClientController.jpf" onclick="return verifyExitScheduleTest();"><span>Install Software</span></a></li>
    <li><a href="/TestSessionInfoWeb/downloadtest/DownloadTestController.jpf" onclick="return verifyExitScheduleTest();"><span>Download Test</span></a></li>
</ul>
</ctb:auth>
<ctb:auth roles="Administrator">
<c:if test="${ sessionScope.isTopLevelUser }">
<h1>Export</h1>
<ul>
    <li><a href="/DataExportWeb/dataExportPageFlow/begin.do" onclick="return verifyExitScheduleTest();"><span>Data Export</span></a></li>
    <li><a href="/DataExportWeb/dataExportPageFlow/beginViewStatus.do" onclick="return verifyExitScheduleTest();"><span>View Status</span></a></li>
    
</ul>
 </c:if>
</ctb:auth>

