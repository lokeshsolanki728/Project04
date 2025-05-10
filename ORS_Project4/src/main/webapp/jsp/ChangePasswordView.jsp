<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ page import="com.rays.pro4.controller.ORSView" %>
<%@ page import="com.rays.pro4.controller.ChangePasswordCtl" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
<head>
    <c:set var="ctx" value="${pageContext.request.contextPath}"/>
    <title>Change Password</title>
    <link rel="icon" type="image/png" href="${ctx}/img/logo.png" sizes="16x16"/>
    <link rel="stylesheet" href="${ctx}/css/style.css"/>
</head>
<body>
<form action="${ctx}${ORSView.CHANGE_PASSWORD_CTL}" method="post">
    <%@ include file="Header.jsp" %>

    <jsp:useBean id="bean" class="com.rays.pro4.DTO.UserDTO" scope="request"></jsp:useBean>
    <c:set var="errors" value="${requestScope.errors}"/>
    <div class="message-container">
        <c:if test="${not empty success}">
            <div class="alert alert-success" role="alert">${success}</div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="alert alert-danger" role="alert">${error}</div>
        </c:if>
    </div>
        <div class="container">
            <h1 class="text-center">Change Password</h1>
            <div class="message-container">
                <c:if test="${not empty msg}">
					<div class="alert alert-success" role="alert">
						<c:out value="${msg}"></c:out>
					</div>
				</c:if>
				<c:if test="${not empty error}">
					<div class="alert alert-danger" role="alert">
						<c:out value="${error}"></c:out>
					</div>
				</c:if>
            </div>

            <table class="table table-borderless w-50 mx-auto">
                <tr>
                    <th class="text-left">
                        <label for="oldPassword">Old Password<span class="required">*</span></label>
                    </th>
                    <td>
                        <input type="password" id="oldPassword" name="oldPassword" placeholder="Enter Old Password"
                            value="" class="form-control">
                         <span class="error-message">${errors.oldPassword}</span>
                    </td>
                </tr>
                <tr>
                    <th class="text-left">
                        <label for="newPassword">New Password<span class="required">*</span></label>
                    </th>
                    <td>
                        <input type="password" id="newPassword" name="newPassword" placeholder="Enter New Password"
                            value="" class="form-control">
                           <span class="error-message">${errors.newPassword}</span>
                    </td>
                </tr>
                <tr>
                    <th class="text-left">
                        <label for="confirmPassword">Confirm Password<span class="required">*</span></label>
                    </th>
                    <td>
                        <input type="password" id="confirmPassword" name="confirmPassword"
                            placeholder="Enter Confirm Password" value="" class="form-control">
                         <span class="error-message">${errors.confirmPassword}</span>
                    </td>
                </tr>
                <tr>
                    <th></th>
                    <td>
                        <div class="button-container">
                            <input type="submit" name="operation" value="${ChangePasswordCtl.OP_SAVE}"
                                class="btn btn-primary">
                            <input type="submit" name="operation"
                                value="${ChangePasswordCtl.OP_CHANGE_MY_PROFILE}" class="btn btn-secondary">
                        </div>
                    </td>
                </tr>
            </table>
        </div>
    </form>
    <%@ include file="Footer.jsp"%>
</body>
</html>
