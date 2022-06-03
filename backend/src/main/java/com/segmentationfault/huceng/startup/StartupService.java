package com.segmentationfault.huceng.startup;

import com.segmentationfault.huceng.entity.AppUser;
import com.segmentationfault.huceng.entity.Role;

public interface StartupService {

    AppUser saveUser(AppUser user);

    Role saveRole(Role role);

    void assignRoleToUser(String username, String roleName);

    AppUser getUser(String username);

    AppUser getUser(Long id);
}
