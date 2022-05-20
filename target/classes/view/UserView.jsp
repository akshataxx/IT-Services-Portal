<!DOCTYPE html>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"  %>

<html lang="en">
<head>
	<title>User View</title>
	<link rel="stylesheet" href="css/style.css" type="text/css">
	<link href="https://cdn.jsdelivr.net/npm/@fortawesome/fontawesome-free@5.15.4/css/fontawesome.min.css" rel = "stylesheet">
	<script src="validation.js" type="text/javascript"></script>
	<script src="dynamicForms.js" type="text/javascript"></script>
	<script>
		const reportCriteria = {
			name: "reportForm",

			//the actual html element id for each field
			fields: {
				title: "title",
				description: "description",
				category: "category",
				subCategory: "subCategory",
			},

			validations: {
				title: {
					required: true,
					lengthMin: 1,
					lengthMax: 100,
				},

				description: {
					required: true,
					lengthMin: 1,
					lengthMax: 2000,
				},

				category: {
					required: true
				},

				subCategory: {
					required: true
				},
			},
		};

		const collections = {
			<c:forEach var="category" items="${requestScope.enumBean.categoryValues}">
			${category.name}: [
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
<h1>Welcome to IT Issue Reporting System</h1>
<hr>

<h2><a href="${pageContext.request.contextPath}/knowledgeBase">View Knowledge Base</a></h2>

<h2>Your Issues</h2>

<div class="display-issues">
	<ul>
		<c:set var="spacing" scope="request" value="5"/>
		<c:forEach var="issue" items="${user.issues}">
			<li>
				<a href="issue?id=${issue.uniqueId}">
					<span class="display-issues-title"><c:out value="${issue.title}"/></span>
					<c:forEach begin="1" end="${spacing}">&nbsp;</c:forEach>
					<c:out value="${issue.category.main.displayName}"/>
					<c:forEach begin="1" end="${spacing}">&nbsp;</c:forEach>
					<c:out value="${issue.category.sub.displayName}"/>
					<c:forEach begin="1" end="${spacing}">&nbsp;</c:forEach>
					<c:out value="${issue.state.displayName}"/><br/>
					<c:out value="Issue #${issue.uniqueId} opened on "/><fmt:formatDate value="${issue.reportDate}" pattern="dd-MM-yyyy"/> at <fmt:formatDate value="${issue.reportDate}" pattern="hh:mm aa"/><c:out value=" by ${issue.reporter.firstName} ${issue.reporter.surname}"/>
				</a>
			</li>
		</c:forEach>
	</ul>
</div>

<hr/>

<h2>Report an Issue</h2>
<form id="reportForm" name="reportForm" action="report" onsubmit="return handleSubmit(reportCriteria)"  method="get" class="form-style-2">
	<fieldset>
		<legend>Report Details</legend>
		<label for="title">Title <span class="required">*</span></label><br/>
		<input type="text" class="input-field" id="title" name="title" placeholder="title"><br/>
		<p id="title_err" class="err">Must be less than 100 characters</p><br/>
		<label for="description">Description <span class="required">*</span></label><br/>
		<textarea name="description" id="description" rows="10"></textarea>
		<p id="description_err" class="err">Must be less than 2000 characters</p><br/>
		<label for="category">Category <span class="required">*</span></label>
		<select name="category" id="category">
			<c:forEach var="category" items="${requestScope.enumBean.categoryValues}">
				<option value="${category.name}"><c:out value="${category.displayName}"/></option>
			</c:forEach>
		</select>
		<label for="subCategory">Sub-Category <span class="required">*</span></label>
		<select name="subCategory" id="subCategory">
		</select><br/>
		<script>
			addDynamicSelect("category","subCategory", collections);
		</script>
		<p id="category_err" class="err"></p><br/>
		<input type="submit" value="Report Issue"/> &emsp;
	</fieldset>
</form>
</body>
</html>
