<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <link rel="stylesheet" href="css/style.css" type="text/css">
    <meta charset="UTF-8">
    <title>Stats</title>
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
<h1>Statistics</h1>
<hr>
<h1>Stress Rate: ${requestScope.report.stressRate}</h1>
</body>
<footer>
    &copy; All rights reserved , Copyright University of Newcastle-Callaghan 2022 <br/>
    Akshata Dhuraji, Jacob Boyce, Wei Chen,  SENG2050, University of Newcastle
</footer>
</html>