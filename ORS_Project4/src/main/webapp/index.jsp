<%@page import="com.rays.pro4.DTO.UserDTO"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="com.rays.pro4.Util.ServletUtility"%> 
<%@page import="com.rays.pro4.controller.ORSView"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">

<title>Online Result System</title>
<link rel="icon" type="image/png"
	href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16x16" />

</head>

<body>

	<br>
	<br>
	<%
		

		UserDTO userDto = (UserDTO) session.getAttribute("user");

		if (userDto != null) {
			ServletUtility.forward(ORSView.WELCOME_VIEW, request, response);

		}
	%>
	<!-- <marquee behavior="alternate" scrollamount="300" loop="1"> -->
	<div align="center">
		<img src="img/customLogo.jpg" align="middle" width="315" height="127"
			border="0">
	</div>
	<!-- </marquee> -->

	<br>
	<br>

	<h1 align="center">
		<font size="10px" color="red"> <a
			href="<%=ORSView.WELCOME_CTL%>">Online Result System</a></font>
			
	</h1> 
</body>
</html>