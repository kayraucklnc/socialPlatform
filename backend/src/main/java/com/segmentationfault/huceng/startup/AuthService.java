package com.segmentationfault.huceng.startup;

import com.segmentationfault.huceng.entity.AppUser;
import com.segmentationfault.huceng.entity.Banned;
import com.segmentationfault.huceng.entity.repository.AppUserRepository;
import com.segmentationfault.huceng.entity.repository.BannedRepository;
import com.segmentationfault.huceng.exception.UserBannedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService implements UserDetailsService {
    private final AppUserRepository userRepository;
    private final BannedRepository bannedRepository;

    /*
    Our AppUser and Spring User are different. Here in authentication we find the user with the username
    and then create a Spring User which we return to Spring Security.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepository.findAppUserByUsername(username).orElseThrow(() -> {
            log.error("User not found in the database, username: {}", username);
            return new UsernameNotFoundException("User not found in the database.");
        });

        Optional<Banned> bannedTag = bannedRepository.findByUsername(username);
        bannedTag.ifPresent(tag -> {
            if(tag.getIndefinite())
                throw new UserBannedException(username);

            if(new Date(System.currentTimeMillis()).before(tag.getTimeout()))
                throw new UserBannedException(username, tag.getTimeout());

        });

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });

        return new User(user.getUsername(), user.getPassword(), authorities);

    }
}
