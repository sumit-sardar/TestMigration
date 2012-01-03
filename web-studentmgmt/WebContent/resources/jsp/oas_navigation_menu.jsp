<%@ page import="java.io.*, java.util.*"%>
<%@ taglib uri="label.tld" prefix="lb" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<lb:bundle baseName="studentApplicationResource" />

	<div id="page-wrap">
        <ul class="dropdown">
        	<li id="assessments" class="simpleMenu"><a href="#"><lb:label key="stu.menu.assessments" /></a>
        		<ul class="sub_menu">
        			 <li>   			 	
        			 	<a href="#" id="sessionsLink" onClick="gotoMenuAction('assessments.do', 'sessionsLink');"><lb:label key="stu.menu.sessions" /></a>
        			 </li>
        			  <c:if test="${sessionScope.hasScoringConfigured}">    
						<ctb:auth roles="Administrator, Administrative Coordinator, Coordinator, Proctor">
	        			 	<li>
	        					<a href="#" id="studentScoringLink" onClick="gotoMenuAction('assessments.do', 'studentScoringLink');"><lb:label key="stu.menu.studentScoring" /></a>
	        			 	</li>
        			 	</ctb:auth>
    				 </c:if>
    				 <c:if test="${sessionScope.hasProgramStatusConfigured}">
        			 <li>
        				<a href="#" id="programStatusLink" onClick="gotoMenuAction('assessments.do', 'programStatusLink');"><lb:label key="stu.menu.programStatus" /></a>
        			 </li>
        			 </c:if>
        		</ul>
        	</li>
        	<li id="organizations" class="simpleMenu"><a href="#"><lb:label key="stu.menu.orgs" /></a>        	
        		<ul class="sub_menu">
        			 <li>   			
        			 	<a href="#" style="float:left;"><lb:label key="stu.menu.students" /></a><span class='ui-menuicon ui-icon-triangle-1-e' style="float:right;"></span>
        				<ul>
        					<li><a href="#" id="studentsLink" onClick="gotoMenuAction('organizations.do', 'studentsLink');"><lb:label key="stu.menu.studentList" /></a></li>
        					<c:if test="${sessionScope.isBulkAccommodationConfigured}">
								<ctb:auth roles="Administrator, Administrative Coordinator">	
        							<li><a href="#" id="bulkAccomLink" onclick="gotoMenuAction('organizations.do', 'bulkAccomLink');"><lb:label key="stu.menu.assignAccom" /></a></li>
        						</ctb:auth>
							</c:if>
							<c:if test="${sessionScope.isBulkMoveConfigured}">
        						<li><a href="#"  id="bulkMoveLink" onclick="gotoMenuAction('organizations.do', 'bulkMoveLink');"><lb:label key="stu.menu.moveStudents" /></a></li>
        					</c:if>
        					<li><a href="#" ><lb:label key="stu.menu.outOfSchools" /></a></li>
        				</ul>
        			 </li>
        			 <ctb:auth roles="Administrator">	
        			 <li>
        				<a href="#" id="usersLink" onClick="gotoMenuAction('organizations.do', 'usersLink');"><lb:label key="stu.menu.users" /></a>
        			 </li>
        			 </ctb:auth>
					 <ctb:auth roles="Administrator">	
        			 <li>
        				<a href="#" id="organizationsLink" onClick="gotoMenuAction('organizations.do', 'organizationsLink');"><lb:label key="stu.menu.manageOrgs" /></a>	
        			 </li>
        			 </ctb:auth>
        		</ul>
        	</li>
        	<c:if test="${sessionScope.showReportTab}">
        	<li id="reports" class="simpleMenu"><a href="#"  id="reportsLink" onClick="gotoMenuAction('reports.do', 'reportsLink');"><lb:label key="stu.menu.reports" /></a>
        	</li>
        	</c:if>		
        	<li id="services" class="simpleMenu"><a href="#"><lb:label key="stu.menu.services" /></a>
        		<ul class="sub_menu">
        			 <li>
        			 	<a href="#" style="float:left;"><lb:label key="stu.menu.workstationSetup" /></a><span class='ui-menuicon ui-icon-triangle-1-e' style="float:right;"></span>
        				<ul>
        					<li><a href="#" id="installSoftwareLink" onClick="gotoMenuAction('services.do', 'installSoftwareLink');"><lb:label key="stu.menu.installSoftware" /></a></li>
        					<li><a href="#" id="downloadTestLink" onClick="gotoMenuAction('services.do', 'downloadTestLink');"><lb:label key="stu.menu.downloadTest" /></a></li>
        				</ul>
        			 </li>
        			 <c:if test="${sessionScope.hasUploadDownloadConfigured}">	
	        			 <li>
	        			 	<a href="#" style="float:left;"><lb:label key="stu.menu.studentLoad" /></a><span class='ui-menuicon ui-icon-triangle-1-e' style="float:right;"></span>        			 	
	        				<ul>
	        					<li><a href="#" id="downloadDataLink" onClick="gotoMenuAction('services.do', 'downloadDataLink');"><lb:label key="stu.menu.import" /></a></li>
	        					<li><a href="#" id="uploadDataLink" onClick="gotoMenuAction('services.do', 'uploadDataLink');"><lb:label key="stu.menu.export" /></a></li>
	        				</ul>
	        			 </li>
        			 </c:if>	
        			 <c:if test="${sessionScope.hasLicenseConfigured}">	
        			 <li>
        				<a href="#" id="manageLicensesLink" onClick="gotoMenuAction('services.do', 'manageLicensesLink');"><lb:label key="stu.menu.licenses" /></a>
        			 </li>
        			 </c:if>
        		</ul>
        	</li>
        </ul>		
	</div>