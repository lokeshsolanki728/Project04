<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="com.rays.pro4.controller.ORSView" %>
<%@page import="com.rays.pro4.controller.MarksheetCtl" %>
<%@page import="java.util.List" %>
<%@page import="com.rays.pro4.Util.HTMLUtility" %>
<%@page import="com.rays.pro4.Util.DataUtility" %>
<%@page import="com.rays.pro4.Util.ServletUtility" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@page import="com.rays.pro4.Bean.MarksheetBean"%>
<%@page import="com.rays.pro4.Bean.StudentBean"%>
<!DOCTYPE html>
<html>
<head>
    <link rel="icon" type="image/png"
          href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16*16"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <title>Marksheet Register</title>
</head>
<body>
    

<jsp:useBean id="bean" class="com.rays.pro4.Bean.MarksheetBean"
             scope="request"></jsp:useBean>
<%@ include file="Header.jsp" %>
<form action="<%=ORSView.MARKSHEET_CTL%>" method="post" class="container">
    <c:set var="studentList" value="${requestScope.studentList}"/>

    <div class="text-center">
        <h1>
            <c:choose>
                <c:when test="${not empty bean.id}">Update Marksheet</c:when>
                <c:otherwise>Add Marksheet</c:otherwise>
            </c:choose>
        </h1>

        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger" role="alert">${errorMessage}</div>
        </c:if>
        <c:if test="${not empty successMessage}">
            <div class="alert alert-success" role="alert">${successMessage}</div>
        </c:if>
    </div>
    <div>
        <input type="hidden" name="id" value="${bean.id}">
        <input type="hidden" name="createdBy" value="${bean.createdBy}">
        <input type="hidden" name="modifiedBy" value="${bean.modifiedBy}">
        <input type="hidden" name="createdDatetime" value="${bean.createdDatetime}">
        <input type="hidden" name="modifiedDatetime" value="${bean.modifiedDatetime}">
        <table>
            <tr>
                <th class="text-left"><label for="rollNo">Roll No <span class="required">*</span> :</label>
                </th>
                <td><input type="text" id="rollNo" name="rollNo" placeholder="Enter Roll No" class="form-control"
                           value="<%=DataUtility.getStringData(request.getParameter("rollNo")) != null ? DataUtility.getStringData(request.getParameter("rollNo")) : ""%>"></td>
                <td>
                    <span class="error-message">${requestScope.rollNo}</span>
                </td>
            </tr>

             <tr>
                <th class="text-left"><label for="studentld">Name <span class="required">*</span> :</label>
                </th>
                <td>${HTMLUtility.getList("studentld", bean.studentld, studentList)}</td>
                <td>
                    <span class="error-message">${requestScope.studentId}</span>
                </td>
            </tr>

            <tr>
                <th class="text-left"><label for="physics">Physics<span class="required">*</span> :</label>
                </th>
                 <td><input type="number" id="physics" name="physics" maxlength="3"
                           placeholder="Enter Physics" class="form-control"
                           value="<%=DataUtility.getStringData(request.getParameter("physics")) != null ? DataUtility.getStringData(request.getParameter("physics")) : ""%>"></td>
                <td>
                    <span class="error-message">${requestScope.physics}</span>
                </td>
            </tr>

            <tr>
                <th class="text-left"><label for="chemistry">Chemistry<span class="required">*</span> :</label>
                </th>
                 <td><input type="number" id="chemistry" name="chemistry" maxlength="3"
                           placeholder="Enter Chemistry" class="form-control"
                           value="<%=DataUtility.getStringData(request.getParameter("chemistry")) != null ? DataUtility.getStringData(request.getParameter("chemistry")) : ""%>"></td>
                <td>
                    <span class="error-message">${requestScope.chemistry}</span>
                </td>
            </tr>

            <tr>
                <th class="text-left"><label for="maths">Maths <span class="required">*</span> :</label>
                </th>
                 <td><input type="number" id="maths" name="maths" maxlength="3"
                           placeholder="Enter Maths" class="form-control"
                           value="<%=DataUtility.getStringData(request.getParameter("maths")) != null ? DataUtility.getStringData(request.getParameter("maths")) : ""%>"></td>
                <td>
                    <span class="error-message">${requestScope.maths}</span>
                </td>
            </tr>

            <tr>
                <th></th>
                <td colspan="2">
                    <c:choose>
                        <c:when test="${not empty bean.id}">
                            <input type="submit" name="operation" value="<%=MarksheetCtl.OP_UPDATE%>">
                            <input type="submit" name="operation" value="<%=MarksheetCtl.OP_CANCEL%>">
                        </c:when>
                        <c:otherwise>
                             <input type="submit" name="operation" value="<%=MarksheetCtl.OP_SAVE%>">
                             <input type="submit" name="operation" value="<%=MarksheetCtl.OP_RESET%>">
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </table>
    </div>
</form>

<%@include file="Footer.jsp" %>
</body>
</html>