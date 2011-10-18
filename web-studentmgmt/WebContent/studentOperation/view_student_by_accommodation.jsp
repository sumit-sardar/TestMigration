<%@ page import="java.io.*, java.util.*"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.ctb.bean.studentManagement.CustomerConfiguration"%>
<%@ page import="dto.StudentAccommodationsDetail"%>
<%@ page import="com.ctb.bean.studentManagement.MusicFiles"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="ctb-web.tld" prefix="ctbweb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>

<% 
    StudentAccommodationsDetail accommodations = (StudentAccommodationsDetail)request.getAttribute("accommodations");
    CustomerConfiguration[] customerConfigurations = (CustomerConfiguration[])request.getAttribute("customerConfigurations");
	Boolean viewOnly = (Boolean) request.getAttribute("viewOnly");
	MusicFiles[] musicList = (MusicFiles[])request.getAttribute("musicList");
%>

<input type="hidden" name="param" id="param" value="">


<ctbweb:viewStudentAccommodations accommodations="<%=accommodations%>" customerConfigurations="<%=customerConfigurations%>" viewOnly="<%=viewOnly%>" musicList="<%=musicList%>" />

<br/>

