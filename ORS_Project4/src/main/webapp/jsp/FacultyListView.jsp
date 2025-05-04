<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page import="com.rays.pro4.controller.ORSView" %>
<%@page import="com.rays.pro4.controller.FacultyListCtl" %>
<%@page import="com.rays.pro4.Bean.FacultyBean" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<html>
<head>
    <meta charset="UTF-8">
    <c:set var="ctx" value="${pageContext.request.contextPath}"/>
    <link rel="icon" type="image/png" href="${ctx}/img/logo.png" sizes="16*16"/>
    <title>
        <c:choose>
            <c:when test="${not empty facultyList}">Faculty List</c:when>
            <c:otherwise>Faculty List</c:otherwise>
        </c:choose>
    </title>
    <link rel="stylesheet" href="${ctx}/css/style.css" />
	<script src="${ctx}/js/jquery.min.js"></script>
    <script src="${ctx}/js/Checkbox11.js"></script> 

</head>
<body class="bg-light main-body">
<jsp:useBean id="facultyBean"
             class="com.rays.pro4.Bean.FacultyBean" scope="request"></jsp:useBean>
	<jsp:useBean id="collegeBean" class="com.rays.pro4.Bean.CollegeBean"
		scope="request"></jsp:useBean> 
	<jsp:useBean id="courseBean" class="com.rays.pro4.Bean.CourseBean"
		scope="request"></jsp:useBean>
		<%@include file="Header.jsp"%>
	<form action="${ctx}${ORSView.FACULTY_LIST_CTL}" method="post">
		<div class="container">
			<h1 class="text-center">
                Faculty List
			</h1>

			<div class="message-container">
				<c:if test="${not empty requestScope.errorMessage}">
					<div class="alert alert-danger" role="alert">
						${requestScope.errorMessage}
					</div>
				</c:if>
				<c:if test="${not empty requestScope.successMessage}">
					<div class="alert alert-success" role="alert">
						${requestScope.successMessage}
					</div>
				</c:if>
			</div>
            <c:set var="collegeList" value="${requestScope.CollegeList}"/>
            <c:set var="courseList" value="${requestScope.CourseList}"/>
            <c:set var="next" value="${requestScope.nextlist}"/>
            <c:set var="pageNo" value="${requestScope.pageNo}"/>
            <c:set var="pageSize" value="${requestScope.pageSize}"/>
            <c:set var="index" value="${(pageNo - 1) * pageSize + 1}"/>
            <c:set var="facultyList" value="${requestScope.list}"/>

			<c:if test="${not empty facultyList}">
				<table class="search-table w-100">
                    <tr>
                        <td class="text-center">
                            <label for="firstname">First Name :</label>
                            <input type="text" id="firstname" name="firstname" placeholder="Enter First Name"
                                   class="form-control-inline" value="${param.firstname}" />

                            <label for="collegeid">College Name : </label>
                            <select id="collegeid" name="collegeid" class="form-control-inline">
                                <option value="">Select College</option>
                                <c:forEach var="college" items="${collegeList}">
                                    <option value="${college.id}" ${facultyBean.collegeId == college.id ? 'selected' : ''}>
                                        ${college.name}
                                    </option>
                                </c:forEach>
                            </select>

                            <label for="courseid">Course Name :</label>
                            <select id="courseid" name="courseid" class="form-control-inline">
                                <option value="">Select Course</option>
                                <c:forEach var="course" items="${courseList}">
                                    <option value="${course.id}" ${facultyBean.courseId == course.id ? 'selected' : ''}>
                                        ${course.name}
                                    </option>
                                </c:forEach>
                            </select>
                            <input type="submit" name="operation" value="${FacultyListCtl.OP_SEARCH}" class="btn btn-primary" />
                            <input type="submit" name="operation" value="${FacultyListCtl.OP_RESET}" class="btn btn-secondary" />
                        </td>
                    </tr>
                </table>
                <tr>

                </table>
                <table class="table table-bordered table-striped w-100">
                    <thead class="table-header">
                        <tr>
                            <th><input type="checkbox" id="select_all" name="select"> Select All</th>
                            <th>S.No.</th>
                            <th>First Name</th>
                            <th>Last Name</th>
                            <th>Email Id</th>
                            <th>College Name</th>
                            <th>Course Name</th>
                            <th>Subject Name</th>
                            <th>DOB</th>
                            <th>Mobile No</th>
                            <th>Edit</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="faculty" items="${facultyList}" varStatus="loop">
                            <tr class="text-center table-row">
                                <td><input type="checkbox" class="checkbox" name="ids" value="${faculty.id}" /></td>
                                <td>
                                    ${index + loop.index}
                                </td>
                                <td>
                                    <c:out value="${faculty.firstName}" />
                                </td>
                                <td>
                                    <c:out value="${faculty.lastName}"/>
                                </td>
                                <td>
                                    <c:out value="${faculty.emailId}"/>
                                </td>
                                <td>
                                    <c:out value="${faculty.collegeName}"/>
                                </td>
                                <td>
                                    <c:out value="${faculty.courseName}"/>
                                </td>
                                <td>
                                    <c:out value="${faculty.subjectName}"/>
                                </td>
                                <td>
                                    <fmt:formatDate type="date" value="${faculty.dob}" pattern="MM/dd/yyyy"/>
                                </td>
                                <td>
                                    <c:out value="${faculty.mobileNo}"/>
                                </td>
                                <td>
                                    <a href="${ctx}${ORSView.FACULTY_CTL}?id=${faculty.id}" class="btn btn-link">Edit</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                <table class="w-100 my-4">
                    <tr>
                        <td><input type="submit" name="operation" class="btn btn-secondary" value="${FacultyListCtl.OP_PREVIOUS}" ${pageNo == 1 ? 'disabled' : ''} /></td>
                        <td><input type="submit" name="operation" class="btn btn-danger" value="${FacultyListCtl.OP_DELETE}" /></td>
                        <td><input type="submit" name="operation" class="btn btn-success" value="${FacultyListCtl.OP_NEW}" /></td>
                        <td class="text-right"><input type="submit" name="operation" class="btn btn-primary" value="${FacultyListCtl.OP_NEXT}" ${facultyList.size() < pageSize || next == 0 ? 'disabled' : ''} /></td>
                    </tr>
                </table>
            </c:if>
            <c:if test="${empty facultyList}">
                <div class="text-center">
                    <h3>No Result Found</h3> 
                </div> 
            </c:if>
            <input type="hidden" name="pageNo" value="${pageNo}" />
            <input type="hidden" name="pageSize" value="${pageSize}" />
        </div>
	</form>
    <%@ include file="Footer.jsp" %>
</body>
</html>
