--- a/ORS_Project4/src/main/webapp/jsp/ForgetPasswordView.jsp
+++ b/ORS_Project4/src/main/webapp/jsp/ForgetPasswordView.jsp

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@page import="com.rays.pro4.controller.ORSView" %>
<%@ page import="com.rays.pro4.controller.ForgetPasswordCtl" %>
<%@page import="com.rays.pro4.Bean.UserBean" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<!DOCTYPE html>
<html>
    <c:set var="ctx" value="${pageContext.request.contextPath}"/>
    <head>
        <link rel="icon" type="image/png" href="${ctx}/img/logo.png" sizes="16*16"/>
        <title>Forget Password</title>
        <link rel="stylesheet" href="${ctx}/css/style.css">
    </head>
    <body>
        <jsp:useBean id="bean" class="com.rays.pro4.Bean.UserBean" scope="request"></jsp:useBean>
        <%@ include file="Header.jsp" %>
        <form action="${ctx}${ORSView.FORGET_PASSWORD_CTL}" method="post">
            <div class="container">
                <h1 class="text-center">Forgot your password ?</h1>
                <p class="text-center">Submit your Email address to reset your password.</p>
                <div class="message-container">
                    <c:if test="${not empty successMessage}">
                        <div class="alert alert-success" role="alert">${successMessage}</div>
                    </c:if>
                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger" role="alert">${errorMessage}</div>
                    </c:if>
                </div>
                <div class="input-container">
                    <input type="hidden" name="id">
                    <label for="login">Email <span class="required">*</span></label>
                    <input type="text" id="login" name="login" placeholder="Enter Email Address"
                           value="${bean.login}"
                           class="form-control">
                    <span class="error-message">${requestScope.login}</span>
                </div>
                <div class="button-container">
                    <input type="submit" name="operation" class="btn btn-primary"
                           value="${ForgetPasswordCtl.OP_GO}">
                    <input type="submit" name="operation" class="btn btn-secondary"
                           value="${ForgetPasswordCtl.OP_RESET}">
                </div>
            </div>
        </form>
        <%@ include file="Footer.jsp" %>
    </body>
</html>
