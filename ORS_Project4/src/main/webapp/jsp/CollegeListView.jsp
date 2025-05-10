<%@page import="com.rays.pro4.controller.CollegeListCtl"%>
<%@page import="com.rays.pro4.controller.ORSView"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
    <head>
        <c:set var="ctx" value="${pageContext.request.contextPath}" />
        <link rel="icon" type="image/png" href="${ctx}/img/logo.png" sizes="16*16" />
        <title>
            <c:choose>
                <c:when test="${not empty list}">College List</c:when>
                <c:otherwise>College List</c:otherwise>
            </c:choose>
        </title>
        <link rel="stylesheet" href="${ctx}/css/style.css">
        <link rel="stylesheet" href="${ctx}/resources/demos/style.css">
        <script src="${ctx}/js/jquery.min.js"></script>
        <script src="${ctx}/js/Checkbox11.js"></script>
    </head>
    <body>
        <form action="${ctx}${ORSView.COLLEGE_LIST_CTL}" method="POST">
            <%@ include file="Header.jsp"%>
            <c:set var="nextlist" value="${requestScope.nextlist}" />
            <c:set var="collegeList" value="${requestScope.CollegeList}" />
            <c:set var="list" value="${requestScope.list}" />
            <c:set var="pageNo" value="${requestScope.pageNo}" />
            <c:set var="pageSize" value="${requestScope.pageSize}" />
            <c:set var="index" value="${(pageNo-1) * pageSize + 1}" />
            <c:set var="orderBy" value="${requestScope.orderBy}" />
            <c:set var="sortOrder" value="${requestScope.sortOrder}" />

            <div class="container">
                <h1 class="text-center">College List</h1>
                <div class="message-container">
                    <c:if test="${not empty requestScope.errorMessage}">
                        <div class="alert alert-danger" role="alert">${requestScope.errorMessage}</div>
                    </c:if>
                    <c:if test="${not empty requestScope.successMessage}">
                        <div class="alert alert-success" role="alert">${requestScope.successMessage}</div>
                    </c:if>
                </div>
                <c:if test="${not empty list}">
                    <table class="search-table w-100">
                        <tr>
                            <td class="text-center">
                                <label for="collegename">College Name :</label>
                                <select name="collegename" class="form-control-inline">
                                    <option value="">Select Name</option>
                                    <c:forEach items="${collegeList}" var="col" >
                                        <option value="${col.id}" ${col.id == requestScope.dto.id ? 'selected' : ''}>
                                            ${col.name}
                                        </option>
                                    </c:forEach>
                                </select>
                                <label for="city">City :</label>
                                <input type="text" id="city" name="city" placeholder="Enter City Name" class="form-control-inline"
                                       value="${param.city}">
 <input type="hidden" name="orderBy" value="${orderBy}">
 <input type="hidden" name="sortOrder" value="${sortOrder}">
 <input type="submit" name="operation" class="btn btn-primary ms-2" value="<c:out value="${CollegeListCtl.OP_SEARCH}" />">
                                <input type="submit" name="operation" class="btn btn-secondary ms-2" value="<c:out value="${CollegeListCtl.OP_RESET}" />">
                            </td>
                        </tr>
                    </table>
                    <table class="table table-bordered table-striped w-100">
                        <thead>
                            <tr>
                                <th><input type="checkbox" id="select_all" name="select">Select</th>
                                <th >S.No.</th>
                                <th>
                                    <a href="${ctx}${ORSView.COLLEGE_LIST_CTL}?orderBy=name&sortOrder=${sortOrder eq 'asc' ? 'desc' : 'asc'}&pageNo=${pageNo}&pageSize=${pageSize}&city=${param.city}">Name
                                        <c:if test="${orderBy eq 'name'}">
                                            <c:choose>
                                                <c:when test="${sortOrder eq 'asc'}">
                                                    <i class="fas fa-arrow-up"></i>
                                                </c:when>
                                                <c:otherwise>
                                                    <i class="fas fa-arrow-down"></i>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:if>
                                    </a>
                                </th>
                                <th >Address</th>
                                <th >State</th>
                                <th>City</th>
                                <th>PhoneNo</th>
                                <th>Edit</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${list}" var="bean" varStatus="loop">
                                <tr class="text-center">
                                    <td class="table-row">
                                        <input type="checkbox" class="checkbox" name="ids" value="${bean.id}">
                                    </td>
                                    <td class="table-row">${index+loop.index}</td>
                                    <td class="table-row"><c:out value="${bean.name}" /></td>
                                    <td class="table-row"><c:out value="${bean.address}" /></td>
                                    <td class="table-row"><c:out value="${bean.state}" /></td>
                                    <td class="table-row"><c:out value="${bean.city}" /></td>
                                    <td class="table-row"><c:out value="${bean.phoneNo}" /></td>
                                    <td class="table-row">
                                        <a href="CollegeCtl?id=${bean.id}" class="btn btn-link">Edit</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    <table class="w-100">
                        <tr >
                             <td class="float-start">
                                <input type="submit" name="operation" class="btn btn-secondary " value="<c:out value="${CollegeListCtl.OP_PREVIOUS}" />"
                                       ${pageNo == 1 ? 'disabled' : ''}>
                            </td>
                            <td class="float-center">
                                <input type="submit" name="operation" class="btn btn-danger ms-2" value="<c:out value="${CollegeListCtl.OP_DELETE}" />">
                            </td>
                            <td class="float-center">
                                <input type="submit" name="operation" class="btn btn-success ms-2" value="<c:out value="${CollegeListCtl.OP_NEW}" />">
                            </td>
                            <td class="float-end">
                                <input type="submit" class="btn btn-primary ms-2" name="operation" value="<c:out value="${CollegeListCtl.OP_NEXT}" />"
                                       ${list.size() < pageSize || nextlist == 0 ? 'disabled' : ''}>
                            </td>
                        </tr>
                    </table>
                </c:if>
                <c:if test="${empty list}">
                    <div class="text-center">
                        <h3>No Result Found</h3>
                    </div>
                </c:if>
                <input type="hidden" name="pageNo" value="${pageNo}">
                <input type="hidden" name="pageSize" value="${pageSize}">
            </div>
        </form>
        <%@ include file="Footer.jsp"%>
    </body>
</html>
