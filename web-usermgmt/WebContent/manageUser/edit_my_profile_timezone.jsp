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

<netui:html>
  
<head>
    <netui:base/>
    <title><netui-template:attribute name="title"/></title>
    <link href="<%=request.getContextPath()%>/resources/css/legacy.css" type="text/css" rel="stylesheet" />
    <link href="<%=request.getContextPath()%>/resources/css/widgets.css" type="text/css" rel="stylesheet" />
    <link href="<%=request.getContextPath()%>/resources/css/autosuggest.css" type="text/css" rel="stylesheet" />
    
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/widgets.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/js_web.jsp"></script>   
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/setupbuttons.js"></script>         
</head>

<body>

<jsp:include page="/resources/jsp/header.jsp" />

<table class="legacyBodyLayout">
<tr><td>&nbsp;</td></tr>


<tr><td id="legacyBody">
<!-- ********************************************************************************************************************* -->
<!-- Start Page Content -->
<!-- ********************************************************************************************************************* -->
<h1>
    <netui:content value="Edit My Profile"/>
</h1>      


<!-- title message -->
<p>
    <netui:content value="Enter information about yourself in the form below. Required fields are marked by a blue asterisk *."/><br/>
    <netui:content value="Although Email is not required, it is recommended that a valid email address be entered."/><br/>
    <netui:content value="Your User Profile is missing required information. Select the time zone in which you are located. You need to do this only one time."/>
</p>



<!-- start form -->
<netui:form action="addEditUser">

<input type="hidden" name="firstFocusId" id="firstFocusId" value="{actionForm.userProfile.firstName}" />

<netui:hidden dataSource="actionForm.actionElement"/> 
<netui:hidden dataSource="actionForm.currentAction"/>
<netui:hidden dataSource="actionForm.selectedUserId"/>
<netui:hidden dataSource="actionForm.userProfile.loginId"/>
<netui:hidden dataSource="actionForm.userProfile.addressId"/> 
<netui:hidden dataSource="actionForm.userProfile.role"/>
<netui:hidden dataSource="actionForm.userProfile.roleId"/>
<%-- CR Dex --%>
<netui:hidden dataSource="actionForm.userProfile.extPin1"/>
<!-- message -->
<jsp:include page="/manageUser/show_message.jsp" />



<!-- buttons -->
<p>
    <netui:button type="submit" value="Save" action="saveMyProfile"/>
   
</p>


<!-- collapsible sections -->
<a name="moduleUserProfile"><!-- moduleUserProfile --></a>    
<p>
    <ctb:showHideSection sectionId="moduleUserProfile" sectionTitle="User Information" sectionVisible="actionForm.byUserProfileVisible">
        <jsp:include page="/manageUser/edit_my_profile_info.jsp" />
    </ctb:showHideSection>
</p>


<a name="moduleUserContact"><!-- moduleUserContact --></a>    
<p>
    <ctb:showHideSection sectionId="moduleUserContact" sectionTitle="Contact Information" sectionVisible="actionForm.byUserContactVisible">
        <jsp:include page="/manageUser/add_edit_user_by_contact.jsp" />
    </ctb:showHideSection>
</p>



<!-- buttons -->
<p>
    <netui:button type="submit" value="Save" action="saveMyProfile"/>
    
</p>


</netui:form>

</td></tr></table>

<jsp:include page="/resources/jsp/footer.jsp" />  

</body>
</netui:html>

  
<!-- ********************************************************************************************************************* -->
<!-- End Page Content -->
<!-- ********************************************************************************************************************* -->

