<!-- list -->
<%@ page import = "java.util.List" %>
<%@ page import = "beans.Article" %>
<%
	List<Article> articles = (List<Article>)request.getAttribute("Articles");
%>

<html>
<head>
<link rel="stylesheet" type="text/css" href="http://natas.labs.overthewire.org/css/level.css">
<link rel="stylesheet" href="http://natas.labs.overthewire.org/css/jquery-ui.css" />
<link rel="stylesheet" href="http://natas.labs.overthewire.org/css/wechall.css" />
<script src="http://natas.labs.overthewire.org/js/jquery-1.9.1.js"></script>
<script src="http://natas.labs.overthewire.org/js/jquery-ui.js"></script>
<script> function goBack() { window.history.back(); } </script>
</head>
<body>
<h1>Forbes | Articles</h1>
<div id="content">
	<center>

		<br>
		<div style="color: #002E00; font-size: 30px">Forbes | Your source of real content</div>
		<br>
		<form method="POST" action="<%=request.getContextPath()%>/article/"/>
		<table >
			<tr><td>Article title</td><td><input type="text" name="title"/></td></tr>
			<tr><td colspan="1"></td><td><button type="submit">Search</button>
		</table>
		</form>

		<table>
			<% if (request.getAttribute("title") != null) { %>
				<tr><th>Article</th><td><%=request.getAttribute("title")%></td></tr>
			<% } %>
		</table>
		<br>
		<hr/>

		<% if (articles.size() > 0) { %>
			<table border="1">
			<tr><th>Author</th><th>Title</th><th>Content</th></tr>
			<% for (Article article : articles) { %>
				<tr>
					<td><%=article.getAuthor()%></td>
					<td><%=article.getTitle()%></td>
					<td><form method="POST" action="<%=request.getContextPath()%>/article/">
						<input type="hidden" name="title2" value="<%=article.getTitle()%>"/>
						<input type="submit" value="View Details"/></form></td>
				</tr>
			<% } %>
			</table>
		<% } else { %>
			<font size="+1" color="red">No article matches the supplied criteria</font>
		<% } %>
		<hr/>
			<td>
				<form method="POST" action="<%=request.getContextPath()%>/article/">
					<input type="hidden" name="page" value="<%=(Integer)request.getAttribute("page")%>"/>
					<button>Next</button></form><button onclick="goBack()">Prev</button>
			</td>
	</center>
</div>
</body>
</html>
