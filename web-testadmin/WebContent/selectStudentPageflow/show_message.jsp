<%@ page import="java.io.*, java.util.*"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="netui-tags-databinding.tld" prefix="netui-compat-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui-compat"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-compat-template"%>

<netui-compat-data:getData resultId="messageType" value="{actionForm.message.type}"/>
<c:if test="${messageType != null}">     
<p>
    <% String style = (String)pageContext.getAttribute("messageType"); %> 
    <ctb:message title="{actionForm.message.title}" style="<%= style %>" >
          <netui-compat:content value="{actionForm.message.content}"/>
    </ctb:message>
</p>
</c:if> 
