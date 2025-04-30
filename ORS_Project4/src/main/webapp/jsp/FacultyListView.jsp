<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@page import="com.rays.pro4.controller.ORSView"%>
<%@page import="com.rays.pro4.controller.FacultyListCtl"%>
<%@page import="com.rays.pro4.Util.HTMLUtility"%>
<%@page import="com.rays.pro4.Util.DataUtility"%>
<%@page import="com.rays.pro4.Util.ServletUtility"%>
<%@page import="java.util.List"%>
<%@page import="com.rays.pro4.Bean.FacultyBean"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">
<html>
<head>
<link rel="icon" type="image/png" href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16*16" />
<title>Faculty List</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
<script src="<%=ORSView.APP_CONTEXT%>/js/jquery.min.js"></script>
<script src="<%=ORSView.APP_CONTEXT%>/js/Checkbox11.js"></script>
</head>
<body>
	<%@include file="Header.jsp"%>
	<jsp:useBean id="facultyBean" class="com.rays.pro4.Bean.FacultyBean" scope="request"></jsp:useBean>
	<jsp:useBean id="collegeBean" class="com.rays.pro4.Bean.CollegeBean" scope="request"></jsp:useBean>
	<jsp:useBean id="courseBean" class="com.rays.pro4.Bean.CourseBean" scope="request"></jsp:useBean>

	<form action="${pageContext.request.contextPath}${ORSView.FACULTY_LIST_CTL}" method="post">
		<div class="container">
			<div class="text-center">
				<h1>Faculty List</h1>
				<div>
					<span class="success-message"> ${successMessage}</span> <span class="error-message">${errorMessage}</span>
				</div>
			</div>
			<c:set var="collegeList" value="${requestScope.CollegeList}" />
			<c:set var="courseList" value="${requestScope.CourseList}" />
			<c:set var="next" value="${requestScope.nextlist}" />
			<c:set var="pageNo" value="${requestScope.pageNo}" />
			<c:set var="pageSize" value="${requestScope.pageSize}" />
			<c:set var="index" value="${(pageNo - 1) * pageSize + 1}" />
			<c:set var="facultyList" value="${requestScope.list}" />

			<c:if test="${not empty facultyList}">
				<div class="search-container">
					<label for="firstname">First Name : <input type="text" id="firstname" name="firstname" placeholder="Enter First Name" value="${param.firstname}"> </label>
					<label for="collegeid">College Name : ${HTMLUtility.getList("collegeid", facultyBean.collegeId, collegeList)}</label>
					<label for="courseid">Course Name : ${HTMLUtility.getList("courseid", facultyBean.courseId, courseList)}</label>
					<input type="submit" name="operation" value="${FacultyListCtl.OP_SEARCH}"> <input type="submit" name="operation" value="${FacultyListCtl.OP_RESET}">
				</div>
				<table class="list-table">
					<tr>
						<th><input type="checkbox" id="select_all" name="Select"> Select All</th>
						<th>S.No.</th>
						<th>First Name.</th>
						<th>Last Name.</th>
						<th>EmailId.</th>
						<th>College Name.</th>
						<th>Course Name.</th>
						<th>Subject Name.</th>
						<th>DOB.</th>
						<th>Mobile No.</th>
						<th>Edit</th>
					</tr>
					<c:forEach var="faculty" items="${facultyList}">
						<tr>
							<td><input type="checkbox" class="checkbox" name="ids" value="${faculty.id}"></td>
							<td>${index} <c:set var="index" value="${index + 1}" /></td>
							<td>${faculty.firstName}</td>
							<td>${faculty.lastName}</td>
							<td>${faculty.emailId}</td>
							<td>${faculty.collegeName}</td>
							<td>${faculty.courseName}</td>
							<td>${faculty.subjectName}</td>
							<td>${faculty.dob}</td>
							<td>${faculty.mobileNo}</td>
							<td><a href="FacultyCtl?id=${faculty.id}">Edit</a></td>
						</tr>
					</c:forEach>
				</table>
				<div class="button-container">
					<c:choose>
						<c:when test="${pageNo == 1}">
							<input type="submit" name="operation" disabled="disabled" value="${FacultyListCtl.OP_PREVIOUS}">
						</c:when>
						<c:otherwise>
							<input type="submit" name="operation" value="${FacultyListCtl.OP_PREVIOUS}">
						</c:otherwise>
					</c:choose>
					<input type="submit" name="operation" value="${FacultyListCtl.OP_DELETE}">
					<input type="submit" name="operation" value="${FacultyListCtl.OP_NEW}">
					<c:choose>
						<c:when test="${facultyList.size() < pageSize || next == 0}">
							<input type="submit" name="operation" disabled="disabled" value="${FacultyListCtl.OP_NEXT}">
						</c:when>
						<c:otherwise>
							<input type="submit" name="operation" value="${FacultyListCtl.OP_NEXT}">
						</c:otherwise>
					</c:choose>
				</div>
			</c:if>
			<c:if test="${empty facultyList}">
				<div class="button-container">
					<input type="submit" name="operation" value="${FacultyListCtl.OP_BACK}">
				</div>
			</c:if>
			<input type="hidden" name="pageNo" value="${pageNo}">
			<input type="hidden" name="pageSize" value="${pageSize}">
			</div>
	</form>
	<%@include file="Footer.jsp"%>
</body>
</html>