<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.ctb.bean.testAdmin.ScorableItem"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>

<netui-data:declareBundle bundlePath="oasResources" name="oas" />
<netui-data:declareBundle bundlePath="webResources" name="web" />
<netui-data:declareBundle bundlePath="widgetResources" name="widgets" />
<netui-data:declareBundle bundlePath="helpResources" name="help" />
<netui-template:template templatePage="/resources/jsp/scoring_template.jsp">
	<!-- 
template_find_student.jsp
-->

	<netui-template:setAttribute name="title" value="${bundle.web['scoringByItem.window.title']}" />
	<netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.handScoring']}" />
	<netui-template:section name="bodySection">

		<!-- ********************************************************************************************************************* -->
		<!-- Start Page Content -->
		<!-- ********************************************************************************************************************* -->
		<h1><netui:content value="${pageFlow.pageTitle}" /></h1>


		<!-- title -->

		<p><netui:content value="${bundle.web['ItemList.window.subHeading']}" /><br />
		</p>
		
		<netui:form action="findItemDetails">
        <netui:hidden tagId="actionElement" dataSource="actionForm.actionElement"/> 
        <netui:hidden tagId="currentAction" dataSource="actionForm.currentAction"/>
        <netui:hidden  dataSource="actionForm.itemMaxPage"/>
        <netui:hidden tagId="testAdminId" dataSource="actionForm.testAdminId"/>
        <netui:hidden tagId="testSessionName" dataSource="actionForm.testSessionName"/>
        <netui:hidden tagId="testAccessCode" dataSource="actionForm.testAccessCode"/>
		
		<table width="100%" cellpadding="0" cellspacing="0" class="transparent">
				<tr class="transparent">
					<td class="transparent" ><netui:content value="${bundle.web['common.column.accessCode']}" /></td>
					<td class="transparent">    
        <div class="formValueLarge"><netui:span value="${actionForm.testAccessCode}" styleClass="formValueLarge"/></div>
    </td>
				</tr>
				<tr valign="transparent">
					<td  class="transparent"><netui:content value="${bundle.web['common.column.testSessionName']}" /></td>
					<td class="transparent">    
        <div class="formValueLarge"><netui:span value="${actionForm.testSessionName}" styleClass="formValueLarge"/></div>
    </td>
				</tr>
				
			</table>
			<br/><br/>
			
			
			
			<table class="sortable" width="100%">       
<netui-data:repeater dataSource="requestScope.itemList">
    <netui-data:repeaterHeader>
    
    <tr class="sortable">
        <ctb:tableSortColumnGroup columnDataSource="actionForm.itemSortColumn" orderByDataSource="actionForm.itemSortOrderBy" id="itemSearchResult" anchorName="itemSearchResult">
            <th class="sortable" width="5%" align="center" ><ctb:tableSortColumn value="ItemSetOrder">&nbsp;Item No.</ctb:tableSortColumn></th>
            <th class="sortable" width="20%" align="center" nowrap><ctb:tableSortColumn value="ItemSetName">Subtest Name</ctb:tableSortColumn></th>

            <th class="sortable" width="20%" align="center"><ctb:tableSortColumn value="ItemType">&nbsp;Item Type</ctb:tableSortColumn></th>
            <th class="sortable" width="10%" align="center" >&nbsp;Maximum Score</th>
          
        </ctb:tableSortColumnGroup>
    </tr>
    
    </netui-data:repeaterHeader>
    <netui-data:repeaterItem>
    
    <tr class="sortable">
    
       <td class="sortable" align="center">
       
        <netui-data:getData resultId="itemSetOrder" value="${container.item.itemSetOrder}"/>
         <netui-data:getData resultId="itemSetName" value="${container.item.itemSetName}"/>
          <netui-data:getData resultId="maxPoints" value="${container.item.maxPoints}"/>
          <netui-data:getData resultId="itemType" value="${container.item.itemType}"/>
          <netui-data:getData resultId="countStudent" value="${container.item.studentCount}"/>
			<c:if test="${countStudent > 0}">         
                   <netui:anchor href="goto_student_list.do?itemId=${container.item.itemId}&itemType=${container.item.itemType}&itemSetId=${container.item.itemSetId}&testAdminId=${actionForm.testAdminId}&itemSetOrder=${container.item.itemSetOrder}&itemSetName=${container.item.itemSetName}&maxPoints=${container.item.maxPoints}">
                        <netui:span value="${container.item.itemSetOrder}" defaultValue="&nbsp;"/>
                    </netui:anchor>
            </c:if>
            <c:if test="${countStudent <= 0}">         
            	<font color="#999999">
            		<u>
            			<netui:span value="${container.item.itemSetOrder}" defaultValue="&nbsp;"/>
            		</u>
            	</font>
            </c:if>
        </td>
          
        <td class="sortable" align="center">
            <netui:span value="${container.item.itemSetName}"/>
        </td>
   
         <td class="sortable" align="center">
          <netui-data:getData resultId="itemtype" value="${container.item.itemType}"/> 
         	 <c:if test="${itemtype =='AI'}">  
            <netui:span  value="Audio Item" />  
            </c:if>
             <c:if test="${itemtype =='CR'}">  
            <netui:span value="Constructive Response" />   
            </c:if>     
        </td>
     
	   <td class="sortable" align="center"><netui:span value="${container.item.maxPoints}" /></td>
					
      
        
    </tr>
    
    </netui-data:repeaterItem>
    <netui-data:repeaterFooter>
    
        <tr class="sortable">
            <td class="sortableControls" colspan="9">
                <ctb:tablePager dataSource="actionForm.itemPageRequested" summary="request.itemPagerSummary" objectLabel="${bundle.oas['object.items']}" id="itemSearchResult" anchorName="itemSearchResult"/>
            </td>
        </tr>         
            
    </netui-data:repeaterFooter>
</netui-data:repeater>   

</table>
		<c:if test="${itemSearchEmpty != null}">     
    <ctb:message title="Search Result" style="informationMessage" >
          <netui:content value="${requestScope.itemSearchEmpty}"/>
    </ctb:message>
</c:if>  
			<br/>
    <netui:button type="submit" value="${bundle.web['common.button.home']}" action="goToHomePage"/>
		</netui:form>
<br/><br/>
		<!-- ********************************************************************************************************************* -->
		<!-- End Page Content -->
		<!-- ********************************************************************************************************************* -->
	</netui-template:section>
	</netui-template:template>