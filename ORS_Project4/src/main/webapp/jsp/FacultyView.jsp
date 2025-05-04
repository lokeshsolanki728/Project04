
<%@ taglib uri=\"http://java.sun.com/jsp/jstl/core\" prefix=\"c\" %>
<%@ taglib uri=\"http://java.sun.com/jsp/jstl/functions\" prefix=\"fn\" %>
<%@ taglib uri=\"http://java.sun.com/jsp/jstl/fmt\" prefix=\"fmt\" %>
<%@ page import=\"com.rays.pro4.controller.FacultyCtl\" %>
<%@ page import=\"com.rays.pro4.controller.ORSView\" %>
<%@ page import="com.rays.pro4.Bean.FacultyBean" %>
<%@ page import="java.util.List" %>
<%@ page import="com.rays.pro4.Bean.CollegeBean" %>
<%@ page language=\"java\" contentType=\"text/html; charset=ISO-8859-1\" pageEncoding=\"ISO-8859-1\" %>

<html>
<head>
    <c:set var="ctx" value="${pageContext.request.contextPath}" />
    <link rel="icon" type="image/png" href="${ctx}/img/logo.png" sizes="16x16" />
    <meta charset="UTF-8">
    <title>
            <c:choose>
                <c:when test=\"${not empty bean.id}\">Update Faculty</c:when>
                <c:otherwise>Add Faculty</c:otherwise>
            </c:choose>
        </title>

        <link rel=\"stylesheet\" href=\"${ctx}/css/style.css\" />
    <script src="${ctx}/js/jquery.min.js"></script>
    <script src="${ctx}/js/jquery-ui.min.js"></script>
    <script>
        $(function () {
            $("#date").datepicker({
                changeMonth: true,
                changeYear: true,
                yearRange: '1980:2020'
            });
        });
    </script>
</head>
<body>
    <jsp:useBean id="bean" class="com.rays.pro4.Bean.FacultyBean" scope="request" />
    <%@ include file="Header.jsp" %>

    <form action="${ctx}${ORSView.FACULTY_CTL}" method="post">
        <div class="container">
            <h1>
                <c:choose>
                    <c:when test="${not empty bean.id}">Update Faculty</c:when>
                    <c:otherwise>Add Faculty</c:otherwise>
                </c:choose>
            </h1>

            <div class="message-container">
                <c:if test="${not empty requestScope.success}">
                    <div class="alert alert-success">${requestScope.success}</div>
                </c:if>
                <c:if test="${not empty requestScope.error}">
                    <div class="alert alert-danger">${requestScope.error}</div>
                </c:if>
            </div>

 <input type="hidden" name="id" value="${bean.id}" />
            <input type="hidden" name="createdBy" value="${bean.createdBy}" />
            <input type="hidden" name="modifiedBy" value="${bean.modifiedBy}" />
            <input type="hidden" name="createdDatetime" value="${bean.createdDatetime}" />
            <input type="hidden" name="modifiedDatetime" value="${bean.modifiedDatetime}" />

            <table class="table table-borderless w-50 mx-auto">
                <tr>
                    <th class="text-left"><label for="firstname">First Name <span class="required">*</span>:</label></th>
                    <td><input type="text" id="firstname" name="firstname" placeholder="Enter First Name"
                               class="form-control" value="${bean.firstname}" maxlength="45" />
                        <span class="error-message">${requestScope.firstname}</span>
                    </td>
                </tr>
                <tr>
                    <th class="text-left"><label for="lastname">Last Name <span class="required">*</span>:</label></th>
                    <td><input type="text" id="lastname" name="lastname" placeholder="Enter Last Name"
                               class="form-control" value="${bean.lastName}" maxlength="45" />
                        <span class="error-message">${requestScope.lastname}</span>
                    </td>
                </tr>
                <tr>
                    <th class="text-left"><label for="gender">Gender <span class="required">*</span>:</label></th>
                    <td>
                        <select id="gender" name="gender" class="form-control">
                            <option value="" selected>Select Gender</option>
                            <option value="Male" ${bean.gender == 'Male' ? 'selected' : ''}>Male</option>
                            <option value="Female" ${bean.gender == 'Female' ? 'selected' : ''}>Female</option>\
                        </select>
                        <span class="error-message">${requestScope.gender}</span>
                    </td>
                </tr>
                <tr>
                    <th class="text-left"><label for="collegeId">College Name <span class="required">*</span>:</label></th>
                    <td>
                        <select id="collegeId" name="collegeId" class="form-control">
                            <option value="" selected>Select College</option>
                            <c:forEach var="college" items="${CollegeList}">
                                <option value="${college.id}" ${bean.collegeId == college.id ? 'selected' : ''}>${college.name}</option>
                            </c:forEach>
                        </select>
                        <span class="error-message">${requestScope.collegeId}</span>
                    </td>
                </tr>
                <tr>
                    <th class="text-left"><label for="courseId">Course Name <span class="required">*</span>:</label></th>
                    <td>
                        <select id="courseId" name="courseId" class="form-control">
                            <option value="" selected>Select Course</option>
                            <c:forEach var="course" items="${CourseList}">
                                <option value="${course.id}" ${bean.courseId == course.id ? 'selected' : ''}>${course.name}</option>
                            </c:forEach>
                        </select>
                        <span class="error-message">${requestScope.courseId}</span>
                    </td>
                </tr>
                <tr>
                    <th class="text-left"><label for="subjectId">Subject Name <span class="required">*</span>:</label></th>
                    <td>
                        <select id="subjectId" name="subjectId" class="form-control">
                            <option value="" selected>Select Subject</option>
 <c:forEach var="subject" items="${SubjectList}">
                                <option value="${subject.id}" ${bean.subjectId == subject.id ? 'selected' : ''}>${subject.subjectName}</option>
                            </c:forEach>
                        </select>
                        <div class="error-message">${requestScope.subjectId}</div>
                    </td>
                </tr>
                <tr>
                    <th class="text-left"><label for="date">Date Of Birth <span class="required">*</span>:</label></th>
                    <td><input type="text" name="dob" id="date" placeholder="Enter Date Of Birth"
                               class="form-control" readonly="readonly"
                               value="<fmt:formatDate type='date' value='${bean.dob}' pattern='MM/dd/yyyy'/>" />
                        <span class="error-message">${requestScope.dob}</span>
                    </td>
                </tr>
                <tr>
                    <th class="text-left"><label for="emailId">Login ID <span class="required">*</span>:</label></th>
                    <td><input type="text" id="emailId" name="emailId" class="form-control" maxlength="50"
                               placeholder="Enter Email ID" value="${bean.emailID}" />
                        <span class="error-message">${requestScope.emailId}</span>
                    </td>
                </tr>
                <tr>
                    <th class="text-left"><label for="mobileno">Mobile No <span class="required">*</span>:</label></th>
                    <td><input type="text" id="mobileno" name="mobileno" class="form-control" maxlength="10"
                               placeholder="Enter Mobile No" value="${bean.mobileNo}" />
                        <span class="error-message">${requestScope.mobileno}</span>
                    </td>
                </tr>
                <tr>
                    <th></th>
                    <td>
                        <div class="button-container">
                            <input type="submit" name="operation" class="btn btn-primary"
                                   value="<c:out value="${empty bean.id ? FacultyCtl.OP_SAVE : FacultyCtl.OP_UPDATE}" />" />
                            <input type="submit" name="operation" class="btn btn-secondary"
                                   value="<c:out value="${empty bean.id ? FacultyCtl.OP_RESET : FacultyCtl.OP_CANCEL}" />" />
                            <c:if test="${not empty bean.id}">
                                <input type="submit" name="operation" class="btn btn-danger"
                                       value="${FacultyCtl.OP_DELETE}" />
                            </c:if>
                        </div>
                    </td>
                </tr>
            </table>
        </div>
    </form>
    <%@ include file="Footer.jsp" %>
</body>
</html>
