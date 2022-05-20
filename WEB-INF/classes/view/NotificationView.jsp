<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <link rel="stylesheet" href="css/style.css" type="text/css">
    <meta charset="UTF-8">
    <script src="validation.js" type="text/javascript"></script>
    <script>
        const loginCriteria = {
            name: "loginForm",

            //the actual html element id for each field
            fields: {
                username: "username",
                password: "password"
            },

            validations: {
                username: {
                    required: true,
                    lengthMin: 1,
                    lengthMax: 20,
                },

                password: {
                    required: true,
                    lengthMin: 1,
                    lengthMax: 20,
                }
            },
        };
    </script>
    <title>Notifications</title>
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
<h1>Your Notifications</h1>
<ul class="display-issues">
    <c:forEach var="notification" items="${requestScope.sorted.sorted}">
        <li><a href="${pageContext.request.contextPath}/issue?id=<c:out value="${notification.issueBean.uniqueId}"/>">
            <span class="display-issues-title"><c:out value="${notification.title}"/></span> . <span style="color: gray"><fmt:formatDate value="${notification.postDate}" pattern="dd-MM-yyyy"/> at <fmt:formatDate value="${notification.postDate}" pattern="hh:mm aa"/></span>
            <p><c:out value="${notification.comment}"/></p>
        </a></li>
    </c:forEach>
</ul>
</body>
</html>