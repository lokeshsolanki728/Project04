<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.rays.pro4.Bean.RoleBean"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="com.rays.pro4.controller.ORSView"%>
<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <meta charset="ISO-8859-1">
    <title>Welcome Page</title>
    <style>
        .welcome-container {
            text-align: center;
        }
        .welcome-heading {
            font-size: 36px;
            color: red;
        }
        .student-marksheet-link {
            font-size: 24px;
            margin-top: 20px;
        }
    </style>
</head>
<body>
    <form action="${pageContext.request.contextPath}${ORSView.WELCOME_CTL}">
        <%@ include file="Header.jsp"%>
        <div class="welcome-container">
            <h1 class="welcome-heading">
                Welcome to ORS
            </h1>

            <c:if test="${not empty sessionScope.user}">
                <c:if test="${sessionScope.user.roleId == RoleBean.STUDENT}">
                    <h2 class="student-marksheet-link">
                        <a href="${pageContext.request.contextPath}${ORSView.GET_MARKSHEET_CTL}">Click here to see your Marksheet</a>
                    </h2>
                </c:if>
            </c:if>
        </div>
    </form>

    <%@ include file="Footer.jsp"%>
</body>
</html>