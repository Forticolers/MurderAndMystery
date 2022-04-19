<%-- 
    Document   : index
    Created on : 19 avr. 2022, 03:33:52
    Author     : JeanbourquJ
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%--
--%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%--
--%><%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %><%--

--%>
<%@page import="core.domain.Entity"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
        <p>${Entity.s}</p>
    </body>
</html>
