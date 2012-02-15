<%@ page import="java.io.*, java.util.*"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.ctb.bean.studentManagement.CustomerConfiguration"%>
<%@ page import="dto.StudentAccommodationsDetail"%>
<%@ page import="com.ctb.bean.studentManagement.MusicFiles"%>

<% 
    StudentAccommodationsDetail accommodations1 = (StudentAccommodationsDetail)request.getAttribute("accommodations");
    CustomerConfiguration[] customerConfigurations1 = (CustomerConfiguration[])request.getAttribute("customerConfigurations");
	Boolean viewOnly2 = (Boolean) request.getAttribute("viewOnly");
	MusicFiles[] musicList1 = (MusicFiles[])request.getAttribute("musicList");
%>

<input type="hidden" name="param" id="param" value="">


<ctbweb:viewStudentAccommodations accommodations="<%=accommodations1%>" customerConfigurations="<%=customerConfigurations1%>" viewOnly="<%=viewOnly2%>" musicList="<%=musicList1%>" />

<br/>

