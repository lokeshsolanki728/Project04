--- a/ORS_Project4/src/main/webapp/jsp/LoginView.jsp
+++ b/ORS_Project4/src/main/webapp/jsp/LoginView.jsp

--- a/ORS_Project4/src/main/webapp/jsp/LoginView.jsp
+++ b/ORS_Project4/src/main/webapp/jsp/LoginView.jsp

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@page import="com.rays.pro4.controller.LoginCtl" %>
<%@page import="com.rays.pro4.controller.ORSView" %>

<!DOCTYPE html>
<html>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<head>

    <meta charset="ISO-8859-1">
    <link rel="icon" type="image/png" href="${ctx}/img/logo.png" sizes="16*16"/>
    <title>Login</title>
    <link rel="stylesheet" href="${ctx}/css/style.css">
</head>
<body>
<form action="${ctx}${ORSView.LOGIN_CTL}" method="post">
    <%@ include file="Header.jsp" %>

    <jsp:useBean id="bean" class="com.rays.pro4.Bean.UserBean" scope="request"></jsp:useBean>

    <input type="hidden" name="URI" value="${URI}">
    <div class="container">
        <h1>Login</h1>
        <c:if test="${not empty successMessage}">
            <div class="success">${successMessage}</div>
        </c:if>
        <c:if test="${not empty errorMessage}">
            <div class="error">${errorMessage}</div>
        </c:if>

        <div class="hidden-inputs">
            <input type="hidden" name="id" value="${bean.id}">
            <input type="hidden" name="createdBy" value="${bean.createdBy}">
            <input type="hidden" name="modifiedBy" value="${bean.modifiedBy}">
            <input type="hidden" name="createdDatetime" value="${bean.createdDatetime}">
            <input type="hidden" name="modifiedDatetime" value="${bean.modifiedDatetime}">
        </div>

            <div class="row">
                <label for="login">LoginId <span class="required">*</span> </label>
                <input type="text" id="login" name="login"  placeholder="Enter valid Email-Id" value="${bean.login}" class="form-control">
              
                <div class="error">${requestScope.login}</div>
            </div>

            <div class="row">
                <label for="password">Password <span class="required">*</span></label>
                <input type="password" id="password" name="password"  placeholder="Enter Password" value="${bean.password}" class="form-control">
              
               <div class="error">${requestScope.password}</div>
            </div>

            <div class="button-container">
                <input type="submit" name="operation" value="${LoginCtl.OP_SIGN_IN}">
                <input type="submit" name="operation" value="${LoginCtl.OP_SIGN_UP}">
            </div>

            <div>
                <a href="${ctx}${ORSView.FORGET_PASSWORD_CTL}">Forget my password</a>
            </div>
        </div>
        </form>
        <%@ include file="Footer.jsp" %>


</body>
</html>
