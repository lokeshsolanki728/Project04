<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="com.rays.pro4.controller.ORSView" %>
<%@page import="com.rays.pro4.controller.CollegeCtl" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>

<!DOCTYPE html>
<html>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<head>
    <link rel="icon" type="image/png" href="${ctx}/img/logo.png" sizes="16x16"/>
    <title>
        <c:choose>
            <c:when test="${not empty bean.id}">Update College</c:when>
            <c:otherwise>Add College</c:otherwise>
        </c:choose>
    </title>
    <meta charset="utf-8">
    <link rel="stylesheet" href="${ctx}/resources/demos/style.css">
    <link rel="stylesheet" href="${ctx}/css/style.css">
</head>

<body>
<form action="${ctx}${ORSView.COLLEGE_CTL}" method="POST">
    <%@ include file="Header.jsp" %>

    <jsp:useBean id="bean" class="com.rays.pro4.Bean.CollegeBean" scope="request"></jsp:useBean>
    <div class="container">
        <h1 >
            <c:choose>
                <c:when test="${not empty bean.id}">Update College</c:when>
                <c:otherwise>Add College</c:otherwise>
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
        <input type="hidden" name="id" value="${bean.id}">
        <input type="hidden" name="createdBy" value="${bean.createdBy}">
        <input type="hidden" name="modifiedBy" value="${bean.modifiedBy}">
        <input type="hidden" name="createdDatetime" value="${bean.createdDatetime}">
        <input type="hidden" name="modifiedDatetime" value="${bean.modifiedDatetime}">

        <div class="input-container">
            <label for="name">Name<span class="required">*</span></label>
            <input type="text" id="name" name="name" placeholder="Enter College Name" class="form-control" value="${bean.name}">
            <div class="error">${requestScope.name}</div>
        </div>
				<div class="input-container">
					<label for="address">Address<span class="required">*</span></label>
					<input type="text" id="address" name="address" class="form-control"
						placeholder="Enter Address" value="${bean.address}">
					<div class="error">${requestScope.address}</div>
				</div>
				<div class="input-container">
					<label for="state">State<span class="required">*</span></label> <input
						type="text" id="state" name="state" class="form-control"
						placeholder="Enter State" value="${bean.state}">
					<div class="error">${requestScope.state}</div>
				</div>
				<div class="input-container">
					<label for="city">City<span class="required">*</span></label> <input
						type="text" id="city" name="city" class="form-control" placeholder="Enter City"
						value="${bean.city}">
					<div class="error">${requestScope.city}</div>
				</div>
				<div class="input-container">
					<label for="phoneNo">Phone No<span class="required">*</span></label>
					<input type="number" id="phoneNo" name="phoneNo" class="form-control"
						maxlength="10" placeholder="Enter Phone No." value="${bean.phoneNo}">
					<div class="error">${requestScope.phoneNo}</div>
				</div>

				<div class="button-container" >
					<input type="submit" name="operation"
						value="<c:out value="${empty bean.id ? CollegeCtl.OP_SAVE : CollegeCtl.OP_UPDATE}" />">
					<input type="submit" name="operation"
						value="<c:out value="${empty bean.id ? CollegeCtl.OP_RESET : CollegeCtl.OP_CANCEL}" />">
                         <c:if test="${not empty bean.id}">
                         <input type="submit" name="operation" value="<c:out value="${CollegeCtl.OP_DELETE}" />">
                        </c:if>
				</div>
			</div>
		</div>
	</form>

    <%@ include file="Footer.jsp" %>
</body>

</html>