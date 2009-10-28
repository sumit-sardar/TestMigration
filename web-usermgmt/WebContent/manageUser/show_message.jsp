<%@ page import="java.io.*, java.util.*"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>

<% 
	String messageType = (String)request.getAttribute("messageType");
	String messageTitle = (String)request.getAttribute("messageTitle");
	String messageContent = (String)request.getAttribute("messageContent");
%> 

<c:if test="${messageType != null}">     
<p>
    <ctb:message title="<%= messageTitle %>"  style="<%= messageType %>" >
          <netui:content value="<%= messageContent %>" />
    </ctb:message>
</p>
</c:if> 
