import React, { useState, useEffect } from "react";
import Home from "./pages/home/Home";
import Login from "./pages/login/Login";
import Register from "./pages/register/Register";
import Profile from "./pages/profile/Profile";
import Announcements from "./pages/announcements/Announcements";
import Admin from "./pages/admin/Admin";
import Messages from "./pages/messages/Messages";
import Jobs from "./pages/jobs/Jobs";
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Redirect,
} from "react-router-dom";

var stompClient = null;
const App = () => {
  const [isSearched, setSearched] = useState(false);
  const [keyword, setKeyword] = useState("");
  const [isNotify, setIsNotify] = useState(false);
  const [messageSender, setMessageSender] = useState("");
  const [stomp, setStomp] = useState(null);
  const userId = sessionStorage.getItem("id");

  useEffect(() => {
    console.log("TRIES TO CONNECT");
    connect();
  }, []);

  const connect = () => {
    const Stomp = require("stompjs");
    var SockJS = require("sockjs-client");
    SockJS = new SockJS("http://localhost:8080/ws");
    stompClient = Stomp.over(SockJS);
    setStomp(stompClient);
    stompClient.connect({}, onConnected, (err) => {
      console.log(err);
    });
    console.log("StompClient: ");
    console.log(stompClient);
  };

  const onConnected = () => {
    console.log("connected");
    console.log("Safa is current");
    stompClient.subscribe(
      "/user/" + userId + "/queue/messages",
      onMessageReceived
    );
  };

  const onMessageReceived = (msg) => {
    const notification = JSON.parse(msg.body);
    console.log(notification);
    setMessageSender(notification.senderName);
    setIsNotify(true);
  };

  return (
    <>
      <Router>
        <Switch>
          <Route path="/login" render={(props) => <Login {...props} />} />
          <Route path="/signup" render={(props) => <Register {...props} />} />
          <Route
            path="/home"
            render={(props) => (
              <Home
                {...props}
                keyword={keyword}
                setKeyword={setKeyword}
                isSearched={isSearched}
                setSearched={setSearched}
                isNotify={isNotify}
                setIsNotify={setIsNotify}
                messageSender={messageSender}
              />
            )}
          />
          <Route
            path="/profile/:profilename"
            render={(props) => (
              <Profile
                {...props}
                keyword={keyword}
                setKeyword={setKeyword}
                isSearched={isSearched}
                setSearched={setSearched}
                isNotify={isNotify}
                setIsNotify={setIsNotify}
                messageSender={messageSender}
              />
            )}
          />
          <Route
            path="/announcements"
            render={(props) => (
              <Announcements
                {...props}
                keyword={keyword}
                setKeyword={setKeyword}
                isSearched={isSearched}
                setSearched={setSearched}
                isNotify={isNotify}
                setIsNotify={setIsNotify}
                messageSender={messageSender}
              />
            )}
          />
          <Route
            path="/admin"
            render={(props) => (
              <Admin
                {...props}
                keyword={keyword}
                setKeyword={setKeyword}
                isSearched={isSearched}
                setSearched={setSearched}
                isNotify={isNotify}
                setIsNotify={setIsNotify}
                messageSender={messageSender}
              />
            )}
          />
          <Route
            path="/messages"
            render={(props) => (
              <Messages
                {...props}
                keyword={keyword}
                setKeyword={setKeyword}
                isSearched={isSearched}
                setSearched={setSearched}
                stompClient={stomp}
                isNotify={isNotify}
                setIsNotify={setIsNotify}
                messageSender={messageSender}
              />
            )}
          />
          <Route
            path="/jobs"
            render={(props) => (
              <Jobs
                {...props}
                keyword={keyword}
                setKeyword={setKeyword}
                isSearched={isSearched}
                setSearched={setSearched}
                isNotify={isNotify}
                setIsNotify={setIsNotify}
                messageSender={messageSender}
              />
            )}
          />
          <Route exact path="/">
            <Redirect to="/login" />
          </Route>
        </Switch>
      </Router>
    </>
  );
};

export default App;
