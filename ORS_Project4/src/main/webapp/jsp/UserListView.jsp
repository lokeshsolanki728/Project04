<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@page import="com.rays.pro4.Model.RoleModel"%>

<%@page import="com.rays.pro4.Bean.RoleBean"%>
<%@page import="com.rays.pro4.Bean.UserBean"%>
<%@page import="com.rays.pro4.Util.HTMLUtility"%>

<%@page import="java.util.List"%>

<%@page import="com.rays.pro4.controller.ORSView"%>
<%@page import="com.rays.pro4.controller.UserListCtl"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
<head>
<link rel="icon" type="image/png"
	href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16*16" />
<title>User List View</title>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/demos/style.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">

<script src="<%=ORSView.APP_CONTEXT%>/js/jquery.min.js"></script>
<script src="<%=ORSView.APP_CONTEXT%>/js/Checkbox11.js"></script>

</head>
<body>
	<jsp:useBean id="bean" class="com.rays.pro4.Bean.UserBean" scope="request"></jsp:useBean>
	<%@include file="Header.jsp"%>

	<form action="${pageContext.request.contextPath}/UserListCtl" method="post">
		<div align="center">
		
			<h1>User List</h1>
			<H3>
				<font color="red"> <c:if test="${not empty error}">
						${error}
					</c:if></font>
			</H3>
			<H3>
				<font color="green"> <c:if test="${not empty success}">
						${success}
					</c:if></font>
			</H3>
		</div>
	
			<c:set var="rlist" value="${requestScope.RoleList}"></c:set>
			<c:set var="ulist" value="${requestScope.LoginId}"></c:set>
		    <c:set var="next" value="${requestScope.nextlist}" />
			<c:set var="pageNo" value="${param.pageNo}" />
            <c:set var="pageSize" value="${param.pageSize}" />
			<c:set var="index" value="${(pageNo - 1) * pageSize + 1}" />
            <c:set var="list" value="${requestScope.list}" />
			<c:if test="${not empty list}">
			<table width="100%" align="center" class="search-table">
				<tr>
					<th></th>
					<td align="center">
					<label for="firstName">FirstName : </label>
					<input type="text" id="firstName" name="firstName" placeholder="Enter First Name" value="${param.firstName}">
					<label for="loginid">LoginId :</label> 
					<input type="text" id="loginid" name="loginid" placeholder="Enter Login-Id" value="${param.login}">
						
					<label for="roleid">Role : </label>
					
					<select id="roleid" name="roleid">
					    <c:forEach var="item" items="${rlist}">
					        <option value="${item.key}">${item.value}</option>
					    </c:forEach>
					</select>
					
					<input type="submit" name="operation" value="<%=UserListCtl.OP_SEARCH%>">
					<input type="submit" name="operation" value="<%=UserListCtl.OP_RESET%>">
					</td>
				</tr>
			</table>
			<br>
            <table border="1" width="100%" align="center" cellpadding="6px" cellspacing=".2" class="list-table">
				<tr style="background: skyblue">
					<th><input type="checkbox" id="select_all" name="select">Select
						All</th>
				<th>S.No.</th>
				<th>FirstName</th>
				<th>LastName</th>
				<th>Role</th>
				<th>LoginId</th>
				<th>Gender</th>
				<th>Date Of Birth</th>
				<th>Mobile No</th>
				<th>Edit</th>
			</tr>
			<c:forEach var="user" items="${list}" varStatus="loop">
				<c:set var="roleId" value="${user.roleId}" />
                <c:set var="roleModel" value="${new com.rays.pro4.Model.RoleModel()}" />
                <c:set var="rolebean" value="${roleModel.findByPK(roleId)}" />


                <tr align="center">
                    <td>
                        <c:choose>
                            <c:when test="${sessionScope.userBean.id == user.id || user.roleId == 1}">
                                <input type="checkbox" class="checkbox" name="ids" value="${user.id}" disabled>
                            </c:when>
                            <c:otherwise>
                                <input type="checkbox" class="checkbox" name="ids" value="${user.id}">
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>${index + loop.index}</td>
                    <td>${user.firstName}</td>
                    <td>${user.lastName}</td>
                    <td>${rolebean.name}</td>
                    <td>${user.login}</td>
                    <td>${user.gender}</td>
                    <td>${user.dob}</td>
                    <td>${user.mobileNo}</td>
                    <td>
                        <c:choose>
                            <c:when test="${sessionScope.userBean.id == user.id || user.roleId == 1}">
                                <a href="UserCtl?id=${user.id}" onclick="return false;">Edit</a>
                            </c:when>
                            <c:otherwise>
                                <a href="UserCtl?id=${user.id}">Edit</a>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>

			</table>

			<table width="100%">
				<tr>
					<th></th>
					<c:choose>
                       <c:when test="${pageNo == 1}">
                           <td><input type="submit" name="operation" disabled="disabled" value="<%=UserListCtl.OP_PREVIOUS%>"></td>
                       </c:when>
                       <c:otherwise>
                           <td><input type="submit" name="operation" value="<%=UserListCtl.OP_PREVIOUS%>"></td>
                       </c:otherwise>
                   </c:choose>
					<td><input type="submit" name="operation" value="<%=UserListCtl.OP_DELETE%>"></td>
					<td><input type="submit" name="operation" value="<%=UserListCtl.OP_NEW%>"></td>
					<td align="right">
					  <input type="submit" name="operation" value="<%=UserListCtl.OP_NEXT%>"
					      <c:if test="${(list.size() < pageSize) || next == 0}">disabled</c:if>>
					</td>
				</tr>
			</table>
			</c:if>
			<c:if test="${list.size() == 0}">
            <td align="center"><input type="submit" name="operation" value="<%=UserListCtl.OP_BACK%>"></td>
         </c:if>

			<input type="hidden" name="pageNo" value="${pageNo}">
			 <input type="hidden" name="pageSize" value="${pageSize}">
	</form>
	</br>
	</br>
	</br>
	</br>
	</br>
	</br>
	</br>
	

	<%@include file="Footer.jsp"%>
</body>
</html>
