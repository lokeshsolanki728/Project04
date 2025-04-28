--- a/ORS_Project4/src/main/webapp/jsp/LoginView.jsp
+++ b/ORS_Project4/src/main/webapp/jsp/LoginView.jsp

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="com.rays.pro4.controller.LoginCtl"%>
<%@page import="com.rays.pro4.Util.DataUtility"%>
<%@page import="com.rays.pro4.Util.ServletUtility"%>
<%@page import="com.rays.pro4.controller.ORSView"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<link rel="icon" type="image/png" href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16*16" />
<title>Login</title>
<link rel="stylesheet" href="<%=ORSView.APP_CONTEXT%>/css/style.css">
</head>
<body>
    <form action="<%=ORSView.LOGIN_CTL%>" method="post">
        <%@ include file="Header.jsp"%>

        <jsp:useBean id="bean" class="com.rays.pro4.Bean.UserBean" scope="request"></jsp:useBean>

        <%
            String uri = (String) request.getAttribute("URI");
        %>
        <input type="hidden" name="URI" value="<%=uri%>">
        <div class="container">
            <h1>Login</h1>
            <div class="message">
                <span class="success-message"><%=ServletUtility.getSuccessMessage(request)%></span>
                <span class="error-message"><%=ServletUtility.getErrorMessage(request)%></span>
            </div>

            <div style="display: none;">
                <input type="hidden" name="id" value="<%=bean.getId()%>">
                <input type="hidden" name="createdBy" value="<%=bean.getCreatedBy()%>">
                <input type="hidden" name="modifiedBy" value="<%=bean.getModifiedBy()%>">
                <input type="hidden" name="createdDatetime" value="<%=DataUtility.getTimestamp(bean.getCreatedDatetime())%>">
                <input type="hidden" name="modifiedDatetime" value="<%=DataUtility.getTimestamp(bean.getModifiedDatetime())%>">
            </div>

            <div class="row">
                <label for="login">LoginId <span class="required">*</span> :</label>
                <input type="text" id="login" name="login" size="30" placeholder="Enter valid Email-Id" value="<%=DataUtility.getStringData(bean.getLogin())%>">
                <span class="error-message"><%=ServletUtility.getErrorMessage("login", request)%></span>
            </div>

            <div class="row">
                <label for="password">Password <span class="required">*</span> :</label>
                <input type="password" id="password" name="password" size="30" placeholder="Enter Password" value="<%=DataUtility.getStringData(bean.getPassword())%>">
                <span class="error-message"><%=ServletUtility.getErrorMessage("password", request)%></span>
            </div>

            <div class="button-container">
                <input type="submit" name="operation" value="<%=LoginCtl.OP_SIGN_IN%>">
                <input type="submit" name="operation" value="<%=LoginCtl.OP_SIGN_UP%>">
            </div>

            <div>
                <a href="<%=ORSView.FORGET_PASSWORD_CTL%>">Forget my password</a>
            </div>
        </div>
    </form>
    <%@ include file="Footer.jsp"%>


</body>
</html>