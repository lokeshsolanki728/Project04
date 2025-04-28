--- a/ORS_Project4/src/main/webapp/jsp/ForgetPasswordView.jsp
+++ b/ORS_Project4/src/main/webapp/jsp/ForgetPasswordView.jsp

<%@ page import="com.rays.pro4.controller.ForgetPasswordCtl" %>
<%@ page import="com.rays.pro4.Util.ServletUtility" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>

<html>
<head>
    <link rel="icon" type="image/png" href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16*16"/>
    <title>Forget Password</title>
    <link rel="stylesheet" href="<%=ORSView.APP_CONTEXT%>/css/style.css">
</head>
<body>
    <%@ include file="Header.jsp" %>
    <form action="<%=ORSView.FORGET_PASSWORD_CTL%>" method="post">
        <div class="container">
            <h1 class="text-center">Forgot your password ?</h1>
            <p class="text-center">Submit your Email address and we'll send your password.</p>
            <div class="message">
                <span class="success-message">
                    <%=ServletUtility.getSuccessMessage(request)%>
                </span>
                <span class="error-message">
                    <%=ServletUtility.getErrorMessage(request)%>
                </span>
            </div>
            <div class="input-container">
                <input type="hidden" name="id">
            </div>
            <div class="input-container">
                <label for="login">Email Id <span class="required">*</span></label>
                <input type="text" id="login" name="login" placeholder="Enter the Valid-Email Id"
                       value="<%=ServletUtility.getParameter("login", request)%>">
                <span class="error-message">
                    <%=ServletUtility.getErrorMessage("login", request)%>
                </span>
            </div>
            <div class="button-container">
                <input type="submit" name="operation" value="<%=ForgetPasswordCtl.OP_GO%>">
                <input type="submit" name="operation" value="<%=ForgetPasswordCtl.OP_RESET%>">
            </div>
        </div>
    </form>
    <%@ include file="Footer.jsp" %>
</body>
</html>
