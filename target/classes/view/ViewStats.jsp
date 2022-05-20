<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"  %>
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
<h1>Stress Rate</h1>
<p><strong>Stress Rate: </strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${requestScope.report.stressRate}" /></p>
<p>The stress rate is the (total unsolved incidents)/(total IT Staff * 5)</p>
<h1>Total Resolved Incidents Per Category In Last 7 Days</h1>
<c:forEach var="stat" items="${requestScope.solved}">
    <c:choose>
        <c:when test="${stat.key.subCategory}">
            <p><c:out value=" - ${stat.key.sub.displayName}: ${stat.value}"/></p>
        </c:when>
        <c:otherwise>
            <p><strong><c:out value="${stat.key.main.displayName}: "/>${stat.value}</strong></p>
        </c:otherwise>
    </c:choose>
</c:forEach>

<h1>Total Unsolved Incidents Per Category</h1>
<c:forEach var="stat" items="${requestScope.unsolved}">
    <c:choose>
        <c:when test="${stat.key.subCategory}">
            <p><c:out value=" - ${stat.key.sub.displayName}: ${stat.value}"/></p>
        </c:when>
        <c:otherwise>
            <p><strong><c:out value="${stat.key.main.displayName}: "/>${stat.value}</strong></p>
        </c:otherwise>
    </c:choose>
</c:forEach>
</body>
</html>