<%-- 
    Document   : connexion
    Created on : 31 mai 2022, 04:27:28
    Author     : jeanbourquj
--%><%@page import="murderandmystery.routing.ActionPage"%>



<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%--
--%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%--
--%><%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %><%--

--%>


<!DOCTYPE html>
<html lang="fr">
    <%@include file="../jspf/entete.jspf" %>
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
                <label for="codeConnexionAdmin">Connexion admin :</label>
                <input type="text" id="codeConnexionAdmin"
                       name="codeConnexionAdmin"/>
                <button type="submit" name="action"
                        value="<%= ActionPage.CONNEXION.name()%>">
                    Connexion
                </button>

            </form>
                    <p>${TE}</p>
        </section>
    </body>
</html>
