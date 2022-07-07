<%-- 
    Document   : acceuil
    Created on : 28 mai 2022, 03:08:37
    Author     : jeanbourquj
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%--
--%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%--
--%><%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %><%--

--%>
<!DOCTYPE html>
<html>
<%@include file="./jspf/entete.jspf" %>
<body>
    <h1>Acceuil</h1>
    <p><a href="${pageContext.request.contextPath}/connexion.html">Connexion à un personnage</a></p>
    <p><a href="${pageContext.request.contextPath}/admin/connexion.html">Gestion admin</a></p>
</body>
</html>
<!--<html>
    <head>
        <title>File Uploading Form</title>
    </head>

    <body>
        <h3>File Upload:</h3>
        Select a file to upload: <br />
        <form action = "${pageContext.request.contextPath}/admin/acceuil.html" method = "post"
              enctype = "multipart/form-data">
            <input type = "file" name = "file" size = "50" />
            <br />
            <input type = "submit" value = "Upload File" />
        </form>
    </body>

</html>-->
