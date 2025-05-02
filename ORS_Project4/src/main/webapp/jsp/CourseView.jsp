<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="com.rays.pro4.Util.HTMLUtility"%>
<%@page import="com.rays.pro4.controller.ORSView"%>
<%@page import="com.rays.pro4.controller.CourseCtl"%>
<%@page import="com.rays.pro4.Util.DataUtility"%>
<%@page import="com.rays.pro4.Util.ServletUtility"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<head>
<link rel="icon" type="image/png" href="${ctx}/img/logo.png"
	sizes="16*16" />
<title>
	<c:choose>
		<c:when test="${not empty bean.id}">Update Course</c:when>
		<c:otherwise>Add Course</c:otherwise>
	</c:choose>
</title>
<link rel="stylesheet" href="${ctx}/css/style.css">
</head>
<body>
	<jsp:useBean id="bean" class="com.rays.pro4.Bean.CourseBean"
		scope="request"></jsp:useBean>
	<form action="${ctx}${ORSView.COURSE_CTL}" method="post">
		<%@ include file="Header.jsp"%>
		<div class="form-container">
			<h1 class="text-center"><c:choose>
					<c:when test="${not empty bean.id}">Update Course</c:when>
					<c:otherwise>Add Course</c:otherwise>
				</c:choose></h1>

			<div>
				<c:if test="${not empty successMessage}">
				<div class="success">${successMessage}</div>
				</c:if>
				<c:if test="${not empty errorMessage}">
				<div class="error">${errorMessage}</div>
				</c:if>
			</div>
			<div class="input-container">
				<input type="hidden" name="id" value="${bean.id}">
				<input type="hidden" name="createdby" value="${bean.createdBy}">
				<input type="hidden" name="modifiedby" value="${bean.modifiedBy}">
				<input type="hidden" name="createdDatetime"
					value="${bean.createdDatetime}"> <input type="hidden"
					name="modifiedDatetime" value="${bean.modifiedDatetime}">
			</div>

			<div class="input-container">
				<label for="name">Course Name<span class="required">*</span></label>
				<input type="text" id="name" name="name"
					placeholder="Enter Course Name" class="form-control"
					value="${bean.name}">
				<div class="error">${requestScope.name}</div>
			</div>

			<div class="input-container">
				<label for="duration">Duration <span class="required">*</span></label>
				<select id="duration" name="duration" class="form-control">
					<option value="">Select Duration</option>
					<option value="1 Year"
						${bean.duration == '1 Year' ? 'selected' : ''}>1 Year</option>
					<option value="2 Year"
						${bean.duration == '2 Year' ? 'selected' : ''}>2 Year</option>
					<option value="3 Year"
						${bean.duration == '3 Year' ? 'selected' : ''}>3 Year</option>
					<option value="4 Year"
						${bean.duration == '4 Year' ? 'selected' : ''}>4 Year</option>
					<option value="5 Year"
						${bean.duration == '5 Year' ? 'selected' : ''}>5 Year</option>
					<option value="6 Year"
						${bean.duration == '6 Year' ? 'selected' : ''}>6 Year</option>
				</select>
				<div class="error">${requestScope.duration}</div>
			</div>

			<div class="input-container">
				<label for="description">Description <span class="required">*</span></label>
				<input type="text" id="description" name="description"
					placeholder="Enter Description" class="form-control"
					value="${bean.description}">
				<div class="error">${requestScope.description}</div>
			</div>

			<div class="button-container">
				<input type="submit" name="operation"
					value="${bean.id > 0 ? CourseCtl.OP_UPDATE : CourseCtl.OP_SAVE}">
				<input type="submit" name="operation"
					value="${bean.id > 0 ? CourseCtl.OP_CANCEL : CourseCtl.OP_RESET}">
			</div>
		</div>
	</form>
	<%@ include file="Footer.jsp"%>
</body>
</html>