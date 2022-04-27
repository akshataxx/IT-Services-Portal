<!DOCTYPE html>
<html lang="en">
	<head>
		<title>User View</title>
		<link href="css/style.css" rel = "stylesheet" type="text/css"/>
		<link href="https://cdn.jsdelivr.net/npm/@fortawesome/fontawesome-free@5.15.4/css/fontawesome.min.css" rel = "stylesheet">
	</head>
	<body>
		<h1>Welcome to IT Issue Reporting System </h1>
		<hr>
		<form id="userview" action="${pageContext.request.contextPath}/UserView" method="post">
		<div class="container">
			<div><h1>Report New Issue </h1>
			<br>
			<p>Click here to report details of new issue and provide details of the problems faced</p>
			</div>
			<div><h1> View Reported Issue </h1><br> 
				<p>Inorder to view the status of previously reported issue you may click here</p>
			</div>
			<div><h1>Update Issue</h1><br> 
				<p>Inorder to update the status of previously reported issue you may click here</p>
			</div>
			<div><h1>View Knowledgebase</h1> <br>
				<p>Check knowledgebase for solution to your problem</p></div>
			<div><h1>Accept Solution</h1> <br>
				<p>Check the solution provided by IT Staff and accept the solution</p></div>
			<div><h1> Reject Solution </h1> <br>
				<p>Check the solution provided by IT Staff and reject the solution</p>
			</div>
		</div>
		</form>
	</body>
	<footer> 
		&copy; All rights reserved , Copyright University of NewCastle-Callaghan 2022 <br/>
		Akshata Dhuraji, SENG2050, University of NewCastle, <u>Email:c3309266@uon.edu.au</u>
	</footer>
</html>