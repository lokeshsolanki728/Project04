<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@page import="com.rays.pro4.controller.ORSView"%>
<%@page import="com.rays.pro4.controller.SubjectListCtl"%>
<%@page import="com.rays.pro4.Util.DataUtility"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.rays.pro4.Bean.SubjectBean"%>
<%@page import="com.rays.pro4.Bean.CourseBean"%>
<%@page import="java.util.List"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<html>
<head>
<title>Subject List</title>
<link rel="icon" type="image/png" href="${pageContext.request.contextPath}/img/logo.png" sizes="16x16" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
<script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/Checkbox11.js"></script>
<style type="text/css">
.table-style {
	width: 100%;
	border-collapse: collapse;
	margin-top: 20px;
}

.table-style th, .table-style td {
	border: 1px solid #dddddd;
	text-align: left;
	padding: 8px;
}

.table-style th {
	background-color: #f2f2f2;
}

.table-style tr:nth-child(even) {
	background-color: #f9f9f9;
}

.table-style tr:hover {
	background-color: #f1f1f1;
}

.search-form {
	width: 100%;
	margin-bottom: 20px;
}

.search-form label {
	font-weight: bold;
	margin-right: 10px;
}

.search-form select {
	padding: 5px;
	border: 1px solid #ccc;
	border-radius: 4px;
}

.search-form input[type="submit"] {
	padding: 5px 10px;
	background-color: #4CAF50;
	color: white;
	border: none;
	border-radius: 4px;
	cursor: pointer;
}

.search-form input[type="submit"]:hover {
	background-color: #45a049;
}

.btn {
	padding: 5px 10px;
	background-color: #4CAF50;
	color: white;
	border: none;
	border-radius: 4px;
	cursor: pointer;
}

.btn:disabled {
	background-color: #cccccc;
	cursor: default;
}

.btn:hover {
	background-color: #45a049;
}
</style>
</head>
<body>
	<%@include file="Header.jsp"%>
	<div class="container">
		<h1 class="text-center" style="margin-bottom: -15; color: navy;">Subject List</h1>

		<div style="height: 15px; margin-bottom: 12px">
			<c:if test="${not empty errorMessage}">
				<h3 style="color: red;">${errorMessage}</h3>
			</c:if>
			<c:if test="${not empty successMessage}">
				<h3 style="color: green;">${successMessage}</h3>
			</c:if>
		</div>
		<jsp:useBean id="bean" class="com.rays.pro4.Bean.SubjectBean" scope="request"></jsp:useBean>
		<form action="${pageContext.request.contextPath}${ORSView.SUBJECT_LIST_CTL}" method="post">

			<c:set var="pageNo" value="${requestScope.pageNo}" />
			<c:set var="pageSize" value="${requestScope.pageSize}" />
			<c:set var="index" value="${(pageNo - 1) * pageSize + 1}" />
			<c:set var="nextPageSize" value="${requestScope.nextListSize}" />

			<c:if test="${not empty requestScope.courseList}">
				<c:set var="clist" value="${requestScope.courseList}" />
			</c:if>
			<c:if test="${not empty requestScope.subjectList}">
				<c:set var="slist" value="${requestScope.subjectList}" />
			</c:if>
			<c:if test="${not empty requestScope.list}">
				<c:set var="list" value="${requestScope.list}" />
			</c:if>

			<c:if test="${not empty list}">
				<input type="hidden" name="pageNo" value="${pageNo}">
				<input type="hidden" name="pageSize" value="${pageSize}">

				<div class="search-form">
				<table class="table-style" style="width: 100%">
					<tr>
						<td align="center">
							<label for="subjectId"><b>Subject Name :</b></label> 
							<select name="subjectId" id="subjectId">
								<option value=""
									<%=(request.getAttribute("bean") != null && ((com.rays.pro4.Bean.SubjectBean) request.getAttribute("bean")).getId() == 0) ? "selected" : ""%>>Select
									Subject</option>
								<c:forEach items="${slist}" var="s">
									<option value="${s.id}"
										${s.id == bean.id ? 'selected' : ''}>${s.subjectName}</option>
								</c:forEach>
							</select>
							<label for="courseId"><b>Course Name :</b></label> 
							<select name="courseId" id="courseId">
								<option value=""
									<%=(request.getAttribute("bean") != null && ((com.rays.pro4.Bean.SubjectBean) request.getAttribute("bean")).getCourseId() == 0) ? "selected" : ""%>>Select Course</option>
								<c:forEach items="${clist}" var="c">
									<option value="${c.id}" ${c.id == bean.courseId ? 'selected' : ''}>${c.courseName}</option>
								</c:forEach>
							</select>
							<input type="submit" name="operation" value="<%=SubjectListCtl.OP_SEARCH%>">&nbsp; 
							<input type="submit" name="operation" value="<%=SubjectListCtl.OP_RESET%>">
						</td>
					</tr>
				</table>
				</div>
				<br>

				<table class="table-style" border="1" width="100%" align="center" cellpadding="6px" cellspacing=".2">
					<tr style="background: skyblue;">
						<th><input type="checkbox" id="select_all" name="select"> Select All.</th>
						<th width="5%">S.No</th>
						<th width="30%">Subject Name</th>
						<th width="15%">Course Name</th>
						<th width="40%">Description</th>
						<th width="5%">Edit</th>
					</tr>
					<c:forEach items="${list}" var="subject">
						<tr>
							<td><input type="checkbox" class="checkbox" name="ids" value="${subject.id}"></td>
							<td style="text-align: center;">${index}</td>
							<c:set var="index" value="${index + 1}" />
							<td style="text-align: center; text-transform: capitalize;">${subject.subjectName}</td>
							<td style="text-align: center; text-transform: capitalize;">${subject.courseName}</td>
							<td style="text-align: center; text-transform: capitalize;">${subject.description}</td>
							<td style="text-align: center;"><a href="SubjectCtl?id=${subject.id}">Edit</a></td>
						</tr>
					</c:forEach>
				</table>
				<table style="width: 100%">
					<tr>
						<td style="width: 25%"><input class="btn" type="submit" name="operation" value="<%=SubjectListCtl.OP_PREVIOUS%>" ${pageNo > 1 ? '' : 'disabled'}></td>
						<td align="center" style="width: 25%"><input class="btn" type="submit" name="operation" value="<%=SubjectListCtl.OP_NEW%>"></td>
						<td align="center" style="width: 25%"><input class="btn" type="submit" name="operation" value="<%=SubjectListCtl.OP_DELETE%>"></td>
						<td style="width: 25%" align="right"><input class="btn" type="submit" name="operation" value="<%=SubjectListCtl.OP_NEXT%>" ${nextPageSize != 0 ? '' : 'disabled'}></td>
					</tr>
				</table>
			</c:if>
			<c:if test="${empty list}">
				<table>
					<tr>
						<td align="right"><input type="submit" name="operation" value="<%=SubjectListCtl.OP_BACK%>"></td>
					</tr>
				</table>
			</c:if>
		</form>
	</div>
	</br>
	</br>
	</br>
	</br>
	<%@include file="Footer.jsp"%>
</body>
</html>