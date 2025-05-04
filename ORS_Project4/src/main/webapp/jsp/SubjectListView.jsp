<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.rays.pro4.controller.ORSView" %>
<%@ page import="com.rays.pro4.controller.SubjectListCtl" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" %>
<html>

<head>
    <c:set var="ctx" value="${pageContext.request.contextPath}" />
    <title>Subject List</title>
    <link rel="icon" type="image/png" href="${ctx}/img/logo.png" sizes="16x16" />
    <link rel="stylesheet" href="${ctx}/css/style.css">
    <script src="${ctx}/js/jquery.min.js"></script>
    <script src="${ctx}/js/Checkbox11.js"></script>
</head>

<body>
    <%@include file="Header.jsp"%>
    <jsp:useBean id="bean" class="com.rays.pro4.Bean.SubjectBean" scope="request"></jsp:useBean>
    <form action="${ctx}${ORSView.SUBJECT_LIST_VIEW}" method="post">
        <div class="container">
            <h1 class="text-center">Subject List</h1>
            <c:if test="${not empty requestScope.errorMessage}">
                <div class="alert alert-danger">${requestScope.errorMessage}</div>
            </c:if>
            <c:if test="${not empty requestScope.successMessage}">
                <div class="alert alert-success">${requestScope.successMessage}</div>
            </c:if>
            <c:set var="pageNo" value="${requestScope.pageNo}" />
            <c:set var="pageSize" value="${requestScope.pageSize}" />
            <c:set var="index" value="${(pageNo - 1) * pageSize + 1}" />
            <c:set var="nextPageSize" value="${requestScope.nextListSize}" />
            <c:if test="${not empty requestScope.courseList}">
                <c:set var="clist" value="${requestScope.courseList}" />
            </c:if>
            <c:if test="${not empty requestScope.subjectList}">
                <c:set var="slist" value="${requestScope.subjectList}" />
            </c:if>
            <c:if test="${not empty requestScope.list}">
                <c:set var="list" value="${requestScope.list}" />
            </c:if>
            <c:if test="${not empty list}">
                <input type="hidden" name="pageNo" value="${pageNo}">
                <input type="hidden" name="pageSize" value="${pageSize}">
                <table class="search-table w-100">
                    <tr>
                        <td class="text-center">
                        <label for="subjectId">Subject Name :</label>
                            <select name="subjectId" id="subjectId" class="form-control-inline">
                                <option value="">Select Subject</option>
                                <c:forEach items="${slist}" var="s">
                                    <option value="${s.id}" ${s.id == bean.id ? 'selected' : ''}>${s.subjectName}</option>
                                </c:forEach>
                            </select>
                            <label for="courseId">Course Name :</label>
                            <select name="courseId" id="courseId" class="form-control-inline">
                                <option value="">Select Course</option>
                                <c:forEach items="${clist}" var="c">
                                    <option value="${c.id}" ${c.id == bean.courseId ? 'selected' : ''}>${c.courseName}</option>
                                </c:forEach>
                            </select>
                            <input type="submit" name="operation" class="btn btn-primary"
                                value="<%=SubjectListCtl.OP_SEARCH%>">
                            <input type="submit" name="operation" class="btn btn-secondary"
                                value="<%=SubjectListCtl.OP_RESET%>">
                        </td>
                    </tr>
                </table>
                <table class="table table-bordered table-striped w-100">
                    <thead class="table-header">
                        <tr>
                            <th><input type="checkbox" id="select_all" name="select"> Select All.</th>
                            <th>S.No</th>
                            <th>Subject Name</th>
                            <th>Description</th>
                            <th>Course Name</th>
                            <th>Edit</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${list}" var="subject">
                            <tr class="text-center table-row">
                                <td><input type="checkbox" class="checkbox" name="ids" value="${subject.id}"></td>
                                <td>${index}
                                    <c:set var="index" value="${index + 1}" />
                                </td>
                                <td style="text-transform: capitalize;">${subject.subjectName}</td>
                                <td style="text-transform: capitalize;">${subject.description}</td>
                                <td style="text-transform: capitalize;">${subject.courseName}</td>
                                <td><a href="${ctx}${ORSView.SUBJECT_CTL}?id=${subject.id}" class="btn btn-link">Edit</a></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                <table class="w-100">
                    <tr>
                        <td><input type="submit" name="operation" class="btn btn-secondary"
                                value="<%=SubjectListCtl.OP_PREVIOUS%>" ${pageNo > 1 ? '' : 'disabled'}></td>
                        <td><input type="submit" name="operation" class="btn btn-success"
                                value="<%=SubjectListCtl.OP_NEW%>"></td>
                        <td><input type="submit" name="operation" class="btn btn-danger"
                                value="<%=SubjectListCtl.OP_DELETE%>"></td>
                        <td class="text-right"><input type="submit" name="operation" class="btn btn-primary"
                                value="<%=SubjectListCtl.OP_NEXT%>" ${nextPageSize != 0 ? '' : 'disabled'}></td>
                    </tr>
                </table>
            </c:if>
            <c:if test="${empty list}">
                <div class="text-center">
                    <input type="submit" name="operation" class="btn btn-primary"
                        value="<%=SubjectListCtl.OP_BACK%>">
                </div>
            </c:if>
        </div>
    </form>
    <%@include file="Footer.jsp"%>
</body>

</html>