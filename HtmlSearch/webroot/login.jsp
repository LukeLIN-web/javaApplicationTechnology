<%@ page language="java" contentType="text/html/htm/jsp; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="webServer.Response.test, java.io.*,java.util.*" %> 
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>我的JSP页面</title>
</head>
<body>
	<%
		Date date = new Date();
		String dateStr;
		dateStr = String.format("%tY年%tm月%td日",date,date,date);
	%>
	<%=dateStr %>
	<%
		String str, ;
		test te = new test();
	%>
	
   	<table>
	<tr>
		<td>用户名:</td>
		<td><%=te.sd()%></td>
	</tr>
	<tr>
		<td>密码:</td>
		<td><%=te.sd()%></td>
	</tr>
	</table>
</body>
</html>