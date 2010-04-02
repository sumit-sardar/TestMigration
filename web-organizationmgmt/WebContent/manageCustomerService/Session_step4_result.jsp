<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>
<table class="sortable">

    <tr class="sortable">
        <td class="sortableControls" colspan="3">
        &nbsp;
<table class="tableFilter">
    <tr class="tableFilter">
        <td class="tableFilter" width="100" align="right">Ticket ID:</td>
        <td class="tableFilter" width="*"><netui:textBox tagId="testAdminId" dataSource="actionForm.studentProfile.testAdminId" tabindex="1"/></td>
        <td class="tableFilter" width="*">
        &nbsp;&nbsp;
        <td class="tableFilter" width="100" alighn="right">Request Description:</td>
        <td class="tableFilter" width="*"><netui:textBox tagId="RequestDescription" dataSource=""/>
    </tr>
    <tr class="tableFilter">
        <td class="tableFilter" width="100" align="right">Requestor:</td>
        <td class="tableFilter" width="*"><netui:textBox tagId="Requestor" dataSource="actionForm.studentProfile.Requestor" tabindex="5"/></td>
        
    </tr>
   
</table>    
<br/>       </td>
    </tr>
  <tr class="sortable">
            <th class="sortable alignLeft" width="30%" nowrap><ctb:tableSortColumn value="StudentName">Student</ctb:tableSortColumn></th>
            <th class="sortable alignLeft" width="30%" nowrap><ctb:tableSortColumn value="SubtestStatus">Subtest Status</ctb:tableSortColumn></th>
            <th class="sortable alignLeft" width="30%" nowrap><ctb:tableSortColumn value="StartDate">Start Date</ctb:tableSortColumn></th>
            <th class="sortable alignLeft" width="30%" nowrap><ctb:tableSortColumn value="CompletionDate">Completion Date</ctb:tableSortColumn></th>
			<th class="sortable alignLeft" width="30%" nowrap><ctb:tableSortColumn value="ItemsAnswered">Items Answered</ctb:tableSortColumn></th>
			<th class="sortable alignLeft" width="30%" nowrap><ctb:tableSortColumn value="TimeSpent">Time Spent</ctb:tableSortColumn></th>
    </tr>
    
    <netui-data:repeaterItem>
        <tr class="sortable">
                
        <td class="sortable">
            <netui:span value="${container.item.studentName}"/>
        </td>
        <td class="sortable">
            <netui:span value="${container.item.subtestStatus}"/>
        </td>
        <td class="sortable">
            <netui:span value="${container.item.startDate}"/>
        </td>
        <td class="sortable">
            <netui:span value="${container.item.completionDate}"/>
        </td>
        <td class="sortable">
            <netui:span value="${container.item.itemsAnswered}"/>
        </td>
        <td class="sortable">
            <netui:span value="${container.item.timeSpent}"/>
        </td>
    </tr>
    
    </netui-data:repeaterItem>
              
</table>