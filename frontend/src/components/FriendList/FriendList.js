import React from "react";
import { Users } from "../../dummydata";
import Online from "../Online/Online";
import Friend from "../Friend/Friend";
import "./FriendList.css";

const FriendList = ({ type }) => {
  let isOnlineFriends = type === "onlineFriends" ? true : false;
  let isSidebarFriends = type === "sidebarFriends" ? true : false;

  return (
    <>
      {isOnlineFriends && (
        <ul className="rightbarFriendList">
          {Users.map((u) => (
            <Online key={u.id} user={u} />
          ))}
        </ul>
      )}
      {isSidebarFriends && (
        <ul className="sidebarFriendList">
          {Users.map((u) => (
            <Friend key={u.id} user={u} />
          ))}
        </ul>
      )}
    </>
  );
};

export default FriendList;
