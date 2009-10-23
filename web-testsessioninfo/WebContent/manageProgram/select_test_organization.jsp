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
<% 
    Boolean multiplePrograms = (Boolean)request.getAttribute("multiplePrograms"); 
    Boolean multipleTests = (Boolean)request.getAttribute("multipleTests"); 
    Boolean singleTest = (Boolean)request.getAttribute("singleTest");
    Boolean noTest = (Boolean)request.getAttribute("noTest");
%> 
        

<table class="sortable">

    <tr class="sortable">
        <th class="sortable alignLeft" height="25" colspan="2">&nbsp;&nbsp;<span>Program Information</span></th>
    </tr>

    <tr class="normal" valign="top">
        <td class="normal" width="350" valign="top">
    
            <table class="transparent">
                <tr class="normal">
                    <td class="normal">Customer:</td>
                    <td class="normal"><netui:span value="${pageFlow.customerName}" defaultValue="&nbsp;"/></td>
                </tr>
                <tr class="normal">
                    <td class="normal">Program:</td>
                    <td class="normal">
                        <c:if test="${!multiplePrograms}">
                            <netui:span value="${pageFlow.programName}" defaultValue="&nbsp;"/>
                            <netui:hidden dataSource="actionForm.selectedProgramId"/>
                        </c:if>
                        <c:if test="${multiplePrograms}">
                        <netui:select optionsDataSource="${pageFlow.programList}" dataSource="actionForm.selectedProgramId" size="1" onChange="setElementValueAndSubmit('{actionForm.currentAction}', 'onProgramChange');"/>
                        </c:if>
                    </td>
                </tr>
                <tr class="normal">
                    <td class="normal">Organization:</td>
                    <td class="normal"><netui:span value="${actionForm.selectedOrgNodeName}" defaultValue="&nbsp;"/></td>
                </tr>
                <tr class="normal">
                    <td class="normal">Test:</td>
                    <td class="normal">
                        <c:if test="${noTest}">
                            <netui:span value="${bundle.web['manageProgram.noTest.label']}" defaultValue="&nbsp;"/>
                        </c:if>
                        <c:if test="${singleTest}">
                            <netui:span value="${pageFlow.testName}" defaultValue="&nbsp;"/>
                            <netui:hidden dataSource="actionForm.selectedTestId"/>
                        </c:if>
                        <c:if test="${multipleTests}">
                        <netui:select optionsDataSource="${pageFlow.testList}" dataSource="actionForm.selectedTestId" size="1" onChange="setElementValueAndSubmit('{actionForm.currentAction}', 'onTestChange');"/>
                        </c:if>
                    </td>
                </tr>    
            </table>

        </td>

        
        <td class="normal2" valign="top">        

            <table class="sortable">
                <tr class="sortable">
                    <td class="sortableControls" colspan="3">
                        <ctb:tablePathList 	valueDataSource="actionForm.orgNodeId" 
                        					labelDataSource="actionForm.orgNodeName" 
                        					pathList="request.orgNodePath" />
                    </td>
                </tr>
            
            <netui-data:repeater dataSource="requestScope.orgNodes">
                <netui-data:repeaterHeader>
                
                <tr class="sortable">
                    <ctb:tableSortColumnGroup columnDataSource="actionForm.orgSortColumn" 
                    						  orderByDataSource="actionForm.orgSortOrderBy" >
                        <th class="sortable alignCenter" width="50" nowrap><netui:content value="${bundle.web['common.column.select']}"/></th>                
                        <th class="sortable alignLeft" width="100%" nowrap><ctb:tableSortColumn value="OrgNodeName"><netui:content value="${requestScope.orgCategoryName}"/></ctb:tableSortColumn></th>
                        <th class="sortable alignLeft" width="100%" nowrap><ctb:tableSortColumn value="OrgNodeCode"><netui:content value="${bundle.web['common.column.orgCode']}"/></ctb:tableSortColumn></th>
                    </ctb:tableSortColumnGroup>
                </tr>
                
                </netui-data:repeaterHeader>
                <netui-data:repeaterItem>
                
                <tr class="sortable">
                    <td class="sortable alignCenter">
                        <netui:radioButtonGroup dataSource="actionForm.selectedOrgNodeId">
                            &nbsp;<netui:radioButtonOption value="${container.item.id}" onClick="setElementValueAndSubmit('{actionForm.actionElement}', '{actionForm.actionElement}');">&nbsp;</netui:radioButtonOption>                
                        </netui:radioButtonGroup>
                    </td>        
                    <td class="sortable alignLeft">     
                        <ctb:tablePathEntry srcLabelDataSource="${container.item.name}" 
                        					srcValueDataSource="${container.item.id}" 
                        					dstLabelDataSource="actionForm.orgNodeName" 
                        					dstValueDataSource="actionForm.orgNodeId" 
                        					shownAsLink="${container.item.hasChildren}"/>
                    </td>
                    <td class="sortable alignLeft">     
                        <netui:span value="${container.item.orgCode}" defaultValue="&nbsp;"/>
                    </td>
                </tr>
                
                </netui-data:repeaterItem>
                <netui-data:repeaterFooter>
                
                    <tr class="sortable">
                        <td class="sortableControls" colspan="3">
                            <ctb:tablePager dataSource="actionForm.orgPageRequested" 
                            				summary="request.orgPagerSummary" 
                            				objectLabel="${bundle.oas['object.organizations']}" id="tablePathListAnchor" anchorName="tablePathListAnchor"/>
                        </td>
                    </tr>         
                         
                </netui-data:repeaterFooter>
            </netui-data:repeater>
                
            </table>
                        
        </td>
    </tr>

</table>
