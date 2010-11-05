<%@ page import="java.io.*, java.util.*"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

 
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>
<%
   
	Boolean isStudentIdConfigurable = (Boolean)request.getAttribute("isStudentIdConfigurable"); //Change For CR - GA2011CR001
	Boolean isABECustomer = (Boolean)request.getAttribute("isABECustomer");
%>


<table class="simple">
    <tr class="transparent">

<!-- column 1 -->
<td class="transparent-top" width="50%">
<table class="transparent">
    <tr class="transparent">
        <td class="transparent" width="120"><netui:content value="First Name:"/></td>
        <td class="transparent"><netui:label value="${studentProfileData.firstName}"/></td>
    </tr>
    <tr class="transparent">
        <td class="transparent" width="120"><netui:content value="Middle Name:"/></td>
        <td class="transparent"><netui:label value="${studentProfileData.middleName}"/></td>
    </tr>
    <tr class="transparent">
        <td class="transparent" width="120"><netui:content value="Last Name:"/></td>
        <td class="transparent"><netui:label value="${studentProfileData.lastName}"/></td>
    </tr>
    <tr class="transparent">
        <td class="transparent" width="120"><netui:content value="Login ID:"/></td>
        <td class="transparent"><netui:label value="${studentProfileData.userName}"/></td>
    </tr>
    <tr class="transparent">
        <td class="transparent" width="120"><netui:content value="Date of Birth:"/></td>
        <td class="transparent"><netui:label value="${studentProfileData.birthdateString}"/></td>
    </tr>
       <tr class="transparent">
        <td class="transparent" width="213"><netui:content value="Grade:"/></td>
        <td class="transparent"><netui:label value="${studentProfileData.grade}"/></td>
    </tr>
    <tr class="transparent">
        <td class="transparent" width="213"><netui:content value="Gender:"/></td>
        <td class="transparent"><netui:label value="${studentProfileData.gender}"/></td>
    </tr>
</table>

</td>





<!-- column 2 -->
<td class="transparent-top" width="50%">
<table class="transparent">
<tr class="transparent">
        <td class="transparent" width="270"><netui:content value="Instructor First Name:"/></td>
        <td class="transparent"><netui:label value="${studentProfileData.firstName}"/></td>
    </tr>
    <tr class="transparent">
        <td class="transparent" width="270"><netui:content value="Instructor Last Name:"/></td>
        <td class="transparent"><netui:label value="${studentProfileData.middleName}"/></td>
    </tr>
 
    <tr class="transparent">
        <td class="transparent" width="270">
        
         <c:if test="${isStudentIdConfigurable}">   
        <netui:content value="${studentIdArrValue[0]}:"/></td>
         </c:if>
          <c:if test="${!isStudentIdConfigurable}">   
        <netui:content value="Social Security Number/Student ID:"/></td>
         </c:if>
        <td class="transparent"><netui:label value="${studentProfileData.studentNumber}"/></td>
    </tr>
    <tr class="transparent">
        <td class="transparent-top" width="270"><netui:content value="Organization:"/></td>
        <td class="transparent-top">
            <table class="transparent">
            <netui-data:repeater dataSource="requestScope.organizationNodes">
                <netui-data:repeaterItem>            
                <tr class="transparent">
                    <td class="transparent-small"><netui:content value="${container.item.orgNodeName}"/></td>
                </tr>
                </netui-data:repeaterItem>
            </netui-data:repeater>
            </table>        
        </td>
    </tr>
     <tr class="transparent">
        <td class="transparent" width="270"><netui:content value="Make student visible across organizations:"/></td>
        <td class="transparent">Yes</td>
    </tr>
    
</table>

</td>
</tr>
</table>

<br/>
