<%@page import="java.util.LinkedHashMap"%>
<%@page import="com.rays.pro4.Util.HTMLUtility"%>
<%@page import="com.rays.pro4.controller.CourseCtl"%>
<%@page import="com.rays.pro4.Util.DataUtility"%>
<%@page import="com.rays.pro4.Util.ServletUtility"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<html>
<head>
<link rel="icon" type="image/png" href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16*16" />
<title>Course Registration Page</title>
<link rel="stylesheet" href="<%=ORSView.APP_CONTEXT%>/css/style.css">
</head>
<body>
	<jsp:useBean id="bean" class="com.rays.pro4.Bean.CourseBean" scope="request"></jsp:useBean>
	<form action="<%=ORSView.COURSE_CTL%>" method="post">
		<%@include file="Header.jsp"%>
		<div class="form-container">
			<h1 align="center">
				<%=bean.getId() > 0 ? "Update Course" : "Add Course"%>
			</h1>

			<div class="text-center">
				<div class="success-message"><%=ServletUtility.getSuccessMessage(request)%></div>
				<div class="error-message"><%=ServletUtility.getErrorMessage(request)%></div>
			</div>

			<div class="input-container">
				<input type="hidden" name="id" value="<%=bean.getId()%>">
				<input type="hidden" name="createdby" value="<%=bean.getCreatedBy()%>">
				<input type="hidden" name="modifiedby" value="<%=bean.getModifiedBy()%>">
				<input type="hidden" name="createdDatetime" value="<%=DataUtility.getTimestamp(bean.getCreatedDatetime())%>">
				<input type="hidden" name="modifiedDatetime" value="<%=DataUtility.getTimestamp(bean.getModifiedDatetime())%>">
			</div>

			<div class="input-container">
				<label for="name">Course Name<span class="error-message">*</span>:</label>
				<input type="text" id="name" name="name" placeholder="Enter Course Name" value="<%=DataUtility.getStringData(bean.getName())%>">
				<div class="error-message"><%=ServletUtility.getErrorMessage("name", request)%></div>
			</div>

			<div class="input-container">
				<label for="duration">Duration <span class="error-message">*</span>:</label>
				<%
					LinkedHashMap<String, String> map = new LinkedHashMap<>();
					map.put("1 Year", "1 Year");
					map.put("2 Year", "2 Year");
					map.put("3 Year", "3 Year");
					map.put("4 Year", "4 Year");
					map.put("5 Year", "5 Year");
					map.put("6 Year", "6 Year");
					String htmlList = HTMLUtility.getList("duration", bean.getDuration(), map);
				%>
				<%=htmlList%>
				<div class="error-message"><%=ServletUtility.getErrorMessage("duration", request)%></div>
			</div>

			<div class="input-container">
				<label for="description">Description <span class="error-message">*</span>:</label>
				<input type="text" id="description" name="description" placeholder="Enter Description" value="<%=DataUtility.getStringData(bean.getDescription())%>">
				<div class="error-message"><%=ServletUtility.getErrorMessage("description", request)%></div>
			</div>

			<div class="button-container">
				<input type="submit" name="operation" value="<%=bean.getId() > 0 ? CourseCtl.OP_UPDATE : CourseCtl.OP_SAVE%>">
				<input type="submit" name="operation" value="<%=bean.getId() > 0 ? CourseCtl.OP_CANCEL : CourseCtl.OP_RESET%>">
			</div>
		</div>
	</form>
	<%@include file="Footer.jsp"%>
</body>
</html>