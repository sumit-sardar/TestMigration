<%@ page import="java.io.*, java.util.*"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>

<netui-data:getData resultId="messageType" value="${pageMessage.type}"/>
<c:if test="${messageType != null}">     
<p>
    <% String style = (String)pageContext.getAttribute("messageType"); %> 
    <ctb:message title="${pageMessage.title}" style="<%= style %>" >
          <netui:content value="${pageMessage.content}"/>
    </ctb:message>
</p>
</c:if>  
