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
 
<%
String userAgent = request.getHeader("User-Agent").toLowerCase();
Boolean windowsVista = new Boolean(userAgent.indexOf("windows nt 6.0") > 0);  
%>
 
<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->
<h1><netui:content value="${bundle.web['loadTest.title']}"/></h1>
<div id="showContent">
<p><netui:content value="${bundle.web['loadTest.title.message1']}"/></p>
<div id="showSelectTestError"
     style="<c:out value="${selectTestErrorStyle}"/>">
    <ctb:message title="${bundle.web['loadTest.selectTest.warning.title']}" style="alertMessage">
        <netui:content value="${bundle.web['loadTest.selectTest.warning.message']}"/>
    </ctb:message>
    <br/>
</div>
<p><netui:content value="${bundle.web['loadTest.title.message2']}"/></p>
<p align="right">

<a href="<netui:content value="/help/index.html#downloading_the_test_on_the_student_workstation.htm"/>" onClick="return showHelpWindow(this.href);"><netui:content value="Tell me more..."/></a>

</p>
<p><h2><netui:content value="${bundle.web['loadTest.selectTests.title']}"/></h2></p>
<p><netui:content value="${bundle.web['loadTest.selectTests.title.message1']}"/></p>
<p><netui:content value="${bundle.web['loadTest.selectTests.title.message2']}"/></p>

<table class="sortable">
<netui:form action="select_tests">
    <netui:hidden dataSource="actionForm.actionElement"/>
    <netui:hidden dataSource="actionForm.maxPage"/>
    <tr class="sortable">
        <td class="sortableControls" colspan="5">
            <table class="tableFilter">
            <tr class="tableFilter">
                <td class="tableFilter">        
                    <netui:button styleClass="button" tagId="selectAllTests" value="${bundle.widgets['button.selectAllTests']}" type="submit" action="selectAllTests" disabled="${requestScope.noTestToDownload}"/>
                    <netui:button styleClass="button" tagId="deselectAllTests" value="${bundle.widgets['button.deselectAllTests']}" type="submit" action="deselectAllTests" disabled="${requestScope.deselectAllTestDisableButton}"/>
                </td>
            </tr>
            </table>
        </td>
    </tr>
<netui-data:repeater dataSource="requestScope.tests">
    <netui-data:repeaterHeader>

        <tr class="sortable">
            <ctb:tableSortColumnGroup columnDataSource="actionForm.testSortColumn" 
                                      orderByDataSource="actionForm.testSortOrderBy" >
                <th class="sortable alignCenter">
                    <netui:span value="${bundle.web['common.column.select']}"/>
                </th>                
                <th class="sortable alignLeft" nowrap>
                    <ctb:tableSortColumn value="ItemSetName">
                            <netui:span value="${bundle.web['common.column.testName']}"/>
                    </ctb:tableSortColumn>
                </th>
                <th class="sortable alignCenter" nowrap>
                    <ctb:tableSortColumn value="ItemSetLevel">
                            <netui:span value="${bundle.web['common.column.level']}"/>
                    </ctb:tableSortColumn>
                </th>                
                <th class="sortable alignCenter" nowrap>
                    <ctb:tableSortColumn value="ItemSetGrade">
                            <netui:span value="${bundle.web['common.column.grade']}"/>
                    </ctb:tableSortColumn>
                </th>                
                <th class="sortable alignRight" nowrap>
                    <ctb:tableSortColumn value="ContentSize">
                        <netui:span value="${bundle.web['common.column.size']}"/>
                    </ctb:tableSortColumn>
                </th>
            </ctb:tableSortColumnGroup>
        </tr>
            
    </netui-data:repeaterHeader>
    <netui-data:repeaterItem>

        <tr class="sortable">
            <td class="sortable alignCenter">
                <netui:checkBoxGroup dataSource="actionForm.itemSetIds">
                    &nbsp;
                    <netui:checkBoxOption value="${container.item.itemSetId}" onClick="enableDeselectAllTestButton(getNetuiTagName('deselectAllTests')); enableDeselectAllTestButton(getNetuiTagName('nextButton'));">
                        &nbsp;
                    </netui:checkBoxOption>
                </netui:checkBoxGroup>
            </td>
            <td class="sortable alignLeft">
                <netui:anchor action="goto_view_test_sessions" formSubmit="true">
                    <netui:span value="${container.item.itemSetName}" defaultValue="&nbsp;"/>
                    <netui:parameter name="itemSetId" value="${container.item.itemSetId}"/>
                    <netui:parameter name="itemSetName" value="${container.item.itemSetName}"/>
               </netui:anchor>
            </td>
            <td class="sortable alignCenter">
                <netui:span value="${container.item.itemSetLevel}" defaultValue="&nbsp;"/>
            </td>
            <td class="sortable alignCenter">
                <netui:span value="${container.item.itemSetGrade}" defaultValue="&nbsp;"/>
            </td>
            <td class="sortable alignRight">
                <netui:span value="${container.item.displayContentSize}" defaultValue="&nbsp;"/>
            </td>
        </tr>

    </netui-data:repeaterItem>
    <netui-data:repeaterFooter>
        <tr class="sortable">
            <td class="sortableControls" colspan="5">
                <ctb:tablePager dataSource="actionForm.testPageRequested" 
                                summary="request.testPagerSummary" 
                                objectLabel="${bundle.oas['object.tests']}" />
            </td>
        </tr>       
    </netui-data:repeaterFooter>
    
</netui-data:repeater>
    
</netui:form>
    <tr class="simple" style="<c:out value="${noTestErrorStyle}"/>">
        <td class="simple" colspan="5">
            <ctb:message style="tableMessage" 
                         title="${bundle.web['loadTest.noTest.warning.title']}" >
                <netui:span value="${bundle.web['loadTest.noTest.warning.message']}"/>
            </ctb:message>
        </td>
    </tr>
</table>
<p>
<br/>
<netui:button styleClass="button" tagId="nextButton" value=" Next " type="button" onClick="submitFormAndSwitchPage('tests_selected.do')" disabled="${requestScope.deselectAllTestDisableButton}"/>
&nbsp;
<netui:button styleClass="button" tagId="cancelButton" value="Cancel" type="button" onClick="submitFormAndSwitchPage('goto_homepage.do')"/>

</p>
</div>
<div id="showDMError" style="display:none">
    <ctb:message title="${bundle.web['loadTest.installClient.warning.title']}" style="alertMessage">
        <netui:content value="${bundle.web['loadTest.installClient.warning.message1']}"/>
        <a href="goto_downloadclient.do"><netui:content value="${bundle.web['loadTest.installClient.warning.link']}"/></a>
        <netui:content value="${bundle.web['loadTest.installClient.warning.message2']}"/>
    </ctb:message>
</div>
<div id="showPCNotIEError" style="display:none">
    <ctb:message title="${bundle.web['loadTest.pcNotIe.error.title']}" style="errorMessage">
        <netui:content value="${bundle.web['loadTest.pcNotIe.error.message']}"/>
    </ctb:message>
</div>
<div id="showMacOSError" style="display:none">
    <ctb:message title="${bundle.web['loadTest.macNotSupported.error.title']}" style="alertMessage">
        <netui:content value="${bundle.web['loadTest.macNotSupported.error.message']}"/>
    </ctb:message>
</div>
<div id="showJavaVersionError" style="display:none">
    <ctb:message title="${bundle.web['loadTest.javaVersionNotSupported.error.title']}" style="errorMessage">
        <netui:content value="${bundle.web['loadTest.javaVersionNotSupported.error.message']}"/>
    </ctb:message>
</div>
 

<script>
	checkForDownloadManager();
</script>


<% if ( windowsVista.booleanValue() ) { %>
<applet name="JavaVersionApplet" height="0" width="0" code="JavaVersionApplet.class"></applet>
<script type="text/javascript">
	checkForJavaVersion();
</script>
<% } %>


<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
    </netui-template:section>
</netui-template:template>

