<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<%@page import="com.rays.pro4.controller.LoginCtl" %>
<%@page import="com.rays.pro4.controller.ORSView"%>
<%@page import="com.rays.pro4.Bean.UserBean"%>

<!DOCTYPE html>
<html lang="en">
<c:set var="ctx" value="${pageContext.request.contextPath}" />


<head>
    <head>
        <meta charset="ISO-8859-1">
        <link rel="icon" type="image/png" href="${ctx}/img/logo.png" sizes="16*16"/>
        <title>Login</title>
        <link rel="stylesheet" href="${ctx}/css/style.css">
    </head>
    <body>
		<form action="${ctx}${ORSView.LOGIN_CTL}" method="post">
            <%@ include file="Header.jsp" %>
            <jsp:useBean id="bean" class="com.rays.pro4.Bean.UserBean"
                         scope="request"></jsp:useBean>
            <c:set var="errors" value="${requestScope.errors}"/>
            <input type="hidden" name="URI" value="${URI}">
            <div class="container">
                <h1>Login</h1>
                <div class="message-container">
                    <c:if test="${not empty success}">
                        <div class="alert alert-success" role="alert">${success}</div>
                    </c:if>
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger" role="alert">${error}</div>
                    </c:if>
                </div>
                

                <div class="input-container">
					<label for="login">LoginId <span class="required">*</span></label>
					<input type="text" id="login" name="login"
						placeholder="Enter valid Email-Id" value="${bean.login}"
						class="form-control"> <span class="error-message">${requestScope.login}</span>
				</div>

				<div class="input-container">
                    <label for="password">Password <span class="required">*</span></label>
                    <input type="password" id="password" name="password"
                           placeholder="Enter Password" class="form-control">
                    <span class="error-message">${errors.password}</span>
				</div>

				<div class="button-container">
					<input type="submit" name="operation" class="btn btn-primary"
						value="${LoginCtl.OP_SIGN_IN}"> <input type="submit"
						name="operation" class="btn btn-success"
						value="${LoginCtl.OP_SIGN_UP}">
				</div>

				<div>
					<a href="${ctx}${ORSView.FORGET_PASSWORD_CTL}">Forget my
						password</a>
				</div>
			</div>
		</form>
		<%@ include file="Footer.jsp"%>
	</body>
</html>

