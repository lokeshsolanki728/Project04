<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.rays.pro4.Bean.UserBean" %>
<%@ page import="com.rays.pro4.Bean.RoleBean" %>
<%@ page import="com.rays.pro4.controller.LoginCtl" %>
<%@ page import="com.rays.pro4.controller.ORSView" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ page import="com.rays.pro4.Util.PropertyReader" %>
<html>
    <c:set var="ctx" value="${pageContext.request.contextPath}"/>
    <head>
        <link rel="stylesheet" href="${ctx}/css/style.css"/>
    </head>
    <body>
        <div class="header">
            <c:set var="userBean" value="${sessionScope.user}"/>
            <c:set var="userLoggedIn" value="${not empty userBean}"/>
            <c:set var="welcomeMsg" value="${PropertyReader.getValue('WELCOME_MESSAGE')}"/>
            <c:choose>
                <c:when test="${userLoggedIn}">
                    <c:set var="role" value="${sessionScope.role}"/>
                    <c:set var="welcomeMsg" value="${welcomeMsg}${userBean.firstName} ${userBean.lastName} (${role})"/>
                </c:when>
                <c:otherwise>
                    <c:set var="welcomeMsg" value="${welcomeMsg}${PropertyReader.getValue('GUEST_MESSAGE')}"/>
                </c:otherwise>
            </c:choose>

            <div class="header-content ">
                 <div class="header">
                <div class="logo-container ">
                    <h1 class="text-logo">ORS Project</h1>

                </div>
                <div class="user-info">
                    <a class="nav-link" href="${ctx}${ORSView.WELCOME_CTL}">Welcome</a>
                    <c:choose>
                        <c:when test="${userLoggedIn}">
                            <a class="nav-link"
                               href="${ctx}${ORSView.LOGIN_CTL}?operation=${LoginCtl.OP_LOG_OUT}">Logout</a>
                        </c:when>
                        <c:otherwise>
                            <a class="nav-link" href="${ctx}${ORSView.LOGIN_CTL}">Login</a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
            <div class="welcome-message">
                <h3>${welcomeMsg}</h3>
            </div>

            <c:if test="${userLoggedIn}">
            <input type="checkbox" id="menu-toggle" class="menu-toggle">
            <label for="menu-toggle" class="menu-icon">&#9776;</label>
                <nav class="main-navigation">
                    <ul>
                        <li><a href="${ctx}${ORSView.MY_PROFILE_CTL}">MyProfile</a></li>
                        <li><a href="${ctx}${ORSView.CHANGE_PASSWORD_CTL}">Change Password</a></li>
                        <li><a href="${ctx}${ORSView.GET_MARKSHEET_CTL}">Get Marksheet</a></li>
                        <li><a href="${ctx}${ORSView.MARKSHEET_MERIT_LIST_CTL}">Marksheet MeritList</a></li>
                        <c:if test="${userBean.roleId == RoleBean.ADMIN}">
                            <li><a href="${ctx}${ORSView.MARKSHEET_CTL}">Add Marksheet</a></li>
                            <li><a href="${ctx}${ORSView.MARKSHEET_LIST_CTL}">Marksheet List</a></li>
                            <li><a href="${ctx}${ORSView.USER_CTL}">Add User</a></li>
                            <li><a href="${ctx}${ORSView.USER_LIST_CTL}">User List</a></li>
                            <li><a href="${ctx}${ORSView.COLLEGE_CTL}">Add College</a></li>
                            <li><a href="${ctx}${ORSView.COLLEGE_LIST_CTL}">College List</a></li>
                            <li><a href="${ctx}${ORSView.ROLE_CTL}">Add Role</a></li>
                            <li><a href="${ctx}${ORSView.ROLE_LIST_CTL}">Role List</a></li>
                            <li><a href="${ctx}${ORSView.STUDENT_CTL}">Add Student</a></li>
                            <li><a href="${ctx}${ORSView.STUDENT_LIST_CTL}">Student List</a></li>
                            <li><a href="${ctx}${ORSView.COURSE_CTL}">Add Course</a></li>
                            <li><a href="${ctx}${ORSView.COURSE_LIST_CTL}">Course List</a></li>
                            <li><a href="${ctx}${ORSView.SUBJECT_CTL}">Add Subject</a></li>
                            <li><a href="${ctx}${ORSView.SUBJECT_LIST_CTL}">Subject List</a></li>
                            <li><a href="${ctx}${ORSView.FACULTY_CTL}">Add Faculty</a></li>
                            <li><a href="${ctx}${ORSView.FACULTY_LIST_CTL}">Faculty List</a></li>
                            <li><a href="${ctx}${ORSView.TIMETABLE_CTL}">Add TimeTable</a></li>
                            <li><a href="${ctx}${ORSView.TIMETABLE_LIST_CTL}">TimeTable List</a></li>
                            <li><a target="blank" href="${ctx}${ORSView.JAVA_DOC_VIEW}">Java Doc</a></li>
                        </c:if>
                        <c:if test="${userBean.roleId == RoleBean.STUDENT}">
                            <li><a href="${ctx}${ORSView.COLLEGE_LIST_CTL}">College List</a></li>
                            <li><a href="${ctx}${ORSView.STUDENT_LIST_CTL}">Student List</a></li>
                            <li><a href="${ctx}${ORSView.COURSE_LIST_CTL}">Course List</a></li>
                            <li><a href="${ctx}${ORSView.SUBJECT_LIST_CTL}">Subject List</a></li>
                            <li><a href="${ctx}${ORSView.FACULTY_LIST_CTL}">Faculty List</a></li>
                            <li><a href="${ctx}${ORSView.TIMETABLE_LIST_CTL}">TimeTable List</a></li>
                        </c:if>
                        <c:if test="${userBean.roleId == RoleBean.KIOSK}">
                            <li><a href="${ctx}${ORSView.COLLEGE_LIST_CTL}">College List</a></li>
                            <li><a href="${ctx}${ORSView.TIMETABLE_LIST_CTL}">TimeTable List</a></li>
                            <li><a href="${ctx}${ORSView.COURSE_LIST_CTL}">Course List</a></li>
                        </c:if>
                        <c:if test="${userBean.roleId == RoleBean.FACULTY}">
                            <li><a href="${ctx}${ORSView.MARKSHEET_CTL}">Add Marksheet</a></li>
                            <li><a href="${ctx}${ORSView.MARKSHEET_LIST_CTL}">Marksheet List</a></li>
                            <li><a href="${ctx}${ORSView.COLLEGE_LIST_CTL}">College List</a></li>
                            <li><a href="${ctx}${ORSView.STUDENT_CTL}">Add Student</a></li>
                            <li><a href="${ctx}${ORSView.STUDENT_LIST_CTL}">Student List</a></li>
                            <li><a href="${ctx}${ORSView.COURSE_LIST_CTL}">Course List</a></li>
                            <li><a href="${ctx}${ORSView.SUBJECT_CTL}">Add Subject</a></li>
                            <li><a href="${ctx}${ORSView.SUBJECT_LIST_CTL}">Subject List</a></li>
                            <li><a href="${ctx}${ORSView.TIMETABLE_CTL}">Add TimeTable</a></li>
                            <li><a href="${ctx}${ORSView.TIMETABLE_LIST_CTL}">TimeTable List</a></li>
                        </c:if>
                        <c:if test="${userBean.roleId == RoleBean.COLLEGE}">
                            <li><a href="${ctx}${ORSView.MARKSHEET_CTL}">Add Marksheet</a></li>
                            <li><a href="${ctx}${ORSView.MARKSHEET_LIST_CTL}">Marksheet List</a></li>
                            <li><a href="${ctx}${ORSView.STUDENT_CTL}">Add Student</a></li>
                            <li><a href="${ctx}${ORSView.STUDENT_LIST_CTL}">Student List</a></li>
                            <li><a href="${ctx}${ORSView.FACULTY_LIST_CTL}">Faculty List</a></li>
                            <li><a href="${ctx}${ORSView.TIMETABLE_LIST_CTL}">TimeTable List</a></li>
                            <li><a href="${ctx}${ORSView.COURSE_LIST_CTL}">Course List</a></li>
                        </c:if>
                    </ul>
                </nav>
            </c:if>
        </div>
        </div>
        <hr/>
    </body>
</html>
