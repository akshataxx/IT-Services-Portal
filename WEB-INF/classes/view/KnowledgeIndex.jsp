<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"  %>
<!DOCTYPE html>
<html lang="en">
<head>
    <link rel="stylesheet" href="css/style.css" type="text/css">
    <meta charset="UTF-8">
    <script src="validation.js" type="text/javascript"></script>
    <script src="dynamicForms.js" type="text/javascript"></script>
    <script>
        const collections = {
        <c:forEach var="category" items="${requestScope.enumBean.categoryValues}">
        ${category.name}: [
            {name: "", value: ""},
            <c:forEach var="subcategory" items="${category.subCategories}">
            {name: "<c:out value="${subcategory.displayName}"/>", value: "${subcategory.name}"},
            </c:forEach>
        ],
        </c:forEach>
        }
    </script>
    <title>Knowledge Base</title>
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

<h1>List of All Knowledge Articles</h1>
<p class="form-style-2-heading">Sort and Group Knowledge Articles</p>
<form class="form-style-2" name="sortForm" action="${pageContext.request.contextPath}/knowledgeBase">
    <fieldset>
        <p class="form-style-2-heading">Group Options</p>
        <label for="category">Knowledge Category</label>
        <select name="category" id="category">
            <option value=""></option>
            <c:forEach var="category" items="${requestScope.enumBean.categoryValues}">
                <option value="${category.name}"><c:out value="${category.displayName}"/></option>
            </c:forEach>
        </select>
        <label for="subCategory">Sub-Category</label>
        <select name="subCategory" id="subCategory">
        </select><br/>
        <script>
            addDynamicSelect("category","subCategory", collections);
        </script>
        <input type="submit" value="Sort and Group">
    </fieldset>
</form>

<h2>Knowledge Articles</h2>

<div class="display-issues">
    <ul>
        <c:set var="spacing" scope="request" value="5"/>
        <c:forEach var="issue" items="${issues.list}">
            <li>
                <a href="knowledge?id=${issue.uniqueId}">
                    <span class="display-issues-title"><c:out value="${issue.title}"/></span>
                    <c:forEach begin="1" end="${spacing}">&nbsp;</c:forEach>
                    <c:out value="${issue.category.main.displayName}"/>
                    <c:forEach begin="1" end="${spacing}">&nbsp;</c:forEach>
                    <c:out value="${issue.category.sub.displayName}"/>
                    <br/>
                    <c:out value="Knowledge Article opened on "/><fmt:formatDate value="${issue.reportDate}" pattern="dd-MM-yyyy"/> at <fmt:formatDate value="${issue.reportDate}" pattern="hh:mm aa"/><c:out value=" by ${issue.reporter.firstName} ${issue.reporter.surname}"/>
                </a>
            </li>
        </c:forEach>
    </ul>
</div>
<hr>
</body>
</html>