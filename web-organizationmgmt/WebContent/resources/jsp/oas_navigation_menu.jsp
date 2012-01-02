<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>

	<div id="page-wrap">
        <ul class="dropdown">
        	<li id="assessments" class="simpleMenu"><a href="#">Assessments</a>
        		<ul class="sub_menu">
        			 <li>   			 	
        			 	<a href="#" id="sessionsLink" onClick="gotoMenuAction('assessments.do', 'sessionsLink');">Manage Sessions</a>
        			 </li>
        			  <c:if test="${sessionScope.hasScoringConfigured}">    
						<ctb:auth roles="Administrator, Administrative Coordinator, Coordinator, Proctor">
	        			 	<li>
	        					<a href="#" id="studentScoringLink" onClick="gotoMenuAction('assessments.do', 'studentScoringLink');">Student Scoring</a>
	        			 	</li>
        			 	</ctb:auth>
    				 </c:if>
    				 <c:if test="${sessionScope.hasProgramStatusConfigured}">
        			 <li>
        				<a href="#" id="programStatusLink" onClick="gotoMenuAction('assessments.do', 'programStatusLink');">Program Status</a>
        			 </li>
        			 </c:if>
        		</ul>
        	</li>
        	<li id="organizations" class="simpleMenu"><a href="#">Organizations</a>        	
        		<ul class="sub_menu">
        			 <li>   			
        			 	<a href="#" style="float:left;">Manage Students</a><span class='ui-menuicon ui-icon-triangle-1-e' style="float:right;"></span>
        				<ul>
        					<li><a href="#" id="studentsLink" onClick="gotoMenuAction('organizations.do', 'studentsLink');">Student List</a></li>
        					<c:if test="${sessionScope.isBulkAccommodationConfigured}">
								<ctb:auth roles="Administrator, Administrative Coordinator">	
        							<li><a href="#" id="bulkAccomLink" onclick="gotoMenuAction('organizations.do', 'bulkAccomLink');">Assign Accommodations</a></li>
        						</ctb:auth>
							</c:if>
							<c:if test="${sessionScope.isBulkMoveConfigured}">
        						<li><a href="#"  id="bulkMoveLink" onclick="gotoMenuAction('organizations.do', 'bulkMoveLink');">Move Students</a></li>
        					</c:if>
        					<li><a href="#" >Out Of School Students</a></li>
        				</ul>
        			 </li>
        			 <ctb:auth roles="Administrator">	
        			 <li>
        				<a href="#" id="usersLink" onClick="gotoMenuAction('organizations.do', 'usersLink');">Manage Users</a>
        			 </li>
        			 </ctb:auth>
					 <ctb:auth roles="Administrator">	
        			 <li>
        				<a href="#" id="organizationsLink" onClick="gotoMenuAction('organizations.do', 'organizationsLink');">Manage Hierarchy</a>	
        			 </li>
        			 </ctb:auth>
        		</ul>
        	</li>
        	<c:if test="${sessionScope.showReportTab}">
        	<li id="reports" class="simpleMenu"><a href="#"  id="reportsLink" onClick="gotoMenuAction('reports.do', 'reportsLink');">Reports</a>
        	</li>
        	</c:if>		
        	<li id="services" class="simpleMenu"><a href="#">Services</a>
        		<ul class="sub_menu">
        			 <li>
        			 	<a href="#" style="float:left;">Workstation Setup</a><span class='ui-menuicon ui-icon-triangle-1-e' style="float:right;"></span>
        				<ul>
        					<li><a href="#" id="installSoftwareLink" onClick="gotoMenuAction('services.do', 'installSoftwareLink');">Install Software</a></li>
        					<li><a href="#" id="downloadTestLink" onClick="gotoMenuAction('services.do', 'downloadTestLink');">Download Test</a></li>
        				</ul>
        			 </li>
        			 <c:if test="${sessionScope.hasUploadDownloadConfigured}">	
	        			 <li>
	        			 	<a href="#" style="float:left;">User/Student Load</a><span class='ui-menuicon ui-icon-triangle-1-e' style="float:right;"></span>        			 	
	        				<ul>
	        					<li><a href="#" id="downloadDataLink" onClick="gotoMenuAction('services.do', 'downloadDataLink');">Import</a></li>
	        					<li><a href="#" id="uploadDataLink" onClick="gotoMenuAction('services.do', 'uploadDataLink');">Export</a></li>
	        				</ul>
	        			 </li>
        			 </c:if>	
        			 <c:if test="${sessionScope.hasLicenseConfigured}">	
        			 <li>
        				<a href="#" id="manageLicensesLink" onClick="gotoMenuAction('services.do', 'manageLicensesLink');">Manage Licenses</a>
        			 </li>
        			 </c:if>
        		</ul>
        	</li>
        </ul>		
	</div>