<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@page import="com.rays.pro4.controller.ChangePasswordCtl"%>
<%@ page import="com.rays.pro4.controller.ORSView"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<html>
<head>
    <c:set var="ctx" value="${pageContext.request.contextPath}" />
    <title>
        <c:choose>
            <c:when test="${not empty bean.id}">Change Password</c:when>
        </c:choose>
    </title>
    <link rel="icon" type="image/png" href="${ctx}/img/logo.png" sizes="16x16" />
    <link rel="stylesheet" href="${ctx}/css/style.css" />
</head>
<body>
    <form action="${ctx}${ORSView.CHANGE_PASSWORD_CTL}" method="post">
        <%@ include file="Header.jsp"%>
        <jsp:useBean id="bean" class="com.rays.pro4.Bean.UserBean" scope="request" />
        <div class="container">
            <h1 class="text-center">Change Password</h1>
            <div class="message-container">
                <c:if test="${not empty requestScope.success}">
                    <div class="alert alert-success" role="alert">${requestScope.success}</div>
                </c:if>
                <c:if test="${not empty requestScope.error}">
                    <div class="alert alert-danger" role="alert">${requestScope.error}</div>
                </c:if>
            </div>
            <input type="hidden" name="id" value="${bean.id}">
            <input type="hidden" name="createdBy" value="${bean.createdBy}">
            <input type="hidden" name="modifiedBy" value="${bean.modifiedBy}">
            <input type="hidden" name="createdDatetime" value="${bean.createdDatetime}">
            <input type="hidden" name="modifiedDatetime" value="${bean.modifiedDatetime}">
            <table class="table table-borderless w-50 mx-auto">
                <tr>
                    <th class="text-left">
                        <label for="oldPassword">Old Password<span class="required">*</span></label>
                    </th>
                    <td>
                        <input type="password" id="oldPassword" name="oldPassword" placeholder="Enter Old Password"
                            value="${param.oldPassword}" class="form-control">
                        <span class="error-message">${requestScope.oldPassword}</span>
                    </td>
                </tr>
                <tr>
                    <th class="text-left">
                        <label for="newPassword">New Password<span class="required">*</span></label>
                    </th>
                    <td>
                        <input type="password" id="newPassword" name="newPassword" placeholder="Enter New Password"
                            value="${param.newPassword}" class="form-control">
                        <span class="error-message">${requestScope.newPassword}</span>
                    </td>
                </tr>
                <tr>
                    <th class="text-left">
                        <label for="confirmPassword">Confirm Password<span class="required">*</span></label>
                    </th>
                    <td>
                        <input type="password" id="confirmPassword" name="confirmPassword"
                            placeholder="Enter Confirm Password" value="${param.confirmPassword}" class="form-control">
                        <span class="error-message">${requestScope.confirmPassword}</span>
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
