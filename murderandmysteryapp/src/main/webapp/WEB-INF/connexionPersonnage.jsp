<%-- 
    Document   : index
    Created on : 19 avr. 2022, 03:33:52
    Author     : JeanbourquJ
--%>


<%@page import="murderandmystery.routing.ActionPage"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%--
--%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%--
--%><%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %><%--

--%>


<!DOCTYPE html>
<html lang="fr">
    <%@include file="./jspf/entete.jspf" %>
    <body>

        <c:if test="${error_message != null}">
            <section>
                <p class="error_message">${error_message}</p>
            </section>

        </c:if>
        <section>


            <form method="POST">
                <nav>
                    <button type="submit" name="action" formnovalidate=""
                            value="<%= ActionPage.QUITTER.name()%>">
                        Retour a l'acceuil
                    </button>
                </nav>
                <label for="codeConnexion">Code connexion du personnage :</label>
                <input type="text" id="codeConnexion"
                       name="codeConnexion"/>
                <button type="submit" name="action"
                        value="<%= ActionPage.CONNEXION.name()%>">
                    Connexion
                </button>

            </form>
        </section>
    </body>
</html>