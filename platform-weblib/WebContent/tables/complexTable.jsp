<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="org.apache.struts.action.ActionForm"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>

<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>

<netui-template:template templatePage="/resources/jsp/template.jsp">
    <netui-template:setAttribute name="title" value="Complex Table"/>
    <netui-template:section name="bodySection">

    <h1><netui:span value="Complex Table"/></h1>
    <p>
    The "complex" table is really nothing more than an assembly of various components. In this example,
    the following tags were used: 
    TableTabGroup, TableTab, TableSortColumnGroup, TableSortColumn, TableFilter, TableFilterToggle,
    TableNoResults, and TablePager.
    </p>
    
    <netui:form action="viewComplexTable">
        <ctb:tableTabGroup dataSource="actionForm.filter_tab">
            <ctb:tableTab value="tab1"><netui:span value="${bundle.widgets['tab.current']}"/></ctb:tableTab>
            <ctb:tableTab value="tab2"><netui:span value="${bundle.widgets['tab.completed']}"/></ctb:tableTab>
        </ctb:tableTabGroup>
        <table class="sortable">
        <tr class="sortable">
            <td class="sortableControls" colspan="4">

                <ctb:tableFilter dataSource="actionForm.filterVisible">
                    <table class="tableFilter">
                    <tr class="tableFilter">
                        <td class="tableFilter alignLeft">
                            <netui:span value="Left"/>
                        </td>
                        <td class="tableFilter alignCenter">
                            <netui:span value="Center"/>
                        </td>
                        <td class="tableFilter alignRight">
                            <netui:span value="Right"/>
                        </td>
                    </tr>
                    </table>
                    <hr class="simpleControls"/>
                </ctb:tableFilter>
                
                <table class="tableFilter">
                <tr class="tableFilter">
                    <td class="tableFilter">
                        <ctb:tableFilterToggle dataSource="actionForm.filterVisible" />
                        <netui:button styleClass="button" type="button" value="View"/>
                        <netui:button styleClass="button" type="button" value="Edit"/>
                    </td>
                </tr>
                </table>
                
            </td>
        </tr>
        <netui-data:repeater dataSource="pageFlow.students">
            <netui-data:repeaterHeader>
                <tr class="sortable">
                    <ctb:tableSortColumnGroup columnDataSource="actionForm.sortColumn" orderByDataSource="actionForm.sortOrderBy" >
                    <th class="sortable alignLeft"><ctb:tableSortColumn value="studentNumber"><netui:span value="Student Number"/></ctb:tableSortColumn></th>
                    <th class="sortable alignLeft"><ctb:tableSortColumn value="firstName"><netui:span value="First Name"/></ctb:tableSortColumn></th>
                    <th class="sortable alignLeft"><ctb:tableSortColumn value="middleName"><netui:span value="Middle Name"/></ctb:tableSortColumn></th>
                    <th class="sortable alignLeft"><ctb:tableSortColumn value="lastName"><netui:span value="Last Name"/></ctb:tableSortColumn></th>
                    </ctb:tableSortColumnGroup>
                </tr>
            </netui-data:repeaterHeader>
            <netui-data:repeaterItem>
                <tr class="sortable">
                    <td class="sortable"><netui:span value="${container.item.studentNumber}" defaultValue="&nbsp;"/></td>
                    <td class="sortable"><netui:span value="${container.item.firstName}" defaultValue="&nbsp;"/></td>
                    <td class="sortable"><netui:span value="${container.item.middleName}" defaultValue="&nbsp;"/></td>
                    <td class="sortable"><netui:span value="${container.item.lastName}" defaultValue="&nbsp;"/></td>
                </tr>
            </netui-data:repeaterItem>
            <netui-data:repeaterFooter>
                <tr class="sortable">
                    <td class="sortableControls" colspan="4">
                        <ctb:tablePager dataSource="actionForm.pageRequested" summary="request.somePagerSummary" objectLabel="${bundle.oas['object.students']}" />
                    </td>
                </tr>
            </netui-data:repeaterFooter>
        </netui-data:repeater>
        <ctb:tableNoResults dataSource="request.showNoResultSection">
            <tr class="sortable">
                <td class="sortable" colspan="4">
                    <ctb:message title="No Students Found." style="tableMessage">
                        <netui:span value="Sorry, no students were found.  Please try again."/>
                    </ctb:message>
                </td>
            </tr>
        </ctb:tableNoResults>
        </table>
    </netui:form>



    </netui-template:section>
</netui-template:template>

