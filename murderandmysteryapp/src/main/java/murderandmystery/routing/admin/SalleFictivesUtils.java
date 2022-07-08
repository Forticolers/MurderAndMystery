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
      
        String nom = request.getParameter(JSP_ATTRIBUT_NOM);
        HashMap<Point, Objet> objetsModification = new HashMap<>();

        //TODO READ OBJET LINKED
      
        SalleFictiveBase.Builder builder = SalleFictiveBase.builder()
                .identifiant(identifiant)
                .nom(nom)
               
                .objets(objetsModification);

        return builder.build();
    }

    public static void displayImage(final SalleFictive detail, final HttpServletRequest request, final HttpServletResponse response) throws UnsupportedEncodingException {
      

    }

    public static void displayObjets(final SalleFictive detail, HttpServletRequest request, final HttpServletResponse response) {
       

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
