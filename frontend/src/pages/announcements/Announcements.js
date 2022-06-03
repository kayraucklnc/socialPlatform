import React, {useState} from "react";
import Navbar from "../../components/Navbar/Navbar";
import Sidebar from "../../components/Sidebar/Sidebar";
import Feed from "../../components/Feed/Feed";
import Rightbar from "../../components/Rightbar/Rightbar";
import "./Announcements.css";
import AlertSnackbar from "../../components/Snackbar/AlertSnackbar";

const Announcements = (props) => {

  const user = JSON.parse(sessionStorage.getItem("user"));
  return (
    <>
      <Navbar setSearched={props.setSearched} setKeyword={props.setKeyword}/>
      <AlertSnackbar
        isNotify={props.isNotify}
        setIsNotify={props.setIsNotify}
        messageSender={props.messageSender}
      />
      <div className="announcementsContainer">
        <Sidebar />
        <Feed type="announcements" profilename={user.sub} />
        <Rightbar />
      </div>
    </>
  );
};

export default Announcements;
