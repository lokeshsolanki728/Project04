<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@page import="com.rays.pro4.controller.FacultyCtl"%>
<%@page import="com.rays.pro4.controller.ORSView"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"%>\
<%--
	isELIgnored="false" pageEncoding="ISO-8859-1"%>
<html>
<head>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<link rel="icon" type="image/png" href="${ctx}/img/logo.png" sizes="16*16" />
<title><c:choose>
		<c:when test="${not empty bean.id}">Update Faculty
        </c:when>
		<c:otherwise>Add Faculty
        </c:otherwise>
    </c:choose>
</title>
<link rel="stylesheet" href="${ctx}/css/style.css"/>
<script src="${ctx}/js/jquery.min.js"></script>
<script src="${ctx}/js/jquery-ui.min.js"></script>
<script>
	$(function() {$("#date").datepicker({changeMonth: true,changeYear: true,yearRange: '1980:2020'});});
</script>
</head>
<body >
	<jsp:useBean id="bean" class="com.rays.pro4.Bean.FacultyBean"
		scope="request"></jsp:useBean>
	<%@include file="Header.jsp"%>
	<form action="${ctx}${ORSView.FACULTY_CTL}" method="post">
		<%
		%>
			<input type="hidden" name="id" value="${bean.id}"> <input type="hidden" name="createdby" value="${bean.createdBy}"> <input type="hidden" name="modifiedby" value="${bean.modifiedBy}"> <input type="hidden" name="createdDatetime" value="${bean.createdDatetime}"> <input
				type="hidden" name="modifiedDatetime" value="${bean.modifiedDatetime}"><div class="container">
				<h1>
					<c:choose>
						<c:when test="${not empty bean.id}">Update Faculty</c:when>
						<c:otherwise>Add Faculty</c:otherwise>
					</c:choose>
				</h1>
				<div>
					<c:if test="${not empty successMessage}">
						<div class="success">${successMessage}</div>
					</c:if>
					<c:if test="${not empty errorMessage}">
						<div class="error">${errorMessage}</div>
					</c:if>
				</div>
				<div class="input-container">
					<label for="firstname">First Name <span class="required">*</span>
						:</label> <input type="text" id="firstname" name="firstname"
						placeholder=" Enter First Name" class="form-control" value="${bean.firstName}">
					<div class="error">
						${errorMap.firstname}</div>
                </div>
				<div class="input-container">
					<label for="lastname">Last Name <span class="required">*</span>
						:</label> <input type="text" id="lastname" name="lastname" class="form-control"
						placeholder=" Enter last Name" value="${bean.lastName}">
					<div class="error">${errorMap.lastname}</div>
				</div>
				<div class="input-container">
					<label for="gender">Gender <span class="required">*</span>
						:</label> <select id="gender" name="gender" class="form-control">
						<option value="">Select Gender</option>
						<option value="Male" ${bean.gender == 'Male' ? 'selected' : ''}>Male</option>
						<option value="Female" ${bean.gender == 'Female' ? 'selected' : ''}>Female</option>
					</select>
					<div class="error">${errorMap.gender}</div>
				</div>
				<div class="input-container">
					<label for="collegeId">College Name<span class="required">*</span>:</label>
					<select id="collegeId" name="collegeId" class="form-control">
						<option value="">Select College</option>
						<c:forEach items="${CollegeList}" var="college">
							<option value="${college.id}" ${bean.collegeId == college.id ? 'selected' : ''}>${college.name}</option>
						</c:forEach>
					</select>
					<div class="error">${errorMap.collegeId}</div>
				</div>
				<div class="input-container">
					<label for="courseId">Course Name<span class="required">*</span>:</label>
					<select id="courseId" name="courseId" class="form-control">
						<option value="">Select Course</option>
						<c:forEach items="${CourseList}" var="course">
							<option value="${course.id}" ${bean.courseId == course.id ? 'selected' : ''}>${course.name}</option>
						</c:forEach>
					</select>
					<div class="error">${errorMap.courseId}</div>
				</div>
				<div class="input-container">
					<label for="subjectId">Subject Name<span class="required">*</span>:</label>
					<select id="subjectId" name="subjectId" class="form-control">
						<option value="">Select Subject</option>
						<c:forEach items="${SubjectList}" var="subject">
							<option value="${subject.id}" ${bean.subjectId == subject.id ? 'selected' : ''}>${subject.subjectName}</option>
						</c:forEach>
					</select>
					<div class="error">${errorMap.subjectId}</div>
				</div>
				<div class="input-container">
					<label for="date">Date Of Birth <span class="required">*</span>
						:</label> <input type="text" name="dob" placeholder="Enter Date Of Birth"
						class="form-control" readonly="readonly" id="date" value="${bean.dob}">
					<div class="error">${errorMap.dob}</div>
				</div>
				<div class="input-container">
					<label for="loginid">LoginId <span class="required">*</span>
						:</label> <input type="text" id="loginid" name="loginid" class="form-control"
						placeholder=" Enter Email Id" value="${bean.emailId}">
					<div class="error">${errorMap.emailId}</div>
				</div>
				<div class="input-container">
					<label for="mobileno">MobileNo <span class="required">*</span>
						:</label> <input type="text" id="mobileno" name="mobileno"
						class="form-control" maxlength="10" placeholder=" Enter Mobile No"
						value="${bean.mobileNo}">
					<div class="error">${errorMap.mobileno}</div>
				</div>
				<div class="button-container">
					<c:choose>
						<c:when test="${not empty bean.id}">
							<input type="submit" name="operation" value="${FacultyCtl.OP_UPDATE}">
							<input type="submit" name="operation" value="${FacultyCtl.OP_CANCEL}">
						</c:when>
						<c:otherwise>
							<input type="submit" name="operation" value="${FacultyCtl.OP_SAVE}">
							<input type="submit" name="operation" value="${FacultyCtl.OP_RESET}">
						</c:otherwise>
					</c:choose>
				</div>
			</div></div>
	</form>
	<%@include file="Footer.jsp"%>
</body>