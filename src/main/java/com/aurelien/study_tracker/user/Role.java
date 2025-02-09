package com.aurelien.study_tracker.user;

import java.util.Arrays;
import java.util.List;

public enum Role {
    ADMIN(Arrays.asList(
            Permission.SESSION_CREATE,
            Permission.SESSION_DELETE,
            Permission.TASK_CREATE,
            Permission.TASK_GET_ALL_FOR_USER_ID,
            Permission.TASK_GET_ALL_ACTIVE_FOR_USER_ID,
            Permission.TASK_UPDATE,
            Permission.HIGHLIGHT_GET,
            Permission.USER_DETAILS_GET_FOR_ID,
            Permission.DEMO_USER_LOGIN,
            Permission.DEMO_USER_CREATE,
            Permission.FORGOT_PASSWORD,
            Permission.RESET_PASSWORD,
            Permission.VERIFY_EMAIL,
            Permission.SESSION_TASK_LIST)),

    USER(Arrays.asList(
            Permission.SESSION_CREATE,
            Permission.SESSION_DELETE,
            Permission.TASK_CREATE,
            Permission.TASK_GET_ALL_FOR_USER_ID,
            Permission.TASK_GET_ALL_ACTIVE_FOR_USER_ID,
            Permission.TASK_UPDATE,
            Permission.HIGHLIGHT_GET,
            Permission.USER_DETAILS_GET_FOR_ID,
            Permission.FORGOT_PASSWORD,
            Permission.RESET_PASSWORD,
            Permission.VERIFY_EMAIL,
            Permission.SESSION_TASK_LIST)),

    DEMO_USER(Arrays.asList(
            Permission.SESSION_CREATE,
            Permission.SESSION_DELETE,
            Permission.TASK_CREATE,
            Permission.TASK_GET_ALL_FOR_USER_ID,
            Permission.TASK_GET_ALL_ACTIVE_FOR_USER_ID,
            Permission.TASK_UPDATE,
            Permission.HIGHLIGHT_GET,
            Permission.USER_DETAILS_GET_FOR_ID,
            Permission.DEMO_USER_LOGIN,
            Permission.FORGOT_PASSWORD,
            Permission.RESET_PASSWORD,
            Permission.VERIFY_EMAIL,
            Permission.SESSION_TASK_LIST));


    private List<Permission> permissions;

    Role(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }
}
