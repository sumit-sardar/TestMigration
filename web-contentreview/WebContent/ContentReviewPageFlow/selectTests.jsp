<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
        
<netui:html>
    <head>
        <title>Select Test</title>
    </head>
    <body>
    <jsp:include page="header.jsp" />  
        <netui:form method="post" action="showSubtests">
            <p>Select a test and click <strong>Next</strong></p>           
            <netui-data:repeater dataSource="pageFlow.tests">
                <netui-data:repeaterHeader>
                    <table class="" border="0" cellpadding="0" cellspacing="0">
                </netui-data:repeaterHeader>
                <netui-data:repeaterItem>
                    <tr valign="top">
                        <td>
                            <netui:radioButtonGroup dataSource="pageFlow.testTitle">     
                                <netui:radioButtonOption value="${container.item.title}"/>
                            </netui:radioButtonGroup>
                        </td>
                    </tr>
                    <tr><td height="10">&nbsp;</td></tr>
                </netui-data:repeaterItem>
                <netui-data:repeaterFooter></table></netui-data:repeaterFooter>
            </netui-data:repeater>
            <br/>
            <netui:button value="Next >" type="submit"/>
            <br/>
        </netui:form>
    </body>
</netui:html>
