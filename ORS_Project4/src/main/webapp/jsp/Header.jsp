<%@ page import="com.rays.pro4.Bean.UserBean" %>
<%@ page import="com.rays.pro4.Bean.RoleBean" %>
<%@ page import="com.rays.pro4.controller.LoginCtl" %>
<%@ page import="com.rays.pro4.controller.ORSView" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ page import="com.rays.pro4.Util.PropertyReader" %>
<html>
<head>
    <link rel="stylesheet" href="<%=ORSView.APP_CONTEXT%>/css/style.css">
</head>
<body>
<div class="header">
    <%
        UserBean userBean = (UserBean) session.getAttribute("user");
        boolean userLoggedIn = userBean != null;
        String welcomeMsg = PropertyReader.getValue("WELCOME_MESSAGE");
        if (userLoggedIn) {
            String role = (String) session.getAttribute("role");
            welcomeMsg += userBean.getFirstName() + "  " + userBean.getLastName() + " (" + role + ")";
        } else {
            welcomeMsg += PropertyReader.getValue("GUEST_MESSAGE");
        }
    %>

    <div class="header-content">
        <div class="logo-container">
            <img class="logo" src="<%=ORSView.APP_CONTEXT%><%=PropertyReader.getValue("LOGO_PATH")%>"
                 width="<%=PropertyReader.getValue("LOGO_WIDTH")%>" height="<%=PropertyReader.getValue("LOGO_HEIGHT")%>" alt="Logo">
        </div>

        <div class="user-info">
            <a href="<%=ORSView.WELCOME_CTL%>">Welcome</a>
            <% if (userLoggedIn) { %>
            <a href="<%=ORSView.LOGIN_CTL%>?operation=<%=LoginCtl.OP_LOG_OUT%>">Logout</a>
            <% } else { %>
            <a href="<%=ORSView.LOGIN_CTL%>">Login</a>
            <% } %>
        </div>
    </div>
    <div class="welcome-message">
        <h3><%=welcomeMsg%></h3>
    </div>
    <% if (userLoggedIn) { %>
    <nav class="navigation">
        <ul>
            <li><a href="<%=ORSView.MY_PROFILE_CTL%>">MyProfile</a></li>
            <li><a href="<%=ORSView.CHANGE_PASSWORD_CTL%>">Change Password</a></li>
            <li><a href="<%=ORSView.GET_MARKSHEET_CTL%>">Get Marksheet</a></li>
            <li><a href="<%=ORSView.MARKSHEET_MERIT_LIST_CTL%>">Marksheet MeritList</a></li>

            <% if (userBean.getRoleId() == RoleBean.ADMIN) { %>
            <li><a href="<%=ORSView.MARKSHEET_CTL%>">Add Marksheet</a></li>
            <li><a href="<%=ORSView.MARKSHEET_LIST_CTL%>">Marksheet List</a></li>
            <li><a href="<%=ORSView.USER_CTL%>">Add User</a></li>
            <li><a href="<%=ORSView.USER_LIST_CTL%>">User List</a></li>
            <li><a href="<%=ORSView.COLLEGE_CTL%>">Add College</a></li>
            <li><a href="<%=ORSView.COLLEGE_LIST_CTL%>">College List</a></li>
            <li><a href="<%=ORSView.ROLE_CTL%>">Add Role</a></li>
            <li><a href="<%=ORSView.ROLE_LIST_CTL%>">Role List</a></li>
            <li><a href="<%=ORSView.STUDENT_CTL%>">Add Student</a></li>
            <li><a href="<%=ORSView.STUDENT_LIST_CTL%>">Student List</a></li>
            <li><a href="<%=ORSView.COURSE_CTL%>">Add Course</a></li>
            <li><a href="<%=ORSView.COURSE_LIST_CTL%>">Course List</a></li>
            <li><a href="<%=ORSView.SUBJECT_CTL%>">Add Subject</a></li>
            <li><a href="<%=ORSView.SUBJECT_LIST_CTL%>">Subject List</a></li>
            <li><a href="<%=ORSView.FACULTY_CTL%>">Add Faculty</a></li>
            <li><a href="<%=ORSView.FACULTY_LIST_CTL%>">Faculty List</a></li>
            <li><a href="<%=ORSView.TIMETABLE_CTL%>">Add TimeTable</a></li>
            <li><a href="<%=ORSView.TIMETABLE_LIST_CTL%>">TimeTable List</a></li>
            <li><a target="blank" href="<%=ORSView.JAVA_DOC_VIEW%>">Java Doc</a></li>
            <% } %>

            <% if (userBean.getRoleId() == RoleBean.STUDENT) { %>
            <li><a href="<%=ORSView.COLLEGE_LIST_CTL%>">College List</a></li>
            <li><a href="<%=ORSView.STUDENT_LIST_CTL%>">Student List</a></li>
            <li><a href="<%=ORSView.COURSE_LIST_CTL%>">Course List</a></li>
            <li><a href="<%=ORSView.SUBJECT_LIST_CTL%>">Subject List</a></li>
            <li><a href="<%=ORSView.FACULTY_LIST_CTL%>">Faculty List</a></li>
            <li><a href="<%=ORSView.TIMETABLE_LIST_CTL%>">TimeTable List</a></li>
            <% } %>

            <% if (userBean.getRoleId() == RoleBean.KIOSK) { %>
            <li><a href="<%=ORSView.COLLEGE_LIST_CTL%>">College List</a></li>
            <li><a href="<%=ORSView.TIMETABLE_LIST_CTL%>">TimeTable List</a></li>
            <li><a href="<%=ORSView.COURSE_LIST_CTL%>">Course List</a></li>
            <% } %>

            <% if (userBean.getRoleId() == RoleBean.FACULTY) { %>
            <li><a href="<%=ORSView.MARKSHEET_CTL%>">Add Marksheet</a></li>
            <li><a href="<%=ORSView.MARKSHEET_LIST_CTL%>">Marksheet List</a></li>
            <li><a href="<%=ORSView.COLLEGE_LIST_CTL%>">College List</a></li>
            <li><a href="<%=ORSView.STUDENT_CTL%>">Add Student</a></li>
            <li><a href="<%=ORSView.STUDENT_LIST_CTL%>">Student List</a></li>
            <li><a href="<%=ORSView.COURSE_LIST_CTL%>">Course List</a></li>
            <li><a href="<%=ORSView.SUBJECT_CTL%>">Add Subject</a></li>
            <li><a href="<%=ORSView.SUBJECT_LIST_CTL%>">Subject List</a></li>
            <li><a href="<%=ORSView.TIMETABLE_CTL%>">Add TimeTable</a></li>
            <li><a href="<%=ORSView.TIMETABLE_LIST_CTL%>">TimeTable List</a></li>
            <% } %>

            <% if (userBean.getRoleId() == RoleBean.COLLEGE) { %>
            <li><a href="<%=ORSView.MARKSHEET_CTL%>">Add Marksheet</a></li>
            <li><a href="<%=ORSView.MARKSHEET_LIST_CTL%>">Marksheet List</a></li>
            <li><a href="<%=ORSView.STUDENT_CTL%>">Add Student</a></li>
            <li><a href="<%=ORSView.STUDENT_LIST_CTL%>">Student List</a></li>
            <li><a href="<%=ORSView.FACULTY_LIST_CTL%>">Faculty List</a></li>
            <li><a href="<%=ORSView.TIMETABLE_LIST_CTL%>">TimeTable List</a></li>
            <li><a href="<%=ORSView.COURSE_LIST_CTL%>">Course List</a></li>
            <% } %>
        </ul>
    </nav>
    <% } %>
</div>
<hr/>
</body>
</html>
