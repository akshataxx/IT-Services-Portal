<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <link rel="stylesheet" href="css/style.css" type="text/css">
    <meta charset="UTF-8">
    <script src="validation.js" type="text/javascript"></script>
    <script src="dynamicForms.js" type="text/javascript"></script>
    <title><c:out value="Knowledge Article"/></title>
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

<h1><c:out value="${requestScope.issue.title}"/></h1>
<p>Article Opened on the <fmt:formatDate value="${requestScope.issue.reportDate}" pattern="dd-MM-yyyy"/> at <fmt:formatDate value="${requestScope.issue.reportDate}" pattern="hh:mm aa"/> by <c:out
        value="${requestScope.issue.reporter.firstName} ${requestScope.issue.reporter.surname}"/></p>
<hr/>
<p><strong>State: </strong><c:out value="${requestScope.issue.state.displayName}"/></p>
<p><strong>Category: </strong><c:out value="${requestScope.issue.category.main.displayName}"/>
    <strong>Sub-Category: </strong><c:out value="${requestScope.issue.category.sub.displayName}"/></p>
<hr/>
<h2>Issue Description</h2>
<p><c:out value="${requestScope.issue.description}"/></p>
<h2>Solution</h2>
<a id="solutions"></a>
<c:if test="${requestScope.issue.resolved}">
    <!-- If the issue is only completed and is in the knowledge base then it won't have a resolve date because it hasn't been resolved yet! -->
    <p>Issue Resolved on <fmt:formatDate value="${requestScope.issue.resolveDate}" pattern="dd-MM-yyyy"/> at <fmt:formatDate value="${requestScope.issue.resolveDate}" pattern="hh:mm aa"/></p>
</c:if>
<div class="issue-solution">
    <p><c:out value="Solution by ${solution.author.firstName} ${solution.author.surname} posted on the "/><fmt:formatDate value="${solution.date}" pattern="dd-MM-yyyy"/> at <fmt:formatDate value="${solution.date}" pattern="hh:mm aa"/></p>
    <p><strong>Solution Status: </strong><c:out value="${solution.state.displayName}"/></p>
    <p><strong>Resolution Details: </strong><c:out value="${solution.text}"/></p>
</div>
<h2>Comments</h2>
<a id="comments"></a>
<c:forEach var="comment" items="${requestScope.commentSorter.sorted}">
    <div class="issue-solution">
        <p style="color: #3b3b3b"><c:out
                value="Comment by ${comment.author.firstName} ${comment.author.surname} posted on the "/><fmt:formatDate value="${comment.date}" pattern="dd-MM-yyyy"/> at <fmt:formatDate value="${comment.date}" pattern="hh:mm aa"/></p>
        <p><c:out value="${comment.text}"/></p>
    </div>
</c:forEach>
<hr/>
</body>
</html>