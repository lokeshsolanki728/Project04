--- a/ORS_Project4/src/main/webapp/jsp/CourseListView.jsp
+++ b/ORS_Project4/src/main/webapp/jsp/CourseListView.jsp
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.rays.pro4.controller.CourseListCtl"%>
<%@ page import="com.rays.pro4.controller.ORSView"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"

	pageEncoding="ISO-8859-1"%>

<html lang="en">
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<jsp:useBean id="bean" class="com.rays.pro4.Bean.CourseBean"
	scope="request" />
<head>
<link rel="icon" type="image/png" href="${ctx}/img/logo.png"
	sizes="16*16" />
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">

<title>
	<c:choose>
		<c:when test="${not empty requestScope.list}">Course List</c:when>
	</c:choose>
</title>
<link rel="stylesheet" href="${ctx}/css/style.css">
</head>

<body class="bg-light">

	<form action="${ctx}${ORSView.COURSE_LIST_CTL}" method="post">
		<%@ include file="Header.jsp"%>
		<c:set var="orderBy" value="${requestScope.orderBy}" />
        <c:set var="sortOrder" value="${requestScope.sortOrder}" />
		<div class="container">
			<h1 class="text-center">Course List</h1>
			<div class="message-container">
				<c:if test="${not empty requestScope.error}">
					<div class="alert alert-danger" role="alert">${requestScope.error}</div>
				</c:if>
				<c:if test="${not empty requestScope.success}">
					<div class="alert alert-success" role="alert">${requestScope.success}</div>
				</c:if>
			</div>


			<c:set var="list" value="${requestScope.list}" />
			<c:set var="nextPage" value="${requestScope.nextlist}" />
			<c:set var="pageNo" value="${requestScope.pageNo}" />
			<c:set var="pageSize" value="${requestScope.pageSize}" />
			<c:set var="index" value="${(pageNo - 1) * pageSize + 1}" />

			<c:if test="${not empty requestScope.list}">
				<table class="search-table w-100">
					<tr>
						<td class="text-center"><label for="name">Course
								Name:</label> <input type="text" id="name" name="name" placeholder="Enter Course Name" class="form-control-inline" value="${param.name}"> 
								<input type="submit" name="operation" value="${CourseListCtl.OP_SEARCH}" class="btn btn-primary"> 
								<input type="submit" name="operation" value="${CourseListCtl.OP_RESET}" class="btn btn-secondary"></td>
					</tr>
				</table>
				<table class="table table-bordered table-striped w-100">
					<thead class="table-header">
						<tr>
							<th><input type="checkbox" id="select_all" name="select">Select
								All</th>
							<th>S.NO.</th>
							<th><a href="${ctx}${ORSView.COURSE_LIST_CTL}?orderBy=name&sortOrder=${sortOrder eq 'asc' ? 'desc' : 'asc'}&pageNo=${pageNo}&pageSize=${pageSize}&name=${param.name}">Course
                                    Name
                                    <c:if test="${orderBy eq 'name'}">
                                        <c:choose>
                                            <c:when test="${sortOrder eq 'asc'}">
                                                <i class="fas fa-arrow-up"></i>
                                            </c:when>
                                            <c:otherwise>
                                                <i class="fas fa-arrow-down"></i>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:if>
                                </a>
                            </th>
							<th>Duration</th>
							<th>Description</th>
							<th>Edit</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="course" items="${list}" varStatus="loop">
							<tr class="text-center table-row">
								<td><input type="checkbox" class="checkbox" name="ids"
									value="${course.id}"></td>
								<td>${index + loop.index}</td>
								<td><c:out value="${course.name}" /></td>
								<td><c:out value="${course.duration}" /></td>
								<td><c:out value="${course.description}" /></td>
								<td><a href="${ctx}${ORSView.COURSE_CTL}?id=${course.id}"
									class="btn btn-link">Edit</a></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<table class="w-100">
					<tr>
					<td class="float-start">
						<input type="submit" name="operation"
							class="btn btn-secondary" value="${CourseListCtl.OP_PREVIOUS}"
							${pageNo == 1 ? 'disabled' : ''}>
						</td>
						<td class="float-center">
							<input type="submit" name="operation" value="${CourseListCtl.OP_DELETE}"
								class="btn btn-danger">
						</td>
						<td><input type="submit" name="operation"
							value="${CourseListCtl.OP_NEW}" class="btn btn-success"></td>
						<td class="float-end">
							<input type="submit" name="operation" value="${CourseListCtl.OP_NEXT}"
								class="btn btn-primary"
								${list.size() < pageSize || nextPage == 0 ? 'disabled' : ''}>
						</td>
					</tr>
				</table>
			</c:if>
			<c:if test="${empty requestScope.list}">
				<div class="text-center">
					<h3>No Result Found</h3>
				</div> 
				</c:if>
                <input type="hidden" name="orderBy" value="${orderBy}">
                <input type="hidden" name="sortOrder" value="${sortOrder}">

			<input type="hidden" name="pageNo" value="${pageNo}"> <input
				type="hidden" name="pageSize" value="${pageSize}">
		</div>
	</form>
	<script src="${ctx}/js/jquery.min.js"></script>
	<script src="${ctx}/js/Checkbox11.js"></script>
	<%@ include file="Footer.jsp"%>
</body>
</html>
