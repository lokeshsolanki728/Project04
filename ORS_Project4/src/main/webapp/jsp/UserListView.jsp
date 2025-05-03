<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@page import="com.rays.pro4.Model.RoleModel"%>

<%@page import="com.rays.pro4.Bean.UserBean"%>
<%@page import="com.rays.pro4.Util.HTMLUtility"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.rays.pro4.controller.ORSView"%>
<%@page import="com.rays.pro4.controller.UserListCtl"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<html>
<head>
    <link rel="icon" type="image/png"
          href="${pageContext.request.contextPath}/img/logo.png" sizes="16*16"/>
    <title>User List View</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/Checkbox11.js"></script>
</head>
<body>
	<jsp:useBean id="bean" class="com.rays.pro4.Bean.UserBean" scope="request"></jsp:useBean>
	<%@include file="Header.jsp"%>
	<form action="${pageContext.request.contextPath}${ORSView.USER_LIST_CTL}" method="post">
	<div class="container">
			<h1 class="text-center">User List</h1>
            <div class="message-container">
                <c:if test="${not empty requestScope.error}">
                    <div class="alert alert-danger">${requestScope.error}</div>
                </c:if>
                <c:if test="${not empty requestScope.success}">
                    <div class="alert alert-success">${requestScope.success}</div>
                </c:if>
            </div>
            <c:set var="rlist" value="${requestScope.RoleList}"/>
            <c:set var="next" value="${requestScope.nextlist}"/>
            <c:set var="pageNo" value="${requestScope.pageNo}"/>
            <c:set var="pageSize" value="${requestScope.pageSize}"/>
            <c:set var="index" value="${(pageNo - 1) * pageSize + 1}"/>
            <c:set var="list" value="${requestScope.list}"/>
            <c:if test="${not empty list}">
                <table class="search-table w-100">
                    <tr>
                        <td class="text-center">
                            <label for="firstName">FirstName : </label>
                            <input type="text" id="firstName" name="firstName" class="form-control-inline"
                                   placeholder="Enter First Name" value="${param.firstName}">
                            <label for="loginid">LoginId :</label>
                            <input type="text" id="loginid" name="loginid" class="form-control-inline"
                                   placeholder="Enter Login-Id" value="${param.loginid}">
                            <label for="roleid">Role : </label>
                            <select id="roleid" name="roleid" class="form-control-inline">
                                <option value="">Select Role</option>
                                <c:forEach var="item" items="${rlist}">
                                    <option value="${item.key}" ${item.key == param.roleid ? 'selected' : ''}>${item.value}</option>
                                </c:forEach>
                            </select>
                            <input type="submit" name="operation" class="btn btn-primary"
                                   value="<%=UserListCtl.OP_SEARCH%>">
                            <input type="submit" name="operation" class="btn btn-secondary"
                                   value="<%=UserListCtl.OP_RESET%>">
                        </td>
                    </tr>
                </table>
                <table class="table table-bordered table-striped w-100">
                    <thead class="table-header">
                    <tr>
                        <th><input type="checkbox" id="select_all" name="select">Select
                            All</th>
                        <th>S.No.</th>
                        <th>FirstName</th>
                        <th>LastName</th>
                        <th>Role</th>
                        <th>LoginId</th>
                        <th>Gender</th>
                        <th>Date Of Birth</th>
                        <th>Mobile No</th>
                        <th>Edit</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="user" items="${list}" varStatus="loop">
                        <c:set var="roleId" value="${user.roleId}"/>
                        <c:set var="roleModel" value="${new com.rays.pro4.Model.RoleModel()}"/>
                        <c:set var="rolebean" value="${roleModel.findByPK(roleId)}"/>
                        <tr align="center" class="table-row">
                            <td>
                                <c:choose>
                                    <c:when test="${sessionScope.userBean.id == user.id || user.roleId == 1}">
                                        <input type="checkbox" class="checkbox" name="ids" value="${user.id}" disabled>
                                    </c:when>
                                    <c:otherwise>
                                        <input type="checkbox" class="checkbox" name="ids" value="${user.id}">
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>${index} <c:set var="index" value="${index + 1}"/></td>
                            <td>${user.firstName}</td>
                            <td>${user.lastName}</td>
                            <td>${rolebean.name}</td>
                            <td>${user.login}</td>
                            <td>${user.gender}</td>
                            <td>${user.dob}</td>
                            <td>${user.mobileNo}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${sessionScope.userBean.id == user.id || user.roleId == 1}">
                                        <a class="btn btn-link" href="#" onclick="return false;">Edit</a>
                                    </c:when>
                                    <c:otherwise>
                                        <a class="btn btn-link" href="${pageContext.request.contextPath}/UserCtl?id=${user.id}">Edit</a>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
                <table class="w-100">
                    <tr>
                        <td><input type="submit" name="operation" class="btn btn-secondary" value="<%=UserListCtl.OP_PREVIOUS%>" ${pageNo == 1 ? 'disabled' : ''}></td>
                        <td><input type="submit" name="operation" class="btn btn-danger" value="<%=UserListCtl.OP_DELETE%>"></td>
                        <td><input type="submit" name="operation" class="btn btn-success" value="<%=UserListCtl.OP_NEW%>"></td>
                        <td class="text-right"><input type="submit" name="operation" class="btn btn-primary" value="<%=UserListCtl.OP_NEXT%>" ${list.size() < pageSize || next == 0 ? 'disabled' : ''}></td>
                    </tr>
                </table>
            </c:if>
            <c:if test="${empty list}">
                <div class="text-center"><input type="submit" name="operation" class="btn btn-primary" value="<%=UserListCtl.OP_BACK%>"></div>
            </c:if>
            <input type="hidden" name="pageNo" value="${pageNo}">
            <input type="hidden" name="pageSize" value="${pageSize}">
        </div>
    
	</form>
	<%@include file="Footer.jsp"%>
</body>
</html>
