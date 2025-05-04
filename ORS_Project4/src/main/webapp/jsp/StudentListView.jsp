<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@page import="com.rays.pro4.Util.DataUtility"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.rays.pro4.controller.StudentListCtl"%>
<%@page import="com.rays.pro4.Bean.StudentBean"%>
<%@page import="com.rays.pro4.Bean.CollegeBean"%>
<%@page import="com.rays.pro4.controller.ORSView"%>
<%@page import="com.rays.pro4.Util.HTMLUtility"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
<head>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<link rel="icon" type="image/png" href="${ctx}/img/logo.png"
	sizes="16*16" />
<title>Student List</title>
<script src="${ctx}/js/jquery.min.js"></script>
<script src="${ctx}/js/Checkbox11.js"></script>
<link rel="stylesheet" href="${ctx}/css/style.css">
</head>
<body>
<jsp:useBean id="cbean" class="com.rays.pro4.Bean.CollegeBean"
	scope="request"></jsp:useBean>
<jsp:useBean id="bean" class="com.rays.pro4.Bean.StudentBean"
	scope="request"></jsp:useBean>
<form action="${ctx}${ORSView.STUDENT_LIST_CTL}" method="post">
	<%@include file="Header.jsp"%>
	<div class="container">
		<h1 class="text-center">Student List</h1>
		<div class="message-container">
			<c:if test="${not empty requestScope.error}">
				<div class="alert alert-danger">${requestScope.error}</div>
			</c:if>
			<c:if test="${not empty requestScope.success}">
				<div class="alert alert-success">${requestScope.success}</div>
			</c:if>
		</div>
		<c:set var="clist" value="${requestScope.CollegeList}" />
		<c:set var="pageNo" value="${requestScope.pageNo}" />
		<c:set var="orderBy" value="${requestScope.orderBy}" />
		<c:set var="sortOrder" value="${requestScope.sortOrder}" />
		<c:set var="pageSize" value="${requestScope.pageSize}" />
		<c:set var="index" value="${((pageNo - 1) * pageSize) + 1}" />
		<c:set var="list" value="${requestScope.list}" />
		<c:set var="studentBean" value="${bean}" />
		<c:if test="${not empty list}">
			<table class="search-table w-100">
				<tr>					
					<td class="text-center">
					    <label for="firstName">First Name :</label>
					    <input type="text" id="firstName" name="firstName"
					           placeholder="Enter First Name" class="form-control-inline"
					           value="${param.firstName}">
					    
					    <label for="lastName">Last Name :</label>
					    <input type="text" id="lastName" name="lastName"
					           placeholder="Enter Last Name" class="form-control-inline"
					           value="${param.lastName}">
					           
					     <label for="rollNo">RollNo :</label>
					    <input type="text" id="rollNo" name="rollNo"
					           placeholder="Enter RollNo" class="form-control-inline"
					           value="${param.rollNo}">
					           
					      <label for="mobileNo">Mobile No :</label>
					    <input type="text" id="mobileNo" name="mobileNo"
					           placeholder="Enter Mobile No" class="form-control-inline"
					           value="${param.mobileNo}">		    
					    <label for="collegename">College Name :</label>
					    <select id="collegename" name="collegename" class="form-control-inline">
							${HTMLUtility.getList("collegename", studentBean.collegeId, clist)}
						</select> 
						<input type="submit" name="operation" class="btn btn-primary"
							value="<%=StudentListCtl.OP_SEARCH%>">
					    <input type="submit" name="operation" class="btn btn-secondary" value="<%=StudentListCtl.OP_RESET%>">
					</td>
				</tr>
			</table>
			<table class="table table-bordered table-striped w-100">
				<thead class="table-header">
					<tr>
						<th><input type="checkbox" id="select_all" name="select">Select
							All.</th>
						<th>S.No.</th>
						<th>College Name.</th>
						<th>
						<c:choose>
								<c:when test="${orderBy == 'firstName'}">
									<c:choose>
										<c:when test="${sortOrder == 'asc'}">
											<a href="${ctx}/ctl/StudentListCtl?orderBy=firstName&sortOrder=desc">First Name <i class="fa fa-sort-asc"></i></a>
										</c:when>
										<c:otherwise>
											<a href="${ctx}/ctl/StudentListCtl?orderBy=firstName&sortOrder=asc">First Name <i class="fa fa-sort-desc"></i></a>
										</c:otherwise>
									</c:choose>
								</c:when>
								<c:otherwise>
									<a href="${ctx}/ctl/StudentListCtl?orderBy=firstName&sortOrder=asc">First Name</a>
								</c:otherwise>
							</c:choose>
						</th>
						<th>Last Name.</th>
						<th>Roll No.</th>
						<th>Mobile No.</th>
						<th>Email Id.</th>
						<th>Edit</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="student" items="${list}">
						<tr class="text-center table-row">
							<td><input type="checkbox" class="checkbox" name="ids"
								value="${student.id}"></td>
							<td>${index}
							    <c:set var="index" value="${index + 1}" />
							</td>
							<td>${student.collegeName}</td>
							<td>${student.firstName}</td>
							<td>${student.lastName}</td>
							<td>${student.rollNo}</td>
							<td>${student.mobileNo}</td>
							<td>${student.email}</td>
							<td><a href="${ctx}/StudentCtl?id=${student.id}" class="btn btn-link">Edit</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<table class="w-100">
				<tr class="button-container">
					<td class="float-start"><input type="submit" name="operation"
						class="btn btn-secondary" value="<%=StudentListCtl.OP_PREVIOUS%>"
						${pageNo == 1 ? 'disabled' : ''}></td>
					<td class="text-center">
					    <input type="submit" name="operation" class="btn btn-danger" value="<%=StudentListCtl.OP_DELETE%>">
					</td>
					<td class="float-end">
					    <input type="submit" name="operation" class="btn btn-success" value="<%=StudentListCtl.OP_NEW%>">
					    <input type="submit" name="operation" class="btn btn-primary" value="<%=StudentListCtl.OP_NEXT%>" ${list.size() < pageSize ? 'disabled' : ''}>
					</td>
					
					<%--<td class="text-right"><input type="submit" name="operation"--%>
						<%--class="btn btn-primary" value="<%=StudentListCtl.OP_NEXT%>"--%>
						<%--${list.size() < pageSize || model.nextPK()-1 == bean.id ? 'disabled' : ''}></td>--%>
				</tr>
				<c:if test="${empty list}">
					<div class="text-center">
						<input type="submit" name="operation" class="btn btn-primary" value="<%=StudentListCtl.OP_BACK%>">
					</div>
				</c:if>
			</table>
		</c:if>
		<c:if test="${empty list}">
		    <div class="text-center">
		        <h3>No Record Found</h3>
		    </div>
		</c:if>
		<input type="hidden" name="pageNo" value="${pageNo}">
		<input type="hidden" name="pageSize" value="${pageSize}">
		<input type="hidden" name="orderBy" value="${orderBy}">
		<input type="hidden" name="sortOrder" value="${sortOrder}">
	</div>
	<script src="https://kit.fontawesome.com/48b351528a.js"
		crossorigin="anonymous"></script>
</form>
<%@include file="Footer.jsp"%>
</body>
</html>

							<td>${student.dob}</td>
							<td>${student.mobileNo}</td>
							<td>${student.email}</td>
							<td><a href="${ctx}/StudentCtl?id=${student.id}"
								class="btn btn-link">Edit</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<table class="w-100">
				<tr>
					<td><input type="submit" name="operation"
						class="btn btn-secondary"
						value="<%=StudentListCtl.OP_PREVIOUS%>"
						${pageNo == 1 ? 'disabled' : ''}></td>
					<td><input type="submit" name="operation"
						class="btn btn-danger" value="<%=StudentListCtl.OP_DELETE%>"></td>
					<td><input type="submit" name="operation"
						class="btn btn-success" value="<%=StudentListCtl.OP_NEW%>"></td>
					<c:set var="model" value="<%=new StudentModel()%>" />
					<td class="text-right"><input type="submit" name="operation"
						class="btn btn-primary" value="<%=StudentListCtl.OP_NEXT%>"
						${list.size() < pageSize || model.nextPK()-1 == bean.id ? 'disabled' : ''}></td>
				</tr>
			</table>
		</c:if>
		<c:if test="${empty list}">
			<div class="text-center">
				<input type="submit" name="operation" class="btn btn-primary"
					value="<%=StudentListCtl.OP_BACK%>">
			</div>
		</c:if>
		<input type="hidden" name="pageNo" value="${pageNo}"> <input
			type="hidden" name="pageSize" value="${pageSize}">
	</div>
</form>
<%@include file="Footer.jsp"%>
</body>
</html>