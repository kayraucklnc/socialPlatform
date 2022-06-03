package com.segmentationfault.huceng.entity.repository;

import com.segmentationfault.huceng.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

    @Query("select p from Profile p where p.appUser.username=:username")
    Optional<Profile> findProfileByUsername(String username);

}
