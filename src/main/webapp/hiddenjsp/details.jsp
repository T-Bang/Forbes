<!-- details -->
<%@ page import = "beans.Article" %>

<%
	Article article = (Article)request.getAttribute("article");
%>

<html>
<head>
<link rel="stylesheet" type="text/css" href="http://natas.labs.overthewire.org/css/level.css">
<link rel="stylesheet" href="http://natas.labs.overthewire.org/css/jquery-ui.css" />
<link rel="stylesheet" href="http://natas.labs.overthewire.org/css/wechall.css" />
<script src="http://natas.labs.overthewire.org/js/jquery-1.9.1.js"></script>
<script src="http://natas.labs.overthewire.org/js/jquery-ui.js"></script>
</head>
<body>
<h1>Forbes | Details</h1>
<div id="content">
		<center>
			<table border="1">
				<tr>
					<td>Author :</td>
					<td><%=article.getAuthor()%></td>
				</tr>
				<tr>
					<td>Title :</td>
					<td><%=article.getTitle()%></td>
				</tr>
				<tr>
					<td>Content :</td>
					<td><%=article.getContent()%></td>
				</tr>
			</table>
			<br>

			<hr>
			<button onclick="goBack()">Back</button>
			<script>
				function goBack() {
					window.history.back();
				}
			</script>
        </center>
</div>
</body>
</html>