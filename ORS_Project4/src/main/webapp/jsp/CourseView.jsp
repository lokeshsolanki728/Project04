--- a/ORS_Project4/src/main/webapp/jsp/CourseView.jsp
+++ b/ORS_Project4/src/main/webapp/jsp/CourseView.jsp
@@
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@page import="java.util.LinkedHashMap" %>
<%@page import="com.rays.pro4.Util.HTMLUtility" %>
<%@page import="com.rays.pro4.controller.ORSView" %>
<%@page import="com.rays.pro4.controller.CourseCtl" %>
<%@page import="com.rays.pro4.Util.DataUtility" %>
<%@page import="com.rays.pro4.Util.ServletUtility" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<html>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
    <head>
        <link rel="icon" type="image/png" href="${ctx}/img/logo.png" sizes="16*16" />
        <title>
            <c:choose>
                <c:when test="${not empty bean.id}">
                    Update Course
                </c:when>
                <c:otherwise>
                    Add Course
                </c:otherwise>
            </c:choose>
        </title>
        <link rel="stylesheet" href="${ctx}/css/style.css" />
    </head>
    <body>
    <jsp:useBean id="bean" class="com.rays.pro4.Bean.CourseBean" scope="request"></jsp:useBean>
    <form action="${ctx}${ORSView.COURSE_CTL}" method="post">
        <%@ include file="Header.jsp" %>
        <div class="container">
            <h1 class="text-center">
                <c:choose>
                    <c:when test="${not empty bean.id}">Update Course</c:when>
                    <c:otherwise>Add Course</c:otherwise>
                </c:choose>
            </h1>
            <div class="message-container">
                <c:if test="${not empty requestScope.success}">
                    <div class="alert alert-success" role="alert">${requestScope.success}</div>
                </c:if>
                <c:if test="${not empty requestScope.error}">
                    <div class="alert alert-danger" role="alert">${requestScope.error}</div>
                </c:if>
            </div>
            <input type="hidden" name="id" value="${bean.id}">
            <input type="hidden" name="createdBy" value="${bean.createdBy}"/>
            <input type="hidden" name="modifiedBy" value="${bean.modifiedBy}"/>
            <input type="hidden" name="createdDatetime" value="${bean.createdDatetime}"/>
            <input type="hidden" name="modifiedDatetime" value="${bean.modifiedDatetime}"/>
            <table class="table table-borderless w-50 mx-auto">
                <tr>
                    <th class="text-left">
                        <label for="name">Course Name<span class="required">*</span></label>
                    </th>
                    <td>
                        <input type="text" id="name" name="name" placeholder="Enter Course Name" class="form-control"
                               value="${bean.name}">
                        <div class="error-message">${requestScope.name}</div>
                    </td>
                </tr>
                <tr>
                    <th class="text-left">
                        <label for="duration">Duration <span class="required">*</span></label>
                    </th>
                    <td>
                        <select id="duration" name="duration" class="form-control">
                            <option value="">Select Duration</option>
                            <option value="6 Year" ${bean.duration == '6 Year' ? 'selected' : ''}>
                                6 Year
                            </option>
                            <option value="5 Year" ${bean.duration == '5 Year' ? 'selected' : ''}>
                                5 Year
                            </option>
                            <option value="4 Year" ${bean.duration == '4 Year' ? 'selected' : ''}>
                                4 Year
                            </option>
                            <option value="3 Year" ${bean.duration == '3 Year' ? 'selected' : ''}>
                                3 Year
                            </option>
                            <option value="2 Year" ${bean.duration == '2 Year' ? 'selected' : ''}>
                                2 Year
                            </option>
                            <option value="1 Year" ${bean.duration == '1 Year' ? 'selected' : ''}>
                                1 Year
                            </option>
                        </select>
                        <div class="error-message">${requestScope.duration}</div>
                    </td>
                </tr>
                <tr>
                    <th class="text-left">
                        <label for="description">Description <span class="required">*</span></label>
                    </th>
                    <td>
                        <input type="text" id="description" name="description" placeholder="Enter Description"
                               class="form-control" value="${bean.description}">
                        <div class="error-message">${requestScope.description}</div>
                    </td>
                </tr>
                <tr>
                    <th></th>
                    <td>
                         <div class="button-container">
                            <input type="submit" name="operation" class="btn btn-primary"
                                   value="${empty bean.id ? CourseCtl.OP_SAVE : CourseCtl.OP_UPDATE}">
                            <input type="submit" name="operation" class="btn btn-secondary"
                                   value="${empty bean.id ? CourseCtl.OP_RESET : CourseCtl.OP_CANCEL}">
                            <c:if test="${not empty bean.id}">
                                <input type="submit" name="operation" class="btn btn-danger" value="${CourseCtl.OP_DELETE}">
                            </c:if>
                        </div>
                    </td>
                </tr>
            </table>
        </div>
    </form>
         <%@ include file="Footer.jsp" %>
     </body>
</html>
