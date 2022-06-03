import React, { useState, useEffect } from "react";
import { Save } from "@mui/icons-material";
import "./Comment.css";
import { useHistory } from "react-router-dom";

const Comment = ({ comment, setCommentCheck, commentCheck }) => {
  const [isLiked, setIsLiked] = useState();
  const [likeCount, setLikeCount] = useState(comment.likedByUsers.length);
  const [likeCheck, setLikeCheck] = useState(false);
  const [hoverCheck, setHoverCheck] = useState(false);
  const [isEdit, setIsEdit] = useState(false);
  const [content, setContent] = useState(comment.content);
  const [newContent, setNewContent] = useState(comment.content);
  let timestamp = new Date(comment.timestamp);
  const history = useHistory();
  const user = JSON.parse(sessionStorage.getItem("user"));
  const token = sessionStorage.getItem("token");

  const commentEditHandler = () => {
    setIsEdit(!isEdit);
    setNewContent(content);
  };

  const commentSaveHandler = () => {
    fetch("http://localhost:8080/api/comment/" + comment.id, {
      method: "PUT",
      headers: {
        "Content-type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({
        content: newContent,
        photoLink: "none",
      }),
    })
      .then((res) => {
        return res;
      })
      .then((data) => {
        setContent(newContent);
        setIsEdit(!isEdit);
        setCommentCheck(!commentCheck);
        return data;
      })
      .catch((error) => {
        console.error("There's an error", error);
      });
  };

  const commentDeleteHandler = () => {
    fetch("http://localhost:8080/api/comment/" + comment.id, {
      method: "DELETE",
      headers: {
        "Content-type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    })
      .then((res) => {
        return res;
      })
      .then((data) => {
        setCommentCheck(!commentCheck);
        return data;
      })
      .catch((error) => {
        console.error("There's an error", error);
      });
  };

  const formatTime = (t) => {
    const timeNowInSeconds = new Date().getTime() / 1000;
    const timestampInSeconds = new Date(comment.timestamp).getTime() / 1000;
    const timeDiffInSeconds = timeNowInSeconds - timestampInSeconds;
    const sec = timeDiffInSeconds;

    // 1 hour = 3600 seconds || 1 day = 86400 seconds || 14 days = 1209600 seconds
    if (sec < 60) {
      // Under a minute -> Say "just now"
      return `just now`;
    } else if (sec < 3600) {
      // Under a hour -> Say "x mins ago"
      return `${Math.floor(sec / 60)} minutes ago`;
    } else if (sec < 86400) {
      // Under 24 hours -> Say "x hours ago"
      return `${Math.floor(sec / 3600)} hours ago`;
    } else if (sec < 1209600) {
      // Under 14 days -> Say "x days ago"
      return `${Math.floor(sec / 86400)} days ago`;
    } else {
      // After 14 days -> Say Day/Month/Year
      return `${t.toLocaleString()}`;
    }
  };

  const linkFormatter = (link) => {
    if (link.charAt(0) === ".") {
      link = link.substring(1);
    }

    return link;
  };

  const opts = {
    method: "GET",
    headers: {
      "Content-type": "application/json",
      Authorization: `Bearer ${token}`,
    },
  };

  const commentLikeHandler = () => {
    fetch("http://localhost:8080/api/comment/like/" + comment.id, {
      method: "POST",
      headers: {
        "Content-type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    })
      .then((res) => {
        return res.json();
      })
      .then((data) => {
        setIsLiked(data.liked);
        setLikeCheck(!likeCheck);
        return data;
      })
      .catch((error) => {
        console.error("There's an error", error);
      });
  };

  useEffect(() => {
    fetch("http://localhost:8080/api/comment/" + comment.id, {
      method: "GET",
      headers: {
        "Content-type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    })
      .then((res) => {
        return res.json();
      })
      .then((data) => {
        setLikeCount(data.likedByUsers.length);
        return data;
      })
      .catch((error) => {
        console.error("There's an error", error);
      });
  }, [likeCheck]);

  useEffect(() => {
    fetch("http://localhost:8080/api/comment/liked/" + comment.id, {
      method: "GET",
      headers: {
        "Content-type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    })
      .then((res) => {
        return res.json();
      })
      .then((data) => {
        setIsLiked(data.liked);
        return data;
      })
      .catch((error) => {
        console.error("There's an error", error);
      });
  }, [comment]);

  useEffect(() => {
    let isMounted = true;
    if(isMounted){
      setLikeCount(comment.likedByUsers.length);
      setContent(comment.content);
      timestamp = comment.timestamp;
    }
    return () => { isMounted = false };
  },[comment])

  const redirectTo = (path) => {
    history.push(path);
    window.scrollTo(0, 0);
  };
  return (
    <div id={`comment${comment.id}`}>
      {comment && (
        <div className="commentHolder">
          <img
            className="commentProfilePic"
            src={linkFormatter(comment.author.profile.profilePicture)}
            alt=""
          />
          <div>
            <div className="commmentBoxTop">
              <div
                className="commentBox"
                onMouseEnter={() => setHoverCheck(true)}
                onMouseLeave={() => setHoverCheck(false)}
              >
                <span
                  className="commentAuthor"
                  onClick={() =>
                    redirectTo(`/profile/${comment.author.username}`)
                  }
                >
                  {comment.author.username}
                </span>
                {!isEdit ? (
                  <div className="paddingText commentText">{content}</div>
                ) : (
                  <div className="commentEditField">
                    <input
                      id={`commentEditInputField${comment.id}`}
                      className="commentEditInput"
                      value={newContent}
                      type="text"
                      onChange={(event) => setNewContent(event.target.value)}
                    />
                    <Save
                      className="commentSaveBtn"
                      style={{ fontSize: 30 }}
                      onClick={commentSaveHandler}
                    />
                  </div>
                )}
              </div>
              {hoverCheck && (
                <div className="commentTimestamp">
                  {" "}
                  {formatTime(timestamp)}{" "}
                </div>
              )}
            </div>
            <div className="comentBottomOptions">
              <a
                onClick={commentLikeHandler}
                className={
                  "unselectable " +
                  (isLiked ? "likeBtnActive" : "likeBtnPassive")
                }
              >
                Like ({likeCount})
              </a>
              {(comment.author.username === user.sub ||
                user.roles[0] === "ROLE_ADMIN") && (
                <div>
                  <a
                    onClick={commentEditHandler}
                    className={"unselectable likeBtnPassive"}
                  >
                    Edit
                  </a>

                  <a
                    onClick={commentDeleteHandler}
                    className={"unselectable likeBtnPassive"}
                  >
                    Delete
                  </a>
                </div>
              )}
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default Comment;
