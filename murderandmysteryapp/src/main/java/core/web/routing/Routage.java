package core.web.routing;

import core.web.RequestUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import murderandmystery.domain.SalleFictiveBase;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.MultipartStream;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author jeanbourquj
 */
public abstract class Routage extends HttpServlet {

    private Map<String, Action> actions;

    private Action actionNull;

    public Routage() {
        actions = new HashMap<>();

    }

    public Map<String, Action> getActions() {
        return actions;
    }

    public Action getActionNull() {
        return actionNull;
    }

    public void setActionNull(final Action actionNull) {
        this.actionNull = actionNull;
    }

    protected void processRequest(
            final HttpServletRequest request,
            final HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding(StandardCharsets.UTF_8.name());
        String actionStr = RequestUtils.extractActionParameter(request);
        Action action = actions.get(actionStr);
        if (action == null) {
            action = this.actionNull;
        }
        action.execute(request, response);
    }

    @Override
    protected void doGet(
            final HttpServletRequest request,
            final HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);
    }

    @Override
    protected void doPost(
            final HttpServletRequest request,
            final HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);
    }

    /* protected void processMultipartRequested(
            final HttpServletRequest request,
            final HttpServletResponse response)
            throws ServletException, IOException, FileUploadException {
        request.setCharacterEncoding(StandardCharsets.UTF_8.name());
        String actionStr = RequestUtils.extractActionParameter(request);
        Action action = actions.get(actionStr);
        if (action == null) {
            action = this.actionNull;
        }
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(4096);
        List<FileItem> items = new ServletFileUpload(factory).parseRequest(request);
        
        action.execute(request, response);
    }
     //boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        
        //if (!isMultipart) {
     /*} else {
            //Map<String, String[]> tesr3 = request.getParameterMap();

            //Collection<Part> test = request.getParts();
            
            
            try {
                Collection<Part> test = request.getParts();
               // Map<String, String[]> tesr3 = request.getParameterMap();
                DiskFileItemFactory factory = new DiskFileItemFactory();
                ServletContext srvCtx = this.getServletConfig().getServletContext();
                File repo = (File) srvCtx.getAttribute("test");
                factory.setSizeThreshold(4096);
                factory.setRepository(repo);
                ServletFileUpload upload = new ServletFileUpload(factory);
               // Collection<Part> test = request.getParts();
                List<FileItem> items = upload.parseRequest(request);
                
                String field;
                String actionStr;
                Action action = null;
                for (FileItem item : items) {
                    if (item.isFormField()) {
                        field = item.getFieldName();
                        actionStr = item.getString();

                        action = actions.get(actionStr);

                    } else {
                        field = item.getFieldName();
                        request.setAttribute(field, item);
                    }

                }
                action.execute(request, response);
            } catch (FileUploadException ex) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.toString());
            }
        }*/
}
