<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.rays.pro4.controller.ORSView"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<html>
<head>
	<c:set var="ctx" value="${pageContext.request.contextPath}" />
	<link rel="icon" type="image/png" href="${ctx}/img/logo.png"
		sizes="16*16" />
	<link rel="stylesheet" href="${ctx}/css/style.css">
	<title>Marksheet Merit List</title>
</head>
<body>
	<jsp:useBean id="bean" class="com.rays.pro4.Bean.MarksheetBean"
		scope="request"></jsp:useBean>
	<form action="${ctx}${ORSView.MARKSHEET_MERIT_LIST_CTL}" method="POST">
		<%@include file="Header.jsp"%>
		<div class="container">
			<h1 class="text-center">Marksheet Merit List</h1>
			<div class="table-container">
				<table class="table table-bordered table-striped w-100">
					<thead class="table-header">
						<tr>
							<th>S.No.</th>
							<th>Roll No</th>
							<th>Name</th>
							<th>Physics</th>
							<th>Chemistry</th>
							<th>Maths</th>
							<th>Total</th>
							<th>Percentage</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="marksheet" items="${requestScope.list}"
							varStatus="loop">
							<c:set var="phy" value="${marksheet.physics}" />
							<c:set var="chem" value="${marksheet.chemistry}" />
							<c:set var="maths" value="${marksheet.maths}" />
							<c:set var="total" value="${phy + chem + maths}" />
							<c:set var="perc" value="${total / 3}" />
							<tr class="text-center table-row">
								<td>${loop.index + 1}</td>
								<td>${marksheet.rollNo}</td>
								<td>${marksheet.name}</td>
								<td>${phy}</td>
								<td>${chem}</td>
								<td>${maths}</td>
								<td>${total}</td>
								<td><span class="text-success">${perc}%</span></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<div class="button-container">
				<input type="submit" name="operation" class="btn btn-secondary"
					value="Back">
			</div>
			<input type="hidden" name="pageNo" value="${requestScope.pageNo}">
			<input type="hidden" name="pageSize"
				value="${requestScope.pageSize}">
		</div>
	</form>
	<%@include file="Footer.jsp"%>
</body>
</html>
