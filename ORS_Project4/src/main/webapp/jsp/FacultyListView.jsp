<%@page import="com.rays.pro4.Bean.FacultyBean"%>
<%@page import="java.util.List"%>
<%@page import="com.rays.pro4.Util.ServletUtility"%>
<%@page import="com.rays.pro4.Util.DataUtility"%>
<%@page import="com.rays.pro4.Util.HTMLUtility"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.rays.pro4.controller.FacultyListCtl"%>
<%@page import="com.rays.pro4.controller.ORSView"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<html>
<head>
<link rel="icon" type="image/png" href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16*16" />
<title>Faculty List</title>
<link rel="stylesheet" href="<%=ORSView.APP_CONTEXT%>/css/style.css">
<script src="<%=ORSView.APP_CONTEXT%>/js/jquery.min.js"></script>
<script src="<%=ORSView.APP_CONTEXT%>/js/Checkbox11.js"></script>
</head>
<body>
	<%@include file="Header.jsp"%>	
	<jsp:useBean id="facultyBean" class="com.rays.pro4.Bean.FacultyBean" scope="request"></jsp:useBean>
	<jsp:useBean id="collegeBean" class="com.rays.pro4.Bean.CollegeBean" scope="request"></jsp:useBean>
	<jsp:useBean id="courseBean" class="com.rays.pro4.Bean.CourseBean" scope="request"></jsp:useBean>

	<form action="<%=ORSView.FACULTY_LIST_CTL%>" method="post">	
		<div class="container">
			<h1>Faculty List</h1>
			<div>
				<span class="success-message"> <%=ServletUtility.getSuccessMessage(request)%></span>
				<span class="error-message"> <%=ServletUtility.getErrorMessage(request)%></span>
			</div>			
			<%
			    List<com.rays.pro4.Bean.CollegeBean> collegeList = (List) request.getAttribute("CollegeList");
			    List<com.rays.pro4.Bean.CourseBean> courseList = (List) request.getAttribute("CourseList");
			    int next = DataUtility.getInt(request.getAttribute("nextlist").toString());
			    int pageNo = ServletUtility.getPageNo(request);
			    int pageSize = ServletUtility.getPageSize(request);
			    int index = (pageNo - 1) * pageSize + 1;
			    List<FacultyBean> facultyList = ServletUtility.getList(request);
			    Iterator<FacultyBean> it = facultyList.iterator();
			    if (!facultyList.isEmpty()) {
			%>			
			<div class="search-container">
			  <label for="firstname">First Name : <input type="text" id="firstname" name="firstname" placeholder="Enter First Name" value=<%=ServletUtility.getParameter("firstname", request)%>> </label>
			  <label for="collegeid">College Name : <%=HTMLUtility.getList("collegeid", String.valueOf(facultyBean.getCollegeId()), collegeList)%></label>
			  <label for="courseid">Course Name : <%=HTMLUtility.getList("courseid",String.valueOf(facultyBean.getCourseId()), courseList)%></label>
			  <input type="submit" name="operation" value="<%=FacultyListCtl.OP_SEARCH%>">
			  <input type="submit" name="operation" value="<%=FacultyListCtl.OP_RESET%>">
			</div>
			<table class="list-table">
				<tr>
					<th><input type="checkbox" id="select_all" name="Select"> Select All</th>
					<th>S.No.</th><th>First Name.</th><th>Last Name.</th><th>EmailId.</th>
					<th>College Name.</th><th>Course Name.</th><th>Subject Name.</th>
					<th>DOB.</th><th>Mobile No.</th><th>Edit</th>
				</tr>				
				<%
				    while (it.hasNext()) {
				    	facultyBean = it.next();
				%>
				<tr>
					<td><input type="checkbox" class="checkbox" name="ids" value="<%=facultyBean.getId()%>"></td>
					<td><%=index++%></td><td><%=facultyBean.getFirstName()%></td>
					<td><%=facultyBean.getLastName()%></td><td><%=facultyBean.getEmailId()%></td>
					<td><%=facultyBean.getCollegeName()%></td><td><%=facultyBean.getCourseName()%></td>
					<td><%=facultyBean.getSubjectName()%></td><td><%=facultyBean.getDob()%></td>
					<td><%=facultyBean.getMobileNo()%></td><td><a href="FacultyCtl?id=<%=facultyBean.getId()%>">Edit</a></td>
				</tr>
				<%
				    }
				%>
			</table>
			<div class="button-container">
				<%
				    if (pageNo == 1) {
				%>
				<input type="submit" name="operation" disabled="disabled" value="<%=FacultyListCtl.OP_PREVIOUS%>">
				<%
				    } else {
				%>
				<input type="submit" name="operation" value="<%=FacultyListCtl.OP_PREVIOUS%>">
				<%
				    }
				%>
				<input type="submit" name="operation" value="<%=FacultyListCtl.OP_DELETE%>">
				<input type="submit" name="operation" value="<%=FacultyListCtl.OP_NEW%>">
				<input type="submit" name="operation" value="<%=FacultyListCtl.OP_NEXT%>" <%=((facultyList.size() < pageSize || next == 0) ? "disabled" : "")%>>
			</div>
			<%
			    }
			    if (facultyList.isEmpty()) {
			%>
			<div class="button-container">
				<input type="submit" name="operation" value="<%=FacultyListCtl.OP_BACK%>">
			</div>
			<%
			    }
			%>
			<input type="hidden" name="pageNo" value="<%=pageNo%>">
			<input type="hidden" name="pageSize" value="<%=pageSize%>">
		</div>
	</form>
	<%@include file="Footer.jsp"%>	
</body>
</html>