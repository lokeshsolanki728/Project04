<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="com.rays.pro4.controller.ORSView" %>
<%@page import="com.rays.pro4.controller.RoleListCtl" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<html>
<head>
    <c:set var="ctx" value="${pageContext.request.contextPath}" />
    <link rel="icon" type="image/png" href="${ctx}/img/logo.png" sizes="16*16" />
    <title>Role List</title>
    <link rel="stylesheet" href="${ctx}/css/style.css">
    <script src="${ctx}/js/jquery.min.js"></script>
    <script src="${ctx}/js/Checkbox11.js"></script>
</head>
<body>
    <jsp:useBean id="bean" class="com.rays.pro4.Bean.RoleBean" scope="request" />
    <%@include file="Header.jsp" %>
    <form action="${ctx}/RoleListCtl" method="post">
        <div class="container">
            <h1 class="text-center">Role List</h1>
            <div class="message-container">
                <c:if test="${not empty requestScope.error}">
                    <div class="alert alert-danger" role="alert">${requestScope.error}</div>
                </c:if>
                <c:if test="${not empty requestScope.success}">
                    <div class="alert alert-success" role="alert">${requestScope.success}</div>
                </c:if>
            </div>

            <c:set var="rlist" value="${requestScope.RoleList}" />
            <c:set var="next" value="${requestScope.nextlist}" />
            <c:set var="pageNo" value="${requestScope.pageNo}" />
            <c:set var="pageSize" value="${requestScope.pageSize}" />
            <c:set var="index" value="${(pageNo - 1) * pageSize + 1}" />
            <c:set var="list" value="${requestScope.list}" />

            <c:if test="${not empty list}">
                <table class="search-table w-100">
                    <tr>
                        <td class="text-center">
                            <label for="roleid">Role :</label>
                            <select name="roleid" id="roleid" class="form-control-inline">
                                <option value="">Select Role</option>
                                <c:forEach items="${rlist}" var="role">
                                    <option value="${role.id}" ${bean.id == role.id ? 'selected' : ''}>${role.name}</option>
                                </c:forEach>
                            </select>
                            <input type="submit" name="operation" class="btn btn-primary" value="<%=RoleListCtl.OP_SEARCH%>">
                            <input type="submit" name="operation" class="btn btn-secondary" value="<%=RoleListCtl.OP_RESET%>">
                        </td>
                    </tr>
                </table>
                <table class="table table-bordered table-striped w-100">
                    <thead class="table-header">
                        <tr>
                            <th><input type="checkbox" id="select_all" name="select">Select All</th>
                            <th>S.No.</th>
                            <th>Role</th>
                            <th>Description</th>
                            <th>Edit</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="bean2" items="${list}">
                            <tr class="text-center">
                                <td><input type="checkbox" class="checkbox" name="ids" value="${bean2.id}" <c:if test="${userBean.id == bean2.id || bean2.id == 1}">disabled</c:if>/></td>
                                <td>${index} <c:set var="index" value="${index + 1}" /></td>
                                <td>${bean2.name}</td>
                                <td>${bean2.description}</td>
                                <td>
                                    <a href="${ctx}/RoleCtl?id=${bean2.id}" <c:if test="${userBean.id == bean2.id || bean2.id == 1}"> onclick="return false;"</c:if> class="btn btn-link">Edit</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                <table class="w-100">
                    <tr>
                        <td class="text-left"><input type="submit" name="operation"
                                                       class="btn btn-secondary" value="<%=RoleListCtl.OP_PREVIOUS%>" ${pageNo == 1 ? 'disabled' : ''}></td>
                        <td>
                            <input type="submit" name="operation" class="btn btn-danger" value="<%=RoleListCtl.OP_DELETE%>">
                        </td>
                        <td>
                            <input type="submit" name="operation" class="btn btn-success" value="<%=RoleListCtl.OP_NEW%>">
                        </td>
                        <td class="text-right">
                            <input type="submit" name="operation" class="btn btn-primary" value="<%=RoleListCtl.OP_NEXT%>" <c:if test="${list.size() < pageSize || next == 0}">disabled</c:if>>
                        </td>
                    </tr>
                </table>
            </c:if>
            <c:if test="${empty list}">
                <div class="text-center">
                    <input type="submit" name="operation" class="btn btn-primary" value="<%=RoleListCtl.OP_BACK%>">
                </div>
            </c:if>
            <input type="hidden" name="pageNo" value="${pageNo}"/>
            <input type="hidden" name="pageSize" value="${pageSize}"/>
        </div>
    </form>
    <%@include file="Footer.jsp" %>
</body>
</html>

