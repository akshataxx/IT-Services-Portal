<%@ page import = "java.io.*,java.util.*" %>
<!DOCTYPE html>
<html lang="en">
	<head>
		<title>Report-Incidents Resolved in Last 7 Days</title>
			<link href="css/style.css" rel = "stylesheet" type="text/css"/>
		</title>
	</head>
	<body>
		<h1>IT Issue Reporting System </h1>
		<hr>
		<table>
			<thead>
				<tr>
					<th>Issue Id </th>
					<th>Issue Description </th>
					<th>Category </th>
					<th>Subcategory </th>
					<th> IT Comments </th>
					<th> User Comments </th>
					<th>DateRaised </th>
					<th>DateResolved </th>
				</tr>
			</thead>
		<%for (int recordCount=0; recordCount < IssueRecSet.size(); recordCount++){%>
		<tr>
			<%Issue listIssues = (Issue)IssueRecSet.get(recordCount);%>
			<td>
				<%listIssues.getIssueId();%>
			</td>
			<td>
				<%listIssues.getIssueDescription();%>
			</td>
			<td>
				<%listIssues.getCategoryName();%>
			</td>
			<td>
				<%listIssues.getSubCategoryNaem();%>
			</td>
			<td>
				<%listIssues.getITComment();%>
			</td>
			<td>
				<%listIssues.getUserComment();%>
			</td>
			<td>
				<%listIssues.getIssueStatus();%>
			</td>
			<td>
				<%listIssues.getDateRaised();%>
			</td>
			<td>
				<%listIssues.getDateResolved();%>
			</td>
		</tr>
		<%}%>
		</table>