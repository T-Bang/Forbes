<html>
<head>
	<link rel="stylesheet" type="text/css" href="http://natas.labs.overthewire.org/css/level.css">
	<link rel="stylesheet" href="http://natas.labs.overthewire.org/css/jquery-ui.css" />
	<link rel="stylesheet" href="http://natas.labs.overthewire.org/css/wechall.css" />
	<script src="http://natas.labs.overthewire.org/js/jquery-1.9.1.js"></script>
	<script src="http://natas.labs.overthewire.org/js/jquery-ui.js"></script>
</head>
<style>
	footer {
		display: block;
	}
</style>
<body>
<h1>Forbes | Media</h1>
<div id="content">
	<center>
		<br>
		<div style="color: #002E00; font-size: 30px">Forbes | Your Source Of Real Content</div>
		<br>
		<form method="POST" action="<%=request.getContextPath()%>/article/"/>
		<table>
			<tr><td>Article title</td><td><input type="text" name="title"/></td></tr>
			<tr><td colspan="1"></td><td><button>Search</button>
		</table>
		</form>

	</center>
</div>
</body>
<footer>
	<center>
		<p>Designed by <a href="mailto:maptee1@gmail.com">Thabang Maphothoane</a> &copy;</p>
	</center>
</footer>
</html>

