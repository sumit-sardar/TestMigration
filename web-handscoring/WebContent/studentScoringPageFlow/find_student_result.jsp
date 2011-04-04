<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>


<%
    Boolean isStudentIdConfigurable = (Boolean)request.getAttribute("isStudentIdConfigurable"); //Start Change For CR - GA2011CR001
%>

<!--  studentList table -->
<table class="sortable">

   
        
<netui-data:repeater dataSource="requestScope.studentList">
    <netui-data:repeaterHeader>
    
    <tr class="sortable">
        
        <ctb:tableSortColumnGroup columnDataSource="actionForm.studentSortColumn" orderByDataSource="actionForm.studentSortOrderBy" anchorName="studentSearchResult">
            <th class="sortable alignLeft" width="20%" nowrap><ctb:tableSortColumn value="LoginId">Login ID</ctb:tableSortColumn></th>
            <th class="sortable alignLeft" width="20%" nowrap><ctb:tableSortColumn value="StudentName">Student Name</ctb:tableSortColumn></th>
            <th class="sortable alignLeft" width="20%" nowrap>&nbsp;&nbsp;Organization</th>
            <th class="sortable alignLeft" width="20%" nowrap><ctb:tableSortColumn value="TestSessionName">Test Session Name</ctb:tableSortColumn></th>
            <th class="sortable alignLeft" width=5%" nowrap><ctb:tableSortColumn value="Grade">Grade</ctb:tableSortColumn></th>
           <c:if test="${isStudentIdConfigurable}">   
          <th class="sortable alignLeft" width="20%" nowrap><ctb:tableSortColumn value="StudentIdNumber">${studentIdArrValue[0]}</ctb:tableSortColumn></th>
          </c:if>
          <c:if test="${!isStudentIdConfigurable}">   
        <th class="sortable alignLeft" width="20%" nowrap><ctb:tableSortColumn value="StudentIdNumber">Student ID</ctb:tableSortColumn></th>
         </c:if>
           
        </ctb:tableSortColumnGroup>
    </tr>
    
    </netui-data:repeaterHeader>
    <netui-data:repeaterItem>
    
    <tr class="sortable">
    
           <td class="sortable">
            <netui-data:getData resultId="itemCount" value="${container.item.itemCountCRAI}"/> 
             <c:if test="${itemCount > 0}">  
             <netui-data:getData resultId="rosterId" value="${container.item.rosterId}"/>  
             
             <netui-data:getData resultId="accessCode" value="${container.item.accessCode}"/> 
             <netui-data:getData resultId="userName" value="${container.item.userName}"/> 
             <netui-data:getData resultId="itemSetIdTC" value="${container.item.itemSetIdTC}"/> 
             <netui-data:getData resultId="testAdminId" value="${container.item.testAdminId}"/> 
                    <% 
                    String testSessionName = (String)pageContext.getAttribute("testSessionName");
                    String href = "beginDisplayStudItemList.do?rosterId=" + pageContext.getAttribute("rosterId")
                    		+ "&accessCode="+ pageContext.getAttribute("accessCode")
                    		+ "&itemSetIdTC="+ pageContext.getAttribute("itemSetIdTC")
                    		+ "&testAdminId="+ pageContext.getAttribute("testAdminId")
                    		+ "&userName="+ pageContext.getAttribute("userName");
                   %>
                
                    <netui:anchor href="<%= href %>" formSubmit="true">
                        <netui:span  value="${container.item.userName}" defaultValue="&nbsp;"/>
                    </netui:anchor>
        	 </c:if>
        	  <c:if test="${itemCount <= 0}">  
        	  <netui:content value="${container.item.userName}"/>
        	  </c:if>
           </td>
        <td class="sortable">
			<netui:content value="${container.item.displayName}"/>
        </td>
        <td class="sortable">
            <netui:span value="${container.item.orgNodeNamesString}"/>
        </td>
         <td class="sortable">
            <netui:span value="${container.item.testSessionName}"/>
        </td>
       
        <td class="sortable">
            <netui:span value="${container.item.grade}"/>
        </td>
        <td class="sortable">
            <netui:span value="${container.item.studentNumber}"/>
        </td>
    </tr>
    
    </netui-data:repeaterItem>
    <netui-data:repeaterFooter>
    
        <tr class="sortable">
            <td class="sortableControls" colspan="7">
                <ctb:tablePager dataSource="actionForm.studentPageRequested" summary="request.studentPagerSummary" objectLabel="${bundle.oas['object.students']}" foundLabel="Found" id="studentSearchResult" anchorName="studentSearchResult"/>
            </td>
        </tr>         
            
    </netui-data:repeaterFooter>
</netui-data:repeater>
    
</table>
