import React, { useEffect, useState } from "react";
import Navbar from "../../components/Navbar/Navbar";
import Sidebar from "../../components/Sidebar/Sidebar";
import Feed from "../../components/Feed/Feed";
import Rightbar from "../../components/Rightbar/Rightbar";
import AlertSnackbar from "../../components/Snackbar/AlertSnackbar";
import "./Home.css";

const Home = (props) => {
  const [followPing, setFollowPing] = useState(true);

  const user = JSON.parse(sessionStorage.getItem("user"));

  return (
    <>
      <Navbar setSearched={props.setSearched} setKeyword={props.setKeyword} />
      <AlertSnackbar
        isNotify={props.isNotify}
        setIsNotify={props.setIsNotify}
        messageSender={props.messageSender}
      />
      <div className="homeContainer">
        <Sidebar />
        <Feed
          type="home"
          profilename={user.sub}
          isSearched={props.isSearched}
          keyword={props.keyword}
          followPing={followPing}
          setFollowPing={setFollowPing}
        />
        <Rightbar followPing={followPing} />
      </div>
    </>
  );
};

export default Home;
