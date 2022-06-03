package com.segmentationfault.huceng.entity.repository;

import com.segmentationfault.huceng.entity.PendingUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PendingUserRepository extends JpaRepository<PendingUser, Long> {

    Optional<PendingUser> findPendingUserByUsername(String username);

    @Query("select username from PendingUser ")
    List<String> getAllUsernames();

}
