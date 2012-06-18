<%@ page import="java.io.*, java.util.*"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

  <head>
    <title><netui-template:attribute name="title"/></title>

    <link href="<%=request.getContextPath()%>/resources/css/widgets.css" type="text/css" rel="stylesheet" />
    <link href="<%=request.getContextPath()%>/resources/css/main.css" type="text/css" rel="stylesheet" />
    <link href="<%=request.getContextPath()%>/resources/css/menu.css" type="text/css" rel="stylesheet" />
    <link href="<%=request.getContextPath()%>/resources/css/tabs.css" type="text/css" rel="stylesheet" />
    <link href="<%=request.getContextPath()%>/resources/css/main.css" type="text/css" rel="stylesheet" />
    <link href="<%=request.getContextPath()%>/resources/css/roundCorners.css" type="text/css" rel="stylesheet" />


    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/main.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/menu.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery.corners.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/roundCorners.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/tabs.js"></script>


    <!-- 
     -->
</head>

 <body>


<!-- MAIN BODY -->
<table class="simpleBody" >
 
	<tr>
		<td align="center" valign="top" >
			<table class="bodyLayout">

				<!-- HEADER SECTION -->
				<tr class="bodyLayout">
					<td>
					 
						<table class="headerLayout" >
							<tr>
								<td align="left" width="70%"><img src="<%=request.getContextPath()%>/resources/images/ctb_oas_logo.png"></td>
								<td align="left" width="30%">
									<table border="0" cellpadding="0" cellspacing="0">
									<tr height="22">
										<td align="center">
											<b>You are logged in as tai_ctb</b>
										</td>
									</tr>
									<tr>
										<td>
        								<div style="background-color:#DEECF6; padding:5px" class="rounded {5px}">
											&nbsp;&nbsp;<a href="#"><b>Home</b></a>&nbsp;&nbsp;
											<img src="images/dotdot.jpg"/>&nbsp;&nbsp;
											<a href="#"><b>My Profile</b></a>&nbsp;&nbsp;
											<img src="images/dotdot.jpg"/>&nbsp;&nbsp;
											<a href="https://oas.ctb.com/help/index.html#about_the_home_page.htm"><b>Help</b></a>&nbsp;&nbsp;
											<img src="images/dotdot.jpg"/>&nbsp;&nbsp;
											<a href="logout.html"><b>Logout</b></a>&nbsp;&nbsp;
										</div>
										</td>
									</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>


				<!-- TABS SECTION -->
				<tr>
				  	<td align="left" valign="top">

					  <!-- TABS HEADERS -->
					  <div id="featureTabsBody">
					  
					    <div id="featureTabsContainer">
					      <a href="javascript:" onclick="tab(0)" class="tab rounded {top transparent}">Assessments</a>
					      <a href="javascript:" onclick="tab(1)" class="tab rounded {top transparent}">Organizations</a>
					      <a href="javascript:" onclick="tab(2)" class="tab rounded {top transparent}">Reports</a>
					      <a href="javascript:" onclick="tab(3)" class="tab rounded {top transparent}">Services</a>
					    </div>


					  	<!-- TABS BODY -->
    					<div id="featureElementsContainer" class="rounded {right bottom}">

							<!-- ASSESSMENT MENU -->	
							<div id="assessments" style="display: none">					
							<table class="toolbar">
							<tr class="toolbar">
								<td class="toolbar" width="120">
									<a href="#" id="sessionsLink" onClick="selectAssessmentsLink('sessionsLink');"><b>Sessions</b></a>						
								</td>
								<td class="toolbar" width="120">
									<a href="#" id="scheduleSessionLink" onClick="selectAssessmentsLink('scheduleSessionLink');"><b>Schedule Session</b></a>						
								</td>
								<td class="toolbar" width="120">
									<a href="#" id="studentScoringLink" onClick="selectAssessmentsLink('studentScoringLink');"><b>Student Scoring</b></a>						
								</td>
								<td class="toolbar" width="120">
									<a href="#" id="programStatusLink" onClick="selectAssessmentsLink('programStatusLink');"><b>Program Status</b></a>						
								</td>
								<td width="*">&nbsp;</td>		
							</tr>
							</table>						
							</div>

							<!-- ORGANIZATION MENU -->
							<div id="organizations" style="display: none">					
							<table class="toolbar">
							<tr class="toolbar">
								<td class="toolbar" width="120">
									<a href="#" id="organizationsLink" onClick="selectOrganizationsLink('organizationsLink');"><b>Organizations</b></a>						
								</td>
								<td class="toolbar" width="120">
									<a href="#" id="studentsLink" onClick="selectOrganizationsLink('studentsLink');"><b>Students</b></a>						
								</td>
								<td class="toolbar" width="120">
									<a href="#" id="usersLink" onClick="selectOrganizationsLink('usersLink');"><b>Users</b></a>						
								</td>
								<td width="*">&nbsp;</td>		
							</tr>
							</table>						
							</div>

							<!-- REPORTS MENU -->
							<div id="reports" style="display: none">					
							</div>

							<!-- SERVICES MENU -->
							<div id="services" style="display: none">	
							<table class="toolbar">
							<tr class="toolbar">
							
								<td class="toolbar" width="120">
									<a href="#" id="manageLicensesLink" onClick="selectServicesLink('manageLicensesLink');"><b>Manage Licenses</b></a>						
								</td>
								
								<td  class="toolbar-alignleft" width="330">
								
									<div id="service-menu">
									<ul>
										<li>
											<a href="#"><span style="color: blue">Workstation Setup&nbsp;
												<em>
													<img src="images/zonebar-downarrow.png" alt="dropdown" />
												</em>
											</span></a>
											<ul class="submenu" style="background: #DEECF6">
												<li><a href="#" style="color: #0000ff" id="installSoftwareLink" onClick="selectServicesLink('installSoftwareLink');">Install Software</a></li>
												<li><a href="#" style="color: #0000ff" id="downloadtestLink" onClick="selectServicesLink('downloadtestLink');">Download Test</a></li>
											</ul>
										</li>
										
										<li>
											<a href="#"><span style="color: blue">User/Student Load&nbsp;&nbsp;
												<em>
													<img src="images/zonebar-downarrow.png" alt="dropdown" />
												</em>
											</span></a>
											<ul class="submenu" style="background: #DEECF6">
												<li align="left"><a href="#" style="color: #0000ff" id="uploadDataLink" onClick="selectServicesLink('uploadDataLink');">Import</a></li>
												<li><a href="#" style="color: #0000ff" id="downloadDataLink" onClick="selectServicesLink('downloadDataLink');">Export</a></li>
											</ul>
										</li>
									</ul>
									</div>
								
								</td>
								<td width="*">&nbsp;</td>		
								
								</tr>
								</table>
							</div>
							
      	<div class="feature" id="assessmentsContent">
			<img id="assessmentsContentImage" src="images/sessions.jpg" />
      	</div>

      <div class="feature" id="organizationsContent">
	  		<img id="organizationsContentImage" src="images/organization.jpg" />
      </div>

      <div class="feature" id="reportsContent">
<p>
<h2 style="color:black">Reports</h2>
<span style="color:black">Click a report name to view the report</span>
</p>							
<!-- TURNLEAF REPORT LIST -->
<p>
<table class="transparent">
<tr class="transparent"><td class="transparent" width="32" valign="top" >&nbsp;</td><td class="transparent" width="650" valign="top" ><li style="list-style-type: square;" ><a href="homepage/viewReports.do?report=GroupList" style="display: block" >Group List Report</a></li>Displays results for all tests a selected group of students has taken,  using scale scores and derived scores such as percentile ranks, Grade Equivalent, NRS Level, and Expected GED score. A summary of performance is also provided for groups of 10 or more students.</td></tr>
<tr class="transparent"><td class="transparent" width="32" valign="top" >&nbsp;</td><td class="transparent" width="650" valign="top" ><li style="list-style-type: square;" ><a href="homepage/viewReports.do?report=IndividualPortfolio" style="display: block" >Individual Portfolio Report</a></li>Displays results for all tests an individual has taken,  using scale scores and derived scores such as percentile ranks, Grade Equivalent, NRS Level, and Expected GED score.</td></tr>
<tr class="transparent"><td class="transparent" width="32" valign="top" >&nbsp;</td><td class="transparent" width="650" valign="top" ><li style="list-style-type: square;" ><a href="homepage/viewReports.do?report=ExportIndividualStudentResults" style="display: block" >Individual Student Results Export</a></li>Exports student demographic information and subtest and objectives performance data in standard format.</td></tr>
<tr class="transparent"><td class="transparent" width="32" valign="top" >&nbsp;</td><td class="transparent" width="650" valign="top" ><li style="list-style-type: square;" ><a href="homepage/viewReports.do?report=ItemAnalysis" style="display: block" >Item Analysis Report</a></li>Indicates the individual item performances, grouped by objective and subskill, for a group of students. Includes the correct answer for each item and the students' answer choice selection frequencies.</td></tr>
<tr class="transparent"><td class="transparent" width="32" valign="top" >&nbsp;</td><td class="transparent" width="650" valign="top" ><li style="list-style-type: square;" ><a href="homepage/viewReports.do?report=Locator" style="display: block" >Locator Test Report</a></li>Indicates the number of test items answered correctly in each subject area of the Locator test and recommends the appropriate TABE level(s) to administer.</td></tr>
</table>
<br/>
</p>
      </div>

      <div class="feature" id="servicesContent">
			<img id="servicesContentImage" src="images/managelicense.jpg" />
      </div>

      <div class="feature">
        Obtain support and the latest version at: <a href="http://plugins.jquery.com/project/corners">http://plugins.jquery.com/project/corners</a>
        <br />
        <br />
      </div>

    </div>
  </div>


					</td>
			</tr>
			




				<!-- FOOTER SECTION -->
				<tr>
				  <td align="left" valign="top">
				  
					<table class="footerLayout" >
					  <tr>
						<td class="footerLayout">
						    <span>
						        Copyright &copy; 2008 by CTB/McGraw-Hill LLC. All rights reserved.
						    </span>
						
						    <span>
						        Subject to <a href="/html/terms_of_use.html" onClick="showTermsOfUseWindow(this.href); return false;">Terms of Use</a>.
						    </span>
						
						    <span>
						        Read our <a href="/html/privacy_policy.html" onClick="showPrivacyPolicyWindow(this.href); return false;">Privacy Policy Online</a>.
						    </span>
						
						    <span>
						        Review <a href="/html/coppa_policy.html" onClick="showCOPPAWindow(this.href); return false;">COPPA Policy</a>.
						    </span>
						</td>
					  </tr>
					</table>
				  </td>
				</tr>

			</table>
		</td>
	</tr>


</table>
 
</body>

</html>
