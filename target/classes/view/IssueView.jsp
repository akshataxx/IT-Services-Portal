<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <link rel="stylesheet" href="css/style.css" type="text/css">
    <meta charset="UTF-8">
    <script src="validation.js" type="text/javascript"></script>
    <script src="dynamicForms.js" type="text/javascript"></script>
    <title><c:out value="Issue #${requestScope.issue.uniqueId}"/></title>
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

        const solutionCriteria = {
            name: "solutionForm",

            fields: {
                solution: "solution"
            },

            validations: {
                solution: {
                    required: true,
                    lengthMin: 1,
                    lengthMax: 2000,
                },
            },
        }

        const collections = {
        <c:forEach var="category" items="${requestScope.enumBean.categoryValues}">
        ${category.name}:
        [
            <c:forEach var="subcategory" items="${category.subCategories}">
            {name: "<c:out value="${subcategory.displayName}"/>", value: "${subcategory.name}"},
            </c:forEach>
        ],
        </c:forEach>
        }
    </script>
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
<h3 style="color: #3b3b3b"><c:out value="Issue #${requestScope.issue.uniqueId}"/></h3>
<p>Issue Opened on the <fmt:formatDate value="${requestScope.issue.reportDate}" pattern="dd-MM-yyyy"/> at <fmt:formatDate value="${requestScope.issue.reportDate}" pattern="hh:mm aa"/> by <c:out
        value="${requestScope.issue.reporter.firstName} ${requestScope.issue.reporter.surname}"/></p>
<hr/>
<p><strong>State: </strong><c:out value="${requestScope.issue.state.displayName}"/></p>
<p><strong>Category: </strong><c:out value="${requestScope.issue.category.main.displayName}"/>
    <strong>Sub-Category: </strong><c:out value="${requestScope.issue.category.sub.displayName}"/></p>

<c:if test="${ user.role.name() == 'IT_STAFF'}">
    <c:if test="${!requestScope.issue.resolved}">
        <h3>Update Issue</h3>
        <c:if test="${requestScope.issue.state.name() ne 'COMPLETED'}">
            <form name="solutionSubmit" action="${pageContext.request.contextPath}/updateIssue" method="get">
                <label for="issue">Update State</label> <input type="hidden" id="issue" name="issue" value="<c:out value="${requestScope.issue.uniqueId}"/>">
                <c:if test="${requestScope.issue.state.name() ne 'IN_PROGRESS'}">
                    <input type="submit" name="value" value="Set In Progress">
                </c:if>
                <c:if test="${requestScope.issue.state.name() ne 'WAITING_ON_THIRD_PARTY'}">
                    <input type="submit" name="value" value="Wait for Third Party">
                </c:if>
                <c:if test="${requestScope.issue.state.name() ne 'WAITING_ON_REPORTER'}">
                    <input type="submit" name="value" value="Wait for Reporter">
                </c:if>
            </form>
            <br/>
        </c:if>
        <form name="categorySubmit" action="${pageContext.request.contextPath}/updateCategory" method="get">
            <input type="hidden" name="issue" value="${requestScope.issue.uniqueId}">
            <label for="category">Update Category</label>
            <select name="category" id="category">
                <c:forEach var="category" items="${requestScope.enumBean.categoryValues}">
                    <option value="${category.name}"><c:out value="${category.displayName}"/></option>
                </c:forEach>
            </select>
            <label for="subCategory">Sub-Category</label>
            <select name="subCategory" id="subCategory">
            </select>
            <script>
                addDynamicSelect("category", "subCategory", collections);
            </script>
            <input type="submit" value="Update Category">
        </form>
    </c:if>
    <c:if test="${requestScope.issue.canBeAddedToKnowledgeBase()}">
        <h3>Add To Knowledge Base</h3>
        <form name="knowledge" method="get" action="${pageContext.request.contextPath}/addKnowledge">
            <input type="hidden" name="issue" value="${requestScope.issue.uniqueId}">
            <input value="Add To Knowledge" type="submit">
        </form>
    </c:if>
</c:if>
<hr/>
<h2>Issue Description</h2>
<p><c:out value="${requestScope.issue.description}"/></p>
<h2>Solutions</h2>
<a id="solutions"></a>
<c:if test="${requestScope.issue.resolved}">
    <p>Issue Resolved on <fmt:formatDate value="${requestScope.issue.resolveDate}" pattern="dd-MM-yyyy"/> at <fmt:formatDate value="${requestScope.issue.resolveDate}" pattern="hh:mm aa"/></p>
</c:if>
<c:forEach var="solution" items="${requestScope.sorter.sorted}">
    <div class="issue-solution">
        <p><c:out
                value="Solution by ${solution.author.firstName} ${solution.author.surname} posted on the "/><fmt:formatDate value="${solution.date}" pattern="dd-MM-yyyy"/> at <fmt:formatDate value="${solution.date}" pattern="hh:mm aa"/></p>
        <p><strong>Solution Status: </strong><c:out value="${solution.state.displayName}"/></p>
        <p><strong>Resolution Details: </strong><c:out value="${solution.text}"/></p>
        <!-- Solution may only be accepted or rejected if the issue is not in its final state -->
        <c:if test="${ ! requestScope.issue.resolved}">
            <c:if test="${user.role.name() == 'USER'}">
                <c:if test="${ solution.state.name() == 'WAITING'}">
                    <form name="solutionSubmit" action="${pageContext.request.contextPath}/solution" method="get">
                        <input type="hidden" name="id" value="<c:out value="${solution.uniqueId}"/>">
                        <input type="hidden" name="issue" value="<c:out value="${requestScope.issue.uniqueId}"/>">
                        <input type="submit" name="value" value="accept"> <input type="submit" name="value"
                                                                                 value="reject">
                    </form>
                </c:if>
            </c:if>
        </c:if>
    </div>
</c:forEach>

<c:if test="${ !requestScope.issue.resolved && requestScope.issue.state.name() ne 'COMPLETED'}">
    <c:if test="${user.role.name() == 'IT_STAFF'}">
        <h3>Add a solution</h3>
        <form class="form-style-2" name="solutionForm" method="get" onsubmit="return handleSubmit(solutionCriteria)"
              action="${pageContext.request.contextPath}/addSolution">
            <fieldset>
                <legend>New Solution</legend>
                <label for="solution">Resolution Details <span class="required">*</span></label><br/>
                <input type="hidden" name="issue" value="<c:out value="${requestScope.issue.uniqueId}"/>">
                <textarea id="solution" name="solution" rows="5"></textarea>
                <p id="solution_err" class="err">Must be less than 2000 characters</p>
                <input type="submit" value="Post Solution"/>
            </fieldset>
        </form>
    </c:if>
</c:if>
<h2>Comments</h2>
<a id="comments"></a>
<c:forEach var="comment" items="${requestScope.commentSorter.sorted}">
    <div class="issue-solution">
        <p style="color: #3b3b3b"><c:out
                value="Comment by ${comment.author.firstName} ${comment.author.surname} posted on the "/><fmt:formatDate value="${comment.date}" pattern="dd-MM-yyyy"/> at <fmt:formatDate value="${comment.date}" pattern="hh:mm aa"/></p>
        <p><c:out value="${comment.text}"/></p>
    </div>
</c:forEach>
<c:if test="${! requestScope.issue.resolved}">
    <h3>Add a comment</h3>
    <form class="form-style-2" name="commentForm" method="get" action="${pageContext.request.contextPath}/comment"
          onsubmit="return handleSubmit(commentCriteria)">
        <fieldset>
            <legend>New Comment</legend>
            <label for="text">Comment <span class="required">*</span></label><br/>
            <input type="hidden" name="issue" value="<c:out value="${requestScope.issue.uniqueId}"/>">
            <textarea id="text" name="text" rows="5"></textarea>
            <p id="text_err" class="err">Must be less than 500 characters</p>
            <input type="submit" value="Post Comment"/>
        </fieldset>
    </form>
</c:if>
<hr/>
</body>
</html>