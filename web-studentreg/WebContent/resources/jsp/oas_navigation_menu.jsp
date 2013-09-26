	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	<div id="page-wrap">
        <ul class="dropdown">
        	<li id="assessments" class="simpleMenu"><a class="tab" href="#"><lb:label key="student.registration.menu.assessments" /></a>
        		<ul class="sub_menu">
        			 <li>   			 	
        			 	<a href="#" id="sessionsLink" onClick="gotoMenuAction('assessments.do', 'sessionsLink');"><lb:label key="student.registration.menu.sessions" /></a>
        			 </li>
    				 <c:if test="${sessionScope.hasProgramStatusConfigured}">
        			 <li>
        				<a href="#" id="programStatusLink" onClick="gotoMenuAction('assessments.do', 'programStatusLink');"><lb:label key="student.registration.menu.programStatus" /></a>
        			 </li>
        			  </c:if>
        			<c:if test="${sessionScope.hasRapidRagistrationConfigured}">
        			 <li>
        				<a href="#" id="studentRegistrationLink" onClick="gotoMenuAction('assessments.do', 'studentRegistrationLink');"><lb:label key="student.registration.menu" /></a>
        			 </li>
        			 </c:if>
        		</ul>
        	</li>
        	<li id="organizations" class="simpleMenu"><a class="tab" href="#"><lb:label key="student.registration.menu.orgs" /></a>        	
        		<ul class="sub_menu">
        			 <li>   			
        			 	<a href="#" style="float:left;"><lb:label key="student.registration.menu.students" /></a><span class='ui-menuicon ui-icon-triangle-1-e' style="float:right;"></span>
        				<ul>
        					<li><a href="#" id="studentsLink" onClick="gotoMenuAction('organizations.do', 'studentsLink');"><lb:label key="student.registration.menu.studentList" /></a></li>
        					<c:if test="${sessionScope.isBulkAccommodationConfigured}">
								<ctb:auth roles="Administrator, Administrative Coordinator">	
        							<li><a href="#" id="bulkAccomLink" onclick="gotoMenuAction('organizations.do', 'bulkAccomLink');"><lb:label key="student.registration.menu.assignAccom" /></a></li>
        						</ctb:auth>
							</c:if>
							<c:if test="${sessionScope.isBulkMoveConfigured}">
								<ctb:auth roles="Administrator, Administrative Coordinator">
        							<li><a href="#"  id="bulkMoveLink" onclick="gotoMenuAction('organizations.do', 'bulkMoveLink');"><lb:label key="student.registration.menu.moveStudents " /></a></li>
        						</ctb:auth>
        					</c:if>
        					<c:if test="${sessionScope.isOOSConfigured}">
        						<ctb:auth roles="Administrator, Administrative Coordinator">
        							<li><a href="#"  id="OOSLink" onclick="gotoMenuAction('organizations.do', 'OOSLink');"><lb:label key="student.registration.menu.outOfSchools" /></a></li>
        						</ctb:auth>
        					</c:if>
        				</ul>
        			 </li>
        			 <ctb:auth roles="Administrator">	
        			 <li>
        				<a href="#" id="usersLink" onClick="gotoMenuAction('organizations.do', 'usersLink');"><lb:label key="student.registration.menu.users" /></a>
        			 </li>
        			 </ctb:auth>
					 <ctb:auth roles="Administrator">	
        			 <li>
        				<a href="#" id="organizationsLink" onClick="gotoMenuAction('organizations.do', 'organizationsLink');"><lb:label key="student.registration.menu.manageOrgs" /></a>	
        			 </li>
        			 </ctb:auth>
        		</ul>
        	</li>
        	<c:if test="${sessionScope.showReportTab}">
        	<li id="reports" class="simpleMenu"><a class="tab" href="#"  id="reportsLink" onClick="gotoMenuAction('reports.do', 'reportsLink');"><lb:label key="student.registration.menu.reports" /></a>
        	</li>
        	</c:if>
        	<c:if test="${sessionScope.hasScoringConfigured}">
        		<ctb:auth roles="Administrator, Administrative Coordinator, Coordinator, Proctor">
	        		<li id="scoring" class="simpleMenu"><a class="tab" href="#"  id="studentScoringLink" onClick="gotoMenuAction('studentScoring.do', 'studentScoringLink');"><lb:label key="student.registration.menu.scoring" /></a>
	        		</li>
	        	</ctb:auth>
        	</c:if>
        	<li id="services" class="simpleMenu"><a class="tab" href="#"><lb:label key="student.registration.menu.services" /></a>
        		<ul class="sub_menu">
        			 <li>
        			 	<a href="#" style="float:left;"><lb:label key="student.registration.menu.workstationSetup" /></a><span class='ui-menuicon ui-icon-triangle-1-e' style="float:right;"></span>
        				<ul>
        					<li><a href="#" id="installSoftwareLink" onClick="gotoMenuAction('services.do', 'installSoftwareLink');"><lb:label key="student.registration.menu.installSoftware" /></a></li>
        					<li><a href="#" id="downloadTestLink" onClick="gotoMenuAction('services.do', 'downloadTestLink');"><lb:label key="student.registration.menu.downloadTest" /></a></li>
        				</ul>
        			 </li>
        			 <c:if test="${sessionScope.hasUploadConfigured}">	
	        			 <li>
	        			 	<a href="#" style="float:left;"><lb:label key="student.registration.menu.studentLoad" /></a><span class='ui-menuicon ui-icon-triangle-1-e' style="float:right;"></span>        			 	
	        				<ul>
	        					<li><a href="#" id="uploadDataLink" onClick="gotoMenuAction('services.do', 'uploadDataLink');"><lb:label key="student.registration.menu.import" /></a></li>
	        				</ul>
	        			 </li>
        			 </c:if>
        			 <c:if test="${sessionScope.hasDownloadConfigured}">	
	        			 <li>
	        			 	<a href="#" style="float:left;"><lb:label key="student.registration.menu.studentLoad" /></a><span class='ui-menuicon ui-icon-triangle-1-e' style="float:right;"></span>        			 	
	        				<ul>
	        					<li><a href="#" id="downloadDataLink" onClick="gotoMenuAction('services.do', 'downloadDataLink');"><lb:label key="student.registration.menu.export" /></a></li>
	        				</ul>
	        			 </li>
        			 </c:if>
        			 <c:if test="${sessionScope.hasUploadDownloadConfigured}">	
	        			 <li>
	        			 	<a href="#" style="float:left;"><lb:label key="student.registration.menu.studentLoad" /></a><span class='ui-menuicon ui-icon-triangle-1-e' style="float:right;"></span>        			 	
	        				<ul>
	        					<li><a href="#" id="uploadDataLink" onClick="gotoMenuAction('services.do', 'uploadDataLink');"><lb:label key="student.registration.menu.import" /></a></li>
	        					<li><a href="#" id="downloadDataLink" onClick="gotoMenuAction('services.do', 'downloadDataLink');"><lb:label key="student.registration.menu.export" /></a></li>
	        				</ul>
	        			 </li>
        			 </c:if>
        			 <ctb:auth roles="Administrator">
	        			 <c:if test="${sessionScope.showDataExportTab}">			
		        			 <li>
		        			 	<a href="#" style="float:left;"><lb:label key="session.menu.dataExport" /></a><span class='ui-menuicon ui-icon-triangle-1-e' style="float:right;"></span>        			 	
		        				<ul>
		        				<li><a href="#" id="exportDataLink" onClick="gotoMenuAction('services.do', 'exportDataLink');"><lb:label key="session.menu.dataExport.1" /></a></li>
		        				<li><a href="#" id="viewStatusLink" onClick="gotoMenuAction('services.do', 'viewStatusLink');"><lb:label key="session.menu.dataExport.2" /></a></li>
		        				</ul>
		        			 </li>
	        			 </c:if>
        			 </ctb:auth>	
        			 <c:if test="${sessionScope.hasLicenseConfigured}">	
        			 <li>
        				<a href="#" id="manageLicensesLink" onClick="gotoMenuAction('services.do', 'manageLicensesLink');"><lb:label key="student.registration.menu.licenses" /></a>
        			 </li>
        			 </c:if>
        			 <c:if test="${sessionScope.hasResetTestSession}">	
        			 <li>
        				<a href="#" id="resetTestSessionLink" onClick="gotoMenuAction('services.do', 'resetTestSessionLink');"><lb:label key="session.menu.resetTestSession" /></a>
        			 </li>
        			 </c:if>
        			 <!-- Changes for defect #74757
        			 <c:if test="${sessionScope.isAccountFileDownloadVisible}">	
        			 <li>
        				<a href="#" id="showAccountFileDownloadLink" onClick="gotoMenuAction('services.do', 'showAccountFileDownloadLink');"><lb:label key="session.menu.userAccountsFiles" /></a>
        			 </li>
        			 </c:if>
        			 -->
					
        		</ul>
        	</li>   
        </ul>		
	</div>