<%@page contentType="text/html" pageEncoding="UTF-8"%><%--
--%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%--
        
--%><!DOCTYPE html>
<html>
    <%@include file="../../jspf/entete.jspf" %>
    <body>
        <form  method="POST" >
            <%@include file="../../jspf/navbar_admin.jspf" %>
        </form>
        <section>
            <header>
                <h1>Salle Fictives</h1>
            </header>
            <%@include file="./filtre.jspf" %>
            <section>
                <h2>Liste</h2>
                <form  method="POST" >
                    <button name="action" value="CREER">
                        Ajouter une nouvelle salle...
                    </button>
                </form>

                <div class="item-list">
                    <c:forEach var="detail" items="${liste}">
                        <article class="item">
                            <header>
                                <small>${detail.identifiant.UUID}</small>
                                <h3>
                                    <a href="${pageContext.request.contextPath}/admin/salle_fictives${OLD_CODE}/${detail.identifiant.UUID}.html">
                                        ${detail.nom}
                                    </a>
                                </h3>

                            </header>
                        </article>
                    </c:forEach>
                </div>

            </section>

        </section>
    </body>
</html>
