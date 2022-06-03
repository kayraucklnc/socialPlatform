import React, { useEffect } from "react";
import { useState } from "react";
import "./ProfileCard.css";
import Box from "@mui/material/Box";
import Rating from "@mui/material/Rating";
import { GppGood, BusinessCenter } from "@mui/icons-material";
import Follow from "../Follow/Follow";
import { useHistory } from "react-router-dom";

const ProfileCard = ({ appUser, followPing, setFollowPing }) => {
  const curUser = JSON.parse(sessionStorage.getItem("user"));
  const token = sessionStorage.getItem("token");

  const history = useHistory();

  const [isFollowed, setIsFollowed] = useState(false);

  const followGetterHandler = () => {
    fetch("http://localhost:8080/api/follow/" + appUser.id, {
      method: "GET",
      headers: {
        "Content-type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    })
      .then((res) => {
        return res.json();
      })
      .then((e) => {
        setIsFollowed(e.following);
      })
      .catch((error) => {
        console.error("There's an error", error);
      });
  };

  const followHandler = () => {
    fetch("http://localhost:8080/api/follow/" + appUser.id, {
      method: "POST",
      headers: {
        "Content-type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    })
      .then((res) => {
        return res.json();
      })
      .then((e) => {
        setIsFollowed(e.following);
      })
      .catch((error) => {
        console.error("There's an error", error);
      });
  };

  useEffect(() => {
    followGetterHandler();
  }, []);

  useEffect(() => {
    setFollowPing(!followPing);
  }, [isFollowed]);

  const redirectTo = (path) => {
    history.push(path);
    window.scrollTo(0, 0);
  };

  return (
    <div className="tabWrapper">
      <div className="post postWrapper userCard">
        <img
          src={appUser.profile.profilePicture}
          className="cardProfilePic"
          onClick={() => redirectTo(`/profile/${appUser.username}`)}
        ></img>
        <div className="profileCardMiddle">
          <div className="profileCardUsernameHolder">
            <div
              onClick={() => redirectTo(`/profile/${appUser.username}`)}
              className="cardUserName"
            >
              {appUser.username}
            </div>

            {appUser.roles[0].name == "ROLE_ADMIN" && (
              <GppGood style={{ color: "cornflowerblue" }} />
            )}
            {appUser.roles[0].name == "ROLE_ACADEMICIAN" && (
              <BusinessCenter style={{ color: "cornflowerblue" }} />
            )}
            <Box
              className="cardStars"
              sx={{
                "& > legend": { mt: 2 },
                display: "flex",
              }}
            >
              <Rating
                name="read-only"
                value={appUser.profile.rating}
                precision={0.2}
                readOnly
                size="medium"
              />
            </Box>
          </div>
          <div>
            <div className="cardName">
              {appUser.firstName} {appUser.lastName}
            </div>
            <div className="cardName">{appUser.profile.about}</div>
          </div>
        </div>
        {curUser.sub !== appUser.username && (
          <div className="cardFollowButton">
            <Follow
              clik={() => {
                followHandler();
              }}
              followed={isFollowed}
            />
          </div>
        )}
      </div>
    </div>
  );
};

export default ProfileCard;
