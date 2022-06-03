import React, { useState, useEffect } from "react";
import { useHistory } from "react-router-dom";
import { MoreVert, Delete, Edit, Save } from "@mui/icons-material";
import { CSSTransition } from "react-transition-group";
import "./Announcement.css";

const Announcement = ({ type, announcement, all }) => {
  const MiniAnnouncement = (a) => {
    const [isContent, setIsContent] = useState(false);

    return (
      <>
        <div className="announcementBody">
          <div
            className="sidebarAnnouncement unselectable"
            onClick={() => {
              setIsContent(!isContent);
            }}
          >
            {a.a.title}
          </div>
        </div>
        <CSSTransition
          in={isContent}
          classNames="sidebarAnnouncementContent"
          timeout={500}
          unmountOnExit
        >
          <div className="sidebarAnnouncementContent">{a.a.content}</div>
        </CSSTransition>
      </>
    );
  };

  const [isSidebar, setIsSidebar] = useState(type === "sidebar" ? true : false);
  const [isFeed, setIsFeed] = useState(type === "feed" ? true : false);
  const [isEdit, setIsEdit] = useState(false);
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [showError, setShowError] = useState(false);
  const [formattedTime, setFormattedTime] = useState("");

  const history = useHistory();

  const user = JSON.parse(sessionStorage.getItem("user"));
  const token = sessionStorage.getItem("token");

  const timestamp = new Date(announcement?.timestamp);

  const timeNowInSeconds = new Date().getTime() / 1000;
  const timestampInSeconds = new Date(announcement?.timestamp).getTime() / 1000;
  const timeDiffInSeconds = timeNowInSeconds - timestampInSeconds;

  const formatTime = (sec) => {
    // 1 hour = 3600 seconds || 1 day = 86400 seconds || 14 days = 1209600 seconds
    if (sec < 60) {
      // Under a minute -> Say "just now"
      setFormattedTime(`just now`);
    } else if (sec < 3600) {
      // Under a hour -> Say "x mins ago"
      setFormattedTime(`${Math.floor(sec / 60)} minutes ago`);
    } else if (sec < 86400) {
      // Under 24 hours -> Say "x hours ago"
      setFormattedTime(`${Math.floor(sec / 3600)} hours ago`);
    } else if (sec < 1209600) {
      // Under 14 days -> Say "x days ago"
      setFormattedTime(`${Math.floor(sec / 86400)} days ago`);
    } else {
      // After 14 days -> Say Day/Month/Year
      setFormattedTime(`${timestamp.toLocaleString()}`);
    }
  };

  useEffect(() => {
    formatTime(timeDiffInSeconds);
  }, []);

  const redirectTo = (path) => {
    history.push(path);
    window.scrollTo(0, 0);
  };

  const resetErrors = () => {
    setShowError(false);
  };

  const editHandler = () => {
    resetErrors();
    setIsEdit(!isEdit);
    setTitle(announcement?.title);
    setContent(announcement?.content);
  };

  const saveHandler = () => {
    setIsEdit(!isEdit);

    if (title === "" && content === "") {
      setShowError(true);
      setTimeout(() => {
        setShowError(false);
      }, 2000);
      return;
    }

    const opts = {
      method: "PUT",
      headers: {
        "Content-type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({
        title: title,
        content: content,
      }),
    };
    fetch(
      `http://localhost:8080/api/announcement/edit/${announcement.id}`,
      opts
    )
      .then((res) => {
        return res;
      })
      .then(() => {
        window.location.reload();
      })
      .catch((error) => {
        console.error("There's an error", error);
      });
  };

  const deleteHandler = () => {
    const opts = {
      method: "DELETE",
      headers: {
        "Content-type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    };
    fetch(`http://localhost:8080/api/announcement/${announcement.id}`, opts)
      .then((res) => {
        return res;
      })
      .then((data) => {
        window.location.reload();
        return data;
      })
      .catch((error) => {
        console.error("There's an error", error);
      });
  };

  return (
    <>
      {isSidebar && (
        <div>
          <div className="announcementContainer">
            <img
              src="/assets/megaphone.png"
              alt=""
              className="announcementImg"
            />
            <span className="announcementText">
              <b>ANNOUNCEMENTS</b>
            </span>
          </div>
          {all.map((a) => (
            <MiniAnnouncement key={a.id} a={a} />
          ))}
        </div>
      )}
      {isFeed && (
        <>
          <div className="announcement">
            <div className="announcementWrapper">
              <div className="announcementTop">
                <div className="announcementTopLeft">
                  <img
                    className="announcementProfileImg"
                    src={announcement.author.profile.profilePicture}
                    alt=""
                  />
                  <span
                    className="announcementUsername"
                    onClick={() =>
                      redirectTo(`/profile/${announcement.author.username}`)
                    }
                  >
                    {announcement.author.username}
                  </span>
                  <span className="announcementDate">{formattedTime}</span>
                </div>
                {(announcement.author.username === user.sub ||
                  user.roles[0] === "ROLE_ADMIN") && (
                  <div className="announcementTopRight">
                    {isEdit ? (
                      <Save
                        className="announcementOptionsBtn"
                        onClick={saveHandler}
                      />
                    ) : (
                      <React.Fragment>
                        <Delete
                          className="announcementOptionsBtn"
                          style={{ cursor: "pointer" }}
                          onClick={deleteHandler}
                        />
                        <Edit
                          className="announcementOptionsBtn"
                          style={{ marginLeft: "10px" }}
                          onClick={editHandler}
                        />
                      </React.Fragment>
                    )}
                  </div>
                )}
              </div>
              <div className="announcementCenter">
                {showError && (
                  <h4 className="alert-text invalidEdit">
                    Invalid Title and Description!
                  </h4>
                )}
                {!isEdit ? (
                  <h2 className="announcementTitle">{announcement?.title}</h2>
                ) : (
                  <input
                    className="editInput announcementTitle"
                    type="text"
                    defaultValue={announcement?.title}
                    onChange={(event) => setTitle(event.target.value)}
                    placeholder="Title"
                  />
                )}

                {!isEdit ? (
                  <span className="announcementText">
                    {announcement?.content}
                  </span>
                ) : (
                  <input
                    className="editInput announcementText"
                    type="text"
                    defaultValue={announcement?.content}
                    onChange={(event) => setContent(event.target.value)}
                    placeholder="Content"
                  />
                )}
              </div>
              <div className="announcementBottom"></div>
            </div>
          </div>
        </>
      )}
    </>
  );
};

export default Announcement;
