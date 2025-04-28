<%@page import="com.rays.pro4.Bean.SubjectBean"%>
<%@page import="com.rays.pro4.Bean.CollegeBean"%>
<%@page import="com.rays.pro4.Bean.CourseBean"%>
<%@page import="java.util.List"%>
<%@page import="com.rays.pro4.Util.HTMLUtility"%>+
<%@page import="java.util.HashMap"%>
<%@page import="com.rays.pro4.controller.FacultyCtl"%>
<%@page import="com.rays.pro4.Util.DataUtility"%>
<%@page import="com.rays.pro4.Util.ServletUtility"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" isELIgnored="false"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<link rel="icon" type="image/png" href="<%=ORSView.APP_CONTEXT%>/img/logo.png"
	sizes="16*16" />
<title>Faculty Registration Page</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="<%=ORSView.APP_CONTEXT%>/js/jquery-ui-1.12.1/jquery-ui.css">
<link rel="stylesheet" href="<%=ORSView.APP_CONTEXT%>/css/style.css">
<script src="<%=ORSView.APP_CONTEXT%>/js/jquery-1.12.4.js"></script>
<script src="<%=ORSView.APP_CONTEXT%>/js/jquery-ui-1.12.1/jquery-ui.js"></script>
<script>
	$(function() {
		$("#date").datepicker({
			changeMonth: true,
			changeYear: true,
			yearRange: '1980:2020',
		});
	});
</script>
</head>
<body>
	<jsp:useBean id="bean" class="com.rays.pro4.Bean.FacultyBean"
		scope="request"></jsp:useBean>
	<%@include file="Header.jsp"%>
	<form action="<%=ORSView.FACULTY_CTL%>" method="post">
		<%
			List<CollegeBean> colist = (List<CollegeBean>) request.getAttribute("CollegeList");
			List<CourseBean> clist = (List<CourseBean>) request.getAttribute("CourseList");
			List<SubjectBean> slist = (List<SubjectBean>) request.getAttribute("SubjectList");
		%>
		<div class="container">
			<h1>
				<%=bean.getId() > 0 ? "Update Faculty" : "Add Faculty"%>
			</h1>
			<div>
				<div class="message">
					<%=ServletUtility.getSuccessMessage(request)%>
					<%=ServletUtility.getErrorMessage(request)%>
				</div>
			</div>
			<input type="hidden" name="id" value="<%=bean.getId()%>"> <input type="hidden" name="createdby" value="<%=bean.getCreatedBy()%>"> <input
				type="hidden" name="modifiedby" value="<%=bean.getModifiedBy()%>"> <input type="hidden" name="createdDatetime"
				value="<%=DataUtility.getStringData(bean.getCreatedDatetime())%>"> <input type="hidden" name="modifiedDatetime"
				value="<%=DataUtility.getStringData(bean.getModifiedDatetime())%>">

			<div class="input-container">
				<label>First Name <span class="required">*</span> :</label> <input type="text" name="firstname" placeholder=" Enter First Name"
					size="25" value="<%=DataUtility.getStringData(bean.getFirstName())%>">
				<div class="error">
					<%=ServletUtility.getErrorMessage("firstname", request)%>
				</div>
			</div>
			<div class="input-container">
				<label>Last Name <span class="required">*</span> :</label> <input type="text" name="lastname" placeholder=" Enter last Name" size="25"
					value="<%=DataUtility.getStringData(bean.getLastName())%>">
				<div class="error">
					<%=ServletUtility.getErrorMessage("lastname", request)%>
				</div>
			</div>
			<div class="input-container">
				<label>Gender <span class="required">*</span> :</label>
				<%
					HashMap map = new HashMap();
					map.put("Male", "Male");
					map.put("Female", "Female");
					String hlist = HTMLUtility.getList("gender", String.valueOf(bean.getGender()), map);
				%> <%=hlist%>
				<div class="error">
					<%=ServletUtility.getErrorMessage("gender", request)%>
				</div>
			</div>
			<div class="input-container">
				<label>CollegeName <span class="required">*</span> :</label>
				<%=HTMLUtility.getList("collegeid", String.valueOf(bean.getCollegeId()), colist)%>
				<div class="error">
					<%=ServletUtility.getErrorMessage("collegeid", request)%>
				</div>
			</div>
			<div class="input-container">
				<label>CourseName <span class="required">*</span> :</label>
				<%=HTMLUtility.getList("courseid", String.valueOf(bean.getCourseId()), clist)%>
				<div class="error">
					<%=ServletUtility.getErrorMessage("courseid", request)%>
				</div>
			</div>
			<div class="input-container">
				<label>SubjectName <span class="required">*</span> :</label>
				<%=HTMLUtility.getList("subjectid", String.valueOf(bean.getSubjectId()), slist)%>
				<div class="error">
					<%=ServletUtility.getErrorMessage("subjectid", request)%>
				</div>
			</div>
			<div class="input-container">
				<label>Date Of Birth <span class="required">*</span> :</label> <input type="text" name="dob" placeholder="Enter Date Of Birth" size="25"
					readonly="readonly" id="date" value="<%=DataUtility.getDateString(bean.getDob())%>">
				<div class="error">
					<%=ServletUtility.getErrorMessage("dob", request)%>
				</div>
			</div>
			<div class="input-container">
				<label>LoginId <span class="required">*</span> :</label> <input type="text" name="loginid" placeholder=" Enter Login Id" size="25"
					value="<%=DataUtility.getStringData(bean.getEmailId())%>">
				<div class="error">
					<%=ServletUtility.getErrorMessage("loginid", request)%>
				</div>
			</div>
			<div class="input-container">
				<label>MobileNo <span class="required">*</span> :</label> <input type="text" name="mobileno" size="25" maxlength="10"
					placeholder=" Enter Mobile No" value="<%=DataUtility.getStringData(bean.getMobileNo())%>">
				<div class="error">
					<%=ServletUtility.getErrorMessage("mobileno", request)%>
				</div>
			</div>
			<div class="button-container">
				<%
					if (bean.getId() > 0) {
				%>
				<input type="submit" name="operation" value="<%=FacultyCtl.OP_UPDATE%>"> <input type="submit" name="operation" value="<%=FacultyCtl.OP_CANCEL%>">
				<%
				} else {
				%>
				<input type="submit" name="operation" value="<%=FacultyCtl.OP_SAVE%>"> <input type="submit" name="operation" value="<%=FacultyCtl.OP_RESET%>">
				<%
					}
				%>
			</div>
		</div>
	</form>
	<%@include file="Footer.jsp"%>
</body>
</html>

	<form action="<%=ORSView.FACULTY_CTL%>" method="post">

		<%
			List<CollegeBean> colist = (List<CollegeBean>) request.getAttribute("CollegeList");
			List<CourseBean> clist = (List<CourseBean>) request.getAttribute("CourseList");
			List<SubjectBean> slist = (List<SubjectBean>) request.getAttribute("SubjectList");
		%>

		<div align="center">
			<div>
				<h1>
					<%
						if (bean.getId() > 0) {
					%>
					Update Faculty
					<%
						} else {
					%>
					Add Faculty
					<%
						}
					%>
				</h1>
				<div>
					<span class="success"> <%=ServletUtility.getSuccessMessage(request)%>
					</span> <span class="error"> <%=ServletUtility.getErrorMessage(request)%>
					</span>
				</div>
			</div>

			<input type="hidden" name="id" value=<%=bean.getId()%>> 
			<input type="hidden" name="createdby" value=<%=bean.getCreatedBy()%>>
			<input type="hidden" name="modifiedby" value=<%=bean.getModifiedBy()%>>
			 <input type="hidden" name="createdDatetime" value=<%=DataUtility.getStringData(bean.getCreatedDatetime())%>>
			<input type="hidden" name="modifiedDatetime" value=<%=DataUtility.getStringData(bean.getModifiedDatetime())%>>

			<div class="row">
				<div>
					<label>First Name <span class="required">*</span> :</label>
				</div>
				<div>
					<input type="text" name="firstname"
						placeholder=" Enter First Name" size="25"
						value="<%=DataUtility.getStringData(bean.getFirstName())%>">
				</div>
				<div class="error">
					<%=ServletUtility.getErrorMessage("firstname", request)%>
				</div>
			</div>

			<div class="row">
				<div>
					<label>Last Name <span class="required">*</span> :</label>
				</div>
				<div>
					<input type="text" name="lastname" placeholder=" Enter last Name"
						size="25" value="<%=DataUtility.getStringData(bean.getLastName())%>">
				</div>
				<div class="error">
					<%=ServletUtility.getErrorMessage("lastname", request)%>
				</div>
			</div>

			<div class="row">
				<div>
					<label>Gender <span class="required">*</span> :</label>
				</div>
				<div>
					<%
						HashMap map = new HashMap();
						map.put("Male", "Male");
						map.put("Female", "Female");

						String hlist = HTMLUtility.getList("gender",
								String.valueOf(bean.getGender()), map);
					%> <%=hlist%>
				</div>
				<div class="error">
					<%=ServletUtility.getErrorMessage("gender", request)%>
				</div>
			</div>

			<div class="row">
				<div>
					<label>CollegeName <span class="required">*</span> :</label>
				</div>
				<div>
					<%=HTMLUtility.getList("collegeid", String.valueOf(bean.getCollegeId()), colist)%>
				</div>
				<div class="error">
					<%=ServletUtility.getErrorMessage("collegeid", request)%>
				</div>
			</div>

			<div class="row">
				<div>
					<label>CourseName <span class="required">*</span> :</label>
				</div>
				<div>
					<%=HTMLUtility.getList("courseid", String.valueOf(bean.getCourseId()), clist)%>
				</div>
				<div class="error">
					<%=ServletUtility.getErrorMessage("courseid", request)%>
				</div>
			</div>

			<div class="row">
				<div>
					<label>SubjectName <span class="required">*</span> :</label>
				</div>
				<div>
					<%=HTMLUtility.getList("subjectid", String.valueOf(bean.getSubjectId()), slist)%>
				</div>
				<div class="error">
					<%=ServletUtility.getErrorMessage("subjectid", request)%>
				</div>
			</div>

			<div class="row">
				<div>
					<label>Date Of Birth <span class="required">*</span> :</label>
				</div>
				<div>
					<input type="text" name="dob" placeholder="Enter Date Of Birth"
						size="25" readonly="readonly" id="date"
						value="<%=DataUtility.getDateString(bean.getDob())%>">
				</div>
				<div class="error">
					<%=ServletUtility.getErrorMessage("dob", request)%>
				</div>
			</div>

			<div class="row">
				<div>
					<label>LoginId <span class="required">*</span> :</label>
				</div>
				<div>
					<input type="text" name="loginid" placeholder=" Enter Login Id"
						size="25" value="<%=DataUtility.getStringData(bean.getEmailId())%>">
				</div>
				<div class="error">
					<%=ServletUtility.getErrorMessage("loginid", request)%>
				</div>
			</div>

			<div class="row">
				<div>
					<label>MobileNo <span class="required">*</span> :</label>
				</div>
				<div>
					<input type="text" name="mobileno" size="25" maxlength="10"
						placeholder=" Enter Mobile No"
						value="<%=DataUtility.getStringData(bean.getMobileNo())%>">
				</div>
				<div class="error">
					<%=ServletUtility.getErrorMessage("mobileno", request)%>
				</div>
			</div>

			<%
				if (bean.getId() > 0) {
			%>
			<div class="row">
				<input type="submit" name="operation" value="<%=FacultyCtl.OP_UPDATE%>">
				<input type="submit" name="operation" value="<%=FacultyCtl.OP_CANCEL%>">
			</div>
			<%
				} else {
			%>
			<div class="row">
				<input type="submit" name="operation" value="<%=FacultyCtl.OP_SAVE%>">
				<input type="submit" name="operation" value="<%=FacultyCtl.OP_RESET%>">
			</div>
			<%
				}
			%>
		</div>

	</form>
	<%@include file="Footer.jsp"%>
</body>
<style>
	.row {
		display: flex;
		flex-direction: column;
		margin-bottom: 10px; /* Espacio entre las filas */
	}
	
	.required {
		color: red;
	}
	
	.error {
		color: red;
		font-size: 14px; /* Ajusta el tamaño de la fuente del mensaje de error */
		margin-top: 5px; /* Añade un poco de espacio encima del mensaje de error */
	}
	
	.success {
		color: green;
		font-size: 14px;
		margin-top: 5px;
	}
	</style>
</html>