/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package murderandmystery.routing;

/**
 *
 * @author jeanbourquj
 */
public final class PersonnageUtils {

    private PersonnageUtils() {

    }
    public static final String ELEMENT = "personnages";
    public static final String URL_CODE_CONNEXION_REGEX = ".*/" + ELEMENT + "/([0-9a-zA-Z]*).html";

    public static final String PAGE_JSP = "/WEB-INF/personnages/detail.jsp";
    public static final String TEMPLATE_URL = "/personnages/%s.html";
    public static final String TEMPLATE_URL_CONNEXION = "%s/connexion.html";
    public static final String JSP_ATTRIBUT_ETAT_PAGE = "etatPage";
    public static final String JSP_ATTRIBUT_CODE_CONNEXION = "codeConnexion";
    public static final String JSP_ATTRIBUT_FENETRE_MODALE = "fenetreModale";
    public static final String JSP_ATTRIBUT_FILTRE = "filtre";
    public static final String JSP_ATTRIBUT_LISTE = "liste";
    public static final String JSP_ATTRIBUT_DETAIL = "detail";

}
