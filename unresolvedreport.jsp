<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import = "java.util.*"%>
<%@ page import = "pkg.Issue"%>
<%@ page import = "pkg.Report"%>
<!DOCTYPE html>

<html lang="en">
	<head>
		<title>Report-Unresolved Incidents</title>
			<link href="css/style.css" rel = "stylesheet" type="text/css"/>
		</title>
	</head>
	<body>
		<h1>IT Issue Reporting System - Statistics</h1>
		<h2> Total unsolved incidents in each category </h2>
		<hr>
		<form action = "UnResolved" method ="Post">
		<center><p> Select Category &nbsp <select name="category" placeholder="Select Category"> </p>
			<option value = "Network"> Network </option>
			<option value ="Software"> Software </option>
			<option value = "Hardware"> Hardware </option>
			<option value = "Email"> Email </option>
			<option value = "Account"> Account </option>
		</select> <br><br>
		<input type ="Submit">
		</center></form>
		<hr>
		<% ArrayList<Issue> IssueRecSet=(ArrayList<Issue>)request.getAttribute("IssueRecSet"); %>
		<p>Total Number of Records: <% out.println(IssueRecSet.size());%></p>
		<table id= "report">
			<thead>
				<tr>
					<th> Issue Id 			</th>
					<th> Issue Description 	</th>
					<th> Category 			</th>
					<th> Subcategory 		</th>
					<th> IT Comments 		</th>
					<th> User Comments 		</th>
					<th> Status				</th>
					<th> DateRaised 		</th>
					<th> DateResolved 		</th>
				</tr>
			</thead>
			<tbody>
				<%for (int recordCount=0; recordCount < IssueRecSet.size(); recordCount++){%>
				<tr>
				<%Issue listIssues = (Issue)IssueRecSet.get(recordCount);%>
					<td> <%out.println(listIssues.getIssueId());%> 				</td>
					<td> <%out.println(listIssues.getIssueDescription());%> 	</td>
					<td> <%out.println(listIssues.getCategoryName());%> 		</td>
					<td> <%out.println(listIssues.getSubCategoryName());%> 		</td>
					<td> <%out.println(listIssues.getITComment());%>			</td>
					<td> <%out.println(listIssues.getUserComment());%> 			</td>
					<td> <%out.println(listIssues.getIssueStatus());%> 			</td>
					<td> <%out.println(listIssues.getDateRaised());%> 			</td>
					<td> <%out.println(listIssues.getDateResolved());%> 		</td>
				</tr>
				<%}%>
			</tbody>
		</table>
	</body>
</html>