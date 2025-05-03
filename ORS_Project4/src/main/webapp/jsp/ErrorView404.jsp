--- a/ORS_Project4/src/main/webapp/jsp/ErrorView404.jsp
+++ b/ORS_Project4/src/main/webapp/jsp/ErrorView404.jsp
@@
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" isErrorPage="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="com.rays.pro4.controller.ORSView"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<link rel="icon" type="image/png" href="${ctx}/img/logo.png"
	sizes="16*16" />
<title>Error 404</title>
<link rel="stylesheet" href="${ctx}/css/style.css">
</head>
<body>
	<div class="error-page-container">
		<img src="${ctx}/img/error_404.jpg" class="error-page-image"
			alt="Error Image">
		<div class="error-page-header">
			<h1 class="error-page-message">Oops! Something went
				wrong...</h1>
		</div>
		<div class="error-page-code">
			<span><b>404</b> : Requested Page not available</span>
		</div>
		<div class="error-page-suggestions">
			<div>
				<h3>Try:</h3>
			</div>
			<div>
				<ul class="error-page-list">
					<li class="error-page-list-item">Server under Maintain
						please try after Some Time</li>
					<li class="error-page-list-item">Check the network
						cables, modem, and router</li>
					<li class="error-page-list-item">Reconnect to network or
						Wi-Fi</li>
				</ul>
			</div>
		</div>
		<div class="error-page-link-container">
			<a href="${ctx}${ORSView.WELCOME_CTL}"
				class="btn btn-primary error-page-link">Go to Home Page</a>
		</div>
	</div>
</body>
</html>