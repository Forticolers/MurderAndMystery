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
import core.web.routing.Action;
import core.web.routing.Routage;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import murderandmystery.datasource.MapperManager;
import murderandmystery.datasource.Utils;
import murderandmystery.domain.Objet;
import murderandmystery.domain.ObjetBase;
import murderandmystery.domain.SalleFictive;
import murderandmystery.routing.ActionPage;
import murderandmystery.routing.EtatPage;

/**
 *
 * @author jeanbourquj
 */
@WebServlet(name = "SalleFictiveRoutage",
        urlPatterns = {"/admin/salle_fictives/*"})
@MultipartConfig(maxFileSize = 1073741824)
public class SalleFictiveRoutage extends Routage {

    private final Pattern idPatternRegex;

    private TransactionManager transactionManager;
    private static final Logger LOG
            = Logger.getLogger(SalleFictiveRoutage.class.getName());

    public SalleFictiveRoutage() {
        idPatternRegex = Pattern.compile(SalleFictivesUtils.ID_PATTERN_REGEX);
    }

    @Override
    public void init() throws ServletException {

        this.transactionManager
                = Utils.getTransactionManager(this.getServletContext());

        this.getActions().put(ActionPage.VISUALISER.name(), new Action.Forward(SalleFictivesUtils.PAGE_JSP_DETAIL) {
            @Override
            public void execute(
                    final HttpServletRequest request,
                    final HttpServletResponse response)
                    throws ServletException, IOException {

                try {
                    RequestUtils.validateAdminSession(request, response);
                    if (RequestUtils.adminSession(request)) {
                        SalleFictive detail = getDetail(request);
                        String test = request.getParameter("offsetTop");

                        if (detail != null) {
                            SalleFictivesUtils.displayImage(detail, request, response);
                            SalleFictivesUtils.displayObjets(detail, request, response);
                            request.setAttribute(SalleFictivesUtils.JSP_ATTRIBUT_DETAIL,
                                    detail);

                            request.getSession().setAttribute(
                                    SalleFictivesUtils.JSP_ATTRIBUT_ETAT_PAGE,
                                    EtatPage.VISUALISATION);
                            super.execute(request,
                                    response);
                        } else {
                            response.sendError(
                                    HttpServletResponse.SC_NOT_FOUND);
                        }
                    }
                } catch (PersistenceException ex) {
                    LOG.log(Level.SEVERE,
                            null,
                            ex);
                    response.sendError(
                            HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                            ex.toString());

                }
            }
        }
        );

        this.getActions()
                .put(ActionPage.UPLOAD_IMAGE.name(),
                        new Action.Forward(SalleFictivesUtils.PAGE_JSP_DETAIL) {
                    @Override
                    public void execute(
                            final HttpServletRequest request,
                            final HttpServletResponse response)
                            throws ServletException, IOException {
                        RequestUtils.validateAdminSession(request, response);
                        if (RequestUtils.adminSession(request)) {
                            Part part = null;
                            try {
                                part = request.getPart(SalleFictivesUtils.JSP_PART_IMAGE);
                            } catch (IOException ex) {

                                LOG.log(Level.SEVERE,
                                        null,
                                        ex);
                                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                                        ex.toString());
                            } catch (ServletException ex) {

                                LOG.log(Level.SEVERE,
                                        null,
                                        ex);
                                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                                        ex.toString());
                            }
                            boolean tests = part.getSubmittedFileName().equals("");

                            //String fileName = Paths.get(part.getSubmittedFileName()).getFileName().toString();
                            byte[] submitedDataImage = part.getInputStream().readAllBytes();
                            //String returnImage = UploadImage(request, response, fileName);
                            byte[] encodedBytes = Base64.getEncoder().encode(submitedDataImage);
                            //detail.setImage();
                            String base64EncodedImage = new String(encodedBytes, "UTF-8");
                            SalleFictive detail;
                            try {
                                detail = getDetail(request);

                                if (detail != null) {
                                    request.setAttribute(SalleFictivesUtils.JSP_ATTRIBUT_DETAIL,
                                            detail);
                                }
                                SalleFictivesUtils.displayObjets(detail, request, response);
                                if (part != null && !part.getSubmittedFileName().equals("")) {
                                    request.setAttribute(SalleFictivesUtils.JSP_ATTRIBUT_IMAGEUTF8,
                                            SalleFictivesUtils.JSP_ATTRIBUT_IMAGEDATA + base64EncodedImage);
                                    request.getServletContext().setAttribute(SalleFictivesUtils.JSP_ATTRIBUT_IMAGECONTEXT,
                                            submitedDataImage);
                                } else {
                                    byte[] encodedBytesOriData = Base64.getEncoder().encode(detail.getImage());
                                    String base64EncodedImageOriData = new String(encodedBytesOriData, "UTF-8");
                                    request.setAttribute(SalleFictivesUtils.JSP_ATTRIBUT_IMAGEUTF8,
                                            SalleFictivesUtils.JSP_ATTRIBUT_IMAGEDATA + base64EncodedImageOriData);
                                    request.getServletContext().setAttribute(SalleFictivesUtils.JSP_ATTRIBUT_IMAGECONTEXT,
                                            encodedBytesOriData);
                                }
                                request.getSession().setAttribute(
                                        SalleFictivesUtils.JSP_ATTRIBUT_ETAT_PAGE,
                                        EtatPage.MODIFICATION);
                                super.execute(request, response);
                            } catch (PersistenceException ex) {
                                LOG.log(Level.SEVERE,
                                        null,
                                        ex);
                                response.sendError(
                                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                                        ex.toString());
                            }

                            //SalleFictiveRoutage.this.getActions().get(ActionPage.MODIFIER.name()).execute(request, response);
                        }
                    }
                }
                );

        this.getActions()
                .put(ActionPage.MODIFIER.name(),
                        new Action.Forward(
                                SalleFictivesUtils.PAGE_JSP_DETAIL
                        ) {
                    @Override
                    public void execute(HttpServletRequest request,
                            HttpServletResponse response)
                            throws ServletException, IOException {

                        try {
                            RequestUtils.validateAdminSession(request, response);
                            if (RequestUtils.adminSession(request)) {
                                /*String uuid = RequestUtils.extractId(request,
                            idPatternRegex);
                    final Identifiant id = IdentifiantBase.builder()
                            .uuid(uuid)
                            .build();

                    SalleFictive detail = (SalleFictive) transactionManager
                            .executeTransaction(
                                    new TransactionManager.Operation<MapperManager>() {
                                @Override
                                public Object execute(final MapperManager mm)
                                        throws PersistenceException {
                                    return mm.getSalleFictive()
                                            .retrieve(id);
                                }
                            });*/
                                SalleFictive detail = getDetail(request);

                                if (detail != null) {

                                    if (request.getAttribute(SalleFictivesUtils.JSP_ATTRIBUT_IMAGEUTF8) == null) {
                                        SalleFictivesUtils.displayImage(detail, request, response);
                                    }
                                    SalleFictivesUtils.displayObjets(detail, request, response);
                                    request.getServletContext().setAttribute(SalleFictivesUtils.JSP_ATTRIBUT_IMAGECONTEXT, null);
                                    request.setAttribute(SalleFictivesUtils.JSP_ATTRIBUT_DETAIL,
                                            detail);

                                    request.getSession().setAttribute(
                                            SalleFictivesUtils.JSP_ATTRIBUT_ETAT_PAGE,
                                            EtatPage.MODIFICATION);
                                    super.execute(request,
                                            response);
                                } else {
                                    response.sendError(
                                            HttpServletResponse.SC_NOT_FOUND);
                                }
                            }
                        } catch (PersistenceException ex) {
                            LOG.log(Level.SEVERE,
                                    null,
                                    ex);
                            response.sendError(
                                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                                    ex.toString());

                        }

                    }

                }
                );

        this.getActions()
                .put(ActionPage.VALIDER_MODIFICATION.name(),
                        new Action() {
                    @Override
                    public void execute(HttpServletRequest request,
                            HttpServletResponse response)
                            throws ServletException, IOException {
                        try {
                            RequestUtils.validateAdminSession(request, response);
                            if (RequestUtils.adminSession(request)) {
                                final SalleFictive entite
                                        // = getDetail(request);
                                        = SalleFictivesUtils.lireFormulaire(request, transactionManager);
                                SalleFictive entiteDb = getDetail(request);
                                byte[] testImage = (byte[]) request.getServletContext().getAttribute(SalleFictivesUtils.JSP_ATTRIBUT_IMAGECONTEXT);
                                if (testImage != null) {
                                    entite.setImage(testImage);
                                } else {

                                    entite.setImage(entiteDb.getImage());
                                }
                                //SalleFictive detail = getDetail(request);
                                //HashMap<Point, Objet> linkedObjet = (HashMap<Point, Objet>) request.getAttribute("linked_objets_modification");
                                //request.removeAttribute("linked_objets_modification");
                                /*if (linkedObjet == null) {
                                    linkedObjet = new HashMap<>();
                                    linkedObjet.put(new Point(10, 10),
                                            ObjetBase.builder()
                                                    .identifiant(IdentifiantBase
                                                            .builder()
                                                            .uuid(UUID.randomUUID().toString())
                                                            .build()
                                                    )
                                                    .nom("test")
                                                    .build());
                                    entite.setObjets(linkedObjet);
                                }
                                if (linkedObjet != null && linkedObjet.size() != entite.getObjets().size()) {
                                    entite.setObjets(linkedObjet);
                                }*/

                                transactionManager.executeTransaction(
                                        new TransactionManager.Operation<MapperManager>() {
                                    @Override
                                    public Object execute(final MapperManager mm)
                                            throws PersistenceException {
                                        mm.getSalleFictive().update(entite);
                                        return null;
                                    }
                                });
                                response.sendRedirect(String.format(
                                        SalleFictivesUtils.TEMPLATE_URL_DETAIL,
                                        request.getContextPath(),
                                        entite.getIdentifiant().getUUID()));
                            }
                        } catch (PersistenceException ex) {
                            LOG.log(Level.SEVERE,
                                    null,
                                    ex);
                            response.sendError(
                                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                                    ex.toString());
                        }
                    }

                }
                );

        this.getActions()
                .put(ActionPage.QUITTER.name(), new Action() {
                    @Override
                    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                        RequestUtils.validateAdminSession(request, response);
                        if (RequestUtils.adminSession(request)) {
                            EtatPage etatPage = (EtatPage) request.getSession()
                                    .getAttribute(SalleFictivesUtils.JSP_ATTRIBUT_ETAT_PAGE);
                            switch (etatPage) {
                                case SUPPRESSION:
                                case MODIFICATION:
                                    String uuid = RequestUtils.extractId(request,
                                            idPatternRegex);

                                    response.sendRedirect(String.format(
                                            SalleFictivesUtils.TEMPLATE_URL_DETAIL,
                                            request.getContextPath(),
                                            uuid));
                                    break;

                                default:
                                    response.sendRedirect(String.format(
                                            SalleFictivesUtils.TEMPLATE_URL_LISTE,
                                            request.getContextPath()));
                                    break;

                            }
                        }
                    }

                }
                );

        this.getActions()
                .put(ActionPage.SUPPRIMER.name(),
                        new Action.Forward(SalleFictivesUtils.PAGE_JSP_DETAIL) {
                    @Override
                    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                        try {
                            RequestUtils.validateAdminSession(request, response);
                            if (RequestUtils.adminSession(request)) {
                                SalleFictive detail = getDetail(request);
                                if (detail != null) {
                                    if (request.getAttribute(SalleFictivesUtils.JSP_ATTRIBUT_IMAGEUTF8) == null) {
                                        SalleFictivesUtils.displayImage(detail, request, response);
                                    }
                                    SalleFictivesUtils.displayObjets(detail, request, response);
                                    request.setAttribute(SalleFictivesUtils.JSP_ATTRIBUT_DETAIL,
                                            detail);

                                    request.getSession().setAttribute(
                                            SalleFictivesUtils.JSP_ATTRIBUT_ETAT_PAGE,
                                            EtatPage.SUPPRESSION);
                                    super.execute(request,
                                            response);
                                } else {
                                    response.sendError(
                                            HttpServletResponse.SC_NOT_FOUND);
                                }
                            }
                        } catch (PersistenceException ex) {
                            LOG.log(Level.SEVERE,
                                    null,
                                    ex);
                            response.sendError(
                                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                                    ex.toString());

                        }
                    }

                }
                );

        this.getActions()
                .put(ActionPage.VALIDER_SUPPRESSION.name(),
                        new Action.Redirect(
                                String.format(SalleFictivesUtils.TEMPLATE_URL_LISTE,
                                        this.getServletContext().getContextPath())
                        ) {
                    @Override
                    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                        try {
                            RequestUtils.validateAdminSession(request, response);
                            if (RequestUtils.adminSession(request)) {
                                final SalleFictive entite
                                        = SalleFictivesUtils.lireFormulaire(request, transactionManager);

                                transactionManager.executeTransaction(
                                        new TransactionManager.Operation<MapperManager>() {
                                    @Override
                                    public Object execute(final MapperManager mm)
                                            throws PersistenceException {
                                        mm.getSalleFictive().delete(entite);
                                        return null;
                                    }
                                });
                                super.execute(request, response);
                            }
                        } catch (PersistenceException ex) {
                            LOG.log(Level.SEVERE,
                                    null,
                                    ex);
                            response.sendError(
                                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                                    ex.toString());
                        }
                    }

                }
                );

        this.getActions()
                .put(ActionPage.QUITTER_RECHERCHE.name(),
                        new ActionQuitterRecherche(SalleFictivesUtils.PAGE_JSP_DETAIL, transactionManager, this));

        this.getActions()
                .put("QUITTER_DETAIL_LINKED",
                        new ActionQuitterDetail(SalleFictivesUtils.PAGE_JSP_DETAIL, transactionManager, this));

        this.getActions()
                .put("RECHERCHE_OBJET",
                        new ActionRechercheObjet(SalleFictivesUtils.PAGE_JSP_DETAIL, transactionManager, this));
        this.getActions()
                .put("DETAIL_LINKED_OBJET",
                        new ActionDetailLinkedObjet(SalleFictivesUtils.PAGE_JSP_DETAIL, transactionManager, this));

        this.getActions()
                .put("SUPPRIMER_LINKED_OBJET",
                        new ActionSupprimerLinkedObjet(SalleFictivesUtils.PAGE_JSP_DETAIL, transactionManager, this));

        this.getActions()
                .put(ActionPage.SELECTIONNER_OBJET.name(),
                        new ActionSelectObjet(SalleFictivesUtils.PAGE_JSP_DETAIL, transactionManager, this));
        this.getActions()
                .put(ActionPage.QUITTER_ADMIN.name(),
                        new AcceuilAdminRoutage.ActionQuitterAdmin());

        this.setActionNull(
                this.getActions().get(ActionPage.VISUALISER.name()));
    }

    protected SalleFictive getDetail(HttpServletRequest request) throws PersistenceException {
        String uuid = RequestUtils.extractId(request,
                idPatternRegex);
        final Identifiant id = IdentifiantBase.builder()
                .uuid(uuid)
                .build();

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

    public static class ActionQuitterRecherche extends Action.Forward {

        private final TransactionManager transactionManager;
        private final SalleFictiveRoutage salleFictiveRoutage;

        public ActionQuitterRecherche(
                final String page,
                final TransactionManager tm,
                final SalleFictiveRoutage sfr) {
            super(page);
            this.transactionManager = tm;
            this.salleFictiveRoutage = sfr;
        }

        @Override
        public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            try {
                RequestUtils.validateAdminSession(request, response);
                if (RequestUtils.adminSession(request)) {
                    SalleFictive detail = salleFictiveRoutage.getDetail(request);

                    if (detail != null) {
                        request.setAttribute(SalleFictivesUtils.JSP_ATTRIBUT_DETAIL,
                                detail);
                        if (request.getAttribute(SalleFictivesUtils.JSP_ATTRIBUT_IMAGEUTF8) == null) {
                            SalleFictivesUtils.displayImage(detail, request, response);
                        }
                        SalleFictivesUtils.displayObjets(detail, request, response);
                        request.getSession().setAttribute(
                                SalleFictivesUtils.JSP_ATTRIBUT_ETAT_PAGE,
                                EtatPage.MODIFICATION);
                        request.removeAttribute(
                                SalleFictivesUtils.JSP_ATTRIBUT_LISTE_OBJET);
                    }

                    super.execute(request, response);
                }
            } catch (PersistenceException ex) {
                LOG.log(Level.SEVERE,
                        null,
                        ex);
                response.sendError(
                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        ex.toString());

            }
        }

    }

    public static class ActionQuitterDetail extends Action.Forward {

        private final TransactionManager transactionManager;
        private final SalleFictiveRoutage salleFictiveRoutage;

        public ActionQuitterDetail(
                final String page,
                final TransactionManager tm,
                final SalleFictiveRoutage sfr) {
            super(page);
            this.transactionManager = tm;
            this.salleFictiveRoutage = sfr;
        }

        @Override
        public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            try {
                RequestUtils.validateAdminSession(request, response);
                if (RequestUtils.adminSession(request)) {
                    SalleFictive detail = salleFictiveRoutage.getDetail(request);

                    if (detail != null) {
                        request.setAttribute(SalleFictivesUtils.JSP_ATTRIBUT_DETAIL,
                                detail);
                        if (request.getAttribute(SalleFictivesUtils.JSP_ATTRIBUT_IMAGEUTF8) == null) {
                            SalleFictivesUtils.displayImage(detail, request, response);
                        }
                        SalleFictivesUtils.displayObjets(detail, request, response);
//                    request.getSession().setAttribute(
//                            SalleFictivesUtils.JSP_ATTRIBUT_ETAT_PAGE,
//                            EtatPage.MODIFICATION);
                        request.removeAttribute(
                                "objetLinked");
                    }

                    super.execute(request, response);
                }
            } catch (PersistenceException ex) {
                LOG.log(Level.SEVERE,
                        null,
                        ex);
                response.sendError(
                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        ex.toString());

            }
        }

    }

    public static class ActionRechercheObjet extends Action.Forward {

        private final TransactionManager transactionManager;
        private final SalleFictiveRoutage salleFictiveRoutage;

        public ActionRechercheObjet(
                final String page,
                final TransactionManager tm,
                final SalleFictiveRoutage sfr) {
            super(page);
            this.transactionManager = tm;
            this.salleFictiveRoutage = sfr;
        }

        @Override
        public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            try {
                RequestUtils.validateAdminSession(request, response);
                if (RequestUtils.adminSession(request)) {
                    SalleFictive detail = salleFictiveRoutage.getDetail(request);

                    List<Objet> objets
                            = (List<Objet>) transactionManager.executeTransaction(
                                    new TransactionManager.Operation<MapperManager>() {
                                @Override
                                public Object execute(final MapperManager mm)
                                        throws PersistenceException {
                                    return mm.getObjetMapper()
                                            .retrieve(".*");
                                }
                            });

                    String x = request.getParameter("imagePosX");
                    String y = request.getParameter("imagePosY");
                    if (detail != null && objets != null) {
                        request.setAttribute(SalleFictivesUtils.JSP_ATTRIBUT_DETAIL,
                                detail);
                        SalleFictivesUtils.displayImage(detail, request, response);
                        SalleFictivesUtils.displayObjets(detail, request, response);

//                    request.getSession().setAttribute(
//                            SalleFictivesUtils.JSP_ATTRIBUT_ETAT_PAGE,
//                            EtatPage.MODIFICATION);
                        request.setAttribute(
                                SalleFictivesUtils.JSP_ATTRIBUT_LISTE_OBJET,
                                objets);
                        request.getSession().setAttribute("OBJET_LINK_X", x);
                        request.getSession().setAttribute("OBJET_LINK_Y", y);
                        super.execute(request,
                                response);
                    } else {
                        response.sendError(
                                HttpServletResponse.SC_NOT_FOUND);
                    }
                }
            } catch (PersistenceException ex) {
                LOG.log(Level.SEVERE,
                        null,
                        ex);
                response.sendError(
                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        ex.toString());

            }
        }

    }

    public static class ActionSelectObjet extends Action.Forward {

        private final TransactionManager transactionManager;
        private final SalleFictiveRoutage salleFictiveRoutage;

        public ActionSelectObjet(
                final String page,
                final TransactionManager tm,
                final SalleFictiveRoutage sfr) {
            super(page);
            this.transactionManager = tm;
            this.salleFictiveRoutage = sfr;
        }

        @Override
        public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            try {
                RequestUtils.validateAdminSession(request, response);
                if (RequestUtils.adminSession(request)) {
                    SalleFictive detail
                            = //SalleFictivesUtils.lireFormulaire(request, transactionManager);
                            salleFictiveRoutage.getDetail(request);
                    Map<String, String> actionParam
                            = RequestUtils.extractActionAttribut(request);
                    final String objetUUID = actionParam.get(
                            SalleFictivesUtils.JSP_ATTRIBUT_OBJET_LINK_UUID);
                    String x = (String) request.getSession().getAttribute("OBJET_LINK_X");
                    String y = (String) request.getSession().getAttribute("OBJET_LINK_Y");
                    HashMap<Point, Objet> objetsModification = new HashMap<>();
                    Point p = new Point();
                    if (x != null && y != null) {
                        p.x = Integer.parseInt(x);
                        p.y = Integer.parseInt(y);
                    }
                    Objet objet = (Objet) transactionManager.executeTransaction(
                            new TransactionManager.Operation<MapperManager>() {
                        @Override
                        public Object execute(final MapperManager mm)
                                throws PersistenceException {
                            if (objetUUID == null) {
                                return null;

                            }

                            return mm.getObjetMapper().retrieve(
                                    IdentifiantBase.builder()
                                            .uuid(objetUUID)
                                            .build());
                        }
                    });

                    if (request.getAttribute("linked_objets") != null) {
                        objetsModification
                                = (HashMap<Point, Objet>) request.getAttribute("linked_objets");
                        detail.setObjets(objetsModification);
                    }
                    if (objet != null) {
                        detail.addObjet(p, objet);
                    }

                    request.setAttribute(SalleFictivesUtils.JSP_ATTRIBUT_DETAIL,
                            detail);
                    request.getServletContext()
                            .setAttribute("linked_objets_modification", detail.getObjets());
                    SalleFictivesUtils.displayImage(detail, request, response);
                    SalleFictivesUtils.displayObjets(detail, request, response);

//                request.getSession().setAttribute(
//                        SalleFictivesUtils.JSP_ATTRIBUT_ETAT_PAGE,
//                        EtatPage.MODIFICATION);
                    request.removeAttribute(
                            SalleFictivesUtils.JSP_ATTRIBUT_LISTE_OBJET);
                    super.execute(request, response);
                }
            } catch (PersistenceException ex) {
                LOG.log(Level.SEVERE,
                        null,
                        ex);
                response.sendError(
                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        ex.toString());

            }
        }

    }

    public static class ActionDetailLinkedObjet extends Action.Forward {

        private final TransactionManager transactionManager;
        private final SalleFictiveRoutage salleFictiveRoutage;

        public ActionDetailLinkedObjet(
                final String page,
                final TransactionManager tm,
                final SalleFictiveRoutage sfr) {
            super(page);
            this.transactionManager = tm;
            this.salleFictiveRoutage = sfr;
        }

        @Override
        public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            try {
                RequestUtils.validateAdminSession(request, response);
                if (RequestUtils.adminSession(request)) {
                    SalleFictive detail = salleFictiveRoutage.getDetail(request);
                    Map<String, String> actionParam
                            = RequestUtils.extractActionAttribut(request);
                    final String objetUUID = actionParam.get(
                            SalleFictivesUtils.JSP_ATTRIBUT_OBJET_LINK_UUID);
                    final String xStr = actionParam.get("x");
                    final String yStr = actionParam.get("y");

                    if (detail != null) {

                        Point p = new Point(Integer.parseInt(xStr), Integer.parseInt(yStr));
                        SalleFictivesUtils.displayImage(detail, request, response);
                        SalleFictivesUtils.displayObjets(detail, request, response);
                        request.setAttribute("objetLinked", detail.getObjet(p));
                        request.setAttribute(SalleFictivesUtils.JSP_ATTRIBUT_DETAIL,
                                detail);
//                    request.getSession().setAttribute(
//                            SalleFictivesUtils.JSP_ATTRIBUT_ETAT_PAGE,
//                            EtatPage.MODIFICATION);

                        super.execute(request, response);
                    } else {
                        response.sendError(
                                HttpServletResponse.SC_NOT_FOUND);
                    }
                }
            } catch (PersistenceException ex) {
                LOG.log(Level.SEVERE,
                        null,
                        ex);
                response.sendError(
                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        ex.toString());

            }
        }
    }

    public static class ActionSupprimerLinkedObjet extends Action.Forward {

        private final TransactionManager transactionManager;
        private final SalleFictiveRoutage salleFictiveRoutage;

        public ActionSupprimerLinkedObjet(
                final String page,
                final TransactionManager tm,
                final SalleFictiveRoutage sfr) {
            super(page);
            this.transactionManager = tm;
            this.salleFictiveRoutage = sfr;
        }

        @Override
        public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            try {
                RequestUtils.validateAdminSession(request, response);
                if (RequestUtils.adminSession(request)) {
                    SalleFictive detail = salleFictiveRoutage.getDetail(request);
                    Map<String, String> actionParam
                            = RequestUtils.extractActionAttribut(request);
                    final String objetUUID = actionParam.get(
                            SalleFictivesUtils.JSP_ATTRIBUT_OBJET_LINK_UUID);
                    final String xStr = actionParam.get("x");
                    final String yStr = actionParam.get("y");
                    HashMap<Point, Objet> objetsModification = new HashMap<>();
                    Objet objet = (Objet) transactionManager.executeTransaction(
                            new TransactionManager.Operation<MapperManager>() {
                        @Override
                        public Object execute(final MapperManager mm)
                                throws PersistenceException {
                            if (objetUUID == null) {
                                return null;

                            }

                            return mm.getObjetMapper().retrieve(
                                    IdentifiantBase.builder()
                                            .uuid(objetUUID)
                                            .build());
                        }
                    });
                    if (detail != null) {

                        Point p = new Point(Integer.parseInt(xStr), Integer.parseInt(yStr));

                        detail.removeObjet(p);
                        SalleFictivesUtils.displayImage(detail, request, response);
                        SalleFictivesUtils.displayObjets(detail, request, response);
                        request.setAttribute(SalleFictivesUtils.JSP_ATTRIBUT_DETAIL,
                                detail);
                        request.getServletContext()
                                .setAttribute("linked_objets_modification", detail.getObjets());
//                    request.getSession().setAttribute(
//                            SalleFictivesUtils.JSP_ATTRIBUT_ETAT_PAGE,
//                            EtatPage.MODIFICATION);
                        request.removeAttribute(
                                "objetLinked");
                        super.execute(request, response);
                    } else {
                        response.sendError(
                                HttpServletResponse.SC_NOT_FOUND);
                    }
                }
            } catch (PersistenceException ex) {
                LOG.log(Level.SEVERE,
                        null,
                        ex);
                response.sendError(
                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        ex.toString());

            }
        }
    }
}
