<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>



<!--  studentList table -->
<table class="sortable">

<p><b>Step2:</b> Select a subtest which needs to reset</p>   
<tr>
	<td>
  		<netui:select dataSource="actionForm.selectedProductName" optionsDataSource="${pageFlow.productNameList}" size="1" multiple="false" onChange="setElementValueAndSubmit('currentAction', 'changeProduct');">
        </netui:select> 
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;      
	</td>
</tr> 
<a name="studentSearchResult"><!-- studentSearchResult --></a>    
<c:if test="${studentList != null}">     
    <p><netui:content value="${pageFlow.pageMessage}"/></p>
   
</c:if>

<c:if test="${searchResultEmpty != null}">     
    <ctb:message title="Search Result" style="informationMessage" >
          <netui:content value="${requestScope.searchResultEmpty}"/>
    </ctb:message>
</c:if>

 
  
   
</table>
