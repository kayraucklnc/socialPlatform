import React from "react";
import FavoriteIcon from "@mui/icons-material/Favorite";
import { pink } from "@mui/material/colors";
import "./Like.css";

const Like = ({ type, onClick }) => {
  let isLike = type === "like" ? true : false;
  let isLiked = type === "liked" ? true : false;
  return (
    <>
      {isLike && <FavoriteIcon style={{ color: 'gray' }} className="likeIcons" onClick={onClick} />}
      {isLiked && (
        <FavoriteIcon
          className="likeIcons"
          sx={{ color: pink[500] }}
          onClick={onClick}
        />
      )}
    </>
  );
};

export default Like;
