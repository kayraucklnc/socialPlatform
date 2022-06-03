import React, { useEffect } from "react";
import { useState } from "react";
import "./ProfileCard.css";
import Box from "@mui/material/Box";
import Rating from "@mui/material/Rating";
import { GppGood, BusinessCenter } from "@mui/icons-material";
import Follow from "../Follow/Follow";
import { useHistory } from "react-router-dom";

const ShortProfileCard = ({ appUser }) => {
  const curUser = JSON.parse(sessionStorage.getItem("user"));
  const token = sessionStorage.getItem("token");

  const history = useHistory();

  const [isFollowed, setIsFollowed] = useState(false);

  useEffect(() => {
    let isMounted = true;
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
        if (isMounted){
          setIsFollowed(e.following);
        }
      })
      .catch((error) => {
        console.error("There's an error", error);
      });
    
    return () => { isMounted = false };
  }, []);

  const redirectTo = (path) => {
    history.push(path);
    window.scrollTo(0, 0);
  };

  return (
    <div
      style={{ justifyContent: "center" }}
      className="post userCard userCardSmall cardUserName cardUserNameSmall unselectable"
      onClick={() => redirectTo(`/profile/${appUser.username}`)}
    >
      <img
        src={"../" + appUser.profile.profilePicture}
        className="cardProfilePic userCardSmallProfilePic"
      ></img>
      <div>
        <div className="">{appUser.username}</div>
      </div>
    </div>
  );
};

export default ShortProfileCard;
