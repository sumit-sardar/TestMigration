<%@ page import="java.io.*, java.util.*"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>
<netui-data:declareBundle bundlePath="helpResources" name="help"/>

<netui-template:template templatePage="/resources/jsp/template.jsp">
    <netui-template:setAttribute name="title" value="${bundle.web['loadTest.window.title']}"/>
    <netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.loadTestSelectTests']}"/>
<netui-template:section name="bodySection">
 
 
<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->
<h1><netui:content value="${bundle.web['loadTest.title']}"/></h1>

<p><netui:content value="${bundle.web['loadTest.title.message1']}"/></p>
<p><netui:content value="${bundle.web['loadTest.title.message3']}"/>
For details, click this link: <a href="<netui:content value="/help/index.html#downloading_the_test_on_the_student_workstation.htm"/>" onClick="return showHelpWindow(this.href);"><netui:content value="Tell me more..."/></a>
</p>
<br/>
<p>
<table class="sortable">

<netui-data:repeater dataSource="requestScope.fileInfoList">
    <netui-data:repeaterHeader>

       <tr class="sortable">
                <th class="sortable alignLeft" nowrap>
                    <div class="notCurrentSort"><span>Product Type</span></div>
                </th>
                <th class="sortable alignRight" nowrap>
                    <div class="notCurrentSort"><span>Size</span></div>
                </th>
        </tr>

    </netui-data:repeaterHeader>
    <netui-data:repeaterItem>

        <tr class="sortable">
            <td class="sortable alignLeft">
        		<netui-data:getData resultId="fileName" value="${container.item.fileName}"/>    
        		<% String fileName = (String)pageContext.getAttribute("fileName"); %> 
        		          
            	<a href="<%= fileName %>">
               		<netui:span value="${container.item.displayName}" defaultValue="&nbsp;"/>
            	</a>
            </td>
            <td class="sortable alignRight">
               <netui:span value="${container.item.size}" defaultValue="&nbsp;"/>
            </td>
        </tr>
    </netui-data:repeaterItem>

</netui-data:repeater>


</table>
</p>




<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
    </netui-template:section>
</netui-template:template>

