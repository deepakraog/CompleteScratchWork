<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Update-Feed Form</title>
</head>
<body>
	<h1>Mandatory Input Fields</h1>

	<form action="dbUpdate.jsp" method="get">
		Enter MSISDN: <input type="text" name="msisdn" value="" /><br/>
		Enter pin: <input type="text" name="pin"  value="" /><br/>
		Enter action: <input type="text" name="action"  value="" /><br/>
		<input type="submit" value="Click"/>
	</form>
</body>
</html>