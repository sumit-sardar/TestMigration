	<div id="page-wrap">
        <ul class="dropdown">
        	<li id="assessments" class="simpleMenu"><a class="tab" href="#"><lb:label key="org.menu.assessments" /></a>
        		<ul class="sub_menu">
        			 <li>   			 	
        			 	<a href="#" id="sessionsLink" onClick="verifyEditLicenseAndGotoMenuAction('assessments.do', 'sessionsLink');"><lb:label key="org.menu.sessions" /></a>
        			 </li>
    				 <c:if test="${sessionScope.hasProgramStatusConfigured}">
        			 <li>
        				<a href="#" id="programStatusLink" onClick="verifyEditLicenseAndGotoMenuAction('assessments.do', 'programStatusLink');"><lb:label key="org.menu.programStatus" /></a>
        			 </li>
        			 </c:if>
        			 <c:if test="${sessionScope.hasRapidRagistrationConfigured}">
        			 <li>
        				<a href="#" id="studentRegistrationLink" onClick="gotoMenuAction('assessments.do', 'studentRegistrationLink');"><lb:label key="student.registration.menu" /></a>
        			 </li>
        			 </c:if>
        		</ul>
        	</li>
        	<li id="organizations" class="simpleMenu"><a class="tab" href="#"><lb:label key="org.menu.orgs" /></a>        	
        		<ul class="sub_menu">
        			 <li>   			
        			 	<a href="#" style="float:left;"><lb:label key="org.menu.students" /></a><span class='ui-menuicon ui-icon-triangle-1-e' style="float:right;"></span>
        				<ul>
        					<li><a href="#" id="studentsLink" onClick="verifyEditLicenseAndGotoMenuAction('organizations.do', 'studentsLink');"><lb:label key="org.menu.studentList" /></a></li>
        					<c:if test="${sessionScope.isBulkAccommodationConfigured}">
								<ctb:auth roles="Administrator, Administrative Coordinator">	
        							<li><a href="#" id="bulkAccomLink" onclick="verifyEditLicenseAndGotoMenuAction('organizations.do', 'bulkAccomLink');"><lb:label key="org.menu.assignAccom" /></a></li>
        						</ctb:auth>
							</c:if>
							<c:if test="${sessionScope.isBulkMoveConfigured}">
								<ctb:auth roles="Administrator, Administrative Coordinator">
        							<li><a href="#"  id="bulkMoveLink" onclick="verifyEditLicenseAndGotoMenuAction('organizations.do', 'bulkMoveLink');"><lb:label key="org.menu.moveStudents" /></a></li>
        						</ctb:auth>
        					</c:if>
        					<c:if test="${sessionScope.isOOSConfigured}">
        						<ctb:auth roles="Administrator, Administrative Coordinator">
        							<li><a href="#"  id="OOSLink" onclick="verifyEditLicenseAndGotoMenuAction('organizations.do', 'OOSLink');"><lb:label key="org.menu.outOfSchools" /></a></li>
        						</ctb:auth>
        					</c:if>
        				</ul>
        			 </li>
        			 <ctb:auth roles="Administrator">	
        			 <li>
        				<a href="#" id="usersLink" onClick="verifyEditLicenseAndGotoMenuAction('organizations.do', 'usersLink');"><lb:label key="org.menu.users" /></a>
        			 </li>
        			 </ctb:auth>
					 <ctb:auth roles="Administrator">	
        			 <li>
        				<a href="#" id="organizationsLink" onClick="verifyEditLicenseAndGotoMenuAction('organizations.do', 'organizationsLink');"><lb:label key="org.menu.manageOrgs" /></a>	
        			 </li>
        			 </ctb:auth>
        		</ul>
        	</li>
        	<c:if test="${sessionScope.showReportTab}">
        	<li id="reports" class="simpleMenu"><a class="tab" href="#"  id="reportsLink" onClick="verifyEditLicenseAndGotoMenuAction('reports.do', 'reportsLink');"><lb:label key="org.menu.reports" /></a>
        	</li>
        	</c:if>
        	<c:if test="${sessionScope.hasScoringConfigured}">
        		<ctb:auth roles="Administrator, Administrative Coordinator, Coordinator, Proctor">
	        		<li id="scoring" class="simpleMenu"><a class="tab" href="#"  id="studentScoringLink" onClick="verifyEditLicenseAndGotoMenuAction('studentScoring.do', 'studentScoringLink');"><lb:label key="org.menu.scoring" /></a>
	        		</li>
	        	</ctb:auth>
        	</c:if>
        	<li id="services" class="simpleMenu"><a class="tab" href="#"><lb:label key="org.menu.services" /></a>
        		<ul class="sub_menu">
        			 <li>
        			 	<a href="#" style="float:left;"><lb:label key="org.menu.workstationSetup" /></a><span class='ui-menuicon ui-icon-triangle-1-e' style="float:right;"></span>
        				<ul>
        					<li><a href="#" id="installSoftwareLink" onClick="verifyEditLicenseAndGotoMenuAction('services.do', 'installSoftwareLink');"><lb:label key="org.menu.installSoftware" /></a></li>
        					<li><a href="#" id="downloadTestLink" onClick="verifyEditLicenseAndGotoMenuAction('services.do', 'downloadTestLink');"><lb:label key="org.menu.downloadTest" /></a></li>
        				</ul>
        			 </li>
        			 <c:if test="${sessionScope.hasUploadDownloadConfigured}">	
	        			 <li>
	        			 	<a href="#" style="float:left;"><lb:label key="org.menu.studentLoad" /></a><span class='ui-menuicon ui-icon-triangle-1-e' style="float:right;"></span>        			 	
	        				<ul>
	        					<li><a href="#" id="uploadDataLink" onClick="verifyEditLicenseAndGotoMenuAction('services.do', 'uploadDataLink');"><lb:label key="org.menu.import" /></a></li>
	        					<li><a href="#" id="downloadDataLink" onClick="verifyEditLicenseAndGotoMenuAction('services.do', 'downloadDataLink');"><lb:label key="org.menu.export" /></a></li>
	        				</ul>
	        			 </li>
        			 </c:if>	
        			 <c:if test="${sessionScope.hasLicenseConfigured}">	
        			 <li>
        				<a href="#" id="manageLicensesLink" onClick="verifyEditLicenseAndGotoMenuAction('services.do', 'manageLicensesLink');"><lb:label key="org.menu.licenses" /></a>
        			 </li>
        			 </c:if>
        		</ul>
        	</li>
        </ul>		
	</div>