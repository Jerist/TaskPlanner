package ru.bulavin.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class JspPathCreator {
    private final static String JSP_DEFAULT_FORMAT = "/WEB-INF/jsp/%s.jsp";
    private final static String JSP_USER_FORMAT = "/WEB-INF/jsp/user/%s.jsp";

    public static String getDefaultPath(String jsp) {
        return JSP_DEFAULT_FORMAT.formatted(jsp);
    }

    public static String getUserPath(String jsp) {
        return JSP_USER_FORMAT.formatted(jsp);
    }

}