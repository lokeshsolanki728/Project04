<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@page import="com.rays.pro4.controller.ORSView"%>
<%@page import="com.rays.pro4.controller.UserCtl"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<link rel="icon" type="image/png" href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16*16"/>
<title> User Page</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">

  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
  <link rel="stylesheet" href="/resources/demos/style.css">
  <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
  <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
  <script>
  $( function() {
    $( "#udatee" ).datepicker({
      changeMonth: true,
      changeYear: true,
      yearRange: '1980:2002',
    });
  } );
  </script>
  <style type="text/css">
	.error-message {
	    color: red;
	}
	
	.required {
	    color: red;
	}
</style>

</head>
<body>
	<jsp:useBean id="bean" class="com.rays.pro4.Bean.UserBean"
		scope="request"></jsp:useBean>
	<%@ include file="Header.jsp"%>

	<div align="center">
		<form action="<%=ORSView.USER_CTL%>" method="post">
			<c:set var="roleList" scope="request" value="${requestScope.roleList}"></c:set>
			<div align="center">
				<h1>
					<c:choose>
						<c:when test="${bean.id > 0}">
							Update User
						</c:when>
						<c:otherwise>
							Add User
						</c:otherwise>
					</c:choose>
				</h1>
				<h3>
					<font color="red"> ${errorMessage}</font> <font color="green">
						${successMessage}</font>
				</h3>
			</div>
			<input type="hidden" name="id" value="${bean.id}"> <input
				type="hidden" name="createdBy" value="${bean.createdBy}"> <input
				type="hidden" name="modifiedBy" value="${bean.modifiedBy}"> <input
				type="hidden" name="createdDatetime" value="${bean.createdDatetime}">
			<input type="hidden" name="modifiedDatetime"
				value="${bean.modifiedDatetime}">
			<table>
				<tr>
					<th align="left"><label for="firstName">First Name <span class="required">*</span>:</label></th>
					<td><input type="text" name="firstName" id="firstName" class="form-control"
						placeholder="Enter First Name" value="${bean.firstName}"></td>
					<td><div class="error-message">${requestScope.firstName}</div></td>
				</tr>
				<tr>
					<th style="padding: 3px"></th>
				</tr>
				<tr>
					<th align="left"><label for="lastName">Last Name <span class="required">*</span>:</label></th>
					<td><input type="text" name="lastName" id="lastName" class="form-control"
						placeholder="Enter Last Name" value="${bean.lastName}"></td>
					<td><div class="error-message">${requestScope.lastName}</div></td>
				</tr>
				<tr>
					<th style="padding: 3px"></th>
				</tr>
				<tr>
					<th align="left"><label for="login">Login Id <span class="required">*</span>:</label></th>
					<td><input type="text" name="login" id="login" class="form-control"
						placeholder="Enter EmailId" value="${bean.login}"
						<c:if test="${bean.id > 0}">readonly</c:if>></td>
					<td><div class="error-message">${requestScope.login}</div></td>
				</tr>
				<c:choose>
					<c:when test="${bean.id > 0}">
						<tr>
							<td><input type="hidden" name="password"
								value="${bean.password}"></td>
							<td><input type="hidden" name="confirmPassword"
								value="${bean.password}"></td>
						</tr>
					</c:when>
					<c:otherwise>
						<tr>
							<th style="padding: 3px"></th>
						</tr>
						<tr>
							<th align="left"><label for="password">Password <span class="required">*</span>:</label></th>
							<td><input type="password" name="password" id="password" class="form-control"
								placeholder="Enter Password" value="${bean.password}"></td>
							<td><div class="error-message">${requestScope.password}</div></td>
						</tr>
						<tr>
							<th style="padding: 3px"></th>
						</tr>
						<tr>
							<th align="left"><label for="confirmPassword">Confirm Password <span class="required">*</span>:</label></th>
							<td><input type="password" name="confirmPassword" id="confirmPassword" class="form-control"
								placeholder="Re-Enter Password" value="${bean.confirmPassword}"></td>
							<td><div class="error-message">${requestScope.confirmPassword}</div></td>
						</tr>
					</c:otherwise>
				</c:choose>
				<tr>
					<th style="padding: 3px"></th>
				</tr>
				<tr>
					<th align="left"><label for="gender">Gender <span class="required">*</span>:</label></th>
					<td><select name="gender" id="gender" class="form-control">
                            <option value="">Select Gender</option>
                             <option value="Male" ${bean.gender == 'Male' ? 'selected' : ''}>Male</option>
                             <option value="Female" ${bean.gender == 'Female' ? 'selected' : ''}>Female</option>
                             </select></td>
					<td><div class="error-message">${requestScope.gender}</div></td>
				</tr>
				<tr>
					<th style="padding: 3px"></th>
				</tr>
				<tr>
					<th align="left"><label for="roleId">Role <span class="required">*</span>:</label></th>
					<td><select class="form-control" name="roleId" id="roleId">
					<c:forEach items="${roleList}" var="role">
					 <option value="${role.key}" ${role.key == bean.roleId ? 'selected' : ''}>${role.value}</option>
                    </c:forEach>
                    </select></td>
					<td><div class="error-message">${requestScope.roleId}</div></td>
				</tr>
				<tr>
					<th style="padding: 3px"></th>
				</tr>
				<tr>
					<th align="left"><label for="udatee">Date Of Birth <span class="required">*</span>:</label></th>
					<td><input type="text" name="dob" id="udatee" class="form-control"
						placeholder="Enter Date Of Birth" readonly="readonly"
						value="${bean.dob}"></td>
					<td><div class="error-message">${requestScope.dob}</div></td>
				</tr>
				<tr>
				<tr>
					<th style="padding: 3px"></th>
				</tr>
				<tr>
					<th align="left"><label for="mobileNo">Mobile No <span class="required">*</span>:</label></th>
					<td><input type="number" name="mobileNo" id="mobileNo" class="form-control"
						maxlength="10" placeholder="Enter Mobile No" value="${bean.mobileNo}"></td>
					<td><div class="error-message">${requestScope.mobileNo}</div></td>
				</tr>
				<tr>
					<th style="padding: 3px"></th>
				</tr>
				<tr>
					<th></th>
					<c:choose>
						<c:when test="${bean.id > 0}">
							<td colspan="2">&nbsp; &emsp; <input type="submit"
								name="operation" value="<%=UserCtl.OP_UPDATE%>"> &nbsp;
								&nbsp; <input type="submit" name="operation"
								value="<%=UserCtl.OP_CANCEL%>"></td>
						</c:when>
						<c:otherwise>
							<td colspan="2">&nbsp; &emsp; <input type="submit"
								name="operation" value="<%=UserCtl.OP_SAVE%>"> &nbsp;
								&nbsp; <input type="submit" name="operation"
								value="<%=UserCtl.OP_RESET%>"></td>
						</c:otherwise>
					</c:choose>
				</tr>
			</table>
		</form>
	</div>
	<%@ include file="Footer.jsp"%>
</body>
</html>