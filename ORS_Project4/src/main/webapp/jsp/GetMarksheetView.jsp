<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<%@page import="com.rays.pro4.controller.GetMarksheetCtl"%>
<%@page import="com.rays.pro4.Util.ServletUtility"%>
<%@page import="com.rays.pro4.Util.DataUtility"%>
<%@page import="com.rays.pro4.controller.ORSView"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
<head>
<link rel="icon" type="image/png"
	href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16*16" />
<title> Get marksheet</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/style.css">

</head>
<body >
	<jsp:useBean id="bean" class="com.rays.pro4.Bean.MarksheetBean"
		scope="request"></jsp:useBean>
	<form action="<%=ORSView.GET_MARKSHEET_CTL%>" method="post">
		<%@ include file="Header.jsp"%>


		<input type="hidden" name="id" value="${bean.id}">

		<center>
			<div align="center">
				<h1>Get Marksheet</h1>

				<c:if test="${not empty errorMessage}">
					<h3 class="error-message"> ${errorMessage}</h3>
				</c:if>
				<c:if test="${not empty successMessage}">
					<h3 class="success-message"> ${successMessage}</h3>
				</c:if>
			</div>

			<table>
				<tr>
					<th align="left"><label for="rollNo">Roll No <span class="required">*</span>:</label></th>
					<td><input type="text" name="rollNo" id="rollNo"
						placeholder="Enter RollNo." class="form-control"
						value="${param.rollNo}"></td>
					<td>
						<div class="error-message">${requestScope.rollNo}</div>
					</td>
				</tr>

				<tr>
					<th style="padding: 3px"></th>
				</tr>

				<tr>
					<th></th>
					<td>&nbsp;&emsp;<input type="submit" name="operation"
						value="<%=GetMarksheetCtl.OP_GO%>"> &nbsp;&nbsp;<input
						type="submit" name="operation"
						value="<%=GetMarksheetCtl.OP_RESET%>"></td>
				</tr>

			</table>

			<c:if test="${not empty bean.name && fn:length(bean.name) >= 0}">

				<table>
					<table border="1" width="100%">
						<tr align="center" class="table-header">
							<td>
								<h2>Rays Technologies</h2>
							</td>
						</tr>
					</table>

					<table border="1" width="100%">
						<tr align="center" class="table-row">
							<th>Name</th>
							<td>${bean.name}</td>
							<th>Roll No</th>
							<td>${bean.rollNo}</td>

						</tr>
						<tr align="center" class="table-row">
							<td>Status</td>
							<th>Regular</th>
							<td>Course</td>
							<th>BE</th>
						</tr>
					</table>
					<c:set var="phy" value="${bean.physics}" />
					<c:set var="chem" value="${bean.chemistry}" />
					<c:set var="math" value="${bean.maths}" />
					<c:set var="total" value="${phy + chem + math}" />
					<c:set var="perc" value="${total / 3}" />

					<table border="1" width="100%">

						<tr align="center" class="table-header">
							<th>Subject</th>
							<th>Maximum Marks</th>
							<th>Minimum Marks</th>
							<th>Marks Obtained</th>
							<th>Grade</th>
						</tr>

						<tr align="center" class="table-row">
							<td>Physics</td>
							<td>100</td>
							<td>33</td>
							<td>${phy} <c:if test="${phy < 33}">
									<span class="required">*</span>
								</c:if></td>

							<td align="center" class="table-row"><c:choose>
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

						<tr align="center" class="table-row">
							<td>Chemistry</td>
							<td>100</td>
							<td>33</td>
							<td>${chem} <c:if test="${chem < 33}">
									<span class="required">*</span>
								</c:if></td>

							<td align="center" class="table-row"><c:choose>
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

						<tr align="center" class="table-row">
							<td>Maths</td>
							<td>100</td>
							<td>33</td>
							<td>${math} <c:if test="${math < 33}">
									<span class="required">*</span>
								</c:if></td>

							<td align="center" class="table-row"><c:choose>
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
					</table>

					<table border="1" width="100%">
						<tr class="table-header">
							<th>Total</th>
							<th>Percentage</th>
							<th>Division</th>
							<th>Result</th>
						</tr>
						<tr>
							<th class="table-row">${total} <c:if test="${total < 99 || phy < 33 || chem < 33 || math < 33}">
									<span class="required">*</span>
								</c:if>
							</th>

							<th class="table-row">${perc} %</th>
							<th><c:choose>
									<c:when test="${perc < 100 && perc >= 60}">
										1<sup>st</sup>
									</c:when>
									<c:when test="${perc < 60 && perc >= 40}">
										2<sup>nd</sup>
									</c:when>
									<c:when test="${perc < 40 && perc >= 0}">
										3<sup>rd</sup>
									</c:when>
								</c:choose></th>

							<th class="table-row"><c:choose>
									<c:when test="${phy >= 33 && chem >= 33 && math >= 33}">
										<span class="success-message"> Pass</span>
									</c:when>
									<c:otherwise>
										<span class="required"> Fail</span>
									</c:otherwise>
								</c:choose></th>
						</tr>
					</table>

				</c:if>
		</table>
	</form>
	</center>
	</div>
	<%@ include file="Footer.jsp"%>
</body>
</html>