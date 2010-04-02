<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<ctb:auth roles="Administrator, Administrative Coordinator, Coordinator">
<h1>Tests</h1>
<ul>
    <c:if test="${ requestScope.isSelectTest }">
        <li><span class="navleft-unclickable">Schedule Test Session</span></li>
    </c:if>
    <c:if test="${ !requestScope.isSelectTest }">    
        <li><a href="/TestAdministrationWeb/scheduleTestPageflow/ScheduleTestController.jpf" onclick="return verifyExitAddUser();"><span>Schedule Test Session</span></a></li>
    </c:if>
        <li><a href="/TestSessionInfoWeb/viewtestsessions/ViewTestSessionsController.jpf" onclick="return verifyExitAddUser();"><span>Find Test Session</span></a></li>
</ul>
</ctb:auth>

<ctb:auth roles="Administrator, Administrative Coordinator, Coordinator, Proctor">
<h1>Students</h1>
<ul>
<ctb:auth roles="Administrator, Administrative Coordinator">
        <li><a href="/StudentManagementWeb/manageStudent/beginAddStudent.do" onclick="return verifyExitAddUser();" ><span>Add Student</span></a></li>
</ctb:auth>
    <li><a href="/StudentManagementWeb/manageStudent/beginFindStudent.do" onclick="return verifyExitAddUser();" ><span>Find Student</span></a></li>
</ul>
</ctb:auth>


<ctb:auth roles="root, Account Manager, Administrator">
<h1>Users</h1>
<ul>
    <c:if test="${ requestScope.isAddUser }">
        <li><span class="navleft-unclickable">Add User</span></li>
    </c:if>
    <c:if test="${ !requestScope.isAddUser }">    
        <li><a href="/UserManagementWeb/manageUser/beginAddUser.do" onclick="return verifyExitAddUser();" ><span>Add User</span></a></li>
    </c:if>
    <c:if test="${ requestScope.isFindUser }">
        <li><span class="navleft-unclickable">Find User</span></li>
    </c:if>
    <c:if test="${ !requestScope.isFindUser }">    
        <li><a href="/UserManagementWeb/manageUser/beginFindUser.do" onclick="return verifyExitAddUser();" ><span>Find User</span></a></li>
    </c:if>
</ul>
</ctb:auth>


<ctb:auth roles="root, Account Manager, Administrator">
<h1>Organizations</h1>
<ul>
    <li><a href="/OrganizationManagementWeb/manageOrganization/beginAddOrganization.do" onclick="return verifyExitAddUser();"><span>Add Organization</span></a></li>
    <li><a href="/OrganizationManagementWeb/manageOrganization/beginFindOrganization.do" onclick="return verifyExitAddUser();"><span>Find Organization</span></a></li>
</ul>
</ctb:auth>


<ctb:auth roles="root, Account Manager">
<h1>Customers</h1>
<ul>
    <li><a href="/OrganizationManagementWeb/manageCustomer/beginAddCustomer.do" onclick="return verifyExitAddUser();"><span>Add Customer</span></a></li>
    <li><a href="/OrganizationManagementWeb/manageCustomer/beginFindCustomer.do" onclick="return verifyExitAddUser();"><span>Find Customer</span></a></li>
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
    <li><a href="/TestSessionInfoWeb/downloadclient/DownloadClientController.jpf" onclick="return verifyExitAddUser();" ><span>Install Software</span></a></li>
    <li><a href="/TestSessionInfoWeb/downloadtest/DownloadTestController.jpf" onclick="return verifyExitAddUser();" ><span>Download Test</span></a></li>
</ul>
</ctb:auth>


