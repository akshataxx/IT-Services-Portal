<!DOCTYPE html>
<html lang="en">
	<head>
		<title>Login</title>
		<link href="css/style.css" rel = "stylesheet" type="text/css"/>
		<script>
			function validateForm() {
				var userName = document.forms["home"]["userName"].value;
	
				if (userName == null || userName == ""||passwrd == null || passwrd == " ") { //checks user name is not null
					alert("Please fill out this field");
					return false;
				}
			}
		</script>
	</head>
	<body>
		<h1>Welcome to IT Issue Reporting System </h1>
		<hr>
		<div class="error">
			<p> ${sessionScope.ERROR} </p>
		</div>
		<div class="form-style-2">
		<div class="form-style-2-heading">Please Enter Your Credintials here...</div>
			<form id="loginform" action="${pageContext.request.contextPath}/Login" onsubmit= "return validateForm()" method="post">
				<fieldset>
				<legend>Login Details</legend>			
				<label for="username"><span>Name<span class="required">*</span></span>
				<input type="text" class="input-field" id="username" name="username" required  placeholder="UserName"><br><br></label>
				<label for="passwrd"><span>Password<span class="required">*</span></span>
				<input type="text" class="input-field" id="passwrd" name="passwrd" required  placeholder="Password"><br><br></label>

				<input type="submit" name="login" value="Login" /> &emsp;
				</fieldset>
			</form>
		</div>
	 </body>
	<footer> 
		&copy; All rights reserved , Copyright University of NewCastle-Callaghan 2022 <br/>
		Akshata Dhuraji, SENG2050, University of NewCastle, <u>Email:c3309266@uon.edu.au</u>
	</footer>
</html>