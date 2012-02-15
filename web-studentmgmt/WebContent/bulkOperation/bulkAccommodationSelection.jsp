<%@ page import="java.io.*, java.util.*"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.ctb.bean.studentManagement.CustomerConfiguration"%>
<%@ page import="dto.StudentAccommodationsDetail"%>
<%@ taglib uri="ctb-web.tld" prefix="ctbweb"%>
 

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
	   				<span><b><lb:label key="stu.msg.accomPopupTitleText" /></b></span>
					<p style="margin: 0 0 4px !important;"><lb:label key="stu.msg.accomPopupSubmitText" /></p>
					<p style="margin: 0 0 4px !important;"><lb:label key="stu.msg.accomPopupResetText" /></p>
				</td>
				
			</tr>
		</tbody>
	</table>
	
			
					

<ctbweb:studentBulkAccommodations accommodations="<%=accommodations%>" customerConfigurations="<%=customerConfigurations%>" viewOnly="<%=viewOnly%>"/>
<br/>
