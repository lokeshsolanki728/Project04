<%@page import="com.rays.pro4.Util.DataUtility"%>
<%@page import="com.rays.pro4.Util.HTMLUtility"%>
<%@page import="com.rays.pro4.controller.CourseListCtl"%>
<%@page import="java.util.List"%>
<%@page import="com.rays.pro4.Bean.CourseBean"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.rays.pro4.Util.ServletUtility"%>
<%@page import="com.rays.pro4.controller.ORSView"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<html>
<head>
    <link rel="icon" type="image/png" href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16*16" />
    <title>Course List</title>
    <link rel="stylesheet" href="<%=ORSView.APP_CONTEXT%>/css/style.css">
</head>

<body>
	<jsp:useBean id="bean" class="com.rays.pro4.Bean.CourseBean" scope="request"></jsp:useBean>
	<form action="<%=ORSView.COURSE_LIST_CTL%>" method="post">
		<%@include file="Header.jsp"%>

		<div class="list-container">
			<h1 align="center">Course List</h1>
			<div align="center">
				<span class="error-message">${error}</span> <span class="success-message">${success}</span>
			</div>
			<%
			    List<CourseBean> list = (List<CourseBean>) request.getAttribute("CourseList");
			    int nextPage = DataUtility.getInt(request.getAttribute("nextlist").toString());
			    int pageNo = ServletUtility.getPageNo(request);
			    int pageSize = ServletUtility.getPageSize(request);
			    int index = (pageNo - 1) * pageSize + 1;
			    Iterator<CourseBean> it = list.iterator();
			    if (!list.isEmpty()) {
			%>
			<div class="search-options">
			<label>Course Name : <%=HTMLUtility.getList("cname", String.valueOf(bean.getId()), list)%></label>
				<input type="submit" name="operation" value="<%=CourseListCtl.OP_SEARCH%>">
				<input type="submit" name="operation" value="<%=CourseListCtl.OP_RESET%>">
			</div>
			<table class="table">
				<thead>
					<tr>
						<th><input type="checkbox" id="select_all" name="select">Select All</th>
						<th>S.NO.</th>
						<th>Course Name.</th>
						<th>Duration.</th>
						<th>Description.</th>
						<th>Edit</th>
					</tr>
				</thead>
				<tbody>
					<%
					    while (it.hasNext()) {
					        bean = it.next();
					%>
					<tr align="center">
						<td><input type="checkbox" class="checkbox" name="ids" value="<%=bean.getId()%>"></td>
						<td><%=index++%></td>
						<td><%=bean.getName()%></td>
						<td><%=bean.getDuration()%></td>
						<td><%=bean.getDescription()%></td>
						<td><a href="CourseCtl?id=<%=bean.getId()%>">Edit</a></td>
					</tr>
					<%
					    }
					%>
				</tbody>
			</table>
			<div class="pagination">
				<input type="submit" name="operation" value="<%=CourseListCtl.OP_PREVIOUS%>" <%=pageNo == 1 ? "disabled" : ""%>>
				<input type="submit" name="operation" value="<%=CourseListCtl.OP_DELETE%>">
				<input type="submit" name="operation" value="<%=CourseListCtl.OP_NEW%>">
				<input type="submit" name="operation" value="<%=CourseListCtl.OP_NEXT%>" <%=(list.size() < pageSize || nextPage == 0) ? "disabled" : ""%>>
			</div>
			<%
			    } else {
			%>
			<div align="center">
				<input type="submit" name="operation" value="<%=CourseListCtl.OP_BACK%>">
			</div>
			<%
			    }
			%>
			<input type="hidden" name="pageNo" value="<%=pageNo%>">
			<input type="hidden" name="pageSize" value="<%=pageSize%>">
		</div>
	</form>
	<script src="<%=ORSView.APP_CONTEXT%>/js/jquery.min.js"></script>
	<script src="<%=ORSView.APP_CONTEXT%>/js/Checkbox11.js"></script>
	<%@include file="Footer.jsp"%>
</body>
</html>