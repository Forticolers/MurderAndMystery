/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package murderandmystery.routing;

import core.datasource.PersistenceException;
import core.datasource.TransactionManager;
import core.web.RequestUtils;
import core.web.routing.Action;
import core.web.routing.Routage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import murderandmystery.datasource.MapperManager;
import murderandmystery.datasource.Utils;
import murderandmystery.domain.Personnage;

/**
 *
 * @author jeanbourquj
 */
@WebServlet(name = "ConnexionPersonnageRoutage",
        urlPatterns = {"/connexion.html"})
public class ConnexionPersonnageRoutage extends Routage {

    private TransactionManager transactionManager;
    private static final Logger LOG
            = Logger.getLogger(ConnexionPersonnageRoutage.class.getName());

    @Override
    public void init() throws ServletException {
        this.transactionManager
                = Utils.getTransactionManager(this.getServletContext());
        this.getActions().put(ActionPage.CONNEXION.name(), new Action()/*.Redirect(this.getServletContext().getContextPath() + "/personnages/")*/ {
            @Override
            public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                try {
                    String codeConnexion = request.getParameter(ConnexionPersonnageUtils.JSP_ATTRIBUT_CODE_CONNEXION);
                    Matcher matcher = Pattern.compile(ConnexionPersonnageUtils.REGEX_CODE_CONNEXION).matcher(codeConnexion);
                    if (!codeConnexion.isBlank() &&  matcher.matches()) {
                        Personnage detail = (Personnage) transactionManager
                                .executeTransaction((TransactionManager.Operation<MapperManager>) (final MapperManager mm) -> {
                                    try {
                                        return mm.getPersonnageMapper().retrieveEntiteCode(codeConnexion);
                                    } catch (SQLException ex) {
                                        Logger.getLogger(ConnexionPersonnageRoutage.class.getName())
                                                .log(Level.SEVERE, null, ex);
                                    }
                                    return null;
                                });
                        if (detail != null) {
                            //request.setAttribute("code", codeConnexion);
                            //request.setAttribute("action", "TEST");

                            //request.setAttribute("detail", detail);
                            /* RequestDispatcher dispatcher = ConnexionPersonnageRoutage.this.getServletContext()
                                .getRequestDispatcher(
                                        String.format(ConnexionPersonnageUtils.TEMPLATE_URL_CONNEXION, 
                                                detail.getCodeConnexion()));
                        dispatcher.forward(request, response);*/
                            request.getSession().setAttribute(RequestUtils.JSP_ATTRIBUT_CONNECTED_SESSION, true);
                            request.getSession().setAttribute(RequestUtils.JSP_ATTRIBUT_CODE_CONNECTED_SESSION, codeConnexion);
                            //request.getRequestDispatcher(String.format(ConnexionPersonnageUtils.TEMPLATE_URL_CONNEXION, 
                            //detail.getCodeConnexion())).forward(request, response);
                            response.sendRedirect(ConnexionPersonnageRoutage.this.getServletContext().getContextPath()
                                    + String.format(ConnexionPersonnageUtils.TEMPLATE_URL_CONNEXION, detail.getCodeConnexion()));
                        } else {
                            /*request.getSession().removeAttribute(RequestUtils.JSP_ATTRIBUT_CONNECTED_SESSION);
                            request.getSession().removeAttribute(RequestUtils.JSP_ATTRIBUT_CODE_CONNECTED_SESSION);
                           */
                            request.setAttribute(RequestUtils.JSP_ATTRIBUT_ERROR, 
                                   new String(RequestUtils.JSP_ERROR_CODE_PERSONNAGE_INCONNU.getBytes(), "UTF-8"));
                            request.getRequestDispatcher(ConnexionPersonnageUtils.PAGE_JSP_CONNEXION).forward(request, response);
                        }
                    }else{
                        request.setAttribute(RequestUtils.JSP_ATTRIBUT_ERROR, 
                                RequestUtils.JSP_ERROR_CODE_CONNEXION_INVALIDE);
                        request.getRequestDispatcher(ConnexionPersonnageUtils.PAGE_JSP_CONNEXION).forward(request, response);
                    }
                    //super.execute(request, response); //To change body of generated methods, choose Tools | Templates.
                } catch (PersistenceException ex) {
                    LOG.log(Level.SEVERE,
                            null,
                            ex);
                    response.sendError(
                            HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                            ex.toString());

                }
            }
            /*.Forward("/WEB-INF/connexionPersonnage.jsp")*/
        });
        this.getActions().put(ActionPage.QUITTER.name(),
                new Action.Redirect(String.format(RequestUtils.JSP_PAGE_ACCEUIL_TEMPLATE, this.getServletContext().getContextPath())) {
            @Override
            public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                request.getSession().invalidate();
                super.execute(request, response); //To change body of generated methods, choose Tools | Templates.
            }

        });
        this.getActions().put("TEST", new Action.Forward("/WEB-INF/personnages/detail.jsp") {
            @Override
            public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

                request.setAttribute("detail", "PONEY");
                super.execute(request, response);
            }
            /*.Forward("/WEB-INF/connexionPersonnage.jsp")*/
        });
        this.setActionNull(new Action.Forward(ConnexionPersonnageUtils.PAGE_JSP_CONNEXION));
    }
}
