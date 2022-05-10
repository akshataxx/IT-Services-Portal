<!DOCTYPE html>
<html lang="en">
	<head>
		<link rel="stylesheet" href="style.css">
		<script src="validation.js" type="text/javascript"></script>
		<script>
			const loginForm = {
				name: "loginForm",

				//the actual html element id for each field
				fields: {
					username: "username",
					password: "password"
				},

				validations: {
					username: {
						required: true,
						minLength: 1,
						maxLength: 20,
					},

					password: {
						required: true,
						minLength: 1,
						maxLength: 20,
					}
				},
			}
		</script>
		<title>Login</title>
	</head>
	<body>
		<h1>Welcome to IT Issue Reporting System </h1>
		<hr>
		<p class="error"> ${param.login_err} </p>
		<div class="form-style-2">
		<div class="form-style-2-heading">Please Enter Your Credentials Here...</div>
			<!-- The reason I have the method as get is, so we can easily test breaking the parameters! This can be changed back to post
			     just before submission-->
			<form id="loginForm" name="loginForm" action="login" onsubmit="return handleSubmit(loginForm)" method="get">
				<fieldset>
				<legend>Login Details</legend>
				<label for="username">Name <span class="required">*</span></label><br/>
				<input type="text" class="input-field" id="username" name="username" required  placeholder="Username"><br/>
				<p id="username_err" class="err">Lorem Ipsum</p><br/>
				<label for="password">Password <span class="required">*</span></label><br/>
				<input type="text" class="input-field" id="password" name="password" required  placeholder="Password"><br/>
				<p id="password_err" class="err">Lorem Ipsum</p><br/>
				<input type="submit" value="Login"/> &emsp;
				</fieldset>
			</form>
		</div>
	 </body>
	<footer> 
		&copy; All rights reserved , Copyright University of Newcastle-Callaghan 2022 <br/>
		Akshata Dhuraji, Jacob Boyce, Wei Chen,  SENG2050, University of Newcastle
	</footer>
</html>