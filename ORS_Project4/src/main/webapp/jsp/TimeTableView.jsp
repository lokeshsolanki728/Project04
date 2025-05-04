<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="com.rays.pro4.controller.TimeTableCtl"%>
<%@page import="com.rays.pro4.controller.ORSView"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
    <c:set var="ctx" value="${pageContext.request.contextPath}" />
    <link rel="icon" type="image/png" href="${ctx}/img/logo.png" sizes="16*16" />
    <title>Time Table Register Page</title>
    <link rel="stylesheet" href="${ctx}/css/style.css">
    <meta charset="utf-8">
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
            $("#udate5").datepicker({
                changeMonth: true,
                changeYear: true,
                yearRange: '0:+2',
                dateFormat: 'mm/dd/yy',
                beforeShowDay: disableSunday,
                minDate: 0
            });
        });
    </script>

</head>
<body>
	<jsp:useBean id="timeTableBean" class="com.rays.pro4.Bean.TimeTableBean"
		scope="request"></jsp:useBean>

	<form action="${ctx}${ORSView.TIMETABLE_CTL}" method="post">
		<%@include file="Header.jsp"%>
		<c:set var="courseList" value="${requestScope.CourseList}" />
		<c:set var="subjectList" value="${requestScope.SubjectList}" />
		<div class="container">
			<h1 class="text-center">
				<c:choose>
					<c:when test="${not empty bean.id && bean.id > 0}">
					Update Time Table
					</c:when>
					<c:otherwise>
						Add Time Table
					</c:otherwise>
				</c:choose>
			</h1>

			<div class="message-container">
				<c:if test="${not empty requestScope.errorMessage}">
					<div class="alert alert-danger" role="alert">
						${requestScope.errorMessage}</div>
                </c:if>
                <c:if test="${not empty requestScope.successMessage}">
					<div class="alert alert-success" role="alert">
						${requestScope.successMessage}</div>
				</c:if>
			</div>

			<input type="hidden" name="id" value="${timeTableBean.id}"> <input
				type="hidden" name="createdby" value="${timeTableBean.createdBy}">
			<input type="hidden" name="modifiedby" value="${timeTableBean.modifiedBy}">
			<input type="hidden" name="createddatetime"
				value="${timeTableBean.createdDatetime}"> <input type="hidden"
				name="modifiedby" value="${timeTableBean.modifiedDatetime}">
			<table class="table table-borderless w-50">
				<tr>
					<th class="text-left"><label for="courseId">Course <span class="required">*</span>:</label></th>
					<td>${HTMLUtility.getList("courseId", timeTableBean.courseId, courseList)}
						<span class="error">${requestScope.courseId}</span></td>
				</tr>

				<tr>
					<th class="text-left"><label for="subjectId">Subject <span class="required">*</span>:</label></th>
					<td>${HTMLUtility.getList("subjectId", timeTableBean.subjectId,
						subjectList)}
						<span class="error">${requestScope.subjectId}</span></td>
				</tr>

				<tr>
                <th class="text-left"><label for="semester">Semester<span
							class="required">*</span> :</label></th>

					<td><select name="semester" id="semester"
						class="form-control">
							<option value="">Select Semester</option>
							<option value="1st"${timeTableBean.semester == '1st' ? 'selected' : ''}>1st</option>
							<option value="2nd"${timeTableBean.semester == '2nd' ? 'selected' : ''}>2nd</option>
							<option value="3rd"${timeTableBean.semester == '3rd' ? 'selected' : ''}>3rd</option>
							<option value="4th"${timeTableBean.semester == '4th' ? 'selected' : ''}>4th</option>
							<option value="5th"${timeTableBean.semester == '5th' ? 'selected' : ''}>5th</option>
							<option value="6th"${timeTableBean.semester == '6th' ? 'selected' : ''}>6th</option>
							<option value="7th"${timeTableBean.semester == '7th' ? 'selected' : ''}>7th</option>
							<option value="8th"${timeTableBean.semester == '8th' ? 'selected' : ''}>8th</option>
					</select>
						<span class="error">${requestScope.semester}</span></td>
				</tr>
				<tr>
                <th class="text-left"><label for="udate5">Exam Date <span
							class="required">*</span> :</label></th>
                    <td><input type="text" readonly="readonly" id="udate5"
                               placeholder="Select Exam Date" name="ExDate" value="${timeTableBean.examDate}" class="form-control">
                        <span class="error">${requestScope.ExDate}</span></td>
                </tr>
				<tr>
					<th align="left"><label for="ExTime">Exam Time <span
							class="required">*</span> :</label></th>
					<td><select name="ExTime" id="ExTime" class="form-control">
							<option value="">Select Time</option>	<option value="08:00 AM to 11:00 AM"${timeTableBean.examTime == '08:00 AM to 11:00 AM' ? 'selected' : ''}>08:00 AM to 11:00 AM</option>
							<option value="12:00 PM to 03:00 PM"${timeTableBean.examTime == '12:00 PM to 03:00 PM' ? 'selected' : ''}>12:00 PM to 03:00 PM</option>
							<option value="04:00 PM to 07:00 PM"${timeTableBean.examTime == '04:00 PM to 07:00 PM' ? 'selected' : ''}>04:00 PM to 07:00 PM</option>
					</select>
						<span class="error">${requestScope.ExTime}</span></td>
				</tr>
				<tr>
					<th></th>
					<td>
						<div class="button-container">
							<c:choose>	
								<c:when test="${not empty bean.id && bean.id > 0}">
									<input type="submit" name="operation" class="btn btn-primary"
										value="<%=TimetableCtl.OP_UPDATE%>"> <input type="submit"
										name="operation" class="btn btn-secondary"
										value="<%=TimetableCtl.OP_CANCEL%>">
								</c:when>
								<c:otherwise>
									<input type="submit" name="operation" class="btn btn-primary"
										value="<%=TimetableCtl.OP_SAVE%>"> <input type="submit"
										name="operation" class="btn btn-secondary"
										value="<%=TimetableCtl.OP_RESET%>">
								</c:otherwise>
							</c:choose>
						</div>
					</td>
				</tr>
			</table>
		</div>
	</form>
	<%@include file="Footer.jsp"%>
	<%@ page import="com.rays.pro4.util.HTMLUtility"%>
</body>
</html>