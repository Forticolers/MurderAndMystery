package core.web.routing;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jeanbourquj
 */
public interface Action {

    void execute(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException;

    /**
     * Action permettant la redirection vers l'url saisi dans le constructeur.
     */
    class Redirect implements Action {

        private final String url;

        public Redirect(final String url) {
            this.url = url;
        }

        @Override
        public void execute(
                final HttpServletRequest request,
                final HttpServletResponse response)
                throws ServletException, IOException {
            response.sendRedirect(this.url);
        }
    }

    /**
     * Action permettant de transférer la requète vers le path saisi dans le
     * constructeur.
     */
    class Forward implements Action {

        private final String path;

        public Forward(final String path) {
            this.path = path;
        }

        @Override
        public void execute(
                final HttpServletRequest request,
                final HttpServletResponse response)
                throws ServletException, IOException {

            request.getRequestDispatcher(path)
                    .forward(request, response);
        }
    }
    
}
