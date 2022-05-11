<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"  %>
<!DOCTYPE html>
<html lang="en">
<head>
    <link rel="stylesheet" href="css/style.css" type="text/css">
    <meta charset="UTF-8">
    <script src="validation.js" type="text/javascript"></script>
    <title><c:out value = "Issue #${requestScope.issue.uniqueId}"/></title>
    <script>
        const commentCriteria = {
            name: "commentForm",

            //the actual html element id for each field
            fields: {
                text: "text",
            },

            validations: {
                text: {
                    required: true,
                    lengthMin: 1,
                    lengthMax: 500,
                },
            },
        };
    </script>
</head>
<body>

<div class="topnav">
    <a href="${pageContext.request.contextPath}/user">Home</a>
    <div class="topnav-right">
        <a href="${pageContext.request.contextPath}/logout">Log Out</a>
        <a href="${pageContext.request.contextPath}/notifications">Notifications</a>
        <a href="${pageContext.request.contextPath}/user"><c:out value="${user.firstName} ${user.surname}"/></a>
    </div>
</div>

<h1><c:out value="${requestScope.issue.title}"/></h1>
<h3 style="color: #3b3b3b"><c:out value="Issue #${requestScope.issue.uniqueId}"/></h3>
<p>Issue Opened on the <fmt:formatDate value="${requestScope.issue.reportDate}" pattern="dd-MM-yyyy HH:mm"/> by <c:out value="${requestScope.issue.reporter.firstName} ${requestScope.issue.reporter.surname}"/></p>
<hr/>
<p><strong>State: </strong><c:out value="${requestScope.issue.state.displayName}"/></p>
<p><strong>Category: </strong><c:out value="${requestScope.issue.category.main.displayName}"/> <strong>Sub-Category: </strong><c:out value="${requestScope.issue.category.sub.displayName}"/></p>
<hr/>
<h2>Issue Description</h2>
<p><c:out value="${requestScope.issue.description}"/></p>
<h2>Solutions</h2>
<c:if test="${requestScope.issue.resolved}">
    <p>Issue Resolved on <fmt:formatDate value="${requestScope.issue.resolveDate}" pattern="dd-MM-yyyy HH:mm"/></p>
</c:if>
<c:forEach var="solution" items="${requestScope.sorter.sorted}">
    <div class="issue-solution">
        <p><c:out value="Solution by ${solution.author.firstName} ${solution.author.surname} posted on the "/><fmt:formatDate value="${solution.date}" pattern="dd-MM-yyyy HH:mm"/></p>
        <p><strong>Solution Status: </strong><c:out value="${solution.state.displayName}"/></p>
        <p><strong>Resolution Details: </strong><c:out value="${solution.text}"/></p>
        <c:if test="${ ! requestScope.issue.resolved }">
            <c:set var="accept" value="ACCEPTED"/>
            <c:if test="${ solution.state.name() ne 'ACCEPTED'}">
                <form name="solutionSubmit" action="${pageContext.request.contextPath}/solution" method="get">
                <input type="hidden" name="id" value="<c:out value="${solution.uniqueId}"/>">
                <input type="hidden" name="issue" value="<c:out value="${requestScope.issue.uniqueId}"/>">
                <input type="hidden" name="type" value="submit">
                <input type="submit" name="value" value="accept"> <input type="submit" name="value" value="reject">
                </form>
            </c:if>
        </c:if>
    </div>
</c:forEach>
<h2>Comments</h2>
<c:forEach var="comment" items="${requestScope.commentSorter.sorted}">
    <div class="issue-solution">
        <p style="color: #3b3b3b"><c:out value="Comment by ${comment.author.firstName} ${comment.author.surname} posted on the "/><fmt:formatDate value="${comment.date}" pattern="dd-MM-yyyy HH:mm"/></p>
        <p><c:out value="${comment.text}"/></p>
    </div>
</c:forEach>
<form class="form-style-2" name="commentForm" method="get" action="${pageContext.request.contextPath}/comment" onsubmit="return handleSubmit(commentCriteria)">
    <fieldset>
    <legend>Comment Details</legend>
    <label for="text">Comment <span class="required">*</span></label><br/>
    <input type="hidden" name="issue" value="<c:out value="${requestScope.issue.uniqueId}"/>">
    <textarea id="text" name="text" rows="5"></textarea>
    <p id="text_err" class="err">Lorem Ipsum</p>
    <input type="submit" value="Post Comment"/>
    </fieldset>
</form>
<hr/>
</body>
</html>