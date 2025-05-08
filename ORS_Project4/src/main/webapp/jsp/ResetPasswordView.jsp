<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@page import="com.rays.pro4.DTO.UserDTO"%>
<%@page import="com.rays.pro4.Util.ServletUtility"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Reset Password</title>
</head>
<body>

    <div align="center">
    <h1>Reset Password</h1>

        <font color="red"><%= ServletUtility.getErrorMessage(request) %></font>
         <c:if test="${not empty successMessage}">
            <font color="green">${successMessage}</font>
          </c:if>
       <c:if test="${not empty error}">
            <font color="red">${error}</font>
        </c:if>

        <%
            // Retrieve the token from the request attribute (set in the controller's doGet)
            UserDTO user = (UserDTO) request.getAttribute("user");
            String token = (String) request.getAttribute("token"); //getting token from request attribute

             if (token==null || token.trim().isEmpty()) {
               token = request.getParameter("token");
           }
        %>

        <form action="<%= request.getContextPath() %>/resetPassword" method="post">

            <input type="hidden" name="token" value="<%= (token != null) ? token : "" %>">

            <div>
                <label for="newPassword">New Password:</label>
                 <input type="password" id="newPassword" name="newPassword" value="${newPassword}" required>
                  <c:if test="${not empty errorPassword}">
                    <font color="red">${errorPassword}</font>
                  </c:if>
            </div>
            <br>
            <div>
                <label for="confirmPassword">Confirm Password:</label>
                <input type="password" id="confirmPassword" name="confirmPassword" required>
                   <c:if test="${not empty errorConfirmPassword}">
                    <font color="red">${errorConfirmPassword}</font>
                  </c:if>
            </div>
            <br>
            <div>
                <input type="submit" value="Reset Password">
            </div>

        </form>
</div>

</body>
</html>