package core.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jeanbourquj
 */
public final class RequestUtils {

    public static final String ACTION_PARAMETRE = "action";
    public static final String ACTION_PATTERN_REGEX
            // = "([^\\]]*)(?:\\[([^=]*)=([^\\]]*)\\])";
            = "([^\\]]*)(?:\\[([^=]*)=([^\\]]*)\\])";
    public static final int ACTION_PATTERN_GROUPE_KEY = 2;
    public static final int ACTION_PATTERN_GROUPE_VALUE = 3;
    public static final String JSP_PAGE_ACCEUIL_TEMPLATE = "%s/index.html";
    public static final String JSP_PAGE_ACCEUIL = "/WEB-INF/acceuil.jsp";
    public static final String JSP_ATTRIBUT_CONNECTED_SESSION = "connected";
    public static final String JSP_ATTRIBUT_CONNECTED_ADMIN_SESSION = "connected_admin";
    public static final String JSP_ATTRIBUT_CODE_CONNECTED_SESSION = "code_connected";
    public static final String JSP_ATTRIBUT_ERROR = "error_message";
    public static final String JSP_ERROR_CODE_CONNEXION_INVALIDE = "Code connexion invalide.";
    public static final String JSP_ERROR_NOM_SALLE_UNIQUE = "Ce nom est déjà utilisé pour une autre salle.";
    public static final String JSP_ERROR_IMAGE_UPLOAD = "Veuillez uploader une image.";
    private static final Logger LOG
            = Logger.getLogger(RequestUtils.class.getName());
    public static final String JSP_ERROR_CODE_PERSONNAGE_INCONNU = "Ce code n'est pas associé à un personnage.";

    private RequestUtils() {
    }

    public static String extractRegexUrl(
            final HttpServletRequest req,
            final Pattern idRegex) {
        String id = null;

        Matcher matcher = idRegex.matcher(req.getRequestURL());
        if (matcher.matches()) {
            id = matcher.group(1);

        }
        return id;
    }

    public static String extractId(
            final HttpServletRequest req,
            final Pattern idRegex) {
        String id = null;

        Matcher matcher = idRegex.matcher(req.getRequestURL());
        if (matcher.matches()) {
            id = matcher.group(1);

        }
        return id;
    }

    public static boolean adminSession(final HttpServletRequest request) {
        if (request.getSession().getAttribute(RequestUtils.JSP_ATTRIBUT_CONNECTED_ADMIN_SESSION) != null) {
            return (boolean) request.getSession().getAttribute(RequestUtils.JSP_ATTRIBUT_CONNECTED_ADMIN_SESSION);
        }
        return false;
    }

    public static void validateAdminSession(final HttpServletRequest request, final HttpServletResponse response) 
            throws IOException{
        if(!adminSession(request)){
            response.sendRedirect(String.format(JSP_PAGE_ACCEUIL_TEMPLATE, request.getContextPath()));
        }
    }
    public static String extractActionParameter(
            final HttpServletRequest request) {
        String actionStr = request.getParameter(ACTION_PARAMETRE);
        Pattern actionPattern = Pattern.compile(ACTION_PATTERN_REGEX);
        Object test = null;
        if (actionStr != null) {
            Matcher matcher = actionPattern.matcher(actionStr);
            test = matcher.groupCount();
            if (matcher.find()) {
                actionStr = matcher.group(1);
            }
        }
        return actionStr;
    }

    public static Map<String, String> extractActionAttribut(final HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        Pattern actionPattern = Pattern.compile(ACTION_PATTERN_REGEX);
        String actionStr = request.getParameter(ACTION_PARAMETRE);
        String key;
        String value;
        Object test = null;
        if (actionStr != null) {
            Matcher matcher = actionPattern.matcher(actionStr);
            test = matcher.groupCount();
            while (matcher.find()) {
                key = matcher.group(ACTION_PATTERN_GROUPE_KEY);
                value = matcher.group(ACTION_PATTERN_GROUPE_VALUE);
                map.put(key, value);
            }
        }
        return map;
    }

    public static Long extractLongParametre(
            final HttpServletRequest request,
            final String param) {
        String valeurStr = request.getParameter(param);
        Long valeur = null;
        try {
            if (!(valeurStr == null || "".equals(valeurStr))) {
                valeur = Long.valueOf(valeurStr);
            }
        } catch (NumberFormatException ex) {
            LOG.log(Level.WARNING,
                    String.format("%s, %s", ex.getMessage(), valeurStr));
        }
        return valeur;
    }

    public static Integer extractIntegerParametre(
            final HttpServletRequest request,
            final String param) {
        String valeurStr = request.getParameter(param);
        Integer valeur = null;
        try {
            if (!(valeurStr == null || "".equals(valeurStr))) {
                valeur = Integer.valueOf(valeurStr);
            }

        } catch (NumberFormatException ex) {
            LOG.log(Level.WARNING,
                    String.format("%s, %s", ex.getMessage(), valeurStr));
        }
        return valeur;
    }

    public static Double extractDoubleParametre(
            final HttpServletRequest request,
            final String param) {
        Double valeur = null;
        String valeurStr = request.getParameter(param);
        try {
            if (!(valeurStr == null || "".equals(valeurStr))) {
                valeur = Double.valueOf(valeurStr);
            }
        } catch (NumberFormatException ex) {
            LOG.log(Level.WARNING,
                    String.format("%s, %s", ex.getMessage(), valeurStr));
        }

        return valeur;
    }

}
