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

}
