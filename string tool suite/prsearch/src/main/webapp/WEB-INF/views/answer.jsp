<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<table>
<h1 style="color:#84C1FF">Answer</h1>
<c:forEach items="${scd}" var="arr">
	<tr><td><a href="${arr.getid()}"><h4 style="color:#2828FF">${arr.gettitle()}</h4></a></td><td><h5 style="color:#02DF82">score:${arr.getpr()}</h5></td></tr>
	<tr><td>${arr.getins()}</td>
	<td><h5><a href="file://localhost/J:/java/WebPageSource/wxdoc/html/${arr.gettheid()}.html">快照</a></h5></td>
	</tr>
</c:forEach>
</table>
</body>
</html>