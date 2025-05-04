<%@page import="com.rays.pro4.Bean.SubjectBean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@page import="java.util.List"%>
<%@ page import="com.rays.pro4.Util.DataUtility"%>
<%@ page import="com.rays.pro4.Util.HTMLUtility"%>
<%@ page import="com.rays.pro4.Bean.CourseBean"%>
<%@ page import="com.rays.pro4.controller.ORSView"%>
<%@ page import="com.rays.pro4.Util.ServletUtility"%>
<%@ page import="com.rays.pro4.controller.SubjectCtl"%>

<%@ page import="java.util.HashMap"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<link rel="icon" type="image/png" href="${ctx}/img/logo.png"
	sizes="16*16" />
<title>Subject Registration Page</title>
<link rel="stylesheet" href="${ctx}/css/style.css">
</head>
<body>	
	<jsp:useBean id="bean" class="com.rays.pro4.Bean.SubjectBean"
		scope="request" />
	<form action="${ctx}${ORSView.SUBJECT_CTL}" method="post">
		<div class="container">
			<h1 class="text-center">
				<c:choose>
					<c:when test="${not empty bean.id}">
						Update Subject
					</c:when>
					<c:otherwise>
						Add Subject
					</c:otherwise>
				</c:choose>
			</h1>
			<div class="message-container">
				<c:if test="${not empty param.successMessage}">
					<div class="success">${param.successMessage}</div>
				</c:if>
				<c:if test="${not empty param.errorMessage}">
					<div class="error">${param.errorMessage}</div>
				</c:if>
			</div>
			<input type="hidden" name="id" value="${bean.id}">
			<input type="hidden" name="createdby" value="${bean.createdBy}">
			<input type="hidden" name="modifiedby" value="${bean.modifiedBy}">
			<input type="hidden" name="createddatetime"
				value="${bean.createdDatetime}">
			<input type="hidden" name="modifieddatetime"
				value="${bean.modifiedDatetime}">
			<c:set var="CourseList" value="${requestScope.CourseList}" />
			<table class="table table-borderless w-50">
				<tr>
					<th align="left"><label for="courseId">Course Name<span
							class="required">*</span> :</label></th>
					<td>${HTMLUtility.getList("courseId", bean.courseId, CourseList)}
						<div class="error">${requestScope.courseName}</div></td>
				</tr>

				<tr>
					<th align="left"><label for="name">Subject Name <span
							class="required">*</span> :</label></th>
					<td><input type="text" name="subjectName" id="name"
						placeholder="Enter Subject Name" class="form-control"
						value="${bean.subjectName}">
						<div class="error">${requestScope.subjectName}</div></td>
				</tr>

				<tr>
					<th align="left"><label for="description">Description<span
							class="required">*</span> :</label></th>
					<td><input type="text" name="description" id="description"
						placeholder="Enter Description" class="form-control"
						value="${bean.description}">
						<div class="error">${requestScope.description}</div></td>
				</tr>
				<tr>
					<th></th>
					<td><c:choose>
							<c:when test="${not empty bean.id}">
								<input type="submit" name="operation" value="<%=SubjectCtl.OP_UPDATE%>">
								<input type="submit" name="operation" value="<%=SubjectCtl.OP_CANCEL%>">
							</c:when>
							<c:otherwise>
								<input type="submit" name="operation" value="<%=SubjectCtl.OP_SAVE%>">
								<input type="submit" name="operation" value="<%=SubjectCtl.OP_RESET%>">
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</table>
		</div>
	</form>
	<%@include file="Footer.jsp"%>
</body>
</html>
