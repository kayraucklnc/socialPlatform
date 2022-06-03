import React from "react";
import FavoriteBorderIcon from "@mui/icons-material/FavoriteBorder";
import FavoriteIcon from "@mui/icons-material/Favorite";
import { pink } from "@mui/material/colors";
import "./Heart.css";

const Heart = ({ type, onClick }) => {
  let isHeart = type === "heart" ? true : false;
  let isHearted = type === "hearted" ? true : false;

  return (
    <>
      {isHeart && (
        <FavoriteBorderIcon className="likeIcons" onClick={onClick} />
      )}
      {isHearted && (
        <FavoriteIcon
          className="likeIcons"
          sx={{ color: pink[500] }}
          onClick={onClick}
        />
      )}
    </>
  );
};

export default Heart;
