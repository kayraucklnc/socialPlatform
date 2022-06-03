import React from "react";
import "./Friend.css";



const Friend = ({user}) => {

  const linkFormatter = (link) => {
    if (link.charAt(0) === ".") {
      link = link.substring(1);
    }

    return link;
  };

  return (
    <li className="sidebarFriend">
      <img className="sidebarFriendImg" src={linkFormatter(user.profilePicture)} alt="" />
      <span className="sidebarFriendName">{user.username}</span>
    </li>
  );
};

export default Friend;
