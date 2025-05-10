
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@page import="com.rays.pro4.controller.ORSView"%>
<%@page import="com.rays.pro4.controller.ForgetPasswordCtl"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<head>
<link rel="icon" type="image/png" href="${ctx}/img/logo.png" sizes="16*16" />
<title>Forget Password</title>
<link rel="stylesheet" href="${ctx}/css/style.css">
</head>
<body>
	<jsp:useBean id="bean" class="com.rays.pro4.DTO.UserDTO"
		scope="request"></jsp:useBean>
	<c:set var="errors" value="${requestScope.errors}"/>
	<%@ include file="Header.jsp"%>
	<form action="${ctx}/ForgetPasswordCtl" method="post">
		<div class="container ">
			<h1 class="text-center">Forgot your password ?</h1>
			<p class="text-center">Submit your Email address to reset your password.</p>

			<%@ include file="message.jsp"%>

			<div class="input-container">
				<label for="login">Email<span class="required">*</span></label>
                <input type="text" id="login" name="login" placeholder="Enter Email Address" value="${bean.login}" class="form-control">
                <span class="error-message">${errors.login}</span>
			</div>
			<div class="button-container">
				<input type="submit" name="operation" class="btn btn-primary" value="${ForgetPasswordCtl.OP_GO}"> <input type="submit" name="operation" class="btn btn-secondary" value="${ForgetPasswordCtl.OP_RESET}">
			</div>
		</div>
	</form>
	<%@ include file="Footer.jsp"%>
</body>
</html>
