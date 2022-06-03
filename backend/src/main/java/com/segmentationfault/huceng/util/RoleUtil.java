package com.segmentationfault.huceng.util;

import com.segmentationfault.huceng.entity.AppUser;

public class RoleUtil {
    public static String ROLE_ADMIN = "ROLE_ADMIN";
    public static String ROLE_STUDENT = "ROLE_STUDENT";
    public static String ROLE_ACADEMICIAN = "ROLE_ACADEMICIAN";
    public static String ROLE_GRADUATE = "ROLE_GRADUATE";

    public static boolean hasRole(AppUser appUser, String role) {
        return appUser.getRoles().stream().anyMatch(a -> a.getName().equals(role));
    }
}
