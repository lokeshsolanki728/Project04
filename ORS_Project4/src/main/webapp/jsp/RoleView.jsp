<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="com.rays.pro4.controller.RoleCtl"%>
<%@page import="com.rays.pro4.Bean.RoleBean"%>
<%@page import="com.rays.pro4.controller.ORSView"%>
<%@page import="com.rays.pro4.Util.ServletUtility"%>
<%@page import="com.rays.pro4.Util.DataUtility"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
        <meta charset="ISO-8859-1">

        <c:set var="ctx" value="${pageContext.request.contextPath}"/>
        <link rel="icon" type="image/png" href="${ctx}/img/logo.png" sizes="16x16"/>
        <title>Role</title>
        <link rel="stylesheet" href="${ctx}/css/style.css">
    </head>
<body>
	<jsp:useBean id="bean" class="com.rays.pro4.Bean.RoleBean"
		scope="request" />
        <form action="<%=ORSView.ROLE_CTL%>" method="post">
            <%@ include file="Header.jsp" %>

            <div class="container">
                <c:choose>
                    <c:when test="${not empty bean.id}">
                        <h1 class="text-center">Update Role</h1>
                    </c:when>
                    <c:otherwise>
                        <h1 class="text-center">Add Role</h1>
                    </c:otherwise>
                </c:choose>
                <div class="message-container">
                    <c:if test="${not empty requestScope.success}">

                        <div class="alert alert-success" role="alert">${requestScope.success}</div>
                    </c:if>
                    <c:if test="${not empty requestScope.error}">
                        <div class="alert alert-danger" role="alert">${requestScope.error}</div>
                    </c:if>
                </div>
                <input type="hidden" name="id" value="${bean.id}">

                <input type="hidden" name="createdBy" value="${bean.createdBy}">
                <input type="hidden" name="modifiedBy" value="${bean.modifiedBy}">
                <input type="hidden" name="createdDatetime" value="${bean.createdDatetime}">
                <input type="hidden" name="modifiedDatetime" value="${bean.modifiedDatetime}">

                <table class="table table-borderless w-50 mx-auto">
                    <tr>
                        <th class="text-left">
                            <label for="name">Name <span class="required">*</span> :</label>
                        </th>
                        <td>
                            <input type="text" id="name" name="name" placeholder="Enter Role Name"
                                class="form-control" value="<%=DataUtility.getStringData(bean.getName())%>">
                            <font style="position: fixed" color="red">${requestScope.name}</font>
                        </td>

                    </tr>
                    <tr>
                        <th class="text-left">
                            <label for="description">Description <span class="required">*</span> :</label>
                        </th>
                        <td>
                            <input type="text" id="description" name="description"
                                placeholder="Enter Description" class="form-control"
                                value="<%=DataUtility.getStringData(bean.getDescription())%>"> <font
                                style="position: fixed" color="red">${requestScope.description}</font>
                        </td>
                    </tr>
                    <tr>
                        <th></th>
                        <td>
                            <div class="button-container">
                                <input type="submit" name="operation" class="btn btn-primary" value="<c:out value='${empty bean.id ? RoleCtl.OP_SAVE : RoleCtl.OP_UPDATE}'/>"/>
                                <input type="submit" name="operation" class="btn btn-secondary" value="<%=RoleCtl.OP_RESET%>"/>
                                <input type="submit" name="operation" class="btn btn-secondary" value="<%=RoleCtl.OP_CANCEL%>"/>
                                <c:if test="${bean.id > 0}">
                                    <input type="submit" name="operation" class="btn btn-danger"
                                           value="<%=RoleCtl.OP_DELETE%>">
                                </c:if>
                            </div>
                                </td>
                    </tr>
                </table>
            </div>
        </form>
        <%@ include file="Footer.jsp" %>
    </body>
</html>
