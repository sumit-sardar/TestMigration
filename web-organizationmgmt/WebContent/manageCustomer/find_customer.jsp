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

<netui-template:template templatePage="/resources/jsp/template_find_customer.jsp">
<netui-template:setAttribute name="title" value="${bundle.web['findcustomer.window.title']}"/>
<netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.findCustomer']}"/>
<netui-template:section name="bodySection">

<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->

<!-- start form -->
<netui:form action="findCustomer">

<input type="hidden" name="firstFocusId" id="firstFocusId" value="{actionForm.customerProfile.name}" />

<netui:hidden dataSource="actionForm.enableLicense" tagId="actionPermission"/>
<h1>
    <netui:content value="${pageFlow.pageTitle}"/>
</h1>

<!-- title -->
<p>
    <netui:content value="To see a list of all customers, click Search. To find specific customers, enter the known information on which to search."/><br/>
</p>

<!-- hidden parameters-->
<netui:hidden tagId="actionElement" dataSource="actionForm.actionElement"/> 
<netui:hidden tagId="currentAction" dataSource="actionForm.currentAction"/>
<netui:hidden tagId="customerMaxPage" dataSource="actionForm.customerMaxPage"/> 


<!--  include message page -->
<jsp:include page="/manageCustomer/show_message.jsp" />

<table class="sortable">
    <tr class="sortable">
        <td class="sortableControls">
<br/>        
<table class="tableFilter">
    <tr class="tableFilter">
        <td class="tableFilter" width="100" align="right">Customer Name:</td>
        <td class="tableFilter" width="200"><netui:textBox tagId="customerName" dataSource="actionForm.customerProfile.name" style="width:180px" tabindex="1" maxlength="32"/></td>
        <td class="tableFilter" width="100" align="right">State:</td>
        <td class="tableFilter" width="*">
            <netui:select optionsDataSource="${pageFlow.stateOptions}" dataSource="actionForm.customerProfile.state" size="1" style="width:180px" tabindex="3" defaultValue="${actionForm.customerProfile.state}"/>
        </td>
    </tr>
    <tr class="tableFilter">
        <td class="tableFilter" width="100" align="right">Customer ID:</td>
        <td class="tableFilter" width="200"><netui:textBox tagId="code" dataSource="actionForm.customerProfile.code" style="width:180px" tabindex="2" maxlength="32"/></td>
        <td class="tableFilter" width="100" align="right">&nbsp;</td>
        <td class="tableFilter" width="*">
            <netui:button styleClass="button" value="Search" type="submit" onClick="setElementValueAndSubmitWithAnchor('currentAction', 'applySearch', 'customerProfileResult');" tabindex="4"/>
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <netui:button styleClass="button" value="Clear All" type="submit" onClick="setElementValueAndSubmit('currentAction', 'clearSearch');" tabindex="5"/>&nbsp;
        </td>
    </tr>
</table>    
<br/>
        </td>
    </tr>
</table>




<br/>
<!--  customer search result -->
<a name="customerSearchResult"><!-- customerSearchResult --></a>   
<c:if test="${customerResult != null}">     
    <p><netui:content value="${pageFlow.pageMessage}"/></p>
    <p> <jsp:include page="/manageCustomer/find_customer_result.jsp" /></p>
</c:if>

<c:if test="${searchResultEmpty != null}">     
    <ctb:message title="Search Result" style="informationMessage" >
          <netui:content value="${requestScope.searchResultEmpty}"/>
    </ctb:message>
</c:if>

    
    

</netui:form>
        
<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
</netui-template:section>
</netui-template:template>
