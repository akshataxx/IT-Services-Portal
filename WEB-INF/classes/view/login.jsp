<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
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
		<title>Login</title>
	</head>
	<body>
		<h1>Welcome to IT Issue Reporting System </h1>
		<hr>
		<p class="error"><c:out value="${requestScope.login_err}"/></p>
		<div class="form-style-2">
		<div class="form-style-2-heading">Please Enter Your Credentials Here...</div>
			<!-- The reason I have the method as get is, so we can easily test breaking the parameters! This can be changed back to post
			     just before submission-->
			<form id="loginForm" name="loginForm" action="login" onsubmit="return handleSubmit(loginCriteria)" method="get">
				<fieldset>
				<legend>Login Details</legend>
				<label for="username">Name <span class="required">*</span></label><br/>
				<input type="text" class="input-field" id="username" name="username" placeholder="Username"><br/>
				<p id="username_err" class="err"></p><br/>
				<label for="password">Password <span class="required">*</span></label><br/>
				<input type="text" class="input-field" id="password" name="password" placeholder="Password"><br/>
				<p id="password_err" class="err"></p><br/>
				<input type="submit" value="Login"/> &emsp;
				</fieldset>
			</form>
		</div>
		<footer>
			&copy; All rights reserved , Copyright University of Newcastle-Callaghan 2022 <br/>
			Akshata Dhuraji, Jacob Boyce, Wei Chen,  SENG2050, University of Newcastle
		</footer>
	 </body>
</html>