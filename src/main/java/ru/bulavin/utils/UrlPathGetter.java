package ru.bulavin.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UrlPathGetter {
    public static final String LOGIN_URL = "/login";
    public static final String REGISTRATION_URL = "/registration";
    public static final String TASKS_LIST_URL = "/user/tasks";
    public static final String DEFAULT_URL = TASKS_LIST_URL;
    public static final String LOGOUT_URL = "/user/logout";
    public static final String ADD_TASK_URL = "/user/addTask";

    public static String getFullPath(String relativePath) {
        return "/TaskPlanner" + relativePath;
    }
}