<%@page import="com.rays.pro4.Util.MessageConstant"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<%@page import="com.rays.pro4.controller.StudentCtl"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<html>
<head>
<link rel="icon" type="image/png" href="${pageContext.request.contextPath}/img/logo.png" sizes="16*16" />
<title>Student Registration Page</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">

  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/demos/style.css">
  <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
   <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/jquery-ui.min.css" />
  <script type="text/javascript" src="${pageContext.request.contextPath}/resources/jquery-3.5.1.min.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/resources/jquery-ui.min.js"></script>
  <script>
  $( function() {
    $( "#udate" ).datepicker({
      changeMonth: true,
      changeYear: true,
	  yearRange:'1980:2002', 
    });
  } );
  </script>
<style type="text/css">
</style>
<jsp:useBean id="bean" class="com.rays.pro4.Bean.StudentBean" scope="request"></jsp:useBean>
</head>
<body>
	<%@include file="Header.jsp"%>
	<form action="${pageContext.request.contextPath}/ctl/StudentCtl" method="post">
		<c:set var="clist" value="${requestScope.collegeList}"></c:set>
		<div class="text-center">
			<h1>
				<c:choose>
					<c:when test="${not empty bean.id}">Update Student</c:when>
					<c:otherwise>Add Student</c:otherwise>
				</c:choose>
			</h1>
			<c:if test="${not empty successMessage}">
				<div class="alert alert-success" role="alert">${successMessage}</div>
			</c:if>
			<c:if test="${not empty errorMessage}">
				<div class="alert alert-danger" role="alert">${errorMessage}</div>
			</c:if>
		</div>
		<input type="hidden" name="id" value="${bean.id}">
		<input type="hidden" name="createdby" value="${bean.createdBy}">
		<input type="hidden" name="modifiedby" value="${bean.modifiedBy}">
		<input type="hidden" name="createddatetime" value="${bean.createdDatetime}">
		<input type="hidden" name="modifieddatetime" value="${bean.modifiedDatetime}">
		<div class="container">
			<table>
				<tr>
					<th align="left"><label for="collegename">CollegeName <span class="required">*</span> :</label></th>
					<td><select class="form-control" id="collegename" name="collegename">
							<option value="">Select College</option>
							<c:forEach items="${clist}" var="college">
								<option value="${college.id}" ${college.id == bean.collegeId ? 'selected' : ''}>${college.name}</option>
							</c:forEach>
					</select></td>
					<td><div class="error-message">${requestScope.collegename}</div></td>
				</tr>
				<tr>
					<th align="left"><label for="firstname">FirstName <span class="required">*</span> :</label></th>
					<td><input type="text" id="firstname" class="form-control" name="firstname" placeholder="Enter First Name" value="${bean.firstName}"></td>
					<td><div class="error-message">${requestScope.firstname}</div></td>
				</tr>
				<tr>
					<th align="left"><label for="lastname">LastName <span class="required">*</span> :</label></th>
					<td><input type="text" id="lastname" class="form-control" name="lastname" placeholder="Enter Last Name" value="${bean.lastName}"></td>
					<td><div class="error-message">${requestScope.lastname}</div></td>
				</tr>
				<tr>
					<th align="left"><label for="udate">Date Of Birth <span class="required">*</span> :</label></th>
					<td><input type="text" id="udate" class="form-control" name="dob" readonly="readonly" placeholder=" Date of Birth" value="${bean.dob}"></td>
					<td><div class="error-message">${requestScope.dob}</div></td>
				</tr>
				<tr>
					<th align="left"><label for="mobile">MobileNo <span class="required">*</span> :</label></th>
					<td><input type="text" id="mobile" class="form-control" name="mobile" maxlength="10" placeholder="Enter Mobile No" value="${bean.mobileNo}"></td>
					<td><div class="error-message">${requestScope.mobile}</div></td>
				</tr>
				<tr>
					<th align="left"><label for="email">Email-Id <span class="required">*</span> :</label></th>
					<td><input type="text" id="email" class="form-control" name="email" placeholder="Enter Email_Id" value="${bean.email}"></td>
					<td><div class="error-message">${requestScope.email}</div></td>
				</tr>
				<tr>
					<th></th>
					<c:choose>
						<c:when test="${bean.id > 0}">
							<td>&nbsp; &emsp; <input type="submit" name="operation" value="${StudentCtl.OP_UPDATE}"> &nbsp; &nbsp; <input type="submit" name="operation" value="${StudentCtl.OP_CANCEL}"></td>
						</c:when>
						<c:otherwise>
							<td>&nbsp; &emsp; <input type="submit" name="operation" value="${StudentCtl.OP_SAVE}"> &nbsp; &nbsp; <input type="submit" name="operation" value="${StudentCtl.OP_RESET}"></td>
						</c:otherwise>
					</c:choose>
				</tr>
			</table>
		</div>
	</form>
	<%@ include file="Footer.jsp"%>
</body>
</html>
