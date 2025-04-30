<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@page import="com.rays.pro4.Model.StudentModel"%>
<%@page import="com.rays.pro4.controller.StudentListCtl"%>
<%@page import="com.rays.pro4.Bean.StudentBean"%>
<%@page import="com.rays.pro4.Bean.CollegeBean"%>
<%@page import="com.rays.pro4.controller.ORSView"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="com.rays.pro4.Util.HTMLUtility"%>
<%@page import="com.rays.pro4.Util.ServletUtility"%>
<%@page import="com.rays.pro4.Util.ServletUtility"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="icon" type="image/png" href="${pageContext.request.contextPath}/img/logo.png" sizes="16*16" />
<title> Student List</title>
<script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/Checkbox11.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/demos/style.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
	<jsp:useBean id="cbean" class="com.rays.pro4.Bean.CollegeBean" scope="request"></jsp:useBean>
	<jsp:useBean id="bean" class="com.rays.pro4.Bean.StudentBean" scope="request"></jsp:useBean>

	<form action="${pageContext.request.contextPath}${ORSView.STUDENT_LIST_CTL}" method="post">
		<%@include file="Header.jsp"%>

		<div class="text-center">
			<h1>Student List</h1>
			<c:if test="${not empty error}">
				<div class="alert alert-danger">${error}</div>
			</c:if>
			<c:if test="${not empty success}">
				<div class="alert alert-success">${success}</div>
			</c:if>
		</div>

		<c:set var="clist" value="${requestScope.CollegeList}" />
		<c:set var="pageNo" value="${requestScope.pageNo}" />
		<c:set var="pageSize" value="${requestScope.pageSize}" />
		<c:set var="index" value="${((pageNo - 1) * pageSize) + 1}" />
		<c:set var="list" value="${requestScope.list}" />
		<c:set var="studentBean" value="${bean}" />

		<c:if test="${not empty list}">
			<table class="w-100">
				<tr>
					<td align="right">
						<label for="firstName">First Name:</label>
						<input type="text" id="firstName" name="firstName" placeholder="Enter Student Name"
							class="form-control-sm" value="${param.firstName}">
						<label for="lastName">Last Name:</label>
						<input type="text" id="lastName" name="lastName" placeholder="Enter last Name"
							class="form-control-sm" value="${param.lastName}">
						<label for="email">EmailId:</label>
						<input type="text" id="email" name="email" placeholder="Enter Email_id" class="form-control-sm"
							value="${param.email}">
						<label for="collegename">College Name:</label>
						<select id="collegename" name="collegename" class="form-control-sm">
							${HTMLUtility.getList("collegename",studentBean.collegeId,clist)}
						</select>
						<input type="submit" name="operation" value="<%=StudentListCtl.OP_SEARCH%>">
						<input type="submit" name="operation" value="<%=StudentListCtl.OP_RESET%>">
					</td>
				</tr>
			</table>
			<br>
			<table border="1" class="w-100" cellpadding="6px" cellspacing=".2">
				<tr class="bg-primary text-white">
					<th><input type="checkbox" id="select_all" name="select">Select All.</th>
					<th>S No.</th>
					<th>College.</th>
					<th>First Name.</th>
					<th>Last Name.</th>
					<th>Date Of Birth.</th>
					<th>Mobile No.</th>
					<th>Email_Id.</th>
					<th>Edit</th>
				</tr>
				<c:forEach var="student" items="${list}">
					<tr align="center">
						<td><input type="checkbox" class="checkbox" name="ids" value="${student.id}"></td>
						<td>${index}
							<c:set var="index" value="${index + 1}" /></td>
						<td>${student.collegeName}</td>
						<td>${student.firstName}</td>
						<td>${student.lastName}</td>
						<td>${student.dob}</td>
						<td>${student.mobileNo}</td>
						<td>${student.email}</td>
						<td><a href="StudentCtl?id=${student.id}">Edit</a></td>
					</tr>
				</c:forEach>
			</table>
			<table class="w-100">
				<tr>
					<c:choose>
						<c:when test="${pageNo == 1}">
							<td><input type="submit" name="operation" disabled="disabled"
								value="<%=StudentListCtl.OP_PREVIOUS%>"></td>
						</c:when>
						<c:otherwise>
							<td><input type="submit" name="operation" value="<%=StudentListCtl.OP_PREVIOUS%>"></td>
						</c:otherwise>
					</c:choose>

					<td><input type="submit" name="operation" value="<%=StudentListCtl.OP_DELETE%>"></td>
					<td><input type="submit" name="operation" value="<%=StudentListCtl.OP_NEW%>"></td>

					<c:set var="model" value="<%=new StudentModel()%>" />
					<c:choose>
						<c:when test="${list.size() < pageSize || model.nextPK()-1 == bean.id}">
							<td align="right"><input type="submit" name="operation" disabled="disabled"
								value="<%=StudentListCtl.OP_NEXT%>"></td>
						</c:when>
						<c:otherwise>
							<td align="right"><input type="submit" name="operation" value="<%=StudentListCtl.OP_NEXT%>"></td>
						</c:otherwise>
					</c:choose>
				</tr>
			</table>
		</c:if>
		<c:if test="${empty list}">
			<div class="text-center"><input type="submit" name="operation" value="<%=StudentListCtl.OP_BACK%>"></div>
		</c:if>
		<input type="hidden" name="pageNo" value="${pageNo}">
		<input type="hidden" name="pageSize" value="${pageSize}">
	</form>
	<%@include file="Footer.jsp"%>
</body>
</html>