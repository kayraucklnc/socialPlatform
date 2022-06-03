import React, { useEffect, useState } from "react";
import { useHistory } from "react-router-dom";
import { CSSTransition } from "react-transition-group";
import { Delete, Edit, Save, ModeComment, SendRounded } from "@mui/icons-material";

import Comment from "../Comment/Comment";
import Heart from "../Heart/Heart";

import "./Post.css";

const Post = ({ post }) => {
  const [like, setLike] = useState(post.likedByUsers.length);
  const [isLiked, setIsLiked] = useState(false);
  const [isEdit, setIsEdit] = useState(false);
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [photoLink, setPhotoLink] = useState("");
  const [showError, setShowError] = useState(false);
  const [formattedTime, setFormattedTime] = useState("");
  const [topComment, setTopComment] = useState(false);
  const [commentCount, setCommentCount] = useState(0);
  const [allComments, setAllComment] = useState(false);
  const [isComment, setIsComment] = useState(false);
  const [commentContent, setCommentContent] = useState("");
  const [commentCheck, setCommentCheck] = useState(false);
  const [isLoadMore, setIsLoadMore] = useState(false);
  const history = useHistory();

  const user = JSON.parse(sessionStorage.getItem("user"));
  const token = sessionStorage.getItem("token");

  const timestamp = new Date(post.timestamp);

  const timeNowInSeconds = new Date().getTime() / 1000;
  const timestampInSeconds = new Date(post.timestamp).getTime() / 1000;
  const timeDiffInSeconds = timeNowInSeconds - timestampInSeconds;

  const commentSendHandler = () => {

    fetch("http://localhost:8080/api/comment", {
      method: "POST",
      headers: {
        "Content-type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({
        "postID": post.id,
        "content": commentContent,
        "photo_link": null      
      })
    })
      .then((res) => {
        return res;
      })
      .then((data) => {
        setCommentCheck(!commentCheck);
        document.getElementById(`commentInputField${post.id}`).value = "";
        setCommentContent("");
        setIsComment(!isComment);
        return data;
      })
      .catch((error) => {
        console.error("There's an error", error);
      });

  }

  const commentHandler = () => {
    setIsComment(!isComment);
  }

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

  const resetErrors = () => {
    setShowError(false);
  };

  const likeHandler = () => {
    setLike(isLiked ? like - 1 : like + 1);
    setIsLiked(!isLiked);
    const opts = {
      method: "POST",
      headers: {
        "Content-type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({
        liked: !isLiked,
      }),
    };
    fetch(`http://localhost:8080/api/post/like/${post.id}`, opts)
      .then((res) => {
        return res;
      })
      .catch((error) => {
        console.error("There's an error", error);
      });
  };

  const editHandler = () => {
    resetErrors();
    setIsEdit(!isEdit);
    setTitle(post?.title);
    setContent(post?.content);
    setPhotoLink(post?.photoLink);
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
        photoLink: photoLink,
        content: content,
      }),
    };
    fetch(`http://localhost:8080/api/post/edit/${post.id}`, opts)
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
    fetch(`http://localhost:8080/api/post/${post.id}`, opts)
      .then((res) => {
        return res;
      })
      .then((data) => {
        window.location.reload();
        return data;
      });
  };

  const loadMoreHandler = (e) => {
    if (e) {
      fetch("http://localhost:8080/api/comment/ofPost/" + post.id, {
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
          setIsLoadMore(true);
          setAllComment(data);
          setTopComment(data[0]); // Need to fetch TOP COMMENT API later on
          return data;
        })
        .catch((error) => {
          console.error("There's an error", error);
        });
    } else {
      setIsLoadMore(false);
    }
  };

  const linkFormatter = (link) => {
    if (link.charAt(0) === ".") {
      link = link.substring(1);
    }

    return link;
  };

  const redirectTo = (path) => {
    history.push(path);
    window.scrollTo(0, 0);
  };

  useEffect(() => {
    let likedByUsers = post.likedByUsers;
    likedByUsers.forEach((u) => {
      if (u.username === user.sub) {
        setIsLiked(true);
      }
    });
    let isMounted = true;
    // #TODO this needs to be top comment in the future
    fetch("http://localhost:8080/api/comment/ofPost/" + post.id, {
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
        if (isMounted){
          setTopComment(data[0]);
          setCommentCount(data.length);
        }
        return data;
      })
      .catch((error) => {
        console.error("There's an error", error);
      });
      return () => { isMounted = false };
  }, []);

  useEffect(() => {
    let isMounted = true;
    fetch("http://localhost:8080/api/comment/ofPost/" + post.id, {
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
        if (isMounted){
          setAllComment(data);
          setTopComment(data[0]); // Need to fetch TOP COMMENT API later on
          setCommentCount(data.length);
        }
        return data;
      })
      .catch((error) => {
        console.error("There's an error", error);
      });
      return () => { isMounted = false };
  },[commentCheck])


  return (
    <div className="post">
      <div className="postWrapper">
        <div className="postTop">
          <div className="postTopLeft">
            <img
              className="postProfileImg"
              src={linkFormatter(post.appUser.profile.profilePicture)}
              alt=""
            />
            <span
              className="postUsername"
              onClick={() => redirectTo(`/profile/${post.author}`)}
            >
              {post.author}
            </span>
            <span className="postDate">{formattedTime}</span>
          </div>
          {(post.author === user.sub || user.roles[0] === "ROLE_ADMIN") && (
            <div className="postTopRight">
              {isEdit ? (
                <Save className="postOptionsBtn" onClick={saveHandler} />
              ) : (
                <React.Fragment>
                  <Delete
                    className="postOptionsBtn"
                    style={{ cursor: "pointer" }}
                    onClick={deleteHandler}
                  />
                  <Edit
                    className="postOptionsBtn"
                    style={{ marginLeft: "10px" }}
                    onClick={editHandler}
                  />
                </React.Fragment>
              )}
            </div>
          )}
        </div>
        <div className="postCenter">
          {showError && (
            <h4 className="alert-text invalidEdit">
              Invalid Title and Description!
            </h4>
          )}
          {!isEdit ? (
            <h2 className="postTitle">{post?.title}</h2>
          ) : (
            <input
              className="editInput postTitle"
              type="text"
              defaultValue={post?.title}
              onChange={(event) => setTitle(event.target.value)}
              placeholder="Title"
            />
          )}

          {!isEdit ? (
            <span className="postText">{post?.content}</span>
          ) : (
            <input
              className="editInput postText"
              type="text"
              defaultValue={post?.content}
              onChange={(event) => setContent(event.target.value)}
              placeholder="Content"
            />
          )}
          {!isEdit ? (
            <img className="postImg" src={post?.photoLink} alt="" />
          ) : (
            <input
              className="editInput postText"
              type="text"
              defaultValue={post?.photoLink}
              onChange={(event) => setPhotoLink(event.target.value)}
              placeholder="Image Link"
            />
          )}
        </div>
        <hr></hr>

        <div className="allComments">
          {!isLoadMore && topComment && <Comment comment={topComment} setCommentCheck={setCommentCheck} commentCheck={commentCheck} />}

          {!isLoadMore && (commentCount > 1) && (
            <div onClick={() => loadMoreHandler(true)} className="loadMoreText">
              Load more
            </div>
          )}

          {isLoadMore && topComment && (
            <div className="allCommentsTest">
              {allComments.map((c) => (
                <Comment key={c.id} comment={c} setCommentCheck={setCommentCheck} commentCheck={commentCheck} />
              ))}
            </div>
          )}

          {isLoadMore && topComment && (
            <div
              onClick={() => loadMoreHandler(false)}
              className="loadMoreText"
            >
              Load less
            </div>
          )}
        </div>

        <div>
          <div className="postBottom">
            <div className="postBottomLeft">
              {isLiked ? (
                <Heart type="hearted" onClick={likeHandler} />
              ) : (
                <Heart type="heart" onClick={likeHandler} />
              )}
              {like > 0 ? (
                like === 1 ? (
                  <span className="postLikeCounter">
                    A person liked this post.
                  </span>
                ) : (
                  <span className="postLikeCounter">
                    {like} people liked this post.
                  </span>
                )
              ) : (
                <span className="postLikeCounter">Wow such empty!</span>
              )}
            </div>
            <div className="postBottomRight" onClick={commentHandler} >
              <span className="postCommentText unselectable"> <ModeComment style={{marginRight:4}} /> Comment </span>
            </div>
          </div>
          <CSSTransition
            in={isComment}
            classNames="createComment"
            timeout={500}
            unmountOnExit> 
            <div className="createComment">
              <br></br>
              <input
                id={`commentInputField${post.id}`}
                className="commentInput"
                type="text"
                placeholder={"Comment here!"}
                autoComplete="off"
                onChange={(event) => setCommentContent(event.target.value)}
              />
              <SendRounded className="commentSendIcon" style={{fontSize:30}} onClick={commentSendHandler} />
            </div>
          </CSSTransition>
        </div>
      </div>
    </div>
  );
};

export default Post;
