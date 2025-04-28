--- a/ORS_Project4/src/main/webapp/jsp/ErrorView404.jsp
+++ b/ORS_Project4/src/main/webapp/jsp/ErrorView404.jsp
@@
 <%@page import="com.rays.pro4.controller.ORSView"%>
 <%@ page language="java" contentType="text/html; charset=ISO-8859-1"
 -    pageEncoding="ISO-8859-1" isErrorPage="true"%>
 
 <html>
 <head>
 <link rel="icon" type="image/png"
 -    href="<%=ORSView.APP_CONTEXT%>/img/customLogo.png" sizes="16*16" />
 <title>Error Page</title>
 <style>
 .error-container {
     text-align: center;
     margin: 50px auto;
     padding: 20px;
     border: 1px solid #ddd;
     width: 80%;
 }
 
 .error-image {
     max-width: 100%;
     height: auto;
 }
 
 .error-message {
     color: red;
 }
 
 .error-code {
     color: black;
     font-size: 25px;
 }
 
 .error-suggestions {
     width: 50%;
     margin: 20px auto;
     text-align: left;
 }
 
 .error-back-link {
     color: silver;
 }
 </style>
 
 </head>
 <body>
 -
 -    <div align="center">
 -        <img src="<%=ORSView.APP_CONTEXT%>/img/Error.jpg" width="550" height="250">
 -        <h1 align="center" style="color: red"> Ooops! Something went wrong..</h1>
 -        <font style="color: black ; font-size: 25px ">
 -        <b>404</b> : Requested Page not available
 -        </font>
 -    <div style="width: 25% ; text-align: justify;">
 -        <h3 >Try :</h3>
 -        <ul>
 -            <li> Server under Maintain please try after Some Time </li>
 -            <li> Check the network cables , modem and router</li>
 -            <li> Reconnect to network or wi-fi</li>
 -        </ul>
 -    </div>
 -    </div>
 -    
 -        <h4 align="center">
 -            <font size="5px" color="black">
 -            <a href="<%=ORSView.WELCOME_CTL %>" style="color: silver">*Click here to Go Back*</a>            
 -            </font>
 -        </h4>
 -    </form>    
 +    <div class="error-container">
 +        <img class="error-image" src="<%=ORSView.APP_CONTEXT%>/img/error.jpg">
 +        <h1 class="error-message">Ooops! Something went wrong..</h1>
 +        <span class="error-code"><b>404</b> : Requested Page not available</span>
 +        <div class="error-suggestions">
 +            <h3>Try :</h3>
 +            <ul>
 +                <li>Server under Maintain please try after Some Time</li>
 +                <li>Check the network cables, modem and router</li>
 +                <li>Reconnect to network or wi-fi</li>
 +            </ul>
 +        </div>
 +    </div>
 +    <h4>
 +        <a class="error-back-link" href="<%=ORSView.WELCOME_CTL%>">*Click here to Go Back*</a>
 +    </h4>
 </body>
 </html>

<html>
<head>
<link rel="icon" type="image/png" href="<%=ORSView.APP_CONTEXT%>/img/customLogo.png" sizes="16*16"/>
<title> Error Page</title>

</head>
<body>
<%-- 
	<form action="<%=ORSView.ERROR_CTL%>" method="get"> --%>
 	
 	<div align="center">
 		<img src="<%=ORSView.APP_CONTEXT%>/img/Error.jpg" width="550" height="250">
 		<h1 align="center" style="color: red"> Ooops! Something went wrong..</h1>
 		<font style="color: black ; font-size: 25px ">
 		<b>404</b> : Requested Page not available
 		</font>
 	<div style="width: 25% ; text-align: justify;">
 		<h3 >Try :</h3>
 		<ul>
 			<li> Server under Maintain please try after Some Time </li>
 			<li> Check the network cables , modem and router</li>
 			<li> Reconnect to network or wi-fi</li>
 		</ul>
 	</div>
 	</div>
	
		<h4 align="center">
			<font size="5px" color="black">
			<a href="<%=ORSView.WELCOME_CTL %>" style="color: silver">*Click here to Go Back*</a>			
			</font>
		</h4>
	</form>	
</body>
</html>