<%@ page import="java.io.*, java.util.*"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 
<%
	List licenseDetails = (List) session.getAttribute("licenseDetails");
%>

<table class="simpletable" width="100%">
<netui-data:repeater dataSource="sessionScope.licenseDetails">
    <netui-data:repeaterHeader>
       <tr class="tableHeader">
           <th width="15%" style="padding-left:5px;" align="left">
               <div class="notCurrentSort"><span>Order Number</span></div>
           </th>
           <th width="15%" style="padding-left:5px;" align="left">
               <div class="notCurrentSort"><span>Available</span></div>
           </th>
           <th width="20%" style="padding-left:5px;" align="left">
               <div class="notCurrentSort"><span>Purchase Date</span></div>
           </th>
           <th width="20%" style="padding-left:5px;" align="left">
               <div class="notCurrentSort"><span>Expiry Date</span></div>
           </th>
           <th width="30%" style="padding-left:5px;" align="left">
               <div class="notCurrentSort"><span>Purchase Order</span></div>
           </th>
        </tr>
    </netui-data:repeaterHeader>
    <netui-data:repeaterItem>
    	<netui-data:getData resultId="expiryStatus" value="${container.item.expiryStatus}"/>
    	<%  String expiryStatus = (String)pageContext.getAttribute("expiryStatus");
		   	boolean aboutExpire = false; 
		   	if(expiryStatus.equalsIgnoreCase("ABOUT_EXPIRED"))
		   		aboutExpire = true;
		   	if(!expiryStatus.equalsIgnoreCase("EXPIRED")){				
			   	if(aboutExpire){ 
			%>	
	    		<tr class="simpletableYellow">
	    		<%} else{ %>
	        	<tr class="simpletable">
		        	<%} if(aboutExpire){%>
		        	<td class="simpletableYellow">
		        	<%}else{ %>
		            <td class="simpletable">
		            <%}%>
		            	<netui:content value="${container.item.orderNumber}" defaultValue="&nbsp;"/>
		            </td>
		            <%if(aboutExpire){%>
		        	<td class="simpletableYellow">
		        	<%}else{ %>
		            <td class="simpletable">
		            <%}%>
		            	<netui:content value="${container.item.licenseQuantity}" defaultValue="&nbsp;"/>
		            </td>
		            <%if(aboutExpire){%>
		        	<td class="simpletableYellow">
		        	<%}else{ %>
		            <td class="simpletable">
		            <%}%>
		            	<netui:content value="${container.item.purchaseDate}" defaultValue="&nbsp;"/>
		            </td>
		            <%if(aboutExpire){%>
		        	<td class="simpletableYellow">
		        	<%}else{ %>
		            <td class="simpletable">
		            <%}%>
		            	<netui:content value="${container.item.expiryDate}" defaultValue="&nbsp;"/>
		            </td>
		            <%if(aboutExpire){%>
		        	<td class="simpletableYellow">
		        	<%}else{ %>
		            <td class="simpletable">
		            <%}%>
		            	<netui:content value="${container.item.purchaseOrder}" defaultValue="&nbsp;"/>
		            </td>
	        	</tr>
		<% }%>
    </netui-data:repeaterItem>
</netui-data:repeater>
</table>
