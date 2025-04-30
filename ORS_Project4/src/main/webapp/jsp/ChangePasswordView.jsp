<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@page import="com.rays.pro4.controller.ChangePasswordCtl"%>
<%@ page import="com.rays.pro4.controller.ORSView"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
<head>
<title>Change Password</title>
<link rel="icon" type="image/png" href="${pageContext.request.contextPath}/img/logo.png" sizes="16x16" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
	<form action="${pageContext.request.contextPath}${ORSView.CHANGE_PASSWORD_CTL}" method="post">

		<%@ include file="Header.jsp"%>
		<jsp:useBean id="bean" class="com.rays.pro4.Bean.UserBean" scope="request" />
		<div class="bodyDiv">
			<h1 class="title">Change Password</h1>
			<div class="message">
				<c:if test="${not empty successMessage}">
					<p class="successMessage">${successMessage}</p>
				</c:if>
				<c:if test="${not empty errorMessage}">
					<p class="errorMessage">${errorMessage}</p>
				</c:if>
			</div>
			<div class="inputDiv">
				<input type="hidden" name="id" value="${bean.id}"> <input
					type="hidden" name="createdBy" value="${bean.createdBy}">
				<input type="hidden" name="modifiedBy" value="${bean.modifiedBy}">
				<input type="hidden" name="createdDatetime" value="${bean.createdDatetime}">
				<input type="hidden" name="modifiedDatetime" value="${bean.modifiedDatetime}">
			</div>
			<div class="inputDiv">
				<label class="label" for="oldPassword">Old Password<span
					class="required">*</span></label> <input type="password"
					id="oldPassword" name="oldPassword" placeholder="Enter Old Password"
					value="${param.oldPassword}">
				<p class="errorMessage fixed">${oldPassword}</p>
			</div>
			<div class="inputDiv">
				<label class="label" for="newPassword">New Password<span
					class="required">*</span></label> <input type="password"
					id="newPassword" name="newPassword" placeholder="Enter New Password"
					value="${param.newPassword}">
				<p class="errorMessage fixed">${newPassword}</p>
			</div>
			<div class="inputDiv">
				<label class="label" for="confirmPassword">Confirm Password<span
					class="required">*</span></label> <input type="password"
					id="confirmPassword" name="confirmPassword" placeholder="Enter Confirm Password"
					value="${param.confirmPassword}">
				<p class="errorMessage fixed">${confirmPassword}</p>
			</div>
			<div class="buttonDiv">
				<input type="submit" name="operation" value="<%=ChangePasswordCtl.OP_SAVE%>">
				&nbsp; <input type="submit" name="operation" value="<%=ChangePasswordCtl.OP_CHANGE_MY_PROFILE%>">
			</div>
		</div>
	</form>
	<%@ include file="Footer.jsp"%>
</body>
</html>