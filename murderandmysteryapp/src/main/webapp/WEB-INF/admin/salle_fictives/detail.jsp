<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%><%--
--%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%--
        
--%><%@page import="murderandmystery.routing.ActionPage"%><%--
--%><%@page import="murderandmystery.routing.EtatPage"%><%--
--%><%@page import="murderandmystery.routing.admin.SalleFictivesUtils"%><%--
--%><!DOCTYPE html>
<html>
    <%@include file="../../jspf/entete.jspf" %>
    <body>
        <form  method="POST" >
            <%@include file="../../jspf/navbar_admin.jspf" %>
        </form>

        <script>
            var test = null;
            var test2 = null;
            <c:if test="${EtatPage.MODIFICATION == etatPage
                          or EtatPage.CREATION == etatPage}">
            var test = null;
            var test3 = null;
            var allowTracking = true;
            function getPos(e, obj) {
                if (allowTracking) {

                    element = obj;
                    x = (e.clientX - element.x);
                    y = (e.clientY - element.y);
                    cursor = "X: " + x + "/" + element.width + " and Y : " + y + "/" + element.height;
                    document.getElementById("displayArea").innerHTML = cursor;
                    document.getElementById("imagePosX").value = x;
                    document.getElementById("imagePosY").value = y;
                }
            }
            var test2 = null;
            function handleSelectObjet(e) {
                stopTracking();

                allowTracking = false;

                document.getElementById("btnSearchObjet").click();
            }
            function stopTracking() {
                cursor = "Pas sur l'image";
                document.getElementById("displayArea").innerHTML = cursor;

            }



            </c:if>
            function detailObjetLinked(obj) {
                var btn = document.getElementById("btnDetailLinkedObjet");
                var actionBtn = btn.value;


                var actionComplete = actionBtn + "[${SalleFictivesUtils.JSP_ATTRIBUT_OBJET_LINK_UUID}=" + obj.id + "][x=" + parseInt(obj.style.left) + "][y=" + parseInt(obj.style.top) + "]";

                btn.value = actionComplete;
                btn.click();

            }

        </script>


        <section>
            <header>
                <h1>Salle fictives</h1>
            </header>
            <%@include file="./filtre.jspf" %>
            <section>
                <h2>Détail</h2>
                <form class="grid-form" method="POST" id="main-form">
                    <nav>
                        <c:if test="${error_message != null}">
                            <section>
                                <p class="error_message">${error_message}</p>
                            </section>

                        </c:if>
                        <button type="submit" name="action" formnovalidate=""
                                value="<%= ActionPage.QUITTER.name()%>">
                            Quitter
                        </button>

                        <c:choose>
                            <c:when test="${EtatPage.VISUALISATION == etatPage}">
                                <button type="submit" name="action"
                                        value="<%= ActionPage.MODIFIER.name()%>">
                                    Modifier...
                                </button>
                                <button type="submit" name="action"
                                        value="<%= ActionPage.SUPPRIMER.name()%>">
                                    Supprimer...
                                </button>

                            </c:when>
                            <c:when test="${EtatPage.MODIFICATION == etatPage}">
                                <button type="submit" name="action"
                                        value="<%= ActionPage.VALIDER_MODIFICATION.name()%>">
                                    Modifier
                                </button>
                            </c:when>
                            <c:when test="${EtatPage.SUPPRESSION == etatPage}">
                                <button type="submit" name="action"
                                        value="<%= ActionPage.VALIDER_SUPPRESSION.name()%>">
                                    Supprimer
                                </button>
                            </c:when>
                            <c:when test="${EtatPage.CREATION == etatPage}">
                                <button type="submit" name="action"
                                        value="<%= ActionPage.VALIDER_CREATION.name()%>">
                                    Créer
                                </button>
                            </c:when>

                        </c:choose>

                    </nav>
                    <%@include file="../../jspf/identifiant.jspf" %>

                    <input hidden="" name="${SalleFictivesUtils.JSP_ATTRIBUT_UUID}" 
                           value="${detail.identifiant.UUID}"/>
                    <input hidden="" name="${SalleFictivesUtils.JSP_ATTRIBUT_VERSION}" 
                           value="${detail.identifiant.version}"/>


                    <label class="new_row" for="salleFictiveNom">nom</label>
                    <input type="text" id="salleFictiveNom" 
                           name="${SalleFictivesUtils.JSP_ATTRIBUT_NOM}"
                           value="${detail.nom}" 
                           <c:if test="${EtatPage.VISUALISATION == etatPage or 
                                         EtatPage.SUPPRESSION == etatPage}">
                                 readonly=""
                           </c:if>

                           placeholder="nom" required="" />

                </form>
                <form method="POST" enctype="multipart/form-data">
                    <section>
                        <c:if test="${EtatPage.MODIFICATION == etatPage
                                      or EtatPage.CREATION == etatPage}">
                              <p id="displayArea">Pas sur l'image</p>
                        </c:if>
                        <span class="new_row">
                            <c:choose>
                                <c:when test='${image_utf8 != ""}'>
                                    <input name="imagePosX" id="imagePosX" hidden="" type="text" value=""/>
                                    <input name="imagePosY" id="imagePosY" hidden="" type="text" value=""/>

                                    <div
                                        style="position:relative;">


                                        <image  style="
                                                z-index: 1;
                                                "
                                                src='${image_utf8}'  alt="image_salle_fictive"  id="image_salle_fictive" 
                                                <c:if test="${EtatPage.MODIFICATION == etatPage
                                                              or EtatPage.CREATION == etatPage}"> 
                                                      onmousemove="getPos(event, this);"
                                                      onmouseout="stopTracking()" 
                                                      onmousedown="handleSelectObjet(event)"


                                                </c:if>/>
                                        <c:if test="${linked_objets != null}">
                                            <c:forEach var="linked_objet" items="${linked_objets}">
                                                <!--<p>y : ${linked_objet.key.y} - offest : ${imgTop}  = ${linked_objet.key.y - imgTop}</p>
                                                <p>x : ${linked_objet.key.x} - offest : ${imgLeft}  = ${linked_objet.key.x - imgLeft}</p>
                                                -->
                                                <image class="linked_objet_image"
                                                       id="${linked_objet.value.identifiant.UUID}"
                                                       title="${linked_objet.value.nom}"
                                                       <c:if test="${EtatPage.CREATION == etatPage 
                                                                     or EtatPage.MODIFICATION == etatPage}">
                                                             onmousemove="getPos(event, document.getElementById('image_salle_fictive'))"
                                                       </c:if>
                                                       onmousedown="detailObjetLinked(this)"
                                                       style="
                                                       position:absolute;
                                                       top: ${linked_objet.key.y}px;
                                                       left: ${linked_objet.key.x}px;

                                                       z-index: 2;
                                                       width: 0.9em;
                                                       height: 0.9em;
                                                       "
                                                       src="${linked_objet.value.getEncodedImage("UTF-8", SalleFictivesUtils.JSP_ATTRIBUT_IMAGEDATA)}"

                                                       />
                                            </c:forEach>
                                        </c:if>
                                    </div>
                                    <button hidden="" id="btnSearchObjet" type="submit" name="action"
                                            value="RECHERCHE_OBJET">

                                    </button>
                                    <button hidden="" id="btnDetailLinkedObjet" type="submit" name="action"
                                            value="DETAIL_LINKED_OBJET">

                                    </button>
                                    <!--<input type="image" src="${image_utf8}" 
                                           name="action" value="SELECT_OBJET" formnovalidate="true"
                                           onclick="return false;"
                                    <c:if test="${EtatPage.MODIFICATION == etatPage
                                                  or EtatPage.CREATION == etatPage}"> 
                                          onmousemove="getPos(event, this)" 
                                          onmouseout="stopTracking()" 
                                          onmousedown="handleSelectObjet(event)"

                                    </c:if>
                                    />-->
                                </c:when>                            
                                <c:otherwise>
                                    <p>Pas d'image trouvé</p>
                                </c:otherwise>
                            </c:choose>
                        </span>
                        <c:if test="${EtatPage.CREATION == etatPage 
                                      or EtatPage.MODIFICATION == etatPage}">
                              <span class="new_row">
                                  <label for="salleFictiveImage">
                                      Modifier l'image:
                                  </label>
                                  <input type="file" id="salleFictiveImage" name="image" accept="image/jpeg" >
                                  <button type="submit" name="action"
                                          value="UPLOAD_IMAGE">
                                      Upload
                                  </button>
                              </span>
                        </c:if>
                    </section>
                    <c:if test="${ (objetList != null) }">
                        <div class="FenetreModale">
                            <div class="FenetreModale-dialog">
                                <button type="submit" name="action"
                                        value="<%= ActionPage.QUITTER_RECHERCHE.name()%>">
                                    Fermer
                                </button>

                                <c:if test="${objetList != null}">
                                    <c:forEach var="objet" items="${objetList}">
                                        <article>
                                            <header>
                                                <h3>${objet.nom}</h3>
                                                <p>${objet.description}</p>
                                            </header>
                                            <button type="submit" name="action"
                                                    value="<%= ActionPage.SELECTIONNER_OBJET.name()%>[${SalleFictivesUtils.JSP_ATTRIBUT_OBJET_LINK_UUID}=${objet.identifiant.UUID}]">
                                                Selectionner
                                            </button>
                                        </article>
                                    </c:forEach>
                                </c:if>

                            </div>
                        </div>
                    </c:if>  
                    <c:if test="${objetLinked != null}">        
                        <div class="FenetreModale" >
                            <div class="FenetreModale-dialog">
                                <button type="submit" name="action"
                                        value="QUITTER_DETAIL_LINKED">
                                    Fermer
                                </button>
                                <c:forEach var="objet_linked" items="${objetLinked}">

                                    <article>
                                        <header>
                                            <h3>${objet_linked.value.nom}</h3>
                                            <p>${objet_linked.value.description}</p>

                                            <p>X: <fmt:formatNumber value="${objet_linked.key.x}" maxFractionDigits="0"/> Y: <fmt:formatNumber value="${objet_linked.key.y}" maxFractionDigits="0" /></p>
                                        </header>
                                        <c:if test="${EtatPage.CREATION == etatPage 
                                                      or EtatPage.MODIFICATION == etatPage}">
                                              <button type="submit" name="action"
                                                      value="SUPPRIMER_LINKED_OBJET[${SalleFictivesUtils.JSP_ATTRIBUT_OBJET_LINK_UUID}=${objet_linked.value.identifiant.UUID}][x=<fmt:formatNumber value="${objet_linked.key.x}" maxFractionDigits="0"/>][y=<fmt:formatNumber value="${objet_linked.key.y}" maxFractionDigits="0"/>]">
                                                  Supprimer
                                              </button>
                                        </c:if>
                                    </article>
                                </c:forEach>

                            </div>
                        </div>
                    </c:if>
                </form>

            </section>

        </section>
    </body>

</html>
