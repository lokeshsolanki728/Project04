<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@page import="com.rays.pro4.controller.TimetableCtl"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="com.rays.pro4.Bean.TimeTableBean"%>
<%@page import="java.util.List"%>
<%@page import="com.rays.pro4.Util.DataUtility"%>
<%@page import="com.rays.pro4.controller.ORSView"%>
<%@page import="com.rays.pro4.Util.ServletUtility"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<link rel="icon" type="image/png" href="${pageContext.request.contextPath}/img/logo.png" sizes="16*16" />
<title>TimeTable Register Page</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
<meta charset="utf-8">
  <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/demos/style.css">
  <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
  <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>

  <script>
  function disableSunday(d) {
		var day = d.getDay();
		if (day == 0) {
			return [ false ];
		} else {
			return [ true ];
		}
	}

	$(function() {
		$("#udate5").datepicker({
			changeMonth : true,
			changeYear : true,
			yearRange : '0:+2',
			dateFormat : 'mm/dd/yy',

			//Disable for Sunday
			beforeShowDay : disableSunday,
			// Disable for back date
			minDate : 0
		});
	});	  </script>


<style type="text/css">
</style>
</head>
<body>
	<jsp:useBean id="bean" class="com.rays.pro4.Bean.TimeTableBean" scope="request"></jsp:useBean>

	<form action="${pageContext.request.contextPath}${ORSView.TIMETABLE_CTL}" method="post">

		<%@include file="Header.jsp"%>

		<center>
			<c:set var="courseList" value="${requestScope.CourseList}" />
			<c:set var="subjectList" value="${requestScope.SubjectList}" />

			<input type="hidden" name="id" value="${bean.id}">
			<input type="hidden" name="createdby" value="${bean.createdBy}">
			<input type="hidden" name="modifiedby" value="${bean.modifiedBy}">
			<input type="hidden" name="createddatetime" value="${bean.createdDatetime}">
			<input type="hidden" name="modifiedby" value="${bean.modifiedDatetime}">

			<div align="center">
				<h1>
					<c:choose>
						<c:when test="${not empty bean.id && bean.id > 0}">
							Update TimeTable
						</c:when>
						<c:otherwise>
							Add TimeTable
						</c:otherwise>
					</c:choose>
				</h1>

				<h3 align="center">
					<div class="error-message">${requestScope.errorMessage}</div>
				</h3>
				<h3 align="center">
					<div class="success-message">${requestScope.successMessage}</div>
				</h3>
			</div>

			<table>
				<tr >
					<th align="left"><label for="courseId">Course <span class="required">*</span> :</label></th>
					<td>${HTMLUtility.getList("courseId", bean.courseId, courseList)}</td>
					<td class="error-message">${requestScope.courseId}</td>
				</tr>

				<tr>
					<th style="padding: 3px"></th>
				</tr>

				<tr>
					<th align="left"><label for="subjectId">Subject <span class="required">*</span> :</label></th>
					<td>${HTMLUtility.getList("subjectId", bean.subjectId, subjectList)}</td>
					<td class="error-message">${requestScope.subjectId}</td>
				</tr>

				<tr>
					<th style="padding: 3px"></th>
				</tr>

				<tr>
					<th align="left"><label for="semester">Semester<span class="required">*</span> :</label></th>
					<td><select name="semester" id="semester">
							<option value="">Select Semester</option>
							<option value="1st" ${bean.semester == '1st' ? 'selected' : ''}>1st</option>
							<option value="2nd" ${bean.semester == '2nd' ? 'selected' : ''}>2nd</option>
							<option value="3rd" ${bean.semester == '3rd' ? 'selected' : ''}>3rd</option>
							<option value="4th" ${bean.semester == '4th' ? 'selected' : ''}>4th</option>
							<option value="5th" ${bean.semester == '5th' ? 'selected' : ''}>5th</option>
							<option value="6th" ${bean.semester == '6th' ? 'selected' : ''}>6th</option>
							<option value="7th" ${bean.semester == '7th' ? 'selected' : ''}>7th</option>
							<option value="8th" ${bean.semester == '8th' ? 'selected' : ''}>8th</option>
						</select></td>
					<td class="error-message">${requestScope.semester}</td>
				</tr>

				<tr>
					<th style="padding: 3px"></th>
				</tr>

				<tr>
					<th align="left"><label for="udate5">Exam Date <span class="required">*</span> :</label></th>
					<td><input type="text" readonly="readonly" id="udate5" size="25" placeholder="Select Date" name="ExDate" value="${bean.examDate}"></td>
					<td class="error-message">${requestScope.ExDate}</td>
				</tr>

				<tr>
					<th style="padding: 3px"></th>
				</tr>

				<tr>
					<th align="left"><label for="ExTime">Exam Time <span class="required">*</span> :</label></th>
					<td><select name="ExTime" id="ExTime">
					<option value="">Select Time</option>
					<option value="08:00 AM to 11:00 AM" ${bean.examTime == '08:00 AM to 11:00 AM' ? 'selected' : ''}>08:00 AM to 11:00 AM</option>
					<option value="12:00 PM to 03:00 PM" ${bean.examTime == '12:00 PM to 03:00 PM' ? 'selected' : ''}>12:00 PM to 03:00 PM</option>
					<option value="04:00 PM to 07:00 PM" ${bean.examTime == '04:00 PM to 07:00 PM' ? 'selected' : ''}>04:00 PM to 07:00 PM</option>
					</select>
				</td>
					<td class="error-message">${requestScope.ExTime}</td>
				</tr>
				<tr>
					<th style="padding: 3px"></th>
					<td></td>
				</tr>

				<tr align="center">
					<c:choose>
						<c:when test="${not empty bean.id && bean.id > 0}">
							<td colspan="2">
								&emsp; &emsp; &emsp; <input type="submit" name="operation" value="<%=TimetableCtl.OP_UPDATE%>"> &nbsp; &nbsp; <input
								type="submit" name="operation" value="<%=TimetableCtl.OP_CANCEL%>">
							</td>
						</c:when>
						<c:otherwise>
							<td colspan="2">
								&nbsp; &emsp; <input type="submit" name="operation" value="<%=TimetableCtl.OP_SAVE%>"> &nbsp; &nbsp; <input
								type="submit" name="operation" value="<%=TimetableCtl.OP_RESET%>">
							</td>
						</c:otherwise>
					</c:choose>
				</tr>
			</table>
		</form>
	</center>

	<%@include file="Footer.jsp"%>
</body>
</html>