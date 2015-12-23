<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Home</title>
</head>
<body>
<h1>
	Hello world!  
</h1>

<P>  The time on the server is ${serverTime}. </P>

<form action="http://localhost:8080/SpringRestExample/rest/emp/create" method="post">
  <input type="text" name="id" value="1000" />
  <input type="submit" />
</form>
</body>
</html>
