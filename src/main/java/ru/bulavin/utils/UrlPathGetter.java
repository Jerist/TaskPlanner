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
    public static final String DELETE_TASK_URL = "/user/deleteTask";
    public static final String ADD_PROJECT_URL = "/user/addProject";
    public static final String PROJECT_INFO_URL = "/user/project";
    public static final String DELETE_TASK_IN_PROJECT_URL = "/user/deleteTaskInProject";
    public static final String DELETE_PROJECT_URL = "/user/deleteProject";
    public static final String UPDATE_TASK_URL = "/user/updateTask";
    public static final String UPDATE_PROJECT_URL = "/user/updateProject";
    public static final String ADD_EXIST_TASK_TO_PROJECT_URL = "/user/addExistingTaskToProject";

    public static String getFullPath(String relativePath) {
        return "/TaskPlanner" + relativePath;
    }
}