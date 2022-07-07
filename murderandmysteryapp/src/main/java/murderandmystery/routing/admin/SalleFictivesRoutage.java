/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package murderandmystery.routing.admin;

import core.datasource.ContrainteUniquePersistenceException;
import core.datasource.PersistenceException;
import core.datasource.TransactionManager;
import core.domain.IdentifiantBase;
import core.web.RequestUtils;
import core.web.routing.Action;
import core.web.routing.Routage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import murderandmystery.datasource.MapperManager;
import murderandmystery.datasource.Utils;
import murderandmystery.domain.SalleFictive;
import murderandmystery.domain.SalleFictiveBase;
import murderandmystery.routing.ActionPage;
import murderandmystery.routing.EtatPage;
import static murderandmystery.routing.admin.SalleFictivesUtils.JSP_ATTRIBUT_NOM;
import static murderandmystery.routing.admin.SalleFictivesUtils.JSP_ATTRIBUT_UUID;
import static murderandmystery.routing.admin.SalleFictivesUtils.JSP_ATTRIBUT_VERSION;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author jeanbourquj
 */
@WebServlet(name = "SalleFictivesRoutage",
        urlPatterns = {"/admin/salle_fictives.html"})
@MultipartConfig
public class SalleFictivesRoutage extends Routage {

    private TransactionManager transactionManager;
    private static final Logger LOG
            = Logger.getLogger(SalleFictivesRoutage.class.getName());

    @Override
    public void init() throws ServletException {

        this.transactionManager
                = Utils.getTransactionManager(this.getServletContext());

        this.getActions().put(ActionPage.FILTRER.name(),
                new Action.Forward(SalleFictivesUtils.PAGE_JSP_LISTE) {
            @Override
            public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

                try {
                    RequestUtils.validateAdminSession(request, response);
                    if (RequestUtils.adminSession(request)) {
                        List<SalleFictive> liste = new ArrayList<>();

                        final String filtre = request.getParameter(SalleFictivesUtils.JSP_ATTRIBUT_FILTRE);

                        liste = (List<SalleFictive>) transactionManager
                                .executeTransaction(
                                        new TransactionManager.Operation<MapperManager>() {
                                    @Override
                                    public Object execute(final MapperManager mm)
                                            throws PersistenceException {
                                        String filtreValue = (filtre != null ? filtre : "");
                                        return mm.getSalleFictive()
                                                .retrieve(filtreValue);
                                    }
                                });

                        request.setAttribute(SalleFictivesUtils.JSP_ATTRIBUT_LISTE, liste);
                        request.getSession().setAttribute(
                                SalleFictivesUtils.JSP_ATTRIBUT_ETAT_PAGE,
                                EtatPage.LISTE);
                        super.execute(request, response); //To change body of generated methods, choose Tools | Templates.
                    }
                } catch (java.util.regex.PatternSyntaxException ex) {
                    response.sendError(
                            HttpServletResponse.SC_BAD_REQUEST,
                            ex.getMessage());

                } catch (PersistenceException ex) {
                    LOG.log(Level.SEVERE, null, ex);
                    response.sendError(
                            HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                            ex.toString());
                }
            }

        });
        this.getActions().put(ActionPage.CREER.name(),
                new Action.Forward(SalleFictivesUtils.PAGE_JSP_DETAIL) {
            @Override
            public void execute(
                    final HttpServletRequest request,
                    final HttpServletResponse response)
                    throws ServletException, IOException {
                RequestUtils.validateAdminSession(request, response);
                if (RequestUtils.adminSession(request)) {
                    SalleFictive detail = SalleFictiveBase.builder().build();

                    if (detail != null) {
                        request.setAttribute(SalleFictivesUtils.JSP_ATTRIBUT_DETAIL,
                                detail);
                        /*File f = new File(request.getContextPath() + "/WEB-INF/tmp/");
                    request.getServletContext().setAttribute("test", f);*/
                        request.getServletContext().setAttribute(SalleFictivesUtils.JSP_ATTRIBUT_IMAGECONTEXT, null);
                        request.getSession().setAttribute(
                                SalleFictivesUtils.JSP_ATTRIBUT_ETAT_PAGE,
                                EtatPage.CREATION);
                        super.execute(request,
                                response);
                    } else {
                        response.sendError(
                                HttpServletResponse.SC_NOT_FOUND);
                    }

                }
            }

        }
        );

        this.getActions()
                .put(ActionPage.VALIDER_CREATION.name(),
                        new Action() {
                    @Override
                    public void execute(
                            final HttpServletRequest request,
                            final HttpServletResponse response)
                            throws ServletException, IOException {

                        try {
                            RequestUtils.validateAdminSession(request, response);
                            if (RequestUtils.adminSession(request)) {
                                final SalleFictive entite = SalleFictivesUtils.lireFormulaire(request,
                                        transactionManager);
                                if (entite.getIdentifiant().getUUID() != null) {
                                    byte[] testImage = (byte[]) request.getServletContext().getAttribute(SalleFictivesUtils.JSP_ATTRIBUT_IMAGECONTEXT);
                                    if (testImage != null) {

                                        entite.setImage(testImage);
                                        try {
                                            SalleFictive nouvelleEntite
                                                    = (SalleFictive) transactionManager.executeTransaction(
                                                            new TransactionManager.Operation<MapperManager>() {
                                                        @Override
                                                        public Object execute(final MapperManager mm)
                                                                throws PersistenceException {
                                                            return mm.getSalleFictive().create(entite);
                                                        }
                                                    });
                                            response.sendRedirect(String.format(
                                                    SalleFictivesUtils.TEMPLATE_URL_DETAIL,
                                                    request.getContextPath(),
                                                    nouvelleEntite.getIdentifiant().getUUID()));
                                        } catch (ContrainteUniquePersistenceException ex) {
                                            request.setAttribute(RequestUtils.JSP_ATTRIBUT_ERROR, RequestUtils.JSP_ERROR_NOM_SALLE_UNIQUE);
                                            request.getSession().setAttribute(
                                                    SalleFictivesUtils.JSP_ATTRIBUT_ETAT_PAGE,
                                                    EtatPage.CREATION);
                                            request.getRequestDispatcher(SalleFictivesUtils.PAGE_JSP_DETAIL).forward(request, response);
                                        }

                                    } else {
                                        request.setAttribute(RequestUtils.JSP_ATTRIBUT_ERROR, RequestUtils.JSP_ERROR_IMAGE_UPLOAD);
                                        request.getSession().setAttribute(
                                                SalleFictivesUtils.JSP_ATTRIBUT_ETAT_PAGE,
                                                EtatPage.CREATION);
                                        request.getRequestDispatcher(SalleFictivesUtils.PAGE_JSP_DETAIL).forward(request, response);
                                    }
                                } else {
                                    //Map<String, String[]> test2 = request.getParameterMap();
                                    //String test = (request.getParameterValues(JSP_ATTRIBUT_NOM) == null) + "";
                                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                                            "uuid null");
                                }
                            }
                        } catch (PersistenceException ex) {
                            LOG.log(Level.SEVERE,
                                    null,
                                    ex);
                            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
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
                            // File f = new File(request.getParameter("image"));
                            //  Collection<Part> test32 = request.getParts();
                            Part part = null;
                            try {
                                part = request.getPart(SalleFictivesUtils.JSP_PART_IMAGE);
                            } catch (IOException ex) {
                                // TODO Auto-generated catch block
                                ex.printStackTrace();

                                LOG.log(Level.SEVERE,
                                        null,
                                        ex);
                                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                                        ex.toString());
                            } catch (ServletException ex) {
                                // TODO Auto-generated catch block
                                ex.printStackTrace();
                                LOG.log(Level.SEVERE,
                                        null,
                                        ex);
                                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                                        ex.toString());
                            }

                            //String fileName = Paths.get(part.getSubmittedFileName()).getFileName().toString();
                            byte[] test = part.getInputStream().readAllBytes();
                            //String returnImage = UploadImage(request, response, fileName);
                            byte[] encodedBytes = Base64.getEncoder().encode(test);
                            //detail.setImage();
                            String base64EncodedImage = new String(encodedBytes, "UTF-8");

                            request.setAttribute(SalleFictivesUtils.JSP_ATTRIBUT_IMAGEUTF8, SalleFictivesUtils.JSP_ATTRIBUT_IMAGEDATA + base64EncodedImage);
                            request.getServletContext().setAttribute(SalleFictivesUtils.JSP_ATTRIBUT_IMAGECONTEXT, test);
                            request.getSession().setAttribute(
                                    SalleFictivesUtils.JSP_ATTRIBUT_ETAT_PAGE,
                                    EtatPage.CREATION);
                            super.execute(request, response);
                        }
                    }
                }
                );

        this.getActions()
                .put(ActionPage.QUITTER_ADMIN.name(),
                        new AcceuilAdminRoutage.ActionQuitterAdmin());
        //TODO: Implements linked_objet in 'ajouter salle_fictive'

        this.setActionNull(
                this.getActions().get(ActionPage.FILTRER.name()));
    }

}
