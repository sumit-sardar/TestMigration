<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>

<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>
<netui-data:declareBundle bundlePath="helpResources" name="help"/>
<%

//Start Change For CR - GA2011CR001
//Boolean isStudentIdConfigurable = (Boolean)request.getAttribute("isStudentIdConfigurable"); 
//String []studentIdArrValue = (String[])request.getAttribute("studentIdArrValue");


%>
<netui-template:template templatePage="/resources/jsp/template.jsp">
    <netui-template:setAttribute name="title" value="${bundle.web['individualStudentScoring.window.title']}"/>
    <netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.findStudent']}"/>
<netui-template:section name="bodySection">

<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->
             
<h1><netui:content value="${bundle.web['individualStudentScoring.title']}"/></h1>
<p><netui:content value="${bundle.web['individualStudentScoring.title.message']}"/></p>

<netui:form action="beginDisplayStudItemList">
<netui:hidden tagId="actionElement" dataSource="actionForm.actionElement"/> 
<netui:hidden tagId="currentAction" dataSource="actionForm.currentAction"/>
<netui:hidden tagId="accessCode" dataSource="actionForm.accessCode"/>
<netui:hidden tagId="userName" dataSource="actionForm.userName"/>
<netui:hidden tagId="testAdminId" dataSource="actionForm.testAdminId"/>
<netui:hidden tagId="rosterId" dataSource="actionForm.rosterId"/>
<netui:hidden tagId="itemSetIdTC" dataSource="actionForm.itemSetIdTC"/>
<netui:hidden  dataSource="actionForm.itemMaxPage"/> 
<h2><netui:content value="${bundle.web['individualStudentScoring.StudentDetails.title']}"/></h2>
<table class="transparent" width="100%">

<tr class="transparent">
    <td class="transparent">
        <netui:content value="${bundle.web['individualStudentScoring.StudentDetails.AccessCode']}"/>
    </td>
    <td class="transparent">    
        <div class="formValueLarge"><netui:span value="${requestScope.accessCode}" styleClass="formValueLarge"/></div>
    </td>
</tr>


<tr class="transparent">
    <td class="transparent">
        <netui:content value="${bundle.web['individualStudentScoring.StudentDetails.StudentLoginName']}"/>
    </td>
    <td class="transparent">
        <div class="formValueLarge"><netui:span value="${requestScope.userName}" styleClass="formValueLarge"/></div>
    </td>
</tr>
<tr class="transparent">
    <td class="transparent">
        <netui:content value="${bundle.web['individualStudentScoring.StudentDetails.TestSessionName']}"/>
    </td>
    <td class="transparent">
        <div class="formValueLarge"><netui:span value="${requestScope.testSessionName}" styleClass="formValueLarge"/></div>

    </td>
</tr> 
 
  
</table><br/>
 <table class="sortable" width="100%">       
<netui-data:repeater dataSource="requestScope.itemList">
    <netui-data:repeaterHeader>
    
    <tr class="sortable">
        <ctb:tableSortColumnGroup columnDataSource="actionForm.itemSortColumn" orderByDataSource="actionForm.itemSortOrderBy" anchorName="studentSearchResult">
            <th class="sortable alignLeft" width="5%" nowrap><ctb:tableSortColumn value="ItemSetOrder">Item No</ctb:tableSortColumn></th>
            <th class="sortable alignLeft" width="5%" nowrap>&nbsp;&nbsp;View Rubric </th>
            <th class="sortable alignLeft" width="20%" nowrap><ctb:tableSortColumn value="ItemSetName">Subtest Name</ctb:tableSortColumn></th>
            <th class="sortable alignLeft" width="5%" nowrap>&nbsp;&nbsp;ViewQuestion</th> 
            <th class="sortable alignLeft" width="5%" nowrap>&nbsp;&nbsp;Response</th>
            <th class="sortable alignLeft" width="20%" nowrap><ctb:tableSortColumn value="Answered">Status</ctb:tableSortColumn></th>
            <th class="sortable alignLeft" width="10%" nowrap><ctb:tableSortColumn value="ScoreStatus">Manual Scoring Status</ctb:tableSortColumn></th>
            <th class="sortable alignLeft" width="5%" nowrap>&nbsp;&nbsp;Maximum Score</th>
            <th class="sortable alignLeft" width="5%" nowrap>&nbsp;&nbsp;Score Obtained</th>
        </ctb:tableSortColumnGroup>
    </tr>
    
    </netui-data:repeaterHeader>
    <netui-data:repeaterItem>
    
    <tr class="sortable">
            
        <td class="sortable">
            <netui:span value="${container.item.itemSetOrder}"/>
        </td>
        <td class="sortable">
                    <% String href = "beginDisplayStudItemList.do"; %> 
                    <netui:anchor href="<%= href %>">
                        <netui:span value="View" defaultValue="&nbsp;"/>
                    </netui:anchor>
        </td>  
        <td class="sortable">
            <netui:span value="${container.item.itemSetName}"/>
        </td>
        <td class="sortable">
           	<netui-data:getData resultId="itemNumber" value="${container.item.itemSetOrder}"/>  
           		<%Integer itemNumber = (Integer)pageContext.getAttribute("itemNumber"); %>
             <input name="ViewQuestion" type="button" value="View Question" onclick="openViewQuestionWindow(<%=itemNumber%>); return true;"/>
                      
        </td>
         <td class="sortable">
          <netui-data:getData resultId="itemtype" value="${container.item.itemType}"/> 
           <netui-data:getData resultId="answered" value="${container.item.answered}"/> 
         	 <c:if test="${itemtype =='AI'}">  
         	  <c:if test="${answered == 'NA'}">
           		 <netui:button type="button" value="Audio Response" disabled="true"/>  
           	  </c:if>
           	  <c:if test="${answered == 'A'}">
           		 <netui:button type="button" value="Audio Response"/>  
           	  </c:if>
            </c:if>
             <c:if test="${itemtype =='CR'}">  
             	<c:if test="${answered == 'NA'}">
           		  <netui:button type="button" value="Text Response" disabled="true"/>  
           	    </c:if>
           	    <c:if test="${answered == 'A'}">
           		  <netui:button type="button" value="Text Response" />  
           	    </c:if>
           
            </c:if>     
        </td>
        <td class="sortable">
        <netui-data:getData resultId="isanswered" value="${container.item.answered}"/> 
        	<c:if test="${isanswered =='NA'}">  
            <netui:span value="Not Answered"/>
            </c:if>
             <c:if test="${isanswered =='A'}">  
           <netui:span value="Answered"/>
            </c:if>     
        </td>
        <td class="sortable">
            <netui:span value="${container.item.scoreStatus}"/>
        </td>
        <td class="sortable">
            <netui:span value="${container.item.maxPoints}"/>
        </td>
        <td class="sortable">
            <netui:span value="${container.item.scorePoint}"/>
        </td>
    </tr>
    
    </netui-data:repeaterItem>
    <netui-data:repeaterFooter>
    
        <tr class="sortable">
            <td class="sortableControls" colspan="9">
                <ctb:tablePager dataSource="actionForm.itemPageRequested" summary="request.itemPagerSummary" objectLabel="${bundle.oas['object.items']}" foundLabel="Found" id="itemSearchResult" anchorName="itemSearchResult"/>
            </td>
        </tr>         
            
    </netui-data:repeaterFooter>
</netui-data:repeater>   
</table>

<c:if test="${itemSearchResultEmpty != null}">     
    <ctb:message title="Search Result" style="informationMessage" >
          <netui:content value="${requestScope.itemSearchResultEmpty}"/>
    </ctb:message>
</c:if>
</br>

<!-- buttons -->
<p>
  
    <netui:button type="submit" value="Back" action="returnToFindStudent"/>
</p>
</netui:form>
<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
        
    </netui-template:section>
</netui-template:template>
