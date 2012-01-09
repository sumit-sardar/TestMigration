<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="label.tld" prefix="lb" %>
<lb:bundle baseName="studentApplicationResource" />
 
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>
<netui-data:declareBundle bundlePath="helpResources" name="help"/>

<netui-template:template templatePage="/resources/jsp/oas_template.jsp">

<netui-template:setAttribute name="title" value="${bundle.web['findstudent.window.title']}"/>
<netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.findStudent']}"/>
<netui-template:section name="bodySection">

<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->

<input type="hidden" id="jqgFirstNameID" name="jqgFirstNameID" value=<lb:label key="stu.info.firstName" prefix="'" suffix="'"/>/>
<input type="hidden" id="jqgLastNameID" name="jqgLastNameID" value=<lb:label key="stu.info.lastName" prefix="'" suffix="'"/>/>
<input type="hidden" id="jqgMiddleIniID" name="jqgMiddleIniID" value=<lb:label key="stu.label.midInitial" prefix="'" suffix="'"/>/>
<input type="hidden" id="jqgGradeID" name="jqgGradeID" value=<lb:label key="stu.info.grade" prefix="'" suffix="'"/>/>
<input type="hidden" id="jqgOrgID" name="jqgOrgID" value=<lb:label key="stu.info.org" prefix="'" suffix="'"/>/>
<input type="hidden" id="jqgGenderID" name="jqgGenderID" value=<lb:label key="stu.info.gender" prefix="'" suffix="'"/>/>
<input type="hidden" id="jqgAccoID" name="jqgAccoID" value=<lb:label key="stu.label.acco" prefix="'" suffix="'"/>/>
<input type="hidden" id="jqgLoginID" name="jqgLoginID" value=<lb:label key="stu.info.loginID" prefix="'" suffix="'"/>/>
<input type="hidden" id="addStuID" name="addStuID" value=<lb:label key="stu.label.addStu" prefix="'" suffix="'"/>/>
<input type="hidden" id="moveStuGrid" name="moveStuGrid" value=<lb:label key="stu.label.list" prefix="'" suffix="'"/>/>
<input type="hidden" id="selectMoveOrg" name="selectMoveOrg" value=<lb:label key="stu.label.moveSelectOrg" prefix="'" suffix="'"/>/>
<input type="hidden" id="confirmMoveAlert" name="confirmMoveAlert" value=<lb:label key="stu.label.confirmAlert" prefix="'" suffix="'"/>/>



<!-- start form -->
<netui:form action="beginBulkMoveStudent">
<input type="hidden" id="selectedBulkTreeOrgNodeId" />

        <jsp:include page="/bulkMoveOperation/select_students_for_move.jsp" />

</netui:form>

<script type="text/javascript">
$(document).ready(function(){
	setMenuActive("organizations", "bulkMoveLink");
});
</script>
        
<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->
</netui-template:section>
</netui-template:template>
