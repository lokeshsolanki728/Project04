<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.rays.pro4.Bean.SubjectBean"%>
<%@page import="com.rays.pro4.Bean.CollegeBean"%>
<%@page import="com.rays.pro4.Bean.CourseBean"%>
<%@page import="java.util.List"%>
<%@page import="com.rays.pro4.Util.HTMLUtility"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.rays.pro4.controller.FacultyCtl"%>
<%@page import="com.rays.pro4.Util.DataUtility"%>
<%@page import="com.rays.pro4.Util.ServletUtility"%>
<%@page import="com.rays.pro4.controller.ORSView"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" isELIgnored="false"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>	
<link rel="icon" type="image/png" href="<%=ORSView.APP_CONTEXT%>/img/logo.png"
	sizes="16*16" />
<title>Faculty Registration Page</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="<%=ORSView.APP_CONTEXT%>/js/jquery-ui-1.12.1/jquery-ui.css">
<link rel="stylesheet" href="<%=ORSView.APP_CONTEXT%>/css/style.css">
<script src="<%=ORSView.APP_CONTEXT%>/js/jquery-1.12.4.js"></script>
<script src="<%=ORSView.APP_CONTEXT%>/js/jquery-ui-1.12.1/jquery-ui.js"></script>
<script>
	$(function() {
		$("#date").datepicker({
			changeMonth: true,
			changeYear: true,
			yearRange: '1980:2020',
		});
	});
</script>
 <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
	<jsp:useBean id="bean" class="com.rays.pro4.Bean.FacultyBean"
		scope="request"></jsp:useBean>
	<%@include file="Header.jsp"%>
	<form action="<%=ORSView.FACULTY_CTL%>" method="post">
		<%
			List<CollegeBean> colist = (List<CollegeBean>) request.getAttribute("CollegeList");
			List<CourseBean> clist = (List<CourseBean>) request.getAttribute("CourseList");
			List<SubjectBean> slist = (List<SubjectBean>) request.getAttribute("SubjectList");
		%>
		<div class="container">
			<h1>
				<c:choose>
					<c:when test="${not empty bean.id}">Update Faculty</c:when>
					<c:otherwise>Add Faculty</c:otherwise>
				</c:choose>
			</h1>
			<div>
				<c:if test="${not empty successMessage}">
					<div class="alert alert-success" role="alert">${successMessage}</div>
				</c:if>
				<c:if test="${not empty errorMessage}">
					<div class="alert alert-danger" role="alert">${errorMessage}</div>
				</c:if>
			</div>
			<input type="hidden" name="id" value="${bean.id}"> <input
				type="hidden" name="createdby" value="${bean.createdBy}"> <input
				type="hidden" name="modifiedby" value="${bean.modifiedBy}"> <input
				type="hidden" name="createdDatetime" value="${bean.createdDatetime}">
			<input type="hidden" name="modifiedDatetime" value="${bean.modifiedDatetime}">

			<div class="input-container">
				<label for="firstname">First Name <span class="required">*</span>
					:</label> <input type="text" id="firstname" name="firstname"
					placeholder=" Enter First Name" class="form-control" value="${bean.firstName}">
				<div class="error">${requestScope.firstname}</div>
			</div>
			<div class="input-container">
				<label for="lastname">Last Name <span class="required">*</span>
					:</label> <input type="text" id="lastname" name="lastname" class="form-control"
					placeholder=" Enter last Name" value="${bean.lastName}">
				<div class="error">${requestScope.lastname}</div>
			</div>
			<div class="input-container">
				<label for="gender">Gender <span class="required">*</span>
					:</label> <select id="gender" name="gender" class="form-control">
					<option value="">Select Gender</option>
					<option value="Male" ${bean.gender == 'Male' ? 'selected' : ''}>Male</option>
					<option value="Female" ${bean.gender == 'Female' ? 'selected' : ''}>Female</option>
				</select>
				<div class="error">${requestScope.gender}</div>
			</div>
			<div class="input-container">
				<label for="collegeid">CollegeName <span class="required">*</span>
					:</label> ${requestScope.collegeid}
				<div class="error">${requestScope.collegeid}</div>
			</div>
			<div class="input-container">
				<label for="courseid">CourseName <span class="required">*</span>
					:</label> ${requestScope.courseid}
				<div class="error">${requestScope.courseid}</div>
			</div>
			<div class="input-container">
				<label for="subjectid">SubjectName <span class="required">*</span>
					:</label> ${requestScope.subjectid}
				<div class="error">${requestScope.subjectid}</div>
			</div>
			<div class="input-container">
				<label for="date">Date Of Birth <span class="required">*</span>
					:</label> <input type="text" name="dob" placeholder="Enter Date Of Birth" class="form-control"
					readonly="readonly" id="date" value="${bean.dob}">
				<div class="error">${requestScope.dob}</div>
			</div>
			<div class="input-container">
				<label for="loginid">LoginId <span class="required">*</span>
					:</label> <input type="text" id="loginid" name="loginid" class="form-control"
					placeholder=" Enter Login Id" value="${bean.emailId}">
				<div class="error">${requestScope.loginid}</div>
			</div>
			<div class="input-container">
				<label for="mobileno">MobileNo <span class="required">*</span>
					:</label> <input type="text" id="mobileno" name="mobileno" class="form-control" maxlength="10"
					placeholder=" Enter Mobile No" value="${bean.mobileNo}">
				<div class="error">${requestScope.mobileno}</div>
			</div>
			<div class="button-container">
				<c:choose>
					<c:when test="${not empty bean.id}">
						<input type="submit" name="operation"
							value="<%=FacultyCtl.OP_UPDATE%>"> <input type="submit"
							name="operation" value="<%=FacultyCtl.OP_CANCEL%>">
					</c:when>
					<c:otherwise>
						<input type="submit" name="operation"
							value="<%=FacultyCtl.OP_SAVE%>"> <input type="submit"
							name="operation" value="<%=FacultyCtl.OP_RESET%>">
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</form>
	<%@include file="Footer.jsp"%>
</body>