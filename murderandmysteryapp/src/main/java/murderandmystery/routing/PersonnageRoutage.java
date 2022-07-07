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
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import murderandmystery.datasource.MapperManager;
import murderandmystery.datasource.Utils;
import java.util.Base64;
import murderandmystery.domain.Personnage;
import org.apache.commons.lang3.ArrayUtils;

/**
 *
 * @author jeanbourquj
 */
@WebServlet(name = "PersonnageRoutage",
        urlPatterns = {"/personnages/*"})
public class PersonnageRoutage extends Routage {

    private TransactionManager transactionManager;
    private static final Logger LOG
            = Logger.getLogger(PersonnageRoutage.class.getName());

    @Override
    public void init() throws ServletException {
        this.transactionManager
                = Utils.getTransactionManager(this.getServletContext());

        this.getActions().put(ActionPage.CONNECTED.name(),
                new Action.Forward(PersonnageUtils.PAGE_JSP) {
            @Override
            public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                try {
                    /*String codeConnexion = request.getParameter("codeConnexion");s
                    request.setAttribute("code", codeConnexion);*/
                    String codeConnexion = RequestUtils.extractRegexUrl(request, Pattern.compile(PersonnageUtils.URL_CODE_CONNEXION_REGEX));
                    //request.setAttribute("code", codeConnexion);
                    if (!codeConnexion.equals("") && codeConnexion.equals(request.getSession().getAttribute(RequestUtils.JSP_ATTRIBUT_CODE_CONNECTED_SESSION))) {
                        Personnage detail = (Personnage) transactionManager
                                .executeTransaction((TransactionManager.Operation<MapperManager>) (final MapperManager mm) -> {
                                    try {
                                        return mm.getPersonnageMapper().retrieveEntiteCode(codeConnexion);
                                    } catch (SQLException ex) {
                                        Logger.getLogger(ConnexionPersonnageRoutage.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    return null;
                                });
                        
                        if (detail != null) {
                            if (detail.getImage() != null) {
                                byte[] encodedBytes = Base64.getEncoder().encode(detail.getImage());
                                //detail.setImage();
                                String base64EncodedImage = new String(encodedBytes, "UTF-8");

                                request.setAttribute("image_utf8", "data:image/jpeg;base64," + base64EncodedImage);
                            } else {
                                request.setAttribute("image_utf8", "");
                            }
                            if(detail.getInventaire().size() > 0){
                                request.setAttribute("inventaire_empty", false);
                            }else{
                                request.setAttribute("inventaire_empty", true);
                            }
                            request.setAttribute(PersonnageUtils.JSP_ATTRIBUT_DETAIL, detail);
                            // response.sendRedirect(request.getContextPath() + "/" + detail.getCodeConnexion() + ".html");
                        } else {
                            response.sendError(
                                    HttpServletResponse.SC_NOT_FOUND);
                        }
                    } else {
                        response.sendError(
                                HttpServletResponse.SC_NOT_FOUND);
                    }
                    super.execute(request, response);
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
        /*
        this.getActions().put("TEST", new Action.Forward("/WEB-INF/personnages/detail.jsp") {
            @Override
            public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                
            }
        });
         */
        this.getActions().put(ActionPage.QUITTER.name(),
                new Action.Redirect(String.format(RequestUtils.JSP_PAGE_ACCEUIL_TEMPLATE, this.getServletContext().getContextPath())) {
            @Override
            public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                request.getSession().invalidate();
                super.execute(request, response); //To change body of generated methods, choose Tools | Templates.
            }

        });
        this.setActionNull(new Action() {
            @Override
            public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                if (request.getSession().getAttribute(RequestUtils.JSP_ATTRIBUT_CONNECTED_SESSION) != null) {
                    String code_url = RequestUtils.extractRegexUrl(request, Pattern.compile(PersonnageUtils.URL_CODE_CONNEXION_REGEX));
                    if ((boolean) request.getSession().getAttribute(RequestUtils.JSP_ATTRIBUT_CONNECTED_SESSION)
                            && request.getSession().getAttribute(RequestUtils.JSP_ATTRIBUT_CODE_CONNECTED_SESSION).equals(code_url)) {
                        PersonnageRoutage.this.getActions().get(ActionPage.CONNECTED.name()).execute(request, response);
                    } else {
                        response.sendRedirect(String.format(PersonnageUtils.TEMPLATE_URL_CONNEXION, request.getContextPath()));
                    }

                } else {
                    response.sendRedirect(String.format(PersonnageUtils.TEMPLATE_URL_CONNEXION, request.getContextPath()));
                    //request.getRequestDispatcher("/WEB-INF/connexionPersonnage.jsp").forward(request, response);
                }

            }
        }
        /*new Action.Redirect(this.getServletContext().getContextPath() + "/connexion.html")*/);
    }
}
