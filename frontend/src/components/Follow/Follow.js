import React from "react";
import "./Follow.css";
import {
  PersonAddAltOutlined,
  PersonRemoveOutlined,
} from "@mui/icons-material";

const Follow = ({ followed, clik, className }) => {
  return (
    <div
      onClick={clik}
      className={
        followed
          ? "followWrapper followed unselectable"
          : "followWrapper unfollowed unselectable"
      }
    >
      {followed ? (
        <>
          <span className="unfollowIcon">
            <PersonRemoveOutlined />
          </span>
          <p className="followText">Unfollow</p>
        </>
      ) : (
        <>
          <span className="followIcon">
            <PersonAddAltOutlined />
          </span>
          <p className="followText">Follow</p>
        </>
      )}
    </div>
  );
};

export default Follow;
