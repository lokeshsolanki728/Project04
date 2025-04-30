<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@page import="com.rays.pro4.Bean.CourseBean"%>
<%@page import="java.util.List"%>
<%@page import="com.rays.pro4.Util.DataUtility"%>
<%@ page import="com.rays.pro4.Util.HTMLUtility"%>
<%@page import="com.rays.pro4.controller.ORSView"%>
<%@page import="com.rays.pro4.controller.SubjectCtl"%>
<%@page import="com.rays.pro4.Util.ServletUtility"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<head>
<link rel="icon" type="image/png" href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16*16"/>
<title> Subject Registration Page</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">


</head>
<body>
<jsp:useBean id="bean" class="com.rays.pro4.Bean.SubjectBean" scope="request" />
<form action="${pageContext.request.contextPath}${ORSView.SUBJECT_CTL}" method="post">

	<%@include file="Header.jsp"%>
	<c:set var="CourseList" value="${requestScope.CourseList}" />
	<center>
		<h1>
			<c:choose>
				<c:when test="${not empty bean.id}">Update Subject</c:when>
				<c:otherwise>Add Subject</c:otherwise>
			</c:choose>
		</h1>
		<div>
			<c:if test="${not empty successMessage}">
				<div class="alert alert-success" role="alert">${successMessage}</div>
			</c:if>
			<c:if test="${not empty errorMessage}">
				<div class="alert alert-danger" role="alert">${errorMessage}</div>
			</c:if>
		</div>

		<input type="hidden" name="id" value="${bean.id}"> <input type="hidden"
			name="createdby" value="${bean.createdBy}"> <input
			type="hidden" name="modifiedby" value="${bean.modifiedBy}"> <input
			type="hidden" name="createddatetime" value="${bean.createdDatetime}">
		<input type="hidden" name="modifieddatetime"
			value="${bean.modifiedDatetime}">

		<table>

			<tr>
				<th align="left"><label for="coursename">Course Name<span class="required">*</span> :</label></th>
				<td>${HTMLUtility.getList("coursename", bean.courseId, CourseList)}</td>
				<td><font color="red"><div class="error-message">${requestScope.coursename}</div></font></td>
			</tr>

			<tr>
				<th style="padding: 3px"></th>
			</tr>

			<tr>
				<th align="left"><label for="name">Subject Name <span class="required">*</span> :</label></th>
				<td><input type="text" name="name" id="name"
					placeholder="Enter Subject Name" class="form-control" value="${bean.subjectName}"></td>
				<td><font color="red"><div class="error-message">${requestScope.name}</div></font></td>
			</tr>

			<tr>
				<th style="padding: 3px"></th>
			</tr>

			<tr>
				<th align="left"><label for="description">Description<span class="required">*</span> :</label></th>
				<td><input type="text" name="description" id="description"
					placeholder="Enter Description" class="form-control" value="${bean.description}"></td>
				<td><font color="red"><div class="error-message">${requestScope.description}</div></font></td>
			</tr>

			<tr>
				<th style="padding: 3px"></th>
			</tr>
	
			<tr>
				<th></th>
				<td colspan="2"> &nbsp; &emsp; <c:choose>
						<c:when test="${not empty bean.id}">
							<input type="submit" name="operation" value="<%=SubjectCtl.OP_UPDATE%>"> &nbsp; &nbsp; <input type="submit" name="operation" value="<%=SubjectCtl.OP_CANCEL%>">
						</c:when>
						<c:otherwise>
							<input type="submit" name="operation" value="<%=SubjectCtl.OP_SAVE%>"> &nbsp; &nbsp; <input type="submit" name="operation" value="<%=SubjectCtl.OP_RESET%>">
						</c:otherwise>
					</c:choose></td>
			</tr>
		</table>

	</form>
	</br> </br>
	</center>

	<%@include file="Footer.jsp"%>
</body>
</html>