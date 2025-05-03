<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@page import="com.rays.pro4.Model.StudentModel"%>
<%@page import="com.rays.pro4.controller.StudentListCtl"%>
<%@page import="com.rays.pro4.Bean.StudentBean"%>
<%@page import="com.rays.pro4.Bean.CollegeBean"%>
<%@page import="com.rays.pro4.controller.ORSView"%>
<%@page import="com.rays.pro4.Util.HTMLUtility"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
<head>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<link rel="icon" type="image/png" href="${ctx}/img/logo.png"
	sizes="16*16" />
<title>Student List</title>
<script src="${ctx}/js/jquery.min.js"></script>
<script src="${ctx}/js/Checkbox11.js"></script>
<link rel="stylesheet" href="${ctx}/css/style.css">
</head>
<body>
<jsp:useBean id="cbean" class="com.rays.pro4.Bean.CollegeBean"
	scope="request"></jsp:useBean>
<jsp:useBean id="bean" class="com.rays.pro4.Bean.StudentBean"
	scope="request"></jsp:useBean>
<form action="${ctx}${ORSView.STUDENT_LIST_CTL}" method="post">
	<%@include file="Header.jsp"%>
	<div class="container">
		<h1 class="text-center">Student List</h1>
		<div class="message-container">
			<c:if test="${not empty requestScope.error}">
				<div class="alert alert-danger">${requestScope.error}</div>
			</c:if>
			<c:if test="${not empty requestScope.success}">
				<div class="alert alert-success">${requestScope.success}</div>
			</c:if>
		</div>
		<c:set var="clist" value="${requestScope.CollegeList}" />
		<c:set var="pageNo" value="${requestScope.pageNo}" />
		<c:set var="pageSize" value="${requestScope.pageSize}" />
		<c:set var="index" value="${((pageNo - 1) * pageSize) + 1}" />
		<c:set var="list" value="${requestScope.list}" />
		<c:set var="studentBean" value="${bean}" />
		<c:if test="${not empty list}">
			<table class="search-table w-100">
				<tr>
					<td class="text-center"><label for="firstName">First
							Name:</label> <input type="text" id="firstName" name="firstName"
						placeholder="Enter Student Name" class="form-control-inline"
						value="${param.firstName}"> <label for="lastName">Last
							Name:</label> <input type="text" id="lastName" name="lastName"
						placeholder="Enter last Name" class="form-control-inline"
						value="${param.lastName}"> <label for="email">EmailId:</label>
						<input type="text" id="email" name="email"
						placeholder="Enter Email_id" class="form-control-inline"
						value="${param.email}"> <label for="collegename">College
							Name:</label> <select id="collegename" name="collegename"
						class="form-control-inline">
							${HTMLUtility.getList("collegename", studentBean.collegeId, clist)}
					</select> <input type="submit" name="operation" class="btn btn-primary"
						value="<%=StudentListCtl.OP_SEARCH%>"> <input type="submit"
						name="operation" class="btn btn-secondary"
						value="<%=StudentListCtl.OP_RESET%>"></td>
				</tr>
			</table>
			<table class="table table-bordered table-striped w-100">
				<thead class="table-header">
					<tr>
						<th><input type="checkbox" id="select_all" name="select">Select
							All.</th>
						<th>S No.</th>
						<th>College.</th>
						<th>First Name.</th>
						<th>Last Name.</th>
						<th>Date Of Birth.</th>
						<th>Mobile No.</th>
						<th>Email_Id.</th>
						<th>Edit</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="student" items="${list}">
						<tr class="text-center table-row">
							<td><input type="checkbox" class="checkbox" name="ids"
								value="${student.id}"></td>
							<td>${index}<c:set var="index" value="${index + 1}" /></td>
							<td>${student.collegeName}</td>
							<td>${student.firstName}</td>
							<td>${student.lastName}</td>
							<td>${student.dob}</td>
							<td>${student.mobileNo}</td>
							<td>${student.email}</td>
							<td><a href="${ctx}/StudentCtl?id=${student.id}"
								class="btn btn-link">Edit</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<table class="w-100">
				<tr>
					<td><input type="submit" name="operation"
						class="btn btn-secondary"
						value="<%=StudentListCtl.OP_PREVIOUS%>"
						${pageNo == 1 ? 'disabled' : ''}></td>
					<td><input type="submit" name="operation"
						class="btn btn-danger" value="<%=StudentListCtl.OP_DELETE%>"></td>
					<td><input type="submit" name="operation"
						class="btn btn-success" value="<%=StudentListCtl.OP_NEW%>"></td>
					<c:set var="model" value="<%=new StudentModel()%>" />
					<td class="text-right"><input type="submit" name="operation"
						class="btn btn-primary" value="<%=StudentListCtl.OP_NEXT%>"
						${list.size() < pageSize || model.nextPK()-1 == bean.id ? 'disabled' : ''}></td>
				</tr>
			</table>
		</c:if>
		<c:if test="${empty list}">
			<div class="text-center">
				<input type="submit" name="operation" class="btn btn-primary"
					value="<%=StudentListCtl.OP_BACK%>">
			</div>
		</c:if>
		<input type="hidden" name="pageNo" value="${pageNo}"> <input
			type="hidden" name="pageSize" value="${pageSize}">
	</div>
</form>
<%@include file="Footer.jsp"%>
</body>
</html>