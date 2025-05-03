<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.rays.pro4.controller.ORSView" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<link rel="stylesheet" href="${ctx}/css/style.css"/>

<div class="footer-container">
    <hr/>
    <h3 class="text-center footer-text">
        <fmt:setBundle basename="com.rays.pro4.bundle.system" var="sys"/>
        &copy; 2024 <fmt:message key="COPYRIGHT_MESSAGE" bundle="${sys}"/>
    </h3>
</div>