package com.segmentationfault.huceng.entity.repository;

import com.segmentationfault.huceng.entity.Banned;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BannedRepository extends JpaRepository<Banned, Long> {

    Integer countByUsernameAndEmail(String username, String email);

    Integer countByUsername(String username);

    Integer countByEmail(String email);

    Optional<Banned> findByUsername(String username);
}
