<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="com.rays.pro4.Util.HTMLUtility"%>
<%@page import="com.rays.pro4.Util.DataUtility"%>
<%@page import="com.rays.pro4.Util.ServletUtility"%>
<%@page import="com.rays.pro4.controller.UserCtl"%>
<%@page import="com.rays.pro4.controller.ORSView"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<html>
    <head>
        <c:set var="ctx" value="${pageContext.request.contextPath}" />
        <link rel="icon" type="image/png" href="${ctx}/img/logo.png"
              sizes="16*16" />
        <title>User</title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet"
              href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
        <link rel="stylesheet" href="${ctx}/css/style.css">
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
                <c:if test="${not empty requestScope.error}">
                    <div class="alert alert-danger" role="alert">${requestScope.error}</div>
                </c:if>
                <c:if test="${not empty requestScope.success}">
                    <div class="alert alert-success" role="alert">${requestScope.success}</div>
                </c:if>
            </div>
            <form action="${ctx}/UserCtl" method="post">
                <%
                    List l = (List) request.getAttribute("roleList");
                %>
                <input type="hidden" name="id" value="<%=bean.getId()%>">
                <input type="hidden" name="createdBy" value="<%=bean.getCreatedBy()%>">
                <input type="hidden" name="modifiedBy"
                       value="<%=bean.getModifiedBy()%>">
                <input type="hidden"
                       name="createdDatetime"
                       value="<%=DataUtility.getTimestamp(bean.getCreatedDatetime())%>">
                <input type="hidden" name="modifiedDatetime"
                       value="<%=DataUtility.getTimestamp(bean.getModifiedDatetime())%>">
                <table class="table table-borderless w-50 mx-auto">
                    <tr>
                        <th align="left"><label for="firstName">First Name <span
                                class="required">*</span> :</label></th>
                        <td><input type="text" name="firstName" id="firstName"
                                   placeholder="Enter First Name" class="form-control"
                                   value="<%=DataUtility.getStringData(bean.getFirstName())%>">
                            <div class="error-message">
                                <%=ServletUtility.getErrorMessage("firstName", request)%></div></td>
                    </tr>
                    <tr>
                        <th align="left"><label for="lastName">Last Name <span
                                class="required">*</span> :</label></th>
                        <td><input type="text" name="lastName" id="lastName"
                                   placeholder="Enter Last Name" class="form-control"
                                   value="<%=DataUtility.getStringData(bean.getLastName())%>">
                            <div class="error-message">
                                <%=ServletUtility.getErrorMessage("lastName", request)%></div></td>
                    </tr>
                    <tr>
                        <th align="left"><label for="login">LoginId <span
                                class="required">*</span> :</label></th>
                        <td><input type="text" name="login" id="login"
                                   placeholder="Enter EmailId" class="form-control"
                                   value="<%=DataUtility.getStringData(bean.getLogin())%>"
                                <%=(bean.getId() > 0) ? "readonly" : ""%>>
                            <div class="error-message">
                                <%=ServletUtility.getErrorMessage("login", request)%></div></td>
                    </tr>
                    <c:if test="<%=bean.getId() == 0%>">
                        <tr>
                            <th align="left"><label for="password">Password <span
                                    class="required">*</span> :</label></th>
                            <td><input type="password" name="password" id="password"
                                       placeholder="Enter Password" class="form-control">
                                <div class="error-message">
                                    <%=ServletUtility.getErrorMessage("password", request)%></div></td>
                        </tr>
                        <tr>
                            <th align="left"><label for="confirmPassword">Confirm
                                    Password <span class="required">*</span> :</label></th>
                            <td><input type="password" name="confirmPassword"
                                       id="confirmPassword" placeholder="Re-Enter Password"
                                       class="form-control">
                                <div class="error-message">
                                    <%=ServletUtility.getErrorMessage("confirmPassword", request)%></div></td>
                        </tr>
                    </c:if>
                    <tr>
                        <th align="left"><label for="gender">Gender <span
                                class="required">*</span> :</label></th>
                        <td>
                            <%
                                HashMap map = new HashMap();
                                map.put("Male", "Male");
                                map.put("Female", "Female");

                                String hlist = HTMLUtility.getList("gender", String.valueOf(bean.getGender()), map);
                            %> <%=hlist%>
                            <div class="error-message">
                                <%=ServletUtility.getErrorMessage("gender", request)%></div></td>
                    </tr>
                    <tr>
                        <th align="left"><label for="roleId">Role <span
                                class="required">*</span> :</label></th>
                        <td><%=HTMLUtility.getList("roleId", String.valueOf(bean.getRoleId()), l)%>
                            <div class="error-message">
                                <%=ServletUtility.getErrorMessage("roleId", request)%></div></td>
                    </tr>
                    <tr>
                        <th align="left"><label for="dob">Date Of Birth <span
                                class="required">*</span> :</label></th>
                        <td><input type="text" name="dob" id="dob"
                                   placeholder="Enter Date Of Birth" readonly="readonly"
                                   class="form-control"
                                   value="<%=DataUtility.getDateString(bean.getDob())%>">
                            <div class="error-message">
                                <%=ServletUtility.getErrorMessage("dob", request)%></div></td>
                    </tr>
                    <tr>
                        <th align="left"><label for="mobileNo">Mobile No <span
                                class="required">*</span> :</label></th>
                        <td><input type="number" name="mobileNo" id="mobileNo"
                                   maxlength="10" placeholder="Enter Mobile No" class="form-control"
                                   value="<%=DataUtility.getStringData(bean.getMobileNo())%>">
                            <div class="error-message">
                                <%=ServletUtility.getErrorMessage("mobileNo", request)%></div></td>
                    </tr>
                    <tr>
                        <th></th>
                        <td>
                            <div class="button-container">
                                <c:choose>
                                    <c:when test="<%=bean.getId() > 0%>">
                                        <input type="submit" name="operation"
                                               value="<%=UserCtl.OP_UPDATE%>" class="btn btn-primary">
                                        <input type="submit" name="operation"
                                               value="<%=UserCtl.OP_CANCEL%>" class="btn btn-secondary">
                                    </c:when>
                                    <c:otherwise>
                                        <input type="submit" name="operation"
                                               value="<%=UserCtl.OP_SAVE%>" class="btn btn-primary">
                                        <input type="submit" name="operation"
                                               value="<%=UserCtl.OP_RESET%>" class="btn btn-secondary">
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
        <%@ include file="Footer.jsp"%>
    </body>
</html>