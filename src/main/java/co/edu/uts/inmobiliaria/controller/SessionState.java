package co.edu.uts.inmobiliaria.controller;

import javax.servlet.http.HttpSession;

/**
 * Estado temporal de navegación. Se usa para que los formularios POST/redirect
 * no tengan que exponer identificadores ni mensajes en la URL.
 */
public final class SessionState {
    public static final String SELECTED_PROPERTY = "propiedadSeleccionadaId";
    public static final String LOGIN_TARGET = "rutaDespuesLogin";
    public static final String SELECTED_CHAT = "chatSeleccionadoId";
    public static final String EDIT_PROPERTY = "propiedadEditarId";
    public static final String CATALOG_FILTERS = "filtrosCatalogo";

    private SessionState() {
    }

    public static void flash(HttpSession session, String key, String message) {
        if (session != null && message != null) {
            session.setAttribute("flash." + key, message);
        }
    }

    public static String consumeFlash(HttpSession session, String key) {
        if (session == null) {
            return null;
        }
        String attribute = "flash." + key;
        Object value = session.getAttribute(attribute);
        session.removeAttribute(attribute);
        return value == null ? null : value.toString();
    }

    public static Integer integerAttribute(HttpSession session, String key) {
        if (session == null) {
            return null;
        }
        Object value = session.getAttribute(key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        try {
            return value == null ? null : Integer.valueOf(value.toString());
        } catch (NumberFormatException exception) {
            session.removeAttribute(key);
            return null;
        }
    }
}
