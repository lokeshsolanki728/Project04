<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@page import="com.rays.pro4.controller.ChangePasswordCtl" %>
<%@ page import="com.rays.pro4.controller.ORSView" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<html>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<head>
    <title>
        <c:choose>
            <c:when test="${not empty bean.id}">Change Password</c:when>
        </c:choose>
    </title>
    <link rel="icon" type="image/png" href="${ctx}/img/logo.png" sizes="16x16"/>
    <link rel="stylesheet" href="${ctx}/css/style.css">
</head>
<body >
<form action="${ctx}${ORSView.CHANGE_PASSWORD_CTL}" method="post">

    <%@ include file="Header.jsp" %>
    <jsp:useBean id="bean" class="com.rays.pro4.Bean.UserBean" scope="request"/>
    <div class="container">
        <h1 >Change Password</h1>
        <div>
            <c:if test="${not empty successMessage}">
                <div class="alert alert-success" role="alert">${successMessage}</div>
            </c:if>
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger" role="alert">${errorMessage}</div>
            </c:if>
        </div>
        <div class="input-container">
            <input type="hidden" name="id" value="${bean.id}"> <input type="hidden" name="createdBy" value="${bean.createdBy}">
            <input type="hidden" name="modifiedBy" value="${bean.modifiedBy}">
            <input type="hidden" name="createdDatetime" value="${bean.createdDatetime}">
            <input type="hidden" name="modifiedDatetime" value="${bean.modifiedDatetime}">
        </div>
        <div class="input-container">
            <label  for="oldPassword">Old Password<span class="required">*</span></label>
            <input type="password" id="oldPassword" name="oldPassword" placeholder="Enter Old Password" value="${param.oldPassword}">
            <div class="error">${requestScope.oldPassword}</div>
        </div>

		<div class="input-container">
                <label for="newPassword">New Password<span class="required">*</span></label>
                <input type="password" id="newPassword" name="newPassword" placeholder="Enter New Password" value="${param.newPassword}">
				<div class="error">${requestScope.newPassword}</div>
        </div>
		<div class="input-container">
                <label for="confirmPassword">Confirm Password<span class="required">*</span></label>
                <input type="password" id="confirmPassword" name="confirmPassword" placeholder="Enter Confirm Password" value="${param.confirmPassword}">
				<div class="error">${requestScope.confirmPassword}</div>
        </div>
            <div class="button-container">
                <input type="submit" name="operation" value="${ChangePasswordCtl.OP_SAVE}">
                &nbsp; <input type="submit" name="operation" value="${ChangePasswordCtl.OP_CHANGE_MY_PROFILE}">
            </div>
        </div>
</form>
<%@ include file="Footer.jsp" %>
</body>
</html>
