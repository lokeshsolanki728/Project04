<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.rays.pro4.Bean.UserBean" %>
<%@ page import="com.rays.pro4.Bean.RoleBean" %>
<%@ page import="com.rays.pro4.controller.LoginCtl" %>
<%@ page import="com.rays.pro4.controller.ORSView" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ page import="com.rays.pro4.Util.PropertyReader" %>
<html>
<head>
    <title>Header</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    
</head>
<body>
<div class="header" >
    <c:set var="userBean" value="${sessionScope.user}" />
    <c:set var="userLoggedIn" value="${not empty userBean}" />
    <c:choose>
    <c:when test="${userLoggedIn}">
            <c:set var="role" value="${sessionScope.role}" />
            <c:set var="welcomeMsg" value="${PropertyReader.getValue('WELCOME_MESSAGE')}${userBean.firstName} ${userBean.lastName} (${role})" />
        </c:when>
        <c:otherwise>
             <c:set var="welcomeMsg" value="${PropertyReader.getValue('WELCOME_MESSAGE')}${PropertyReader.getValue('GUEST_MESSAGE')}" />
        </c:otherwise>
    </c:choose>
    
    
    

    <div class="header-content">
        <div class="logo-container">
            <img class="logo" src="<%=ORSView.APP_CONTEXT%><%=PropertyReader.getValue("LOGO_PATH")%>"
                 width="<%=PropertyReader.getValue("LOGO_WIDTH")%>" height="<%=PropertyReader.getValue("LOGO_HEIGHT")%>" alt="Logo">
        </div>

        <div class="user-info">
           <a href="${pageContext.request.contextPath}${ORSView.WELCOME_CTL}">Welcome</a>
            <c:choose>
                <c:when test="${userLoggedIn}">
                    <a href="${pageContext.request.contextPath}${ORSView.LOGIN_CTL}?operation=${LoginCtl.OP_LOG_OUT}">Logout</a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}${ORSView.LOGIN_CTL}">Login</a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
    <div class="welcome-message">
        <h3>${welcomeMsg}</h3>
    </div>
    <c:if test="${userLoggedIn}">
        
    
   
    <nav class="navigation">
        <ul>
            <li><a href="<%=ORSView.MY_PROFILE_CTL%>">MyProfile</a></li>
            <li><a href="<%=ORSView.CHANGE_PASSWORD_CTL%>">Change Password</a></li>
            <li><a href="<%=ORSView.GET_MARKSHEET_CTL%>">Get Marksheet</a></li>
            <li><a href="<%=ORSView.MARKSHEET_MERIT_LIST_CTL%>">Marksheet MeritList</a></li>

            <c:if test="${userBean.roleId == RoleBean.ADMIN}">
              <li><a href="${pageContext.request.contextPath}${ORSView.MARKSHEET_CTL}">Add Marksheet</a></li>
              <li><a href="${pageContext.request.contextPath}${ORSView.MARKSHEET_LIST_CTL}">Marksheet List</a></li>
              <li><a href="${pageContext.request.contextPath}${ORSView.USER_CTL}">Add User</a></li>
              <li><a href="${pageContext.request.contextPath}${ORSView.USER_LIST_CTL}">User List</a></li>
              <li><a href="${pageContext.request.contextPath}${ORSView.COLLEGE_CTL}">Add College</a></li>
              <li><a href="${pageContext.request.contextPath}${ORSView.COLLEGE_LIST_CTL}">College List</a></li>
              <li><a href="${pageContext.request.contextPath}${ORSView.ROLE_CTL}">Add Role</a></li>
              <li><a href="${pageContext.request.contextPath}${ORSView.ROLE_LIST_CTL}">Role List</a></li>
              <li><a href="${pageContext.request.contextPath}${ORSView.STUDENT_CTL}">Add Student</a></li>
              <li><a href="${pageContext.request.contextPath}${ORSView.STUDENT_LIST_CTL}">Student List</a></li>
              <li><a href="${pageContext.request.contextPath}${ORSView.COURSE_CTL}">Add Course</a></li>
              <li><a href="${pageContext.request.contextPath}${ORSView.COURSE_LIST_CTL}">Course List</a></li>
              <li><a href="${pageContext.request.contextPath}${ORSView.SUBJECT_CTL}">Add Subject</a></li>
              <li><a href="${pageContext.request.contextPath}${ORSView.SUBJECT_LIST_CTL}">Subject List</a></li>
              <li><a href="${pageContext.request.contextPath}${ORSView.FACULTY_CTL}">Add Faculty</a></li>
              <li><a href="${pageContext.request.contextPath}${ORSView.FACULTY_LIST_CTL}">Faculty List</a></li>
              <li><a href="${pageContext.request.contextPath}${ORSView.TIMETABLE_CTL}">Add TimeTable</a></li>
              <li><a href="${pageContext.request.contextPath}${ORSView.TIMETABLE_LIST_CTL}">TimeTable List</a></li>
              <li><a target="blank" href="${pageContext.request.contextPath}${ORSView.JAVA_DOC_VIEW}">Java Doc</a></li>
            </c:if>
            <c:if test="${userBean.roleId == RoleBean.STUDENT}">
               <li><a href="${pageContext.request.contextPath}${ORSView.COLLEGE_LIST_CTL}">College List</a></li>
              <li><a href="${pageContext.request.contextPath}${ORSView.STUDENT_LIST_CTL}">Student List</a></li>
              <li><a href="${pageContext.request.contextPath}${ORSView.COURSE_LIST_CTL}">Course List</a></li>
              <li><a href="${pageContext.request.contextPath}${ORSView.SUBJECT_LIST_CTL}">Subject List</a></li>
              <li><a href="${pageContext.request.contextPath}${ORSView.FACULTY_LIST_CTL}">Faculty List</a></li>
              <li><a href="${pageContext.request.contextPath}${ORSView.TIMETABLE_LIST_CTL}">TimeTable List</a></li>
            </c:if>
            <c:if test="${userBean.roleId == RoleBean.KIOSK}">
              <li><a href="${pageContext.request.contextPath}${ORSView.COLLEGE_LIST_CTL}">College List</a></li>
              <li><a href="${pageContext.request.contextPath}${ORSView.TIMETABLE_LIST_CTL}">TimeTable List</a></li>
              <li><a href="${pageContext.request.contextPath}${ORSView.COURSE_LIST_CTL}">Course List</a></li>
            </c:if>
            <c:if test="${userBean.roleId == RoleBean.FACULTY}">
              <li><a href="${pageContext.request.contextPath}${ORSView.MARKSHEET_CTL}">Add Marksheet</a></li>
              <li><a href="${pageContext.request.contextPath}${ORSView.MARKSHEET_LIST_CTL}">Marksheet List</a></li>
              <li><a href="${pageContext.request.contextPath}${ORSView.COLLEGE_LIST_CTL}">College List</a></li>
              <li><a href="${pageContext.request.contextPath}${ORSView.STUDENT_CTL}">Add Student</a></li>
              <li><a href="${pageContext.request.contextPath}${ORSView.STUDENT_LIST_CTL}">Student List</a></li>
              <li><a href="${pageContext.request.contextPath}${ORSView.COURSE_LIST_CTL}">Course List</a></li>
              <li><a href="${pageContext.request.contextPath}${ORSView.SUBJECT_CTL}">Add Subject</a></li>
              <li><a href="${pageContext.request.contextPath}${ORSView.SUBJECT_LIST_CTL}">Subject List</a></li>
              <li><a href="${pageContext.request.contextPath}${ORSView.TIMETABLE_CTL}">Add TimeTable</a></li>
              <li><a href="${pageContext.request.contextPath}${ORSView.TIMETABLE_LIST_CTL}">TimeTable List</a></li>
            </c:if>
            <c:if test="${userBean.roleId == RoleBean.COLLEGE}">
                <li><a href="${pageContext.request.contextPath}${ORSView.MARKSHEET_CTL}">Add Marksheet</a></li>
              <li><a href="${pageContext.request.contextPath}${ORSView.MARKSHEET_LIST_CTL}">Marksheet List</a></li>
              <li><a href="${pageContext.request.contextPath}${ORSView.STUDENT_CTL}">Add Student</a></li>
              <li><a href="${pageContext.request.contextPath}${ORSView.STUDENT_LIST_CTL}">Student List</a></li>
              <li><a href="${pageContext.request.contextPath}${ORSView.FACULTY_LIST_CTL}">Faculty List</a></li>
              <li><a href="${pageContext.request.contextPath}${ORSView.TIMETABLE_LIST_CTL}">TimeTable List</a></li>
              <li><a href="${pageContext.request.contextPath}${ORSView.COURSE_LIST_CTL}">Course List</a></li>
            </c:if>
        </ul>
    </nav>
    </c:if>
</div>
<hr/>
</body>
</html>
