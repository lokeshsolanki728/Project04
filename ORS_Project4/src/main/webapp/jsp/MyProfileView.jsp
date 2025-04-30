
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@page import="com.rays.pro4.controller.MyProfileCtl"%>
<%@page import="com.rays.pro4.controller.ORSView"%>
<%@page import="com.rays.pro4.Util.DataUtility"%>
<%@page import="com.rays.pro4.Util.ServletUtility"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<link rel="icon" type="image/png"
	href="${pageContext.request.contextPath}/img/logo.png" sizes="16*16" />
<title>My Profile</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">


<meta charset="utf-8">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/demos/style.css">
</head>
<body>

	<form action="<%=ORSView.MY_PROFILE_CTL%>" method="post">

		<%@ include file="Header.jsp"%>
		<jsp:useBean id="bean" class="com.rays.pro4.Bean.UserBean"
			scope="request"></jsp:useBean>

		<div class="text-center">
			<h1>My Profile</h1>

			<c:if test="${not empty errorMessage}">
				<div class="alert alert-danger" role="alert">${errorMessage}</div>
			</c:if>
			<c:if test="${not empty successMessage}">
				<div class="alert alert-success" role="alert">${successMessage}</div>
			</c:if>
		</div>
		<input type="hidden" name="id" value="${bean.id}"> <input
			type="hidden" name="createdBy" value="${bean.createdBy}"> <input
			type="hidden" name="modifiedBy" value="${bean.modifiedBy}">
		<input type="hidden" name="createdDatetime"
			value="${bean.createdDatetime}"> <input type="hidden"
			name="modifiedDatetime" value="${bean.modifiedDatetime}">

		<div class="container">
			<table>
				<tr>
					<th align="left"><label for="login">Login Id*</label></th>
					<td><input type="text" id="login" name="login"
						class="form-control" value="${bean.login}" readonly="readonly">
						<div class="error-message">${requestScope.login}</div></td>
				</tr>

				<tr>
					<th align="left"><label for="firstName">First Name*</label></th>
					<td><input type="text" id="firstName" name="firstName"
						class="form-control" value="${bean.firstName}">
						<div class="error-message">${requestScope.firstName}</div></td>
				</tr>
				<tr>
					<th align="left"><label for="lastName">Last Name*</label></th>
					<td><input type="text" id="lastName" name="lastName"
						class="form-control" value="${bean.lastName}">
						<div class="error-message">${requestScope.lastName}</div></td>
				</tr>
				<tr>
					<th align="left"><label for="gender">Gender</label></th>
					<td><input type="text" name="gender" id="gender"
						class="form-control" value="${bean.gender}"></td>
				</tr>
				<tr>
					<th align="left"><label for="mobileNo">Mobile No*</label></th>
					<td><input type="text" id="mobileNo" name="mobileNo"
						class="form-control" value="${bean.mobileNo}">
						<div class="error-message">${requestScope.mobileNo}</div></td>
				</tr>

				<tr>
					<th align="left"><label for="dob">Date Of Birth (mm/dd/yyyy)</label></th>
					<td><input type="text" id="dob" name="dob" readonly="readonly"
						class="form-control" value="${bean.dob}">
						<div class="error-message">${requestScope.dob}</div></td>
				</tr>

				<tr>
					<th></th>
					<td colspan="2"><input type="submit" name="operation"
						value="<%=MyProfileCtl.OP_CHANGE_MY_PASSWORD%>"> &nbsp; <input
						type="submit" name="operation" value="<%=MyProfileCtl.OP_SAVE%>">
						&nbsp;</td>
				</tr>
			</table>
		</div>
    </form>
	<%@ include file="Footer.jsp"%>


</body>
</html>