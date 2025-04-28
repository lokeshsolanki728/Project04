--- a/ORS_Project4/src/main/webapp/jsp/ErrorView.jsp
+++ b/ORS_Project4/src/main/webapp/jsp/ErrorView.jsp

<%@page import="com.rays.pro4.controller.ORSView"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" isErrorPage="true"%>
<html>
<head>
<link rel="icon" type="image/png" href="<%=ORSView.APP_CONTEXT%>/img/customLogo.png" sizes="16*16" />
<title>Error Page</title>
<link rel="stylesheet" href="<%=ORSView.APP_CONTEXT%>/css/style.css">
</head>
<body>
	<div class="error-container">
		<img src="<%=ORSView.APP_CONTEXT%>/img/Error.jpg" class="error-image" alt="Error Image">
		<div class="error-header">
			<h1 class="error-message">Ooops! Something went wrong..</h1>
		</div>
		<div class="error-code">
			<span><b>500</b> : Requested resources is not available</span>
		</div>
		<div class="error-suggestions">
			<div>
				<h3>Try :</h3>
			</div>
			<div>
				<ul>
					<li>Check the network cables, modem, and router</li>
					<li>Reconnect to network or wi-fi</li>
				</ul>
			</div>
		</div>
		<div class="error-link-container">
			<a href="<%=ORSView.WELCOME_CTL%>" class="error-link">*Click here to Go Back*</a>
		</div>
	</div>
</body>
</html>

