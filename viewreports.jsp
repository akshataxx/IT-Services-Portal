
<!DOCTYPE html>
<html lang="en">
	<head>
		<title>View Reports</title>
		<link href="css/style.css" rel = "stylesheet" type="text/css"/>
		<link href="https://cdn.jsdelivr.net/npm/@fortawesome/fontawesome-free@5.15.4/css/fontawesome.min.css" rel = "stylesheet">
	</head>
	<body>
		<h1>Welcome to IT Issue Reporting System</h1>
		<hr>
		<div class="wrapper">
			<div class="box">
				<div class="image"> <img src="images/resolvedreports.png"></img></div>
				<h1><a href= "<%=request.getContextPath()%>/Resolved">Total Resolved Incidents <br>Within Last 7 Days<br> In Each Category</a></h1>
					<div> Report Shows List Of All The Resolved Incidents <br> in last 7 days, sorted or grouped by category</div>
			</div>
			<div class="box">
				<div class="image"><img src="images/unresolved.png"></img></div>
				<h1><a href= "<%=request.getContextPath()%>/UnResolved">Total Unresolved Incident <br> By Category</a></h1><br><br>
					<div>Report Shows All The Unresolved Issues By Selected Category</div>
			</div>
			<div class="box">
				<div class="image"><img src="images/stress.png"></img></div>
				<h1><a href= "<%=request.getContextPath()%>/StressRate"> Stress Rate</a></h1><br><br><br><br>
					<div>detailed text that explains what the title means</div>
			</div>
		</div>
	</body>
</html>