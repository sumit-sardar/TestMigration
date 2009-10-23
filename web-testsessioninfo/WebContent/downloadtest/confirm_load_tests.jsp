<%@ page import="java.io.*, java.util.*"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 
 <%
String userAgent = request.getHeader("User-Agent").toLowerCase();
Boolean ie7 = new Boolean(userAgent.indexOf("msie 7") > 0);        
Boolean fireFox = new Boolean(userAgent.indexOf("firefox") > 0);        
Boolean mac = new Boolean(userAgent.indexOf("mac") > 0);        

String objectBankPath = "C:\\Program Files\\CTB\\Online Assessment\\data\\objectbank\\";
if (mac.booleanValue()) {
    objectBankPath = "/Library/Application Support/LockdownBrowser/ObjectBank";
}
%>

<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>
<netui-data:declareBundle bundlePath="helpResources" name="help"/>

<netui-template:template templatePage="/resources/jsp/template.jsp">
    <netui-template:setAttribute name="title" value="${bundle.web['loadTest.confirmSelection.window.title']}"/>
    <netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.loadTestConfirm']}"/>
<netui-template:section name="bodySection">
 
<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->
<% if ( ie7.booleanValue() ) { %>
<script type="text/javascript">
    document.writeln('<OBJECT ID="LDBJSBridgeCTL" CLASSID="CLSID:56871AC2-4683-4D8E-B5EB-E7E3AA40DD52" VIEWASTEXT WIDTH=0 HEIGHT = 0></OBJECT>' );
</script>
<% } %>

<h1><netui:content value="${bundle.web['loadTest.confirmSelections.title']}"/></h1>
<p><netui:content value="${bundle.web['loadTest.confirmSelections.title.message']}"/></p>
<br>
<p><netui:content value="${bundle.web['loadTest.confirmSelections.tableTitle']}"/></p>
<table class="sortable">
<netui:form action="confirm_load_tests">
    <netui:hidden dataSource="actionForm.actionElement"/>
    <netui:hidden dataSource="actionForm.maxPage"/>
    
<input type="hidden" id="objectBankPath" name="objectBankPath" value="<%=objectBankPath%>" />     

<% if ( ie7.booleanValue() ) { %>
<script type="text/javascript">
    var objectBankPath =  LDBJSBridgeCTL.GetRegistryKeyValue("ObjectbankPath"); 
    document.getElementById("objectBankPath").value = objectBankPath;
</script>
<% } %>

<netui-data:repeater dataSource="requestScope.tests">
    <netui-data:repeaterHeader>

        <tr class="sortable">
            <ctb:tableSortColumnGroup columnDataSource="actionForm.testSortColumn" 
                                      orderByDataSource="actionForm.testSortOrderBy" >
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
            <td class="sortable alignLeft">
                <netui:span value="${container.item.itemSetName}" defaultValue="&nbsp;">
                </netui:span>
            </td>
            <td class="sortable alignCenter">
                <netui:span value="${container.item.itemSetLevel}" defaultValue="&nbsp;"/>
            </td>
            <td class="sortable alignCenter">
                <netui:span value="${container.item.itemSetGrade}" defaultValue="&nbsp;"/>
            </td>            
            <td class="sortable alignRight">
                <netui:span value="${container.item.displayContentSize}" defaultValue="&nbsp;">
                </netui:span>
            </td>
        </tr>

    </netui-data:repeaterItem>
    <netui-data:repeaterFooter>
        <tr class="sortable">
            <td class="sortableControls" colspan="4">
                <ctb:tablePager dataSource="actionForm.testPageRequested" 
                                summary="request.testPagerSummary" 
                                objectLabel="${bundle.oas['object.tests']}" />
            </td>
        </tr>       
    </netui-data:repeaterFooter>
    
</netui-data:repeater>
    
    
<script type="text/javascript">
</script>
    
    
</netui:form>
</table>
<br/>
<p><b>
<netui:content value="${bundle.web['loadTest.confirmSelections.message']}"/>
<netui:content value="${requestScope.totalLoadSize}"/>
<netui:content value="${bundle.web['loadTest.MB']}"/></b></p>

<netui:button styleClass="button" tagId="backButton" value=" Back " type="submit" onClick="submitFormAndSwitchPage('returnto_select_tests.do')"/>
&nbsp;
<netui:button styleClass="button" tagId="loadTestButton" value="Load Test" type="submit" onClick="submitFormAndSwitchPage('load_progress.do')"/>
&nbsp;
<netui:button styleClass="button" tagId="cancelButton" value="Cancel" type="submit" onClick="submitFormAndSwitchPage('goto_homepage.do')"/>

    </netui-template:section>
</netui-template:template>