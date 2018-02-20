<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" language="java" import="java.util.*" pageEncoding="UTF-8"%>
<html>
<head>
	<title>Home</title>
	<script>
	function search(){
		form1.submit();
	}
	</script>
</head>
<body>
<h1>
	Search the NEWS! 
</h1>
<form action="search" id="form1">
<input type="text" id="searchstr" name="searchstr" style="width:200px;height:30;">
</form>
<input type="button" value="Search" style="background-color:#ADFF2F;color:white" onclick="search()">
</body>
</html>
