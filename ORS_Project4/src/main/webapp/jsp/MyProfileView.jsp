
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@page import="com.rays.pro4.controller.MyProfileCtl" %>
<%@page import="com.rays.pro4.controller.ORSView" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<!DOCTYPE html>
<html>
<head>
    <c:set var="ctx" value="${pageContext.request.contextPath}"/>
    <link rel="icon" type="image/png" href="${ctx}/img/logo.png" sizes="16*16"/>
    <title>My Profile</title>
    <link rel="stylesheet" href="${ctx}/css/style.css">
    <meta charset="utf-8">
</head>
<body>
    <form action="${ctx}${ORSView.MY_PROFILE_CTL}" method="post">
        <%@ include file="Header.jsp" %>
        <jsp:useBean id="bean" class="com.rays.pro4.Bean.UserBean" scope="request"></jsp:useBean>
        <div class="container">
            <h1 class="text-center">My Profile</h1>
            <div class="message-container">
                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-danger" role="alert">${errorMessage}</div>
                </c:if>
                <c:if test="${not empty successMessage}">
                    <div class="alert alert-success" role="alert">${successMessage}</div>
                </c:if>
            </div>
            <div class="hidden-inputs">
                <input type="hidden" name="id" value="${bean.id}">
                <input type="hidden" name="createdBy" value="${bean.createdBy}">
                <input type="hidden" name="modifiedBy" value="${bean.modifiedBy}">
                <input type="hidden" name="createdDatetime" value="${bean.createdDatetime}">
                <input type="hidden" name="modifiedDatetime" value="${bean.modifiedDatetime}">
            </div>
            <table class="table table-borderless w-50">
                <tr>
                    <th align="left">
                        <label for="login">Login Id <span class="required">*</span></label>
                    </th>
                    <td>
                        <input type="text" id="login" name="login" class="form-control"
                               value="${bean.login}" readonly="readonly">
                        <div class="error">${requestScope.login}</div>
                    </td>
                </tr>
                <tr>
                    <th align="left">
                        <label for="firstName">First Name <span class="required">*</span></label>
                    </th>
                    <td>
                        <input type="text" id="firstName" name="firstName" class="form-control"
                               value="${bean.firstName}">
                        <div class="error">${requestScope.firstName}</div>
                    </td>
                </tr>
                <tr>
                    <th align="left">
                        <label for="lastName">Last Name <span class="required">*</span></label>
                    </th>
                    <td>
                        <input type="text" id="lastName" name="lastName" class="form-control"
                               value="${bean.lastName}">
                        <div class="error">${requestScope.lastName}</div>
                    </td>
                </tr>
                <tr>
                    <th align="left">
                        <label for="gender">Gender</label>
                    </th>
                    <td>
                        <input type="text" name="gender" id="gender" class="form-control" value="${bean.gender}">
                    </td>
                </tr>
                <tr>
                    <th align="left">
                        <label for="mobileNo">Mobile No <span class="required">*</span></label>
                    </th>
                    <td>
                        <input type="text" id="mobileNo" name="mobileNo" class="form-control"
                               value="${bean.mobileNo}">
                        <div class="error">${requestScope.mobileNo}</div>
                    </td>
                </tr>
                <tr>
                    <th align="left">
                        <label for="dob">Date Of Birth (mm/dd/yyyy)</label>
                    </th>
                    <td>
                        <input type="text" id="dob" name="dob" readonly="readonly" class="form-control"
                               value="${bean.dob}">
                        <div class="error">${requestScope.dob}</div>
                    </td>
                </tr>
                <tr>
                    <th></th>
                    <td>
                        <div class="button-container">
                            <input type="submit" name="operation" class="btn btn-secondary"
                                   value="<%=MyProfileCtl.OP_CHANGE_MY_PASSWORD%>">
                            <input type="submit" name="operation" class="btn btn-primary"
                                   value="<%=MyProfileCtl.OP_SAVE%>">
                        </div>
                    </td>
                </tr>
            </table>
        </div>
    </form>
    <%@ include file="Footer.jsp" %>
</body>
</html>