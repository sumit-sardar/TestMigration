<%@ page import="java.io.*, java.util.*"%>
<%@ page import="dto.Message"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>


<netui-data:getData resultId="messageType" value="${requestScope.pageMessage}"/>
 <c:if test="${messageType != null}">     
<p>
    <% Message  msg= (Message)pageContext.getAttribute("messageType"); 
    String style =  msg.getType();
    String content = msg.getContent();
    String title = msg.getTitle();
    %> 
     <ctb:message title="<%= title%>" style="<%= style %>" >
          <netui:content value="<%= content %>"/>
    </ctb:message>
   
</p>
</c:if> 

