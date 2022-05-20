<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"  %>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>IT Staff View</title>
	<link rel="stylesheet" href="css/style.css" type="text/css">
	<link href="https://cdn.jsdelivr.net/npm/@fortawesome/fontawesome-free@5.15.4/css/fontawesome.min.css" rel = "stylesheet">
</head>
<body>
<div class="topnav">
	<a href="${pageContext.request.contextPath}/user">Home</a>
	<div class="topnav-right">
		<a href="${pageContext.request.contextPath}/logout">Log Out</a>
		<a href="${pageContext.request.contextPath}/notifications">Notifications <c:out value="[${user.unreadNotifications}]"/></a>
		<a href="${pageContext.request.contextPath}/user"><c:out value="${user.firstName} ${user.surname}"/></a>
	</div>
</div>
<h1>Welcome to IT Issue Reporting System</h1>
<hr>
<div class="itcontainer">
	<div><a href="${pageContext.request.contextPath}/knowledgeBase"><h1>View Knowledge</h1></a><br>
		<p>View knowledge base articles here</p>
	</div>
	<div><a href="${pageContext.request.contextPath}/IssueIndex"><h1> View All Issue </h1></a><br>
		<p>View all the open issues reported by users</p>
	</div>
	<div><a href="${pageContext.request.contextPath}/statistics"><h1>View Statistics</h1></a><br/>
		<p>Generate reports to see total resolved incidents within last 7 days, Total unresolved incident and Stress rate</p>
	</div>
</div>
<footer>
	&copy; All rights reserved , Copyright University of Newcastle-Callaghan 2022 <br/>
	Akshata Dhuraji, Jacob Boyce, Wei Chen SENG2050, University of NewCastle
</footer>
</body>
</html>