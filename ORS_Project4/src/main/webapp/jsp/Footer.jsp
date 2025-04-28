<%@ page language="java" contentType="text/html; charset=ISO-8859-1"%><%@page import="com.rays.pro4.Util.PropertyReader"%><%@ page
	import="com.rays.pro4.controller.ORSView"%>
    pageEncoding="ISO-8859-1"%>
<html>
<head>

  <style type="text/css">
#footer {
    
    position: fixed;
    left:0;
    width:100%; 
    bottom:0;
    background-color:white;
    color:black;
   
   
   text-align:center;
}
</style>
 
</head>
<body>
<div id ="footer">
<CENTER><hr>
    <H3><%=PropertyReader.getValue("COPYRIGHT_MESSAGE") %></H3>
</CENTER>
</div>

</html>