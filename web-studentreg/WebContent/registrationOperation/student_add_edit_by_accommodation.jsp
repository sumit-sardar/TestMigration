<%@ page import="java.io.*, java.util.*"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.ctb.bean.studentManagement.CustomerConfiguration"%>
<%@ page import="dto.StudentAccommodationsDetail"%>
<%@ page import="com.ctb.bean.studentManagement.MusicFiles"%>

<% 
    StudentAccommodationsDetail accommodations = (StudentAccommodationsDetail)request.getAttribute("accommodations");
    CustomerConfiguration[] customerConfigurations = (CustomerConfiguration[])request.getAttribute("customerConfigurations");
	Boolean viewOnly3 = (Boolean) request.getAttribute("viewOnly");
	MusicFiles[] musicList = (MusicFiles[])request.getAttribute("musicList");
%>

<input type="hidden" name="param" id="param" value="">


<ctbweb:studentAccommodations accommodations="<%=accommodations%>" customerConfigurations="<%=customerConfigurations%>" viewOnly="<%=viewOnly3%>" musicList="<%=musicList%>" />

<br/>

