<%@page import="com.rays.pro4.controller.CollegeCtl"%>
<%@page import="com.rays.pro4.Util.DataUtility"%>
<%@page import="com.rays.pro4.Util.ServletUtility"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%
    boolean isUpdate = (request.getAttribute("isUpdate") != null && (Boolean) request.getAttribute("isUpdate"));
%>

<html>
<head>
    <title>College</title>
    <link rel="icon" type="image/png" href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16x16" />
    <link rel="stylesheet" type="text/css" href="<%=ORSView.APP_CONTEXT%>/css/style.css">
</head>

<body>
    <form action="CollegeCtl" method="POST">
        <%@ include file="Header.jsp"%>

        <jsp:useBean id="bean" class="com.rays.pro4.Bean.CollegeBean" scope="request"></jsp:useBean>
<div class="container">
    <div class="main-content">
        <h1 class="content-header">
            <%= isUpdate ? "Update" : "Add" %> College
        </h1>
        <div class="error-success">
            <span class="success-message">
                <%=ServletUtility.getSuccessMessage(request)%>
            </span>
            <span class="error-message">
                <%=ServletUtility.getErrorMessage(request)%>
            </span>
        </div>
        <input type="hidden" name="id" value="<%=bean.getId()%>">
        <input type="hidden" name="createdBy" value="<%=bean.getCreatedBy()%>">
        <input type="hidden" name="modifiedBy" value="<%=bean.getModifiedBy()%>">
        <input type="hidden" name="createdDatetime" value="<%=DataUtility.getTimestamp(bean.getCreatedDatetime())%>">
        <input type="hidden" name="modifiedDatetime" value="<%=DataUtility.getTimestamp(bean.getModifiedDatetime())%>">

        <div class="input-container">
            <label for="name">Name<span class="required">*</span></label>
            <input type="text" id="name" name="name" placeholder="Enter College Name" value="<%=DataUtility.getStringData(bean.getName())%>">
            <span class="error-message"> <%=ServletUtility.getErrorMessage("name", request)%></span>
        </div>
        <div class="input-container">
            <label for="address">Address<span class="required">*</span></label>
            <input type="text" id="address" name="address" placeholder="Enter Address" value="<%=DataUtility.getStringData(bean.getAddress())%>">
            <span class="error-message"> <%=ServletUtility.getErrorMessage("address", request)%></span>
        </div>
        <div class="input-container">
            <label for="state">State<span class="required">*</span></label>
            <input type="text" id="state" name="state" placeholder="Enter State" value="<%=DataUtility.getStringData(bean.getState())%>">
            <span class="error-message"> <%=ServletUtility.getErrorMessage("state", request)%></span>
        </div>
        <div class="input-container">
            <label for="city">City<span class="required">*</span></label>
            <input type="text" id="city" name="city" placeholder="Enter City" value="<%=DataUtility.getStringData(bean.getCity())%>">
            <span class="error-message"> <%=ServletUtility.getErrorMessage("city", request)%></span>
        </div>
        <div class="input-container">
            <label for="phoneNo">Phone No<span class="required">*</span></label>
            <input type="number" id="phoneNo" name="phoneNo" maxlength="10" placeholder="Enter Phone No." value="<%=DataUtility.getStringData(bean.getPhoneNo())%>">
            <span class="error-message"> <%=ServletUtility.getErrorMessage("phoneNo", request)%></span>
        </div>

        <div class="button-container">
            <input type="submit" name="operation" value="<%=isUpdate ? CollegeCtl.OP_UPDATE : CollegeCtl.OP_SAVE%>">
            <input type="submit" name="operation" value="<%=isUpdate ? CollegeCtl.OP_CANCEL : CollegeCtl.OP_RESET%>">
        </div>
    </div>
		</div>
	</form>

	<%@ include file="Footer.jsp"%>
</body>
</html>