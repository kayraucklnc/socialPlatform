import React, { useEffect, useState } from "react";
import "./Rightbar.css";
import Announcement from "../Announcement/Announcement";
import Ad from "../Ad/Ad";
import ShortProfileCard from "../ProfileCard/ShortProfileCard";
import { useParams } from "react-router-dom";
import Box from "@mui/material/Box";
import Grid from "@mui/material/Grid";
import { RvHookupRounded } from "@mui/icons-material";

const Rightbar = ({ profile, followPing, profilename }) => { 
  const HomeRightbar = () => {
    const [announcements, setAnnouncements] = useState([]);
    const [followedUsers, setFollowedUsers] = useState([]);
    const token = sessionStorage.getItem("token");

    const getFolloweds = () => {
      fetch("http://localhost:8080/api/follow/followedUsers", {
        method: "GET",
        headers: {
          "Content-type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      })
        .then((res) => {
          return res.json();
        })
        .then((data) => {
          setFollowedUsers(data);
          return data;
        })
        .catch((error) => {
          console.error("There's an error", error);
        });
    };

    const getAnnouncements = () => {
      fetch("http://localhost:8080/api/announcement/all", {
        method: "GET",
        headers: {
          "Content-type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      })
        .then((res) => {
          return res.json();
        })
        .then((data) => {
          setAnnouncements(data);
          return data;
        })
        .catch((error) => {
          console.error("There's an error", error);
        });
    };

    useEffect(() => {
      getFolloweds();
    }, [followPing]);

    useEffect(() => {
      getAnnouncements();
    }, []);

    return (
      <>
        <Announcement type="sidebar" all={announcements} />
        <Ad />
        <div>
          <div className="followTitle"> Followed Users </div>
          {
            <Box sx={{ flexGrow: 1, padding: 2 }}>
              <Grid container rowSpacing={2} columnSpacing={2}>
                {followedUsers.map((u) => (
                  <Grid key={u.username} style={{ minWidth: 120 }} item xs={4}>
                    <ShortProfileCard appUser={u} />
                  </Grid>
                ))}
              </Grid>
            </Box>
          }
        </div>
      </>
    );
  };

  const ProfileRightBar = () => {
    const [followers, setFollowers] = useState([]);

    const token = sessionStorage.getItem("token");

    useEffect(() => {
      let isMounted = true;
      fetch("http://localhost:8080/api/follow/followers/username/" + profilename, {
        method: "GET",
        headers: {
          "Content-type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      })
        .then((res) => {
          return res.json();
        })
        .then((data) => {
          if (isMounted){
            setFollowers(data);
          }
          return data;
        })
        .catch((error) => {
          console.error("There's an error", error);
        });
      return () => { isMounted = false };
    },[]);

    return (
      <div>
        <div className="followTitle"> Followers </div>
        {
          <Box sx={{ flexGrow: 1, padding: 2 }}>
            <Grid container rowSpacing={2} columnSpacing={2}>
              {followers.map((u) => (
                <Grid key={u.username} style={{ minWidth: 120 }} item xs={2}>
                  <ShortProfileCard appUser={u} />
                </Grid>
              ))}
            </Grid>
          </Box>
        }
      </div>
    );
  };
  
  return (
    <div className={profile ? "rightbar-profile" : "rightbar"}>
      <div className="rightbarWrapper">
        {profile ? <ProfileRightBar /> : <HomeRightbar />}
      </div>
    </div>
  );
};

export default Rightbar;
