<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@page import="com.rays.pro4.Util.DataUtility"%>
<%@page import="com.rays.pro4.Util.ServletUtility"%>
<%@page import="com.rays.pro4.controller.ORSView"%>
<%@page import="com.rays.pro4.controller.RoleCtl"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
<head>
<link rel="icon" type="image/png"
	href="${pageContext.request.contextPath}/img/logo.png" sizes="16*16" />
<title>Role</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
	href="${pageContext.request.contextPath}/resources/demos/style.css">



</head>
<body>
	<jsp:useBean id="bean" class="com.rays.pro4.Bean.RoleBean"
		scope="request"></jsp:useBean>
	<form action="${pageContext.request.contextPath}/RoleCtl" method="post">
		<%@ include file="Header.jsp"%>


		<center>


			<div align="center">

				<c:choose>
					<c:when test="${not empty bean.id}">
						<h1>Update Role</h1>
					</c:when>
					<c:otherwise>
						<h1>Add Role</h1>
					</c:otherwise>
				</c:choose>

				<c:if test="${not empty successMessage}">
					<div class="alert alert-success" role="alert">${successMessage}</div>
				</c:if>
				<c:if test="${not empty errorMessage}">
					<div class="alert alert-danger" role="alert">${errorMessage}</div>
				</c:if>
			</div>
			<input type="hidden" name="id" value="${bean.id}"> <input
				type="hidden" name="createdBy" value="${bean.createdBy}"> <input
				type="hidden" name="modifiedBy" value="${bean.modifiedBy}"> <input
				type="hidden" name="createdDatetime" value="${bean.createdDatetime}">
			<input type="hidden" name="modifiedDatetime"
				value="${bean.modifiedDatetime}">


			<table>
				<tr>
					<th align="left"><label for="name">Name <span
							class="required">*</span> :</label></th>
					<td><input type="text" id="name" name="name"
						placeholder="Enter Role Name" class="form-control" size="25"
						value="${bean.name}"></td>
					<td><div class="error-message">${requestScope.name}</div></td>
				</tr>
				<tr>
					<th style="padding: 3px"></th>
				</tr>
				<tr>
					<th align="left"><label for="description">Description <span
							class="required">*</span> :</label></th>
					<td><input type="text" id="description" name="description"
						placeholder="Enter Description" class="form-control" size="25"
						value="${bean.description}"></td>
					<td><div class="error-message">${requestScope.description}</div></td>
				</tr>
				<tr>
					<th style="padding: 3px"></th>
				</tr>
				<tr>
					<th></th>
					<td colspan="2">&nbsp; &emsp; <input type="submit"
						name="operation" value="${RoleCtl.OP_SAVE}"> &nbsp; &nbsp; <input
						type="submit" name="operation" value="${RoleCtl.OP_RESET}"></td>
				</tr>
			</table>
	</form>
	</center>

	<%@ include file="Footer.jsp"%>
</body>
</html>
