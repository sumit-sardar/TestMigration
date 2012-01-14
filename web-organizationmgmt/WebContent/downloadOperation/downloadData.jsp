<%@ page import="java.io.*, java.util.*"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="label.tld" prefix="lb" %>
<lb:bundle baseName="organizationApplicationResource" />

<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>
<netui-data:declareBundle bundlePath="helpResources" name="help"/>

<netui-template:template templatePage="/resources/jsp/oas_template.jsp">
<netui-template:setAttribute name="title" value="${bundle.web['download.window.title']}"/>
<netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.downloadData']}"/>
<netui-template:section name="bodySection">
 
 
<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->

<%
    String userName = (String)request.getSession().getAttribute("userName"); 
    String userFile = userName + "_User.xls";  
    String studentFile = userName + "_Student.xls";
%>


<netui:form action="downloadData">

<table width="97%" style="margin:15px auto;" border="0"> 
	<tr>
		<td style="padding-left:5px;">
    		<h1><lb:label key="services.export.title" /></h1>
			<lb:label key="services.export.msg" /> 		
		</td>
	</tr>
	<tr>
		<td align="right">
        	<a href="#" onclick="submitPage();" class="rounded {transparent} button" style="text-decoration: none;" >
          		<lb:label key="services.export.button" />
           	</a>                	                                    
		</td>
	</tr>
	<tr> 
		<td style="padding-left:6px;">
			<table class="simpletable" style="padding: 5px" > 
			    <tr class="simpletable">
			        <th class="simpletable alignCenter" height="25" nowrap width="50">Select</th>                
			        <th class="simpletable alignLeft" height="25" nowrap>&nbsp;&nbsp;Name</th>
			        <th class="simpletable alignLeft" height="25" nowrap>&nbsp;&nbsp;Description</th>
			    </tr>    
			    <tr class="simpletable">
			        <td class="simpletable alignCenter">
			            <netui:radioButtonGroup dataSource="actionForm.fileName">
			                &nbsp;<netui:radioButtonOption value="<%=userFile%>" onClick="return enableDownloadData();">&nbsp;</netui:radioButtonOption>                
			            </netui:radioButtonGroup>        
			        </td>
			        <td class="simpletable">
			        	<lb:label key="services.export.userdata" />
			        </td>        
			        <td class="simpletable">
			        	<lb:label key="services.export.userdata.msg" />
			        </td>        
			    </tr>
			    <tr class="simpletable">
			        <td class="simpletable alignCenter">
			            <netui:radioButtonGroup dataSource="actionForm.fileName">
			                &nbsp;<netui:radioButtonOption value="<%=studentFile%>" onClick="return enableDownloadData();">&nbsp;</netui:radioButtonOption>                
			            </netui:radioButtonGroup>        
			        </td>
			        <td class="simpletable">
			        	<lb:label key="services.export.studentdata" />
			        </td>        
			        <td class="simpletable">
			        	<lb:label key="services.export.userdata.msg" />
			        </td>        
			    </tr>
			</table>	
		</td>
	</tr>
</table>

</netui:form>

<script type="text/javascript">
$(document).ready(function(){
	setMenuActive("services", "downloadDataLink");
});
</script>

<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
    </netui-template:section>
</netui-template:template>


