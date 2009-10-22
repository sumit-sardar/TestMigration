<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>

<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>

<netui-template:template templatePage="/resources/jsp/template.jsp">
    <netui-template:setAttribute name="title" value="Tables"/>
    <netui-template:section name="bodySection">

    <h1><netui:span value="Tables"/></h1>
    <p>
    TBD
    </p>

    <netui:form action="viewComplexTable">
        <ctb:complexTableContainer pageRequestedDataSource="{actionForm.pageRequested}" columnSortDataSource="{actionForm.sortColumn}" columnOrderByDataSource="{actionForm.sortOrder}">
            <table class="c">
            <tr>
                <td class="controls" colspan="4">
                    <ctb:filter visibleDataSource="{actionForm.visibleFilter}">
                        <table class="filter">
                        <tr>
                            <td><netui:span value="Last Name:"/></td>
                            <td><netui:textBox dataSource="actionForm.filterLastName" styleClass="textField"/>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <netui:button type="button" value="Apply Filter" onClick="this.form.submit();"/>
                            </td>
                        </tr>
                        </table>
                        <hr>
                    </ctb:filter>
                    <ctb:filterToggle visibleDataSource="{actionForm.visibleFilter}" />
                    <netui:button type="button" styleClass="button" value="Edit..."/>
                </td>
            </tr>
            <tr>
                <th class="alignCenter"><netui:span value="Select"/></th>
                <ctb:columnSort styleClass="alignLeft" value="firstName"><netui:span value="First Name"/></ctb:columnSort>
                <ctb:columnSort styleClass="alignLeft" value="middleName"><netui:span value="Middle Name"/></ctb:columnSort>
                <ctb:columnSort styleClass="alignLeft" value="lastName"><netui:span value="Last Name"/></ctb:columnSort>
            </tr>
            <netui-data:repeater dataSource="pageFlow.students">
                <netui-data:repeaterItem>
                <tr>
                    <td class="alignCenter">
                        <netui:radioButtonGroup dataSource="actionForm.studentId">
                            <netui:radioButtonOption value="${container.item.studentId}">&nbsp;</netui:radioButtonOption>
                        </netui:radioButtonGroup>
                    </td>
                    <td class="alignLeft"><netui:span value="${container.item.firstName}"/></td>
                    <td class="alignLeft"><netui:span value="${container.item.middleName}"/></td>
                    <td class="alignLeft"><netui:span value="${container.item.lastName}"/></td>
                </tr>
                </netui-data:repeaterItem>
                <netui-data:repeaterFooter>
                <tr>
                    <td class="controls" colspan="4">
                        <ctb:pager objectLabelDataSource="{bundle.oas['object.students']}" totalFoundDataSource="{request.totalFound}" totalFilteredDataSource="{request.totalFiltered}" totalPagesDataSource="{request.totalPages}" />
                    </td>
                </tr>
                </netui-data:repeaterFooter>        
            </netui-data:repeater>
            <ctb:noResults dataSource="{pageFlow.students}">
                <tr>
                    <td colspan="4">
                        no results message
                    </td>
                </tr>
            </ctb:noResults>
            </table>
        </ctb:complexTableContainer>


        <div class="debugger">
            <table>
            <tr>
                <th colspan="2"><netui:span value="Test Harness"/></th>
            </tr>
            <tr>
                <td>Table Results:</td>
                <td><netui:select dataSource="actionForm.testHarnessDisplayResults" optionsDataSource="${pageFlow.testHarnessResultDisplay}" styleClass="selectOption" onChange="this.form.submit();"/></td>
            </tr>
            </table>
        </div>
    
    </netui:form>


    </netui-template:section>
</netui-template:template>
