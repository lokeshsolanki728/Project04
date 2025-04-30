<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="com.rays.pro4.controller.CourseListCtl" %>
<%@page import="com.rays.pro4.Util.DataUtility" %>
<%@page import="com.rays.pro4.Util.ServletUtility" %>
<%@page import="com.rays.pro4.controller.ORSView" %>
<%@page import="java.util.List" %>
<%@page import="java.util.Iterator" %>
<%@page import="com.rays.pro4.Bean.CourseBean" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<html lang="en">
<head>
    <link rel="icon" type="image/png" href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16*16" />
    <title>Course List</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>

<body>

<jsp:useBean id="bean" class="com.rays.pro4.Bean.CourseBean" scope="request"></jsp:useBean>
<form action="${pageContext.request.contextPath}${ORSView.COURSE_LIST_CTL}" method="post">
    <%@include file="Header.jsp"%>

    <div class="list-container">
        <h1 class="text-center">Course List</h1>
        <div class="text-center">
            <span class="error-message">${error}</span>
            <span class="success-message">${success}</span>
        </div>

        <c:set var="list" value="${requestScope.CourseList}" />
        <c:set var="nextPage" value="${requestScope.nextlist}" />
        <c:set var="pageNo" value="${requestScope.pageNo}" />
        <c:set var="pageSize" value="${requestScope.pageSize}" />
        <c:set var="index" value="${(pageNo - 1) * pageSize + 1}" />

        <c:if test="${not empty list}">
            <div class="search-options">
               <input type="submit" name="operation" value="<%=CourseListCtl.OP_SEARCH%>" class="btn btn-primary">
                <input type="submit" name="operation" value="<%=CourseListCtl.OP_RESET%>" class="btn btn-secondary">
            </div>
            <table class="table">
                <thead>
                <tr>
                    <th><input type="checkbox" id="select_all" name="select">Select All</th>
                    <th>S.NO.</th>
                    <th>Course Name</th>
                    <th>Duration</th>
                    <th>Description</th>
                    <th>Edit</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="course" items="${list}" varStatus="loop">
                    <tr class="text-center">
                        <td><input type="checkbox" class="checkbox" name="ids" value="${course.id}"></td>
                        <td>${index + loop.index}</td>
                        <td>${course.name}</td>
                        <td>${course.duration}</td>
                        <td>${course.description}</td>
                        <td><a href="${pageContext.request.contextPath}/CourseCtl?id=${course.id}">Edit</a></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <div class="pagination">
                <input type="submit" name="operation" value="<%=CourseListCtl.OP_PREVIOUS%>" class="btn btn-secondary"
                       <c:if test="${pageNo == 1}">disabled</c:if>>
                <input type="submit" name="operation" value="<%=CourseListCtl.OP_DELETE%>" class="btn btn-danger">
                <input type="submit" name="operation" value="<%=CourseListCtl.OP_NEW%>" class="btn btn-success">
                <input type="submit" name="operation" value="<%=CourseListCtl.OP_NEXT%>" class="btn btn-secondary"
                       <c:if test="${list.size() < pageSize || nextPage == 0}">disabled</c:if>>
            </div>
        </c:if>
        <c:if test="${empty list}">
            <div class="text-center">
                <input type="submit" name="operation" value="<%=CourseListCtl.OP_BACK%>" class="btn btn-primary">
            </div>
        </c:if>

        <input type="hidden" name="pageNo" value="${pageNo}">
        <input type="hidden" name="pageSize" value="${pageSize}">
    </div>
</form>
<script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/Checkbox11.js"></script>
<%@include file="Footer.jsp"%>
</body>
</html>
