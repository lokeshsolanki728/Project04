<%@page import="com.rays.pro4.controller.CollegeListCtl"%> 
<%@page import="com.rays.pro4.controller.ORSView"%> 
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%> 
 
<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\"> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<link rel="icon" type="image/png" href="${pageContext.request.contextPath}/img/logo.png" sizes="16*16" />
<title>College List</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/demos/style.css"> 
 <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/Checkbox11.js"></script>
</head> 
<body> 
    <jsp:useBean id="bean" class="com.rays.pro4.Bean.CollegeBean" scope="request"></jsp:useBean> 
    <%@ include file="Header.jsp"%> 
    <c:set var="nextlist" value="${requestScope.nextlist}" /> 
    <c:set var="collegeList" value="${requestScope.CollegeList}" /> 
    <c:set var="list" value="${requestScope.list}" /> 
    <c:set var="pageNo" value="${requestScope.pageNo}" /> 
    <c:set var="pageSize" value="${requestScope.pageSize}" /> 
    <c:set var="index" value="${(pageNo-1) * pageSize + 1}" /> 
    <form action="<%=ORSView.COLLEGE_LIST_CTL%>" method="POST"> 
        <div class="container-fluid"> 
            <h1 align="center">College List</h1> 
            <div class="message-container"> 
                <span class="error-message">${requestScope.errorMessage}</span> 
                <span class="success-message">${requestScope.successMessage}</span> 
            </div> 
            <c:if test="${not empty list}"> 
            <div class="search-container"> 
                <label for="collegeid">College Name :</label> 
                 <select name="collegeid">
                 <option value="">Select College</option>
                  <c:forEach items="${collegeList}" var="col" >
                     <option value="${col.id}" ${col.id == bean.id ? 'selected' : ''}>${col.name}</option>
                  </c:forEach>
                 </select>
                <label for="city">City :</label> 
                <input type="text" id="city" name="city" placeholder="Enter City Name" value="${param.city}"> 
                <input type="submit" name="operation" value="<%=CollegeListCtl.OP_SEARCH%>"> 
                <input type="submit" name="operation" value="<%=CollegeListCtl.OP_RESET%>"> 
            </div> 
            <table class="table table-bordered"> 
                <thead class="thead-light"> 
                    <tr> 
                        <th><input type="checkbox" id="select_all" name="select">Select All</th> 
                        <th>S.No.</th> 
                        <th>Name</th> 
                        <th>Address</th> 
                        <th>State</th> 
                        <th>City</th> 
                        <th>PhoneNo</th> 
                        <th>Edit</th> 
                    </tr> 
                </thead> 
                <tbody> 
                   <c:forEach items="${list}" var="bean" varStatus="loop">
                        <tr align="center"> 
                            <td><input type="checkbox" class="checkbox" name="ids" value="${bean.id}"></td> 
                            <td><c:out value="${index + loop.index}" /></td> 
                            <td>${bean.name}</td> 
                            <td>${bean.address}</td> 
                            <td>${bean.state}</td> 
                            <td>${bean.city}</td> 
                            <td>${bean.phoneNo}</td> 
                            <td><a href="CollegeCtl?id=${bean.id}">Edit</a></td> 
                        </tr> 
                    </c:forEach>
                </tbody> 
            </table> 
            <div class="button-container"> 
                <input type="submit" name="operation" value="<%=CollegeListCtl.OP_PREVIOUS%>" ${pageNo == 1 ? "disabled" : ""}> 
                <input type="submit" name="operation" value="<%=CollegeListCtl.OP_DELETE%>"> 
                <input type="submit" name="operation" value="<%=CollegeListCtl.OP_NEW%>"> 
                <input type="submit" name="operation" value="<%=CollegeListCtl.OP_NEXT%>" ${(list.size() < pageSize || nextlist == 0) ? "disabled" : ""}> 
            </div> 
            </c:if>
              <c:if test="${empty list}"> 
                <div class="button-container"> 
                <input type="submit" name="operation" value="<%=CollegeListCtl.OP_BACK%>"> 
                </div> 
            </c:if> 
            <input type="hidden" name="pageNo" value="${pageNo}"> 
            <input type="hidden" name="pageSize" value="${pageSize}"> 
        </div> 
    </form> 
    <%@ include file="Footer.jsp"%> 
</body>
</html>
