<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.rays.pro4.controller.ORSView"%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">

<div class="footer">
	<hr />
	<h3 class="text-center">
		<fmt:setBundle basename="com.rays.pro4.bundle.system" var="sys" />
		<fmt:message key="COPYRIGHT_MESSAGE" bundle="${sys}" />
	</h3>
</div>