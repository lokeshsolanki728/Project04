<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.rays.pro4.controller.ORSView"%>
<%@ page import="com.rays.pro4.controller.MarksheetListCtl"%>
<%@ page import="com.rays.pro4.Util.HTMLUtility"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
	<head>
		<c:set var="ctx" value="${pageContext.request.contextPath}" />
		<link rel="icon" type="image/png" href="${ctx}/img/logo.png"
			sizes="16*16" />
		<link rel="stylesheet" href="${ctx}/css/style.css">
		<title>
			<c:choose>
				<c:when test="${not empty requestScope.list}">Marksheet List</c:when>
				<c:otherwise>Marksheet List</c:otherwise>
			</c:choose>
		</title>
		<script src="${ctx}/js/jquery.min.js"></script>
		<script src="${ctx}/js/Checkbox11.js"></script>
	</head>
	<body class="bg-light main-body">
		<jsp:useBean id="bean" class="com.rays.pro4.Bean.MarksheetBean"
			scope="request"></jsp:useBean>
		<form action="${ctx}${ORSView.MARKSHEET_LIST_CTL}" method="POST">
			<%@include file="Header.jsp"%>
			<c:set var="list" value="${requestScope.list}" />
			<c:set var="rollNoList" value="${requestScope.rollNo}" />
			<c:set var="next" value="${requestScope.nextlist}" />
			<c:set var="pageNo" value="${requestScope.pageNo}" />
			<c:set var="pageSize" value="${requestScope.pageSize}" />
			<c:set var="index" value="${(pageNo - 1) * pageSize + 1}" />
			<div class="container">
				<div class="text-center">
					<h1>Marksheet List</h1>
					<c:if test="${not empty requestScope.errorMessage}">
						<span class="alert alert-danger" role="alert">${requestScope.errorMessage}</span>
					</c:if>
					<c:if test="${not empty requestScope.successMessage}">
						<span class="alert alert-success" role="alert">${requestScope.successMessage}</span>
					</c:if>
				</div>
				<c:if test="${not empty list}">
					<table class="search-table w-100">
						<tr>
							<td class="text-center">
								<label for="name"> Student Name :</label>
								<input type="text" id="name" name="name" placeholder="Enter Student Name"
                                       class="form-control-inline" value="${param.name}">
                                <label for="rollNo">RollNo :</label>
                                ${HTMLUtility.getList("rollNo", param.rollNo, rollNoList)}
                                <input type="submit" name="operation" class="btn btn-primary"
                                       value="${MarksheetListCtl.OP_SEARCH}"> <input
                                    type="submit" name="operation" class="btn btn-secondary"
								value="${MarksheetListCtl.OP_RESET}">
							</td>
						</tr>
					</table>
					<table class="table table-bordered table-striped w-100">
						<thead class="table-header">
							<tr>
								<th><input type="checkbox" id="select_all" name="select">
									Select All.</th>
								<th>S.No.</th>
								<th>RollNo</th>
								<th>Name</th>
								<th>Physics</th>
								<th>Chemistry</th>
								<th>Maths</th>
								<th>Total</th>
								<th>Percentage</th>
								<th>Result</th>
								<th>Edit</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="marksheet" items="${list}">
								<c:set var="phy" value="${marksheet.physics}" />
								<c:set var="chem" value="${marksheet.chemistry}" />
								<c:set var="math" value="${marksheet.maths}" />
								<c:set var="total" value="${phy + chem + math}" />
								<c:set var="perc" value="${total / 3}" />
								<tr class="text-center table-row">
									<td><input type="checkbox" class="checkbox" name="ids"
										value="${marksheet.id}"></td>
									<td>${index}</td>
									<td>${marksheet.rollNo}</td>
									<td>${marksheet.name}</td>
									<td>${marksheet.physics}</td>
									<td>${marksheet.chemistry}</td>
									<td>${marksheet.maths}</td>
									<td>${total}</td>
									<td>${perc}%</td>
									<td><c:choose>
											<c:when test="${phy >= 33 && chem >= 33 && math >= 33}">
												<span class="text-success"> Pass</span>
											</c:when>
											<c:otherwise>
												<span class="text-danger"> Fail</span>
											</c:otherwise>
										</c:choose></td>
									<td><a href="${ctx}/MarksheetCtl?id=${marksheet.id}"
										class="btn btn-link">Edit</a></td>
								</tr>
								<c:set var="index" value="${index + 1}" />
							</c:forEach>
						</tbody>
					</table>					
					<table class="w-100">						
						<tr>
							<td class="float-start">
								<input type="submit" name="operation" class="btn btn-secondary"
                                       value="${MarksheetListCtl.OP_PREVIOUS}"
                                       ${pageNo == 1 ? 'disabled' : ''}>
							</td>
							<td><input type="submit" name="operation"
                                       class="btn btn-danger" value="${MarksheetListCtl.OP_DELETE}"></td>
							<td><input type="submit" name="operation"
								class="btn btn-success" value="${MarksheetListCtl.OP_NEW}"></td>
							<td class="text-right"><input type="submit"
								class="btn btn-primary" name="operation"
								value="${MarksheetListCtl.OP_NEXT}"
								${list.size() < pageSize || next == 0 ? 'disabled' : ''}></td>
						</tr>
					</table>
				</c:if>
				<c:if test="${empty list}">
					<div class="text-center">
						<h3>No Marksheet Found</h3>
					</div>
				</c:if>
				<input type="hidden" name="pageNo" value="${pageNo}"> <input
					type="hidden" name="pageSize" value="${pageSize}">
			</div>
		</form>
		<%@include file="Footer.jsp"%>
	</body>
</html>