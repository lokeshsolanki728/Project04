<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="com.rays.pro4.controller.ORSView"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="icon" type="image/png" href="${pageContext.request.contextPath}/img/logo.png"
	sizes="16*16" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
<title>Marksheet Merit List</title>
<style type="text/css">
.table-container {
	width: 100%;
	overflow-x: auto;
}
table {
	width: 100%;
	border-collapse: collapse;
}
th, td {
	border: 1px solid #ddd;
	padding: 8px;
	text-align: center;
}
th {
	background-color: skyblue;
}
tr:nth-child(even) {
	background-color: #f2f2f2;
}
.green-text {
	color: green;
}
</style>
</head>
<body>
	<jsp:useBean id="bean" class="com.rays.pro4.Bean.MarksheetBean"
		scope="request"></jsp:useBean>
	<form action="${pageContext.request.contextPath}${ORSView.MARKSHEET_MERIT_LIST_CTL}" method="POST">
		<%@include file="Header.jsp"%>

		<div class="text-center">
			<h1>Marksheet Merit List</h1>
		</div>
		<div class="table-container">
			<table class="table table-striped table-bordered">
				<thead class="thead-dark">
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
					<c:forEach var="marksheet" items="${list}" varStatus="loop">
						<c:set var="phy" value="${marksheet.physics}" />
						<c:set var="chem" value="${marksheet.chemistry}" />
						<c:set var="maths" value="${marksheet.maths}" />
						<c:set var="total" value="${phy + chem + maths}" />
						<c:set var="perc" value="${total / 3}" />
						<tr align="center">
							<td>${loop.index + 1}</td>
							<td>${marksheet.rollNo}</td>
							<td>${marksheet.name}</td>
							<td>${phy}</td>
							<td>${chem}</td>
							<td>${maths}</td>
							<td>${total}</td>
							<td class="green-text">${perc}%</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>

		<div class="text-center">
			<input type="submit" name="operation" value="Back">
		</div>
		<input type="hidden" name="pageNo" value="${pageNo}"> <input
			type="hidden" name="pageSize" value="${pageSize}">
	</form>
	<%@include file="Footer.jsp"%>
</body>
</html>

