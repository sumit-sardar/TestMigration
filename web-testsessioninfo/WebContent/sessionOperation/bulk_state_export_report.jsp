<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="label.tld" prefix="lb" %>
<lb:bundle baseName="testsessionApplicationResource" />

<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="helpResources" name="help"/>


<netui-template:template templatePage="/resources/jsp/oas_template.jsp">
<netui-template:setAttribute name="title" value="${bundle.web['reports.window.title']}"/>
    <netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.reports']}"/>
    <netui-template:section name="bodySection">
    <netui:form action="tabeBulkStateReporting">
    <input type="hidden" id="menuId" name="menuId" value="reportsLink" />
		<table width="97%" style="margin:15px auto;" border="0"> 
			<tr>
				<td style="padding-left:5px;">
		    		<h1><lb:label key="tabe.bulk.state.report.link" /></h1>
					<p style="color:#000"><lb:label key="tabe.bulk.state.report.msg" /></p> 
					<br/>		
				</td>
			</tr>
		</table>
	</netui:form>
	</netui-template:section>
</netui-template:template>
