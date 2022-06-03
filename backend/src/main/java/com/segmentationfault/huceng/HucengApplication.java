package com.segmentationfault.huceng;

import com.segmentationfault.huceng.entity.Role;
import com.segmentationfault.huceng.startup.StartupService;
import com.segmentationfault.huceng.usecases.announcement.service.AnnouncementService;
import com.segmentationfault.huceng.usecases.comment.service.CommentService;
import com.segmentationfault.huceng.usecases.following.service.FollowingService;
import com.segmentationfault.huceng.usecases.post.service.PostService;
import com.segmentationfault.huceng.usecases.profile.service.ProfileService;
import com.segmentationfault.huceng.usecases.rate.service.RatingService;
import com.segmentationfault.huceng.usecases.scholarshipJobs.service.ScholarshipJobService;
import com.segmentationfault.huceng.usecases.signup.service.SignupService;
import com.segmentationfault.huceng.util.DummyDataController;
import com.segmentationfault.huceng.util.RoleUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@SpringBootApplication
public class HucengApplication {

    public static void main(String[] args) {
        SpringApplication.run(HucengApplication.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Allow requests from the localhost:3000 to pass through to Spring Security
     * Cross-Origin mapping.
     *
     * @return
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new
                UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    /**
     * This code will be executed before the app starts.
     * Ideal place to initialize starting users for testing.
     * Also, admins have to be added here since they are predetermined.
     *
     * @return
     */
    @Bean
    CommandLineRunner runner(StartupService service, AnnouncementService announcementService,
                             PostService postService, ProfileService profileService,
                             RatingService ratingService, CommentService commentService,
                             SignupService signupService, FollowingService followingService,
                             ScholarshipJobService scholarshipJobService
    ) {
        return args -> {
            service.saveRole(new Role(null, RoleUtil.ROLE_ADMIN));
            service.saveRole(new Role(null, RoleUtil.ROLE_ACADEMICIAN));
            service.saveRole(new Role(null, RoleUtil.ROLE_STUDENT));
            service.saveRole(new Role(null, RoleUtil.ROLE_GRADUATE));

            DummyDataController dummyDataController = new DummyDataController(
                    service,
                    profileService,
                    announcementService,
                    postService,
                    ratingService,
                    commentService,
                    signupService,
                    followingService,
                    scholarshipJobService
            );
            dummyDataController.createDummyData();
        };
    }
}
