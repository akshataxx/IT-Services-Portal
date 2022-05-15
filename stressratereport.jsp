<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import = "java.util.*"%>
<%@ page import = "pkg.Issue"%>
<%@ page import = "pkg.Report"%>
<!DOCTYPE html>

<html lang="en">
	<head>
		<title>Report-Stress Rate</title>
			<link href="css/style.css" rel = "stylesheet" type="text/css"/>
		</title>
	</head>
	<body>
		<h1>IT Issue Reporting System - Statistics</h1>
		<h2> Stress Rate </h2>
		<hr>
		<% System.out.println("From JSP: " + request.getAttribute("StressRate")); //DEBUG CODE %>
		<p>Stress Rate : <% out.println(request.getAttribute("StressRate"));%></p>
	</body>
</html>