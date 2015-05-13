<%@ page import="java.io.*, java.util.*"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="ctb-web.tld" prefix="ctbweb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="label.tld" prefix="lb" %>
<lb:bundle baseName="testsessionApplicationResource" />
<%
    List reportList = (List)request.getAttribute("reportList");
    String userAgent = request.getHeader("User-Agent").toLowerCase();

    String displayStyle = " style=\"display: block\"";
    if (userAgent.indexOf("firefox") != -1) {
        displayStyle = " style=\"display: inline\"";
    }    
%>

<!-- TURNLEAF REPORT LIST -->
<ctbweb:reportNameList reportList = "<%= reportList %>" displayStyle = "<%= displayStyle %>"/>

<!-- TABE LACES REPORT EXPORT -->
<c:if test="${sessionScope.hasBulkStateReportExport}">
<ctb:auth roles="Administrator, Administrative Coordinator">
	<div id="bulkStateReport" style="display: inline">
		<table class="transparent">
			<tr class="transparent">
				<td class="transparent" width="32" valign="top"> </td>
				<td class="transparent" width="650" valign="top">
					<li style="list-style-type: square;">
						<a style="display: inline" href="/SessionWeb/sessionOperation/tabeBulkStateReporting.do">						
						<lb:label key="tabe.bulk.state.report.link" />
						</a>
					</li>
					<lb:label key="tabe.bulk.state.report.instruction" />				
				</td>
			</tr>
		</table>
	</div> 
</ctb:auth>
</c:if>