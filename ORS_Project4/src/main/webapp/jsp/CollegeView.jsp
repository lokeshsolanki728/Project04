<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.rays.pro4.controller.ORSView"%>
<%@page import="com.rays.pro4.controller.CollegeCtl"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html>
<head>
<link rel="icon" type="image/png"
	href="${pageContext.request.contextPath}/img/logo.png" sizes="16x16" />
<title>College</title>
<meta charset="utf-8">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/demos/style.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
<style type="text/css">
</style>
</head>

<body>
	<form action="${pageContext.request.contextPath}/CollegeCtl"
		method="POST">
		<%@ include file="Header.jsp"%>

		<jsp:useBean id="bean" class="com.rays.pro4.Bean.CollegeBean"
			scope="request"></jsp:useBean>
		<div class="container">
			<div class="main-content">
				<h1 class="content-header">
					<c:choose>
						<c:when test="${not empty bean.id}">Update College</c:when>
						<c:otherwise>Add College</c:otherwise>
					</c:choose>
				</h1>
				<div class="error-success">
					<c:if test="${not empty successMessage}">
						<div class="alert alert-success" role="alert">${successMessage}</div>
					</c:if>
					<c:if test="${not empty errorMessage}">
						<div class="alert alert-danger" role="alert">${errorMessage}</div>
					</c:if>
				</div>
				<input type="hidden" name="id" value="${bean.id}"> <input
					type="hidden" name="createdBy" value="${bean.createdBy}"> <input
					type="hidden" name="modifiedBy" value="${bean.modifiedBy}">
				<input type="hidden" name="createdDatetime" value="${bean.createdDatetime}">
				<input type="hidden" name="modifiedDatetime" value="${bean.modifiedDatetime}">

				<div class="input-container">
					<label for="name">Name<span class="required">*</span></label> <input
						type="text" id="name" name="name" placeholder="Enter College Name" class="form-control"
						value="${bean.name}">
					<div class="error-message">${requestScope.name}</div>
				</div>
				<div class="input-container">
					<label for="address">Address<span class="required">*</span></label>
					<input type="text" id="address" name="address" class="form-control"
						placeholder="Enter Address" value="${bean.address}">
					<div class="error-message">${requestScope.address}</div>
				</div>
				<div class="input-container">
					<label for="state">State<span class="required">*</span></label> <input
						type="text" id="state" name="state" class="form-control"
						placeholder="Enter State" value="${bean.state}">
					<div class="error-message">${requestScope.state}</div>
				</div>
				<div class="input-container">
					<label for="city">City<span class="required">*</span></label> <input
						type="text" id="city" name="city" class="form-control" placeholder="Enter City"
						value="${bean.city}">
					<div class="error-message">${requestScope.city}</div>
				</div>
				<div class="input-container">
					<label for="phoneNo">Phone No<span class="required">*</span></label>
					<input type="number" id="phoneNo" name="phoneNo" class="form-control"
						maxlength="10" placeholder="Enter Phone No." value="${bean.phoneNo}">
					<div class="error-message">${requestScope.phoneNo}</div>
				</div>

				<div class="button-container">
					<input type="submit" name="operation"
						value="<c:out value="${empty bean.id ? CollegeCtl.OP_SAVE : CollegeCtl.OP_UPDATE}" />">
					<input type="submit" name="operation"
						value="<c:out value="${empty bean.id ? CollegeCtl.OP_RESET : CollegeCtl.OP_CANCEL}" />">
				</div>
			</div>
		</div>
	</form>

	<%@ include file="Footer.jsp"%>
</body>
	</form>

	<%@ include file="Footer.jsp"%>
</body>
</html>