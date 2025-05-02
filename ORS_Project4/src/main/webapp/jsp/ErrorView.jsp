--- a/ORS_Project4/src/main/webapp/jsp/ErrorView.jsp
+++ b/ORS_Project4/src/main/webapp/jsp/ErrorView.jsp

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="com.rays.pro4.controller.ORSView" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isErrorPage="true"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
<link rel="icon" type="image/png"
	href="${ctx}/img/customLogo.png"
	sizes="16*16" />
<title>Error</title>
<link rel="stylesheet" href="${ctx}/css/style.css">
</head>
<body>
	<div class="errorContainer">
		<img src="${ctx}/img/Error.jpg"
			class="errorImage" alt="Error Image">
		<div class="errorHeader">
			<h1 class="errorMessage">Ooops! Something went wrong..</h1>
		</div>
		<div class="errorCode">
			<span><b>500</b> : Requested resources is not available</span>
		</div>
		<div class="errorSuggestions">
			<div>
				<h3>Try :</h3>
			</div>
			<div>
				<ul class="errorList">
					<li class="errorListItem">Check the network cables, modem,
						and router</li>
					<li class="errorListItem">Reconnect to network or wi-fi</li>
				</ul>
			</div>
		</div>
		<div class="errorLinkContainer">
			<a href="${ctx}${ORSView.WELCOME_CTL}" class="errorLink">*Click here to Go Back*</a>
		</div>
	</div>
</body>
