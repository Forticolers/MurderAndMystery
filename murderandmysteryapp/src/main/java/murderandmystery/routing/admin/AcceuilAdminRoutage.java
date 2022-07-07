/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package murderandmystery.routing.admin;

import core.datasource.TransactionManager;
import core.web.RequestUtils;
import core.web.routing.Action;
import core.web.routing.Routage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import murderandmystery.datasource.Utils;
import murderandmystery.routing.ActionPage;

/**
 *
 * @author jeanbourquj
 */
@WebServlet(name = "AcceuilAdminRoutage",
        urlPatterns = {"/admin/acceuil.html"})
public class AcceuilAdminRoutage extends Routage {

    private TransactionManager transactionManager;
    private static final Logger LOG
            = Logger.getLogger(AcceuilAdminRoutage.class.getName());

    @Override
    public void init() throws ServletException {
        this.transactionManager
                = Utils.getTransactionManager(this.getServletContext());
        this.getActions().put(ActionPage.CONNECTED.name(), new Action()/*.Forward("/WEB-INF/admin/acceuil.jsp")*/ {
            @Override
            public void execute(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
                
                    RequestUtils.validateAdminSession(request, response);
                    if (RequestUtils.adminSession(request)) {
                        request.getRequestDispatcher("/WEB-INF/admin/acceuil.jsp").forward(request, response);
                    }
//if (request.getSession().getAttribute(RequestUtils.JSP_ATTRIBUT_CONNECTED_ADMIN_SESSION) != null) {
//                    if (RequestUtils.adminSession(request)) {
//                        //request.setAttribute("TEST", request.getSession().getAttribute(RequestUtils.JSP_ATTRIBUT_CONNECTED_ADMIN_SESSION));
//                        
//                    } else {
//                        response.sendRedirect(String.format(RequestUtils.JSP_PAGE_ACCEUIL_TEMPLATE, request.getContextPath()));
//                    }

//                    } else {
//                        //request.getRequestDispatcher("/WEB-INF/admin/acceuil.jsp").forward(request, response);
//                        //request.getRequestDispatcher(RequestUtils.JSP_PAGE_ACCEUIL).forward(request, response);
//                        response.sendRedirect(String.format(RequestUtils.JSP_PAGE_ACCEUIL_TEMPLATE, request.getContextPath()));
//                        //response.sendRedirect(String.format(RequestUtils.JSP_PAGE_ACCEUIL_TEMPLATE, request.getContextPath()));
//                    }
              
                //super.execute(request, response); //To change body of generated methods, choose Tools | Templates.
            }

        });
        this.getActions().put(ActionPage.QUITTER_ADMIN.name(),
                new ActionQuitterAdmin());
        this.setActionNull(this.getActions().get(ActionPage.CONNECTED.name()));
    }

    public static class ActionQuitterAdmin implements Action {

        @Override
        public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

            if (request.getSession().getAttribute(RequestUtils.JSP_ATTRIBUT_CONNECTED_ADMIN_SESSION) != null) {
                request.getSession().removeAttribute(RequestUtils.JSP_ATTRIBUT_CONNECTED_ADMIN_SESSION);
            }
            //AcceuilAdminRoutage.this.getActions().get(ActionPage.CONNECTED.name()).execute(request, response);
            response.sendRedirect(String.format(RequestUtils.JSP_PAGE_ACCEUIL_TEMPLATE, request.getContextPath()));

        }
    }
}
