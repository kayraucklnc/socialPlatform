package com.segmentationfault.huceng.entity.repository;

import com.segmentationfault.huceng.entity.AppUser;
import com.segmentationfault.huceng.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findAllByAppUser(AppUser appUser);

}
