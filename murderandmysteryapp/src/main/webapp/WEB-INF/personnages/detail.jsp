<%-- 
    Document   : detail
    Created on : 28 mai 2022, 04:02:18
    Author     : jeanbourquj
--%>

<%@page import="murderandmystery.routing.ActionPage"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <%@include file="../jspf/entete.jspf" %>
    <body>
        <section>
            <header>
                <h1>Fiche personnage</h1>            
            </header>
            <form method="POST">
                <nav>
                    <button type="submit" name="action" formnovalidate=""
                            value="<%= ActionPage.QUITTER.name()%>">
                        Se déconnecter
                    </button>
                </nav>
                <section>
                    <c:choose>
                        <c:when test='${image_utf8 != ""}'>
                            <image  src='${image_utf8}'  alt="image_personnage"/>
                        </c:when>
                        <c:otherwise>
                            <p>Pas d'image trouvé</p>
                        </c:otherwise>
                    </c:choose>
                    <div class='personnage_detail'>
                        <p>${detail.nom}</p>
                        <p>${detail.codeConnexion}</p>
                        <div class='classe_detail'>
                            <p>${detail.classe.nom}</p>
                            <small>${detail.classe.description}</small>
                        </div>
                    </div>                    
                </section>
                <section>
                    <div class='personnage_competences'>
                        <c:forEach var="competence" items="${detail.classe.competences}">
                            <c:choose>
                                <c:when test='${competence.couleurOverride == null}'>
                                    <div style='background-color: ${competence.couleur};'>
                                    </c:when>
                                    <c:otherwise> 
                                        <div style='background-color: ${competence.couleurOverride};'>
                                        </c:otherwise>
                                    </c:choose>
                                    <input type="hidden" name="competence_uuid_hidden_field" value="${competence.identifiant.UUID}" />
                                    <input type="hidden" name="competence_cooldown_hidden_field" value="${competence.cooldown}" />
                                    <button type="submit" name="action" formnovalidate=""
                                            value="COMPETENCE_USAGE">${competence.nom}
                                    </button>
                                </div>
                            </c:forEach>
                        </div>
                </section>
                <section>
                    <div class="personnage_inventaire">
                        
                        <c:choose>
                            <c:when test='${!inventaire_empty}'>


                                <c:forEach var="objet" items="${detail.inventaire}">
                                    <div>
                                        <input type="hidden" name="objet_uuid_hidden_field" value="${objet.identifiant.UUID}" />

                                        <button type="submit" name="action" formnovalidate=""
                                                value="OBJET_ACTION">
                                            ${objet.nom}
                                        </button>
                                    </div>
                                </c:forEach>
                            </c:when>
                            <c:otherwise> 
                                <p>Pas d'objet dans votre inventaire...</p>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </section>
            </form>
        </section>

    </body>
</html>
