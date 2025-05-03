<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@page import="com.rays.pro4.controller.GetMarksheetCtl"%>
<%@page import="com.rays.pro4.controller.ORSView"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
<head>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<link rel="icon" type="image/png" href="${ctx}/img/logo.png"
	sizes="16*16" />
<title>
	<c:choose>
		<c:when test="${not empty bean.id}">Get Marksheet</c:when>
		<c:otherwise>Get Marksheet</c:otherwise>
	</c:choose>
</title>
<link rel="stylesheet" href="${ctx}/css/style.css">
</head>
<body>
	<jsp:useBean id="bean" class="com.rays.pro4.Bean.MarksheetBean"
		scope="request"></jsp:useBean>
	<form action="${ctx}${ORSView.GET_MARKSHEET_CTL}" method="post">
		<%@ include file="Header.jsp"%>
		<div class="container">
			<h1 class="text-center">Get Marksheet</h1>
			<input type="hidden" name="id" value="${bean.id}">
			<div class="message-container">
				<c:if test="${not empty errorMessage}">
					<div class="alert alert-danger" role="alert">${errorMessage}</div>
				</c:if>
				<c:if test="${not empty successMessage}">
					<div class="alert alert-success" role="alert">${successMessage}</div>
				</c:if>
			</div>
			<div class="input-container">
				<label for="rollNo">Roll No <span class="required">*</span>:</label> <input
					type="text" name="rollNo" id="rollNo" placeholder="Enter RollNo."
					class="form-control" value="${param.rollNo}">
				<div class="error">${requestScope.rollNo}</div>
			</div>
			<div class="button-container">
				<input type="submit" name="operation" class="btn btn-primary"
					value="${GetMarksheetCtl.OP_GO}"> <input type="submit"
					name="operation" class="btn btn-secondary"
					value="${GetMarksheetCtl.OP_RESET}">
			</div>
			<c:if test="${not empty bean.name && fn:length(bean.name) >= 0}">
				<table class="table table-bordered w-100">
					<thead class="table-header">
						<tr>
							<th colspan="4" class="text-center">
								<h2>Rays Technologies</h2>
							</th>
						</tr>
					</thead>
					<tbody>
						<tr class="table-row">
							<th>Name</th>
							<td>${bean.name}</td>
							<th>Roll No</th>
							<td>${bean.rollNo}</td>
						</tr>
						<tr class="table-row">
							<th>Status</th>
							<td>Regular</td>
							<td>Course</td>
							<td>BE</td>
						</tr>
					</tbody>
				</table>
				<c:set var="phy" value="${bean.physics}" />
				<c:set var="chem" value="${bean.chemistry}" />
				<c:set var="math" value="${bean.maths}" />
				<c:set var="total" value="${phy + chem + math}" />
				<c:set var="perc" value="${total / 3}" />
				<table class="table table-bordered w-100">
					<thead class="table-header">
						<tr>
							<th>Subject</th>
							<th>Maximum Marks</th>
							<th>Minimum Marks</th>
							<th>Marks Obtained</th>
							<th>Grade</th>
						</tr>
					</thead>
					<tbody>
						<c:set var="fail" value="Fail" />
						<tr class="table-row text-center">
							<td>Physics</td>
							<td>100</td>
							<td>33</td>
							<td>${phy} <c:if test="${phy < 33}">
									<span class="required">*</span>
								</c:if></td>
							<td><c:choose>
									<c:when test="${phy <= 100 && phy > 85}">A+</c:when>
									<c:when test="${phy <= 85 && phy > 75}">B+</c:when>
									<c:when test="${phy <= 75 && phy > 65}">B</c:when>
									<c:when test="${phy <= 65 && phy > 55}">C+</c:when>
									<c:when test="${phy <= 55 && phy >= 33}">C</c:when>
									<c:when test="${phy < 33 && phy >= 0}">
										<span class="required"> Fail</span>
									</c:when>
								</c:choose></td>
						</tr>
						<tr class="table-row text-center">
							<td>Chemistry</td>
							<td>100</td>
							<td>33</td>
							<td>${chem} <c:if test="${chem < 33}">
									<span class="required">*</span>
								</c:if></td>
							<td><c:choose>
									<c:when test="${chem <= 100 && chem > 85}">A+</c:when>
									<c:when test="${chem <= 85 && chem > 75}">B+</c:when>
									<c:when test="${chem <= 75 && chem > 65}">B</c:when>
									<c:when test="${chem <= 65 && chem > 55}">C+</c:when>
									<c:when test="${chem <= 55 && chem >= 33}">C</c:when>
									<c:when test="${chem < 33 && chem >= 0}">
										<span class="required"> Fail</span>
									</c:when>
								</c:choose></td>
						</tr>
						<tr class="table-row text-center">
							<td>Maths</td>
							<td>100</td>
							<td>33</td>
							<td>${math} <c:if test="${math < 33}">
									<span class="required">*</span>
								</c:if></td>
							<td><c:choose>
									<c:when test="${math <= 100 && math > 85}">A+</c:when>
									<c:when test="${math <= 85 && math > 75}">B+</c:when>
									<c:when test="${math <= 75 && math > 65}">B</c:when>
									<c:when test="${math <= 65 && math > 55}">C+</c:when>
									<c:when test="${math <= 55 && math >= 33}">C</c:when>
									<c:when test="${math < 33 && math >= 0}">
										<span class="required"> Fail</span>
									</c:when>
								</c:choose></td>
						</tr>
					</tbody>
				</table>
				<table class="table table-bordered w-100">
					<thead class="table-header">
						<tr>
							<th>Total</th>
							<th>Percentage</th>
							<th>Division</th>
							<th>Result</th>
						</tr>
					</thead>
					<tr class="table-row text-center">
						<th>${total} <c:if
								test="${total < 99 || phy < 33 || chem < 33 || math < 33}">
								<span class="required">*</span>
							</c:if></th>
						<th>${perc} %</th>
						<th><c:choose>
								<c:when test="${perc le 100 && perc >= 60}">
									1<sup>st</sup>
								</c:when>
								<c:when test="${perc < 60 && perc >= 40}">
									2<sup>nd</sup>
								</c:when>
								<c:when test="${perc < 40 && perc >= 0}">
									3<sup>rd</sup>
								</c:when>
							</c:choose></th>
						<th><c:choose>
								<c:when test="${phy >= 33 && chem >= 33 && math >= 33}">
									<span class="success-message"> Pass</span>
								</c:when>
								<c:otherwise>
									<span class="required">${fail}</span>
								</c:otherwise>
							</c:choose></th>
					</tr>
				</table>
			</c:if>
		</div>
	</form>
	<%@ include file="Footer.jsp"%>
</body>
</html>