<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@page import="com.rays.pro4.Util.HTMLUtility"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<html>

<head>
        <c:set var="ctx" value="${pageContext.request.contextPath}" />
        <link rel="icon" type="image/png" href="${ctx}/img/logo.png"
              sizes="16*16" />
        <title>User</title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet"
            href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css"> <link rel="stylesheet" href="${ctx}/css/style.css">
        <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
        <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
        <script>
            $(function() {
                $("#dob").datepicker({
                    changeMonth : true,
                    changeYear : true,
                    yearRange : '1980:2002',
                });
            });
        </script>
</head>

<body>
        <jsp:useBean id="bean" class="com.rays.pro4.Bean.UserBean"
                     scope="request"></jsp:useBean>
        <%@ include file="Header.jsp"%>
      <div class="container">
        <c:choose>
                <c:when test="${not empty bean.id}">
                    <h1 class="text-center">Update User</h1>
                </c:when>
                <c:otherwise>
                    <h1 class="text-center">Add User</h1>
                </c:otherwise>
            </c:choose>
            <div class="message-container">
                 <c:if test="${not empty error}">
                    <div class="alert alert-danger" role="alert">${error}</div>
                </c:if>
                 <c:if test="${not empty errorMessage}">
                    <div class="alert alert-danger" role="alert">${errorMessage}</div>
                </c:if>
                <c:if test="${not empty successMessage}">
                 <div class="alert alert-success" role="alert">
                 ${successMessage}</div>
               </c:if>
               
            </div>
            <form action="${ctx}/UserCtl" method="post"> <input type="hidden" name="id" value="${bean.id}"> <input type="hidden" name="createdBy" value="${bean.createdBy}"> <input type="hidden" name="modifiedBy" value="${bean.modifiedBy}"> <input type="hidden" name="createdDatetime" value="${bean.createdDatetime}"> <input type="hidden" name="modifiedDatetime" value="${bean.modifiedDatetime}"><c:set var="errors" value="${requestScope.errors}" />
                <table class="table table-borderless w-50 mx-auto">
                  <tr>
                    <th style="width: 20%"><label for="firstName">First Name <span class="required">*</span> :</label></th>
                    <td><input type="text" name="firstName" id="firstName"
                               placeholder="Enter First Name" class="form-control" value="${bean.firstName}">
                        <span class="error-message">${errors.firstName}</span>
                    </td>
                  </tr>
                  <tr>
                      <th style="width: 20%"><label for="lastName">Last Name <span class="required">*</span> :</label></th><td><input type="text" name="lastName" id="lastName" placeholder="Enter Last Name"
                      class="form-control" value="${bean.lastName}"> <span class="error-message">${errors.lastName}</span>
                    </td>
                  </tr>
                  <tr>
                    <th style="width: 20%"><label for="login">LoginId <span class="required">*</span> :</label></th>
                    <td><input type="text" name="login" id="login" placeholder="Enter EmailId" class="form-control"
                      value="${bean.login}" <c:if test="${bean.id > 0}">readonly</c:if>> <span class="error-message">
                    ${errors.login}</span></td>
                  </tr>
                  <c:if test="${bean.id == 0}">
                    <tr>
                      <th style="width: 20%"><label for="password">Password <span class="required">*</span> :</label></th>
                      <td><input type="password" name="password" id="password" placeholder="Enter Password"
                        class="form-control"> <span class="error-message">${errors.password}</span></td>
                    </tr>
                    <tr>
                      <th style="width: 20%"><label for="confirmPassword">Confirm Password <span
                          class="required">*</span> :</label></th>
                      <td><input type="password" name="confirmPassword" id="confirmPassword"
                        placeholder="Re-Enter Password" class="form-control"> <span
                        class="error-message">${requestScope.confirmPassword}</span></td>
                     </tr>
                  </c:if>
                  <tr>
                    <th style="width: 20%"><label for="gender">Gender <span class="required">*</span> :</label></th>
                    <td>
                     <c:set var="genderMap" value="${requestScope.genderMap}" />
                        ${HTMLUtility.getList("gender", bean.gender, genderMap)} <span
                            class="error-message">${errors.gender}</span>
                    </td>                  
                  </tr>
                  <tr>
                    <th style="width: 20%"><label for="roleId">Role <span class="required">*</span> :</label></th>
                    <td>${HTMLUtility.getList("roleId", bean.roleId, requestScope.roleList)} <span
                      class="error-message">${errors.roleId}</span></td>
                  </tr>
                    <tr>
                        <th style="width: 20%"><label for="dob">Date Of Birth <span class="required">*</span> :</label></th>
                        <td><input type="text" name="dob" id="dob" placeholder="Enter Date Of Birth" readonly="readonly" class="form-control" value="${bean.dob}">
                            <div class="error-message">${errors.dob}</div>                            
                        </td>
                    </tr>
                    <tr>
                        <th style="width: 20%"><label for="mobileNo">Mobile No <span class="required">*</span> :</label></th>
                        <td><input type="text" name="mobileNo" id="mobileNo" maxlength="10" placeholder="Enter Mobile No" class="form-control" value="${bean.mobileNo}">
                           <div class="error-message">${errors.mobileNo}</div>
                        </td>
                    </tr>
                   <tr>
                      <th></th>
                        <td>
                            <div class="button-container">
                                <c:choose>
                                    <c:when test="${bean.id > 0}">
                                        <input type="submit" name="operation" value="Update" class="btn btn-primary">
                                        <input type="submit" name="operation" value="Cancel" class="btn btn-secondary">
                                    </c:when>
                                    <c:otherwise>
                                        <input type="submit" name="operation" value="Save" class="btn btn-primary">
                                        <input type="submit" name="operation" value="Reset" class="btn btn-secondary">
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </td>
                  </tr>
                </table>
            </form> </div>
        <%@ include file="Footer.jsp"%> </body>

</html>