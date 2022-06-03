import React from "react";
import "./Sidebar.css";
import {
  Home,
  Campaign,
  Work,
  Message,
  Groups,
  LiveTv,
  School,
  Report,
  AddTask,
  Logout,
  AdminPanelSettings,
} from "@mui/icons-material";
import Footer from "../Footer/Footer";
import FriendList from "../FriendList/FriendList";
import { CSSTransition } from "react-transition-group";
import { useHistory } from "react-router-dom";
import { useState } from "react";
import { useParams } from "react-router-dom";

const Sidebar = () => {
  const [activeSidebar, setActiveSidebar] = useState("closed");
  const user = JSON.parse(sessionStorage.getItem("user"));

  const history = useHistory();

  const redirectTo = (path) => {
    history.push(path);
    window.scrollTo(0, 0);
    history.go(0);
  };

  return (
    <div className="sidebar">
      <div className="sidebarWrapper">
        <ul className="sidebarList">
          <li
            className="sidebarListItem"
            onClick={() => {
              redirectTo("/home");
              window.scrollTo(0, 0);
            }}
          >
            <Home
              className={
                history.location.pathname === "/home"
                  ? "sidebarIcon selectedIcon"
                  : "sidebarIcon"
              }
            />
            <span
              className={
                history.location.pathname === "/home"
                  ? "sidebarListItemText selectedIcon"
                  : "sidebarIcon"
              }
            >
              Home
            </span>
          </li>
          <li
            className="sidebarListItem"
            onClick={() => {
              redirectTo("/announcements");
              window.scrollTo(0, 0);
            }}
          >
            <Campaign
              className={
                history.location.pathname === "/announcements"
                  ? "sidebarIcon selectedIcon"
                  : "sidebarIcon"
              }
            />
            <span
              className={
                history.location.pathname === "/announcements"
                  ? "sidebarListItemText selectedIcon"
                  : "sidebarIcon"
              }
            >
              Announcements
            </span>
          </li>
          <li
            className="sidebarListItem"
            onClick={() => {
              redirectTo("/jobs");
              window.scrollTo(0, 0);
            }}
          >
            <Work
              className={
                history.location.pathname === "/jobs"
                  ? "sidebarIcon selectedIcon"
                  : "sidebarIcon"
              }
            />
            <span
              className={
                history.location.pathname === "/jobs"
                  ? "sidebarListItemText selectedIcon"
                  : "sidebarIcon"
              }
            >
              Scholarships/Jobs
            </span>
          </li>
          <li
            className="sidebarListItem"
            onClick={() => {
              redirectTo("/messages");
              window.scrollTo(0, 0);
            }}
          >
            <Message
              className={
                history.location.pathname === "/messages"
                  ? "sidebarIcon selectedIcon"
                  : "sidebarIcon"
              }
            />
            <span
              className={
                history.location.pathname === "/messages"
                  ? "sidebarListItemText selectedIcon"
                  : "sidebarIcon"
              }
            >
              Messages
            </span>
          </li>
          {user && user.roles[0] === "ROLE_ADMIN" && (
            <li
              className="sidebarListItem"
              onClick={() => {
                redirectTo("/admin");
                window.scrollTo(0, 0);
              }}
            >
              <AdminPanelSettings
                className={
                  history.location.pathname === "/admin"
                    ? "sidebarIcon selectedIcon"
                    : "sidebarIcon"
                }
              />
              <span
                className={
                  history.location.pathname === "/admin"
                    ? "sidebarListItemText selectedIcon"
                    : "sidebarIcon"
                }
              >
                Admin Panel
              </span>
            </li>
          )}
          <li
            className="sidebarListItem"
            onClick={() => {
              sessionStorage.removeItem("token");
              redirectTo("/login");
              window.scrollTo(0, 0);
            }}
          >
            <Logout
              className="sidebarIcon"
              sx={{ transform: "rotate(180deg)" }}
            />
            <span className="sidebarListItemText">Log Out</span>
          </li>
        </ul>
        <Footer />
      </div>
    </div>
  );
};

export default Sidebar;
