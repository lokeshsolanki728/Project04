<%@page import="com.rays.pro4.controller.CollegeListCtl"%>
<%@page import="com.rays.pro4.Util.ServletUtility"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.rays.pro4.Util.DataUtility"%>
<%@page import="com.rays.pro4.Util.HTMLUtility"%>
<%@page import="com.rays.pro4.Bean.CollegeBean"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
  <head>
    <link rel="icon" type="image/png" href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16*16" />
    <title>College List</title>
    <link rel="stylesheet" href="<%=ORSView.APP_CONTEXT%>/css/style.css">
  </head>
  <body>
    <jsp:useBean id="bean" class="com.rays.pro4.Bean.CollegeBean" scope="request"></jsp:useBean>
    <%@ include file="Header.jsp"%>
    <form action="<%=ORSView.COLLEGE_LIST_CTL%>" method="POST">
      <div class="container-fluid">
        <h1>College List</h1>
        <div class="message-container">
          <span class="error-message"><%=ServletUtility.getErrorMessage(request)%></span>
          <span class="success-message"><%=ServletUtility.getSuccessMessage(request)%></span>
        </div>
        <%
          List<CollegeBean> collegeList = (List) request.getAttribute("CollegeList");
          int next = DataUtility.getInt(request.getAttribute("nextlist").toString());
          int pageNo = ServletUtility.getPageNo(request);
          int pageSize = ServletUtility.getPageSize(request);
          int index = ((pageNo - 1) * pageSize) + 1;
          List<CollegeBean> list = ServletUtility.getList(request);
          Iterator<CollegeBean> it = list.iterator();
          if (!list.isEmpty()) {
        %>
        <div class="search-container">
          <label for="collegeid">College Name :</label>
          <%=HTMLUtility.getList("collegeid", String.valueOf(bean.getId()), collegeList)%>
          <label for="city">City :</label>
          <input type="text" name="city" placeholder="Enter City Name" value="<%=ServletUtility.getParameter("city", request)%>">
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
            <%
              while (it.hasNext()) {
                bean = it.next();
            %>
            <tr align="center">
              <td><input type="checkbox" class="checkbox" name="ids" value="<%=bean.getId()%>"></td>
              <td><%=index++%></td>
              <td><%=bean.getName()%></td>
              <td><%=bean.getAddress()%></td>
              <td><%=bean.getState()%></td>
              <td><%=bean.getCity()%></td>
              <td><%=bean.getPhoneNo()%></td>
              <td><a href="CollegeCtl?id=<%=bean.getId()%>">Edit</a></td>
            </tr>
            <%
              }
            %>
          </tbody>
        </table>
        <div class="button-container">
          <input type="submit" name="operation" value="<%=CollegeListCtl.OP_PREVIOUS%>" <%=pageNo == 1 ? "disabled" : ""%>>
          <input type="submit" name="operation" value="<%=CollegeListCtl.OP_DELETE%>">
          <input type="submit" name="operation" value="<%=CollegeListCtl.OP_NEW%>">
          <input type="submit" name="operation" value="<%=CollegeListCtl.OP_NEXT%>" <%=(list.size() < pageSize || next == 0) ? "disabled" : ""%>>
        </div>
        <%
          }
          if (list.isEmpty()) {
        %>
        <div class="button-container">
          <input type="submit" name="operation" value="<%=CollegeListCtl.OP_BACK%>">
        </div>
        <%
          }
        %>
        <input type="hidden" name="pageNo" value="<%=pageNo%>">
        <input type="hidden" name="pageSize" value="<%=pageSize%>">
      </div>
    </form>
    <%@ include file="Footer.jsp"%>
    <script src="<%=ORSView.APP_CONTEXT%>/js/jquery.min.js"></script>
    <script src="<%=ORSView.APP_CONTEXT%>/js/Checkbox11.js"></script>
  </body>
</html>
