<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<ctb:auth roles="Administrator, Administrative Coordinator, Coordinator">
    <h1>Tests</h1>
    <ul>
        <li><a href="/TestAdministrationWeb/scheduleTestPageflow/ScheduleTestController.jpf" onclick="return verifyExitAddOrganization();"><span>Schedule Test Session</span></a></li>
        <li><a href="/TestSessionInfoWeb/viewtestsessions/ViewTestSessionsController.jpf" onclick="return verifyExitAddOrganization();"><span>Find Test Session</span></a></li>
    </ul>
</ctb:auth>
   

<ctb:auth roles="Administrator, Administrative Coordinator, Coordinator, Proctor">
<h1>Students</h1>
<ul>
<ctb:auth roles="Administrator, Administrative Coordinator">
    <li><a href="/StudentManagementWeb/manageStudent/beginAddStudent.do" onclick="return verifyExitAddOrganization();"><span>Add Student</span></a></li>
</ctb:auth>
    <li><a href="/StudentManagementWeb/manageStudent/beginFindStudent.do" onclick="return verifyExitAddOrganization();"><span>Find Student</span></a></li>
</ul>
</ctb:auth>


<ctb:auth roles="root, Account Manager, Administrator">
<h1>Users</h1>
<ul>
    <li><a href="/UserManagementWeb/manageUser/beginAddUser.do" onclick="return verifyExitAddOrganization();"><span>Add User</span></a></li>
    <li><a href="/UserManagementWeb/manageUser/beginFindUser.do" onclick="return verifyExitAddOrganization();"><span>Find User</span></a></li>
</ul>
</ctb:auth>


<ctb:auth roles="root, Account Manager, Administrator">
<h1>Organizations</h1>
<ul>
    
    <c:if test="${ !requestScope.isAddOrganization}">
        <li><a href="/OrganizationManagementWeb/manageOrganization/beginAddOrganization.do" onclick="return verifyExitAddOrganization();"><span>Add Organization</span></a></li>
    </c:if>
    <c:if test="${ requestScope.isAddOrganization}">
        <li><span class="navleft-unclickable">Add Organization</span></li>
    </c:if>
    <c:if test="${ !requestScope.isFindOrganization}">
        <li><a href="/OrganizationManagementWeb/manageOrganization/beginFindOrganization.do" onclick="return verifyExitAddOrganization();"><span>Find Organization</span></a></li>
    </c:if>
    <c:if test="${ requestScope.isFindOrganization}">
        <li><span class="navleft-unclickable">Find Organization</span></li>
    </c:if>
</ul>
</ctb:auth>


<ctb:auth roles="root, Account Manager">
<h1>Customers</h1>
    <ul>
        <li><a href="/OrganizationManagementWeb/manageCustomer/beginAddCustomer.do" onclick="return verifyExitAddOrganization();"><span>Add Customer</span></a></li>
        <li><a href="/OrganizationManagementWeb/manageCustomer/beginFindCustomer.do" onclick="return verifyExitAddOrganization();"><span>Find Customer</span></a></li>
   </ul>
</ctb:auth>


<ctb:auth roles="root, Account Manager">
<h1>Tests</h1>
<ul>
    <li><a href="/OrganizationManagementWeb/manageCustomerService/CustomerServiceManagementController.jpf"><span>Reset Test Session</span></a></li>
</ul>
</ctb:auth>


<ctb:auth roles="Administrator, Administrative Coordinator, Coordinator, Proctor">
<h1>Workstation Setup</h1>
<ul>
    <li><a href="/TestSessionInfoWeb/downloadclient/DownloadClientController.jpf" onclick="return verifyExitAddOrganization();"><span>Install Software</span></a></li>
    <li><a href="/TestSessionInfoWeb/downloadtest/DownloadTestController.jpf" onclick="return verifyExitAddOrganization();"><span>Download Test</span></a></li>
</ul>
</ctb:auth>





