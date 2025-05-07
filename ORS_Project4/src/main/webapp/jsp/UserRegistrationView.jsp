<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@page import="com.rays.pro4.controller.UserRegistrationCtl"%>
<%@page import="com.rays.pro4.controller.ORSView"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="com.rays.pro4.Bean.UserBean"%>

<!DOCTYPE html>
<html>
    <head>
        <link rel="icon" type="image/png" href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16*16"/>
        <title>User Registration</title>

        <meta charset="utf-8">

        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/demos/style.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/jquery-ui.min.css"/>
        <script type="text/javascript" src="${pageContext.request.contextPath}/resources/jquery-3.5.1.min.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/resources/jquery-ui.min.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/UserRegistration.js"></script>
    </head>
    <body>

        <jsp:useBean id="bean" class="com.rays.pro4.Bean.UserBean"
			scope="request"></jsp:useBean>
        <%@include file="Header.jsp"%>

        <form action="<%=ORSView.USER_REGISTRATION_CTL%>" method="post">

            <div class="text-center">
                <h1>
                    <font color="red">
                    	<c:out value="${error}" />
                    </font>
                    <c:choose>
                        <c:when test="${not empty bean.id}">Update User</c:when>
                        <c:otherwise>Add User</c:otherwise>
                    </c:choose>
                </h1>

                <c:if test="${not empty successMessage}">
                    <div class="alert alert-success" role="alert">${successMessage}</div>
                </c:if>
                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-danger" role="alert">${errorMessage}</div>
                </c:if>
            </div>

            <div class="container">
                <table>
                    <tr>
                        <th class="text-left"><label for="firstName">First Name <span class="required">*</span>:</label></th>
                        <td>
                            <input type="text" id="firstName" name="firstName"
								placeholder="Enter First Name" class="form-control"
								value="${bean.firstName}">
                        </td>
                        <td>
                            <span class="error-message">${requestScope.firstName}</span>
                        </td>                       
                    </tr>

                    <tr>
                        <th align="left"><label for="lastName">Last Name <span class="required">*</span>:</label></th>
                        <td>
                            <input type="text" id="lastName" name="lastName"
								placeholder="Enter last Name" class="form-control"
								value="${bean.lastName}">
                        </td>
                        <td>
                            <span class="error-message">${requestScope.lastName}</span>
                        </td>
                    </tr>

                    <tr>
                        <th align="left"><label for="login">Login Id <span class="required">*</span>:</label></th>
                        <td>
                            <input type="text" id="login" name="login"
								placeholder="Enter valid Email-Id" class="form-control"
								value="${bean.login}">
                        </td>
                        <td>
                            <span class="error-message">${requestScope.login}</span>
                        </td>
                    </tr>

                    <tr>  
                        <th align="left"><label for="gender">Gender <span class="required">*</span>:</label></th>
                        <td>
                            <select id="gender" name="gender" class="form-control">
                                <option value="">Select Gender</option>
                                <option value="Male"
                                    <c:if test="${bean.gender == 'Male'}"> selected </c:if>>Male</option>
                                <option value="Female"
                                    <c:if test="${bean.gender == 'Female'}"> selected </c:if>>Female</option>
                            </select>
                        </td>
                        <td>

                            <span class="error-message">${requestScope.gender}</span>
                        </td>                      
                    </tr>

                    <tr>
                        <th class="text-left"><label for="udate">Date Of Birth <span class="required">*</span>:</label></th>
                        <td>
                            <input type="text" name="dob" id="udate" class="form-control"
								placeholder="Enter Dob " value="${bean.dob}"
								readonly="readonly">
                            <span class="error-message">${requestScope.dob}</span>
                        </td>                      
                       
                    </tr>                 

                    <tr>
                        <th class="text-left"><label for="mobileNo">Mobile No <span class="required">*</span>:</label></th>
                        <td>
                            <input type="text" name="mobileNo" id="mobileNo"
								placeholder="Enter Mobile No" class="form-control"
								maxlength="10" value="${bean.mobileNo}"> <span
								class="error-message">${requestScope.mobileNo}</span>
                        </td>
                    </tr>

                    <tr>
                        <th class="text-left"><label for="password">Password <span class="required">*</span>:</label></th>
                        <td>
                            <input type="password" name="password" id="password" placeholder="Enter Password"
								class="form-control" value="${bean.password}">
						</td>

                        <td>
                            <div class="error-message">${requestScope.password}</div>
                        </td>
                    </tr>

                    <tr>                      
                        <th class="text-left"><label for="confirmPassword">Confirm Password <span class="required">*</span>:</label></th>
                        <td>
                            <input type="password" name="confirmPassword" id="confirmPassword"
								placeholder="Re-Enter password" class="form-control"
								value="${bean.confirmPassword}">
						</td>
                        <td>
                           <span class="error-message">${requestScope.confirmPassword}</span>
                        </td>
                    </tr>
                    <tr>
                        <th></th>
                        <td colspan="2"> &nbsp; &emsp;
                            <input type="submit" name="operation"
								value="<%=UserRegistrationCtl.OP_SIGN_UP%>"
								class="btn btn-primary"> &nbsp; <input type="submit"
								name="operation" value="<%=UserRegistrationCtl.OP_RESET%>">
                            &nbsp;
                        </td>
                    </tr>
                </table>
            </div>
        </form>

        <%@ include file="Footer.jsp"%>

    </body>
</html>