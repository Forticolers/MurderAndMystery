/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package murderandmystery.routing.admin;

import core.datasource.PersistenceException;
import core.datasource.TransactionManager;
import core.domain.Identifiant;
import core.domain.IdentifiantBase;
import core.web.RequestUtils;
import java.awt.Point;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.Part;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import murderandmystery.datasource.MapperManager;
import murderandmystery.datasource.db.CheckedFunction;
import murderandmystery.domain.Objet;
import murderandmystery.domain.SalleFictive;
import murderandmystery.domain.SalleFictiveBase;
import murderandmystery.routing.EtatPage;

/**
 *
 * @author jeanbourquj
 */
public class SalleFictivesUtils {

    private SalleFictivesUtils() {

    }
    public static final String ELEMENT = "admin/salle_fictives";
    public static final String ID_PATTERN_REGEX = ".*/" + ELEMENT + "/([\\w-]*).html";

    public static final String PAGE_JSP_LISTE = "/WEB-INF/" + ELEMENT + "/liste.jsp";
    public static final String PAGE_JSP_DETAIL = "/WEB-INF/" + ELEMENT + "/detail.jsp";
    public static final String TEMPLATE_URL_LISTE = "%s/" + ELEMENT + ".html";
    public static final String TEMPLATE_URL_DETAIL = "%s/" + ELEMENT + "/%s.html";

    public static final String JSP_ATTRIBUT_ETAT_PAGE = "etatPage";
    public static final String JSP_ATTRIBUT_FENETRE_MODALE = "fenetreModale";
    public static final String JSP_ATTRIBUT_LISTE_OBJET = "objetList";
    public static final String JSP_ATTRIBUT_FILTRE = "filtre";
    public static final String JSP_ATTRIBUT_LISTE = "liste";
    public static final String JSP_ATTRIBUT_DETAIL = "detail";
    public static final String JSP_PART_IMAGE = "image";
    public static final String JSP_ATTRIBUT_IMAGEUTF8 = "image_utf8";
    public static final String JSP_ATTRIBUT_IMAGEDATA = "data:image/jpeg;base64,";
    public static final String JSP_ATTRIBUT_IMAGECONTEXT = "image_bytes";
    public static final String JSP_ATTRIBUT_OBJET_LINK_UUID = "objet_uuid";

    public static final String JSP_ATTRIBUT_UUID = "uuid";
    public static final String JSP_ATTRIBUT_VERSION = "version";

    public static final String INDEX_ACTION_PARAM = "index";

    public static final String JSP_ATTRIBUT_NOM = "nom";
    public static final String JSP_ATTRIBUT_IMAGE = "image";

    /* public static final String JSP_ATTRIBUT_PREPARATION = "preparation";
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
    private static SalleFictive getDbDetail(final Identifiant id, final TransactionManager transactionManager) throws PersistenceException {
        SalleFictive detail = (SalleFictive) transactionManager
                .executeTransaction(
                        new TransactionManager.Operation<MapperManager>() {
                    @Override
                    public Object execute(final MapperManager mm)
                            throws PersistenceException {
                        return mm.getSalleFictive()
                                .retrieve(id);
                    }

                });
        return detail;
    }

    public static SalleFictive lireFormulaire(
            final HttpServletRequest request,
            final TransactionManager transactionManager)
            throws PersistenceException, UnsupportedEncodingException {

        String uuid = request.getParameter(JSP_ATTRIBUT_UUID);
        if (uuid == "") {
            uuid = UUID.randomUUID().toString();
        }

        Long version = RequestUtils.extractLongParametre(
                request,
                JSP_ATTRIBUT_VERSION);
        Identifiant identifiant = IdentifiantBase.builder()
                .uuid(uuid)
                .version(version)
                .build();
        SalleFictive detailDb = getDbDetail(identifiant, transactionManager);
        String nom = request.getParameter(JSP_ATTRIBUT_NOM);
        HashMap<Point, Objet> objetsModification = new HashMap<>();
        EtatPage etatPage = (EtatPage) request.getSession()
                .getAttribute(SalleFictivesUtils.JSP_ATTRIBUT_ETAT_PAGE);
        byte[] bytesImage = null;
        if (etatPage
                == EtatPage.MODIFICATION) {
            if (request.getServletContext().getAttribute("linked_objets_modification") != null) {
                objetsModification
                        = (HashMap<Point, Objet>) request.getServletContext().getAttribute("linked_objets_modification");

            }
            /* if (request.getServletContext().getAttribute(SalleFictivesUtils.JSP_ATTRIBUT_IMAGECONTEXT) == null) {
                //TODO faire en sorte que les image soient lue ici pendant la modification et création.
                 String base64EncodedImage = detailDb.getEncodedImage("UTF-8", SalleFictivesUtils.JSP_ATTRIBUT_IMAGEDATA);
                request.setAttribute(SalleFictivesUtils.JSP_ATTRIBUT_IMAGEUTF8, base64EncodedImage);
                bytesImage = detailDb.getImage();
            } else {
                byte[] contextBytes = (byte[]) request.getServletContext().getAttribute(SalleFictivesUtils.JSP_ATTRIBUT_IMAGECONTEXT);
                if (!Arrays.equals(contextBytes, detailDb.getImage())) {
                    detailDb.setImage(contextBytes);
                }
                String base64EncodedImage = detailDb.getEncodedImage("UTF-8", SalleFictivesUtils.JSP_ATTRIBUT_IMAGEDATA);
                request.setAttribute(SalleFictivesUtils.JSP_ATTRIBUT_IMAGEUTF8, base64EncodedImage);
                bytesImage = detailDb.getImage();
            }*/
        }
        SalleFictiveBase.Builder builder = SalleFictiveBase.builder()
                .identifiant(identifiant)
                .nom(nom)
                //.image(bytesImage)
                .objets(objetsModification);

        return builder.build();
    }

    public static void displayImage(final SalleFictive detail, final HttpServletRequest request, final HttpServletResponse response) throws UnsupportedEncodingException {
        if (detail.getImage() != null) {

            /* byte[] encodedBytes = Base64.getEncoder().encode(detail.getImage());
                //detail.setImage();
                String base64EncodedImage = new String(encodedBytes, "UTF-8");*/
            if (request.getServletContext().getAttribute(SalleFictivesUtils.JSP_ATTRIBUT_IMAGECONTEXT) == null) {
                String base64EncodedImage = detail.getEncodedImage("UTF-8", SalleFictivesUtils.JSP_ATTRIBUT_IMAGEDATA);
                request.setAttribute(SalleFictivesUtils.JSP_ATTRIBUT_IMAGEUTF8, base64EncodedImage);
            } else {
                byte[] contextBytes = (byte[]) request.getServletContext().getAttribute(SalleFictivesUtils.JSP_ATTRIBUT_IMAGECONTEXT);
                if (!Arrays.equals(contextBytes, detail.getImage())) {
                    detail.setImage(contextBytes);
                }
                String base64EncodedImage = detail.getEncodedImage("UTF-8", SalleFictivesUtils.JSP_ATTRIBUT_IMAGEDATA);
                request.setAttribute(SalleFictivesUtils.JSP_ATTRIBUT_IMAGEUTF8, base64EncodedImage);
            }

        } else {
            request.setAttribute(SalleFictivesUtils.JSP_ATTRIBUT_IMAGEUTF8, "");
        }

    }

    public static void displayObjets(final SalleFictive detail, HttpServletRequest request, final HttpServletResponse response) {
        HashMap<Point, Objet> objetsTest = ((HashMap<Point, Objet>) request.getServletContext()
                .getAttribute("linked_objets_modification") != null
                ? ((HashMap<Point, Objet>) request.getServletContext()
                        .getAttribute("linked_objets_modification"))
                : new HashMap<>());

        /* if (detail.getObjets().size() > 0 
                && request.getSession().getAttribute(SalleFictivesUtils.JSP_ATTRIBUT_ETAT_PAGE) != EtatPage.MODIFICATION) {
            /*CheckedFunction<Point, Objet> throwingConsumer = (key, value) ->{
                request.getSession().getAttribute("linked_objets")
            };
            request.setAttribute("linked_objets", detail.getObjets());
        } else if (request.getSession().getAttribute(SalleFictivesUtils.JSP_ATTRIBUT_ETAT_PAGE) == EtatPage.MODIFICATION 
                && request.getAttribute("linked_objets_modification") != null) {
            request.setAttribute("linked_objets",
                    (HashMap<Point, Objet>) request.getAttribute("linked_objets_modification"));
        }else if (request.getSession().getAttribute(SalleFictivesUtils.JSP_ATTRIBUT_ETAT_PAGE) == EtatPage.MODIFICATION 
                && request.getAttribute("linked_objets_modification") == null && detail.getObjets().size() > 0) {
            request.setAttribute("linked_objets",
                   detail.getObjets());
        }else{
             request.setAttribute("linked_objets", null);
        }*/
        request.setAttribute("modObjetLinked", false);
        CheckedFunction<Point, Objet> compareObjetLinked = (key, value) -> {
            request.setAttribute("modObjetLinked", compareEntite(key, value, objetsTest));
        };
        EtatPage etatPage = (EtatPage) request.getSession().getAttribute(SalleFictivesUtils.JSP_ATTRIBUT_ETAT_PAGE);
        if (!detail.getObjets().isEmpty() || !objetsTest.isEmpty()) {
            if (etatPage
                    == EtatPage.MODIFICATION
                    && !objetsTest.isEmpty()) {
                if (detail.getObjets().size() == objetsTest.size()) {
                    detail.getObjets().forEach(compareObjetLinked);
                    if (request.getAttribute("mobObjetLinked") != null) {
                        if (!(boolean) request.getAttribute("mobObjetLinked")) {

                        }
                    }
                }
                detail.setObjets(objetsTest);
            }

            request.setAttribute("linked_objets", detail.getObjets());
            request.getServletContext().removeAttribute("linked_objets_modification");
        }

    }

    private static boolean compareEntite(final Point key, final Objet value, final HashMap<Point, Objet> entite) {
        boolean returnValue = true;
        if (entite.containsKey(key)) {
            if (entite.get(key).equals(value)) {
                returnValue = true;
            } else {
                returnValue = false;
            }
        } else {
            returnValue = false;
        }
        return returnValue;
    }

}
