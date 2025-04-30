<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@page import="com.rays.pro4.Model.RoleModel"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="com.rays.pro4.controller.ORSView"%>
<%@page import="com.rays.pro4.Util.HTMLUtility"%>
<%@page import="com.rays.pro4.Util.DataUtility"%>

<%@page import="com.rays.pro4.controller.RoleListCtl"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<html>
<head>
<link rel="icon" type="image/png" href="${pageContext.request.contextPath}/img/logo.png" sizes="16*16" />
<title>Role List</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">

<script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/Checkbox11.js"></script>

</head>
<body>
	<jsp:useBean id="bean" class="com.rays.pro4.Bean.RoleBean" scope="request"></jsp:useBean>
	<%@include file="Header.jsp"%>
	<div align="center">

		<form action="${pageContext.request.contextPath}/RoleListCtl" method="post">

			<div align="center">
				<h1>Role List</h1>
				<c:if test="${not empty error}">
					<div class="error-message">${error}</div>
				</c:if>
				<c:if test="${not empty success}">
					<div class="success-message">${success}</div>
				</c:if>

			</div>
			<c:set var="rlist" value="${requestScope.RoleList}"></c:set>
			<c:set var="next" value="${requestScope.nextlist}"></c:set>
			<c:set var="pageNo" value="${param.pageNo}"></c:set>
			<c:set var="pageSize" value="${param.pageSize}"></c:set>
			<c:set var="index" value="${(pageNo - 1) * pageSize + 1}"></c:set>
			<c:set var="list" value="${requestScope.list}"></c:set>
			<c:if test="${not empty list}">

				<table width="100%" align="center">
					<tr>
						<td align="center"><label for="roleid">Role :</label> <select name="roleid" id="roleid">
							<option value="">Select Role</option>
								<c:forEach items="${rlist}" var="role">
                                   <option value="${role.id}" ${bean.id == role.id ? 'selected' : ''}>${role.name}</option>
                                   </c:forEach>
						</select> &nbsp; <input type="submit" name="operation" value="<%=RoleListCtl.OP_SEARCH%>"> &nbsp; <input type="submit" name="operation"
							value="<%=RoleListCtl.OP_RESET%>"></td>
					</tr>
				</table>
				<br>

				<table border="1" width="100%" align="center" cellpadding=7px cellspacing=".2">
					<tr style="background: skyblue">
						<th><input type="checkbox" id="select_all" name="select">Select All</th>

						<th>S.No.</th>
						<th>Role</th>
						<th>Description</th>
						<th>Edit</th>
					</tr>

					<c:forEach var="bean2" items="${list}">
						<tr align="center">
							<td><input type="checkbox" class="checkbox" name="ids" value="${bean2.id}" <c:if test="${userBean.id == bean2.id || bean2.id == 1}">disabled</c:if>></td>
							<td>${index}</td>
							<td>${bean2.name}</td>
							<td>${bean2.description}</td>
							<td><a href="RoleCtl?id=${bean2.id}" <c:if test="${userBean.id == bean2.id || bean2.id == 1}"> onclick = "return false;"</c:if>>Edit</a></td>
						</tr>
						<c:set var="index" value="${index + 1}"></c:set>
					</c:forEach>
				</table>

				<table width="100%">
					<tr>
						<c:choose>
							<c:when test="${pageNo == 1}">
								<td><input type="submit" name="operation" disabled="disabled" value="<%=RoleListCtl.OP_PREVIOUS%>"></td>
							</c:when>
							<c:otherwise>
								<td><input type="submit" name="operation" value="<%=RoleListCtl.OP_PREVIOUS%>"></td>
							</c:otherwise>
						</c:choose>
						<td><input type="submit" name="operation" value="<%=RoleListCtl.OP_DELETE%>"></td>
						<td><input type="submit" name="operation" value="<%=RoleListCtl.OP_NEW%>"></td>

						<td align="right"><input type="submit" name="operation" value="<%=RoleListCtl.OP_NEXT%>" <c:if test="${list.size()<pageSize||next==0}">disabled</c:if>></td>
					</tr>
				</table>
			</c:if>
			<c:if test="${list.size() == 0}">
				<td align="center"><input type="submit" name="operation" value="<%=RoleListCtl.OP_BACK%>"></td>
			</c:if>

			<input type="hidden" name="pageNo" value="${pageNo}"> <input type="hidden" name="pageSize" value="${pageSize}">
		</form>
	</div>
	<br> </br> </br> </br>

	<%@include file="Footer.jsp"%>
</body>
</html>
