jsp
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.rays.pro4.controller.TimetableListCtl" %>
<%@ page import="com.rays.pro4.Util.HTMLUtility" %>
<%@ page import="com.rays.pro4.controller.ORSView" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<html>
    <head>
        <c:set var="ctx" value="${pageContext.request.contextPath}"/>
        <link rel="icon" type="image/png" href="${ctx}/img/logo.png" sizes="16*16"/>
        <title>TimeTable List</title>
        <link rel="stylesheet" href="${ctx}/css/style.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
        <script src="${ctx}/js/jquery.min.js"></script>
        <script src="${ctx}/js/Checkbox11.js"></script>
        <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
        <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
        <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
        <script>
            function disableSunday(d) {
                var day = d.getDay();
                if (day == 0) {
                    return [false];
                } else {
                    return [true];
                }
            }

            $(function () {
                $("#abcd").datepicker({
                    changeMonth: true,
                    changeYear: true,
                    yearRange: '0:+10',
                    dateFormat: 'dd-mm-yy',
                    beforeShowDay: disableSunday,
                    minDate: 0
                });
            });
        </script>
    </head>
    <body>
        <jsp:useBean id="bean" class="com.rays.pro4.Bean.TimeTableBean" scope="request"></jsp:useBean>
        <%@include file="Header.jsp" %>
        <form action="${ctx}${ORSView.TIMETABLE_LIST_CTL}" method="post">
            <div class="container">
                <h1 class="text-center">TimeTable List</h1>
                <div class="message-container">
                    <c:if test="${not empty requestScope.error}">
                        <span class="alert alert-danger">${requestScope.error}</span>
                    </c:if>
                    <c:if test="${not empty requestScope.success}">
                        <span class="alert alert-success">${requestScope.success}</span>
                    </c:if>
                </div>
                <c:set var="cList" value="${requestScope.courseList}"/>
                <c:set var="sList" value="${requestScope.subjectList}"/>
                <c:set var="next" value="${requestScope.nextlist}"/>
                <c:set var="pageNo" value="${requestScope.pageNo}"/>
                <c:set var="pageSize" value="${requestScope.pageSize}"/>
                <c:set var="index" value="${(pageNo - 1) * pageSize + 1}"/>
                <c:set var="list" value="${requestScope.list}"/>
                <c:if test="${not empty list}">
                    <table class="search-table w-100">
                        <tr>
                            <td class="text-center d-flex gap-2 justify-content-center align-items-center">
                                <label for="clist">Course Name : </label>
                                    ${HTMLUtility.getList("clist", bean.courseId, cList, "Select Course")}
                                
                                <label for="slist">Subject Name:</label>
                                    ${HTMLUtility.getList("slist", bean.subjectId, sList,"Select Subject")}
                               
                                <input type="submit" name="operation" value="${TimetableListCtl.OP_SEARCH}"
                                       class="btn btn-primary">
                                <input type="submit" name="operation" value="<%=TimetableListCtl.OP_RESET %>"
                                       class="btn btn-secondary">
                            </td>
                        </tr>
                    </table>
                    <table class="table table-bordered table-striped w-100">
                        <thead class="table-header">
                            <tr>
                                <th><input type="checkbox" id="select_all" name="Select">Select All.</th>
                                <th>S.No.</th>
                                <th>
                                    <a href="${ctx}${ORSView.TIMETABLE_LIST_CTL}?orderBy=CourseName&sortOrder=${sortOrder eq 'asc' ? 'desc' : 'asc'}&pageNo=${pageNo}&pageSize=${pageSize}">
                                        Course Name
                                        <c:if test="${orderBy eq 'CourseName'}">
                                            <c:choose>
                                                <c:when test="${sortOrder eq 'asc'}"><i class="fas fa-arrow-up"></i></c:when>
                                                <c:otherwise><i class="fas fa-arrow-down"></i></c:otherwise>
                                            </c:choose>
                                        </c:if>
                                    </a>
                                </th>
                                <th> <a href="${ctx}${ORSView.TIMETABLE_LIST_CTL}?orderBy=SubjectName&sortOrder=${sortOrder eq 'asc' ? 'desc' : 'asc'}&pageNo=${pageNo}&pageSize=${pageSize}">Subject Name
                                       <c:if test="${orderBy eq 'SubjectName'}">
                                            <c:choose><c:when test="${sortOrder eq 'asc'}"><i class="fas fa-arrow-up"></i></c:when><c:otherwise><i class="fas fa-arrow-down"></i></c:otherwise></c:choose>
                                        </c:if>
                                    </a></th>
                                <th>Semester.</th>
                                <th>ExamDate.</th>
                                <th>ExamTime.</th>
                                <th>Edit</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="bean" items="${list}">
                                <tr class="text-center table-row">
                                    <td><input type="checkbox" class="checkbox" name="ids" value="${bean.id}"></td>
                                    <td><c:out value="${index}"/> <c:set var="index" value="${index + 1}"/></td>
                                    <td>${bean.courseName}</td>
                                    <td>${bean.subjectName}</td>
                                    <td>${bean.semester}</td>
                                    <td>${bean.examDate}</td>
                                    <td>${bean.examTime}</td>
                                    <td><a href="${ctx}/TimeTableCtl?id=${bean.id}" class="btn btn-link">Edit</a></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    <table class="w-100">
                        <tr >
                            <td class="float-start"><input type="submit" name="operation"
                                                          value="${TimetableListCtl.OP_PREVIOUS}"
                                                          class="btn btn-secondary" ${pageNo == 1 ? 'disabled' : ''}></td>
                            <td class="text-center"><input type="submit" name="operation" value="${TimetableListCtl.OP_DELETE}"
                                       class="btn btn-danger"><input type="submit" name="operation" value="${TimetableListCtl.OP_NEW}"
                                       class="btn btn-success">
                            </td>
                            <td class="float-end"><input type="submit" name="operation"
                                                           value="<%=TimetableListCtl.OP_NEXT%>"
                                                           class="btn btn-primary"
                                    ${list.size() < pageSize || next == 0 ? 'disabled' : ''}></td>
                        </tr>
                    </table>
                </c:if>
                <c:if test="${empty list}">
                    <div class="text-center">
                        <input type="submit" name="operation" value="<%=TimetableListCtl.OP_BACK%>"
                               class="btn btn-primary">
                    </div>
                </c:if>
                <input type="hidden" name="pageNo" value="${pageNo}">
                <input type="hidden" name="pageSize" value="${pageSize}">
            </div>
        </form>
        <%@include file="Footer.jsp" %>
    </body>
</html>