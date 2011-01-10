<%@ page import="java.io.*, java.util.*"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.ctb.bean.studentManagement.CustomerConfiguration"%>
<%@ page import="dto.StudentAccommodationsDetail"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="ctb-web.tld" prefix="ctbweb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>

<% 
    StudentAccommodationsDetail accommodations = (StudentAccommodationsDetail)request.getAttribute("accommodations");
    CustomerConfiguration[] customerConfigurations = (CustomerConfiguration[])request.getAttribute("customerConfigurations");
	Boolean viewOnly = (Boolean) request.getAttribute("viewOnly");
%>

	<input type="hidden" name="param" id="param" value="">

	<table class="legacyBodyLayout">
	    <tbody>
	    	<tr>
	        
	        	<td nowrap=""> 
	   				<h3><span>Select Bulk Accommodations</span></h3>
					<p>You may assign accommodations to all students in an Organization. Click <b>Reset</b> to clear the selections below if required.</p>
				</td>
				
			</tr>
		</tbody>
	</table>
	
			<div style="display: block;" id="moduleStudentAccommodation_show">
				<table class="Collapsible">
					<tbody><tr class="Collapsible">
						<td class="Collapsible">
							<table class="Collapsible">
								<tbody>
									<tr class="Collapsible">
										<td class="CollapsibleHeader" style="padding-top:2px">
											 
											<!--<input type="reset" value="  Reset  "/>-->
											<input type="button" value="Reset" onclick="resetRadioAccommodation()"/>
										</td>
									</tr>
								</tbody>
							</table>
						</td>
					</tr>
					</tbody>
					</table>
					</div>
					

<ctbweb:studentBulkAccommodations accommodations="<%=accommodations%>" customerConfigurations="<%=customerConfigurations%>" viewOnly="<%=viewOnly%>"/>
<br/>
