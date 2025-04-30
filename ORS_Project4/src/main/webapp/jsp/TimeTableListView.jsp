<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@page import="com.rays.pro4.controller.TimetableListCtl"%>
<%@page import="com.rays.pro4.Util.HTMLUtility"%>
<%@page import="com.rays.pro4.Util.ServletUtility"%>
<%@page import="com.rays.pro4.Model.TimeTableModel"%>
<%@page import="com.rays.pro4.Bean.TimeTableBean"%>
<%@page import="com.rays.pro4.controller.ORSView"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<html>
<head>
<link rel="icon" type="image/png" href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16*16"/>
<title> TimeTable List</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">

  <script src="<%=ORSView.APP_CONTEXT %>/js/jquery.min.js"></script>
<script src="<%=ORSView.APP_CONTEXT %>/js/Checkbox11.js"></script>
  

<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
  <link rel="stylesheet" href="/resources/demos/style.css">
  <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
  <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
  
  
 

  <script>
  
  function disableSunday(d){
	  var day = d.getDay();
	  if(day==0)
	  {
	   return [false];
	  }else
	  {
		  return [true];
	  }
  }
  
  $( function() {
	  $( "#abcd" ).datepicker({
		  changeMonth :true,
		  changeYear :true,
		  yearRange :'0:+10',
		  dateFormat:'dd-mm-yy',

// Disable for Sunday
		  beforeShowDay : disableSunday,		  
// Disable for back date
		  minDate : 0   
	  });
  } );
  </script>

</head>
<body >
<jsp:useBean id="bean" class="com.rays.pro4.Bean.TimeTableBean" scope="request"></jsp:useBean>
<%@include file = "Header.jsp" %>


<form action="<%=ORSView.TIMETABLE_LIST_CTL %>" method="post"> 
	
	<div class="text-center">
		<h1>TimeTable List</h1>

		<c:if test="${not empty error}">
			<div class="error-message">${error}</div>
		</c:if>
		<c:if test="${not empty success}">
			<div class="success-message">${success}</div>
		</c:if>
	</div>
	<c:set var="cList" value="${requestScope.courseList}" />
	<c:set var="sList" value="${requestScope.subjectList}" />
	<c:set var="eList" value="${requestScope.examtime}" />
	<c:set var="next" value="${requestScope.nextlist}" />
	<c:set var="pageNo" value="${param.pageNo}" />
	<c:set var="pageSize" value="${param.pageSize}" />
	<c:set var="index" value="${(pageNo - 1) * pageSize + 1}" />

	<c:set var="list" value="${requestScope.list}" />

	<c:if test="${not empty list}">
		<table class="w-100">
			<tr>
				<td class="text-center">
					<label for="clist">Course Name :</label>
					${HTMLUtility.getList("clist", bean.courseId, cList)}
					<label for="slist">Subject Name :</label>
					${HTMLUtility.getList("slist", bean.subjectId, sList)}

					<input type="submit" name="operation" value="<%=TimetableListCtl.OP_SEARCH%>" class="btn btn-primary">
					&nbsp;
					<input type="submit" name="operation" value="<%=TimetableListCtl.OP_RESET %>" class="btn btn-secondary">
				</td>
			</tr>
		</table>
		<br>
		<table border="1" class="w-100" cellpadding="6px" cellspacing=".2">
			<tr class="bg-skyblue">
				<th width="5%"><input type="checkbox" id="select_all" name="Select">Select All.</th>
				<th>S.No.</th>
				<th>Course Name.</th>
				<th>Subject Name.</th>
				<th>Semester.</th>
				<th>ExamDate.</th>
				<th>ExamTime.</th>
				<th>Edit</th>
			</tr>
			<c:forEach var="bean" items="${list}">
				<tr class="text-center">
					<td><input type="checkbox" class="checkbox" name="ids" value="${bean.id}"></td>
					<td><c:out value="${index}" /> <c:set var="index" value="${index + 1}" /></td>
					<td>${bean.courseName}</td>
					<td>${bean.subjectName}</td>
					<td>${bean.semester}</td>
					<td>${bean.examDate}</td>
					<td>${bean.examTime}</td>
					<td><a href="TimeTableCtl?id=${bean.id}">Edit</a></td>
				</tr>
			</c:forEach>
		</table>

		<table class="w-100">
			<tr>
				<th></th>
				<c:choose>
					<c:when test="${pageNo == 1}">
						<td class="text-left"><input type="submit" name="operation" disabled="disabled" value="<%=TimetableListCtl.OP_PREVIOUS%>" class="btn btn-primary"></td>
					</c:when>
					<c:otherwise>
						<td class="text-left"><input type="submit" name="operation" value="<%=TimetableListCtl.OP_PREVIOUS%>" class="btn btn-primary"></td>
					</c:otherwise>
				</c:choose>

				<td><input type="submit" name="operation" value="<%=TimetableListCtl.OP_DELETE%>" class="btn btn-danger"></td>
				<td><input type="submit" name="operation" value="<%=TimetableListCtl.OP_NEW%>" class="btn btn-success"></td>

				<c:choose>
					<c:when test="${list.size() < pageSize || next == 0}">
						<td class="text-right"><input type="submit" name="operation" disabled="disabled" value="<%=TimetableListCtl.OP_NEXT%>" class="btn btn-primary"></td>
					</c:when>
					<c:otherwise>
						<td class="text-right"><input type="submit" name="operation" value="<%=TimetableListCtl.OP_NEXT%>" class="btn btn-primary"></td>
					</c:otherwise>
				</c:choose>
			</tr>
		</table>
	</c:if>
	<c:if test="${empty list}">
		<td class="text-center"><input type="submit" name="operation" value="<%=TimetableListCtl.OP_BACK%>" class="btn btn-primary"></td>
	</c:if>
	<input type="hidden" name="pageNo" value="${pageNo}">
	<input type="hidden" name="pageSize" value="${pageSize}">
</form>
</br>
</br>
</br>
                   </br>
                   </br>
                   </br>

<%@include file = "Footer.jsp" %>
</body>
</html>