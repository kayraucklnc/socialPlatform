package com.segmentationfault.huceng.startup;

import com.segmentationfault.huceng.entity.AppUser;
import com.segmentationfault.huceng.entity.Profile;
import com.segmentationfault.huceng.entity.Role;
import com.segmentationfault.huceng.entity.repository.AppUserRepository;
import com.segmentationfault.huceng.entity.repository.ProfileRepository;
import com.segmentationfault.huceng.entity.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j // Logging
public class StartupServiceImpl implements StartupService {
    private final AppUserRepository appUserRepository;
    private final RoleRepository roleRepository;
    private final ProfileRepository profileRepository;

    private final BCryptPasswordEncoder encoder;

    @Override
    public AppUser saveUser(AppUser user) {
        // Here we need to check that the username is actually unique and does not already exist
        Profile profile = new Profile();
        profile.setAppUser(user);
        profileRepository.save(profile);
        user.setProfile(profile);

        user.setPassword(encoder.encode(user.getPassword()));
        return appUserRepository.save(user);
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public void assignRoleToUser(String username, String roleName) {
        AppUser appUser = appUserRepository.findAppUserByUsername(username).orElseThrow();
        Role role = roleRepository.findByName(roleName).orElseThrow();


        appUser.getRoles().add(role);
    }

    @Override
    public AppUser getUser(String username) {
        return appUserRepository.findAppUserByUsername(username).orElseThrow();
    }

    @Override
    public AppUser getUser(Long id) {
        return appUserRepository.findById(id).orElseThrow();
    }
}
