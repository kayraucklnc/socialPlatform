import React, { useEffect } from "react";
import { useState } from "react";
import "./Feed.css";
import Share from "../Share/Share";
import Post from "../Post/Post";
import Loader from "../Loader/Loader";
import Announcement from "../Announcement/Announcement";
import JobApplicationForm from "../JobApplicationForm/JobApplicationForm";
import { useHistory } from "react-router-dom";
import { SentimentVeryDissatisfiedRounded } from "@mui/icons-material";
import ProfileCard from "../ProfileCard/ProfileCard";

const Feed = ({
  type,
  profilename,
  isSearched,
  keyword,
  followPing,
  setFollowPing,
}) => {
  const [loading, setLoading] = useState(true);
  const [posts, setPosts] = useState([]);
  const [announcements, setAnnouncements] = useState([]);
  const [users, setUsers] = useState([]);
  const [jobs, setJobs] = useState([]);
  const [isHome, setIsHome] = useState(type === "home" ? true : false);
  const [isProfile, setIsProfile] = useState(type === "profile" ? true : false);
  const [isAnnouncements, setIsAnnouncements] = useState(
    type === "announcements" ? true : false
  );
  const [isJobs, setIsJobs] = useState(type === "jobs" ? true : false);

  const user = JSON.parse(sessionStorage.getItem("user"));
  const token = sessionStorage.getItem("token");
  const history = useHistory();

  const opts = {
    method: "GET",
    headers: {
      "Content-type": "application/json",
      Authorization: `Bearer ${token}`,
    },
  };

  useEffect(() => {
    let urlEnds = user.roles[0] !== "ROLE_ADMIN" ? "/feed" : "";
    if (!keyword || keyword === "") {
      if (isHome) {
        fetch("http://localhost:8080/api/post" + urlEnds, opts)
          .then((res) => {
            return res.json();
          })
          .then((data) => {
            setPosts(data);
            return data;
          })
          .then(() => {
            setLoading(false);
          })
          .catch((error) => {
            console.error("There's an error", error);
          });
      } else if (isProfile) {
        fetch(`http://localhost:8080/api/post/${profilename}`, opts)
          .then((res) => {
            return res.json();
          })
          .then((data) => {
            setPosts(data.reverse());
            return data;
          })
          .then(() => {
            setLoading(false);
          })
          .catch((error) => {
            console.error("There's an error", error);
          });
      } else if (isAnnouncements) {
        fetch("http://localhost:8080/api/announcement/all", opts)
          .then((res) => {
            return res.json();
          })
          .then((data) => {
            setAnnouncements(data);
            return data;
          })
          .then(() => {
            setLoading(false);
          })
          .catch((error) => {
            console.error("There's an error", error);
          });
      } else if (isJobs) {
        fetch("http://localhost:8080/api/scholarshipJob/", opts)
          .then((res) => {
            return res.json();
          })
          .then((data) => {
            if (data instanceof Array) {
              setJobs(data);
              console.log(data);
            }
            return data;
          })
          .then(() => {
            setLoading(false);
          })
          .catch((error) => {
            console.error("There's an error", error);
          });
      }
    }
  }, [profilename, keyword]);

  useEffect(() => {
    if (keyword && keyword !== "") {
      setLoading(true);
      const opts = {
        method: "POST",
        headers: {
          "Content-type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          searchTerm: keyword,
          page: 0,
          size: 1000,
        }),
      };

      fetch("http://localhost:8080/api/search/", opts)
        .then((res) => {
          return res.json();
        })
        .then((data) => {
          setUsers(data.appUsers);
          setAnnouncements(data.announcements);
          setPosts(data.posts);

          return data;
        })
        .then(() => {
          setLoading(false);
        })
        .catch((error) => {
          console.error("There's an error", error);
        });
    }
  }, [keyword]);

  return (
    <div className="feed">
      {!isSearched && (isHome || isProfile) && (
        <div className="feedWrapper">
          {profilename && profilename === user.sub && <Share />}
          {loading && (
            <center>
              <Loader />
            </center>
          )}
          {!loading && posts.map((p) => <Post key={p.id} post={p} />)}

          {!loading && posts.length === 0 && (
            <div>
              <div className="notFound">
                <br></br>{" "}
                <SentimentVeryDissatisfiedRounded
                  style={{ fontSize: 500, alignSelf: "center" }}
                />
                <b> No Posts Found! </b>
              </div>
            </div>
          )}
        </div>
      )}
      {!isSearched && isAnnouncements && (
        <div className="feedWrapper">
          {loading && (
            <center>
              <Loader />
            </center>
          )}
          {!loading &&
            announcements.map((a) => (
              <Announcement key={a.id} announcement={a} type="feed" />
            ))}
        </div>
      )}
      {!isSearched && isJobs && (
        <div className="feedWrapper">
          {profilename && profilename === user.sub && user.roles[0] !== "ROLE_STUDENT" && <Share jobs />}
          {loading && (
            <center>
              <Loader />
            </center>
          )}
          {!loading &&
            jobs.map((j) => (
              <JobApplicationForm key={j.id} applicationForm={j} />
            ))}
        </div>
      )}
      {isSearched && (
        <div className="feedWrapper">
          <div id="userResults">
            <div className="searchInfo">
              <b>{users.length}</b> profile result{users.length !== 1 && "s"}{" "}
              found for "<b>{keyword}</b>":
            </div>
            {loading && (
              <center>
                <Loader />
              </center>
            )}
            {!loading &&
              users.map((u) => (
                <ProfileCard
                  key={u.id}
                  appUser={u}
                  followPing={followPing}
                  setFollowPing={setFollowPing}
                />
              ))}
          </div>

          <div id="announcementResults">
            <div className="searchInfo">
              <b>{announcements.length}</b> announcement result
              {announcements.length !== 1 && "s"} found for "<b>{keyword}</b>":
            </div>
            {loading && (
              <center>
                <Loader />
              </center>
            )}
            {!loading &&
              announcements.map((a) => (
                <Announcement key={a.id} announcement={a} type="feed" />
              ))}
          </div>

          <div id="postResults">
            <div className="searchInfo">
              <b>{posts.length}</b> post result{posts.length !== 1 && "s"} found
              for "<b>{keyword}</b>":
            </div>
            {loading && (
              <center>
                <Loader />
              </center>
            )}
            {!loading && posts.map((p) => <Post key={p.id} post={p} />)}
          </div>
          <div>
            {users.length === 0 &&
              announcements.length === 0 &&
              posts.length === 0 && (
                <div>
                  <div className="notFound">
                    <br></br>{" "}
                    <SentimentVeryDissatisfiedRounded
                      style={{ fontSize: 500, alignSelf: "center" }}
                    />
                    <b> No Results Found! </b>
                  </div>
                </div>
              )}
          </div>
        </div>
      )}
    </div>
  );
};

export default Feed;
