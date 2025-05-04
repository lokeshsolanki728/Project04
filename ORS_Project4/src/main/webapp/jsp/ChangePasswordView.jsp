<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="com.rays.pro4.util.DataUtility"%>
<%@ page import="com.rays.pro4.controller.ORSView"%>
<%@ page import="com.rays.pro4.controller.ChangePasswordCtl"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

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
    <form action="<%=ORSView.CHANGE_PASSWORD_CTL%>" method="post">
        <%@ include file="Header.jsp"%>
        
        <jsp:useBean id="bean" class="com.rays.pro4.Bean.UserBean" scope="request"></jsp:useBean>
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
            <input type="hidden" name="id" value="<%=DataUtility.getString(String.valueOf(bean.getId()))%>">
	        <input type="hidden" name="createdBy" value="<%=DataUtility.getString(bean.getCreatedBy())%>">
	        <input type="hidden" name="modifiedBy" value="<%=DataUtility.getString(bean.getModifiedBy())%>">
	        <input type="hidden" name="createdDatetime" value="<%=DataUtility.getString(DataUtility.getTimestamp(bean.getCreatedDatetime()).toString())%>">
	        <input type="hidden" name="modifiedDatetime" value="<%=DataUtility.getString(DataUtility.getTimestamp(bean.getModifiedDatetime()).toString())%>">
	        
            <table class="table table-borderless w-50 mx-auto">
                <tr>
                    <th class="text-left">
                        <label for="oldPassword">Old Password<span class="required">*</span></label>
                    </th>
                    <td>
                        <input type="password" id="oldPassword" name="oldPassword" placeholder="Enter Old Password"
                            value="" class="form-control">
                        <span class="error-message">${oldPassword}</span>
                    </td>
                </tr>
                <tr>
                    <th class="text-left">
                        <label for="newPassword">New Password<span class="required">*</span></label>
                    </th>
                    <td>
                        <input type="password" id="newPassword" name="newPassword" placeholder="Enter New Password"
                            value="" class="form-control">
                        <span class="error-message">${newPassword}</span>
                    </td>
                </tr>
                <tr>
                    <th class="text-left">
                        <label for="confirmPassword">Confirm Password<span class="required">*</span></label>
                    </th>
                    <td>
                        <input type="password" id="confirmPassword" name="confirmPassword"
                            placeholder="Enter Confirm Password" value="" class="form-control">
                        <span class="error-message">${confirmPassword}</span>
                    </td>
                </tr>
                <tr>
                    <th></th>
                    <td>
                        <div class="button-container">
                            <input type="submit" name="operation" value="<%=ChangePasswordCtl.OP_SAVE%>"
                                class="btn btn-primary">
                            <input type="submit" name="operation"
                                value="<%=ChangePasswordCtl.OP_CHANGE_MY_PROFILE%>" class="btn btn-secondary">
                        </div>
                    </td>
                </tr>
            </table>
        </div>
    </form>
    <%@ include file="Footer.jsp"%>
</body>
</html>
