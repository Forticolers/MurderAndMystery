/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package murderandmystery.routing.admin;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import core.datasource.PersistenceException;
import core.datasource.TransactionManager;
import core.web.RequestUtils;
import core.web.routing.Action;
import core.web.routing.Routage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import murderandmystery.datasource.MapperManager;
import murderandmystery.datasource.Utils;
import murderandmystery.datasource.db.DatabaseSetupImpl;
import murderandmystery.datasource.db.DbMapperManagerImpl;
import murderandmystery.datasource.db.SQL;
import murderandmystery.domain.Personnage;
import murderandmystery.routing.ActionPage;

/**
 *
 * @author jeanbourquj
 */
@WebServlet(name = "ConnexionAdminRoutage",
        urlPatterns = {"/admin/connexion.html"})
public class ConnexionAdminRoutage extends Routage {

    private TransactionManager transactionManager;
    private static final Logger LOG
            = Logger.getLogger(ConnexionAdminRoutage.class.getName());
    private MapperManager dbManager;

    @Override
    public void init() throws ServletException {
        // this.dbManager = new DbMapperManagerImpl();
        this.transactionManager
                = Utils.getTransactionManager(this.getServletContext());

        this.getActions().put(ActionPage.CONNEXION.name(), new Action()/*.Forward(ConnexionAdminUtils.PAGE_JSP_CONNEXION) */ {
            @Override
            public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                try {
                    String codeConnexion = request.getParameter(ConnexionAdminUtils.JSP_ATTRIBUT_CODE_CONNEXION);
                    HashCode hashed = HashCode.fromBytes(Hashing.sha256().hashString(codeConnexion, StandardCharsets.UTF_8).asBytes());
                    HashCode codeBDD = (HashCode) transactionManager.executeTransaction((TransactionManager.Operation<MapperManager>) (final MapperManager mm) -> {
                        try {
                            return ((DatabaseSetupImpl) mm.getDatabaseSetup()).getAdminMapper().retrieveAdmin(codeConnexion);
                        } catch (SQLException ex) {
                            Logger.getLogger(ConnexionAdminRoutage.class.getName()).log(Level.SEVERE, null, ex);
                            request.setAttribute("error_message", ex.toString());
                        } catch (PersistenceException ex) {
                            Logger.getLogger(ConnexionAdminRoutage.class.getName()).log(Level.SEVERE, null, ex);
                            request.setAttribute("error_message", ex.toString());
                        }
                        return "";
                    });
                    if (codeBDD != null) {
                        //request.setAttribute("TE", codeBDD.toString() + "     " + hashed.toString());
                        if (hashed.equals(codeBDD)) {
                            request.setAttribute("TE", "CONNECTED");
                            request.getSession().setAttribute(RequestUtils.JSP_ATTRIBUT_CONNECTED_ADMIN_SESSION, true);
                            //request.getRequestDispatcher("/WEB-INF/admin/acceuil.jsp").forward(request, response);
                            response.sendRedirect(String.format("%s/admin/acceuil.html", request.getContextPath()));
                        }
                    } else {
                        request.setAttribute("error_message", "Ce code ne correspond pas à celui de la base de données.");
                        request.getRequestDispatcher(ConnexionAdminUtils.PAGE_JSP_CONNEXION).forward(request, response);
                    }
                    //super.execute(request, response);
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
        this.getActions().put(ActionPage.QUITTER.name(), new Action.Redirect(String.format(RequestUtils.JSP_PAGE_ACCEUIL_TEMPLATE, this.getServletContext().getContextPath())) {
            @Override
            public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                request.getSession().invalidate();
                super.execute(request, response); //To change body of generated methods, choose Tools | Templates.
            }
        });
        this.setActionNull(new Action.Forward(ConnexionAdminUtils.PAGE_JSP_CONNEXION));
    }

}
