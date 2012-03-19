<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>

<script type="text/javascript"> 

	function openImmediateStudentScorePdfReport( anchor, rosterId, testAdminId, sessionName ) {
	    var url = "/ImmediateReportWeb/immediateReportByStudent/studentsImmediateScoreReportInPDF.do";
	    anchor.href  = url;
	    anchor.href += "?testAdminId=" + testAdminId;
	    anchor.href += "&rosterId=" + rosterId;
	    anchor.href += "&sessionName=" + sessionName;
	    return true;
	};
	
	function openImmediateStudentScoreCSVReport( anchor, rosterId, testAdminId) {
	    var url = "/ImmediateReportWeb/immediateReportByStudent/studentsImmediateScoreReportInCSV.do";
	    anchor.href  = url;
	    anchor.href += "?testAdminId=" + testAdminId;
	    anchor.href += "&rosterId=" + rosterId;
	    return true;
	};


</script>

<!--

//-->
</script>

<%
    Boolean isStudentIdConfigurable = (Boolean)request.getAttribute("isStudentIdConfigurable"); //Start Change For CR - GA2011CR001
%>

<!--  studentList table -->
<table class="sortable">

   
        
<netui-data:repeater dataSource="requestScope.studentList">
    <netui-data:repeaterHeader>
    
    <tr class="sortable">
        
        <ctb:tableSortColumnGroup columnDataSource="actionForm.studentSortColumn" orderByDataSource="actionForm.studentSortOrderBy" anchorName="studentSearchResult">
            <th class="sortable alignLeft" width="10%" nowrap><ctb:tableSortColumn value="LoginId">Login ID</ctb:tableSortColumn></th>
            <th class="sortable alignLeft" width="10%" nowrap><ctb:tableSortColumn value="StudentName">Student Name</ctb:tableSortColumn></th>
            <th class="sortable alignLeft" width="10%" nowrap><ctb:tableSortColumn value="Grade">Grade</ctb:tableSortColumn></th>
            <th class="sortable alignLeft" width="10%" nowrap>&nbsp;Content area(s) completed and scored&nbsp;</th>
            <th class="sortable alignLeft" width=10%" nowrap><ctb:tableSortColumn value="TestSessionName">Administration name</ctb:tableSortColumn></th>
            <th class="sortable alignLeft" width="10%" nowrap><ctb:tableSortColumn value="TestWindowOpenDate">Administration date</ctb:tableSortColumn></th>
          	<th class="sortable alignLeft" width="10%" nowrap>&nbsp;Teacher name</th>
        	<th class="sortable alignLeft" width="10%" nowrap><ctb:tableSortColumn value="Form">Form</ctb:tableSortColumn></th>
        	<th class="sortable alignLeft" width="10%" nowrap><ctb:tableSortColumn value="DefaultScheduler">Created By</ctb:tableSortColumn></th>
        	<th class="sortable alignLeft" width="10%" nowrap>&nbsp;Download</th>
           
        </ctb:tableSortColumnGroup>
    </tr>
    
    </netui-data:repeaterHeader>
    <netui-data:repeaterItem>
    
    <tr class="sortable">
    
           <td class="sortable">
             
             <netui-data:getData resultId="rosterId" value="${container.item.rosterId}"/> 
             <netui-data:getData resultId="testAdminId" value="${container.item.testAdminId}"/> 
                    <% 
                    String testSessionName = (String)pageContext.getAttribute("testSessionName");
                    String href = "beginDisplayStudScoringReport.do?rosterId=" + pageContext.getAttribute("rosterId")
                    		+ "&testAdminId="+ pageContext.getAttribute("testAdminId");
                   %>
                
                    <netui:anchor href="<%= href %>" formSubmit="true">
                        <netui:span  value="${container.item.userName}" defaultValue="&nbsp;"/>
                    </netui:anchor>
        	  
           </td>
        <td class="sortable">
			<netui:content value="${container.item.displayName}"/>
        </td>
        <td class="sortable">
            <netui:span value="${container.item.grade}"/>
        </td>
        <td class="sortable">
            <netui:span value="${container.item.contentAreaString}"/>
        </td>
         <td class="sortable">
            <netui:span value="${container.item.testSessionName}"/>
        </td>
       
        <td class="sortable">
            <netui:span value="${container.item.testWindowOpenDateString}"/>
        </td>
        <td class="sortable">
            <netui:span value="${container.item.organizationNames}"/>
        </td>
        <td class="sortable">
            <netui:span value="${container.item.form}"/>
        </td>
        <td class="sortable">
            <netui:span value="${container.item.defaultScheduler}"/>
        </td>
        <td class="sortable">
        			<% 
                    String sessionName = (String)pageContext.getAttribute("testSessionName");
                    String method = "return openImmediateStudentScorePdfReport(this,"+  pageContext.getAttribute("rosterId")+ " ,"+ pageContext.getAttribute("testAdminId")+" , "+sessionName+" );";
                   %>
            <netui:anchor href="#" onClick="<%= method %>" ;>PDF</netui:anchor>
            <br>
            <%
            	method = "return openImmediateStudentScoreCSVReport(this,"+  pageContext.getAttribute("rosterId")+ " ,"+ pageContext.getAttribute("testAdminId")+");";
            %>
            <netui:anchor href="#" onClick="<%= method %>" ;>CSV</netui:anchor>
        </td>
    </tr>
    
    </netui-data:repeaterItem>
    <netui-data:repeaterFooter>
    
        <tr class="sortable">
            <td class="sortableControls" colspan="10">
                <ctb:tablePager dataSource="actionForm.studentPageRequested" summary="request.studentPagerSummary" objectLabel="${bundle.oas['object.students']}" foundLabel="Found" id="studentSearchResult" anchorName="studentSearchResult"/>
            </td>
        </tr>         
            
    </netui-data:repeaterFooter>
</netui-data:repeater>
    
</table>
