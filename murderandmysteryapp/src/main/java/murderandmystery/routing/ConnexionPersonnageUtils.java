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
public final class ConnexionPersonnageUtils {
    private ConnexionPersonnageUtils(){
        
    }
    
    public static final String ELEMENT = "";
    public static final String REGEX_CODE_CONNEXION = "([0-9a-zA-Z]*)";
    public static final String URL_CODE_CONNEXION_REGEX = ".*/personnages/"+REGEX_CODE_CONNEXION +".html";

    public static final String PAGE_JSP_CONNEXION = "/WEB-INF/connexionPersonnage.jsp";
    public static final String TEMPLATE_URL_CONNEXION = "/personnages/%s.html";
    public static final String TEMPLATE_URL = "%s/connexion.html";

    public static final String JSP_ATTRIBUT_ETAT_PAGE = "etatPage";
    public static final String JSP_ATTRIBUT_CODE_CONNEXION = "codeConnexion";
    /*public static final String JSP_ATTRIBUT_CONNECTED_SESSION = "connected";
    public static final String JSP_ATTRIBUT_CODE_CONNECTED_SESSION = "code_connected";*/
    public static final String JSP_ATTRIBUT_FENETRE_MODALE = "fenetreModale";
    public static final String JSP_ATTRIBUT_LISTE_UNITE = "uniteListe";
    public static final String JSP_ATTRIBUT_LISTE_INGREDIENT = "ingredientListe";
    public static final String JSP_ATTRIBUT_FILTRE = "filtre";
    public static final String JSP_ATTRIBUT_LISTE = "liste";
    public static final String JSP_ATTRIBUT_DETAIL = "detail";
   

   /*public static final String JSP_ATTRIBUT_UUID = "uuid";
    public static final String JSP_ATTRIBUT_VERSION = "version";

    public static final String INDEX_ACTION_PARAM = "index";

    public static final String JSP_ATTRIBUT_NOM = "nom";
    public static final String JSP_ATTRIBUT_RECETTE_DETAIL = "recette_detail";
    public static final String JSP_ATTRIBUT_PREPARATION = "preparation";
    public static final String JSP_ATTRIBUT_NOMBRE_PERSONNES = "nombrePersonnes";
    public static final String COMPOSANT_PARAMETRE_PATTERN_REGEX
            = "composants\\[(\\d+)\\]\\d+\\.(\\w+)";
    public static final String JSP_ATTRIBUT_COMPOSANT_UNITE_UUID = "unite_uuid";
    public static final String JSP_ATTRIBUT_COMPOSANT_INGREDIENT_UUID = "ingredient_uuid";
    public static final String JSP_ATTRIBUT_COMPOSANT_QUANTITE = "quantite";
    public static final String JSP_ATTRIBUT_COMPOSANT_COMMENTAIRE = "commentaire";
    public static final String JSP_ATTRIBUT_FOMULAIRE_COMPOSANT = "nouveauComposant";
    public static final String JSP_ATTRIBUT_FOMULAIRE_UNITE_UUID = "nouveauComposant_unite_uuid";
    public static final String JSP_ATTRIBUT_FOMULAIRE_INGREDIENT_UUID = "nouveauComposant_ingredient_uuid";
    public static final String JSP_ATTRIBUT_FOMULAIRE_QUANTITE = "nouveauComposant_quantite";
    public static final String JSP_ATTRIBUT_FOMULAIRE_COMMENTAIRE = "nouveauComposant_commentaire";*/
}
