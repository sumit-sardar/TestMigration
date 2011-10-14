<%@ page import="java.io.*, java.util.*"%>
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

<netui-template:template templatePage="/resources/jsp/oas_template.jsp">
<netui-template:setAttribute name="title" value="${bundle.web['finduser.window.title']}"/>
<netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.findUser']}"/>
<netui-template:section name="bodySection">
 
 
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery.jstree.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/grid.locale-en.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/verifyuserinfo.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery.jqGrid.min.js"></script>	
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/hierarchy.js"></script>

 
<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->



<netui:form action="organizations_manageUsers">
<input type="hidden" id="treeOrgNodeId" />

	<table class="simple">
	
	    <tr class="simple">
	        <td class="simple" style="background-color : #ffffff" valign="top">
	        
		      	<div  id="searchheader" class="treeHeader">
		      		&nbsp;User Search
		      	</div>
			      	
		    	<script>
		    		populateTree();
		    	</script>
		    	
	       		<div id ="orgNodeHierarchy" class="treeBody">
				</div> 
					
			 </td>
			 
			 <td class="simple" width="10px">&nbsp;</td>
			  
			 <td class="simple" valign="top">
		    	<table id="list2" ></table>
				<div id="pager2"  ></div>
	         </td>
	        
	    </tr>
	</table>


</netui:form>

<script type="text/javascript">
$(document).ready(function(){
	selectTab("organizations", "usersLink");
});
</script>

<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
    </netui-template:section>
</netui-template:template>


