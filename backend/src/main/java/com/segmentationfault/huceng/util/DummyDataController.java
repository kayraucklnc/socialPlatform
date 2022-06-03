package com.segmentationfault.huceng.util;

import com.segmentationfault.huceng.entity.*;
import com.segmentationfault.huceng.startup.StartupService;
import com.segmentationfault.huceng.usecases.announcement.service.AnnouncementService;
import com.segmentationfault.huceng.usecases.comment.service.CommentService;
import com.segmentationfault.huceng.usecases.following.service.FollowingService;
import com.segmentationfault.huceng.usecases.post.service.PostService;
import com.segmentationfault.huceng.usecases.profile.dto.ProfileRequest;
import com.segmentationfault.huceng.usecases.profile.service.ProfileService;
import com.segmentationfault.huceng.usecases.rate.service.RatingService;
import com.segmentationfault.huceng.usecases.scholarshipJobs.service.ScholarshipJobService;
import com.segmentationfault.huceng.usecases.signup.service.SignupService;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional
public class DummyDataController {
    public static final String LEVENTOGLU = "leventoglu";
    public static final String KAYRAUCKILINC = "kayrauckilinc";
    public static final String OZGUROKUMUS = "ozgurokumus";
    public static final String DAVUTKULAKSIZ = "davutkulaksiz";
    public static final String NIKOLA_DRLJACA = "nikolaDrljaca";

    private StartupService service;
    private AnnouncementService announcementService;
    private PostService postService;
    private ProfileService profileService;
    private RatingService ratingService;
    private CommentService commentService;
    private SignupService signupService;
    private FollowingService followingService;
    private ScholarshipJobService scholarshipJobService;

    private Random random;


    public DummyDataController(StartupService service,
                               ProfileService profileService,
                               AnnouncementService announcementService,
                               PostService postService,
                               RatingService ratingService,
                               CommentService commentService,
                               SignupService signupService,
                               FollowingService followingService,
                               ScholarshipJobService scholarshipJobService) {
        this.announcementService = announcementService;
        this.postService = postService;
        this.service = service;
        this.profileService = profileService;
        this.ratingService = ratingService;
        this.commentService = commentService;
        this.signupService = signupService;
        this.followingService = followingService;
        this.scholarshipJobService = scholarshipJobService;
    }

    public void createDummyData() {
        this.random = new Random();

        createUsers();
        createProfile();
        createAnnouncements();
        createPosts();
        createComments();
        setLikes();
        setRates();
        createPendingUsers();
        createScholarshipJob();
    }

    private void createPendingUsers() {
        createSinglePendingUser("Pending", "User", "mail@mail.com", "pendinguser", "1234", RoleUtil.ROLE_ADMIN);
        for(int i = 0; i< 8; i++) {
            int num = 2+i;
            createSinglePendingUser("Pending" + num, "User" + num, "mail" + num + "@mail.com", "pendinguser" + num, "1234", RoleUtil.ROLE_STUDENT);
        }
        for(int i = 0; i< 5; i++) {
            int num = 10+i;
            createSinglePendingUser("Pending" + num, "User" + num, "mail" + num + "@mail.com", "pendinguser" + (num), "1234", RoleUtil.ROLE_ACADEMICIAN);
        }
        for(int i = 0; i< 3; i++) {
            int num = 15+i;
            createSinglePendingUser("Pending" + num, "User" + num, "mail" + num + "@mail.com", "pendinguser" + (num), "1234", RoleUtil.ROLE_GRADUATE);
        }
    }

    private void createSinglePendingUser(String first_name, String last_name, String email, String username, String password, String role) {
        PendingUser user = new PendingUser(
                null,
                first_name,
                last_name,
                email,
                username,
                password,
                new ArrayList<>()
        );
        signupService.saveUserAsPending(user);
        signupService.assignRoleToPendingUser(user.getUsername(), role);
    }

    private void setRates() {
        ratingService.rateProfileByUsername(KAYRAUCKILINC, LEVENTOGLU, 3.0);
        ratingService.rateProfileByUsername(OZGUROKUMUS, LEVENTOGLU, 5.0);
        ratingService.rateProfileByUsername(DAVUTKULAKSIZ, LEVENTOGLU, 0.0);

        ratingService.rateProfileByUsername(KAYRAUCKILINC, DAVUTKULAKSIZ, 4.0);

        ratingService.rateProfileByUsername(LEVENTOGLU, KAYRAUCKILINC, 2.0);
        ratingService.rateProfileByUsername(DAVUTKULAKSIZ, KAYRAUCKILINC, 5.0);

        ratingService.rateProfileByUsername(DAVUTKULAKSIZ, NIKOLA_DRLJACA, 1.0);
    }

    private void createUsers() {
        createAdminUsers();
        createDummyUsers();
    }

    private void createAdminUsers() {
        ArrayList<AppUser> appUsers = new ArrayList<AppUser>();
        appUsers.add(new AppUser(null, "Nikola", "Drljaca", "drljacan@outlook.com", "nikolaDrljaca", "1234", new ArrayList<>()));
        appUsers.add(new AppUser(null, "Kayra", "Uckilinc", "kayrauckilinc1@gmail.com", "kayrauckilinc", "1234", new ArrayList<>()));
        appUsers.add(new AppUser(null, "Davut", "Kulaksiz", "kulaksiz@gmail.com", "davutkulaksiz", "1234", new ArrayList<>()));
        appUsers.add(new AppUser(null, "Safa", "Leventoglu", "leventoglu@gmail.com", "leventoglu", "1234", new ArrayList<>()));
        appUsers.add(new AppUser(null, "Özgür", "Okumuş", "ozgurokumus@gmail.com", "ozgurokumus", "1234", new ArrayList<>()));

        applyFollows(appUsers);

        for (AppUser appUser: appUsers) {
            service.saveUser(appUser);
        }

        service.assignRoleToUser("nikolaDrljaca", RoleUtil.ROLE_ADMIN);
        service.assignRoleToUser("kayrauckilinc", RoleUtil.ROLE_ADMIN);
        service.assignRoleToUser("davutkulaksiz", RoleUtil.ROLE_ADMIN);
        service.assignRoleToUser("leventoglu", RoleUtil.ROLE_ADMIN);
        service.assignRoleToUser("ozgurokumus", RoleUtil.ROLE_ADMIN);
    }

    private void createDummyUsers() {
        for (int i = 0; i < 3; i++) {
            String username = "student" + (i > 0 ? i: "");
            service.saveUser(new AppUser(null, "Some", "User", "imsonew@gmail.com", username, "1234", new ArrayList<>()));
            service.assignRoleToUser(username, RoleUtil.ROLE_STUDENT);
        }

        for (int i = 0; i < 2; i++) {
            String username = "academician" + (i > 0 ? i: "");
            service.saveUser(new AppUser(null, "Academician", "User", "teacher@gmail.com", username, "1234", new ArrayList<>()));
            service.assignRoleToUser(username, RoleUtil.ROLE_ACADEMICIAN);
        }

        for (int i = 0; i < 2; i++) {
            String username = "graduate" + (i > 0 ? i: "");
            service.saveUser(new AppUser(null, "Graduate", "User", "graduate@gmail.com", username, "1234", new ArrayList<>()));
            service.assignRoleToUser(username, RoleUtil.ROLE_GRADUATE);
        }
    }

    private void applyFollows(List<AppUser> appUsers) {
        applyFollow(appUsers.get(0), appUsers.get(1));
        applyFollow(appUsers.get(0), appUsers.get(2));
        applyFollow(appUsers.get(2), appUsers.get(3));
        applyFollow(appUsers.get(1), appUsers.get(3));
        applyFollow(appUsers.get(1), appUsers.get(0));
        applyFollow(appUsers.get(4), appUsers.get(2));
        applyFollow(appUsers.get(3), appUsers.get(2));
        applyFollow(appUsers.get(3), appUsers.get(0));
    }

    private void applyFollow(AppUser domainUser, AppUser targetUser) {
        domainUser.getFollowedUsers().add(targetUser);
        targetUser.getFollowers().add(domainUser);
    }

    private void setLikes() {
        ArrayList<Post> posts = new ArrayList<>(postService.returnAllPosts());
        postService.likePostWithUser(posts.get(0).getId(), service.getUser(KAYRAUCKILINC).getId());
        postService.likePostWithUser(posts.get(0).getId(), service.getUser(DAVUTKULAKSIZ).getId());
        postService.likePostWithUser(posts.get(0).getId(), service.getUser(OZGUROKUMUS).getId());
        postService.likePostWithUser(posts.get(0).getId(), service.getUser(LEVENTOGLU).getId());

        postService.likePostWithUser(posts.get(1).getId(), service.getUser(LEVENTOGLU).getId());
        postService.likePostWithUser(posts.get(1).getId(), service.getUser(NIKOLA_DRLJACA).getId());

        postService.likePostWithUser(posts.get(2).getId(), service.getUser(DAVUTKULAKSIZ).getId());
        postService.likePostWithUser(posts.get(2).getId(), service.getUser(LEVENTOGLU).getId());
        postService.likePostWithUser(posts.get(2).getId(), service.getUser(OZGUROKUMUS).getId());

        ArrayList<Comment> comments = new ArrayList<>(commentService.getAllComments());
        commentService.likeCommentWithUser(comments.get(0).getId(), service.getUser(LEVENTOGLU).getId());
        commentService.likeCommentWithUser(comments.get(1).getId(), service.getUser(DAVUTKULAKSIZ).getId());
        commentService.likeCommentWithUser(comments.get(3).getId(), service.getUser(DAVUTKULAKSIZ).getId());
        commentService.likeCommentWithUser(comments.get(3).getId(), service.getUser(NIKOLA_DRLJACA).getId());
        commentService.likeCommentWithUser(comments.get(4).getId(), service.getUser(NIKOLA_DRLJACA).getId());
    }

    private void createAnnouncements() {
        announcementService.saveAnnouncement(new Announcement(null,
                "DEL3 Delay",
                "The demo for LinkedHU_CENG has been delayed one week. ",
                this.createPastTime(0, 0, 0, 1), service.getUser(KAYRAUCKILINC)));
        announcementService.saveAnnouncement(new Announcement(null,
                "Regarding Undergraduate Course Schedule",
                "It is been updated, please check the updated schedule.",
                this.createPastTime(0, 0, 3, 1), service.getUser(KAYRAUCKILINC)));
        announcementService.saveAnnouncement(new Announcement(null,
                "Graduate Ice Breaker Discord Group Announcement",
                "All willing graduate students are invited.",
                this.createPastTime(0, 1, 1, 10), service.getUser(NIKOLA_DRLJACA)));
        announcementService.saveAnnouncement(new Announcement(null,
                "For Current Announcements on Erasmus Mobility",
                "See the European Union Coordination website.",
                this.createPastTime(0, 2, 8, 3), service.getUser(DAVUTKULAKSIZ)));
        announcementService.saveAnnouncement(new Announcement(null,
                "Internship Documentation Has Been Updated",
                "Only the current documentation will be taken into consideration.",
                this.createPastTime(0, 3, 14, 12), service.getUser(LEVENTOGLU)));
        announcementService.saveAnnouncement(new Announcement(null,
                "Senate Decision About 2021-2022 Spring Semester Courses",
                "Please check the school's website for Senate Decision.",
                this.createPastTime(0, 8, 10, 20), service.getUser(OZGUROKUMUS)));
        announcementService.saveAnnouncement(new Announcement(null,
                "\"Young Minds, New Ideas 2022 - Ankara\"",
                "With their project LinkedHU_CENG, Segmentation Fault has been awarded in this event.",
                this.createPastTime(1, 1, 1, 1), service.getUser(OZGUROKUMUS)));

        announcementService.saveAnnouncement(new Announcement(null,
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.",
                this.createPastTime(1, 8, 10, 14), service.getUser(KAYRAUCKILINC)));
    }

    private void createPosts() {
        String catLink = "https://www.thesprucepets.com/thmb/w9n4rZdTCbtZiNnkM0jbRloKWho=/941x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/guide-to-cat-eyes-552114-hero-75d820458de24543a35543a584b9eec6.jpg";
        String RL = "https://theme.zdassets.com/theme_assets/1094427/189dce017fb19e3ca1b94b2095d519cc514df22c.jpg";
        String valo1 = "https://images.contentstack.io/v3/assets/bltb6530b271fddd0b1/blta7a40fb9d8bc8653/61d4af200d81d913d2ac70e3/VALORANT_Ep4_A1_Social_Updates_NeonContent_Stack_Thumbnail.png";
        String valo2 = "https://www.riotgames.com/darkroom/1000/9b3da9b650454646cf2a52a1635d5fe2:951146e9be67f6b31f556f888d7ac5a0/beta-key-art-valorant.jpg";
        String valo3 = "https://shiftdelete.net/wp-content/uploads/2022/02/valorant-coktu-mu-erisim-sorunlari-yasaniyor1.jpg";

        postService.createPost(new Post(
                "A Photo of Beytepe I Took Today",
                "/assets/post/9.jpg",
                "Want to see more of Beytepe photos? Please consider interacting with this post.",
                new Date(System.currentTimeMillis() - 1000 * 60), service.getUser(KAYRAUCKILINC)));
        postService.createPost(new Post(
                "What to Do in Ankara on a Rainy Day?",
                "/assets/post/5.jpg",
                "I've been vibing in Tunalı for the last few hours, but I need some new ideas.",
                this.createPastTime(0, 0, 0, 3), service.getUser(DAVUTKULAKSIZ)));
        postService.createPost(new Post(
                "Nintendo64",
                "/assets/post/2.jpg",
                "I've been playing with my dad's Nintendo64 for the past few weeks.",
                this.createPastTime(0, 0, 1, 6), service.getUser(OZGUROKUMUS)));
        postService.createPost(new Post(
                "Look at My Setup!",
                "/assets/post/1.jpg",
                "I've been trying to create this setup for years and it's finally complete!",
                this.createPastTime(0, 1, 8, 20), service.getUser(KAYRAUCKILINC)));
        postService.createPost(new Post(
                "Positivity Week Picture",
                catLink,
                null,
                this.createPastTime(0, 3, 14, 14), service.getUser(NIKOLA_DRLJACA)));
        postService.createPost(new Post(
                "OMG, Rocket League is out on Switch!!",
                RL,
                null,
                this.createPastTime(0, 8, 10, 18), service.getUser(DAVUTKULAKSIZ)));
        postService.createPost(new Post(
                "Anyone Needs a Teammate? Contact Me!",
                valo1,
                null,
                this.createPastTime(1, 2, 3, 9), service.getUser(OZGUROKUMUS)));
        postService.createPost(new Post(
                "Just Found a Great Game",
                valo2,
                "I wish I discovered this game before midterms :'(",
                this.createPastTime(1, 9, 22, 1), service.getUser(KAYRAUCKILINC)));
        postService.createPost(new Post(
                null,
                valo3,
                "I've been playing Valo for 24 hours straight, someone please help me!",
                this.createPastTime(1, 10, 26, 9), service.getUser(LEVENTOGLU)));
        postService.createPost(new Post(
                "Important Announcement",
                null,
                "I expect to see everyone in today's lesson. I'm going to make some important announcements.",
                this.createPastTime(2, 1, 3, 3), service.getUser("academician")));
    }

    private void createComments() {
        String coolImage = "https://media.istockphoto.com/vectors/you-are-great-cartoon-symbol-vector-id520504182";
        ArrayList<Post> posts = new ArrayList<>(postService.returnAllPosts());

        commentService.createComment(new Comment(
                posts.get(0),
                "This looks great!",
                null,
                new Date(System.currentTimeMillis() - 1000 * 30), service.getUser(DAVUTKULAKSIZ)));
        commentService.createComment(new Comment(
                posts.get(2),
                "FYI!",
                coolImage,
                this.createPastTime(0, 0, 1, 5), service.getUser(LEVENTOGLU)));
        commentService.createComment(new Comment(
                posts.get(1),
                "Özgür is right! Indeed!",
                "https://pix10.agoda.net/geo/city/2423/1_2423_02.jpg?ca=6&ce=1&s=1920x822",
                this.createPastTime(0, 0, 0, 2, 20), service.getUser(LEVENTOGLU)));
        commentService.createComment(new Comment(
                posts.get(1),
                "Have you tried visiting Anıtkabir? It looks gorgeous",
                null,
                this.createPastTime(0, 0, 0, 2, 15), service.getUser(OZGUROKUMUS)));
        commentService.createComment(new Comment(
                posts.get(6),
                "I will be there in 2 hours",
                null,
                this.createPastTime(1, 1, 8, 20), service.getUser(KAYRAUCKILINC)));
    }

    private void createProfile(){
//        1-10 range
//        photo: "./assets/post/2.jpg"
//        photo: "./assets/person/2.png"
//        photo: "./assets/banner/2.jpg"
        profileService.updateProfile(KAYRAUCKILINC, new ProfileRequest("./assets/person/1.png", "./assets/banner/1.jpg","I experiment and share"));
        profileService.updateProfile(OZGUROKUMUS, new ProfileRequest("./assets/person/3.png", "./assets/banner/8.jpg","I wish I could have more time to play MMOs"));
        profileService.updateProfile(DAVUTKULAKSIZ, new ProfileRequest("./assets/person/2.png", "./assets/banner/6.jpg","Wannabe Web Developer"));
        profileService.updateProfile(LEVENTOGLU, new ProfileRequest("./assets/person/6.png", "./assets/banner/5.jpg","Your Favorite Discord Admin"));
        profileService.updateProfile(NIKOLA_DRLJACA, new ProfileRequest("./assets/person/7.png", "./assets/banner/3.jpg","Student at Hacettepe University in Ankara, Turkey."));
    }

    private void createScholarshipJob() {
        ScholarshipJob scholarshipJob = new ScholarshipJob(
            "Segmentation Fault Job Advertisement",
            null,
            "Segmentation Fault Team needs new developers for an incredible project! Apply now!",
            this.createPastTime(0, 0, 0, 1, 20),
            service.getUser(LEVENTOGLU)
        );
        scholarshipJobService.createScholarshipJob(scholarshipJob);
        scholarshipJobService.createAppeal(new ScholarshipJobAppeal(
            "Please pick me! I am the best around here!",
            "",
            null,
            this.createPastTime(0, 0, 0, 1, 50),
            scholarshipJob,
            service.getUser("student1")
        ));
        scholarshipJobService.createAppeal(new ScholarshipJobAppeal(
            "I would love to be working with you",
            "",
            null,
            this.createPastTime(0,0,0,1,55),
            scholarshipJob,
            service.getUser("student2")
        ));

        scholarshipJob = new ScholarshipJob(
            "Looking for spirited students to work on a project!",
            "https://www.betterteam.com/images/betterteam-free-job-posting-sites-5877x3918-20210222.jpg?crop=4:3,smart&width=1200&dpr=2",
            "",
            this.createPastTime(0,0,0,3, 10),
            service.getUser("academician")
        );
        scholarshipJobService.createScholarshipJob(scholarshipJob);
        scholarshipJobService.createAppeal(new ScholarshipJobAppeal(
            "Please pick me!! I am the best you can find!",
            "",
            null,
            this.createPastTime(0,0,0,3,22),
            scholarshipJob,
            service.getUser("student1")
        ));
        scholarshipJobService.createAppeal(new ScholarshipJobAppeal(
                "Hello! I'm so thrilled to work on a project! I would love to work with you dear honorable teacher.",
                "",
                null,
                this.createPastTime(0,0,0,3,48),
                scholarshipJob,
                service.getUser("student")
        ));
    }

    // Creates time from current time with random minutes.
    private Date createPastTime(int years, int months, int days, int hours) {
        return this.createPastTime(years, months, days, hours, this.random.nextInt(0, 60));
    }

    // Creates a time past from current time.
    private Date createPastTime(int years, int months, int days, int hours, int setMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -years);
        cal.add(Calendar.MONTH, -months);
        cal.add(Calendar.DAY_OF_YEAR, -days);
        cal.add(Calendar.HOUR, -hours);
        cal.set(Calendar.MINUTE, setMinutes);

        return cal.getTime();
    }

    // Creates a random time in last 2 weeks.
    private Date createRandomTime() {
        int max = 1000 * 60 * 60 * 24 * 14; // 2 weeks
        int min = 1000 * 60; // 1 minute
        return new Date(System.currentTimeMillis() - (this.random.nextInt(max + 1 - min) + min));
    }
}
