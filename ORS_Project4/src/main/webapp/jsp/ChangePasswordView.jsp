<%@page import="com.rays.pro4.controller.ChangePasswordCtl"%>
<%@page import="com.rays.pro4.Util.DataUtility"%>
<%@page import="com.rays.pro4.Util.ServletUtility"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
pageEncoding="ISO-8859-1"%>
<html>
<head>
    <title>Change Password</title>
    <link rel="icon" type="image/png" href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16x16" />    
    <link rel="stylesheet" href="<%=ORSView.APP_CONTEXT%>/css/style.css">
</head>
<body>
	<form action="<%=ORSView.CHANGE_PASSWORD_CTL%>" method="post">

		<%@ include file="Header.jsp"%>
	<jsp:useBean id="bean" class="com.rays.pro4.Bean.UserBean"
			scope="request" />
		<div class="bodyDiv">
            <h1 class="title">Change Password</h1>
            <div class="message">
                <%
                    String successMsg = ServletUtility.getSuccessMessage(request);
                    if (successMsg != null && !successMsg.isEmpty()) {
                %>
                <p class="successMessage"><%=successMsg%></p>
                <%
                    }
                %>
                <%
                    String errorMsg = ServletUtility.getErrorMessage(request);
                    if (errorMsg != null && !errorMsg.isEmpty()) {
                %>
                <p class="errorMessage"><%=errorMsg%></p>
                <%
                    }
                %>
            </div>
            <div class="inputDiv">                
                <input type="hidden" name="id" value="<%=bean.getId()%>">
                <input type="hidden" name="createdBy" value="<%=bean.getCreatedBy()%>">
                <input type="hidden" name="modifiedBy" value="<%=bean.getModifiedBy()%>">
                <input type="hidden" name="createdDatetime" value="<%=DataUtility.getTimestamp(bean.getCreatedDatetime())%>">
                <input type="hidden" name="modifiedDatetime" value="<%=DataUtility.getTimestamp(bean.getModifiedDatetime())%>">
            </div>
			<div class="inputDiv">
                <label class="label">Old Password<span class="required">*</span></label>
                <input type="password" name="oldPassword" placeholder="Enter Old Password" value="<%=DataUtility.getString(request.getParameter("oldPassword") == null ? "" : request.getParameter("oldPassword"))%>">
                <p class="errorMessage fixed"> <%=ServletUtility.getErrorMessage("oldPassword", request)%></p>
            </div>
            <div class="inputDiv">
                <label class="label">New Password<span class="required">*</span></label>
                <input type="password" name="newPassword" placeholder="Enter New Password" value="<%=DataUtility.getString(request.getParameter("newPassword") == null ? "" : request.getParameter("newPassword"))%>">
                <p class="errorMessage fixed"><%=ServletUtility.getErrorMessage("newPassword", request)%></p>
            </div>
            <div class="inputDiv">
                <label class="label">Confirm Password<span class="required">*</span></label>
                <input type="password" name="confirmPassword" placeholder="Enter Confirm Password" value="<%=DataUtility.getString(request.getParameter("confirmPassword") == null ? "" : request.getParameter("confirmPassword"))%>">
                <p class="errorMessage fixed"><%=ServletUtility.getErrorMessage("confirmPassword", request)%></p>
            </div>
            <div class="buttonDiv">                
                <input type="submit" name="operation" value="<%=ChangePasswordCtl.OP_SAVE%>">
                &nbsp; <input type="submit" name="operation" value="<%=ChangePasswordCtl.OP_CHANGE_MY_PROFILE%>">            
            </div>
        </div>        
	</form>
	<%@ include file="Footer.jsp"%>    
</body>
</html>